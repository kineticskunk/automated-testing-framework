package com.kineticskunk.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.kineticskunk.auto.chrome.ChromeDesiredCapabilities;
import com.kineticskunk.auto.desiredcapabilities.LoadDesiredCapabilities;
import com.kineticskunk.auto.firefox.LoadFireFoxProfile;
import com.kineticskunk.driverfactory.DriverFactory;
import com.kineticskunk.events.SeleniumEventHandler;

public class TestBaseSetup {

	private static int WAIT = 60;

	private Logger logger = LogManager.getLogger(TestBaseSetup.class.getName());
	private Marker TESTBASESETUP = MarkerManager.getMarker("TESTBASESETUP");

	private DriverFactory df;
	private WebDriver wd;
	private EventFiringWebDriver eventDriver;
	private TestExecutionProperties tep;
	private LoadDesiredCapabilities ldc;
	private DesiredCapabilities dc;
	private SeleniumEventHandler handler = new SeleniumEventHandler();

	public TestBaseSetup(String browserType, String testExecutionProperties, String desiredCapabilities, boolean useRemoteWebDriver, String remoteWebDriverURL) {
		try {
			this.tep = TestExecutionProperties.getInstance(testExecutionProperties);
			this.loadWebDriverSystemPropertyEnviromentVariables();
			this.ldc = new LoadDesiredCapabilities(desiredCapabilities);
			this.dc = new DesiredCapabilities();
			this.dc.setBrowserName(browserType);
			this.dc.merge(this.ldc.getCommonDesiredCapabilties());
			switch (browserType.toLowerCase()) {
			case "firefox":
				LoadFireFoxProfile lffp = new LoadFireFoxProfile();
				this.dc.setCapability(FirefoxDriver.PROFILE, lffp.getFirefoxProfile());
				this.dc.setCapability(CapabilityType.BROWSER_NAME, browserType);
				break;
			case "chrome":
				ChromeDesiredCapabilities cdc = new ChromeDesiredCapabilities();
				this.dc.merge(cdc.getDesiredCapabilities());
				break;
			case "opera":

				break;
			}
			
			if (useRemoteWebDriver) {
				this.df = new DriverFactory(dc, useRemoteWebDriver, remoteWebDriverURL);
			} else {
				this.df = new DriverFactory(dc);
			}
			
			this.wd = this.df.getDriver();
			this.wd.manage().timeouts().implicitlyWait(WAIT, TimeUnit.SECONDS);
			this.wd.manage().window().maximize();
			this.eventDriver = new EventFiringWebDriver(this.wd);
			this.eventDriver.register(handler);
		} catch (WebDriverException e) {
			this.logException(browserType, e);
		} catch (FileNotFoundException e) {
			this.logException(browserType, e);
		} catch (IOException e) {
			this.logException(browserType, e);
		} catch (Exception e) {
			this.logException(browserType, e);
		}
	}

	public WebDriver getDriver() {
		return this.eventDriver;
	}

	public void navigateToURL(String url) {
		this.eventDriver.navigate().to(url);
	}

	public void quitDriver() {
		//this.eventDriver.quit();
	}

	public void loadWebDriverSystemPropertyEnviromentVariables() {
		Iterator<?> iterator = this.tep.getWebDriverExecutables().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			System.setProperty(key, value);
			this.logger.info(TESTBASESETUP, "Loaded system property " + (char)34 + key + (char)34 + " with value " + (char)34 + value + (char)34);
		}

	}

	private void logException(String browserType, Exception e) {
		this.logger.fatal(TESTBASESETUP, "Failed to load browser " + (char)34 + browserType + (char)34 + ".");
		this.logger.debug(TESTBASESETUP, "Message: " + e.getMessage());
		this.logger.debug(TESTBASESETUP, "Localized message: " + (char)34 + e.getLocalizedMessage() + (char)34 + ".");
		this.logger.debug(TESTBASESETUP, "Cause: " + (char)34 + e.getCause() + (char)34 + ".");
		this.logger.debug(TESTBASESETUP, "Stack Trace: " + e.getStackTrace());
	}

}
