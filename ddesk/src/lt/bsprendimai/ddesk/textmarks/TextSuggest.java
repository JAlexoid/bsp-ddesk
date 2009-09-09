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
package lt.bsprendimai.ddesk.textmarks;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import lt.bsprendimai.ddesk.ClientAccessor;
import lt.bsprendimai.ddesk.ProjectAccessor;
import lt.bsprendimai.ddesk.TicketAccessor;
import lt.bsprendimai.ddesk.UserHandler;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class TextSuggest implements CommandContext {

    private UserHandler userHandler = new UserHandler();
    private TicketAccessor tickets = new TicketAccessor();
    private ClientAccessor clientAccessor = new ClientAccessor();
    private ProjectAccessor projects = new ProjectAccessor();
    private List<ChangeCommitListener> listeners = new LinkedList<ChangeCommitListener>();

    private String lastCycle = null;

    private static List<Object> commanders = new ArrayList<Object>();

    static {
	try {
	    Properties prp = new Properties();
	    InputStream is = TextSuggest.class
		    .getResourceAsStream("/commanders.properties");
	    prp.load(is);

	    for (Object name : prp.values()) {
		Class commander = Class.forName(name.toString());
		commanders.add(commander.newInstance());
	    }

	} catch (Exception ex) {
	    throw new RuntimeException("Could not initialize", ex);
	}
    }

    private boolean allowCycles = true;
    private boolean fillOnly = false;

    /**
     * Algorithm:
     * Traverse all commands, mapped to annotaions
     * Remove all after a terminator if exists
     * Split by lines
     * Process each line
     */

    /** Creates a new instance of TextSuggest */
    public TextSuggest() {
    }

    /** Creates a new instance of TextSuggest */
    public TextSuggest(TicketAccessor tickets, UserHandler userHandler,
	    ClientAccessor clientAccessor, ProjectAccessor projects) {
	this.tickets = tickets;
	this.userHandler = userHandler;
	this.clientAccessor = clientAccessor;
	this.projects = projects;
    }

    /** Creates a new instance of TextSuggest */
    public TextSuggest(int uid) {

	allowCycles = true;
	fillOnly = false;

	// userHandler.getUser().setLoginCode(login);
	// userHandler.setLanguage(1);

	tickets.setUserHandler(userHandler);
	clientAccessor.setUserHandler(userHandler);
	projects.setUserHandler(userHandler);
	tickets.setProjects(projects);
	tickets.setClientAccessor(clientAccessor);

	userHandler.loginNoPw(uid);

    }

    /** Creates a new instance of TextSuggest */
    public TextSuggest(String login) {

	allowCycles = true;
	fillOnly = false;

	userHandler.getUser().setLoginCode(login);
	// userHandler.setLanguage(1);

	tickets.setUserHandler(userHandler);
	clientAccessor.setUserHandler(userHandler);
	projects.setUserHandler(userHandler);
	tickets.setProjects(projects);
	tickets.setClientAccessor(clientAccessor);

	userHandler.loginNoPw();

    }

    public String extractTitle(String text) {
	return extractTitleStatic(text);
    }

    public static String extractTitleStatic(String text) {
	// if(text.indexOf("\n") > 0){
	// return text.substring(0,text.indexOf("\n"));
	// } else {
	// if(text.length() > 50){
	// return text.substring(0,50)+"...";
	// } else {
	return text;
	// }
	// }
    }

    public static String extractBody(String text) {
	// if(text.indexOf("\n") > 0){
	// return text.substring(text.indexOf("\n"));
	// } else {
	// if(text.length() > 50){
	return text;
	// } else {
	// return "";
	// }
	// }
    }

    public void extractProject() {

    }

    public void extractSeverity() {

    }

    public void extractPriority() {

    }

    public void extractType() {

    }

    public void extractParts() {

    }

    public void extractComments() {

    }

    public void extract(String body) {
	String[] texts = body.split("\\n");
	boolean cancelBlock = false;
	for (int i = 0; i < texts.length; i++) {
	    String t = texts[i];
	    if (t != null && t.startsWith(":")) {
		String cmd;
		if (t.trim().indexOf(' ') > 0) {
		    cmd = t.trim().substring(1, t.trim().indexOf(' '));
		} else {
		    cmd = t.trim().substring(1);
		}
		ObjectMethod om = getMethod(cmd.toLowerCase());
		if (om != null) {
		    if (om.ch.newCycle() || om.ch.terminator())
			cancelBlock = false;
		    try {
			if (!cancelBlock)
			    om.mt.invoke(om.ot, this, texts, i);
		    } catch (CancelOperationException ex) {
			break;
		    } catch (CancelBlockException ex) {
			cancelBlock = true;
			continue;
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    }
	}

	for (int i = 0; i < texts.length; i++) {
	    String t = texts[i];
	    if (t == null || t.startsWith(":")) {
		texts[i] = null;
	    }
	}

	ObjectMethod om = getMethod("end");
	if (om != null) {
	    try {
		om.mt.invoke(om.ot, this, texts, texts.length - 1);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}

    }

    private ObjectMethod getMethod(String command) {
	ObjectMethod ret = null;
	for (Object o : commanders) {
	    for (Method mt : o.getClass().getMethods()) {
		if (mt.isAnnotationPresent(CommandHandler.class)) {
		    CommandHandler pm = mt.getAnnotation(CommandHandler.class);
		    if (pm.command().equalsIgnoreCase(command)) {
			ret = new ObjectMethod();
			ret.mt = mt;
			ret.ot = o;
			ret.ch = pm;
			break;
		    }
		    if (pm.fallback()) {
			ret = new ObjectMethod();
			ret.mt = mt;
			ret.ot = o;
			ret.ch = pm;
		    }
		}
	    }
	}
	return ret;
    }

    public Object getHandler() {
	return tickets;
    }

    private static class ObjectMethod {
	Method mt;
	Object ot;
	CommandHandler ch;
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

    public TicketAccessor getTickets() {
	return tickets;
    }

    public void setTickets(TicketAccessor tickets) {
	this.tickets = tickets;
    }

    public ClientAccessor getClientAccessor() {
	return clientAccessor;
    }

    public void setClientAccessor(ClientAccessor clientAccessor) {
	this.clientAccessor = clientAccessor;
    }

    public ProjectAccessor getProjects() {
	return projects;
    }

    public void setProjects(ProjectAccessor projects) {
	this.projects = projects;
    }

    public static List<Object> getCommanders() {
	return commanders;
    }

    public static void setCommanders(List<Object> aCommanders) {
	commanders = aCommanders;
    }

    public boolean isAllowCycles() {
	return allowCycles;
    }

    public void setAllowCycles(boolean allowCycles) {
	this.allowCycles = allowCycles;
    }

    public boolean isFillOnly() {
	return fillOnly;
    }

    public void setFillOnly(boolean fillOnly) {
	this.fillOnly = fillOnly;
    }

    public void fireAddEvent() throws PropertyVetoException {
	for (ChangeCommitListener c : listeners) {
	    c.addEvent(this);
	}
    }

    public void fireUpdateEvent() throws PropertyVetoException {
	for (ChangeCommitListener c : listeners) {
	    c.updateEvent(this);
	}
    }

    public void addListener(ChangeCommitListener changeCommitListener) {
	listeners.add(changeCommitListener);
    }

    public void removeListener(ChangeCommitListener changeCommitListener) {
	listeners.remove(changeCommitListener);
    }

    public String getLastCycle() {
	return lastCycle;
    }

    public void setLastCycle(String lastCycle) {
	this.lastCycle = lastCycle;
    }

}
