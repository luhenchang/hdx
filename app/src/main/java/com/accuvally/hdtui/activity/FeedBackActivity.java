package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.CheckTextBox;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

/**
 * 意见反馈
 * 
 * @author Semmer Wang
 * 
 */
public class FeedBackActivity extends BaseActivity implements TextWatcher, OnClickListener {


	private EditText edContent, edEmail;

	private Button addAccuvallyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	public void initView() {
		setTitle(R.string.setting_feedback);
		edContent = (EditText) findViewById(R.id.edContent);
		edEmail = (EditText) findViewById(R.id.edEmail);
		addAccuvallyBtn = (Button) findViewById(R.id.addAccuvallyBtn);

		// edContent.addTextChangedListener(this);
		addAccuvallyBtn.setOnClickListener(this);
		// setViewTouchListener(findViewById(R.id.scrollview));
		if (application.checkIsLogin()) {
			edEmail.setText(application.getUserInfo().getPhone());
		}
		
		findViewById(R.id.btQuestion).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.addAccuvallyBtn:// 反馈
			String content = edContent.getText().toString().trim();
			String email = edEmail.getText().toString().trim();
			if ("".equals(content)) {
				application.showMsg("您还未输入意见");
				return;
			}

			// if ("".equals(email)) {
			// application.showMsg("您还未输入联系方式");
			// return;
			// }
			if (!"".equals(email)) {
				if (CheckTextBox.isEmail(email) || CheckTextBox.isPhoneNumberValid(email) || CheckTextBox.isQQ(email)) {
					feedback(content, email);
				} else {
					application.showMsg("请输入正确的联系方式");
				}
			} else {
				feedback(content, "");
			}
			break;
			
		case R.id.btQuestion:
			toActivity(QuestionActivity.class);
			break;
		}
	}

	public void feedback(String content, String email) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("contact", email));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("source", "1"));//来源，1表示活动行App
		params.add(new BasicNameValuePair("deviceid", application.getIMEI()));
		showProgress("正在上传您的反馈意见");
		httpCilents.postA(Url.ACCUPASS_FEEDBACK, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					application.showMsg(messageInfo.getMsg());
					if (messageInfo.isSuccess()) {
						finish();
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
	public void afterTextChanged(Editable arg0) {
		// int destLen = getCharacterNum(arg0.toString());
		// if (destLen < maxNum) {
		// tvMaxLength.setText((maxNum - destLen) + "");
		// } else {
		// tvMaxLength.setText("0");
		// }
		// String destStr = "";
		// for (int i = 0; i < arg0.toString().length(); i++) {
		// destStr += arg0.toString().charAt(i);
		// if (getCharacterNum(destStr) > maxNum) {
		// destStr = destStr.substring(0, destStr.length() - 1);
		// break;
		// }
		// }
		// arg0.delete(destStr.length(), arg0.toString().length());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	// private int getCharacterNum(String content) {
	// if (content == null) {
	// return 0;
	// } else {
	// int length = 0;
	// try {
	// length = content.getBytes("GB2312").length;
	// } catch (UnsupportedEncodingException e) {
	//
	// }
	// return length;
	// }
	// }

}
