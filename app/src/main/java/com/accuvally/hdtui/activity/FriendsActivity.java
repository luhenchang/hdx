package com.accuvally.hdtui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MemberBean;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.EventFollow;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class FriendsActivity extends BaseActivity {

	private ListView mListView;
	private CommonAdapter<MemberBean> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		EventBus.getDefault().register(this);
		
		initProgress();
		setTitle("我的同伴");
		initView();
		requestMyfollowers();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listView);

		mAdapter = new CommonAdapter<MemberBean>(this, R.layout.listitem_friends) {

			@Override
			public void convert(ViewHolder viewHolder, final MemberBean item, final int position) {
				viewHolder.setText(R.id.tvFriendNick, item.Nick);
				viewHolder.setImageUrl(R.id.ivFriendAvatar, item.Logo, UILoptions.defaultUser);
				
				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, UserDetailActivity.class);
						intent.putExtra("position", position);
						intent.putExtra("id", item.Account);
						intent.putExtra("nick", item.Nick);
						intent.putExtra("avatarUrl", item.Logo);
						startActivity(intent);
					}
				});
			}
		};

		mListView.setAdapter(mAdapter);
		
		List<MemberBean> list = JSON.parseArray(application.sharedUtils.readString("friendList"), MemberBean.class);
		mAdapter.setList(list);
	}

	private void requestMyfollowers() {
		startProgress();
		httpCilents.get(Url.social_myfollowers, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						application.sharedUtils.writeString("friendList", response.result);
						List<MemberBean> list = JSON.parseArray(response.result, MemberBean.class);
						mAdapter.setList(list);
					}
				}
			}
		});
	}
	
	public void onEventMainThread(EventFollow event) {
		requestMyfollowers();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
