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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatting functions
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class DateFormatter {
    /** Creates a new instance of DateFormatter */
    public DateFormatter() {
    }

    /**
     * Format the date in {@link DateFormat#MEDIUM} predefined pattern with default locale.
     * 
     * @param dt
     *            date to format to string
     * @return formated and readable date string
     */
    public static String formatDate(Date dt) {
	if (dt == null)
	    return null;
	try {
	    DateFormat formatter = DateFormat
		    .getDateInstance(DateFormat.MEDIUM);
	    return formatter.format(dt);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the date in {@link DateFormat#MEDIUM} predefined pattern with provided locale ID.
     * 
     * @param dt
     *            date to format to string
     * @param lc
     *            locale ID to use
     * @return formated and readable date string
     */
    public static String formatLocaleDate(Date dt, String lc) {
	if (dt == null)
	    return null;
	try {
	    DateFormat formatter = DateFormat.getDateInstance(
		    DateFormat.MEDIUM, new Locale(lc));
	    return formatter.format(dt);
	} catch (NullPointerException exc) {
	    return null;
	}

    }

    /**
     * Format the date and time in {@link DateFormat#MEDIUM} predefined pattern with default locale.
     * 
     * @param dt
     *            date to format to string
     * @return formated and readable date string
     */
    public static String formatDateTime(Date dt) {
	if (dt == null)
	    return null;
	try {
	    DateFormat formatter = DateFormat.getDateTimeInstance(
		    DateFormat.MEDIUM, DateFormat.MEDIUM);
	    return formatter.format(dt);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the date and time in {@link DateFormat#MEDIUM} predefined pattern with provided locale
     * ID.
     * 
     * @param dt
     *            date to format to string
     * @param lc
     *            locale ID to use
     * @return formated and readable date string
     */
    public static String formatLocaleDateTime(Date dt, String lc) {
	if (dt == null)
	    return null;
	try {
	    DateFormat formatter = DateFormat.getDateTimeInstance(
		    DateFormat.MEDIUM, DateFormat.MEDIUM, new Locale(lc));
	    return formatter.format(dt);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

    /**
     * Format the date with provided pattern with default locale.
     * 
     * @param dt
     *            date to format to string
     * @param pattern
     *            pattern to use {@link SimpleDateFormat}
     * @return formated and readable date string
     * @see SimpleDateFormat
     */
    public static String formatPatternDate(Date dt, String pattern) {
	if (dt == null)
	    return null;
	try {
	    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	    return formatter.format(dt);
	} catch (NullPointerException exc) {
	    return null;
	}
    }

}
