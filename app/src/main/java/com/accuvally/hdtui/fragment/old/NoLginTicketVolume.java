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
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//
//import com.accuvally.hdtui.activity.LoginActivityNew;
//import com.accuvally.hdtui.activity.PersonalActivity;
//import com.accuvally.hdtui.adapter.NoLginTicketAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.model.NotTicket;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.IHomeCallListener;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//
///**
// * 未登录票劵
// * 
// * @author Vanness
// * 
// */
//public class NoLginTicketVolume extends BaseFragment implements OnClickListener, IHomeCallListener {
//	private XListView noLginLv;
//	private NoLginTicketAdapter noLginTicketAdapter;
//
//	private LinearLayout lyLoading;
//	private TextView top_ll;
//	private int page = 1;
//	private IHomeCallListener iHomeCallListener;
//	// private CircleImageView ivSettingHead;
//	private RelativeLayout rlPersonTicke;
//
//	public void onAttach(Activity activity) {
//		try {
//			iHomeCallListener = (IHomeCallListener) activity;
//		} catch (Exception e) {
//			throw new ClassCastException(activity.toString() + "must implement mbtnListener");
//		}
//		super.onAttach(activity);
//	}
//
//	@Override
//	public void transfermsg() {
//	}
//
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_not_login_ticket, container, false);
//		initView(rootView);
//		getData(page);
//		return rootView;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//	}
//
//	public void onStart() {
//		super.onStart();
//	}
//
//	public void initView(View rootView) {
//		setViewFailure(rootView);
//		lyLoading = (LinearLayout) rootView.findViewById(R.id.lyLoading);
//
//		noLginLv = (XListView) rootView.findViewById(R.id.not_login_ticket_listview);
//		View noLogin = LayoutInflater.from(getActivity()).inflate(R.layout.ticket_no_login_header, null);
//		rlPersonTicke = (RelativeLayout) noLogin.findViewById(R.id.rlPersonTicke);
//		top_ll = (TextView) noLogin.findViewById(R.id.top_ll);
//
//		noLginLv.addHeaderView(noLogin);
//		noLginLv.setPullLoadEnable(false);
//		noLginLv.setPullRefreshEnable(false);
//		noLginLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//			}
//		});
//
//		rlPersonTicke.setOnClickListener(this);
//		showFailureOnClick(new OnClickCallBack() {
//			public void callBack(boolean is) {
//				if (is) {
//					iHomeCallListener.transfermsg();
//				} else {
//					goneFailure();
//					lyLoading.setVisibility(View.VISIBLE);
//					page = 1;
//					getData(page);
//				}
//			}
//		});
//		noLginTicketAdapter = new NoLginTicketAdapter(mContext);
//	}
//
//	private void getData(final int page) {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("pi", String.valueOf(page)));
//		list.add(new BasicNameValuePair("ps", "5"));
//		String tag = "";
//		if ("".equals(tag)) {
//			StringBuilder builder2 = new StringBuilder();
//			List<String> list2 = dbManager.getClassfy();
//			if (list2.size() != 0) {
//				for (int i = 0; i < list2.size(); i++) {
//					builder2.append(list2.get(i) + ",");
//				}
//				tag = builder2.toString().replace("/", ",");
//			} else {
//				tag = "";
//			}
//		}
//		list.add(new BasicNameValuePair("tag", tag));
//		String city = application.sharedUtils.readString("cityName");
//		if (city == null) {
//			list.add(new BasicNameValuePair("city", ""));
//		} else {
//			list.add(new BasicNameValuePair("city", city));
//		}
//		httpCilents.get(httpCilents.printURL(Config.TICKETACTS, list), new WebServiceCallBack() {
//			public void callBack(int code, Object result) {
//				goneFailure();
//				lyLoading.setVisibility(8);
//				noLginLv.setVisibility(0);
//				if (code == 1) {
//					top_ll.setVisibility(0);
//					List<NotTicket> hes = JSON.parseArray(result.toString(), NotTicket.class);
//					if (page == 1 && hes.size() == 0) {
//						noLginLv.setVisibility(View.GONE);
//						top_ll.setVisibility(8);
//						showFailure();
//					}
//					if (page == 1 && noLginTicketAdapter.getList() != null) {
//						noLginTicketAdapter.removeAll();
//					}
//					if (noLginTicketAdapter.getList() != null) {
//						noLginTicketAdapter.addAll(hes);
//					} else {
//						noLginTicketAdapter.setList(hes);
//						noLginLv.setAdapter(noLginTicketAdapter);
//					}
//					application.cacheUtils.put("noticket", result.toString());
//				} else {
//					if (application.cacheUtils.getAsString("noticket") == null) {
//						application.showMsg(result.toString());
//						noLginLv.setVisibility(View.GONE);
//						top_ll.setVisibility(View.GONE);
//						showWifi();
//					} else {
//						top_ll.setVisibility(0);
//						if (page == 1) {
//							List<NotTicket> hes = JSON.parseArray(application.cacheUtils.getAsString("noticket"), NotTicket.class);
//							if (page == 1 && hes.size() == 0) {
//								noLginLv.setVisibility(View.GONE);
//								top_ll.setVisibility(8);
//								showFailure();
//							}
//							if (page == 1 && noLginTicketAdapter.getList() != null) {
//								noLginTicketAdapter.removeAll();
//							}
//							if (noLginTicketAdapter.getList() != null) {
//								noLginTicketAdapter.addAll(hes);
//							} else {
//								noLginTicketAdapter.setList(hes);
//								noLginLv.setAdapter(noLginTicketAdapter);
//							}
//						}
//					}
//				}
//			}
//		});
//	}
//
//	public void onClick(View arg0) {
//
//		switch (arg0.getId()) {
//		case R.id.rlPersonTicke:
//			if (Utils.isFastDoubleClick())
//				return;
//			if (application.checkIsLogin()) {
//				startActivity(new Intent(mContext, PersonalActivity.class));
//			} else {
//				startActivity(new Intent(mContext, LoginActivityNew.class).putExtra("tag", Config.NOTTICKET_CONTENT));
//			}
//			break;
//		}
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
