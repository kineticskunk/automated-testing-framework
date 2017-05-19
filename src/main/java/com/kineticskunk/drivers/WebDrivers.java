package com.kineticskunk.drivers;

import com.kineticskunk.drivers.ConfigureChromeDriver;
import com.kineticskunk.drivers.ConfigureFireFoxDriver;
import com.kineticskunk.library.ApplicationProperties;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class WebDrivers {
    private static Logger logger;
    private WebDriver wd;
    private ApplicationProperties ap;

    public WebDrivers() {
        logger = LogManager.getLogger(WebDrivers.class.getName());
        this.ap = ApplicationProperties.getInstance();
    }

    public void setUPTestEnvironment() throws IOException {
        this.ap.loadPropertiesFile("", "kineticskunk.properties");
    }

    public void setDriver(String browserName) throws IOException {
        switch (browserName.toLowerCase()) {
            case "chrome": {
                logger.info("-------------***LAUNCHING GOOGLE CHROME***--------------");
                try {
                    ConfigureChromeDriver driver = new ConfigureChromeDriver();
                    driver.configureDriver();
                    this.wd = driver.getDriver();
                }
                catch (Exception e) {
                    logger.fatal("An error occurred while attempting to load the Chrome browser");
                    logger.error(e.getLocalizedMessage());
                }
                break;
            }
            case "firefox": {
                logger.info("-------------***LAUNCHING MOZILLA FIREFOX***--------------");
                ConfigureFireFoxDriver driver = new ConfigureFireFoxDriver();
                driver.configureDriver();
                this.wd = driver.getDriver();
                break;
            }
            default: {
                logger.fatal("Brower '" + browserName + "' is unsupported");
            }
        }
    }

    public WebDriver getDriver(String URL2) {
        this.wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        this.wd.get(URL2);
        return this.wd;
    }

    public void quitDriver() {
        this.wd.quit();
    }
}

