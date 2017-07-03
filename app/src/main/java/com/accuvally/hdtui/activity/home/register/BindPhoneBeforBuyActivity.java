package com.accuvally.hdtui.activity.home.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.accuvally.hdtui.utils.Utils;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 购票-验证购票人信息：在第三方登录了，但没有注册手机号的时候。可以绑定一个未注册的手机号
 * 
 * @author Semmer Wang
 * 
 */
public class BindPhoneBeforBuyActivity extends BaseActivity implements OnClickListener {



    private EditText register_phone;
    private EditText verification_code;
//    private EditText register_password;

    private EditText register_email;
    private EditText register_real_name;



	private TextView sendCheckNumTv;
    private TextView tvNext;

	MyCount mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindphone_beforbuy);
		initView();

	}

	public void initView() {
		setTitle("完善信息");


		register_phone = (EditText) findViewById(R.id.register_phone);
		verification_code = (EditText) findViewById(R.id.verification_code);



        register_email = (EditText) findViewById(R.id.register_email);
        register_real_name = (EditText) findViewById(R.id.register_real_name);

        sendCheckNumTv = (TextView) findViewById(R.id.sendCheckNumTv);
        sendCheckNumTv.setOnClickListener(this);

        tvNext = (TextView) findViewById(R.id.tvFirstNext);
        tvNext.setOnClickListener(this);


        if(!TextUtils.isEmpty(application.getUserInfo().getPhone())){//不为空的话把手机号显示出来

            register_phone.setText(application.getUserInfo().getPhone());

            if(application.getUserInfo().isPhoneActivated()){//验证过还要把验证的相关UI隐藏
                register_phone.setEnabled(false);
                sendCheckNumTv.setVisibility(View.GONE);
                findViewById(R.id.ll_verification_code).setVisibility(View.GONE);

            }
        }

        if(!TextUtils.isEmpty(application.getUserInfo().getEmail())){
            register_email.setText(application.getUserInfo().getEmail());

            if(application.getUserInfo().isEmailActivated()){
                register_email.setEnabled(false);
            }
        }

        if(!TextUtils.isEmpty(application.getUserInfo().getRealName())){
            register_real_name.setText(application.getUserInfo().getRealName());

        }

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvFirstNext:// 下一步
			if (Utils.isFastDoubleClick())
				return;

            completeAccount();
			break;
		case R.id.sendCheckNumTv://获取验证码
			if (Utils.isFastDoubleClick())
				return;
			sendCode(1, register_phone.getText().toString().trim());
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
		httpCilents.postB(Url.ACCUPASS_SEND_CODE, params, new WebServiceCallBack() {

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




	// 完善账户信息
	public void completeAccount() {

        final String phone = register_phone.getText().toString().trim();
        String code = verification_code.getText().toString().trim();

        final String email = register_email.getText().toString().trim();
        final String real_name = register_real_name.getText().toString().trim();


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String url="";

        if(!application.getUserInfo().isPhoneActivated()){//手机没验证过

            if (TextUtils.isEmpty(phone)) {
                application.showMsg("手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                application.showMsg("验证码不能为空");
                return;
            }

            params.add(new BasicNameValuePair("value", phone));
            params.add(new BasicNameValuePair("code", code));
            params.add(new BasicNameValuePair("type", 1 + ""));

            url=Url.ACCUPASS_VALICODE;
        }else {
            url=Url.ACCUPASS_UPDATE_USERINFO;
        }


        if(TextUtils.isEmpty(application.getUserInfo().getEmail())){
            if (TextUtils.isEmpty(email)) {
                application.showMsg("邮箱不能为空");
                return;
            }

            if(!CheckTextBox.isEmail(email)){
                application.showMsg("邮箱格式错误");
                return;
            }
        }

        if(TextUtils.isEmpty(application.getUserInfo().getRealName())){
            if (TextUtils.isEmpty(real_name)) {
                application.showMsg("姓名不能为空");
                return;
            }
        }

        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("realname", real_name));

		showProgress("正在完善账户信息");


		httpCilents.postA(url, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);

                        if (info.isSuccess()) {
                            UserInfo user = application.getUserInfo();
                            application.showMsg("账户信息已完善");

                            user.setPhoneActivated(true);
                            user.setPhone(phone);
                            application.sharedUtils.writeString("userName", phone);

                                user.setEmail(email);
                                user.setRealName(real_name);
                                application.setUserInfo(user);


                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
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
}
