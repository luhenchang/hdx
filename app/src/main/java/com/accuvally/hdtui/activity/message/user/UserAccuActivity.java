package com.accuvally.hdtui.activity.message.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.AccuBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
//关注的活动  发布的活动
public class UserAccuActivity extends BaseActivity {

	private int pageIndex = 1, pageSize = 100;
	private boolean isFollows;
	private ListView mListView;
	private CommonAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_accu);
		
		initProgress();

		isFollows = getIntent().getBooleanExtra("isFollows", false);
		String title = getIntent().getStringExtra("title");
		setTitle(title);

		initListView();
		
		String account = getIntent().getStringExtra("account");
		requestUserAccu(account);
	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new CommonAdapter<AccuBean>(this, R.layout.listitem_home_recommend) {

			@Override
			public void convert(ViewHolder viewHolder, final AccuBean item, int position) {
				viewHolder.setImageUrl(R.id.ivItemRecommendImg, item.logo);

				viewHolder.setText(R.id.tvItemTitle, item.title);
				viewHolder.setText(R.id.tvItemTime, item.getTimeStr());
				viewHolder.setText(R.id.tvItemAddress, item.address);
				viewHolder.setText(R.id.tvItemVisitNum, item.visitnum);
				viewHolder.setText(R.id.tvItemPriceArea, item.pricearea);

				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dbManager.insertSaveBeHavior(application.addBeHavior(10, 0 + "", item.id, "", "", "", ""));
						Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
						intent.putExtra("id", item.id);
						intent.putExtra("isHuodong", item.sourcetype);
						mContext.startActivity(intent);
					}
				});
			}
		};

		mListView.setAdapter(mAdapter);
	}

	private void requestUserAccu(String account) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));

		String url = isFollows ? Url.user_follow_acts : Url.user_acts;
		
		startProgress();
		httpCilents.get(httpCilents.printURL(url, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}
				
				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						List<AccuBean> list = JSON.parseArray(response.result, AccuBean.class);
						mAdapter.addAll(list);
					} else {
						ToastUtil.showMsg(response.msg);
					}
				}
			}
		});
	}
}
