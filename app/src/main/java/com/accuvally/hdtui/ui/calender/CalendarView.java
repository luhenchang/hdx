package com.accuvally.hdtui.ui.calender;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class CalendarView extends View {

	private static final String TAG = "CalendarView";
	/**
	 * 两种模式 （月份和星期）
	 */
	public static final int MONTH_STYLE = 0;
	public static final int WEEK_STYLE = 1;

	private static final int TOTAL_COL = 7;
	private static final int TOTAL_ROW = 6;

	private Paint mCirclePaint;
	private Paint mTextPaint;
	private Paint mLunarPaint;
	private int mViewWidth;
	private int mViewHight;
	private int mCellSpace;
	private Row rows[] = new Row[TOTAL_ROW];
	public static CustomDate mShowDate;// 自定义的日期 包括year month day
	private int style = MONTH_STYLE;
	private final Resources res;

	private static final int WEEK = 7;
	private CallBack mCallBack;// 回调
	private int touchSlop;
	private boolean callBackCellSpace;

	private Bitmap mApplyed; // 活动标记的图片
	private int mApplyedResId;// 活动标记的资源

	public static interface CallBack {

		void clickDate(CustomDate date, Rect rect);// 回调点击的日期

		void onMesureCellHeight(int cellSpace);// 回调cell的高度确定slidingDrawer高度

		void changeDate(CustomDate date);// 回调滑动viewPager改变的日期

	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		res = getResources();
		init(context);

	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = getResources();
		init(context);

	}

	public CalendarView(Context context) {
		super(context);
		res = getResources();
		init(context);
	}

	public CalendarView(Context context, int style, CallBack mCallBack, int applyResId) {
		super(context);
		res = getResources();
		this.style = style;
		this.mCallBack = mCallBack;
		if (applyResId > 0) {
			mApplyed = BitmapFactory.decodeResource(getResources(), applyResId);
		}
		init(context);
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mCellSpace <= 0)
			return;

		for (int i = 0; i < TOTAL_ROW; i++) {
			if (rows[i] != null)
				rows[i].drawCells(canvas);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
			throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
		}
		setMeasuredDimension(widthSpecSize, MeasureSpec.makeMeasureSpec(TOTAL_ROW * mCellSpace, MeasureSpec.EXACTLY));
	}

	private void init(Context context) {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setColor(Color.parseColor("#fefefe"));

		mLunarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLunarPaint.setColor(Color.parseColor("#d6f5d1"));

		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		todayCell = null;
		mClickCell = null;
		initDate();
	}

	private void initDate() {
		if (style == MONTH_STYLE) {
			mShowDate = new CustomDate();
		} else if (style == WEEK_STYLE) {
			mShowDate = DateUtil.getNextSunday(res);
		}
		changeDate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHight = h;
		// mCellSpace = Math.min(mViewHight / TOTAL_ROW, mViewWidth /
		// TOTAL_COL);
		mCellSpace = mViewWidth / TOTAL_COL;
		if (!callBackCellSpace) {
			mCallBack.onMesureCellHeight(mCellSpace);
			callBackCellSpace = true;
		}
		mTextPaint.setTextSize(mCellSpace / 3);
		mLunarPaint.setTextSize(mCellSpace / 6f);
	}

	private static Cell mClickCell;
	private static Cell todayCell;
	private float mDownX;
	private float mDownY;

	/*
	 * 
	 * 触摸事件为了确定点击的位置日期
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float disX = event.getX() - mDownX;
			float disY = event.getY() - mDownY;
			if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
				int col = (int) (mDownX / mCellSpace);
				int row = (int) (mDownY / mCellSpace);
				measureClickCell(col, row);
			}
			break;
		}
		return true;
	}

	private void measureClickCell(int col, int row) {
		if (col >= TOTAL_COL || row >= TOTAL_ROW)
			return;

		if (style == WEEK_STYLE) {
			if (mClickCell == null && todayCell != null) {
				row = todayCell.j;
			} else if (mClickCell != null) {
				rows[mClickCell.j].cells[mClickCell.i].state = mClickCell.state;
				row = mClickCell.j;
			}
		} else {
			if (mClickCell != null) {
				clearClickState();
			}
		}

		

		if (rows[row] != null) {
			mClickCell = new Cell(rows[row].cells[col].date, rows[row].cells[col].state, rows[row].cells[col].i, rows[row].cells[col].j);
			rows[row].cells[col].state = State.CLICK_DAY;
			CustomDate date = rows[row].cells[col].date;
			date.week = col;

			if (this.style == MONTH_STYLE && (mClickCell.state == State.NEXT_MONTH_DAY || mClickCell.state == State.PAST_MONTH_DAY)) {
			} else {
				Rect rect = new Rect(mClickCell.i * mCellSpace, mClickCell.j * mCellSpace, mClickCell.i * mCellSpace + mCellSpace, mClickCell.j * mCellSpace + mCellSpace);
				mCallBack.clickDate(date, rect);
				invalidate();
			}
		}
	}

	/**
	 * 清除错误的日期状态
	 */
	private void clearClickState() {
		if (mClickCell != null) {
			for (int j = 0; j < TOTAL_ROW; j++) {
				for (int i = 0; i < TOTAL_COL; i++) {
					if (rows[j].cells[i].state == State.CLICK_DAY) {
						rows[j].cells[i].state = getState(j, i);
					}
				}
			}
			rows[mClickCell.j].cells[mClickCell.i].state = getState(mClickCell.j, mClickCell.i);
		}
	}

	public State getState(int j, int i) {
		final CustomDate cs = rows[j].cells[i].date;
		long today = DateUtil.getTimeInMillis(cs);
		long[] monthsMillis = DateUtil.getMonthStartTimeAndEndTimeByFlag(mShowDate);
		if (today > monthsMillis[1]) {
			return State.NEXT_MONTH_DAY;
		} else if (today < monthsMillis[0]) {
			return State.PAST_MONTH_DAY;
		} else {
			if (DateUtil.isToday(cs)) {
				return State.TODAY;
			} else {
				return State.CURRENT_MONTH_DAY;
			}
		}
	}

	// 组
	public class Row {
		public int j;

		Row(int j) {
			this.j = j;
		}

		public Cell[] cells = new Cell[TOTAL_COL];

		public void drawCells(Canvas canvas) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null)
					cells[i].drawSelf(canvas);
			}
		}
	}

	public float fraction;

	public float getFraction() {
		return fraction;
	}

	public void setFraction(float fraction) {
		this.fraction = fraction;
	}

	// 单元格
	public class Cell {
		public CustomDate date;
		public State state;
		public int i;
		public int j;

		public Cell(CustomDate date, State state, int i, int j) {
			this.date = date;
			this.state = state;
			this.i = i;
			this.j = j;
		}

		// 绘制一个单元格 如果颜色需要自定义可以修改
		public void drawSelf(Canvas canvas) {

			switch (state) {
			case CURRENT_MONTH_DAY:
				int color = ColorFactory.gradientTextColor(fraction);
				mTextPaint.setColor(color);
				mLunarPaint.setColor(ColorFactory.gradientLunarTextColor(fraction));
				break;
			case NEXT_MONTH_DAY:
			case PAST_MONTH_DAY:
				mTextPaint.setColor(ColorFactory.gradientTextPassColor(fraction));
				mLunarPaint.setColor(ColorFactory.gradientLunarTextColor(fraction));
				break;
			case TODAY:
				mTextPaint.setColor(ColorFactory.gradientTextTodayColor(fraction));
				mCirclePaint.setColor(ColorFactory.gradientTodayCircle(fraction));
				canvas.drawCircle((float) (mCellSpace * (i + 0.5)), (float) ((j + 0.5) * mCellSpace), mCellSpace / 2, mCirclePaint);
				mLunarPaint.setColor(ColorFactory.gradientLunarTodayTextColor(fraction));
				break;
			case CLICK_DAY:
				mTextPaint.setColor(ColorFactory.gradientTextClickColor(fraction));
				mCirclePaint.setColor(ColorFactory.gradientClickCircle(fraction));
				canvas.drawCircle((float) (mCellSpace * (i + 0.5)), (float) ((j + 0.5) * mCellSpace), mCellSpace / 2, mCirclePaint);
				mLunarPaint.setColor(ColorFactory.gradientLunarClickTextColor(fraction));
				break;
			}

			// 画数字
			// if (!(state == State.NEXT_MONTH_DAY || state ==
			// State.PAST_MONTH_DAY)) {
			// 绘制文字
			// 画阳历的日期
			String content = date.day + "";
			float x = (float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2);
			float y = (float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2);
			canvas.drawText(content, x, y, mTextPaint);

			// ToDo
			date.applyed = ManagerFragmentHelp.get(date.toString());
			// 画圆点,即活动标记
			if (mApplyed != null && date.applyed) {
				canvas.drawBitmap(mApplyed, (i + 0.7f) * mCellSpace, (j + 0.2f) * mCellSpace, null);
			}

			// 画阴历的日期
			x = (float) ((i + 0.5) * mCellSpace - mLunarPaint.measureText(date.lunarDay) / 2);
			y = y + ((j + 0.7f) * mCellSpace - y) / 2 + (mLunarPaint.descent() - mLunarPaint.ascent());
			canvas.drawText(date.lunarDay, x, y, mLunarPaint);
		}
	}

	/**
	 * 
	 * @author huang cell的state 当前月日期，过去的月的日期，下个月的日期，今天，点击的日期
	 * 
	 */
	public enum State {
		// 当月所有的天
		CURRENT_MONTH_DAY,
		// 上个月的天
		PAST_MONTH_DAY,
		// 下个月的天
		NEXT_MONTH_DAY,
		// 今天
		TODAY,
		// 点击的天
		CLICK_DAY;
	}

	/**
	 * 填充日期的数据
	 * 
	 * @param leftOrRight
	 */
	private void changeDate() {

		if (style == MONTH_STYLE) {
			fillMonthDate();

		} else if (style == WEEK_STYLE) {
			fillWeekDate();
		}
		mCallBack.changeDate(mShowDate);
	}

	/**
	 * 这个不是具体的日期,是相对的日期，是为了周模式下用的
	 * 
	 * @return
	 */
	private Cell getCurrentCell() {
		if (mClickCell == null) {
			return todayCell;
		} else {
			return mClickCell;
		}
	}

	/**
	 * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
	 */
	private void fillWeekDate() {

		final Cell cell = getCurrentCell();
		final int index = cell.j;
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);

		rows[index] = new Row(index);
		int day = mShowDate.day;
		int month = mShowDate.month;
		int year = mShowDate.year;
		for (int i = TOTAL_COL - 1; i >= 0; i--) {
			day -= 1;
			if (day < 1) {
				day = lastMonthDays;
				month = month - 1;
			}
			CustomDate date = CustomDate.newInstence(res, year, month, day);

			if (DateUtil.isToday(date)) {
				date.week = i;
				rows[index].cells[i] = new Cell(date, State.TODAY, i, index);
				date.applyed = applyed(date);
				continue;
			}
			rows[index].cells[i] = new Cell(date, State.CURRENT_MONTH_DAY, i, index);
			date.applyed = applyed(date);
			// 保留点击的颜色
			if (mClickCell != null && mClickCell.date.isSameDay(date)) {
				rows[index].cells[i].state = State.CLICK_DAY;
			}
		}
	}

	private boolean applyed(CustomDate cus) {
		return false;
	}

	/**
	 * 填充月份模式下数据 通过getWeekDayFromDate得到一个月第一天是星期几就可以算出所有的日期的位置 然后依次填充 这里最好重构一下
	 * 
	 * @param leftOrRight
	 */
	private void fillMonthDate() {

		int monthDay = DateUtil.getCurrentMonthDay();
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
		int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
		int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);
		boolean isCurrentMonth = false;
		if (DateUtil.isCurrentMonth(mShowDate)) {
			isCurrentMonth = true;
		}
		int day = 0;
		for (int j = 0; j < TOTAL_ROW; j++) {
			rows[j] = new Row(j);
			for (int i = 0; i < TOTAL_COL; i++) {
				int postion = i + j * TOTAL_COL;
				if (postion >= firstDayWeek && postion < firstDayWeek + currentMonthDays) {
					day++;
					if (isCurrentMonth && day == monthDay) {
						CustomDate date = CustomDate.modifiDayForObject(getResources(), mShowDate, day);
						date.week = i;
						date.applyed = applyed(date);
						rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
						todayCell = rows[j].cells[i];
						continue;
					}

					rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(getResources(), mShowDate, day), State.CURRENT_MONTH_DAY, i, j);
					if (mClickCell != null)
						// 保留点击的颜色
						if (mClickCell != null && mClickCell.date.isSameDay(rows[j].cells[i].date)) {
							rows[j].cells[i].state = State.CLICK_DAY;
						}
				} else if (postion < firstDayWeek) {
					final CustomDate cd = CustomDate.newInstence(res, mShowDate.year, mShowDate.month - 1, lastMonthDays - (firstDayWeek - postion - 1));
					cd.applyed = applyed(cd);
					rows[j].cells[i] = new Cell(cd, State.PAST_MONTH_DAY, i, j);
				} else if (postion >= firstDayWeek + currentMonthDays) {
					int year = mShowDate.year;
					int month = mShowDate.month + 1;
					int monthday = postion - firstDayWeek - currentMonthDays + 1;
					CustomDate cDate = CustomDate.newInstence(res, year, month, monthday);
					cDate.applyed = applyed(cDate);
					rows[j].cells[i] = new Cell(cDate, State.NEXT_MONTH_DAY, i, j);
				}
			}
		}
	}

	public void update() {
		changeDate();
		invalidate();
	}

	public void backToday() {
		initDate();
		invalidate();
	}

	// 切换style,没有点击任何日期
	public void switchStyle(int style) {
		this.style = style;
		if (style == MONTH_STYLE) {
			update();
		} else if (style == WEEK_STYLE) {

			// int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
			// mShowDate.month);
			// // int day = 1 + WEEK - firstDayWeek;
			// int day = firstDayWeek - 1;
			// mShowDate.day = day;
			CustomDate cs = DateUtil.getSunday(res, getCurrentCell().date.year, getCurrentCell().date.month, getCurrentCell().date.day);
			mShowDate.month = cs.month;
			mShowDate.day = cs.day;
			update();
		}
	}

	// 向右滑动
	public void rightSilde() {

		if (style == MONTH_STYLE) {

			if (mShowDate.month == 12) {
				mShowDate.month = 1;
				mShowDate.year += 1;
			} else {
				mShowDate.month += 1;
			}

		} else if (style == WEEK_STYLE) {
			int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
			if (mShowDate.day + WEEK > currentMonthDays) {
				if (mShowDate.month == 12) {
					mShowDate.month = 1;
					mShowDate.year += 1;
				} else {
					mShowDate.month += 1;
				}
				mShowDate.day = WEEK - currentMonthDays + mShowDate.day;
			} else {
				mShowDate.day += WEEK;
			}
		}
		update();
	}

	// 向左滑动
	public void leftSilde() {

		if (style == MONTH_STYLE) {
			if (mShowDate.month == 1) {
				mShowDate.month = 12;
				mShowDate.year -= 1;
			} else {
				mShowDate.month -= 1;
			}

		} else if (style == WEEK_STYLE) {
			int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
			if (mShowDate.day - WEEK < 1) {
				if (mShowDate.month == 1) {
					mShowDate.month = 12;
					mShowDate.year -= 1;
				} else {
					mShowDate.month -= 1;
				}
				mShowDate.day = lastMonthDays - WEEK + mShowDate.day;
			} else {
				mShowDate.day -= WEEK;
			}
		}

		update();
	}

	public int getSrcSize() {
		return TOTAL_ROW * mCellSpace;
	}

	public int getCellH() {
		return mCellSpace;
	}

	public int getMaxY() {
		int index = getCurrentRowIndex();
		return (index + 1) * mCellSpace;
	}

	public int getCurrentCellH() {
		return getCurrentRowIndex() * mCellSpace;
	}

	public int getCurrentRowIndex() {

		int index = 0;
		Cell cell = getCurrentCell();
		if (cell != null) {
			index = cell.j;
		}
		return index;
	}

	public int getTopY() {
		return getCurrentCellH();
	}

	public int getWeekHeigh() {
		return (getCurrentRowIndex()) * mCellSpace;
	}

	public Cell getClickCell() {
		return this.mClickCell;
	}

	public Cell getTodayCell() {
		return this.todayCell;
	}

	public void refreshIcon() {
		// boolean needInvadate = false;
		// if (style == WEEK_STYLE) {
		// final Cell cell = getCurrentCell();
		// final int index = cell.j;
		// for (int i = TOTAL_COL - 1; i >= 0; i--) {
		// CustomDate cs = rows[index].cells[i].date;
		// boolean apply = ManagerFragmentHelp.get(cs.toString());
		// if(apply){
		// cs.applyed = true;
		// needInvadate = true;
		// }
		// }
		// } else if (style == MONTH_STYLE) {
		// for (int j = 0; j < TOTAL_ROW; j++) {
		// for (int i = 0; i < TOTAL_COL; i++) {
		// CustomDate cs = rows[j].cells[i].date;
		// boolean apply = ManagerFragmentHelp.get(cs.toString());
		// if(apply){
		// cs.applyed = true;
		// needInvadate = true;
		// }
		// }
		// }
		// }
		//
		// if(needInvadate){
		invalidate();
		// }
	}
}
