package com.accuvally.hdtui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.adapter.SessionAdapter;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 会话列表
 * 
 * @author Semmer Wang
 * 
 */
public class SessionFragment extends BaseFragment implements OnClickListener {

	private ListView mListView;

	private SessionAdapter mAdapter;

	private List<SessionInfo> list;

	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_session, container, false);
		EventBus.getDefault().register(this);
		Log.i("info", "SessionFragment:onCreateView()");
		initView(view);
		initDate();
		return view;
	}

	public void initView(View view) {
		mListView = (ListView) view.findViewById(R.id.listview);
		lyFailure = (LinearLayout) view.findViewById(R.id.lyFailure);
		tvNoData = (TextView) view.findViewById(R.id.tvNoData);
		ivFailure = (ImageView) view.findViewById(R.id.ivFailure);
		SquareBtn = (Button) view.findViewById(R.id.SquareBtn);

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

	
	/**
	 * 从数据库查询所有的会话
	 */
	public void initDate() {
		List<SessionInfo> list = dbManager.querySession(application.getUserInfo().getAccount(), 0, Integer.MAX_VALUE);
		mAdapter.addAll(list);
		if(list.size()==0){
			lyFailure.setVisibility(View.VISIBLE);
			mListView.setEmptyView(lyFailure);
		} 
		
		//dbManager.queryUserSessions();
		//dbManager.querySessions();
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
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btLogin:
			toActivity(LoginActivityNew.class);
			break;
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			getActivity().finish();
			break;
		}
	}

}
