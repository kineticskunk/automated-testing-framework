package com.kineticskunk.events;

import com.kineticskunk.synchronization.WebElementSynchronization;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ComboBoxes extends WebElementSynchronization {
    private Select ComboBox;

    public ComboBoxes(WebDriver driver, WebElement element) {
        super(driver);
        this.ComboBox = new Select(this.waitForVisibilityOfElement(element));
    }

    public String getComboBoxSelectedValue() {
        return this.ComboBox.getFirstSelectedOption().getText();
    }

    public void selectComboboxValue(String value) {
        if (this.valueIsInCombobox(value)) {
            this.ComboBox.selectByVisibleText(value);
        }
    }

    public boolean valueIsInCombobox(String value) {
        List<WebElement> options = this.ComboBox.getOptions();
        return options.contains(value);
    }
}

