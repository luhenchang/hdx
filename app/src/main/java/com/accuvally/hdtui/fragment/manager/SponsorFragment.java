package com.accuvally.hdtui.fragment.manager;//package com.accuvally.hdtui.fragment.manager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.LoginActivityNew;
//import com.accuvally.hdtui.activity.SponsorDetailActivity;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.fragment.ManagerFragment;
//import com.accuvally.hdtui.model.SponsorBean;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.ui.XListView.IXListViewListener;
//import com.accuvally.hdtui.ui.calender.DateUtil;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.ViewHolder;
//import com.accuvally.hdtui.utils.eventbus.ChangeAttentionState;
//import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
//import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
//import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;
//import com.alibaba.fastjson.JSON;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
//import de.greenrobot.event.EventBus;
//
///*
// * 关注主办方
// */
//public class SponsorFragment extends BaseFragment {
//
//	private XListView mListView;
//	private List<SponsorBean> listData = new ArrayList<SponsorBean>();
//	private int pageIndex = 1, pageSize = 10;
//	private MyAdapter mAdapter;
//
//	private DisplayImageOptions options;
//	private View emptyView;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		EventBus.getDefault().register(this);
//
//		options = new DisplayImageOptions.Builder()
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		.showImageOnLoading(R.drawable.default_rectangle_image)
//		.showImageForEmptyUri(R.drawable.default_rectangle_image)
//		.showImageOnFail(R.drawable.default_rectangle_image)
////		.displayer(new FadeInBitmapDisplayer(500))
//		.displayer(new RoundedBitmapDisplayer(15))
//		.cacheInMemory(true)
//		.cacheOnDisk(true)
//		.build();
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		EventBus.getDefault().unregister(this);
//	}
//
//	public void onEventMainThread(ChangeUserStateEventBus stateEventBus) {
//		showStateView();
//		if (stateEventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
//			pageIndex = 1;
//			if (listData != null) {
//				listData.clear();
//				mAdapter.notifyDataSetChanged();
//			}
//			getListData();
//		}
//	}
//	
//	public void onEventMainThread(EventHideFooterView event) {
//		//if (event.clazz == this.getClass()) {
//			ManagerFragment parentF = (ManagerFragment) getParentFragment();
//			if(parentF!=null){
//				((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin=parentF.getListViewBottomMargin();
//		//	}
//		}
//	}
//
//	public void onEventMainThread(ChangeAttentionState IsAttention) {
//		pageIndex = 1;
//		getListData();
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_registrtion, container, false);
//		emptyView = view.findViewById(R.id.manageEmpty);
//
//		OnClickListener onClickListener = new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(mContext, LoginActivityNew.class);
//				startActivity(intent);
//			}
//		};
//		view.findViewById(R.id.btLogin).setOnClickListener(onClickListener);
//
//		mListView = (XListView) view.findViewById(R.id.lvRegistration);
//		mAdapter = new MyAdapter();
//		mListView.setAdapter(mAdapter);
//		mListView.setXListViewListener(new IXListViewListener() {
//
//			@Override
//			public void onRefresh() {
//				pageIndex = 1;
//				getListData();
//			}
//
//			@Override
//			public void onLoadMore() {
//				pageIndex++;
//				getListData();
//			}
//		});
//		
//		// 缓存
//		String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_spnosor");
//		if (pageIndex == 1 && !TextUtils.isEmpty(cache)) {
//			List<SponsorBean> listCache = JSON.parseArray(cache, SponsorBean.class);
//			if (listCache != null) {
//				listData.addAll(listCache);
//				mAdapter.notifyDataSetChanged();
//			}
//		}
//
//		showStateView();
//		getListData();
//		return view;
//	}
//
//	private void showStateView() {
//		if (application.checkIsLogin()) {
//			emptyView.setVisibility(View.GONE);
//			mListView.setVisibility(View.VISIBLE);
//		} else {
//			emptyView.setVisibility(View.VISIBLE);
//			mListView.setVisibility(View.GONE);
//		}
//	}
//
//	public void getListData() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("pi", pageIndex + ""));
//		params.add(new BasicNameValuePair("ps", pageSize + ""));
//		
//		ManagerFragment parentF = (ManagerFragment) getParentFragment();
//		if(parentF!=null){
//			((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin=parentF.getListViewBottomMargin();
//		}
//		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYFOLLOW, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
//				mListView.stopRefresh();
//				mListView.stopLoadMore();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<SponsorBean> list = JSON.parseArray(result.toString(), SponsorBean.class);
//					if (list == null) return;
//					if (pageIndex == 1) {
//						listData.clear();
//					}
//					listData.addAll(list);
//					mAdapter.notifyDataSetChanged();
//					// 缓存
//					if (pageIndex == 1 && list != null && list.size() != 0) {
//						application.cacheUtils.put(application.getUserInfo().getAccount() + "_spnosor", result.toString());
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//
//			}
//
//		});
//	}
//
//	class MyAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			if (listData == null) {
//				return 0;
//			}
//			return listData.size();
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) {
//			if (null == convertView) {
//				convertView = View.inflate(getActivity(), R.layout.listitem_registration, null);
//			}
//			SponsorBean bean = listData.get(position);
//			ImageView ivOrganizer = ViewHolder.get(convertView, R.id.ivOrganizer);
//			application.mImageLoader.displayImage(bean.getLogo(), ivOrganizer, options);
//			TextView tvTitle = ViewHolder.get(convertView, R.id.tvTitle);
//			TextView tvLikeNum = ViewHolder.get(convertView, R.id.tvLikeNum);
//			TextView tvDescride = ViewHolder.get(convertView, R.id.tvDescride);
//
//			tvTitle.setText(bean.getName());
//			if ("".equals(bean.getDesc()))
//				tvDescride.setText("暂无简介");
//			else
//				tvDescride.setText(bean.getDesc());
//			tvLikeNum.setText(bean.getFollows() + "");
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					SponsorBean bean = listData.get(position);
//					Intent intent = new Intent(getActivity(), SponsorDetailActivity.class);
//					intent.putExtra("orgId", bean.getId());
//					startActivity(intent);
//				}
//			});
//			return convertView;
//		}
//	}
//}
