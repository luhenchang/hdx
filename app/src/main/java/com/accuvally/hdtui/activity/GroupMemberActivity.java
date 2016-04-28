package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.GroupMemberBean;
import com.accuvally.hdtui.model.MemberBean;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

public class GroupMemberActivity extends BaseActivity {

	private GridView mGridView;
	private CommonAdapter<MemberBean> mAdapter;
	private CheckBox cbDND;
	private ImageView ivDND;
	private TextView tvGroupName;
	private String title;
	private String cid;

	private Dialog dialog;
	private boolean isPrivateChat;
	private TextView tvAllMember;
	private GroupMemberBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_member);
		EventBus.getDefault().register(this);
		initProgress();
		isPrivateChat = getIntent().getBooleanExtra("isPrivateChat", false);
		initView();
		parseIntent();
		requestData();
	}

	private void parseIntent() {
		title = getIntent().getStringExtra("title");
		tvGroupName.setText(title);
		if (isPrivateChat) {
			findViewById(R.id.rlGroupName).setVisibility(View.GONE);
			setTitle("聊天详情");
		}
	}

	private void initView() {
		setGridView();

		tvAllMember = (TextView) findViewById(R.id.tvAllMember);
		tvGroupName = (TextView) findViewById(R.id.tvGroupName);
		View rlGroupAnnounce = findViewById(R.id.rlGroupAnnounce);
		View linAccDetail = findViewById(R.id.linAccDetail);
		if (isPrivateChat) {
			rlGroupAnnounce.setVisibility(View.GONE);
			linAccDetail.setVisibility(View.GONE);
		} else {
			rlGroupAnnounce.setVisibility(View.VISIBLE);
			linAccDetail.setVisibility(View.VISIBLE);
			rlGroupAnnounce.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GroupAnnounceActivity.class);
					intent.putExtra("cid", cid);
					intent.putExtra("creator", getIntent().getStringExtra("creator"));
					startActivity(intent);
				}
			});

			linAccDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
					intent.putExtra("id", cid);
					startActivity(intent );
				}
			});
		}

		ivDND = (ImageView) findViewById(R.id.ivDND);
		cbDND = (CheckBox) findViewById(R.id.cbDND);
		cbDND.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				application.sharedUtils.writeBoolean(application.getUserInfo().getAccount() + "_" + cid, isChecked);
				if (isChecked) {
					ivDND.setVisibility(View.VISIBLE);
				} else {
					ivDND.setVisibility(View.GONE);
				}
			}
		});

		cid = getIntent().getStringExtra("cid");
		cbDND.setChecked(application.sharedUtils.readBoolean(application.getUserInfo().getAccount() + "_" + cid, false));

		findViewById(R.id.btQuitCircle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Utils.isFastDoubleClick()) {
					requestQuitCircle();
				}
			}
		});

		tvAllMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bean != null) {
					Intent intent = new Intent(mContext, AllMemberActivity.class);
					intent.putExtra("cid", cid);
					intent.putExtra("bean", bean);
					startActivity(intent);
				}
			}
		});

	}

	private void setGridView() {
		mGridView = (GridView) findViewById(R.id.grid);
		mGridView.setAdapter(mAdapter = new CommonAdapter<MemberBean>(this, R.layout.griditem_group) {

			@Override
			public void convert(ViewHolder viewHolder, MemberBean item, int position) {
				viewHolder.setText(R.id.tvMemberNick, item.Nick);
				viewHolder.setImageUrl(R.id.ivMemberLogo, item.Logo, UILoptions.defaultUser);
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MemberBean bean = mAdapter.getList().get(position);
				if (!AccountManager.getAccount().equals(bean.Account)) {
					Intent intent = new Intent(mContext, UserDetailActivity.class);
					intent.putExtra("id", bean.Account);
					intent.putExtra("nick", bean.Nick);
					intent.putExtra("avatarUrl", bean.Logo);

					startActivity(intent);
				}
			}
		});
	}

	public void showDialog(final MemberBean info) {
		if (dialog == null) {
			dialog = new Dialog(mContext, R.style.DefaultDialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.dialog_chat_one);

			dialog.findViewById(R.id.ivCloseDialog).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		ImageView ivAvatar = (ImageView) dialog.findViewById(R.id.ivAvatar);
		TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
		ImageLoader.getInstance().displayImage(info.Logo, ivAvatar, UILoptions.defaultUser);
		tvName.setText(info.Nick);

		dialog.findViewById(R.id.btSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				SessionInfo privateSession = SessionTable.queryPrivateSession(info.Account, AccountManager.getAccount());
				if (privateSession == null) {
					requestCreateSession(info);
				} else {
					application.setCurrentSession(privateSession);
					ToChatActivity();
				}
			}
		});
		dialog.show();
	}

	private void requestCreateSession(final MemberBean info) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uid", AccountManager.getAccount()));
		params.add(new BasicNameValuePair("touid", info.Account));
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
						sessionInfo.setTitle(info.Nick);
						sessionInfo.setLogoUrl(info.Logo);// 对方头像
						sessionInfo.friendId = info.Account;
						application.setCurrentSession(sessionInfo);
						SessionTable.insertSession(sessionInfo);
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
		mContext.startActivity(intent);
	}

	// 读取活跃圈子成员
	private void requestData() {
		startProgress();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", cid));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_CIRCLE_MEMBER, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse resultInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (resultInfo.isSuccess()) {
						processResponse(resultInfo);
					}
					break;

				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	private void processResponse(BaseResponse resultInfo) {
		bean = JSON.parseObject(resultInfo.getResult(), GroupMemberBean.class);
		if (isPrivateChat) {
			setTitle("聊天详情");
			for (MemberBean memberBean : bean.member) {
				if (!memberBean.Account.equals(AccountManager.getAccount())) {
					mAdapter.add(memberBean);
				}
			}
		} else {
			mAdapter.setList(bean.member);
			setTitle("群信息(" + bean.total + "人)");
			tvAllMember.setText("全部群成员 (" + bean.total + "人)");
		}
	}

	// 删除并退出圈子
	protected void requestQuitCircle() {
		showProgress("正在删除并退出圈子");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", cid));
		httpCilents.postA(Url.ACCUPASS_QUIT_EVENT_CIRCLE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse resultInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (resultInfo.isSuccess()) {
						String userId = application.getUserInfo().getAccount();
						application.sharedUtils.delete(userId + "_" + cid);
						SessionTable.deleteSessionById(cid);
						EventBus.getDefault().post(new ChangeMessageEventBus());
						setResult(123);
						finish();
					} else {
						application.showMsg(resultInfo.getResult());
					}
					break;

				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	public void onEventMainThread(MemberBean item) {
		mAdapter.remove(item);
		--bean.total;
		setTitle("群信息(" + bean.total + "人)");
		tvAllMember.setText("全部群成员 (" + bean.total + "人)");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
