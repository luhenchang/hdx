package com.accuvally.hdtui.model;

import java.io.Serializable;

public class TicketDetails implements Serializable {
	private String Address;
	private String End;
	private String ExpiredDate;
	private String Price;
	private String QRCode;
	private String RemindNotes;
	private String Start;
	private String TicketType;
	private String UserEmail;
	private String UserName;
	private String UserLogo;
	private String UserPhone;
	private String Title;
	private String ID;
	private int Status;

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

	private String StatusStr;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public String getEnd() {
		return End;
	}

	public void setEnd(String end) {
		End = end;
	}

	public String getExpiredDate() {
		return ExpiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		ExpiredDate = expiredDate;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}

	public String getRemindNotes() {
		return RemindNotes;
	}

	public void setRemindNotes(String remindNotes) {
		RemindNotes = remindNotes;
	}

	public String getStart() {
		return Start;
	}

	public void setStart(String start) {
		Start = start;
	}

	public String getTicketType() {
		return TicketType;
	}

	public void setTicketType(String ticketType) {
		TicketType = ticketType;
	}

	public String getUserEmail() {
		return UserEmail;
	}

	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserPhone() {
		return UserPhone;
	}

	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}

	public String getUserLogo() {
		return UserLogo;
	}

	public void setUserLogo(String userLogo) {
		UserLogo = userLogo;
	}

}
