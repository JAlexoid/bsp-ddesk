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

package eu.activelogic.jsf;

import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

/**
 * SelectOne component that skips validation of posted values.
 * 
 * Original {@link HtmlSelectOneMenu} validates the value against the server's list of values. In
 * AJAX call this is not needed.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class SelectOneNoValidations extends
	org.apache.myfaces.component.html.ext.HtmlSelectOneMenu {

    /** Creates a new instance of SelectOneNoValidations */
    public SelectOneNoValidations() {
    }

    protected void validateValue(FacesContext context, Object convertedValue) {
	boolean empty = convertedValue == null
		|| (convertedValue instanceof String && ((String) convertedValue)
			.length() == 0);

	if (isRequired() && empty) {
	    _MessageUtils.addErrorMessage(context, this, REQUIRED_MESSAGE_ID,
		    new Object[] { getId() });
	    setValid(false);
	    return;
	}

	if (!empty) {
	    _ComponentUtils.callValidators(context, this, convertedValue);
	}
    }

}
