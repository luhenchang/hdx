package com.accuvally.hdtui.model;

import java.io.Serializable;

public class DetailExtern implements Serializable {

	private String Title;

	private String Content;

	private String Summary;

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
