package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AttendAdapter extends BaseListAdapter<HomeEventInfo> {

	DisplayImageOptions options;

	public AttendAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_attend, null);
			viewHolder = new ViewHolder();
			viewHolder.tvAttendTitle = (TextView) convertView.findViewById(R.id.tvAttendTitle);
			viewHolder.tvAttendStartTime = (TextView) convertView.findViewById(R.id.tvAttendStartTime);
			viewHolder.tvAttendStatus = (TextView) convertView.findViewById(R.id.tvAttendStatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final HomeEventInfo info = mList.get(position);
		viewHolder.tvAttendTitle.setText(info.getTitle());
		viewHolder.tvAttendStartTime.setText(info.getStart() + " - " + info.getEnd());
		viewHolder.tvAttendStatus.setText(info.getStatusStr());
		if (info.getStatus() == 1) {// 即将开始
			viewHolder.tvAttendStatus.setTextColor(mContext.getResources().getColor(R.color.orange));
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.attend_time_icon_bg);
			viewHolder.tvAttendStatus.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		} else if (info.getStatus() == 2) {// 结束
			viewHolder.tvAttendStatus.setTextColor(Color.argb(255, 205, 205, 205));
			viewHolder.tvAttendStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		} else if (info.getStatus() == 3) {// 取消
			viewHolder.tvAttendStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		} else if (info.getStatus() == 4) {// 正在进行
			viewHolder.tvAttendStatus.setTextColor(mContext.getResources().getColor(R.color.txt_green));
			viewHolder.tvAttendStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView tvAttendTitle, tvAttendStartTime, tvAttendStatus;

	}

}
