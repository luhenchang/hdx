package com.accuvally.hdtui.activity.home.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.MyCount;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsRegEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 购票-验证购票人信息
 * 
 * @author Semmer Wang
 * 
 */
public class BuyTicketFirstActivity extends BaseActivity implements OnClickListener {

	private TextView tvNext;

	private EditTextWithDel edDialogPhone, edCheckNum;

	private TextView sendCheckNumTv;

	MyCount mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_ticket_first);
		initView();
	}

	public void initView() {
		setTitle(getResources().getString(R.string.buy_ticket_first_title));

		tvNext = (TextView) findViewById(R.id.tvFirstNext);
		edDialogPhone = (EditTextWithDel) findViewById(R.id.edDialogPhone);
		edCheckNum = (EditTextWithDel) findViewById(R.id.edCheckNum);
		sendCheckNumTv = (TextView) findViewById(R.id.sendCheckNumTv);

		tvNext.setOnClickListener(this);
		sendCheckNumTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvFirstNext:// 下一步
			if (Utils.isFastDoubleClick())
				return;
			if (TextUtils.isEmpty(edDialogPhone.getText().toString().trim())) {
				application.showMsg("请输入手机号");
				return;
			}
			if (TextUtils.isEmpty(edCheckNum.getText().toString().trim())) {
				application.showMsg("请输入验证码");
				return;
			}
			checkLogin(edDialogPhone.getText().toString().trim(), edCheckNum.getText().toString().trim(), "", "", "");
			break;
		case R.id.sendCheckNumTv:
			if (Utils.isFastDoubleClick())
				return;
			sendCode(1, edDialogPhone.getText().toString().trim());
			break;
		}
	}

	// 获取验证码
	public void sendCode(int type, String aim) {
		if (TextUtils.isEmpty(aim)) {
			application.showMsg("请输入手机号");
			return;
		}
		if (!CheckTextBox.isPhoneNumberValid(aim)) {
			application.showMsg("请输入正确的手机号");
			return;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		params.add(new BasicNameValuePair("forreg", "false"));
		if (application.getUserInfo().getLoginType() == 1)
			params.add(new BasicNameValuePair("openid", application.getUserInfo().getOpenIdQQ()));
		else
			params.add(new BasicNameValuePair("openid", application.getUserInfo().getOpenIdWeibo()));
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
						mc = new MyCount(70000, 1000, sendCheckNumTv);
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

	// 验证账户信息
	public void checkLogin(final String phone, String code, String nick, String sex, String name) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("password", "123456"));
		params.add(new BasicNameValuePair("nick", ""));
		params.add(new BasicNameValuePair("deviceid",application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("type", 1 + ""));
		params.add(new BasicNameValuePair("realname", ""));
		params.add(new BasicNameValuePair("gender", ""));
		try {
			params.add(new BasicNameValuePair("appversion", Util.getVersionName(mContext)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.add(new BasicNameValuePair("baiduuid", application.sharedUtils.readString("BaiduUID")));
		params.add(new BasicNameValuePair("baiduchannelid", application.sharedUtils.readString("baiduchannelid")));
		showProgress("正在验证账户信息");
		httpCilents.postA(Url.ACCUPASS_USER_REGISTER, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msgInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msgInfo.isSuccess()) {
						application.sharedUtils.writeString("userName", phone);
						UserInfo user = JSON.parseObject(msgInfo.getResult(), UserInfo.class);
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, user.getId());
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, user.getToken());
						application.setUserInfo(user);
						application.leanCloudLogin(user.getAccount());
						EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGIN));
						EventBus.getDefault().post(new ChangeDetailsRegEventBus(true));
						finish();
					} else {
						application.showMsg(msgInfo.getMsg());
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
