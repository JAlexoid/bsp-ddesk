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

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lt.bsprendimai.ddesk.mailtools.MailGetter;
import eu.activelogic.mailparse.PeriodicMailParser;

/**
 * Mail fetcher background processor thread initializer.
 *
 * @author alex
 * @version
 *
 *          Web application lifecycle listener.
 */

public class MailReaderListener implements ServletContextListener {

    static PeriodicMailParser pmp;
    static MailGetter mg = new MailGetter();

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
	    Properties prp = new Properties();
	    InputStream is = MailReaderListener.class
		    .getResourceAsStream("/mail.properties");
	    prp.load(is);

	    pmp = new PeriodicMailParser(mg, prp);
	    pmp.setTimeout(Long.parseLong(prp.getProperty("read.timeout",
		    "60000")));
	    pmp.setRunState(true);
	    System.out.println("Started mail collection");
	} catch (Exception ex) {
	    throw new RuntimeException("Could not initialize", ex);
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
	try {
	    pmp.setRunState(false);
	    pmp.getMyThread().notify();
	} catch (Exception ex) {
	}
	System.out.println("Stopped mail collection");
    }
}
