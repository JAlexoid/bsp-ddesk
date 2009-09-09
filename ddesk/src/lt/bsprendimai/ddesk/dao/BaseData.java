/*
 *    Copyright 2006 Baltijos Sprendimai (http://www.bsprendimai.lt/)
 *              Authorship: Aleksandr Panzin (http://www.activelogic.eu/)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;

import lt.bsprendimai.ddesk.UIMessenger;
import lt.bsprendimai.ddesk.StandardResults;

import org.hibernate.HibernateException;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public abstract class BaseData implements Serializable {

    public String add() {
	try {
	    SessionHolder.currentSession().getSess().save(this);
	    SessionHolder.currentSession().getSess().flush();
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    ex.printStackTrace();
	    UIMessenger.addFatalMessage(ex.getMessage(), ex.getMessage());
	    return StandardResults.FAIL;
	}
	return StandardResults.SUCCESS;
    }

    public String update() {
	try {
	    SessionHolder.currentSession().getSess().update(this);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.SUCCESS;
	} catch (Exception ex) {
	    SessionHolder.endSession();
	    ex.printStackTrace();
	    UIMessenger.addFatalMessage(ex.getMessage(), ex.getMessage());
	    return StandardResults.FAIL;
	}
    }

    public String delete() {
	try {
	    SessionHolder.currentSession().getSess().delete(this);
	    SessionHolder.currentSession().getSess().flush();
	    return StandardResults.SUCCESS;
	} catch (HibernateException ex) {
	    SessionHolder.endSession();
	    ex.printStackTrace();
	    UIMessenger.addFatalMessage(ex.getMessage(), ex.getMessage());
	    return StandardResults.FAIL;
	}
    }

}
