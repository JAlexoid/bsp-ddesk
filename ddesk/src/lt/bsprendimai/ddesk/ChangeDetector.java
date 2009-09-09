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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lt.bsprendimai.ddesk.dao.EventType;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.Priority;
import lt.bsprendimai.ddesk.dao.Project;
import lt.bsprendimai.ddesk.dao.ProjectModule;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.Severity;
import lt.bsprendimai.ddesk.dao.Status;
import lt.bsprendimai.ddesk.dao.Ticket;
import lt.bsprendimai.ddesk.dao.TicketHistory;

import org.hibernate.Session;

/**
 * <pre>
 * This class is for detecting changes in between version of tickets,
 * generating change markuped information and transforming markup into HTML and text
 * Uses changeFormats resource bundle.&lt;br/&gt;
 * Syntax {...} - tags that are substituted accordingly to their content
 * {[0123456789...]} - time in ms, to format in correct locale
 * Tags
 * 	{taskChange}
 * 	{changeTo}
 * 	{setTo}
 * 	{empty}
 * Field headers:
 * 	{name}
 *  	{requestNotes}
 *  	{status}
 * 	{acceptedNotes}
 * 	{assignedTo}
 * 	{assignedBy}
 * 	{distance}
 * 	{worktime}
 * 	{additionalTime}
 * 	{planedDate}
 * 	{actualDate}
 * 	{serviceCode}
 * 	{priority}
 * 	{type}
 * 	{resolution}
 * 	{taskClosed}
 * 	{taskAccepted}
 * 	{taskAssigned}
 * 	{assignBy}
 * 	{assignTo}
 * 	{taskWorktime}
 * 	{addedComment}
 * Decoration markers:
 * 	%df% Date field value start  &lt;span class=&quot;date&quot;&gt;
 * 	%fd% Date field value end &lt;/span&gt;
 * 	%fs% Field value start &lt;span class=&quot;field&quot;&gt;
 * 	%sf% Field value end &lt;/span&gt;
 * 	%ft% Notes and long text start &lt;span class=&quot;notes&quot;&gt;
 * 	%tf% Notes and long text end &lt;/span&gt;
 * 	%hb% Header block start &lt;div class=&quot;hb&quot;&gt;
 * 	%bh% Header block end &lt;/div&gt;
 * 	%dt% Multiline and descriptive text start &lt;div class=&quot;dt&quot;&gt;
 * 	%td% Multiline and descriptive text end &lt;/div&gt;&lt;br/&gt;
 * 	%ei% Change cause name start &lt;span class=&quot;action&quot;&gt;
 * 	%ie% Change cause name end &lt;/span&gt;
 * </pre>
 * 
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ChangeDetector {

    private static ChangeDetector singleInstance = new ChangeDetector();

    /** Creates a new instance of ChangeDetector */
    private ChangeDetector() {
    }

    public synchronized static ChangeDetector getInstance() {
	return singleInstance;
    }

    /**
     * Generate array of strings with I18N markup of identified changes between
     * the Ticket objects.
     * 
     * @param updatedTicket
     *            Updated ticket
     * @param originalTicket
     *            Original ticket
     * @param editor
     *            Person that made the changes
     * @return array of strings with markup
     */
    private String[] detectChanges(Ticket updatedTicket, Ticket originalTicket,
	    Person editor) {
	String[] ret = new String[2];
	String changeNotes = "";
	String publicChangeNotes = "";

	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);
	sess.evict(originalTicket);

	Status oldStatus, newStatus;
	Priority oldPriority, newPriority;
	EventType oldType, newType;
	Project oldProject, newProject;
	ProjectModule oldProjectModule, newProjectModule;
	Severity oldSeverity, newSeverity;

	if (originalTicket.getProject() != null
		&& originalTicket.getProject() == 0) {
	    originalTicket.setProject(null);
	}

	if (originalTicket.getModule() != null
		&& originalTicket.getModule() == 0) {
	    originalTicket.setModule(null);
	}

	if (originalTicket.getStatus() != null
		&& originalTicket.getStatus() == 0) {
	    originalTicket.setStatus(null);
	}

	if (originalTicket.getSeverity() != null
		&& originalTicket.getSeverity() == 0) {
	    originalTicket.setSeverity(null);
	}

	if (originalTicket.getType() != null && originalTicket.getType() == 0) {
	    originalTicket.setType(null);
	}

	if (originalTicket.getPriority() != null
		&& originalTicket.getPriority() == 0) {
	    originalTicket.setPriority(null);
	}

	if (updatedTicket.getProject() != null
		&& updatedTicket.getProject() == 0) {
	    updatedTicket.setProject(null);
	}

	if (updatedTicket.getModule() != null && updatedTicket.getModule() == 0) {
	    updatedTicket.setModule(null);
	}

	if (updatedTicket.getStatus() != null && updatedTicket.getStatus() == 0) {
	    updatedTicket.setStatus(null);
	}

	if (updatedTicket.getSeverity() != null
		&& updatedTicket.getSeverity() == 0) {
	    updatedTicket.setSeverity(null);
	}

	if (updatedTicket.getType() != null && updatedTicket.getType() == 0) {
	    updatedTicket.setType(null);
	}

	if (updatedTicket.getPriority() != null
		&& updatedTicket.getPriority() == 0) {
	    updatedTicket.setPriority(null);
	}

	if (originalTicket.getProject() != null) {
	    oldProject = (Project) sess.get(Project.class, originalTicket
		    .getProject());
	} else {
	    oldProject = new Project();
	    oldProject.setName("{empty}");
	}
	if (updatedTicket.getProject() != null) {
	    newProject = (Project) sess.get(Project.class, updatedTicket
		    .getProject());
	} else {
	    newProject = new Project();
	    newProject.setName("{empty}");
	}

	if (originalTicket.getModule() != null) {
	    oldProjectModule = (ProjectModule) sess.get(ProjectModule.class,
		    originalTicket.getModule());
	} else {
	    oldProjectModule = new ProjectModule();
	    oldProjectModule.setModule("{empty}");
	}
	if (updatedTicket.getModule() != null) {
	    newProjectModule = (ProjectModule) sess.get(ProjectModule.class,
		    updatedTicket.getModule());
	} else {
	    newProjectModule = new ProjectModule();
	    newProjectModule.setModule("{empty}");
	}

	if (originalTicket.getStatus() != null) {
	    oldStatus = (Status) sess.get(Status.class, originalTicket
		    .getStatus());
	} else {
	    oldStatus = new Status();
	    oldStatus.setName("{empty}");
	}
	if (updatedTicket.getStatus() != null) {
	    newStatus = (Status) sess.get(Status.class, updatedTicket
		    .getStatus());
	} else {
	    newStatus = new Status();
	    newStatus.setName("{empty}");
	}

	if (originalTicket.getPriority() != null) {
	    oldPriority = (Priority) sess.get(Priority.class, originalTicket
		    .getPriority());
	} else {
	    oldPriority = new Priority();
	    oldPriority.setName("{empty}");
	}
	if (updatedTicket.getPriority() != null) {
	    newPriority = (Priority) sess.get(Priority.class, updatedTicket
		    .getPriority());
	} else {
	    newPriority = new Priority();
	    newPriority.setName("{empty}");
	}

	if (originalTicket.getType() != null) {
	    oldType = (EventType) sess.get(EventType.class, originalTicket
		    .getType());
	} else {
	    oldType = new EventType();
	    oldType.setName("{empty}");
	}
	if (updatedTicket.getType() != null) {
	    newType = (EventType) sess.get(EventType.class, updatedTicket
		    .getType());
	} else {
	    newType = new EventType();
	    newType.setName("{empty}");
	}

	if (originalTicket.getSeverity() != null) {
	    oldSeverity = (Severity) sess.get(Severity.class, originalTicket
		    .getSeverity());
	} else {
	    oldSeverity = new Severity();
	    oldSeverity.setName("{empty}");
	}
	if (updatedTicket.getSeverity() != null) {
	    newSeverity = (Severity) sess.get(Severity.class, updatedTicket
		    .getSeverity());
	} else {
	    newSeverity = new Severity();
	    newSeverity.setName("{empty}");
	}

	if (!safeEquals(originalTicket.getName(), updatedTicket.getName())) {
	    if (originalTicket.getName() == null) {
		changeNotes += "{name} {setTo} %fs%" + updatedTicket.getName()
			+ "%sf%\n";
		publicChangeNotes += "{name} {setTo} %fs%"
			+ updatedTicket.getName() + "%sf%\n";
	    } else {
		changeNotes += "{name} %fs%" + originalTicket.getName()
			+ "%sf% {changeTo} %fs%" + updatedTicket.getName()
			+ "%sf%\n";
		publicChangeNotes += "{name} %fs%" + originalTicket.getName()
			+ "%sf% {changeTo} %fs%" + updatedTicket.getName()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getProject(), updatedTicket.getProject())) {
	    if (originalTicket.getProject() == null) {
		if (updatedTicket.getProject() != null) {
		    changeNotes += "{project} {setTo} %fs%"
			    + newProject.getName() + "%sf%\n";
		}
	    } else {
		changeNotes += "{project} %fs%" + oldProject.getName()
			+ "%sf% {changeTo} %fs%" + newProject.getName()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getModule(), updatedTicket.getModule())) {
	    if (originalTicket.getModule() == null) {
		if (updatedTicket.getModule() != null) {
		    changeNotes += "{module} {setTo} %fs%"
			    + newProjectModule.getModule() + "%sf%\n";
		}
	    } else {
		changeNotes += "{module} %fs%" + oldProjectModule.getModule()
			+ "%sf% {changeTo} %fs%" + newProjectModule.getModule()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getStatus(), updatedTicket.getStatus())) {
	    if (originalTicket.getStatus() == null) {
		if (updatedTicket.getStatus() != null) {
		    changeNotes += "{status} {setTo} %fs%"
			    + newStatus.getName() + "%sf%\n";
		}
	    } else {
		changeNotes += "{status} %fs%" + oldStatus.getName()
			+ "%sf% {changeTo} %fs%" + newStatus.getName()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getSeverity(), updatedTicket
		.getSeverity())) {
	    if (originalTicket.getSeverity() == null) {
		if (updatedTicket.getSeverity() != null) {
		    changeNotes += "{severity} {setTo} %fs%"
			    + newSeverity.getName() + "%sf%\n";
		}
	    } else {
		changeNotes += "{severity} %fs%" + oldSeverity.getName()
			+ "%sf% {changeTo} %fs%" + newSeverity.getName()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getWorktime(), updatedTicket
		.getWorktime())) {
	    if (originalTicket.getWorktime() == null) {
		changeNotes += "{worktime} {setTo} %fs%"
			+ updatedTicket.getWorktime() + "%sf%\n";
		publicChangeNotes += "{worktime} {setTo} %fs%"
			+ updatedTicket.getWorktime() + "%sf%\n";
	    } else {
		changeNotes += "{worktime}  %fs%"
			+ originalTicket.getWorktime()
			+ "%sf%  {changeTo} %fs%" + updatedTicket.getWorktime()
			+ "%sf%\n";

	    }
	}

	if (!safeEquals(originalTicket.getVersion(), updatedTicket.getVersion())) {
	    if (originalTicket.getVersion() == null) {
		changeNotes += "{version} {setTo} %fs%"
			+ updatedTicket.getVersion() + "%sf%\n";
	    } else {
		changeNotes += "{version}  %fs%" + originalTicket.getVersion()
			+ "%sf%  {changeTo} %fs%" + updatedTicket.getVersion()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getAdditionalTime(), updatedTicket
		.getAdditionalTime())) {
	    if (originalTicket.getAdditionalTime() == null) {
		changeNotes += "{additionalTime} {setTo} %fs%"
			+ updatedTicket.getAdditionalTime() + "%sf%\n";

	    } else {
		changeNotes += "{additionalTime}  %fs%"
			+ originalTicket.getAdditionalTime()
			+ "%sf%  {changeTo} %fs%"
			+ updatedTicket.getAdditionalTime() + "%sf%\n";

	    }
	}

	if (!safeEqualMuinutes(originalTicket.getPlanedDate(), updatedTicket
		.getPlanedDate())) {
	    if (originalTicket.getPlanedDate() == null) {
		changeNotes += "{planedDate} {setTo} %df%{["
			+ updatedTicket.getPlanedDate().getTime() + "]}%fd%\n";

	    } else {
		changeNotes += "{planedDate}  {changeTo} %df%{["
			+ updatedTicket.getPlanedDate().getTime() + "]}%fd%\n";

	    }
	}

	if (!safeEqualMuinutes(originalTicket.getActualDate(), updatedTicket
		.getActualDate())) {
	    if (originalTicket.getActualDate() == null) {
		changeNotes += "{actualDate} {setTo} %df%{["
			+ updatedTicket.getActualDate().getTime() + "]}%fd%\n";
		publicChangeNotes += "{actualDate} {setTo} %df%{["
			+ updatedTicket.getActualDate().getTime() + "]}%fd%\n";
	    } else {
		changeNotes += "{actualDate}  {changeTo} %df%{["
			+ updatedTicket.getActualDate().getTime() + "]}%fd%\n";

	    }
	}

	if (!safeEquals(originalTicket.getServiceCode(), updatedTicket
		.getServiceCode())) {
	    if (originalTicket.getServiceCode() == null) {
		if (!updatedTicket.getServiceCode().equals(""))
		    changeNotes += "{serviceCode} {setTo} %fs%"
			    + updatedTicket.getServiceCode() + "%sf%\n";
	    } else {
		changeNotes += "{serviceCode}  {changeTo} %fs%"
			+ updatedTicket.getServiceCode() + "%sf%\n";

	    }
	}

	if (!safeEquals(originalTicket.getPriority(), updatedTicket
		.getPriority())) {
	    if (originalTicket.getPriority() == null) {
		if (updatedTicket.getPriority() != null) {
		    changeNotes += "{priority} {setTo} %fs%"
			    + newPriority.getName() + "%sf%\n";
		    publicChangeNotes += "{priority} {setTo} %fs%"
			    + newPriority.getName() + "%sf%\n";
		}
	    } else {
		changeNotes += "{priority} {changeTo} %fs%"
			+ newPriority.getName() + "%sf%\n";
		publicChangeNotes += "{priority} {changeTo} %fs%"
			+ newPriority.getName() + "%sf%\n";

	    }
	}

	if (!safeEquals(originalTicket.getType(), updatedTicket.getType())) {
	    if (originalTicket.getType() == null) {
		if (updatedTicket.getType() != null) {
		    changeNotes += "{type} {setTo} %fs%" + newType.getName()
			    + "%sf%\n";
		}
	    } else {
		changeNotes += "{type} {changeTo} %fs%" + newType.getName()
			+ "%sf%\n";

	    }
	}

	if (!safeEquals(originalTicket.isChargeable(), updatedTicket
		.isChargeable())) {
	    if (updatedTicket.isChargeable()) {
		changeNotes += "%fs%{chargeable}%sf%\n";
	    } else {
		changeNotes += " %fs%{notChargeable}%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getDescription(), updatedTicket
		.getDescription())) {
	    if (originalTicket.getDescription() == null) {
		changeNotes += "%ft%" + updatedTicket.getDescription()
			+ "%tf%\n";
		publicChangeNotes += "%ft%" + updatedTicket.getDescription()
			+ "%tf%\n";
	    } else {
		changeNotes += " {oldNotes} %ft%"
			+ originalTicket.getDescription() + "%tf%\n";
		publicChangeNotes += " {oldNotes} %ft%"
			+ originalTicket.getDescription() + "%tf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getAcceptedNotes(), updatedTicket
		.getAcceptedNotes())) {
	    if (originalTicket.getAcceptedNotes() == null) {
		changeNotes += "%ft%" + updatedTicket.getAcceptedNotes()
			+ "%tf%\n";
		publicChangeNotes += "%ft%" + updatedTicket.getAcceptedNotes()
			+ "%tf%\n";
	    } else {
		changeNotes += "%ft%" + updatedTicket.getAcceptedNotes()
			+ "%tf%\n";
		publicChangeNotes += "%ft%" + updatedTicket.getAcceptedNotes()
			+ "%tf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getResolution(), updatedTicket
		.getResolution())) {
	    if (originalTicket.getResolution() == null) {
		changeNotes += "%fs%" + updatedTicket.getResolution()
			+ "%sf%\n";
		publicChangeNotes += "%fs%" + updatedTicket.getResolution()
			+ "%sf%\n";
	    } else {
		changeNotes += " %fs%" + updatedTicket.getResolution()
			+ "%sf%\n";
		publicChangeNotes += "%fs%" + updatedTicket.getResolution()
			+ "%sf%\n";
	    }
	}

	if (!safeEquals(originalTicket.getAssignedTo(), updatedTicket
		.getAssignedTo())) {
	    updatedTicket.setAssignedDate(new Date());
	    Person person2 = null;
	    try {
		person2 = (Person) sess.get(Person.class, updatedTicket
			.getAssignedTo());
	    } catch (Exception ex) {
		person2 = new Person();
		person2.setName("{empty}");
	    }
	    changeNotes = "%hb%{taskAssigned} {assignTo} %fs%"
		    + person2.getName() + "%sf%  %df%{["
		    + updatedTicket.getAssignedDate().getTime()
		    + "]}%fd%%bh% %dt%" + changeNotes + "%td%";
	    // publicChangeNotes +=
	    // "%hb%{taskAssigned} {assignTo} %fs%"+person2.getName()+"%sf%  %df%{["+updatedTicket.getAssignedDate().getTime()+"]}%fd%%bh% %dt%"+changeNotes+"%td%";
	} else if (!changeNotes.trim().equals("")) {
	    changeNotes = "%dt%" + changeNotes + "%td%";
	    if (!publicChangeNotes.trim().equals("")) {
		publicChangeNotes = "%dt%" + publicChangeNotes + "%td%";
	    }
	}

	ret[0] = changeNotes;
	ret[1] = publicChangeNotes;

	return ret;

    }

    /**
     * <pre>
     * Create a ticket history and change entry for ticket change.
     * 
     * This method populates TicketHistory object with changes and values from changed ticket.
     * Fetches the unchanged Ticket from the database and does the comparison.
     * 
     * Prepends {taskChange} to the changeNotes
     * 
     * Should be called when a Ticket is changed in all cases, except for Accept, Comment, Close and
     * ReOpen
     * </pre>
     * 
     * See {@link ChangeDetector#preAccept(Ticket)},
     * {@link ChangeDetector#preComment(Ticket, String, boolean)},
     * {@link ChangeDetector#preClose(Ticket)} and {@link ChangeDetector#preReopen(Ticket)}
     * 
     * @param updatedTicket
     *            the changed ticket
     * @return TicketHistory object populated with changed values and detected changes
     */
    public TicketHistory preUpdate(Ticket updatedTicket) {

	String changeNotes;
	String publicChangeNotes;

	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);

	Ticket originalTicket = (Ticket) sess.get(Ticket.class, updatedTicket
		.getId());
	if (originalTicket == null) {
	    return copyTicket(updatedTicket);
	}
	sess.evict(originalTicket);

	Person person = null;

	try {
	    person = (Person) sess.get(Person.class, updatedTicket.getEditBy());
	} catch (Exception ex) {
	    person = new Person();
	    person.setName("{empty}");
	}

	String[] changes = this.detectChanges(updatedTicket, originalTicket,
		person);
	changeNotes = changes[0];
	publicChangeNotes = changes[1];

	TicketHistory th = null;

	if (!changeNotes.equals("")) {
	    changeNotes = "%hb%{taskChange} %fs%" + person.getName()
		    + "%sf% %df%{[" + updatedTicket.getEditDate().getTime()
		    + "]}%fd%%bh%" + changeNotes;

	    th = copyTicket(updatedTicket);
	    th.setChangeNotes(changeNotes);
	    if (!publicChangeNotes.trim().equals("")) {
		publicChangeNotes = " %hb%{taskChange} %df%{["
			+ updatedTicket.getEditDate().getTime() + "]}%fd%%bh%"
			+ publicChangeNotes;
		th.setNotesPublic(publicChangeNotes);
	    } else {
		th.setNotesPublic(null);
	    }
	}

	return th;
    }

    /**
     * <pre>
     * Create a ticket history and change entry for ticket accept operation.
     * 
     * This method populates TicketHistory object with changes and values from changed ticket.
     * Fetches the unchanged Ticket from the database and does the comparison.
     * 
     * Prepends {taskAccepted} to the changeNotes
     * 
     * Should be called when a Ticket is changed in case of Accept operation
     * </pre>
     * 
     * See {@link ChangeDetector#preUpdate(Ticket)}
     * 
     * @param updatedTicket
     *            the changed ticket
     * @return TicketHistory object populated with changed values and detected changes
     */
    public TicketHistory preAccept(Ticket updatedTicket) {
	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);

	Ticket originalTicket = (Ticket) sess.get(Ticket.class, updatedTicket
		.getId());
	sess.evict(originalTicket);

	TicketHistory th = null;

	th = copyTicket(updatedTicket);

	Person person = null;

	try {
	    person = (Person) sess.get(Person.class, updatedTicket.getEditBy());
	} catch (Exception ex) {
	    person = new Person();
	    person.setName("{empty}");
	}

	String changeNotes;
	String publicChangeNotes;

	String[] changes = this.detectChanges(updatedTicket, originalTicket,
		person);
	changeNotes = changes[0];
	publicChangeNotes = changes[1];

	changeNotes = "%hb%{taskAccepted} %fs%" + person.getName()
		+ "%sf% %df%{[" + updatedTicket.getEditDate().getTime()
		+ "]}%fd%%bh%" + changeNotes;
	th.setChangeNotes(changeNotes);
	if (!publicChangeNotes.trim().equals("")) {
	    publicChangeNotes = "%hb% {taskAccepted} %df%{["
		    + updatedTicket.getEditDate().getTime() + "]}%fd%%bh%"
		    + publicChangeNotes;
	    th.setNotesPublic(publicChangeNotes);
	} else {
	    th.setNotesPublic(null);
	}
	return th;
    }

    /**
     * <pre>
     * Create a ticket history and change entry for ticket close operation.
     * 
     * This method populates TicketHistory object with changes and values from changed ticket.
     * Fetches the unchanged Ticket from the database and does the comparison.
     * 
     * Prepends {taskClosed} to the changeNotes
     * 
     * Should be called when a Ticket is changed as a result of Close operation
     * </pre>
     * 
     * See {@link ChangeDetector#preUpdate(Ticket)}
     * 
     * @param updatedTicket
     *            the changed ticket
     * @return TicketHistory object populated with changed values and detected changes
     * 
     */
    public TicketHistory preClose(Ticket updatedTicket) {
	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);

	Ticket originalTicket = (Ticket) sess.get(Ticket.class, updatedTicket
		.getId());
	sess.evict(originalTicket);

	Person person = null;

	try {
	    person = (Person) sess.get(Person.class, updatedTicket
		    .getClosedBy());
	} catch (Exception ex) {
	    person = new Person();
	    person.setName("{empty}");
	}

	String changeNotes;
	String publicChangeNotes;

	String[] changes = this.detectChanges(updatedTicket, originalTicket,
		person);
	changeNotes = changes[0];
	publicChangeNotes = changes[1];

	TicketHistory th = null;

	th = copyTicket(updatedTicket);

	changeNotes = "%hb%{taskClosed} %fs%" + person.getName()
		+ "%sf% %df%{[" + updatedTicket.getDateClosed().getTime()
		+ "]}%fd%%bh%" + changeNotes;
	th.setChangeNotes(changeNotes);
	if (!publicChangeNotes.trim().equals("")) {
	    publicChangeNotes = "%hb%{taskClosed}%df%{["
		    + updatedTicket.getEditDate().getTime() + "]}%fd%%bh%"
		    + publicChangeNotes;
	    th.setNotesPublic(publicChangeNotes);
	} else {
	    th.setNotesPublic(null);
	}
	// String publicChangeNotes =
	// "{taskClosed} {["+updatedTicket.getDateClosed().getTime()+"]}\n";
	// th.setNotesPublic(publicChangeNotes);
	return th;
    }

    /**
     * <pre>
     * Create a ticket history and change entry for ticket re-open operation.
     * 
     * This method populates TicketHistory object with changes and values from changed ticket.
     * Fetches the unchanged Ticket from the database and does the comparison.
     * 
     * Prepends {teskReopen} to the changeNotes
     * 
     * Should be called when a Ticket is changed as a result of ReOPen operation
     * </pre>
     * 
     * See {@link ChangeDetector#preUpdate(Ticket)}
     * 
     * @param updatedTicket
     *            the changed ticket
     * @return TicketHistory object populated with changed values and detected changes
     */
    public TicketHistory preReopen(Ticket updatedTicket) {
	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);

	Ticket originalTicket = (Ticket) sess.get(Ticket.class, updatedTicket
		.getId());
	sess.evict(originalTicket);

	Person person = null;

	try {
	    person = (Person) sess.get(Person.class, updatedTicket.getEditBy());
	} catch (Exception ex) {
	    person = new Person();
	    person.setName("{empty}");
	}

	String changeNotes;
	String publicChangeNotes;

	String[] changes = this.detectChanges(updatedTicket, originalTicket,
		person);
	changeNotes = changes[0];
	publicChangeNotes = changes[1];

	TicketHistory th = null;

	th = copyTicket(updatedTicket);

	changeNotes = "%hb%{teskReopen} %fs%" + person.getName()
		+ "%sf% %df%{[" + originalTicket.getEditDate().getTime()
		+ "]}%fd%%bh%" + changeNotes;
	th.setChangeNotes(changeNotes);
	if (!publicChangeNotes.trim().equals("")) {
	    publicChangeNotes = "%hb% {teskReopen} %df%{["
		    + updatedTicket.getEditDate().getTime() + "]}%fd%%bh%"
		    + publicChangeNotes;
	    th.setNotesPublic(publicChangeNotes);
	} else {
	    th.setNotesPublic(null);
	}
	// String publicChangeNotes =
	// "{taskClosed} {["+updatedTicket.getDateClosed().getTime()+"]}\n";
	// th.setNotesPublic(publicChangeNotes);
	return th;
    }

    /**
     * <pre>
     * Create a ticket history and change entry for new comment on ticket.
     * 
     * This method populates TicketHistory object with changes and values from changed ticket.
     * Fetches the unchanged Ticket from the database and does the comparison.
     * 
     * Prepends {addedComment} to the changeNotes
     * 
     * Should be called when a Ticket is changed as a result of Comment operation
     * </pre>
     * 
     * See {@link ChangeDetector#preUpdate(Ticket)}
     * 
     * @param updatedTicket
     *            the changed ticket
     * @param comment
     *            text of the comment (Since there is no comment field in ticket)
     * @param clientInformed
     *            if true, then this comment will be sent to the appropriate client
     * @return TicketHistory object populated with changed values and detected changes
     */
    public TicketHistory preComment(Ticket updatedTicket, String comment,
	    boolean clientInformed) {
	Session sess = SessionHolder.currentSession().getSess();
	sess.evict(updatedTicket);

	Person person = null;

	try {
	    person = (Person) sess.get(Person.class, updatedTicket.getEditBy());
	} catch (Exception ex) {
	    person = new Person();
	    person.setName("{empty}");
	}

	TicketHistory th = copyTicket(updatedTicket);
	th.setChangeNotes("%hb%{addedComment} %fs%" + person.getName()
		+ "%sf% %df%{[" + updatedTicket.getEditDate().getTime()
		+ "]}%fd% "
		+ (clientInformed ? "{clientNotified}%bh%" : "%bh%"));

	if (clientInformed)
	    th.setNotesPublic("%hb%{addedComment} %df%{["
		    + updatedTicket.getEditDate().getTime()
		    + "]}%fd%%bh% %dt%%ft%" + comment + "%tf%%td%");

	return th;
    }

    /**
     * <pre>
     * Process input string with markup and substitude markup elements with
     * entries from resource bundle based on the locale provided.
     * 
     * changeFormats resources are used here.
     * 
     * The result will contain decoration merkers.
     * </pre>
     * 
     * @param changeNotes
     *            input string with markup elements
     * @param lc
     *            locale to load the resource bundle for
     * @return transformed input string
     */
    private String formatTextInt(String changeNotes, Locale lc) {
	if (changeNotes == null || lc == null) {
	    return "";
	}
	// Locale.setDefault(new Locale("lt"));
	DateFormat dtf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
		DateFormat.MEDIUM, lc);
	dtf.setTimeZone(TimeZone.getDefault());
	// System.out.println("Locale for date"+lc);
	// System.out.println("Haha  "+dtf.format(new Date()));
	ResourceBundle rb = ResourceBundle.getBundle("changeFormats", lc, this
		.getClass().getClassLoader());
	// System.out.println("Locale for RB"+lc);
	String formatedMessage = changeNotes;

	Pattern pt = Pattern.compile("\\{\\[(\\d+)\\]\\}", Pattern.MULTILINE
		| Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	Matcher mt = pt.matcher(formatedMessage);
	String formatedDate = "";
	while (mt.find()) {
	    for (int i = 1; i <= mt.groupCount(); i++) {
		String timeString = mt.group(i);
		try {
		    formatedDate = dtf.format(new Date(Long
			    .parseLong(timeString)));
		} catch (NumberFormatException ex) {
		}
		formatedMessage = pt.matcher(formatedMessage).replaceFirst(
			formatedDate);
	    }
	}

	pt = Pattern.compile("\\{(\\w+)\\}", Pattern.MULTILINE
		| Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	mt = pt.matcher(formatedMessage);
	String formated = "";
	while (mt.find()) {
	    for (int i = 1; i <= mt.groupCount(); i++) {
		try {
		    formated = rb.getString(mt.group(i));
		} catch (NumberFormatException ex) {
		}
		formatedMessage = pt.matcher(formatedMessage).replaceFirst(
			formated);
	    }
	}
	return formatedMessage;
    }

    /**
     * <pre>
     * Formats string markups with locale based values.
     * Removes decoration merkers.
     * </pre>
     * 
     * @param changeNotes
     *            string to convert
     * @param lc
     *            locale to use
     * @return text formatted string
     */
    public String formatText(String changeNotes, Locale lc) {
	return formatTextInt(changeNotes, lc).replaceAll("\\%df\\%", "")
		.replaceAll("\\%fd\\%", "").replaceAll("\\%fs\\%", "")
		.replaceAll("\\%sf\\%", "").replaceAll("\\%ft\\%", "")
		.replaceAll("\\%tf\\%", "").replaceAll("\\%hb\\%", "")
		.replaceAll("\\%bh\\%", "\n").replaceAll("\\%dt\\%", "")
		.replaceAll("\\%td\\%", "").replaceAll("\\%ei\\%", "")
		.replaceAll("\\%ie\\%", "");
    }

    /**
     * <pre>
     * Formats string markups with locale based values.
     * Replaces decoration merkers with HTML entities.
     * Transforms some text into HTML entities.
     * &gt; as &amp;gt;
     * &lt; as &amp;lt;
     * &quot; as &amp;quot;
     * &amp; as &amp;amp;
     * \n as &lt;br/&gt;
     * </pre>
     * 
     * @param changeNotes
     *            string to convert
     * @param lc
     *            locale to use
     * @return HTML formatted string
     */
    public String formatHtml(String changeNotes, Locale lc) {
	return formatTextInt(changeNotes, lc).replaceAll("\\>", "&gt;")
		.replaceAll("\\<", "&lt;").replaceAll("\\\"", "&quot;")
		.replaceAll("\\&", "&amp;").replaceAll("\\n", "<br/>")
		.replaceAll("\\%df\\%", "<span class=\"date\">").replaceAll(
			"\\%fd\\%", "</span>").replaceAll("\\%fs\\%",
			"<span class=\"field\">").replaceAll("\\%sf\\%",
			"</span>").replaceAll("\\%ft\\%",
			"<span class=\"notes\">").replaceAll("\\%tf\\%",
			"</span>").replaceAll("\\%hb\\%", "<div class=\"hb\">")
		.replaceAll("\\%bh\\%", "</div>").replaceAll("\\%dt\\%",
			"<div class=\"dt\">").replaceAll("\\%td\\%", "</div>")
		.replaceAll("\\%ei\\%", "<span class=\"action\">").replaceAll(
			"\\%ie\\%", "</span>");

    }

    /**
     * Create a ticket copy as a ticket history object
     * 
     * @param t
     *            ticket to copy
     * @return ticket history with data from the ticket
     */
    public TicketHistory copyTicket(Ticket t) {
	TicketHistory tt = new TicketHistory();

	tt.setTicket(t.getId());

	tt.setName(t.getName());
	tt.setCompany(t.getCompany());
	tt.setPerson(t.getPerson());
	tt.setReportDate(t.getReportDate());
	tt.setReportBy(t.getReportBy());
	tt.setDescription(t.getDescription());
	tt.setStatus(t.getStatus());
	tt.setAcceptedNotes(t.getAcceptedNotes());
	tt.setAssignedDate(t.getAssignedDate());
	tt.setAssignedTo(t.getAssignedTo());
	tt.setAssignedBy(t.getAssignedBy());
	tt.setWorktime(t.getWorktime());
	tt.setAdditionalTime(t.getAdditionalTime());
	tt.setPlanedDate(t.getPlanedDate());
	tt.setActualDate(t.getActualDate());
	tt.setServiceCode(t.getServiceCode());
	tt.setPriority(t.getPriority());
	tt.setType(t.getType());
	tt.setResolution(t.getResolution());
	tt.setDateClosed(t.getDateClosed());
	tt.setClosedBy(t.getClosedBy());
	tt.setEditDate(t.getEditDate());
	tt.setEditBy(t.getEditBy());

	return tt;

    }

    /**
     * NULL pointer safe equals method. Both objects have to be non null for equals method is
     * called.
     * 
     * @param a
     *            Object a
     * @param b
     *            Object b
     * @return true, if objects equals method return true or are both null. Otherwise false.
     * @see Object#equals(Object)
     */
    public static boolean safeEquals(Object a, Object b) {
	if (a == null && b == null)
	    return true;
	if (a == null || b == null)
	    return false;
	return a.equals(b);
    }

    /**
     * Safe equals for date objects with minute precision.
     * 
     * Will check for null's. Both dates as null are equals
     * 
     * @param a
     *            date a
     * @param b
     *            data b
     * @return true, if dates have the same minute or are both null. Otherwise false.
     */
    public static boolean safeEqualMuinutes(Date a, Date b) {
	if (a == null && b == null)
	    return true;
	if (a == null || b == null)
	    return false;

	return ((long) a.getTime() / 60000l) == ((long) b.getTime() / 60000l);

    }

    /*
     * public boolean safeEquals(Integer a, Integer b){ if(a == null || b ==
     * null) return false; return a.equals(b); }
     * 
     * public boolean safeEquals(Long a, Long b){ if(a == null || b == null)
     * return false; return a.equals(b); }
     * 
     * public boolean safeEquals(Boolean a, Boolean b){ if(a == null || b ==
     * null) return false; return a.equals(b); }
     * 
     * public boolean safeEquals(Double a, Double b){ if(a == null || b == null)
     * return false; return a.equals(b); }
     * 
     * public boolean safeEquals(Float a, Float b){ if(a == null || b == null)
     * return false; return a.equals(b); }
     * 
     * public boolean safeEquals(Date a, Date b){ if(a == null || b == null)
     * return false; return a.equals(b); }
     */

}
