package com.accuvally.hdtui.activity;

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
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;

/**
 * Created by Andy Liu on 2017/3/27.
 */
public class WebActivity extends BaseActivity {

    private WebView webView;

    private String loadingUrl;

    private LinearLayout lyLoading;

    private String injectJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);
        initView();
        initData();
    }

    public void initView() {
        injectJs = getIntent().getStringExtra("injectJs");
        loadingUrl = getIntent().getStringExtra("loadingUrl");
        webView = (WebView) findViewById(R.id.details_webView);
        lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
        if (loadingUrl.indexOf("huodongxing.com/news") != -1){
            setTitle("专题详情");
        }
//        setTitle("活动详情");
        ((LinearLayout)findViewById(R.id.share_ly)).setVisibility(View.GONE);
    }

    public void initData() {
        webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
        webView.getSettings().setBuiltInZoomControls(true);// 是否开启缩放
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
            webView.loadUrl(loadingUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webView.setWebViewClient(new DetailsWebViewClient());
    }

    class DetailsWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (url.indexOf("huodongxing.com/event") != -1 || url.indexOf("huodongxing.com/go") != -1) {
                mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                        putExtra("id", url).putExtra("isHuodong", 0));
            }else {
                lyLoading.setVisibility(View.VISIBLE);
                view.loadUrl(url);
            }
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            lyLoading.setVisibility(View.GONE);
            if (injectJs!=null && (!"".equals(injectJs))) {
                webView.loadUrl("javascript:(function() { " + injectJs + "})();");
            }
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
