package com.accuvally.hdtui.adapter;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;

public class LocationAdapter extends BaseListAdapter<HashMap<String, Object>> {

	private Context context;

	private Handler handler;

	public LocationAdapter(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_dialog_location_map, null);
			viewHolder = new ViewHolder();
			viewHolder.tvMapName = (TextView) convertView.findViewById(R.id.tvMapName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, Object> map = mList.get(position);
		viewHolder.tvMapName.setText(map.get("mapName").toString());
		Drawable drawable = null;
		if (Integer.parseInt(map.get("mapId").toString()) == 1) {
			drawable = context.getResources().getDrawable(R.drawable.baidu_location_bg);
		} else if (Integer.parseInt(map.get("mapId").toString()) == 2) {
			drawable = context.getResources().getDrawable(R.drawable.gaode_location_bg);
		} 
		viewHolder.tvMapName.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				if (Integer.parseInt(map.get("mapId").toString()) == 1) {
					msg.obj = 1;
					msg.what = 1;
					handler.sendMessage(msg);
				} else if (Integer.parseInt(map.get("mapId").toString()) == 2) {
					msg.obj = 2;
					msg.what = 2;
					handler.sendMessage(msg);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView tvMapName;
	}
}
