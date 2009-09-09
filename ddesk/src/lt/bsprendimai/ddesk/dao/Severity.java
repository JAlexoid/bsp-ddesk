package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class Severity extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    /** full constructor */
    public Severity(String name) {
	this.name = name;

    }

    /** default constructor */
    public Severity() {
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
	if (o instanceof Severity) {
	    Severity me = (Severity) o;
	    return this.getId().equals(me.getId()) || me == this;
	} else {
	    return false;
	}
    }

}
