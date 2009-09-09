package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Permission extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String code;

    /** persistent field */
    private Integer person;

    /** full constructor */
    public Permission(String code, Integer person) {
	this.code = code;
	this.person = person;
    }

    /** default constructor */
    public Permission() {
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public Integer getPerson() {
	return this.person;
    }

    public void setPerson(Integer person) {
	this.person = person;
    }

    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).toString();
    }

}
