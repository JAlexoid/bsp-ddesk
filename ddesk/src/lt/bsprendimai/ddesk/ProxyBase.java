/*
 *    Copyright 2006 Baltijos Sprendimai (http://www.bsprendimai.lt/)
 *              Authorship: Aleksandr Panzin (http://www.activelogic.eu/)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package lt.bsprendimai.ddesk;

import java.io.Serializable;

/**
 * Base class for all backing bean proxy classes<br/>
 *
 * Proxy class is used to set request parameters
 * on session scoped managed beans. No other logic
 * in subclasses is to be implemented.<br/>
 * ID setting is delegated to session scoped beans.<br/>
 *
 * <code>
 *       <managed-property>
 *           <property-name>id</property-name>
 *           <value>#{param.id}</value>
 *       </managed-property>
 * </code>
 * <br/>
 * dummyOutput is for forced intialization of bean without any functional impact.<br/>
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public abstract class ProxyBase implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5020134535594118395L;

    public String getDummyOutput() {
	return "";
    }

    public void setDummyOutput(String dummyOutput) {
    }

}
