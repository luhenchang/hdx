package com.accuvally.hdtui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.entry.MainActivityNew;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.utils.SharedUtils;

public class ViewPagerAdapter extends PagerAdapter {

	// private List<View> views;

	private Context context;

	private int tag;

	SharedUtils shareUtils;

	LayoutInflater mLayoutInflater;

	AccuApplication application;

	public ViewPagerAdapter(List<View> views, Context context, int tag) {
		// this.views = views;
		this.context = context;
		shareUtils = new SharedUtils(context);
		this.tag = tag;
		mLayoutInflater = LayoutInflater.from(context);
		application = (AccuApplication) context.getApplicationContext();
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		// if (views != null) {
		// return views.size();
		// }
		return 3;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// ((ViewPager) arg0).addView(views.get(arg1), 0);
		int resId = 0;
		switch (arg1) {
		case 0:
			resId = R.layout.layout_guide1;
			break;
		case 1:
			resId = R.layout.layout_guide2;
			break;
		case 2:
			resId = R.layout.layout_guide3;
			break;
		}
		View view = mLayoutInflater.inflate(resId, null);
		((ViewGroup) arg0).addView(view, 0);
		if (arg1 == 2) {
			Button mWhatNew = (Button) arg0.findViewById(R.id.what_new);
			mWhatNew.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goHome();
				}

			});
		}
		return view;
	}

	private void goHome() {
		if (tag == 1) {
			((Activity) context).finish();
		} else {
			application.sharedUtils.writeBoolean("isSynchronous", true);
			application.sharedUtils.writeInt("remind", 1);
			application.sharedUtils.writeInt("isBaidu", 3);
			application.sharedUtils.writeBoolean("isRegDevice", false);
			application.sharedUtils.writeBoolean("isFirstIn", true);
			application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, Config.ACCUPASS_ID);
			application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, Config.ACCUPASS_KEY);
			context.startActivity(new Intent(context, MainActivityNew.class));
			((Activity) context).finish();
		}
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}
}
