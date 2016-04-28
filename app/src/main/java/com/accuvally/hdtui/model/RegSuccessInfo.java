package com.accuvally.hdtui.model;

import java.io.Serializable;

import com.accuvally.hdtui.utils.TimeUtils;

/*
 *  	
 *
 "NeedApply": true,       -- 是否需要审核    
 "Msg": "报名成功，请等待审核！",       -- 
 "UserName": "AA",          -- 用户名    
 "Email": "linxueji@accuvally.com",
 "Phone": "13878787654",    -- 电话
 "Title": "两种票种活动-1", -- 活动名称
 "Logo": "",                
 "StartTime": "2014-12-07 08:00",
 "EndTime": "2014-12-09 16:00",
 "Count": 1,                -- 票券数量
 "TicketType": "免费票",      -- 票券类型
 "Price": 0.0,              -- 单价
 "Total": 0.0               -- 总价
 "IsAppPay": 1,             -- 是否使用App本地支付  1 app支付  0  网页支付
 "EecryptId":""             -- 加密后的票券id  支付时使用
 "PayUrl":                  -- 网页支付url
 "https://www.huodongxing.com/pay/checkout?tids=12B5447B627839F6A3C7DA6&t0",
 */
public class RegSuccessInfo implements Serializable {

	private String Summary;

	private String Id;

	private String City;

	private String UserName;

	private String Email;

	private String Phone;

	private String Title;

	private String Logo;

//	private String StartTime;
//	private String EndTime;
	
	private String startutc;
	private String endutc;

	private int Count;

	private String TicketId;

	private String TicketType;

	private String BuyDate;

	private double Price;

	private double Total;

	private String PayUrl;

//	private String EecryptId;
	private String encryptid;

	private int IsAppPay;

	private boolean NeedApply;

	private String Msg;

	private String RemainingTimeStr;

	private String ShareUrl;

	private String RefundPolicy;

	public String getTicketId() {
		return TicketId;
	}

	public void setTicketId(String ticketId) {
		TicketId = ticketId;
	}

	public String getRemainingTimeStr() {
		return RemainingTimeStr;
	}

	public void setRemainingTimeStr(String remainingTimeStr) {
		RemainingTimeStr = remainingTimeStr;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public boolean isNeedApply() {
		return NeedApply;
	}

	public void setNeedApply(boolean needApply) {
		NeedApply = needApply;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public int getIsAppPay() {
		return IsAppPay;
	}

	public void setIsAppPay(int isAppPay) {
		IsAppPay = isAppPay;
	}

	public String getBuyDate() {
		return BuyDate;
	}

	public void setBuyDate(String buyDate) {
		BuyDate = buyDate;
	}

	public String getEncryptid() {
		return encryptid;
	}

	public void setEncryptid(String encryptid) {
		this.encryptid = encryptid;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

//	public String getStartTime() {
//		return StartTime;
//	}
//
//	public void setStartTime(String startTime) {
//		StartTime = startTime;
//	}
//
//	public String getEndTime() {
//		return EndTime;
//	}
//
//	public void setEndTime(String endTime) {
//		EndTime = endTime;
//	}

	public int getCount() {
		return Count;
	}

	public String getStartutc() {
		return TimeUtils.utcToLocal(startutc);
	}

	public void setStartutc(String startutc) {
		this.startutc = startutc;
	}

	public String getEndutc() {
		return TimeUtils.utcToLocal(endutc);
	}

	public void setEndutc(String endutc) {
		this.endutc = endutc;
	}

	public void setCount(int count) {
		Count = count;
	}

	public String getTicketType() {
		return TicketType;
	}

	public void setTicketType(String ticketType) {
		TicketType = ticketType;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public double getTotal() {
		return Total;
	}

	public void setTotal(double total) {
		Total = total;
	}

	public String getPayUrl() {
		return PayUrl;
	}

	public void setPayUrl(String payUrl) {
		PayUrl = payUrl;
	}

	public String getRefundPolicy() {
		return RefundPolicy;
	}

	public void setRefundPolicy(String refundPolicy) {
		RefundPolicy = refundPolicy;
	}

}
