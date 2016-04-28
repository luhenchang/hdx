package com.accuvally.hdtui.model;

public class PayInfo {

	private String Id;

	private String ProductTitle;

	private String Amount;

	private String NotifyUrl;

	private String TicketID;

	/** -------wx---------- **/

	private String AppId;

	private String Noncestr;

	private String Package;

	private String PartnerId;

	private String PrepayId;

	private String Sign;

	private String Timestamp;

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public String getNoncestr() {
		return Noncestr;
	}

	public void setNoncestr(String noncestr) {
		Noncestr = noncestr;
	}

	public String getPackage() {
		return Package;
	}

	public void setPackage(String package1) {
		Package = package1;
	}

	public String getPartnerId() {
		return PartnerId;
	}

	public void setPartnerId(String partnerId) {
		PartnerId = partnerId;
	}

	public String getPrepayId() {
		return PrepayId;
	}

	public void setPrepayId(String prepayId) {
		PrepayId = prepayId;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getTicketID() {
		return TicketID;
	}

	public void setTicketID(String ticketID) {
		TicketID = ticketID;
	}

	public String getNotifyUrl() {
		return NotifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		NotifyUrl = notifyUrl;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getProductTitle() {
		return ProductTitle;
	}

	public void setProductTitle(String productTitle) {
		ProductTitle = productTitle;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

}
