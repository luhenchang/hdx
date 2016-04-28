package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.MyCount;
import com.alibaba.fastjson.JSON;

public class FindPasswordActivity extends BaseActivity implements OnClickListener, TextWatcher {

	private EditTextWithDel find_password_password;
	private EditTextWithDel again_submit_password;
	private TextView acpuire_verification_code;
	private EditTextWithDel verification_code;
	private EditTextWithDel register_phone;
	private MyCount myCount;
	private Button find_password_submit_button;
	private LinearLayout lyFindBg;
	int dinmen;
	String user_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initView();
		addListener();
	}

	private void initView() {
		setTitle(R.string.setting_password);
		register_phone = (EditTextWithDel) findViewById(R.id.register_phone);
		verification_code = (EditTextWithDel) findViewById(R.id.verification_code);
		acpuire_verification_code = (TextView) findViewById(R.id.acpuire_verification_code);
		find_password_password = (EditTextWithDel) findViewById(R.id.find_password_password);
		again_submit_password = (EditTextWithDel) findViewById(R.id.again_submit_password);
		find_password_submit_button = (Button) findViewById(R.id.find_password_submit_button);
		lyFindBg = (LinearLayout) findViewById(R.id.lyFindBg);
	}

	private void addListener() {
		acpuire_verification_code.setOnClickListener(this);
		find_password_submit_button.setOnClickListener(this);
		again_submit_password.setLongClickable(false);
		find_password_password.setLongClickable(false);
		register_phone.addTextChangedListener(this);
		verification_code.addTextChangedListener(this);
		find_password_password.addTextChangedListener(this);
		again_submit_password.addTextChangedListener(this);
		dinmen = getResources().getDimensionPixelSize(R.dimen.little_8);
		user_name = getIntent().getStringExtra("user_name");
		if (!TextUtils.isEmpty(user_name)) {
			register_phone.setText(user_name);
			Editable userName = register_phone.getText();
			Selection.setSelection(userName, userName.length());
		}
		if (TextUtils.isEmpty(register_phone.getText().toString())) {
			acpuire_verification_code.setEnabled(false);
		} else {
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_green);
			acpuire_verification_code.setPadding(dinmen, dinmen, dinmen, dinmen);
			acpuire_verification_code.setEnabled(true);
		}
		find_password_submit_button.setEnabled(false);
	}

	public void sendCodes(int type, String aim) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
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
						myCount = new MyCount(70000, 1000, acpuire_verification_code);
						myCount.start();
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
						application.showMsg(info.getMsg());
						application.sharedUtils.writeString("userName", register_phone.getText().toString().trim());

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acpuire_verification_code:
			String phone = register_phone.getText().toString().trim();
			if (CheckTextBox.isPhoneNumberValid(phone))
				sendCodes(1, phone);
			else if (CheckTextBox.isEmail(phone))
				sendCodes(0, phone);
			else
				application.showMsg("请输入正确的手机号");

			break;
		case R.id.find_password_submit_button:
			findPassword();
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(FindPasswordActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
			}
			break;

		default:
			break;
		}
	}

	private void findPassword() {
		String code = verification_code.getText().toString().trim();
		String phoneNum = register_phone.getText().toString().trim();
		String password = find_password_password.getText().toString().trim();
		String passwordTwo = again_submit_password.getText().toString().trim();
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
		if (!passwordTwo.equals(password)) {
			application.showMsg("两次输入的密码不一致");
			return;
		}
		if (CheckTextBox.isPhoneNumberValid(phoneNum))
			update(1, code, phoneNum, password);
		else if (CheckTextBox.isEmail(phoneNum))
			update(0, code, phoneNum, password);
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		boolean Sign1 = register_phone.getText().length() > 0;
		boolean Sign2 = verification_code.getText().length() > 0;
		boolean Sign3 = find_password_password.getText().length() > 0;
		boolean Sign4 = again_submit_password.getText().length() > 0;
		if ("获取验证码".equals(acpuire_verification_code.getText().toString())) {
			if (!Sign1) {
				acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_gray2);
				acpuire_verification_code.setPadding(dinmen, dinmen, dinmen, dinmen);
				acpuire_verification_code.setEnabled(false);
			} else {
				acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_green);
				acpuire_verification_code.setPadding(dinmen, dinmen, dinmen, dinmen);
				acpuire_verification_code.setEnabled(true);
			}
		}

		if (Sign1 & Sign2 & Sign3 & Sign4) {
			lyFindBg.setBackgroundResource(R.drawable.selector_wane_green);
			find_password_submit_button.setEnabled(true);
		} else {
			lyFindBg.setBackgroundResource(R.drawable.selector_wane_gray2);
			find_password_submit_button.setEnabled(false);
		}
	}
}
