package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	private static final int STATE_LAZY = 3;

	private ImageView imageView;

	private Context mContext;

	private View mContentView;

	private TextView mHintView;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		if (state == STATE_READY) {
			imageView.setVisibility(View.INVISIBLE);
			mHintView.setVisibility(View.INVISIBLE);
			mHintView.setText(R.string.pull_to_refresh_release_label);
		} else if (state == STATE_LOADING) {
			mHintView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_to_refresh_footer_refreshing_label);
		} else {
			imageView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.pull_to_refresh_footer_pull_label);
			mHintView.setVisibility(View.INVISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		imageView.setVisibility(View.VISIBLE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		imageView.setVisibility(View.GONE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);

		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
		imageView = (ImageView) moreView.findViewById(R.id.xlistview_footer_progressbar);
		imageView.setBackgroundResource(R.drawable.loading);
		final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		imageView.post(new Runnable() {
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
	}

}
