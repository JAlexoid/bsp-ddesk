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

import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.textmarks.TextSuggest;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ParserTester {

    /** Creates a new instance of ParserTester */
    public ParserTester() {
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
	try {
	    SessionHolder.currentSession();

	    TextSuggest ts = new TextSuggest("alex");

	    String input = ":new When looking at account, \"add acontact\" link should also be where the Contacts are listed.\n"
		    + ":new What was checkmark of Organization for Individual?\n"
		    + ":new When adding a contact already knowing organiztion, nothing is copying with it.\n"
		    + "It should automatically copy: ??? what ???\n"
		    + ":new Client . Not \"Name\", but \"First Name\"\n"
		    + ":new In contacts, no need for \"CEO\"\n"
		    + ":new When I ask for notification if task is completed, it sends\n"
		    + "notification to the one who needs to do the task.\n"
		    + "That is not necessary.\n"
		    + "However, I do not get the email that the task has been completed.\n"
		    + "Totally mixed up!\n"
		    + ":new What is \"reminder\" field in Events?\n"
		    + ":new Reminder did not work for Events.\n"
		    + "\n"
		    + ":new error when editing account industry\n"
		    + "When in editing mode for Account, I choose to Add new industry, after that function, \"Save\" and \"Apply\" buttons are disabled. I can't save it\n"
		    + "anymore!\n"
		    + "\n"
		    + ":new After editing and clicking Apply, it takes to View section\n"
		    + "and does not stay at the same place. After \"Apply\" se shoudl see the blue info bar on top that the info has been saved.\n"
		    + "\n" + "";
	    ts.extract(input);

	    SessionHolder.closeSession();
	} catch (Exception excasdasd) {
	    excasdasd.printStackTrace();
	}
    }

}
