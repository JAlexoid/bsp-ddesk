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
public class ClientAccessProxy extends ProxyBase implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -1131707028637627016L;
    private ClientAccessor clientAccessor;

    /** Creates a new instance of ClientAccessProxy */
    public ClientAccessProxy() {
    }

    public String getCompanyId() {
	if (clientAccessor.getCompanyId() != null)
	    return clientAccessor.getCompanyId().toString();
	else
	    return "";
    }

    public void setCompanyId(String id) {
	try {
	    clientAccessor.setCompanyId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

    public String getPersonId() {
	if (clientAccessor.getPersonId() != null)
	    return clientAccessor.getPersonId().toString();
	else
	    return "";
    }

    public void setPersonId(String id) {
	try {
	    if (id != null && id.equals("-1"))
		clientAccessor.setPersonId(null);
	    clientAccessor.setPersonId(new Integer(id.trim()));
	} catch (Exception ex) {
	    // Skip any problems here
	}
    }

    public ClientAccessor getClientAccessor() {
	return clientAccessor;
    }

    public void setClientAccessor(ClientAccessor clientAccessor) {
	this.clientAccessor = clientAccessor;
    }

}
