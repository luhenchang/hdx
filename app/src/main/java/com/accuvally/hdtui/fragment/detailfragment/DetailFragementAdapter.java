package com.accuvally.hdtui.fragment.detailfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Andy Liu on 2017/2/7.
 */
public class DetailFragementAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] title = { "活动", "详情" };

    public DetailFragementAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public DetailFragementAdapter(FragmentManager fm, List<Fragment> fragments, String[] title) {
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