package com.accuvally.hdtui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.adapter.SelDialogAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class SelectFragment extends BaseFragment implements IXListViewListener, OnClickListener {

	private XListView mListView;

	private LinearLayout lySelTime, lySelTag, lySelType;

	private ImageView ivOrg;

	String[] sel_price = new String[] { "全价位", "免费", "收费" };

	private String time = "", type = "", price = "";

	private TextView tvSelTime, tvSelType, tvSelPrice;

	private PopupWindow popupWindow;
	private View mPopupWindowView;
	private int dialogTag;
	List<String> listTime = new ArrayList<String>();
	List<String> listPrice = new ArrayList<String>();
	List<String> listType = new ArrayList<String>();
	private int pageIndex = 1, pageSize = 20;
	private List<SelInfo> list;
	private CommonAccuAdapter mAdapter;
	String cityName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_select, container, false);
		EventBus.getDefault().register(this);
		initView(rootView);
		initData();
		return rootView;
	}

	public void initView(View view) {
		mListView = (XListView) view.findViewById(R.id.listview);
		lySelTime = (LinearLayout) view.findViewById(R.id.lySelTime);
		lySelTag = (LinearLayout) view.findViewById(R.id.lySelTag);
		lySelType = (LinearLayout) view.findViewById(R.id.lySelType);
		ivOrg = (ImageView) view.findViewById(R.id.ivOrg);
		tvSelTime = (TextView) view.findViewById(R.id.tvSelTime);
		tvSelType = (TextView) view.findViewById(R.id.tvSelType);
		tvSelPrice = (TextView) view.findViewById(R.id.tvSelPrice);

		mListView.setXListViewListener(this);
		lySelTime.setOnClickListener(this);
		lySelTag.setOnClickListener(this);
		lySelType.setOnClickListener(this);
		ivOrg.setOnClickListener(this);
		initTag();
		list = new ArrayList<SelInfo>();
		mAdapter = new CommonAccuAdapter(mContext);
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		mListView.setAutoLoadMore(true);

		cityName = application.sharedUtils.readString("cityName");
		if (application.cacheUtils.getAsString(cityName + "_select") != null) {
			list = JSON.parseArray(application.cacheUtils.getAsString(cityName + "_select"), SelInfo.class);
			mAdapter.addAll(list);
		}
	}

	public void onEventMainThread(ChangeCityEventBus eventBus) {
		pageIndex = 1;
		cityName = eventBus.getCity();
		initData();
	}

	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", type));// 类型
		params.add(new BasicNameValuePair("city", cityName));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("date", time));// 时间
		params.add(new BasicNameValuePair("free", price));// 免费
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					mListView.stopRefresh();
					mListView.stopLoadMore();
					
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						list = JSON.parseArray(response.result, SelInfo.class);
						if (pageIndex == 1 && list.size() != 0) {
							mAdapter.removeAll();
							application.cacheUtils.put(cityName + "_select", response.getResult());
							mAdapter.addAll(list);
							mListView.setSelection(1);
						} else if (list.size() == 0) {
							if (pageIndex == 1) {
								mAdapter.removeAll();
								application.showMsg("亲，没有查到你要求的数据");
							} else {
								application.showMsg("亲，没有更多数据");
							}
						} else {
							// 这种状态不太明确
							mAdapter.addAll(list);
						}
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					mListView.stopRefresh();
					mListView.stopLoadMore();
					if (pageIndex == 1) {
						mAdapter.removeAll();
					}
					if (pageIndex == 1 && application.cacheUtils.getAsString(cityName + "_select") != null) {
						list = JSON.parseArray(application.cacheUtils.getAsString(cityName + "_select"), SelInfo.class);
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lySelTime:
			dialogTag = 1;
			selectionTab(0);
			break;
		case R.id.lySelTag:
			dialogTag = 0;
			selectionTab(1);
			break;
		case R.id.lySelType:
			dialogTag = 2;
			selectionTab(2);
			break;
		case R.id.ivOrg:// 主办方
			break;
		}
	}

	public void selectionTab(int tag) {
		initPopupWindow();
		switch (tag) {
		case 0:
			tvSelTime.setTextColor(getResources().getColor(R.color.txt_green));
			if (!popupWindow.isShowing()) {
				popupWindow.showAsDropDown(lySelTime, mListView.getLayoutParams().width, 0);
			} else {
				popupWindow.dismiss();
			}
			break;
		case 1:
			tvSelType.setTextColor(getResources().getColor(R.color.txt_green));
			if (!popupWindow.isShowing()) {
				popupWindow.showAsDropDown(lySelTag, mListView.getLayoutParams().width, 0);
			} else {
				popupWindow.dismiss();
			}
			break;
		case 2:
			tvSelPrice.setTextColor(getResources().getColor(R.color.txt_green));
			if (!popupWindow.isShowing()) {
				popupWindow.showAsDropDown(lySelType, mListView.getLayoutParams().width, 0);
			} else {
				popupWindow.dismiss();
			}
			break;
		}
	}

	private void initPopupWindow() {
		initPopupWindowView();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeigh = dm.heightPixels;
		if (dialogTag == 0)
			popupWindow = new PopupWindow(mPopupWindowView, LayoutParams.MATCH_PARENT, screenHeigh / 2);
		else
			popupWindow = new PopupWindow(mPopupWindowView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.dialogWindowAnim);
		Drawable drawable = getResources().getDrawable(android.R.color.white);
		popupWindow.setBackgroundDrawable(drawable);
		popupWindow.update();
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				tvSelType.setTextColor(getResources().getColor(R.color.gary_content));
				tvSelPrice.setTextColor(getResources().getColor(R.color.gary_content));
				tvSelTime.setTextColor(getResources().getColor(R.color.gary_content));
				popupWindow.dismiss();
			}
		});
	}

	private void initPopupWindowView() {
		mPopupWindowView = LayoutInflater.from(mContext).inflate(R.layout.dialog_sel, null);
		ListView mListView = (ListView) mPopupWindowView.findViewById(R.id.listview);
		SelDialogAdapter adapter = null;
		switch (dialogTag) {
		case 0:
			if (listType != null)
				listType.clear();
			if (application.cacheUtils.getAsString("selTag") != null) {
				listType = JSON.parseArray(application.cacheUtils.getAsString("selTag"), String.class);
			} else {
				String[] type = getResources().getStringArray(R.array.sel_type);
				for (int i = 0; i < type.length; i++) {
					listType.add(type[i]);
				}
			}
			adapter = new SelDialogAdapter(mContext, listType);
			break;
		case 1:
			if (listTime != null)
				listTime.clear();
			String[] time = getResources().getStringArray(R.array.sel_time);
			for (int i = 0; i < time.length; i++) {
				listTime.add(time[i]);
			}
			adapter = new SelDialogAdapter(mContext, listTime);
			break;
		case 2:
			if (listPrice != null)
				listPrice.clear();
			for (int i = 0; i < sel_price.length; i++) {
				listPrice.add(sel_price[i]);
			}
			adapter = new SelDialogAdapter(mContext, listPrice);
			break;
		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (dialogTag) {
				case 0:
					type = listType.get(arg2);
					tvSelType.setText(type);
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_SEARCH_CATEGORY", type));
					break;
				case 1:
					if (listTime.get(arg2).equals("全时段")) {
						time = "";
					} else if (listTime.get(arg2).equals("今天")) {
						time = "t2";
					} else if (listTime.get(arg2).equals("明天")) {
						time = "t3";
					} else if (listTime.get(arg2).equals("本周")) {
						time = "t6";
					} else if (listTime.get(arg2).equals("本周末")) {
						time = "t4";
					} else if (listTime.get(arg2).equals("本月")) {
						time = "t7";
					}
					tvSelTime.setText(listTime.get(arg2));
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_SEARCH_PERIOD", listTime.get(arg2)));
					break;
				case 2:
					if (listPrice.get(arg2).equals("免费")) {
						price = "1";
					} else if (listPrice.get(arg2).equals("收费")) {
						price = "0";
					} else if (listPrice.get(arg2).equals("全价位")) {
						price = "";
					}
					tvSelPrice.setText(listPrice.get(arg2));
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_SEARCH_PRICE", listPrice.get(arg2)));
					break;
				}
				pageIndex = 1;
				initData();
				popupWindow.dismiss();
			}
		});
	}

	/** 获得Tag列表 **/
	public void initTag() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String location = application.sharedUtils.readString("longitude") + "," + application.sharedUtils.readString("latitude");
		params.add(new BasicNameValuePair("coordinates", location));
		params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));
		params.add(new BasicNameValuePair("type", type));
		
		
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_TAG, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msgInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msgInfo.isSuccess()) {
						application.cacheUtils.put("selTag", msgInfo.getResult());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
