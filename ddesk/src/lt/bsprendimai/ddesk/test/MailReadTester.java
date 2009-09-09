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
package lt.bsprendimai.ddesk.test;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Session;

import lt.bsprendimai.ddesk.mailtools.MailGetter;
import lt.bsprendimai.ddesk.servlets.MailReaderListener;
import eu.activelogic.mailparse.MailParser;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class MailReadTester {

    /** Creates a new instance of MailReadTester */
    public MailReadTester() {
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
	try {
	    Properties prp = new Properties();
	    InputStream is = MailReaderListener.class
		    .getResourceAsStream("/mail.properties");
	    prp.load(is);

	    MailGetter mg = new MailGetter();
	    MailParser mp = new MailParser(mg);
	    Session ss = Session.getInstance(prp, null);
	    mp.parse(ss);
	    System.out.println("Started mail collection");
	} catch (Exception ex) {
	    throw new RuntimeException("Could not initialize", ex);
	}
    }

}
