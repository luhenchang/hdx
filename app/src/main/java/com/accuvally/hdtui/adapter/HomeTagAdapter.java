package com.accuvally.hdtui.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.accuvally.hdtui.activity.CalenderTypeActivity;
import com.accuvally.hdtui.fragment.HomeTypeFragment;

public class HomeTagAdapter extends FragmentPagerAdapter {

	private List<String> list;
	private int type;

	public HomeTagAdapter(FragmentManager fm, List<String> list, int type) {
		super(fm);
		this.list = list;
		this.type = type;
	}
	 
 

	@Override
	public Fragment getItem(int position) {
		HomeTypeFragment fragment = new HomeTypeFragment();
		Bundle b = new Bundle();
		Log.i("info", "position:" + position + ",,,," + list.get(position));
		String title = list.get(position);
		b.putString("tag", title);
		b.putInt("type", type);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
