package com.accuvally.hdtui.model;

import android.content.Context;
import android.util.Log;

import com.accuvally.hdtui.utils.ImageTools;
import com.accuvally.hdtui.utils.TimeUtils;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MessageInfo implements Serializable,Comparable<MessageInfo> {

	public static final int TYPE_TEXT = -1;// 文本消息 -1
	public static final int TYPE_PHOTO = -2;// 图像消息 -2
	public static final int TYPE_RECORDER = -3;// 音频消息
	public static final int TYPE_VIDEO = -4;// 视频消息 -4
	public static final int TYPE_LOCATION = -5;// 位置消息 -5
	public static final int TYPE_FILE = -6;// 文件消息 -6

	public String title;
	public String newContent;
	/** 1有人发起申请 	2对方已经同意申请 **/
	public int extend;
	public int message_type;// IM的消息类	16——代表公告

	private long id;
	private String messageId = "";
	private String nickName;
	private String logourl;// 用户头像
	private String content;
	private int msgType = TYPE_TEXT;
	private long timestamp;// 上传的时间戳
	private long receipTimestamp;// 保留字段
	private String sessionId = "";// 会话ID
	private String userId;// 谁发的信息
	private String textStr = "";// 文本的内容
	private String filePath = "";// 文件的本地地址
	private String fileUrl = "";// 文件的服务器地址
	private String fileThumbnailUrl = "";// 文件的缩略图服务器地址
	private double lengh;// 文件大小

	private int imgWidth;// 上传图片是保存的值
	private int imgHeight;

	private int insertCount;// 更新Message count

	public int getInsertCount() {
		return insertCount;
	}

	public void setInsertCount(int insertCount) {
		this.insertCount = insertCount;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getTextStr() {
		return textStr;
	}

	public void setTextStr(String text) {
		this.textStr = text;
	}

	public MessageInfo() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getReceipTimestamp() {
		return receipTimestamp;
	}

	public void setReceipTimestamp(long receipTimestamp) {
		this.receipTimestamp = receipTimestamp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private void setMessage(Map<String, Object> map) {
		if (map != null) {
			Object obj = map.get("content");
			
			if (obj != null) {// 文字
				NotificationInfo info = JSON.parseObject((String) obj, NotificationInfo.class);
				setContent(obj.toString());
				setUserId(info.getSource_id());
				setLogourl(info.getSource_logo_url());
				setNickName(info.getSource_name());
				setTextStr(info.getMessage());
				title = info.getTitle();
				newContent = info.getMessage();
				extend = info.extend;
				message_type = info.getMessage_type();
			}
			
			Object lenObj = map.get("lengh");// 语音
			if (lenObj != null) {
				double len = Double.parseDouble(lenObj.toString());
				setLengh(len);
			}
		}
	}

	public MessageInfo(Context ctx, AVIMMessage message) {

		setMessageId(message.getMessageId());
		setSessionId(message.getConversationId());

		setTimestamp(message.getTimestamp());
		setReceipTimestamp(message.getReceiptTimestamp());


		if (message instanceof AVIMTypedMessage) {

			int type = (((AVIMTypedMessage) message).getMessageType());

			switch (type) {
			case -1:
				try {
					AVIMTextMessage textMsg = (AVIMTextMessage) message;
					setMessage(textMsg.getAttrs());
					setMsgType(TYPE_TEXT);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case -2:
				AVIMImageMessage imageMsg = (AVIMImageMessage) message;
				setMessage(imageMsg.getAttrs());
				// 保存图片的宽度和高度
				setImgWidth(imageMsg.getWidth());
				setImgHeight(imageMsg.getHeight());
				setFileUrl(imageMsg.getFileUrl());

				int[] xy = ImageTools.scaleXY(ctx, imageMsg.getWidth(), imageMsg.getHeight());
				setFileThumbnailUrl(imageMsg.getAVFile().getThumbnailUrl(true, xy[0], xy[1]));
				setMsgType(TYPE_PHOTO);
				break;
			case -3:
				AVIMAudioMessage audioMsg = (AVIMAudioMessage) message;
				setMessage(audioMsg.getAttrs());
				setFileUrl(audioMsg.getFileUrl());
				setMsgType(TYPE_RECORDER);
				break;
			case -4:
				AVIMFileMessage fileMsg = (AVIMFileMessage) message;
				setMessage(fileMsg.getAttrs());
				setMsgType(TYPE_FILE);
				break;
			case -5:
				AVIMVideoMessage videoMsg = (AVIMVideoMessage) message;
				setMessage(videoMsg.getAttrs());
				setMsgType(TYPE_VIDEO);
				break;
			case -6:
				AVIMLocationMessage locMsg = (AVIMLocationMessage) message;
				setMessage(locMsg.getAttrs());
				setMsgType(TYPE_LOCATION);
				break;
			}
		} else {

			NotificationInfo info = JSON.parseObject(message.getContent(), NotificationInfo.class);
			setUserId(message.getFrom());
			setLogourl(info.getSource_logo_url());
			setNickName(info.getSource_name());
			setTextStr(info.getMessage());
			setContent(message.getContent());
			setMsgType(TYPE_TEXT);
		}
		Log.d("dd", message+ ": " +toString());
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public double getLengh() {
		return lengh;
	}

	public void setLengh(double lengh) {
		this.lengh = lengh;
	}

	public String getFileThumbnailUrl() {
		return fileThumbnailUrl;
	}

	public void setFileThumbnailUrl(String fileThumbnailUrl) {
		this.fileThumbnailUrl = fileThumbnailUrl;
	}

	public static String toContentJSON(UserInfo info, SessionInfo session, MessageInfo msg, boolean isPrivateChat) {
		JSONObject obj;
		try {
			obj = new JSONObject();

			if (isPrivateChat) {// 私聊
				obj.put("title", info.getNick());// 设置自己的nick为title
				obj.put("logo_url", info.getLogoLarge());// 设置自己的头像为logo
				obj.put("target_type", 0); // 0表示给单个用户发消息
			} else {// 群聊
				obj.put("title", session.getTitle());
				obj.put("logo_url", session.getLogoUrl());
				obj.put("target_type", 1); // 1表示往群里发消息
			}

			obj.put("to_id", session.getSessionId());// 会话ID
			obj.put("message", msg.getTextStr());
			obj.put("type", msg.getMsgType());

			obj.put("source_id", info.getAccount());
			obj.put("source_name", info.getNick());
			obj.put("from_name", info.getNick());
			obj.put("to_name", info.getNick());
			obj.put("source_logo_url", info.getLogoLarge());

			obj.put("from_peer", "");
			obj.put("message_type", 13);// 会话类型
			obj.put("isNotification", "0");
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String toString() {
		return "MessageInfo [timestamp=" + TimeUtils.SD_MDHM_S.format(new Date(timestamp))  + ", textStr=" + textStr + "]";
	}

	@Override
	public int compareTo(MessageInfo another) {
		if(another.getTimestamp()-getTimestamp()>0){
			return 1;
		}else if(another.getTimestamp()-getTimestamp()<0){
			return -1;
		}else{
			return 0;
		}
	}
	
	public String getMessageTime() {
		if (timestamp == 0) {
			return "";
		}
		
		Date receiveDate = new Date(timestamp);
		Calendar now = Calendar.getInstance();
		Calendar receive = Calendar.getInstance();
		receive.setTime(receiveDate);

		if (now.get(Calendar.YEAR) == receive.get(Calendar.YEAR)) {
			boolean sameMonth = now.get(Calendar.MONTH) == receive.get(Calendar.MONTH);
			if (sameMonth && now.get(Calendar.DAY_OF_MONTH) == receive.get(Calendar.DAY_OF_MONTH)) {
				return TimeUtils.SD_HM.format(receiveDate);
			} else {
				return TimeUtils.mdhmFormat.format(receiveDate);
			}
		} else {
			return TimeUtils.yyyyMMdd_HHmm.format(receiveDate);
		}
	}
	
	public String getSessionTime() {
		if (timestamp == 0) {
			return "";
		}
		
		Date receiveDate = new Date(timestamp);
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

}
