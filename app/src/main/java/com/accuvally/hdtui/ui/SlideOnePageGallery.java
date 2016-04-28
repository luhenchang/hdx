package com.accuvally.hdtui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * SlideOnePageGallery, only slide one page every slide
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-22
 */
public class SlideOnePageGallery extends Gallery {

	private float gTouchStartX;

	private float gTouchStartY;

	public SlideOnePageGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SlideOnePageGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlideOnePageGallery(Context context) {
		super(context);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			gTouchStartX = ev.getX();
			gTouchStartY = ev.getY();
			super.onTouchEvent(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			final float touchDistancesX = Math.abs(ev.getX() - gTouchStartX);
			final float touchDistancesY = Math.abs(ev.getY() - gTouchStartY);
			if (touchDistancesY * 2 >= touchDistancesX) {
				return false;
			} else {
				return true;
			}
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return false;
	}
}
