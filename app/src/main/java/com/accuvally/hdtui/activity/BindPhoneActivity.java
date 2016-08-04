package com.accuvally.hdtui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.MyCount;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定手机号码  邮箱
 * 
 * @author Semmer Wang
 * 
 */
public class BindPhoneActivity extends BaseActivity implements OnClickListener {

	private int TAG;

	private TextView tvPrompts;

	private ImageView ivIconPhone;

	private EditText edBindPhone, edBindCode;

	private Button bindBtn;
	TextView sendBindCodeTv;
	MyCount mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_phone);
		initView();
	}

	public void initView() {
		tvPrompts = (TextView) findViewById(R.id.tvPrompts);
		ivIconPhone = (ImageView) findViewById(R.id.ivIconPhone);
		edBindPhone = (EditText) findViewById(R.id.edBindPhone);
		edBindCode = (EditText) findViewById(R.id.edBindCode);
		sendBindCodeTv = (TextView) findViewById(R.id.sendBindCodeTv);
		bindBtn = (Button) findViewById(R.id.bindBtn);

		((LinearLayout) findViewById(R.id.share_ly)).setVisibility(View.GONE);
		TAG = getIntent().getIntExtra("TAG", 0);
		switch (TAG) {
		case 1:
			setTitle(R.string.personal_bind_phone_title);
			tvPrompts.setText(R.string.personal_bind_promt1);
			ivIconPhone.setBackgroundResource(R.drawable.update_pass_icon_phone_bg);
			break;
		case 2:
			setTitle(R.string.personal_bind_email_title);
			tvPrompts.setText(R.string.personal_bind_promt2);
			edBindPhone.setHint(getResources().getString(R.string.personal_update_password_email_hint));
			ivIconPhone.setBackgroundResource(R.drawable.update_pass_icon_email_bg);
			break;
		}
		sendBindCodeTv.setOnClickListener(this);
		bindBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sendBindCodeTv:// 发送验证码
			String phone = edBindPhone.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				if (TAG == 1)
					application.showMsg("请输入手机号");
				else
					application.showMsg("请输入邮箱");
				return;
			}
			if (TAG == 1) {// phone
				if (CheckTextBox.isPhoneNumberValid(phone)) {
					sendCode(1, phone);
				} else {
					application.showMsg("请输入正确的手机号");
				}
			} else {// email
				if (CheckTextBox.isEmail(phone)) {
					sendCode(0, phone);
				} else {
					application.showMsg("请输入正确的邮箱");
				}
			}
			break;
		case R.id.bindBtn:// 绑定
			String phoneNum = edBindPhone.getText().toString().trim();
			String code = edBindCode.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNum)) {
				if (TAG == 1)
					application.showMsg("请输入手机号");
				else
					application.showMsg("请输入邮箱");
				return;
			}
			if (TextUtils.isEmpty(phoneNum)) {
				if (TAG == 1)
					application.showMsg("请输入手机号");
				else
					application.showMsg("请输入邮箱");
				return;
			}
			if (TextUtils.isEmpty(code)) {
				application.showMsg("请输入验证码");
				return;
			}
			if (TAG == 1)
				checkCode(1, phoneNum);
			else
				checkCode(0, phoneNum);
			break;
		}
	}

	// 获取验证码
	/**
	 * 
	 * @param type 0 表示邮箱，1 表示手机
	 * @param aim
	 */
	public void sendCode(int type, String aim) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		if (application.getUserInfo().getLoginType() == 1) {
			params.add(new BasicNameValuePair("openid", application.getUserInfo().getOpenIdQQ()));
			params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
			params.add(new BasicNameValuePair("devicetype", 2 + ""));
			params.add(new BasicNameValuePair("app", "hdx"));
		} else {
			params.add(new BasicNameValuePair("openid", application.getUserInfo().getOpenIdWeibo()));
			params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
			params.add(new BasicNameValuePair("devicetype", 2 + ""));
			params.add(new BasicNameValuePair("app", "hdx"));
		}
		params.add(new BasicNameValuePair("forreg","false"));
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));
		showProgress("正在获取验证码");
		httpCilents.postA(Url.ACCUPASS_SEND_CODE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					if (info.isSuccess()) {
						mc = new MyCount(70000, 1000,sendBindCodeTv);
						mc.start();
						application.showMsg(info.getMsg());
					} else {
						application.showMsg(info.getMsg());
					}
					// edBindCode.setText(result.toString());
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	// 绑定邮箱或手机
	public void checkCode(int type, final String aim) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		params.add(new BasicNameValuePair("code", edBindCode.getText().toString().trim()));
		if (TAG == 1)
			showProgress("正在绑定手机");
		else
			showProgress("正在绑定邮箱");
		httpCilents.postA(Url.ACCUPASS_VALICODE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					UserInfo user = application.getUserInfo();
					if (info.isSuccess()) {
						application.showMsg(info.getMsg());
						if (TAG == 1) {
							user.setPhoneActivated(true);
							user.setPhone(aim);
						} else {
							user.setEmailActivated(true);
							user.setEmail(aim);
						}
						application.setUserInfo(user);
						finish();
					} else {
						application.showMsg(info.getMsg());
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
	protected void onPause() {
		super.onPause();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BindPhoneActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mc != null) {
			mc.cancel();
		}
	}

}
