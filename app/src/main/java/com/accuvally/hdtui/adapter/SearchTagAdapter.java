package com.accuvally.hdtui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.SearchResultActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.ui.AutoScaleTextView;

public class SearchTagAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private Context mContext;

	private List<String> list;

	private DBManager dbManager;

	AccuApplication application;

	public SearchTagAdapter(Context context, List<String> list) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.list = list;
		dbManager = new DBManager(mContext);
		application = (AccuApplication) mContext.getApplicationContext();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_search_tag, null);
			viewHolder = new ViewHolder();
			viewHolder.menu_tv = (AutoScaleTextView) convertView.findViewById(R.id.menu_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.menu_tv.setText(list.get(position));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dbManager.insertSaveBeHavior(application.addBeHavior(80, 0+"", "", "", list.get(position),"",""));
				mContext.startActivity(new Intent(mContext, SearchResultActivity.class).putExtra("searchdata", list.get(position)).putExtra("flag", 1));
			}
		});
		return convertView;
	}

	static class ViewHolder {
		AutoScaleTextView menu_tv;
	}
}
