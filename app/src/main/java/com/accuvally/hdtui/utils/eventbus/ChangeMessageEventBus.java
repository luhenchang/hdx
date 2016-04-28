package com.accuvally.hdtui.utils.eventbus;

import com.accuvally.hdtui.model.MessageInfo;


/***
 * {@link com.accuvally.hdtui.activity.ChatActivity#onEventMainThread(ChangeMessageEventBus)}
 * 
 * 
 * post
 * {@link com.accuvally.hdtui.AccuApplication.MsgHandler#onMessage}
 * 
 * 
 */
public class ChangeMessageEventBus {
	
	private int msgId;
	public String sessionId;
	private MessageInfo messageInfo;
	
	public ChangeMessageEventBus() {}
	
	public ChangeMessageEventBus(int msgId) {
		this.msgId = msgId;
	}
	
	public ChangeMessageEventBus(String sessionId, MessageInfo messageInfo) {
		this.sessionId = sessionId;
		this.messageInfo = messageInfo;
	}


	public int getMsgId() {
		return msgId;
	}


	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(MessageInfo messageInfo) {
		this.messageInfo = messageInfo;
	}


}
