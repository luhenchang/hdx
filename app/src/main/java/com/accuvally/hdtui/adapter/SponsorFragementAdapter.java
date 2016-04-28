package com.accuvally.hdtui.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class SponsorFragementAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private String[] title = { "未来活动", "过去活动" };

	public SponsorFragementAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	
	public SponsorFragementAdapter(FragmentManager fm, List<Fragment> fragments, String[] title) {
		super(fm);
		this.fragments = fragments;
		this.title = title;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}
}
