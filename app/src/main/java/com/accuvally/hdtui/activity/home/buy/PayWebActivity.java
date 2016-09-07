package com.accuvally.hdtui.activity.home.buy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
//网页支付
public class PayWebActivity extends BaseActivity {

	private String payUrl;

	private WebView webView;

	private LinearLayout lyLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_web);
		initView();
		initWebView();
	}

	public void initView() {
		payUrl = getIntent().getStringExtra("payUrl");
		setTitle("付款");
		
		webView = (WebView) findViewById(R.id.pay_webView);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
	}

	public void initWebView() {
		webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		String userAgent = webView.getSettings().getUserAgentString() + "/HDX-APP";
		webView.getSettings().setUserAgentString(userAgent);
		webView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				if (url != null && url.startsWith("http://"))
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		});
		try {
			webView.loadUrl(payUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		webView.setWebViewClient(new DetailsWebViewClient());
	}

	class DetailsWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			lyLoading.setVisibility(View.VISIBLE);
			view.loadUrl(url);
			return true;
		}

		public void onPageFinished(WebView view, String url) {
			lyLoading.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			application.showMsg("网页加载出错,请稍后再试!");
			lyLoading.setVisibility(View.GONE);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		webView.destroy();
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
}
