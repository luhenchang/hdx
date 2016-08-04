package com.accuvally.hdtui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
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
//最新  热门  附近
public class HomeTypeActivity extends BaseActivity {

	private int pageIndex = 1, pageSize = 20;
	private int type;
	private String cityName;
	
	private XListView mListView;
	private CommonAccuAdapter mAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_type);
		initView();
		initProgress();
		initData();
	}

	public void initView() {
		cityName = application.sharedUtils.readString("cityName");
		type = getIntent().getIntExtra("type", 0);
		((LinearLayout) findViewById(R.id.share_ly)).setVisibility(View.GONE);
		switch (type) {
		case 1:
			setTitle("附近");
			break;
		case 2:
			setTitle("热门");
			break;
		case 3:
			setTitle("最新");
			break;
		}
		
		mListView = (XListView) findViewById(R.id.listview);
		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);
		
		mListView.setXListViewListener(new IXListViewListener() {
			
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
		});

	}

	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
//		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("filter", type + ""));
		params.add(new BasicNameValuePair("city", cityName));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
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
						if (pageIndex == 1 && list.size() != 0) {
							mAdapter.removeAll();
						}
						mAdapter.addAll(list);
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
