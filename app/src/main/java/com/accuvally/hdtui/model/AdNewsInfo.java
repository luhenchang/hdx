package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class AdNewsInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Project> AdNews;
	private String Logo;

	public List<Project> getAdNews() {
		return AdNews;
	}
	public void setAdNews(List<Project> adNews) {
		AdNews = adNews;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}



}
