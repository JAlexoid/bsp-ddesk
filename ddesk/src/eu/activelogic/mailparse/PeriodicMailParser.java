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

package eu.activelogic.mailparse;

import java.util.Properties;

import javax.mail.Session;

/**
 * A thread for the parsing process. Implements mail parser and runs periodically with timeouts,
 * until stopped.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class PeriodicMailParser extends MailParser implements Runnable {

    private boolean runState = false;
    private Properties prp;
    private Session ss;
    private long timeout = 10000;
    private Thread myThread;
    private MailFilter mf;

    /** Creates a new instance of PeriodicMailParser */
    public PeriodicMailParser(MailFilter mf, Properties prp) {
	super(mf);
	this.prp = prp;
	this.setMf(mf);
	ss = Session.getInstance(prp);
    }

    public void run() {
	while (runState) {
	    // System.out.println("Checking for new mail");
	    synchronized (ss) {
		if (ss == null) {
		    ss = Session.getInstance(prp);
		}
	    }
	    mf.cycleBegin();

	    try {
		super.parse(ss);
	    } catch (Exception ex) {
		ex.printStackTrace();
		return;
	    }
	    mf.cycleEnd();
	    try {
		// System.out.println("Sleeping for "+(timeout/1000)+"s");
		Thread.sleep(timeout);
	    } catch (InterruptedException ex) {
	    }
	}
    }

    public void stop() {
	runState = false;
    }

    public boolean isRunState() {
	return runState;
    }

    public void setRunState(boolean runState) {
	if (runState != this.runState && runState) {
	    this.runState = runState;
	    this.myThread = new Thread(this);
	    myThread.start();
	} else {
	    this.runState = runState;
	}
    }

    public long getTimeout() {
	return timeout;
    }

    public void setTimeout(long timeout) {
	this.timeout = timeout;
    }

    public Thread getMyThread() {
	return myThread;
    }

    public void setMyThread(Thread myThread) {
	this.myThread = myThread;
    }

    public MailFilter getMf() {
	return mf;
    }

    public void setMf(MailFilter mf) {
	this.mf = mf;
    }

}
