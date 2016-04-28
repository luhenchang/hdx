package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.BaseResponse;
//import com.accuvally.hdtui.model.UserInfo;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.ImageBig;
//import com.accuvally.hdtui.utils.PickPhoto;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
//public class FirstPersonalActivity extends BaseActivity implements OnClickListener {
//
//	private ImageView ivHeadPortrait;
//	private EditText etNick;
//	private EditText etPersonality;
//	private LinearLayout llOpen;
//	private String nick;
//	private String personality;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_first_personal);
//
//		llOpen = (LinearLayout) findViewById(R.id.llOpen);
//		ivHeadPortrait = (ImageView) findViewById(R.id.ivHeadPortrait);
//		etNick = (EditText) findViewById(R.id.etNick);
//		etPersonality = (EditText) findViewById(R.id.etPersonality);
//		ivHeadPortrait.setOnClickListener(this);
//		llOpen.setOnClickListener(this);
//
//		findViewById(R.id.ivBack).setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.ivHeadPortrait:
//			Intent intent = new Intent();
//			intent.setType("image/*");
//			intent.setAction(Intent.ACTION_PICK);
//			startActivityForResult(intent, PickPhoto.PICK_PHOTO);
//			break;
//		case R.id.llOpen:
//			nick = etNick.getText().toString();
//			personality = etPersonality.getText().toString();
//			if (!TextUtils.isEmpty(nick) || !TextUtils.isEmpty(nick)) {
//				update();
//			}
//			break;
//		case R.id.ivBack:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (data == null)
//			return;
//		if (requestCode == PickPhoto.PICK_PHOTO) {
//			try {
//				Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
//				cursor.moveToFirst();
//				String path = ImageBig.scalePicture(mContext, cursor.getString(1), 600, 600);
//				Intent intent = new Intent(mContext, ChoosePhotoActivity.class);
//				intent.putExtra("imagePath", path);
//				startActivityForResult(intent, PickPhoto.CROP_PHOTO);
//				cursor.close();
//			} catch (Exception e) {
//				application.showMsg("请选择相册中的照片");
//			}
//		} else if (requestCode == PickPhoto.CROP_PHOTO) {
//			String temppath = data.getStringExtra("path");
//			if (temppath != null) {
//				setBitmap(temppath);
//			}
//		}
//	}
//
//	private void setBitmap(String path) {
//		Bitmap bitmap = ImageBig.getimage(path);
//		ivHeadPortrait.setImageBitmap(bitmap);
//		byte[] mContent = ImageBig.Bitmap2Bytes(bitmap);
//		uploadHead(mContent);
//	}
//
//	public void uploadHead(byte[] bytes) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("img", bytes == null ? "" : Base64.encodeToString(bytes, Base64.DEFAULT)));
//		showProgress("正在上传头像");
//		httpCilents.postA(Url.ACCUPASS_UPLOAD_HEAD, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
//					if (messageInfo.isSuccess()) {
//						application.showMsg(messageInfo.getMsg());
//						UserInfo user = application.getUserInfo();
//						user.setLogo(messageInfo.getResult());
//						user.setLogoLarge(messageInfo.getResult());
//						application.setUserInfo(user);
//					} else {
//						application.showMsg(messageInfo.getMsg());
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	// 更新个人资料
//	public void update() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("nick", nick));
//		params.add(new BasicNameValuePair("Brief", personality));
//		showProgress("正在更新资料");
//		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					BaseResponse messageInfo = JSONObject.parseObject(result.toString(), BaseResponse.class);
//					if (messageInfo.isSuccess()) {
//						UserInfo user = application.getUserInfo();
//						application.showMsg("更新资料成功");
//						user.setNick(nick);
//						user.setBrief(personality);
//						application.setUserInfo(user);
//					}
//
//					finish();
//				}
//			}
//		});
//	}
//}
