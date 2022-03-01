package com.example.APIConnector;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class APIConnector {
	private String stringInput;
	private ArrayList<String> strings;
	private URL logicalCharsURL;
	private HttpURLConnection connection;

	public APIConnector(String stringInput) {
		this.stringInput = stringInput;
	}

	public APIConnector(ArrayList<String> strings) {
		this.strings = strings;
	}

	public void getLogicalChars() {
		try {
			String string = stringInput.replace(" ", "%20");
			string = string.replace("’", "'");
			logicalCharsURL = new URL("https://indic-wp.thisisjava.com/api/getLogicalChars.php?string=" + string
					+ "&language=English");
			connection = (HttpURLConnection) logicalCharsURL.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getLogicalCharacters(ArrayList<String> strings) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			for (int i = 0; i < strings.size(); i++) {
				logicalCharsURL = new URL("https://indic-wp.thisisjava.com/api/getLogicalChars.php?string="
						+ strings.get(i) + "&language=English");
				connection = (HttpURLConnection) logicalCharsURL.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strings;
	}

	public void disconnect(HttpURLConnection connection) {
		connection.disconnect();
	}

	public String getStringInput() {
		return stringInput;
	}

	public void setStringInput(String stringInput) {
		this.stringInput = stringInput;
	}

	public URL getLogicalCharsURL() {
		return logicalCharsURL;
	}

	public void setLogicalCharsURL(URL logicalCharsURL) {
		this.logicalCharsURL = logicalCharsURL;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public void setConn(HttpURLConnection conn) {
		this.connection = conn;
	}

	public ArrayList<String> getStrings() {
		return strings;
	}

	public void setStrings(ArrayList<String> strings) {
		this.strings = strings;
	}
}
