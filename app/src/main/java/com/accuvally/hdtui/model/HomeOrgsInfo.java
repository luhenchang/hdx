package com.accuvally.hdtui.model;

/**
 * 首页主办方
 * 
 * @author Semmer Wang
 * 
 */
public class HomeOrgsInfo {

	private String id;

	private String Logo;

	private String Name;

	private String Url;

	private int Follows;// 关注的数量

	private int EventNum;// 主办方已主办的活动数量

	private boolean HasFollowed;// 是否关注

	public boolean isHasFollowed() {
		return HasFollowed;
	}

	public void setHasFollowed(boolean hasFollowed) {
		HasFollowed = hasFollowed;
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

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
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
