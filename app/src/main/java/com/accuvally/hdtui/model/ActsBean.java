package com.accuvally.hdtui.model;

import java.io.Serializable;

import com.accuvally.hdtui.utils.TimeUtils;

public class ActsBean implements Serializable{

	public String Id;

	public int Max;

	public boolean IsQing;// 是否是轻活动

	public String Title;// title

	public String Address;// 地址


	public int LikeNum;// 喜欢
	public int VisitNum;// 浏览次数

	public String Url;

	public boolean IsFollow;

	public int SourceType;

	public String ShareUrl;
	
	public String PriceArea;
	
	
	/**
	 * --  活动状态，共三种：
	   --  过期          
	   --  额满                                                                                                    
	   --  热销
	 */
	public String statusstr;
	/** 图片地址 **/
	public String logo;
	/** 活动开始UTC时间 **/
	public String startutc;
	/** 活动结束UTC时间 **/
	public String endutc;
	
	/** 活动开始本地时间 **/
	public String getStartutc() {
		return TimeUtils.utcToLocal(startutc);
	}
	
	public String getTimeStr() {
		return TimeUtils.getTimeAreaStr(1, startutc, endutc);
	}
	

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public int getMax() {
		return Max;
	}

	public void setMax(int max) {
		Max = max;
	}

	public boolean isIsQing() {
		return IsQing;
	}

	public void setIsQing(boolean isQing) {
		IsQing = isQing;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getLikeNum() {
		return LikeNum;
	}

	public void setLikeNum(int likeNum) {
		LikeNum = likeNum;
	}

	public int getVisitNum() {
		return VisitNum;
	}

	public void setVisitNum(int visitNum) {
		VisitNum = visitNum;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public boolean isIsFollow() {
		return IsFollow;
	}

	public void setIsFollow(boolean isFollow) {
		IsFollow = isFollow;
	}

	public int getSourceType() {
		return SourceType;
	}

	public void setSourceType(int sourceType) {
		SourceType = sourceType;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public String getPriceArea() {
		return PriceArea;
	}

	public void setPriceArea(String priceArea) {
		PriceArea = priceArea;
	}
}
