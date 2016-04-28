package com.accuvally.hdtui;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

import de.greenrobot.event.EventBus;

public class MsgServices extends IntentService {

	private AVIMClient mAVIMClient;
	private String sessionId;
	private String userId;
	private DBManager dbManager;
	private int limit = 20;
	private boolean downLoadFinish = false;
	private boolean isRefresh;

	public MsgServices() {
		super("com.accuvally.hdtui.MsgServices");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		UserInfo userInfo = (UserInfo) intent.getSerializableExtra("user");
		SessionInfo sessionInfo = (SessionInfo) intent.getSerializableExtra("session");
		downLoadFinish = false;
		if (userInfo != null && sessionInfo != null) {
			userId = userInfo.getAccount();
			sessionId = sessionInfo.getSessionId();

			dbManager = new DBManager(getBaseContext());

			mAVIMClient = AVIMClient.getInstance(userId);

			final AVIMConversation conversation = mAVIMClient.getConversation(sessionId);

			leanCloudLogin(conversation, userId);

		}

	}

	/**
	 * leanCloud登录
	 * 
	 * @param userId
	 */
	public void leanCloudLogin(final AVIMConversation conversation, String userId) {
		final AVIMClient imClient = AVIMClient.getInstance(userId);
		imClient.open(new AVIMClientCallback() {
			public void done(AVIMClient client, AVIMException e) {
				if (null != e) {
					e.printStackTrace();
				} else {
					// 每次都要拉去最新的，然后依次迭代
					queryMsg(conversation, null);
				}
			}
		});
	}


	private void queryMsg(final AVIMConversation conversation, final MessageInfo last) {
		if (last == null) {
			conversation.queryMessages(limit, new AVIMMessagesQueryCallback() {
				@Override
				public void done(List<AVIMMessage> arg0, AVIMException arg1) {
					try {
						if (arg0 != null && !downLoadFinish) {
							Log.d("d", "queryMessages----last == null--------  " + (arg0 == null ? ("arg0=" + arg0) : arg0.size()));
							MessageInfo info = dbManager.bulkinsertMsg(arg0, userId);
							// 如果库里已经有这条数据，说明已经全部缓存成功
							if (info.getInsertCount() < limit) {
								// 最近的数据拉去完成
								// 还有以前的数据可能没有拉去下来
								if(dbManager.queryFirstMsg(info.getSessionId())!= null)
									queryMsg(conversation, dbManager.queryFirstMsg(info.getSessionId()));

							} else {
								// 继续拉去最新的数据
								// 还有数据没有拉取下来
								if(info!=null)
									queryMsg(conversation, info);
							}
							// 只更新一次
							if (info.getInsertCount() > 0 && !isRefresh) {
								// 有数据就更新
								// 数据全部拉取下来
								EventBus.getDefault().post(new ChangeMessageEventBus(2));
								isRefresh = true;
								Log.d("d", "last == null-----------EventBus.getDefault().post--------- ");
							}
						} else {
							downLoadFinish = true;
							// 一条都没有不需要更新
							// 或者是数据都已经拉去完成
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("d", e.getMessage());
					}
				}
			});
		} else {

			conversation.queryMessages(last.getMessageId(), last.getTimestamp(), limit, new AVIMMessagesQueryCallback() {
				@Override
				public void done(List<AVIMMessage> arg0, AVIMException arg1) {

					try {
						Log.d("d", "queryMessages-----last != null--------  " + (arg0 == null ? ("arg0=" + arg0) : arg0.size()));
						if (arg0 != null && !downLoadFinish) {

							MessageInfo info = dbManager.bulkinsertMsg(arg0, userId);
							// 如果库里已经有这条数据，说明已经全部缓存成功
							if (info.getInsertCount() < limit) {
								// 数据全部拉取下来
								downLoadFinish = true;
								// 只更新一次
								if (downLoadFinish/* && !isRefresh*/) {
									// 有数据就更新
									// 数据全部拉取下来
									//EventBus.getDefault().post(new ChangeMessageEventBus(2));
									isRefresh = true;
									Log.d("d", "----------EventBus.getDefault().post--------- ");
								}
							} else {
								// 还有数据没有拉取下来
								if(info!=null)
									queryMsg(conversation, info);
							}
							
						} else {
							// 一条都没有不需要更新
							downLoadFinish = true;
							// 只更新一次
							if (downLoadFinish) {
								// 有数据就更新
								// 数据全部拉取下来
								//EventBus.getDefault().post(new ChangeMessageEventBus(2));
								isRefresh = true;
								Log.d("d", "----------EventBus.getDefault().post--------- ");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("d", e.getMessage());
					}
				}
			});
		}
	}
 	
}
