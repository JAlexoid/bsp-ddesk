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
 * See {@link ProxyBase}
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ProjectAccessorProxy extends ProxyBase implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 1187926300337026665L;
    private ProjectAccessor projects;

    /** Creates a new instance of ProjectAccessorProxy */
    public ProjectAccessorProxy() {
    }

    public ProjectAccessor getProjects() {
	return projects;
    }

    public void setProjects(ProjectAccessor projects) {
	this.projects = projects;
    }

    public String getProjectId() {
	if (projects.getProjectId() != null)
	    return projects.getProjectId().toString();
	else
	    return "";
    }

    public void setProjectId(String id) {
	try {
	    projects.setProjectId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

    public String getModuleId() {
	if (projects.getModuleId() != null)
	    return projects.getModuleId().toString();
	else
	    return "";
    }

    public void setModuleId(String id) {
	try {
	    projects.setModuleId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

}
