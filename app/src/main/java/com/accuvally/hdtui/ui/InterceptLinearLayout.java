package com.accuvally.hdtui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptLinearLayout extends LinearLayout {

	private boolean intercept;

	public boolean isIntercept() {
		return intercept;
	}

	public void setIntercept(boolean intercept) {
		this.intercept = intercept;
	}

	public InterceptLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return intercept;
	}
}
