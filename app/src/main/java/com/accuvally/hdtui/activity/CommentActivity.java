package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.CommentAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.CommentInfo;
//import com.accuvally.hdtui.model.BaseResponse;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.ui.XListView.IXListViewListener;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.alibaba.fastjson.JSON;
//
///**
// * comment
// * 
// * @author Semmer Wang
// * 
// */
//public class CommentActivity extends BaseActivity implements IXListViewListener, OnClickListener {
//
//	private String id;
//
//	private int pageIndex = 1, pageSize = 10;
//
//	private XListView mListView;
//
//	CommentAdapter mAdapter;
//
//	private List<CommentInfo> list;
//
//	private EditText edCommContent;
//
//	private Button upCommBtn;
//
//	private int source;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_comment);
//		initView();
//		initData();
//		initComment(pageIndex);
//	}
//
//	public void initView() {
//		id = getIntent().getStringExtra("id");
//		source = getIntent().getIntExtra("source", 0);
//		setTitle(getResources().getString(R.string.comment_title));
//
//		mListView = (XListView) findViewById(R.id.listview);
//		edCommContent = (EditText) findViewById(R.id.edCommContent);
//		upCommBtn = (Button) findViewById(R.id.upCommBtn);
//
//		mListView.setXListViewListener(this);
//		upCommBtn.setOnClickListener(this);
//	}
//
//	public void initData() {
//		mAdapter = new CommentAdapter(mContext);
//		list = new ArrayList<CommentInfo>();
//		mAdapter.setList(list);
//		mListView.setAdapter(mAdapter);
//	}
//
//	public void initComment(final int pageIndex) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("type", "0"));
//		params.add(new BasicNameValuePair("target", id));
//		params.add(new BasicNameValuePair("pi", pageIndex + ""));
//		params.add(new BasicNameValuePair("ps", pageSize + ""));
//		params.add(new BasicNameValuePair("source", source + ""));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_COMMENT_LIST, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<CommentInfo> list = JSON.parseArray(result.toString(), CommentInfo.class);
//					if (pageIndex == 1 && list != null) {
//						mAdapter.removeAll();
//					}
//					// tvComSize.setText("讨论" + list.size());
//					mListView.stopLoadMore();
//					mListView.stopRefresh();
//					mAdapter.addAll(list);
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	public void uploadComment() {
//		String content = edCommContent.getText().toString().trim();
//		if (TextUtils.isEmpty(content)) {
//			application.showMsg("请输入评论内容");
//			return;
//		}
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", id));
//		params.add(new BasicNameValuePair("type", "0"));
//		params.add(new BasicNameValuePair("comment", content));
//		params.add(new BasicNameValuePair("replyid", "0"));
//		params.add(new BasicNameValuePair("source", source + ""));
//		showProgress("正在提交评论");
//		httpCilents.postA(Url.ACCUPASS_UPALOD_COMMENT, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CommentActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
//					if (info.isSuccess()) {
//						dbManager.insertSaveBeHavior(application.addBeHavior(70, 0+"", id, "","", "",""));
//						application.showMsg(info.getMsg());
//						edCommContent.setText("");
//						pageIndex = 1;
//						initComment(pageIndex);
//					} else {
//						application.showMsg(info.getMsg());
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onRefresh() {
//		pageIndex = 1;
//		initComment(pageIndex);
//	}
//
//	@Override
//	public void onLoadMore() {
//		pageIndex++;
//		initComment(pageIndex);
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.upCommBtn:
//			if (application.checkIsLogin()) {
//				if (NetworkUtils.isNetworkAvailable(mContext)) {
//					uploadComment();
//				} else
//					application.showMsg("请检查网络是否连接");
//			} else {
//				toActivity(LoginActivityNew.class);
//			}
//			break;
//		}
//	}
//}
