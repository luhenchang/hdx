package com.accuvally.hdtui.model;

import java.util.List;

public class ProjectDetails {
	private List<HomeEventInfo> Acts;
	private ProjectDetailsChild News;

	public List<HomeEventInfo> getActs() {
		return Acts;
	}

	public void setActs(List<HomeEventInfo> acts) {
		Acts = acts;
	}

	public ProjectDetailsChild getNews() {
		return News;
	}

	public void setNews(ProjectDetailsChild news) {
		News = news;
	}

}
