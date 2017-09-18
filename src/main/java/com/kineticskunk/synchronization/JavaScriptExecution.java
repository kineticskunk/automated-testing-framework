package com.kineticskunk.synchronization;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JavaScriptExecution {
	
	private WebDriver driver;
	protected JavascriptExecutor je;
    protected int waitFor;

    public JavaScriptExecution(WebDriver driver) {
        this.je = (JavascriptExecutor)((Object)driver);
        this.driver = driver;
        this.waitFor = 0;
    }

    public void setWaitFor(int waitFor) {
        this.waitFor = waitFor;
    }
    
    public void waitForAjax() {
        new WebDriverWait(this.driver, this.waitFor).until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean)JavaScriptExecution.this.je.executeScript("return jQuery.active == 0", new Object[0]);
            }
        });
    }

    public void waitForAjax(WebDriver driver) {
        new WebDriverWait(driver, this.waitFor).until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean)JavaScriptExecution.this.je.executeScript("return jQuery.active == 0", new Object[0]);
            }
        });
    }

    public void scrollWebElementIntoView(WebElement element) {
        this.je.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void waitForAjaxToFinish() {
        @SuppressWarnings("unused")
		boolean ajaxWorking;
        int timeout = 0;
        while (timeout < this.waitFor && !(ajaxWorking = ((Boolean)this.je.executeScript("return jQuery.active == 0", new Object[0])).booleanValue())) {
            try {
                ++timeout;
                Thread.sleep(500);
            }
            catch (Exception var3_3) {}
        }
    }

}
