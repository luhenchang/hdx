package com.accuvally.hdtui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.LoginActivityNew;
import com.accuvally.hdtui.adapter.FragmentViewPagerAdapter;
import com.accuvally.hdtui.fragment.manager.CollectionFragment;
import com.accuvally.hdtui.fragment.manager.EnrollFragment;
import com.accuvally.hdtui.fragment.manager.UnfinishedFragment;
import com.accuvally.hdtui.ui.PagerSlidingTabStrip;
import com.accuvally.hdtui.ui.calender.AccuSlidingDrawer;
import com.accuvally.hdtui.ui.calender.CalendarView;
import com.accuvally.hdtui.ui.calender.CalendarViewBuilder;
import com.accuvally.hdtui.ui.calender.CalendarViewPagerLisenter;
import com.accuvally.hdtui.ui.calender.CanlendarParent;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.CustomViewPagerAdapter;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;
import com.accuvally.hdtui.utils.eventbus.RefreshCalendar;

import de.greenrobot.event.EventBus;

public class ManagerFragment extends BaseFragment {

	private ViewPager viewpager;
	private PagerSlidingTabStrip tabStrip;
	private DisplayMetrics dm;
	private List<Fragment> fragments;

	private CollectionFragment collectionFragment;
	private EnrollFragment organizersFragment;
//	private SponsorFragment sponsorFragment;
	private UnfinishedFragment mUnfinishedFragment;
	private FragmentViewPagerAdapter mAdapter;
	private View rootView;
	private LinearLayout mFooter;
	private String[] title = { "收藏", "票券", "未完成"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_manager, container, false);

		initView(rootView);
		initData();
		// 初始日历控件，只有当Tag==3的时候才显示日期
		ManagerFragmentHelp.updateFragment(0);
		initWithCalender();
		return rootView;
	}

	public void initView(View view) {
		dm = getResources().getDisplayMetrics();
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabStrip);
		viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		mFooter = (LinearLayout) getActivity().findViewById(R.id.fragment_bottom);
	}

	public void initData() {
		fragments = new ArrayList<Fragment>();
		collectionFragment = new CollectionFragment();
		organizersFragment = new EnrollFragment();
		mUnfinishedFragment = new UnfinishedFragment();
//		sponsorFragment = new SponsorFragment();
		fragments.add(collectionFragment);
		fragments.add(organizersFragment);
		fragments.add(mUnfinishedFragment);
//		fragments.add(sponsorFragment);
		mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), viewpager, fragments,title, "");
		viewpager.setAdapter(mAdapter);
		tabStrip.setViewPager(viewpager);
		setTabsValue();
	}

	private void setTabsValue() {
		tabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabStrip.setSelectedTextColor(getResources().getColor(R.color.txt_green));
	}

	public void onEventMainThread(RefreshCalendar rc) {
		builder.refresh();
	}

	// ==========================================================================日历添加的代码
	// 日历功能
	private ViewPager mCanlendarVp;
	private int marginTopDis;
	private boolean closed = true;
	private TextView mMonthDayTv;
	private AccuSlidingDrawer mAccuSlidingDrawer;
	private CalendarViewBuilder builder = new CalendarViewBuilder();
	private CalendarView[] views;
	private CalendarViewPagerLisenter mCalendarViewPagerLisenter;
	private CanlendarParent mCanlendarParent;
	private View mBottom;
	private ImageView mRight;
	private RelativeLayout mTitle;
	private TableRow mWeekRow;
	private int mOrigleY;
	private CustomDate mCustomDate;
	private int listViewBottomMargin;

	public int getListViewBottomMargin() {
		return listViewBottomMargin;
	}

	public CustomDate getCustomDate() {
		return mCustomDate;
	}

	private void calenderToggle() {
		if (closed) {
			listViewBottomMargin = marginTopDis;
		} else {
			listViewBottomMargin = 0;
		}
		// 发送消息，让listview底部的footview显示
		if (viewpager.getCurrentItem() == 0) {
			EventBus.getDefault().post(new EventHideFooterView(CollectionFragment.class));
		} else if (viewpager.getCurrentItem() == 1) {
			EventBus.getDefault().post(new EventHideFooterView(EnrollFragment.class));
		} else if (viewpager.getCurrentItem() == 2) {
			EventBus.getDefault().post(new EventHideFooterView(UnfinishedFragment.class));
		} else if (viewpager.getCurrentItem() == 3) {
//			EventBus.getDefault().post(new EventHideFooterView(SponsorFragment.class));
		}
		closed = !closed;
		if (closed) {
			// 关闭查询全部
			mCustomDate = null;
			EventBus.getDefault().post(new EventCustomDate(UnfinishedFragment.class, mCustomDate));
			mAccuSlidingDrawer.close();
			mBottom.animate().setDuration(100).translationY(0).start();
		} else {
			mBottom.animate().setDuration(100).translationY(marginTopDis).start();
		}
	}

	// 日历添加的代码
	private void initWithCalender() {
		rootView.findViewById(R.id.dayleft).setOnClickListener(new PageMoveListioner(true));
		rootView.findViewById(R.id.dayright).setOnClickListener(new PageMoveListioner(false));
		mCanlendarVp = (ViewPager) rootView.findViewById(R.id.viewpager_calender);
		mMonthDayTv = (TextView) rootView.findViewById(R.id.monthday);
		mTitle = (RelativeLayout) rootView.findViewById(R.id.monthday_layout);
		mWeekRow = (TableRow) rootView.findViewById(R.id.weekrow);
		// 显示日历的按钮

		mRight = ((ImageView) getActivity().findViewById(R.id.ivMainHeaderUnfinished));
//		if (tabStrip != null) {
//			tabStrip.setRightMenu(mRight);
//		}
		((View) mRight.getParent()).setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.btn_calender);
		mRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calenderToggle();
			}
		});

		setViewPager();
		mBottom = rootView.findViewById(R.id.bottom_layout);

		mAccuSlidingDrawer = (AccuSlidingDrawer) rootView.findViewById(R.id.drawer);
		mAccuSlidingDrawer.setViews(mMonthDayTv, mWeekRow);
		mAccuSlidingDrawer.setOnDrawerScrollListener(new AccuSlidingDrawer.OnDrawerScrollListener() {

			@Override
			public void onSrolling(int y) {
				float h = mAccuSlidingDrawer.getHandle().getBottom() + mOrigleY;
				if (mBottom != null && h > 0) {
					boolean iscloseoropend = (y == AccuSlidingDrawer.EXPANDED_FULL_OPEN) || (y == AccuSlidingDrawer.COLLAPSED_FULL_CLOSED);
					if (!iscloseoropend) {
						mBottom.animate().translationY(h).setDuration(0).start();
						if (mAccuSlidingDrawer != null) {
							mAccuSlidingDrawer.draw(1.0f * y / mCanlendarParent.getMaxHeigth());
						}
					}
				}
			}

			@Override
			public void onScrollStarted() {
				builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
			}

			@Override
			public void onScrollEnded() {
			}

			@Override
			public void onScroll(boolean willBackward) {
			}

			@Override
			public void onPreScrollStarted() {

			}
		});

		mAccuSlidingDrawer.setOnDrawerOpenListener(new AccuSlidingDrawer.OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				if (mBottom != null) {
					int position = mAccuSlidingDrawer.getHandle().getBottom();
					mBottom.animate().translationY(position + mOrigleY).setDuration(0).start();
				}

				if (!mAccuSlidingDrawer.isOpened()) {
					if (mAccuSlidingDrawer != null) {
						mAccuSlidingDrawer.draw(1);
					}
					builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
				}
			}
		});

		mAccuSlidingDrawer.setOnDrawerCloseListener(new AccuSlidingDrawer.OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				if (mBottom != null) {
					mBottom.animate().translationY(mAccuSlidingDrawer.getHandle().getBottom() + mOrigleY).setDuration(0).start();
				}
				if (mAccuSlidingDrawer != null) {
					mAccuSlidingDrawer.draw(0);
				}
				builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
			}
		});
		mAccuSlidingDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mOrigleY = mTitle.getMeasuredHeight() + mWeekRow.getMeasuredHeight();
			}
		});
		builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
		mAccuSlidingDrawer.draw(0);

	}

	private void setViewPager() {
		mCanlendarVp = (ViewPager) rootView.findViewById(R.id.viewpager_calender);
		mCanlendarParent = ((CanlendarParent) mCanlendarVp.getParent());
		views = builder.createMassCalendarViews(getActivity(), 3, CalendarView.MONTH_STYLE, new CalendarView.CallBack() {

			@Override
			public void onMesureCellHeight(int cellSpace) {
			}

			@Override
			public void changeDate(CustomDate date) {
				if (date.year != DateUtil.getYear()) {
					mMonthDayTv.setText(date.year + "年" + date.month + "月");
				} else {
					mMonthDayTv.setText(date.month + "月");
				}
			}

			@Override
			public void clickDate(final CustomDate date, Rect rect) {
				mCustomDate = date;
				if (application.checkIsLogin()) {
					if (viewpager.getCurrentItem() == 0) {
						EventBus.getDefault().post(new EventCustomDate(CollectionFragment.class, date));
					} else if (viewpager.getCurrentItem() == 1) {
						EventBus.getDefault().post(new EventCustomDate(EnrollFragment.class, date));
					} else if (viewpager.getCurrentItem() == 2) {
						EventBus.getDefault().post(new EventCustomDate(UnfinishedFragment.class, date));
					}
				} else {
					startActivity(new Intent(mContext, LoginActivityNew.class));
				}
			}
		});
		builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		mCanlendarVp.setAdapter(viewPagerAdapter);
		mCanlendarVp.setCurrentItem(498);
		mCalendarViewPagerLisenter = new CalendarViewPagerLisenter(viewPagerAdapter);
		mCanlendarVp.setOnPageChangeListener(mCalendarViewPagerLisenter);
		// 初始画动画参数
		mCanlendarVp.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mCanlendarVp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				marginTopDis = mCanlendarParent.getCurCalendarView().getCellH() + mTitle.getMeasuredHeight() + mWeekRow.getMeasuredHeight() + mAccuSlidingDrawer.getHandle().getMeasuredHeight();
			}
		});
	}

	private class PageMoveListioner implements OnClickListener {

		private boolean left;

		public PageMoveListioner(boolean left) {
			this.left = left;
		}

		@Override
		public void onClick(View v) {
			if (left) {
				mCanlendarVp.setCurrentItem(mCanlendarVp.getCurrentItem() - 1);
			} else {
				mCanlendarVp.setCurrentItem(mCanlendarVp.getCurrentItem() + 1);
			}
		}
	}

}
