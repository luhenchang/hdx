package com.accuvally.hdtui.activity.mine.login;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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
import com.accuvally.hdtui.activity.mine.personal.ChoosePhotoActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.ui.InterceptLinearLayout;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ImageBig;
import com.accuvally.hdtui.utils.MyCount;
import com.accuvally.hdtui.utils.PickPhoto;
import com.accuvally.hdtui.utils.RotateY;
import com.accuvally.hdtui.utils.eventbus.ChangeLoginOrRegEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeRegSuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeRegThreeLogin;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

//注册界面，
public class RegisterActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private EditTextWithDel register_phone;
    private EditTextWithDel verification_code;
	private EditTextWithDel register_password;

    private EditTextWithDel register_email;
    private EditTextWithDel register_real_name;
	private EditTextWithDel register_nick_name;

	private Button register_submit_botton;


	private TextView acpuire_verification_code;
	private MyCount mc;
	private LinearLayout save_ly;
	private TextView tvLoginAndReg;
	private TextView tvLoginQQ, tvLoginSina, tvLoginAli, tvLoginWx;
	private LinearLayout lyRegBg;
	int dinmen;
	private Dialog dialog;
	String user_name;

	private View rootView;
	private InterceptLinearLayout includePersonal;
	private View includeRegister;
	private ImageView ivHeadPortrait;
	private LinearLayout llOpen;
	private EditText etNick;
	private EditText etPersonality;
	private String nick;
	private String personality;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_merge_personal);
		initView();
		initViewNew();
		addListener();
	}

	private void initViewNew() {
		rootView = findViewById(R.id.rootView);
		includePersonal = (InterceptLinearLayout) findViewById(R.id.includePersonal);// 个人信息页面
		includeRegister = findViewById(R.id.includeRegister);// 注册页面
		llOpen = (LinearLayout) findViewById(R.id.llOpen);
		ivHeadPortrait = (ImageView) findViewById(R.id.ivHeadPortrait);
		etNick = (EditText) findViewById(R.id.etNick);
		etPersonality = (EditText) findViewById(R.id.etPersonality);
		ivHeadPortrait.setOnClickListener(this);
		llOpen.setOnClickListener(this);
	}

	private void initView() {
		setTitle(R.string.setting_register);
		register_phone = (EditTextWithDel) findViewById(R.id.register_phone);
		verification_code = (EditTextWithDel) findViewById(R.id.verification_code);
		acpuire_verification_code = (TextView) findViewById(R.id.acpuire_verification_code);
		register_password = (EditTextWithDel) findViewById(R.id.register_password);

        register_email = (EditTextWithDel) findViewById(R.id.register_email);
        register_real_name = (EditTextWithDel) findViewById(R.id.register_real_name);
		register_nick_name = (EditTextWithDel) findViewById(R.id.register_nick_name);

		register_submit_botton = (Button) findViewById(R.id.register_submit_botton);
		save_ly = (LinearLayout) findViewById(R.id.save_ly);
		tvLoginAndReg = (TextView) findViewById(R.id.tvLoginAndReg);
		tvLoginQQ = (TextView) findViewById(R.id.tvLoginQQ);
		tvLoginSina = (TextView) findViewById(R.id.tvLoginSina);
		tvLoginAli = (TextView) findViewById(R.id.tvLoginAli);
		tvLoginWx = (TextView) findViewById(R.id.tvLoginWx);
		lyRegBg = (LinearLayout) findViewById(R.id.lyRegBg);

		save_ly.setVisibility(View.VISIBLE);
		tvLoginAndReg.setText("我已有账号");
		dinmen = getResources().getDimensionPixelSize(R.dimen.little_8);
		user_name = getIntent().getStringExtra("user_name");
		Log.i("info", "TextUtils.isEmpty(user_name):" + TextUtils.isEmpty(user_name));
		if (!TextUtils.isEmpty(user_name)) {
			register_phone.setText(user_name);
			Editable userName = register_phone.getText();
			Selection.setSelection(userName, userName.length());
		}

	}

	private void addListener() {
		acpuire_verification_code.setOnClickListener(this);
		register_submit_botton.setOnClickListener(this);
		save_ly.setOnClickListener(this);
		tvLoginQQ.setOnClickListener(this);
		tvLoginSina.setOnClickListener(this);
		tvLoginAli.setOnClickListener(this);
		tvLoginWx.setOnClickListener(this);
		register_password.setLongClickable(false);

		register_phone.addTextChangedListener(this);
		verification_code.addTextChangedListener(this);
		register_password.addTextChangedListener(this);

		register_email.addTextChangedListener(this);
        register_real_name.addTextChangedListener(this);
        register_nick_name.addTextChangedListener(this);

		findViewById(R.id.ivBack).setOnClickListener(this);

		if ("".equals(register_phone.getText().toString())) {
			acpuire_verification_code.setEnabled(false);
		} else {
			acpuire_verification_code.setBackgroundResource(R.drawable.selector_wane_green);
			acpuire_verification_code.setPadding(dinmen, dinmen, dinmen, dinmen);
			acpuire_verification_code.setEnabled(true);
		}
		register_submit_botton.setEnabled(false);
	}

	
	/**
	 * 发送验证码
	 * @param type 0 表示邮箱，1 表示手机
	 * @param aim  手机号码或邮箱
	 */
	public void sendCode(int type, String aim) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("value", aim));
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("forreg", "true"));
		params.add(new BasicNameValuePair("devicedesc", application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion", application.getAppversion()));
		
		showProgress("正在获取验证码");
		httpCilents.postB(Url.ACCUPASS_SEND_CODE, params, new WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        // edRegCode.setText(result.toString());
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (info.isSuccess()) {
                            mc = new MyCount(70000, 1000, acpuire_verification_code);
                            mc.start();
                        } else {
                        }
                        application.showMsg(info.getMsg());
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
	}

	public void register() {
		final String phone = register_phone.getText().toString().trim();
		String code = verification_code.getText().toString().trim();
		final String password = register_password.getText().toString().trim();

        String email = register_email.getText().toString().trim();
        String real_name = register_real_name.getText().toString().trim();
		String nick = register_nick_name.getText().toString().trim();

		if (TextUtils.isEmpty(phone)) {
			application.showMsg("手机号不能为空");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			application.showMsg("验证码不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			application.showMsg("密码不能为空");
			return;
		}

        if (password.length() < 6 || password.length() > 16) {
            application.showMsg("密码长度在6-16位之间");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            application.showMsg("邮箱不能为空");
            return;
        }

        if(!CheckTextBox.isEmail(email)){
            application.showMsg("邮箱格式错误");
            return;
        }

        if (TextUtils.isEmpty(real_name)) {
            application.showMsg("姓名不能为空");
            return;
        }

		 if (TextUtils.isEmpty(nick)) {
		 application.showMsg("昵称不能为空");
		 return;
		 }

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("code", code));

		params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("realname", real_name));
        params.add(new BasicNameValuePair("nick", nick));


        params.add(new BasicNameValuePair("country", application.sharedUtils.readString("country")));
        params.add(new BasicNameValuePair("province", application.sharedUtils.readString("province")));
        params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));

		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		params.add(new BasicNameValuePair("devicetype", 2 + ""));
		params.add(new BasicNameValuePair("app", "hdx"));
		params.add(new BasicNameValuePair("devicedesc",application.getDeviceDesc()));
		params.add(new BasicNameValuePair("appversion",application.getAppversion()));
		params.add(new BasicNameValuePair("baiduuid", application.sharedUtils.readString("BaiduUID")));
		params.add(new BasicNameValuePair("baiduchannelid", application.sharedUtils.readString("baiduchannelid")));
		params.add(new BasicNameValuePair("reg", "true"));
 
		showProgress("正在注册账号");
		httpCilents.postA(Url.ACCUPASS_USER_REGISTER, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse req = JSON.parseObject(result.toString(),BaseResponse.class);
					if (req.isSuccess()) {
						MobclickAgent.onEvent(mContext, "register_count");
						application.showMsg("注册成功");
						application.sharedUtils.writeString("userName", phone);
						UserInfo user = JSON.parseObject(req.getResult(), UserInfo.class);
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, user.getId());
						application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, user.getToken());
						application.setUserInfo(user);
                        application.leanCloudLogin(user.getAccount());
                        //退出本界面，退出登录界面(若存在)
                        EventBus.getDefault().post(new ChangeRegSuccessEventBus(1));
                        finish();
//						includePersonal.setVisibility(View.VISIBLE);
//						includeRegister.setVisibility(View.GONE);

					} else {
						application.showMsg(req.getMsg());
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
				sendCode(1, phone);
			else if (CheckTextBox.isEmail(phone))
				sendCode(0, phone);
			else
				application.showMsg("请输入正确的手机号");
			break;

		case R.id.register_submit_botton:
			register();
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
			}
			break;
		case R.id.save_ly:
			if (!"".equals(register_phone.getText().toString())) {
				EventBus.getDefault().post(new ChangeLoginOrRegEventBus(register_phone.getText().toString(), 1));
			}
			finish();
			break;
		case R.id.tvLoginQQ:
			EventBus.getDefault().post(new ChangeRegThreeLogin(1));
			finish();
			break;
		case R.id.tvLoginSina:
			EventBus.getDefault().post(new ChangeRegThreeLogin(2));
			finish();
			break;
		case R.id.tvLoginAli:
			EventBus.getDefault().post(new ChangeRegThreeLogin(3));
			finish();
			break;
		case R.id.tvLoginWx:
			EventBus.getDefault().post(new ChangeRegThreeLogin(4));
			finish();
			break;
		case R.id.ivHeadPortrait:// 点击头像
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, PickPhoto.PICK_PHOTO);
			break;
		case R.id.llOpen:// 开门动画
			nick = etNick.getText().toString();
			personality = etPersonality.getText().toString();
			if (!TextUtils.isEmpty(nick) || !TextUtils.isEmpty(personality)) {
				update();
			}
			OpenAnimation();
			break;
		case R.id.ivBack:
			finish();
			break;
		default:
			break;
		}

	}

	private void OpenAnimation() {
		includeRegister.setVisibility(View.VISIBLE);
		includePersonal.setIntercept(true);// 拦截
		float centerX = 0;
		float centerY = rootView.getHeight() / 2.0f;
		RotateY rotateY = new RotateY(centerX, centerY);
		rotateY.setDuration(2000);
		rotateY.setFillAfter(true);
		includePersonal.startAnimation(rotateY);
		Timer timer = new Timer();
		timer.schedule(task, 2000);
	}
	
	TimerTask task = new TimerTask(){
		@Override
		public void run() {
			EventBus.getDefault().post(new ChangeRegSuccessEventBus(1));
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == PickPhoto.PICK_PHOTO) {
			try {
				Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
				cursor.moveToFirst();
				String path = ImageBig.scalePicture(mContext, cursor.getString(1), 600, 600);
				Intent intent = new Intent(mContext, ChoosePhotoActivity.class);
				intent.putExtra("imagePath", path);
				startActivityForResult(intent, PickPhoto.CROP_PHOTO);
				cursor.close();
			} catch (Exception e) {
				application.showMsg("请选择相册中的照片");
			}
		} else if (requestCode == PickPhoto.CROP_PHOTO) {
			String temppath = data.getStringExtra("path");
			if (temppath != null) {
				setBitmap(temppath);
			}
		}
	}

	private void setBitmap(String path) {
		Bitmap bitmap = ImageBig.getimage(path);
		ivHeadPortrait.setImageBitmap(bitmap);
		byte[] mContent = ImageBig.Bitmap2Bytes(bitmap);
		uploadHead(mContent);
	}

	public void uploadHead(byte[] bytes) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("img", bytes == null ? "" : Base64.encodeToString(bytes, Base64.DEFAULT)));
		showProgress("正在上传头像");
		httpCilents.postA(Url.ACCUPASS_UPLOAD_HEAD, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						application.showMsg(messageInfo.getMsg());
						UserInfo user = application.getUserInfo();
						user.setLogo(messageInfo.getResult());
						user.setLogoLarge(messageInfo.getResult());
						application.setUserInfo(user);
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

	// 更新个人资料
	public void update() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nick", nick));
		params.add(new BasicNameValuePair("brief", personality));
		showProgress("正在更新资料");
		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSONObject.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						UserInfo user = application.getUserInfo();
						application.showMsg("更新资料成功");
						user.setNick(nick);
						user.setBrief(personality);
						application.setUserInfo(user);
					}

					finish();
				}
			}
		});
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
		boolean Sign3 = register_password.getText().length() > 0;
		 boolean Sign4 = register_email.getText().length() > 0;
        boolean Sign5 = register_real_name.getText().length() > 0;
        boolean Sign6 = register_nick_name.getText().length() > 0;
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
		if (Sign1 & Sign2 & Sign3 &Sign4 &Sign5 &Sign6) {
			lyRegBg.setBackgroundResource(R.drawable.selector_wane_green);
			register_submit_botton.setEnabled(true);
		} else {
			lyRegBg.setBackgroundResource(R.drawable.selector_wane_gray2);
			register_submit_botton.setEnabled(false);
		}
	}


}
