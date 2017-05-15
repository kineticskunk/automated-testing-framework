/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.drivers;

import com.kineticskunk.library.ApplicationProperties;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ConfigureChromeDriver {
    private static final String CHROMEDRIVERLOCATION = "chromedriverlocation";
    private static final String CHROMEDRIVERNAME = "chromedrivername";
    private DesiredCapabilities dc = DesiredCapabilities.chrome();
    private ChromeDriver cd = null;
    private ChromeDriverService cs = null;
    private ChromeOptions co = new ChromeOptions();
    private ApplicationProperties ap = ApplicationProperties.getInstance();

    public void configureDriver() throws Exception {
        this.setChromeService();
        this.setChromeOptions();
        this.setDesiredCapabilities();
        this.setDriver();
        this.startChromeService();
        this.cd.manage().window().maximize();
    }

    public ChromeDriver getDriver() {
        return this.cd;
    }

    private void startChromeService() throws IOException {
        this.cs.start();
    }

    public void stopChromeService() {
        this.cs.stop();
    }

    private void setChromeService() {
        this.cs = (ChromeDriverService)((ChromeDriverService.Builder)((ChromeDriverService.Builder)new ChromeDriverService.Builder().usingDriverExecutable(new File(this.ap.getPropValue("chromedriverlocation") + this.ap.getPropValue("chromedrivername")))).usingAnyFreePort()).build();
    }

    public ChromeDriverService getChromeDriverService() {
        return this.cs;
    }

    private void setChromeOptions() {
        this.co.addArguments("start-maximized");
        this.co.addArguments("ignore-certificate-errors");
    }

    private void setDesiredCapabilities() {
        this.dc.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
        this.dc.setJavascriptEnabled(true);
        this.dc.setCapability("chromeOptions", this.co);
    }

    private void setDriver() throws Exception {
        this.cd = new ChromeDriver(this.cs, this.dc);
    }
}

