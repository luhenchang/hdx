package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	// private ProgressBar mProgressBar;

	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	//	private Animation mRotateUpAnim;
	//	private Animation mRotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;
	private Animation animation;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	ImageView imageView;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);

		//		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		//		mRotateUpAnim.setFillAfter(true);
		//		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		//		mRotateDownAnim.setFillAfter(true);

		imageView = (ImageView) findViewById(R.id.xlistview_header_progressbar);
		imageView.setBackgroundResource(R.drawable.loading);
		final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		imageView.post(new Runnable() {
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
	}

	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) {
			//			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		} else {
			mArrowImageView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		}

		//		switch (state) {
		//		case STATE_NORMAL:
		//			if (mState == STATE_READY) {
		//				//						mArrowImageView.startAnimation(mRotateDownAnim);
		//			}
		//			if (mState == STATE_REFRESHING) {
		//				mArrowImageView.clearAnimation();
		//			}
		//			mHintTextView.setText(R.string.pull_to_refresh_pull_label);
		//			break;
		//		case STATE_READY:
		//			if (mState != STATE_READY) {
		//				mArrowImageView.clearAnimation();
		//				//						mArrowImageView.startAnimation(mRotateUpAnim);
		//				mHintTextView.setText(R.string.pull_to_refresh_release_label);
		//			}
		//			break;
		//		case STATE_REFRESHING:
		//			mHintTextView.setText(R.string.pull_to_refresh_refreshing_label);
		//			break;
		//		default:
		//		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
