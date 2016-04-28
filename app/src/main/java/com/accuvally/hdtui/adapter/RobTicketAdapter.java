package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.model.RobTicketBean;
import com.accuvally.hdtui.utils.ViewHolder;

public class RobTicketAdapter extends BaseListAdapter<RobTicketBean> {

	public RobTicketAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = View.inflate(mContext, R.layout.listitem_robticket, null);
		}
		
		TextView tvRobTitle = ViewHolder.get(convertView, R.id.tvRobTitle);
		TextView tvRobAddress = ViewHolder.get(convertView, R.id.tvRobAddress);
		TextView tvRobTime = ViewHolder.get(convertView, R.id.tvRobTime);
		TextView tvRobVisitNum = ViewHolder.get(convertView, R.id.tvRobVisitNum);
		TextView tvRobPrice = ViewHolder.get(convertView, R.id.tvRobPrice);
		TextView tvPrimeCost = ViewHolder.get(convertView, R.id.tvPrimeCost);
		
		TextPaint paint = tvPrimeCost.getPaint();
		paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		
		final RobTicketBean item = mList.get(position);
		tvRobTitle.setText(item.getTitle());
		tvRobTime.setText(item.getTimeStr());
		tvRobVisitNum.setText(item.getVisitNum());
		tvRobPrice.setText(item.getPrice());
		tvPrimeCost.setText(item.getOriginPrice());
		
		ImageView ivRobImage = ViewHolder.get(convertView,R.id.ivRobImage);
		application.mImageLoader.displayImage(item.getLogo(), ivRobImage, UILoptions.rectangleOptions);
		
//		ImageView ivRobState = ViewHolder.get(convertView,R.id.ivRobState);
//		if (item.isRushOut()) {
//			ivRobState.setImageResource(R.drawable.rob_pickedup);
//		}
		
		Log.e(item.getId(), item.getTitle());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("isHuodong", 0);// 1 活动推 0 活动行
				intent.putExtra("isRobTicket", true);// 是否抢票活动
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

}
