package com.accuvally.hdtui.utils.eventbus;

public class EventRedDot {

	/**
	 * 消息tab小红点是否显示
	 */
	public boolean isShow;
    public boolean check;//是否还需要再次读取数据库检查红点
//	private int unReadNum;


	public EventRedDot(int unReadNum,boolean check) {
        this.check = check;
		isShow = unReadNum > 0;
	}

}
