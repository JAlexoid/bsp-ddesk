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

package eu.activelogic.jsf.popmenu;

import java.io.IOException;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Component that creates a rollover popup container for another component.
 * 
 * Subcomponents are rendered in an invisible DIV. Facet mainLink is the
 * component that is visible at all times, all other are inlined in the
 * invisible DIV.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class PopMenu extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.hotwebnote.jsf.PopMenu";
    public static final String COMPONENT_FAMILY = "com.hotwebnote.jsf.PopMenu";

    private String idPrefix;
    private String id;
    private String styleClass;

    /** Creates a new instance of PopMenu */
    public PopMenu() {
    }

    public boolean getRendersChildren() {
	return true;
    }

    public String getFamily() {
	return COMPONENT_FAMILY;
    }

    /**
     * Render the invisible DIV with appropriate JavaScript references.
     */
    public void encodeBegin(FacesContext context) throws IOException {
	ResponseWriter writer = context.getResponseWriter();
	if (styleClass == null)
	    styleClass = (String) getAttributes().get("styleClass");
	String id = getId();

	if (this.getParent() instanceof UIColumn) {
	    id = ((UIColumn) this.getParent()).getClientId(context);
	}
	UIComponent mainLink = this.getFacet("mainLink");
	if (mainLink != null && mainLink.isRendered()) {
	    mainLink.getAttributes().put("onmouseover",
		    "_popmenu_showHelper('div_popmenu" + id + "');");
	    mainLink.getAttributes().put("onmouseout",
		    "_popmenu_hideHelper('div_popmenu" + id + "');");
	    mainLink.encodeBegin(context);
	    if (mainLink.getRendersChildren())
		mainLink.encodeChildren(context);
	    mainLink.encodeEnd(context);
	}

	writer.startElement("div", this);

	if (styleClass != null) {
	    writer.writeAttribute("class", styleClass, null);
	} else {
	    writer.writeAttribute("style", "            display: none;"
		    + "            position:absolute;"
		    + "            background: white;"
		    + "            border: 1px solid black;"
		    + "            border-top: none;"
		    + "            padding: 2px;}", null);
	}
	writer.writeAttribute("id", "div_popmenu" + id, null);

	writer.writeAttribute("onmouseover",
		"_popmenu_stayAliveHelper('div_popmenu" + id + "');", null);
	writer.writeAttribute("onmouseout",
		"_popmenu_hideHelperReal('div_popmenu" + id + "');", null);

    }

    public void encodeEnd(FacesContext context) throws IOException {
	ResponseWriter writer = context.getResponseWriter();
	writer.endElement("div");
    }

    public String getIdPrefix() {
	return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
	this.idPrefix = idPrefix;
    }

    public String getRendererType() {
	return null;
    }

    /**
     * Render all subcomponents
     * 
     * mainLink facet is to be handled separately, it is left visible.
     */
    public void encodeChildren(FacesContext context) throws IOException {
	String id = getId();
	if (this.getParent() instanceof UIColumn) {
	    id = ((UIColumn) this.getParent()).getClientId(context);
	}
	UIComponent mainLink = this.getFacet("mainLink");
	if (this.getChildren() != null) {
	    for (Object o : getChildren()) {
		if (o == mainLink)
		    continue;
		UIComponent ou = (UIComponent) o;
		ou.getAttributes().put("onmouseover",
			"_popmenu_stayAliveHelper('div_popmenu" + id + "');");
		ou.encodeBegin(context);
		if (ou.getRendersChildren())
		    ou.encodeChildren(context);
		ou.encodeEnd(context);
	    }
	} else
	    return;
    }

    public String getStyleClass() {
	return styleClass;
    }

    public void setStyleClass(String styleClass) {
	this.styleClass = styleClass;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Object saveState(FacesContext context) {
	Object values[] = new Object[2];
	values[0] = super.saveState(context);
	values[1] = styleClass;
	return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
	Object values[] = (Object[]) state;
	super.restoreState(context, values[0]);
	styleClass = (String) values[1];
    }

}
