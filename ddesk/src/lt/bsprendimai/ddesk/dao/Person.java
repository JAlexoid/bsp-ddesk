package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

/** @author Hibernate CodeGenerator */
public class Person extends BaseData implements Serializable {

    public static final int ADMIN = 0;
    public static final int OTHER = 1;
    public static final int PARTNER = 2;

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer company;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String position;

    /** nullable persistent field */
    private String phoneNo;

    /** nullable persistent field */
    private String email;

    /** nullable persistent field */
    private String loginCode;

    /** nullable persistent field */
    private String password;

    private String passwordShadow;

    /** nullable persistent field */
    private Integer loginLevel;

    /** nullable persistent field */
    private String notes;

    /** persistent field */
    private String language;

    private Date lastLogin;

    private String searchFilter;

    private boolean loginChange = false;

    /** default constructor */
    public Person() {
    }

    /** minimal constructor */
    public Person(String name) {
	this.name = name;
    }

    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getCompany() {
	return this.company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPosition() {
	return this.position;
    }

    public void setPosition(String position) {
	this.position = position;
    }

    public String getPhoneNo() {
	return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
    }

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getLoginCode() {
	return this.loginCode;
    }

    public void setLoginCode(String loginCode) {
	if (loginCode != null && !loginCode.equals(this.loginCode))
	    this.setLoginChange(true);
	this.loginCode = loginCode;
    }

    public String getPassword() {
	return this.password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Integer getLoginLevel() {
	return this.loginLevel;
    }

    public void setLoginLevel(Integer loginLevel) {
	this.loginLevel = loginLevel;
    }

    public String getNotes() {
	return this.notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public boolean equals(Object o) {
	if (o instanceof Person) {
	    Person me = (Person) o;
	    return this.getId().equals(me.getId());
	} else {
	    return false;
	}
    }

    public String getLanguage() {
	return language;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getPasswordShadow() {
	return passwordShadow;
    }

    public void setPasswordShadow(String passwordShadow) {
	this.passwordShadow = passwordShadow;
    }

    public Date getLastLogin() {
	return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
	this.lastLogin = lastLogin;
    }

    public boolean isLoginChange() {
	return loginChange;
    }

    public void setLoginChange(boolean loginChange) {
	this.loginChange = loginChange;
    }

    public String update() {
	String retValue;
	retValue = super.update();
	SessionHolder.clearCache();
	return retValue;
    }

    public String delete() {
	String retValue;
	retValue = super.delete();
	SessionHolder.clearCache();
	return retValue;
    }

    public String add() {
	String retValue;
	retValue = super.add();
	SessionHolder.clearCache();
	return retValue;
    }

    public String getSearchFilter() {
	return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
	this.searchFilter = searchFilter;
    }

}
