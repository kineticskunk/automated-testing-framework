package com.kineticskunk.auto;

import com.kineticskunk.drivers.ConfigureChromeDriver;
import com.kineticskunk.drivers.ConfigureFireFoxDriver;
import com.kineticskunk.events.EventHandler;
import com.kineticskunk.drivers.WebDrivers;
import com.kineticskunk.library.ApplicationProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class TestBaseSetup extends WebDrivers {
	
	private static TestBaseSetup tbs;

	public static TestBaseSetup getInstance(String configFileLocation, String configFileName) throws IOException {
		if (tbs == null ) {
			synchronized (TestBaseSetup.class) {
				if (tbs == null) {
					tbs = new TestBaseSetup(configFileLocation, configFileName);
				}
			}
		}
		return tbs;
	}
	
	private static Logger logger;
	protected WebDriver wd;
	protected ApplicationProperties ap;
	protected EventFiringWebDriver eventDriver;
	protected EventHandler handler = new EventHandler();
	
	@Parameters({ "resourceFileLocation", "testExecutionPropertiesFile" })
	public TestBaseSetup(String configFileLocation, String configFileName) throws IOException {
		logger = LogManager.getLogger(Thread.currentThread().getName());
		ap = ApplicationProperties.getInstance();
		ap.loadPropertiesFile(configFileLocation, configFileName);
	}
	
	public ApplicationProperties getApplicationProperties() {
		return ap;
	}
	
	@Parameters({ "browserType", "resourceFileLocation" })
	@BeforeClass
	public void setDriver(String browserType, String resourceFileLocation) {
		switch (browserType.toLowerCase()) {
		case "chrome":
			logger.info("***-------------***LAUNCHING GOOGLE CHROME***--------------***");
			try {
				ConfigureChromeDriver driver = new ConfigureChromeDriver();
				driver.configureDriver();
				this.wd = driver.getDriver();
			} catch (Exception e) {
				logger.fatal("An error occurred while attempting to load the Chrome browser");
				logger.error(e.getLocalizedMessage());
			}
			break;
		case "firefox":
			logger.info("***-------------***LAUNCHING MOZILLA FIREFOX***--------------***");
			try {
				ConfigureFireFoxDriver driver = new ConfigureFireFoxDriver();
				driver.configureDriver();
				this.wd = driver.getDriver();
			} catch (Exception e) {
				logger.fatal("An error occurred while attempting to load the FireFox browser");
				logger.error(e.getLocalizedMessage());
			}
			break;
		default:
			logger.fatal("Brower '" + browserType + "' is unsupported");
			break;
		}
		this.wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		//this.eventDriver = new EventFiringWebDriver(this.wd);
		//this.eventDriver.register(handler);
	}

	public WebDriver getDriver() {
		return this.eventDriver;
	}
	
	public void navigateToURL(String url) {
		this.eventDriver.navigate().to(url);
	}
	
	@AfterClass
	public void quitDriver() {
		this.eventDriver.unregister(handler);
		this.wd.quit();
	}
}

