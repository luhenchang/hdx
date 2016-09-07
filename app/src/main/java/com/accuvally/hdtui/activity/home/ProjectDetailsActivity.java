package com.accuvally.hdtui.activity.home;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.model.ProjectDetailsChild;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import de.greenrobot.event.EventBus;

/**
 * 专题精选详情
 * 
 * @author Vanness
 * 
 */
public class ProjectDetailsActivity extends BaseActivity implements OnClickListener {
	private String id;
	private List<HomeEventInfo> lists;
	private LinearLayout lyLoading;
	private WebView webView;
	private UMSocialService mController;
	private Dialog shareDialog;
	ProjectDetailsChild project;
	String notice1;
	ShareUtils shareUtils;
	private LinearLayout share_ly;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_details);
		initView();
		initData();
	}

	public void initView() {
		id = getIntent().getStringExtra("id");
		String title = getIntent().getStringExtra("title");
		setTitle(title);
		setViewFailure();
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		webView = (WebView) findViewById(R.id.webView);
		share_ly = (LinearLayout) findViewById(R.id.share_ly);
		share_ly.setVisibility(View.VISIBLE);

		showFailureOnClick(new OnClickCallBack() {
			public void callBack(boolean is) {
				if (is) {
					EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
					finish();
				} else {
					initData();
				}
			}
		});
		share_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (project != null) {
					MobclickAgent.onEvent(mContext, "click_features_share_count");
					dialogShare();
				}
			}
		});
		mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
		shareUtils = new ShareUtils(mContext, mController, id);
	}

	private void initData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("id", id));
		httpCilents.get(httpCilents.printURL(Url.NEWS, list), new WebServiceCallBack() {
			public void callBack(int code, Object result) {
				goneFailure();
				lyLoading.setVisibility(8);
				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						project = JSON.parseObject(response.result, ProjectDetailsChild.class);
						if (project != null) {
							initWebview();
						} else {
							showFailure();
						}
					}
					
//					String content = Html.fromHtml(project.getNews().getContent()).toString();
					shareUtils.initConfig(ProjectDetailsActivity.this, project.getTitle(), "", project.getLogo(), project.getUrl());
				} else {
					application.showMsg(result.toString());
					showWifi();
				}
			}
		});
	}

	public void initWebview() {
		webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
		webView.getSettings().setSupportZoom(true);

		webView.loadDataWithBaseURL("about:blank", project.getContent(), "text/html", "utf-8", null);

		webView.setWebViewClient(new DetailsWebViewClient());
	}

	class DetailsWebViewClient extends WebViewClient {

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return super.shouldInterceptRequest(view, url);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("info", url);
			if (url.indexOf("huodongxing.com/event") != -1 || url.indexOf("huodongxing.com/go") != -1) {
				mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", url).putExtra("isHuodong", 0));
			} else {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
			}
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			lyLoading.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			application.showMsg("网页加载出错,请稍后再试!");
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed(); // 接受所有网站的证书
		}
	}

	public void dialogShare() {
		shareDialog = new Dialog(mContext, R.style.dialog);
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

		shareSina.setOnClickListener(this);
		shareQzone.setOnClickListener(this);
		shareWx.setOnClickListener(this);
		shareWxpy.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		lydismiss.setOnClickListener(this);
		tvShareMsg.setOnClickListener(this);
		DialogUtils.dialogSet(shareDialog, mContext, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
		shareDialog.show();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvShareSina:// sina share
			shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvShareQzone:// Qzone share
			shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewx:// weixin share
			shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewxpy:// py share
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
			String smsContent = "我在活动行APP上发现了一个好活动：" + project.getTitle() + "," + project.getPublishDate() + "，一起去参加吧？记得联系我。" + project.getUrl();
			mIntent.putExtra("sms_body", smsContent);// 短信内容
			startActivity(mIntent);
			shareDialog.dismiss();
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		} else {
			finish();
		}
		return false;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
