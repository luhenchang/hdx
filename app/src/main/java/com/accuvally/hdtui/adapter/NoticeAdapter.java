package com.accuvally.hdtui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.activity.ChatActivity;
import com.accuvally.hdtui.activity.GeTuiWapActivity;
import com.accuvally.hdtui.activity.ProjectDetailsActivity;
import com.accuvally.hdtui.activity.TicketTabActivity;
import com.accuvally.hdtui.db.AnnounceTable;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.AnnounceBean;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.TimeUtils;

public class NoticeAdapter extends PagerAdapter {

	private List<AnnounceBean> list = new ArrayList<AnnounceBean>();
	private Context mContext;

	public NoticeAdapter(Context context, String sessionId) {
		this.mContext = context;
		list = AnnounceTable.query(sessionId);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View layoutview = LayoutInflater.from(mContext).inflate(R.layout.pageritem_notice, null);
		TextView tvNoticeContent = (TextView) layoutview.findViewById(R.id.tvNoticeContent);

		final AnnounceBean bean = list.get(position);
		String relativeTime = TimeUtils.relativeTimeStr(bean.sendTimestamp);
		String content = relativeTime + ": " + bean.message;

		SpannableStringBuilder style = new SpannableStringBuilder(content);
		style.setSpan(new ForegroundColorSpan(0xffa0a0a0), 0, relativeTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvNoticeContent.setText(style);

		tvNoticeContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				skipIntent(bean);
			}
		});

		container.addView(layoutview);
		return layoutview;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	private void skipIntent(AnnounceBean info) {
		switch (info.op_type) {
		case 1:// 活动
			Intent intent1 = new Intent(mContext, AccuvallyDetailsActivity.class);
			intent1.putExtra("id", info.op_value);
			intent1.putExtra("isHuodong", 0);
			mContext.startActivity(intent1);
			break;
		case 2:// 专题
			Intent intent2 = new Intent(mContext, ProjectDetailsActivity.class);
			intent2.putExtra("title", info.title);
			intent2.putExtra("id", info.op_value);
			mContext.startActivity(intent2);
			break;
		case 3:// 圈子
			SessionInfo session = SessionTable.querySessionById(info.op_value);
			if (session == null) {
				session = new SessionInfo();
				session.setSessionId(info.op_value);
				session.setTitle(info.title);
				session.setLogoUrl(info.logourl);
				SessionTable.insertSession(session);
			}
			AccuApplication.getInstance().setCurrentSession(session);
			mContext.startActivity(new Intent(mContext, ChatActivity.class));
			break;
		case 4:// 主办方
			Intent intent4 = new Intent(mContext, ProjectDetailsActivity.class);
			intent4.putExtra("orgId", info.op_value);
			mContext.startActivity(intent4);
			break;
		case 5:// 网页
			Intent intent5 = new Intent(mContext, GeTuiWapActivity.class);
			intent5.putExtra("title", info.title);
			intent5.putExtra("url", info.op_value);
			mContext.startActivity(intent5);
			break;
		case 6:// 票券
			mContext.startActivity(new Intent(mContext, TicketTabActivity.class));
			break;
		}
	}
}
