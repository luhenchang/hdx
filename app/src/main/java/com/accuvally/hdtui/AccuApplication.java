package com.accuvally.hdtui;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.db.AccuvallySQLiteOpenHelper;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.db.SystemMessageTable;
import com.accuvally.hdtui.manager.LeanCloud;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.accuvally.hdtui.model.SaveBeHaviorInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.push.NotifyMessageReceiver;
import com.accuvally.hdtui.utils.CacheUtils;
import com.accuvally.hdtui.utils.SharedUtils;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.baidu.mapapi.SDKInitializer;
import com.microquation.linkedme.android.LinkedME;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Stack;

import de.greenrobot.event.EventBus;

public class AccuApplication extends Application {

	public AccuvallySQLiteOpenHelper accuvallySQLiteOpenHelper;

	private static AccuApplication accuApplication;

	public ImageLoader mImageLoader;

	public SharedUtils sharedUtils;

	public UserInfo userInfo;

	public Long hawkOffset = 0L;

	public int count = 0;

	private TelephonyManager mTelephonyManager;

	public boolean isBound;

	// activity栈
	private Stack<Activity> stack = new Stack<Activity>();

	public CacheUtils cacheUtils;

	public NotificationManager notificationManager;

	private boolean leanCloudConnected;// 判断当前leanclound的登录状态

	public boolean isLeanCloudConnected() {
		return leanCloudConnected;
	}

	public void setLeanCloudConnected(boolean leanCloudConnected) {
		this.leanCloudConnected = leanCloudConnected;
	}

	// 当前会话
	public SessionInfo currentSession = null;

	public SessionInfo getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(SessionInfo currentSession) {
		this.currentSession = currentSession;
	}

	public static synchronized AccuApplication getInstance() {
		return accuApplication;
	}

	public void onCreate() {
		super.onCreate();
		accuApplication = this;
		SDKInitializer.initialize(getApplicationContext());
		cacheUtils = CacheUtils.get(this);
		init();
		getDeviceInfo(this);
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		initImageLoader(accuApplication);
		initLeanCloud();
		if (notificationManager == null)
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initLinkMe();

	}

    private void initLinkMe(){
        try {
            if (BuildConfig.DEBUG){
                //设置debug模式下打印LinkedME日志
                LinkedME.getInstance(this).setDebug();
            }else{
                LinkedME.getInstance(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	public void initLeanCloud() {
		AVOSCloud.initialize(this, Config.ACCUPASS_LEANCLOUD_APP_ID, Config.ACCUPASS_LEANCLOUD_APP_KEY);
		AVIMClient.setClientEventHandler(new AVIMClientEventHandler() {

			@Override
			public void onConnectionResume(AVIMClient arg0) {
				Log.i("fo", "onConnectionResume");
//				if (checkIsLogin()) {
//					leanCloudLogin(getUserInfo().getAccount());
//				}
			}

			@Override
			public void onConnectionPaused(AVIMClient arg0) {
				Log.i("fo", "onConnectionPaused");
				leanCloudConnected = false;
			}

			@Override
			public void onClientOffline(AVIMClient arg0, int arg1) {
			}
		});
		AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MsgHandler());
	}

	class CustomMessageHandler extends AVIMMessageHandler {
		@Override
		public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

			NotificationInfo info = JSON.parseObject(message.getContent(), NotificationInfo.class);
			info.setMsgId(message.getMessageId());
			info.setTime(message.getTimestamp());
			info.setUnreadNum(SessionTable.querySessionByUnReadNum(conversation.getConversationId()) + 1);
			
			SessionInfo session = new SessionInfo(info, conversation, false);
			SessionTable.insertSession(session);
			if (ChatActivity.isShown && getCurrentSession().getSessionId().equals(info.getTo_id())) {
				SessionTable.updateSessionByUnReadNum(info.getTo_id());
			} else {
				if (!sharedUtils.readBoolean(getUserInfo().getAccount() + "_" + message.getConversationId(), false))
					normalSpecial(accuApplication, session);
			}

			EventBus.getDefault().post(new ChangeMessageEventBus());
		}
	}

	/*
	 * 文本消息 -1 图像消息 -2 音频消息 -3 视频消息 -4 位置消息 -5 文件消息 -6
	 */
	private class MsgHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

		@Override
		public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
			Object isSystem = conversation.getAttribute("sys");

			MessageInfo msg = new MessageInfo(accuApplication, message);
			if (isSystem != null && (Boolean) isSystem) {
				SystemMessageTable.insertMessage(msg);
				notifyAddNewFriend(msg.title, msg.newContent);
				
				String conversationId = conversation.getConversationId();
				SessionInfo session = new SessionInfo();
				session.setSessionId(conversation.getConversationId());
				session.setTitle(msg.title);
				session.setContent(msg.newContent);
				session.setMessageId(message.getMessageId());
				session.setTime(message.getTimestamp());
				session.setUnReadNum(SessionTable.querySessionByUnReadNum(conversationId) + 1);
				session.extend = 1;
				
				SessionTable.insertSession(session);
				
				EventBus.getDefault().post(new ChangeMessageEventBus());
				return;
			}

			DBManager dbManager = new DBManager(accuApplication);
			dbManager.insertFileMsg(msg);
			NotificationInfo info = JSON.parseObject(msg.getContent(), NotificationInfo.class);
			info.setMsgId(message.getMessageId());
			info.setTime(message.getTimestamp());

			String conversationId = conversation.getConversationId();
			info.setUnreadNum(SessionTable.querySessionByUnReadNum(conversationId) + 1);

			SessionInfo session = new SessionInfo(info, conversation, false);
			SessionTable.insertSession(session);

			if (ChatActivity.isShown && getCurrentSession().getSessionId().equals(info.getTo_id())) {
				SessionTable.updateSessionByUnReadNum(info.getTo_id());
			} else {
				if (!sharedUtils.readBoolean(getUserInfo().getAccount() + "_" + message.getConversationId(), false)) {
					normalSpecial(accuApplication, session);
				}
			}

			ChangeMessageEventBus messageEvent = new ChangeMessageEventBus(conversationId, msg);
			EventBus.getDefault().post(messageEvent);
		}

		@Override
		public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
			// 请加入你自己需要的逻辑...
			Log.d("z", "message.getMessageType():" + message.getMessageType());
		}
	}

	/**
	 * leanCloud断线重连
	 */
	public void leanCloudConnect() {
		String account = getUserInfo().getAccount();
		if (!TextUtils.isEmpty(account)) {
			AVIMClient imClient = AVIMClient.getInstance(account);
			imClient.open(null);
		}
	}

	/**
	 * leanCloud登录
	 * 
	 * @param userId
	 */
	public void leanCloudLogin(String userId) {
		final AVIMClient imClient = AVIMClient.getInstance(userId);
		imClient.open(new AVIMClientCallback() {
			@Override
			public void done(AVIMClient client, AVIMException e) {
				if (null != e) {
					if (ChatActivity.isShown) {
						showMsg("网络出问题啦，聊天功能暂不可用");
					}
					e.printStackTrace();
				} else {
					if (!sharedUtils.readBoolean(Keys.insertAllSession, false)) {
						LeanCloud.querySession();
					}
				}
			}
		});
	}

	public void initImageLoader(Context context) { // 用默认的,没有动画
		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(0)
				.showImageForEmptyUri(R.drawable.default_details_image).showImageOnFail(R.drawable.default_details_image)
				.cacheInMemory(false).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.defaultDisplayImageOptions(options).memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	private String getIMEI2() {
		try {
			String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
					+ Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10
					+ Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length()
					% 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
			return m_szDevIDShort;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getIMEI() {
		String imei = mTelephonyManager.getDeviceId();
		if ("000000000000000".equals(imei) || "".equals(imei)) {
			imei = getIMEI2();
		}
		return imei;
	}

	public void showMsg(String message) {
		if(!TextUtils.isEmpty(message))
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public void showMsg(String message, int duration) {
		if(!TextUtils.isEmpty(message))
			Toast.makeText(this, message, duration).show();
	}

	public void showMsg(int message) {
		Toast.makeText(this, getResources().getString(message), Toast.LENGTH_LONG).show();
	}

	public void init() {
		accuvallySQLiteOpenHelper = new AccuvallySQLiteOpenHelper(this);
		sharedUtils = new SharedUtils(this);
		mImageLoader = ImageLoader.getInstance();
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = tm.getDeviceId();
			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserInfo getUserInfo() {
		if (userInfo == null) {
			SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
			userInfo = new UserInfo();
			userInfo.setAccount(sp.getString("Account", ""));
			userInfo.setBrief(sp.getString("Brief", ""));
			userInfo.setCity(sp.getString("City", ""));
			userInfo.setCountry(sp.getString("Country", ""));
			userInfo.setEmail(sp.getString("Email", ""));
			userInfo.setEmailActivated(sp.getBoolean("EmailActivated", false));
			userInfo.setFollowEvents(sp.getInt("FollowEvents", 0));
			userInfo.setFollowOrgs(sp.getInt("FollowOrgs", 0));
			userInfo.setIsEventCreator(sp.getBoolean("IsEventCreator", false));
			userInfo.setLogo(sp.getString("Logo", ""));
			userInfo.setLogoLarge(sp.getString("LogoLarge", ""));
			userInfo.setMyPubEvents(sp.getInt("MyPubEvents", 0));
			userInfo.setMyRegEvents(sp.getInt("MyRegEvents", 0));
			userInfo.setOpenIdBaidu(sp.getString("OpenIdBaidu", ""));
			userInfo.setOpenIdQQ(sp.getString("OpenIdQQ", ""));
			userInfo.setOpenIdRenRen(sp.getString("OpenIdRenRen", ""));
			userInfo.setOpenIdWeibo(sp.getString("OpenIdWeibo", ""));
			userInfo.setPhone(sp.getString("Phone", ""));
			userInfo.setPhoneActivated(sp.getBoolean("PhoneActivated", false));
			userInfo.setProvince(sp.getString("Province", ""));
			userInfo.setRealName(sp.getString("RealName", ""));
			userInfo.setNick(sp.getString("Nick", ""));
			userInfo.setGender(sp.getInt("Gender", 0));
			userInfo.setStatus(sp.getInt("Status", 0));
			userInfo.setThirdLoginSucess(sp.getBoolean("ThirdLoginSucess", false));
			userInfo.setLoginType(sp.getInt("LoginType", 0));
		}
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		if (userInfo != null) {
			SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("Account", userInfo.getAccount());
			editor.putString("Brief", userInfo.getBrief());
			editor.putString("City", userInfo.getCity());
			editor.putString("Country", userInfo.getCountry());
			editor.putString("Email", userInfo.getEmail());
			editor.putBoolean("EmailActivated", userInfo.isEmailActivated());
			editor.putInt("FollowEvents", userInfo.getFollowEvents());
			editor.putInt("FollowOrgs", userInfo.getFollowOrgs());
			editor.putBoolean("IsEventCreator", userInfo.isIsEventCreator());
			editor.putString("Logo", userInfo.getLogo());
			editor.putString("LogoLarge", userInfo.getLogoLarge());
			editor.putInt("MyPubEvents", userInfo.getMyPubEvents());
			editor.putInt("MyRegEvents", userInfo.getMyRegEvents());
			editor.putString("Nick", userInfo.getNick());
			editor.putString("OpenIdBaidu", userInfo.getOpenIdBaidu());
			editor.putString("OpenIdQQ", userInfo.getOpenIdQQ());
			editor.putString("OpenIdRenRen", userInfo.getOpenIdRenRen());
			editor.putString("OpenIdWeibo", userInfo.getOpenIdWeibo());
			editor.putString("Phone", userInfo.getPhone());
			editor.putBoolean("PhoneActivated", userInfo.isPhoneActivated());
			editor.putString("Province", userInfo.getProvince());
			editor.putString("RealName", userInfo.getRealName());
			editor.putInt("Gender", userInfo.getGender());
			editor.putInt("Status", userInfo.getStatus());
			editor.putBoolean("ThirdLoginSucess", userInfo.isThirdLoginSucess());
			editor.putInt("LoginType", userInfo.getLoginType());
			editor.commit();
		}
		this.userInfo = userInfo;
	}

	/**
	 * 退出登录
	 */
	public void logout() {
		SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, Config.ACCUPASS_ID);
		sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, Config.ACCUPASS_KEY);

		sharedUtils.writeBoolean(Keys.insertAllSession, false);
		SessionTable.deleteAllUserSession();
		userInfo = null;
	}

	/**
	 * 验证是否登录
	 * 
	 * @return
	 */
	public boolean checkIsLogin() {
		return !TextUtils.isEmpty(getUserInfo().getAccount());
	}

	/**
	 * 添加到堆栈
	 * 
	 * @param activity
	 */
	public void addTask(Activity activity) {
		if (stack == null) {
			stack = new Stack<Activity>();
		}
		stack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = stack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = stack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			stack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : stack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	// 判断实例中是否有当前activity
	public boolean hasActivity(Class<?> cls) {
		for (Activity activity : stack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = stack.size(); i < size; i++) {
			if (null != stack.get(i)) {
				stack.get(i).finish();
			}
		}
		stack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			mImageLoader.clearMemoryCache();
		} catch (Exception e) {
		}
	}

	/**
	 * @param target_type : 0-活动，1-主办方，2-用户，3-专题
	 */
	public SaveBeHaviorInfo addBeHavior(int type, String target_type, String target_id, String tag, String keyword, String event_name, String event_data) {
		SaveBeHaviorInfo beHaviorInfo = new SaveBeHaviorInfo();
		if ("000000000000000".equals(getIMEI())) {
			beHaviorInfo.setTid(getIMEI2());
		} else {
			beHaviorInfo.setTid(("".equals(getIMEI()) ? getIMEI2() : getIMEI()));
		}
		if (checkIsLogin()) {
			beHaviorInfo.setUid(getUserInfo().getAccount());
		} else {
			beHaviorInfo.setUid("androidapp");
		}
		beHaviorInfo.setType(type + "");
		beHaviorInfo.setTarget_type(target_type);
		beHaviorInfo.setTarget_id(target_id);
		beHaviorInfo.setData_source("hdx");
		beHaviorInfo.setLocation(sharedUtils.readString("longitude") + "," + sharedUtils.readString("latitude"));
		beHaviorInfo.setCountry("");
		beHaviorInfo.setProvince(sharedUtils.readString("province") == null ? "" : sharedUtils.readString("province"));
		beHaviorInfo.setCity(sharedUtils.readString("cityName"));
		beHaviorInfo.setDate_time(TimeUtils.getUTCTimeStr());
		beHaviorInfo.setKey_word(keyword);
		beHaviorInfo.setDevice_type(2 + "");
		beHaviorInfo.setTag(tag);
		beHaviorInfo.setVersion(Util.getVersionName(accuApplication));
		beHaviorInfo.setEvent_name(event_name);
		beHaviorInfo.setEvent_data(event_data);
		return beHaviorInfo;
	}

	private void normalSpecial(Context mContext, SessionInfo sessionInfo) {
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(mContext);
		// 下面三个参数是必须要设定的
		notifyBuilder.setSmallIcon(R.drawable.icon);
		notifyBuilder.setContentTitle(sessionInfo.getTitle());
		if (sessionInfo.getIsNotification() == 1) {
			notifyBuilder.setContentText(sessionInfo.getContent());
		} else {
			switch (sessionInfo.getMessageType()) {
			case MessageInfo.TYPE_TEXT:
				if (!TextUtils.isEmpty(sessionInfo.getContent())) {
					// 屏蔽@自己的消息，因为个推会重新发送消息
					if (sessionInfo.getContent().contains("@" + getUserInfo().getNick()) || sessionInfo.getContent().contains("＠" + getUserInfo().getNick())) {
						return;
					}
				}
				notifyBuilder.setContentText(sessionInfo.getNickName() + ":" + sessionInfo.getContent());
				break;
			case MessageInfo.TYPE_PHOTO:
				notifyBuilder.setContentText(sessionInfo.getNickName() + ":[图片]");
				break;
			case MessageInfo.TYPE_RECORDER:
				notifyBuilder.setContentText(sessionInfo.getNickName() + ":[语音]");
				break;
			}
		}

		notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		notifyBuilder.setAutoCancel(true);

//		Intent notifyIntent = new Intent(mContext, MainActivityNew.class);
//		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent notifyIntent = new Intent(NotifyMessageReceiver.action);
		int requestCode = (int) SystemClock.uptimeMillis();
		PendingIntent pendIntent = PendingIntent.getBroadcast(mContext, requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		notifyBuilder.setContentIntent(pendIntent);
		notifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

		Notification notify = notifyBuilder.build();
		notify.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
//		notify.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_SHOW_LIGHTS | Notification.DEFAULT_SOUND
//				| Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(1, notify);
	}
	
	private void notifyAddNewFriend(String title, String content) {
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(accuApplication);
		notifyBuilder.setSmallIcon(R.drawable.icon);
		notifyBuilder.setContentTitle(title);
		notifyBuilder.setContentText(content);
		
		notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		notifyBuilder.setAutoCancel(true);
		
		Intent notifyIntent = new Intent(NotifyMessageReceiver.action);
		PendingIntent pendIntent = PendingIntent.getBroadcast(accuApplication, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notifyBuilder.setContentIntent(pendIntent);
		notifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
		Notification notify = notifyBuilder.build();
		notificationManager.notify(1, notify);
	}

	/*
	 * 设备描述 
	 */
	public String getDeviceDesc() {
		return Build.MODEL + "_" + Build.VERSION.RELEASE;
	}

	/*
	 *
	 * App版本
	 */
	public String getAppversion() {
		PackageInfo info;
		try {
			final PackageManager manager =  getPackageManager();
			info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
