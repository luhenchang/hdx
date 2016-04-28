package com.accuvally.hdtui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.model.SponsorDetailBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

public class SponsorDetailFragment extends BaseFragment {
	private int pageIndex = 1, pageSize = 10;
	private String orgId;
	
	private XListView mListView;
	private CommonAccuAdapter mAdapter;
	
	private SponsorDetailBean bean;
	private List<SelInfo> listData;
	private boolean isFuture;

	public static SponsorDetailFragment newInstance(String orgId, SponsorDetailBean bean, boolean isFuture) {
		SponsorDetailFragment f = new SponsorDetailFragment();
		Bundle b = new Bundle();
		b.putString("orgId", orgId);
		b.putBoolean("isFuture", isFuture);
		b.putSerializable("bean", bean);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			orgId = bundle.getString("orgId");
			isFuture = bundle.getBoolean("isFuture");
			bean = (SponsorDetailBean) bundle.getSerializable("bean");
			if (isFuture) {
				listData = bean.futures;
			} else {
				listData = bean.olds;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_future, container, false);
		setListView(rootView);
		return rootView;
	}

	private void setListView(View rootView) {
		mListView = (XListView) rootView.findViewById(R.id.id_stickynavlayout_innerscrollview);
		mAdapter = new CommonAccuAdapter(mContext);
		mAdapter.setList(listData);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getListData();
			}

			@Override
			public void onLoadMore() {
				pageIndex++;
				getListData();
			}
		});
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", orgId));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));

		String url = isFuture ? Url.ACCUPASS_FUTUREACTIVITY : Url.ACCUPASS_OLDACTIVITY;
		httpCilents.get(httpCilents.printURL(url, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				mListView.stopRefresh();
				mListView.stopLoadMore();

				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()) {
						List<SelInfo> list = JSON.parseArray(response.result, SelInfo.class);
						if (list == null) return;
						if (pageIndex == 1) {
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
