package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.lang.reflect.Field;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.LoginActivityNew;
//import com.accuvally.hdtui.activity.MainActivity;
//import com.accuvally.hdtui.activity.SearchActivityNew;
//import com.accuvally.hdtui.activity.UpdateCityActivity;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.ui.CircleImageView;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.Utils;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//
//public class LeftSlidingMenuFragment extends BaseFragment implements OnClickListener {
//
//	private TextView tvHome, tvHomeTicket, tvHomeAddAccu, tvHomeMore, tvHomeSearch, tvHomeFenlei;
//
//	public TextView tvHomeCity;
//
//	public static CircleImageView ivHead;
//
//	public static TextView tvUserName;
//	private String cityName;
//
//	private IntentFilter filter;
//
//	private ChangeCityReceiver receiver;
//
//	private LinearLayout llHomeCity;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.main_left_layout, container, false);
//
//		initView(view);
//		return view;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
////		cityName = Config.cityName;
//		if (cityName == null) {
//			cityName = "北京";
//		}
//		tvHomeCity.setText(cityName);
//		initData();
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		getActivity().registerReceiver(receiver, filter);
//	}
//
//	@Override
//	public void onStart() {
//		if (receiver.isOrderedBroadcast()) {
//			getActivity().unregisterReceiver(receiver);
//		}
//		super.onStart();
//	}
//
//	public void initView(View view) {
//		tvHome = (TextView) view.findViewById(R.id.tvHome);
//		tvHomeTicket = (TextView) view.findViewById(R.id.tvHomeTicket);
//		tvHomeAddAccu = (TextView) view.findViewById(R.id.tvHomeMyAccu);
//		tvHomeMore = (TextView) view.findViewById(R.id.tvHomeMore);
//		ivHead = (CircleImageView) view.findViewById(R.id.ivHead);
//		tvUserName = (TextView) view.findViewById(R.id.tvUserName);
//		tvHomeSearch = (TextView) view.findViewById(R.id.tvHomeSearch);
//		tvHomeFenlei = (TextView) view.findViewById(R.id.tvHomeFenlei);
//		tvHomeCity = (TextView) view.findViewById(R.id.tvHomeCity);
//		llHomeCity = (LinearLayout) view.findViewById(R.id.llHomeCity);
//		tvHome.setOnClickListener(this);
//		tvHomeTicket.setOnClickListener(this);
//		tvHomeAddAccu.setOnClickListener(this);
//		tvHomeMore.setOnClickListener(this);
//		ivHead.setOnClickListener(this);
//		tvHomeSearch.setOnClickListener(this);
//		tvHomeFenlei.setOnClickListener(this);
//		tvHomeCity.setOnClickListener(this);
//		llHomeCity.setOnClickListener(this);
//
//		tvHomeCity.setText(cityName);
////		filter = new IntentFilter(Config.WEBVIEW_HOME_ADD);
////		filter.addAction(Config.HOME_TOP);
//		receiver = new ChangeCityReceiver();
//
//	}
//
//	public void initData() {
//		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.not_lgin_head_bg).showImageForEmptyUri(R.drawable.not_lgin_head_bg).showImageOnFail(R.drawable.not_lgin_head_bg).cacheInMemory(true).cacheOnDisk(true).build();
//		if (application.checkIsLogin()) {
//			application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivHead, options);
//			tvUserName.setText(application.getUserInfo().getNick());
//		} else {
//			application.mImageLoader.displayImage("drawable://" + R.drawable.not_lgin_head_bg, ivHead, options);
//			tvUserName.setText("点击登录");
//		}
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.tvHomeCity:
//		case R.id.llHomeCity:
//			if (!NetworkUtils.isNetworkAvailable(getActivity())) {
//				application.showMsg("没有网络");
//				return;
//			}
//			toActivity(new Intent(mContext, UpdateCityActivity.class).putExtra("tag", 1).putExtra("cityname", tvHomeCity.getText().toString()));
//			break;
//		case R.id.tvHome:
//			selection(0);
//			((MainActivity) getActivity()).setTabSelection(0);
//			break;
//		case R.id.tvHomeTicket:
//			selection(2);
//			((MainActivity) getActivity()).setTabSelection(2);
//			break;
//		case R.id.tvHomeMyAccu:
//			if (!application.checkIsLogin()) {
//				toActivity(new Intent(mContext, LoginActivityNew.class).putExtra("tag", Config.NOTTICKET_CONTENT));
//			} else {
//				selection(3);
//				((MainActivity) getActivity()).setTabSelection(3);
//			}
//			break;
//		case R.id.tvHomeSearch:
//			Intent intent = new Intent(mContext, SearchActivityNew.class);
//			intent.putExtra("temp", 1);
//			((MainActivity) getActivity()).startActivityForResult(intent, 12);
//			break;
//		case R.id.tvHomeMore:
//			selection(4);
//			((MainActivity) getActivity()).setTabSelection(4);
//			break;
//		case R.id.tvHomeFenlei:
//			selection(1);
//			((MainActivity) getActivity()).setTabSelection(1);
//			break;
//		case R.id.ivHead:
//			if (Utils.isFastDoubleClick())
//				return;
//			if (!application.checkIsLogin()) {
//				toActivity(new Intent(mContext, LoginActivityNew.class).putExtra("tag", Config.NOTTICKET_CONTENT));
//			} else {
//				selection(3);
//				((MainActivity) getActivity()).setTabSelection(3);
//			}
//			break;
//		}
//	}
//
//	void setDrawable(int homeDraw, int classify, int ticketDraw, int accuvallyDraw, int settingDraw) {
//		Drawable drawable = getResources().getDrawable(homeDraw);
//		Drawable drawable2 = getResources().getDrawable(classify);
//		Drawable drawable3 = getResources().getDrawable(ticketDraw);
//		Drawable drawable4 = getResources().getDrawable(settingDraw);
//		Drawable drawable5 = getResources().getDrawable(accuvallyDraw);
//		tvHome.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//		tvHomeFenlei.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
//		tvHomeTicket.setCompoundDrawablesWithIntrinsicBounds(drawable3, null, null, null);
//		tvHomeAddAccu.setCompoundDrawablesWithIntrinsicBounds(drawable5, null, null, null);
//		tvHomeMore.setCompoundDrawablesWithIntrinsicBounds(drawable4, null, null, null);
//	}
//
//	void setTextColor(int homeColor, int classify, int ticketColr, int accuvallyColor, int settingColor, int homeDraw, int classifyDraw, int ticketDraw, int accuvallyDraw, int settingDraw) {
//		tvHome.setTextColor(getResources().getColor(homeColor));
//		tvHomeFenlei.setTextColor(getResources().getColor(classify));
//		tvHomeTicket.setTextColor(getResources().getColor(ticketColr));
//		tvHomeAddAccu.setTextColor(getResources().getColor(accuvallyColor));
//		tvHomeMore.setTextColor(getResources().getColor(settingColor));
//		tvHome.setBackgroundResource(homeDraw);
//		tvHomeFenlei.setBackgroundResource(classifyDraw);
//		tvHomeTicket.setBackgroundResource(ticketDraw);
//		tvHomeAddAccu.setBackgroundResource(accuvallyDraw);
//		tvHomeMore.setBackgroundResource(settingDraw);
//	}
//
//	public void selection(int id) {
//		if (id == 0) {
//			setDrawable(R.drawable.home_bg_selected, R.drawable.classfly_bg_normal, R.drawable.ticket_bg_normal, R.drawable.add_accu_bg_normal, R.drawable.setting_bg_normal);
//			setTextColor(R.color.txt_green, R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, R.drawable.main_left_selected_bg, 0, 0, 0, 0);
//		} else if (id == 1) {
//			setDrawable(R.drawable.home_bg_normal, R.drawable.classfly_bg_selected, R.drawable.ticket_bg_normal, R.drawable.add_accu_bg_normal, R.drawable.setting_bg_normal);
//			setTextColor(R.color.main_left_text_color, R.color.txt_green, R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, 0, R.drawable.main_left_selected_bg, 0, 0, 0);
//		} else if (id == 2) {
//			setDrawable(R.drawable.home_bg_normal, R.drawable.classfly_bg_normal, R.drawable.ticket_bg_selected, R.drawable.add_accu_bg_normal, R.drawable.setting_bg_normal);
//			setTextColor(R.color.main_left_text_color, R.color.main_left_text_color, R.color.txt_green, R.color.main_left_text_color, R.color.main_left_text_color, 0, 0, R.drawable.main_left_selected_bg, 0, 0);
//		} else if (id == 3) {
//			setDrawable(R.drawable.home_bg_normal, R.drawable.classfly_bg_normal, R.drawable.ticket_bg_normal, R.drawable.add_accu_bg_selected, R.drawable.setting_bg_normal);
//			setTextColor(R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, R.color.txt_green, R.color.main_left_text_color, 0, 0, 0, R.drawable.main_left_selected_bg, 0);
//		} else if (id == 4) {
//			setDrawable(R.drawable.home_bg_normal, R.drawable.classfly_bg_normal, R.drawable.ticket_bg_normal, R.drawable.add_accu_bg_normal, R.drawable.setting_bg_selected);
//			setTextColor(R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, R.color.main_left_text_color, R.color.txt_green, 0, 0, 0, 0, R.drawable.main_left_selected_bg);
//		}
//	}
//
//	class ChangeCityReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			final String action = intent.getAction();
////			if (action.equals(Config.WEBVIEW_HOME_ADD)) {
////				cityName = intent.getStringExtra("tag");
////				if (!cityName.isEmpty()) {
////					tvHomeCity.setText(cityName);
////				}
////			} else if (action.equals(Config.HOME_TOP)) {
////				selection(0);
////				((MainActivity) getActivity()).setTabSelection(0);
////			}
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
