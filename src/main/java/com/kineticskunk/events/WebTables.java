package com.kineticskunk.events;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WebTables {

	private WebTableBuilder wtb = new WebTableBuilder();

	private String cellData;
	private JSONObject json;

	public WebTables () {
		this.cellData = null;
	}

	private WebTables (WebTableBuilder builder) {
		this();
		this.cellData = builder.cellData;
		this.json = builder.json;
	}

	public String getCellData(WebDriver driver, WebElement table, String headerTag, String headersTag, String referenceHeader, String bodyTag, String rowTag, String cellTag, String referenceValue, String searchHeader) {
		this.wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderIndex(referenceHeader).setBody(bodyTag).setRows(rowTag).setCellData(cellTag, referenceValue, searchHeader).build();
		return this.cellData;
	}

	public void clickTableCellWebElement(WebDriver driver, WebElement table, String headerTag, String headersTag, String referenceHeader, String bodyTag, String rowTag, String cellTag, String referenceValue) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderIndex(referenceHeader).setBody(bodyTag).setRows(rowTag).clickCellWebElement(cellTag, referenceValue).build();
	}

	public void clickCellNestedWebElement(WebDriver driver, WebElement table, String headerTag, String headersTag, String referenceHeader, String bodyTag, String rowTag, String cellTag, String searchValue, String nestWebElementTag, String referenceValue) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setHeaderIndex(referenceHeader).setBody(bodyTag).setRows(rowTag).clickCellNestedWebElement(cellTag, searchValue, nestWebElementTag, referenceValue).build();
	}

	public JSONObject writeTabelToJSONObject(WebDriver driver, WebElement table, String headerTag, String headersTag, String bodyTag, String rowTag, String cellTag, String tableName, String headerArrayName) {
		WebTableBuilder wtb = new WebTableBuilder();
		wtb.setKineticSkunkActions(driver).setTable(table).setHeader(headerTag).setHeaders(headersTag).setBody(bodyTag).setRows(rowTag).writeTabelToJSONObject(cellTag, tableName, headerArrayName).build();
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
		
		private JSONObject json;
		
		private String cellData;
		private int headerIndex;
		private String referenceColumnName;
		
		public WebTableBuilder() {
			this.json = new JSONObject();
			this.cellData = null;
			this.headerIndex = 0;
			this.referenceColumnName = null;
		}

		private WebTableBuilder setKineticSkunkActions(WebDriver driver) {
			this.ksa = KineticSkunkActions.getInstance(driver);
			return this;
		}

		public WebTableBuilder setTable(WebElement table) {
			try {
				this.table = table;
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException(String.format("Table defined by (%s) does not exist", this.table.toString()));
			}
			return this;
		}

		public WebTableBuilder setHeader(String headerTag) {
			try {
				this.header = this.table.findElement(By.tagName(headerTag));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException(String.format("Header defined by (%s) does not exist", this.header.toString()));
			}
			return this;
		}

		public WebTableBuilder setHeaders(String headersTag) {
			try {
				this.headers = this.header.findElements(By.tagName(headersTag));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException(String.format("Headers defined by (%s) does not exist", this.headers.toString()));
			}
			return this;
		}

		public WebTableBuilder setBody(String bodyTag) {
			try {
				this.body = this.table.findElement(By.tagName(bodyTag));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException(String.format("Headers defined by (%s) does not exist", this.body.toString()));
			}
			return this;
		}

		public WebTableBuilder setRows(String rowTag) {
			try {
				this.rows = this.body.findElements(By.tagName(rowTag));
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException(String.format("Rows defined by (%s) does not exist", this.rows.toString()));
			}
			return this;
		}

		private WebTableBuilder setHeaderIndex(String headerName) {
			this.referenceColumnName = headerName;
			this.headerIndex = this.getHeaderIndex(headerName);
			return this;
		}
		
		private int getHeaderIndex(String headerName) {
			int headerIndex = 0;
			boolean foundHeader = false;
			while (!foundHeader && (headerIndex <= this.headers.size() - 1)) {
				if (this.headers.get(headerIndex).getText().equalsIgnoreCase(headerName)) {
					foundHeader = true;
				} else {
					++headerIndex;
				}
			}
			this.logger.debug(WEBTABLEBUILDER, String.format("Index of header (%s) = (%s) ", headerName, headerIndex));
			return headerIndex;
		}

		public WebTableBuilder setCellData(String cellTag, String referenceValue, String searchHeader) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				if (!cells.get(this.headerIndex).getText().equalsIgnoreCase(referenceValue)) continue;
					this.cellData = this.ksa.getText(cells.get(this.getHeaderIndex(searchHeader)));
					this.logger.debug(WEBTABLEBUILDER, String.format("Cell data = (%s) where the search header = (%s) and the reference value = (%s) in reference column = (%s)", this.cellData, searchHeader, referenceValue, this.referenceColumnName));
					break;
			}
			return this;
		}

		public WebTableBuilder clickCellWebElement(String cellTag, String searchValue) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				System.out.println(cells.get(headerIndex).getText());
				if (!cells.get(headerIndex).getText().equalsIgnoreCase(searchValue)) continue;
				WebElement cell = cells.get(headerIndex);
				this.ksa.performActionDoubleClick(cell);
				return this;
			}
			return this;
		}

		private WebTableBuilder clickCellNestedWebElement(String cellTag, String searchValue, String nestWebElementTag, String nestedWebElementText) {
			for (WebElement row : this.rows) {
				List<WebElement> cells = row.findElements(By.tagName(cellTag));
				if (!cells.get(headerIndex).getText().equalsIgnoreCase(searchValue)) continue;
				WebElement cell = cells.get(this.headerIndex);
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
		private WebTableBuilder writeTabelToJSONObject(final String cellTag, final String tableName, final String headerArrayName) {
			JSONObject jsonX = new JSONObject();
			int arrayNameIndex = this.getHeaderIndex(headerArrayName);
			for (int rowCounter = 0; (rowCounter <= (this.rows.size() - 1)); rowCounter++) {
				JSONArray jsonArr = new JSONArray();
				JSONObject jo = new JSONObject();
				List<WebElement> cells = this.rows.get(rowCounter).findElements(By.tagName(cellTag));
				for (int cellCounter = 0; (cellCounter <= (cells.size() - 1)); cellCounter++) {
					if (cellCounter != arrayNameIndex) {
						jo.put(this.headers.get(cellCounter).getText(), cells.get(cellCounter).getText());
					}
				}
				jsonArr.add(jo);
				jsonX.put((this.rows.get(rowCounter).findElements(By.tagName(cellTag))).get(arrayNameIndex).getText(), jo);
			}
			this.json.put(tableName, jsonX);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(this.json.toJSONString());
			
			String prettyJsonString = gson.toJson(je);

			System.out.println(prettyJsonString);


			return this;
		}


		public WebTables build() {
			return new WebTables(this);
		}

	}

}
