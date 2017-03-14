package com.accuvally.hdtui.activity.mine.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.MyCount;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改密码
 * 
 * @author Semmer Wang
 * 
 */
public class UpdataPasswordActivity extends BaseActivity implements OnClickListener {

	private EditTextWithDel edPhone, edCodeNumber, edNewPass;

	private Button updatePassBtn;
	private TextView sendCodeTV;
	private MyCount mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_password);
		initView();
	}

	public void initView() {
		setTitle(R.string.personal_update_password_title);

		edPhone = (EditTextWithDel) findViewById(R.id.edPhone);
		edCodeNumber = (EditTextWithDel) findViewById(R.id.edCodeNumber);
		edNewPass = (EditTextWithDel) findViewById(R.id.edNewPass);
		sendCodeTV = (TextView) findViewById(R.id.sendCodeTV);
		updatePassBtn = (Button) findViewById(R.id.updatePassBtn);

		sendCodeTV.setOnClickListener(this);
		updatePassBtn.setOnClickListener(this);
		// setViewTouchListener(findViewById(R.id.linearlayout));
	}

	// 获取验证码
	public void sendCode(int type, String aim) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("forreg","false"));
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));
		
		showProgress("正在获取验证码");
		httpCilents.postB(Url.ACCUPASS_SEND_CODE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					// edCodeNumber.setText(result.toString());
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					if (info.isSuccess()) {
						mc = new MyCount(70000, 1000, sendCodeTV);
						mc.start();
						application.showMsg(info.getMsg());
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

	// 修改密码
	public void update(int type, String code, String phone, String newpwd) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("aim", phone));
		params.add(new BasicNameValuePair("newpwd", newpwd));
		showProgress("正在修改密码");
		httpCilents.postA(Url.ACCUPASS_UPDATE_PASS, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					if (info.isSuccess()) {
						application.showMsg(info.msg);
						finish();
					} else {
						application.showMsg(info.msg);
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sendCodeTV:// 获取验证码
			String phone = edPhone.getText().toString().trim();
			if (phone.indexOf("@") != -1) {
				if (CheckTextBox.isEmail(phone)) {
					sendCode(0, phone);
				} else {
					application.showMsg("请输入正确的邮箱");
				}
			} else {
				if (CheckTextBox.isPhoneNumberValid(phone)) {
					sendCode(1, phone);
				} else {
					application.showMsg("请输入正确的手机号");
				}
			}
			break;
		case R.id.updatePassBtn:// 修改密码
			String code = edCodeNumber.getText().toString().trim();
			String phoneNum = edPhone.getText().toString().trim();
			String password = edNewPass.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				application.showMsg("请输入验证码");
				return;
			}
			if (TextUtils.isEmpty(phoneNum)) {
				application.showMsg("手机号或邮箱不能为空");
				return;
			}
			if (TextUtils.isEmpty(password)) {
				application.showMsg("请输入新密码");
				return;
			}
			if (password.length() < 6) {
				application.showMsg("密码长度小于6位，请重新输入");
				return;
			}
			if (!CheckTextBox.isPhoneNumberValid(phoneNum)) {
				application.showMsg("请输入正确的手机号");
			}
			if (CheckTextBox.isPhoneNumberValid(phoneNum)) {
				update(1, code, phoneNum, password);
			} else if (CheckTextBox.isEmail(phoneNum)) {
				update(0, code, phoneNum, password);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mc != null) {
			mc.cancel();
		}
	}

}
