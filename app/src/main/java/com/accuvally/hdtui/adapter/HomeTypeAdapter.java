package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.utils.Utils;

public class HomeTypeAdapter extends BaseListAdapter<SelInfo> {

	private int type;

	public HomeTypeAdapter(Context context, int type) {
		super(context);
		this.type = type;
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
			viewHolder.lySelHomeType = (LinearLayout) convertView.findViewById(R.id.lySelHomeType);
			viewHolder.ivSelHomeType = (ImageView) convertView.findViewById(R.id.ivSelHomeType);
			viewHolder.tvSelHomeType = (TextView) convertView.findViewById(R.id.tvSelHomeType);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SelInfo info = mList.get(position);
		viewHolder.lySelHomeType.setVisibility(View.VISIBLE);
		viewHolder.tvSelLikeNum.setVisibility(View.VISIBLE);
		viewHolder.tvSelLikeNum.setText(info.getLikeNum() + "");
		if (type == 1) {// 附近
			viewHolder.lySelHomeType.setBackgroundResource(R.drawable.sel_near_bg);
			viewHolder.ivSelHomeType.setBackgroundResource(R.drawable.sel_location_icon_bg);
			viewHolder.tvSelHomeType.setText(info.getDistance());
			viewHolder.tvSelLikeNum.setVisibility(View.VISIBLE);
		} else if (type == 2) {// 热门
			viewHolder.lySelHomeType.setBackgroundResource(R.drawable.sel_hot_bg);
			viewHolder.ivSelHomeType.setBackgroundResource(R.drawable.sel_hot_icon_bg);
			viewHolder.tvSelHomeType.setText(info.getVisitNum());
			viewHolder.tvSelLikeNum.setVisibility(View.GONE);
		} else if(type==3){// 最新
			viewHolder.lySelHomeType.setBackgroundResource(R.drawable.sel_new_bg);
			viewHolder.ivSelHomeType.setBackgroundResource(R.drawable.sel_new_icon_bg);
			viewHolder.tvSelHomeType.setText(info.getRemindStr());
			viewHolder.tvSelLikeNum.setVisibility(View.VISIBLE);
		}else if(type==4){
			viewHolder.lySelHomeType.setVisibility(View.GONE);
			viewHolder.ivSelHomeType.setVisibility(View.GONE);;
			viewHolder.tvSelHomeType.setVisibility(View.GONE);;
			viewHolder.tvSelLikeNum.setVisibility(View.GONE);
		}
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivSelImage);
		viewHolder.tvSelTime.setText(info.getStartutc());
		viewHolder.tvSelTitle.setText(info.getTitle());
		viewHolder.tvSelAddress.setText(info.getAddress());
		viewHolder.tvState.setText(info.getStatusstr());
		viewHolder.tvSelLikeNum.setText(info.getVisitNum() + "");
		if (info.getSourceType() == 1) {// 1活动推 隐藏价格
			viewHolder.tvSelPriceArea.setVisibility(View.GONE);
		} else {
			viewHolder.tvSelPriceArea.setVisibility(View.VISIBLE);
			viewHolder.tvSelPriceArea.setText(info.getPriceArea());
		}
//		if (info.isIsFollow()) {
//			Drawable left = mContext.getResources().getDrawable(R.drawable.sel_follow_selected);
//			viewHolder.tvSelLikeNum.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
//		} else {
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
		ImageView ivSelImage, ivSelHomeType;
		TextView tvSelPriceArea, tvSelTitle, tvSelAddress, tvSelTime, tvState, tvSelLikeNum;
		LinearLayout lySelHomeType;
		TextView tvSelHomeType;
	}

}
