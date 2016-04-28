package com.accuvally.hdtui.fragment.old;
//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Bundle;
//import android.util.Log;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.HotActivitiesAdapter;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.ProjectDetails;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.alibaba.fastjson.JSON;
//
//public class TopicWebDetailActivity extends BaseActivity {
//
//	private String title;
//	private String id;
//	private WebView webView;
//	protected ProjectDetails project;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_topic_webdetail);
//		id = getIntent().getStringExtra("id");
//		title = getIntent().getStringExtra("title");
//		setTitle(title);
//		initProgress();
//		initWebview();
//	}
//
//	public void initWebview() {
//		webView = (WebView) findViewById(R.id.webView);
//		webView.getSettings().setJavaScriptEnabled(true);// 是否开启JAVASCRIPT
//		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
//		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
//		webView.getSettings().setSupportZoom(true);
//
//		webView.loadDataWithBaseURL("about:blank", project.getNews().getContent(), "text/html", "utf-8", null);
//
//		webView.setWebViewClient(new DetailsWebViewClient());
//	}
//
//	private void initData() {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("id", id));
//		httpCilents.get(httpCilents.printURL(Url.NEWS, list), new WebServiceCallBack() {
//			public void callBack(int code, Object result) {
//				goneFailure();
//				if (code == 1) {
//					project = JSON.parseObject(result.toString(), ProjectDetails.class);
//					if (project != null) {
//						initWebview();
//					} else {
//						showFailure();
//					}
//				} else {
//					application.showMsg(result.toString());
//					showWifi();
//				}
//			}
//		});
//	}
//
//	class DetailsWebViewClient extends WebViewClient {
//
//		@Override
//		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//			return super.shouldInterceptRequest(view, url);
//		}
//
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			Log.i("info", url);
//			if (url.indexOf("huodongxing.com/event") != -1 || url.indexOf("huodongxing.com/go") != -1) {
//				mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", url).putExtra("isHuodong",
//						0));
//			} else {
//				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
//			}
//			return true;
//		}
//
//		public void onPageFinished(WebView view, String url) {
//
//		}
//
//		@Override
//		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//			application.showMsg("网页加载出错,请稍后再试!");
//		}
//
//		@Override
//		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			handler.proceed(); // 接受所有网站的证书
//		}
//	}
//
//}
