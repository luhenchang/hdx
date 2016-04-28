package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.CommentInfo;
import com.accuvally.hdtui.ui.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class CommentAdapter extends BaseListAdapter<CommentInfo> {

	DisplayImageOptions options;

	public CommentAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fragment_details_layout2, null);
			viewHolder = new ViewHolder();
			viewHolder.tvCommentNick = (TextView) convertView.findViewById(R.id.tvCommentNick);
			viewHolder.tvCommentContent = (TextView) convertView.findViewById(R.id.tvCommentContent);
			viewHolder.tvCommentTime = (TextView) convertView.findViewById(R.id.tvCommentTime);
			viewHolder.ivCommentLogoUrl = (CircleImageView) convertView.findViewById(R.id.tvCommentLogoUrl);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CommentInfo info = mList.get(position);
		viewHolder.tvCommentNick.setText(info.getNick() + ":");
		viewHolder.tvCommentContent.setText(info.getContent());
		viewHolder.tvCommentTime.setText(info.getCreateDate());
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivCommentLogoUrl, options);
		return convertView;
	}

	static class ViewHolder {
		CircleImageView ivCommentLogoUrl;
		TextView tvCommentNick, tvCommentContent, tvCommentTime;
	}
}
