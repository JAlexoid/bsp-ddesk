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
package lt.bsprendimai.ddesk.servlets;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lt.bsprendimai.ddesk.UserHandler;
import lt.bsprendimai.ddesk.dao.SessionHolder;

/**
 * Manages logouts and session destruction.
 * Saves ticket filter data.
 *
 * @author alex
 * @version
 *
 *          Web application lifecycle listener.
 */

public class SessionEndListener implements HttpSessionListener {
    /**
     * ### Method from HttpSessionListener ###
     *
     * Called when a session is created.
     */
    public void sessionCreated(HttpSessionEvent evt) {
	// TODO add your code here:
    }

    /**
     * ### Method from HttpSessionListener ###
     *
     * Called when a session is destroyed(invalidated).
     */
    public void sessionDestroyed(HttpSessionEvent evt) {
	try {
	    SessionHolder.currentSession();
	    UserHandler uh = ((UserHandler) evt.getSession().getAttribute(
		    "userHandler"));
	    uh.saveFilter();
	    SessionHolder.closeSession();
	} catch (Exception ex) {
	    try {
		SessionHolder.endSession();
	    } catch (Exception excasdasd) {
	    }
	}
    }
}
