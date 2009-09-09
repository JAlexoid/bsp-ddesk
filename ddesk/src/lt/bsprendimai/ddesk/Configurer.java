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

import java.io.FileOutputStream;
import java.util.Properties;

import lt.bsprendimai.Configuration;

/**
 * Backing bean for configuration page of the application.
 * 
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class Configurer {

    private UserHandler userHandler;

    private Properties prp = Configuration.getPreferences();

    /** Creates a new instance of Configurer */
    public Configurer() {

    }

    /**
     * Save configuration to location specified by {@link Configuration#getPropertiesFile()}
     * 
     * @return value based on result. See {@link StandardResults}
     */
    public String saveConfiguration() {
	try {
	    prp.store(new FileOutputStream(Configuration.getPropertiesFile()),
		    "");
	} catch (Exception ex) {
	    ex.printStackTrace();
	    String message = UIMessenger.getMessage(getUserHandler()
		    .getUserLocale(), "application.error.io");
	    UIMessenger.addInfoMessage(message, "");
	    return StandardResults.FAIL;
	}
	return StandardResults.STAY;
    }

    public String getMail() {
	return prp.getProperty("ddesk.mail.to", "");
    }

    public void setMail(String mail) {
	prp.setProperty("ddesk.mail.to", mail);
    }

    public String getAdminMail() {
	return prp.getProperty("ddesk.mail.admin", "");
    }

    public void setAdminMail(String mail) {
	prp.setProperty("ddesk.mail.admin", mail);
    }

    public String getSubjectAdd() {
	return prp.getProperty("ddesk.mail.subject.added", "");
    }

    public void setSubjectAdd(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.added", subjectAdd);
    }

    public String getSubjectChange() {
	return prp.getProperty("ddesk.mail.subject.changed", "");
    }

    public void setSubjectChange(String subjectChange) {
	prp.setProperty("ddesk.mail.subject.changed", subjectChange);
    }

    public String getSubjectClose() {
	return prp.getProperty("ddesk.mail.subject.closed", "");
    }

    public void setSubjectClose(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.closed", subjectAdd);
    }

    public String getSubjectAccept() {
	return prp.getProperty("ddesk.mail.subject.accepted", "");
    }

    public void setSubjectAccept(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.accepted", subjectAdd);
    }

    public String getSubjectComment() {
	return prp.getProperty("ddesk.mail.subject.comment", "");
    }

    public void setSubjectComment(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.comment", subjectAdd);
    }

    public String getSubjectClientEdit() {
	return prp.getProperty("ddesk.mail.subject.clientEdit", "");
    }

    public void setSubjectClientEdit(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.clientEdit", subjectAdd);
    }

    public String getSubjectReopen() {
	return prp.getProperty("ddesk.mail.subject.reopen", "");
    }

    public void setSubjectReopen(String subjectAdd) {
	prp.setProperty("ddesk.mail.subject.reopen", subjectAdd);
    }

    public String getMailFrom() {
	return prp.getProperty("ddesk.mail.from", "");
    }

    public void setMailFrom(String mailFrom) {
	prp.setProperty("ddesk.mail.from", mailFrom);
	prp.setProperty("mail.from", mailFrom);
    }

    public String getSubjectAddedClient() {
	return prp.getProperty("ddesk.mail.subject.added.client", "");
    }

    public void setSubjectAddedClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.added.client", mailFrom);
    }

    public String getSubjectAcceptedClient() {
	return prp.getProperty("ddesk.mail.subject.accepted.client", "");
    }

    public void setSubjectAcceptedClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.accepted.client", mailFrom);
    }

    public String getSubjectClosedClient() {
	return prp.getProperty("ddesk.mail.subject.closed.client", "");
    }

    public void setSubjectClosedClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.closed.client", mailFrom);
    }

    public String getSubjectReopenClient() {
	return prp.getProperty("ddesk.mail.subject.reopen.client", "");
    }

    public void setSubjectReopenClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.reopen.client", mailFrom);
    }

    public String getSubjectCommentClient() {
	return prp.getProperty("ddesk.mail.subject.comment.client", "");
    }

    public void setSubjectCommentClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.comment.client", mailFrom);
    }

    public String getSubjectChangedClient() {
	return prp.getProperty("ddesk.mail.subject.changed.client", "");
    }

    public void setSubjectChangedClient(String mailFrom) {
	prp.setProperty("ddesk.mail.subject.changed.client", mailFrom);
    }

    public String getSMTPHost() {
	return prp.getProperty("mail.smtp.host", "");
    }

    public void setSMTPHost(String host) {
	prp.setProperty("mail.smtp.host", host);
    }

    public String getSMTPUser() {
	return prp.getProperty("mail.smtp.user", "");
    }

    public void setSMTPUser(String username) {
	prp.setProperty("mail.smtp.user", username);
    }

    public String getSMTPPassword() {
	return prp.getProperty("mail.smtp.password", "");
    }

    public void setSMTPPassword(String password) {
	prp.setProperty("mail.smtp.password", password);
    }

    public boolean getSMTPAuthentication() {
	return prp.containsKey("mail.authenticate");
    }

    public void setSMTPAuthentication(boolean authentication) {
	if (authentication) {
	    prp.setProperty("mail.authenticate", "yes");
	} else {
	    prp.remove("mail.authenticate");
	}
    }

    public boolean getMailActivated() {
	return "true".equalsIgnoreCase(prp.getProperty("ddesk.mail.activated"));
    }

    public void setMailActivated(boolean mailActivated) {
	prp.setProperty("ddesk.mail.activated", mailActivated ? "true"
		: "false");

    }

    public UserHandler getUserHandler() {
	return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
	this.userHandler = userHandler;
    }

}
