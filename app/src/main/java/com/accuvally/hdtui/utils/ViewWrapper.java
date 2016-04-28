package com.accuvally.hdtui.utils;


import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class ViewWrapper {

	private View mTarget;
	public static final String MarginTop = "marginTop";

	public ViewWrapper(View target) {
		mTarget = target;
	}

	public int getWidth() {
		return mTarget.getLayoutParams().width;
	}

	public void setWidth(int width) {
		mTarget.getLayoutParams().width = width;
		mTarget.requestLayout();
	}

	public int getHeight() {
		return mTarget.getLayoutParams().height;
	}

	public void setHeight(int height) {
		mTarget.getLayoutParams().height = height;
		mTarget.requestLayout();
	}

	public int getMarginTop() {
		return ((FrameLayout.LayoutParams) mTarget.getLayoutParams()).topMargin;
	}

	public void setMarginTop(int marginTop) {
		Log.d("marginTop", "marginTop ="+marginTop);
		((FrameLayout.LayoutParams) mTarget.getLayoutParams()).topMargin = marginTop;
		mTarget.requestLayout();
	}

	private ObjectAnimator mAnimator;

	public void reverse() {
		if (mAnimator != null) {
			mAnimator.cancel();
			mAnimator.reverse();
		}
	}

	public void makeMarginTopAnimate(View view, int marginTop, long duration, AnimatorListener listener) {
		if (mAnimator == null) {
			mAnimator = ObjectAnimator.ofInt(new ViewWrapper(view), MarginTop, marginTop);
			mAnimator.setDuration(duration);
			if (listener != null)
				mAnimator.addListener(listener);
		}
	}

	public void play() {
		if (mAnimator != null) {
			mAnimator.cancel();
			mAnimator.start();
		}
	}

	public static void doMarginTopAnimate(View view, int marginTop, long duration, AnimatorListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofInt(new ViewWrapper(view), MarginTop, marginTop);
		animator.setDuration(duration);
		if (listener != null)
			animator.addListener(listener);
		animator.start();
	}
	
	public static void doMarginTopAnimate(View view, int marginTop, long duration) {
		doMarginTopAnimate(view,marginTop,duration,null);
	}
}
