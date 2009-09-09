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

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * JavaServer Faces Message helper class. Provides i18n support via ResourceBundles.
 * Caches resource bundles.
 *
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class UIMessenger {

    static HashMap<Locale, ResourceBundle> rbMap = new HashMap<Locale, ResourceBundle>();

    static {
	ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(
		"lt"), UIMessenger.class.getClassLoader());
	rbMap.put(rb.getLocale(), rb);
	rb = ResourceBundle.getBundle("messages", Locale.getDefault(),
		UIMessenger.class.getClassLoader());
	rbMap.put(rb.getLocale(), rb);
    }

    /**
     * Creates a new instance of Messenger
     */
    public UIMessenger() {
    }

    public static String getMessage(Locale lc, String id) {
	ResourceBundle rb = ResourceBundle.getBundle("messages", lc,
		UIMessenger.class.getClassLoader());
	return rb.getString(id);
    }

    public static boolean hasLocale(Locale lc) {
	return true;
    }

    public static void addErrorMessage(String message, String descr) {
	try {
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
			    descr));
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}

    }

    public static void addInfoMessage(String message, String descr) {
	try {
	    FacesContext.getCurrentInstance()
		    .addMessage(
			    null,
			    new FacesMessage(FacesMessage.SEVERITY_INFO,
				    message, descr));
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}

    }

    public static void addFatalMessage(String message, String descr) {
	try {
	    FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(FacesMessage.SEVERITY_FATAL, message,
			    descr));
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

    public static void addWarnMessage(String message, String descr) {
	try {
	    FacesContext.getCurrentInstance()
		    .addMessage(
			    null,
			    new FacesMessage(FacesMessage.SEVERITY_WARN,
				    message, descr));
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

    public static void addFatalKeyMessage(String key, Locale lc) {
	try {
	    String message = getMessage(lc, key);
	    addFatalMessage(message, message);
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

    public static void addInfoKeyMessage(String key, Locale lc) {
	try {
	    String message = getMessage(lc, key);
	    addInfoMessage(message, message);
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

    public static void addErrorKeyMessage(String key, Locale lc) {
	try {
	    String message = getMessage(lc, key);
	    addErrorMessage(message, message);
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

    public static void addWarnKeyMessage(String key, Locale lc) {
	try {
	    String message = getMessage(lc, key);
	    addWarnMessage(message, message);
	} catch (Exception excasdasd) {
	 // Skip any problems here
	}
    }

}
