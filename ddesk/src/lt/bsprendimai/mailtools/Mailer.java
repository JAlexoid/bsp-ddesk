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
package lt.bsprendimai.mailtools;

import java.io.File;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import lt.bsprendimai.Configuration;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class Mailer {
    private InternetAddress from = null;
    private InternetAddress[] to = null;

    /** Creates a new instance of Mailer */
    public Mailer() {
    }

    /** Creates a new instance of Mailer */
    public Mailer(String from, String to) throws Exception {
	this.from = new InternetAddress(from);
	if (to != null)
	    this.to = InternetAddress.parse(to);
    }

    private boolean isActivated() {
	return "true".equalsIgnoreCase(Configuration.getPreferences()
		.getProperty("ddesk.mail.activated"));
    }

    public void sendFile(String subj, File data) throws Exception {
	if (!isActivated())
	    return;
	System.setProperty("mail.mime.charset", "UTF-8");
	MimeMessage msg = new MimeMessage(Configuration.getMailSession());

	InternetAddress[] address;
	if (to == null)
	    address = InternetAddress.parse(Configuration.getPreferences()
		    .getProperty("tbs.mail.mailto"));
	else
	    address = to;

	if (address.length == 0)
	    throw new Exception("No recipients defined in smtp.properties file");

	if (from != null)
	    msg.setFrom(from);

	msg.setRecipients(Message.RecipientType.TO, address);

	msg.setSubject(MimeUtility.encodeText(subj.replaceAll("\\n", ""),
		"UTF-8", null));

	Multipart mp = new MimeMultipart();
	MimeBodyPart mbp = new MimeBodyPart();

	// attach the file to the message
	FileDataSource fds = new javax.activation.FileDataSource(data);
	mbp.setDataHandler(new DataHandler(fds));
	mbp.setFileName(fds.getName());

	mp.addBodyPart(mbp);

	// add the Multipart to the message
	msg.setContent(mp);

	// set the Date: header
	msg.setSentDate(new Date());

	msg.saveChanges(); // don't forget this
	transportSend(msg, address);

    }

    public void send(String subj, String data, String mime) throws Exception {
	if (!isActivated())
	    return;
	System.setProperty("mail.mime.charset", "UTF-8");
	MimeMessage msg = new MimeMessage(Configuration.getMailSession());
	InternetAddress[] address;
	if (to == null)
	    address = InternetAddress.parse(Configuration.getPreferences()
		    .getProperty("ddesk.mail.mailto"));
	else
	    address = to;

	if (address.length == 0)
	    throw new Exception("No recipients defined in smtp.properties file");

	if (from != null)
	    msg.setFrom(from);

	// msg.setHeader("Content-Type",mime+" ;charset=UTF-8");
	msg.setRecipients(Message.RecipientType.TO, address);

	msg.setSubject(MimeUtility.encodeText(subj.replaceAll("\\n", ""),
		"UTF-8", null));

	msg
		.setDataHandler(new DataHandler(new ByteArrayDataSource(data,
			mime)));

	// set the Date: header
	msg.setSentDate(new Date());

	// send the message

	msg.saveChanges(); // don't forget this
	transportSend(msg, address);
    }

    public void sendWith(String subj, String data, String mime, File[] files)
	    throws Exception {
	if (!isActivated())
	    return;
	System.setProperty("mail.mime.charset", "UTF-8");
	MimeMessage msg = new MimeMessage(Configuration.getMailSession());
	InternetAddress[] address;
	if (to == null)
	    address = InternetAddress.parse(Configuration.getPreferences()
		    .getProperty("ddesk.mail.mailto"));
	else
	    address = to;

	if (address.length == 0)
	    throw new Exception("No recipients defined in smtp.properties file");

	if (from != null)
	    msg.setFrom(from);

	msg.setRecipients(Message.RecipientType.TO, address);

	msg.setSubject(MimeUtility.encodeText(subj.replaceAll("\\n", ""),
		"UTF-8", null));

	msg.setHeader("Content-Type", mime + " ;charset=UTF-8");
	Multipart mp = new MimeMultipart();
	// create and fill the first message part
	MimeBodyPart mbp = new MimeBodyPart();
	mbp
		.setDataHandler(new DataHandler(new ByteArrayDataSource(data,
			mime)));
	mp.addBodyPart(mbp);

	for (File f : files) {
	    // create the second message part
	    mbp = new MimeBodyPart();

	    // attach the file to the message
	    FileDataSource fds = new FileDataSource(f);
	    mbp.setDataHandler(new DataHandler(fds));
	    mbp.setFileName(f.getName());
	    mp.addBodyPart(mbp);

	}

	// add the Multipart to the message
	msg.setContent(mp);

	// set the Date: header
	msg.setSentDate(new Date());

	// send the message

	msg.saveChanges(); // don't forget this

	transportSend(msg, address);

    }

    private void transportSend(MimeMessage msg, InternetAddress[] address)
	    throws Exception {
	Transport tr = Configuration.getMailSession().getTransport(
		Configuration.getPreferences().getProperty(
			"mail.transport.protocol", "smtp"));
	if (!tr.isConnected()) {
	    if (Configuration.getPreferences().containsKey("mail.authenticate")) {
		tr.connect(Configuration.getPreferences().getProperty(
			"mail.smtp.user"), Configuration.getPreferences()
			.getProperty("mail.smtp.password"));
	    } else {
		tr.connect();
	    }
	}
	tr.sendMessage(msg, address);
    }

}
