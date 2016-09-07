package com.accuvally.hdtui.activity.mine.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.register.BindPhoneActivity;
import com.accuvally.hdtui.activity.home.util.ChooseCityActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ImageBig;
import com.accuvally.hdtui.utils.LoginUtil;
import com.accuvally.hdtui.utils.PickPhoto;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 个人资料
 * 
 * @author Semmer Wang
 * 
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rlNickName, rlUpdateCity, rlSex, rlBindPhone, rlBindEmail;

	private LinearLayout rlName;

    public static final String TAG="PersonalActivity";

	private LinearLayout rlPersonality;

	private TextView tvUpdatePass, tvAccount;

	private TextView tvPhoneActivated, tvEmailActivated;

	private TextView tvNickName, tvRealName, tvSex, tvCity, tvSignature;

	private CircleImageView ivHead;

	private Dialog photoDialog, sexDialog;

	private UMSocialService mController;

	private String OUTPUTFILEPATH;

	private CircleImageView ivUpLoadHead;

	private FileCache fileCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initView();
        EventBus.getDefault().register(this);
        LoginUtil.getUserInfo(httpCilents, application);

	}



    public void onEventMainThread(ChangeUserStateEventBus eventBus) {
        if (eventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
            initData();
        }
    }

	public void initView() {
		setTitle(R.string.personal_title);

		rlNickName = (RelativeLayout) findViewById(R.id.rlNickName);
		rlName = (LinearLayout) findViewById(R.id.rlName);
		rlUpdateCity = (RelativeLayout) findViewById(R.id.rlUpdateCity);
		rlSex = (RelativeLayout) findViewById(R.id.rlSex);
		rlPersonality = (LinearLayout) findViewById(R.id.rlPersonality);
		rlBindEmail = (RelativeLayout) findViewById(R.id.rlBindEmail);
		rlBindPhone = (RelativeLayout) findViewById(R.id.rlBindPhone);
		tvUpdatePass = (TextView) findViewById(R.id.tvUpdatePass);
		ivHead = (CircleImageView) findViewById(R.id.ivHead);
		tvNickName = (TextView) findViewById(R.id.tvNickName);
		tvRealName = (TextView) findViewById(R.id.tvRealName);
		tvSex = (TextView) findViewById(R.id.tvSex);
		tvSignature = (TextView) findViewById(R.id.tvSignature);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvAccount = (TextView) findViewById(R.id.tvAccount);
		tvPhoneActivated = (TextView) findViewById(R.id.tvPhoneActivated);
		tvEmailActivated = (TextView) findViewById(R.id.tvEmailActivated);
		ivUpLoadHead = (CircleImageView) findViewById(R.id.ivUpLoadHead);
		mController = UMServiceFactory.getUMSocialService("com.umeng.login", RequestType.SOCIAL);

		rlNickName.setOnClickListener(this);
		rlName.setOnClickListener(this);
		rlPersonality.setOnClickListener(this);
		tvUpdatePass.setOnClickListener(this);
		rlUpdateCity.setOnClickListener(this);
		ivHead.setOnClickListener(this);
		rlSex.setOnClickListener(this);
		rlBindEmail.setOnClickListener(this);
		rlBindPhone.setOnClickListener(this);
		ivUpLoadHead.setOnClickListener(this);

		fileCache = new FileCache(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	public void initData() {
		tvAccount.setText("头像");
		tvNickName.setText(application.getUserInfo().getNick());
		tvRealName.setText(application.getUserInfo().getRealName());
        switch (application.getUserInfo().getGender()){
            case 0:tvSex.setText("保密");
                break;
            case 1:tvSex.setText("男");
                break;
            case 2:tvSex.setText("女");
                break;
        }
		application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivHead);

		tvSignature.setText(application.getUserInfo().getBrief());
		tvCity.setText(application.getUserInfo().getCity());
		if (application.getUserInfo().isPhoneActivated()) {
			tvPhoneActivated.setTextColor(getResources().getColor(R.color.txt_green));
			tvPhoneActivated.setText(application.getUserInfo().getPhone());
			rlBindPhone.setClickable(false);
		} else {
			tvPhoneActivated.setText("未绑定");
			rlBindPhone.setClickable(true);
		}
		if (application.getUserInfo().isEmailActivated()) {
			tvEmailActivated.setTextColor(getResources().getColor(R.color.txt_green));
			tvEmailActivated.setText(application.getUserInfo().getEmail());
			rlBindEmail.setClickable(false);
		} else {
			tvEmailActivated.setText("未绑定");
			rlBindEmail.setClickable(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rlNickName:// 修改昵称
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, UpdatePersonalActivity.class).putExtra("TAG", 1).putExtra("content", application.getUserInfo().getNick()));
			break;
		case R.id.rlName:// 修改姓名
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, UpdatePersonalActivity.class).putExtra("TAG", 2).putExtra("content", application.getUserInfo().getRealName()));
			break;
		case R.id.rlPersonality:// 修改个性签名
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, UpdatePersonalActivity.class).putExtra("TAG", 3).putExtra("content", application.getUserInfo().getBrief()));
			break;
		case R.id.tvUpdatePass:// 修改密码
			if (Utils.isFastDoubleClick())
				return;
			if (application.getUserInfo().getLoginType() == -1) {
				toActivity(UpdataPasswordActivity.class);
			} else {
				application.showMsg("第三方登录账号不允许修改密码");
			}
			break;
		case R.id.rlUpdateCity:// 修改城市
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, ChooseCityActivity.class).putExtra("tag", 2).putExtra("cityname", application.getUserInfo().getCity()));
			break;
		case R.id.ivHead:// 修改头像
			if (Utils.isFastDoubleClick())
				return;
			// if (application.getUserInfo().getLoginType() == 0) {
			// application.showMsg("第三方账号不需修改头像");
			// } else if (application.getUserInfo().getLoginType() == 1) {
			// application.showMsg("第三方账号不需修改头像");
			// } else {
			photoDialog();
			// }
			break;
		case R.id.ivUpLoadHead:
			if (Utils.isFastDoubleClick())
				return;
			photoDialog();
			break;
		case R.id.rlSex:// 修改性别
			if (Utils.isFastDoubleClick())
				return;
			sexDialog(application.getUserInfo().getGender());
			break;
		case R.id.rlBindPhone:// 绑定手机
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, BindPhoneActivity.class).putExtra("TAG", 1));
			break;
		case R.id.rlBindEmail:// 绑定email
			// startActivity(new Intent(mContext,
			// BindPhoneActivity.class).putExtra("TAG", 2));
			break;
		case R.id.tvPhotograph:// 拍照
			photoDialog.dismiss();
			OUTPUTFILEPATH = fileCache.getImageCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
			PickPhoto.takePhoto(PersonalActivity.this, OUTPUTFILEPATH);
			break;
		case R.id.tvAlbum:// 相册

			photoDialog.dismiss();

			// PickPhoto.pickPhoto(PersonalActivity.this);

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, PickPhoto.PICK_PHOTO);
			break;
		}
	}

	// 修改头像
	public void photoDialog() {
		photoDialog = new Dialog(mContext, R.style.dialog);
		photoDialog.setCancelable(true);
		photoDialog.setCanceledOnTouchOutside(true);
		photoDialog.setContentView(R.layout.dialog_select_photo);
		TextView tvPhotograph = (TextView) photoDialog.findViewById(R.id.tvPhotograph);
		TextView tvAlbum = (TextView) photoDialog.findViewById(R.id.tvAlbum);
		tvPhotograph.setOnClickListener(this);
		tvAlbum.setOnClickListener(this);
		DialogUtils.dialogSet(photoDialog, mContext, Gravity.BOTTOM, 1, 1, true, false, true);
		photoDialog.show();
	}

	// 修改性别
	public void sexDialog(int tag) {
		sexDialog = new Dialog(mContext, R.style.dialog);
		sexDialog.setCancelable(true);
		sexDialog.setCanceledOnTouchOutside(true);
		sexDialog.setContentView(R.layout.dialog_update_sex);
		RadioGroup radioGroup = (RadioGroup) sexDialog.findViewById(R.id.radioGroup);
		RadioButton radioButton1 = (RadioButton) sexDialog.findViewById(R.id.radioButton1);
		RadioButton radioButton2 = (RadioButton) sexDialog.findViewById(R.id.radioButton2);
        RadioButton radioButton0 = (RadioButton) sexDialog.findViewById(R.id.radioButton0);

        switch (tag){
            case 0:radioButton0.setChecked(true);
                break;
            case 1:radioButton1.setChecked(true);
                break;
            case 2:radioButton2.setChecked(true);
                break;
        }
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.radioButton1://男
					update(1, "Gender", 1 + "");
					break;
				case R.id.radioButton2://女
					update(1, "Gender", 2 + "");
					break;
                    case R.id.radioButton0://保密
                        update(1, "Gender", 0 + "");
                        break;
				}
			}
		});
		DialogUtils.dialogSet(sexDialog, mContext, Gravity.BOTTOM, 1, 1, true, false, true);
		sexDialog.show();
	}

	// 更新个人资料
	public void update(final int tag, String key, final String value) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(key, value));//向后台提交了性别数据
        Trace.d(TAG,"向后台提交了性别数据 value="+value);
		showProgress("正在修改资料");
		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
                    BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					UserInfo user = application.getUserInfo();
					if (messageInfo.isSuccess()) {
						if (tag == 1) {
                            switch (value){
                                case "0":tvSex.setText("保密");
                                    user.setGender(0);
                                    break;
                                case "1":tvSex.setText("男");
                                    user.setGender(1);
                                    break;
                                case "2":tvSex.setText("女");
                                    user.setGender(2);
                                    break;
                            }
							application.setUserInfo(user);
							application.showMsg(messageInfo.msg);
						}
					} else {
						application.showMsg(messageInfo.msg);
					}
					sexDialog.dismiss();
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (resultCode == Activity.RESULT_OK) {
			String path = null;
			switch (requestCode) {
			case PickPhoto.TAKE_PHOTO:
				path = ImageBig.scalePicture(mContext, OUTPUTFILEPATH, 600, 600);
				if (path != null) {
					setBitmap(path);
				}
				break;
			case PickPhoto.PICK_PHOTO:
				try {
					Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
					cursor.moveToFirst();
					path = ImageBig.scalePicture(mContext, cursor.getString(1), 600, 600);
					Intent intent = new Intent(mContext, ChoosePhotoActivity.class);
					intent.putExtra("imagePath", path);
					startActivityForResult(intent, PickPhoto.CROP_PHOTO);
					cursor.close();
					// if (path != null) {
					// setBitmap(path);
					// }
				} catch (Exception e) {
					application.showMsg("请选择相册中的照片");
				}
				break;
			case PickPhoto.CROP_PHOTO:
				if (data != null) {
					String temppath = data.getStringExtra("path");
					if (temppath != null) {
						setBitmap(temppath);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	private void setBitmap(String path) {
		ivHead.setVisibility(View.GONE);
		ivUpLoadHead.setVisibility(View.VISIBLE);
		Bitmap bitmap = ImageBig.getimage(path);
		ivUpLoadHead.setImageBitmap(bitmap);
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
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						MobclickAgent.onEvent(mContext, "change_personal_photo_count");
						UserInfo user = application.getUserInfo();
						user.setLogo(response.msg);
						user.setLogoLarge(response.msg);
						application.setUserInfo(user);
					} else {
						application.showMsg(response.msg);
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
