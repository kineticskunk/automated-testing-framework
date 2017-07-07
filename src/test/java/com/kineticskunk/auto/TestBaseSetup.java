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
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class TestBaseSetup extends WebDrivers {
	
	private final Logger logger = LogManager.getLogger(TestBaseSetup.class.getName());
	private final Marker TESTBASESETUP = MarkerManager.getMarker("TESTBASESETUP");
	
	protected WebDriver wd;
	protected ApplicationProperties ap;
	protected EventFiringWebDriver eventDriver;
	protected EventHandler handler = new EventHandler();
	
	@Parameters({ "browserType", "desiredCapabilitiesConfigJSON" })
	public TestBaseSetup(String browserType, String desiredCapabilitiesConfigJSON) throws IOException {
		this.setDriver(browserType, desiredCapabilitiesConfigJSON);
	}
	
	@BeforeClass
	public void setDriver(String browserType, String desiredCapabilitiesConfigJSON) {
		switch (browserType.toLowerCase()) {
		case "chrome":
			this.logger.info(TESTBASESETUP, "***-------------***LAUNCHING GOOGLE CHROME***--------------***");
			try {
				ConfigureChromeDriver driver = new ConfigureChromeDriver();
				driver.configureDriver();
				this.wd = driver.getDriver();
			} catch (Exception e) {
				this.logger.fatal(TESTBASESETUP, "An error occurred while attempting to load a Chrome browser");
			}
			break;
		case "firefox":
			logger.info(TESTBASESETUP, "***-------------***LAUNCHING MOZILLA FIREFOX***--------------***");
			try {
				ConfigureFireFoxDriver driver = new ConfigureFireFoxDriver();
				driver.configureDriver();
				this.wd = driver.getDriver();
			} catch (Exception e) {
				this.logger.fatal("TESTBASESETUP, An error occurred while attempting to load a FireFox browser");
			}
			break;
		case "opera":
			logger.info(TESTBASESETUP, "***-------------***LAUNCHING MOZILLA FIREFOX***--------------***");
			try {
				ConfigureFireFoxDriver driver = new ConfigureFireFoxDriver();
				driver.configureDriver();
				this.wd = driver.getDriver();
			} catch (Exception e) {
				this.logger.fatal("TESTBASESETUP, An error occurred while attempting to load a Opera browser");
			}
			break;
		default:
			this.logger.fatal(TESTBASESETUP, "Brower '" + browserType + "' is unsupported");
			break;
		}
		this.wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		this.eventDriver = new EventFiringWebDriver(this.wd);
		this.eventDriver.register(handler);
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

