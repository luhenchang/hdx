package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class ItemInfo implements Serializable {

	private int Sort;

	private String Type;

	private String Title;

	private boolean Required;

	public boolean isRequired() {
		return Required;
	}

	public void setRequired(boolean required) {
		Required = required;
	}

	private List<String> SubItems;

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public List<String> getSubItems() {
		return SubItems;
	}

	public void setSubItems(List<String> subItems) {
		SubItems = subItems;
	}

}
