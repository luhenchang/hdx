package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.utils.Util;

public class PaySuccessAdapter extends BaseListAdapter<HomeEventInfo> {

	DBManager db;

	public PaySuccessAdapter(Context context) {
		super(context);
		db = new DBManager(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_home_new, null);
			viewHolder = new ViewHolder();
			viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			viewHolder.tvHomeTitle = (TextView) convertView.findViewById(R.id.tvHomeTitle);
			viewHolder.tvHomeAddress = (TextView) convertView.findViewById(R.id.tvHomeAddress);
			viewHolder.tvHomeTime = (TextView) convertView.findViewById(R.id.tvHomeTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final HomeEventInfo info = mList.get(position);
		viewHolder.tvHomeTitle.setText(Util.stringFilter(info.getTitle()));
		String str = "";
		if (info.getAddress().length() > 6) {
			str = info.getAddress().substring(0, 5) + "...";
		} else {
			str = info.getAddress();
		}
		String addressAndTime = str + "  |  " + info.getStart().substring(5, 10).replace("-", "月");
		viewHolder.tvHomeAddress.setText(addressAndTime);

		if (!"".equals(info.getStatusStr())) {
			viewHolder.tvHomeTime.setVisibility(View.VISIBLE);
			viewHolder.tvHomeTime.setText(info.getStatusStr());
		} else {
			viewHolder.tvHomeTime.setVisibility(View.GONE);
		}
		application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivImage);
		return convertView;
	}

	static class ViewHolder {
		ImageView ivImage;
		TextView tvHomeTitle, tvHomeAddress, tvHomeTime;
	}

}
