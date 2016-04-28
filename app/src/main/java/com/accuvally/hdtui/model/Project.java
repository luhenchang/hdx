package com.accuvally.hdtui.model;

public class Project {
	private String Url;
	private String Logo;
	private String Title;
	private String Id;
	private String ShareUrl;
	private boolean OpenInWeb;

	public boolean isOpenInWeb() {
		return OpenInWeb;
	}

	public void setOpenInWeb(boolean openInWeb) {
		OpenInWeb = openInWeb;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

}
