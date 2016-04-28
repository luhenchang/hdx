package com.accuvally.hdtui.model;

import java.io.Serializable;

public class SponsorDetailOrg implements Serializable {

	public String Id;
	public String Description;
	public int Comments;
	public String Name;
	public String Logo;

	public boolean isfollow;
	public int EventNum;
	public int Follows;
	public String Desc;
	public String ShareUrl;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getComments() {
		return Comments;
	}

	public void setComments(int comments) {
		Comments = comments;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public int getEventNum() {
		return EventNum;
	}

	public void setEventNum(int eventNum) {
		EventNum = eventNum;
	}

	public int getFollows() {
		return Follows;
	}

	public void setFollows(int follows) {
		Follows = follows;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}
}
