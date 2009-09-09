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

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public abstract class AbstractTicket extends BaseData {

    public abstract Integer getStatus();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getAcceptedNotes();

    public abstract String getResolution();

    // public abstract String getJobsDone() ;
    //
    // public abstract void setJobsDone(String jobsDone);

    // public abstract Double getWorktime();
    //
    // public abstract void setWorktime(Double worktime);

    // private Map<String, String> jobParameters = new HashMap<String, String>();

    public boolean isClosedState() {
	return getStatus() != null && getStatus() >= Status.CLOSED;
    }

    public boolean isAcceptedState() {
	return getStatus() != null && getStatus() >= Status.ACCEPTED
		&& getStatus() < Status.CLOSED;
    }

    public boolean isNewState() {
	return getStatus() == null || getStatus() <= Status.REPORTED;
    }

    // public Map<String, String> getJobParameters() {
    // return jobParameters;
    // }
    //
    // public void setJobParameters(Map<String, String> jobParameters) {
    // this.jobParameters = jobParameters;
    // }

    public void preSave() {
	// try{
	// double dtime = 0.0d;
	// StringBuilder sb = new StringBuilder();
	// for(int i = 1; i <= 5; i++){
	// String name = jobParameters.get("serviceName"+i);
	// String time = jobParameters.get("serviceTime"+i);
	// if(name == null || time == null)
	// continue;
	// try {
	// time = time.toString().replace(',','.');
	// time = time.toString().replace(" ","");
	// Double timeD = new Double(time);;
	// dtime += timeD;
	// time = timeD.toString();
	// } catch (Exception ex) {
	// }
	//
	// sb.append(name);
	// sb.append(";--;");
	// sb.append(time);
	// sb.append("\n");
	// }
	// this.setJobsDone(sb.toString());
	// if(dtime != 0.0d)
	// this.setWorktime(dtime);
	// } catch (Exception ee){
	// ee.printStackTrace();
	// }
    }

    public void postLoad() {
	// jobParameters.clear();
	// if(this.getJobsDone() == null){
	// for(int i = 0; i < 5; i++){
	// jobParameters.put("serviceName"+(i+1), "");
	// jobParameters.put("serviceTime"+(i+1), "");
	// }
	// return;
	// }
	// try{
	// String s[] = this.getJobsDone().split("\\n");
	//
	// for(int i = 0; i < s.length; i++){
	// if(s[i] == null || s[i].trim().equals(""))
	// continue;
	// String pair[] = s[i].split(";--;");
	// if(pair.length < 2)
	// continue;
	// jobParameters.put("serviceName"+(i+1), pair[0]);
	// jobParameters.put("serviceTime"+(i+1), pair[1]);
	// }
	//
	// } catch (Exception ee){
	// ee.printStackTrace();
	//
	// }
    }

    public String update() {
	String retValue;
	SessionHolder.clearCache();
	retValue = super.update();
	return retValue;
    }

    public String delete() {
	String retValue;
	SessionHolder.clearCache();
	retValue = super.delete();
	return retValue;
    }

    public String add() {
	String retValue;
	SessionHolder.clearCache();
	retValue = super.add();
	return retValue;
    }

    public String getDescriptionTruncated() {
	if (this.getDescription().length() < 80) {
	    return this.getDescription();
	} else {
	    return this.getDescription().substring(0, 80).replaceFirst(
		    "\\s*[\\d\\w_]*\\z", "").trim();
	}
    }

    public String htmlBR(String s) {
	if (s != null)
	    return s.replaceAll("\\>", "&gt;").replaceAll("\\<", "&lt;")
		    .replaceAll("\\\"", "&quot;").replaceAll("\\&", "&amp;")
		    .replaceAll("\\n", "<br/>");
	else
	    return null;
    }

    public String getDescriptionHTML() {
	return htmlBR(getDescription());
    }

    public String getAcceptedNotesHTML() {
	return htmlBR(getAcceptedNotes());
    }

    public String getResolutionHTML() {
	return htmlBR(getResolution());
    }

}
