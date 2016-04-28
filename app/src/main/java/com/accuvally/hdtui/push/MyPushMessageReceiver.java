package com.accuvally.hdtui.push;//百度推送更换个推
//package com.accuvally.hdtui.push;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.SystemClock;
//import android.support.v4.app.NotificationCompat;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.accuvally.hdtui.AccuApplication;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.ResultInfo;
//import com.accuvally.hdtui.utils.HttpCilents;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//import com.baidu.frontia.api.FrontiaPushMessageReceiver;
//
///**
// * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
// * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
// * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
// * 
// * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
// * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
// * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
// * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
// * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
// * 
// * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
// * 
// */
//@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//@SuppressLint("NewApi")
//public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
//	/** TAG to Log */
//	public static final String TAG = MyPushMessageReceiver.class.getSimpleName();
//
//	AccuApplication application;
//
//	NotificationManager notificationManager;
//
//	/**
//	 * 调用PushManager.startWork后，sdk将对push
//	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
//	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
//	 * 
//	 * @param context
//	 *            BroadcastReceiver的执行Context
//	 * @param errorCode
//	 *            绑定接口返回值，0 - 成功
//	 * @param appid
//	 *            应用id。errorCode非0时为null
//	 * @param userId
//	 *            应用user id。errorCode非0时为null
//	 * @param channelId
//	 *            应用channel id。errorCode非0时为null
//	 * @param requestId
//	 *            向服务端发起的请求id。在追查问题时有用；
//	 * @return none
//	 */
//	@Override
//	public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
//		String responseString = "onBind errorCode=" + errorCode + " appid=" + appid + " userId=" + userId + " channelId=" + channelId + " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
//		if (errorCode == 0) {
//			updateBaiduId(context, userId, channelId);
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * 接收透传消息的函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param message
//	 *            推送的消息
//	 * @param customContentString
//	 *            自定义内容,为空或者json字符串
//	 */
//	@Override
//	public void onMessage(Context context, String message, String customContentString) {
//		String messageString = "透传消息 message=\"" + message + "\" customContentString=" + customContentString;
//		if (notificationManager == null)
//			notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		Log.d(TAG, messageString);
//		// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
//		if (!TextUtils.isEmpty(message)) {
//			JSONObject customJson = null;
//			try {
//				customJson = new JSONObject(message);
//				JSONObject obj = new JSONObject(customJson.getString("mykey"));
//				int msgId = obj.getInt("msgId");
//				int msgType = obj.getInt("msgType");
//				switch (msgType) {
//				case 1:// msgType==1(活动)
//					JSONObject msgObj = new JSONObject(obj.getString("msg"));
//					String id = msgObj.getString("id");
//					String title = msgObj.getString("title");
//					String content = msgObj.getString("content");
//					int site_from = msgObj.getInt("site_from");
//					getNotification(context, msgId, id, title, content, site_from);
//					break;
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, messageString);
//	}
//
//	/**
//	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param title
//	 *            推送的通知的标题
//	 * @param description
//	 *            推送的通知的描述
//	 * @param customContentString
//	 *            自定义内容，为空或者json字符串
//	 */
//	@Override
//	public void onNotificationClicked(Context context, String title, String description, String customContentString) {
//		String notifyString = "通知点击 title=\"" + title + "\" description=\"" + description + "\" customContent=" + customContentString;
//		Log.d(TAG, notifyString);
//
////		// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
////		if (!TextUtils.isEmpty(customContentString)) {
////			JSONObject customJson = null;
////			try {
////				customJson = new JSONObject(customContentString);
////				String myvalue = null;
////				if (customJson.isNull("mykey")) {
////					myvalue = customJson.getString("mykey");
////				}
////			} catch (JSONException e) {
////				e.printStackTrace();
////			}
////		}
////
////		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
////		updateContent(context, notifyString);
//	}
//
//	/**
//	 * setTags() 的回调函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
//	 * @param successTags
//	 *            设置成功的tag
//	 * @param failTags
//	 *            设置失败的tag
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onSetTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
//		String responseString = "onSetTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags=" + failTags + " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * delTags() 的回调函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
//	 * @param successTags
//	 *            成功删除的tag
//	 * @param failTags
//	 *            删除失败的tag
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onDelTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {
//		String responseString = "onDelTags errorCode=" + errorCode + " sucessTags=" + sucessTags + " failTags=" + failTags + " requestId=" + requestId;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * listTags() 的回调函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示列举tag成功；非0表示失败。
//	 * @param tags
//	 *            当前应用设置的所有tag。
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
//		String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
//		Log.d(TAG, responseString);
//
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	/**
//	 * PushManager.stopWork() 的回调函数。
//	 * 
//	 * @param context
//	 *            上下文
//	 * @param errorCode
//	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
//	 * @param requestId
//	 *            分配给对云推送的请求的id
//	 */
//	@Override
//	public void onUnbind(Context context, int errorCode, String requestId) {
//		String responseString = "onUnbind errorCode=" + errorCode + " requestId = " + requestId;
//		Log.d(TAG, responseString);
//
//		// 解绑定成功，设置未绑定flag，
//		if (errorCode == 0) {
//			Utils.setBind(context, false);
//		}
//		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//		updateContent(context, responseString);
//	}
//
//	private void updateContent(Context context, String content) {
//	}
//
//	private void normalSpecial(Context mContext, int msgId, String Id, String title, String content, int site_from) {
//		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(mContext);
//		// 下面三个参数是必须要设定的
//		notifyBuilder.setSmallIcon(R.drawable.icon);
//		notifyBuilder.setContentTitle(title);
//		notifyBuilder.setContentText(content);
//		notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
//		notifyBuilder.setAutoCancel(true);
//
//		Intent notifyIntent = new Intent(mContext, AccuvallyDetailsActivity.class);
//		if (site_from == 1) {
//			notifyIntent.putExtra("id", Id).putExtra("isHuodong", 0);
//		} else {
//			notifyIntent.putExtra("id", Id).putExtra("isHuodong", 1);
//		}
//		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//		int requestCode = (int) SystemClock.uptimeMillis();
//		PendingIntent pendIntent = PendingIntent.getActivity(mContext, requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		notifyBuilder.setContentIntent(pendIntent);
//		notifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
//
//		notificationManager.notify(msgId, notifyBuilder.build());
//	}
//
//	public void getNotification(Context mContext, int msgId, String Id, String title, String content, int site_from) {
//		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//			normalSpecial(mContext, msgId, Id, title, content, site_from);
//		} else {
//			getNotification2(mContext, msgId, Id, title, content, site_from);
//		}
//	}
//
//	@SuppressWarnings("deprecation")
//	private void getNotification2(Context mContext, int msgId, String Id, String title, String content, int site_from) {
//		Notification notification = new Notification();
//		notification.icon = R.drawable.icon;
//		notification.tickerText = title;
//		notification.when = System.currentTimeMillis();
//		Intent notifyIntent = new Intent(mContext, AccuvallyDetailsActivity.class);
//		if (site_from == 1) {
//			notifyIntent.putExtra("id", Id).putExtra("isHuodong", 0);
//		} else {
//			notifyIntent.putExtra("id", Id).putExtra("isHuodong", 1);
//		}
//		notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(mContext, title, content, pendingIntent);
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//		notification.defaults = Notification.DEFAULT_ALL;
//		notificationManager.notify(msgId, notification);
//	}
//
//	public void updateBaiduId(final Context context, final String userId, final String channelId) {
//		application = (AccuApplication) context.getApplicationContext();
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		if("000000000000000".equals(application.getIMEI())){
//			params.add(new BasicNameValuePair("DeviceID", application.getIMEI2()));
//		}else{
//			params.add(new BasicNameValuePair("DeviceID", ("".equals(application.getIMEI()) ? application.getIMEI2() : application.getIMEI())));
//		}
//		params.add(new BasicNameValuePair("BaiduUID", userId));
//		params.add(new BasicNameValuePair("baiduchannelid", channelId));
//		params.add(new BasicNameValuePair("app", "hdx"));
//		HttpCilents cilents = new HttpCilents(context);
//		cilents.postA(Url.ACCUPASS_UPDATEBAIDU, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					Log.i("info", "更新百度ID成功");
//					application.sharedUtils.writeString("BaiduUID", userId);
//					application.sharedUtils.writeString("baiduchannelid", channelId);
//					Log.i("info", "userId:" + userId + "-->>channelId:" + channelId);
//					ResultInfo messageInfo = JSON.parseObject(result.toString(), ResultInfo.class);
//					if (messageInfo.isRet()) {
//						Utils.setBind(context, true);
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//}
