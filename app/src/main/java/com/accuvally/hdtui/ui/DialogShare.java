package com.accuvally.hdtui.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.ShareUtils.shareCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Utils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class DialogShare implements OnClickListener {
	
	private Context mContext;
	private UMSocialService mController;
	private ShareUtils shareUtils;
	
	private Dialog shareDialog;
	private String shareId;
	
	private String shareTitle;//分享标题
	private String startTime;//开始时间
	private String shareUrl;//分享地址 
	
	private boolean isHideShareMsg;
	private boolean isRobTicket;//如果是抢票活动要显示抢票的提示
	private String description;//抢票分享的提示信息
	private shareCallBack shareComplete;//分享成功监听
	
	public DialogShare(Context mContext, String shareId, String shareTitle, String startTime, String shareUrl) {
		this.mContext = mContext;
		this.shareId = shareId;
		this.shareTitle = shareTitle;
		this.startTime = startTime;
		this.shareUrl = shareUrl;
		
		mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
		shareUtils = new ShareUtils(mContext, mController, shareId);
	}

	public void showDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(mContext, R.style.DefaultDialog);
			shareDialog.setCancelable(true);
			shareDialog.setCanceledOnTouchOutside(true);
			shareDialog.setContentView(R.layout.dialog_share);
			TextView shareSina = (TextView) shareDialog.findViewById(R.id.tvShareSina);
			TextView shareQzone = (TextView) shareDialog.findViewById(R.id.tvShareQzone);
			TextView shareWx = (TextView) shareDialog.findViewById(R.id.tvSharewx);
			TextView shareWxpy = (TextView) shareDialog.findViewById(R.id.tvSharewxpy);
			TextView tvCancel = (TextView) shareDialog.findViewById(R.id.tvCancel);
			TextView tvShareMsg = (TextView) shareDialog.findViewById(R.id.tvShareMsg);
			LinearLayout lydismiss = (LinearLayout) shareDialog.findViewById(R.id.lydismiss);
			LinearLayout llRobTips = (LinearLayout) shareDialog.findViewById(R.id.llRobTips);// 分享的抢票提示

			if (isRobTicket) {// 是抢票活动
				llRobTips.setVisibility(View.VISIBLE);// 分享的抢票提示 ——显示
				tvShareMsg.setVisibility(View.INVISIBLE);// 短信分享不可见

				TextView tvTips = (TextView) shareDialog.findViewById(R.id.tvTips);
				tvTips.setText(description);

				if (shareComplete != null) {
					shareUtils.setShareSuccessListener(shareComplete);
				}
			} else {
				llRobTips.setVisibility(View.GONE);
			}

			if (isHideShareMsg) {
				tvShareMsg.setVisibility(View.INVISIBLE);
			} else {
				tvShareMsg.setVisibility(View.VISIBLE);
			}

			shareSina.setOnClickListener(this);
			shareQzone.setOnClickListener(this);
			shareWx.setOnClickListener(this);
			shareWxpy.setOnClickListener(this);
			tvCancel.setOnClickListener(this);
			lydismiss.setOnClickListener(this);
			tvShareMsg.setOnClickListener(this);
			DialogUtils.dialogSet(shareDialog, mContext, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
		}
		shareDialog.show();
	}

	@Override
	public void onClick(View v) {
		if (Utils.isFastDoubleClick()) return;
		
		switch (v.getId()) {
		case R.id.tvShareSina:// sina share 新浪微博分享
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				ToastUtil.showMsg(R.string.network_check);
				return;
			}
			shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvShareQzone:// Qzone share QQ空间分享
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				ToastUtil.showMsg(R.string.network_check);
				return;
			}
			shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewx:// weixin share 微信分享
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				ToastUtil.showMsg(R.string.network_check);
				return;
			}
			shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewxpy:// py share 朋友圈分享
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				ToastUtil.showMsg(R.string.network_check);
				return;
			}
			shareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE, 34, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvCancel:// dialog cancel
			shareDialog.dismiss();
			break;
		case R.id.lydismiss:
			shareDialog.dismiss();
			break;
		case R.id.tvShareMsg:
			Uri smsToUri = Uri.parse("smsto:");// 联系人地址
			Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			mIntent.putExtra("sms_body", "我在活动行APP上发现了一个好活动：" + shareTitle + "," + startTime + "，一起去参加吧？记得联系我。" + shareUrl);// 短信内容
			mContext.startActivity(mIntent);
			shareDialog.dismiss();
			break;
		}
	}
	
	public boolean isRobTicket() {
		return isRobTicket;
	}

	public void setRobTicket(boolean isRobTicket) {
		this.isRobTicket = isRobTicket;
	}

	public boolean isHideShareMsg() {
		return isHideShareMsg;
	}

	public void setHideShareMsg(boolean isHideShareMsg) {
		this.isHideShareMsg = isHideShareMsg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public shareCallBack getShareComplete() {
		return shareComplete;
	}

	public void setShareComplete(shareCallBack l) {
		this.shareComplete = l;
	}

	public void initConfig(Activity activity, String title, String content, String logoUrl, String loadingUrl) {
		shareUtils.initConfig(activity, title, content, logoUrl, loadingUrl);
	}

}
