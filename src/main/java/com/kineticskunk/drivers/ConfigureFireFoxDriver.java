/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.drivers;

import com.kineticskunk.library.ApplicationProperties;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ConfigureFireFoxDriver {
    private static final String ENABLENATIVEEVENTS = "enablenativeevents";
    private static final String BROWSERCACHEDDISKENABLE = "browsercacheddiskenabled";
    private static final String BROWSERDOWNLOADDIRECTORY = "downloadlocation";
    private static final String BROWSERHELPERAPPSNEVERASKSAVETODISK = "helperappfilestypes";
    private WebDriver driver = null;
    private FirefoxProfile profile = new FirefoxProfile();
    private DesiredCapabilities dc = new DesiredCapabilities();
    private ApplicationProperties ap = ApplicationProperties.getInstance();

    public void configureDriver() throws IOException {
        this.setProfile();
        this.setDesiredCapabilities();
        this.setDriver();
        this.driver.manage().window().maximize();
    }

    private void setDriver() {
        this.driver = new FirefoxDriver(this.getDesiredCapabilities());
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    private void setProfile() throws IOException {
        this.profile.setAcceptUntrustedCertificates(true);
        //this.profile.setEnableNativeEvents(Boolean.valueOf(this.ap.getPropValue("enablenativeevents")));
        this.profile.setPreference("browser.cache.disk.enable", Boolean.valueOf(this.ap.getPropValue("browsercacheddiskenabled")));
        this.profile.setPreference("browser.download.dir", this.ap.getPropValue("downloadlocation"));
        this.profile.setPreference("browser.download.folderList", 2);
        this.profile.setPreference("browser.download.manager.showWhenStarting", false);
        this.profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        this.profile.setPreference("browser.helperApps.neverAsk.saveToDisk", this.ap.getPropValue("helperappfilestypes"));
        this.profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        this.profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        this.profile.setPreference("browser.download.manager.focusWhenStarting", false);
        this.profile.setPreference("browser.download.manager.useWindow", false);
        this.profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        this.profile.setPreference("browser.download.manager.closeWhenDone", false);
        this.profile.addExtension(new File("/Users/yodaqua/Library/Application Support/Firefox/Profiles/24nxve48.default/extensions/firebug@software.joehewitt.com.xpi"));
        this.profile.setPreference("extensions.firebug.currentVersion", "2.0.10b1");
        this.profile.setPreference("extensions.firebug.console.enableSites", true);
        this.profile.setPreference("extensions.firebug.script.enableSites", true);
        this.profile.setPreference("extensions.firebug.net.enableSites", true);
        this.profile.setPreference("extensions.firebug.allPagesActivation", "on");
        this.profile.setPreference("extensions.firebug.cookies.enableSites", true);
    }

    private void setDesiredCapabilities() {
        this.dc.setJavascriptEnabled(true);
        this.dc.setCapability("firefox_profile", this.profile);
    }

    private DesiredCapabilities getDesiredCapabilities() {
        return this.dc;
    }
}

