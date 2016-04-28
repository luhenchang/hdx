package com.accuvally.hdtui.model;

import java.io.Serializable;

public class SponsorBean implements Serializable{

	private String id;

	private String Logo;

	private String Name;

	private String Desc;

	private int Comments;

	private int Follows;// 关注的数量

	private int EventNum;// 主办方已主办的活动数量

	private String ActTitle;// 最近发布的活动标题

	private String ActUrl;// 活动详情链接

	private boolean HasFollowed;
	private String ActID;

	public String getActID() {
		return ActID;
	}

	public void setActID(String actID) {
		ActID = actID;
	}

	public boolean isHasFollowed() {
		return HasFollowed;
	}

	public void setHasFollowed(boolean hasFollowed) {
		HasFollowed = hasFollowed;
	}

	public String getActTitle() {
		return ActTitle;
	}

	public void setActTitle(String actTitle) {
		ActTitle = actTitle;
	}

	public String getActUrl() {
		return ActUrl;
	}

	public void setActUrl(String actUrl) {
		ActUrl = actUrl;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String description) {
		Desc = description;
	}

	public int getComments() {
		return Comments;
	}

	public void setComments(int comments) {
		Comments = comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getFollows() {
		return Follows;
	}

	public void setFollows(int follows) {
		Follows = follows;
	}

	public int getEventNum() {
		return EventNum;
	}

	public void setEventNum(int eventNum) {
		EventNum = eventNum;
	}
}
