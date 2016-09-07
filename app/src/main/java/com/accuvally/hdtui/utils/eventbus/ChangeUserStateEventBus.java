package com.accuvally.hdtui.utils.eventbus;

public class ChangeUserStateEventBus {

	public static int LOGIN = 22;
	public static int LOGOUT = 23;
//    public static int REFRESH= 24;//刷新信息
	private int msg;

	public ChangeUserStateEventBus(int msg) {
		this.msg = msg;
	}

	public int getMsg() {
		return msg;
	}
}
