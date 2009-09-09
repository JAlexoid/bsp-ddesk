package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class EventType extends BaseData implements Serializable {

    public static final int NONE = 0;
    public static final int FEATURE = 2;
    public static final int BUG = 1;
    public static final int OTHER = 3;

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    /** full constructor */
    public EventType(String name) {
	this.name = name;

    }

    /** default constructor */
    public EventType() {
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
	if (o instanceof EventType) {
	    EventType me = (EventType) o;
	    return this.getId().equals(me.getId()) || me == this;
	} else {
	    return false;
	}
    }

}
