package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.SelInfo;

public class CommonAccuAdapter extends CommonAdapter<SelInfo> {

	
	private AccuApplication application;
	private DBManager db;

	public CommonAccuAdapter(Context context) {
		super(context, R.layout.listitem_home_recommend);
		application = (AccuApplication) context.getApplicationContext();
		db = new DBManager(context);
	}

	@Override
	public void convert(ViewHolder viewHolder, final SelInfo item, int position) {
		viewHolder.setImageUrl(R.id.ivItemRecommendImg, item.getLogo());
		
		viewHolder.setText(R.id.tvItemTitle, item.getTitle());
		viewHolder.setText(R.id.tvItemTime, item.getTimeStr());
		viewHolder.setText(R.id.tvItemAddress, item.getAddress());
		viewHolder.setText(R.id.tvItemVisitNum, item.getVisitNum());
		viewHolder.setText(R.id.tvItemPriceArea, item.getPriceArea());
		
		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.insertSaveBeHavior(application.addBeHavior(10, 0+"", item.getId(), "", "","",""));
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("isHuodong", item.getSourceType());// 1 活动推 0 活动行
				mContext.startActivity(intent);
			}
		});
	}

}
