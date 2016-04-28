package com.accuvally.hdtui.model;

public class KeyValueInfo {
	private String Key;

	private String Value = "";
	
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	@Override
	public String toString() {
		StringBuilder json = new StringBuilder();
		json.append("{\"Key\":\"" + Key + "\",");
		json.append("\"Value\":\"" + Value + "\"}");
		return json.toString();
	}
}
