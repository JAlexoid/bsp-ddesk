/*
 * PersonReport.java
 *
 * Created on March 14, 2006, 12:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package lt.bsprendimai.ddesk.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class PersonReport {

    private Integer id;
    private String name;
    private String code;
    private String vatCode;
    private String contractNo;
    private Timestamp contractDate;
    private Timestamp expiryDate;
    private String serviceCode;
    private Integer billSum;
    private Integer billingPeriod;
    private Integer freeHours;
    private Double hourlyRate;
    private Timestamp lastInvoiceDate;
    private Double responceTime;
    private Long recordNumber;
    private Double totaltime;
    private Double addtime;
    private boolean chargeable;

    /** Creates a new instance of PersonReport */
    public PersonReport() {
    }

    public PersonReport(ResultSet rs) throws Exception {
	this.fill(rs);
    }

    public void fill(ResultSet rs) throws Exception {
	id = rs.getInt("id");
	name = rs.getString("name");
	code = rs.getString("code");
	vatCode = rs.getString("vatcode");
	contractNo = rs.getString("contractno");
	contractDate = rs.getTimestamp("contractdate");
	expiryDate = rs.getTimestamp("expirydate");
	serviceCode = rs.getString("servicecode");
	billSum = rs.getInt("billsum");
	billingPeriod = rs.getInt("billingperiod");
	freeHours = rs.getInt("freehours");
	hourlyRate = rs.getDouble("hourlyrate");
	lastInvoiceDate = rs.getTimestamp("lastinvoicedate");
	responceTime = rs.getDouble("responcetime");
	recordNumber = rs.getLong("recordnumber");
	totaltime = rs.getDouble("totaltime");
	addtime = rs.getDouble("addtime");
	chargeable = rs.getBoolean("chargeable");
    }

    public void clear() {
	id = null;
	name = null;
	code = null;
	vatCode = null;
	contractNo = null;
	contractDate = null;
	expiryDate = null;
	serviceCode = null;
	billSum = null;
	billingPeriod = null;
	freeHours = null;
	hourlyRate = null;
	lastInvoiceDate = null;
	responceTime = null;
	recordNumber = null;
	totaltime = null;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getVatCode() {
	return vatCode;
    }

    public void setVatCode(String vatCode) {
	this.vatCode = vatCode;
    }

    public String getContractNo() {
	return contractNo;
    }

    public void setContractNo(String contractNo) {
	this.contractNo = contractNo;
    }

    public Timestamp getContractDate() {
	return contractDate;
    }

    public void setContractDate(Timestamp contractDate) {
	this.contractDate = contractDate;
    }

    public Timestamp getExpiryDate() {
	return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
	this.expiryDate = expiryDate;
    }

    public String getServiceCode() {
	return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
    }

    public Integer getBillSum() {
	return billSum;
    }

    public void setBillSum(Integer billSum) {
	this.billSum = billSum;
    }

    public Integer getBillingPeriod() {
	return billingPeriod;
    }

    public void setBillingPeriod(Integer billingPeriod) {
	this.billingPeriod = billingPeriod;
    }

    public Integer getFreeHours() {
	return freeHours;
    }

    public void setFreeHours(Integer freeHours) {
	this.freeHours = freeHours;
    }

    public Double getHourlyRate() {
	return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
	this.hourlyRate = hourlyRate;
    }

    public Timestamp getLastInvoiceDate() {
	return lastInvoiceDate;
    }

    public void setLastInvoiceDate(Timestamp lastInvoiceDate) {
	this.lastInvoiceDate = lastInvoiceDate;
    }

    public Double getResponceTime() {
	return responceTime;
    }

    public void setResponceTime(Double responceTime) {
	this.responceTime = responceTime;
    }

    public Long getRecordNumber() {
	return recordNumber;
    }

    public void setRecordNumber(Long recordNumber) {
	this.recordNumber = recordNumber;
    }

    public Double getTotaltime() {
	return totaltime;
    }

    public void setTotaltime(Double totaltime) {
	this.totaltime = totaltime;
    }

    public boolean isChargeable() {
	return chargeable;
    }

    public void setChargeable(boolean chargeable) {
	this.chargeable = chargeable;
    }

    public Double getAddtime() {
	return addtime;
    }

    public void setAddtime(Double addtime) {
	this.addtime = addtime;
    }
}
