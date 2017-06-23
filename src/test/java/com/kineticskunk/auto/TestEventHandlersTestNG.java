package com.kineticskunk.auto;

import java.io.IOException;

import org.testng.annotations.Parameters;

public class TestEventHandlersTestNG extends TestBaseSetup {
	
	@Parameters({ "configFileLocation", "resourceFileLocation" })
	public TestEventHandlersTestNG(String configFileLocation, String resourceFileLocation) throws IOException {
		super(configFileLocation, resourceFileLocation);
		// TODO Auto-generated constructor stub
	}

}
