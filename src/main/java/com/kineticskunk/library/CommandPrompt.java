/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import com.kineticskunk.library.LinuxInteractor;
import java.io.IOException;

public class CommandPrompt {
    Process p;
    ProcessBuilder builder;
    LinuxInteractor objLI;

    public String runCommand(String command) throws InterruptedException, IOException {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            this.builder = new ProcessBuilder("cmd.exe", "/c", command);
            this.builder.redirectErrorStream(true);
            Thread.sleep(1000);
            this.p = this.builder.start();
        } else {
            this.objLI = new LinuxInteractor();
        }
        return this.objLI.executeRuntimeCommand(command, true);
    }

    public static void main(String[] args) throws Exception {
        CommandPrompt cmd = new CommandPrompt();
        cmd.runCommand("dir");
    }
}

