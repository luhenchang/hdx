package com.accuvally.hdtui.model;

import com.accuvally.hdtui.utils.TimeUtils;

public class UnfinishedTicket {
	public String Logo;
	public int BtnStatus;
	public String Title;
	public int IsAppPay;
	public String PayUrl;
	public String Id;
	public String EncryptId;
	public String ExpiredDate;
	public int Total;
	public int LikeNum;
	public String Price;

	public String startutc;
	public String endutc;
	public String address;

	public int getLikeNum() {
		return LikeNum;
	}

	public void setLikeNum(int likeNum) {
		LikeNum = likeNum;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public String getExpiredDate() {
		return ExpiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		ExpiredDate = expiredDate;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getEncryptId() {
		return EncryptId;
	}

	public void setEncryptId(String encryptId) {
		EncryptId = encryptId;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public int getBtnStatus() {
		return BtnStatus;
	}

	public void setBtnStatus(int btnStatus) {
		BtnStatus = btnStatus;
	}

	public int getIsAppPay() {
		return IsAppPay;
	}

	public void setIsAppPay(int isAppPay) {
		IsAppPay = isAppPay;
	}

	public String getPayUrl() {
		return PayUrl;
	}

	public void setPayUrl(String payUrl) {
		PayUrl = payUrl;
	}
	
	
	public String getStartutc() {
		return TimeUtils.utcToLocal(startutc);
	}

	public String getEndutc() {
		return TimeUtils.utcToLocal(startutc);
	}
	
	public String getTimeStr() {
		return TimeUtils.getTimeAreaStr(1, startutc, endutc);
	}

}
