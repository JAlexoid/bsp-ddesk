package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ProjectInfo implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String code;

    /** nullable persistent field */
    private Integer company;

    /** nullable persistent field */
    private Integer person;

    /** nullable persistent field */
    private Integer manager;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private Double version;

    /** nullable persistent field */
    private String companyName;

    /** nullable persistent field */
    private String personName;

    /** nullable persistent field */
    private String managerName;

    /** nullable persistent field */
    private String managerEmail;

    /** default constructor */
    public ProjectInfo() {
    }

    /** minimal constructor */
    public ProjectInfo(Integer id) {
	this.setId(id);
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
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

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Double getVersion() {
	return this.version;
    }

    public void setVersion(Double version) {
	this.version = version;
    }

    public String getCompanyName() {
	return this.companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public String getPersonName() {
	return this.personName;
    }

    public void setPersonName(String personName) {
	this.personName = personName;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public Integer getManager() {
	return manager;
    }

    public void setManager(Integer manager) {
	this.manager = manager;
    }

    public String getManagerName() {
	return managerName;
    }

    public void setManagerName(String managerName) {
	this.managerName = managerName;
    }

    public String getManagerEmail() {
	return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
	this.managerEmail = managerEmail;
    }

}
