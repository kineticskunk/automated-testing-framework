package com.kineticskunk.auto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.kineticskunk.auto.chrome.ChromeDesiredCapabilities;
import com.kineticskunk.auto.desiredcapabilities.LoadDesiredCapabilities;
import com.kineticskunk.auto.firefox.LoadFireFoxProfile;
import com.kineticskunk.driverfactory.DriverFactory;
import com.kineticskunk.events.EventHandler;

public class WDFTestBaseSetup extends LoadDesiredCapabilities {
	
	private static int WAIT = 60;
	
	private Logger logger = LogManager.getLogger(WDFTestBaseSetup.class.getName());
	private Marker WDFTESTBASESETUP = MarkerManager.getMarker("WDFTESTBASESETUP");

	private DriverFactory df;
	private WebDriver wd;
	private EventFiringWebDriver eventDriver;
	private EventHandler handler = new EventHandler();
	
	public WDFTestBaseSetup(String browserType, String desiredCapabilitiesConfigJSON, String bringBrowserToFront, String resizeBrowser) {
		super();
		try {
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browserType);
			dc.merge(this.getCommonDesiredCapabilties());
			switch (browserType.toLowerCase()) {
			case "firefox":
				LoadFireFoxProfile lffp = new LoadFireFoxProfile();
				dc.setCapability(FirefoxDriver.PROFILE, lffp.getFirefoxProfile());
				break;
			case "chrome":
				ChromeDesiredCapabilities cdc = new ChromeDesiredCapabilities();
				dc.merge(cdc.getDesiredCapabilities());
				break;
			case "opera":
				
				break;
			}
			this.df = new DriverFactory(dc);
			this.wd = this.df.getDriver();
			this.wd.manage().timeouts().implicitlyWait(WAIT, TimeUnit.SECONDS);
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
		this.eventDriver.quit();
	}
	
	private void logException(String browserType, Exception e) {
		this.logger.fatal(WDFTESTBASESETUP, "Failed to load browser " + (char)34 + browserType + (char)34 + ".");
		this.logger.debug(WDFTESTBASESETUP, "Message: " + e.getMessage());
		this.logger.debug(WDFTESTBASESETUP, "Localized message: " + (char)34 + e.getLocalizedMessage() + (char)34 + ".");
		this.logger.debug(WDFTESTBASESETUP, "Cause: " + (char)34 + e.getCause() + (char)34 + ".");
		this.logger.debug(WDFTESTBASESETUP, "Stack Trace: " + e.getStackTrace());
	}
	
}
