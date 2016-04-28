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
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.model.ActsBean;
import com.accuvally.hdtui.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SponsorAccuvallyAdapter extends BaseListAdapter<ActsBean> {


	public SponsorAccuvallyAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = View.inflate(mContext, R.layout.listitem_collection, null);
		}

		TextView tvSelLikeNum = ViewHolder.get(convertView, R.id.tvSelLikeNum);

		ActsBean bean = mList.get(position);
		TextView tvSelPriceArea = ViewHolder.get(convertView, R.id.tvSelPriceArea);
		TextView tvSelTitle = ViewHolder.get(convertView, R.id.tvSelTitle);
		TextView tvSelAddress = ViewHolder.get(convertView, R.id.tvSelAddress);
		TextView tvSelTime = ViewHolder.get(convertView, R.id.tvSelItemTime);
		TextView tvState = ViewHolder.get(convertView, R.id.tvState);

		ImageView ivSelImage = ViewHolder.get(convertView, R.id.ivSelImage);
		application.mImageLoader.displayImage(bean.logo, ivSelImage, UILoptions.rectangleOptions);

		tvSelPriceArea.setText(bean.getPriceArea());
		tvSelTitle.setText(bean.getTitle());
		tvSelAddress.setText(bean.getAddress());
		tvSelTime.setText(bean.getTimeStr());
		tvState.setText(bean.statusstr);
		tvSelLikeNum.setText(bean.getVisitNum() + "");//收藏次数改为浏览次数

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				ActsBean bean = mList.get(position);
				if (bean == null) return;
				intent.putExtra("id", bean.getId());
				if (bean.getSourceType() == 1) {// 活动推
					intent.putExtra("isHuodong", 1);
				} else {// 活动行
					intent.putExtra("isHuodong", 0);
				}
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}
}
