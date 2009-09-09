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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.model.SelectItem;

import lt.bsprendimai.ddesk.dao.EventType;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.Project;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.Status;
import lt.bsprendimai.ddesk.dao.Ticket;
import lt.bsprendimai.ddesk.dao.TicketHistory;
import lt.bsprendimai.ddesk.dao.TicketInfo;
import lt.bsprendimai.ddesk.dao.Timekeeping;
import lt.bsprendimai.ddesk.textmarks.TextSuggest;

import org.hibernate.HibernateException;

/**
 * Ticket operations related functionality.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class TicketAccessor implements Serializable {

    private static final ArrayList<TicketInfo> EMPTYInfoList = new ArrayList<TicketInfo>(
	    0);

    private UserHandler userHandler;
    private Ticket selected = new Ticket();
    private TicketInfo selectedInfo = new TicketInfo();
    private Timekeeping timer = new Timekeeping();
    private ClientAccessor clientAccessor;
    private ProjectAccessor projects;

    private transient ChangeDetector cdt = ChangeDetector.getInstance();
    private transient MessageHandler mfg = new MessageHandler(this);
    private transient UIParameter ticketIdParameter = new UIParameter();
    private transient UIParameter timerIdParameter = new UIParameter();
    private transient TextSuggest ts = null;

    private String comment = "";
    private String description = "";

    private boolean close = false;
    private boolean accept = false;

    private boolean voidPlandedDate = false;
    private boolean voidActualDate = false;

    private boolean informClient = false;

    private transient SearchParameters searchParameters = new SearchParameters();

    private Integer qProject;
    private Integer qPerson;

    // private Map<String, String> freeParameters = new HashMap<String, String>();

    private Integer id;

    /** Creates a new instance of TicketAccessor */
    public TicketAccessor() {
	// System.out.println("New TicketAccessor");
    }

    public Ticket getSelected() {
	// if(userHandler.isLoggedIn()){
	return selected;
	// }else{
	// return new Ticket();
	// }
    }

    public void setSelected(Ticket selected) {
	this.selected = selected;
    }

    public String getDeselect() {
	setId(-1);
	this.id = null;
	return "";
    }

    public String getReselect() {
	this.setInformClient(false);
	if (getId() == null) {
	    setSelected(new Ticket());
	    return "";
	}

	// if(userHandler.isLoggedIn()){
	try {
	    Ticket li = (Ticket) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    " FROM "
				    + Ticket.class.getName()
				    + " t WHERE t.id = ? AND ((t.company = ?  and t.chargeable = true ) OR ? = 0) ")
		    .setInteger(0, getId()).setInteger(1,
			    getUserHandler().getUser().getCompany())
		    .setInteger(2, getUserHandler().getUser().getCompany())
		    .uniqueResult();

	    if (li != null) {

		this.setSelected(li);

		if (li.getPlanedDate() != null) {
		    this.setVoidPlandedDate(true);
		}

		if (li.getActualDate() != null) {
		    this.setVoidActualDate(true);
		}

		setSelectedInfo(getId());
		getSelected().postLoad();

		// iterateAndRestore(selected);
		// System.out.println("Element "+id+" found "+selected.getName());
	    } else {
		// System.out.println("Element "+id+" not found");
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    this.setId(null);
	}
	return "";
    }

    public void autofill(String text) {
	if (getTs() == null) {
	    setTs(new TextSuggest(this, getUserHandler(), getClientAccessor(),
		    getProjects()));
	    getTs().setAllowCycles(false);
	    getTs().setFillOnly(true);
	}

	getTs().extract(text);
    }

    public String addQBug() {
	getSelected().setName(null);
	getSelected().setDescription(null);
	autofill(":new\n" + getDescription());
	getSelected().setReportDate(new Date());
	getSelected().setStatus(Status.REPORTED);
	getSelected().setType(1);
	getSelected().setChargeable(true);
	getSelected().setSeverity(0);
	if (getSelected().getAssignedTo() == null)
	    getSelected().setAssignedTo(qPerson);
	if (getSelected().getProject() == null
		|| getSelected().getProject() == 0) {
	    if (qProject != null)
		getSelected().setProject(qProject);
	    else
		getSelected().setProject(0);
	}
	this.setDescription("");
	return add();
    }

    public String addQFeature() {
	getSelected().setName(null);
	getSelected().setDescription(null);
	autofill(":new\n" + getDescription());
	getSelected().setReportDate(new Date());
	getSelected().setStatus(Status.REPORTED);
	getSelected().setType(2);
	getSelected().setChargeable(true);
	getSelected().setSeverity(0);
	if (getSelected().getAssignedTo() == null)
	    getSelected().setAssignedTo(qPerson);
	if (getSelected().getProject() == null
		|| getSelected().getProject() == 0) {
	    if (qProject != null)
		getSelected().setProject(qProject);
	    else
		getSelected().setProject(0);
	}
	this.setDescription("");
	return add();
    }

    public String accept() {
	if (getSelected() != null) {
	    getSelected().setEditBy(this.getUserHandler().getUser().getId());
	    getSelected().setEditDate(new Date());
	    // selected.setAssignedTo(userHandler.getUser().getId());
	    // selected.setAssignedBy(userHandler.getUser().getId());
	    getSelected().setAssignedDate(new Date());
	    getSelected().setStatus(Status.ACCEPTED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    TicketHistory ti = getCdt().preAccept(getSelected());
	    if (!getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
		this.setInformClient(false);
	    }
	    String ret = getSelected().update();
	    if (ret.equals(StandardResults.SUCCESS)) {
		try {
		    setSelectedInfo(getSelected().getId());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}

		ti.add();
		getMfg().sendMessagesAccepted(ti);
		return StandardResults.TICKET;
	    } else {
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort",
			userHandler.getUserLocale());
	    }
	    return ret;
	}
	return StandardResults.FAIL;
    }

    public String reopenTask() {
	if (getSelected() != null) {
	    getSelected().setDateClosed(null);
	    getSelected().setClosedBy(null);
	    getSelected().setEditDate(new Date());
	    getSelected().setEditBy(getUserHandler().getUser().getId());
	    getSelected().setStatus(Status.ACCEPTED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    TicketHistory ti = getCdt().preReopen(getSelected());
	    if (!getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
		this.setInformClient(false);
	    }

	    String ret = getSelected().update();
	    if (ret.equals(StandardResults.SUCCESS)) {
		try {
		    setSelectedInfo(getSelected().getId());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		ti.add();
		getMfg().sendMessagesReopened(ti);
		return StandardResults.TICKET;
	    } else {
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort",
			userHandler.getUserLocale());
	    }
	    return ret;
	}
	return StandardResults.FAIL;
    }

    public String closeTask() {
	if (getSelected() != null) {
	    getSelected().setDateClosed(new Date());
	    getSelected().setClosedBy(getUserHandler().getUser().getId());
	    getSelected().setEditDate(new Date());
	    getSelected().setEditBy(getUserHandler().getUser().getId());
	    getSelected().setStatus(Status.CLOSED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    iterateAndAdd(getSelected());
	    TicketHistory ti = getCdt().preClose(getSelected());
	    if (!getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
		this.setInformClient(false);
	    }

	    String ret = getSelected().update();
	    if (ret.equals(StandardResults.SUCCESS)) {
		try {
		    setSelectedInfo(getSelected().getId());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		ti.add();
		getMfg().sendMessagesClosed(ti);
		return StandardResults.TICKET;
	    } else {
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort",
			userHandler.getUserLocale());
	    }
	    return ret;
	}
	return StandardResults.FAIL;
    }

    public String addExternal() {
	if (getSelected() != null) {
	    if (getSelected().getCompany() == null)
		getSelected().setCompany(
			getUserHandler().getUser().getCompany());
	    getSelected().setReportBy(getUserHandler().getUser().getId());
	    getSelected().setUniqueId("");
	    getSelected().setStatus(Status.REPORTED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    if (getSelected().getAssignedTo() == null)
		getSelected().setAssignedTo(qPerson);
	    if (getSelected().getProject() == null
		    || getSelected().getProject() == 0) {
		if (qProject != null)
		    getSelected().setProject(qProject);
		else
		    getSelected().setProject(0);
	    }
	    return addInternal();
	}
	return StandardResults.FAIL;
    }

    public String addIndirect() {
	if (getSelected() != null) {
	    if (getSelected().getCompany() == null)
		getSelected().setCompany(getClientAccessor().getCompanyId());

	    getSelected().setReportBy(getUserHandler().getUser().getId());
	    getSelected().setUniqueId("");
	    getSelected().setStatus(Status.REPORTED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    return addInternal();
	}
	return StandardResults.FAIL;
    }

    public String add() {
	if (getSelected() != null) {
	    getSelected().setUniqueId("");
	    getSelected().setCompany(getUserHandler().getUser().getCompany());
	    getSelected().setPerson(getUserHandler().getUser().getId());
	    getSelected().setReportBy(getUserHandler().getUser().getId());
	    getSelected().setReportDate(new Date());
	    getSelected().setStatus(Status.REPORTED);
	    if (getSelected().getType() == null)
		getSelected().setType(0);
	    return addInternal();
	}
	return StandardResults.FAIL;
    }

    private String addInternal() {
	this.id = null;
	// System.out.println("Adding");
	getSelected().setEditBy(this.getUserHandler().getUser().getId());
	getSelected().setEditDate(new Date());
	if (getSelected().getReportDate() == null)
	    getSelected().setReportDate(new Date());
	String ret = getSelected().add();
	if (ret.equals(StandardResults.SUCCESS)) {
	    this.setId(getSelected().getId());
	    try {
		setSelectedInfo(getSelected().getId());
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	    TicketHistory ti = getCdt().copyTicket(getSelected());
	    if (getComment() != null && !getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
		this.setInformClient(false);
	    }
	    ti.add();
	    getMfg().sendMessagesAdded();
	    setSelected(new Ticket());
	    return StandardResults.LIST;
	} else {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	}
	return ret;
    }

    public void resetInternal() {
	setSelected(new Ticket());
	getSelected().setStatus(0);
	getSelected().setProject(0);
	getSelected().setModule(0);
	getSelected().setSeverity(0);
	getSelected().setType(EventType.OTHER);
    }

    public String updateParamed() {
	if (this.ticketIdParameter.getValue() != null) {
	    try {
		this
			.setId(new Integer(ticketIdParameter.getValue()
				.toString()));
		this.selected.setAssignedTo(qPerson);
		qPerson = null;
	    } catch (Exception ex) {
	    }
	    String ret = update();
	    if (ret.equals(StandardResults.TICKET))
		return StandardResults.STAY;
	    else
		return ret;
	} else
	    return StandardResults.FAIL;

    }

    public String update() {
	if (getSelected() != null) {
	    // System.out.println("Update called "+getSelected().isChargeable());

	    // if(!isVoidPlandedDate()){
	    // getSelected().setPlanedDate(null);
	    // }
	    // if(!isVoidActualDate() || !isClose()){
	    // getSelected().setActualDate(null);
	    // }

	    getSelected().setEditBy(this.getUserHandler().getUser().getId());
	    getSelected().setEditDate(new Date());

	    if (isAccept()) {
		getSelected().setStatus(Status.ACCEPTED);
		if (!isClose())
		    return this.accept();
	    }

	    if (isClose()) {
		getSelected().setStatus(Status.CLOSED);
		getSelected().setClosedBy(getUserHandler().getUser().getId());
		getSelected().setDateClosed(getSelected().getEditDate());
		return this.closeTask();
	    }

	    // iterateAndAdd(selected);

	    TicketHistory ti = getCdt().preUpdate(getSelected());
	    if (ti == null)
		return StandardResults.TICKET;
	    if (!getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
		this.setInformClient(false);
	    }

	    String ret = getSelected().update();

	    if (ret.equals(StandardResults.SUCCESS)) {
		try {
		    setSelectedInfo(getSelected().getId());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		ti.add();
		getMfg().sendMessagesChange(ti);
		setSelected(new Ticket());

		return StandardResults.TICKET;
	    } else {
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort",
			userHandler.getUserLocale());
	    }
	    return ret;
	}
	return StandardResults.FAIL;
    }

    private void iterateAndAdd(Ticket tt) {
	// try{
	// StringBuilder sb = new StringBuilder();
	// for(int i = 1; i <= 5; i++){
	// String name = freeParameters.get("serviceName"+i);
	// String time = freeParameters.get("serviceTime"+i);
	//
	// sb.append(name);
	// sb.append(";--;");
	// sb.append(time);
	// sb.append("\n");
	// }
	// tt.setJobsDone(sb.toString());
	// } catch (Exception ee){}
	// freeParameters.clear();
	tt.preSave();
    }

    // private void iterateAndRestore(Ticket tt){
    // try{
    // String s[] = tt.getJobsDone().split("\\n");
    //
    // for(int i = 0; i < s.length; i++){
    // String pair[] = s[i].split(";--;");
    // freeParameters.put("serviceName"+(i+1), pair[0]);
    // freeParameters.put("serviceTime"+(i+1), pair[1]);
    // }
    //
    // } catch (Exception ee){}
    // freeParameters.clear();
    // tt.postLoad();
    // }

    public String comment() {
	if (getSelected() != null
		|| (getComment() != null && !getComment().equals(""))) {
	    if (getUserHandler().getUser().getCompany() != 0)
		this.setInformClient(true);

	    getSelected().setEditBy(this.getUserHandler().getUser().getId());
	    getSelected().setEditDate(new Date());
	    TicketHistory ti = getCdt().preComment(getSelected(), getComment(),
		    isInformClient());
	    ti.setNotes(getComment());
	    if (!getComment().equals("")) {
		ti.setNotes(getComment());
		setComment("");
	    } else {
		return StandardResults.STAY;
	    }

	    String ret = getSelected().update();
	    if (ret.equals(StandardResults.SUCCESS)) {
		try {
		    setSelectedInfo(getSelected().getId());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		ti.add();
		if (isInformClient()) {
		    getMfg().sendMessagesComment(ti);
		}

		setSelected(new Ticket());
		SessionHolder.currentSession().getSess().flush();
		this.setInformClient(false);
		return StandardResults.STAY;
	    } else {
		SessionHolder.endSession();
		UIMessenger.addFatalKeyMessage("error.transaction.abort",
			userHandler.getUserLocale());

	    }
	    this.setInformClient(false);
	    return ret;
	}
	this.setInformClient(false);
	return StandardResults.FAIL;
    }

    public String deleteTicket() {
	try {
	    Ticket cmp = (Ticket) SessionHolder.currentSession().getSess().get(
		    Ticket.class,
		    (Serializable) getTicketIdParameter().getValue());
	    this.getTicketIdParameter().setValue(null);
	    SessionHolder.currentSession().getSess().delete(cmp);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.LIST;
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    return StandardResults.FAIL;
	}
    }

    // public String deleteCTicket(){
    // try {
    // SessionHolder.currentSession().getSess().delete(selected);
    // SessionHolder.currentSession().getSess().flush();
    // return StandartResults.SUCCESS;
    // } catch (HibernateException ex) {
    // ex.printStackTrace();
    //
    // return StandartResults.FAIL;
    // }
    // }

    public Integer getTotalCount() {
	try {
	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.DATE, 1);
	    c.set(Calendar.HOUR, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    Number l = (Number) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "SELECT COUNT(*) from "
				    + Ticket.class.getName()
				    + " t WHERE t.company = ? and t.reportDate > ? and t.chargeable = true ")
		    .setInteger(0, getUserHandler().getUser().getCompany())
		    .setTimestamp(1, c.getTime()).list().iterator().next();

	    return l.intValue();
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    return 0;
	}
    }

    public List<String> getFullTicketHistory() {
	try {
	    Iterator l = SessionHolder.currentSession().getSess().createQuery(
		    "SELECT t.changeNotes, t.notes from "
			    + TicketHistory.class.getName()
			    + " t WHERE t.ticket = ?  ORDER BY t.id ASC ")
		    .setInteger(0, getId()).list().iterator();
	    // System.out.println("Reading history"+id);

	    List<String> lc = new ArrayList<String>();

	    while (l.hasNext()) {
		Object[] o = (Object[]) l.next();
		// System.out.println("[]Reading history"+o);
		if (o != null) {
		    // System.out.println("[]Reading history "+Arrays.toString(o));
		    if (o[0] != null || o[1] != null) {
			String s = o[0] != null ? o[0].toString() : "";
			// System.out.println("A:"+s);
			s = o[1] != null ? s + "%dt%" + o[1].toString()
				+ "%td%" : s;
			// System.out.println("B:"+s);
			s = getCdt().formatHtml(s,
				getUserHandler().getUserLocale());
			// System.out.println("C:"+s);
			lc.add(s);
		    }
		}
	    }

	    return lc;
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    UIMessenger.addFatalKeyMessage("error.transaction.abort", userHandler
		    .getUserLocale());
	    ex.printStackTrace();
	    return new ArrayList<String>(0);
	}
    }

    public List<String> getClientTicketHistory() {
	try {
	    Iterator l = SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "SELECT t.changeNotesPublic from "
				    + TicketHistory.class.getName()
				    + " t WHERE t.ticket = ? AND t.notesPublic IS NOT NULL ORDER BY t.id DESC ")
		    .setInteger(0, getId()).list().iterator();

	    List<String> lc = new ArrayList<String>();

	    while (l.hasNext()) {
		Object o = l.next();
		if (o != null) {
		    String s = o.toString();
		    lc.add(getCdt().formatHtml(s,
			    getUserHandler().getUserLocale()));
		}
	    }

	    return lc;
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return new ArrayList<String>(0);
	}
    }

    public List<TicketInfo> getFullTicketList() {
	try {
	    if (!userHandler.isPartner()) {
		return (List<TicketInfo>) SessionHolder.currentSession()
			.getSess().createQuery(
				"from " + TicketInfo.class.getName()
					+ " t ORDER BY t.id ").list();
	    } else {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.projectsCompany = ? OR t.assignedTo = ? OR t.reportBy = ? ORDER BY t.id ")
			.setInteger(0, userHandler.getUser().getCompany())
			.setInteger(1, userHandler.getUser().getId())
			.setInteger(2, userHandler.getUser().getId()).list();
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getTicketList() {
	try {
	    if (!userHandler.isPartner()) {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.company = ? and t.chargeable = true ORDER BY t.id DESC ")
			.setInteger(0, getUserHandler().getUser().getCompany())
			.list();
	    } else {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.company = ? and t.chargeable = true AND (t.projectsCompany = ? OR t.assignedTo = ? OR t.reportBy = ?) ORDER BY t.id DESC ")
			.setInteger(0, getUserHandler().getUser().getCompany())
			.setInteger(1, userHandler.getUser().getCompany())
			.setInteger(2, userHandler.getUser().getId())
			.setInteger(3, userHandler.getUser().getId()).list();
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getTicketListPerson() {
	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.assignedTo = ? AND t.dateClosed IS NULL ORDER BY t.id DESC LIMIT 50 ")
		    .setInteger(0, getUserHandler().getUser().getId()).list();
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getActiveTicketList() {
	try {
	    if (!userHandler.isPartner()) {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.dateClosed IS NULL ORDER BY t.id ")
			.list();
	    } else {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.dateClosed IS NULL AND (t.projectsCompany = ? OR t.assignedTo = ? OR t.reportBy = ?) ORDER BY t.id ")
			.setInteger(0, userHandler.getUser().getCompany())
			.setInteger(1, userHandler.getUser().getId())
			.setInteger(2, userHandler.getUser().getId()).list();
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getTopTicketList() {
	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.company = ? AND t.dateClosed IS NULL and t.chargeable = true ORDER BY t.id DESC, t.status ASC, t.priority ASC LIMIT 5 ")
		    .setInteger(0, getUserHandler().getUser().getCompany())
		    .setMaxResults(5).list();
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getFreeTicketList() {
	try {
	    if (!userHandler.isPartner()) {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.dateClosed IS NULL AND t.assignedTo IS NULL ORDER BY t.id DESC, t.status ASC, t.priority ASC LIMIT 8 ")
			.setMaxResults(8).list();
	    } else {
		return (List<TicketInfo>) SessionHolder
			.currentSession()
			.getSess()
			.createQuery(
				"from "
					+ TicketInfo.class.getName()
					+ " t WHERE t.dateClosed IS NULL AND t.assignedTo IS NULL AND (t.projectsCompany = ? OR t.reportBy = ?) ORDER BY t.id DESC, t.status ASC, t.priority ASC LIMIT 8 ")
			.setMaxResults(8).setInteger(0,
				userHandler.getUser().getCompany()).setInteger(
				1, userHandler.getUser().getId()).list();
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public boolean isAnyLate() {
	try {
	    Number l = (Number) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "SELECT COUNT(*) from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.dateClosed IS NULL AND t.assignedTo = ? AND t.planedDate <= ? ")
		    .setInteger(0, getUserHandler().getUser().getId())
		    .setTimestamp(1,
			    new Date(System.currentTimeMillis() - 43200000l))// -12h
		    .list().iterator().next();
	    return l.intValue() > 0;
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return false;
	}
    }

    public List<TicketInfo> getLateTicketList() {
	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.dateClosed IS NULL AND t.assignedTo = ? AND (t.planedDate <= ? OR t.planedDate IS NULL) ORDER BY t.planedDate DESC, t.priority ASC ")
		    .setInteger(0, getUserHandler().getUser().getId())
		    .setTimestamp(1,
			    new Date(System.currentTimeMillis() - 43200000l))// -12h
		    .list();
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public List<TicketInfo> getTodaysTicketList() {
	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.dateClosed IS NULL AND t.assignedTo = ? AND t.planedDate BETWEEN ? AND ? ORDER BY t.planedDate DESC, t.priority ASC ")
		    .setInteger(0, getUserHandler().getUser().getId())
		    .setTimestamp(1,
			    new Date(System.currentTimeMillis() - 43200000l))// -12h
		    .setTimestamp(2,
			    new Date(System.currentTimeMillis() + 43200000l))// +12h
		    .list();
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return getEMPTYInfoList();
	}
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	if (id != null && id.equals(this.id))
	    return;
	this.setVoidPlandedDate(false);
	this.setVoidActualDate(false);
	if (id == null || id < 0) {
	    setSelected(new Ticket());
	    return;
	} else
	    this.id = id;

	// if(userHandler.isLoggedIn()){
	try {
	    Ticket li = (Ticket) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    " FROM "
				    + Ticket.class.getName()
				    + " t WHERE t.id = ? AND ((t.company = ?  and t.chargeable = true ) OR ? = 0) ")
		    .setInteger(0, id).setInteger(1,
			    getUserHandler().getUser().getCompany())
		    .setInteger(2, getUserHandler().getUser().getCompany())
		    .uniqueResult();

	    if (li != null) {

		this.setSelected(li);

		if (li.getPlanedDate() != null) {
		    this.setVoidPlandedDate(true);
		}

		if (li.getActualDate() != null) {
		    this.setVoidActualDate(true);
		}

		setSelectedInfo(id);
		getSelected().postLoad();
		// iterateAndRestore(selected);
		// System.out.println("Element "+id+" found "+selected.getName());
	    } else {
		// System.out.println("Element "+id+" not found");
	    }
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    this.id = null;
	}
	// } else {
	// System.out.println("Not logged in");
	// }

    }

    public void setSelectedInfo(int id) throws Exception {
	try {
	    SessionHolder.currentSession().getSess().evict(getSelectedInfo());
	} catch (Exception excasdasd) {
	}
	TicketInfo lii;
	if (!userHandler.isPartner()) {
	    lii = (TicketInfo) SessionHolder.currentSession().getSess()
		    .createQuery(
			    " FROM " + TicketInfo.class.getName()
				    + " t WHERE t.id = ?").setInteger(0, id)
		    .uniqueResult();
	} else {
	    lii = (TicketInfo) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    " FROM "
				    + TicketInfo.class.getName()
				    + " t WHERE t.id = ? AND (t.projectsCompany = ? OR t.assignedTo = ? OR t.reportBy = ?)")
		    .setInteger(0, id).setInteger(1,
			    userHandler.getUser().getCompany()).setInteger(2,
			    userHandler.getUser().getId()).setInteger(3,
			    userHandler.getUser().getId()).uniqueResult();
	}
	if (lii == null)
	    throw new Exception("No acceptible tickets");
	this.setSelectedInfo(lii);
	lii.postLoad();
    }

    public TicketInfo getSelectedInfo() {
	return selectedInfo;
    }

    public void setSelectedInfo(TicketInfo selectedInfo) {
	this.selectedInfo = selectedInfo;
    }

    public String getSafe(String s) {
	if (s != null)
	    return s;
	else
	    return "";
    }

    public ClientAccessor getClientAccessor() {
	return clientAccessor;
    }

    public void setClientAccessor(ClientAccessor clientAccessor) {
	this.clientAccessor = clientAccessor;
    }

    public UIParameter getTicketIdParameter() {
	return ticketIdParameter;
    }

    public void setTicketIdParameter(UIParameter ticketIdParameter) {
	this.ticketIdParameter = ticketIdParameter;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public boolean isClose() {
	return close;
    }

    public void setClose(boolean close) {
	this.close = close;
    }

    public boolean isAccept() {
	return accept;
    }

    public void setAccept(boolean accept) {
	this.accept = accept;
    }

    public boolean isInformClient() {
	return informClient;
    }

    public void setInformClient(boolean informClient) {
	this.informClient = informClient;
    }

    public boolean isVoidPlandedDate() {
	return voidPlandedDate;
    }

    public void setVoidPlandedDate(boolean voidPlandedDate) {
	this.voidPlandedDate = voidPlandedDate;
    }

    public boolean isVoidActualDate() {
	return voidActualDate;
    }

    public void setVoidActualDate(boolean voidActualDate) {
	this.voidActualDate = voidActualDate;
    }

    public SearchParameters getSearchParameters() {
	return searchParameters;
    }

    public void setSearchParameters(SearchParameters searchParameters) {
	this.searchParameters = searchParameters;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
	s.defaultWriteObject();
    }

    private void readObject(ObjectInputStream s) throws IOException {

	try {
	    s.defaultReadObject();
	} catch (ClassNotFoundException ex) {
	    throw new IOException(ex.getMessage());
	}

	setCdt(ChangeDetector.getInstance());
	setMfg(new MessageHandler(this));
	setTicketIdParameter(new UIParameter());
	setTimerIdParameter(new UIParameter());
	setSearchParameters(new SearchParameters());
    }

    public ProjectAccessor getProjects() {
	return projects;
    }

    public void setProjects(ProjectAccessor projects) {
	this.projects = projects;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * If its a pure number than its a simple ID,
     * Else try as a uniqueId
     */
    public boolean setUniqueId(String id) {
	if (id == null || id.trim().equals(""))
	    return false;
	try {
	    int realId = new Integer(id);
	    this.setId(realId);
	    return this.getSelected() != null;
	} catch (NumberFormatException ex) {
	    try {
		this.setSelected((Ticket) SessionHolder.currentSession()
			.getSess().createQuery(
				" from " + Ticket.class.getName()
					+ " WHERE uniqueId = ? ").setString(0,
				id).uniqueResult());
		this.setId(getSelected().getId());
		return getSelected().getId() != null;
	    } catch (Exception exa) {
		return false;
	    }
	} catch (Exception exa) {
	    return false;
	}
    }

    /**
     * Find a person with givven login
     */
    public Person getAsignee(String name) {
	try {
	    return (Person) SessionHolder.currentSession().getSess()
		    .createQuery(
			    " from " + Person.class.getName()
				    + " WHERE lower(loginCode) = lower(?) ")
		    .setString(0, name.trim()).uniqueResult();
	} catch (Exception ex) {
	    return null;
	}

    }

    public Project getProjectCode(String name) {
	try {
	    return (Project) SessionHolder.currentSession().getSess()
		    .createQuery(
			    " from " + Project.class.getName()
				    + " WHERE lower(code) = lower(?) ")
		    .setString(0, name.trim()).uniqueResult();
	} catch (Exception ex) {
	    return null;
	}
    }

    /**
     * Find a person with givven login
     */
    public boolean setAsignee(String name) {
	try {
	    Person nn = getAsignee(name);
	    if (nn == null)
		return false;
	    this.getSelected().setAssignedTo(nn.getId());
	    return true;
	} catch (Exception ex) {
	    return false;
	}

    }

    public boolean setProjectCode(String name) {
	try {
	    Project nn = getProjectCode(name);
	    if (nn == null)
		return false;
	    this.getSelected().setProject(nn.getId());
	    this.getSelected().setCompany(nn.getCompany());
	    return true;
	} catch (Exception ex) {
	    return false;
	}
    }

    /**
     * Find a person with givven login and set it to qPerson
     */
    public boolean setAsigneeGlobal(String name) {
	try {
	    Person nn = getAsignee(name);
	    if (nn == null)
		return false;
	    qPerson = nn.getId();
	    return true;
	} catch (Exception ex) {
	    return false;
	}

    }

    /**
     * Find a person with givven code and set it to qProject
     */
    public boolean setProjectCodeGlobal(String name) {
	try {
	    Project nn = getProjectCode(name);
	    if (nn == null)
		return false;
	    qProject = nn.getId();
	    return true;
	} catch (Exception ex) {
	    return false;
	}
    }

    /**
     * Tries to find:
     * Unique ID -commented out
     * ID
     * User
     * Project
     */

    public void setAny(String text) {
	if (
	// setUniqueId(text) ||
	setAsignee(text) || setProjectCode(text)) {
	    // DOES NOTHING:
	    // As paula would say: BRILLANT! And my personal: GENUS!
	}
    }

    /**
     * Tries to find:
     * Unique ID -commented out
     * ID
     * User
     * Project
     */

    public void setAnyGlobal(String text) {
	if (
	// setUniqueId(text) ||
	setAsigneeGlobal(text) || setProjectCodeGlobal(text)) {
	    // DOES NOTHING:
	    // As paula would say: BRILLANT! And my personal: GENUS!
	}
    }

    public List<SelectItem> getModules() {
	try {
	    return ProjectAccessor.getProjectModules(this.getSelected()
		    .getProject());
	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return new LinkedList<SelectItem>();
	}
    }

    public Timekeeping getTimer() {
	return timer;
    }

    public void setTimer(Timekeeping timer) {
	this.timer = timer;
    }

    public Integer getTimerId() {
	if (getTimer() != null)
	    return getTimer().getId();
	else
	    return null;
    }

    public void setTimerId(Integer timerId) {
	if (timerId == null || timerId < 0) {
	    setTimer(new Timekeeping());
	    getTimer().setStart(new Date());
	    getTimer().setFinish(new Date());
	    return;
	}
	try {
	    setTimer((Timekeeping) SessionHolder.currentSession().getSess()
		    .createQuery(
			    " FROM " + Timekeeping.class.getName()
				    + " t WHERE t.id = ?  ").setInteger(0,
			    timerId).uniqueResult());

	} catch (Exception ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    this.setTimer(null);
	}
    }

    public UIParameter getTimerIdParameter() {
	return timerIdParameter;
    }

    public void setTimerIdParameter(UIParameter timerIdParameter) {
	this.timerIdParameter = timerIdParameter;
    }

    public String deleteTimer() {
	try {
	    Timekeeping cmp = (Timekeeping) SessionHolder.currentSession()
		    .getSess().get(Timekeeping.class,
			    (Serializable) getTimerIdParameter().getValue());
	    this.getTimerIdParameter().setValue(null);
	    SessionHolder.currentSession().getSess().delete(cmp);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.LIST;
	} catch (HibernateException ex) {
	    // SessionHolder.endSession();
	    // Messenger.addFatalKeyMessage("error.transaction.abort",userHandler.getUserLocale());
	    ex.printStackTrace();
	    return StandardResults.FAIL;
	}
    }

    public static ArrayList<TicketInfo> getEMPTYInfoList() {
	return EMPTYInfoList;
    }

    public ChangeDetector getCdt() {
	return cdt;
    }

    public void setCdt(ChangeDetector cdt) {
	this.cdt = cdt;
    }

    public MessageHandler getMfg() {
	return mfg;
    }

    public void setMfg(MessageHandler mfg) {
	this.mfg = mfg;
    }

    public TextSuggest getTs() {
	return ts;
    }

    public void setTs(TextSuggest ts) {
	this.ts = ts;
    }

    public Integer getQProject() {
	return qProject;
    }

    public void setQProject(Integer qProject) {
	this.qProject = qProject;
    }

    public Integer getQPerson() {
	return qPerson;
    }

    public void setQPerson(Integer qPerson) {
	this.qPerson = qPerson;
    }

}
