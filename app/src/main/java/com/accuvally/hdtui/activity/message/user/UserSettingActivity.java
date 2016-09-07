package com.accuvally.hdtui.activity.message.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.eventbus.EventFollow;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
//资料设置
public class UserSettingActivity extends BaseActivity {

	private CheckBox cbBlacklist;
	private String account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);

		account = getIntent().getStringExtra("account");

		cbBlacklist = (CheckBox) findViewById(R.id.cbBlacklist);
		cbBlacklist.setChecked(application.sharedUtils.readBoolean(AccountManager.getAccount() + "_" + account, false));
		cbBlacklist.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				requestAddBlacklist(isChecked);
			}
		});

		if (getIntent().getBooleanExtra("isparter", false)) {
			View btUnfollow = findViewById(R.id.btUnfollow);
			btUnfollow.setVisibility(View.VISIBLE);
			btUnfollow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					requestUnFollow();
				}
			});
		}
		
		findViewById(R.id.linReport).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ReportActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
			}
		});

	}

	private void requestAddBlacklist(final boolean isChecked) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("target", account));

		String url = isChecked ? Url.blacklist_add : Url.blacklist_remove;

		String tips = isChecked ? "正在将对方添加至黑名单" : "正在移除黑名单";
		showProgress(tips);
		httpCilents.postA(url, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						application.sharedUtils.writeBoolean(AccountManager.getAccount() + "_" + account, isChecked);
					}
					ToastUtil.showMsg(response.msg);
				}
			}
		});
	}

	private void requestUnFollow() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("target", account));

		showProgress("正在删除对方");
		httpCilents.postA(Url.social_unfollow, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					ToastUtil.showMsg(response.msg);
					int position = getIntent().getIntExtra("position", -1);
					EventBus.getDefault().post(new EventFollow(position));
					finish();
				}
			}
		});
	}

}
