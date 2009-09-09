package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PersonInfo implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String email;

    /** nullable persistent field */
    private String project;

    /** nullable persistent field */
    private String code;

    private String company;
    private Integer projectId;
    private Integer companyId;

    /** default constructor */
    public PersonInfo() {
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

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getProject() {
	return this.project;
    }

    public void setProject(String project) {
	this.project = project;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public Integer getProjectId() {
	return projectId;
    }

    public void setProjectId(Integer projectId) {
	this.projectId = projectId;
    }

    public Integer getCompanyId() {
	return companyId;
    }

    public void setCompanyId(Integer companyId) {
	this.companyId = companyId;
    }

}
