package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class Company extends BaseData implements Serializable {

    public static final int OWNER = 0;

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String code;

    /** nullable persistent field */
    private String vatcode;

    /** persistent field */
    private String name;

    /** persistent field */
    private String adress;

    /** persistent field */
    private String billingAdress;

    /** nullable persistent field */
    private Integer distance;

    /** nullable persistent field */
    private String notes;

    /** full constructor */
    public Company(String code, String vatcode, String name, String adress,
	    String billingAdress, Integer distance, String notes) {
	this.code = code;
	this.vatcode = vatcode;
	this.name = name;
	this.adress = adress;
	this.billingAdress = billingAdress;
	this.distance = distance;
	this.notes = notes;
    }

    /** default constructor */
    public Company() {
    }

    /** minimal constructor */
    public Company(String code, String name, String adress, String billingAdress) {
	this.code = code;
	this.name = name;
	this.adress = adress;
	this.billingAdress = billingAdress;
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

    public String getVatcode() {
	return this.vatcode;
    }

    public void setVatcode(String vatcode) {
	this.vatcode = vatcode;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAdress() {
	return this.adress;
    }

    public void setAdress(String adress) {
	this.adress = adress;
    }

    public String getBillingAdress() {
	return this.billingAdress;
    }

    public void setBillingAdress(String billingAdress) {
	this.billingAdress = billingAdress;
    }

    public Integer getDistance() {
	return this.distance;
    }

    public void setDistance(Integer distance) {
	this.distance = distance;
    }

    public String getNotes() {
	return this.notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public boolean equals(Object o) {
	if (o instanceof Company) {
	    Company me = (Company) o;
	    return this.getId().equals(me.getId());
	} else {
	    return false;
	}
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

}
