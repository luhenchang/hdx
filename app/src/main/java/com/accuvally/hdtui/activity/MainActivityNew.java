package com.accuvally.hdtui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.fragment.HomeFragment;
import com.accuvally.hdtui.fragment.MessageFragment;
import com.accuvally.hdtui.fragment.MineFragment;
import com.accuvally.hdtui.fragment.SelectFragment;
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.EventRedDot;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackLayout;

import de.greenrobot.event.EventBus;

/**
 * 主页
 * 
 * @author Semmer Wang
 * 
 */
public class MainActivityNew extends BaseActivity implements OnClickListener {

	private TextView tvMainBottomHome, tvMainBottomSelect,
            tvMainBottomSession, tvMainBottomMessage;

	private FragmentManager fragmentManager;

	private Fragment homeFragment;//推荐
	private Fragment selectFragment;//寻找
	private Fragment messageFragment;//消息
	private Fragment mineFragment;//我的

	private long exitTime = 0;

	private ImageView tvMainHeader;

	private TextView tvMainHeaderCity;

	private int IS_HOME_TEMP = 0;

	private ImageView ivMainHeaderSearch;

	AnimationDrawable animationDrawable;

	private ImageView ivMainHeaderLeft;

//	public static SlidingMenu slidingMenu;

	private boolean isSlidingMenuOpen = true;

	private SwipeBackLayout mSwipeBackLayout;

	private ImageView ivMainHeaderUnfinished;


	private FrameLayout rlMainHeaderMessage;

	private TextView tvMainMessageUnReadNum;

	private ImageView ivMainUnReadNum;

	private View main_top_view;

	private RelativeLayout header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_new);
		application.sharedUtils.writeBoolean("isFirstIn", true);
		EventBus.getDefault().register(this);
		initProgress();
		fragmentManager = getSupportFragmentManager();
		if (savedInstanceState != null) {
			homeFragment = fragmentManager.findFragmentByTag("homeFragment");
			selectFragment = fragmentManager.findFragmentByTag("selectFragment");
			messageFragment = fragmentManager.findFragmentByTag("messageFragment");
			mineFragment = fragmentManager.findFragmentByTag("mineFragment");
		}
//		initSlidingMenu();
		initView();
		initData();//定位城市
		initGetuiPush();
		// 日历的小黄点用到
		ManagerFragmentHelp.init();
		if (application.checkIsLogin()) {
			application.leanCloudLogin(application.getUserInfo().getAccount());
		}
		updateUnreadNum();
		
		int tabIndex = getIntent().getIntExtra("tabIndex", 0);
		selection(tabIndex);
		
		ToAccuvallyDetail(getIntent());
	}

	private void ToAccuvallyDetail(Intent intent) {
		String id = intent.getStringExtra("id");
		
		if (!TextUtils.isEmpty(id)) {
			Intent aIntent = new Intent(this, AccuvallyDetailsActivity.class);
			aIntent.putExtra("id", id);
			startActivity(aIntent);
		}
	}

	// SDK初始化，第三方程序启动时，都要进行SDK初始化   （个推）
	private void initGetuiPush() {
		com.igexin.sdk.PushManager.getInstance().initialize(this);
	}

	public void initView() {
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.STATE_IDLE);

		tvMainBottomHome = (TextView) findViewById(R.id.tvMainBottomHome);
		tvMainBottomSelect = (TextView) findViewById(R.id.tvMainBottomSelect);
		tvMainBottomSession = (TextView) findViewById(R.id.tvMainBottomSession);
		tvMainBottomMessage = (TextView) findViewById(R.id.tvMainBottomMessage);
		tvMainHeaderCity = (TextView) findViewById(R.id.tvMainHeaderCity);
		ivMainHeaderSearch = (ImageView) findViewById(R.id.ivMainHeaderSearch);
		ivMainHeaderLeft = (ImageView) findViewById(R.id.ivMainHeaderLeft);
		ivMainHeaderUnfinished = (ImageView) findViewById(R.id.ivMainHeaderUnfinished);
		ivMainUnReadNum = (ImageView) findViewById(R.id.ivMainUnReadNum);
		rlMainHeaderMessage = (FrameLayout) findViewById(R.id.rlMainHeaderMessage);
		tvMainMessageUnReadNum = (TextView) findViewById(R.id.tvMainMessageUnReadNum);
		tvMainHeader = (ImageView) findViewById(R.id.tvMainHeader);
		main_top_view = (View) findViewById(R.id.main_top_view);
		header = (RelativeLayout) findViewById(R.id.header);

		tvMainBottomHome.setOnClickListener(this);
		tvMainBottomSelect.setOnClickListener(this);
		tvMainBottomSession.setOnClickListener(this);
		findViewById(R.id.linClickTv).setOnClickListener(this);
		
		tvMainHeaderCity.setOnClickListener(this);
		ivMainHeaderLeft.setOnClickListener(this);
		ivMainHeaderSearch.setOnClickListener(this);
		ivMainHeaderUnfinished.setOnClickListener(this);
		rlMainHeaderMessage.setOnClickListener(this);
		tvMainMessageUnReadNum.setVisibility(View.GONE);
	}

    //定位城市
	public void initData() {
		if (application.sharedUtils.readString("cityName") != null) {
			if("香港特别行政区".equals(application.sharedUtils.readString("cityName"))){
				tvMainHeaderCity.setText("香港");
			}else{
				tvMainHeaderCity.setText(application.sharedUtils.readString("cityName"));
			}
		} else {
			tvMainHeaderCity.setText("定位中..");
		}
	}

	public void onEventMainThread(ChangeCityEventBus eventBus) {
		if (TextUtils.isEmpty(eventBus.getCity())) {
			tvMainHeaderCity.setText("全国");
		} else {
			if ("香港特别行政区".equals(eventBus.getCity())) {
				tvMainHeaderCity.setText("香港");
			} else {
				tvMainHeaderCity.setText(eventBus.getCity());
			}
		}
	}

	public void onEventMainThread(ChangeBackHomeEventBus eventBus) {
		selection(eventBus.getMsg());
	}

	public void onEventMainThread(ChangeHomeLoaderEventBus eventBus) {
		if (eventBus.isMsg()) {
			startProgress();
		} else {
			stopProgress();
		}
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
		updateUnreadNum();
	}
	
	public void onEventMainThread(EventRedDot event) {
		if (event.isShow) {
			ivMainUnReadNum.setVisibility(View.VISIBLE);
		} else {
			ivMainUnReadNum.setVisibility(View.GONE);
		}
	}

	public void updateUnreadNum() {
		if (SessionTable.hasUnReadMessage()) {
			ivMainUnReadNum.setVisibility(View.VISIBLE);
		} else {
			ivMainUnReadNum.setVisibility(View.GONE);
		}
	}

	public void onEventMainThread(ChangeMainSelectEventBus eventBus) {
		if (eventBus.getTag() == 1) {
			selection(0);
		} else if (eventBus.getTag() == 2) {
			selection(1);
		} else if (eventBus.getTag() == 3) {
			selection(3);
		} else if (eventBus.getTag() == 0) {
			selection(2);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvMainBottomHome:
			selection(0);
			break;
		case R.id.tvMainBottomSelect:
			selection(1);
			break;
		case R.id.linClickTv:
			selection(2);
			break;
		case R.id.tvMainBottomSession:
			selection(3);
			break;
		case R.id.tvMainHeaderCity://选择城市
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				application.showMsg(R.string.network_check);
				return;
			}
			toActivity(new Intent(mContext, ChooseCityActivity.class).
                    putExtra("tag", 1).putExtra("cityname", tvMainHeaderCity.getText().toString()));
			break;
		case R.id.ivMainHeaderLeft:// 设置
//			slidingMenu.showMenu();
			toActivity(SettingActivity.class);
			break;
		case R.id.ivMainHeaderSearch:
			if (Utils.isFastDoubleClick())
				return;
			startActivity(new Intent(mContext, SearchActivityNew.class));
			break;
		case R.id.ivMainHeaderUnfinished://我的票卷
			if (Utils.isFastDoubleClick())
				return;
			if (application.checkIsLogin()) {
				startActivity(new Intent(mContext, UnfinishedActivity.class));
			} else {
				toActivity(LoginActivityNew.class);
			}
			break;
//		case R.id.rlMainHeaderMessage:
//			if (Utils.isFastDoubleClick())
//				return;
//			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_NOTICATION", ""));
//			List<SessionInfo> list = dbManager.querySession(application.getUserInfo().getAccount(), 1, Integer.MAX_VALUE);
//			for (int i = 0; i < list.size(); i++) {
//				dbManager.updateSessionByUnReadNum(list.get(i).getSessionId());
//			}
//			tvMainMessageUnReadNum.setVisibility(View.GONE);
//			if (application.checkIsLogin()) {
//				startActivity(new Intent(mContext, NotificationActivity.class));
//			} else {
//				toActivity(LoginActivityNew.class);
//			}
//			break;
		}
	}

	public void selection(int selection) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.push_left_in,
		// R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out);
		hideFragments(transaction);
		switch (selection) {
		case 0:
			stDrawable(0);
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				transaction.add(R.id.content, homeFragment, "homeFragment");
			} else {

				transaction.show(homeFragment);
				if (homeFragment.isAdded()) {
					homeFragment.onResume();
				}
				if (selectFragment != null) {
					transaction.hide(selectFragment);
				}
				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				if (messageFragment != null) {
					transaction.hide(messageFragment);
				}
			}
			changeHeader(0);
			break;
		case 1:
			stDrawable(1);
			if (selectFragment == null) {
				selectFragment = new SelectFragment();
				transaction.add(R.id.content, selectFragment, "selectFragment");
			} else {
				transaction.show(selectFragment);
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}
				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
				if (messageFragment != null) {
					transaction.hide(messageFragment);
				}
			}
			changeHeader(1);
			break;
		case 2:
			stDrawable(2);
			if (messageFragment == null) {
				messageFragment = new MessageFragment();
				transaction.add(R.id.content, messageFragment, "messageFragment");
			} else {
				transaction.show(messageFragment);
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}
				if (selectFragment != null) {
					transaction.hide(selectFragment);
				}
				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}
			}
			changeHeader(2);
			break;
		case 3:
			stDrawable(3);
			if (mineFragment == null) {
				mineFragment = new MineFragment();
				transaction.add(R.id.content, mineFragment, "mineFragment");
			} else {
				transaction.show(mineFragment);
				if (homeFragment != null) {
					transaction.hide(homeFragment);
				}
				if (selectFragment != null) {
					transaction.hide(selectFragment);
				}
				if (messageFragment != null) {
					transaction.hide(messageFragment);
				}
			}
			changeHeader(3);
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (selectFragment != null) {
			transaction.hide(selectFragment);
		}
		if (mineFragment != null) {
			transaction.hide(mineFragment);
		}
		if (messageFragment != null) {
			transaction.hide(messageFragment);
		}
	}

	public void stDrawable(int index) {
		if (index == 0) {
			Drawable drawable = getResources().getDrawable(R.drawable.fragment_bottom_home_selected);
			Drawable selNormal = getResources().getDrawable(R.drawable.fragment_bottom_select_normal);
			Drawable mangerNormal = getResources().getDrawable(R.drawable.hometab_third_normal);
			Drawable sessionNormal = getResources().getDrawable(R.drawable.fragment_bottom_circle_normal);
			tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null, selNormal, null, null);
			tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null, mangerNormal, null, null);
			tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null, sessionNormal, null, null);
			tvMainBottomHome.setTextColor(getResources().getColor(R.color.home_bottom_color_selected));
			tvMainBottomSelect.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomMessage.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSession.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
		} else if (index == 1) {
			Drawable drawable = getResources().getDrawable(R.drawable.fragment_bottom_select_selected);
			Drawable homeNormal = getResources().getDrawable(R.drawable.fragment_bottom_home_normal);
			Drawable mangerNormal = getResources().getDrawable(R.drawable.hometab_third_normal);
			Drawable sessionNormal = getResources().getDrawable(R.drawable.fragment_bottom_circle_normal);
			tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null, homeNormal, null, null);
			tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null, mangerNormal, null, null);
			tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null, sessionNormal, null, null);
			tvMainBottomHome.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSelect.setTextColor(getResources().getColor(R.color.home_bottom_color_selected));
			tvMainBottomMessage.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSession.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
		} else if (index == 2) {
			Drawable homeNormal = getResources().getDrawable(R.drawable.fragment_bottom_home_normal);
			Drawable selNormal = getResources().getDrawable(R.drawable.fragment_bottom_select_normal);
			Drawable drawable = getResources().getDrawable(R.drawable.hometab_third_selected);
			Drawable sessionNormal = getResources().getDrawable(R.drawable.fragment_bottom_circle_normal);
			tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null, homeNormal, null, null);
			tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null, selNormal, null, null);
			tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null, sessionNormal, null, null);
			tvMainBottomHome.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSelect.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomMessage.setTextColor(getResources().getColor(R.color.home_bottom_color_selected));
			tvMainBottomSession.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
		} else if (index == 3) {
			Drawable homeNormal = getResources().getDrawable(R.drawable.fragment_bottom_home_normal);
			Drawable selNormal = getResources().getDrawable(R.drawable.fragment_bottom_select_normal);
			Drawable mangerNormal = getResources().getDrawable(R.drawable.hometab_third_normal);
			Drawable drawable = getResources().getDrawable(R.drawable.fragment_bottom_circle_selected);
			tvMainBottomHome.setCompoundDrawablesWithIntrinsicBounds(null, homeNormal, null, null);
			tvMainBottomSelect.setCompoundDrawablesWithIntrinsicBounds(null, selNormal, null, null);
			tvMainBottomMessage.setCompoundDrawablesWithIntrinsicBounds(null, mangerNormal, null, null);
			tvMainBottomSession.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			tvMainBottomHome.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSelect.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomMessage.setTextColor(getResources().getColor(R.color.home_bottom_color_normal));
			tvMainBottomSession.setTextColor(getResources().getColor(R.color.home_bottom_color_selected));
		}
	}

	public void changeHeader(int index) {
		if (index == 0) {
			IS_HOME_TEMP = 0;
			ivMainHeaderSearch.setVisibility(View.VISIBLE);
			tvMainHeaderCity.setVisibility(View.VISIBLE);
			ivMainHeaderUnfinished.setVisibility(View.GONE);
			rlMainHeaderMessage.setVisibility(View.GONE);
			main_top_view.setVisibility(View.VISIBLE);
			header.setBackgroundColor(getResources().getColor(R.color.main_top_bg));
			tvMainHeader.setVisibility(View.VISIBLE);
			ivMainHeaderLeft.setVisibility(View.GONE);
		} else if (index == 1) {
			IS_HOME_TEMP = 1;
			ivMainHeaderSearch.setVisibility(View.VISIBLE);
			tvMainHeaderCity.setVisibility(View.VISIBLE);
			ivMainHeaderUnfinished.setVisibility(View.GONE);
			rlMainHeaderMessage.setVisibility(View.GONE);
			main_top_view.setVisibility(View.VISIBLE);
			header.setBackgroundColor(getResources().getColor(R.color.main_top_bg));
			tvMainHeader.setVisibility(View.VISIBLE);
			ivMainHeaderLeft.setVisibility(View.GONE);
		} else if (index == 2) {
			IS_HOME_TEMP = 2;
			ivMainHeaderSearch.setVisibility(View.GONE);
			activity_header_progressbar.setVisibility(View.GONE);
			tvMainHeaderCity.setVisibility(View.GONE);
			ivMainHeaderUnfinished.setVisibility(View.GONE);
			rlMainHeaderMessage.setVisibility(View.GONE);
			main_top_view.setVisibility(View.VISIBLE);
			header.setBackgroundColor(getResources().getColor(R.color.main_top_bg));
			tvMainHeader.setVisibility(View.VISIBLE);
			ivMainHeaderLeft.setVisibility(View.GONE);
		} else if (index == 3) {
			IS_HOME_TEMP = 3;
			ivMainHeaderSearch.setVisibility(View.GONE);
			tvMainHeaderCity.setVisibility(View.GONE);
			ivMainHeaderUnfinished.setVisibility(View.GONE);
			rlMainHeaderMessage.setVisibility(View.GONE);
			header.setBackgroundColor(getResources().getColor(R.color.transparent));
			main_top_view.setVisibility(View.GONE);
			tvMainHeader.setVisibility(View.INVISIBLE);
			ivMainHeaderLeft.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		if (IS_HOME_TEMP == 0 && isSlidingMenuOpen) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - exitTime >= 2000) {
				application.showMsg("再按一次返回键退出活动行");
				exitTime = currentTime;
			} else {
				finish();
				application.AppExit(mContext);
			}
		} else {
			if (isSlidingMenuOpen) {
				selection(0);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		if(dbManager!=null){
			dbManager.closeDatabase();
		}
	}
	

}
