package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AttendActivity;
//import com.accuvally.hdtui.activity.FollowActivity;
//import com.accuvally.hdtui.activity.HistoryActivity;
//import com.accuvally.hdtui.activity.PersonalActivity;
//import com.accuvally.hdtui.activity.RecommendActivity;
//import com.accuvally.hdtui.adapter.InvalidTicketAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.model.UnfinishedTicket;
//import com.accuvally.hdtui.ui.CircleImageView;
//import com.accuvally.hdtui.ui.PullToRefreshView;
//import com.accuvally.hdtui.ui.PullToRefreshView.OnHeaderRefreshListener;
//import com.accuvally.hdtui.ui.ScrolListView;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//
//public class MyAccuvallyFragment extends BaseFragment implements OnClickListener {
//
//	private TextView unfinished_ticket_count;
//
//	private ScrolListView mListView;
//
//	private InvalidTicketAdapter adapter;
//
//	private LinearLayout ll_unfinished_ticket_header;
//
//	private List<UnfinishedTicket> unfinishedTicket;
//
//	private TextView tvRecommend;
//
//	private View iv_recommend_line;
//
//	private TextView tvHistory;
//
//	private TextView tvAttend, tvUpLoad, tvOrg;
//
//	private PullToRefreshView pullToRefreshView;
//
//	private TextView openValidTicket;
//
//	private View openTicketLine;
//
//	private int tag = 1;
//
//	List<UnfinishedTicket> unfinishedTicketList;
//
//	CircleImageView ivHead;
//	TextView tvUserName;
//	TextView tvUserCity;
//	TextView tvUserSex;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.activity_accuvally_new, container, false);
//		initView(rootView);
//		return rootView;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.not_lgin_head_bg).showImageForEmptyUri(R.drawable.not_lgin_head_bg).showImageOnFail(R.drawable.not_lgin_head_bg).cacheInMemory(true).cacheOnDisk(true).build();
//		application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivHead, options);
//		tvUserName.setText(application.getUserInfo().getNick());
//		if ("".equals(application.getUserInfo().getCity()))
//			tvUserCity.setText("暂无地址");
//		else
//			tvUserCity.setText(application.getUserInfo().getCity());
//		if (application.getUserInfo().getGender() == 0) {
//			tvUserSex.setText("女");
//			Drawable drawable = getResources().getDrawable(R.drawable.accu_sex_bg);
//			tvUserSex.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//		} else {
//			Drawable drawable = getResources().getDrawable(R.drawable.accu_nan_bg);
//			tvUserSex.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//			tvUserSex.setText("男");
//		}
//		initData(1);
//	}
//
//	public void initView(View view) {
//		unfinished_ticket_count = (TextView) view.findViewById(R.id.unfinished_ticket_count);
//		mListView = (ScrolListView) view.findViewById(R.id.lv_valid_ticket);
//		ll_unfinished_ticket_header = (LinearLayout) view.findViewById(R.id.ll_unfinished_ticket_header);
//		tvHistory = (TextView) view.findViewById(R.id.tvHistory);
//		tvRecommend = (TextView) view.findViewById(R.id.tvRecommend);
//		iv_recommend_line = (View) view.findViewById(R.id.iv_recommend_line);
//		tvAttend = (TextView) view.findViewById(R.id.tvAttend);
//		tvUpLoad = (TextView) view.findViewById(R.id.tvUpLoads);
//		tvUpLoad.setVisibility(View.GONE);
//		tvOrg = (TextView) view.findViewById(R.id.tvOrg);
//		pullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pullToRefreshView);
//		openValidTicket = (TextView) view.findViewById(R.id.openValidTicket);
//		openTicketLine = (View) view.findViewById(R.id.openTicketLine);
//
//		tvHistory.setOnClickListener(this);
//		tvRecommend.setOnClickListener(this);
//		tvAttend.setOnClickListener(this);
//		tvUpLoad.setOnClickListener(this);
//		tvOrg.setOnClickListener(this);
//
//		unfinishedTicket = new ArrayList<UnfinishedTicket>();
//		adapter = new InvalidTicketAdapter(mContext);
//		adapter.setList(unfinishedTicket);
//		mListView.setAdapter(adapter);
//
//		if (Config.UMENG_CHANNEL_ANZHI.equals(Utils.getChannel(mContext)) || Config.UMENG_CHANNEL_HUAWEI.equals(Utils.getChannel(mContext))) {
//			tvRecommend.setVisibility(View.GONE);
//			iv_recommend_line.setVisibility(View.GONE);
//		}
//
//		ivHead = (CircleImageView) view.findViewById(R.id.ivHead);
//		tvUserName = (TextView) view.findViewById(R.id.tvUserName);
//		tvUserCity = (TextView) view.findViewById(R.id.tvUserCity);
//		tvUserSex = (TextView) view.findViewById(R.id.tvUserSex);
//
//		ivHead.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				startActivity(new Intent(mContext, PersonalActivity.class));
//			}
//		});
//		pullToRefreshView.setPullLoadEnable(false);
//		pullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
//			public void onHeaderRefresh(PullToRefreshView view) {
//				initData(1);
//			}
//		});
//		openValidTicket.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (tag == 1) {
//					List<UnfinishedTicket> list = new ArrayList<UnfinishedTicket>();
//					for (int i = 0; i < unfinishedTicketList.size(); i++) {
//						if (i >= 2) {
//							UnfinishedTicket ticket = unfinishedTicketList.get(i);
//							list.add(ticket);
//						}
//					}
//					adapter.addAll(list);
//					tag = 2;
//					openValidTicket.setText("收缩");
//				} else {
//					for (int i = 0; i < unfinishedTicketList.size(); i++) {
//						if (i >= 2) {
//							UnfinishedTicket ticket = unfinishedTicketList.get(i);
//							adapter.remove(ticket);
//						}
//					}
//					tag = 1;
//					openValidTicket.setText("展开其他未完成订单");
//				}
//				adapter.notifyDataSetChanged();
//			}
//		});
//	}
//
//	private void initData(final int pageIndex) {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("pi", pageIndex + ""));
//		list.add(new BasicNameValuePair("ps", Integer.MAX_VALUE + ""));
//		httpCilents.get(httpCilents.printURL(Config.MYTICKETS_UNFINISHED, list), new WebServiceCallBack() {
//			public void callBack(int code, Object result) {
//				pullToRefreshView.stopRefresh();
//				mListView.setVisibility(View.VISIBLE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					unfinishedTicketList = JSON.parseArray(result.toString(), UnfinishedTicket.class);
//					if (pageIndex == 1 && unfinishedTicketList != null) {
//						adapter.removeAll();
//					}
//					if (unfinishedTicketList.size() > 2) {
//						openValidTicket.setVisibility(View.VISIBLE);
//						openTicketLine.setVisibility(View.VISIBLE);
//						openValidTicket.setText("展开其他未完成订单");
//						List<UnfinishedTicket> list = new ArrayList<UnfinishedTicket>();
//						for (int i = 0; i < unfinishedTicketList.size(); i++) {
//							if (i < 2) {
//								UnfinishedTicket ticket = unfinishedTicketList.get(i);
//								list.add(ticket);
//							}
//						}
//						adapter.addAll(list);
//					} else {
//						openValidTicket.setVisibility(View.GONE);
//						openTicketLine.setVisibility(View.GONE);
//						adapter.addAll(unfinishedTicketList);
//					}
//					if (adapter != null && adapter.getList() != null && adapter.getList().size() > 0 && adapter.getList().get(0).getTotal() > 0) {
//						ll_unfinished_ticket_header.setVisibility(View.VISIBLE);
//						String text = adapter.getList().get(0).getTotal() + "个未完成订单";
//						SpannableStringBuilder style = new SpannableStringBuilder(text);
//						style.setSpan(new ForegroundColorSpan(Color.rgb(250, 94, 95)), 0, (adapter.getList().get(0).getTotal() + "").length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//						unfinished_ticket_count.setText(style);
//					} else {
//						ll_unfinished_ticket_header.setVisibility(View.GONE);
//					}
//					if (adapter.getList() == null || adapter.getList().size() == 0) {
//						ll_unfinished_ticket_header.setVisibility(View.GONE);
//					}
//					application.cacheUtils.put("unfinishedTicketList", result.toString());
//					break;
//				case Config.RESULT_CODE_ERROR:
//					if (application.cacheUtils.getAsString("ticket") == null) {
//						ll_unfinished_ticket_header.setVisibility(View.GONE);
//					} else {
//						unfinishedTicket = JSON.parseArray(application.cacheUtils.getAsString("unfinishedTicketList"), UnfinishedTicket.class);
//						adapter.setList(unfinishedTicket);
//					}
//					break;
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.tvHistory:
//			toActivity(HistoryActivity.class);
//			break;
//		case R.id.tvRecommend:
//			if (Utils.isFastDoubleClick())
//				return;
//			if (Config.UMENG_CHANNEL_360.equals(Utils.getChannel(mContext)))
//				com.qihoo.gamead.QihooAdAgent.loadAd(mContext);
//			else
//				startActivity(new Intent(mContext, RecommendActivity.class));
//			break;
//		case R.id.tvAttend:
//			startActivity(new Intent(mContext, AttendActivity.class));
//			break;
//		case R.id.tvUpLoads:
//			break;
//		case R.id.tvOrg:
//			startActivity(new Intent(mContext, FollowActivity.class));
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
//
//}
