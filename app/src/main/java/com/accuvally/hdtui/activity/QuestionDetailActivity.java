package com.accuvally.hdtui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;

public class QuestionDetailActivity extends BaseActivity {

	private WebView webView;
	private String webData;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		webData = getIntent().getStringExtra("webData");
		title = getIntent().getStringExtra("title");
		initView();
	}

	private void initView() {
		((TextView) findViewById(R.id.tvQuestionTitle)).setText(title);

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		};
		findViewById(R.id.ivBack).setOnClickListener(onClickListener);
		findViewById(R.id.rlBottom).setOnClickListener(onClickListener);

		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
//		webSettings.setUseWideViewPort(false);
//		webSettings.setLoadWithOverviewMode(true);
//		webView.setWebViewClient(new WebViewClient());
//		webView.setWebChromeClient(new WebChromeClient());
		
		if (!TextUtils.isEmpty(webData)) {
			webView.loadData(webData, "text/html; charset=UTF-8", null);
		}
	}

}
