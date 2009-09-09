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
public class TicketParameterProxy extends ProxyBase implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -9001392344296934458L;
    private TicketParameters ticketParameters;

    /** Creates a new instance of TicketParameters */
    public TicketParameterProxy() {
    }

    public String getStatus() {
	if (ticketParameters.getStatusId() != null)
	    return ticketParameters.getStatusId().toString();
	else
	    return "";

    }

    public void setStatus(String id) {
	try {
	    ticketParameters.setStatusId(new Integer(id.trim()));
	} catch (Exception ex) {
	    if (ticketParameters.getStatusId() != null)
		ticketParameters.setStatusId(null);
	}
    }

    public String getPriority() {
	if (ticketParameters.getPriorityId() != null)
	    return ticketParameters.getPriorityId().toString();
	else
	    return "";
    }

    public void setPriority(String id) {
	try {
	    ticketParameters.setPriorityId(new Integer(id.trim()));
	} catch (Exception ex) {
	    if (ticketParameters.getPriorityId() != null)
		ticketParameters.setPriorityId(null);
	}
    }

    public String getSeverity() {
	if (ticketParameters.getSeverityId() != null)
	    return ticketParameters.getSeverityId().toString();
	else
	    return "";
    }

    public void setSeverity(String id) {
	try {
	    ticketParameters.setSeverityId(new Integer(id.trim()));
	} catch (Exception ex) {
	    if (ticketParameters.getSeverityId() != null)
		ticketParameters.setSeverityId(null);
	}
    }

    public String getEventType() {
	if (ticketParameters.getEventTypeId() != null)
	    return ticketParameters.getEventTypeId().toString();
	else
	    return "";
    }

    public void setEventType(String id) {
	try {
	    ticketParameters.setEventTypeId(new Integer(id.trim()));
	} catch (Exception ex) {
	    if (ticketParameters.getEventTypeId() != null)
		ticketParameters.setEventTypeId(null);
	}
    }

    public TicketParameters getTicketParameters() {
	return ticketParameters;
    }

    public void setTicketParameters(TicketParameters ticketParameters) {
	this.ticketParameters = ticketParameters;
    }

}
