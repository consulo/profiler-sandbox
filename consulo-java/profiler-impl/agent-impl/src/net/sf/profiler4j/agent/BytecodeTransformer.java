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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * Transforms class bytecodes to inject calls to the profiling method of
 * {@link ThreadProfiler}.
 *
 * @author Antonio S. R. Gomes
 */
public class BytecodeTransformer {

	public static final AtomicLong totalTransformTime = new AtomicLong(0);

	public static final List<String> list = new CopyOnWriteArrayList<String>();

	private static Pattern getterRegex = Pattern.compile("^get[A-Z].*$");
	private static Pattern getterBoolRegex = Pattern.compile("^is[A-Z].*$");
	private static Pattern setterRegex = Pattern.compile("^set[A-Z].*$");

	volatile static boolean enabled = false;

	public static byte[] transform(String className,
			ClassLoader loader,
			byte[] classBytes,
			Config config) throws IOException {
		if (!enabled) {
			return null;
		}
		if(className.startsWith("consulo.profiler.internal.") ||
				className.startsWith("net.sf.profiler4j.") ||
				className.startsWith("consulo.profiler"))
		{
			return classBytes;
		}
		list.add(className);
		return transformMethodAsNeeded(classBytes, config);
	}

	private static byte[] transformMethodAsNeeded(byte[] classBytes, Config config) {
		ClassReader cr = new ClassReader(classBytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		P4JEnterExitClassAdapter ca = new P4JEnterExitClassAdapter(cw, config);
		cr.accept(ca, ClassReader.EXPAND_FRAMES);
		if (ca.changedMethods > 0) {
			return cw.toByteArray();
		}
		return null;
	}

	/**
	 * Custom class adapter that modifies methods by an around advice.
	 * @author Antonio S. R. Gomes
	 */
	private static class P4JEnterExitClassAdapter extends ClassVisitor {
		Type classType;
		int changedMethods;
		Config config;
		public P4JEnterExitClassAdapter(ClassVisitor cv, Config config) {
			super(Opcodes.ASM5, cv);
			this.config = config;
		}
		@Override
		public void visit(int version,
				int access,
				String name,
				String signature,
				String superName,
				String[] interfaces) {
			super.visit(version, access, name, signature, superName, interfaces);
			classType = Type.getType("L" + name + ";");
		}
		@Override
		public MethodVisitor visitMethod(int access,
				String name,
				String desc,
				String signature,
				String[] exceptions) {
			String[] names = makeMethodName(classType, name, desc);
			String globalName = names[0];
			String localName = names[1];
			if (canProfileMethod(classType,
					access,
					name,
					desc,
					signature,
					exceptions,
					config,
					globalName,
					localName)) {
				if (changedMethods == 0) {
					Log.print(1, "Instrumenting class " + classType.getClassName());
				}
				int gmid = ThreadProfiler.newMethod(globalName);
				Log.print(3, "    method " + localName);
				MethodVisitor mv = cv.visitMethod(access,
						name,
						desc,
						signature,
						exceptions);
				P4JEnterExitAdviceAdapter ma = new P4JEnterExitAdviceAdapter(mv, access, name, desc, gmid);
				changedMethods++;
				return ma;
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	/**
	 * Custom around advice that calls the profilings from {@link ThreadProfiler}.
	 * @author Antonio S. R. Gomes
	 */
	private static class P4JEnterExitAdviceAdapter extends AdviceAdapter
	{
		private int gmid;
		public P4JEnterExitAdviceAdapter(MethodVisitor mv,
				int access,
				String name,
				String desc,
				int gmid) {
			super(Opcodes.ASM5, mv, access, name, desc);
			this.gmid = gmid;
		}
		@Override
		protected void onMethodEnter() {
			mv.visitLdcInsn(new Integer(gmid));
			mv.visitMethodInsn(INVOKESTATIC,
					"net/sf/profiler4j/agent/ThreadProfiler",
					"enterMethod",
					"(I)V");
		}
		@Override
		protected void onMethodExit(int opcode) {
			mv.visitLdcInsn(new Integer(gmid));
			mv.visitMethodInsn(INVOKESTATIC,
					"net/sf/profiler4j/agent/ThreadProfiler",
					"exitMethod",
					"(I)V");
		}
	}

	private static boolean canProfileMethod(Type classType,
			int access,
			String name,
			String desc,
			String signature,
			String[] exceptions,
			Config config,
			String globalName,
			String localName) {
		if (((access & Opcodes.ACC_ABSTRACT) | (access & Opcodes.ACC_NATIVE) | (access & Opcodes.ACC_SYNTHETIC)) != 0) {
			return false;
		}
		// if (name.equals("<init>") || name.equals("<clinit>")) {
		// return false;
		// }
		return true;
		/*List<Rule> rules = config.getRules();
		if (rules == null) {
			return false;
		}
		Rule selectedRule = null;
		for (Rule rule : rules) {
			if (rule.matches(globalName)) {
				selectedRule = rule;
				break;
			}
		}
		if (selectedRule == null) {
			return false;
		}
		if (selectedRule.getAction() == Rule.Action.ACCEPT) {
			if (selectedRule.isBooleanOptionSet(Rule.Option.BEANPROPS, config)
					&& isGetterSetter(access, name, desc)) {
				return false;
			}
			boolean packageAccess = (access & 0x7) == 0;
			boolean protectedAccess = (access & Opcodes.ACC_PROTECTED) != 0;
			boolean publicAccess = (access & Opcodes.ACC_PUBLIC) != 0;
			String accessStr = selectedRule.getOption(Rule.Option.ACCESS, config);
			boolean acceptVisiblity = "private".equals(accessStr)
					|| ("package".equals(accessStr) && (packageAccess || protectedAccess || publicAccess))
					|| ("protected".equals(access) && (protectedAccess || publicAccess) || ("public"
					.equals(access) && publicAccess));
			return acceptVisiblity;
		}

		return false;   */
	}

	private static boolean isGetterSetter(int flag, String name, String methodDescriptor) {
		if ((Opcodes.ACC_PUBLIC | flag) == 0
				|| ((Opcodes.ACC_STATIC | flag) | (Opcodes.ACC_NATIVE | flag)
				| (Opcodes.ACC_ABSTRACT | flag) | (Opcodes.ACC_SYNCHRONIZED | flag)) != 0) {
			return false;
		}
		Type[] pTypes = Type.getArgumentTypes(methodDescriptor);
		Type rType = Type.getReturnType(methodDescriptor);
		if (getterRegex.matcher(name).matches()
				|| getterBoolRegex.matcher(name).matches()) {
			return pTypes.length == 0 && !rType.equals(Type.VOID_TYPE);
		}
		if (setterRegex.matcher(name).matches()) {
			return pTypes.length == 1 && !rType.equals(Type.VOID_TYPE);
		}
		return false;
	}

	private static String[] makeMethodName(Type classType,
			String methodName,
			String methodDescriptor) {
		StringBuilder sbName = new StringBuilder();
		StringBuilder sbMethod = new StringBuilder();
		sbName.append(classType.getClassName());
		sbName.append(".");
		sbMethod.append(methodName);
		sbMethod.append("(");
		boolean comma = false;
		for (Type pt : Type.getArgumentTypes(methodDescriptor)) {
			if (comma) {
				sbMethod.append(",");
			}
			comma = true;
			sbMethod.append(pt.getClassName());
		}
		sbMethod.append(")");
		sbName.append(sbMethod);
		return new String[]{sbName.toString(), sbMethod.toString()};
	}
}
