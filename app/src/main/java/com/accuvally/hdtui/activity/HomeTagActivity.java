package com.accuvally.hdtui.activity;

import android.os.Bundle;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.SelAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HomeTagActivity extends BaseActivity implements IXListViewListener {

	String title;

	String tag;

	int pageIndex = 1, pageSize = 20;

	private XListView mListView;

	private SelAdapter mAdapter;

	private List<SelInfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_tag);
		title = getIntent().getStringExtra("titleName");
		tag = getIntent().getStringExtra("tag");
		initProgress();
		initView();
		if (application.cacheUtils.getAsString("home_act" + tag + "_" + application.sharedUtils.readString("cityName")) != null) {
			List<SelInfo> list = JSON.parseArray(application.cacheUtils.getAsString("home_act" + tag + "_" + application.sharedUtils.readString("cityName")), SelInfo.class);
			if (list.size() == 0 || list == null) {
				if (pageIndex == 1)
					application.showMsg("亲，这个城市还没有精选套餐喔~");
			} else {
				mAdapter.addAll(list);
			}
		} else {
			initData();
		}
	}

	public void initView() {
		setTitle(title);
		mListView = (XListView) findViewById(R.id.listview);

		mAdapter = new SelAdapter(mContext);
		list = new ArrayList<SelInfo>();
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
	}

	// TAG搜索
	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));
		params.add(new BasicNameValuePair("iscombo", "true"));
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()){
						List<SelInfo> list = JSON.parseArray(response.getResult(), SelInfo.class);
						if (pageIndex == 1 && list != null) {
							mAdapter.removeAll();
							application.cacheUtils.put("home_act" + tag + "_" + application.sharedUtils.readString("cityName"),response.getResult(), 1200);
						}
						if (list.size() == 0 || list == null) {
							if (pageIndex == 1)
								application.showMsg("亲，这个城市还没有精选套餐喔~");
						}
						mAdapter.addAll(list);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					if (pageIndex == 1 && application.cacheUtils.getAsString("home_act" + tag + "_" + application.sharedUtils.readString("cityName")) != null) {
						List<SelInfo> list2 = JSON.parseArray(application.cacheUtils.getAsString("home_act" + tag + "_" + application.sharedUtils.readString("cityName")), SelInfo.class);
						mAdapter.addAll(list2);
					}
					break;
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		initData();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		initData();
	}

}
