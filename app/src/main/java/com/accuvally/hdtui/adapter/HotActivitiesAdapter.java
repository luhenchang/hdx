package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 热门-推荐-发布活动
 * 
 * @author Semmer Wang
 * 
 */
public class HotActivitiesAdapter extends BaseListAdapter<HomeEventInfo> {

	DisplayImageOptions options;

	DBManager db;

	Handler handler;

	int tag;

	public HotActivitiesAdapter(Context context, Handler handler, int tag) {
		super(context);
		db = new DBManager(context);
		this.handler = handler;
		this.tag = tag;
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_details_accuvally_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tvHotTitle = (TextView) convertView.findViewById(R.id.tvHotTitle);
			viewHolder.ivHotLogoUrl = (ImageView) convertView.findViewById(R.id.ivHotLogoUrl);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final HomeEventInfo info = mList.get(position);
		viewHolder.tvHotTitle.setText(info.getTitle());
		application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivHotLogoUrl, options);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isFastDoubleClick())
					return;
				db.insertHistory(info);
				if (tag == 1) {
					Message message = new Message();
					message.what = 1;
					message.obj = info.getId();
					handler.sendMessage(message);
				} else
					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView tvHotTitle;
		ImageView ivHotLogoUrl;
	}
}
