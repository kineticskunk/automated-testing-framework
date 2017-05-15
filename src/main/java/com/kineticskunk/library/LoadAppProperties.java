/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadAppProperties {
    private static Logger logger;
    private Properties propApplicationMap;

    public LoadAppProperties() {
        logger = LogManager.getLogger(LoadAppProperties.class.getName());
        this.propApplicationMap = new Properties();
    }

    public void loadAppPropertiesFile(String strPropertiesAbsoluteFileName) throws FileNotFoundException {
        File configFile = new File(strPropertiesAbsoluteFileName);
        FileInputStream inputStream = new FileInputStream(configFile);
        try {
            this.propApplicationMap.load(inputStream);
            inputStream.close();
        }
        catch (FileNotFoundException FNFE) {
            logger.error("Application map \"" + strPropertiesAbsoluteFileName + '\"' + " does not exist");
            logger.error(FNFE.getMessage());
        }
        catch (IOException IOE) {
            logger.error("Loading of pplication map \"" + strPropertiesAbsoluteFileName + '\"' + " produced an IOException");
            logger.error(IOE.getMessage());
        }
    }

    public String getProperty(String strPropertyName) {
        return this.propApplicationMap.getProperty(strPropertyName, "NOTFOUND");
    }
}

