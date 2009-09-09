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

import java.util.Locale;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import lt.bsprendimai.ddesk.ParameterAccess;

/**
 * Setts the appropriate locale on the view root of all phases in JSF.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class FacesLocaleListener implements javax.faces.event.PhaseListener {

    /**
	 *
	 */
    private static final long serialVersionUID = -4798473317339096015L;

    /** Creates a new instance of FacesLocaleListener */
    public FacesLocaleListener() {
    }

    public PhaseId getPhaseId() {
	return javax.faces.event.PhaseId.ANY_PHASE;
    }

    public void beforePhase(PhaseEvent phaseEvent) {
	Locale lc = (Locale) phaseEvent.getFacesContext().getExternalContext()
		.getSessionMap().get("selectedLocale");

	Object o = phaseEvent.getFacesContext().getExternalContext()
		.getRequestParameterMap().get("lang");
	if (o != null) {
	    String language = o.toString().trim();
	    if (!language.equals("")) {
		lc = ParameterAccess.getLocale(language);
		try {
		    phaseEvent.getFacesContext().getExternalContext()
			    .getSessionMap()
			    .put("selectedLocaleCode", language);
		    phaseEvent.getFacesContext().getExternalContext()
			    .getSessionMap().put("selectedLocale", lc);
		} catch (Exception ex) {
		}

	    }
	}

	try {
	    if (lc != null) {
		phaseEvent.getFacesContext().getViewRoot().setLocale(lc);
		// System.out.println("Setting view locale to "+lc+" "+phaseEvent.getPhaseId().toString());
	    }
	} catch (Exception ex) {
	}
    }

    public void afterPhase(PhaseEvent phaseEvent) {

    }

}
