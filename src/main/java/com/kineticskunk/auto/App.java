package com.kineticskunk.auto;

import com.kineticskunk.library.ApplicationProperties;
import java.io.IOException;

public class App {
    protected static ApplicationProperties ap;

    public static void main(String[] args) throws IOException {
        ap = ApplicationProperties.getInstance();
        ap.loadPropertiesFile(args[0], args[1]);
    }
}