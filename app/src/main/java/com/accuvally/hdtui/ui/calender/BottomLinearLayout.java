package com.accuvally.hdtui.ui.calender;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.accuvally.hdtui.R;

public class BottomLinearLayout extends LinearLayout {

	private static final String tag = "BottomLinearLayout";
	private ViewPager mCalerder;

	public void setTopView(ViewPager mCalerder) {
		this.mCalerder = mCalerder;
	}

	private IEvent mEvent;

	public void setIEvent(IEvent event) {
		this.mEvent = event;
	}

	public static interface IEvent {
		public void onOpen();

		public void onClose();
	}

	public BottomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BottomLinearLayout(Context context) {
		super(context);
	}

	public BottomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCalerder = (ViewPager) findViewById(R.id.viewpager_calender);
	}


	private AnimatorListener upListioner = new AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			if (mEvent != null) {
				mEvent.onClose();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}
	};

	private AnimatorListener downListioner = new AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			if (mEvent != null) {
				mEvent.onOpen();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}
	};

	private int mLastMoveY;
	private int mLastMoveX;
	private int mScrolY;

	public boolean dispatchTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMoveX = (int) event.getX();
			mLastMoveY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) (event.getX() - mLastMoveX);
			int moveY = (int) (event.getY() - mLastMoveY);
			 
			if (Math.abs(moveX) > Math.abs(moveY)) {
				// TODO
				// 忽略横移时间
			} else {
				if (mCalerder != null) {
					Log.d(tag, "moveY========= " + moveY+this.getTop()+":"+mCalerder.getTop());
					final CalendarView cv = (CalendarView) mCalerder.getChildAt(0);
					mScrolY += moveY;
					if (cv.getStyle() == CalendarView.MONTH_STYLE) {
						if (cv != null) {
							// 拦截掉事件，传给up
							
//							offsetTopAndBottom(moveY);
//							mCalerder.offsetTopAndBottom(moveY);
							
							return true;
						}
					}
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mLastMoveY = 0;
			mLastMoveX = 0;
			mScrolY = 0;
			break;
		}
		 return super.dispatchTouchEvent(event);
		//return true;
	}

}
