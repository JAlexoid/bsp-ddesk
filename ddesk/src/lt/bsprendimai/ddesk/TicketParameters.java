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
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import lt.bsprendimai.ddesk.dao.EventType;
import lt.bsprendimai.ddesk.dao.Priority;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.Severity;
import lt.bsprendimai.ddesk.dao.Status;

import org.hibernate.HibernateException;

/**
 * Ticket parameter access and operations related functionality.
 * Operates on:
 *  - {@link Status}
 *  - {@link Priority}
 *  - {@link EventType}
 *  - {@link Severity}
 *
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class TicketParameters implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -8963125892330025355L;

    private UserHandler userHandler;

    private Integer statusId;
    private Integer priorityId;
    private Integer eventTypeId;
    private Integer severityId;

    private Status selectedStatus = new Status();
    private Priority selectedPriority = new Priority();
    private EventType selectedEvent = new EventType();
    private Severity selectedSeverity = new Severity();

    private static final ArrayList<SelectItem> ids;
    private ArrayList<SelectItem> flags = new ArrayList<SelectItem>();
    static {
	ids = new ArrayList<SelectItem>(100);
	SelectItem si;
	si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ids.add(si);
	for (int i = 1; i <= 100; i++) {
	    si = new SelectItem();
	    si.setValue(i);
	    si.setLabel(Integer.toString(i));
	    ids.add(si);
	}

    }

    /** Creates a new instance of TicketParameters */
    public TicketParameters() {
    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
	SelectItem si;
	si = new SelectItem();
	si.setValue("0");
	si.setLabel("");
	flags.add(si);

	si = new SelectItem();
	si.setValue("1");
	si.setLabel(UIMessenger.getMessage(getUserHandler().getUserLocale(),
		"application.flag.set"));
	flags.add(si);

	si = new SelectItem();
	si.setValue("2");
	si.setLabel(UIMessenger.getMessage(getUserHandler().getUserLocale(),
		"application.flag.clear"));
	flags.add(si);
    }

    public List<Status> getStatusList() {
	try {
	    return (List<Status>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Status.class.getName()
				    + " WHERE id <> 0 ORDER BY id").list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<Status>();
	}
    }

    public List<Priority> getPriorityList() {
	try {
	    return (List<Priority>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Priority.class.getName()
				    + " WHERE id <> 0  ORDER BY id").list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<Priority>();
	}
    }

    public List<EventType> getEventTypeList() {
	try {
	    return (List<EventType>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + EventType.class.getName()
				    + " WHERE id <> 0  ORDER BY id").list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<EventType>();
	}
    }

    public List<Severity> getSeverityList() {
	try {
	    return (List<Severity>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Severity.class.getName()
				    + " WHERE id <> 0  ORDER BY id").list();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new LinkedList<Severity>();
	}
    }

    public Integer getStatusId() {
	return statusId;
    }

    public void setStatusId(Integer statusId) {
	if (statusId != null && statusId.equals(this.statusId))
	    return;
	this.statusId = statusId;

	if (statusId == null || statusId <= 0) {
	    this.selectedStatus = new Status();
	    return;
	}

	try {
	    this.selectedStatus = (Status) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    " FROM " + Status.class.getName() + " WHERE id = ?")
		    .setInteger(0, statusId).uniqueResult();
	} catch (HibernateException ex) {
	    this.selectedStatus = null;
	}
    }

    public Integer getPriorityId() {
	return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
	if (priorityId != null && priorityId.equals(this.priorityId))
	    return;
	this.priorityId = priorityId;

	if (priorityId == null || priorityId <= 0) {
	    this.selectedPriority = new Priority();
	    return;
	}

	try {
	    this.selectedPriority = (Priority) SessionHolder.currentSession()
		    .getSess().createQuery(
			    " FROM " + Priority.class.getName()
				    + " WHERE id = ?")
		    .setInteger(0, priorityId).uniqueResult();
	} catch (HibernateException ex) {
	    this.selectedPriority = null;
	}
    }

    public Integer getEventTypeId() {
	return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
	if (eventTypeId != null && eventTypeId.equals(this.eventTypeId))
	    return;

	this.eventTypeId = eventTypeId;

	if (eventTypeId == null || eventTypeId <= 0) {
	    this.selectedEvent = new EventType();
	    return;
	}

	try {
	    this.selectedEvent = (EventType) SessionHolder.currentSession()
		    .getSess().createQuery(
			    " FROM " + EventType.class.getName()
				    + " WHERE id = ?").setInteger(0,
			    eventTypeId).uniqueResult();
	} catch (HibernateException ex) {
	    this.selectedEvent = null;
	}
    }

    public String getStatus() {
	if (getStatusId() != null)
	    return getStatusId().toString();
	else
	    return "";

    }

    public void setStatus(String id) {
	try {
	    this.setStatusId(new Integer(id.trim()));
	} catch (Exception ex) {
	    this.setStatusId(null);
	}
    }

    public String getPriority() {
	if (getPriorityId() != null)
	    return getPriorityId().toString();
	else
	    return "";
    }

    public void setPriority(String id) {
	try {
	    this.setPriorityId(new Integer(id.trim()));
	} catch (Exception ex) {
	    this.setPriorityId(null);
	}
    }

    public String getEventType() {
	if (getEventTypeId() != null)
	    return getEventTypeId().toString();
	else
	    return "";
    }

    public void setEventType(String id) {
	try {
	    this.setEventTypeId(new Integer(id.trim()));
	} catch (Exception ex) {
	    this.setEventTypeId(null);
	}
    }

    public String getSeverity() {
	if (getSeverityId() != null)
	    return getSeverityId().toString();
	else
	    return "";
    }

    public void setSeverity(String id) {
	try {
	    this.setSeverityId(new Integer(id.trim()));
	} catch (Exception ex) {
	    this.setSeverityId(null);
	}
    }

    public Status getSelectedStatus() {
	return selectedStatus;
    }

    public void setSelectedStatus(Status selectedStatus) {
	this.selectedStatus = selectedStatus;
    }

    public Priority getSelectedPriority() {
	return selectedPriority;
    }

    public void setSelectedPriority(Priority selectedPriority) {
	this.selectedPriority = selectedPriority;
    }

    public EventType getSelectedEvent() {
	return selectedEvent;
    }

    public void setSelectedEvent(EventType selectedEvent) {
	this.selectedEvent = selectedEvent;
    }

    public List<SelectItem> getEventTypeSelect() {
	List<EventType> la = new ArrayList<EventType>(0);
	try {
	    la = (List<EventType>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + EventType.class.getName()
				    + " e  WHERE e.id <> 0  ORDER BY e.id ")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);
	for (EventType c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getName());
	    ll.add(si);
	}
	return ll;
    }

    public List<SelectItem> getPrioritySelect() {
	List<Priority> la = new ArrayList<Priority>(0);
	try {
	    la = (List<Priority>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Priority.class.getName()
				    + " e WHERE e.id <> 0 ORDER BY e.id ")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();

	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);
	for (Priority c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getId() + " - " + c.getName());
	    ll.add(si);
	}
	return ll;
    }

    public List<SelectItem> getStatusSelect() {
	List<Status> la = new ArrayList<Status>(0);
	try {
	    la = (List<Status>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Status.class.getName()
				    + " e WHERE e.id <> 0 ORDER BY e.id ")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);

	for (Status c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getName());
	    ll.add(si);
	}
	return ll;
    }

    public List<SelectItem> getSeveritySelect() {
	List<Severity> la = new ArrayList<Severity>(0);
	try {
	    la = (List<Severity>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "from " + Severity.class.getName()
				    + " e WHERE e.id <> 0 ORDER BY e.id ")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	List<SelectItem> ll = new LinkedList<SelectItem>();
	SelectItem si = new SelectItem();
	si.setValue("");
	si.setLabel("");
	ll.add(si);

	for (Severity c : la) {
	    si = new SelectItem();
	    si.setValue(c.getId().intValue());
	    si.setLabel(c.getName());
	    ll.add(si);
	}
	return ll;
    }

    public List<SelectItem> getAvailableStatusIds() {
	List<Integer> la = new ArrayList<Integer>(0);
	try {
	    la = (List<Integer>) SessionHolder
		    .currentSession()
		    .getSess()
		    .createQuery(
			    "SELECT e.id from "
				    + Status.class.getName()
				    + " e WHERE e.id <> 0 AND e.id <= 100 ORDER BY e.id DESC")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	ArrayList<SelectItem> ll = (ArrayList<SelectItem>) ids.clone();

	for (Integer c : la) {
	    ll.remove(c.intValue());
	}

	if (selectedStatus != null && selectedStatus.getId() != null) {
	    SelectItem si = new SelectItem();
	    si.setValue(selectedStatus.getId());
	    si.setLabel(selectedStatus.getId().toString());
	    ll.add(selectedStatus.getId(), si);
	}

	return ll;
    }

    public List<SelectItem> getAvailablePriorityIds() {
	List<Integer> la = new ArrayList<Integer>(0);
	try {
	    la = (List<Integer>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "SELECT e.id from " + Priority.class.getName()
				    + " e WHERE e.id <> 0  ORDER BY e.id DESC")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	ArrayList<SelectItem> ll = (ArrayList<SelectItem>) ids.clone();

	for (Integer c : la) {
	    ll.remove(c.intValue());
	}
	if (selectedPriority != null && selectedPriority.getId() != null) {
	    SelectItem si = new SelectItem();
	    si.setValue(selectedPriority.getId());
	    si.setLabel(selectedPriority.getId().toString());
	    ll.add(selectedPriority.getId(), si);
	}

	return ll;
    }

    public List<SelectItem> getAvailableEventIds() {
	List<Integer> la = new ArrayList<Integer>(0);
	try {
	    la = (List<Integer>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "SELECT e.id from " + EventType.class.getName()
				    + " e WHERE e.id <> 0  ORDER BY e.id DESC")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	ArrayList<SelectItem> ll = (ArrayList<SelectItem>) ids.clone();

	for (Integer c : la) {
	    ll.remove(c.intValue());
	}

	if (selectedEvent != null && selectedEvent.getId() != null) {
	    SelectItem si = new SelectItem();
	    si.setValue(selectedEvent.getId());
	    si.setLabel(selectedEvent.getId().toString());
	    ll.add(selectedEvent.getId(), si);
	}

	return ll;
    }

    public List<SelectItem> getAvailableSeverityIds() {
	List<Integer> la = new ArrayList<Integer>(0);
	try {
	    la = (List<Integer>) SessionHolder.currentSession().getSess()
		    .createQuery(
			    "SELECT e.id from " + Severity.class.getName()
				    + " e WHERE e.id <> 0  ORDER BY e.id DESC")
		    .list();
	} catch (HibernateException ex) {
	    ex.printStackTrace();
	}

	ArrayList<SelectItem> ll = (ArrayList<SelectItem>) ids.clone();

	for (Integer c : la) {
	    ll.remove(c.intValue());
	}

	if (selectedSeverity != null && selectedSeverity.getId() != null) {
	    SelectItem si = new SelectItem();
	    si.setValue(selectedSeverity.getId());
	    si.setLabel(selectedSeverity.getId().toString());
	    ll.add(selectedSeverity.getId(), si);
	}

	return ll;
    }

    public Severity getSelectedSeverity() {
	return selectedSeverity;
    }

    public void setSelectedSeverity(Severity selectedSeverity) {
	this.selectedSeverity = selectedSeverity;
    }

    public Integer getSeverityId() {
	return severityId;
    }

    public void setSeverityId(Integer severityId) {
	if (severityId != null && severityId.equals(this.severityId))
	    return;
	this.severityId = severityId;

	if (severityId == null || severityId <= 0) {
	    this.selectedSeverity = new Severity();
	    return;
	}

	try {
	    this.selectedSeverity = (Severity) SessionHolder.currentSession()
		    .getSess().createQuery(
			    " FROM " + Severity.class.getName()
				    + " WHERE id = ?")
		    .setInteger(0, severityId).uniqueResult();
	} catch (HibernateException ex) {
	    this.selectedSeverity = null;
	}
    }

    public List<SelectItem> getSelection() {
	return flags;
    }

}
