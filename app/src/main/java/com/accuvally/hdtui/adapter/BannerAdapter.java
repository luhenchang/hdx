package com.accuvally.hdtui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.BannerInfo;
import com.accuvally.hdtui.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class BannerAdapter extends PagerAdapter {
	
	private List<BannerInfo> bannerList;
	private Context mContext;
	private DBManager dbManager;
	private AccuApplication application;

	public BannerAdapter(Context context, List<BannerInfo> bannerList) {
		this.bannerList = bannerList;
		this.mContext = context;
		
		application = AccuApplication.getInstance();
		dbManager = new DBManager(mContext);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return bannerList == null ? 0 : bannerList.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View layoutview = LayoutInflater.from(mContext).inflate(R.layout.listitem_square_top_list, null);
		ImageView image = (ImageView) layoutview.findViewById(R.id.iv_image);
		final BannerInfo info = bannerList.get(position);
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		application.mImageLoader.displayImage(info.getLogo(), image);
		((ViewPager) view).addView(layoutview, 0);
		layoutview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View positioin) {
				if (Utils.isFastDoubleClick()) {
					return;
				}
				dbManager.insertSaveBeHavior(application.addBeHavior(10, 0 + "", info.getId(), "", "", "", ""));
				if (info.isOpenInWeb()) {
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_BANNER", info.getUrl()));
					Intent intent = new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl", info.getUrl()).putExtra("injectJs", "");
					mContext.startActivity(intent);
				} else {
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_BANNER", info.getId()));
					Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("isHuodong", 0).putExtra("id", info.getId());
					mContext.startActivity(intent);
				}
			}
		});
		return layoutview;
	}

}
