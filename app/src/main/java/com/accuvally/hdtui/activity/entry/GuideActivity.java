package com.accuvally.hdtui.activity.entry;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.ViewPagerAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.utils.ActivityUtils;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * 
 * @author Semmer Wang
 * 
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager guidePages;

	private LinearLayout mRadioGroup;

	private List<View> views;

	private ViewPagerAdapter vpAdapter;

	private ImageView[] imageViews = null;

	private ImageView imageView = null;

	private SwipeBackLayout mSwipeBackLayout;
	
	private TextView mSkip;

	private int tag;//tag 1为从设置界面进入， 0是第一次开机进入

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);
		initView();
		initData();
		setOnclickListioner();
	}
 
	
	public void initView() {
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.STATE_IDLE);
		guidePages = (ViewPager) findViewById(R.id.guidePages);
		mRadioGroup = (LinearLayout) findViewById(R.id.guide_dots);

		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.layout_guide1, null));
		views.add(inflater.inflate(R.layout.layout_guide2, null));
		views.add(inflater.inflate(R.layout.layout_guide3, null));
		tag = getIntent().getIntExtra("tag", 0);
		vpAdapter = new ViewPagerAdapter(views, this, tag);
		guidePages.setAdapter(vpAdapter);
		mSkip = (TextView) findViewById(R.id.tv_skip);
		if (tag == 1) {
			mSkip.setVisibility(View.GONE);
		}
	}

	private void setOnclickListioner(){
		mSkip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (tag != 1) {//首次启动app
					AccuApplication application = (AccuApplication) getApplicationContext();
					application.sharedUtils.writeBoolean("isSynchronous", true);
					application.sharedUtils.writeInt("remind", 1);
					application.sharedUtils.writeInt("isBaidu", 3);
					application.sharedUtils.writeBoolean("isRegDevice", false);
					application.sharedUtils.writeBoolean("isFirstIn", true);
					application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, Config.ACCUPASS_ID);
					application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, Config.ACCUPASS_KEY);

					ActivityUtils.toNext(GuideActivity.this);
				}
				finish();
			}
		});
	}
	public void initData() {
		imageViews = new ImageView[3];
		for (int i = 0; i < 3; i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(10, 5, 0, 5);
			imageView.setLayoutParams(lp);
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.home_new_cur);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.guide_radiogroup_normal);
			}
			mRadioGroup.addView(imageViews[i]);
		}
		guidePages.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i].setBackgroundResource(R.drawable.home_new_cur);
			if (arg0 != i) {
				imageViews[i].setBackgroundResource(R.drawable.guide_radiogroup_normal);
			}
		}
		
		if(tag != 1) {
			if (arg0 == imageViews.length - 1) {
				mSkip.setVisibility(View.GONE);
			} else {
				mSkip.setVisibility(View.VISIBLE);
			}
		}
	}

}
