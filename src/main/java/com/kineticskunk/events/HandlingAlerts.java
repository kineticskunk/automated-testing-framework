package com.kineticskunk.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;

import com.kineticskunk.synchronization.WebDriverSynchronization;

public class HandlingAlerts {
	
	private final Logger logger = LogManager.getLogger(HandlingAlerts.class.getName());
	private final Marker HANDLINGALERTS = MarkerManager.getMarker(HandlingAlerts.class.getName());
	
	private Alert alert;
	private WebDriverSynchronization wds;
	
	public HandlingAlerts(WebDriver driver) {
		this.alert = driver.switchTo().alert();
		this.wds = WebDriverSynchronization.getInstance(driver);
	}
	
	public boolean isAlertPresent() {
		try {
			return this.wds.waitForAlert();
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while checking if the ALERT exists.");
			return false;
		}
		
	}
	
	public String getAlertText() {
		try {
			return this.alert.getText();
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "Could not retrieve the ALERT text.");
			throw new WebDriverException();
		}
	}
	
	public void acceptAlert() {
		try {
			this.alert.accept();
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while emptying to accept ALERT with text (%s).", this.getAlertText());
			throw e;
		}
	}
	
	public void dismissAlert() {
		try {
			this.alert.dismiss();
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while emptying to dismiss ALERT with text (%s).", this.getAlertText());
			throw e;
		}
	}
	
	public void sendKeysToAlert(String keys) {
		try {
			this.alert.sendKeys(keys);
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while emptying to send keys (%s) to ALERT with text (%s).", keys, this.getAlertText());
			throw e;
		}
	}
	
	public void authenticateAlertUsing(String username, String password) {
		try {
			Credentials credentials = new UserAndPassword(username, password);
			this.alert.authenticateUsing(credentials);
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while emptying to authenticate against ALERT with text (%s) with username (%s) and password (%s).", this.getAlertText(), username, password);
			throw e;
		}
	}
	
	public void setAlertCredentials(String username, String password) {
		try {
			Credentials credentials = new UserAndPassword(username, password);
			this.alert.setCredentials(credentials);
		} catch (WebDriverException e) {
			this.logger.error(HANDLINGALERTS, "An error occured while emptying to set credentials against ALERT with text (%s) using, username (%s) and password (%s).", this.getAlertText(), username, password);
			throw e;
		}
	}

}
