package com.accuvally.hdtui.activity.mine.setting;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.entry.GuideActivity;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.Utils;

/**
 * 关于活动行
 * 
 * @author Semmer Wang
 * 
 */
public class AboutAccuvallyActivity extends BaseActivity implements OnClickListener {

	private TextView tvQQ, tvWeixin, tvPhone, tvVersionName;

	private LinearLayout lyQQ, lyWeixin, lyGuide, lyPhone, ly_back, lySina;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_accuvally);
		initView();
		tvVersionName.setText(Util.getVersionName(mContext));
	}

	public void initView() {
		tvQQ = (TextView) findViewById(R.id.tvQQ);
		tvWeixin = (TextView) findViewById(R.id.tvWeixin);
		lyQQ = (LinearLayout) findViewById(R.id.lyQQ);
		lyWeixin = (LinearLayout) findViewById(R.id.lyWeixin);
		lySina = (LinearLayout) findViewById(R.id.lySina);
		lyGuide = (LinearLayout) findViewById(R.id.lyGuide);
		lyPhone = (LinearLayout) findViewById(R.id.lyPhone);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		ly_back = (LinearLayout) findViewById(R.id.ly_back);
		tvVersionName = (TextView) findViewById(R.id.tvVersionName);

		lyQQ.setOnClickListener(this);
		lyWeixin.setOnClickListener(this);
		lySina.setOnClickListener(this);
		lyGuide.setOnClickListener(this);
		lyPhone.setOnClickListener(this);
		ly_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lyQQ:
			if (Utils.isFastDoubleClick())
				return;
			copy(tvQQ.getText().toString().trim(), mContext);
			application.showMsg("已复制到剪贴板");
			break;
		case R.id.lyWeixin:
			if (Utils.isFastDoubleClick())
				return;
			copy(tvWeixin.getText().toString().trim(), mContext);
			application.showMsg("已复制到剪贴板");
			break;
		case R.id.lySina:
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.weibo.com/huodongxing")));
			break;
		case R.id.lyGuide:
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, GuideActivity.class).putExtra("tag", 1));
			break;
		case R.id.lyPhone:
			if (Utils.isFastDoubleClick())
				return;
			Uri telUri = Uri.parse("tel:" + tvPhone.getText().toString().trim());
			Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
			startActivity(intent);
			break;
		case R.id.ly_back:
			finish();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public static void copy(String content, Context context) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}
}
