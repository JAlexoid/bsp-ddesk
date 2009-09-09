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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import lt.bsprendimai.Configuration;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.ProjectInfo;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.dao.TicketHistory;
import lt.bsprendimai.mailtools.Mailer;

/**
 * Message formatter and sender.
 * Formats and sends messages in a particular order and recipients
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class MessageHandler {

    TicketAccessor ti;
    private ChangeDetector cdt = ChangeDetector.getInstance();

    static final SimpleDateFormat sdtf;
    static final SimpleDateFormat sdf;

    static {
	sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	sdtf.setTimeZone(TimeZone.getTimeZone("GMT"));
	sdf = new SimpleDateFormat("yyyy-MM-dd");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /** Creates a new instance of MessageFormatter */
    public MessageHandler(TicketAccessor ti) {
	this.ti = ti;
    }

    public boolean sendMessagesAdded() {
	try {
	    String map = Configuration
		    .getResourceString("/mails/extMessage_lt.txt");
	    String date = sdtf.format(ti.getSelectedInfo().getReportDate());

	    String subject = MessageFormat.format(Configuration
		    .getPreferences().getProperty("ddesk.mail.subject.added"),
		    date, ti.getSelectedInfo().getEventName(), ti
			    .getSelectedInfo().getName(), ti.getSelectedInfo()
			    .getUniqueId(), ti.getSelectedInfo()
			    .getSeverityName());

	    map = map.replaceAll("\\{priorityName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPriorityName()));
	    map = map.replaceAll("\\{typeName\\}", ti.getSafe(ti
		    .getSelectedInfo().getEventName()));
	    map = map.replaceAll("\\{severityName\\}", ti.getSafe(ti
		    .getSelectedInfo().getSeverityName()));
	    map = map.replaceAll("\\{personName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonName()));
	    map = map.replaceAll("\\{companyName\\}", ti.getSafe(ti
		    .getSelectedInfo().getCompanyName()));
	    map = map.replaceAll("\\{personEmail\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonEmail()));
	    map = map.replaceAll("\\{personPosition\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonPosition()));
	    map = map.replaceAll("\\{phoneNo\\}", ti.getSafe(ti
		    .getSelectedInfo().getPhoneNo()));
	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{requestNotes\\}", ti.getSafe(ti
		    .getSelectedInfo().getDescription()));
	    map = map.replaceAll("\\{history\\}", "");

	    sendToAdder(subject, map, "text/plain;charset=UTF-8");
	    sendToPerson(subject, map, "text/plain;charset=UTF-8");

	    subject = MessageFormat.format(Configuration.getPreferences()
		    .getProperty("ddesk.mail.subject.added.client"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = Configuration.getResourceString("/mails/intMessage.txt");

	    map = map.replaceAll("\\{priorityName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPriorityName()));
	    map = map.replaceAll("\\{personName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonName()));
	    map = map.replaceAll("\\{companyName\\}", ti.getSafe(ti
		    .getSelectedInfo().getCompanyName()));
	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{requestNotes\\}", ti.getSafe(ti
		    .getSelectedInfo().getDescription()));
	    map = map.replaceAll("\\{personEmail\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonEmail()));
	    map = map.replaceAll("\\{personPosition\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonPosition()));
	    map = map.replaceAll("\\{phoneNo\\}", ti.getSafe(ti
		    .getSelectedInfo().getPhoneNo()));

	    map = map.replaceAll("\\{history\\}", "");

	    sendToAll(subject, map, "text/plain;charset=UTF-8");

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}

    }

    public boolean sendMessagesAccepted(TicketHistory th) {
	try {
	    String map = Configuration
		    .getResourceString("/mails/accMessage_lt.txt");
	    String date = sdf.format(ti.getSelectedInfo().getReportDate());

	    String subject = MessageFormat.format(Configuration
		    .getPreferences().getProperty(
			    "ddesk.mail.subject.accepted.client"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));

	    sendToPerson(subject, map, "text/plain;charset=UTF-8");

	    map = Configuration.getResourceString("/mails/accMessage_int.txt");

	    subject = MessageFormat.format(Configuration.getPreferences()
		    .getProperty("ddesk.mail.subject.accepted"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{history\\}", cdt.formatText(th
		    .getChangeNotes(), new Locale("lt")));

	    sendToAdmin(subject, map, "text/plain;charset=UTF-8");
	    sendToAsignee(subject, map, "text/plain;charset=UTF-8");
	    sendToManager(subject, map, "text/plain;charset=UTF-8");

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
    }

    public boolean sendMessagesClosed(TicketHistory th) {
	try {
	    String map = Configuration
		    .getResourceString("/mails/closedMessage_lt.txt");
	    String date = sdf.format(ti.getSelectedInfo().getReportDate());

	    String subject = MessageFormat.format(Configuration
		    .getPreferences().getProperty(
			    "ddesk.mail.subject.closed.client"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{resolution\\}", ti.getSafe(ti
		    .getSelectedInfo().getResolution()));

	    sendToPerson(subject, map, "text/plain;charset=UTF-8");

	    map = Configuration
		    .getResourceString("/mails/closedMessage_int.txt");

	    subject = MessageFormat.format(Configuration.getPreferences()
		    .getProperty("ddesk.mail.subject.closed"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{resolution\\}", ti.getSafe(ti
		    .getSelectedInfo().getResolution()));
	    map = map.replaceAll("\\{history\\}", cdt.formatText(th
		    .getChangeNotes(), new Locale("lt")));

	    sendToAdmin(subject, map, "text/plain;charset=UTF-8");
	    sendToAsignee(subject, map, "text/plain;charset=UTF-8");
	    sendToManager(subject, map, "text/plain;charset=UTF-8");

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
    }

    public boolean sendMessagesReopened(TicketHistory th) {
	try {
	    String map = Configuration
		    .getResourceString("/mails/reopenedMessage.txt");
	    String date = sdf.format(ti.getSelectedInfo().getReportDate());

	    String subject = MessageFormat.format(Configuration
		    .getPreferences().getProperty(
			    "ddesk.mail.subject.reopen.client"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{reason\\}", ti.getSafe(ti.getComment()));

	    sendToPerson(subject, map, "text/plain;charset=UTF-8");

	    map = Configuration
		    .getResourceString("/mails/reopenedMessage_int.txt");

	    subject = MessageFormat.format(Configuration.getPreferences()
		    .getProperty("ddesk.mail.subject.reopen"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{reason\\}", ti.getSafe(ti.getComment()));
	    map = map.replaceAll("\\{history\\}", cdt.formatText(th
		    .getChangeNotes(), new Locale("lt")));

	    sendToAdmin(subject, map, "text/plain;charset=UTF-8");
	    sendToAsignee(subject, map, "text/plain;charset=UTF-8");
	    sendToManager(subject, map, "text/plain;charset=UTF-8");

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
    }

    public boolean sendMessagesComment(TicketHistory th) {
	try {
	    String map = Configuration
		    .getResourceString("/mails/commentMessage_lt.txt");
	    String date = sdf.format(ti.getSelectedInfo().getReportDate());

	    String subject = MessageFormat.format(Configuration
		    .getPreferences().getProperty(
			    "ddesk.mail.subject.comment.client"), date, ti
		    .getSelectedInfo().getPriorityName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{comment\\}", ti.getSafe(th.getNotes()));

	    sendToPerson(subject, map, "text/plain;charset=UTF-8");

	    map = Configuration
		    .getResourceString("/mails/commentMessage_int.txt");

	    subject = MessageFormat.format(Configuration.getPreferences()
		    .getProperty("ddesk.mail.subject.comment"), date, ti
		    .getSelectedInfo().getEventName(), ti.getSelectedInfo()
		    .getName(), ti.getSelectedInfo().getUniqueId(), ti
		    .getSelectedInfo().getSeverityName());

	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{comment\\}", ti.getSafe(th.getNotes()));
	    map = map.replaceAll("\\{history\\}", cdt.formatText(ti.getSafe(th
		    .getChangeNotes()), new Locale("lt")));

	    sendToAdmin(subject, map, "text/plain;charset=UTF-8");
	    sendToAsignee(subject, map, "text/plain;charset=UTF-8");
	    sendToManager(subject, map, "text/plain;charset=UTF-8");

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
    }

    public boolean sendMessagesChange(TicketHistory th) {
	try {
	    String map = Configuration
		    .getResourceString("/mails/intMessage.txt");

	    String date = sdf.format(ti.getSelectedInfo().getReportDate());

	    String subject;

	    if (ti.getUserHandler().getUser().getCompany() == 0) {
		subject = MessageFormat.format(Configuration.getPreferences()
			.getProperty("ddesk.mail.subject.changed"), date, ti
			.getSelectedInfo().getEventName(), ti.getSelectedInfo()
			.getName(), ti.getSelectedInfo().getUniqueId(), ti
			.getSelectedInfo().getSeverityName());
	    } else {
		subject = MessageFormat.format(Configuration.getPreferences()
			.getProperty("ddesk.mail.subject.clientEdit"), date, ti
			.getSelectedInfo().getEventName(), ti.getSelectedInfo()
			.getName(), ti.getSelectedInfo().getUniqueId(), ti
			.getSelectedInfo().getSeverityName());
	    }

	    map = map.replaceAll("\\{priorityName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPriorityName()));
	    map = map.replaceAll("\\{personName\\}", ti.getSafe(ti
		    .getSelectedInfo().getPersonName()));
	    map = map.replaceAll("\\{companyName\\}", ti.getSafe(ti
		    .getSelectedInfo().getCompanyName()));
	    map = map.replaceAll("\\{name\\}", ti.getSafe(ti.getSelectedInfo()
		    .getName()));
	    map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
	    map = map.replaceAll("\\{requestNotes\\}", ti.getSafe(ti
		    .getSelectedInfo().getDescription()));
	    map = map.replaceAll("\\{personPhone\\}", ti.getSafe(ti
		    .getSelectedInfo().getPhoneNo()));
	    map = map.replaceAll("\\{history\\}", cdt.formatText(th
		    .getChangeNotes(), new Locale("lt")));

	    sendToAdder(subject, map, "text/plain;charset=UTF-8");
	    sendToAdmin(subject, map, "text/plain;charset=UTF-8");
	    sendToAsignee(subject, map, "text/plain;charset=UTF-8");
	    sendToManager(subject, map, "text/plain;charset=UTF-8");

	    if (th.getNotesPublic() != null
		    && !th.getNotesPublic().trim().equals("")
		    && th.getDateClosed() == null
		    && ti.getSelected().getPerson() != null) {

		subject = MessageFormat.format(Configuration.getPreferences()
			.getProperty("ddesk.mail.subject.changed.client"),
			date, ti.getSelectedInfo().getEventName(), ti
				.getSelectedInfo().getName(), ti
				.getSelectedInfo().getUniqueId(), ti
				.getSelectedInfo().getSeverityName());

		map = Configuration
			.getResourceString("/mails/extMessage_lt.txt");

		map = map.replaceAll("\\{priorityName\\}", ti.getSafe(ti
			.getSelectedInfo().getPriorityName()));
		map = map.replaceAll("\\{personName\\}", ti.getSafe(ti
			.getSelectedInfo().getPersonName()));
		map = map.replaceAll("\\{companyName\\}", ti.getSafe(ti
			.getSelectedInfo().getCompanyName()));
		map = map.replaceAll("\\{name\\}", ti.getSafe(ti
			.getSelectedInfo().getName()));
		map = map.replaceAll("\\{reportDate\\}", ti.getSafe(date));
		map = map.replaceAll("\\{requestNotes\\}", ti.getSafe(ti
			.getSelectedInfo().getDescription()));
		map = map.replaceAll("\\{personPhone\\}", ti.getSafe(ti
			.getSelectedInfo().getPhoneNo()));
		map = map.replaceAll("\\{history\\}", cdt.formatText(th
			.getNotesPublic(), new Locale("lt")));

		sendToPerson(subject, map, "text/plain;charset=UTF-8");
	    }

	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}

    }

    private void sendToAdder(String subject, String body, String mime) {
	try {

	    Mailer mlr = new Mailer(Configuration.getPreferences().getProperty(
		    "ddesk.mail.from"), ti.getUserHandler().getUser()
		    .getEmail());
	    mlr.send(subject, body, mime);

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void sendToPerson(String subject, String body, String mime) {
	try {
	    if (ti.getSelected().getPerson() != null) {
		Person prs = (Person) SessionHolder.currentSession().getSess()
			.get(Person.class, ti.getSelected().getPerson());
		Mailer mlr = new Mailer(Configuration.getPreferences()
			.getProperty("ddesk.mail.from"), prs.getEmail());
		mlr.send(subject, body, mime);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void sendToAdmin(String subject, String body, String mime) {
	try {
	    Mailer mlr = new Mailer(Configuration.getPreferences().getProperty(
		    "ddesk.mail.from"), Configuration.getPreferences()
		    .getProperty("ddesk.mail.admin"));
	    mlr.send(subject, body, mime);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void sendToAll(String subject, String body, String mime)
	    throws Exception {
	try {
	    Mailer mlr = new Mailer(Configuration.getPreferences().getProperty(
		    "ddesk.mail.from"), Configuration.getPreferences()
		    .getProperty("ddesk.mail.to"));
	    mlr.send(subject, body, mime);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void sendToAsignee(String subject, String body, String mime) {
	try {
	    if (ti.getSelected().getAssignedTo() != null) {
		Person prs = (Person) SessionHolder.currentSession().getSess()
			.get(Person.class, ti.getSelected().getAssignedTo());
		Mailer mlr = new Mailer(Configuration.getPreferences()
			.getProperty("ddesk.mail.from"), prs.getEmail());
		mlr.send(subject, body, mime);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void sendToManager(String subject, String body, String mime) {
	try {
	    if (ti.getSelected().getProject() != null) {
		ProjectInfo prs = (ProjectInfo) SessionHolder.currentSession()
			.getSess().get(ProjectInfo.class,
				ti.getSelected().getProject());
		if (prs.getManagerEmail() != null) {
		    Mailer mlr = new Mailer(Configuration.getPreferences()
			    .getProperty("ddesk.mail.from"), prs
			    .getManagerEmail());
		    mlr.send(subject, body, mime);
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}
