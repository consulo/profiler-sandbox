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

/**
 * Exception thrown in the case of an unrecorevable error.
 * 
 * @author Antonio S. R. Gomes
 */
class Profiler4JError extends Error {

    private static final long serialVersionUID = 1L;

    public Profiler4JError(String message) {
        super(message);
    }

    public Profiler4JError(String message, Throwable cause) {
        super(message, cause);
    }

    public Profiler4JError(Throwable cause) {
        super(cause);
    }

}
