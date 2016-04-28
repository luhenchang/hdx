package com.accuvally.hdtui.model;

import java.io.Serializable;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class NotificationInfo implements Serializable{
	
	public int extend;// 1代表新朋友请求
	public long sendTimestamp;

	private String msgId;// 唯一

	private String title;// 如果没有点击会话，铃铛下拉界面上显示的通知�标题

	private String message;// 真正发送的消息实用内容

	private String logo_url;// 聊天界面里的头像url，也就是发送者的头像url

	private String source_id;// 发送者id

	private String source_name;// 发送者的昵称

	private String source_logo_url;// 发送者的头像

	private int type;// 999表示该IM消息为群聊天里的旁白信息         //1为语 2为图3为文

	private String from_name;// 由谁发送，发送者的昵称

	private String from_peer;// 由谁发送者的id

	private String to_id;// 发送到哪里，指的就是会话id

	private String to_peer;// 接收者的id，如：小明的user_id�23we32

	private String to_name;// 接收者的昵称

	private int target_type;// 1表示往群里发消�0表示给单个用户发消息

	public static final int VERSION_UPDATE = 1;//客户端升级通知
	public static final int NOTICE_BEFORE_START = 2;//活动开始前通知
	public static final int RELATED_RECOMMEND = 3;//相关活动推荐通知
	public static final int OPERATE_NOTICE = 4;//运营提醒
	public static final int REGIST_VERIFIED = 5;//活动报名审核通过
	public static final int NEW_EVENTS_BY_FOLLOWED_ORGANIZER = 6;//关注的主办方发布新的活动
	public static final int GROUP_ORGANIZER_SPEAK = 7;//圈子消息--主办方发言
	public static final int GROUP_ORGANIZER_NOTICE = 8;//圈子消息--主办方通知
	public static final int GROUP_OTHER_SPEAK = 9;//圈子消息--其他圈友消息
	public static final int GROUP_AT_ME = 10;//
	public static final int NOTICE_FROM_FRIEND = 11;//圈友一对一消息
	public static final int NOTICE_FOR_NEW_GROUP_EVENT = 12;//新的圈子加入邀请
	public static final int NOTICE_FOR_NEW_GROUP_OTHER = 13;//其他用户发的圈子加入邀请
	public static final int INVITED_FROM_FRIEND = 14;//其他用户发来的加好友邀请
	/** 创建人发布公告 **/
	public static final int ANNOUNCE = 16;

	private int message_type;// IM的消息类

	private String activity_id;// 活动id
	
	private int isNotification;//是否是通知    1是通知   0不是通知

	private int unreadNum;// 未读消息条数
	
	private long time;//收到的时
	
	private int op_type;//0-(默认，没有操作),1-活动,2-专题,3-圈子，4-主办方,5-网页
	
	private String op_value;

	public int getOp_type() {
		return op_type;
	}

	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	public String getOp_value() {
		return op_value;
	}

	public void setOp_value(String op_value) {
		this.op_value = op_value;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getIsNotification() {
		return isNotification;
	}

	public void setIsNotification(int isNotification) {
		this.isNotification = isNotification;
	}

	public String getSource_logo_url() {
		return source_logo_url;
	}

	public void setSource_logo_url(String source_logo_url) {
		this.source_logo_url = source_logo_url;
	}

	public int getUnreadNum() {
		return unreadNum;
	}

	public void setUnreadNum(int unreadNum) {
		this.unreadNum = unreadNum;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getTo_name() {
		return to_name;
	}

	public void setTo_name(String to_name) {
		this.to_name = to_name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFrom_peer() {
		return from_peer;
	}

	public void setFrom_peer(String from_peer) {
		this.from_peer = from_peer;
	}

	public String getTo_id() {
		return to_id;
	}

	public void setTo_id(String to_id) {
		this.to_id = to_id;
	}

	public String getTo_peer() {
		return to_peer;
	}

	public void setTo_peer(String to_peer) {
		this.to_peer = to_peer;
	}

	public int getTarget_type() {
		return target_type;
	}

	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}

	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	
	@Override
	public String toString() {
		return JSON.toJSON(this).toString();
	}
	
	/*
	 *{"target_type":0,"title":"切切切轻�,"message":"我们是社会主义接班人,继承革命先辈的光荣传统！爱祖国爱人民",
	 *"logo_url":"http://www.huodongxing.com/Content/v2.0/img/face/male/small/13.jpg",
	 *"source_id":"3141959723033"
	 *,"source_name":"晓光","type":10,
	 *"from_name":"活动行_name",
	 *"from_peer":"活动行_id","to_id":"123456",
	 *"to_peer":"6201513781803","to_name":"小杰","message_type":10,
	 *"activity_id":"1232123",
	 *"source_logo_url":"http://www.huodongxing.com/Content/v2.0/img/face/male/small/13.jpg",
	 *"isNotification":0}
	 */
	public static String toJSON(UserInfo info,SessionInfo session ,String msg) {
		 JSONObject obj = new JSONObject();
		 obj.put("target_type", 0);
		 obj.put("title", session.getTitle());
		 obj.put("message", msg);
		 obj.put("logo_url", session.getLogoUrl());
		 obj.put("source_id", info.getAccount());
		 obj.put("source_name", info.getNick());
		 obj.put("type", 3);
		 obj.put("from_name", info.getNick());
		 obj.put("from_peer", "");
		 Log.i("info", session.getSessionId()+"-------------");
		 obj.put("to_id", session.getSessionId());//会话ID
		 obj.put("to_name", info.getNick());
		 obj.put("message_type", "13");
		 obj.put("source_logo_url", info.getLogo());
		 obj.put("isNotification", "0");
		 return obj.toString();
	}
}
