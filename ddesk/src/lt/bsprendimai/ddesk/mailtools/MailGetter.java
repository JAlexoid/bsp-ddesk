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
package lt.bsprendimai.ddesk.mailtools;

import java.beans.PropertyVetoException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import lt.bsprendimai.ddesk.TicketAccessor;
import lt.bsprendimai.ddesk.dao.PersonInfo;
import lt.bsprendimai.ddesk.dao.SessionHolder;
import lt.bsprendimai.ddesk.textmarks.ChangeCommitListener;
import lt.bsprendimai.ddesk.textmarks.CommandContext;
import lt.bsprendimai.ddesk.textmarks.TextSuggest;
import lt.bsprendimai.mailtools.Mailer;
import eu.activelogic.mailparse.MailFilter;

/**
 * Filter for emails. Is used to execute operations based on the mail content.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
@SuppressWarnings("unchecked")
public class MailGetter implements MailFilter, ChangeCommitListener {

    private HashMap<String, PersonInfo> mailToPerson = new HashMap<String, PersonInfo>();
    private MimeMessage lastMessage;
    String[] text = null;
    private TextSuggest ts = null;

    /**
     * Creates a new instance of MailGetter
     * 
     * Pre-loads PersonInfo data.
     */
    public MailGetter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @seeeu.activelogic.mailparse.MailFilter#filterStart(javax.mail.internet.
     * MimeMessage)
     */
    @Override
    public void filterStart(MimeMessage msg) {
	synchronized (msg) {
	    int uid = 0;
	    try {
		InternetAddress[] ias = ((InternetAddress[]) msg.getFrom());
		for (InternetAddress ia : ias) {
		    if (ia != null && mailToPerson.containsKey(ia.getAddress())) {
			uid = mailToPerson.get(ia.getAddress()).getId();
			break;
		    }
		}
	    } catch (MessagingException ex) {
	    }
	    if (ts == null) {
		ts = new TextSuggest(uid);
		ts.addListener(this);
	    } else {
		ts.getUserHandler().changeUser(uid);
	    }
	    lastMessage = msg;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.activelogic.mailparse.MailFilter#filterEnd(javax.mail.internet.MimeMessage
     * )
     */
    @Override
    public void filterEnd(MimeMessage msg) {
	synchronized (msg) {
	    try {

		StringBuilder sb = new StringBuilder();
		// System.out.println("Got message:"+msg.getSubject());
		if (!msg.getSubject().trim().toLowerCase().startsWith(":mte")) {
		    sb.append(":new\n");
		    sb.append(msg.getSubject());
		    sb.append("\n");
		}

		for (String gg : this.text) {
		    sb.append(gg);
		}

		if (msg.getSubject().trim().toLowerCase().startsWith(":mte")) {
		    System.out.println("MTE");
		    ts.setAllowCycles(true);
		    ts.extract(sb.toString());
		} else {
		    System.out.println("SINGLE");
		    ts.setAllowCycles(false);
		    ts.extract(sb.toString());
		}
	    } catch (MessagingException ex) {
		ex.printStackTrace();
	    }
	    lastMessage = null;
	    ts.getTickets().setQPerson(null);
	    ts.getTickets().setQProject(null);
	    SessionHolder.restartTransaction();

	    try {
		Address id = msg.getSender();
		if (id == null) {
		    id = msg.getFrom()[0];
		}
		Mailer mlr = new Mailer("desk@no-reply.com", id.toString());
		mlr.send("Processed " + msg.getSubject(), "Success",
			"text/plain");
	    } catch (Exception ex) {
	    }

	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.activelogic.mailparse.MailFilter#filterImages(javax.mail.internet.
     * MimeBodyPart[])
     */
    @Override
    public void filterImages(MimeBodyPart[] msg) {
	return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.activelogic.mailparse.MailFilter#filterText(java.lang.String[])
     */
    @Override
    public void filterText(String[] msg) {
	this.text = msg;
    }

    /*
     * (non-Javadoc)
     * 
     * @seelt.bsprendimai.ddesk.textmarks.ChangeCommitListener#updateEvent(lt.
     * bsprendimai.ddesk.textmarks.CommandContext)
     */
    @Override
    public void updateEvent(CommandContext cc) throws PropertyVetoException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * lt.bsprendimai.ddesk.textmarks.ChangeCommitListener#addEvent(lt.bsprendimai
     * .ddesk.textmarks.CommandContext)
     */
    @Override
    public void addEvent(CommandContext cc) throws PropertyVetoException {
	try {
	    TicketAccessor ta = (TicketAccessor) cc.getHandler();
	    InternetAddress[] ias = ((InternetAddress[]) lastMessage.getFrom());
	    StringBuilder mail = new StringBuilder();
	    for (InternetAddress ia : ias) {
		mail.append(ia.getAddress());
		mail.append(" ");
	    }
	    try {
		ta.getSelected().setReporterMail(mail.toString());
	    } catch (Exception excasdasd) {
	    }
	    if (lastMessage.getSentDate() != null)
		ta.getSelected().setReportDate(lastMessage.getSentDate());
	    else
		ta.getSelected().setReportDate(new Date());
	} catch (MessagingException ex) {
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.activelogic.mailparse.MailFilter#cycleEnd()
     */
    @Override
    public void cycleEnd() {
	SessionHolder.endSession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.activelogic.mailparse.MailFilter#cycleBegin()
     */
    @Override
    public void cycleBegin() {
	try {
	    SessionHolder.currentSession();

	    List<PersonInfo> pi = (List<PersonInfo>) SessionHolder
		    .currentSession().getSess().createQuery(
			    "FROM " + PersonInfo.class).list();

	    for (PersonInfo pp : pi) {
		try {
		    InternetAddress[] ias = InternetAddress.parse(
			    pp.getEmail(), false);
		    for (InternetAddress ia : ias) {
			mailToPerson.put(ia.getAddress(), pp);
		    }
		} catch (Exception ex) {

		}
	    }

	    SessionHolder.closeSession();
	} catch (Exception excasdasd) {
	    SessionHolder.endSession();

	}
    }

    public HashMap<String, PersonInfo> getMailToPerson() {
	return mailToPerson;
    }

    public void setMailToPerson(HashMap<String, PersonInfo> mailToPerson) {
	this.mailToPerson = mailToPerson;
    }

}