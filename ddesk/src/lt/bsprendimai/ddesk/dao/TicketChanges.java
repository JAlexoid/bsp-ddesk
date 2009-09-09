/*
 * TicketChanges.java
 *
 * Created on March 17, 2006, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.sql.ResultSet;

import lt.bsprendimai.ddesk.StandardResults;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class TicketChanges extends AbstractTicket implements Serializable {
    private String notes;
    private String changeNotes;
    private java.sql.Timestamp dateClosed;
    private String uniqueId;
    private java.sql.Timestamp editDate;
    private String name;
    private String person;
    private String company;
    private int id;
    private int status;

    private String data = "";

    public TicketChanges() {
    }

    public String toString() {
	return data;
    }

    public TicketChanges(ResultSet rs) throws Exception {
	this.fill(rs);
    }

    public void fill(ResultSet rs) throws Exception {
	notes = rs.getString("notes");
	changeNotes = rs.getString("changenotes");
	status = rs.getInt("status");
	dateClosed = rs.getTimestamp("dateclosed");
	uniqueId = rs.getString("uniqueid");
	editDate = rs.getTimestamp("editdate");
	name = rs.getString("name");
	person = rs.getString("person");
	company = rs.getString("company");
	id = rs.getInt("id");
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getChangeNotes() {
	return changeNotes;
    }

    public void setChangeNotes(String changeNotes) {
	this.changeNotes = changeNotes;
    }

    public java.sql.Timestamp getDateClosed() {
	return dateClosed;
    }

    public void setDateClosed(java.sql.Timestamp dateClosed) {
	this.dateClosed = dateClosed;
    }

    public String getUniqueId() {
	return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
	this.uniqueId = uniqueId;
    }

    public java.sql.Timestamp getEditDate() {
	return editDate;
    }

    public void setEditDate(java.sql.Timestamp editDate) {
	this.editDate = editDate;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPerson() {
	return person;
    }

    public void setPerson(String person) {
	this.person = person;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    public void preSave() {
    }

    public void postLoad() {

    }

    public String update() {
	return StandardResults.STAY;
    }

    public String add() {
	return StandardResults.STAY;
    }

    public String delete() {
	return StandardResults.STAY;
    }

    public String getDescription() {
	return "";
    }

    public String getAcceptedNotes() {
	return "";
    }

    public String getResolution() {
	return "";
    }

}
