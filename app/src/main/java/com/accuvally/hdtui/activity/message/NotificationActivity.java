package com.accuvally.hdtui.activity.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.activity.mine.FriendsActivity;
import com.accuvally.hdtui.activity.home.ProjectDetailsActivity;
import com.accuvally.hdtui.activity.mine.TicketTabActivity;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 消息通知
 * 
 * @author Semmer Wang
 * 
 */
public class NotificationActivity extends BaseActivity {

	private SwipeMenuListView mListView;

	private List<SessionInfo> mList = new ArrayList<SessionInfo>();

	private CommonAdapter<SessionInfo> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
		EventBus.getDefault().register(this);
		initView();
		initDate();
	}

	public void initView() {
		setTitle("通知");
		mListView = (SwipeMenuListView) findViewById(R.id.listview);
//		mAdapter = new NotificationAdapter(mContext);

		mAdapter = new CommonAdapter<SessionInfo>(this, R.layout.listitem_main_right) {

			@Override
			public void convert(ViewHolder viewHolder, final SessionInfo item, int position) {
				viewHolder.setText(R.id.tvMainRightTitle, item.getTitle());
				viewHolder.setText(R.id.tvMainRightContent, item.getContent());
				viewHolder.setImageUrl(R.id.ivMainRightLogoUrl, item.getLogoUrl(), UILoptions.squareOptions);

				viewHolder.setText(R.id.tvMainRightTime, item.getSessionTime());
			}
		};
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
				List<SessionInfo> list = mAdapter.getList();
				SessionTable.deleteSessionById(list.get(position).getSessionId());
				mAdapter.remove(position);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (Utils.isFastDoubleClick()) {
					return;
				}

				SessionInfo info = mAdapter.getList().get(position);

				switch (info.getOp_type()) {
				case 1:// 活动
					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getOp_value()));
					break;
				case 2:// 专题
					Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
					intent.putExtra("title", info.getTitle());
					intent.putExtra("id", info.getOp_value());
					mContext.startActivity(intent);
					break;
				case 3:// 圈子
					SessionInfo session = SessionTable.querySessionById(info.getOp_value());
					if (session == null) {
						session = new SessionInfo();
						session.setSessionId(info.getOp_value());
						session.setTitle(info.getTitle());
						session.setLogoUrl(info.getLogoUrl());
						SessionTable.insertSession(session);
					}

					application.setCurrentSession(session);
					mContext.startActivity(new Intent(mContext, ChatActivity.class));

					break;
				case 4:// 主办方
					mContext.startActivity(new Intent(mContext, ProjectDetailsActivity.class).putExtra("orgId", info.getOp_value()));
					break;
				case 5:// 网页
					mContext.startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl",
							info.getOp_value()).putExtra("injectJs", ""));
					break;
				case 6:// 票券
					mContext.startActivity(new Intent(mContext, TicketTabActivity.class));
					break;
				case 9:// 同伴(朋友)列表
					toActivity(FriendsActivity.class);
					break;
				}
			}
		});
	}

	public void initDate() {
//		new MyTask().execute();
		List<SessionInfo> list = SessionTable.queryAllNotification(AccountManager.getAccount());
		mAdapter.setList(list);
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
		if (mList.size() != 0) {
			mAdapter.removeAll();
		}
		initDate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	class MyTask extends AsyncTask<Void, Void, List> {

		@Override
		protected List doInBackground(Void... params) {
			List<SessionInfo> list = SessionTable.queryAllNotification(AccountManager.getAccount());
			return list;
		}

		@Override
		protected void onPostExecute(List list) {
			mAdapter.addAll(list);
		}
	}
}
