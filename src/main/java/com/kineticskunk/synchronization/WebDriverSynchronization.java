package com.kineticskunk.synchronization;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.kineticskunk.library.ApplicationProperties;

public class WebDriverSynchronization extends JavaScriptExecution {

	private static final Logger logger = LogManager.getLogger(WebDriverSynchronization.class.getName());
    private int loopFor;
    private int waitFor;
    private static WebDriverSynchronization wds;
    protected WebDriver driver;
    protected WebDriverWait wait;
    public ApplicationProperties ap;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static WebDriverSynchronization getInstance(WebDriver driver) {
        if (wds != null) return wds;
        synchronized (ApplicationProperties.class) {
            if (wds != null) return wds;
            {
                wds = new WebDriverSynchronization(driver);
            }
            return wds;
        }
    }

    public WebDriverSynchronization(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.ap = ApplicationProperties.getInstance();
        this.loopFor = Integer.valueOf(10);
        this.waitFor = Integer.valueOf(15);
        this.wait = new WebDriverWait(this.driver, this.loopFor, this.waitFor);
        this.setWaitFor(this.waitFor);
    }

    public void switchBrowser(String title) {
        for (String winHandle : this.driver.getWindowHandles()) {
            this.driver.switchTo().window(winHandle);
            if (!this.driver.getTitle().equalsIgnoreCase(title)) continue;
            this.driver.switchTo().window(winHandle);
            break;
        }
    }

    public WebDriverWait driverWait() {
        return this.wait;
    }

    public boolean waitForPageLoadedByTitle(final String title) throws NumberFormatException, IOException {
        try {
            return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

                @Override
                public Boolean apply(WebDriver d) {
                    return d.getTitle().equalsIgnoreCase(title);
                }
            });
        }
        catch (NumberFormatException e) {
            logger.error("waitForPageLoadedByURL failed to synchronize with webdriver " + this.driver.toString() + " using title '" + title + "'");
            return false;
        }
    }

    public boolean waitForPageLoadedByText(final String text) {
        try {
            return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

                @Override
                public Boolean apply(WebDriver d) {
                    return d.getPageSource().contains(text);
                }
            });
        }
        catch (Exception e) {
            logger.error("waitForPageLoadedByURL failed to synchronize with webdriver " + this.driver.toString() + " using text '" + text + "'");
            return false;
        }
    }

    public boolean waitForPageLoadedByURL(final String url) {
        try {
            return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

                @Override
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().contains(url);
                }
            });
        }
        catch (Exception e) {
            logger.error("waitForPageLoadedByURL failed to synchronize with webdriver " + this.driver.toString() + " using url '" + url + "'");
            return false;
        }
    }

    public WebDriver frameToBeAvailableAndSwitchToItByTagName(String tagName) {
        return (WebDriver)this.driverWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName(tagName)));
    }
	
	
}
