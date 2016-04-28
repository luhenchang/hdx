package com.accuvally.hdtui.model;

import java.io.Serializable;

public class RobTicketBean implements Serializable {

	private String id;
	private String Logo;
	private boolean IsRush;//是否是抢票活动
	private String VisitNum;
	private String Address;
	private String Title;
	private String TimeStr;
	private String Price;
	private String OriginPrice;
	private int Status;//抢票状态
	private String StatusStr;//抢票状态
	private boolean RushOut; // true表示抢光 ;

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

	public boolean isIsRush() {
		return IsRush;
	}

	public void setIsRush(boolean isRush) {
		IsRush = isRush;
	}

	public String getVisitNum() {
		return VisitNum;
	}

	public void setVisitNum(String visitNum) {
		VisitNum = visitNum;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getTimeStr() {
		return TimeStr;
	}

	public void setTimeStr(String timeStr) {
		TimeStr = timeStr;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getOriginPrice() {
		return OriginPrice;
	}

	public void setOriginPrice(String originPrice) {
		OriginPrice = originPrice;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getStatusStr() {
		return StatusStr;
	}

	public void setStatusStr(String statusStr) {
		StatusStr = statusStr;
	}

	public boolean isRushOut() {
		return RushOut;
	}

	public void setRushOut(boolean rushOut) {
		RushOut = rushOut;
	}

}
