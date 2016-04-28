package com.accuvally.hdtui.adapter;//package com.accuvally.hdtui.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.TicketVolumeDetailsActivity;
//import com.accuvally.hdtui.model.ExistTicket;
//
//public class ValidTicketAdapter extends BaseListAdapter<ExistTicket> {
//
//	public ValidTicketAdapter(Context context) {
//		super(context);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		final ViewHolder viewHolder;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.valid_ticket, null);
//			viewHolder = new ViewHolder();
//			viewHolder.ticket_user_name = (TextView) convertView.findViewById(R.id.ticket_user_name);
//			viewHolder.ticket_price = (TextView) convertView.findViewById(R.id.ticket_price);
//			viewHolder.ticket_accu_name = (TextView) convertView.findViewById(R.id.ticket_accu_name);
//			viewHolder.accu_address = (TextView) convertView.findViewById(R.id.accu_address);
//			viewHolder.ticket_count = (TextView) convertView.findViewById(R.id.ticket_count);
//			viewHolder.two_dimensional_code = (ImageView) convertView.findViewById(R.id.two_dimensional_code);
//			viewHolder.invalid_ticket = (ImageView) convertView.findViewById(R.id.invalid_ticketes);
//			viewHolder.ticket_middle_bg = (ImageView) convertView.findViewById(R.id.ticket_middle_bg);
//			viewHolder.ticket_item = (RelativeLayout) convertView.findViewById(R.id.ticket_item);
//			viewHolder.accu_info = (RelativeLayout) convertView.findViewById(R.id.accu_info);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		final ExistTicket existTicket = mList.get(position);
//		if (existTicket != null) {
//			if (existTicket.getInstanceType() == 1) {
//				viewHolder.ticket_count.setVisibility(View.VISIBLE);
//				viewHolder.ticket_count.setText(existTicket.getCount() + "张");
//			} else {
//				viewHolder.ticket_count.setVisibility(View.GONE);
//			}
//
//			viewHolder.ticket_user_name.setText(existTicket.getUserName());
//			viewHolder.ticket_price.setText(existTicket.getPrice());
//			if (existTicket.isFree()) {
//				viewHolder.ticket_price.setTextColor(Color.argb(255, 98, 182, 81));
//			} else {
//				viewHolder.ticket_price.setTextColor(Color.argb(255, 255, 76, 00));
//			}
//			if (existTicket.isAvailable()) {
//				viewHolder.invalid_ticket.setVisibility(View.GONE);
//				viewHolder.two_dimensional_code.setVisibility(View.VISIBLE);
//
//			} else {
//				viewHolder.invalid_ticket.setVisibility(View.VISIBLE);
//				viewHolder.two_dimensional_code.setVisibility(View.GONE);
//
//			}
//			viewHolder.ticket_accu_name.setText(existTicket.getTitle());
//			viewHolder.accu_address.setText(existTicket.getAddr() + "  |  " + existTicket.getStart().substring(0, 10).replace("-", "."));
//
//			viewHolder.ticket_item.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					mContext.startActivity(new Intent(mContext, TicketVolumeDetailsActivity.class).putExtra("id", existTicket.getId()));
//				}
//			});
//		}
//		return convertView;
//	}
//
//	public static class ViewHolder {
//		TextView ticket_user_name, ticket_price, ticket_accu_name, accu_address, accu_time, ticket_count;
//		ImageView invalid_ticket, two_dimensional_code, ticket_middle_bg;
//		RelativeLayout ticket_item, accu_info;
//
//	}
//}
