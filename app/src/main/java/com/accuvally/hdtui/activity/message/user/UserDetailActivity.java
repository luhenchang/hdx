package com.accuvally.hdtui.activity.message.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.ApplyFriendActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.AccuBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MemberInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class UserDetailActivity extends BaseActivity implements OnClickListener {

	private TextView tvName;
	private TextView tvBrief;
	private TextView tvRegion;
	
	private ImageView ivUserLogo;
	private ImageView ivGender;
	private Button btSend;
	private EditText etReason;

	protected MemberInfo memberInfo;
	
	private LinearLayout linFollowOrg;
	private LinearLayout linFollows;
	private LinearLayout linPublished;
	private String nick;
	private String id;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		initProgress();

		id = getIntent().getStringExtra("id");
		nick = getIntent().getStringExtra("nick");
		setTitle("用户资料");

		String avatarUrl = getIntent().getStringExtra("avatarUrl");
		ivUserLogo = (ImageView) findViewById(R.id.ivUserLogo);
		ImageLoader.getInstance().displayImage(avatarUrl, ivUserLogo, UILoptions.defaultUser);

		initView();
		requestSocialUser(id);
	}

	private void initView() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvBrief = (TextView) findViewById(R.id.tvBrief);
		tvRegion = (TextView) findViewById(R.id.tvRegion);

		ivGender = (ImageView) findViewById(R.id.ivGender);
		btSend = (Button) findViewById(R.id.btSend);

		if (AccountManager.getAccount().equals(id)) {
			btSend.setVisibility(View.GONE);
		}

		linFollowOrg = (LinearLayout) findViewById(R.id.linFollowOrg);
		linFollows = (LinearLayout) findViewById(R.id.linFollows);
		linPublished = (LinearLayout) findViewById(R.id.linPublished);

		findViewById(R.id.linTopRight).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (memberInfo != null) {
					Intent intent = new Intent(mContext, UserSettingActivity.class);
					intent.putExtra("account", id);
					intent.putExtra("isparter", memberInfo.isparter);
					intent.putExtra("position", getIntent().getIntExtra("position", -1));
					startActivity(intent);
				}
			}
		});
	}

	private void requestSocialUser(String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));

		startProgress();
		httpCilents.get(httpCilents.printURL(Url.social_member, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						memberInfo = JSON.parseObject(response.result, MemberInfo.class);
						setViewData(memberInfo);
					} else {
						ToastUtil.showMsg(response.msg);
					}
				} else {
					ToastUtil.showMsg("网络连接断开，请检查网络");
				}
			}
		});
	}

	private void setViewData(MemberInfo memberInfo) {
		if (memberInfo == null)
			return;

		tvName.setText(memberInfo.nick);
		tvBrief.setText(memberInfo.brief);
		tvRegion.setText(memberInfo.region);
		ImageLoader.getInstance().displayImage(memberInfo.logo, ivUserLogo, UILoptions.defaultUser);

		/*if (memberInfo.gender == 0) {
			ivGender.setImageResource(R.drawable.women);
		} else if (memberInfo.gender == 1) {
			ivGender.setImageResource(R.drawable.man);
		} else {
			ivGender.setVisibility(View.GONE);
		}*/
        Trace.e("PersonalActivity","memberInfo.gender="+memberInfo.gender);
        switch (memberInfo.gender){
            case 0:
                ivGender.setImageResource(R.drawable.secret);
                break;
            case 1:
                ivGender.setImageResource(R.drawable.man);
                break;
            case 2:
                ivGender.setImageResource(R.drawable.women);
                break;
        }

		if (memberInfo.isparter) {
			btSend.setText("发消息");
		} else {
			btSend.setText("添加同伴");
		}

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, dm);
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);

		add(linFollowOrg, memberInfo.followorgs);
		add(linFollows, memberInfo.follows);
		add(linPublished, memberInfo.published);

		btSend.setOnClickListener(this);
		linFollowOrg.setOnClickListener(this);
		linFollows.setOnClickListener(this);
		linPublished.setOnClickListener(this);
	}

	private void add(LinearLayout lin, List<AccuBean> list) {
		if (list == null || list.isEmpty()) {
			lin.setVisibility(View.GONE);
			return;
		}

		lin.setVisibility(View.VISIBLE);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, dm);
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);

		for (AccuBean bean : list) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(size, size);
			params.leftMargin = margin;
			imageView.setLayoutParams(params);

			ImageLoader.getInstance().displayImage(bean.logo, imageView, UILoptions.squareOptions);
			lin.addView(imageView);
		}
	}

	private void requestCreateSession(final MemberInfo info) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uid", AccountManager.getAccount()));
		params.add(new BasicNameValuePair("touid", info.account));
		httpCilents.postA(Url.SOCIAL_CONV, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						SessionInfo sessionInfo = new SessionInfo();
						sessionInfo.userId = AccountManager.getAccount();
						sessionInfo.setSessionId(response.result);

						sessionInfo.setTime(System.currentTimeMillis());
						sessionInfo.setTitle(info.nick);
						sessionInfo.setLogoUrl(info.logo);// 对方头像
						sessionInfo.friendId = info.account;
						application.setCurrentSession(sessionInfo);
						SessionTable.insertOrUpdateSession(sessionInfo);
						EventBus.getDefault().post(new ChangeMessageEventBus());

						ToChatActivity();
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	private void ToChatActivity() {
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.putExtra("isPrivateChat", true);// 私聊
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (memberInfo == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.linFollowOrg:
			Intent intentOrg = new Intent(this, UserOrgListActivity.class);
			intentOrg.putExtra("title", nick + " 关注的主办方");
			intentOrg.putExtra("account", id);
			startActivity(intentOrg);
			break;
		case R.id.linFollows:
			Intent intent = new Intent(this, UserAccuActivity.class);
			intent.putExtra("isFollows", true);
			intent.putExtra("account", id);
			intent.putExtra("title", nick + " 关注的活动");
			startActivity(intent);
			break;
		case R.id.linPublished:
			Intent intent1 = new Intent(this, UserAccuActivity.class);
			intent1.putExtra("isFollows", false);
			intent1.putExtra("account", id);
			intent1.putExtra("title", nick + " 发布的活动");
			startActivity(intent1);
			break;
		case R.id.btSend:
			if (memberInfo.isparter) {
				SessionInfo privateSession = SessionTable.queryPrivateSession(memberInfo.account, AccountManager.getAccount());
				if (privateSession == null) {
					requestCreateSession(memberInfo);
				} else {
					application.setCurrentSession(privateSession);
					ToChatActivity();
				}
			} else {
				Intent intent2 = new Intent(this, ApplyFriendActivity.class);
				intent2.putExtra("account", id);
				startActivity(intent2);
			}
			break;
		}
	}

}
