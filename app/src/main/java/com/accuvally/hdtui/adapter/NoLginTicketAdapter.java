package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.model.NotTicket;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.Utils;

/**
 * 未登录票劵
 */
public class NoLginTicketAdapter extends BaseListAdapter<NotTicket> {

	public NoLginTicketAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		NoLginTicketViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_not_login_ticket_list, null);
			viewHolder = new NoLginTicketViewHolder();
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
			viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);			
			viewHolder.rl_click_expand=(RelativeLayout)convertView.findViewById(R.id.rl_click_expand);
			viewHolder.ll_click_tip_detial=(LinearLayout)convertView.findViewById(R.id.ll_click_tip_detial);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (NoLginTicketViewHolder) convertView.getTag();
		}
		final NotTicket info = mList.get(position);
		viewHolder.tv_title.setText(Util.stringFilter(info.getTitle()));
		viewHolder.tv_date.setText(info.getStart());
		viewHolder.tv_right.setText(Util.appendSpace("立即取票") + "");
		viewHolder.ll_click_tip_detial.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (Utils.isFastDoubleClick())
					return;
				mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
			}
		});

		return convertView;
	}
}

class NoLginTicketViewHolder {
	TextView tv_title, tv_right, tv_date;
	LinearLayout ll_click_tip_detial;
	RelativeLayout rl_click_expand;
	

	
}