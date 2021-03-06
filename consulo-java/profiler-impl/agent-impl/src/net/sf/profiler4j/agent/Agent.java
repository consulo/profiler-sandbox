/*
 * Copyright 2006 Antonio S. R. Gomes
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package net.sf.profiler4j.agent;

import static net.sf.profiler4j.agent.AgentConstants.VERSION;
import static net.sf.profiler4j.agent.Log.print;
import static net.sf.profiler4j.agent.ThreadProfiler.globalLock;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import consulo.profiler.ServerThread;

/**
 * Class responsible for the intialization of the agent.
 *
 * @author Antonio S. R. Gomes
 */
public class Agent
{

	static Transformer t;
	static Instrumentation inst;

	static Config config;
	static ServerThread serverThread;

	public static RuntimeMXBean rtbean = ManagementFactory.getRuntimeMXBean();
	public static List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
	public static MemoryMXBean membean = ManagementFactory.getMemoryMXBean();
	public static ThreadMXBean threadbean = ManagementFactory.getThreadMXBean();

	volatile static boolean beingShutdown;

	/**
	 * Set with the name of all currently transformed classes. Notice that his set should
	 * be used only for informative purposes as it may be imprecise.
	 * <p/>
	 * It is interesting to see that the effective number of modified classes may be
	 * greater than <tt>modifiedClasses.size()</tt>. This happens when more than one
	 * class loader loads a class with same name.
	 */
	static final Set<String> modifiedClassNames = new HashSet<String>();
	static int modifiedClassCount;

	public static void agentmain(String args, Instrumentation inst)
	{
		premain(args, inst);
	}

	/**
	 * Premain method called before the target application is initialized.
	 *
	 * @param args command line argument passed via <code>-javaagent</code>
	 * @param inst instance of the instrumentation service
	 */
	public static void premain(String args, Instrumentation inst)
	{

		print(0, "+---------------------------------------------------+");
		print(0, "| Profiler4j Agent " + String.format("%-33s", VERSION) + "|");
		print(0, "| Copyright 2006 Antonio S. R. Gomes                |");
		print(0, "| See LICENSE-2.0.txt details                       |");
		print(0, "+---------------------------------------------------+");

		//doPreInitialization(inst);

		try
		{
			config = new Config(args);
			if(config.isEnabled())
			{

				Log.setVerbosity(config.getVerbosity());

				serverThread = new ServerThread(config);
				serverThread.start();

				t = new Transformer(config);

				BytecodeTransformer.enabled = true;
				/*inst.addTransformer(t);
				if(inst.isRetransformClassesSupported())
				{
					Class[] allLoadedClasses = inst.getAllLoadedClasses();

					for(Class allLoadedClass : allLoadedClasses)
					{
						try
						{
							if(inst.isModifiableClass(allLoadedClass))
							{
								inst.retransformClasses(allLoadedClass);
							}
						}
						catch(InternalError e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					print(0, "isRetransformClassesSupported: false");
				}  */

				Agent.inst = inst;

				Runtime.getRuntime().addShutdownHook(new Thread()
				{
					@Override
					public void run()
					{
						beingShutdown = true;
						BytecodeTransformer.enabled = false;
						if(config.isSaveSnapshotOnExit())
						{
							SnapshotUtil.saveSnapshot();
						}
						ClassUtil.releaseLock();
						print(0, "Profiler stopped");
					}
				});
			}
		}
		catch(Throwable any)
		{
			print(0, "UNEXPECTED ERROR", any);
			System.exit(1);
		}
	}

	/**
	 * Gets all currently loaded classes.
	 *
	 * @param skipNonEnhanceable If <code>true</code> only those classes which are
	 *                           enhanceable will be returned
	 * @return Array of classes
	 */
	public static Class[] getLoadedClasses(boolean skipNonEnhanceable)
	{
		if(!skipNonEnhanceable)
		{
			return inst.getAllLoadedClasses();
		}
		List<Class> list = new ArrayList<Class>();
		for(Class cls : inst.getAllLoadedClasses())
		{
			list.add(cls);
		}
		return (Class[]) list.toArray(new Class[0]);
	}

	/**
	 * Schedules all currently loaded classes to be reloaded, applying the current set of
	 * instrumentation rules.
	 * <p/>
	 * This method will try as much as possible to reload only those really needed classes
	 * (see {@link #mustRedefineClass(Class)} for details) depending on the current and
	 * new set of rules.
	 *
	 * @param optionsAsStr New default rule options given as a string
	 * @param rulesAsStr   New rule list given as a string
	 * @param callback     Callback that allows the caller to monitor the overall progress,
	 *                     which can take several minutes
	 * @return number of classes that will be reloaded
	 * @throws IOException
	 */
	public static int startNewSession(String optionsAsStr, String rulesAsStr, Transformer.Callback callback) throws IOException
	{
		synchronized(globalLock)
		{
			Class[] classes = getLoadedClasses(true);
			print(0, "*** Scheduling class reloading (max " + classes.length + ")");
			ThreadProfiler.startSessionConfig();
			config.parseRules(optionsAsStr, rulesAsStr);
			synchronized(modifiedClassNames)
			{
				modifiedClassNames.clear();
				modifiedClassCount = 0;
			}
			int n = 0;
			for(Class c : classes)
			{
				if(mustRedefineClass(c) && ClassUtil.getClassFile(c).exists())
				{
					Transformer.scheduleRedefine(c.getClassLoader(), c.getName(), callback);
					n++;
				}
			}

			ThreadProfiler.endSessionConfig();
			return n;
		}
	}

	/**
	 * Checks whether a class must be redefined.
	 * <p/>
	 * The rationale for this method is that not all classes must go through all
	 * redefinition steps (load bytes, redefine and transform). If they are neither
	 * affected by the currently active set of rules (so they are uninstrumented) or the
	 * new set of rules (they won't be instrumented) they can be simply skipped. The gains
	 * provided by this verification are directly proportional to the level of
	 * restrictions imposed by the rules (current and new ones).
	 * <p/>
	 * In simple terms, a class must reloaded only if any of the following conditions
	 * holds true:
	 * <ol>
	 * <li>The class was transformed/redefined in the last session. This means that we
	 * need to undo the bytecode instrumentation.
	 * <li>The class will be redefined in the current session. This means that the class
	 * must be instrumented anyway.
	 * </ol>
	 * <p/>
	 * This method depends on the fact that whenever the method
	 * {@link Config#parseRules(String, String)} is called the method
	 * {@link ThreadProfiler#startSessionConfig()} is called as well. Otherwise the
	 * premisse that {@link Config#getLastRules()} is valid vanishes.
	 *
	 * @param c class to check
	 * @return <code>true</code> if the class must be redefined
	 */
	static boolean mustRedefineClass(Class c)
	{
		if(config.getLastRules() != null)
		{
			for(Rule rule : config.getLastRules())
			{
				if(ruleMatchesClass(rule, c))
				{
					if(rule.getAction() == Rule.Action.ACCEPT)
					{
						return true;
					}
					break;
				}
			}
		}
		if(config.getRules() != null)
		{
			for(Rule rule : config.getRules())
			{
				if(ruleMatchesClass(rule, c))
				{
					if(rule.getAction() == Rule.Action.ACCEPT)
					{
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	// *(*)
	// com.foo.*(*)
	// [a-zA-Z\\.\\-\\*]+\\([a-zA-Z\\.\\-\\*\\[\\]\\])
	private static boolean ruleMatchesClass(Rule r, Class c)
	{
		String s = r.getPattern();
		int p1 = s.indexOf('(');
		int p2 = s.indexOf(')');
		if(p1 > 0 && p2 > p1)
		{
			s = s.substring(0, p1);
			return Utils.getRegex(s).matcher(c.getName() + ".").matches();
		}
		throw new Profiler4JError("Invalid rule pattern '" + s + "'");
	}

	public static interface ReloadCallBack
	{
		void setMaxValue(int n) throws Exception;

		void setValue(int n) throws Exception;
	}

}
