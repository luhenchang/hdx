package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.TicketDetails;
import com.accuvally.hdtui.ui.DrawableCenterTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class TicketDetailsAdapter extends BaseListAdapter<TicketDetails> {

	DisplayImageOptions options;

	public TicketDetailsAdapter(Context context) {
		super(context);
		options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(500)).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square_image).showImageForEmptyUri(R.drawable.default_square_image).showImageOnFail(R.drawable.default_square_image).cacheInMemory(true).cacheOnDisk(true).build();
		;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_ticket_details, null);
			viewHolder = new ViewHolder();
			viewHolder.is_more_cb = (DrawableCenterTextView) convertView.findViewById(R.id.is_more_cb);
			viewHolder.details_ticket_more = (LinearLayout) convertView.findViewById(R.id.details_ticket_more);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			viewHolder.tv_email = (TextView) convertView.findViewById(R.id.tv_email);
			viewHolder.valid_ticket = (TextView) convertView.findViewById(R.id.valid_ticket);
			viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			viewHolder.tv_expireddate = (TextView) convertView.findViewById(R.id.tv_expired_date);
			viewHolder.img_code = (ImageView) convertView.findViewById(R.id.img_code);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TicketDetails info = mList.get(position);
		int statuId = info.getStatus();
		int sourceId = 0;
		viewHolder.tv_title.setText(info.getTitle());
		viewHolder.tv_name.setText(info.getUserName());
		viewHolder.tv_time.setText(info.getStart() + "~" + info.getEnd());
		viewHolder.tv_address.setText(info.getAddress());
		viewHolder.tv_phone.setText(info.getUserPhone());
		viewHolder.tv_email.setText(info.getUserEmail());
		viewHolder.tv_type.setText(info.getTicketType());
		viewHolder.tv_price.setText(info.getPrice());
		viewHolder.tv_expireddate.setText(info.getExpiredDate());
		viewHolder.tv_id.setText("NO:" + info.getID());
		if (statuId == 0) {
			viewHolder.valid_ticket.setText("有效票券");
			sourceId = R.drawable.details_ticket_bottom_bg;
		} else if (statuId == 1) {
			viewHolder.valid_ticket.setText("票券已签到");
			sourceId = R.drawable.details_ticket_bottom_bg_other_statu;
		} else if (statuId == 2) {
			viewHolder.valid_ticket.setText("票券已过期");
			sourceId = R.drawable.details_ticket_bottom_bg_other_statu;
		}
		viewHolder.valid_ticket.setBackgroundResource(sourceId);
		application.mImageLoader.displayImage(info.getQRCode(), viewHolder.img_code, options);
		viewHolder.is_more_cb.setOnClickListener(new MoreOnClickListener(viewHolder, viewHolder.details_ticket_more));
		return convertView;
	}

	private class MoreOnClickListener implements View.OnClickListener {

		private View mContentParent;

		private ViewHolder viewHolder;

		private MoreOnClickListener(ViewHolder viewHolder, View contentParent) {
			this.mContentParent = contentParent;
			this.viewHolder = viewHolder;
		}

		public void onClick(View view) {
			boolean isVisible = mContentParent.getVisibility() == View.VISIBLE;
			if (isVisible) {

				mContentParent.setVisibility(View.GONE);
				Drawable nav_up = mContext.getResources().getDrawable(R.drawable.details_ticket_open);
				nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
				viewHolder.is_more_cb.setCompoundDrawables(null, null, nav_up, null);
			} else {
				mContentParent.setVisibility(View.VISIBLE);
				Drawable nav_up = mContext.getResources().getDrawable(R.drawable.details_ticket_close);
				nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
				viewHolder.is_more_cb.setCompoundDrawables(null, null, nav_up, null);
			}
		}
	}

	static class ViewHolder {
		TextView tv_title, tv_name, tv_time, tv_address, tv_phone, tv_email, tv_type, tv_id, tv_price, tv_expireddate;
		LinearLayout details_ticket_more;
		ImageView img_code;
		DrawableCenterTextView is_more_cb;
		TextView valid_ticket;
	}
}
