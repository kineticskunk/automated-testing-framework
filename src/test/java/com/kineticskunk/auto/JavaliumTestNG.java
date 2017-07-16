package com.kineticskunk.auto;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.kineticskunk.base.TestBaseSetup;

public class JavaliumTestNG extends TestBaseSetup {
	
	private WebDriver wd;
	
	@Parameters( { "browserType", "testExecutionProperties", "desiredCapabilitiesProperties"} )
	public JavaliumTestNG(String browserType, String testExecutionProperties, String desiredCapabilitiesProperties) throws IOException {
		super(browserType, testExecutionProperties, desiredCapabilitiesProperties);
		this.wd = getDriver();
	}
	
	@BeforeGroups(groups = "Javalium")
	@Parameters( { "url" })
	public void navigateToSite(String url) {
		this.wd.navigate().to(url);
	}
	
	@Test(priority = 1, groups = "Javalium")
	@Parameters( { "browserTitle" })
	public void verifyBrowserType(String browserTitle) {
		Assert.assertTrue(this.wd.getTitle().equalsIgnoreCase(browserTitle));
	}
	
	@AfterClass()
	public void afterLoadDesiredCapabilitiesTestNG() {
		this.wd.quit();
	}
}
