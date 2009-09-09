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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

/**
 * Mail parser for emails from the email store.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class MailParser {
    MailFilter mf;

    /** Creates a new instance of MailParser */
    public MailParser(MailFilter mf) {
	this.mf = mf;
	if (mf == null)
	    throw new NullPointerException();
    }

    /**
     * Parsing code emails.
     * 
     * Reads emails over the provided session. Calls the MailFilter methods at defined boundaries.
     * See {@link MailFilter} for more info
     * 
     * @param ss
     *            configured mail session
     * @throws MessagingException
     * @throws IOException
     */
    public void parse(Session ss) throws MessagingException, IOException {
	Properties prp = ss.getProperties();
	Store store = ss.getStore(prp
		.getProperty("mail.store.protocol", "pop3"));
	store.connect(prp.getProperty("mail.host"), prp
		.getProperty("mail.user"), prp.getProperty("mail.password"));
	Folder folder = store.getFolder("INBOX");
	folder.open(Folder.READ_WRITE);
	SearchTerm st = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	Message message[] = folder.search(st);
	ArrayList<MimeBodyPart> images = new ArrayList<MimeBodyPart>();
	ArrayList<String> texts = new ArrayList<String>();

	for (Message mg : message) {
	    MimeMessage msg = (MimeMessage) mg;
	    // System.out.println("Reading mail: "+msg.getSubject());
	    mf.filterStart(msg);
	    Object content = msg.getContent();
	    if (content instanceof String) {
		mf.filterText(new String[] { content.toString() });
	    } else if (content instanceof MimeMultipart) {
		MimeMultipart mmp = (MimeMultipart) content;
		for (int i = 0; i < mmp.getCount(); i++) {
		    MimeBodyPart mbp = (MimeBodyPart) mmp.getBodyPart(i);
		    if (mbp.getContentType().toLowerCase().indexOf("text/") >= 0) {
			texts.add(mbp.getContent().toString());
		    } else if (msg.getContentType().toLowerCase().indexOf(
			    "image/") >= 0) {
			images.add(mbp);
		    } else {
			// System.out.println("Sorry content-type: "+mbp.getContentType());
		    }
		}
		if (!texts.isEmpty()) {
		    mf.filterText(texts.toArray(new String[texts.size()]));
		}

		if (!images.isEmpty()) {
		    mf.filterImages(images.toArray(new MimeBodyPart[images
			    .size()]));
		}
	    } else if (content instanceof InputStream) {
		if (msg.getContentType().toLowerCase().indexOf("text/") >= 0) {
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    InputStream is = (InputStream) content;
		    int i = 0;
		    while ((i = is.read()) >= 0) {
			bos.write(i);
		    }
		    mf
			    .filterText(new String[] { new String(bos
				    .toByteArray()) });
		} else if (msg.getContentType().toLowerCase().indexOf("image/") >= 0) {
		} else {
		    // System.out.println("Sorry content-type: "+msg.getContentType());
		}
	    }
	    msg.setFlag(Flags.Flag.SEEN, true);
	    mf.filterEnd(msg);
	}
	// if(message == null || message.length == 0)
	// System.out.println("Sorry no messages");
	folder.close(true);
	store.close();
    }

}
