package com.accuvally.hdtui.utils.eventbus;

public class EventRedDot {

	/**
	 * 消息tab小红点是否显示
	 */
	public boolean isShow;
	private int unReadNum;

	public EventRedDot(boolean isShow) {
		this.isShow = isShow;
	}

	public EventRedDot(int unReadNum) {
		this.unReadNum = unReadNum;
		isShow = unReadNum > 0;
	}

}
