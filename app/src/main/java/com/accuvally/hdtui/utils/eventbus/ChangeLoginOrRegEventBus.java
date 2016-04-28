package com.accuvally.hdtui.utils.eventbus;

public class ChangeLoginOrRegEventBus {

	private int isLoginOrReg;

	private String phone;

	public ChangeLoginOrRegEventBus(String phone, int isLoginOrReg) {
		this.isLoginOrReg = isLoginOrReg;
		this.phone = phone;
	}

	public int getIsLoginOrReg() {
		return isLoginOrReg;
	}

	public String getPhone() {
		return phone;
	}

}
