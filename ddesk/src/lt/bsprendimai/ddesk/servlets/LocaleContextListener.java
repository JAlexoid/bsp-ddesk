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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lt.bsprendimai.Configuration;

import org.apache.commons.logging.LogFactory;

/**
 * Configure all lemenets on web application startup.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 * @version
 *           Web application lifecycle listener.
 */

public class LocaleContextListener implements ServletContextListener {

    /**
     * ### Method from ServletContextListener ###
     *
     * Called when a Web application is first ready to process requests
     * (i.e. on Web server startup and when a context is added or reloaded).
     *
     * For example, here might be database connections established
     * and added to the servlet context attributes.
     */
    public void contextInitialized(ServletContextEvent evt) {

	try {
	    Configuration.setPreferences(new Properties());

	    File f = new File(evt.getServletContext().getRealPath("/")
		    + "/WEB-INF/configuration.properties").getAbsoluteFile();

	    Configuration.setPropertiesFile(f);

	    if (f.exists()) {
		InputStream ins = new FileInputStream(f);
		Configuration.getPreferences().load(ins);
		ins.close();
	    }

	    f = new File(evt.getServletContext().getRealPath("/"));
	    Configuration.setRootFile(f);

	    Configuration.setMailSession(Session.getInstance(Configuration
		    .getPreferences()));

	} catch (Exception excv) {
	    excv.printStackTrace();
	    LogFactory.getLog(Configuration.class).error(
		    "error loading configuration ", excv);
	}
    }

    /**
     * ### Method from ServletContextListener ###
     *
     * Called when a Web application is about to be shut down
     * (i.e. on Web server shutdown or when a context is removed or reloaded).
     * Request handling will be stopped before this method is called.
     *
     * For example, the database connections can be closed here.
     */
    public void contextDestroyed(ServletContextEvent evt) {
    }
}
