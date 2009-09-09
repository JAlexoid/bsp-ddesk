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
package lt.bsprendimai.ddesk.servlets;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.bsprendimai.ddesk.UserHandler;
import lt.bsprendimai.ddesk.dao.Company;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.SessionHolder;

/**
 * Session setup and maintenanace.
 * Database transaction management, uses Hibernates internal transaction management.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 * @version
 */

public class HDSessionFilter implements Filter {

    // The filter configuration object we are associated with. If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public HDSessionFilter() {
    }

    private boolean doBeforeProcessing(RequestWrapper request,
	    ResponseWrapper response) throws IOException, ServletException {

	if (this.getFilterConfig().getServletContext().getAttribute(
		"contextName") == null) {
	    this.getFilterConfig().getServletContext().setAttribute(
		    "contextName", request.getContextPath());
	}

	if (request.getSession(false) == null
		|| request.getSession().getAttribute("selectedLocale") == null) {
	    // System.out.println("Stetting browser locale");
	    // Enumeration<Locale> en = (Enumeration<Locale>) request.getLocales();
	    // boolean found = false;
	    // Locale lc = null;
	    // while(en.hasMoreElements()){
	    // lc = en.nextElement();
	    // if(Messenger.hasLocale(lc)){
	    // found = true;
	    // break;
	    // }
	    // }
	    //
	    // if(found && lc != null){
	    // request.getSession().setAttribute("selectedLocale", lc);
	    // request.getSession().setAttribute("selectedLocaleCode", lc.toString());
	    // } else {
	    request.getSession().setAttribute("selectedLocale",
		    Locale.getDefault());
	    request.getSession().setAttribute("selectedLocaleCode",
		    Locale.getDefault().toString().trim());
	    // }
	}

	if (request.getServletPath().indexOf("intranet") != -1) {
	    UserHandler uh = (UserHandler) request.getSession().getAttribute(
		    "userHandler");
	    if (uh != null
		    && uh.isLoggedIn()
		    && (uh.getUser().getCompany() == Company.OWNER || uh
			    .getUser().getLoginLevel() == Person.PARTNER)) {
		request.getSession().setAttribute("selectedLocale",
			Locale.getDefault());
		request.getSession().setAttribute("selectedLocaleCode",
			Locale.getDefault().toString().trim());
		return true;
	    } else {
		response.sendRedirect(request.getContextPath() + "/login.jsf");
		return false;
	    }
	}

	if (request.getRequestURI().endsWith(".jsf")
		&& !request.getServletPath().startsWith("/login")) {
	    try {
		if (!((UserHandler) request.getSession().getAttribute(
			"userHandler")).isLoggedIn()) {
		    response.sendRedirect(request.getContextPath()
			    + "/login.jsf");
		    // System.out.println("request.getServletPath().startsWith(\"index\") = "+request.getServletPath().startsWith("/index"));
		    return false;
		}
		SessionHolder.currentSession();
		return true;
	    } catch (Exception ex) {
		// System.out.println("request.getServletPath().startsWith(\"index\") = "+request.getServletPath().startsWith("/index"));
		response.sendRedirect(request.getContextPath() + "/login.jsf");
		return false;
	    }
	} else {
	    // System.out.println("request.getServletPath().startsWith(\"index\") = "+request.getServletPath().startsWith("/index"));
	    return true;
	}
    }

    private boolean doAfterProcessing(RequestWrapper request,
	    ResponseWrapper response) throws IOException, ServletException {
	SessionHolder.closeSession();
	return true;
    }

    /**
     *
     * @param request
     *            The servlet request we are processing
     * @param result
     *            The servlet response we are creating
     * @param chain
     *            The filter chain we are processing
     *
     * @exception IOException
     *                if an input/output error occurs
     * @exception ServletException
     *                if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {

	//
	// Create wrappers for the request and response objects.
	// Using these, you can extend the capabilities of the
	// request and response, for example, allow setting parameters
	// on the request before sending the request to the rest of the filter chain,
	// or keep track of the cookies that are set on the response.
	//
	// Caveat: some servers do not handle wrappers very well for forward or
	// include requests.
	//

	RequestWrapper wrappedRequest = new RequestWrapper(
		(HttpServletRequest) request);
	ResponseWrapper wrappedResponse = new ResponseWrapper(
		(HttpServletResponse) response);

	if (!doBeforeProcessing(wrappedRequest, wrappedResponse)) {
	    return;
	}

	Throwable problem = null;

	try {
	    chain.doFilter(wrappedRequest, wrappedResponse);
	} catch (Throwable t) {
	    //
	    // If an exception is thrown somewhere down the filter chain,
	    // we still want to execute our after processing, and then
	    // rethrow the problem after that.
	    //
	    problem = t;
	    t.printStackTrace();
	}

	doAfterProcessing(wrappedRequest, wrappedResponse);

	//
	// If there was a problem, we want to rethrow it if it is
	// a known type, otherwise log it.
	//
	if (problem != null) {
	    if (problem instanceof ServletException)
		throw (ServletException) problem;
	    if (problem instanceof IOException)
		throw (IOException) problem;
	    sendProcessingError(problem, response);
	}
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
	return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig
     *            The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {

	this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {

	this.filterConfig = filterConfig;
	if (filterConfig != null) {
	    // if (debug) {
	    // log("ECSessionFilter: Initializing filter");
	    // }
	}
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {

	if (filterConfig == null)
	    return ("ECSessionFilter()");
	StringBuffer sb = new StringBuffer("ECSessionFilter(");
	sb.append(filterConfig);
	sb.append(")");
	return (sb.toString());

    }

    private void sendProcessingError(Throwable t, ServletResponse response) {

	String stackTrace = getStackTrace(t);

	if (stackTrace != null && !stackTrace.equals("")) {

	    try {

		response.setContentType("text/html");
		PrintStream ps = new PrintStream(response.getOutputStream());
		PrintWriter pw = new PrintWriter(ps);
		pw
			.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); // NOI18N

		// PENDING! Localize this for next official release
		pw
			.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
		pw.print(stackTrace);
		pw.print("</pre></body>\n</html>"); // NOI18N
		pw.close();
		ps.close();
		response.getOutputStream().close();
		;
	    }

	    catch (Exception ex) {
	    }
	} else {
	    try {
		PrintStream ps = new PrintStream(response.getOutputStream());
		t.printStackTrace(ps);
		ps.close();
		response.getOutputStream().close();
		;
	    } catch (Exception ex) {
	    }
	}
    }

    public static String getStackTrace(Throwable t) {

	String stackTrace = null;

	try {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    t.printStackTrace(pw);
	    pw.close();
	    sw.close();
	    stackTrace = sw.getBuffer().toString();
	} catch (Exception ex) {
	}
	return stackTrace;
    }

    public void log(String msg) {
	filterConfig.getServletContext().log(msg);
    }

}
