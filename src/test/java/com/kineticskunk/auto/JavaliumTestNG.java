package com.kineticskunk.auto;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.kineticskunk.auto.desiredcapabilities.LoadDesiredCapabilities;

public class JavaliumTestNG extends WDFTestBaseSetup {
	
	private LoadDesiredCapabilities ldc = new LoadDesiredCapabilities();
	private WebDriver wd;
	
	@Parameters( { "browserType", "desiredCapabilitiesConfigJSON", "bringBrowserToFront", "resizeBrowser"} )
	public JavaliumTestNG(String browserType, String desiredCapabilitiesConfigJSON, String bringBrowserToFront, String resizeBrowser) throws IOException {
		super(browserType, desiredCapabilitiesConfigJSON, bringBrowserToFront, resizeBrowser);
		this.wd = getDriver();
	}
	
	@Test(priority = 1, groups = "LoadDesiredCapabilities")
	@Parameters( { "browserType" })
	public void verifyBrowserType(String browserType) {
		Assert.assertTrue(this.wd.getTitle().equalsIgnoreCase(browserType));
	}
	
	@AfterClass()
	public void afterLoadDesiredCapabilitiesTestNG() {
		this.wd.quit();
	}
}
