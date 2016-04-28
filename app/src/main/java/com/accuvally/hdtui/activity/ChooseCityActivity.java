package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Constant;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class ChooseCityActivity extends BaseActivity {

	
	private List<String> hotCityList;
	private GridView gridView;
	private ArrayAdapter mAdapter;
	private TextView tvLocationCity;
	private int tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_city);

		setTitle(R.string.personal_update_city);
		initProgress();

		String cityList = application.cacheUtils.getAsString(Keys.cityList);
		if (!TextUtils.isEmpty(cityList)) {
			hotCityList = JSON.parseArray(cityList, String.class);
		}else {
			hotCityList = new ArrayList<String>(Arrays.asList(Constant.hotCityName));
		}
		initView();
		requestData();
	}

	private void initView() {
		final String cityNameText = application.sharedUtils.readString(Keys.cityName);
		if (TextUtils.isEmpty(cityNameText) || !hasInCityList(cityNameText)) {
			findViewById(R.id.linLocation).setVisibility(View.GONE);
		} else {
			TextView tvCityTitle = (TextView) findViewById(R.id.tvCityTitle);
			tvCityTitle.setText("当前选择的城市");
			tvLocationCity = (TextView) findViewById(R.id.locationCityName);
			tvLocationCity.setText(cityNameText);
			tvLocationCity.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					chooseCurrentCity(cityNameText);
				}
			});
		}

		gridView = (GridView) findViewById(R.id.gridView);
		mAdapter = new ArrayAdapter<String>(mContext, R.layout.griditem_city, R.id.tvCityName, hotCityList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
				switch (position % 3) {
				case 0:
					view.setGravity(Gravity.LEFT);
					break;
				case 1:
					view.setGravity(Gravity.CENTER);
					break;
				case 2:
					view.setGravity(Gravity.RIGHT);
					break;
				}
				return view;
			}
		};
		gridView.setAdapter(mAdapter);
		
		tag = getIntent().getIntExtra("tag", 0);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String cityNameText = hotCityList.get(position);
				if (tag == 2) {
					update(cityNameText);
				} else {
					chooseCurrentCity(cityNameText);
				}
			}

		});
	}
	
	private void chooseCurrentCity(String cityNameText) {
		EventBus.getDefault().post(new ChangeCityEventBus(cityNameText));
		application.sharedUtils.writeString(Keys.cityName, cityNameText);
		finish();
	}
	
	/** 获取城市列表 **/
	private void requestData() {
		startProgress();
		httpCilents.get(Url.GET_HOT_CITYS, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<String> list = JSON.parseArray(response.result, String.class);
						mAdapter.clear();
						mAdapter.addAll(list);
						mAdapter.notifyDataSetChanged();
						application.cacheUtils.put(Keys.cityList, response.result);
					} else {
						application.showMsg(response.msg);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}

		});
	}
	
	private boolean hasInCityList(String city) {
		for (int i = 0; i < hotCityList.size(); i++) {
			if (city.equals(hotCityList.get(i)))
				return true;
		}
		return false;
	}
	
	public void update(final String cityName) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("City", cityName));

		showProgress("正在修改资料");
		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					UserInfo user = application.getUserInfo();
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						application.showMsg(messageInfo.getMsg());
						user.setCity(cityName);
						application.setUserInfo(user);
						finish();
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
