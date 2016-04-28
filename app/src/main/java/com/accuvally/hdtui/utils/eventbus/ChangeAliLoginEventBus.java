package com.accuvally.hdtui.utils.eventbus;

public class ChangeAliLoginEventBus {
	private int status;

	private String result;

	public ChangeAliLoginEventBus(int status, String result) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public int getStatus() {
		return status;
	}

}
