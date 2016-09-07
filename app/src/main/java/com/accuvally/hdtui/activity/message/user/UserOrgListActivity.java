package com.accuvally.hdtui.activity.message.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.SponsorDetailActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.OrgBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
//关注的主办方
public class UserOrgListActivity extends BaseActivity {

	private int pageIndex = 1, pageSize = 10;
	private XListView mListView;
	private CommonAdapter<OrgBean> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_orglist);

		String title = getIntent().getStringExtra("title");
		setTitle(title);

		initProgress();
		initListView();
		getListData();
	}

	private void initListView() {
		mListView = (XListView) findViewById(R.id.listview);
		mAdapter = new CommonAdapter<OrgBean>(this, R.layout.listitem_registration) {

			@Override
			public void convert(ViewHolder viewHolder, final OrgBean item, int position) {
				viewHolder.setImageUrl(R.id.ivOrganizer, item.logo);

				viewHolder.setText(R.id.tvTitle, item.name);
				viewHolder.setText(R.id.tvLikeNum, item.follows);
				viewHolder.setText(R.id.tvDescride, item.desc);

				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, SponsorDetailActivity.class);
						intent.putExtra("orgId", item.id);
						startActivity(intent);
					}
				});
			}
		};
		
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
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", getIntent().getStringExtra("account")));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYFOLLOW, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<OrgBean> list = JSON.parseArray(response.result, OrgBean.class);
						if (pageIndex == 1) {
							mAdapter.clear();
						}
						mAdapter.addAll(list);
					}
				}
			}
		});
	}
}
