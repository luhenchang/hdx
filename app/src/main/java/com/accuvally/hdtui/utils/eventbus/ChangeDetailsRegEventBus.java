package com.accuvally.hdtui.utils.eventbus;

public class ChangeDetailsRegEventBus {

	private boolean msg;

	public ChangeDetailsRegEventBus(boolean msg) {
		this.msg = msg;
	}

	public boolean isMsg() {
		return msg;
	}

}
