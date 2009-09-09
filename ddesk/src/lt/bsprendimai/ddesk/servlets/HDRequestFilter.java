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

/**
 * Locale and security filter for requests. Redirects all requests to .xhtml to .jsf and changes locale
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 * @version
 */

public class HDRequestFilter implements Filter {

    // The filter configuration object we are associated with. If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public HDRequestFilter() {
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
    public void doFilter(ServletRequest request1, ServletResponse response1,
	    FilterChain chain) throws IOException, ServletException {

	HttpServletRequest request = (HttpServletRequest) request1;
	HttpServletResponse response = (HttpServletResponse) response1;

	if (request.getParameter("lang") != null
		&& !request.getParameter("lang").equals("")) {
	    Locale lc = new Locale(request.getParameter("lang"));
	    request.getSession().setAttribute("selectedLocale", lc);
	    request.getSession().setAttribute("selectedLocaleCode",
		    lc.toString().trim());
	}

	if (request.getSession(false) == null
		|| request.getSession().getAttribute("selectedLocale") == null) {

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

	String path = request.getContextPath() + request.getServletPath();

	path = path.replaceAll("\\.xhtml$", ".jsf");
	if (request.getQueryString() != null)
	    response.sendRedirect(path + "?" + request.getQueryString());
	else
	    response.sendRedirect(path);

	return;

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
	    if (debug) {
		log("ECRequestFilter:Initializing filter");
	    }
	}
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {

	if (filterConfig == null)
	    return ("ECRequestFilter()");
	StringBuffer sb = new StringBuffer("ECRequestFilter(");
	sb.append(filterConfig);
	sb.append(")");
	return (sb.toString());

    }

    // private void sendProcessingError(Throwable t, ServletResponse response) {
    //
    // String stackTrace = getStackTrace(t);
    //
    // if(stackTrace != null && !stackTrace.equals("")) {
    //
    // try {
    //
    // response.setContentType("text/html");
    // PrintStream ps = new PrintStream(response.getOutputStream());
    // PrintWriter pw = new PrintWriter(ps);
    // pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N
    //
    // // PENDING! Localize this for next official release
    // pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
    // pw.print(stackTrace);
    // pw.print("</pre></body>\n</html>"); //NOI18N
    // pw.close();
    // ps.close();
    // response.getOutputStream().close();;
    // }
    //
    // catch(Exception ex){ }
    // } else {
    // try {
    // PrintStream ps = new PrintStream(response.getOutputStream());
    // t.printStackTrace(ps);
    // ps.close();
    // response.getOutputStream().close();;
    // } catch(Exception ex){ }
    // }
    // }

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

    private static final boolean debug = true;
}
