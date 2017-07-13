package com.accuvally.hdtui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by Andy Liu on 2017/7/6.
 */
public class SharePictureUtils {

    AccuApplication application;

    Context mContext;

    UMSocialService mController;


    public SharePictureUtils(Context mContext, UMSocialService mController) {
        this.mContext = mContext;
        application = (AccuApplication) mContext.getApplicationContext();
        this.mController = mController;
    }


    public void initConfig(Activity activity,Bitmap bitmap) {

        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        UMWXHandler wxHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);
        UMWXHandler circleHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);

        wxHandler.addToSocialSDK();
        circleHandler.setToCircle(true);
        circleHandler.addToSocialSDK();

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(circleMedia);

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareImage(new UMImage(mContext, bitmap));
        mController.setShareMedia(weixinContent);
    }


    public void share(SHARE_MEDIA share_MEDIA) {
        mController.postShare(mContext, share_MEDIA, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
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
