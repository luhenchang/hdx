package com.accuvally.hdtui.fragment.core;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.activity.message.NewFriendActivity;
import com.accuvally.hdtui.activity.message.NotificationActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.eventbus.EventRedDot;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MessageFragment extends BaseFragment implements OnClickListener {

	private SwipeMenuListView mListView;
	private CommonAdapter<SessionInfo> mAdapter;//消息fragme的adapter

	private final List<SessionInfo> list = new ArrayList<SessionInfo>();

	private SessionInfo notifyItemSession;
	private boolean hasAddOneNotify;

	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;
	
	private int unReadNum;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_message, container, false);
		EventBus.getDefault().register(this);
		initView(rootView);
		initData();
		return rootView;
	}

	private void initView(View view) {
		initListView(view);

		lyFailure = (LinearLayout) view.findViewById(R.id.lyFailure);
		tvNoData = (TextView) view.findViewById(R.id.tvNoData);
		ivFailure = (ImageView) view.findViewById(R.id.ivFailure);
		SquareBtn = (Button) view.findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("您还没有加入任何活动群哦\n快去参加新活动加入活动群吧");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);
		SquareBtn.setOnClickListener(this);
	}

	private void initListView(View view) {
		mListView = (SwipeMenuListView) view.findViewById(R.id.listview);
		mAdapter = new CommonAdapter<SessionInfo>(mContext, R.layout.listitem_main_right) {

			@Override
			public void convert(ViewHolder viewHolder, SessionInfo item, int position) {

				if (item.isAddNewFriend()) {
					viewHolder.setText(R.id.tvMainRightTitle, "新同伴");
					viewHolder.setText(R.id.tvMainRightContent, item.getTitle());
					viewHolder.setImageResource(R.id.ivMainRightLogoUrl, R.drawable.entrance_newfriend);
				} else if (item.isNotification()) {
					viewHolder.setText(R.id.tvMainRightTitle, "通知");
					viewHolder.setText(R.id.tvMainRightContent, item.getContent());
					viewHolder.setImageResource(R.id.ivMainRightLogoUrl, R.drawable.system_sessioin_entrance);
				} else {
					viewHolder.setText(R.id.tvMainRightTitle, item.getTitle());
					viewHolder.setText(R.id.tvMainRightContent, item.getTypeContent());
					if (item.isPrivateChat()) {
						viewHolder.setImageUrl(R.id.ivMainRightLogoUrl, item.getLogoUrl(), UILoptions.defaultUser);
					} else {
						viewHolder.setImageUrl(R.id.ivMainRightLogoUrl, item.getLogoUrl(), UILoptions.squareOptions);
					}
				}

				TextView tvUnreadNum = viewHolder.getView(R.id.tvUnreadNum);
				if (item.getUnReadNum() > 0) {
					tvUnreadNum.setVisibility(View.VISIBLE);
					tvUnreadNum.setText(item.getUnReadNum() + "");
				} else {
					tvUnreadNum.setVisibility(View.GONE);
				}

				viewHolder.setText(R.id.tvMainRightTime, item.getSessionTime());
			}
		};

		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);

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

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				SessionTable.deleteSessionById(list.get(position).getSessionId());
				if (notifyItemSession != null && position == list.indexOf(notifyItemSession)) {
					ToastUtil.showMsg("不能删除通知");
					return;
				}
				
				if (list.get(position).isAddNewFriend()) {
					ToastUtil.showMsg("不能删除新朋友");
					return;
				}
				
				list.remove(position);
				mAdapter.notifyDataSetChanged();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (Utils.isFastDoubleClick()) {
					return;
				}

				SessionInfo info = list.get(position);
				unReadNum = unReadNum - info.getUnReadNum();
				info.setUnReadNum(0);

				if (info.isAddNewFriend()) {
					SessionTable.clearNewFriendUnReadNum();
					startActivity(new Intent(mContext, NewFriendActivity.class));
				} else if (info.isNotification()) {
					SessionTable.clearNotifyUnReadNum();
					startActivity(new Intent(mContext, NotificationActivity.class));
				} else {
					SessionTable.updateSessionByUnReadNum(info.getSessionId());
					application.setCurrentSession(info);

					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("isPrivateChat", info.isPrivateChat());
					mContext.startActivity(intent);
				}

				mAdapter.notifyDataSetChanged();
				EventBus.getDefault().post(new EventRedDot(unReadNum));
			}
		});

	}

	public void initData() {
		if (application.checkIsLogin()) {
			lyFailure.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			new TaskQuerySession().execute();

		} else {
			lyFailure.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);

			tvNoData.setText("您还没有登入哦");
			SquareBtn.setVisibility(View.VISIBLE);
			SquareBtn.setText("登入");
		}
	}

	private void queryAllSession() {
		unReadNum = 0;
		hasAddOneNotify = false;
		int notifyUnReadNum = 0;
		
		List<SessionInfo> tempList = SessionTable.queryAllSession(AccountManager.getAccount());
		for (SessionInfo info : tempList) {
			unReadNum += info.getUnReadNum();
			if (info.isNotification()) {
				addOnceNotifyItem(info);
				notifyUnReadNum += info.getUnReadNum();
			} else {
				list.add(info);
			}
		}

		if (notifyItemSession != null) {
			notifyItemSession.setUnReadNum(notifyUnReadNum);
		}

	}

	private void addOnceNotifyItem(SessionInfo info) {
		if (!hasAddOneNotify) {
			notifyItemSession = info;
			list.add(info);
			hasAddOneNotify = true;
		}
	}

	public void onEventMainThread(ChangeUserStateEventBus eventBus) {
		list.clear();
		initData();
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
		list.clear();
		initData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
		if (!application.checkIsLogin()) {
			toActivity(LoginActivityNew.class);
		}
	}
	
	class TaskQuerySession extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			queryAllSession();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
			if (list.size() == 0) {
				tvNoData.setText("您还没有加入任何活动群哦\n快去参加新活动加入活动群吧");
				SquareBtn.setVisibility(View.GONE);
				lyFailure.setVisibility(View.VISIBLE);
				mListView.setEmptyView(lyFailure);
			}
		}
	}

}
