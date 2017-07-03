package com.accuvally.hdtui.manager;

import android.text.TextUtils;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;
//leanCloud登录：1.所做的事：连接leancloud服务器，在登录之后从leancloud服务器查询到会话列表（包括一条会话信息），并插入到本地数据库的TABLE_SESSION表中
//2.调用时机：在登录，注册，绑定，或者mainactivity（已登录）中，数据库升级中
//3.代码：AccuApplication.leanCloudLogin（）-->LeanCloud.querySession()+queryMessage();

//主要处理了登录之后查询会话的逻辑
public class LeanCloud {

    public static ExecutorService singleExecutor = null;

	public static void upgradeDB() {//
		String userId = AccountManager.getAccount();
		if (TextUtils.isEmpty(userId)) {
			return;
		}

        Trace.e("LeanCloud  upgradeDB", "Thread.currentThread().getName():" + Thread.currentThread().getName());


		final AVIMClient imClient = AVIMClient.getInstance(userId);
        Trace.e("LeanCloud", "upgradeDB");
		imClient.open(new AVIMClientCallback() {
            public void done(AVIMClient client, AVIMException e) {
                Trace.e("LeanCloud  ", "imClient.open  done");
                if (null != e) {
                    Trace.e("LeanCloud", "imClient.open ,null != e  ");
                    if (ChatActivity.isShown) {
                        ToastUtil.showMsg("网络出问题啦，聊天功能暂不可用");
                    }
                    e.printStackTrace();
                } else {
                    Trace.e("LeanCloud  done", "upgradeDB  querySession");
                    Trace.e("LeanCloud ", "Thread.currentThread().getName():" + Thread.currentThread().getName());
                    querySession();
                }
            }
        });
	}



/*    public static void querySessionAnsy(){
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }

        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                querySession();
            }
        });

    }*/



    //查询所有对话
    public static void querySession() {
        AVIMClient client = AVIMClient.getInstance(AccountManager.getAccount());
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.setLimit(500);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {

            @Override
            public void done(final List<AVIMConversation> list, AVIMException arg1) {

                if (singleExecutor == null) {
                    singleExecutor = Executors.newSingleThreadExecutor();
                }

                singleExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Trace.e("querySession  singleExecutor", "Thread.currentThread().getName():" + Thread.currentThread().getName());
                        if (list != null && list.size() > 0) {
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                AVIMConversation conversation = list.get(i);
                                SessionInfo session = new SessionInfo(conversation, true);
                                session.setTitle(conversation.getName());
                                SessionTable.insertOrUpdateSession(session);
                            }
                        }
                        Trace.e("", "querySession  done  finish");
                    }
                });

                if (list != null && list.size() > 0) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        AVIMConversation conversation = list.get(i);
                        queryMessage(conversation, size);//size 是标志位
                    }
                }

            }
        });
    }

	public static int convPosition=0;

    //查询每个对话的一个具体聊天内容
    public static void queryMessage(final AVIMConversation conversation, final int size) {
        conversation.queryMessages(1, new AVIMMessagesQueryCallback() {

            @Override
            public void done(final List<AVIMMessage> list, AVIMException ex) {

                singleExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size() > 0) {
                            AVIMMessage message = list.get(0);
                            MessageInfo messageInfo = new MessageInfo(AccuApplication.getInstance(), message);
                            NotificationInfo notificationInfo = JSON.parseObject(messageInfo.getContent(), NotificationInfo.class);
                            notificationInfo.setTime(message.getTimestamp());
                            SessionInfo session = new SessionInfo(notificationInfo, conversation, true);
                            SessionTable.insertOrUpdateSession(session);
                        }
//                        Trace.e("queryMessage ", "convPosition:" + (convPosition + 1) + ",size:" + size);

                        if (++convPosition >= size) {
                            Trace.e("queryMessage  singleExecutor", "Thread.currentThread().getName():" + Thread.currentThread().getName());
                            EventBus.getDefault().post(new ChangeMessageEventBus());
                            AccuApplication.getInstance().sharedUtils.writeBoolean(Keys.insertAllSession, true);
                            convPosition = 0;
                        }

                    }
                });

            }
        });
    }



}
