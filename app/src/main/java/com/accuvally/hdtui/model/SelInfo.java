package com.accuvally.hdtui.model;

import java.io.Serializable;

import com.accuvally.hdtui.utils.TimeUtils;

//查询活动实体
public class SelInfo extends BaseResponse implements Serializable {
	public String Id;

	public String Title;

	public String ShareUrl;

	public boolean IsFollow;

	public int LikeNum;

	public String Address;

	public String startutc;

	public String endutc;

	public int SourceType;

	public String Url;

	public String logo;

	public String RemindStr;

	public String PriceArea;

	public String statusstr;

	public String Distance;

	public String VisitNum;// 访问数

	public String getPriceArea() {
		return PriceArea;
	}

	public void setPriceArea(String priceArea) {
		PriceArea = priceArea;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getSourceType() {
		return SourceType;
	}

	public void setSourceType(int sourceType) {
		SourceType = sourceType;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getRemindStr() {
		return RemindStr;
	}

	public void setRemindStr(String remindStr) {
		RemindStr = remindStr;
	}
	
	public String getStatusstr() {
		return statusstr;
	}

	public void setStatusstr(String statusstr) {
		this.statusstr = statusstr;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public String getVisitNum() {
		return VisitNum;
	}

	public void setVisitNum(String visitNum) {
		VisitNum = visitNum;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	
	/** 活动开始本地时间 **/
	public String getStartutc() {
		return TimeUtils.utcToLocal(startutc);
	}

	public String getEndutc() {
		return TimeUtils.utcToLocal(startutc);
	}
	

	public String TimeStr;// 新加字段 活动时间;
	public String getTimeStr() {
		return TimeUtils.getTimeAreaStr(1, startutc, endutc);
	}

}
