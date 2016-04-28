package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class SponsorPeopleBean implements Serializable {

	private String Logo;
	private String Nick;// 昵称
	private String Brief;// 个签
	public boolean HadFollowed;// 是否已关注

	private List<SelInfo> Published;// 已发布的活动
	private List<SelInfo> Finished;// 已结束的活动

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public String getBrief() {
		return Brief;
	}

	public void setBrief(String brief) {
		Brief = brief;
	}

	public boolean isHadFollowed() {
		return HadFollowed;
	}

	public void setHadFollowed(boolean hadFollowed) {
		HadFollowed = hadFollowed;
	}

	public List<SelInfo> getPublished() {
		return Published;
	}

	public void setPublished(List<SelInfo> published) {
		Published = published;
	}

	public List<SelInfo> getFinished() {
		return Finished;
	}

	public void setFinished(List<SelInfo> finished) {
		Finished = finished;
	}

}
