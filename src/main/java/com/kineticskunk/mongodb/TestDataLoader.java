package com.kineticskunk.mongodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.kineticskunk.utilities.ConfigurationLoader;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class TestDataLoader extends ConfigurationLoader {
	
	private final Logger logger = LogManager.getLogger(TestDataLoader.class.getName());
	private final Marker TESTDATALOADER = MarkerManager.getMarker("TESTDATALOADER");
	
	private JSONParser parser = new JSONParser();
	private JSONObject jsonObject = null;
	
	public TestDataLoader(String jsonFile) {
		super(jsonFile);
	}
	
	
	
}
