package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.SearchHistoryAdapter;
import com.accuvally.hdtui.adapter.SearchTagAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.ui.ScrolListView;
import com.accuvally.hdtui.ui.ScrollGridView;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class SearchActivityNew extends BaseActivity implements OnClickListener, OnEditorActionListener {

	private LinearLayout lyHot;
	private LinearLayout lyLoading;
	private LinearLayout lyFailure;
	private Button SquareBtn;

	private int tag = 0;
	private ScrolListView mListView;
	private LinearLayout deleteHistory;

	private TextView tvDelete;
	private SearchHistoryAdapter mAdapter;

	private LinearLayout lyBack;
	private ImageView lySearch;
	private EditText searchContent;
	private int tvWidth;
	private LinearLayout ll_history_data;
	private String searchtag = "";
	private static final int HORIZONTAL_COUNT = 3;
	private ScrollGridView mGridView;
	private SearchTagAdapter searchTagAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search);
		EventBus.getDefault().register(this);
		initView();
		if (application.cacheUtils.getAsString("searchTag") == null) {
			initData();
		} else {
			lyLoading.setVisibility(View.GONE);
			List<String> list = JSON.parseArray(application.cacheUtils.getAsString("searchTag"), String.class);
			initTag(list);
		}
	}

	public void onEventMainThread(ChangeBackHomeEventBus eventBus) {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initHistoryData();
	}

	private void initHistoryData() {
		final List<String> list = dbManager.getSearchHistory();
		mAdapter = new SearchHistoryAdapter(mContext);
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);

		if (list.size() == 0) {
			ll_history_data.setVisibility(View.GONE);
		} else {
			ll_history_data.setVisibility(View.VISIBLE);
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				startActivity(new Intent(mContext, SearchResultActivity.class).putExtra("searchdata", list.get(arg2)).putExtra("flag", 0));
			}
		});
	}

	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("pi", "1"));
//		params.add(new BasicNameValuePair("ps", "15"));
		String location = application.sharedUtils.readString("longitude") + "," + application.sharedUtils.readString("latitude");
		params.add(new BasicNameValuePair("coordinates", location));
		params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));
//		params.add(new BasicNameValuePair("type", type));
		
		lyLoading.setVisibility(View.VISIBLE);
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_TAG, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				lyLoading.setVisibility(View.GONE);
				lyFailure.setVisibility(View.GONE);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						List<String> list = JSON.parseArray(messageInfo.getResult(), String.class);
						initTag(list);
						application.cacheUtils.put("searchTag", messageInfo.getResult());
					} else {
						application.showMsg(messageInfo.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					if (application.cacheUtils.getAsString("searchTag") == null) {
						application.showMsg(result.toString());
						lyHot.setVisibility(View.GONE);
						tag = 0;
					} else {
						final List<String> lists = JSON.parseArray(application.cacheUtils.getAsString("searchTag"), String.class);
						initTag(lists);
					}
					break;
				}
			}
		});
	}

	public void initTag(final List<String> list) {
		searchTagAdapter = new SearchTagAdapter(mContext, list);
		mGridView.setAdapter(searchTagAdapter);
	}

	private void initView() {
		lyHot = (LinearLayout) findViewById(R.id.lyHot);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		lySearch = (ImageView) findViewById(R.id.iv_search);
		lyBack = (LinearLayout) findViewById(R.id.ly_back);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);
		mListView = (ScrolListView) findViewById(R.id.listview);
		deleteHistory = (LinearLayout) findViewById(R.id.deleteHistory);
		searchContent = (EditText) findViewById(R.id.search_content);
		tvDelete = (TextView) findViewById(R.id.tvDelete);
		ll_history_data = (LinearLayout) findViewById(R.id.ll_history_data);
		mGridView = (ScrollGridView) findViewById(R.id.gridView);

		deleteHistory.setOnClickListener(this);
		tvDelete.setOnClickListener(this);
		SquareBtn.setOnClickListener(this);
		lyBack.setOnClickListener(this);
		lySearch.setOnClickListener(this);
		searchContent.setOnEditorActionListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SquareBtn:
			if (tag == 1) {// 广场
				EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
				finish();
			} else {// 重试
				lyFailure.setVisibility(View.GONE);
				initData();
			}
			break;
		case R.id.deleteHistory:
			dbManager.deleteSearchHisttory();
			initHistoryData();
			break;
		case R.id.tvDelete:
			dbManager.deleteSearchHisttory();
			initHistoryData();
			break;
		case R.id.ly_back:
			finish();
			break;
		case R.id.iv_search:
			searchtag = searchContent.getText().toString().trim();
			if (searchtag.length() < 1) {
				return;
			}
			dbManager.insertSearchHistory(searchtag);
			dbManager.insertSaveBeHavior(application.addBeHavior(80, 0+"", "", "", searchContent.getText().toString().trim(),"",""));
			startActivity(new Intent(mContext, SearchResultActivity.class).putExtra("searchdata", searchContent.getText().toString().trim()).putExtra("flag", 0));
			break;

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
			searchtag = searchContent.getText().toString().trim();
			if (searchtag.length() >= 1) {
				dbManager.insertSearchHistory(searchtag);
				dbManager.insertSaveBeHavior(application.addBeHavior(80, 0+"", "", "", searchContent.getText().toString().trim(),"",""));
				startActivity(new Intent(mContext, SearchResultActivity.class).putExtra("searchdata", searchContent.getText().toString().trim()).putExtra("flag", 0));
			}
		}
		return false;
	}
}
