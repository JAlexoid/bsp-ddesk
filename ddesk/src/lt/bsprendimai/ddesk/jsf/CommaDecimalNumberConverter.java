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
package lt.bsprendimai.ddesk.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * Number converter, where commas are relaced by dots to be parsed by decimal parsers.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class CommaDecimalNumberConverter implements
	javax.faces.validator.Validator {

    public static final String DOUBLE_RANGE_CONVERTER = "javax.faces.validator.DoubleRangeValidator.TYPE";

    /** Creates a new instance of CommaDecimalNumberConverter */
    public CommaDecimalNumberConverter() {
    }

    public void validate(FacesContext context, UIComponent component,
	    Object toValidate) throws ValidatorException {
	String value = null;
	if ((context == null) || (component == null)) {
	    throw new NullPointerException();
	}
	if (!(component instanceof UIInput)) {
	    return;
	}
	if (null == toValidate) {
	    return;
	}

	value = toValidate.toString().replace(',', '.');
	value = value.toString().replace(" ", "");

	try {
	    Double.valueOf(value);
	} catch (NumberFormatException ex) {
	    FacesMessage errMsg = MessageFactory.getMessage(context,
		    DOUBLE_RANGE_CONVERTER);
	    throw new ValidatorException(errMsg);
	}

    }

}
