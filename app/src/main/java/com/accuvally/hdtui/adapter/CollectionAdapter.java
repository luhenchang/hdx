package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.model.ActsBean;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CollectionAdapter extends BaseListAdapter<SelInfo> {

	public CollectionAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = View.inflate(mContext, R.layout.listitem_collection, null);
		}
		TextView tvSelLikeNum = ViewHolder.get(convertView, R.id.tvSelLikeNum);
		SelInfo collectBean = mList.get(position);
		TextView tvSelPriceArea = ViewHolder.get(convertView, R.id.tvSelPriceArea);
		TextView tvSelTitle = ViewHolder.get(convertView, R.id.tvSelTitle);
		TextView tvSelAddress = ViewHolder.get(convertView, R.id.tvSelAddress);
		TextView tvSelTime = ViewHolder.get(convertView, R.id.tvSelItemTime);
		TextView tvState = ViewHolder.get(convertView, R.id.tvState);
		ImageView ivSelImage = ViewHolder.get(convertView, R.id.ivSelImage);

		application.mImageLoader.displayImage(collectBean.getLogo(), ivSelImage, UILoptions.rectangleOptions);

		tvSelPriceArea.setText(collectBean.getPriceArea());
		tvSelTitle.setText(collectBean.getTitle());
		tvSelAddress.setText(collectBean.getAddress());
		tvSelTime.setText(collectBean.getTimeStr());
		tvSelLikeNum.setText(collectBean.getVisitNum() + "");
		tvState.setText(collectBean.getStatusstr());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				SelInfo bean = mList.get(position);
				intent.putExtra("id", bean.getId());
				intent.putExtra("isHuodong", bean.getSourceType());
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}
}
