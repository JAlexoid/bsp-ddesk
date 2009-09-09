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
package lt.bsprendimai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Base class for timespan calculation
 */
public class Term implements Comparable<Term> {
    private Date from;
    private Date to;

    /**
     * Default contructor
     */
    public Term() {
    }

    /**
     * Contructor for the interval
     * 
     * @param from
     *            Interval start
     * @param to
     *            Interval end
     */
    public Term(Date from, Date to) {
	this.from = from;
	this.to = to;
    }

    /**
     * Sets to may be null, then viewed as an endless timespan
     * 
     * @param to
     *            end of timespan
     */
    public void setTo(Date to) {
	this.to = to;
    }

    /**
     * Sets the begining of timespan NOT NULL
     * 
     * @param from
     *            begining of timespan
     */
    public void setFrom(Date from) {
	this.from = from;
    }

    /**
     * Getter of the end of timespan
     * 
     * @return end of timspan
     */
    public Date getTo() {
	return to;
    }

    /**
     * Getter fot the begining of this timespan
     * 
     * @return begining of this timespan
     */
    public Date getFrom() {
	return from;
    }

    /**
     * Checks if the suplied date is anywhere inside this timespan
     * 
     * @param dt
     *            Date withch to check
     * @return if inside than true else false
     */
    public boolean isInside(Date dt) {
	if (dt == null)
	    return false;
	long tm1 = Long.MIN_VALUE;
	long tm2 = Long.MAX_VALUE;
	if (from != null)
	    tm1 = from.getTime() / 1000;
	if (to != null)
	    tm2 = to.getTime() / 1000;
	return (tm1 <= dt.getTime() / 1000 && tm2 >= dt.getTime() / 1000);
    }

    /**
     * Checks if the suplied term is anywhere inside this timespan
     * 
     * @param tt
     *            Term to check
     * @return if inside than true else false
     */
    public boolean isInside(Term tt) {
	if (from.getTime() / 1000 > tt.from.getTime() / 1000) {
	    return false;
	}
	if (to != null && tt.to != null
		&& to.getTime() / 1000 < tt.to.getTime() / 1000) {
	    return false;
	}
	if (to != null && tt.to == null) {
	    return false;
	}

	return true;
    }

    /**
     * Checks if the suplied term overlaps this timespan
     * 
     * @return if overlaps than true else false
     * @param tt
     *            Term to check
     */
    public boolean isOverlapping(Term tt) {
	return isOverlapping(tt.from, tt.to);
    }

    /**
     * Checks if the suplied term overlaps this timespan
     * 
     * @return if overlaps than true else false
     * @param dt1
     *            begining of the timespan to check
     * @param dt2
     *            end of the timespan to check(may be NULL for endless terms)
     */
    public boolean isOverlapping(Date dt1, Date dt2) {
	if (dt2 != null && from.getTime() / 1000 >= dt2.getTime() / 1000)
	    return false;
	if (to != null && to.getTime() / 1000 <= dt1.getTime() / 1000)
	    return false;
	return true;
    }

    /**
     * Gets the latest after start of from and supplied date
     * 
     * @param st
     *            date to check
     * @return if this timespans start is later than return it, otherwise the supplied
     *         date is returned
     */
    public Date getStart(Date st) {
	if (from != null && st != null) {
	    if (st.before(from))
		return from;
	}
	if (st != null)
	    return st;
	return from;
    }

    /**
     * Gets the earliest before finish of to and supplied date
     * 
     * @param ed
     *            date to check
     * @return if this timespans end is earlier than return it, otherwise the supplied
     *         date is returned
     */
    public Date getEnd(Date ed) {
	if (to != null && ed != null) {
	    if (ed.after(to))
		return to;
	}
	if (ed != null)
	    return ed;
	return to;

	// if(to == null && ed == null)
	// return null;
	// if(to != null && ed != null && ed.after(to))
	// return to;
	// else if(ed != null)
	// return ed;
	// else
	// return to;
    }

    /**
     * Gets the earliest before start from and supplied date(null is earlier)
     * 
     * @param st
     *            date to check
     * @return if this timespans start is earlier than return it, otherwise the supplied
     *         date is returned
     */
    public Date getBegin(Date st) {
	if (from != null && st != null) {
	    if (st.before(from)) {
		return st;
	    } else {
		return from;
	    }
	}
	return null;
    }

    /**
     * Gets the latest or null from and supplied date(null is later)
     * 
     * @param ed
     *            date to check
     * @return if this timespans end is later than return it, otherwise the supplied
     *         date is returned
     */
    public Date getFinish(Date ed) {
	if (to != null && ed != null) {
	    if (ed.before(to))
		return to;
	    else
		return ed;
	}
	return null;
    }

    /**
     * Returns string representation of this term
     * 
     * @return string representation of this term
     */
    public String toString() {
	return " Term[" + from + " to " + to + "]";
    }

    /**
     * Count months between two dates(term's begining and the end)
     * 
     * @return month count
     */
    public int getMonthCount() {
	return this.splitByMonths().size();
    }

    /**
     * Count months between two dates
     * 
     * @param fromMargin
     *            begining
     * @param toMargin
     *            end
     * @return month count
     */
    public static int getMonthCount(java.util.Date fromMargin,
	    java.util.Date toMargin) {
	return new Term(fromMargin, toMargin).getMonthCount();
    }

    /**
     * Splits this term into terms by months(1 month 1 term)
     * 
     * @return List of terms
     */
    public List<Term> splitByMonths() {
	return splitByMonths(this);
    }

    /**
     * Splits supplied term into terms by months(1 month 1 term)
     * 
     * @return List of terms
     */
    public static List<Term> splitByMonths(Term tt) {
	List<Term> l = new ArrayList<Term>();

	Calendar cFrom = Calendar.getInstance();
	Calendar cTo = Calendar.getInstance();
	if (tt.getFrom().after(tt.getTo())) {
	    cFrom.setTime(tt.getTo());
	    cTo.setTime(tt.getFrom());
	} else {
	    cFrom.setTime(tt.getFrom());
	    cTo.setTime(tt.getTo());
	}

	if (cFrom.get(Calendar.YEAR) != cTo.get(Calendar.YEAR)
		|| cFrom.get(Calendar.MONTH) != cTo.get(Calendar.MONTH)) {

	    Calendar cToI = Calendar.getInstance();
	    cToI.setTime(cFrom.getTime());
	    cToI.set(Calendar.DAY_OF_MONTH, cToI
		    .getActualMaximum(Calendar.DAY_OF_MONTH));
	    cToI.set(Calendar.HOUR_OF_DAY, 23);
	    cToI.set(Calendar.MINUTE, 59);
	    cToI.set(Calendar.SECOND, 59);
	    cToI.set(Calendar.MILLISECOND, 999);

	    l.add(new Term(cFrom.getTime(), cToI.getTime()));

	    while (cToI.get(Calendar.YEAR) != cTo.get(Calendar.YEAR)
		    || cToI.get(Calendar.MONTH) != cTo.get(Calendar.MONTH)) {

		cToI.add(Calendar.MONTH, 1);
		cToI.set(Calendar.DAY_OF_MONTH, cToI
			.getActualMaximum(Calendar.DAY_OF_MONTH));
		cToI.set(Calendar.HOUR_OF_DAY, 23);
		cToI.set(Calendar.MINUTE, 59);
		cToI.set(Calendar.SECOND, 59);
		cToI.set(Calendar.MILLISECOND, 999);
		if (cToI.after(cTo))
		    cToI.setTime(cTo.getTime());

		cFrom.add(Calendar.MONTH, 1);
		cFrom.set(Calendar.HOUR_OF_DAY, 0);
		cFrom.set(Calendar.MINUTE, 0);
		cFrom.set(Calendar.SECOND, 0);
		cFrom.set(Calendar.MILLISECOND, 0);
		cFrom.set(Calendar.DAY_OF_MONTH, 1);

		l.add(new Term(cFrom.getTime(), cToI.getTime()));

	    }
	} else {
	    l.add(new Term(cFrom.getTime(), cTo.getTime()));
	}
	return l;
    }

    /**
     * Convert to human readable form
     * 
     * @return human readable time string
     * @param millis
     *            millisecontd to convert to human readable form
     */
    public static String convertMillis(long millis) {
	String ret = "";
	ret += ((millis / 1000) / 3600);
	ret += ":";
	ret += ((millis / 1000) / 60) % 60;
	ret += ":";
	ret += (millis / 1000) % 60;
	ret += ".";
	ret += (millis % 1000);
	return ret;
    }

    /**
     * Compare two terms(their starts)
     * 
     * @param o
     *            another term
     * @return comparison of two starts
     */
    public int compareTo(Term o) {
	return from.compareTo(o.from);
    }

    /**
     * Checks if the dates are on the same day.
     * 
     * @return returns true if the dates are on the same day
     */

    public boolean isSameDay() {
	return isSameDay(this.from, this.to);
    }

    /**
     * Checks if the dates are on the same day.
     * 
     * @param one
     *            first date
     * @param two
     *            second date
     * @return returns true if the dates are on the same day
     */

    public static boolean isSameDay(Date one, Date two) {
	Calendar c = Calendar.getInstance();
	c.setTime(one);
	int day = c.get(Calendar.DAY_OF_YEAR);
	int year = c.get(Calendar.YEAR);
	c.setTime(two);
	return day == c.get(Calendar.DAY_OF_YEAR)
		&& year == c.get(Calendar.YEAR);
    }

}