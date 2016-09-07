package com.accuvally.hdtui.activity.message;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
//申请信息  添加同伴
public class ApplyFriendActivity extends BaseActivity {

	private String account;
	private EditText etReason;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_friend);
		
		account = getIntent().getStringExtra("account");
		
		etReason = (EditText) findViewById(R.id.etReason);
		findViewById(R.id.tvSend).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestAddUser();
			}
		});
	}

	private void requestAddUser() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("target", account));
		params.add(new BasicNameValuePair("reason", etReason.getText().toString()));

		showProgress("正在请求添加对方");
		httpCilents.postA(Url.social_apply, params, new WebServiceCallBack() {

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
					finish();
				}
			}
		});
	}

}
