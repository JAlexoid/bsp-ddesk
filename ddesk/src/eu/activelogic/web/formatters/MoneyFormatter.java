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
package eu.activelogic.web.formatters;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Money formatting functions
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class MoneyFormatter {

    /** Creates a new instance of MoneyFormatter */
    public MoneyFormatter() {
    }

    /**
     * Format number in money format with the provided locale ID
     * 
     * @param nm
     *            a number to format
     * @param lc
     *            what locale ID to use
     * @return formatted number as a string
     * @see NumberFormat#getCurrencyInstance(Locale)
     */
    public static String formatMoney(Number nm, String lc) {
	if (nm == null)
	    return null;
	try {
	    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale(lc));
	    return nf.format(nm);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

}
