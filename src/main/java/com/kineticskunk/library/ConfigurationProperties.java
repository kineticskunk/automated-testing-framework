/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationProperties {
    private static ConfigurationProperties cp;
    private static Logger logger;
    private Properties prop;
    private InputStream inputStream;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ConfigurationProperties getInstance() {
        if (cp != null) return cp;
        synchronized (ConfigurationProperties.class) {
            if (cp != null) return cp;
            {
                cp = new ConfigurationProperties();
            }
            // ** MonitorExit[var0] (shouldn't be in output)
            return cp;
        }
    }

    public ConfigurationProperties() {
        logger = LogManager.getLogger(ConfigurationProperties.class.getName());
        this.inputStream = null;
        this.prop = new Properties();
    }

    public void LoadPropertiesFile(String sPropertiesFileName) throws IOException {
        block6 : {
            try {
                this.inputStream = this.getClass().getClassLoader().getResourceAsStream(sPropertiesFileName);
                if (this.inputStream != null) {
                    this.prop.load(this.inputStream);
                    break block6;
                }
                logger.error(new FileNotFoundException("Property file '" + sPropertiesFileName + "' not found in the classpath"));
            }
            catch (Exception e) {
                logger.error("Exception: " + e.getMessage());
            }
            finally {
                this.inputStream.close();
            }
        }
    }

    private boolean keyExists(String sKey) {
        return ConfigurationProperties.cp.prop.containsKey(sKey);
    }

    public String getValue(String sKey) {
        if (cp.keyExists(sKey)) {
            return ConfigurationProperties.cp.prop.getProperty(sKey);
        }
        return null;
    }
}

