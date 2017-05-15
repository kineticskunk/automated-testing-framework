/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import com.kineticskunk.library.LinuxInteractor;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeviceConfiguration {
    private static Logger logger;
    public final String strListOfDevices = "List of devices attached";

    public DeviceConfiguration() {
        logger = LogManager.getLogger(DeviceConfiguration.class.getName());
    }

    public String executeADBShellCommand(String strShellScriptLocation, String strShellScriptCommand, boolean blnWaitForResponce) {
        LinuxInteractor objLI = new LinuxInteractor();
        if (System.getenv().containsKey(strShellScriptLocation)) {
            objLI.setShellScriptLocation(System.getenv().get(strShellScriptLocation));
        } else {
            objLI.setShellScriptLocation(strShellScriptLocation);
        }
        objLI.setCommand(strShellScriptCommand);
        objLI.setWaitForResponse(blnWaitForResponce);
        objLI.executeProcessBuilderCommand();
        String strOutput = objLI.getResponse();
        logger.info("executeADBShellCommand executed command " + strShellScriptCommand + " and the output is " + strOutput);
        objLI.tearDown();
        return strOutput;
    }

    public String[] getAndroidDeviceListArray(String strDeviceListString) {
        strDeviceListString = strDeviceListString.substring(strDeviceListString.indexOf("List of devices attached") + "List of devices attached".length(), strDeviceListString.length()).trim();
        strDeviceListString = strDeviceListString.replace(" device", "").trim();
        strDeviceListString = strDeviceListString.replace(" unauthorized", "").trim();
        strDeviceListString = strDeviceListString.replace(" offline", "").trim();
        return strDeviceListString.split(" ");
    }

    public Map<String, String> getDivces() throws Exception {
        HashMap<String, String> devices = new HashMap<String, String>();
        String strOutput = this.executeADBShellCommand("shell-script-lib", "sh adb-server-start-kill.sh start", true);
        if (strOutput.length() == 0) {
            logger.info("adb service already started");
        } else if (strOutput.contains("* daemon started successfully *")) {
            logger.info("adb service started");
        } else if (strOutput.contains("internal or external command")) {
            logger.error("adb path not set in system varibale");
            System.exit(0);
        }
        String[] strDeviceListArray = this.getAndroidDeviceListArray(this.executeADBShellCommand("shell-script-lib", "sh get-android-device-list.sh", true));
        if (strDeviceListArray.length > 0) {
            LinuxInteractor objLI = new LinuxInteractor();
            for (int intDeviceCounter = 0; intDeviceCounter <= strDeviceListArray.length - 1; ++intDeviceCounter) {
                String strDeviceStatus = objLI.runCommandline(new String[]{"bash", "-c", "/usr/local/bin/android-sdk-macosx/platform-tools/adb get-state -s " + strDeviceListArray[intDeviceCounter]});
                if (strDeviceStatus.equalsIgnoreCase("device")) {
                    String strDeviceModel = objLI.runCommandline(new String[]{"bash", "-c", "/usr/local/bin/android-sdk-macosx/platform-tools/adb -s " + strDeviceListArray[intDeviceCounter] + " shell getprop ro.product.model"});
                    String strDeviceBrand = objLI.runCommandline(new String[]{"bash", "-c", "/usr/local/bin/android-sdk-macosx/platform-tools/adb -s " + strDeviceListArray[intDeviceCounter] + " shell getprop ro.product.brand"});
                    String strDeviceOSVersion = objLI.runCommandline(new String[]{"bash", "-c", "/usr/local/bin/android-sdk-macosx/platform-tools/adb -s " + strDeviceListArray[intDeviceCounter] + " shell getprop ro.build.version.release"});
                    logger.info("DeviceStatus = " + strDeviceStatus);
                    logger.info("DeviceModel = " + strDeviceModel);
                    logger.info("DeviceBrand = " + strDeviceBrand);
                    logger.info("DeviceOSVersion = " + strDeviceOSVersion);
                    devices.put("deviceID" + intDeviceCounter, strDeviceListArray[intDeviceCounter]);
                    devices.put("deviceName" + intDeviceCounter, strDeviceModel + " " + strDeviceBrand);
                    devices.put("osVersion" + intDeviceCounter, strDeviceOSVersion);
                    continue;
                }
                logger.info("Status of device " + strDeviceListArray[intDeviceCounter] + " is " + strDeviceStatus + ".");
            }
        } else {
            logger.info("No devices have been detected.");
        }
        return devices;
    }
}

