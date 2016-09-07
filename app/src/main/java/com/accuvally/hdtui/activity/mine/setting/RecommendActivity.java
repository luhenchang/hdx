package com.accuvally.hdtui.activity.mine.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.RecommendAppAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.RecomendAppInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * recommend
 * 
 * @author Semmer Wang
 * 
 */
public class RecommendActivity extends BaseActivity {

	private ListView mListView;
	List<RecomendAppInfo> recommendAppList;
	private RecommendAppAdapter adapter;
	private LinearLayout lyLoading;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_recommend);
		initView();
		if (application.cacheUtils.getAsString("recommend") != null) {
			lyLoading.setVisibility(View.GONE);
			recommendAppList = JSON.parseArray(application.cacheUtils.getAsString("recommend"), RecomendAppInfo.class);
			adapter.addAll(recommendAppList);
		} else {
			initData();
		}
	}

	public void initView() {
		setTitle(getResources().getString(R.string.setting_recommend));
		mListView = (ListView) findViewById(R.id.listview);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		recommendAppList = new ArrayList<RecomendAppInfo>();
		adapter = new RecommendAppAdapter(this);
		adapter.setList(recommendAppList);
		mListView.setAdapter(adapter);
	}

	private void initData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		lyLoading.setVisibility(View.VISIBLE);
		httpCilents.get(httpCilents.printURL(Url.RECOMMEND_APP, list), new WebServiceCallBack() {
			public void callBack(int code, Object result) {
				lyLoading.setVisibility(View.GONE);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						application.cacheUtils.put("recommend", messageInfo.getResult());
						recommendAppList = JSON.parseArray(messageInfo.getResult(), RecomendAppInfo.class);
						adapter.addAll(recommendAppList);
					} else {
						application.showMsg(messageInfo.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
}
