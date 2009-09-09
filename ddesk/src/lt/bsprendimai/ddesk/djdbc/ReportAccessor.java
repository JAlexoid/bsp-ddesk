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
package lt.bsprendimai.ddesk.djdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.spi.Configurator;

import lt.bsprendimai.Configuration;
import lt.bsprendimai.ddesk.ChangeDetector;
import lt.bsprendimai.ddesk.StandardResults;
import lt.bsprendimai.ddesk.UserHandler;
import lt.bsprendimai.ddesk.dao.CompanyReport;
import lt.bsprendimai.ddesk.dao.PersonReport;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.TicketChanges;

/**
 * For those parts that would be too heavy in load on the database.
 * Reports are done using Dynamic JDBC (djdbc) queries.
 * Takes SQL statements for JDBC prepare from sql.properties via {@link Configuration#getSQL(String)}
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("deprecation")
public class ReportAccessor implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -7523206695942574976L;
    private Date start;
    private Date end;
    private Integer companyId;
    private Integer personId;
    private UserHandler userHandler;
    private boolean show;

    /** Creates a new instance of ReportAccessor */
    public ReportAccessor() {
	this.setStart(new Date());
	this.setEnd(new Date());
    }

    public Map<String, Double> getUserCompanyReport() {
	Connection conn = SessionHolder.currentSession().getSess().connection();

	String sql = Configuration.getSQL("sqls.clientUsage.3");

	Calendar c = Calendar.getInstance();
	c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
	c.set(Calendar.HOUR, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);

	Map<String, Double> map = new TreeMap<String, Double>();

	try {
	    PreparedStatement pst = conn.prepareStatement(sql);
	    pst.setInt(1, userHandler.getUser().getCompany());
	    pst.setTimestamp(2, new Timestamp(c.getTimeInMillis()));
	    pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

	    ResultSet rs = pst.executeQuery();
	    if (rs.next()) {
		map.put("worktime", rs.getDouble(1) + rs.getDouble(2));
	    }
	    rs.close();
	    pst.close();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return map;

    }

    public List<TicketChanges> getTicketChanges() {

	Connection conn = SessionHolder.currentSession().getSess().connection();

	String sql = Configuration.getSQL("sqls.lastChanges.5");

	List<TicketChanges> list = new ArrayList<TicketChanges>();

	try {
	    PreparedStatement pst = conn.prepareStatement(sql);
	    pst.setInt(1, userHandler.getUser().getId());
	    pst.setInt(2, userHandler.getUser().getId());
	    pst.setInt(3, userHandler.getUser().getId());
	    pst.setInt(4, userHandler.getUser().getId());
	    pst.setTimestamp(5, new Timestamp(userHandler.getLastLogin()
		    .getTime()));

	    ResultSet rs = pst.executeQuery();
	    while (rs.next()) {
		TicketChanges tc = new TicketChanges(rs);
		ChangeDetector cdt = ChangeDetector.getInstance();
		if (tc.getChangeNotes() != null
			&& !tc.getChangeNotes().equals("")) {
		    tc.setData(cdt.formatHtml(tc.getChangeNotes(), userHandler
			    .getUserLocale()));
		}
		if (tc.getNotes() != null && !tc.getNotes().equals("")) {
		    tc.setData(tc.getData() + "<br/>" + tc.getNotes());
		}
		list.add(tc);
	    }
	    rs.close();
	    pst.close();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return list;

    }

    public List<CompanyReport> getCompanyReport() {
	Connection conn = SessionHolder.currentSession().getSess().connection();

	ArrayList<CompanyReport> clist = new ArrayList<CompanyReport>();
	// System.out.println("Show: "+show);
	if (!show)
	    return clist;

	String sql = Configuration.getSQL("sqls.clientUsages.4");
	// System.out.println(sql);
	Timestamp start = null;
	Timestamp end = null;

	if (this.start != null)
	    start = new Timestamp(this.start.getTime());

	if (this.end != null)
	    end = new Timestamp(this.end.getTime());

	try {
	    PreparedStatement pst = conn.prepareStatement(sql);
	    pst.setTimestamp(1, start);
	    pst.setTimestamp(2, end);
	    if (this.companyId != null) {
		pst.setInt(3, companyId);
		pst.setInt(4, companyId);
	    } else {
		pst.setNull(3, Types.INTEGER);
		pst.setNull(4, Types.INTEGER);
	    }

	    ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
		clist.add(new CompanyReport(rs));
	    }

	    rs.close();
	    pst.close();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return clist;

    }

    public List<PersonReport> getWorkerReport() {
	Connection conn = SessionHolder.currentSession().getSess().connection();

	ArrayList<PersonReport> clist = new ArrayList<PersonReport>();
	// System.out.println("Show: "+show);
	if (!show)
	    return clist;

	String sql = Configuration.getSQL("sqls.clientWork.3");
	// System.out.println(sql);
	Timestamp start = null;
	Timestamp end = null;

	if (this.start != null)
	    start = new Timestamp(this.start.getTime());

	if (this.end != null)
	    end = new Timestamp(this.end.getTime());

	try {
	    PreparedStatement pst = conn.prepareStatement(sql);
	    pst.setTimestamp(1, start);
	    pst.setTimestamp(2, end);
	    if (this.personId != null) {
		pst.setInt(3, personId);

	    } else {
		pst.setNull(3, Types.INTEGER);

	    }

	    ResultSet rs = pst.executeQuery();

	    if (rs.next()) {
		clist.add(new PersonReport(rs));
	    }

	    rs.close();
	    pst.close();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return clist;

    }

    public String apply() {
	this.show = true;
	// System.out.println("Apply. Show: "+show);
	return StandardResults.STAY;
    }

    public String clear() {
	start = null;
	end = null;
	companyId = null;
	personId = null;
	this.show = false;
	return StandardResults.STAY;
    }

    public Date getStart() {
	return start;
    }

    public void setStart(Date start) {
	if (start != null) {
	    Calendar c = Calendar.getInstance();
	    c.setTime(start);
	    c.set(Calendar.DAY_OF_MONTH, c
		    .getActualMinimum(Calendar.DAY_OF_MONTH));
	    c.set(Calendar.HOUR, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    this.start = c.getTime();
	} else
	    this.start = start;
    }

    public Date getEnd() {
	return end;
    }

    public void setEnd(Date end) {
	if (end != null) {
	    Calendar c = Calendar.getInstance();
	    c.setTime(end);
	    c.set(Calendar.DAY_OF_MONTH, c
		    .getActualMaximum(Calendar.DAY_OF_MONTH));
	    c.set(Calendar.HOUR, 23);
	    c.set(Calendar.MINUTE, 59);
	    c.set(Calendar.SECOND, 59);
	    this.end = c.getTime();
	} else
	    this.end = end;
    }

    public Integer getCompanyId() {
	return companyId;
    }

    public void setCompanyId(Integer companyId) {
	this.companyId = companyId;
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

    public boolean isShow() {
	return show;
    }

    public void setShow(boolean show) {
	this.show = show;
    }

    public Integer getPersonId() {
	return personId;
    }

    public void setPersonId(Integer personId) {
	this.personId = personId;
    }

}
