package com.accuvally.hdtui.ui.calender;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.accuvally.hdtui.R;

public class CanlendarParent extends RelativeLayout {

	private ViewPager mCalerder;

	public CanlendarParent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CanlendarParent(Context context) {
		super(context);
	}

	public CanlendarParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCalerder = (ViewPager) findViewById(R.id.viewpager_calender);
	}

	public int getMaxY() {
		if(getCurCalendarView()!=null){
			return getCurCalendarView().getMaxY();
		}
		return 0;
	}

	public int getMinY() {
		if(getCurCalendarView()!=null){
			return getCurCalendarView().getCellH();
		}
		return 0;
	}
	
	
	public int getMaxHeigth() {
		if(getCurCalendarView()!=null){
			return getCurCalendarView().getSrcSize();
		}
		return 0;
	}


	/**
	 * 这个方法只是获取ViewPager中有点击，或今天的CalendarView，而不是真正当前的view
	 * 
	 * @return
	 */
	public CalendarView getCurCalendarView() {
		CalendarView current = null;
		if (mCalerder != null) {
			final int count = mCalerder.getChildCount();
			for (int i = 0; i < count; i++) {
				CalendarView child = (CalendarView) mCalerder.getChildAt(i);
				if (child.getTodayCell() != null) {
					current = child;
				}
				if (child.getClickCell() != null) {
					current = child;
					break;
				}
				//确保不为空
				if(i==count-1 && current==null){
					current = child;
				}
			}
			 
		}
		return current;
	}
	public static void colorGradientBackgroundColor(View view, long duration, int colors) {
		ObjectAnimator objectAnimator = new ObjectAnimator();
		objectAnimator.setIntValues(Color.parseColor("#66b151"), Color.parseColor("#f6f4ec"));
		objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.setPropertyName("backgroundColor");
		objectAnimator.setDuration(duration);
		objectAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
			}
		});
		objectAnimator.setTarget(view);
		objectAnimator.start();
	}

	public static void colorGradientBackgroundColor(View view, int[] colors) {
		ObjectAnimator objectAnimator = new ObjectAnimator();
		objectAnimator.setIntValues(colors);
		objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.setPropertyName("backgroundColor");
		objectAnimator.setDuration(0);
		objectAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
			}
		});
		objectAnimator.setTarget(view);
		objectAnimator.start();
	}
	
}
