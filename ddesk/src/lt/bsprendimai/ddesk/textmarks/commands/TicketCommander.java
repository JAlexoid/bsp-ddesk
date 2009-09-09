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
package lt.bsprendimai.ddesk.textmarks.commands;

import java.beans.PropertyVetoException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lt.bsprendimai.ddesk.TicketAccessor;
import lt.bsprendimai.ddesk.dao.Ticket;
import lt.bsprendimai.ddesk.textmarks.CancelBlockException;
import lt.bsprendimai.ddesk.textmarks.CancelOperationException;
import lt.bsprendimai.ddesk.textmarks.CommandContext;
import lt.bsprendimai.ddesk.textmarks.CommandHandler;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class TicketCommander {

    private static HashSet<String> cyclers = new HashSet<String>();

    static {
	cyclers.add("new");
	cyclers.add("close");
	cyclers.add("end");
	cyclers.add("ticket");
    }

    /** Creates a new instance of TicketCommander */
    public TicketCommander() {

    }

    // :skipAll
    @CommandHandler(command = "skipAll", newCycle = true, terminator = true)
    public void skipCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got skip command"+ texts[index]);
	for (int i = index; i < texts.length; i++) {
	    texts[i] = null;
	}
	endCommand(tt, texts, index);
	// TicketAccessor ta = (TicketAccessor)tt.getHandler();
	// if(ta.getSelected() != null){
	// if(ta.getSelected().getUniqueId() == null || ta.getSelected().getUniqueId().equals("")){
	// ta.add();
	// } else {
	// ta.update();
	// }
	// }
    }

    // :new
    @CommandHandler(command = "new", newCycle = true)
    public void newCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got new command"+ texts[index]);
	if (!tt.isAllowCycles() && index > 0) {
	    // System.out.println("Returning("+tt.isAllowCycles()+":"+index+")");
	    return;
	}
	tt.setLastCycle("new");

	// System.out.println("Got new command"+ texts[index]);
	endCommand(tt, texts, index);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	int newLength = ":new".length();
	if (texts[index].trim().length() > newLength) {
	    ta.getSelected().setName(texts[index].substring(newLength).trim());// tt.extractTitle(texts[index].substring(newLength)).trim());
	} else if (texts.length > index + 1
		&& !texts[index + 1].trim().startsWith(":")) {
	    ta.getSelected().setName(texts[index + 1].trim());
	    // if(ta.getSelected().getName().length() < texts[index+1].length())
	    // texts[index+1] =
	    // texts[index+1].substring(ta.getSelected().getName().length()-3).trim();
	    // else
	    texts[index + 1] = null;
	}
    }

    // :
    @CommandHandler(command = "", fallback = true)
    public void anyCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got any command"+ texts[index]);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	int i = index;
	while (i >= 0) {
	    if (texts[i] != null) {
		if (texts[i].trim().startsWith(":assign")) {
		    ta.setAsignee(texts[index].trim().substring(1));
		    break;
		}
		if (texts[i].trim().startsWith(":new")) {
		    ta.setProjectCode(texts[index].trim().substring(1));
		    break;
		}
	    }
	    i--;
	}
    }

    // :ticket
    @CommandHandler(command = "ticket")
    public void ticketCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got assign command"+ texts[index]);

	String someParams = texts[index].substring(":assign".length()).trim();
	if (!someParams.equals("")) {
	    TicketAccessor ta = (TicketAccessor) tt.getHandler();
	    Ticket tk = ta.getSelected();
	    if (!tt.isAllowCycles()) {
		ta.setSelected(tk);
		throw new CancelOperationException();
	    }
	    if (!ta.setUniqueId(someParams)) {
		throw new CancelBlockException();
	    }
	}
    }

    // :ticket
    @CommandHandler(command = "gassign")
    public void gassignCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got assign command"+ texts[index]);

	String someParams = texts[index].substring(":gassign".length()).trim();
	if (!someParams.equals("")) {
	    TicketAccessor ta = (TicketAccessor) tt.getHandler();
	    // Ticket tk = ta.getSelected();
	    // Integer project = tk.getProject();
	    // Integer person = tk.getAssignedTo();
	    String[] params = someParams.split(" ");
	    for (String param : params) {
		ta.setAnyGlobal(param);
	    }
	}
    }

    // :bug
    @CommandHandler(command = "bug")
    public void bugCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got bug command"+ texts[index]);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	ta.getSelected().setType(1);
	texts[index] = null;
    }

    // :feature
    @CommandHandler(command = "feature")
    public void featureCommand(CommandContext tt, String[] texts, int index) {
	System.out.println("Got feature command" + texts[index]);

	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	ta.getSelected().setType(2);
	texts[index] = null;
    }

    // :version
    @CommandHandler(command = "version")
    public void versionCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got version command"+ texts[index]);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	try {
	    ta.getSelected().setVersion(
		    new Double(texts[index].substring(":version".length())
			    .trim()));
	} catch (Exception ex) {
	    // Left empty for a reason
	}
	texts[index] = null;
    }

    // :assign
    @CommandHandler(command = "assign")
    public void assingnCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got assign command"+ texts[index]);

	String someParams = texts[index].substring(":assign".length()).trim();
	if (!someParams.equals("")) {
	    TicketAccessor ta = (TicketAccessor) tt.getHandler();
	    // Ticket tk = ta.getSelected();
	    // if(!tt.isAllowCycles()){
	    // ta.setSelected(tk);
	    // throw new CancelOperationException();
	    // }
	    String[] params = someParams.split(" ");
	    for (String param : params) {
		ta.setAny(param.trim());
	    }
	    System.out.println("Prj:" + ta.getSelected().getProject());
	    System.out.println("To:" + ta.getSelected().getAssignedTo());
	}
    }

    // :comment
    @CommandHandler(command = "comment")
    public void commentCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got comment command"+ texts[index]);
	String id = texts[index].substring(":comment".length()).trim();
	if (!id.equals("")) {
	    // if(!tt.isAllowCycles())
	    // throw new CancelOperationException();
	    tt.setLastCycle("comment");
	    // TicketAccessor ta = (TicketAccessor)tt.getHandler();
	    // ta.setUniqueId(id);
	}
    }

    // :IC
    @CommandHandler(command = "ic")
    public void informClientCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got ic command"+ texts[index]);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	ta.setInformClient(true);
	texts[index] = null;
    }

    // :close
    @CommandHandler(command = "close", newCycle = true, terminator = true)
    public void closeCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got close command"+ texts[index]);
	// TicketAccessor ta = (TicketAccessor)tt.getHandler();
	// if(ta.getSelected().getUniqueId() == null || ta.getSelected().getUniqueId().equals("")){
	// ta.add();
	// } else {
	// ta.update();
	// }
	// ta.setId(null);
	endCommand(tt, texts, index);
    }

    // :now
    @CommandHandler(command = "now")
    public void dateSetCommand(CommandContext tt, String[] texts, int index) {
	// System.out.println("Got now command"+ texts[index]);
	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	int i = index;
	while (i >= 0) {
	    if (texts[i].startsWith(":assign")) {
		ta.getSelected().setAssignedDate(new Date());
		break;
	    }
	    i--;
	}
    }

    // :accept

    // :end
    @CommandHandler(command = "end", newCycle = true)
    public void endCommand(CommandContext tt, String[] texts, int index) {
	System.out.println("Got end command" + texts[index]);
	int i = index;
	StringBuilder sb = new StringBuilder();
	List<String> reverser = new LinkedList<String>();
	while (i >= 0) {
	    String tta = texts[i];
	    if (tta == null) {
		i--;
		continue;
	    }
	    String cmd = "";
	    if (tta.startsWith(":")) {
		if (tta.trim().indexOf(' ') > 0) {
		    cmd = tta.trim().substring(1, tta.trim().indexOf(' '));
		} else {
		    cmd = tta.trim().substring(1);
		}
	    }
	    if (tta != null
		    && (cyclers.contains(cmd) || cmd.equals(tt.getLastCycle()))) {
		break;
	    }
	    if (tta != null && (!tta.startsWith(":"))) {
		reverser.add(0, tta);
	    }
	    i--;
	}
	boolean first = true;
	for (String al : reverser) {
	    if (al == null)
		continue;
	    if (!first) {
		sb.append("\n");
	    } else {
		first = false;
	    }
	    sb.append(al);
	}

	TicketAccessor ta = (TicketAccessor) tt.getHandler();
	System.out.println("Selected name: " + ta.getSelected().getName());
	if (ta.getSelected() != null && ta.getSelected().getName() != null) {
	    if (ta.getSelected().getUniqueId() == null
		    || ta.getSelected().getUniqueId().equals("")) {
		if (ta.getSelected().getDescription() != null)
		    ta.getSelected().setDescription(
			    ta.getSelected().getDescription() + "\n"
				    + sb.toString().trim());
		else
		    ta.getSelected().setDescription(sb.toString().trim());

		if (tt.isFillOnly())
		    return;
		try {
		    tt.fireAddEvent();
		    System.out.println("Adding");
		    ta.addExternal();
		} catch (PropertyVetoException ex) {
		    return;
		}
	    } else {
		ta.setComment(sb.toString().trim());
		if (tt.isFillOnly())
		    return;
		try {
		    tt.fireUpdateEvent();
		    System.out.println("Upadating");
		    ta.update();
		} catch (PropertyVetoException ex) {
		    return;
		}
	    }
	}
	ta.resetInternal();
    }

}
