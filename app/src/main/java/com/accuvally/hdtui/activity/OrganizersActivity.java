package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.res.ColorStateList;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.OrganizerAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.HomeOrgsInfo;
//import com.accuvally.hdtui.model.OrgTagInfo;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.ui.XListView.IXListViewListener;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.alibaba.fastjson.JSON;
//
///**
// * 活动主办方
// * 
// * @author Semmer Wang
// * 
// */
//public class OrganizersActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnCheckedChangeListener {
//
//	private XListView mListView;
//
//	private OrganizerAdapter mAdapter;
//
//	private List<HomeOrgsInfo> list;
//
//	private RadioGroup radioGroup;
//
//	private LinearLayout lyLoading;
//
//	private int pageIndex = 1, pageSize = 10;
//
//	private String tag = "";
//
//	private TextView tvNoData;
//
//	private Button SquareBtn;
//
//	private ImageView ivFailure;
//
//	private LinearLayout lyFailure;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_organizers);
//		initView();
//		lyLoading.setVisibility(View.VISIBLE);
//		initData();
//	}
//
//	public void initView() {
//		setTitle(R.string.organizers_title);
//
//		mListView = (XListView) findViewById(R.id.listview);
//		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
//		tvNoData = (TextView) findViewById(R.id.tvNoData);
//		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
//		ivFailure = (ImageView) findViewById(R.id.ivFailure);
//		SquareBtn = (Button) findViewById(R.id.SquareBtn);
//
//		SquareBtn.setOnClickListener(this);
//		radioGroup.setOnCheckedChangeListener(this);
//		mListView.setXListViewListener(this);
//
//		mAdapter = new OrganizerAdapter(mContext);
//		list = new ArrayList<HomeOrgsInfo>();
//		mAdapter.setList(list);
//		mListView.setAdapter(mAdapter);
//		// setViewTouchListener(findViewById(R.id.relativelayout));
//	}
//
//	/** 获得Tag列表 **/
//	public void initData() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_ORGANIZER, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				lyLoading.setVisibility(View.GONE);
//				lyFailure.setVisibility(View.GONE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					OrgTagInfo info = JSON.parseObject(result.toString(), OrgTagInfo.class);
//					if (info.getTags().size() == 0) {
//						lyFailure.setVisibility(View.VISIBLE);
//					} else {
//						for (int i = 0; i < info.getTags().size(); i++) {
//							RadioButton radioButton = new RadioButton(mContext);
//							radioButton.setText(info.getTags().get(i));
//							ColorStateList csl = getResources().getColorStateList(R.drawable.selector_radiobutton_textcolor_bg_btn);
//							radioButton.setTextColor(csl);
//							radioButton.setBackgroundResource(R.drawable.selector_radiobutton_bg_btn);
//							radioButton.setTextSize(16);
//							radioButton.setPadding(40, 35, 40, 35);
//							radioButton.setTag(i);
//							radioButton.setId(i);
//							radioButton.setGravity(Gravity.CENTER);
//							radioButton.setButtonDrawable(android.R.color.transparent);
//							radioGroup.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//							if (i == 0) {
//								radioButton.setChecked(true);
//							}
//						}
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					mListView.setVisibility(View.GONE);
//					lyFailure.setVisibility(View.VISIBLE);
//					ivFailure.setBackgroundResource(R.drawable.loading_no_wifi_bg);
//					tvNoData.setText(getResources().getString(R.string.loading_no_wifi));
//					SquareBtn.setText("点击重试");
//					break;
//				}
//			}
//		});
//	}
//
//	/** 根据Tag查找主办方列表 **/
//	public void getOrganizerById(String tag) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("tag", tag));
//		params.add(new BasicNameValuePair("pi", pageIndex + ""));
//		params.add(new BasicNameValuePair("ps", pageSize + ""));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_ORGANIZER_TAG, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				mListView.setVisibility(View.VISIBLE);
//				lyLoading.setVisibility(View.GONE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<HomeOrgsInfo> list = JSON.parseArray(result.toString(), HomeOrgsInfo.class);
//					if (pageIndex == 1 && list != null) {
//						mAdapter.removeAll();
//					}
//					mListView.stopRefresh();
//					mListView.stopLoadMore();
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
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.SquareBtn:
//			lyFailure.setVisibility(View.GONE);
//			pageIndex = 1;
//			initData();
//			break;
//		}
//	}
//
//	@Override
//	public void onRefresh() {
//		pageIndex = 1;
//		getOrganizerById(tag);
//	}
//
//	@Override
//	public void onLoadMore() {
//		pageIndex++;
//		getOrganizerById(tag);
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup arg0, int arg1) {
//		RadioButton radioButton = (RadioButton) findViewById(arg1);
//		tag = radioButton.getText().toString();
//		lyLoading.setVisibility(View.VISIBLE);
//		pageIndex = 1;
//		getOrganizerById(tag);
//	}
//}
