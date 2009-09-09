package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class CertificateEntry extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    private String md5Key;
    private Integer person;
    private String name;
    private String cert;
    private Boolean valid;

    /** default constructor */
    public CertificateEntry() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getMd5Key() {
	return md5Key;
    }

    public void setMd5Key(String md5Key) {
	this.md5Key = md5Key;
    }

    public Integer getPerson() {
	return person;
    }

    public void setPerson(Integer person) {
	this.person = person;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCert() {
	return cert;
    }

    public void setCert(String cert) {
	this.cert = cert;
    }

    public Boolean isValid() {
	return valid;
    }

    public void setValid(Boolean valid) {
	this.valid = valid;
    }

}
