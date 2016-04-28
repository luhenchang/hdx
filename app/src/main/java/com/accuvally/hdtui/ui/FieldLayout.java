package com.accuvally.hdtui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class FieldLayout extends ViewGroup {

	int colums = 2;
	private int mMaxChildWidth = 0;
	private int mMaxChildHeight = 0;
	int count = 0;

	public FieldLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int modeW = 0, modeH = 0;
		if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED)
			modeW = MeasureSpec.UNSPECIFIED;
		if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED)
			modeH = MeasureSpec.UNSPECIFIED;
		
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), modeW);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), modeH);

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
			mMaxChildHeight = Math.max(mMaxChildHeight, child.getMeasuredHeight());
		}
		setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec), resolveSize(mMaxChildHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	}

}
