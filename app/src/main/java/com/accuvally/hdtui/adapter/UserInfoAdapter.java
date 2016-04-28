package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.UserInfo;

public class UserInfoAdapter extends BaseListAdapter<UserInfo> {

	public UserInfoAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_at, null);
			viewHolder = new ViewHolder();
			viewHolder.ivhead = (ImageView) convertView.findViewById(R.id.iv_head);
			viewHolder.tvnick = (TextView) convertView.findViewById(R.id.tv_nick);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		UserInfo info = mList.get(position);
		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivhead);
		viewHolder.tvnick.setText(info.getNick());
		return convertView;
	}

	static class ViewHolder {
		ImageView ivhead;
		TextView tvnick;
	}

}
