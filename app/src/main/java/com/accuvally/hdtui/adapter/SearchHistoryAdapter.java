package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;

public class SearchHistoryAdapter extends BaseListAdapter<String> {

	public SearchHistoryAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_history, null);
			viewHolder = new ViewHolder();
			viewHolder.tvHistoryName = (TextView) convertView.findViewById(R.id.tvHistoryName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String name = mList.get(position);
		viewHolder.tvHistoryName.setText(name);
		return convertView;
	}

	static class ViewHolder {
		TextView tvHistoryName;
	}

}
