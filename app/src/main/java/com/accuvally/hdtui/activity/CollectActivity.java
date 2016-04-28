package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CollectionAdapter;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.fragment.manager.CollectionFragment;
import com.accuvally.hdtui.fragment.manager.UnfinishedFragment;
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
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCollection;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

/*
 * 
 * 收藏列表
 */
public class CollectActivity extends BaseActivity implements IXListViewListener, OnClickListener {

	private XListView mListView;
	private int pageIndex = 1, pageSize = 10;
	private CommonAccuAdapter mAdapter;

	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_collection);
		EventBus.getDefault().register(this);
		initProgress();
		initView();
		initWithCalender();
	}

	public void initView() {
		setTitle("我的收藏");
		mListView = (XListView) findViewById(R.id.lvCollect);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("当前日期没有收藏的活动噢");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);

		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
		SquareBtn.setOnClickListener(this);

		String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_collection");
		if (!TextUtils.isEmpty(cache)) {
			List<SelInfo> listCache = JSON.parseArray(cache, SelInfo.class);
			if (listCache != null) {
				mAdapter.addAll(listCache);
				ManagerFragmentHelp.putAll(CollectionFragment.class, listCache);
			}
		}
		getListData();
	}

	public void onEventMainThread(EventCollection eventCollection) {
		pageIndex = 1;
		getListData();
	}

	public void onEventMainThread(EventCustomDate bus) {
		getListData();
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		// params.add(new BasicNameValuePair("regOnly", "false"));
		boolean onlyUnexpired = application.sharedUtils.readBoolean("onlyunexpired", true);
		params.add(new BasicNameValuePair("excludeExpired", String.valueOf(onlyUnexpired)));// 仅显示未过期
		if (mCustomDate != null) {
			params.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
			params.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
			
		}
		((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = getListViewBottomMargin();
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYLIKED, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();

				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<SelInfo> list = JSON.parseArray(response.result, SelInfo.class);
						if (list == null) {
							// 显示无数据页面
							if (pageIndex == 1) {
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
							return;
						}

						if (pageIndex == 1) {
							mAdapter.removeAll();
							// 显示无数据页面
							if (list.size() == 0) {
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
						}
						mAdapter.addAll(list);
//						ManagerFragmentHelp.putAll(CollectionFragment.class, list);
						// 缓存
						if (pageIndex == 1) {
							application.cacheUtils.put(application.getUserInfo().getAccount() + "_collection", response.result);
						}
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());

					break;
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		getListData();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		getListData();
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
//		mRight.setVisibility(View.VISIBLE);
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
				EventBus.getDefault().post(new EventCustomDate(CollectActivity.class, date));
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			finish();
			break;
		}
	}
}
