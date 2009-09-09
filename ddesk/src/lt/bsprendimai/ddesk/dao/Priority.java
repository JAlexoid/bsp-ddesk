package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class Priority extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    private Double timing;

    /** default constructor */
    public Priority() {
    }

    /** minimal constructor */
    public Priority(String name) {
	this.name = name;
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
	if (o instanceof Priority) {
	    Priority me = (Priority) o;
	    return this.getId().equals(me.getId()) || me == this;
	} else {
	    return false;
	}
    }

    public Double getTiming() {
	return timing;
    }

    public void setTiming(Double timing) {
	this.timing = timing;
    }

}
