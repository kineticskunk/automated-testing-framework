/*
 * Decompiled with CFR 0_115.
 */
package com.kineticskunk.library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestServiceLogging {
    private Logger logger = LogManager.getLogger(TestServiceLogging.class.getName());

    public String retrieveMessage(String loggingMessage) {
        this.logger.entry();
        return this.logger.exit(loggingMessage);
    }

    public void getException(Exception ex) {
        this.logger.entry();
        try {
            this.retrieveMessage(ex.getMessage());
            this.logger.catching(ex);
        }
        catch (Exception tslex) {
            this.logger.catching(tslex);
        }
        this.logger.exit();
    }
}

