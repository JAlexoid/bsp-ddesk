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

package lt.bsprendimai;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Application configuration holder.
 * Contains all properties loaded at startup. File references. As optional methods for ease of
 * resource loading.
 * 
 * Loads sqls.properties from root of the classpath.
 * 
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class Configuration {

    private static Properties preferences;
    private static javax.mail.Session mailSession;

    private static Properties sqls = new Properties();

    private static File propertiesFile;
    private static File rootFile;

    static {
	try {
	    InputStream is = Configuration.class
		    .getResourceAsStream("/sqls.properties");
	    sqls.load(is);
	} catch (Exception exc) {
	    exc.printStackTrace();
	}
    }

    /** Creates a new instance of Configuration */
    public Configuration() {
    }

    public static Properties getPreferences() {
	return preferences;
    }

    public static void setPreferences(Properties aPreferences) {
	preferences = aPreferences;
    }

    public static javax.mail.Session getMailSession() {
	return mailSession;
    }

    public static void setMailSession(javax.mail.Session aMailSession) {
	mailSession = aMailSession;
    }

    /**
     * Load resource from classpath and convert the contents into a string.
     * 
     * @param path
     *            a path to the needed resource
     * @return string value of the resource
     * @throws IOException
     * @see Class#getResourceAsStream(String)
     */
    public static String getResourceString(String path) throws IOException {
	BufferedReader is = new BufferedReader(new InputStreamReader(
		Configuration.class.getResourceAsStream(path), "UTF-8"));
	StringBuffer sb = new StringBuffer();
	int in = 0;
	while ((in = is.read()) != -1) {
	    sb.append((char) in);
	}
	is.close();
	return sb.toString();
    }

    public static File getPropertiesFile() {
	return propertiesFile;
    }

    public static void setPropertiesFile(File aPropertiesFile) {
	propertiesFile = aPropertiesFile;
    }

    public static File getRootFile() {
	return rootFile;
    }

    public static void setRootFile(File aRootFile) {
	rootFile = aRootFile;
    }

    public static String getSQL(String key) {
	return sqls.getProperty(key, "");
    }

    public static Properties getSqls() {
	return sqls;
    }

}
