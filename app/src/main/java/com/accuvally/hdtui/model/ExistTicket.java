package com.accuvally.hdtui.model;

public class ExistTicket {
	private String UserName;
	private String Price;
	private String Title;
	private String Addr;
	private String Start;
	private boolean Available;
	private String Id;
	private boolean Free;
	private int InstanceType;
	private String  CorrelationIds;
	private int Count;

	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	public boolean isFree() {
		return Free;
	}
	public void setFree(boolean free) {
		Free = free;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getAddr() {
		return Addr;
	}
	public void setAddr(String addr) {
		Addr = addr;
	}
	public String getStart() {
		return Start;
	}
	public void setStart(String start) {
		Start = start;
	}

	public boolean isAvailable() {
		return Available;
	}
	public void setAvailable(boolean available) {
		Available = available;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public int getInstanceType() {
		return InstanceType;
	}
	public void setInstanceType(int instanceType) {
		InstanceType = instanceType;
	}
	public String getCorrelationIds() {
		return CorrelationIds;
	}
	public void setCorrelationIds(String correlationIds) {
		CorrelationIds = correlationIds;
	}



}
