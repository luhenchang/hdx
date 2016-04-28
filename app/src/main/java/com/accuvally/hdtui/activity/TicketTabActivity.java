package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.FragmentViewPagerAdapter;
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
import com.accuvally.hdtui.utils.eventbus.ChangeTicketEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;

import de.greenrobot.event.EventBus;

/**
 * 我的票券
 * 
 * @author Semmer Wang
 * 
 */
public class TicketTabActivity extends BaseActivity {
	private ViewPager viewpager;
	private PagerSlidingTabStrip tabStrip;
	private DisplayMetrics dm;
	private List<Fragment> fragments;

	private EnrollFragment enrollFragment;
	private UnfinishedFragment unfinishedFragment;

	private FragmentViewPagerAdapter mAdapter;
	private String[] title = { "我的票券", "未完成票券" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_tab);
		EventBus.getDefault().register(this);
		initView();
		initWithCalender();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ChangeTicketEventBus eventBus) {
//		if (mRight.getVisibility() == View.VISIBLE && !closed) {
//			calenderToggle();
//		}
//		if (eventBus.getMsg() == 1) {
//			mRight.setVisibility(View.INVISIBLE);
//		} else {
//			mRight.setVisibility(View.VISIBLE);
//		}
	}

	public void initView() {
		dm = getResources().getDisplayMetrics();
		tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		fragments = new ArrayList<Fragment>();
		enrollFragment = new EnrollFragment();
		unfinishedFragment = new UnfinishedFragment();
		fragments.add(enrollFragment);
		fragments.add(unfinishedFragment);
		mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), viewpager, fragments, title, "");
		setTabsValue();

	}

	private void setTabsValue() {
		tabStrip.setTicket(true);
		viewpager.setAdapter(mAdapter);
		tabStrip.setViewPager(viewpager);
		tabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		tabStrip.setSelectedTextColor(getResources().getColor(R.color.txt_green));
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
//	private LinearLayout mRight;
	private ImageView two_img;
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
			EventBus.getDefault().post(new EventHideFooterView(EnrollFragment.class));
		} else if (viewpager.getCurrentItem() == 1) {
			EventBus.getDefault().post(new EventHideFooterView(UnfinishedFragment.class));
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
		findViewById(R.id.dayleft).setOnClickListener(new PageMoveListioner(true));
		findViewById(R.id.dayright).setOnClickListener(new PageMoveListioner(false));
		mCanlendarVp = (ViewPager) findViewById(R.id.viewpager_calender);
		mMonthDayTv = (TextView) findViewById(R.id.monthday);
		mTitle = (RelativeLayout) findViewById(R.id.monthday_layout);
		mWeekRow = (TableRow) findViewById(R.id.weekrow);
		// 显示日历的按钮

//		mRight = ((LinearLayout) findViewById(R.id.share_ly));
//		mRight.setVisibility(View.GONE);
//		two_img = (ImageView) findViewById(R.id.two_img);
//		two_img.setImageResource(R.drawable.mine_collection_cal_icon_bg);
//		mRight.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				calenderToggle();
//			}
//		});

		setViewPager();
		mBottom = findViewById(R.id.bottom_layout);

		mAccuSlidingDrawer = (AccuSlidingDrawer) findViewById(R.id.drawer);
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
		mCanlendarVp = (ViewPager) findViewById(R.id.viewpager_calender);
		mCanlendarParent = ((CanlendarParent) mCanlendarVp.getParent());
		views = builder.createMassCalendarViews(mContext, 3, CalendarView.MONTH_STYLE, new CalendarView.CallBack() {

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
				if (viewpager.getCurrentItem() == 0) {
					EventBus.getDefault().post(new EventCustomDate(EnrollFragment.class, date));
				} else if (viewpager.getCurrentItem() == 1) {
					EventBus.getDefault().post(new EventCustomDate(UnfinishedFragment.class, date));
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
				// mCanlendarVp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mCanlendarVp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
