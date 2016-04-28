package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.SessionAdapter;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;

import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CircleSessionActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;

	private SessionAdapter mAdapter;

	private List<SessionInfo> list;

	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_session);
		EventBus.getDefault().register(this);
		setTitle("我的圈子");
		initView();
		initDate();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("您还没有加入任何圈子哦\n快去参加新活动加入圈子吧");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);

		SquareBtn.setOnClickListener(this);
		mAdapter = new SessionAdapter(mContext);
		list = new ArrayList<SessionInfo>();
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
	}

	public void initDate() {
		List<SessionInfo> list = dbManager.querySession(application.getUserInfo().getAccount(), 0, Integer.MAX_VALUE);
		mAdapter.addAll(list);
		if (list.size() == 0) {
			lyFailure.setVisibility(View.VISIBLE);
			mListView.setEmptyView(lyFailure);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btLogin:
			toActivity(LoginActivityNew.class);
			break;
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			finish();
			break;
		}
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
		if (list.size() != 0) {
			mAdapter.removeAll();
		}
		initDate();
	}

	public void onEventMainThread(ChangeUserStateEventBus eventBus) {
		if (list.size() != 0) {
			mAdapter.removeAll();
		}
		initDate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
