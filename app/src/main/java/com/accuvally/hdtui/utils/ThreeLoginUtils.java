package com.accuvally.hdtui.utils;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.utils.eventbus.ChangeThreeLoginEventBus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import de.greenrobot.event.EventBus;

public class ThreeLoginUtils {

	public Context mContext;

	AccuApplication application;

	UMSocialService mController;

	public ThreeLoginUtils(Context mContext, UMSocialService mController) {
		this.mContext = mContext;
		application = (AccuApplication) mContext.getApplicationContext();
		this.mController = mController;
	}

	public void initLogin(Activity activity) {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, Config.QZONE_APPID, Config.QZONE_APPKEY);
		qqSsoHandler.addToSocialSDK();
		UMWXHandler wxHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);
		wxHandler.addToSocialSDK();
		wxHandler.setRefreshTokenAvailable(false);
	}

	public void authVerify(final SHARE_MEDIA share_MEDIA, Activity activity) {
		mController.doOauthVerify(activity, share_MEDIA, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA arg0) {
				application.showMsg("授权开始");
			}

			@Override
			public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
				application.showMsg("授权错误,请检查网络");
			}

			@Override
			public void onComplete(final Bundle arg0, SHARE_MEDIA platform) {
				application.showMsg("授权成功");
				if(platform.equals(SHARE_MEDIA.SINA)){
					getDate(platform, "");
				}else{
					getDate(platform, arg0.getString("openid").toString());
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA arg0) {
				application.showMsg("授权取消");
			}
		});
	}

	public void getDate(SHARE_MEDIA platform, final String openid) {
		mController.getPlatformInfo(mContext, platform, new UMDataListener() {
			@Override
			public void onStart() {
				application.showMsg("获取平台数据开始...");
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				EventBus.getDefault().post(new ChangeThreeLoginEventBus(status, info, openid));
			}
		});
	}

	public void loginOut(SHARE_MEDIA media) {
		mController.deleteOauth(mContext, media, new SocializeClientListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(int status, SocializeEntity entity) {
				if (status == 200) {
					application.showMsg("删除成功.");
				} else {
					application.showMsg("删除失败");
				}
			}
		});

	}
	
//	if (status == 200 && info != null) {
//		StringBuilder sb = new StringBuilder();
//		Set<String> keys = info.keySet();
//		for (String key : keys) {
//			sb.append(key + "=" + info.get(key).toString() + "\r\n");
//		}
//		Log.i("info", sb.toString());
//	} else {
//		Log.i("info", "发生错误：" + status);
//	}
}
