package com.accuvally.hdtui.model;

public class SquareLimitedTimeOffers {
	private String VoteTime;
	private String Id;
	private String LogoUrl;
	private int Status;
	private String Title;
	private String Url;
	private String ShareUrl;

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getVoteTime() {
		return VoteTime;
	}

	public void setVoteTime(String voteTime) {
		VoteTime = voteTime;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getLogoUrl() {
		return LogoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		LogoUrl = logoUrl;
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
