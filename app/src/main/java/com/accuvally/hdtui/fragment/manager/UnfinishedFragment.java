package com.accuvally.hdtui.fragment.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.activity.mine.TicketTabActivity;
import com.accuvally.hdtui.adapter.InvalidTicketAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UnfinishedTicket;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUnFinishedEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/*
 * 未完成票券
 */
public class UnfinishedFragment extends BaseFragment implements IXListViewListener, OnClickListener {

	private List<UnfinishedTicket> list;
	private int pageIndex = 1, pageSize = 20;
	private XListView mListView;
	private InvalidTicketAdapter mAdapter;
	private View root;
	private View emptyView;
	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ChangeUserStateEventBus stateEventBus) {
		showStateView();
		if (stateEventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
			pageIndex = 1;
			if (list != null) {
				list.clear();
				mAdapter.notifyDataSetChanged();
			}
			requestData();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_unfinished, container, false);
		EventBus.getDefault().register(this);
		initView();
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, LoginActivityNew.class);
				startActivity(intent);
			}
		};
		root.findViewById(R.id.btLogin).setOnClickListener(onClickListener);
		emptyView = root.findViewById(R.id.manageEmpty);
		lyFailure = (LinearLayout) root.findViewById(R.id.lyFailure);
		tvNoData = (TextView) root.findViewById(R.id.tvNoData);
		ivFailure = (ImageView) root.findViewById(R.id.ivFailure);
		SquareBtn = (Button) root.findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("当前日期没有未完成的票券噢 ");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);
		lyFailure.setVisibility(View.VISIBLE);
		mListView.setEmptyView(lyFailure);

		SquareBtn.setOnClickListener(this);
		showStateView();
		requestData();

		return root;
	}

	private void showStateView() {
		if (application.checkIsLogin()) {
			emptyView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		} else {
			emptyView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	public void initView() {
		mListView = (XListView) root.findViewById(R.id.listview);

		mAdapter = new InvalidTicketAdapter(mContext);
		list = new ArrayList<UnfinishedTicket>();
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
	}

	public void onEventMainThread(ChangeUnFinishedEventBus eventBus) {
		requestData();
	}

	public void onEventMainThread(EventHideFooterView event) {
		if (event.clazz == this.getClass()) {
			ManagerFragment parentF = (ManagerFragment) getParentFragment();
			if (parentF != null) {
				((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = parentF.getListViewBottomMargin();
			}
		}
	}

	// 点击日期做查询
	private CustomDate mCustomDate;// 查询的时间

	public void onEventMainThread(EventCustomDate bus) {
		if(bus.clazz==getClass())
			requestData();
	}

	private void requestData() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("pi", pageIndex + ""));
		list.add(new BasicNameValuePair("ps", pageSize + ""));
		TicketTabActivity parentF = (TicketTabActivity) getActivity();
		if (parentF != null) {
			((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = parentF.getListViewBottomMargin();
		}
		if (parentF.getCustomDate() != null) {
			mCustomDate = parentF.getCustomDate();
			list.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
			list.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
		}
		httpCilents.get(httpCilents.printURL(Url.MYTICKETS_UNFINISHED, list), new WebServiceCallBack() {
			public void callBack(int code, Object result) {
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<UnfinishedTicket> list = JSON.parseArray(response.getResult(), UnfinishedTicket.class);
						if (pageIndex == 1 && list != null) {
							mAdapter.removeAll();
						}
						mAdapter.addAll(list);
						ManagerFragmentHelp.putAll(UnfinishedFragment.class, list);
					} else {
						application.showMsg(response.getMsg());
					}
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
		requestData();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		requestData();
	}

	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			getActivity().finish();
			break;
		}
	}
}
