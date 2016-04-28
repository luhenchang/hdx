package com.accuvally.hdtui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.DetailsTicketInfo;

public class AccuvallyAdapter extends BaseListAdapter<DetailsTicketInfo> {

	private int arg2;

	private Handler handelr;

	public AccuvallyAdapter(Context context, int arg2, Handler handler) {
		super(context);
		this.arg2 = arg2;
		this.handelr = handler;
	}

	public AccuvallyAdapter(Context context, int arg2) {
		super(context);
		this.arg2 = arg2;

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		// if (convertView == null) {
		convertView = mInflater.inflate(R.layout.listitem_home_details_ticket, null);
		viewHolder = new ViewHolder();
		viewHolder.tvTicketTitle = (TextView) convertView.findViewById(R.id.tvTicketTitle);
		viewHolder.tvTicketPrice = (TextView) convertView.findViewById(R.id.tvTicketPrice);
		viewHolder.tvTicketContent = (TextView) convertView.findViewById(R.id.tvTicketContent);
		viewHolder.ll_detail_ticket = (LinearLayout) convertView.findViewById(R.id.ll_detail_ticket);
		convertView.setTag(viewHolder);
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }
		DetailsTicketInfo info = mList.get(position);
		viewHolder.tvTicketTitle.setText(info.getTitle());
		if (info.getPrice() > 0) {
			viewHolder.tvTicketPrice.setTextColor(mContext.getResources().getColor(R.color.yellow));
		} else {
			viewHolder.tvTicketPrice.setTextColor(mContext.getResources().getColor(R.color.txt_green));
		}
		viewHolder.tvTicketPrice.setText(info.getPriceStr());
		if (position == arg2) {
			viewHolder.ll_detail_ticket.setBackgroundResource(R.drawable.details_ticket_bg_selected);
			viewHolder.tvTicketContent.setVisibility(View.VISIBLE);
			viewHolder.tvTicketContent.setText(info.getDesc());

			Message msg = new Message();
			msg.what = 1;
			msg.arg2 = position;

			handelr.sendMessage(msg);
		}
		viewHolder.ll_detail_ticket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				arg2 = position;
				Message msg = new Message();
				msg.what = 2;
				msg.arg2 = position;
				handelr.sendMessage(msg);
			}
		});

		return convertView;
	}

	public static class ViewHolder {

		TextView tvTicketTitle, tvTicketPrice, tvTicketContent;
		LinearLayout ll_detail_ticket;

	}

}
