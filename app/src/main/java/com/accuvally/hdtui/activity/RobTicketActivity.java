package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.RobTicketBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

public class RobTicketActivity extends BaseActivity {

	private int pageIndex = 1, pageSize = 10;
	private XListView mListView;
	private CommonAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_robticket);
		setTitle("限时抢票");
		initProgress();
		setListView();
		getListData();
	}

	private void setListView() {
		mListView = (XListView) findViewById(R.id.lvRobTicket);
		mListView.setAdapter(mAdapter = new CommonAdapter<RobTicketBean>(this, R.layout.listitem_robticket) {

			@Override
			public void convert(ViewHolder viewHolder, final RobTicketBean item, int position) {
				viewHolder.setImageUrl(R.id.ivRobImage, item.getLogo(), UILoptions.rectangleOptions);
				viewHolder.setText(R.id.tvRobTitle, item.getTitle());
				viewHolder.setText(R.id.tvRobAddress, item.getAddress());
				viewHolder.setText(R.id.tvRobTime, item.getTimeStr());
				viewHolder.setText(R.id.tvRobPrice, item.getPrice());
				viewHolder.setText(R.id.tvPrimeCost, item.getOriginPrice());

				TextView tvPrimeCost = viewHolder.getView(R.id.tvPrimeCost);
				TextPaint paint = tvPrimeCost.getPaint();
				paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
				
				TextView tvRobState = viewHolder.getView(R.id.tvRobState);
				tvRobState.setText(item.getStatusStr());
				if("正在热抢".equals(item.getStatusStr())) {
					tvRobState.setBackgroundResource(R.drawable.rush_img_green);
					tvRobState.setTextColor(getResources().getColor(R.color.white));
				}else {
					tvRobState.setBackgroundResource(R.drawable.rush_img_gray);
					tvRobState.setTextColor(getResources().getColor(R.color.txt_rush));
				}
				
				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dbManager.insertSaveBeHavior(application.addBeHavior(100, "0", item.getId(), "", "", "APP_RUSH", item.getId()));
						Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
						intent.putExtra("id", item.getId());
						intent.putExtra("StatusStr", item.getStatusStr());
						intent.putExtra("isHuodong", 0);// 1 活动推 0 活动行
						intent.putExtra("isRobTicket", true);// 是否抢票活动
						startActivity(intent);
					}
				});
			}
		});
		
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getListData();
			}

			@Override
			public void onLoadMore() {
				pageIndex++;
				getListData();
			}
		});

		useCacheData();
	}

	private void useCacheData() {
		String cache = application.cacheUtils.getAsString("robTicket");
		if (!TextUtils.isEmpty(cache)) {
			List<RobTicketBean> list = JSON.parseArray(cache, RobTicketBean.class);
			mAdapter.setList(list);
		}
	}

	public void getListData() {
		String cityName = application.sharedUtils.readString("cityName");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("city", cityName));

		startProgress();
		httpCilents.get(httpCilents.printURL(Url.GET_RUSH_EVENTS, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();

				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msgInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msgInfo.isSuccess()) {
						application.cacheUtils.put("robTicket", msgInfo.getResult());
						List<RobTicketBean> list = JSON.parseArray(msgInfo.getResult(), RobTicketBean.class);
						if (pageIndex == 1) {
							mAdapter.setList(list);
						} else {
							mAdapter.addAll(list);
						}
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
}
