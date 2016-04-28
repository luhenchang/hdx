package com.accuvally.hdtui.model;

/**
 * 表情信息
 * 
 * @author fuxianwei
 */
public class SmileInfo {

	// 名称
	private String value = "";

	// 资源
	private int resource;

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SmileInfo() {
		super();
	}

	public SmileInfo(String value, int resource) {
		super();
		this.value = value;
		this.resource = resource;
	}

}
