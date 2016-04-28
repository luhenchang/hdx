package com.accuvally.hdtui.model;

import java.io.Serializable;

public class HomeEventInfo implements Serializable {

	private String Id;

	private String Title;// title

	private String Address;// 地址

	private String Start;// 开始时间

	private String End;// 结束时间

	private String LogoUrl;// 图片地址

	private int LikeNum;// 喜欢

	private String Url;

	private boolean IsFollow;

	private int Status;

	private boolean IsQing;// 是否是轻活动

	private String Source;

	private int SourceType;

	private String City;

	private String ShareUrl;

	private String StatusStr;

	public String getStatusStr() {
		return StatusStr;
	}

	public void setStatusStr(String statusStr) {
		StatusStr = statusStr;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public int getSourceType() {
		return SourceType;
	}

	public void setSourceType(int sourceType) {
		SourceType = sourceType;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public boolean isIsQing() {
		return IsQing;
	}

	public void setIsQing(boolean isQing) {
		IsQing = isQing;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public boolean isIsFollow() {
		return IsFollow;
	}

	public void setIsFollow(boolean isFollow) {
		IsFollow = isFollow;
	}

	public int getLikeNum() {
		return LikeNum;
	}

	public void setLikeNum(int likeNum) {
		LikeNum = likeNum;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getEnd() {
		return End;
	}

	public void setEnd(String end) {
		End = end;
	}

	public String getLogoUrl() {
		return LogoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		LogoUrl = logoUrl;
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
