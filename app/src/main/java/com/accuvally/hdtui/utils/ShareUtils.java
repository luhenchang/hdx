package com.accuvally.hdtui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.db.DBManager;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareUtils {

	AccuApplication application;

	Context mContext;

	UMSocialService mController;

	DBManager dbManager;

	String accuId;

	public ShareUtils(Context mContext, UMSocialService mController, String accuId) {
		this.mContext = mContext;
		application = (AccuApplication) mContext.getApplicationContext();
		this.mController = mController;
		dbManager = new DBManager(mContext);
		this.accuId = accuId;
	}

	public void initConfig(Activity activity, String title, String content,
                           String logoUrl, String loadingUrl) {

		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(activity,
                Config.QZONE_APPID, Config.QZONE_APPKEY));
		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL);

		UMWXHandler wxHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);
		UMWXHandler circleHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);

		wxHandler.addToSocialSDK();
		circleHandler.setToCircle(true);
		circleHandler.addToSocialSDK();
		UMImage urlImage;
		if ("".equals(logoUrl)) {
			urlImage = new UMImage(mContext, R.drawable.icon);
		} else
			urlImage = new UMImage(mContext, logoUrl);
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("".equals(content) || content == null ? title : content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(loadingUrl);
		weixinContent.setShareImage(urlImage);
		mController.setShareMedia(weixinContent);

		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("".equals(content) || content == null ? title : content);
		circleMedia.setTitle(title);
		circleMedia.setShareImage(urlImage);
		circleMedia.setTargetUrl(loadingUrl);
		mController.setShareMedia(circleMedia);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("".equals(content) || content == null ? title : content);
		qzone.setTargetUrl(loadingUrl);
		qzone.setTitle(title);
		qzone.setShareImage(urlImage);
		mController.setShareMedia(qzone);
		String contents = "《" + title + "》" + content + "." + loadingUrl;
		if (contents.length() > 140) {
			if (content.length() > 100)
				contents = "《" + title + "》" + content.substring(100) + "." + loadingUrl;
		}
		mController.setShareContent(contents);
		mController.setShareMedia(urlImage);
	}

	public void shareAfterOauth(final SHARE_MEDIA share_MEDIA,
                                final int shareType, final int target_type) {
		if (OauthHelper.isAuthenticated(mContext, share_MEDIA)) {
			share(share_MEDIA, shareType, target_type);
		} else {
			mController.doOauthVerify(mContext, share_MEDIA, new UMAuthListener() {

				@Override
				public void onStart(SHARE_MEDIA arg0) {

				}

				@Override
				public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
					application.showMsg("授权错误");
				}

				@Override
				public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
					application.showMsg("授权成功");
					share(share_MEDIA, shareType, target_type);
				}

				@Override
				public void onCancel(SHARE_MEDIA arg0) {
					application.showMsg("授权取消");
				}
			});
		}
	}

	public void share(SHARE_MEDIA share_MEDIA, final int shareType, final int target_type) {
		mController.postShare(mContext, share_MEDIA, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
				dbManager.insertSaveBeHavior(application.addBeHavior(shareType, target_type + "",
                        accuId, "", "", "", ""));
				if (sharecCallBack != null) {
					sharecCallBack.shareSuccess();
				}
			}
		});
	}
	
	private shareCallBack sharecCallBack; 
	public shareCallBack shareCallBack() {
		return sharecCallBack;
	}

	public void setShareSuccessListener(shareCallBack shBack) {
		this.sharecCallBack = shBack;
	}

	public interface shareCallBack {
		public void shareSuccess();
	}

}
