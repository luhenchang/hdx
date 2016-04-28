package com.accuvally.hdtui.utils.eventbus;

public class ChangeHomeLoaderEventBus {
	private boolean msg;

	public ChangeHomeLoaderEventBus(boolean msg) {
		this.msg = msg;
	}

	public boolean isMsg() {
		return msg;
	}

}
