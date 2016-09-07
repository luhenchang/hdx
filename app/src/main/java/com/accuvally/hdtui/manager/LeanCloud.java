package com.accuvally.hdtui.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.db.DBThreadManager;
import com.accuvally.hdtui.db.MessageTable;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

import de.greenrobot.event.EventBus;

public class LeanCloud {

//	public static AVIMClient imClient = AVIMClient.getInstance(AccountManager.getAccount());

	public static AVIMClient getIMClient() {
		return AVIMClient.getInstance(AccountManager.getAccount());
	}

	public void initLeanCloud() {
		AVOSCloud.initialize(AccuApplication.getInstance(), Config.ACCUPASS_LEANCLOUD_APP_ID, Config.ACCUPASS_LEANCLOUD_APP_KEY);
		AVIMClient.setClientEventHandler(new AVIMClientEventHandler() {

			@Override
			public void onConnectionResume(AVIMClient arg0) {
				if (AccountManager.checkIsLogin()) {
					leanCloudLogin();
				}
			}

			@Override
			public void onConnectionPaused(AVIMClient arg0) {

			}

			@Override
			public void onClientOffline(AVIMClient arg0, int arg1) {
			}
		});
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MsgHandler());
	}

	private class MsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

		@Override
		public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

			if (message == null)
				return;
			MessageInfo msg = new MessageInfo(AccuApplication.getInstance(), message);
			MessageTable.insertMessage(msg);

			NotificationInfo info = JSON.parseObject(msg.getContent(), NotificationInfo.class);
			info.setTime(message.getTimestamp());
			info.setMsgId(message.getMessageId());
			info.setUnreadNum(SessionTable.querySessionByUnReadNum(conversation.getConversationId()));

//			SessionInfo session = new SessionInfo(info);
//			session.setAVIMConversation(conversation);
//			SessionTable.insertSession(session, false);

			// 发消息修改界面
//			ChangeMessageEventBus messageEvent = new ChangeMessageEventBus(1);
//			messageEvent.setMessageInfo(msg);
//			EventBus.getDefault().post(messageEvent);
		}

		@Override
		public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

		}
	}

	public static void leanCloudLogin() {
		String userId = AccountManager.getAccount();
		if (TextUtils.isEmpty(userId)) {
			return;
		}

		final AVIMClient imClient = AVIMClient.getInstance(userId);
		imClient.open(new AVIMClientCallback() {
			public void done(AVIMClient client, AVIMException e) {
				if (null != e) {
					if (ChatActivity.isShown) {
						ToastUtil.showMsg("网络出问题啦，聊天功能暂不可用");
					}
					e.printStackTrace();
				} else {
					querySession();
				}
			}
		});
	}

	public static void querySession() {
		AVIMClient client = AVIMClient.getInstance(AccountManager.getAccount());
		AVIMConversationQuery conversationQuery = client.getQuery();
		conversationQuery.setLimit(500);
		conversationQuery.findInBackground(new AVIMConversationQueryCallback() {

			@Override
			public void done(List<AVIMConversation> list, AVIMException arg1) {
				if (list != null && list.size() > 0) {
					int size = list.size();
					for (int i = 0; i < size; i++) {
						AVIMConversation conversation = list.get(i);
						SessionInfo session = new SessionInfo(conversation, true);
						session.setTitle(conversation.getName());
						SessionTable.AsyncInsertSession(session);
						
						queryMessage(conversation, size);
					}
				}
			}
		});
	}

	public static int convPosition;

	public static void queryMessage(final AVIMConversation conversation, final int size) {
		conversation.queryMessages(1, new AVIMMessagesQueryCallback() {

			@Override
			public void done(List<AVIMMessage> list, AVIMException ex) {
				if (list != null && list.size() > 0) {
					AVIMMessage message = list.get(0);
					MessageInfo messageInfo = new MessageInfo(AccuApplication.getInstance(), message);
					NotificationInfo notificationInfo = JSON.parseObject(messageInfo.getContent(), NotificationInfo.class);
					notificationInfo.setTime(message.getTimestamp());
					final SessionInfo session = new SessionInfo(notificationInfo, conversation, true);
					SessionTable.AsyncInsertSession(session);
					DBThreadManager.insertSession(session);
				}

				if (++convPosition >= size) {
					EventBus.getDefault().post(new ChangeMessageEventBus());
					AccuApplication.getInstance().sharedUtils.writeBoolean(Keys.insertAllSession, true);
					convPosition = 0;
				}
			}
		});
	}

	public static void createConversation(String friendId) {
		List<String> clientIds = new ArrayList<String>();
		clientIds.add(AccountManager.getAccount());
		clientIds.add(friendId);

		int ConversationType_OneOne = 1; // 两个人之间的单聊
		int ConversationType_Group = 0; // 多人之间的群聊
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("type", ConversationType_OneOne);
		getIMClient().createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {

			@Override
			public void done(AVIMConversation conversation, AVIMException e) {
				if (null != conversation) {

				}
			}
		});
	}

	public static void connectLeanCloud() {
		getIMClient().open(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient arg0, AVIMException arg1) {

			}
		});
	}

}
