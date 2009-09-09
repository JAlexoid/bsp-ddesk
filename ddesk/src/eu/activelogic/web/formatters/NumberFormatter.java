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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Formatter functions for numbers
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class NumberFormatter {

    /** Creates a new instance of NumberFormatter */
    public NumberFormatter() {
    }

    /**
     * Format the provided number as a decimal number.
     * 
     * @param nm
     *            number to format
     * @return formatted number as a string
     * @see DecimalFormat
     */
    public static String formatNumber(Number nm) {
	if (nm == null)
	    return null;
	try {
	    NumberFormat nf = new DecimalFormat("#0.#");

	    return nf.format(nm);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the provided number as a percent.
     * 
     * @param nm
     *            number to format
     * @return formatted number as a string
     * @see DecimalFormat
     */
    public static String formatPercent(Number nm) {
	if (nm == null)
	    return null;
	try {
	    NumberFormat nf = new DecimalFormat("#0%");
	    return nf.format(nm);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the provided number using a pattern.
     * 
     * @param nm
     *            number to format
     * @return formatted number as a string
     * @see DecimalFormat
     */
    public static String formatPatternNumber(Number nm, String pattern) {
	if (nm == null)
	    return null;
	try {
	    NumberFormat nf = new DecimalFormat(pattern);
	    return nf.format(nm);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the provided number using a pattern with percentage added.
     * 
     * @param nm
     *            number to format
     * @return formatted number as a string
     * @see DecimalFormat
     */
    public static String formatPatternPercent(Number nm, String pattern) {
	if (nm == null)
	    return null;
	try {
	    NumberFormat nf = new DecimalFormat(pattern + "%");
	    return nf.format(nm);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

}
