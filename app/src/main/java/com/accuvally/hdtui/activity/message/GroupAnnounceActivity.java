package com.accuvally.hdtui.activity.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
//群公告
public class GroupAnnounceActivity extends BaseActivity {

	private String cid;
	private EditText etAnnounce;
	private TextView tvSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_announce);

		cid = getIntent().getStringExtra("cid");

//		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
//		key = time + cid;
//		tvCount = (TextView) findViewById(R.id.tvCount);
//		tvCount.setVisibility(View.GONE);
//		count = SPUtil.getInt(key, 0);
//		tvCount.setText(getString(R.string.announce_count, count));

//		nick = getIntent().getStringExtra("nick");
//		String avatarUrl = getIntent().getStringExtra("avatarUrl");
//		ImageView ivUserLogo = (ImageView) findViewById(R.id.ivUserLogo);
//		ImageLoader.getInstance().displayImage(avatarUrl, ivUserLogo, UILoptions.defaultUser);

		String announceContent = application.sharedUtils.readString(Keys.announceContent + cid);
		etAnnounce = (EditText) findViewById(R.id.etAnnounce);
		etAnnounce.setText(announceContent);

		tvSend = (TextView) findViewById(R.id.tvSend);
		String creator = getIntent().getStringExtra("creator");
		tvSend.setVisibility(View.GONE);
		if (AccountManager.getAccount().equals(creator)) {
			tvSend.setVisibility(View.VISIBLE);
		}
		
		tvSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("编辑".equals(tvSend.getText())) {
					tvSend.setText("发布");
					etAnnounce.setEnabled(true);
					SystemUtil.showSoftInput(mContext, etAnnounce);
				} else {
					publishAnnouncement();
				}
			}
		});
	}

	protected void publishAnnouncement() {
		String content = etAnnounce.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.showMsg("发布的内容不能为空");
			return;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", cid));
		params.add(new BasicNameValuePair("content", content));

		showProgress("正在发布群公告");
		httpCilents.postA(Url.announcement_publish, params, new WebServiceCallBack() {

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
}
