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

import java.io.Serializable;
import java.util.Date;

/**
 * A filter data container for seach functionality.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class TicketFilter implements Serializable {

    public static final long serialVersionUID = 200604261242l;

    public Boolean sortEditDate;
    public Boolean sortAsignee;
    public Boolean sortStatus;
    public Boolean sortPriority;
    public Boolean sortCompany;
    public Boolean sortReportDate;
    public Boolean sortPlaned;
    public Boolean sortName;
    public Boolean sortUniqueId;
    public Boolean sortVersion;
    public Boolean sortType;

    public String ticketName;
    public String uniqueId;
    public Double version;
    public Integer person;
    public Integer company;
    public Integer status;
    public Integer priority;
    public Integer assigned;
    public Integer project;
    public Integer module;
    public Integer type;

    public Date forDate;
    public int accepted = 0;
    public int closed = 0;
    public int chargeable = 0;

    public Boolean getSortEditDate() {
	return sortEditDate;
    }

    public void setSortEditDate(Boolean sortEditDate) {
	this.sortEditDate = sortEditDate;
    }

    public Boolean getSortAsignee() {
	return sortAsignee;
    }

    public void setSortAsignee(Boolean sortAsignee) {
	this.sortAsignee = sortAsignee;
    }

    public Boolean getSortStatus() {
	return sortStatus;
    }

    public void setSortStatus(Boolean sortStatus) {
	this.sortStatus = sortStatus;
    }

    public Boolean getSortPriority() {
	return sortPriority;
    }

    public void setSortPriority(Boolean sortPriority) {
	this.sortPriority = sortPriority;
    }

    public Boolean getSortCompany() {
	return sortCompany;
    }

    public void setSortCompany(Boolean sortCompany) {
	this.sortCompany = sortCompany;
    }

    public Boolean getSortReportDate() {
	return sortReportDate;
    }

    public void setSortReportDate(Boolean sortReportDate) {
	this.sortReportDate = sortReportDate;
    }

    public Boolean getSortPlaned() {
	return sortPlaned;
    }

    public void setSortPlaned(Boolean sortPlaned) {
	this.sortPlaned = sortPlaned;
    }

    public Boolean getSortName() {
	return sortName;
    }

    public void setSortName(Boolean sortName) {
	this.sortName = sortName;
    }

    public Boolean getSortUniqueId() {
	return sortUniqueId;
    }

    public void setSortUniqueId(Boolean sortUniqueId) {
	this.sortUniqueId = sortUniqueId;
    }

    public Boolean getSortVersion() {
	return sortVersion;
    }

    public void setSortVersion(Boolean sortVersion) {
	this.sortVersion = sortVersion;
    }

    public String getTicketName() {
	return ticketName;
    }

    public void setTicketName(String ticketName) {
	this.ticketName = ticketName;
    }

    public String getUniqueId() {
	return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
	this.uniqueId = uniqueId;
    }

    public Double getVersion() {
	return version;
    }

    public void setVersion(Double version) {
	this.version = version;
    }

    public Integer getPerson() {
	return person;
    }

    public void setPerson(Integer person) {
	this.person = person;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getPriority() {
	return priority;
    }

    public void setPriority(Integer priority) {
	this.priority = priority;
    }

    public Integer getAssigned() {
	return assigned;
    }

    public void setAssigned(Integer assigned) {
	this.assigned = assigned;
    }

    public Integer getProject() {
	return project;
    }

    public void setProject(Integer project) {
	this.project = project;
    }

    public Integer getModule() {
	return module;
    }

    public void setModule(Integer module) {
	this.module = module;
    }

    public Date getForDate() {
	return forDate;
    }

    public void setForDate(Date forDate) {
	this.forDate = forDate;
    }

    public int getAccepted() {
	return accepted;
    }

    public void setAccepted(int accepted) {
	this.accepted = accepted;
    }

    public int getClosed() {
	return closed;
    }

    public void setClosed(int closed) {
	this.closed = closed;
    }

    public int getChargeable() {
	return chargeable;
    }

    public void setChargeable(int chargeable) {
	this.chargeable = chargeable;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Boolean getSortType() {
	return sortType;
    }

    public void setSortType(Boolean sortType) {
	this.sortType = sortType;
    }

}
