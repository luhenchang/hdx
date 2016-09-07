package com.accuvally.hdtui.activity.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.ui.calender.AccuSlidingDrawer;
import com.accuvally.hdtui.ui.calender.CalendarView;
import com.accuvally.hdtui.ui.calender.CalendarViewBuilder;
import com.accuvally.hdtui.ui.calender.CalendarViewPagerLisenter;
import com.accuvally.hdtui.ui.calender.CanlendarParent;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.CustomViewPagerAdapter;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CalenderTypeActivity extends BaseActivity {

	private int type = 4;// 用最新的类别去做进一步查询,最新+日期联合查询
	private String cityName;
	
	private String tag = "全部";
	
	private XListView mListView;
	private CommonAccuAdapter mAdapter;
	private int pageIndex = 1, pageSize = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_calender_type);
		initView();
		initProgress();
		
		setTitle("日历");
		// 初始日历控件，只有当Tag==3的时候才显示日期
		initWithCalender();
		
		initData();
	}

	public void initView() {
		cityName = application.sharedUtils.readString("cityName");
		
		mListView = (XListView) findViewById(R.id.listview);
		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				pageIndex = 1;
				initData();
			}
			
			@Override
			public void onLoadMore() {
				pageIndex++;
				initData();
			}
		});
		
		((LinearLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.listview_bottomMargin);

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
	
	public void initData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 用最新的类别去做进一步查询,最新+日期联合查询
		params.add(new BasicNameValuePair("filter", 3 + ""));
		
		mCustomDate = getCustomDate();
		params.add(new BasicNameValuePair("date", "t8"));
		params.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
		params.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
		
//		params.add(new BasicNameValuePair("tag", tag));
		params.add(new BasicNameValuePair("city", cityName));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()){
						List<SelInfo> list = JSON.parseArray(response.getResult(), SelInfo.class);
						if (pageIndex == 1 && list.size() != 0) {
							mAdapter.removeAll();
						} else if (type == 4 && list.size() == 0) {
							if (pageIndex == 1) {
								mAdapter.removeAll();
							} else {
								application.showMsg("亲，没有更多数据", 300);
							}
						}
						mAdapter.addAll(list);
					}
			
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	// 日历功能
	private ViewPager mCanlendarVp;
	private int marginTopDis;
	private TextView mMonthDayTv;
	private AccuSlidingDrawer mAccuSlidingDrawer;
	private CalendarViewBuilder builder = new CalendarViewBuilder();
	private CalendarView[] views;
	private CalendarViewPagerLisenter mCalendarViewPagerLisenter;
	private CanlendarParent mCanlendarParent;
	private View mBottom;
	private View mTitle;
	private TableRow mWeekRow;
	private int mOrigleY;
	private CustomDate mCustomDate = new CustomDate();

	public CustomDate getCustomDate() {
		return mCustomDate;
	}

	// 日历添加的代码
	private void initWithCalender() {
		setSwipeBackEnable(false);
		findViewById(R.id.dayleft).setOnClickListener(new PageMoveListioner(true));
		findViewById(R.id.dayright).setOnClickListener(new PageMoveListioner(false));
		mBottom = findViewById(R.id.bottom);
		mCanlendarVp = (ViewPager) findViewById(R.id.viewpager_calender);
		mMonthDayTv = (TextView) findViewById(R.id.monthday);
		mTitle = findViewById(R.id.monthday_layout);
		mWeekRow = (TableRow) findViewById(R.id.weekrow);
		// 显示日历的按钮

		final ImageView car = ((ImageView) findViewById(R.id.two_img));
		((View) car.getParent()).setVisibility(View.VISIBLE);
		car.setBackgroundResource(R.drawable.btn_calender);
		car.setVisibility(View.GONE);

		setViewPager();

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
		views = builder.createMassCalendarViewsNoMark(this, 3,CalendarView.MONTH_STYLE, new CalendarView.CallBack() {

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
				initData();
			}
		});

		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		mCanlendarVp.setAdapter(viewPagerAdapter);
		mCanlendarVp.setCurrentItem(498);
		mCalendarViewPagerLisenter = new CalendarViewPagerLisenter(viewPagerAdapter);
		mCanlendarVp.setOnPageChangeListener(mCalendarViewPagerLisenter);
		// 初始画动画参数
		// mBootom = (BottomLinearLayout) findViewById(R.id.bottom);
		// mBootom.setTopView((View)mCanlendarVp);
		mCanlendarVp.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mCanlendarVp.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				marginTopDis = mCanlendarParent.getCurCalendarView().getCellH() + mTitle.getMeasuredHeight() + mWeekRow.getMeasuredHeight() + mAccuSlidingDrawer.getHandle().getMeasuredHeight();
				mBottom.animate().setDuration(100).translationY(marginTopDis).start();
			}
		});
	}
}
