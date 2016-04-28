package com.accuvally.hdtui.adapter;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.SmileInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 表情适配器
 * 
 * @author fuxianwei
 */
public class SmileAdapter extends BaseListAdapter<SmileInfo> {

	public SmileAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SmileInfo smileInfo = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.griditem_smile, parent, false);
		}
		((ImageView) convertView).setImageResource(smileInfo.getResource());
		convertView.setTag(smileInfo.getValue());
		return convertView;
	}
}
