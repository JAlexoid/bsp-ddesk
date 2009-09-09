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
package lt.bsprendimai.ddesk.textmarks;

import java.beans.PropertyVetoException;

/**
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public interface CommandContext {

    public Object getHandler();

    boolean isAllowCycles();

    boolean isFillOnly();

    String extractTitle(String text);

    void fireAddEvent() throws PropertyVetoException;

    void fireUpdateEvent() throws PropertyVetoException;

    String getLastCycle();

    void setLastCycle(String lastCycle);
}
