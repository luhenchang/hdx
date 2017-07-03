package com.accuvally.hdtui.activity;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.R.id;
import com.accuvally.hdtui.R.layout;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GeTuiWapActivity extends BaseActivity {
	
	private WebView webView;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_getui_wap);
		initView();
	}

	private void initView() {
		url = getIntent().getStringExtra("url");
		String title = getIntent().getStringExtra("title");
		setTitle(title);
		
		webView = (WebView) findViewById(id.webView);
		webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
		webView.getSettings().setSupportZoom(true);

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);
	}
	
//	public void setWebview() {
//		webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
//		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
//		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
//		webView.getSettings().setSupportZoom(true);
//		webView.loadUrl(url);
//		webView.loadDataWithBaseURL("about:blank", project.getNews().getContent(), "text/html", "utf-8", null);
//		webView.setWebViewClient(new DetailsWebViewClient());
//	}
	
	class WebClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

}
