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
import com.accuvally.hdtui.activity.PayWebActivity;
import com.accuvally.hdtui.activity.SureOrderActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.model.UnfinishedTicket;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class InvalidTicketAdapter extends BaseListAdapter<UnfinishedTicket> {

	private DisplayImageOptions options;

	public InvalidTicketAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.showImageOnLoading(R.drawable.default_rectangle_image)
		.showImageForEmptyUri(R.drawable.default_rectangle_image)
		.showImageOnFail(R.drawable.default_rectangle_image)
		.cacheInMemory(true)
//		.displayer(new FadeInBitmapDisplayer(500))
		.displayer(new RoundedBitmapDisplayer(15))
		.cacheOnDisk(true)
		.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_unfinish, null);
			viewHolder = new ViewHolder();
			viewHolder.ivSelImage = (ImageView) convertView.findViewById(R.id.ivSelImage);
			viewHolder.tvSelTitle = (TextView) convertView.findViewById(R.id.tvSelTitle);
			viewHolder.tvSelAddress = (TextView) convertView.findViewById(R.id.tvSelAddress);
			viewHolder.tvSelItemTime = (TextView) convertView.findViewById(R.id.tvSelItemTime);
			viewHolder.tvSelPriceArea = (TextView) convertView.findViewById(R.id.tvSelPriceArea);
			viewHolder.tvState = (TextView) convertView.findViewById(R.id.tvState);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final UnfinishedTicket unfinishedTicket = mList.get(position);
		application.mImageLoader.displayImage(unfinishedTicket.getLogo(), viewHolder.ivSelImage, UILoptions.rectangleOptions);
		viewHolder.tvSelTitle.setText(unfinishedTicket.getTitle());
		viewHolder.tvSelItemTime.setText(unfinishedTicket.getTimeStr());
		viewHolder.tvSelAddress.setText(unfinishedTicket.address);
		viewHolder.tvSelPriceArea.setText(unfinishedTicket.getPrice());
		if ("免费".equals(unfinishedTicket.getPrice())) {
			viewHolder.tvSelPriceArea.setTextColor(mContext.getResources().getColor(R.color.price_free));
		} else {
			viewHolder.tvSelPriceArea.setTextColor(mContext.getResources().getColor(R.color.price_charge));
		}
		
		if (unfinishedTicket.getBtnStatus() == 1) {
			viewHolder.tvState.setText("立即付款");
			viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.red2));
			viewHolder.tvState.setBackgroundResource(R.drawable.oval_golden);
		} else if (unfinishedTicket.getBtnStatus() == 2) {
			viewHolder.tvState.setText("审核中");
			viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.darkgoldenrod));
			viewHolder.tvState.setBackgroundResource(R.drawable.oval_pink);
		} else if(unfinishedTicket.getBtnStatus() == 3) {
			viewHolder.tvState.setText("票券失效");
			viewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.gary_title));
			viewHolder.tvState.setBackgroundResource(R.drawable.oval_pink);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (viewHolder.tvState.getText().toString().equals("立即付款")) {
					if (Utils.isFastDoubleClick()) {
						return;
					}
					if (!"".equals(unfinishedTicket.getExpiredDate())) {
						if (unfinishedTicket.getIsAppPay() == 1) {
							mContext.startActivity(new Intent(mContext, SureOrderActivity.class).putExtra("id", unfinishedTicket.getId()).putExtra("tag", 0));
						} else {
							mContext.startActivity(new Intent(mContext, PayWebActivity.class).putExtra("payUrl", unfinishedTicket.getPayUrl()));
						}
					}
				} else if (viewHolder.tvState.getText().toString().equals("等待审核")) {
					application.showMsg("还在审核中，请等待片刻哦！");
				}
			}
		});
		return convertView;
	}

	protected void skipActivity(UnfinishedTicket child, TextView right) {

	}

	static class ViewHolder {
		ImageView ivSelImage;
		TextView tvSelTitle, tvSelAddress, tvSelItemTime, tvSelPriceArea, tvState;
	}
}
