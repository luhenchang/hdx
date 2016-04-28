package com.accuvally.hdtui.utils.eventbus;

public class ChangePaySuccessEventBus {
	private int msg;
	
	public ChangePaySuccessEventBus(int msg) {
		this.msg = msg;
	}

	public int getMsg() {
		return msg;
	}
}
