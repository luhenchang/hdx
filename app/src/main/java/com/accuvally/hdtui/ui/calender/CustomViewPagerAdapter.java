package com.accuvally.hdtui.ui.calender;


import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CustomViewPagerAdapter<V extends View> extends PagerAdapter {

	private final V[] views;
	private final int len;

	public CustomViewPagerAdapter(V[] views) {
		super();
		this.views = views;
		this.len = views.length;
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object instantiateItem(View arg0, int position) {
		final ViewPager pager = (ViewPager) arg0;
		
		View child = views[position % len];
		if (child.getParent() != null) {
			pager.removeView(child);
		}
		pager.addView(child);
		return child;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {

	}

	@Override
	public void startUpdate(View arg0) {
	}

	public V[] getAllItems() {
		return views;
	}
}
