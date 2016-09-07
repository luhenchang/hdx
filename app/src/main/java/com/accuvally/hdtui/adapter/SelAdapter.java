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
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class SelAdapter extends BaseListAdapter<SelInfo> {

	DisplayImageOptions options;

	public SelAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image)
				.showImageOnFail(R.drawable.default_rectangle_image).displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true)
				.cacheOnDisk(true).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_select, null);
			viewHolder = new ViewHolder();
			viewHolder.ivSelImage = (ImageView) convertView.findViewById(R.id.ivSelImage);
			viewHolder.tvSelPriceArea = (TextView) convertView.findViewById(R.id.tvSelPriceArea);
			viewHolder.tvSelTitle = (TextView) convertView.findViewById(R.id.tvSelTitle);
			viewHolder.tvSelAddress = (TextView) convertView.findViewById(R.id.tvSelAddress);
			viewHolder.tvSelTime = (TextView) convertView.findViewById(R.id.tvSelItemTime);
			viewHolder.tvState = (TextView) convertView.findViewById(R.id.tvState);
			viewHolder.tvSelLikeNum = (TextView) convertView.findViewById(R.id.tvSelLikeNum);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SelInfo info = mList.get(position);
		viewHolder.tvSelLikeNum.setText(info.getVisitNum() + "");//改为浏览次数
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivSelImage);
		viewHolder.tvSelPriceArea.setText(info.getPriceArea());
		viewHolder.tvSelTime.setText(info.getStartutc());
		viewHolder.tvSelTitle.setText(info.getTitle());
		viewHolder.tvSelAddress.setText(info.getAddress());
		viewHolder.tvState.setText(info.getStatusstr());
//		if (info.isIsFollow()) {
//			Drawable left = mContext.getResources().getDrawable(R.drawable.sel_follow_selected);
//			viewHolder.tvSelLikeNum.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//		}else{
//			Drawable left = mContext.getResources().getDrawable(R.drawable.sel_follow_normal);
//			viewHolder.tvSelLikeNum.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isFastDoubleClick())
					return;
				db.insertSaveBeHavior(application.addBeHavior(10, 0+"", info.getId(), "", "","",""));
				if (info.getSourceType() == 1) {// 活動推
					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 1));
				} else {// 活動行
					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
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
