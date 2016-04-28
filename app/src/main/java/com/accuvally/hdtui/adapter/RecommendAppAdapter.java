package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.RecomendAppInfo;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class RecommendAppAdapter extends BaseListAdapter<RecomendAppInfo> {

	private DisplayImageOptions options;

	public RecommendAppAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_recommend, parent, false);
			holder = new ViewHolder();
			holder.ivRecommendImg = (ImageView) convertView.findViewById(R.id.ivRecommendImg);
			holder.tvRecommendTitle = (TextView) convertView.findViewById(R.id.tvRecommendTitle);
			holder.tvRecommendContent = (TextView) convertView.findViewById(R.id.tvRecommendContent);
			holder.llTipAppUrl = (LinearLayout) convertView.findViewById(R.id.llTipAppUrl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RecomendAppInfo info=mList.get(position);
		holder.tvRecommendTitle.setText(info.getName());
		holder.tvRecommendContent.setText(info.getRemark());
		application.mImageLoader.displayImage(info.getLogo(), holder.ivRecommendImg, options);
		holder.llTipAppUrl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utils.isFastDoubleClick())
					return;

				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(info.getUrl())));
			}
		});
		return convertView;
	}
	class ViewHolder{
		LinearLayout llTipAppUrl;
		ImageView ivRecommendImg;
		TextView tvRecommendTitle;
		TextView tvRecommendContent;
	}


}
