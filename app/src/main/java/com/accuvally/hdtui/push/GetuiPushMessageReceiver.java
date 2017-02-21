package com.accuvally.hdtui.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.activity.GeTuiWapActivity;
import com.accuvally.hdtui.activity.entry.MainActivityNew;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.ProjectDetailsActivity;
import com.accuvally.hdtui.activity.home.SponsorDetailActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.mine.TicketTabActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.GeTuiNotification;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.SharedUtils;
import com.accuvally.hdtui.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

//自定义的BroadcastReceiver与APPID绑定在一起。
//根据PushConsts.CMD_ACTION 进行不同的处理
//PushConsts.GET_MSG_DATA根据得到后台的数据中又细分为几个类：活动详情，专题页，票券（打开相应的activity）
public class GetuiPushMessageReceiver extends BroadcastReceiver {

    public static final String TAG="GetuiPushMessage";

    public static final String ToCommentActivity="ToCommentActivity";
    public static final String TOCommentDisplayActivity_comment="TOCommentDisplayActivity_comment";
    public static final String TOCommentDisplayActivity_consult="TOCommentDisplayActivity_consult";
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action") + "--" + bundle.getInt(PushConsts.CMD_ACTION));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
			if (payload != null) {
				String data = new String(payload);
//				Log.i("GetuiSdkDemo", "Got Payload:" + data);
				try {
					notification(context, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case PushConsts.GET_CLIENTID://不知道哪里触发的，但是个推初始化之后，就会调用这里，使个推id上传
			String cid = bundle.getString("clientid");
			Log.e("GetuiSdkDemo", "onReceive() clientid=" + cid);
			// Toast.makeText(context, "onReceive() clientid="+cid,
			// Toast.LENGTH_LONG).show();
			updateGeTuiID(context, cid);
			//setTag(context);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;
		case PushConsts.GET_SDKONLINESTATE:

			break;
		default:
			break;
		}
	}

	/**
	 * 设置个推tag
	 * 
	 * @param ctx
	 */
	public void setTag(Context ctx) {

		ArrayList<Tag> tags = new ArrayList<Tag>();

		PackageInfo info;

		try {
			SharedUtils sharedUtils = new SharedUtils(ctx);
			if (!TextUtils.isEmpty(sharedUtils.readString("cityName"))) {
				Tag tag = new Tag();
				tag.setName(sharedUtils.readString("cityName"));
				tags.add(tag);
			}
			
			final PackageManager manager = ctx.getPackageManager();
			info = manager.getPackageInfo(ctx.getPackageName(), 0);
			Tag tag = new Tag();
			tag.setName(info.versionName);
			tags.add(tag);
			 
			PushManager.getInstance().setTag(ctx, tags.toArray(new Tag[tags.size()]));
			 
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateGeTuiID(final Context context, final String gettuiID) {
		final AccuApplication application = (AccuApplication) context.getApplicationContext();

		String gettuiInshared = application.sharedUtils.readString("getuiid");
		// 成功上传过个推Id就不再上传
		if (null != gettuiInshared) {
			return;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		 
		params.add(new BasicNameValuePair("getuiid", gettuiID));
		params.add(new BasicNameValuePair("app", "hdx"));// 活动行APP
		HttpCilents cilents = new HttpCilents(context);
		cilents.postA(Url.ACCUPASS_UPDATE_GETUIID, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					Log.i("info", "更新个推      " + gettuiID + "  成功");
					application.sharedUtils.writeString("getuiid", gettuiID);
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						Utils.setBind(context, true);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
	
	private void notification(Context context, String data) {
		GeTuiNotification info = JSON.parseObject(data, GeTuiNotification.class);
		
		AccuApplication application = (AccuApplication) context.getApplicationContext();
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Log.e(TAG, "type="+info.getOp_type());
		switch (info.getOp_type()) {
		case 1:// 活动详情页 , op_value = 活动id
			if (application.hasActivity(MainActivityNew.class)) {
				intent.setClass(context, AccuvallyDetailsActivity.class);
			} else {
				intent.setClass(context, MainActivityNew.class);
			}
			intent.putExtra("id", info.getOp_value());
			context.startActivity(intent);
			break;
		case 2://专题页        op_value= 网址，使用内嵌浏览器打开
			intent.setClass(context, ProjectDetailsActivity.class);
			intent.putExtra("title", info.getTitle());
			intent.putExtra("id", info.getOp_value());
			context.startActivity(intent);
			break;
		case 3://圈子   ,  op_value = 活动id ，直接打开圈子，后台会处理用户是否可以加入圈子的问题
			SessionInfo session = SessionTable.querySessionById(info.getOp_value());
			if (session != null) {
				application.setCurrentSession(session);
				intent.setClass(context, ChatActivity.class);
				context.startActivity(intent);
			} else {
				application.showMsg("您还没有加入该圈子");
			}
			break;
			
		case 4://-主办方
			intent.setClass(context, SponsorDetailActivity.class);
			intent.putExtra("orgId", info.getOp_value());
			context.startActivity(intent);
			break;
		case 5://内部浏览器打开wap页    op_value= 网址，使用内嵌浏览器打开，可以用HTMl5推广页
			intent.setClass(context, GeTuiWapActivity.class);
			intent.putExtra("title", info.getTitle());
			intent.putExtra("url", info.getOp_value());
			context.startActivity(intent);
			break;
		case 6://我的票券
			intent.setClass(context, TicketTabActivity.class);
			context.startActivity(intent);
			break;
        case 9://邀请评价
//               intent.putExtra("id", info.getOp_value());
            String id=info.getOp_value();
//            Log.e(TAG,"id="+id);
//            Log.e(TAG,"**********");
            intent.setClass(context, AccuvallyDetailsActivity.class);
            intent.putExtra(GetuiPushMessageReceiver.ToCommentActivity, true);
            intent.putExtra("id", id);
            context.startActivity(intent);
            break;

            case 11://主办人回复了评价
                String id2=info.getOp_value();
                intent.setClass(context, AccuvallyDetailsActivity.class);
                intent.putExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment, true);
                intent.putExtra("id", id2);
                context.startActivity(intent);

                break;

            case 12://主办人回复了咨询
                String id3=info.getOp_value();
                intent.setClass(context, AccuvallyDetailsActivity.class);
                intent.putExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult, true);
                intent.putExtra("id", id3);
                context.startActivity(intent);
                break;

		}

//		int requestCode = (int) SystemClock.uptimeMillis();
//		PendingIntent pendIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context);
//		notifyBuilder.setContentIntent(pendIntent);
//		notifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
//		notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
//		notifyBuilder.setAutoCancel(true);
//		
//		notifyBuilder.setSmallIcon(R.drawable.icon);
//		notifyBuilder.setContentTitle(info.getTitle());
//		notifyBuilder.setContentText(info.content);
//		
//		Notification notify = notifyBuilder.build();
//		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(1, notify);
	}
}
