package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.activity.HomeTagActivity;
import com.accuvally.hdtui.model.BannerInfo;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HomeAdapter extends BaseListAdapter<BannerInfo> {

	DisplayImageOptions options;
	private int[] homeIcons = { R.drawable.home_icon1, R.drawable.home_icon2, R.drawable.home_icon3, R.drawable.home_icon4,
			R.drawable.home_icon5, R.drawable.home_icon6, R.drawable.home_icon7, };

	public HomeAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_details_image).showImageForEmptyUri(R.drawable.default_details_image).showImageOnFail(R.drawable.default_details_image).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_home, null);
			viewHolder = new ViewHolder();
			viewHolder.ivHomeImg = (ImageView) convertView.findViewById(R.id.ivHomeImg);
			viewHolder.ivhomeTag = (ImageView) convertView.findViewById(R.id.ivhomeTag);
			viewHolder.tvHomeItemTitle = (TextView) convertView.findViewById(R.id.tvHomeItemTitle);
			viewHolder.ivHomeTitleIcon = (ImageView) convertView.findViewById(R.id.ivHomeTitleIcon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final BannerInfo info = mList.get(position);
		viewHolder.ivHomeTitleIcon.setImageResource(homeIcons[position]);//title对应的小图标
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivHomeImg);
		viewHolder.tvHomeItemTitle.setText(info.getTitle());
		viewHolder.ivhomeTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isFastDoubleClick()) {
					return;
				}
				if (info.isResultForSearch()) {
					db.insertSaveBeHavior(application.addBeHavior(80, 0 + "", "", "", info.getParams(), "", ""));
					db.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_TOPIC", info.getParams()));
					mContext.startActivity(new Intent(mContext, HomeTagActivity.class).putExtra("tag", info.getParams()).putExtra("titleName", info.getTitle()));
				} else {
					if (info.isOpenInWeb()) {
						mContext.startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl", info.getUrl()).putExtra("injectJs", ""));
					} else {
						mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()));
					}
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView ivHomeImg, ivhomeTag, ivHomeTitleIcon;
		TextView tvHomeItemTitle;
	}

}
