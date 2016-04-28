package com.accuvally.hdtui.utils.eventbus;


public class EventHideFooterView {

	public Class clazz;
	
	/***
	 * 接收方
	 * {@link com.accuvally.hdtui.fragment.manager.CollectionFragment#onEventMainThread(EventHideFooterView)}}
	 * {@link com.accuvally.hdtui.fragment.manager.EnrollFragment#onEventMainThread(EventHideFooterView)}
	 * {@link com.accuvally.hdtui.fragment.manager.UnfinishedFragment#onEventMainThread(EventHideFooterView)}
	 * 
	 * 发送方
	 * {@link com.accuvally.hdtui.fragment.ManagerFragment#mRight.setOnClickListener}
	 * @param clazz eventBar regist
	 * @param close true close,false open
	 * @param height footview的高度
	 */
	public EventHideFooterView(Class clazz) {
		this.clazz = clazz;
	}

}
