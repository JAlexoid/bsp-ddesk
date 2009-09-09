package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ProjectModule extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String module;

    /** nullable persistent field */
    private String description;

    /** identifier field */
    private Integer project;

    /** full constructor */
    public ProjectModule(String module, String description, Integer project) {
	this.module = module;
	this.description = description;
	this.project = project;
    }

    /** default constructor */
    public ProjectModule() {
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getModule() {
	return this.module;
    }

    public void setModule(String module) {
	this.module = module;
    }

    public String getName() {
	return this.module;
    }

    public void setName(String module) {
	this.module = module;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public Integer getProject() {
	return project;
    }

    public void setProject(Integer project) {
	this.project = project;
    }

}
