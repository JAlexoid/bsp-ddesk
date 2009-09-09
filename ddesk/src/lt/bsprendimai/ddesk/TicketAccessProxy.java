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
public class TicketAccessProxy extends ProxyBase implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -5840431763402407358L;
    private TicketAccessor ticketAccess;

    /** Creates a new instance of TicketAccessProxy */
    public TicketAccessProxy() {
    }

    public void setTicketId(String id) {
	try {
	    getTicketAccess().setId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

    public String getTicketId() {
	if (getTicketAccess().getId() != null)
	    return getTicketAccess().getId().toString();
	else
	    return "";
    }

    public void setTimerId(String id) {
	try {
	    getTicketAccess().setTimerId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

    public String getTimerId() {
	if (getTicketAccess().getTimerId() != null)
	    return getTicketAccess().getTimerId().toString();
	else
	    return "";
    }

    public TicketAccessor getTicketAccess() {
	return ticketAccess;
    }

    public void setTicketAccess(TicketAccessor ticketAccess) {
	this.ticketAccess = ticketAccess;
    }

}
