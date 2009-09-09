package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TicketHistory extends AbstractTicket implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private Integer ticket;

    /** nullable persistent field */
    private String changeNotes;

    /** persistent field */
    private String name;

    /** persistent field */
    private Integer company;

    /** nullable persistent field */
    private Integer person;

    /** persistent field */
    private Date reportDate;

    /** nullable persistent field */
    private Integer reportBy;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Integer status;

    /** nullable persistent field */
    private String acceptedNotes;

    /** nullable persistent field */
    private Date assignedDate;

    /** nullable persistent field */
    private Integer assignedTo;

    /** nullable persistent field */
    private Integer assignedBy;

    /** nullable persistent field */
    private Double worktime;

    /** nullable persistent field */
    private Double additionalTime;

    /** nullable persistent field */
    private Date planedDate;

    /** nullable persistent field */
    private Date actualDate;

    /** nullable persistent field */
    private String serviceCode;

    /** nullable persistent field */
    private Integer priority;

    /** persistent field */
    private Integer type;

    /** nullable persistent field */
    private String resolution;

    /** nullable persistent field */
    private Date dateClosed;

    /** nullable persistent field */
    private Integer closedBy;

    /** nullable persistent field */
    private Date editDate;

    /** nullable persistent field */
    private Integer editBy;

    /** nullable persistent field */
    private String notes;

    /** nullable persistent field */
    private String notesPublic;

    /** nullable persistent field */
    private Double version;

    /** nullable persistent field */
    private Integer project;

    /** nullable persistent field */
    private Integer module;

    /** nullable persistent field */
    private Integer severity;

    /** default constructor */
    public TicketHistory() {
    }

    /** minimal constructor */
    public TicketHistory(Integer ticket, String name, Integer company,
	    Date reportDate, Integer status, Integer type) {
	this.ticket = ticket;
	this.name = name;
	this.company = company;
	this.reportDate = reportDate;
	this.status = status;
	this.type = type;
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

    public String getChangeNotes() {
	return this.changeNotes;
    }

    public void setChangeNotes(String changeNotes) {
	this.changeNotes = changeNotes;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getCompany() {
	return this.company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Integer getPerson() {
	return this.person;
    }

    public void setPerson(Integer person) {
	this.person = person;
    }

    public Date getReportDate() {
	return this.reportDate;
    }

    public void setReportDate(Date reportDate) {
	this.reportDate = reportDate;
    }

    public Integer getReportBy() {
	return this.reportBy;
    }

    public void setReportBy(Integer reportBy) {
	this.reportBy = reportBy;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getAcceptedNotes() {
	return this.acceptedNotes;
    }

    public void setAcceptedNotes(String acceptedNotes) {
	this.acceptedNotes = acceptedNotes;
    }

    public Date getAssignedDate() {
	return this.assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
	this.assignedDate = assignedDate;
    }

    public Integer getAssignedTo() {
	return this.assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
	this.assignedTo = assignedTo;
    }

    public Integer getAssignedBy() {
	return this.assignedBy;
    }

    public void setAssignedBy(Integer assignedBy) {
	this.assignedBy = assignedBy;
    }

    public Double getWorktime() {
	return this.worktime;
    }

    public void setWorktime(Double worktime) {
	this.worktime = worktime;
    }

    public Double getAdditionalTime() {
	return this.additionalTime;
    }

    public void setAdditionalTime(Double additionalTime) {
	this.additionalTime = additionalTime;
    }

    public Date getPlanedDate() {
	return this.planedDate;
    }

    public void setPlanedDate(Date planedDate) {
	this.planedDate = planedDate;
    }

    public Date getActualDate() {
	return this.actualDate;
    }

    public void setActualDate(Date actualDate) {
	this.actualDate = actualDate;
    }

    public String getServiceCode() {
	return this.serviceCode;
    }

    public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
    }

    public Integer getPriority() {
	return this.priority;
    }

    public void setPriority(Integer priority) {
	this.priority = priority;
    }

    public Integer getType() {
	return this.type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getResolution() {
	return this.resolution;
    }

    public void setResolution(String resolution) {
	this.resolution = resolution;
    }

    public Date getDateClosed() {
	return this.dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
	this.dateClosed = dateClosed;
    }

    public Integer getClosedBy() {
	return this.closedBy;
    }

    public void setClosedBy(Integer closedBy) {
	this.closedBy = closedBy;
    }

    public Date getEditDate() {
	return this.editDate;
    }

    public void setEditDate(Date editDate) {
	this.editDate = editDate;
    }

    public Integer getEditBy() {
	return this.editBy;
    }

    public void setEditBy(Integer editBy) {
	this.editBy = editBy;
    }

    public String getNotes() {
	return this.notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getNotesPublic() {
	return this.notesPublic;
    }

    public void setNotesPublic(String notesPublic) {
	this.notesPublic = notesPublic;
    }

    public Double getVersion() {
	return this.version;
    }

    public void setVersion(Double version) {
	this.version = version;
    }

    public Integer getProject() {
	return this.project;
    }

    public void setProject(Integer project) {
	this.project = project;
    }

    public Integer getModule() {
	return this.module;
    }

    public void setModule(Integer module) {
	this.module = module;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public Integer getSeverity() {
	return severity;
    }

    public void setSeverity(Integer severity) {
	this.severity = severity;
    }

}
