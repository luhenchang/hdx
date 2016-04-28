package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.InvalidTicketAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.UnfinishedTicket;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeUnFinishedEventBus;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

/**
 * 未完成票券
 * 
 * @author Semmer Wang
 * 
 */
public class UnfinishedActivity extends BaseActivity implements IXListViewListener {

	private List<UnfinishedTicket> list;

	int pageIndex = 1, pageSize = 20;

	private XListView mListView;

	private InvalidTicketAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_tag);
		EventBus.getDefault().register(this);
		initView();
		initProgress();
		initData();
	}

	public void initView() {
		mListView = (XListView) findViewById(R.id.listview);

		mAdapter = new InvalidTicketAdapter(mContext);
		list = new ArrayList<UnfinishedTicket>();
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
	}
	
	public void onEventMainThread(ChangeUnFinishedEventBus eventBus){
		initData();
	}

	private void initData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("pi", pageIndex + ""));
		list.add(new BasicNameValuePair("ps", pageSize + ""));
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.MYTICKETS_UNFINISHED, list), new WebServiceCallBack() {
			public void callBack(int code, Object result) {
				stopProgress();
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					List<UnfinishedTicket> list = JSON.parseArray(result.toString(), UnfinishedTicket.class);
					if (pageIndex == 1 && list != null) {
						mAdapter.removeAll();
					}
					mAdapter.addAll(list);
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		initData();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		initData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
