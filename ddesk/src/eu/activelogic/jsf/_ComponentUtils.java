/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.activelogic.jsf;

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author Manfred Geiler (latest modification by $Author: schof $)
 * @version $Revision: 264794 $ $Date: 2005-08-30 11:19:58 -0400 (Tue, 30 Aug 2005) $
 */
@SuppressWarnings("deprecation")
class _ComponentUtils {
    private _ComponentUtils() {
    }

    static UIComponent findParentNamingContainer(UIComponent component,
	    boolean returnRootIfNotFound) {
	UIComponent parent = component.getParent();
	if (returnRootIfNotFound && parent == null) {
	    return component;
	}
	while (parent != null) {
	    if (parent instanceof NamingContainer)
		return parent;
	    if (returnRootIfNotFound) {
		UIComponent nextParent = parent.getParent();
		if (nextParent == null) {
		    return parent; // Root
		}
		parent = nextParent;
	    } else {
		parent = parent.getParent();
	    }
	}
	return null;
    }

    static UIComponent getRootComponent(UIComponent component) {
	UIComponent parent;
	for (;;) {
	    parent = component.getParent();
	    if (parent == null)
		return component;
	    component = parent;
	}
    }

    @SuppressWarnings("unchecked")
    static UIComponent findComponent(UIComponent findBase, String id) {
	if (idsAreEqual(id, findBase)) {
	    return findBase;
	}

	for (Iterator it = findBase.getFacetsAndChildren(); it.hasNext();) {
	    UIComponent childOrFacet = (UIComponent) it.next();
	    if (!(childOrFacet instanceof NamingContainer)) {
		UIComponent find = findComponent(childOrFacet, id);
		if (find != null)
		    return find;
	    } else if (idsAreEqual(id, childOrFacet)) {
		return childOrFacet;
	    }
	}

	return null;
    }

    private static boolean idsAreEqual(String id, UIComponent cmp) {
	if (id.equals(cmp.getId()))
	    return true;

	if (cmp instanceof UIData) {
	    UIData uiData = ((UIData) cmp);

	    if (uiData.getRowIndex() == -1) {
		return dynamicIdIsEqual(id, cmp.getId());
	    } else {
		return id.equals(cmp.getId() + "_" + uiData.getRowIndex());
	    }
	}

	return false;
    }

    private static boolean dynamicIdIsEqual(String dynamicId, String id) {
	return dynamicId.matches(id + "_[0-9]*");
    }

    static void callValidators(FacesContext context, UIInput input,
	    Object convertedValue) {
	Validator[] validators = input.getValidators();
	for (int i = 0; i < validators.length; i++) {
	    Validator validator = validators[i];
	    try {
		validator.validate(context, input, convertedValue);
	    } catch (ValidatorException e) {
		input.setValid(false);
		FacesMessage facesMessage = e.getFacesMessage();
		if (facesMessage != null) {
		    facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		    context
			    .addMessage(input.getClientId(context),
				    facesMessage);
		}
	    }
	}

	MethodBinding validatorBinding = input.getValidator();
	if (validatorBinding != null) {
	    try {
		validatorBinding.invoke(context, new Object[] { context, input,
			convertedValue });
	    } catch (EvaluationException e) {
		input.setValid(false);
		Throwable cause = e.getCause();
		if (cause instanceof ValidatorException) {
		    FacesMessage facesMessage = ((ValidatorException) cause)
			    .getFacesMessage();
		    if (facesMessage != null) {
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(input.getClientId(context),
				facesMessage);
		    }
		} else {
		    throw e;
		}
	    }
	}
    }

}
