package com.kineticskunk.events;

import java.util.HashMap;
import java.util.List;
import java.util.Spliterator;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kineticskunk.synchronization.WebDriverSynchronization;

public class WebTables {

	private final Logger logger = LogManager.getLogger(WebTables.class.getName());
	private final Marker WEBTABLES = MarkerManager.getMarker("WEBTABLES");

	private WebTableBuilder wtb = new WebTableBuilder();

	private String cellData;
	private JSONObject json;

	public WebTables () {
		this.cellData = null;
	}

	private WebTables (WebTableBuilder builder) {
		this();
		this.cellData = builder.cellData;
	}

	public String setCellData(WebDriver driver, WebElement table, String headerTag, String headersTag, String searchHeader, String bodyTag, String rowTag, String cellTag, String searchValue, String verificationHeader) {
		this.wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderPosition(searchHeader).setBody(bodyTag).setRows(rowTag).setCellData(cellTag, searchValue, verificationHeader).build();
		return this.cellData;
	}

	public void clickTableCellWebElement(WebDriver driver, WebElement table, String headerTag, String headersTag, String searchHeader, String bodyTag, String rowTag, String cellTag, String cellWebElementText) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderPosition(searchHeader).setBody(bodyTag).setRows(rowTag).clickCellWebElement(cellTag, cellWebElementText).build();
	}

	public void clickCellNestedWebElement(WebDriver driver, WebElement table, String headerTag, String headersTag, String searchHeader, String bodyTag, String rowTag, String cellTag, String searchValue, String nestWebElementTag, String nestedWebElementText) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderPosition(searchHeader).setBody(bodyTag).setRows(rowTag).clickCellNestedWebElement(cellTag, searchValue, nestWebElementTag, nestedWebElementText).build();
	}

	public JSONObject writeTabelToJSONObject(WebDriver driver, WebElement table, String headerTag, String headersTag, String bodyTag, String rowTag, String cellTag, String tableName) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setBody(bodyTag).setRows(rowTag).writeTabelToJSONObject(cellTag, tableName).build();
		return this.json;
	}

	public JSONObject writeTabelToJSONObject(WebDriver driver, WebElement table) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).writeTableContentsToJSONObject().build();
		return this.json;
	}

	private class WebTableBuilder {

		private final Logger logger = LogManager.getLogger(WebTableBuilder.class.getName());
		private final Marker WEBTABLEBUILDER = MarkerManager.getMarker("WEBTABLEBUILDER");

		private KineticSkunkActions ksa;
		private WebElement table;
		private WebElement header;
		private List<WebElement> headers;
		private WebElement body;
		private List<WebElement> rows;

		private String cellData;
		private int headerPosition;

		public WebTableBuilder() {
			this.cellData = null;
			this.headerPosition = 0;
		}

		private WebTableBuilder setKineticSkunkActions(WebDriver driver) {
			this.ksa = KineticSkunkActions.getInstance(driver);
			return this;
		}

		public WebTableBuilder setTable(WebElement table) {
			try {
				this.table = table;
				return this;
			} catch (NoSuchElementException e) {
				this.logger.fatal(WEBTABLEBUILDER, "Method setTable through an exception");
				throw new NoSuchElementException(String.format("Table defined by (%s) does not exist", this.table.toString()));
			}
		}

		public WebTableBuilder setHeader(String headerTag) {
			try {
				this.header = this.table.findElement(By.tagName(headerTag));
				return this;
			} catch (NoSuchElementException e) {
				this.logger.fatal(WEBTABLEBUILDER, "Method setHeader through an exception");
				throw new NoSuchElementException(String.format("Table defined by (%s) does not exist", this.header.toString()));
			}
		}

		public WebTableBuilder setHeaders(String headersTag) {
			this.headers = this.header.findElements(By.tagName(headersTag));
			System.out.println("Number of header = " + this.headers.size());
			return this;
		}

		public WebTableBuilder setBody(String bodyTag) {
			this.body = this.table.findElement(By.tagName(bodyTag));
			System.out.println("Body text = " + this.body.getText());
			return this;
		}

		public WebTableBuilder setRows(String rowTag) {
			this.rows = this.body.findElements(By.tagName(rowTag));
			System.out.println("Number of rows = " + this.rows.size());
			return this;
		}

		private WebTableBuilder setHeaderPosition(String headerName) {
			for (WebElement header : this.headers) {
				System.out.println("Header text = " + header.getText());
				if (header.getText().equalsIgnoreCase(headerName)) break;
				++this.headerPosition;
			}
			System.out.println(String.format("Index of header (%s) = (%s) ", headerName, this.headerPosition));
			return this;
		}

		public WebTableBuilder setCellData(String cellTag, String searchValue, String verificationHeader) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				if (!cells.get(headerPosition).getText().equalsIgnoreCase(searchValue)) continue;
				WebElement cell = cells.get(headerPosition);
				this.ksa.moveToWebElement(cell);
				this.cellData = cells.get(this.headerPosition).getText();
			}
			return this;
		}

		public WebTableBuilder clickCellWebElement(String cellTag, String searchValue) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				System.out.println(cells.get(headerPosition).getText());
				if (!cells.get(headerPosition).getText().equalsIgnoreCase(searchValue)) continue;
				WebElement cell = cells.get(headerPosition);
				this.ksa.performActionDoubleClick(cell);
				return this;
			}
			return this;
		}

		private WebTableBuilder clickCellNestedWebElement(String cellTag, String searchValue, String nestWebElementTag, String nestedWebElementText) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				if (!cells.get(headerPosition).getText().equalsIgnoreCase(searchValue)) continue;
				WebElement cell = cells.get(this.headerPosition);
				List<WebElement> nestedElement = cell.findElements(By.tagName(nestWebElementTag));
				for (WebElement nel : nestedElement) {
					if (!nel.getText().equalsIgnoreCase(nestedWebElementText)) continue;
					nel.click();
					return this;
				}
			}
			return this;
		}

		@SuppressWarnings("unchecked")
		private WebTableBuilder writeTabelToJSONObject(final String cellTag, final String tableName) {


			JSONObject json = new JSONObject();
			



			



			for (int rowCounter = 0; (rowCounter <= (this.rows.size() - 1)); rowCounter++) {
				JSONArray jsonArr = new JSONArray();
				JSONObject jo = new JSONObject();
				List<WebElement> cells = this.rows.get(rowCounter).findElements(By.tagName(cellTag));

				//for (int headerCounter = 1; (headerCounter <= (this.headers.size() - 1)); headerCounter++) {

					

					for (int cellCounter = 1; (cellCounter <= (cells.size() - 1)); cellCounter++) {
						//System.out.println(this.headers.get(cellCounter).getText() + " = " + cells.get(cellCounter).getText());
						jo.put(this.headers.get(cellCounter).getText(), cells.get(cellCounter).getText());

					}
					
					jsonArr.add(jo);
				//}
				System.out.println((this.rows.get(rowCounter).findElements(By.tagName("td"))).get(0).getText());
				
				json.put((this.rows.get(rowCounter).findElements(By.tagName("td"))).get(0).getText(), jo);

				
			}
			
			JSONObject jsonX = new JSONObject();
			
			jsonX.put("Inboxes", json);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonX.toJSONString());
			String prettyJsonString = gson.toJson(je);


			System.out.println(prettyJsonString);

			
			return this;
		}

		@SuppressWarnings("unchecked")
		private WebTableBuilder writeTableContentsToJSONObject() {

			final String HTML = this.table.getAttribute("outerHTML");

			Document document = Jsoup.parse(HTML);
			Element table = document.getElementsByTag(this.table.getTagName()).select("table").first();
			Elements headers = table.getElementsByTag("th");

			Elements rows = table.getElementsByTag("tr");


			String arrayName = table.select("th").first().text();

			//for (int i = 1, l = rows.size(); i < l; i++) {

			JSONObject jsonObj = new JSONObject();

			List<String> header = headers.eachText();
			for (String h : header) {
				JSONArray jsonArr = new JSONArray();
				jsonObj.put(h, jsonArr);



			}
			System.out.println(jsonObj.toString());
			// String key = headers.get(i).text();

			//String value = rows.get(i).text();
			//jo.put(key, value);
			//}



			//	String arrayName = table.select("thead").first().text();

			//
			//	


			//Elements rows = table.getElementsByTag("tr");


			//	JSONObject jo = new JSONObject();


			//jsonArr.add(jo);
			//jsonObj.put(arrayName, jsonArr);
			//System.out.println(jsonObj.toString());
			return this;
		}



		public WebTables build() {
			return new WebTables(this);
		}

	}

}
