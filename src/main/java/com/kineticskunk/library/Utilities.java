/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Utilities {
    private static Logger logger;
    private String strProcessID;

    public Utilities() {
        logger = LogManager.getLogger(Utilities.class.getName());
        this.strProcessID = null;
    }

    public void setProcessID(String strAppName, String strOS) {
        logger.entry();
        String strReportString = "Application \"" + strAppName + '\"' + " has a process ID of ";
        try {
            String line = null;
            Process p = Runtime.getRuntime().exec("ps -e");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            block8 : while ((line = input.readLine()) != null) {
                switch (strOS.toLowerCase().replace(" ", "")) {
                    case "macosx": {
                        if (!line.toLowerCase().contains(strAppName.toLowerCase() + ".app")) continue block8;
                        this.strProcessID = line.substring(1, 5).replace(" ", "");
                        continue block8;
                    }
                }
                logger.warn("OS \"" + strOS + '\"' + " is not supported. Please pray and hope for the best. LOL :)");
            }
            input.close();
        }
        catch (Exception err) {
            logger.error("A catastrophic error has occured. Woman and children first. In the mean time try to make sense of the garbage error message: " + err.getMessage());
            err.printStackTrace();
        }
        logger.info(strReportString + '\"' + this.strProcessID.toString() + '\"');
        logger.exit();
    }

    public void setPID(String strAppName) {
        try {
            String line = null;
            Process p = Runtime.getRuntime().exec("ps -e");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.toLowerCase().contains(strAppName.toLowerCase())) continue;
                this.strProcessID = line.substring(0, 5).replace(" ", "");
            }
            input.close();
        }
        catch (Exception err) {
            logger.error("A catastrophic error has occured. Woman and children first. In the mean time try to make sense of the garbage error message: " + err.getMessage());
            err.printStackTrace();
        }
    }

    public String RandomAlphaNumeric(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = length; i > 0; i -= 12) {
            int n = Math.min(12, Math.abs(i));
            sb.append(StringUtils.leftPad(Long.toString(Math.round(Math.random() * Math.pow(36.0, n)), 36), n, '0'));
        }
        return sb.toString();
    }

    public int RandomNumeric(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public String RandomAlpha(int intLength) {
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(intLength);
        for (int i = 0; i < intLength; ++i) {
            int randomLimitedInt = leftLimit + (int)(new Random().nextFloat() * (float)(rightLimit - leftLimit));
            buffer.append((char)randomLimitedInt);
        }
        return buffer.toString();
    }

    public boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public void pause(int seconds) {
        Date start = new Date();
        Date end = new Date();
        while (end.getTime() - start.getTime() < (long)(seconds * 1000)) {
            end = new Date();
        }
        start = null;
        end = null;
    }

    public boolean takeScreenshot(WebDriver wDriver, String strAbsoluteScreenShotName) throws InterruptedException {
        File scrFile = ((TakesScreenshot)((Object)wDriver)).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(strAbsoluteScreenShotName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return scrFile.exists();
    }

    public String getAbsoluteTestScriptPath(String strFileLocation, String strFileName, String strFileType) {
        String strAbsoluteFileName = null;
        strAbsoluteFileName = strFileLocation.endsWith(System.getProperty("file.separator")) ? strFileLocation + strFileName : strFileLocation + System.getProperty("file.separator") + strFileName;
        if (strFileType != null && strFileType != "") {
            strAbsoluteFileName = strFileType.startsWith(".") ? strAbsoluteFileName + strFileType : strAbsoluteFileName + "." + strFileType;
        }
        return strAbsoluteFileName;
    }

    public String appendValueEndOfStringIfMissing(String sString, String strValue) {
        if (!sString.endsWith(strValue)) {
            return sString + strValue;
        }
        return sString;
    }

    public String setAbsolutePath(String sLocation, String sApplicationName, String sTestSuiteType, String sTestSuiteLibraryName, String sTestSuiteFileType) {
        if (!sLocation.endsWith(System.getProperty("file.separator"))) {
            sLocation = sLocation + System.getProperty("file.separator");
        }
        if (!sTestSuiteFileType.startsWith(".")) {
            sTestSuiteFileType = sTestSuiteFileType + ".";
        }
        return sLocation + sApplicationName + System.getProperty("file.separator") + sTestSuiteType + System.getProperty("file.separator") + sTestSuiteLibraryName + sTestSuiteFileType;
    }

    public void setScreenDimensions(RemoteWebDriver rwd) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        int width = gs[2].getDisplayMode().getWidth();
        int height = gs[2].getDisplayMode().getHeight();
        rwd.manage().window().setSize(new Dimension(width, height));
        rwd.manage().window().setPosition(new Point(0, 0));
    }
}

