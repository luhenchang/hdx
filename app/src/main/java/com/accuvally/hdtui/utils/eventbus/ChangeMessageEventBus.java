package com.accuvally.hdtui.utils.eventbus;

import com.accuvally.hdtui.model.MessageInfo;


/***
 *
 * 
 * 
 * post
 * {@link com.accuvally.hdtui.AccuApplication.MsgHandler#onMessage}
 * 
 * 
 */
//1.ChatActivity:增加一条聊天记录
    //2.MainActivityNew:updateUnreadNum
    //3.NotificationActivity:增加一条session
    //4.ChangeMessageEventBus:增加一条session
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
