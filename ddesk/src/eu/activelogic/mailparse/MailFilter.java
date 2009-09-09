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

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 * 
 * A mail filtering interface to be implemented by
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public interface MailFilter {

    /**
     * This method is called before the parsing cycle.
     */
    public void cycleBegin();

    /**
     * This method is called after the parsing cycle.
     */
    public void cycleEnd();

    /**
     * Method is called on each message from the server. Before MIME Body parts processing.
     * 
     * @param msg
     *            the message that is being processed
     */
    public void filterStart(MimeMessage msg);

    /**
     * Method is called if any textual MIME body parts are found
     * 
     * @param msg
     *            all text message parts
     */
    public void filterText(String[] msg);

    /**
     * Method is called if any image MIME body parts are found
     * 
     * @param msg
     *            all image message parts
     */
    public void filterImages(MimeBodyPart[] msg);

    /**
     * Method is called on each message from the server. After MIME Body parts processing.
     * 
     * @param msg
     *            the message that is being processed
     */
    public void filterEnd(MimeMessage msg);
}
