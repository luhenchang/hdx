package com.accuvally.hdtui.model;

public class NotTicket {
	private String Id;
	private String OrgID;
	private String OrgName;
	private String Start;
	private String Title;
	private String Url;
	private String ShareUrl;
	private String Logo;

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getOrgID() {
		return OrgID;
	}

	public void setOrgID(String orgID) {
		OrgID = orgID;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getStart() {
		return Start;
	}

	public void setStart(String start) {
		Start = start;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
}
