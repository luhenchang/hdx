package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.UserInfoAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.ClearEditText;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.utils.CharacterParser;
import com.accuvally.hdtui.utils.PinyinComparator;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

/**
 * at界面
 * @author ivanlxg
 *
 */
public class AtActivity extends BaseActivity implements OnItemClickListener {

	private XListView mListView;
	private TextView tvNoData;
	private UserInfoAdapter mAdapter;
	private UserInfoAdapter mMatchAdapter;
	private ClearEditText mAutoTv;
	private List<UserInfo> mUserList;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_at);
		initProgress();
		initView();
		loadData();
	}

	private void initView() {
		mListView = (XListView) findViewById(R.id.listview);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		mAutoTv = (ClearEditText) findViewById(R.id.auto_tv);
		mAutoTv.setOnCancelIconClickListioner(new ClearEditText.OnCancelIconClickListioner() {

			@Override
			public void onCancelIconClick() {
				mListView.setAdapter(mAdapter);
			}
		});
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		mMatchAdapter = new UserInfoAdapter(this);
		setTitle("选择要提醒的人");
	}

	private void loadData() {
		mAdapter = new UserInfoAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mAutoTv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null) {
					filterData(s.toString());
				}
			}
		});

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String cid = getIntent().getStringExtra("sessionID");
		params.add(new BasicNameValuePair("cid", cid));
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_CIRCLE_MEMBER, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.setVisibility(View.VISIBLE);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject((String) result, BaseResponse.class);
					org.json.JSONObject jsonObject;
					try {
						jsonObject = new org.json.JSONObject(info.getResult());
						mUserList = JSON.parseArray(jsonObject.getString("member"), UserInfo.class);
						mAdapter.setList(mUserList);

					} catch (JSONException e) {
						e.printStackTrace();
						Log.d("e", e.getMessage());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg((String) result);
					break;
				}
			}
		});

		// mAdapter.setList(mUserList);
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		if (!TextUtils.isEmpty(filterStr)) {
			List<UserInfo> filterDateList = new ArrayList<UserInfo>();
			mMatchAdapter.removeAll();
			if (mUserList != null && mUserList.size() > 0) {
				for (UserInfo info : mUserList) {
					String name = info.getNick();
					if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
						filterDateList.add(info);
					}
				}
				mMatchAdapter.setList(filterDateList);
				if (mMatchAdapter.getCount() > 0) {
					mListView.setAdapter(mMatchAdapter);
				}
			}
			
			// 根据a-z进行排序
			// Collections.sort(filterDateList, pinyinComparator);
			// adapter.updateListView(filterDateList);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserInfo info = (UserInfo) parent.getAdapter().getItem(position);
		Intent intent = new Intent();
		Bundle extras = new Bundle();
		extras.putSerializable("UserInfo", info);
		intent.putExtras(extras);
		setResult(RESULT_OK, intent);
		finish();
	}
}
