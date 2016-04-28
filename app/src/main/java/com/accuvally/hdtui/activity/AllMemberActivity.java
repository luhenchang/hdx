package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.GroupMemberBean;
import com.accuvally.hdtui.model.MemberBean;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import de.greenrobot.event.EventBus;

public class AllMemberActivity extends BaseActivity {

	private SwipeMenuListView mListView;
	private CommonAdapter<MemberBean> mAdapter;
	private String cid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_member);
		initProgress();
		setTitle("全部群成员");
		cid = getIntent().getStringExtra("cid");
		initListView();
		requestAllMember();
	}

	private void initListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.listview);
		mAdapter = new CommonAdapter<MemberBean>(this, R.layout.listitem_friends) {

			@Override
			public void convert(ViewHolder viewHolder, final MemberBean item, final int position) {
				viewHolder.setText(R.id.tvFriendNick, item.Nick);
				viewHolder.setImageUrl(R.id.ivFriendAvatar, item.Logo, UILoptions.defaultUser);
			}
		};
		mListView.setAdapter(mAdapter);
		GroupMemberBean bean = (GroupMemberBean) getIntent().getSerializableExtra("bean");
//		mAdapter.setList(bean.member);
		if (AccountManager.getAccount().equals(bean.creator)) {
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {
					SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
					deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
					deleteItem.setWidth(SystemUtil.dpToPx(mContext, 90));
					deleteItem.setIcon(R.drawable.ic_delete);
					menu.addMenuItem(deleteItem);
				}
			};
			mListView.setMenuCreator(creator);
		}

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				MemberBean item = mAdapter.getItem(position);
				requestRemoveMember(position, item);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MemberBean item = mAdapter.getItem(position);

				Intent intent = new Intent(mContext, UserDetailActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("id", item.Account);
				intent.putExtra("nick", item.Nick);
				intent.putExtra("avatarUrl", item.Logo);
				startActivity(intent);
			}
		});
	}

	protected void requestRemoveMember(final int position, final MemberBean item) {
		showProgress("正在移除成员");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", cid));
		params.add(new BasicNameValuePair("member", item.Account));
		httpCilents.postA(Url.social_remove_member, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						mAdapter.remove(position);
						setTitle("全部群成员(" + mAdapter.getCount() + "人)");
						EventBus.getDefault().post(item);
					}
					ToastUtil.showMsg(response.msg);
					break;

				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	private void requestAllMember() {
		startProgress();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", cid));
		httpCilents.get(httpCilents.printURL(Url.social_all_members, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<MemberBean> listMember = JSON.parseArray(response.result, MemberBean.class);
						mAdapter.setList(listMember);
						setTitle("全部群成员(" + listMember.size() + "人)");
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
