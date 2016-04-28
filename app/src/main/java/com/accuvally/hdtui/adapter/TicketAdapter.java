package com.accuvally.hdtui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.DetailsTicketInfo;

public class TicketAdapter extends BaseListAdapter<DetailsTicketInfo> {
	
	private boolean hasCoupon;//输入优惠码后显示 中划线原价
	private List<DetailsTicketInfo> originalPriceList = new ArrayList<DetailsTicketInfo>();

	public TicketAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_tickets, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTicketPrice = (TextView) convertView.findViewById(R.id.tvTicketPrice);
			viewHolder.tvLineationPrice = (TextView) convertView.findViewById(R.id.tvLineationPrice);//原价 中划线
			viewHolder.tvTicketTitle = (TextView) convertView.findViewById(R.id.tvTicketTitle);
			viewHolder.tvTicketContent = (TextView) convertView.findViewById(R.id.tvTicketContent);
			viewHolder.tvTicketStatus = (TextView) convertView.findViewById(R.id.tvTicketStatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DetailsTicketInfo info = mList.get(position);
		viewHolder.tvTicketTitle.setText(info.getTitle());
		viewHolder.tvTicketContent.setText(info.getDesc());
		setTicketStatus(viewHolder.tvTicketStatus, info.getStatus(), info.isHadReg());

		viewHolder.tvTicketPrice.setText(info.getPriceStr());
		if (info.getPrice() > 0) {
			viewHolder.tvTicketPrice.setTextColor(mContext.getResources().getColor(R.color.yellow));
			if (hasCoupon) {
				viewHolder.tvLineationPrice.setVisibility(View.VISIBLE);

				DetailsTicketInfo originalTicket = originalPriceList.get(position);
				TextPaint paint = viewHolder.tvLineationPrice.getPaint();
				paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
				viewHolder.tvLineationPrice.setText(originalTicket.getPriceStr());
			} else {
				viewHolder.tvLineationPrice.setVisibility(View.GONE);
			}
		} else {
			viewHolder.tvTicketPrice.setTextColor(mContext.getResources().getColor(R.color.txt_green));
			viewHolder.tvLineationPrice.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void setTicketStatus(TextView text, int status, boolean isReg) {
		text.setTextColor(mContext.getResources().getColor(R.color.white));
		if (isReg) {
			text.setText("已购票");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else if (status == 0) {
			text.setText("未开始");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else if (status == 1) {
			text.setText("已截止");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else if (status == 2) {
			text.setText("已暂停");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else if (status == 3) {
			text.setText("已过期");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else if (status == 5) {
			text.setText("已售罄");
			text.setBackgroundResource(R.drawable.ticket_status_gray_bg);
		} else {
			text.setText("购票");
			text.setBackgroundResource(R.drawable.ticket_status_green_bg);
		}
	}
	
	static class ViewHolder {
		TextView tvTicketPrice, tvLineationPrice, tvTicketTitle, tvTicketContent, tvTicketStatus;
	}

	public boolean isHasCoupon() {
		return hasCoupon;
	}

	public void setHasCoupon(boolean hasCoupon) {
		this.hasCoupon = hasCoupon;
	}

	public List<DetailsTicketInfo> getOriginalPrice() {
		return originalPriceList;
	}

	public void setOriginalPrice(List<DetailsTicketInfo> originalPrice) {
		this.originalPriceList = originalPrice;
	}
}
