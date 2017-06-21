package com.kineticskunk.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class EventHandler implements WebDriverEventListener {
	
	private final Logger logger = LogManager.getLogger(EventHandler.class.getName());
    private final Marker EVENTHANDLER = MarkerManager.getMarker("EVENTHANDLER");
    
    private AlertsHandler alertHandler;
    private String alertText;
    private String currentURL;
    private String navigateToURL;

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		this.logger.info(EVENTHANDLER, "In beforeAlertAccept");
		this.alertHandler = new AlertsHandler(driver);
		if (this.alertHandler.isAlertPresent()) {
			this.alertText = this.alertHandler.getAlertText();
			this.logger.debug(EVENTHANDLER, String.format("Alert with text (%s) exists.", this.alertText));
		}
	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		this.logger.info(EVENTHANDLER, "In afterAlertAccept");
		this.alertHandler = new AlertsHandler(driver);
		if (!this.alertHandler.isAlertPresent()) {
			this.logger.debug(EVENTHANDLER, String.format("Alert with text (%s) does not exist.", this.alertText));
		} else {
			this.logger.warn(EVENTHANDLER, String.format("Alert with text (%s) exists.", this.alertText));
		}
	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		this.logger.info(EVENTHANDLER, "In afterAlertDismiss");
		this.alertHandler = new AlertsHandler(driver);
		if (!this.alertHandler.isAlertPresent()) {
			this.logger.debug(EVENTHANDLER, String.format("Alert with text (%s) does not exist.", this.alertText));
		} else {
			this.logger.warn(EVENTHANDLER, String.format("Alert with text (%s) exists.", this.alertText));
		}
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		this.logger.info(EVENTHANDLER, "In beforeAlertDismiss");
		this.alertHandler = new AlertsHandler(driver);
		if (this.alertHandler.isAlertPresent()) {
			this.alertText = this.alertHandler.getAlertText();
			this.logger.debug(EVENTHANDLER, String.format("Alert with text (%s) exists.", this.alertText));
		}
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		this.currentURL = driver.getCurrentUrl();
		this.navigateToURL = url;
		this.logger.info(EVENTHANDLER, "In beforeNavigateTo");
		this.logger.debug(EVENTHANDLER, String.format("Current url = (%s)", this.currentURL));
		this.logger.debug(EVENTHANDLER, String.format("Navigating to url = (%s)", url));
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		this.logger.info(EVENTHANDLER, "In afterNavigateTo");
		if (this.currentURL.equalsIgnoreCase(this.navigateToURL)) {
			this.logger.warn(EVENTHANDLER, String.format("Failed to navigate to url = (%s)", this.navigateToURL));
		} else {
			this.logger.debug(EVENTHANDLER, String.format("Navigated to url = (%s)", driver.getCurrentUrl()));
		}
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		this.logger.info(EVENTHANDLER, String.format("Just before beforeNavigateBack (%s)", driver.getCurrentUrl()));
		
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

}
