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
import java.io.InputStream;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Outputs simple script for the popup menu.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class PopMenuScript extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.hotwebnote.jsf.PopMenuScript";
    public static final String COMPONENT_FAMILY = "com.hotwebnote.jsf.Scripts";

    public static String script = "";

    static {
	try {
	    StringBuilder sb = new StringBuilder();
	    InputStream is = PopMenuScript.class
		    .getResourceAsStream("script.js");
	    int i = -1;
	    while ((i = is.read()) >= 0) {
		sb.append((char) i);
	    }
	    script = sb.toString();
	} catch (IOException ex) {
	    throw new ExceptionInInitializerError(ex);
	}
    }

    /** Creates a new instance of PopMenu */
    public PopMenuScript() {
    }

    public boolean getRendersChildren() {
	return true;
    }

    public String getFamily() {
	return COMPONENT_FAMILY;
    }

    /**
     * Send the short script embedded in the HTML page
     */
    public void encodeBegin(FacesContext context) throws IOException {
	ResponseWriter writer = context.getResponseWriter();
	writer.startElement("script", this);
	writer.writeAttribute("type", "text/javascript", null);
	writer.writeText(script, null);
	writer.endElement("script");
    }

    public void encodeEnd(FacesContext context) throws IOException {

    }

    public String getRendererType() {
	return null;
    }

    public void encodeChildren(FacesContext context) throws IOException {
	return;
    }

}
