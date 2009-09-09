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

package eu.activelogic.mailparse;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

/**
 * A test application on how to work with IMAP over JavaMail
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class IMAPReader {

    /** Creates a new instance of IMAPReader */
    public IMAPReader() {
    }

    /**
     * Sample test script
     */
    public static void main(String[] args) {
	try {
	    Properties prp = new Properties();

	    prp.put("mail.store.protocol", "imap");
	    prp.put("mail.transport.protocol", "smtp");
	    prp.put("mail.host", "mail.example.com");
	    prp.put("mail.user", "alex");
	    prp.put("mail.from", "me@example.com");
	    prp.put("mail.password", "");

	    Session session = Session.getInstance(prp, null);
	    Store store = session.getStore("imap");
	    store.connect("mail.example.com", "alex", "");
	    Folder folder = store.getFolder("INBOX");
	    folder.open(Folder.READ_WRITE);
	    SearchTerm st = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	    Message message[] = folder.search(st);
	    for (Message mg : message) {
		MimeMessage msg = (MimeMessage) mg;
		System.out.println("From: " + msg.getSender());
		System.out.println("Subject: " + msg.getSubject());
		System.out
			.println("\n=========================================================");
		Object content = msg.getContent();
		if (content instanceof String) {
		    System.out.println(content);
		} else if (content instanceof MimeMultipart) {
		    MimeMultipart mmp = (MimeMultipart) content;
		    for (int i = 0; i < mmp.getCount(); i++) {
			MimeBodyPart mbp = (MimeBodyPart) mmp.getBodyPart(i);
			if (mbp.getContentType().indexOf("text/") >= 0) {
			    System.out.println(mbp.getContent());
			} else {
			    System.out.println("Sorry content-type: "
				    + mbp.getContentType());
			}
		    }
		} else if (content instanceof InputStream) {
		    if (msg.getContentType().indexOf("text/") >= 0) {
			InputStream is = (InputStream) content;
			int i = 0;
			while ((i = is.read()) >= 0) {
			    System.out.write(i);
			}
		    } else {
			System.out.println("Sorry content-type: "
				+ msg.getContentType());
		    }
		}

		System.out
			.println("\n=========================================================");
		msg.setFlag(Flags.Flag.SEEN, true);
	    }
	    if (message == null || message.length == 0)
		System.out.println("Sorry no messages");
	    folder.close(true);
	    store.close();
	} catch (Exception excasdasd) {
	    excasdasd.printStackTrace();
	}
    }

}
