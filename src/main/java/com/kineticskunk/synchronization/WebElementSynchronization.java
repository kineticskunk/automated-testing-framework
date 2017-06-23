package com.kineticskunk.synchronization;

import com.kineticskunk.library.ApplicationProperties;
import com.kineticskunk.synchronization.WebDriverSynchronization;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WebElementSynchronization extends WebDriverSynchronization {
	
	private final Logger logger = LogManager.getLogger(WebElementSynchronization.class.getName());
	private final Marker WEBELEMENTSYCHRONIZATION = MarkerManager.getMarker(WebElementSynchronization.class.getName());

    public WebElementSynchronization(WebDriver driver) {
        super(driver);
    }
    
    private static WebElementSynchronization wes;
    
    public static WebElementSynchronization getInstance(WebDriver driver) {
        if (wes != null) return wes;
        synchronized (ApplicationProperties.class) {
            if (wes != null) return wes;
            {
            	wes = new WebElementSynchronization(driver);
            }
            return wes;
        }
    }

    public boolean waitForPresenceOfUnspecifiedText(final WebElement element, final String attributeName) {
        return (Boolean) this.driverWait().until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver d) {
                return !element.getAttribute(attributeName).isEmpty();
            }
        });
    }

    public boolean waitForStringToBecomeNumeric(final WebElement element, final String attributeName) {
        return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver d) {
                return WebElementSynchronization.isNumeric(element.getAttribute(attributeName));
            }
        });
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) continue;
            return false;
        }
        return true;
    }

    public boolean elementNotToBeDisplayed(final WebElement element) {
        return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver d) {
                return !element.isDisplayed();
            }
        });
    }

    public boolean waitForPresenceOfRows(final WebElement element, final String tagName, final int size) {
        if (element.findElements(By.tagName(tagName)).size() > size) {
            return true;
        }
        return (Boolean)this.driverWait().until(new ExpectedCondition<Boolean>(){

            @Override
            public Boolean apply(WebDriver d) {
                return element.findElements(By.tagName(tagName)).size() > size;
            }
        });
    }

    public WebElement waitForVisibilityOfElement(WebElement element) throws NoSuchElementException {
        return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForElementToByPresentByLocator(String findBy, String findByValue) {
        switch (findBy.toUpperCase()) {
            case "XPATH": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath(findByValue)));
            }
            case "CSS": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(findByValue)));
            }
            case "NAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.name(findByValue)));
            }
            case "ID": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.id(findByValue)));
            }
            case "LINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.linkText(findByValue)));
            }
            case "TAGNAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.tagName(findByValue)));
            }
            case "PARTIALLINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(findByValue)));
            }
            case "CLASSNAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.presenceOfElementLocated(By.className(findByValue)));
            }
        }
        logger.error("Find by \"" + findByValue + '\"' + " is unsupported");
        return null;
    }

    public WebElement waitForChildElementToByPresentByLocator(WebElement parentElement, String findBy, String findByValue) {
        switch (findBy.toUpperCase()) {
            case "XPATH": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.xpath(findByValue));
            }
            case "CSS": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.cssSelector(findByValue));
            }
            case "NAME": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.name(findByValue));
            }
            case "ID": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.id(findByValue));
            }
            case "LINKTEXT": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.linkText(findByValue));
            }
            case "TAGNAME": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.tagName(findByValue));
            }
            case "PARTIALLINKTEXT": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.partialLinkText(findByValue));
            }
            case "CLASSNAME": {
                return this.waitForVisibilityOfElement(parentElement).findElement(By.className(findByValue));
            }
        }
        logger.error("Find by \"" + findByValue + '\"' + " is unsupported");
        return null;
    }

    public List<WebElement> waitForChildElementsToByPresentByLocator(WebElement parentElement, String findBy, String findByValue) throws NoSuchElementException {
        switch (findBy.toUpperCase()) {
            case "XPATH": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.xpath(findByValue));
            }
            case "CSS": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.cssSelector(findByValue));
            }
            case "NAME": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.name(findByValue));
            }
            case "ID": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.id(findByValue));
            }
            case "LINKTEXT": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.linkText(findByValue));
            }
            case "TAGNAME": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.tagName(findByValue));
            }
            case "PARTIALLINKTEXT": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.partialLinkText(findByValue));
            }
            case "CLASSNAME": {
                return this.waitForVisibilityOfElement(parentElement).findElements(By.className(findByValue));
            }
        }
        logger.error("Find by \"" + findByValue + '\"' + " is unsupported");
        return null;
    }

    public WebElement waitForVisibilityOfElementBy(String findBy, String findByValue) {
        switch (findBy.toUpperCase()) {
            case "CSS": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(findByValue)));
            }
            case "NAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.name(findByValue)));
            }
            case "ID": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.id(findByValue)));
            }
            case "LINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.linkText(findByValue)));
            }
            case "TAGNAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName(findByValue)));
            }
            case "PARTIALLINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(findByValue)));
            }
            case "CLASSNAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.visibilityOfElementLocated(By.className(findByValue)));
            }
        }
        logger.error("Find by \"" + findBy + '\"' + " is unsupported");
        return null;
    }

    public List<WebElement> getPresenceAllElementsLocatedBy(String findBy, String findByValue) {
        switch (findBy.toLowerCase()) {
            case "id": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(findByValue)));
            }
            case "linktext": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(findByValue)));
            }
            case "name": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(findByValue)));
            }
            case "xpath": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(findByValue)));
            }
            case "css": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(findByValue)));
            }
            case "tagname": {
                return this.driverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(findByValue)));
            }
        }
        logger.error("Find by \"" + findBy + '\"' + " is unsupported");
        return null;
    }

    public boolean waitForInvisibilityOfElementLocatedBy(String findBy, String findByValue) {
        try {
            return (Boolean)this.driverWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id(findByValue)));
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean waitForInvisibilityOfElementBy(String id) {
        try {
            return (Boolean)this.driverWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    public boolean waitForElementToBeDisplayed(WebElement element) {
        return ((WebElement)this.driverWait().until(ExpectedConditions.visibilityOf(element))).isDisplayed();
    }

    public WebElement waitForElementToBeClickable(WebElement element) throws NoSuchElementException {
        return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementToBeClickableBy(String findBy, String findByValue) {
        switch (findBy.toUpperCase()) {
            case "XPATH": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.xpath(findByValue)));
            }
            case "CSS": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(findByValue)));
            }
            case "NAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.name(findByValue)));
            }
            case "ID": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.id(findByValue)));
            }
            case "LINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.linkText(findByValue)));
            }
            case "TAGNAME": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.tagName(findByValue)));
            }
            case "PARTIALLINKTEXT": {
                return (WebElement)this.driverWait().until(ExpectedConditions.elementToBeClickable(By.partialLinkText(findByValue)));
            }
        }
        logger.error("Find by \"" + findBy + '\"' + " is unsupported");
        return null;
    }

    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return (Boolean)this.driverWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean waitForElementToBeStale(WebElement element) {
        return (Boolean)this.driverWait().until(ExpectedConditions.stalenessOf(element));
    }

    public boolean waitForInvisibilityOfElementLocated(String findByValue) {
        try {
            return (Boolean)this.driverWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id(findByValue)));
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }

}
