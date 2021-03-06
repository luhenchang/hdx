package com.accuvally.hdtui.ui;

import com.accuvally.hdtui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
 

public class CircleFlowIndicator extends View implements FlowIndicator, AnimationListener {

	private static final int STYLE_STROKE = 0;

	private static final int STYLE_FILL = 1;

	/** 半径 */
	private float radius = 4;

	/** 显示时间 */
	private int fadeOutTime = 0;

	/** 未选中的背景画笔 */
	private final Paint mPaintInactive = new Paint(Paint.ANTI_ALIAS_FLAG);

	/** 选中的背景画笔 */
	private final Paint mPaintActive = new Paint(Paint.ANTI_ALIAS_FLAG);

	private ViewFlow viewFlow;

	private int currentScroll = 0;

	private int flowWidth = 0;

	private FadeTimer timer;

	public AnimationListener animationListener = this;

	private Animation animation;

	private boolean mCentered = false;

	public CircleFlowIndicator(Context context) {
		super(context);
		initColors(0xFFFFFFFF, 0xFFFFFFFF, STYLE_FILL, STYLE_STROKE);
	}

	public CircleFlowIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleFlowIndicator);

		int activeDefaultColor = 0xFFFFFFFF;
		int inactiveDefaultColor = 0x44FFFFFF;

		int activeType = a.getInt(R.styleable.CircleFlowIndicator_activeType, STYLE_FILL);
		int activeColor = a.getColor(R.styleable.CircleFlowIndicator_activeColor, activeDefaultColor);
		int inactiveType = a.getInt(R.styleable.CircleFlowIndicator_inactiveType, STYLE_STROKE);
		int inactiveColor = a.getColor(R.styleable.CircleFlowIndicator_inactiveColor, inactiveDefaultColor);

		radius = a.getDimension(R.styleable.CircleFlowIndicator_radius, 4.0f);
		fadeOutTime = a.getInt(R.styleable.CircleFlowIndicator_fadeOut, 0);
		mCentered = a.getBoolean(R.styleable.CircleFlowIndicator_centered, false);

		initColors(activeColor, inactiveColor, activeType, inactiveType);
	}

	private void initColors(int activeColor, int inactiveColor, int activeType, int inactiveType) {

		switch (inactiveType) {
		case STYLE_FILL:
			mPaintInactive.setStyle(Style.FILL);
			break;
		default:
			mPaintInactive.setStyle(Style.STROKE);
		}
		mPaintInactive.setColor(inactiveColor);

		switch (activeType) {
		case STYLE_STROKE:
			mPaintActive.setStyle(Style.STROKE);
			break;
		default:
			mPaintActive.setStyle(Style.FILL);
		}
		mPaintActive.setColor(activeColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int count = 3;

		if (viewFlow != null) {
			count = viewFlow.getViewsCount();
		}

		float circleSeparation = 2 * radius + radius;
		int leftPadding = getPaddingLeft();
		/** 画未选中的圆圈 */
		for (int iLoop = 0; iLoop < count; iLoop++) {
			canvas.drawCircle(leftPadding + radius + (iLoop * circleSeparation), getPaddingTop() + radius, radius, mPaintInactive);
		}

		float cx = 0;
		if (flowWidth != 0) {
			cx = (currentScroll * circleSeparation) / flowWidth;
		}
		canvas.drawCircle(leftPadding + radius + cx, getPaddingTop() + radius, radius, mPaintActive);
	}

	@Override
	public void onSwitched(View view, int position) {
	}

	@Override
	public void setViewFlow(ViewFlow view) {
		resetTimer();
		viewFlow = view;
		flowWidth = viewFlow.getWidth();
		invalidate();
	}

	@Override
	public void onScrolled(int h, int v, int oldh, int oldv) {
		setVisibility(View.VISIBLE);
		resetTimer();
		currentScroll = h;
		flowWidth = viewFlow.getWidth();
		// 刷新界面，调用onDraw，重新绘制
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {

		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			int count = 3;
			if (viewFlow != null) {
				count = viewFlow.getViewsCount();
			}
			result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * radius) + (count - 1) * radius + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {

		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (2 * radius + getPaddingTop() + getPaddingBottom() + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	public void setFillColor(int color) {
		mPaintActive.setColor(color);
		invalidate();
	}

	public void setStrokeColor(int color) {
		mPaintInactive.setColor(color);
		invalidate();
	}

	private void resetTimer() {
		if (fadeOutTime > 0) {
			// 如果设置了显示时间
			if (timer == null || timer._run == false) {
				timer = new FadeTimer();
				timer.execute();
			} else {
				timer.resetTimer();
			}
		}
	}

	private class FadeTimer extends AsyncTask<Void, Void, Void> {

		private int timer = 0;

		private boolean _run = true;

		public void resetTimer() {
			timer = 0;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (_run) {
				try {
					Thread.sleep(1);
					timer++;

					if (timer == fadeOutTime) {
						_run = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
			animation.setAnimationListener(animationListener);
			startAnimation(animation);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		setVisibility(View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}