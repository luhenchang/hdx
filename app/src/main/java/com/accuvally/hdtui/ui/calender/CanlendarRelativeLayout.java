package com.accuvally.hdtui.ui.calender;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.accuvally.hdtui.R;
public class CanlendarRelativeLayout extends RelativeLayout {

	private ViewPager mCalerder;
	private View mBottom;
	private IEvent mEvent;
	
	public void setIEvent(IEvent event){
		this.mEvent = event;
	}

	public static interface IEvent{
		public void onOpen();
		public void onClose();
	}
	

	public void setBottom(View bottom) {
		this.mBottom = bottom;
	}

	public CanlendarRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CanlendarRelativeLayout(Context context) {
		super(context);
	}

	public CanlendarRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
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
			if(mEvent!=null){
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
			if(mEvent!=null){
				mEvent.onOpen();
			}
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {
		}
	}; 

	private int mLastMoveY;
	private int mLastMoveX;
 

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
				Log.d("x", "moveLeft or Move Right"+Math.abs(moveX) +"  :   "+ Math.abs(moveY));
			} else {
				if (mCalerder != null) {
					final CalendarView cv = (CalendarView) mCalerder.getChildAt(0);
					boolean moveDown = moveY > 0 ;
					if (moveDown) {
						//过滤onclick事件
						if(Math.abs(moveY) > cv.getCellH() /2 ){
						// 往下画动
							if (cv != null) {
								int y = getBottom() - ((FrameLayout.LayoutParams) mBottom.getLayoutParams()).topMargin;
								mBottom.animate().setListener(downListioner).setDuration(200).translationY(y).start();
								mCalerder.animate().setDuration(200).translationY(0).start();
								Log.d("x", "up or down "+Math.abs(moveX) +"  :   "+ Math.abs(moveY));
							}
						}
					} else {
						//过滤onclick事件
						if(Math.abs(moveY) > cv.getCellH() /2 ){
						// 往上滑动
							if (cv != null) {
								int y = ((View)mCalerder.getParent()).getTop()+ cv.getCellH();
								mBottom.animate().setListener(upListioner).setDuration(200).translationY(y).start();
							}
						}
					}
					// 拦截掉事件，传给up
					return false;
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mLastMoveY = 0;
			mLastMoveX = 0;
			break;
		}
		return super.dispatchTouchEvent(event);
	}

}
