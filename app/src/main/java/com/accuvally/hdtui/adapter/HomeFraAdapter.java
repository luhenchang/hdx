package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.LoginActivityNew;
import com.accuvally.hdtui.activity.SponsorDetailActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.PoporgsBean;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.MyProgressDialog;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.eventbus.ChangeAttentionState;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HomeFraAdapter extends BaseAdapter {

	private final int TYPE_RECOMMEND = 0;
	private final int TYPE_TITLE = 1;
	private final int TYPE_SPONSPOR = 2;

	protected List<SelInfo> mList1 = new ArrayList();
	protected List<PoporgsBean> mList2 = new ArrayList();
	private int size1;
	private int size2;

	protected AccuApplication application;
	protected DBManager dbManager;
	private Context mContext;
	private LayoutInflater mInflater;
	private HttpCilents httpCilents;
	private MyProgressDialog myProgressDialog;

	public HomeFraAdapter(Context context) {
		mContext = context;
		application = (AccuApplication) context.getApplicationContext();
		dbManager = new DBManager(context);
		
		mInflater = LayoutInflater.from(context);
		httpCilents = new HttpCilents(context);
		
		myProgressDialog = new MyProgressDialog(mContext);
		myProgressDialog.setMyCancelable(true);
		myProgressDialog.setMyTouchOutside(false);
	}
	
	public void setLists(List<SelInfo> list1, List<PoporgsBean> list2) {
		mList1 = list1;
		mList2 = list2;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		size1 = mList1 == null ? 0 : mList1.size();
		size2 = mList2 == null ? 0 : mList2.size();
		if (size2 == 0) {
			return size1;
		}
		return size1 + 1 + size2;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mList1.size()) {
			return TYPE_RECOMMEND;
		} else if (position == mList1.size()) {
			return TYPE_TITLE;
		} else {
			return TYPE_SPONSPOR;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		switch (getItemViewType(position)) {
		case TYPE_RECOMMEND:
			viewHolder = ViewHolder.get(mContext, convertView, parent,
                    R.layout.listitem_home_recommend, position);

			final SelInfo item = mList1.get(position);
			viewHolder.setImageUrl(R.id.ivItemRecommendImg, item.getLogo());

			viewHolder.setText(R.id.tvItemTitle, item.getTitle());
			viewHolder.setText(R.id.tvItemAddress, item.getAddress());
			viewHolder.setText(R.id.tvItemTime, item.getTimeStr());
			viewHolder.setText(R.id.tvItemVisitNum, item.getVisitNum());

			TextView tvItemPriceArea = viewHolder.getView(R.id.tvItemPriceArea);
			tvItemPriceArea.setText(item.getPriceArea());

			viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dbManager.insertSaveBeHavior(application.addBeHavior(100, "0", item.getId(), "", "", "APP_RECOMMEND", item.getId()));
					Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
					intent.putExtra("id", item.getId());
					intent.putExtra("isHuodong", item.getSourceType());
					mContext.startActivity(intent);
				}
			});
			break;
		case TYPE_TITLE:
			viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.home_layout_title, position);
			break;
		case TYPE_SPONSPOR:
			viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.listitem_home_sponsor, position);
			
			final PoporgsBean poporgs = mList2.get(position - size1 - 1);
			viewHolder.setImageUrl(R.id.ivSponsorLogo, poporgs.logo);
			
			viewHolder.setText(R.id.tvSponsorName, poporgs.title);
			viewHolder.setText(R.id.tvSponsorBrief, poporgs.desc);
			viewHolder.setText(R.id.tvFollowNum, poporgs.follows + "人关注");
			
//			final ImageView ivAddFollow = viewHolder.getView(R.id.ivAddFollow);
//			ivAddFollow.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					attentionSponsor(poporgs.id, ivAddFollow);
//				}
//			});
			
			viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, SponsorDetailActivity.class);
					intent.putExtra("orgId", poporgs.id);
					mContext.startActivity(intent);
				}
			});
			break;
		}
		return viewHolder.getConvertView();
	}
	
	private void attentionSponsor(final String id, final ImageView ivAddFollow) {
		if (!application.checkIsLogin()) {// 如果未登陆就进入登陆页面
			mContext.startActivity(new Intent(mContext, LoginActivityNew.class));
			return;
		}

		showProgress("正在关注主办方");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		String url = Url.ORG_FOLLOW;
		httpCilents.postA(url, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					EventBus.getDefault().post(new ChangeAttentionState());
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						ivAddFollow.setImageResource(R.drawable.has_attention);
						
						MobclickAgent.onEvent(mContext, "follow_organizer_count");
						dbManager.insertSaveBeHavior(application.addBeHavior(20, 1 + "", id, "", "", "", ""));
					}
				}
			}
		});
	}
	
	public void showProgress(String message) {
		myProgressDialog.setMyMessage(message);
		myProgressDialog.myShow();
	}
	
	public void dismissProgress() {
		if (myProgressDialog != null) {
			myProgressDialog.mydismiss();
		}
	}

}
