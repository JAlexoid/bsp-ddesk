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
package lt.bsprendimai.ddesk.pdf;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.bsprendimai.ddesk.UserHandler;
import lt.bsprendimai.ddesk.dao.Company;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.Ticket;

import org.hibernate.Session;

/**
 * A servlet for PDF ticket generation.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 * @version
 */
public class PDFTicketPrinter extends HttpServlet {

    /**
	 *
	 */
    private static final long serialVersionUID = 4133577666538008830L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void processRequest(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	UserHandler uh = (UserHandler) request.getSession().getAttribute(
		"userHandler");
	if (uh == null || !uh.isLoggedIn() || uh.getUser().getCompany() != 0) {
	    response.sendRedirect(request.getContextPath() + "/login.jsf");
	    return;
	}

	try {
	    Integer id = new Integer(request.getParameter("ticketId"));

	    Session sess = SessionHolder.currentSession().getSess();
	    Ticket tt = (Ticket) sess.get(Ticket.class, id);
	    Company cc = (Company) sess.get(Company.class, tt.getCompany());
	    Company we = (Company) sess.get(Company.class, 0);
	    Person me = null;
	    if (tt.getAssignedTo() != null) {
		try {
		    me = (Person) sess.get(Person.class, tt.getAssignedTo());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    } else {
		me = new Person();
		me.setName("");
	    }
	    Person respo = null;
	    try {
		respo = (Person) sess.get(Person.class, tt.getPerson());
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	    PDFTicket ppd = new PDFTicket(tt, me, respo, cc, we);
	    File f = new File(this.getServletContext().getRealPath("/"));
	    ppd.setRoot(f);
	    byte[] out = ppd.writePage();
	    response.setContentType("application/pdf");
	    response.getOutputStream().write(out);

	} catch (Exception excv) {
	    excv.printStackTrace();
	    RequestDispatcher rd = this.getServletContext()
		    .getRequestDispatcher("/errorInPdf.html");
	    rd.forward(request, response);
	}
    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
	return "Short description";
    }
    // </editor-fold>
}
