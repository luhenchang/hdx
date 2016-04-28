package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.activity.ChatActivity;
import com.accuvally.hdtui.activity.ProjectDetailsActivity;
import com.accuvally.hdtui.activity.TicketTabActivity;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.SessionInfo;

public class NotificationAdapter extends BaseListAdapter<SessionInfo> {

	public NotificationAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_main_right, null);
			viewHolder = new ViewHolder();
			viewHolder.ivMainRightLogoUrl = (ImageView) convertView.findViewById(R.id.ivMainRightLogoUrl);
			viewHolder.tvMainRightTitle = (TextView) convertView.findViewById(R.id.tvMainRightTitle);
			viewHolder.tvMainRightContent = (TextView) convertView.findViewById(R.id.tvMainRightContent);
			viewHolder.tvMainRightTime = (TextView) convertView.findViewById(R.id.tvMainRightTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SessionInfo info = mList.get(position);
		application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivMainRightLogoUrl);
		viewHolder.tvMainRightTitle.setText(info.getTitle());
		viewHolder.tvMainRightContent.setText(info.getContent());
		if (!TextUtils.isEmpty(info.getContent())) {
			viewHolder.tvMainRightContent.setText(info.getContent());
		} else {
			viewHolder.tvMainRightContent.setVisibility(View.GONE);
		}
		Log.i("info", "sessionId:" + info.getSessionId());
		viewHolder.tvMainRightTime.setText(info.getSessionTime());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (info.getOp_type()) {
				case 1:// 活动
					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getOp_value()).putExtra("isHuodong", 0));
					break;
				case 2:// 专题
					Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
					intent.putExtra("title", info.getTitle());
					intent.putExtra("id", info.getOp_value());
					mContext.startActivity(intent);
					break;
				case 3:// 圈子
					SessionInfo session = SessionTable.querySessionById(info.getOp_value());
					if (session == null) {
						session = new SessionInfo();
						session.setSessionId(info.getOp_value());
						session.setTitle(info.getTitle());
						session.setLogoUrl(info.getLogoUrl());
						SessionTable.insertSession(session);
					}
					
					application.setCurrentSession(session);
					mContext.startActivity(new Intent(mContext, ChatActivity.class));
					
					break;
				case 4:// 主办方
					mContext.startActivity(new Intent(mContext, ProjectDetailsActivity.class).putExtra("orgId", info.getOp_value()));
					break;
				case 5:// 网页
					mContext.startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl", info.getOp_value()).putExtra("injectJs", ""));
					break;
				case 6://票券
					mContext.startActivity(new Intent(mContext, TicketTabActivity.class));
					break;
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView ivMainRightLogoUrl;
		TextView tvMainRightTitle, tvMainRightContent;
		TextView tvMainRightTime;
	}

}
