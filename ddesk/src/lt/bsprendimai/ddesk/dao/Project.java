package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Project extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String code;

    /** nullable persistent field */
    private Integer company;

    /** nullable persistent field */
    private Integer person;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private Double version;

    /** nullable persistent field */
    private Integer superior;

    /** nullable persistent field */
    private Integer manager;

    /** default constructor */
    public Project() {
    }

    /** minimal constructor */
    public Project(String name) {
	this.name = name;
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

    public Integer getSuperior() {
	return this.superior;
    }

    public void setSuperior(Integer superior) {
	this.superior = superior;
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

}
