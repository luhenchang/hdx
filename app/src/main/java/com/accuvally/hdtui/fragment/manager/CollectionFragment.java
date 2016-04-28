package com.accuvally.hdtui.fragment.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.LoginActivityNew;
import com.accuvally.hdtui.adapter.CollectionAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.fragment.ManagerFragment;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCollection;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

/*
 * 收藏活动，已收藏
 */
public class CollectionFragment extends BaseFragment {

	private XListView mListView;
	private int pageIndex = 1, pageSize = 10;
	private CollectionAdapter mAdapter;

	private View emptyView;
	protected List<SelInfo> listData = new ArrayList<SelInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_collection, container, false);
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, LoginActivityNew.class);
				startActivity(intent);
			}
		};
		view.findViewById(R.id.btLogin).setOnClickListener(onClickListener);

		emptyView = view.findViewById(R.id.manageEmpty);
		mListView = (XListView) view.findViewById(R.id.lvCollect);
		mAdapter = new CollectionAdapter(mContext);
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

		String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_collection");
		if (!TextUtils.isEmpty(cache)) {
			List<SelInfo> listCache = JSON.parseArray(cache, SelInfo.class);
			if (listCache != null) {
				mAdapter.addAll(listCache);
				ManagerFragmentHelp.putAll(CollectionFragment.class, listCache);
			}
		}

		showStateView();
		getListData();
		return view;
	}

	public void onEventMainThread(ChangeUserStateEventBus stateEventBus) {
		showStateView();
		if (stateEventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
			pageIndex = 1;
			mAdapter.removeAll();
			getListData();
		}
	}

	public void onEventMainThread(EventCollection eventCollection) {
		pageIndex = 1;
		getListData();
	}

	// 点击日期做查询
	private CustomDate mCustomDate;// 查询的时间

	public void onEventMainThread(EventCustomDate bus) {
		if(bus.clazz== getClass())
		getListData();
	}

	public void onEventMainThread(EventHideFooterView event) {
		if (event.clazz == this.getClass()) {
			ManagerFragment parentF = (ManagerFragment) getParentFragment();
			if(parentF!=null){
				((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin=parentF.getListViewBottomMargin();
			}
		}
	}

	private void showStateView() {
		if (application.checkIsLogin()) {
			emptyView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		} else {
			emptyView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		// params.add(new BasicNameValuePair("regOnly", "false"));
		boolean onlyUnexpired = application.sharedUtils.readBoolean("onlyunexpired", true);
		params.add(new BasicNameValuePair("excludeExpired", String.valueOf(onlyUnexpired)));// 仅显示未过期

		ManagerFragment parentF = (ManagerFragment) getParentFragment();
		if(parentF!=null){
			((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin=parentF.getListViewBottomMargin();
		}
		if (parentF.getCustomDate() != null) {
			mCustomDate = parentF.getCustomDate();
			params.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
			params.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
			
		}

		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYLIKED, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				mListView.stopRefresh();
				mListView.stopLoadMore();

				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					List<SelInfo> list = JSON.parseArray(result.toString(), SelInfo.class);
					if (list == null)
						return;
					if (pageIndex == 1) {
						mAdapter.removeAll();
					}
					mAdapter.addAll(list);
					//
					ManagerFragmentHelp.putAll(CollectionFragment.class, list);
					// 缓存
					if (pageIndex == 1 && list != null && list.size() != 0) {
						application.cacheUtils.put(application.getUserInfo().getAccount() + "_collection", result.toString());
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
