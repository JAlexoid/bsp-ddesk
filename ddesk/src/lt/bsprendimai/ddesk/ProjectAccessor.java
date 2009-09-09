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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.model.SelectItem;

import lt.bsprendimai.ddesk.dao.Project;
import lt.bsprendimai.ddesk.dao.ProjectInfo;
import lt.bsprendimai.ddesk.dao.ProjectModule;
import lt.bsprendimai.ddesk.dao.SessionHolder;

import org.hibernate.HibernateException;

/**
 * JSF backing bean provides functionality relating to projects and project modules.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class ProjectAccessor implements Serializable {

    private Integer projectId;
    private Integer moduleId;
    private Project selected = new Project();
    private ProjectModule module = new ProjectModule();
    private ProjectInfo selectedInfo = new ProjectInfo();
    private UserHandler userHandler;

    private transient UIParameter projectIdParameter = new UIParameter();
    private transient UIParameter moduleIdParameter = new UIParameter();

    /** Creates a new instance of ProjectAccessor */
    public ProjectAccessor() {
    }

    public Integer getProjectId() {
	return projectId;
    }

    public void setProjectId(Integer projectId) {
	if (!userHandler.isOwner())
	    return;
	if (projectId != null && projectId.equals(this.projectId))
	    return;
	this.projectId = projectId;
	if (projectId == null || projectId < 0) {
	    this.selected = new Project();
	    this.selectedInfo = new ProjectInfo();
	    return;
	}

	try {
	    this.selected = (Project) SessionHolder.currentSession().getSess()
		    .createQuery(
			    " FROM " + Project.class.getName()
				    + " m WHERE m.id = ?").setInteger(0,
			    projectId).setMaxResults(1).uniqueResult();
	    this.selectedInfo = (ProjectInfo) SessionHolder.currentSession()
		    .getSess().createQuery(
			    " FROM " + ProjectInfo.class.getName()
				    + " m WHERE m.id = ?").setInteger(0,
			    projectId).setMaxResults(1).uniqueResult();
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    this.selected = null;
	    this.selectedInfo = null;
	}
    }

    public Integer getModuleId() {
	return moduleId;
    }

    public void setModuleId(Integer moduleId) {
	if (!userHandler.isOwner())
	    return;
	if (moduleId != null && moduleId.equals(this.moduleId))
	    return;
	this.moduleId = moduleId;
	if (moduleId == null || moduleId < 0) {
	    this.module = new ProjectModule();
	    return;
	}

	try {
	    this.module = (ProjectModule) SessionHolder.currentSession()
		    .getSess().createQuery(
			    " FROM " + ProjectModule.class.getName()
				    + " m WHERE m.id = ?").setInteger(0,
			    moduleId).setMaxResults(1).uniqueResult();
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    this.selected = null;
	    this.selectedInfo = null;
	}
    }

    public Project getSelected() {
	return selected;
    }

    public void setSelected(Project selected) {
	this.selected = selected;
    }

    public ProjectInfo getSelectedInfo() {
	return selectedInfo;
    }

    public void setSelectedInfo(ProjectInfo selectedInfo) {
	this.selectedInfo = selectedInfo;
    }

    public List<ProjectInfo> getProjectList() {
	if (!userHandler.isOwner())
	    return new LinkedList<ProjectInfo>();
	try {
	    return (List<ProjectInfo>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + ProjectInfo.class.getName()
				    + " t WHERE t.id <> 0 ORDER BY t.id ")
		    .list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<ProjectInfo>();
	}
    }

    public List<ProjectModule> getModuleList() {
	if (!userHandler.isOwner())
	    return new LinkedList<ProjectModule>();
	try {
	    return (List<ProjectModule>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + ProjectModule.class.getName()
				    + " t WHERE t.id <> 0 AND t.project = ? ORDER BY t.id ")
		    .setInteger(0, projectId).list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<ProjectModule>();
	}
    }

    public List<ProjectModule> moduleList(Integer project) {
	if (userHandler == null || !userHandler.isOwner() || project == null) {
	    return new LinkedList<ProjectModule>();
	}
	try {
	    return (List<ProjectModule>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + ProjectModule.class.getName()
				    + " t WHERE t.id <> 0 AND t.project = ? ORDER BY t.id ")
		    .setInteger(0, project).list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<ProjectModule>();
	}
    }

    public List<SelectItem> getProjects() {
	List<Project> la = new ArrayList<Project>(0);
	try {
	    if (!userHandler.isPartner()) {
		la = (List<Project>) SessionHolder.currentSession().getSess()
			.createQuery(
				"from " + Project.class.getName()
					+ " t WHERE t.id <> 0 ORDER BY t.id ")
			.list();
	    } else {
		la = (List<Project>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ Project.class.getName()
					+ " t WHERE t.id <> 0 AND (t.company = ? OR t.manager = ?) ORDER BY t.id ")
			.setInteger(0, userHandler.getUser().getCompany())
			.setInteger(1, userHandler.getUser().getId()).list();
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);
	for (Project c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getName());
	    ll.add(si);
	}
	return ll;

    }

    public List<SelectItem> getModules() {
	try {
	    return getProjectModules(projectId);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<SelectItem>();
	}
    }

    public static List<SelectItem> getProjectModules(Integer project)
	    throws Exception {

	if (project == null)
	    return new LinkedList<SelectItem>();

	List<ProjectModule> la = (List<ProjectModule>) SessionHolder
		.currentSession()
		.getSess()
		.createQuery(
			"from "
				+ ProjectModule.class.getName()
				+ " t WHERE t.id <> 0 AND t.project = ? ORDER BY t.id ")
		.setInteger(0, project).list();

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);
	for (ProjectModule c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getModule());
	    ll.add(si);
	}
	return ll;

    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
	s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException {

	try {
	    s.defaultReadObject();
	} catch (ClassNotFoundException ex) {
	    throw new IOException(ex.getMessage());
	}
	projectIdParameter = new UIParameter();
	moduleIdParameter = new UIParameter();
    }

    public UIParameter getProjectIdParameter() {
	return projectIdParameter;
    }

    public void setProjectIdParameter(UIParameter projectIdParameter) {
	this.projectIdParameter = projectIdParameter;
    }

    public UIParameter getModuleIdParameter() {
	return moduleIdParameter;
    }

    public void setModuleIdParameter(UIParameter moduleIdParameter) {
	this.moduleIdParameter = moduleIdParameter;
    }

    public String deleteProject() {
	if (!userHandler.isOwner())
	    return StandardResults.FAIL;
	try {
	    Project cmp = (Project) SessionHolder.currentSession().getSess()
		    .get(Project.class,
			    (Serializable) getProjectIdParameter().getValue());
	    this.getProjectIdParameter().setValue(null);
	    SessionHolder.currentSession().getSess().delete(cmp);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.STAY;
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    return StandardResults.FAIL;
	}
    }

    public String deleteModule() {
	if (!userHandler.isOwner())
	    return StandardResults.FAIL;
	try {
	    ProjectModule cmp = (ProjectModule) SessionHolder.currentSession()
		    .getSess().get(ProjectModule.class,
			    (Serializable) getModuleIdParameter().getValue());
	    this.getProjectIdParameter().setValue(null);
	    SessionHolder.currentSession().getSess().delete(cmp);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.STAY;
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    return StandardResults.FAIL;
	}
    }

    public String addModule() {
	System.out.println("This is called");
	this.module.setProject(this.projectId);
	if (!Integer.valueOf(0).equals(userHandler.getUser().getCompany())
		|| !userHandler.getUser().getCompany().equals(
			selected.getCompany())) {
	    return StandardResults.FAIL;
	}
	String ret = this.module.add();
	if (!ret.equals(StandardResults.SUCCESS)) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    return null;
	}
	this.module = new ProjectModule();
	return ret;
    }

    public ProjectModule getModule() {
	return module;
    }

    public void setModule(ProjectModule module) {
	this.module = module;
    }

}
