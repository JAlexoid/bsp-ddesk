package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

/** @author Hibernate CodeGenerator */
public class CompanyContract extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer company;

    /** nullable persistent field */
    private String contractNo;

    /** nullable persistent field */
    private Date contractDate;

    /** nullable persistent field */
    private Date expiryDate;

    /** nullable persistent field */
    private String serviceCode;

    /** nullable persistent field */
    private String serviceParams;

    /** nullable persistent field */
    private Integer billSum;

    /** nullable persistent field */
    private Integer billingPeriod;

    /** nullable persistent field */
    private Integer freeHours;

    /** nullable persistent field */
    private Double hourlyRate;

    /** nullable persistent field */
    private Date lastInvoiceDate;

    /** nullable persistent field */
    private String notes;

    private Double responceTime;

    /** default constructor */
    public CompanyContract() {
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

    public String getContractNo() {
	return this.contractNo;
    }

    public void setContractNo(String contractNo) {
	this.contractNo = contractNo;
    }

    public Date getContractDate() {
	return this.contractDate;
    }

    public void setContractDate(Date contractDate) {
	this.contractDate = contractDate;
    }

    public Date getExpiryDate() {
	return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
    }

    public String getServiceCode() {
	return this.serviceCode;
    }

    public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
    }

    public String getServiceParams() {
	return this.serviceParams;
    }

    public void setServiceParams(String serviceParams) {
	this.serviceParams = serviceParams;
    }

    public Integer getBillSum() {
	return this.billSum;
    }

    public void setBillSum(Integer billSum) {
	this.billSum = billSum;
    }

    public Integer getBillingPeriod() {
	return this.billingPeriod;
    }

    public void setBillingPeriod(Integer billingPeriod) {
	this.billingPeriod = billingPeriod;
    }

    public Integer getFreeHours() {
	return this.freeHours;
    }

    public void setFreeHours(Integer freeHours) {
	this.freeHours = freeHours;
    }

    public Double getHourlyRate() {
	return this.hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
	this.hourlyRate = hourlyRate;
    }

    public Date getLastInvoiceDate() {
	return this.lastInvoiceDate;
    }

    public void setLastInvoiceDate(Date lastInvoiceDate) {
	this.lastInvoiceDate = lastInvoiceDate;
    }

    public String getNotes() {
	return this.notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public boolean equals(Object o) {
	if (o instanceof CompanyContract) {
	    CompanyContract me = (CompanyContract) o;
	    return this.getId().equals(me.getId());
	} else {
	    return false;
	}
    }

    public Double getResponceTime() {
	return responceTime;
    }

    public void setResponceTime(Double responceTime) {
	this.responceTime = responceTime;
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
