package com.accuvally.hdtui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.SponsorDetailActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SponsorBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ViewHolder;
import com.accuvally.hdtui.utils.eventbus.ChangeAttentionState;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 关注的主办方列表
 * 
 * @author Semmer Wang
 * 
 */
public class OrgListActivity extends BaseActivity implements OnClickListener {

	private XListView mListView;
	private List<SponsorBean> listData = new ArrayList<SponsorBean>();
	private int pageIndex = 1, pageSize = 10;
	private MyAdapter mAdapter;
	private LinearLayout lyFailure;
	private TextView tvNoData;
	private ImageView ivFailure;
	private Button SquareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_registrtion);
		EventBus.getDefault().register(this);
		initProgress();
		initView();
	}

	public void initView() {
		setTitle("关注的主办方");
		mListView = (XListView) findViewById(R.id.lvRegistration);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("您还没有关注任何主办方哦");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);
		
		SquareBtn.setOnClickListener(this);

		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
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

		// 缓存
		String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_spnosor");
		if (pageIndex == 1 && !TextUtils.isEmpty(cache)) {
			List<SponsorBean> listCache = JSON.parseArray(cache, SponsorBean.class);
			if (listCache != null) {
				listData.addAll(listCache);
				mAdapter.notifyDataSetChanged();
			}
		}
		getListData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ChangeUserStateEventBus stateEventBus) {
		if (stateEventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
			pageIndex = 1;
			if (listData != null) {
				listData.clear();
				mAdapter.notifyDataSetChanged();
			}
			getListData();
		}
	}

	public void onEventMainThread(ChangeAttentionState IsAttention) {
		Log.i("info", "-------------->>>>>>>>>>>>>>>>>>>>");
		pageIndex = 1;
		getListData();
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYFOLLOW, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()) {
						List<SponsorBean> list = JSON.parseArray(response.getResult(), SponsorBean.class);
						if (list == null){
							if(pageIndex==1){
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
							return;
						}
						if (pageIndex == 1) {
							listData.clear();
							if(list.size()==0){
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
						}
						listData.addAll(list);
						mAdapter.notifyDataSetChanged();
						// 缓存
						if (pageIndex == 1) {
							application.cacheUtils.put(application.getUserInfo().getAccount() + "_spnosor", response.result);
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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (listData == null) {
				return 0;
			}
			return listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = View.inflate(mContext, R.layout.listitem_registration, null);
			}
			SponsorBean bean = listData.get(position);
			ImageView ivOrganizer = ViewHolder.get(convertView, R.id.ivOrganizer);
			application.mImageLoader.displayImage(bean.getLogo(), ivOrganizer, UILoptions.rectangleOptions);
			TextView tvTitle = ViewHolder.get(convertView, R.id.tvTitle);
			TextView tvLikeNum = ViewHolder.get(convertView, R.id.tvLikeNum);
			TextView tvDescride = ViewHolder.get(convertView, R.id.tvDescride);

			tvTitle.setText(bean.getName());
			if ("".equals(bean.getDesc()))
				tvDescride.setText("暂无简介");
			else
				tvDescride.setText(bean.getDesc());
			tvLikeNum.setText(bean.getFollows() + "");
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SponsorBean bean = listData.get(position);
					Intent intent = new Intent(mContext, SponsorDetailActivity.class);
					intent.putExtra("orgId", bean.getId());
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			finish();
			break;
		}
	}

}
