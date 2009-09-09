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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import lt.bsprendimai.Term;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.TicketInfo;
import lt.bsprendimai.ddesk.dao.Timekeeping;

import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;

/**
 * Backing and model bean for apache myfaces tomahawk schedule componenet.
 * Componenet is found on the internal index.xhtml and prvides \
 * week view model and data for the component.
 *
 * Non portable bean. Depends on apache myfaces tomahawk schedule.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class TicketWeekView implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 8265308351216443783L;

    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, ParameterAccess
	    .getLocale("lt"));

    private UserHandler userHandler;
    private TicketAccessor ticketAccess;

    private transient SimpleScheduleModel model;
    private ArrayList<DefaultScheduleEntry> entryList = new ArrayList<DefaultScheduleEntry>();

    private Integer personId;
    private Date startDate = new Date();

    /** Creates a new instance of TicketWeekView */
    public TicketWeekView() {
    }

    private List<TicketInfo> getTicketsForWeek() {
	Calendar c = Calendar.getInstance();
	if (startDate != null)
	    c.setTime(startDate);
	else
	    startDate = new Date();
	c.set(Calendar.HOUR, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	Date toDay = c.getTime();

	c.add(Calendar.DAY_OF_YEAR, 7);
	c.set(Calendar.HOUR, 23);
	c.set(Calendar.MINUTE, 59);
	c.set(Calendar.SECOND, 59);
	Date sevenDays = c.getTime();

	int id = 0;

	if (personId != null) {
	    id = personId;
	} else {
	    id = userHandler.getUser().getId();
	}

	try {
	    return (List<TicketInfo>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + TicketInfo.class.getName()
				    + " t WHERE t.dateClosed IS NULL AND t.assignedTo = ? AND t.planedDate BETWEEN ? AND ? ORDER BY t.planedDate ASC ")
		    .setInteger(0, id).setTimestamp(1, toDay).setTimestamp(2,
			    sevenDays).list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return TicketAccessor.getEMPTYInfoList();
	}
    }

    private List<Timekeeping> getTimersForWeek() {
	Calendar c = Calendar.getInstance();
	if (startDate != null)
	    c.setTime(startDate);
	else
	    startDate = new Date();
	c.set(Calendar.HOUR, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	Date toDay = c.getTime();

	c.add(Calendar.DAY_OF_YEAR, 7);
	c.set(Calendar.HOUR, 23);
	c.set(Calendar.MINUTE, 59);
	c.set(Calendar.SECOND, 59);
	Date sevenDays = c.getTime();

	int id = 0;

	if (personId != null) {
	    id = personId;
	} else {
	    id = userHandler.getUser().getId();
	}

	try {
	    return (List<Timekeeping>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "from "
				    + Timekeeping.class.getName()
				    + " t WHERE t.person = ? AND (t.start BETWEEN ? AND ? OR t.finish BETWEEN ? AND ? )")
		    .setInteger(0, id).setTimestamp(1, toDay).setTimestamp(2,
			    sevenDays).setTimestamp(3, toDay).setTimestamp(4,
			    sevenDays).list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<Timekeeping>();
	}
    }

    private void refreshModel() {
	if (model == null)
	    return;

	for (DefaultScheduleEntry de : entryList) {
	    model.removeEntry(de);
	}
	entryList.clear();

	for (TicketInfo ti : this.getTicketsForWeek()) {
	    DefaultScheduleEntry appointment = new DefaultScheduleEntry();
	    appointment.setId(ti.getId().toString());
	    appointment.setStartTime(ti.getPlanedDate());
	    appointment.setEndTime(new Date(
		    ti.getPlanedDate().getTime() + 3600000l));
	    appointment.setTitle(ti.getUniqueId() + " " + ti.getName());
	    appointment.setSubtitle(ti.getCompanyName() + " "
		    + ti.getPriorityName() + " " + ti.getStatusName());
	    appointment.setDescription(ti.getDescription());
	    model.addEntry(appointment);
	    entryList.add(appointment);
	}
	for (Timekeeping ti : this.getTimersForWeek()) {
	    DefaultScheduleEntry appointment = new DefaultScheduleEntry();
	    appointment.setId("T" + ti.getId().toString());
	    appointment.setStartTime(ti.getStart());
	    appointment.setEndTime(ti.getFinish());
	    appointment.setTitle(ti.getName());
	    appointment.setSubtitle(ti.getDescription());
	    appointment.setDescription(ti.getDescription());
	    model.addEntry(appointment);
	    entryList.add(appointment);
	}

	model.setSelectedEntry(null);
	if (startDate != null)
	    model.setSelectedDate(startDate);
	else
	    model.setSelectedDate(new Date());
	model.setMode(ScheduleModel.WEEK);
	model.refresh();
    }

    /**
     * <p>
     * action listener for the schedule component.
     * </p>
     *
     * @param actionEvent
     *            the action event
     */
    public void scheduleActionPerformed(ActionEvent actionEvent) {
	if (model.getSelectedEntry() == null)
	    return;
	try {
	    if (model.getSelectedEntry().getId().startsWith("T")) {
		ticketAccess.setTimerId(new Integer(model.getSelectedEntry()
			.getId().substring(1)));
		FacesContext.getCurrentInstance().getApplication()
			.getNavigationHandler().handleNavigation(
				FacesContext.getCurrentInstance(), null,
				StandardResults.TIMER);
	    } else {
		ticketAccess
			.setId(new Integer(model.getSelectedEntry().getId()));
		FacesContext.getCurrentInstance().getApplication()
			.getNavigationHandler().handleNavigation(
				FacesContext.getCurrentInstance(), null,
				StandardResults.TICKET);
	    }
	    // return StandartResults.TICKET;
	} catch (NumberFormatException ex) {
	    ex.printStackTrace();
	    // return StandartResults.STAY;
	}
    }

    public String getCondInit() {
	if (model == null && userHandler != null) {
	    model = new SimpleScheduleModel();
	    refreshModel();
	} else if (model != null) {
	    refreshModel();
	}

	model.setSelectedEntry(null);
	return "";
    }

    public String refresh() {
	refreshModel();
	return StandardResults.STAY;
    }

    public Set<Entry<String, List<TicketInfo>>> getDays() {
	LinkedHashMap<String, List<TicketInfo>> set = new LinkedHashMap<String, List<TicketInfo>>();
	Date lastDate = null;

	ArrayList<TicketInfo> til = null;

	for (TicketInfo ti : this.getTicketsForWeek()) {
	    if (lastDate == null) {
		lastDate = ti.getPlanedDate();
		til = (ArrayList<TicketInfo>) TicketAccessor.getEMPTYInfoList()
			.clone();
		til.ensureCapacity(3);
	    } else if (!Term.isSameDay(lastDate, ti.getPlanedDate())) {
		// System.out.println("Adding to "+df.format(lastDate));
		set.put(df.format(lastDate), til);
		lastDate = ti.getPlanedDate();
		til = (ArrayList<TicketInfo>) TicketAccessor.getEMPTYInfoList()
			.clone();
		til.ensureCapacity(3);
	    }
	    // System.out.println("Adding "+ti.getUniqueId());
	    til.add(ti);
	}
	if (lastDate != null) {
	    // System.out.println("Adding to "+df.format(lastDate));
	    set.put(df.format(lastDate), til);
	}

	return set.entrySet();
    }

    public String getClear() {
	personId = null;
	return "";
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
	if (userHandler != null)
	    personId = userHandler.getUser().getId();
    }

    public Integer getPersonId() {
	return personId;
    }

    public void setPersonId(Integer personId) {
	this.personId = personId;
    }

    public ScheduleModel getModel() {
	return model;
    }

    public void setModel(SimpleScheduleModel model) {
	this.model = model;
    }

    public TicketAccessor getTicketAccess() {
	return ticketAccess;
    }

    public void setTicketAccess(TicketAccessor ticketAccess) {
	this.ticketAccess = ticketAccess;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

}
