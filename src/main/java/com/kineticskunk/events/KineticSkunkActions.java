package com.kineticskunk.events;

/**
 * <KineticSkunkActions interacts with WebElements in a browser.
 *  KineticSkunkActions is a functional test automation framework.
 *  KineticSkunkActions is wrapper which inherets from Selenium 
 *  WebDriver.>
Copyright (C) <2017>  <Donovan Mulder>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.kineticskunk.library.ApplicationProperties;
import com.kineticskunk.synchronization.WebElementSynchronization;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class KineticSkunkActions extends WebElementSynchronization {
	
	private static final Logger logger = LogManager.getLogger(KineticSkunkActions.class.getName());
    private static final Marker WEBELEMENT_ACTION = MarkerManager.getMarker("WEBELEMENT_ACTION");
    protected WebDriver driver;
    protected Actions action;

    public KineticSkunkActions(WebDriver driver) throws NoSuchElementException {
        super(driver);
        this.action = new Actions(driver);
    }
    
    private static KineticSkunkActions ksa;
    
    public static KineticSkunkActions getInstance(WebDriver driver) {
        if (ksa != null) return ksa;
        synchronized (ApplicationProperties.class) {
            if (ksa != null) return ksa;
            {
                ksa = new KineticSkunkActions(driver);
            }
            return ksa;
        }
    }

    public void catchError(String methodName, String params, Exception ex) {
        if (logger.isDebugEnabled()) {
            logger.debug(WEBELEMENT_ACTION, "RUN {} USING {}", methodName, params);
            logger.debug("Cause = " + ex.getCause());
            logger.debug("Message = " + ex.getMessage());
            logger.debug("Local Message = " + ex.getLocalizedMessage());
        }
    }

    public Actions moveToWebElement(WebElement element) {
        return this.action.moveToElement(this.waitForVisibilityOfElement(element));
    }

    public void performActionClickAndHold(WebElement element) {
        this.moveToWebElement(this.waitForElementToBeClickable(element)).clickAndHold().release().perform();
    }

    public void performSelectRadioButton(WebElement element) {
        this.moveToWebElement(element).click().release().perform();
    }

    public void performActionDoubleClick(WebElement element) {
        this.moveToWebElement(this.waitForElementToBeClickable(element)).doubleClick(element).release().build().perform();
    }

    public void performActionClick(WebElement element) throws NoSuchElementException {
        try {
        	if (this.waitForElementToBeDisplayed(element)) {
        		this.waitForElementToBeClickable(element).click();
        	}
        }
        catch (Exception ex) {
            this.catchError("performActionClick", element.toString(), ex);
        }
    }

    public void performRightClick(WebElement element) {
        this.moveToWebElement(this.waitForElementToBeClickable(element)).contextClick(element).build().perform();
    }

    public void performSendEnterKey(WebElement element) {
        this.moveToWebElement(this.waitForElementToBeClickable(element)).sendKeys(Keys.ENTER).perform();
    }

    public void onMouseOver(WebElement sourceElement, WebElement targetElement) {
        this.moveToWebElement(sourceElement).dragAndDrop(sourceElement, targetElement);
    }

    public void setValue(WebElement element, String keys) {
        try {
            this.moveToWebElement(element).sendKeys(element, keys).build().perform();
        }
        catch (Exception ex) {
            this.catchError("setValue", element.toString(), ex);
        }
    }

    public void clearElement(WebElement element) {
        this.moveToWebElement(element);
        element.clear();
    }

    public String getValue(WebElement element, String name) {
        try {
            return this.waitForVisibilityOfElement(element).getAttribute(name);
        }
        catch (Exception ex) {
            this.catchError("performActionClick", element.toString(), ex);
            return null;
        }
    }

    public String getText(WebElement element) {
        try {
            return this.waitForVisibilityOfElement(element).getText();
        }
        catch (Exception ex) {
            this.catchError("performActionClick", element.toString(), ex);
            return null;
        }
    }

    public void clickChildElement(List<WebElement> elements, String attibute, String attributeValue) {
        for (WebElement el : elements) {
            if (!el.getAttribute(attibute).equalsIgnoreCase(attributeValue)) continue;
            this.performActionClick(el);
            break;
        }
    }

    public void setChildElementValue(List<WebElement> elements, String attibute, String attributeValue, String keys) {
        for (WebElement el : elements) {
            if (!el.getAttribute(attibute).equalsIgnoreCase(attributeValue)) continue;
            this.setValue(el, keys);
            break;
        }
    }

    public String getChildElementAttributeValue(List<WebElement> elements, String identificationAttibute, String identificationAttibuteValue, String childElementAttributeName) {
        for (WebElement el : elements) {
            if (!el.getAttribute(identificationAttibute).equalsIgnoreCase(identificationAttibuteValue)) continue;
            return this.getValue(el, childElementAttributeName);
        }
        return null;
    }

    public void selectValue(WebElement element, String value) {
        ComboBoxes cb = new ComboBoxes(this.waitForVisibilityOfElement(element));
        cb.selectComboboxValue(value);
    }

    public void deselectValue(WebElement element, String value) {
        ComboBoxes cb = new ComboBoxes(this.waitForVisibilityOfElement(element));
        cb.deselectComboboxValue(value);
    }

    public String getSelectedValue(WebElement element) {
        ComboBoxes cb = new ComboBoxes(this.waitForVisibilityOfElement(element));
        return cb.getComboBoxSelectedValue();
    }

    public void selectRadioButton(WebElement radioButton) {
        RadioButtons rb = new RadioButtons(this, radioButton);
        rb.selectRadioButton();
    }

    public void selectRadioButtonInList(List<WebElement> radioButtons, String identificationAttibute, String identificationAttibuteValue) {
        RadioButtons rb = new RadioButtons(this, radioButtons);
        rb.selectRadioButtonInList(identificationAttibute, identificationAttibuteValue);
    }

    public boolean isSelectRadioButtonInList(List<WebElement> radioButtons, String identificationAttibute, String identificationAttibuteValue) {
        RadioButtons rb = new RadioButtons(this, radioButtons);
        return rb.isSelectedRadioButtonInList(identificationAttibute, identificationAttibuteValue);
    }

    public boolean isCheckedRadioButtonInList(List<WebElement> radioButtons, String identificationAttibute, String identificationAttibuteValue) {
        RadioButtons rb = new RadioButtons(this, radioButtons);
        return rb.isCheckedRadioButtonInList(identificationAttibute, identificationAttibuteValue);
    }

    private class RadioButtons {
    	
        private WebElement radioButton;
        private List<WebElement> radioButtons;
        private KineticSkunkActions ksa;

        private RadioButtons(KineticSkunkActions ksa, WebElement element) {
            this.ksa = ksa;
            this.radioButton = element;
        }

        private RadioButtons(KineticSkunkActions ksa, List<WebElement> elements) {
            this.ksa = ksa;
            this.radioButtons = elements;
        }

        private boolean isRadioButtonSelected() {
            return this.radioButton.isSelected();
        }

        private void selectRadioButton() {
            if (!this.isRadioButtonSelected()) {
                this.ksa.performActionClick(this.radioButton);
            }
        }

        public void selectRadioButtonInList(String attibute, String attributeValue) {
            for (WebElement el : this.radioButtons) {
                if (!el.getAttribute(attibute).equalsIgnoreCase(attributeValue)) continue;
                	if (el.isSelected()) break;
                	this.ksa.performActionClick(el);
                break;
            }
        }

        public boolean isSelectedRadioButtonInList(String attibute, String attributeValue) {
            for (WebElement el : this.radioButtons) {
                if (!el.getAttribute(attibute).equalsIgnoreCase(attributeValue)) continue;
                return el.isSelected();
            }
            return false;
        }

        public boolean isCheckedRadioButtonInList(String attibute, String attributeValue) {
            for (WebElement el : this.radioButtons) {
                if (!el.getAttribute(attibute).equalsIgnoreCase(attributeValue)) continue;
                return Boolean.valueOf(el.getAttribute("checked"));
            }
            return false;
        }
    }

    public class ComboBoxes {
        private Select comboBox;

        public ComboBoxes(WebElement element) {
            this.comboBox = new Select(element);
        }

        public String getComboBoxSelectedValue() {
            return this.comboBox.getFirstSelectedOption().getText();
        }

        public void selectComboboxValue(String value) {
            if (this.valueIsInCombobox(value)) {
                this.comboBox.selectByVisibleText(value);
            }
        }

        public void deselectComboboxValue(String value) {
            if (this.valueIsInCombobox(value)) {
                this.comboBox.deselectByVisibleText(value);
            }
        }

        public boolean valueIsInCombobox(String value) {
            List<WebElement> options = this.comboBox.getOptions();
            for (WebElement opt : options) {
                if (!opt.getText().equalsIgnoreCase(value)) continue;
                return true;
            }
            return false;
        }
    }

}
