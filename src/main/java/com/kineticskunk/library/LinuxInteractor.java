/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LinuxInteractor {
    private static Logger logger;
    private String strShellScriptLocation;
    private String strCommand;
    private String strResponse;
    public boolean blnWaitForResponce;

    public LinuxInteractor() {
        logger = LogManager.getLogger(LinuxInteractor.class.getName());
        this.strShellScriptLocation = null;
        this.strCommand = null;
        this.strResponse = null;
        this.blnWaitForResponce = false;
    }

    public void tearDown() {
        this.strShellScriptLocation = null;
        this.strCommand = null;
        this.strResponse = null;
        this.blnWaitForResponce = false;
    }

    public void setShellScriptLocation(String strShellScriptLocation) {
        this.strShellScriptLocation = !strShellScriptLocation.endsWith(System.getProperty("file.separator")) ? strShellScriptLocation + System.getProperty("file.separator") : strShellScriptLocation;
    }

    public String getShellScriptLocation() {
        return this.strShellScriptLocation;
    }

    public void setCommand(String strCommand) {
        this.strCommand = strCommand;
    }

    public String getCommand() {
        return this.strCommand;
    }

    public void setResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    public String getResponse() {
        return this.strResponse;
    }

    public void setWaitForResponse(boolean blnWaitForResponce) {
        this.blnWaitForResponce = blnWaitForResponce;
    }

    public boolean getWaitForResponse() {
        return this.blnWaitForResponce;
    }

    public void executeProcessBuilderCommand() {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "cd / ; cd " + this.getShellScriptLocation() + " ; " + this.getCommand());
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
            if (this.getWaitForResponse()) {
                InputStream shellIn = shell.getInputStream();
                shell.waitFor();
                this.setResponse(this.convertInputStreamToStr(shellIn));
                shellIn.close();
            }
        }
        catch (IOException e) {
            logger.error("Error occured while executing command \"" + this.getCommand() + '\"' + " error description: " + e.getMessage());
        }
        catch (InterruptedException e) {
            logger.error("Error occured while executing command \"" + this.getCommand() + '\"' + " error description: " + e.getMessage());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String convertInputStreamToStr(InputStream is) throws IOException {
        if (is != null) {
            StringWriter writer;
            writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                int n;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            finally {
                is.close();
            }
            return writer.toString().trim();
        }
        return "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String newConvertInputStreamToStr(InputStream is) throws IOException {
        StringBuffer output = new StringBuffer();
        if (is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTG-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
            }
            finally {
                is.close();
            }
            return output.toString().trim();
        }
        return "";
    }

    public String executeRuntimeCommand() {
        StringBuffer output = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", this.getCommand()});
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public String executeRuntimeCommand(String strCommand) throws IOException {
        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", strCommand});
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        String allLine = "";
        int i = 1;
        while ((line = r.readLine()) != null) {
            System.out.println("" + i + ". " + line);
            allLine = allLine + "" + line + "\n";
            if (line.contains("Console LogLevel: debug")) break;
            ++i;
        }
        return allLine;
    }

    public String executeRuntimeCommand(String strCommand, boolean blnWaitForResponse) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "cd / | cd  | " + strCommand);
        pb.redirectErrorStream(true);
        try {
            Process shell = pb.start();
            if (blnWaitForResponse) {
                InputStream shellIn = shell.getInputStream();
                int shellExitStatus = shell.waitFor();
                String strOutput = this.convertInputStreamToStr(shellIn);
                logger.info("Command: \"" + strCommand + " exited with a status of " + '\"' + shellExitStatus + '\"' + " and a responce of " + strOutput);
                shellIn.close();
                return strOutput;
            }
            return "not_waiting_for_response";
        }
        catch (IOException e) {
            logger.error("Error occured while executing command \"" + this.getCommand() + '\"' + " error description: " + e.getMessage());
        }
        catch (InterruptedException e) {
            logger.error("Error occured while executing command \"" + this.getCommand() + '\"' + " error description: " + e.getMessage());
        }
        return null;
    }

    public String runCommandline(String[] args) throws IOException {
        String line;
        Process process = new ProcessBuilder(args).start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer output = new StringBuffer();
        while ((line = br.readLine()) != null) {
            output.append(line);
        }
        return output.toString();
    }
}

