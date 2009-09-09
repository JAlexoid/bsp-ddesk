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
package lt.bsprendimai.ddesk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.Status;
import lt.bsprendimai.ddesk.dao.TicketInfo;
import lt.bsprendimai.ddesk.dao.Timekeeping;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

/**
 * Ticket search funtionality.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings( { "deprecation", "unchecked" })
public class SearchParameters implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 7634389289051481925L;

    private TicketAccessor ti;

    private static final int PAGE_STEP = 15;

    private TicketFilter spe = new TicketFilter();

    private String ticketName;
    private String uniqueId;
    // private Double version;
    private Integer person;
    private Integer company;
    private Integer status;
    private Integer priority;
    private Integer assigned;
    private Integer project;
    private Integer module;

    boolean unvoidDate = false;
    Date forDate;

    int page = 0;

    int accepted = 0;
    int closed = 0;
    int chargeable = 0;

    boolean lastEmpty = true;

    /** Creates a new instance of SearchParameters */
    public SearchParameters() {
    }

    public List<TicketInfo> getTicketList() {
	// System.out.println("Getting list at page "+page);
	try {

	    Criteria crit = SessionHolder.currentSession().getSess()
		    .createCriteria(TicketInfo.class);

	    if (getSpe().sortEditDate != null) {
		if (getSpe().sortEditDate)
		    crit.addOrder(Order.asc("editDate"));
		else
		    crit.addOrder(Order.desc("editDate"));
	    } else if (getSpe().sortAsignee != null) {
		if (getSpe().sortAsignee)
		    crit.addOrder(Order.asc("asignee"));
		else
		    crit.addOrder(Order.desc("asignee"));
	    } else if (getSpe().sortStatus != null) {
		if (getSpe().sortStatus)
		    crit.addOrder(Order.asc("status"));
		else
		    crit.addOrder(Order.desc("status"));
	    } else if (getSpe().sortPriority != null) {
		if (getSpe().sortPriority)
		    crit.addOrder(Order.asc("priority"));
		else
		    crit.addOrder(Order.desc("priority"));
	    } else if (getSpe().sortCompany != null) {
		if (getSpe().sortCompany)
		    crit.addOrder(Order.asc("companyName"));
		else
		    crit.addOrder(Order.desc("companyName"));
	    } else if (getSpe().sortReportDate != null) {
		if (getSpe().sortReportDate)
		    crit.addOrder(Order.asc("reportDate"));
		else
		    crit.addOrder(Order.desc("reportDate"));
	    } else if (getSpe().sortName != null) {
		if (getSpe().sortName)
		    crit.addOrder(Order.asc("name"));
		else
		    crit.addOrder(Order.desc("name"));
	    } else if (getSpe().sortUniqueId != null) {
		if (getSpe().sortUniqueId)
		    crit.addOrder(Order.asc("uniqueId"));
		else
		    crit.addOrder(Order.desc("uniqueId"));
	    } else if (getSpe().sortPlaned != null) {
		if (getSpe().sortPlaned)
		    crit.addOrder(Order.asc("planedDate"));
		else
		    crit.addOrder(Order.desc("planedDate"));
	    } else if (getSpe().sortType != null) {
		if (getSpe().sortType)
		    crit.addOrder(Order.asc("type"));
		else
		    crit.addOrder(Order.desc("type"));
	    } else if (getSpe().sortVersion != null) {
		if (getSpe().sortVersion)
		    crit.addOrder(Order.asc("version"));
		else
		    crit.addOrder(Order.desc("version"));
	    } else {
		crit.addOrder(Order.desc("id"));
	    }

	    if (spe.status != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("status", spe.status));
	    else {
		if (spe.accepted > 0) {// for verbosity, if != to initial state
		    if (spe.accepted == 1)
			crit.add(Expression.ge("status", Status.ACCEPTED));
		    else
			crit.add(Expression.lt("status", Status.ACCEPTED));
		}

		if (spe.closed > 0) {// for verbosity, if != to initial state
		    if (spe.closed == 1)
			crit.add(Expression.isNotNull("dateClosed"));
		    else
			crit.add(Expression.isNull("dateClosed"));
		}
	    }

	    if (spe.chargeable != 0)// for verbosity, if != to initial state
		crit.add(Expression.eq("chargeable", spe.chargeable == 1));

	    if (spe.ticketName != null && !spe.ticketName.trim().equals(""))// for verbosity, if !=
									    // to initial state
		crit.add(Expression.like("name", spe.ticketName,
			MatchMode.ANYWHERE));

	    if (spe.uniqueId != null && !spe.uniqueId.trim().equals(""))// for verbosity, if != to
									// initial state
		crit.add(Expression.ilike("uniqueId", spe.uniqueId,
			MatchMode.START));

	    if (spe.person != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("person", spe.person));
	    if (spe.company != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("company", spe.company));

	    if (spe.priority != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("priority", spe.priority));
	    if (spe.assigned != null) {// for verbosity, if != to initial state
		if (spe.assigned >= 0)
		    crit.add(Expression.eq("assignedTo", spe.assigned));
		else {
		    if (spe.assigned == -2)
			crit.add(Expression.isNotNull("assignedTo"));
		    else
			crit.add(Expression.isNull("assignedTo"));

		}
	    }
	    if (spe.project != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("project", spe.project));
	    if (spe.module != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("module", spe.module));
	    if (spe.version != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("version", spe.version));
	    if (spe.type != null)// for verbosity, if != to initial state
		crit.add(Expression.eq("type", spe.type));

	    if (ti.getUserHandler().isPartner()) {
		Disjunction dis = Expression.disjunction();
		dis.add(Expression.eq("projectsCompany", ti.getUserHandler()
			.getUser().getCompany()));
		dis.add(Expression.eq("assignedTo", ti.getUserHandler()
			.getUser().getId()));
		dis.add(Expression.eq("reportBy", ti.getUserHandler().getUser()
			.getId()));
		crit.add(dis);
	    }

	    if (spe.forDate != null) {
		Calendar c = Calendar.getInstance();
		c.setTime(spe.forDate);

		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long time1 = c.getTimeInMillis();

		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		long time2 = c.getTimeInMillis();

		crit.add(Expression.between("planedDate", new Date(time1),
			new Date(time2)));

	    }

	    crit.setCacheMode(CacheMode.NORMAL);
	    crit.setMaxResults(PAGE_STEP);
	    crit.setFirstResult(page * PAGE_STEP);
	    List<TicketInfo> l = (List<TicketInfo>) crit.list();
	    this.lastEmpty = l.size() == 0 || l.size() < PAGE_STEP;
	    return l;
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", ti
		    .getUserHandler().getUserLocale());
	    ex.printStackTrace();
	    return TicketAccessor.getEMPTYInfoList();
	}
    }

    public List<Timekeeping> getTimerList() {

	try {
	    if (forDate == null)
		return new LinkedList<Timekeeping>();
	    Criteria crit = SessionHolder.currentSession().getSess()
		    .createCriteria(Timekeeping.class);
	    crit.addOrder(Order.desc("id"));

	    if (person == null) {
		person = ti.getUserHandler().getUser().getId();
	    }

	    crit.add(Expression.eq("person", person));

	    Calendar c = Calendar.getInstance();
	    c.setTime(forDate);

	    c.set(Calendar.HOUR, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    Date dt1 = c.getTime();

	    c.set(Calendar.HOUR, 23);
	    c.set(Calendar.MINUTE, 59);
	    c.set(Calendar.SECOND, 59);
	    Date dt2 = c.getTime();

	    Conjunction cr = Expression.conjunction();
	    cr.add(Expression.le("start", dt1));
	    cr.add(Expression.ge("finish", dt2));
	    Disjunction dj = Expression.disjunction();
	    dj.add(cr);
	    dj.add(Expression.between("start", dt1, dt2));
	    dj.add(Expression.between("finish", dt1, dt2));
	    crit.add(dj);

	    List<Timekeeping> l = (List<Timekeeping>) crit.list();
	    return l;
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", ti
		    .getUserHandler().getUserLocale());
	    ex.printStackTrace();
	    return new LinkedList<Timekeeping>();
	}
    }

    public List<TicketInfo> getLateTicketList() {
	if (spe.assigned == null)
	    return TicketAccessor.getEMPTYInfoList();
	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.dateClosed IS NULL AND t.assignedTo = ? AND (t.planedDate <= ? OR t.planedDate IS NULL) ORDER BY t.planedDate DESC, t.priority ASC ")
		    .setInteger(0, spe.assigned).setTimestamp(1,
			    new Date(System.currentTimeMillis() - 43200000l))
		    .list();
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", ti
		    .getUserHandler().getUserLocale());
	    ex.printStackTrace();
	    return TicketAccessor.getEMPTYInfoList();
	}
    }

    // Sortings

    public String sortEditDateAction() {
	Boolean copy = getSpe().sortEditDate;
	clearSort();
	if (copy != null) {
	    getSpe().sortEditDate = !copy;
	} else {
	    getSpe().sortEditDate = true;
	}
	return StandardResults.STAY;
    }

    public String sortAsigneeAction() {
	Boolean copy = getSpe().sortAsignee;
	clearSort();
	if (copy != null) {
	    getSpe().sortAsignee = !copy;
	} else {
	    getSpe().sortAsignee = true;
	}
	return StandardResults.STAY;
    }

    public String sortStatusAction() {
	Boolean copy = getSpe().sortStatus;
	clearSort();
	if (copy != null) {
	    getSpe().sortStatus = !copy;
	} else {
	    getSpe().sortStatus = true;
	}
	return StandardResults.STAY;
    }

    public String sortVersionAction() {
	Boolean copy = getSpe().sortVersion;
	clearSort();
	if (copy != null) {
	    getSpe().sortVersion = !copy;
	} else {
	    getSpe().sortVersion = true;
	}
	return StandardResults.STAY;
    }

    public String sortPriorityAction() {
	Boolean copy = getSpe().sortPriority;
	clearSort();
	if (copy != null) {
	    getSpe().sortPriority = !copy;
	} else {
	    getSpe().sortPriority = true;
	}
	return StandardResults.STAY;
    }

    public String sortCompanyAction() {
	Boolean copy = getSpe().sortCompany;
	clearSort();
	if (copy != null) {
	    getSpe().sortCompany = !copy;
	} else {
	    getSpe().sortCompany = true;
	}
	return StandardResults.STAY;
    }

    public String sortReportDateAction() {
	Boolean copy = getSpe().sortReportDate;
	clearSort();
	if (copy != null) {
	    getSpe().sortReportDate = !copy;
	} else {
	    getSpe().sortReportDate = true;
	}
	return StandardResults.STAY;
    }

    public String sortNameAction() {
	Boolean copy = getSpe().sortName;
	clearSort();
	if (copy != null) {
	    getSpe().sortName = !copy;
	} else {
	    getSpe().sortName = true;
	}
	return StandardResults.STAY;
    }

    public String sortUniqueIdAction() {
	Boolean copy = getSpe().sortUniqueId;
	clearSort();
	if (copy != null) {
	    getSpe().sortUniqueId = !copy;
	} else {
	    getSpe().sortUniqueId = true;
	}
	return StandardResults.STAY;
    }

    public String sortPlanedAction() {
	Boolean copy = getSpe().sortPlaned;
	clearSort();
	if (copy != null) {
	    getSpe().sortPlaned = !copy;
	} else {
	    getSpe().sortPlaned = true;
	}
	return StandardResults.STAY;
    }

    public String sortTypeAction() {
	Boolean copy = getSpe().sortType;
	clearSort();
	if (copy != null) {
	    getSpe().sortType = !copy;
	} else {
	    getSpe().sortType = true;
	}
	return StandardResults.STAY;
    }

    //
    // public List<SelectItem> getSearchSubjectSelect() {
    // List<SelectItem> ll = new LinkedList<SelectItem>();
    //
    // SelectItem si;
    //
    // si = new SelectItem();
    // si.setValue(new Integer(0));
    // si.setLabel("Admin");
    // ll.add(si);
    //
    // si = new SelectItem();
    // si.setValue(new Integer(1));
    // si.setLabel("Kiti");
    // ll.add(si);
    //
    // return ll;
    //
    // }

    public String getTicketName() {
	return ticketName;
    }

    public void setTicketName(String ticketName) {
	this.ticketName = ticketName;
    }

    public String getUniqueId() {
	return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
	this.uniqueId = uniqueId;
    }

    public Integer getPerson() {
	return person;
    }

    public void setPerson(Integer person) {
	this.person = person;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public String apply() {
	page = 0;
	return StandardResults.STAY;
    }

    public void clearSort() {

	getSpe().sortEditDate = null;
	getSpe().sortAsignee = null;
	getSpe().sortStatus = null;
	getSpe().sortPriority = null;
	getSpe().sortCompany = null;
	getSpe().sortReportDate = null;
	getSpe().sortName = null;
	getSpe().sortUniqueId = null;
	getSpe().sortVersion = null;
	getSpe().sortType = null;

    }

    public String clear() {
	spe.ticketName = null;
	spe.uniqueId = null;
	spe.person = null;

	spe.company = null;
	spe.accepted = 0;
	spe.closed = 0;
	spe.chargeable = 0;
	page = 0;
	spe.assigned = null;
	spe.version = null;
	spe.status = null;

	unvoidDate = false;
	spe.forDate = null;
	spe.project = null;
	spe.module = null;

	person = null;
	forDate = null;

	clearSort();

	return StandardResults.VIEW;
    }

    public String next() {
	if (!this.lastEmpty)
	    page++;
	// System.out.println("Page inc "+page);
	return StandardResults.STAY;
    }

    public String previous() {
	page--;
	if (page < 0)
	    page = 0;
	// System.out.println("Page dec "+page);
	return StandardResults.STAY;
    }

    public int getPage() {
	return page;
    }

    public void setPage(int page) {
	// System.out.println("Set page "+page);
	this.page = page;
    }

    public TicketAccessor getTi() {
	return ti;
    }

    public void setTi(TicketAccessor ti) {
	if (ti != null)
	    ti.setSearchParameters(this);
	this.ti = ti;
	if (ti.getUserHandler() != null
		&& ti.getUserHandler().getFilterSort() != null)
	    setSpe(ti.getUserHandler().getFilterSort());
    }

    public List<SelectItem> getPersons() {
	List<Person> la = new ArrayList<Person>(0);
	try {
	    la = (List<Person>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Person.class.getName()
				    + " t WHERE t.company = ? ORDER BY t.id ")
		    .setInteger(0, spe.company).list();
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", ti
		    .getUserHandler().getUserLocale());
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);
	for (Person c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getName());
	    ll.add(si);
	}
	return ll;
    }

    public List<SelectItem> getModules() {
	if (spe.project != null) {

	    try {
		return ProjectAccessor.getProjectModules(spe.project);
	    } catch (Exception ex) {
		ex.printStackTrace();
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort", ti
			.getUserHandler().getUserLocale());
		return new LinkedList<SelectItem>();
	    }
	} else {
	    List<SelectItem> ll = new LinkedList<SelectItem>();
	    SelectItem si = new SelectItem();
	    si.setValue("");
	    si.setLabel("");
	    ll.add(si);
	    return ll;
	}
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getPriority() {
	return priority;
    }

    public void setPriority(Integer priority) {
	this.priority = priority;
    }

    public Integer getAssigned() {
	return assigned;
    }

    public void setAssigned(Integer assigned) {
	this.assigned = assigned;
    }

    public boolean isLastEmpty() {
	return lastEmpty;
    }

    public void setLastEmpty(boolean lastEmpty) {
	this.lastEmpty = lastEmpty;
    }

    public Date getForDate() {
	return forDate;
    }

    public void setForDate(Date forDate) {
	this.forDate = forDate;
    }

    public boolean isUnvoidDate() {
	return unvoidDate;
    }

    public void setUnvoidDate(boolean unvoidDate) {
	this.unvoidDate = unvoidDate;
    }

    public Boolean getSortEditDate() {
	return getSpe().sortEditDate;
    }

    public void setSortEditDate(Boolean sortEditDate) {
	getSpe().sortEditDate = sortEditDate;
    }

    public Boolean getSortAsignee() {
	return getSpe().sortAsignee;
    }

    public void setSortAsignee(Boolean sortAsignee) {
	getSpe().sortAsignee = sortAsignee;
    }

    public Boolean getSortStatus() {
	return getSpe().sortStatus;
    }

    public void setSortStatus(Boolean sortStatus) {
	getSpe().sortStatus = sortStatus;
    }

    public Boolean getSortPriority() {
	return getSpe().sortPriority;
    }

    public void setSortPriority(Boolean sortPriority) {
	getSpe().sortPriority = sortPriority;
    }

    public Boolean getSortCompany() {
	return getSpe().sortCompany;
    }

    public void setSortCompany(Boolean sortCompany) {
	getSpe().sortCompany = sortCompany;
    }

    public Boolean getSortReportDate() {
	return getSpe().sortReportDate;
    }

    public void setSortReportDate(Boolean sortReportDate) {
	getSpe().sortReportDate = sortReportDate;
    }

    public Boolean getSortName() {
	return getSpe().sortName;
    }

    public void setSortName(Boolean sortName) {
	getSpe().sortName = sortName;
    }

    public Boolean getSortUniqueId() {
	return getSpe().sortUniqueId;
    }

    public void setSortUniqueId(Boolean sortUniqueId) {
	getSpe().sortUniqueId = sortUniqueId;
    }

    public Boolean getSortPlaned() {
	return getSpe().sortPlaned;
    }

    public void setSortPlaned(Boolean sortPlaned) {
	getSpe().sortPlaned = sortPlaned;
    }

    public Integer getProject() {
	return project;
    }

    public void setProject(Integer project) {
	this.project = project;
    }

    public Integer getModule() {
	return module;
    }

    public void setModule(Integer module) {
	this.module = module;
    }

    public int getAccepted() {
	return accepted;
    }

    public void setAccepted(int accepted) {
	this.accepted = accepted;
    }

    public int getClosed() {
	return closed;
    }

    public void setClosed(int closed) {
	this.closed = closed;
    }

    public int getChargeable() {
	return chargeable;
    }

    public void setChargeable(int chargeable) {
	this.chargeable = chargeable;
    }

    public Double getVersion() {
	return spe.version;
    }

    public void setVersion(Double version) {
	spe.version = version;
    }

    public TicketFilter getSpe() {
	return spe;
    }

    public void setSpe(TicketFilter spe) {
	this.spe = spe;
    }

}
