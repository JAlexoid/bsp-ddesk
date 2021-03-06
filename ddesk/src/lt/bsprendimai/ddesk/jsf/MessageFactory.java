/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.bsprendimai.ddesk.jsf;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
*
* <p>supported filters: <code>package</code> and
* <code>protection</code>.</p>
*/
public class MessageFactory {

    /** Creates a new instance of MessageFactory */
    private MessageFactory() {
    }

    public static String substituteParams(Locale locale, String msgtext,
	    Object[] params) {
	String localizedStr = null;

	if ((params == null) || (msgtext == null)) {
	    return msgtext;
	}

	StringBuffer b = new StringBuffer(100);
	MessageFormat mf = new MessageFormat(msgtext);

	if (locale != null) {
	    mf.setLocale(locale);
	    b.append(mf.format(params));
	    localizedStr = b.toString();
	}

	return localizedStr;
    }

    /**
     * This version of getMessage() is used in the RI for localizing RI
     * specific messages.
     */
    public static FacesMessage getMessage(String messageId, Object[] params) {
	Locale locale = null;
	FacesContext context = FacesContext.getCurrentInstance();

	// context.getViewRoot() may not have been initialized at this point.
	if ((context != null) && (context.getViewRoot() != null)) {
	    locale = context.getViewRoot().getLocale();

	    if (locale == null) {
		locale = Locale.getDefault();
	    }
	} else {
	    locale = Locale.getDefault();
	}

	return getMessage(locale, messageId, params);
    }

    public static FacesMessage getMessage(Locale locale, String messageId,
	    Object[] params) {
	// FacesMessage result = null;
	String summary = null;
	String detail = null;
	String bundleName = null;
	ResourceBundle bundle = null;

	// see if we have a user-provided bundle
	if (null != (bundleName = getApplication().getMessageBundle())) {
	    if (null != (bundle = ResourceBundle.getBundle(bundleName, locale,
		    getCurrentLoader(bundleName)))) {
		// see if we have a hit
		try {
		    summary = bundle.getString(messageId);
		} catch (MissingResourceException e) {
		}
	    }
	}

	// we couldn't find a summary in the user-provided bundle
	if (null == summary) {
	    // see if we have a summary in the app provided bundle
	    bundle = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES,
		    locale, getCurrentLoader(bundleName));

	    if (null == bundle) {
		throw new NullPointerException();
	    }

	    // see if we have a hit
	    try {
		summary = bundle.getString(messageId);
	    } catch (MissingResourceException e) {
	    }
	}

	// we couldn't find a summary anywhere! Return null
	if (null == summary) {
	    return null;
	}

	// At this point, we have a summary and a bundle.
	if ((null == summary) || (null == bundle)) {
	    throw new NullPointerException();
	}

	summary = substituteParams(locale, summary, params);

	try {
	    detail = substituteParams(locale, bundle.getString(messageId
		    + "_detail"), params);
	} catch (MissingResourceException e) {
	}

	return (new FacesMessage(summary, detail));
    }

    //
    // Methods from MessageFactory
    //
    public static FacesMessage getMessage(FacesContext context, String messageId) {
	return getMessage(context, messageId, null);
    }

    public static FacesMessage getMessage(FacesContext context,
	    String messageId, Object[] params) {
	if ((context == null) || (messageId == null)) {
	    throw new NullPointerException(
		    "One or more parameters could be null");
	}

	Locale locale = null;

	// viewRoot may not have been initialized at this point.
	if ((context != null) && (context.getViewRoot() != null)) {
	    locale = context.getViewRoot().getLocale();
	} else {
	    locale = Locale.getDefault();
	}

	if (null == locale) {
	    throw new NullPointerException();
	}

	FacesMessage message = getMessage(locale, messageId, params);

	if (message != null) {
	    return message;
	}

	locale = Locale.getDefault();

	return (getMessage(locale, messageId, params));
    }

    public static FacesMessage getMessage(FacesContext context,
	    String messageId, Object param0) {
	return getMessage(context, messageId, new Object[] { param0 });
    }

    public static FacesMessage getMessage(FacesContext context,
	    String messageId, Object param0, Object param1) {
	return getMessage(context, messageId, new Object[] { param0, param1 });
    }

    public static FacesMessage getMessage(FacesContext context,
	    String messageId, Object param0, Object param1, Object param2) {
	return getMessage(context, messageId, new Object[] { param0, param1,
		param2 });
    }

    public static FacesMessage getMessage(FacesContext context,
	    String messageId, Object param0, Object param1, Object param2,
	    Object param3) {
	return getMessage(context, messageId, new Object[] { param0, param1,
		param2, param3 });
    }

    protected static Application getApplication() {
	return (FacesContext.getCurrentInstance().getApplication());
    }

    protected static ClassLoader getCurrentLoader(Object fallbackClass) {
	ClassLoader loader = Thread.currentThread().getContextClassLoader();

	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}

	return loader;
    }

}
