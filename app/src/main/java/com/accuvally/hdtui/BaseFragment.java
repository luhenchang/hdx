package com.accuvally.hdtui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SaveBeHaviorInfo;
import com.accuvally.hdtui.ui.MyProgressDialog;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment {

	protected Context mContext;

	protected AccuApplication application;

	protected HttpCilents httpCilents;

	protected MyProgressDialog myProgressDialog;

	protected DBManager dbManager;

	public TextView tvNoData;

	public Button SquareBtn;

	private ImageView ivFailure;

	private LinearLayout lyFailure;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		httpCilents = new HttpCilents(mContext);
		dbManager = new DBManager(mContext);
		application = (AccuApplication) getActivity().getApplication();
		init();
	}

	public void setViewFailure(View viewgroup) {
		tvNoData = (TextView) viewgroup.findViewById(R.id.tvNoData);
		lyFailure = (LinearLayout) viewgroup.findViewById(R.id.lyFailure);
		ivFailure = (ImageView) viewgroup.findViewById(R.id.ivFailure);
		SquareBtn = (Button) viewgroup.findViewById(R.id.SquareBtn);
	}

	public void showFailure() {
		lyFailure.setVisibility(View.VISIBLE);
		ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
		tvNoData.setText(getResources().getString(R.string.loading_no_data));
		SquareBtn.setText(R.string.search_guangchang);
	}

	public void showWifi() {
		lyFailure.setVisibility(View.VISIBLE);
		ivFailure.setBackgroundResource(R.drawable.loading_no_wifi_bg);
		tvNoData.setText(getResources().getString(R.string.loading_no_wifi));
		SquareBtn.setText("点击重试");
	}

	public void goneFailure() {
		lyFailure.setVisibility(View.GONE);
	}

	public interface OnClickCallBack {
		public void callBack(boolean is);
	}

	public void showFailureOnClick(final OnClickCallBack onClickCallBack) {

		SquareBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (SquareBtn.getText().toString().equals(getResources().getString(R.string.search_guangchang))) {
					onClickCallBack.callBack(true);
				} else {
					onClickCallBack.callBack(false);
				}
			}
		});
	}

	/** 初始化自定义对话框 */
	private void init() {
		myProgressDialog = new MyProgressDialog(mContext);
		myProgressDialog.setMyCancelable(true);
		myProgressDialog.setMyTouchOutside(false);
	}

	/**
	 * @param message
	 */
	public void showProgress(String message) {
		myProgressDialog.setMyMessage(message);
		myProgressDialog.myShow();
	}

	public void showDefaultProgress() {
		myProgressDialog.setMyMessage("数据加载中，请稍候！");
		myProgressDialog.myShow();
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		myProgressDialog.setMyMessage(message);
	}

	public void dismissProgress() {
		if (myProgressDialog != null) {
			myProgressDialog.mydismiss();
		}
	}

	public void onResume() {
		// Log.i("info", getClass().getSimpleName());
		super.onResume();

		MobclickAgent.onPageStart(getClass().getSimpleName()); // 统计页面
		MobclickAgent.onResume(getActivity()); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
		MobclickAgent.onPause(getActivity());
	}

	public void toActivity(Class<?> activity) {
		Intent intent = new Intent(mContext, activity);
		startActivity(intent);
	}

	public void toActivity(Intent intent) {
		startActivity(intent);
	}

	// 注册设备
	public void regdevice(String longitude, String latitude) {
		// 判断设备是否已注册成功，如果注册成功后就不再调用此接口
		if (application.sharedUtils.readBoolean("isRegDevice")) {
			return;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));
		
		params.add(new BasicNameValuePair("bauduid", ""));
		params.add(new BasicNameValuePair("baiduchannelid", ""));
		
		params.add(new BasicNameValuePair("name", ""));
		params.add(new BasicNameValuePair("phone", ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("channel", Utils.getChannel(mContext)));
		params.add(new BasicNameValuePair("coordinate", longitude + "," + latitude));
		
		httpCilents.postA(Url.ACCUPASS_REGDEVICE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						application.sharedUtils.writeBoolean("isRegDevice", true);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	// 记录用户行为数据
	public void svaeBeHavior(List<SaveBeHaviorInfo> info) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("", info.toString()));
        Trace.e("behaviour",info.toString());
		httpCilents.postA(Url.SAVE_BEHAVIOR_INFO, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						dbManager.deleteSaveBeHavior();
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
	
	protected void addRequest(String url) {
		onRequestStart();
		httpCilents.get(url, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					onRequestSuccess(result.toString());
					break;

				case Config.RESULT_CODE_ERROR:
					onRequestFailure(result.toString());
					break;
				}
			}
		});
	}

	protected void addPostRequest(String url, List<NameValuePair> params) {
		onRequestStart();
		httpCilents.postA(url, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				onResponseStart();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					onRequestSuccess(result.toString());
					break;

				case Config.RESULT_CODE_ERROR:
					onRequestFailure(result.toString());
					break;
				}
			}
		});
	}

	protected void onRequestSuccess(String result) {

	}

	protected void onRequestFailure(String result) {
		application.showMsg(result.toString());
	}
	
	protected void onRequestStart() {
	}
	
	/**
	 * 请求响应后的通用处理
	 */
	protected void onResponseStart() {
		
	}

}
