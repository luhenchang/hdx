package com.accuvally.hdtui.activity.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.ThreeLoginInfo;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.LoginUtil;
import com.accuvally.hdtui.utils.ThreeLoginUtils;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.eventbus.ChangeAliLoginEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeLoginOrRegEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeRegSuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeRegThreeLogin;
import com.accuvally.hdtui.utils.eventbus.ChangeThreeLoginEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.pay.AliLoginUtils;
import com.accuvally.hdtui.utils.pay.AuthResult;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LoginActivityNew extends BaseActivity implements OnClickListener, TextWatcher {

	private EditTextWithDel login_user_name;
	private EditTextWithDel login_password;
	private Button login_submit_botton;
	private TextView forget_password;
	private String str;
	private LinearLayout save_ly;
	private TextView tvLoginAndReg;
	private TextView tvLoginQQ, tvLoginSina, tvLoginAli, tvLoginWx;

	UMSocialService mController;
	ThreeLoginUtils loginUtils;
	int threeTemp;
	private LinearLayout lyLoginBg;
	AliLoginUtils aliLoginUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_new);
		EventBus.getDefault().register(this);
		str = application.sharedUtils.readString("accountId");
		initView();
		initLogin();
	}

	private void initView() {
		setTitle(R.string.setting_login);
		login_user_name = (EditTextWithDel) findViewById(R.id.login_user_name);
		login_password = (EditTextWithDel) findViewById(R.id.login_password);
		login_submit_botton = (Button) findViewById(R.id.login_submit_botton);
		forget_password = (TextView) findViewById(R.id.forget_password);
		save_ly = (LinearLayout) findViewById(R.id.save_ly);
		tvLoginAndReg = (TextView) findViewById(R.id.tvLoginAndReg);
		tvLoginQQ = (TextView) findViewById(R.id.tvLoginQQ);
		tvLoginSina = (TextView) findViewById(R.id.tvLoginSina);
		tvLoginAli = (TextView) findViewById(R.id.tvLoginAli);
		tvLoginWx = (TextView) findViewById(R.id.tvLoginWx);
		lyLoginBg = (LinearLayout) findViewById(R.id.lyLoginBg);

		login_password.setLongClickable(false);
		login_submit_botton.setOnClickListener(this);
		forget_password.setOnClickListener(this);
		save_ly.setOnClickListener(this);
		tvLoginQQ.setOnClickListener(this);
		tvLoginSina.setOnClickListener(this);
		tvLoginAli.setOnClickListener(this);
		tvLoginWx.setOnClickListener(this);
		login_user_name.addTextChangedListener(this);
		login_password.addTextChangedListener(this);
		save_ly.setVisibility(View.VISIBLE);
		tvLoginAndReg.setText("我要注册");
		if (application.sharedUtils.readString("userName") != null)
			login_user_name.setText(application.sharedUtils.readString("userName"));
		Editable userName = login_user_name.getText();
		Selection.setSelection(userName, userName.length());
		login_submit_botton.setEnabled(false);
	}

	public void onEventMainThread(ChangeLoginOrRegEventBus eventBus) {
		if (eventBus.getIsLoginOrReg() == 1) {
			login_user_name.setText(eventBus.getPhone());
		}
	}

	public void onEventMainThread(ChangeRegSuccessEventBus eventBus) {
		finish();
	}

    //第三方登录成功之后修改数据
	public void onEventMainThread(ChangeThreeLoginEventBus eventBus) {
		if (eventBus.getStatus() == 200 && eventBus.getInfo() != null) {
			ThreeLoginInfo threeLoginInfo = new ThreeLoginInfo();
			if (threeTemp == 4) {//4为微信  1为qq   2为新浪
				threeLoginInfo.setLogo(eventBus.getInfo().get("headimgurl").toString());
				threeLoginInfo.setGender(eventBus.getInfo().get("sex").toString());
				threeLoginInfo.setScreen_name(eventBus.getInfo().get("nickname").toString());
				threeLoginInfo.setOpenid(eventBus.getInfo().get("unionid").toString());
			} else {
				threeLoginInfo.setLogo(eventBus.getInfo().get("profile_image_url").toString());
				threeLoginInfo.setGender(eventBus.getInfo().get("gender").toString());
				threeLoginInfo.setScreen_name(eventBus.getInfo().get("screen_name").toString());
				if (threeTemp == 2) {
					threeLoginInfo.setOpenid(eventBus.getInfo().get("uid").toString());
				} else if (threeTemp == 1) {
					threeLoginInfo.setOpenid(eventBus.getOpenid());
				}
			}
			threeLogin(threeLoginInfo);
		} else {
			Log.i("info", "发生错误：" + eventBus.getStatus());
		}
	}

	public void onEventMainThread(ChangeRegThreeLogin eventBus) {
		if (eventBus.getLoginTemp() == 1) {
			threeTemp = 1;
			loginUtils.authVerify(SHARE_MEDIA.QQ, this);
		} else if (eventBus.getLoginTemp() == 2) {
			threeTemp = 2;
			loginUtils.authVerify(SHARE_MEDIA.SINA, this);
		} else if (eventBus.getLoginTemp() == 3) {
			threeTemp = 3;
		} else if (eventBus.getLoginTemp() == 4) {
			threeTemp = 4;
			loginUtils.authVerify(SHARE_MEDIA.WEIXIN, this);
		}
	}

	public void onEventMainThread(ChangeAliLoginEventBus eventBus) {
		if (eventBus.getStatus() == 1) {
			AuthResult authResult = new AuthResult(eventBus.getResult());
			// 支付宝返回此次授权结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
			String resultInfo = authResult.getResult();

			String resultStatus = authResult.getResultStatus();

			// 判断resultStatus 为“9000”且result_code
			// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
			if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
				// 获取alipay_open_id，调支付时作为参数extern_token 的value
				// 传入，则用户使用的支付账户为该授权账户
				application.showMsg("授权成功\n" + authResult.getAlipayOpenId());
				ThreeLoginInfo loginInfo = new ThreeLoginInfo();
				loginInfo.setGender(1 + "");
				loginInfo.setLogo("");
				loginInfo.setOpenid(authResult.getAlipayOpenId());
				loginInfo.setScreen_name(authResult.getAlipayOpenId());
				threeLogin(loginInfo);
			} else {
				// 其他状态值则为授权失败
				application.showMsg("授权失败");
			}
		} else {
			application.showMsg("授权失败");
		}
	}

	public void initLogin() {
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		loginUtils = new ThreeLoginUtils(mContext, mController);
		loginUtils.initLogin(this);
		aliLoginUtils = new AliLoginUtils();
	}

	public void login() {
		final String userName = login_user_name.getText().toString();
		final String password = login_password.getText().toString();
		if ("".equals(userName)) {
			application.showMsg("请输入账号");
			return;
		}
		if ("".equals(password)) {
			application.showMsg("请输入密码");
			return;
		}
		showProgress("正在登录中");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
        Trace.e("LoginActivityNew",application.getIMEI());
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("account", userName));
		params.add(new BasicNameValuePair("password", password));
		
		String cityName =application.sharedUtils.readString("cityName");
		if(!TextUtils.isEmpty(cityName)){
			params.add(new BasicNameValuePair("city",cityName));
		}
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));
		params.add(new BasicNameValuePair("baiduuid", application.sharedUtils.readString("BaiduUID")));
		params.add(new BasicNameValuePair("baiduchannelid", application.sharedUtils.readString("baiduchannelid")));
		
		
		httpCilents.postA(Url.ACCUPASS_LOGIN, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					if (info.isSuccess()) {
						MobclickAgent.onEvent(mContext, "login_count");
						com.alibaba.fastjson.JSONObject obj = JSON.parseObject(info.getResult());
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, obj.getString("id"));
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, obj.getString("token"));
//                        application.sharedUtils.writeString(Keys.logintype, "logintype_password");
						getUserInfo(userName, password);
                        postTag();//帐号登录
                    } else {
						application.showMsg(info.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString() + "");
					break;
				}
			}
		});
	}

	public void getUserInfo(final String name, final String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		showProgress("正在获取用户资料");
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_USER_INFO, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
					if (info.isSuccess()) {
						UserInfo userInfo = JSON.parseObject(info.getResult(), UserInfo.class);
						application.setUserInfo(userInfo);
						application.sharedUtils.writeString("userName", name);
						application.showMsg("登录成功");
						if (userInfo.getAccount().equals(str)) {
							application.sharedUtils.writeBoolean(Config.IS_SAME_ACCOUNT, true);
						} else {
							application.sharedUtils.writeBoolean(Config.IS_SAME_ACCOUNT, false);
						}
						application.sharedUtils.writeString("accountId", userInfo.getAccount());
						Log.i("Login", "phone:" + userInfo.getPhone());
						application.leanCloudLogin(userInfo.getAccount());
						EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGIN));
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

	// public void setUserTags() {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// StringBuilder builder = new StringBuilder();
	// List<String> list = dbManager.getClassfy();
	// if (list.size() != 0) {
	// for (int i = 0; i < list.size(); i++) {
	// builder.append(list.get(i) + ",");
	// }
	// }
	// params.add(new BasicNameValuePair("tags", builder.toString()));
	// httpCilents.postA(Url.ACCUPASS_SETUSERTAGS, params, new
	// WebServiceCallBack() {
	//
	// @Override
	// public void callBack(int code, Object result) {
	// switch (code) {
	// case Config.RESULT_CODE_SUCCESS:
	// break;
	// case Config.RESULT_CODE_ERROR:
	// application.showMsg(result.toString());
	// break;
	// }
	// finish();
	// }
	// });
	// }

	public void threeLogin(ThreeLoginInfo threeLoginInfo) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("unionid",threeLoginInfo.getOpenid()));
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", "2"));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("appversion", Util.getVersionName(mContext)));
		params.add(new BasicNameValuePair("logo", threeLoginInfo.getLogo()));
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));

        //微博中男为1，女为0；   微信中男为1，女为2；   qq中返回的就是男，女
        switch (threeLoginInfo.getGender()){
            case "男":
                params.add(new BasicNameValuePair("gender", "1"));
                break;
            case "女":
                params.add(new BasicNameValuePair("gender", "2"));
                break;
            case "0":
                params.add(new BasicNameValuePair("gender", "2"));
                break;
            case "1":
                params.add(new BasicNameValuePair("gender", "1"));
                break;
        }
		
		/*if (TextUtils.equals(threeLoginInfo.getGender(), "男"))
			params.add(new BasicNameValuePair("gender", "1"));
		else
			params.add(new BasicNameValuePair("gender", threeLoginInfo.getGender()));*/

		params.add(new BasicNameValuePair("nick", threeLoginInfo.getScreen_name()));
		params.add(new BasicNameValuePair("openid", threeLoginInfo.getOpenid()));
		if (threeTemp == 1) {
			params.add(new BasicNameValuePair("type", "1"));
		} else if (threeTemp == 2) {
			params.add(new BasicNameValuePair("type", "0"));
		} else if (threeTemp == 3) {
			params.add(new BasicNameValuePair("type", "9"));
		} else if (threeTemp == 4) {
			params.add(new BasicNameValuePair("type", "10"));
		}
		showProgress("正在登录");
		httpCilents.postA(Url.ACCUPASS_THREE_LOGIN, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msgInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msgInfo.isSuccess()) {
						application.showMsg(msgInfo.getMsg());
						UserInfo userInfo = JSON.parseObject(msgInfo.getResult(), UserInfo.class);
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, userInfo.getId());
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, userInfo.getToken());
//                        application.sharedUtils.writeString(Keys.logintype, "logintype_three");
						application.setUserInfo(userInfo);
						if (userInfo.getAccount().equals(str)) {
							application.sharedUtils.writeBoolean(Config.IS_SAME_ACCOUNT, true);
						} else {
							application.sharedUtils.writeBoolean(Config.IS_SAME_ACCOUNT, false);
						}
						application.sharedUtils.writeString("accountId", userInfo.getAccount());
//						Log.i("Login", "phone:" + userInfo.getPhone());
						// if
						// (application.sharedUtils.readBoolean("isSynchronous"))
						// {
						// if (dbManager.getClassfy().size() != 0) {
						// setUserTags();
						// }
						// }
						application.leanCloudLogin(userInfo.getAccount());
                        EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGIN));

                        postTag();//第三方登录
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

    private void postTag(){
        String str=application.sharedUtils.readString(Keys.select_categorys);
        LoginUtil.setCategory(httpCilents,str);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_submit_botton:
			login();
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LoginActivityNew.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
			}
			break;
		case R.id.forget_password:// 忘记密码
			startActivity(new Intent(this, FindPasswordActivity.class).putExtra("user_name", login_user_name.getText().toString()));
			break;
		case R.id.save_ly:// 注册
			startActivity(new Intent(this, RegisterActivity.class).putExtra("user_name", login_user_name.getText().toString()));
			break;
		case R.id.tvLoginQQ:// qq登录
			threeTemp = 1;
			loginUtils.authVerify(SHARE_MEDIA.QQ, this);
			break;
		case R.id.tvLoginSina:// 新浪登录
			threeTemp = 2;
			loginUtils.authVerify(SHARE_MEDIA.SINA, this);
			break;
		case R.id.tvLoginAli:// 支付宝登录
			threeTemp = 3;
			// target_id可以为空
			aliLoginUtils.auth(this, Config.ALI_APP_ID, Config.ALI_PID, Config.ALI_TARGET_ID);
			break;
		case R.id.tvLoginWx:// 微信登录
			UMWXHandler wxHandler = new UMWXHandler(mContext, Config.WX_APPID, Config.WX_APPSECRET);
			if (wxHandler.isClientInstalled()) {
				threeTemp = 4;
				loginUtils.authVerify(SHARE_MEDIA.WEIXIN, this);
			} else {
				application.showMsg("你未安装微信");
			}
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		boolean Sign1 = login_user_name.getText().length() > 0;
		boolean Sign2 = login_password.getText().length() > 0;
		if (Sign1 & Sign2) {
			lyLoginBg.setBackgroundResource(R.drawable.selector_wane_green);
			login_submit_botton.setEnabled(true);
		} else {
			lyLoginBg.setBackgroundResource(R.drawable.selector_wane_gray2);
			login_submit_botton.setEnabled(false);
		}
	}
}
