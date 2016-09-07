package com.accuvally.hdtui.activity.home.util;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchResultActivity extends BaseActivity implements OnClickListener, IXListViewListener {
	private int pageIndex = 1, pageSize = 20;
	private XListView mListView;

	private CommonAdapter mAdapter;

	private TextView tvNoData;

	private Button SquareBtn;

	private ImageView ivFailure;

	private LinearLayout lyFailure;

	private int tag = 0;

	private int searchflag;

	private String searchtag;

	private LinearLayout lyLoading;

	private int temp;

	protected int returnDataLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		searchtag = getIntent().getStringExtra("searchdata");
		searchflag = getIntent().getIntExtra("flag", 0);
		setContentView(R.layout.activity_search);
		initView();
	}

	public void initView() {
		if (searchflag == 1)
			setTitle(searchtag);
		else
			setTitle(R.string.search_title);
		((LinearLayout) findViewById(R.id.share_ly)).setVisibility(View.GONE);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);
		mListView = (XListView) findViewById(R.id.listview);

		SquareBtn.setOnClickListener(this);
		mListView.setXListViewListener(this);
		SquareBtn.setOnClickListener(this);
		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);
		temp = getIntent().getIntExtra("temp", 0);
		search(searchflag, searchtag);
		if (temp == 2) {
			searchtag = getIntent().getStringExtra("searchText");
			showProgress("正在努力搜索中");
			pageIndex = 1;
			search(1, searchtag);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.SquareBtn:// 去广场页
			if (tag == 1) {// 广场
				EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
				finish();
			} else {// 重试
				pageIndex = 1;
				search(0, searchtag);
			}
			break;
		}
	}

	// TAG搜索
	public void search(int flag, String qs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (flag == 1)
			params.add(new BasicNameValuePair("tag", qs));
		else
			params.add(new BasicNameValuePair("key", qs));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));
//		String latitude = application.sharedUtils.readString("longitude") + "," + application.sharedUtils.readString("latitude");
//		params.add(new BasicNameValuePair("coordinates", latitude));
		lyLoading.setVisibility(View.VISIBLE);
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				lyLoading.setVisibility(View.GONE);
				lyFailure.setVisibility(View.GONE);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					
					mListView.stopRefresh();
					mListView.stopLoadMore();
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()){
						List<SelInfo> list = JSON.parseArray(response.getResult(), SelInfo.class);
						if (pageIndex == 1 && list != null) {
							mAdapter.removeAll();
						}
						if (pageIndex == 1 && list.size() == 0) {
							mListView.setVisibility(View.GONE);
							lyFailure.setVisibility(View.VISIBLE);
							ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
							tvNoData.setText(getResources().getString(R.string.loading_no_data));
							SquareBtn.setText(R.string.search_guangchang);
							tag = 1;
						}
						returnDataLength = list.size();
						mAdapter.addAll(list);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					if (pageIndex == 1) {
						mListView.setVisibility(View.GONE);
						lyFailure.setVisibility(View.VISIBLE);
						ivFailure.setBackgroundResource(R.drawable.loading_no_wifi_bg);
						tvNoData.setText(getResources().getString(R.string.loading_no_wifi));
						SquareBtn.setText("点击重试");
						tag = 0;
					}
					break;
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		if (mAdapter.getCount() < pageSize) {
			mListView.stopRefresh();
			return;
		}
		pageIndex = 1;
		search(searchflag, searchtag);
	}

	@Override
	public void onLoadMore() {
		if (returnDataLength < pageSize) {
			mListView.stopLoadMore();
			return;
		}
		pageIndex++;
		search(searchflag, searchtag);
	}

}
