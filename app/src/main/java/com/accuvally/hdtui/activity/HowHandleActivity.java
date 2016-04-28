package com.accuvally.hdtui.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;

public class HowHandleActivity extends BaseActivity {

	private String urlHow = "http://bbs.huodongxing.com/forum.php?mod=viewthread&tid=324&extra=page%3D2";
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how_handle);
		initProgress();
		setTitle("如何办理退票");
		initView();
	}

	private void initView() {
		WebView webView = (WebView) findViewById(R.id.webView);
		startProgress();
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (isFirst) {
					stopProgress();
					isFirst = !isFirst;
				}
			}
		});
		webView.loadUrl(urlHow);
	}

}
