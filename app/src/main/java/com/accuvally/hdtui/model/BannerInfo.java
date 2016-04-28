package com.accuvally.hdtui.model;

public class BannerInfo {

	private String Logo;

	private String Url;

	private String Title;

	private String ShareUrl;

	private boolean OpenInWeb;

	private String Id;

	private boolean ResultForSearch;

	private String Params;

	private String Content;
	
	private String TitleColor;

	public String getParams() {
		return Params;
	}

	public void setParams(String params) {
		Params = params;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public boolean isOpenInWeb() {
		return OpenInWeb;
	}

	public void setOpenInWeb(boolean openInWeb) {
		OpenInWeb = openInWeb;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public boolean isResultForSearch() {
		return ResultForSearch;
	}

	public void setResultForSearch(boolean resultForSearch) {
		ResultForSearch = resultForSearch;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getTitleColor() {
		return TitleColor;
	}

	public void setTitleColor(String titleColor) {
		TitleColor = titleColor;
	}

}
