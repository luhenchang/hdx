package com.accuvally.hdtui.model;

import android.text.TextUtils;

import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.utils.TimeUtils;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 会话列表
 * 
 * @author Semmer Wang
 * 
 */
public class SessionInfo implements Serializable {

    public String inboxType="";
	
	public String userId = AccountManager.getAccount();// 当前用户id
	public String friendId;// 一对一聊天对方id
	
	public int extend;// 1代表新朋友请求
	
	public boolean isSelfSend;//是自己发送的消息

	public SessionInfo() {}

	public SessionInfo(NotificationInfo info, AVIMConversation avimConversation, boolean isFristReceive) {
		setNotificationInfo(info);
		setAVIMConversation(avimConversation, isFristReceive);
	}
	
	public SessionInfo(NotificationInfo info) {
		setNotificationInfo(info);
	}

	public SessionInfo(AVIMConversation avimConversation, boolean isFristReceive) {
		setAVIMConversation(avimConversation, isFristReceive);
	}

	private void setNotificationInfo(NotificationInfo info) {
		isSelfSend = AccountManager.getAccount().equals(info.getSource_id());
		setLogoUrl(info.getLogo_url());

		setTitle(info.getTitle());
		setNickName(info.getSource_name());
		setContent(info.getMessage());
		setTime(info.getTime());
		setUnReadNum(info.getUnreadNum());
		setFromUserId(info.getSource_id());
		setMessageId(info.getMsgId());
		setSessionType(info.getMessage_type());
		setMessageType(info.getType());
		setIsNotification(info.getIsNotification());
		setOp_type(info.getOp_type());
		setOp_value(info.getOp_value());
		
		extend = info.extend;
	}

	public void setAVIMConversation(AVIMConversation conversation, boolean isFristReceive) {
		sessionId = conversation.getConversationId();
		creator = conversation.getCreator();

		if (TextUtils.isEmpty(logoUrl)) {
			Object obj = conversation.getAttribute("logo");
			if (obj != null) {
				logoUrl = (String) obj;
			}
		}

		try {
			Object type = conversation.getAttribute("type");
			if (type != null && (Integer) type == 1) {// 1 代表一对一聊天 0 群聊
				Object users = conversation.getAttribute("users");
				if (users != null) {
					List<FriendInfo> friendList = JSON.parseArray(users.toString(), FriendInfo.class);
					setFriendList(friendList, isFristReceive);
				}
			}
		} catch (Exception e) {

		}
	}

	public void setFriendList(List<FriendInfo> friendList, boolean isFristReceive) {
		boolean isSelf = userId.equals(friendList.get(0).account);
		FriendInfo friendInfo = isSelf ? friendList.get(1) : friendList.get(0);
		friendId = friendInfo.account;
		if (isFristReceive) {
			title = friendInfo.nick;
			logoUrl = friendInfo.logo;
		}
	}

	private String sessionId;// 会话Id

	private String title;// 会话名称
	
	public String creator;// 会话创建人id

	private String content;// 会话内容

	private long time;// 接收时间

	private String logoUrl;// 会话头像

	private int unReadNum;// 未读消息条数

	private String fromUserId;// 发送者id

	private String toUserId;// 接收者id

	private String messageId;// 消息Id

	private String nickName;// 发送者昵称

	// 1:新版本更新通知
	// 2:已报名的活动开始前一天
	// 3:已报名的活动开始前2个小时
	// 4:已关注的活动开始前一天
	// 5:参与的活动结束后的相关活动推荐
	// 6:关注的主办方发布新的活动
	// 7:有圈的成立邀请通知
	// 8:圈友送过来的信息
	// 9:有新的专题的 通知
	// 10:有人邀请你当圈友
	// 11:有人@你的 通知
	// 12:报名审核通过的 通知
	// 13:聊天
	private int sessionType;// 会话类型

	// 1为语音 2为图片 3为文本4为文件
	private int messageType;// 消息类型

	private int isNotification;// 是否是通知 1是通知 0不是通知

	private int op_type;

	private String op_value;

	// boolean 1-true 0-false
	public boolean isDBMessage;// 对应的sessionId是否插入了所有消息

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSessionType() {
		return sessionType;
	}

	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getIsNotification() {
		return isNotification;
	}

	public void setIsNotification(int isNotification) {
		this.isNotification = isNotification;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SessionInfo [sessionId=" + sessionId + ", title=" + title + ", content=" + content + ", time=" + time + ", logoUrl="
				+ logoUrl + ", unReadNum=" + unReadNum + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", messageId="
				+ messageId + ", nickName=" + nickName + ", sessionType=" + sessionType + ", messageType=" + messageType
				+ ", isNotification=" + isNotification + ", op_type=" + op_type + ", op_value=" + op_value + "]";
	}

	public String getSessionTime() {
		if (time == 0) {
			return "";
		}
		
		Date receiveDate = new Date(time);
		Calendar now = Calendar.getInstance();
		Calendar receive = Calendar.getInstance();
		receive.setTime(receiveDate);

		if (now.get(Calendar.YEAR) == receive.get(Calendar.YEAR)) {
			boolean sameMonth = now.get(Calendar.MONTH) == receive.get(Calendar.MONTH);
			if (sameMonth && now.get(Calendar.DAY_OF_MONTH) == receive.get(Calendar.DAY_OF_MONTH)) {
				return TimeUtils.SD_HM.format(receiveDate);
			} else {
				return TimeUtils.mdFormat.format(receiveDate);
			}
		} else {
			return TimeUtils.yyyyMMdd.format(receiveDate);
		}
	}

	public String getTypeContent() {
		String typeContent = "";
		if (TextUtils.isEmpty(nickName)) {
			return typeContent;
		}

		switch (messageType) {
		case MessageInfo.TYPE_TEXT:
			typeContent = nickName + ":" + content;
			break;
		case MessageInfo.TYPE_PHOTO:
			typeContent = nickName + ":[图片]";
			break;
		case MessageInfo.TYPE_RECORDER:
			typeContent = nickName + ":[语音]";
			break;
		}
		return typeContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public boolean isNotification() {
		return isNotification == 1;
	}

	public boolean isPrivateChat() {
		return !TextUtils.isEmpty(friendId);
	}

	public boolean isAddNewFriend() {
		return extend == 1;
	}
}
