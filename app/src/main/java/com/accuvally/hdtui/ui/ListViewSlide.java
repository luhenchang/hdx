package com.accuvally.hdtui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewSlide extends ListView {

	private int position;
	private SlideView mFocusedItemView;

	public ListViewSlide(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		position = pointToPosition(x, y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (position != INVALID_POSITION) {
				mFocusedItemView = (SlideView) getChildAt(position);
			}
		}
		default:
			break;
		}

		if (mFocusedItemView != null) {
			if (position == INVALID_POSITION) {
				mFocusedItemView.shrink();
				return super.onTouchEvent(event);
			}
			mFocusedItemView.onRequireTouchEvent(event);
		}

		return super.onTouchEvent(event);
	}

}
