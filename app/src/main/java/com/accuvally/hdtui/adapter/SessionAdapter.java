package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.message.NotificationActivity;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.ui.BadgeView;
import com.accuvally.hdtui.ui.EmoteTextView;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;

import java.util.List;

import de.greenrobot.event.EventBus;

public class SessionAdapter extends BaseListAdapter<SessionInfo> {

	public SessionAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_main_right, null);
			viewHolder = new ViewHolder();
			viewHolder.ivUnReadNum = convertView.findViewById(R.id.tvUnreadNum);
			viewHolder.ivMainRightLogoUrl = (ImageView) convertView.findViewById(R.id.ivMainRightLogoUrl);
			viewHolder.tvMainRightTitle = (TextView) convertView.findViewById(R.id.tvMainRightTitle);
			viewHolder.tvMainRightContent = (EmoteTextView) convertView.findViewById(R.id.tvMainRightContent);
			viewHolder.tvMainRightTime = (TextView) convertView.findViewById(R.id.tvMainRightTime);
			viewHolder.unread = new BadgeView(mContext, viewHolder.ivUnReadNum);
			viewHolder.unread.setBackgroundResource(R.drawable.leancloud_unreadnum_bg);
			viewHolder.unread.setTextColor(mContext.getResources().getColor(android.R.color.white));
			viewHolder.unread.setTextSize(12);
			viewHolder.unread.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
			viewHolder.unread.setGravity(Gravity.CENTER);
			viewHolder.unread.setBadgeMargin(10);
			viewHolder.unread.setPadding(0, 0, 0, 0);
			viewHolder.unread.hide();
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final SessionInfo info = mList.get(position);
		
		viewHolder.tvMainRightTime.setText(info.getSessionTime());
		if (info.isNotification()) {
			viewHolder.ivMainRightLogoUrl.setImageResource(R.drawable.system_sessioin_entrance);
			viewHolder.tvMainRightTitle.setText("通知");
			viewHolder.tvMainRightContent.setText(info.getContent());
		} else {
			viewHolder.tvMainRightTitle.setText(info.getTitle());
			viewHolder.tvMainRightContent.setText(info.getTypeContent());
			if (info.isPrivateChat()) {
				application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivMainRightLogoUrl, UILoptions.defaultUser);
			} else {
				application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivMainRightLogoUrl, UILoptions.squareOptions);
			}
		}
		
		if (info.getUnReadNum() > 0) {
			viewHolder.unread.setText(String.valueOf(info.getUnReadNum()));
			viewHolder.unread.show();
			if (info.getUnReadNum() > 100) {
				viewHolder.unread.setText("99+");
				viewHolder.unread.show();
			} else {
				viewHolder.unread.setText(String.valueOf(info.getUnReadNum()));
				viewHolder.unread.show();
			}
		} else {
			viewHolder.unread.hide();
		}
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				info.setUnReadNum(0);
				viewHolder.unread.hide();
				
				if (info.isNotification()) {
//					List<SessionInfo> list = db.querySession(application.getUserInfo().getAccount(), 1, Integer.MAX_VALUE);
					List<SessionInfo> list = SessionTable.queryAllNotification(AccountManager.getAccount());
					for (SessionInfo sessionInfo : list) {
						SessionTable.updateSessionByUnReadNum(sessionInfo.getSessionId());
					}
					
					Intent intent = new Intent(mContext, NotificationActivity.class);
					mContext.startActivity(intent);
				} else {
					SessionTable.updateSessionByUnReadNum(info.getSessionId());
					application.setCurrentSession(info);
					
					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("isPrivateChat", info.isPrivateChat());
					mContext.startActivity(intent);
				}
				
				EventBus.getDefault().post(new ChangeMessageEventBus());
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView ivMainRightLogoUrl;
		TextView tvMainRightTitle;
		EmoteTextView tvMainRightContent;
		BadgeView unread;
		View ivUnReadNum;
		TextView tvMainRightTime;
	}

}
