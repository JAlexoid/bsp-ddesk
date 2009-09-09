package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class Status extends BaseData implements Serializable {

    public static final int REPORTED = 1;
    public static final int ACCEPTED = 2;
    public static final int CLOSED = 1000;

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    /** default constructor */
    public Status() {
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

    public boolean equals(Object o) {
	if (o instanceof Status) {
	    Status me = (Status) o;
	    return this.getId().equals(me.getId()) || me == this;
	} else {
	    return false;
	}
    }

}
