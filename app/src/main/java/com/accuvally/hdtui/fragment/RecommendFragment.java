package com.accuvally.hdtui.fragment;

public class RecommendFragment
        /*extends BaseFragment implements OnClickListener*/ {
	
	/*private static final int SCROLL_WHAT = 0;
	private static final int interval = 5000;//banner 滑动间隔时间

	private LocationUtils locationUtils;

	private Dialog locationDialog, changeDialog;

	private OverScrollView scrollview;

	private ScrolListView mListView;

	private HomeInfo homeInfo;

	private List<BannerInfo> bannerList;//banner 广告条list

	private View llRobTicket;//抢票入口View

	private ImageView ivNewTopic;

	private TextView tvNewTopic;

	private BannerInfo newTopic;//最新专题

//	private GridView mGridView;
	
	private CommonAdapter mAdapter;
//	private CommonAdapter gridAdapter;

	private CirclePageIndicator circleIndicator;
	private ViewPager mViewPager;
	private View rootView;
	private View recommendView;//精彩推荐
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
		EventBus.getDefault().register(this);
		initView(rootView);
		initData();
		isRunningForeground();
		return rootView;
	}
	
	public void initView(View view) {
		locationUtils = new LocationUtils(mContext);
		
		recommendView = rootView.findViewById(R.id.include_recommend);//精彩推荐
		circleIndicator = (CirclePageIndicator) view.findViewById(R.id.cicleIndicator);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		
		view.findViewById(R.id.llHomeTypeNews).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeHots).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeNear).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeCalender).setOnClickListener(this);
		
		llRobTicket = view.findViewById(R.id.llRobTicket);
		llRobTicket.setOnClickListener(this);
		
		scrollview = (OverScrollView) view.findViewById(R.id.scrollview);
		scrollview.setOverScrollListener(new OverScrollListener() {
			
			@Override
			public void headerScroll() {
				pageIndex = 0;
				initHomeData(application.sharedUtils.readString("cityName"));
			}
			
			@Override
			public void footerScroll() {
				pageIndex++;
				requestRecommendData();
			}
		});
		
		view.findViewById(R.id.llNewTopic).setOnClickListener(this);// 最新专题点击监听
		ivNewTopic = (ImageView) view.findViewById(R.id.ivNewTopic);//最新专题图片
		tvNewTopic = (TextView) view.findViewById(R.id.tvNewTopic);//最新专题描述

//		mGridView = (GridView) view.findViewById(R.id.grid);
//		setupGridView();

		mListView = (ScrolListView) view.findViewById(R.id.listview);
		setupListView();
	}

//	private void setupGridView() {
//		mGridView.setAdapter(gridAdapter = new CommonAdapter<BannerInfo>(mContext, R.layout.griditem_home) {
//
//			@Override
//			public void convert(ViewHolder viewHolder, final BannerInfo item, int position) {
//				viewHolder.setText(R.id.tvHandpickTitle, item.getTitle());
//				viewHolder.setText(R.id.tvHandpickContent, item.getContent());
//				viewHolder.setImageUrl(R.id.ivHandpickLogo, item.getLogo());
//				
//				try {
//					TextView tvHandpickTitle = viewHolder.getView(R.id.tvHandpickTitle);
//					tvHandpickTitle.setTextColor(Color.parseColor(item.getTitleColor()));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						if (Utils.isFastDoubleClick()) {
//							return;
//						}
//						if (item.isResultForSearch()) {
//							dbManager.insertSaveBeHavior(application.addBeHavior(80, 0 + "", "", "", item.getParams(), "", ""));
//							dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_TOPIC", item.getParams()));
//							Intent intent = new Intent(mContext, HomeTagActivity.class);
//							mContext.startActivity(intent.putExtra("tag", item.getParams()).putExtra("titleName", item.getTitle()));
//						} else {
//							if (item.isOpenInWeb()) {
//								Intent intent = new Intent(mContext, AccuvallyWebDetailsActivity.class);
//								mContext.startActivity(intent.putExtra("loadingUrl", item.getUrl()).putExtra("injectJs", ""));
//							} else {
//								mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", item.getId()));
//							}
//						}
//					}
//				});
//			}
//		});
//	}

	private void setupListView() {
		mListView.setAdapter(mAdapter = new CommonAdapter<SelInfo>(mContext, R.layout.listitem_home_recommend) {

			@Override
			public void convert(ViewHolder viewHolder, final SelInfo item, int position) {
				viewHolder.setImageUrl(R.id.ivItemRecommendImg, item.getLogo());
				
				viewHolder.setText(R.id.tvItemTitle, item.getTitle());
				viewHolder.setText(R.id.tvItemAddress, item.getAddress());
				viewHolder.setText(R.id.tvItemTime, item.getTimeStr());
				viewHolder.setText(R.id.tvItemVisitNum, item.getVisitNum());
				
				TextView tvItemPriceArea = viewHolder.getView(R.id.tvItemPriceArea);
				tvItemPriceArea.setText(item.getPriceArea());
				if ("免费".equals(item.getPriceArea())) {
					tvItemPriceArea.setTextColor(0xffffae00);
				} else {
					tvItemPriceArea.setTextColor(0xff61b754);
				}
				
				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dbManager.insertSaveBeHavior(application.addBeHavior(100, "0",  item.getId(), "", "", "APP_RECOMMEND", item.getId()));
						Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
						intent.putExtra("id", item.getId());
						intent.putExtra("isHuodong", item.getSourceType());// 1 活动推 0 活动行
						startActivity(intent);
					}
				});
			}
		});
	}

	public void initData() {
		mListView.setFocusable(false);
		if (application.sharedUtils.readBoolean("isLocation") == false) {
			dialog_location();
		} else {
			if (application.cacheUtils.getAsString("home") != null) {
				setViewData(application.cacheUtils.getAsString("home"));
			}
			initLocation();
		}
		regdevice(application.sharedUtils.readString("longitude"), application.sharedUtils.readString("latitude"));
	}
	
	public void isRunningForeground() {
		if (dbManager.getSaveBeHavior().size() > 0) {
			svaeBeHavior(dbManager.getSaveBeHavior());
		}
	}
	
	public void initHomeData(String city) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("city", city));
		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.HOME_URL, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msgInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msgInfo.isSuccess()) {
						application.cacheUtils.put("home", msgInfo.getResult());
						setViewData(msgInfo.getResult());
					}
					locationUtils.stopListener();
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					if (application.cacheUtils.getAsString("home") != null) {
						setViewData(application.cacheUtils.getAsString("home"));
					}
					break;
				}
			}
		});
	}
	
	private int pageIndex = 0, pageSize = 30;

	private void requestRecommendData() {
		String city = application.sharedUtils.readString("cityName");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("city", city));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.search_recommend, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<SelInfo> list = JSON.parseArray(response.result, SelInfo.class);
						mAdapter.addAll(list);
					}
				}
			}
		});
	}
	
	public void setViewData(String result) {
		scrollview.postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollview.scrollTo(0, 0);
			}
		}, 300);
		homeInfo = JSON.parseObject(result, HomeInfo.class);
		
		//设置是否显示抢票入口
		if (homeInfo.isHasRushEvent()) {
			llRobTicket.setVisibility(View.VISIBLE);
		} else {
			llRobTicket.setVisibility(View.GONE);
		}
		
		//listview 精彩推荐活动
		if (homeInfo.getRecommends() == null || homeInfo.getRecommends().isEmpty()) {
			recommendView.setVisibility(View.GONE);
		} else {
			recommendView.setVisibility(View.VISIBLE);
			mAdapter.removeAll();
			mAdapter.addAll(homeInfo.getRecommends());
		}
		
		//4个精选主题
//		gridAdapter.removeAll();
//		gridAdapter.addAll(homeInfo.getCombos());
		
		//顶部banner
		bannerList = new ArrayList<BannerInfo>();
		bannerList.addAll(homeInfo.getBanners());
		mViewPager.setAdapter(new ChannelAdsAdapter());
		circleIndicator.setViewPager(mViewPager);
		sendScrollMessage(interval);//banner开始滚动
		
		//专题精选
		List<BannerInfo> topics = homeInfo.getTopics();
		if (topics == null || topics.isEmpty()) return;
		newTopic = topics.get(0);
		tvNewTopic.setText(newTopic.getTitle());
		ImageLoader.getInstance().displayImage(newTopic.getLogo(), ivNewTopic);
	}
	
	public void dialog_location() {
		locationDialog = new Dialog(mContext, R.style.DefaultDialog);
		locationDialog.setCancelable(false);
		locationDialog.setCanceledOnTouchOutside(false);
		locationDialog.setContentView(R.layout.dialog_collect);
		((TextView) locationDialog.findViewById(R.id.title)).setText("亲~");
		((TextView) locationDialog.findViewById(R.id.message)).setText("活动行小帮手要使用您的地理位置，是否允许");
		((TextView) locationDialog.findViewById(R.id.tvDialogMistake)).setText("否");
		((TextView) locationDialog.findViewById(R.id.tvDialogRemove)).setText("是");

		locationDialog.findViewById(R.id.tvDialogMistake).setOnClickListener(this);
		locationDialog.findViewById(R.id.tvDialogRemove).setOnClickListener(this);
		locationDialog.show();
	}
	
	public void initLocation() {
		if (!NetworkUtils.isNetworkAvailable(mContext)) {
			application.showMsg(R.string.network_check);
			return;
		}
		locationUtils.location(new LocatinCallBack() {

			@Override
			public void callBack(int code, BDLocation location) {
				switch (code) {
				case 1:
					String locationCity = location.getCity().replace("市", "");
					application.sharedUtils.writeString(Keys.locationCity, locationCity);

					if (isRemindSwitchCity(locationCity)) {
						changeLocationDialog(location);
					} else {
						saveLocationInfo(location);
					}
					break;
				case 0:
//					String city = application.sharedUtils.readString("cityName");
//					if (city == null) {
//						city = "全国";
//						application.sharedUtils.writeString("cityName", city);
//					}
					EventBus.getDefault().post(new ChangeCityEventBus("全国"));
					break;
				}
				locationUtils.stopListener();
			}
		});
	}
	
	*//**
	 * 当用户打开app时，进行判断，满足以下全部条件时，触发切换城市提醒：
		1.当前城市与上次设置城市时的所在城市不符
		2.当前城市并不是上次所选城市
		3.当前城市在“城市列表”里
	 *//*
	protected boolean isRemindSwitchCity(String locationCity) {
		boolean sameCity = TextUtils.equals(locationCity, application.sharedUtils.readString("cityName"));
		
		return !sameCity && hasInCityList(locationCity);
	}
	
	private boolean hasInCityList(String city) {
		List<String> hotCityList;
		String cityList = application.cacheUtils.getAsString(Keys.cityList);
		if (!TextUtils.isEmpty(cityList)) {
			hotCityList = JSON.parseArray(cityList, String.class);
		} else {
			hotCityList = new ArrayList<String>(Arrays.asList(Constant.hotCityName));
		}
		for (int i = 0; i < hotCityList.size(); i++) {
			if (city.equals(hotCityList.get(i)))
				return true;
		}
		return false;
	}

	
	public void changeLocationDialog(final BDLocation location) {
		changeDialog = new Dialog(mContext, R.style.DefaultDialog);
		changeDialog.setCancelable(false);
		changeDialog.setCanceledOnTouchOutside(false);
		changeDialog.setContentView(R.layout.dialog_collect);
		((TextView) changeDialog.findViewById(R.id.title)).setText("亲~");
		((TextView) changeDialog.findViewById(R.id.message)).setText("活动行小帮手检查到您的位置在" + location.getCity().replace("市", "") + "，是否切换到当前城市？");
		((TextView) changeDialog.findViewById(R.id.tvDialogMistake)).setText("否");
		((TextView) changeDialog.findViewById(R.id.tvDialogRemove)).setText("是");

		changeDialog.findViewById(R.id.tvDialogMistake).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				initHomeData(application.sharedUtils.readString("cityName"));
				EventBus.getDefault().post(new ChangeCityEventBus(application.sharedUtils.readString("cityName")));
				changeDialog.dismiss();
			}
		});
		changeDialog.findViewById(R.id.tvDialogRemove).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveLocationInfo(location);
				changeDialog.dismiss();
			}
		});
		changeDialog.show();
	}
	
	public void saveLocationInfo(BDLocation location) {
		Log.i("info", "location.getCity():" + location.getCity());
		application.sharedUtils.writeString("longitude", location.getLongitude() + "");
		application.sharedUtils.writeString("latitude", location.getLatitude() + "");
		application.sharedUtils.writeString("cityName", location.getCity().replace("市", ""));
		application.sharedUtils.writeString("addrStr", location.getAddrStr());
		application.sharedUtils.writeString("province", location.getProvince());

		application.sharedUtils.writeBoolean("isLocation", true);
		EventBus.getDefault().post(new ChangeCityEventBus(application.sharedUtils.readString("cityName")));
	}

	@Override
	public void onClick(View v) {
		if (Utils.isFastDoubleClick())
			return;
		
		switch (v.getId()) {
		case R.id.tvDialogMistake:
			locationDialog.dismiss();
			application.sharedUtils.writeBoolean("isLocation", false);
			EventBus.getDefault().post(new ChangeCityEventBus("全国"));
			break;
		case R.id.tvDialogRemove:
			locationDialog.dismiss();
			initLocation();
			break;
			
		case R.id.llHomeTypeNews:// 最新
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_LAST", ""));
			startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 3));
			break;
		case R.id.llHomeTypeHots:// 热门
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_HOT", ""));
			startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 2));
			break;
		case R.id.llHomeTypeNear:// 附近
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_NEAR", ""));
			if (application.sharedUtils.readString("longitude") != null)
				startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 1));
			else if (application.sharedUtils.readBoolean("isLocation") == false) {
				dialog_location();
			}
			break;
		case R.id.llHomeTypeCalender:// 日历
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "Calendar", ""));
			startActivity(new Intent(mContext, CalenderTypeActivity.class).putExtra("type", 4));
			break;
			
		case R.id.llRobTicket:
			startActivity(new Intent(mContext, RobTicketActivity.class));
			break;
		case R.id.llNewTopic://最新专题
			if (newTopic != null) {
				dbManager.insertSaveBeHavior(application.addBeHavior(100, "3", newTopic.getId(), "", "", "APP_SPECIAL", newTopic.getUrl()));
				Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
				intent.putExtra("title", newTopic.getTitle());
				intent.putExtra("id", newTopic.getId());
				startActivity(intent);
			}
			break;
		}
	}
	
	public void onEventMainThread(ChangeCityEventBus eventBus) {
		initHomeData(eventBus.getCity());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeMessages(SCROLL_WHAT);
		mViewPager = null;
		EventBus.getDefault().unregister(this);
	}

	private void sendScrollMessage(long delayTimeInMills) {
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int count = bannerList.size();
			int currentIndex = mViewPager.getCurrentItem();
			if (count > 1) {
				currentIndex = (currentIndex + 1) % count;
				mViewPager.setCurrentItem(currentIndex);
				sendScrollMessage(interval);
			}
		};
	};
	
	private class ChannelAdsAdapter extends PagerAdapter {
		private DisplayImageOptions options;

		ChannelAdsAdapter() {
			options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_details_image)
					.showImageForEmptyUri(R.drawable.default_details_image).showImageOnFail(R.drawable.default_details_image)
					.cacheInMemory(true).cacheOnDisk(true).build();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return bannerList == null ? 0 : bannerList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View layoutview = LayoutInflater.from(mContext).inflate(R.layout.listitem_square_top_list, null);
			ImageView image = (ImageView) layoutview.findViewById(R.id.iv_image);
			final BannerInfo info = bannerList.get(position);
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			application.mImageLoader.displayImage(info.getLogo(), image, options);
			((ViewPager) view).addView(layoutview, 0);
			layoutview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View positioin) {
					if (Utils.isFastDoubleClick()) {
						return;
					}
					dbManager.insertSaveBeHavior(application.addBeHavior(10, 0 + "", info.getId(), "", "", "", ""));
					if (info.isOpenInWeb()) {
						dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_BANNER", info.getUrl()));
						startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl", info.getUrl()).putExtra("injectJs", ""));
					} else {
						dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_BANNER", info.getId()));
						startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("isHuodong", 0).putExtra("id", info.getId()));
					}
				}
			});
			return layoutview;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
	}
*/
}
