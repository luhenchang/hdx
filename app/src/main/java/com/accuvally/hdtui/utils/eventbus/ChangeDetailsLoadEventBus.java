package com.accuvally.hdtui.utils.eventbus;

public class ChangeDetailsLoadEventBus {

	private boolean msg;

	public ChangeDetailsLoadEventBus(boolean msg) {
		this.msg = msg;
	}

	public boolean isMsg() {
		return msg;
	}

}
