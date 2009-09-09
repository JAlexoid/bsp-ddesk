package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class XmppLog extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer ticket;

    /** nullable persistent field */
    private Integer project;

    /** persistent field */
    private String name;

    /** persistent field */
    private Date logTime;

    /** nullable persistent field */
    private String messages;

    /** full constructor */
    public XmppLog(Integer ticket, Integer project, String name, Date logTime,
	    String messages) {
	this.ticket = ticket;
	this.project = project;
	this.name = name;
	this.logTime = logTime;
	this.messages = messages;
    }

    /** default constructor */
    public XmppLog() {
    }

    /** minimal constructor */
    public XmppLog(String name, Date logTime) {
	this.name = name;
	this.logTime = logTime;
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getTicket() {
	return this.ticket;
    }

    public void setTicket(Integer ticket) {
	this.ticket = ticket;
    }

    public Integer getProject() {
	return this.project;
    }

    public void setProject(Integer project) {
	this.project = project;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getLogTime() {
	return this.logTime;
    }

    public void setLogTime(Date logTime) {
	this.logTime = logTime;
    }

    public String getMessages() {
	return this.messages;
    }

    public void setMessages(String messages) {
	this.messages = messages;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

}
