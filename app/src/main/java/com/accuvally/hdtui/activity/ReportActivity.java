package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

public class ReportActivity extends BaseActivity implements OnCheckedChangeListener {

	private int type;
	private String account;
	private String reason;
	private EditText etReason;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		account = getIntent().getStringExtra("account");

		RadioGroup rgOne = (RadioGroup) findViewById(R.id.rgOne);
		RadioGroup rgTwo = (RadioGroup) findViewById(R.id.rgTwo);
		rgOne.setOnCheckedChangeListener(this);
		rgTwo.setOnCheckedChangeListener(this);

		etReason = (EditText) findViewById(R.id.etReason);
		findViewById(R.id.tvSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestReport();
			}
		});
	}

	protected void requestReport() {
		if (type == 0) {
			ToastUtil.showMsgShort("请选择举报类型");
			return;
		}

		if (TextUtils.isEmpty(reason)) {
			ToastUtil.showMsgShort("请选择举报原因");
			return;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("target", account));
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("reason", reason + " " + etReason.getText().toString()));

		showProgress("正在举报");
		httpCilents.postA(Url.social_report, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					ToastUtil.showMsg(response.msg);
					finish();
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbOne1:
			type = 1;
			break;
		case R.id.rbOne2:
			type = 2;
			break;

		case R.id.rbTwo1:
			reason = "色情低俗";
			break;
		case R.id.rbTwo2:
			reason = "广告骚扰";
			break;
		case R.id.rbTwo3:
			reason = "政治敏感";
			break;
		case R.id.rbTwo4:
			reason = "欺骗诈钱";
			break;
		case R.id.rbTwo5:
			reason = "违法(暴力恐怖、违禁物品)";
			break;
		}
	}

}
