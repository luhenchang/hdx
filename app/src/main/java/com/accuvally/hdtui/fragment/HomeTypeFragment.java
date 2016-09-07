package com.accuvally.hdtui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.CalenderTypeActivity;
import com.accuvally.hdtui.adapter.HomeTypeAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsLoadEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class HomeTypeFragment extends BaseFragment implements IXListViewListener {

	private XListView mListView;

	private int type;

	private String tag;

	private int pageIndex = 1, pageSize = 20;

	private List<SelInfo> list;

	HomeTypeAdapter mAdapter;

	String cityName;

	private CustomDate mCustomDate;// 查询的时间

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_type, container, false);
		EventBus.getDefault().register(this);

		initView(view);
		if (application.cacheUtils.getAsString(cityName + "_" + type + "_" + tag) != null) {
			List<SelInfo> list = JSON.parseArray(application.cacheUtils.getAsString(cityName + "_" + type + "_" + tag), SelInfo.class);
			mAdapter.addAll(list);
		} else {
			initData();
		}

		return view;
	}

	public void initView(View view) {
		mListView = (XListView) view.findViewById(R.id.listview);

		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		Bundle b = getArguments();
		tag = b.getString("tag");
		type = b.getInt("type");

		list = new ArrayList<SelInfo>();
		mAdapter = new HomeTypeAdapter(mContext, type);
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		cityName = application.sharedUtils.readString("cityName");

		if (type == 4) {
			((LinearLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = (int) getActivity().getResources().getDimension(R.dimen.listview_bottomMargin);
		}
	}

	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 用最新的类别去做进一步查询,最新+日期联合查询
		if (type == 4) {
			params.add(new BasicNameValuePair("filter", 3 + ""));
			final CalenderTypeActivity act = (CalenderTypeActivity) getActivity();
			if (act.getCustomDate() != null) {
				mCustomDate = act.getCustomDate();
				params.add(new BasicNameValuePair("date", "t8"));
				params.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
				params.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
			}
		} else {
			params.add(new BasicNameValuePair("filter", type + ""));
		}

		if (type == 1) {
			String location = application.sharedUtils.readString("longitude") + "," + application.sharedUtils.readString("latitude");
			params.add(new BasicNameValuePair("coordinates", location));
		}
		Log.i("info", "tag:" + tag);
//		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("city", cityName));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		EventBus.getDefault().post(new ChangeDetailsLoadEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeDetailsLoadEventBus(false));
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					mListView.stopRefresh();
					mListView.stopLoadMore();
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()){
						List<SelInfo> list = JSON.parseArray(response.getResult(), SelInfo.class);
						Log.d("xxxx", mAdapter.getCount() + " " + "  pageIndex =" + pageIndex + "" + "  list.size()=" + list.size());
						if (pageIndex == 1 && list.size() != 0) {
							mAdapter.removeAll();
							// 如果是当日查询，不需要缓存 即type==4
							if (type != 4)
								application.cacheUtils.put(cityName + "_" + type + "_" + tag, response.getResult(), 1200);
						} else if (type == 4 && list.size() == 0) {
							if (pageIndex == 1) {
								mAdapter.removeAll();
								// application.showMsg("亲，没有查到你要求的数据",300);
							} else {
								application.showMsg("亲，没有更多数据", 300);
							}
						}
						mAdapter.addAll(list);
					}
			
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					mListView.stopRefresh();
					mListView.stopLoadMore();
					if (pageIndex == 1 && application.cacheUtils.getAsString(cityName + "_" + type + "_" + tag) != null) {
						list = JSON.parseArray(application.cacheUtils.getAsString(cityName + "_" + type + "_" + tag), SelInfo.class);
						mAdapter.addAll(list);
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

	// 点击日期做查询
	public void onEventMainThread(EventCustomDate bus) {
		if (bus.clazz == this.getClass()) {
			initData();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
}
