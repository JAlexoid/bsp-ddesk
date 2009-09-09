package lt.bsprendimai.ddesk.servlets;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This request wrapper class extends the support class
 * HttpServletRequestWrapper, which implements all the methods in the
 * HttpServletRequest interface, as delegations to the wrapped request. You
 * only need to override the methods that you need to change. You can get
 * access to the wrapped request using the method getRequest()
 * @author NetBeans code generator
 */
@SuppressWarnings("unchecked")
public class RequestWrapper extends HttpServletRequestWrapper {

    private static final boolean debug = false;

    public RequestWrapper(HttpServletRequest request) {
	super(request);
    }

    //
    // You might, for example, wish to add a setParameter() method. To do
    // this
    // you must also override the getParameter, getParameterValues,
    // getParameterMap,
    // and getParameterNames methods.
    //

    protected Hashtable localParams = null;

    public void setParameter(String name, String[] values) {
	if (debug)
	    System.out.println("SSLAutoAuthFilter::setParameter(" + name + "="
		    + values + ")" + " localParams = " + localParams);

	if (localParams == null) {
	    localParams = new Hashtable();
	    //
	    // Copy the parameters from the underlying request.
	    Map wrappedParams = getRequest().getParameterMap();
	    Set entrySet = wrappedParams.entrySet();
	    for (Iterator it = entrySet.iterator(); it.hasNext();) {
		Map.Entry e = (Map.Entry) it.next();
		localParams.put(e.getKey(), e.getValue());
	    }
	}
	localParams.put(name, values);
    }

    public String getParameter(String name) {
	if (debug)
	    System.out.println("SSLAutoAuthFilter::getParameter(" + name
		    + ") localParams = " + localParams);
	if (localParams == null)
	    return getRequest().getParameter(name);
	Object val = localParams.get(name);
	if (val instanceof String)
	    return (String) val;
	if (val instanceof String[]) {
	    String[] values = (String[]) val;
	    return values[0];
	}
	return (val == null ? null : val.toString());
    }

    public String[] getParameterValues(String name) {
	if (debug)
	    System.out.println("SSLAutoAuthFilter::getParameterValues(" + name
		    + ") localParams = " + localParams);
	if (localParams == null)
	    return getRequest().getParameterValues(name);

	return (String[]) localParams.get(name);
    }

    public Enumeration getParameterNames() {
	if (debug)
	    System.out
		    .println("SSLAutoAuthFilter::getParameterNames() localParams = "
			    + localParams);
	if (localParams == null)
	    return getRequest().getParameterNames();

	return localParams.keys();
    }

    public Map getParameterMap() {
	if (debug)
	    System.out
		    .println("SSLAutoAuthFilter::getParameterMap() localParams = "
			    + localParams);
	if (localParams == null)
	    return getRequest().getParameterMap();
	return localParams;
    }
}
