package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;

import java.util.HashMap;
import java.util.List;

public class ClassfyAdapter extends BaseAdapter {

	Context context;

	List<HashMap<String, Object>> list;

	protected LayoutInflater mInflater;

	AccuApplication application;

	List<String> listString;

	public ClassfyAdapter(Context context, List<HashMap<String, Object>> list, List<String> listString) {
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		application = (AccuApplication) context.getApplicationContext();
		this.listString = listString;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_classfly, null);
			viewHolder = new ViewHolder();
			viewHolder.ivClassImg = (ImageView) convertView.findViewById(R.id.ivClassImg);
			viewHolder.tvClassAddImg = (ImageView) convertView.findViewById(R.id.tvClassAddImg);
			viewHolder.tvClassTitle = (TextView) convertView.findViewById(R.id.tvClassTitle);
			viewHolder.tvClassContent = (TextView) convertView.findViewById(R.id.tvClassContent);
			viewHolder.lyclass = (LinearLayout) convertView.findViewById(R.id.lyclass);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int drawable = Integer.parseInt(list.get(position).get("image").toString());
		viewHolder.ivClassImg.setImageResource(drawable);
		viewHolder.tvClassTitle.setText(list.get(position).get("title").toString());
		viewHolder.tvClassContent.setText(list.get(position).get("content").toString());
		try {
			if (listString.contains(list.get(position).get("title").toString())) {
				viewHolder.tvClassAddImg.setVisibility(View.VISIBLE);
				viewHolder.lyclass.setBackgroundResource(R.color.transparent);
			} else {
				viewHolder.tvClassAddImg.setVisibility(View.GONE);
				viewHolder.lyclass.setBackgroundResource(R.drawable.classfly_icon_add_normal);
			}
		} catch (Exception e) {

		}
		return convertView;
	}

	static class ViewHolder {
		ImageView ivClassImg;
		TextView tvClassTitle, tvClassContent;
		ImageView tvClassAddImg;
		LinearLayout lyclass;

	}
}
