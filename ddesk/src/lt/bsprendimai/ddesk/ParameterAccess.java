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

import java.util.Locale;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Session parameter access handler
 *
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ParameterAccess {

    private UserHandler userHandler;
    private static TreeMap<String, Locale> localeCache = new TreeMap<String, Locale>();
    private static TreeMap<Integer, String> languages = new TreeMap<Integer, String>();

    static {
	getLanguages().put(1, "LT");
	getLanguages().put(2, "EN");
	getLanguages().put(3, "RU");
    }

    /** Creates a new instance of TicketAccessProxy */
    public ParameterAccess() {
    }

    public void setLanguage(String language) {
	if (language != null && !language.equals("")) {
	    Locale lc = getLocale(language);
	    try {
		FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().put("selectedLocaleCode", language);
		FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().put("selectedLocale", lc);
	    } catch (Exception excv) {
	    }
	    // FacesContext.getCurrentInstance().getViewRoot().setLocale(lc);
	    if (getUserHandler() != null)
		getUserHandler().setUserLocale(lc);
	    // System.out.println("(1)Setting locale to "+lc);
	}
    }

    public static void sessionClose() {
	try {
	    ((HttpSession) FacesContext.getCurrentInstance()
		    .getExternalContext().getSession(true)).invalidate();
	} catch (Exception excasdasd) {
	}

    }

    public String getLanguage() {
	try {
	    return FacesContext.getCurrentInstance().getExternalContext()
		    .getSessionMap().get("selectedLocaleCode").toString();
	} catch (Exception excasdasd) {
	}
	return "";
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

    public static Locale getLocale(String language) {
	if (localeCache.containsKey(language))
	    return localeCache.get(language);
	else {
	    Locale lc = new Locale(language);
	    localeCache.put(language, lc);
	    return lc;
	}
    }

    public static TreeMap<Integer, String> getLanguages() {
	return languages;
    }
}
