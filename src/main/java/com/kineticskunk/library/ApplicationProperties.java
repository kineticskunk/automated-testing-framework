package com.kineticskunk.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationProperties {
	
	private static ApplicationProperties ap;
    private Logger logger = LogManager.getLogger(Thread.currentThread().getName());
    private Properties prop = new Properties();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ApplicationProperties getInstance() {
        if (ap != null) return ap;
        synchronized (ApplicationProperties.class) {
            if (ap != null) return ap;
            {
                ap = new ApplicationProperties();
            }
            return ap;
        }
    }

    public void loadPropertiesFile(String propertyFileLocation, String propertyFileName) throws IOException {
    	//InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(new File(propertyFileLocation + propertyFileName).getAbsolutePath());
    	
    	try {
    		File propertiesFile = new File(propertyFileLocation + propertyFileName);
    		InputStream inputStream = new FileInputStream(propertiesFile);
    		ApplicationProperties.ap.prop.load(inputStream);
            inputStream.close();
    	} catch (FileNotFoundException fnfex) {
    		
    	}
    }

    private boolean isPropValueNull(String key) {
        return ApplicationProperties.ap.prop.get(key).equals(null);
    }

    private boolean keyExists(String sKey) {
        return ApplicationProperties.ap.prop.containsKey(sKey);
    }

    public String getPropValue(String key) {
        if (!ap.keyExists(key)) {
            ApplicationProperties.ap.logger.info("Property '" + key + "' does not exist in the properties object.");
            return null;
        }
        if (ap.isPropValueNull(key)) {
            ApplicationProperties.ap.logger.info("Property '" + key + "' is NULL.");
            return null;
        }
        return this.prop.getProperty(key);
    }

    public void addProperty(String key, String value) {
        if (ap.keyExists(key)) {
            this.prop.remove(key);
        }
        this.prop.put(key, value);
        if (!ap.keyExists(key)) {
            this.logger.info("Property '" + key + "' has NOT been stored in the properties object");
        }
    }

    public Hashtable<String, String> readPropertyFile(Hashtable<String, String> testData, String propertiesFileLocation, String propertiesFile) throws IOException {
        Properties prop = new Properties();
        FileInputStream inputStream = new FileInputStream(propertiesFileLocation + propertiesFile);
        Hashtable<String, String> propvals = new Hashtable<String, String>();
        try {
            prop.load(inputStream);
            Set<String> propertyNames = prop.stringPropertyNames();
            for (String Property2 : propertyNames) {
                if (prop.getProperty(Property2).equalsIgnoreCase("")) {
                    propvals.put(Property2, "");
                    continue;
                }
                if (prop.getProperty(Property2).equals(null)) {
                    propvals.put(Property2, null);
                    continue;
                }
                propvals.put(Property2, prop.getProperty(Property2));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!testData.isEmpty()) {
            propvals.putAll(testData);
        }
        return propvals;
    }

}
