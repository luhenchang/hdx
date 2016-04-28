package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class FromInfo implements Serializable {

	private String max;

	private String Version;

	public List<ItemInfo> items;

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

}
