package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.FragmentViewPagerAdapter;
import com.accuvally.hdtui.fragment.session.CircleFragment;
import com.accuvally.hdtui.fragment.session.FriendsFragment;
import com.accuvally.hdtui.fragment.session.SessionFragment;
import com.accuvally.hdtui.ui.PagerSlidingTabStrip;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackLayout;

/**
 * 我的圈子
 * 
 * @author Semmer Wang
 * 
 */
public class SessionTabActivity extends BaseActivity {

	private ViewPager viewpager;
	private PagerSlidingTabStrip tabStrip;
	private DisplayMetrics dm;
	private List<Fragment> fragments;

	private SessionFragment sessionFragment;
	// private FriendsFragment friendsFragment;
	// private CircleFragment circleFragment;
	private FragmentViewPagerAdapter mAdapter;
	private String[] title = { "聊天" };

	// private SwipeBackLayout mSwipeBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session_tab);
		initView();
	}

	public void initView() {
		// mSwipeBackLayout = getSwipeBackLayout();
		// mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.STATE_IDLE);
		setTitle("我的圈子");
		dm = getResources().getDisplayMetrics();
		tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		fragments = new ArrayList<Fragment>();
		sessionFragment = new SessionFragment();
		// friendsFragment = new FriendsFragment();
		// circleFragment = new CircleFragment();
		fragments.add(sessionFragment);
		// fragments.add(friendsFragment);
		// fragments.add(circleFragment);
		mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), viewpager, fragments, title, "");
		setTabsValue();
	}

	private void setTabsValue() {
		viewpager.setAdapter(mAdapter);
		tabStrip.setViewPager(viewpager);
		tabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabStrip.setSelectedTextColor(getResources().getColor(R.color.txt_green));
	}

}
