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

/**
 * Standard JSF results. Common results for most actions.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public interface StandardResults {

    public static final String FAIL = "fail";
    public static final String LIST = "list";
    public static final String VIEW = "view";
    public static final String SUCCESS = "success";
    public static final String CLIENTList = "clientList";
    public static final String INTRANET = "intranet";
    public static final String LOGOUT = "logout";
    public static final String TICKET = "ticket";
    public static final String TIMER = "timer";

    public static final String STAY = "stay";// SUCCES WITHOUT GLOBAL REDIRECT

    // public static final String EDITTicket = "editTicket";
    // public static final String NEWTicket = "newTicket";

}
