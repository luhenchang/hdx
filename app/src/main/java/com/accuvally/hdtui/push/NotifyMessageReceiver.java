package com.accuvally.hdtui.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.accuvally.hdtui.activity.MainActivityNew;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;

import de.greenrobot.event.EventBus;

public class NotifyMessageReceiver extends BroadcastReceiver {

	public static final String action = "com.accuvally.hdtui.NotifyMessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//如果主页进入到后台，从通知启动AccuvallyDetailsActivity返回不到主页
//		AccuApplication application = (AccuApplication) context.getApplicationContext();
//		if (application.hasActivity(MainActivityNew.class)) {
//			Intent sIntent = new Intent(context, AccuvallyDetailsActivity.class);
//			sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//			sIntent.putExtra("id", intent.getStringExtra("Op_value"));
//			context.startActivity(sIntent);
//			return;
//		}
		
		
//		String value = intent.getStringExtra("Op_value");
//		if (!TextUtils.isEmpty(value)) {
//			startAct(context, intent);
//			return;
//		}

		Intent sIntent = new Intent(context, MainActivityNew.class);
		sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sIntent.putExtra("tabIndex", 2);
		context.startActivity(sIntent);
		EventBus.getDefault().post(new ChangeMainSelectEventBus(0));
	}
	
	
//	private void startAct(Context context, Intent intent) {
//		AccuApplication application = (AccuApplication) context.getApplicationContext();
//		Intent sIntent = new Intent();
//		sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		if (application.hasActivity(MainActivityNew.class)) {
//			sIntent.setClass(context, AccuvallyDetailsActivity.class);
//		} else {
//			sIntent.setClass(context, MainActivityNew.class);
//		}
//		
//		sIntent.putExtra("id", intent.getStringExtra("Op_value"));
//		context.startActivity(sIntent);
//	}

}
