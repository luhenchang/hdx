package com.accuvally.hdtui.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.EditTextWithDel;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改个人资料（个性签名、姓名、昵称）
 * 
 * @author Semmer Wang
 * 
 */
public class UpdatePersonalActivity extends BaseActivity implements TextWatcher, OnClickListener {

	private int TAG;

	private TextView tvPrompt, tvMaxLength;

	private Button updateBtn;

	private EditTextWithDel edUpdateContent;

	private int maxNum = 32;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_data);
		initView();
		initData();
	}

	public void initView() {

		tvPrompt = (TextView) findViewById(R.id.tvPrompts);
		tvMaxLength = (TextView) findViewById(R.id.tvMaxLength);
		edUpdateContent = (EditTextWithDel) findViewById(R.id.edUpdateContent);
		updateBtn = (Button) findViewById(R.id.updateBtn);

		edUpdateContent.addTextChangedListener(this);
		updateBtn.setOnClickListener(this);
		// setViewTouchListener(findViewById(R.id.linearlayout));
	}

	public void initData() {
		TAG = getIntent().getIntExtra("TAG", 0);
		String content = getIntent().getStringExtra("content");
		if (!"".equals(content)) {
			edUpdateContent.setText(content);
		}
		switch (TAG) {
		case 1:// 更改昵称
			setTitle(R.string.personal_update_nickname);
			tvPrompt.setText(R.string.personal_update_prompt2);
			edUpdateContent.setHint(R.string.personal_update_nickname_hint);
			maxNum = 12;
			// tvMaxLength.setVisibility(View.GONE);
			break;
		case 2:// 更改姓名
			setTitle(R.string.personal_update_name);
			tvPrompt.setText(R.string.personal_update_prompt1);
			edUpdateContent.setHint(R.string.personal_update_realname_hint);
			maxNum = 12;
			// tvMaxLength.setVisibility(View.GONE);
			break;
		case 3:// 更改个性签名
			setTitle(R.string.personal_personality);
			edUpdateContent.setHint(R.string.personal_update_before_hint);
			tvPrompt.setVisibility(View.GONE);
			break;
		}
		int destLen = edUpdateContent.getText().toString().length();
		if (destLen < maxNum) {
			tvMaxLength.setText((maxNum - destLen) + "");
		} else {
			tvMaxLength.setText("0");
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		if (TAG == 3) {
			edUpdateContent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxNum) });
			int destLen = arg0.toString().length();
			if (destLen < maxNum) {
				tvMaxLength.setText((maxNum - destLen) + "");
			} else {
				tvMaxLength.setText("0");
			}
		} else {
			int destLen = arg0.toString().length();
			if (destLen < maxNum) {
				tvMaxLength.setText((maxNum - destLen) + "");
			} else {
				tvMaxLength.setText("0");
			}
		}
//		String destStr = "";
//		for (int i = 0; i < arg0.toString().length(); i++) {
//			destStr += arg0.toString().charAt(i);
//			if (getCharacterNum(destStr) > maxNum) {
//				destStr = destStr.substring(0, destStr.length() - 1);
//				break;
//			}
//		}
//		arg0.delete(destStr.length(), arg0.toString().length());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	private int getCharacterNum(String content) {
		if (content == null) {
			return 0;
		} else {
			int length = 0;
			try {
				length = content.getBytes("GB2312").length;
			} catch (UnsupportedEncodingException e) {

			}
			return length;
		}
	}

	// 更新个人资料
	public void update(final int tag, String key, final String value) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(key, value));
		showProgress("正在修改资料");
		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSONObject.parseObject(result.toString(), BaseResponse.class);
					UserInfo user = application.getUserInfo();
					if (messageInfo.isSuccess()) {
						if (tag == 1) {
							application.showMsg("修改昵称成功");
							user.setNick(value);
						} else if (tag == 2) {
							application.showMsg("修改姓名成功");
							user.setRealName(value);
						} else {
							MobclickAgent.onEvent(mContext, "change_personal_slogan_count");//友盟记录 变更我的一句话次数
							application.showMsg("修改个性签名成功");
							user.setBrief(value);
						}
						application.setUserInfo(user);
						finish();
					} else {
						if (tag == 1) {
							application.showMsg("修改昵称失败");
						} else if (tag == 2) {
							application.showMsg("修改姓名失败");
						} else {
							application.showMsg("修改个性签名失败");
						}
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
		case R.id.updateBtn:
			String content = edUpdateContent.getText().toString().trim();
			if ("".equals(content)) {
				application.showMsg("请输入更改内容");
				return;
			}
			if (TAG == 1) {// 更改昵称
				update(1, "nick", content);
			} else if (TAG == 2) {// 更改姓名
				update(2, "realname", content);
			} else {// 更改个性签名
				update(3, "Brief", content);
			}
			break;
		}
	}
}
