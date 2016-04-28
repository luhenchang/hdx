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
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SelectAdapter extends BaseListAdapter<SelInfo> {

	public SelectAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_collection, null);
			viewHolder = new ViewHolder();
			viewHolder.tvSelLikeNum = (TextView) convertView.findViewById(R.id.tvSelLikeNum);
			viewHolder.tvSelPriceArea = (TextView) convertView.findViewById(R.id.tvSelPriceArea);
			viewHolder.tvSelTitle = (TextView) convertView.findViewById(R.id.tvSelTitle);
			viewHolder.tvSelAddress = (TextView) convertView.findViewById(R.id.tvSelAddress);
			viewHolder.tvSelTime = (TextView) convertView.findViewById(R.id.tvSelItemTime);
			viewHolder.tvState = (TextView) convertView.findViewById(R.id.tvState);
			viewHolder.ivSelImage = (ImageView) convertView.findViewById(R.id.ivSelImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SelInfo info = mList.get(position);
		viewHolder.ivSelImage.setTag(info.getLogo());
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivSelImage, UILoptions.rectangleOptions);
		viewHolder.tvSelTitle.setText(info.getTitle());
		viewHolder.tvSelAddress.setText(info.getAddress());
		viewHolder.tvSelTime.setText(info.getTimeStr());
		viewHolder.tvSelLikeNum.setText(info.getVisitNum() + "");
		viewHolder.tvState.setText(info.getStatusstr());
		if (info.getSourceType() == 1) {// 1活动推 隐藏价格
			viewHolder.tvSelPriceArea.setVisibility(View.GONE);
		} else {
			viewHolder.tvSelPriceArea.setVisibility(View.VISIBLE);
			viewHolder.tvSelPriceArea.setText(info.getPriceArea());
		}
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isFastDoubleClick())
					return;
				db.insertSaveBeHavior(application.addBeHavior(10, 0 + "", info.getId(), "", "", "", ""));
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				if (info.getSourceType() == 1) {// 活動推
					mContext.startActivity(intent.putExtra("id", info.getId()).putExtra("isHuodong", 1));
				} else {// 活動行
					mContext.startActivity(intent.putExtra("id", info.getId()).putExtra("isHuodong", 0));
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView ivSelImage;
		TextView tvSelPriceArea, tvSelTitle, tvSelAddress, tvSelTime, tvState, tvSelLikeNum;
	}
}
