package com.kineticskunk.auto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.kineticskunk.auto.desiredcapabilities.LoadDesiredCapabilities;
import com.kineticskunk.driverfactory.DriverExecutable;
import com.kineticskunk.driverfactory.DriverFactory;

public class JavaliumTestNG extends TestBaseSetup {
	
	public JavaliumTestNG(String configFileLocation, String configFileName) throws IOException {
		super(configFileLocation, configFileName);
		// TODO Auto-generated constructor stub
	}

	private final Logger logger = LogManager.getLogger(JavaliumTestNG.class.getName());
	private final Marker LOADDESIREDCAPABILITIES = MarkerManager.getMarker("LOADDESIREDCAPABILITIES");
	
	private static final String testSite = "https://www.google.co.za";
	private static final String testSiteTitle = "google";
	
	private LoadDesiredCapabilities ldc = new LoadDesiredCapabilities();
	private WebDriver wd;
	private DriverFactory df;
	private DriverExecutable de;
	
	@BeforeClass
	@Parameters( { "browserType", "desiredCapabilitiesConfigJSON" } )
	public void setLoadDesiredCapabilities(String browserType, String desiredCapabilitiesConfigJSON) throws Exception {
		this.ldc = new LoadDesiredCapabilities(browserType, desiredCapabilitiesConfigJSON);
		this.ldc.setBrowerDesiredCapabilities();
		this.de = new DriverExecutable(browserType);
		this.de.setDriverExecutable();
		this.df =  new DriverFactory(this.ldc.getDesiredCapabilities());
		this.wd = this.df.getDriver();
	}
	
	@Test(priority = 0, groups = "LoadDesiredCapabilities")
	public void verifyDesiredCapabiltiesJSONFileISNOTNULL() {
		Assert.assertTrue(this.ldc.getDesiredCapabilitiesJSONObject() != null);
	}
	
	@Test(priority = 1, groups = "LoadDesiredCapabilities")
	@Parameters( { "browserType" })
	public void verifyBrowserType(String browserType) {
		Assert.assertTrue(this.ldc.getDesiredCapabilities().getBrowserName().equalsIgnoreCase(browserType));
	}
	
	@AfterGroups(groups = "LoadDesiredCapabilities")
	@Parameters( { "browserType" })
	public void afterLoadDesiredCapabilities(String browserType) {
		this.wd.get(testSite);
	}
	
	@Test(priority = 2, groups = "DriverFactory")
	public void verifyBrowserOpening() {
		Assert.assertTrue(this.wd.getTitle().equalsIgnoreCase(testSiteTitle));
	}
	
	@AfterClass()
	public void afterLoadDesiredCapabilitiesTestNG() {
		this.wd.quit();
	}
}
