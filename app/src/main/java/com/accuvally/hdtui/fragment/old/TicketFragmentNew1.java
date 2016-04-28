package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.ValidTicketAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.model.ExistTicket;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.ui.XListView.IXListViewListener;
//import com.accuvally.hdtui.utils.FileCache;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.IHomeCallListener;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.alibaba.fastjson.JSON;
//
//public class TicketFragmentNew1 extends BaseFragment implements IHomeCallListener, IXListViewListener {
//	private String actid;
//	FileCache fileCache;
//	private IntentFilter filter;
//	private ChangeReceiver receiver;
//	private XListView mListView;
//	private TextView no_ticket;
//	private List<ExistTicket> existTicket;
//	private ValidTicketAdapter adapter;
//	private IHomeCallListener iHomeCallListener;
//	private LinearLayout lyLoading;
//	private int pageIndex = 1;
//
//	public String getActid() {
//
//		return actid;
//	}
//
//	public void setActid(String actid) {
//		this.actid = actid;
//	}
//
//	public void onAttach(Activity activity) {
//		try {
//
//			iHomeCallListener = (IHomeCallListener) activity;
//		} catch (Exception e) {
//			throw new ClassCastException(activity.toString() + "must implement mbtnListener");
//		}
//		super.onAttach(activity);
//	}
//
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_valid_ticket, container, false);
//		Bundle bundle = new Bundle();
//		bundle.putString("actionId", getActid());
//		initInstance();
//		setViewFailure(rootView);
//		initView(rootView);
//		return rootView;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//
//	}
//
//	private void initView(View view) {
//		mListView = (XListView) view.findViewById(R.id.lv_valid_ticket);
//		no_ticket = (TextView) view.findViewById(R.id.no_ticket);
//		mListView.setXListViewListener(this);
//		lyLoading = (LinearLayout) view.findViewById(R.id.lyLoading);
//
//		existTicket = new ArrayList<ExistTicket>();
//
//		adapter = new ValidTicketAdapter(mContext);
//		adapter.setList(existTicket);
//		mListView.setAdapter(adapter);
//		initData(pageIndex);
//
//		showFailureOnClick(new OnClickCallBack() {
//			public void callBack(boolean is) {
//				if (is) {
//					iHomeCallListener.transfermsg();
//				} else {
//					initData(1);
//				}
//			}
//		});
//	}
//
//	private void initInstance() {
//		filter = new IntentFilter(Config.NOTTICKET_CONTENT);
//		filter.addAction(Config.ACCU_APPLY_SUCCEESS);
//		receiver = new ChangeReceiver();
//		fileCache = new FileCache(mContext);
//	}
//
//	public void onPause() {
//		super.onPause();
//		try {
//
//			getActivity().registerReceiver(receiver, filter);
//
//		} catch (Exception e) {
//		}
//	}
//
//	public void onStart() {
//		super.onStart();
//		if (receiver.isOrderedBroadcast()) {
//			getActivity().unregisterReceiver(receiver);
//
//		}
//	}
//
//	@Override
//	public void transfermsg() {
//
//	}
//
//	private long lastTime = System.currentTimeMillis();
//	private int firstTime = 0;
//
//	private void initData(final int pageIndex) {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("pi", String.valueOf(pageIndex)));
//		list.add(new BasicNameValuePair("ps", "5"));
//		if (actid != null)
//			list.add(new BasicNameValuePair("eid", actid));
//
//		httpCilents.get(httpCilents.printURL(Config.MYTICKETS_MINE, list), new WebServiceCallBack() {
//
//			public void callBack(int code, Object result) {
//				goneFailure();
//				lyLoading.setVisibility(View.GONE);
//				mListView.setVisibility(View.VISIBLE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<ExistTicket> existTicketList = JSON.parseArray(result.toString(), ExistTicket.class);
//					if (pageIndex == 1 && existTicketList.size() == 0) {
//						mListView.setVisibility(View.GONE);
//					}
//					if (pageIndex == 1 && existTicketList != null) {
//						adapter.removeAll();
//					}
//					if (existTicketList.size() == 0 && ((System.currentTimeMillis() - lastTime) > 5000 || firstTime == 0)) {
//
//						// application.showMsgShort("没有更多票券");
//
//						firstTime++;
//					}
//					lastTime = System.currentTimeMillis();
//					mListView.stopRefresh();
//					mListView.stopLoadMore();
//					adapter.addAll(existTicketList);
//					application.cacheUtils.put("existTicketList", result.toString());
//					if (adapter.getList() == null || adapter.getList().size() == 0) {
//						// getNoLoginData(1);
//						// no_ticket.setVisibility(View.VISIBLE);
//						// no_ticket.setText("还木有票券哦！");
//
//						no_ticket.setVisibility(View.VISIBLE);
//						showFailure();
//						tvNoData.setText("现在还木有票券");
//						SquareBtn.setVisibility(View.GONE);
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					if (application.cacheUtils.getAsString("ticket") == null) {
//						// application.showMsg(result.toString());
//						showWifi();
//					} else {
//						existTicket = JSON.parseArray(application.cacheUtils.getAsString("existTicketList"), ExistTicket.class);
//						adapter.setList(existTicket);
//					}
//					break;
//				}
//			}
//		});
//	}
//
//	class ChangeReceiver extends BroadcastReceiver {
//
//		public void onReceive(Context context, Intent intent) {
//			final String action = intent.getAction();
//
//			pageIndex = 1;
//			initData(pageIndex);
//			if (action.equals(Config.NOTTICKET_CONTENT)) {
//				TicketFragmentNew1 ticketFragment = (TicketFragmentNew1) getFragmentManager().findFragmentByTag("TicketFragment");
//				NoLginTicketVolume noLginTicketVolume = (NoLginTicketVolume) getFragmentManager().findFragmentByTag("NoLginTicketVolume");
//				if (noLginTicketVolume.isVisible()) {
//					FragmentTransaction transaction = getFragmentManager().beginTransaction();
//					ticketFragment.onResume();
//					transaction.hide(noLginTicketVolume).show(ticketFragment).commitAllowingStateLoss();
//				}
//			}
//		}
//	}
//
//	@Override
//	public void onRefresh() {
//		if (!NetworkUtils.isNetworkAvailable(mContext)) {
//			application.showMsg("啊哦，网络好像断开了！");
//			mListView.stopRefresh();
//			return;
//		}
//		pageIndex = 1;
//		initData(pageIndex);
//	}
//
//	@Override
//	public void onLoadMore() {
//		if (!NetworkUtils.isNetworkAvailable(mContext)) {
//			application.showMsg("啊哦，网络好像断开了！");
//			mListView.stopLoadMore();
//			return;
//		}
//		pageIndex++;
//		initData(pageIndex);
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		try {
//			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//			childFragmentManager.setAccessible(true);
//			childFragmentManager.set(this, null);
//		} catch (NoSuchFieldException e) {
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
