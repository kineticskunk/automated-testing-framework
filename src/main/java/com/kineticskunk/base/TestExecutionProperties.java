package com.kineticskunk.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONObject;

import com.kineticskunk.utilities.ConfigurationLoader;

public class TestExecutionProperties extends ConfigurationLoader {

	private final Logger logger = LogManager.getLogger(TestExecutionProperties.class.getName());
	private final Marker TESTEXECUTIONPROPERTIES = MarkerManager.getMarker("DRIVEREXECTABLE");
	
	private static final String CONFIGURATIONLOADINGSETTINGS = "configurationloadingsettings";
	private static final String WEBDRIVERSEXECUTABLES = "webdriverexecutables";
	private static final String WEBDRIVERCONFIG = "webdriverconfig";
	private static final String BROWSERSETTINGS = "browsersettings";
	private static final String REMOTEWEBDRIVERSETTINGS = "remotewebdriversettings";
	
	private String textExecutionProperties;
	
	private JSONObject configurationLoadingSettings;
	private JSONObject webDriverExecutables;
	private JSONObject webDriverSettings;
	private JSONObject browserSettings;
	private JSONObject remoteWebDriverSettings;
	
	private static TestExecutionProperties tep;

	/**
	 * Singleton Constructor
	 */
	public static TestExecutionProperties getInstance(String textExecutionProperties) {
		if (tep == null ) {
			synchronized (TestExecutionProperties.class) {
				if (tep == null) {
					tep = new TestExecutionProperties(textExecutionProperties);
				}
			}
		}
		return tep;
	}

	public TestExecutionProperties(String textExecutionProperties) {
		this.textExecutionProperties = textExecutionProperties;
		this.loadConfigurationFile(textExecutionProperties);
		this.configurationLoadingSettings = this.loadConfigElement(CONFIGURATIONLOADINGSETTINGS);
		this.webDriverExecutables = this.loadConfigElement(WEBDRIVERSEXECUTABLES);
		this.webDriverSettings = this.loadConfigElement(WEBDRIVERCONFIG);
		this.browserSettings = this.loadConfigElement(BROWSERSETTINGS);
		this.remoteWebDriverSettings = this.loadConfigElement(REMOTEWEBDRIVERSETTINGS);
	}
	
	private JSONObject loadConfigElement(String configElement) {
		try {
			JSONObject json = (JSONObject) this.getConfiguration().get(configElement);
			this.logger.debug(TESTEXECUTIONPROPERTIES, "Loading configuration " + (char) 34 + configElement + (char)34 + " with data " + (char)34 + json.toJSONString() + (char)34);
			return json;
		} catch (Exception e) {
			this.logger.error(TESTEXECUTIONPROPERTIES, "Failed to configuration " + (char) 34 + configElement + (char)34 + " from " + (char)34 + this.textExecutionProperties + (char)34);
			return null;
		}
 	}
	
	public JSONObject getConfigurationLoadingSettings() {
		return this.configurationLoadingSettings;
	}
	
	public JSONObject getWebDriverExecutables() {
		return this.webDriverExecutables;
	}
	
	public JSONObject getWebDriverConfig() {
		return this.webDriverSettings;
	}
	
	public JSONObject getBrowserSettings() {
		return this.browserSettings;
	}
	
	public JSONObject getRemoteWebDriverSettins() {
		return this.remoteWebDriverSettings;
	}

}
