package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Parcelable;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AccuvallyDetailsActivityNew;
//import com.accuvally.hdtui.activity.AccuvallyWebDetailsActivity;
//import com.accuvally.hdtui.activity.MainActivity;
//import com.accuvally.hdtui.activity.SearchActivityNew;
//import com.accuvally.hdtui.adapter.HomeEventNewAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.db.DBManager;
//import com.accuvally.hdtui.model.AdNewsInfo;
//import com.accuvally.hdtui.model.HomeEventInfo;
//import com.accuvally.hdtui.model.MessageInfo;
//import com.accuvally.hdtui.model.Project;
//import com.accuvally.hdtui.model.SquareLimitedTimeOffers;
//import com.accuvally.hdtui.ui.MyRadioGroup;
//import com.accuvally.hdtui.ui.SyncHorizontalScrollView;
//import com.accuvally.hdtui.ui.XListView;
//import com.accuvally.hdtui.ui.XListView.IXListViewListener;
//import com.accuvally.hdtui.utils.DialogUtils;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.LocationUtils;
//import com.accuvally.hdtui.utils.LocationUtils.LocatinCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.Utils;
//import com.accuvally.hdtui.utils.VersionUtils;
//import com.alibaba.fastjson.JSON;
//import com.baidu.location.BDLocation;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.umeng.update.UmengUpdateAgent;
//
//public class HomeFragmentNew extends BaseFragment implements IXListViewListener, OnClickListener {
//
//	private static XListView mListView;
//
//	private ViewPager mGallery;
//
//	private LinearLayout lyLoading;
//
//	private List<Project> listAdNews;// 轮播图
//
//	private LinearLayout page_change_viewer;
//
//	private TextView loading_textview;
//
//	private int pageIndex = 1;
//
//	private List<HomeEventInfo> list;
//
//	private HomeEventNewAdapter mAdapter;
//
//	private String city;
//
//	private BroadcastReceiver receiver;
//
//	private IntentFilter filter;
//
//	private OnCityChangeListener changeListener;
//
//	private OnSqListener mListener;
//
//	private Dialog locationDialog;
//
//	private Dialog updateLocationDialog;
//
//	private LocationUtils locationUtils;
//
//	private int isFirstLocation = 1;
//
//	private int currentItem = 0; // 当前图片的索引号
//
//	private ScheduledExecutorService scheduledExecutorService;
//
//	private TextView tv_select_accuvally;
//
//	private String costText = "全部";
//
//	private int radioButtonCostId = -1;
//	private int radioButtonAddressId = -1;
//	private int radioButtonTimeId = -1;
//
//	protected String free = "";
//
//	protected String timeText;
//	private String time = "";
//	public static String interest = "";
//	private int screen = 0;
//	private String screening = "";
//
//	private SyncHorizontalScrollView sv_navigation;
//	private ImageView iv_sliding_menu;
//	private ImageView iv_search;
//	private EditText et_search;
//	private LinearLayout ly_slidingMenu;
//	private String logo;
//	private ImageView iv_logo;
//	private ImageView iv_logo_tag;
//	private RelativeLayout rlHeader;
//	private SyncHorizontalScrollView sv_navigation_image;
//	private View header;
//	private ImageView iv_close_select_accuvally_dialog;
//
//	private RelativeLayout rl_nav;
//
//	private RadioGroup rg_nav_content;
//
//	private ImageView iv_nav_indicator;
//
//	private ImageView iv_nav_left;
//
//	private ImageView iv_nav_right;
//
//	private RadioGroup rg_nav_content_image;
//
//	private RelativeLayout rl_nav_image;
//
//	private ImageView iv_nav_indicator_image;
//
//	private ImageView iv_nav_left_image;
//
//	private ImageView iv_nav_right_image;
//
//	private ImageView[] iv;
//	private ImageView[] iv1;
//	protected int height = 0;
//
//	private boolean titleFlag = true;
//
//	protected int lastIndex;
//
//	public interface OnCityChangeListener {
//		public void onUpdateCity(String city);
//	}
//
//	public interface OnSqListener {
//		public void onSqAction();
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnSqListener) activity;
//			changeListener = (OnCityChangeListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString() + " must implement OnFragmentListener");
//		}
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_home_new, container, false);
//		initView(view);
//		initData();
//		lyLoading.setVisibility(View.VISIBLE);
//		initTopPager();
//		addListener();
//		city = application.sharedUtils.readString("cityName");
//		db = new DBManager(mContext);
//		UmengUpdateAgent.setDefault();
//		UmengUpdateAgent.update(mContext);
//		if (db.getSaveBeHavior().size() > 0) {
//			svaeBeHavior(db.getSaveBeHavior());
//		}
//		return view;
//	}
//
//	public static void selectionListView() {
//		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
//			try {
//				mListView.smoothScrollByOffset(2);
//				mListView.smoothScrollToPosition(0);
//			} catch (Exception e) {
//				mListView.setSelection(0);
//			}
//		}
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		getActivity().registerReceiver(receiver, filter);
//	}
//
//	@Override
//	public void onStop() {
//		scheduledExecutorService.shutdown();
//		super.onStop();
//	}
//
//	@Override
//	public void onStart() {
//		if (receiver.isOrderedBroadcast()) {
//			getActivity().unregisterReceiver(receiver);
//		}
//		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
//		super.onStart();
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//	}
//
//	public void onDestroy() {
//		super.onDestroy();
//		mGallery = null;
//		listAdNews = null;
//	}
//
//	public void initView(final View view) {
//		setViewFailure(view);
//		mListView = (XListView) view.findViewById(R.id.home_new_listview);
//		lyLoading = (LinearLayout) view.findViewById(R.id.lyLoading);
//		header = LayoutInflater.from(getActivity()).inflate(R.layout.include_home_new, null);
//		loading_textview = (TextView) header.findViewById(R.id.loading);
//		page_change_viewer = (LinearLayout) header.findViewById(R.id.page_change_viewer);
//		mGallery = (ViewPager) header.findViewById(R.id.home_square_gl);
//		sv_navigation_image = (SyncHorizontalScrollView) header.findViewById(R.id.sv_navigation_image);
//		rg_nav_content_image = (RadioGroup) header.findViewById(R.id.rg_nav_content_image);
//		rl_nav_image = (RelativeLayout) header.findViewById(R.id.rl_nav_image);
//		iv_nav_indicator_image = (ImageView) header.findViewById(R.id.iv_nav_indicator_image);
//		iv_nav_left_image = (ImageView) header.findViewById(R.id.iv_nav_left_image);
//		iv_nav_right_image = (ImageView) header.findViewById(R.id.iv_nav_right_image);
//		tv_select_accuvally = (TextView) view.findViewById(R.id.tv_select_accuvally);
//		iv_sliding_menu = (ImageView) header.findViewById(R.id.iv_sliding_menu);
//		iv_search = (ImageView) header.findViewById(R.id.iv_search);
//		iv_logo = (ImageView) header.findViewById(R.id.iv_logo);
//		iv_logo_tag = (ImageView) header.findViewById(R.id.iv_logo_tag);
//		et_search = (EditText) header.findViewById(R.id.et_search);
//		ly_slidingMenu = (LinearLayout) view.findViewById(R.id.ly_slidingMenu);
//		sv_navigation = (SyncHorizontalScrollView) view.findViewById(R.id.sv_navigation);
//		rl_nav = (RelativeLayout) view.findViewById(R.id.rl_nav);
//		rg_nav_content = (RadioGroup) view.findViewById(R.id.rg_nav_content);
//		iv_nav_indicator = (ImageView) view.findViewById(R.id.iv_nav_indicator);
//		iv_nav_left = (ImageView) view.findViewById(R.id.iv_nav_left);
//		iv_nav_right = (ImageView) view.findViewById(R.id.iv_nav_right);
//
//		iv = new ImageView[11];
//
//		iv[0] = (ImageView) view.findViewById(R.id.iv_1);
//		iv[1] = (ImageView) view.findViewById(R.id.iv_2);
//		iv[2] = (ImageView) view.findViewById(R.id.iv_3);
//		iv[3] = (ImageView) view.findViewById(R.id.iv_4);
//		iv[4] = (ImageView) view.findViewById(R.id.iv_5);
//		iv[5] = (ImageView) view.findViewById(R.id.iv_6);
//		iv[6] = (ImageView) view.findViewById(R.id.iv_7);
//		iv[7] = (ImageView) view.findViewById(R.id.iv_8);
//		iv[8] = (ImageView) view.findViewById(R.id.iv_9);
//		iv[9] = (ImageView) view.findViewById(R.id.iv_10);
//		iv[10] = (ImageView) view.findViewById(R.id.iv_11);
//		iv1 = new ImageView[11];
//		iv1[0] = (ImageView) header.findViewById(R.id.iv_image1);
//		iv1[1] = (ImageView) header.findViewById(R.id.iv_image2);
//		iv1[2] = (ImageView) header.findViewById(R.id.iv_image3);
//		iv1[3] = (ImageView) header.findViewById(R.id.iv_image4);
//		iv1[4] = (ImageView) header.findViewById(R.id.iv_image5);
//		iv1[5] = (ImageView) header.findViewById(R.id.iv_image6);
//		iv1[6] = (ImageView) header.findViewById(R.id.iv_image7);
//		iv1[7] = (ImageView) header.findViewById(R.id.iv_image8);
//		iv1[8] = (ImageView) header.findViewById(R.id.iv_image9);
//		iv1[9] = (ImageView) header.findViewById(R.id.iv_image10);
//		iv1[10] = (ImageView) header.findViewById(R.id.iv_image11);
//
//		mListView.addHeaderView(header);
//		rlHeader = (RelativeLayout) view.findViewById(R.id.rlHeader);
//		rlHeader.setVisibility(View.GONE);
//		sv_navigation.setVisibility(View.GONE);
//		list = new ArrayList<HomeEventInfo>();
//		mAdapter = new HomeEventNewAdapter(mContext, 0);
//		mAdapter.setList(list);
//		mListView.setXListViewListener(this);
//		mListView.setAdapter(mAdapter);
//		rlHeader.setOnClickListener(this);
////		filter = new IntentFilter(Config.WEBVIEW_HOME_ADD);
//		receiver = new ChangeReceiver();
//		locationUtils = new LocationUtils(mContext);
//
//		mListView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				switch (scrollState) {
//				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//					lastIndex = view.getLastVisiblePosition();
//					break;
//				case OnScrollListener.SCROLL_STATE_IDLE:
//					int scrolledIndex = view.getLastVisiblePosition();
//					if (scrolledIndex >= lastIndex) {
//						tv_select_accuvally.setVisibility(View.GONE);
//					} else {
//						tv_select_accuvally.setVisibility(View.VISIBLE);
//						controlCount = 2;
//						new Thread(new MyThread()).start();
//
//					}
//					break;
//				default:
//					break;
//				}
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem > 1) {
//					rlHeader.setVisibility(View.VISIBLE);
//					sv_navigation.setVisibility(View.VISIBLE);
//				} else {
//					rlHeader.setVisibility(View.GONE);
//					sv_navigation.setVisibility(View.GONE);
//				}
//			}
//		});
//		initWidth(sv_navigation, rg_nav_content, rl_nav, iv_nav_indicator, iv_nav_left, iv_nav_right, false);
//		initWidth(sv_navigation_image, rg_nav_content_image, rl_nav_image, iv_nav_indicator_image, iv_nav_left_image, iv_nav_right_image, true);
//	}
//
//	public static final String ARGUMENTS_NAME = "arg";
//	private boolean flag = false;
//	private int indicatorWidth;
//	public static String[] tabTitle = { "精彩推荐", "商务/会议", "创业/分享", "文化/沙龙", "公益/讲座", "亲子/互动", "户外/健身", "生活/社交", "展览/展会", "课程/培训", "演出其他" }; // 标题
//	public static int[] tabDrawableId = { R.drawable.navigation_all, R.drawable.navigation_business_meet, R.drawable.navigation_carve_out, R.drawable.navigation_cultural_salon, R.drawable.navigation_public_lecture, R.drawable.navigation_parent_children_interaction, R.drawable.navigation_outdoor_gym, R.drawable.navigation_life_socail, R.drawable.navigation_exhibition_party, R.drawable.navigation_course_train, R.drawable.navigation_show_other };
//	private LayoutInflater mInflater;
//	private int currentIndicatorLeft = 0;
//	private float currentIndicatorLeftImage = 0;
//
//	private void initWidth(SyncHorizontalScrollView syncHorizontalScrollView, RadioGroup radioGroup, RelativeLayout relativeLayout, ImageView ivIndicator, ImageView ivLeft, ImageView ivRight, boolean flag) {
//		DisplayMetrics dm = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		indicatorWidth = (int) (dm.widthPixels / 4);
//
//		android.view.ViewGroup.LayoutParams cursor_Params = ivIndicator.getLayoutParams();
//		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
//		android.view.ViewGroup.LayoutParams iv_line = iv[0].getLayoutParams();
//		iv_line.width = indicatorWidth;
//		ivIndicator.setLayoutParams(cursor_Params);
//		for (int i = 0; i < iv.length; i++) {
//			iv[i].setLayoutParams(iv_line);
//			iv1[i].setLayoutParams(iv_line);
//		}
//		syncHorizontalScrollView.setSomeParam(relativeLayout, ivLeft, ivRight, getActivity());
//
//		// 获取布局填充器
//		mInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
//
//		initNavigationHSV(flag, radioGroup);
//	}
//
//	private void initNavigationHSV(boolean flag, RadioGroup radioGroup) {
//
//		radioGroup.removeAllViews();
//
//		for (int i = 0; i < tabTitle.length; i++) {
//
//			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
//			rb.setId(i);
//			rb.setText(tabTitle[i]);
//			rb.setLayoutParams(new LayoutParams(indicatorWidth, LayoutParams.MATCH_PARENT));
//			if (i == 0) {
//				float size = 18;
//				if (isAdded()) {
//					size = getResources().getDimensionPixelSize(R.dimen.little_18);
//				}
//				rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//			}
//			if (flag) {
//				Drawable drawable = null;
//				if (isAdded()) {
//					drawable = getResources().getDrawable(tabDrawableId[i]);
//				}
//				rb.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//				iv[0].setVisibility(View.INVISIBLE);
//				iv1[0].setVisibility(View.INVISIBLE);
//			}
//			radioGroup.addView(rb);
//			radioGroup.check(0);
//
//		}
//
//	}
//
//	private void addListener() {
//		tv_select_accuvally.setOnClickListener(this);
//		iv_sliding_menu.setOnClickListener(this);
//		iv_search.setOnClickListener(this);
//		et_search.setOnClickListener(this);
//		ly_slidingMenu.setOnClickListener(this);
//
//		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				if (rg_nav_content.getChildAt(checkedId) != null) {
//
//					TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft, ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
//					animation.setInterpolator(new LinearInterpolator());
//					animation.setDuration(100);
//					animation.setFillAfter(true);
//					if (isAdded()) {
//						setTextSize(checkedId);
//					}
//
//					// 执行位移动画
//
//					iv_nav_indicator.startAnimation(animation);
//					iv_nav_indicator_image.startAnimation(animation);
//					interest = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getText().toString();
//					db.insertSaveBeHavior(application.addBeHavior(90, 0, "", interest, ""));
//					if (interest.indexOf("/") != -1)
//						interest = interest.replaceAll("/", ",");
//					else if ("精彩推荐".equals(interest) || "演出其他".equals(interest)) {
//						if ("精彩推荐".equals(interest)) {
//							interest = "";
//						} else {
//							interest = "演出其他";
//						}
//					} else {
//						interest = interest.substring(0, 2) + "," + interest.substring(2, 4);
//					}
//					setImage(checkedId);
//					if (!NetworkUtils.isNetworkAvailable(mContext)) {
//						application.showMsg("啊哦，网络好像断开了！");
//						return;
//					}
//					pageIndex = 1;
//					screen = 0;
//					flag = true;
//					initSelectData();
//
//					// mViewPager.setCurrentItem(checkedId); //ViewPager 跟随一起 切换
//
//					// 记录当前 下标的距最左侧的 距离
//					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
//					sv_navigation_image.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
//					sv_navigation.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
//				}
//			}
//		});
//		rg_nav_content_image.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				if (rg_nav_content_image.getChildAt(checkedId) != null) {
//
//					TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeftImage, ((RadioButton) rg_nav_content_image.getChildAt(checkedId)).getLeft(), 0f, 0f);
//					animation.setInterpolator(new LinearInterpolator());
//					animation.setDuration(100);
//					animation.setFillAfter(true);
//					if (isAdded()) {
//						setTextSize(checkedId);
//					}
//					// 执行位移动画
//					iv_nav_indicator_image.startAnimation(animation);
//
//					iv_nav_indicator.startAnimation(animation);
//					interest = ((RadioButton) rg_nav_content_image.getChildAt(checkedId)).getText().toString();
//					db.insertSaveBeHavior(application.addBeHavior(90, 0, "", interest, ""));
//					if (interest.indexOf("/") != -1)
//						interest = interest.replaceAll("/", ",");
//					else if ("精彩推荐".equals(interest) || "演出其他".equals(interest)) {
//						if ("精彩推荐".equals(interest)) {
//							interest = "";
//						} else {
//							interest = "演出其他";
//						}
//					} else {
//						interest = interest.substring(0, 2) + "," + interest.substring(2, 4);
//					}
//					setImage(checkedId);
//					if (!NetworkUtils.isNetworkAvailable(mContext)) {
//						application.showMsg("啊哦，网络好像断开了！");
//						return;
//					}
//					pageIndex = 1;
//					screen = 0;
//					flag = false;
//					initSelectData();
//					// mViewPager.setCurrentItem(checkedId); //ViewPager 跟随一起 切换
//
//					// 记录当前 下标的距最左侧的 距离
//					currentIndicatorLeftImage = ((RadioButton) rg_nav_content_image.getChildAt(checkedId)).getLeft();
//
//					sv_navigation_image.smoothScrollTo((checkedId > 2 ? ((RadioButton) rg_nav_content_image.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content_image.getChildAt(2)).getLeft(), 0);
//					sv_navigation.smoothScrollTo((checkedId > 2 ? ((RadioButton) rg_nav_content_image.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content_image.getChildAt(2)).getLeft(), 0);
//				}
//			}
//		});
//	}
//
//	protected void setImage(int checkedId) {
//		for (int i = 0; i < iv.length; i++) {
//			if (i == checkedId) {
//				iv[i].setVisibility(View.INVISIBLE);
//				iv1[i].setVisibility(View.INVISIBLE);
//			} else {
//				iv[i].setVisibility(View.VISIBLE);
//				iv1[i].setVisibility(View.VISIBLE);
//			}
//		}
//	}
//
//	protected void setTextSize(int checkedId) {
//		for (int i = 0; i < tabTitle.length; i++) {
//			if (i == checkedId) {
//				((RadioButton) rg_nav_content.getChildAt(i)).setTextColor(getResources().getColor(R.color.txt_green));
//				((RadioButton) rg_nav_content_image.getChildAt(i)).setTextColor(getResources().getColor(R.color.txt_green));
//				((RadioButton) rg_nav_content.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.little_18));
//				((RadioButton) rg_nav_content_image.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.little_18));
//			} else {
//				((RadioButton) rg_nav_content.getChildAt(i)).setTextColor(getResources().getColor(R.color.txt_gray));
//				((RadioButton) rg_nav_content_image.getChildAt(i)).setTextColor(getResources().getColor(R.color.txt_gray));
//				((RadioButton) rg_nav_content.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.little_16));
//				((RadioButton) rg_nav_content_image.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.little_16));
//			}
//		}
//	}
//
//	public void initData() {
//		mGallery.setVisibility(View.VISIBLE);
//
//		mGallery.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				initPointView(position);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});
//		showFailureOnClick(new OnClickCallBack() {
//			public void callBack(boolean is) {
//				goneFailure();
//				if (is) {
//					mListener.onSqAction();
//				} else {
//					initTopPager();
//				}
//			}
//		});
//	}
//
//	public void initTopPager() {
//		if (application.sharedUtils.readBoolean("isLocation") == false) {
//			if (NetworkUtils.isNetworkAvailable(mContext)) {
//				dialog_location();
//			} else {
////				city = Config.cityName;
//			}
//		} else {
//			if (application.sharedUtils.readString("cityName") == null) {
//				initLocation();
//			} else {
////				city = Config.cityName;
//			}
//		}
//		toppager();
//	}
//
//	public void toppager() {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("city", city));
//		httpCilents.get(httpCilents.printURL(Config.ACCUPASS_SQUAREADNEWS, list), new WebServiceCallBack() {
//			@Override
//			public void callBack(int code, Object result) {
//				mListView.stopRefresh();
//				loading_textview.setVisibility(View.GONE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					MessageInfo info = JSON.parseObject(result.toString(), MessageInfo.class);
//					application.cacheUtils.put("square", info.getResult().toString());
//					square(info.getResult().toString());
//					break;
//				case Config.RESULT_CODE_ERROR:
//					if (application.cacheUtils.getAsString("square") == null) {
//						lyLoading.setVisibility(View.GONE);
//						application.showMsg(result.toString());
//					} else {
//						mListView.setVisibility(View.VISIBLE);
//						square(application.cacheUtils.getAsString("square"));
//					}
//					break;
//				}
//			}
//		});
//	}
//
//	// private HomeEventInfo[] homeEventInfos;
//	public void initRecommend() {
//		String tag = "";
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("pi", pageIndex + ""));
//		if (pageIndex == 1) {
//			list.add(new BasicNameValuePair("ps", "9"));
//		} else {
//			list.add(new BasicNameValuePair("ps", "10"));
//		}
//		list.add(new BasicNameValuePair("filter", "0"));
//		list.add(new BasicNameValuePair("city", city));
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
//		httpCilents.get(httpCilents.printURL(Config.ACCUPASS_HOME_TAB, list), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//
//				lyLoading.setVisibility(View.GONE);
//				mListView.stopRefresh();
//				mListView.stopLoadMore();
//				mListView.setVisibility(View.VISIBLE);
//				goneFailure();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<HomeEventInfo> list = JSON.parseArray(result.toString(), HomeEventInfo.class);
//					if (pageIndex == 1 && list.size() != 0) {
//						mAdapter.removeAll();
//						mListView.setSelectionAfterHeaderView();
//						application.cacheUtils.put("homefirst", result.toString());
//
//						handler.sendEmptyMessage(listview_header_item_option);
//					}
//					mAdapter.addAll(list);
//
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					if (pageIndex == 1 && application.cacheUtils.getAsString("homefirst") == null) {
//						mListView.setVisibility(View.GONE);
//						lyLoading.setVisibility(View.GONE);
//						showWifi();
//					} else if (pageIndex == 1 && application.cacheUtils.getAsString("homefirst") != null) {
//						lyLoading.setVisibility(View.GONE);
//						mListView.setVisibility(View.VISIBLE);
//						List<HomeEventInfo> list2 = JSON.parseArray(application.cacheUtils.getAsString("homefirst"), HomeEventInfo.class);
//						mAdapter.addAll(list2);
//					}
//					break;
//				}
//			}
//		});
//	}
//
//	private int controlCount = 0;
//
//	public List<Project> topicList;
//
//	public List<SquareLimitedTimeOffers> limiteds;
//
//	public class MyThread implements Runnable {
//
//		@Override
//		public void run() {
//
//			try {
//				Thread.sleep(5000);// 线程暂停5秒，单位毫秒
//				Message message = new Message();
//				message.what = controlCount;
//				handler.sendMessage(message);// 发送消息
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	private int alphaCount = 0;
//
//	private List<HomeEventInfo> actsList;
//
//	public void square(String result) {
//		try {
//			AdNewsInfo adNews = JSON.parseObject(result, AdNewsInfo.class);
//			listAdNews = adNews.getAdNews();
//			logo = adNews.getLogo();
//
//			if (alphaCount == 0) {
//				application.mImageLoader.displayImage(logo, iv_logo);
//				Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
//				alphaAnimation.setDuration(5000);
//				iv_logo.setAnimation(alphaAnimation);
//				iv_logo_tag.setAnimation(alphaAnimation);
//				et_search.setAnimation(alphaAnimation);
//				alphaCount++;
//				iv_logo.setVisibility(View.GONE);
//				iv_logo_tag.setVisibility(View.GONE);
//				et_search.setVisibility(View.GONE);
//			}
//
//			mGallery.setAdapter(new ChannelAdsAdapter());
//			initPointView(0);
//			pageIndex = 1;
//			// initRecommendTopicAndLimitActs();
//
//			initSelectData();
//
//		} catch (Exception e) {
//
//		}
//	}
//
//	private void squareTopicAndLimitActs(String result) {
//		JSONObject jo;
//		try {
//			jo = new JSONObject(result);
//			if (pageIndex == 1) {
//				topicList = JSON.parseArray(jo.getString("Topic"), Project.class);
//				limiteds = JSON.parseArray(jo.getString("LimitActs"), SquareLimitedTimeOffers.class);
//			}
//			actsList = JSON.parseArray(jo.getString("Acts"), HomeEventInfo.class);
//			if (pageIndex == 1 && actsList.size() != 0) {
//				mAdapter.removeAll();
//				if (flag) {
//					handler.sendEmptyMessage(scroll_fix_position);
//				} else {
//					mListView.setSelectionAfterHeaderView();
//				}
//				application.cacheUtils.put("topicandlimitacts", result.toString());
//			}
//			if (pageIndex == 1 && actsList.size() == 0) {
//				return;
//
//			}
//			if (pageIndex == 1) {
//				mAdapter.setLimitActs(limiteds);
//				mAdapter.setTopic(topicList);
//			}
//			mAdapter.addAll(actsList);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	protected void initPointView(int pos) {
//		if (listAdNews == null || listAdNews.size() <= 0) {
//			return;
//		}
//		page_change_viewer.removeAllViews();
//		try {
//			for (int i = 0; i < listAdNews.size(); i++) {
//				ImageView pointView = new ImageView(getActivity().getApplicationContext());
//				final ImageView curPointView = new ImageView(getActivity().getApplicationContext());
//				pointView.setBackgroundResource(R.drawable.home_new_point);
//				curPointView.setBackgroundResource(R.drawable.home_new_cur);
//				LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//				layoutParams.leftMargin = 10;
//				if (pos == i) {
//					page_change_viewer.addView(curPointView, layoutParams);
//				} else {
//					page_change_viewer.addView(pointView, layoutParams);
//				}
//			}
//		} catch (NullPointerException e) {
//		}
//
//	}
//
//	// 切换当前显示的图片
//	private int interest_but_option = 2;
//	private int listview_header_item_option = 3;
//	private int scroll_fix_position = 4;
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// 切换当前显示的图片
//			try {
//				if (msg.what == interest_but_option) {
//					tv_select_accuvally.setVisibility(View.GONE);
//				}
//
//				if (msg.what == scroll_fix_position) {
//					selectionListView();
//				}
//				mGallery.setCurrentItem(currentItem);
//			} catch (Exception e) {
//			}
//		};
//
//	};
//
//	private Dialog selectDialog;
//	private TextView sure;
//	private MyRadioGroup myRadioGroupTime;
//	private RadioGroup rgCost;
//	private RadioGroup rgAddress;
//
//	/**
//	 * 换行切换任务
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	private class ScrollTask implements Runnable {
//
//		public void run() {
//			synchronized (mGallery) {
//				System.out.println("currentItem: " + currentItem);
//				currentItem = (currentItem + 1) % listAdNews.size();
//				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
//			}
//		}
//
//	}
//
//	/**
//	 * 广告轮播
//	 */
//	private class ChannelAdsAdapter extends PagerAdapter {
//		private DisplayImageOptions options;
//
//		ChannelAdsAdapter() {
//			options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_details_image).showImageForEmptyUri(R.drawable.default_details_image).showImageOnFail(R.drawable.default_details_image).cacheInMemory(true).cacheOnDisk(true).build();
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			((ViewPager) container).removeView((View) object);
//		}
//
//		@Override
//		public void finishUpdate(View container) {
//		}
//
//		@Override
//		public int getCount() {
//			return listAdNews == null ? 0 : listAdNews.size();
//		}
//
//		@Override
//		public Object instantiateItem(ViewGroup view, final int position) {
//			View layoutview = LayoutInflater.from(mContext).inflate(R.layout.listitem_square_top_list, null);
//			ImageView image = (ImageView) layoutview.findViewById(R.id.iv_image);
//			final Project ads = listAdNews.get(position);
//			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			application.mImageLoader.displayImage(ads.getLogo(), image, options);
//			((ViewPager) view).addView(layoutview, 0);
//			layoutview.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View positioin) {
//					if (Utils.isFastDoubleClick()) {
//						return;
//					}
//					db.insertSaveBeHavior(application.addBeHavior(10, 0, ads.getId(), "", ""));
//					if (ads.isOpenInWeb()) {
//						mContext.startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl", ads.getUrl()).putExtra("injectJs", ""));
//					} else {
//						mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivityNew.class).putExtra("id", ads.getId()).putExtra("isHuodong", 0));
//					}
//				}
//			});
//			return layoutview;
//		}
//
//		@Override
//		public boolean isViewFromObject(View view, Object object) {
//			return view.equals(object);
//		}
//
//		@Override
//		public void restoreState(Parcelable state, ClassLoader loader) {
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//
//		@Override
//		public void startUpdate(View container) {
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
//		initTopPager();
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
//		// initRecommend();
//		initSelectData();
//	}
//
//	DBManager db;
//
//	private String url = "";
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.tvCancel:
//			locationDialog.dismiss();
//			application.sharedUtils.writeBoolean("isLocation", false);
//			city = "北京";
//			// LeftSlidingMenuFragment.tvHomeCity.setText(city);
////			Config.cityName = city;
//			if (isFirstLocation != 0) {
//				pageIndex = 1;
//				// initRecommend();
//				// initSelectData();
//				toppager();
//			}
//			changeListener.onUpdateCity(city);
//			break;
//		case R.id.tvSure:
//			isFirstLocation = 1;
//			locationDialog.dismiss();
//			if (NetworkUtils.isNetworkAvailable(mContext)) {
//				initLocation();
//			}
//			application.sharedUtils.writeBoolean("isLocation", true);
//			break;
//		case R.id.tv_select_accuvally:
//			dialogSelect();
//			break;
//		case R.id.tv_select_activity_submit:
//			if (!NetworkUtils.isNetworkAvailable(mContext)) {
//				application.showMsg("啊哦，网络好像断开了！");
//				mListView.stopLoadMore();
//				return;
//			}
//			flag = true;
//			titleFlag = false;
//			pageIndex = 1;
//			initSelectData();
//			selectDialog.dismiss();
//			break;
//		case R.id.iv_sliding_menu:
//		case R.id.ly_slidingMenu:
//			MainActivity.slidingMenu.showMenu();
//			break;
//		case R.id.iv_search:
//		case R.id.et_search:
//		case R.id.ly_setting:
//			Intent intent = new Intent(mContext, SearchActivityNew.class);
//			intent.putExtra("temp", 1);
//			startActivity(intent);
//			break;
//		case R.id.iv_close_select_accuvally_dialog:
//			selectDialog.dismiss();
//			break;
//		case R.id.rlHeader:
//			selectionListView();
//			break;
//
//		}
//
//	}
//
//	private void initSelectData() {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("pi", pageIndex + ""));
//		if (pageIndex == 1 && "".equals(interest) && titleFlag) {
//			list.add(new BasicNameValuePair("ps", "19"));
//		} else {
//			list.add(new BasicNameValuePair("ps", "20"));
//		}
//		list.add(new BasicNameValuePair("date", time));
//		list.add(new BasicNameValuePair("city", city));
//		list.add(new BasicNameValuePair("sort", "1"));
//		if (!"全部".equals(costText)) {
//			list.add(new BasicNameValuePair("free", free + ""));
//		}
//		if ("".equals(interest)) {
//			StringBuilder builder = new StringBuilder();
//			DBManager dbManager = new DBManager(mContext);
//			List<String> listString = dbManager.getClassfy();
//			if (listString.size() != 0) {
//				for (int i = 0; i < listString.size(); i++) {
//					builder.append(listString.get(i) + ",");
//				}
//				interest = builder.toString().replace("/", ",");
//			} else {
//				interest = "";
//			}
//			list.add(new BasicNameValuePair("tag", interest));
//		} else {
//			list.add(new BasicNameValuePair("tag", interest));
//		}
//		Log.i("info", interest);
//		list.add(new BasicNameValuePair("filter", screen + ""));
//		if (application.sharedUtils.readString("latitude") == null) {
//			list.add(new BasicNameValuePair("coordinates", ""));
//		} else {
//			list.add(new BasicNameValuePair("coordinates", application.sharedUtils.readString("latitude") + "," + application.sharedUtils.readString("longitude")));
//		}
//		if ("".equals(interest) && titleFlag) {
//			url = Config.ACCUPASS_SQUAREEVENTS2;
//		} else {
//			url = Config.ACCUPASS_HOME_TAB;
//			if (topicList != null && topicList.size() > 0) {
//				topicList.clear();
//				limiteds.clear();
//
//				mAdapter.notifyDataSetChanged();
//				mAdapter.setLimitActs(limiteds);
//				mAdapter.setTopic(topicList);
//			}
//		}
//		httpCilents.get(httpCilents.printURL(url, list), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				lyLoading.setVisibility(View.GONE);
//				mListView.stopRefresh();
//				mListView.stopLoadMore();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					if (Config.ACCUPASS_SQUAREEVENTS2.equals(url)) {
//
//						squareTopicAndLimitActs(result.toString());
//					} else {
//						List<HomeEventInfo> list = JSON.parseArray(result.toString(), HomeEventInfo.class);
//						if (pageIndex != 1 && list.size() == 0) {
//							application.showMsg("没有更多相关活动");
//							return;
//						}
//						if (pageIndex == 1 && list.size() != 0) {
//							mAdapter.removeAll();
//							if (flag) {
//								// mListView.setSelection(1);
//								handler.sendEmptyMessage(scroll_fix_position);
//							} else {
//								mListView.setSelectionAfterHeaderView();
//							}
//							application.cacheUtils.put("homeselect", result.toString());
//						}
//						if (pageIndex == 1 && list.size() == 0) {
//							// mListView.setVisibility(View.GONE);
//							application.showMsg("该分类或地域暂无活动");
//							return;
//							// showFailure();
//						}
//						mAdapter.addAll(list);
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					if (Config.ACCUPASS_SQUAREEVENTS2.equals(url)) {
//						if (application.cacheUtils.getAsString("topicandlimitacts") == null) {
//							application.showMsg(result.toString());
//						} else {
//							mListView.setVisibility(View.VISIBLE);
//							squareTopicAndLimitActs(application.cacheUtils.getAsString("topicandlimitacts"));
//						}
//					} else {
//						application.showMsg(result.toString());
//						if (pageIndex == 1 && application.cacheUtils.getAsString("homeselect") == null) {
//							mListView.setVisibility(View.GONE);
//							showWifi();
//						} else if (pageIndex == 1 && application.cacheUtils.getAsString("homeselect") != null) {
//							mListView.setVisibility(View.VISIBLE);
//							mAdapter.removeAll();
//							List<HomeEventInfo> list2 = JSON.parseArray(application.cacheUtils.getAsString("homeselect"), HomeEventInfo.class);
//							mAdapter.addAll(list2);
//						}
//					}
//					break;
//				}
//			}
//
//		});
//	}
//
//	private void dialogSelect() {
//		selectDialog = new Dialog(mContext, R.style.dialog);
//		selectDialog.setCancelable(true);
//		selectDialog.setCanceledOnTouchOutside(true);
//		LayoutInflater inflater = LayoutInflater.from(mContext);
//		View view = inflater.inflate(R.layout.dialog_select_accuvally, null);
//		sure = (TextView) view.findViewById(R.id.tv_select_activity_submit);
//		iv_close_select_accuvally_dialog = (ImageView) view.findViewById(R.id.iv_close_select_accuvally_dialog);
//		myRadioGroupTime = (MyRadioGroup) view.findViewById(R.id.my_radio_group_time);
//		rgCost = (RadioGroup) view.findViewById(R.id.rg_cost);
//		rgAddress = (RadioGroup) view.findViewById(R.id.rg_address);
//		addDialogListener(view);
//		if (radioButtonCostId != -1) {
//			rgCost.check(radioButtonCostId);
//		}
//		if (radioButtonAddressId != -1) {
//			rgAddress.check(radioButtonAddressId);
//		}
//		if (radioButtonTimeId != -1) {
//			myRadioGroupTime.check(radioButtonTimeId);
//		} else {
//			myRadioGroupTime.check(R.id.rb_all_time);
//		}
//		sure.setOnClickListener(this);
//		iv_close_select_accuvally_dialog.setOnClickListener(this);
//		selectDialog.setContentView(view);
//		DialogUtils.dialogSet(selectDialog, mContext, Gravity.BOTTOM, 1.0, -1, true, false, true);
//		selectDialog.show();
//	}
//
//	private void addDialogListener(final View view) {
//		rgAddress.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				radioButtonAddressId = group.getCheckedRadioButtonId();
//				RadioButton rb = (RadioButton) view.findViewById(radioButtonAddressId);
//				screening = rb.getText().toString();
//				if ("附近活动".equals(screening)) {
//					screen = 1;
//
//				} else if ("热门活动".equals(screening)) {
//					screen = 2;
//
//				} else if ("全部".equals(screening)) {
//					screen = 0;
//
//				}
//			}
//		});
//		rgCost.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				radioButtonCostId = group.getCheckedRadioButtonId();
//				RadioButton rb = (RadioButton) view.findViewById(radioButtonCostId);
//				costText = rb.getText().toString();
//				if ("全部".equals(costText)) {
//					free = "";
//				} else if ("免费".equals(costText)) {
//					free = "1";
//				} else {
//					free = "0";
//				}
//			}
//		});
//		myRadioGroupTime.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(MyRadioGroup group, int checkedId) {
//				radioButtonTimeId = checkedId;
//				RadioButton rb = (RadioButton) view.findViewById(checkedId);
//				timeText = rb.getText().toString();
//				if ("最近一周".equals(timeText)) {
//					time = "t1";
//				} else if ("今天".equals(timeText)) {
//					time = "t2";
//				} else if ("明天".equals(timeText)) {
//					time = "t3";
//				} else if ("周末".equals(timeText)) {
//					time = "t4";
//				} else if ("即将开始".equals(timeText)) {
//					time = "t5";
//				} else if ("全部".equals(timeText)) {
//					time = "";
//				}
//			}
//		});
//	}
//
//	public void dialog_location() {
//		locationDialog = new Dialog(mContext, R.style.dialog);
//		locationDialog.setCancelable(false);
//		locationDialog.setCanceledOnTouchOutside(false);
//		locationDialog.setContentView(R.layout.dialog_gps_location);
//		TextView tvCancel = (TextView) locationDialog.findViewById(R.id.tvCancel);
//		TextView tvSure = (TextView) locationDialog.findViewById(R.id.tvSure);
//
//		tvSure.setOnClickListener(this);
//		tvCancel.setOnClickListener(this);
//		locationDialog.show();
//	}
//
//	public void dialog_up_location(String cityName) {
//		updateLocationDialog = new Dialog(mContext, R.style.dialog);
//		updateLocationDialog.setCancelable(false);
//		updateLocationDialog.setCanceledOnTouchOutside(false);
//		updateLocationDialog.setContentView(R.layout.dialog_update_city);
//		final TextView tvUpdateCityContent = (TextView) updateLocationDialog.findViewById(R.id.tvUpContent);
//		TextView tvUpdateCancel = (TextView) updateLocationDialog.findViewById(R.id.tvUpdateCancel);
//		TextView tvUpdateSure = (TextView) updateLocationDialog.findViewById(R.id.tvUpdateSure);
//		tvUpdateCityContent.setText("系统定位你在" + cityName + "，是否切换？");
//		tvUpdateSure.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				updateLocationDialog.dismiss();
//				city = application.sharedUtils.readString("cityName");
//				pageIndex = 1;
//				// initRecommend();
//				initSelectData();
//				changeListener.onUpdateCity(city);
//			}
//		});
//		tvUpdateCancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				updateLocationDialog.dismiss();
//			}
//		});
//		updateLocationDialog.show();
//	}
//
//	public void initLocation() {
//		if (!NetworkUtils.isNetworkAvailable(mContext)) {
//			application.showMsg("啊哦，网络好像断开了！");
//			return;
//		}
//		locationUtils.location(new LocatinCallBack() {
//
//			@Override
//			public void callBack(int code, BDLocation location) {
//				dismissProgress();
//				switch (code) {
//				case 1:
//					try {
//						application.sharedUtils.writeString("longitude", location.getLongitude() + "");
//						application.sharedUtils.writeString("latitude", location.getLatitude() + "");
//						application.sharedUtils.writeString("cityName", location.getCity().replace("市", ""));
//						application.sharedUtils.writeString("addrStr", location.getAddrStr());
//						application.sharedUtils.writeString("province", location.getProvince());
//						city = application.sharedUtils.readString("cityName");
//						regdevice(location.getLongitude(), location.getLatitude());
//					} catch (Exception e) {
//						city = "北京";
//					}
//					break;
//				case 0:
//					city = "北京";
//					break;
//				}
//				locationUtils.stopListener();
//				changeListener.onUpdateCity(city);
//				// LeftSlidingMenuFragment.tvHomeCity.setText(city);
////				Config.cityName = city;
//				pageIndex = 1;
//				initTopPager();
//				// initSelectData();
//
//			}
//		});
//	}
//
//	public void changeLocation() {
//		if (!NetworkUtils.isNetworkAvailable(mContext)) {
//			application.showMsg("啊哦，网络好像断开了！");
//			return;
//		}
//		locationUtils.location(new LocatinCallBack() {
//
//			@Override
//			public void callBack(int code, BDLocation location) {
//				dismissProgress();
//				switch (code) {
//				case 1:
//					dialog_up_location(location.getCity().replace("市", ""));
//					break;
//				case 0:
//					city = "北京";
//					pageIndex = 1;
//					// initRecommend();
//					// initSelectData();
//					// pageIndex = 1;
//					initTopPager();
//					break;
//				}
//				locationUtils.stopListener();
//			}
//		});
//	}
//
//	public void regdevice(double lo, double la) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		if ("000000000000000".equals(application.getIMEI())) {
//			params.add(new BasicNameValuePair("DeviceID", application.getIMEI2()));
//		} else {
//			params.add(new BasicNameValuePair("DeviceID", ("".equals(application.getIMEI()) ? application.getIMEI2() : application.getIMEI())));
//		}
//		params.add(new BasicNameValuePair("DeviceType", 2 + ""));
//		params.add(new BasicNameValuePair("DeviceDesc", android.os.Build.MODEL));
//		try {
//			params.add(new BasicNameValuePair("AppVersion", VersionUtils.getVersionName(mContext)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		params.add(new BasicNameValuePair("BaiduUID", ""));
//		params.add(new BasicNameValuePair("UserName", ""));
//		params.add(new BasicNameValuePair("Phone", ""));
//		params.add(new BasicNameValuePair("app", "hdx"));
//		params.add(new BasicNameValuePair("channel", Utils.getChannel(mContext)));
//		params.add(new BasicNameValuePair("Coordinate", lo + "," + la));
//		httpCilents.postA(Config.ACCUPASS_REGDEVICE, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					MessageInfo messageInfo = JSON.parseObject(result.toString(), MessageInfo.class);
//					if (messageInfo.isRet()) {
//						application.sharedUtils.writeBoolean("isRegDevice", true);
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	class ChangeReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			final String action = intent.getAction();
////			if (action.equals(Config.WEBVIEW_HOME_ADD)) {
////				city = intent.getStringExtra("tag");
////				pageIndex = 1;
////				initTopPager();
//
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
