package com.accuvally.hdtui.fragment.core;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.RobTicketActivity;
import com.accuvally.hdtui.activity.home.CalenderTypeActivity;
import com.accuvally.hdtui.activity.home.HomeTypeActivity;
import com.accuvally.hdtui.activity.home.ProjectDetailsActivity;
import com.accuvally.hdtui.adapter.BannerAdapter;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.config.CityConstant;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BannerInfo;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.HomeInfo;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.CirclePageIndicator;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.LocationUtils;
import com.accuvally.hdtui.utils.LocationUtils.LocatinCallBack;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

//布局ListView + header
//header：轮播图，四个选项(最新，热门...)，专题精选
// ListView 有两种layout item所以在Adpter里的getItemViewType返回不同的类型

public class HomeFragment extends BaseFragment implements OnClickListener {
	
	private static final int SCROLL_WHAT = 0;
	private static final int interval = 5000;//banner 滑动间隔时间

    public static final String TAG="HomeFragment";

	private int pageIndex = 0, pageSize = 31;
	
	private LocationUtils locationUtils;
	
	private Dialog locationDialog, changeDialog;

	private List<BannerInfo> bannerList = new ArrayList();//banner 广告条list

	private View llRobTicket;//抢票入口View
	private ImageView ivNewTopic;
	private TextView tvNewTopic;
	private BannerInfo newTopic;//最新专题
	
	private CirclePageIndicator circleIndicator;
	private ViewPager mViewPager;
	
	private XListView mListView;
	private CommonAccuAdapter mAdapter;//原来是HomeFraAdapter 2016.09.22
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		locationUtils = new LocationUtils(mContext);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		mListView = (XListView) view.findViewById(R.id.listview);

		View listHeader = inflater.inflate(R.layout.listheader_home, null);
		initView(listHeader);
		mListView.addHeaderView(listHeader);
		
		mListView.setPullLoadEnable(true);
		setupListView();
		
		initData();
		isRunningForeground();
		return view;
	}

	public void initView(View view) {
		
		circleIndicator = (CirclePageIndicator) view.findViewById(R.id.cicleIndicator);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		
		view.findViewById(R.id.llHomeTypeNews).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeHots).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeNear).setOnClickListener(this);
		view.findViewById(R.id.llHomeTypeCalender).setOnClickListener(this);
		
		llRobTicket = view.findViewById(R.id.llRobTicket);
		llRobTicket.setOnClickListener(this);
		
		view.findViewById(R.id.llNewTopic).setOnClickListener(this);// 最新专题点击监听
		ivNewTopic = (ImageView) view.findViewById(R.id.ivNewTopic);//最新专题图片
		tvNewTopic = (TextView) view.findViewById(R.id.tvNewTopic);//最新专题描述
	}
	
	private void setupListView() {
		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);
		
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				pageIndex = 0;
				initHomeData(application.sharedUtils.readString("cityName"));
			}
			
			@Override
			public void onLoadMore() {


                pullToLoadMore(application.sharedUtils.readString("cityName"));
//                mListView.setPullLoadEnable(false);
			}
		});
	}
	
	public void initData() {
		mListView.setFocusable(false);
		if (application.sharedUtils.readBoolean("isLocation") == false) {
			dialog_location();
		} else {
			if (application.cacheUtils.getAsString("home") != null) {
                String str=application.cacheUtils.getAsString("home");
//                Trace.e(TAG,str);
				HomeInfo homeInfo = JSON.parseObject(str, HomeInfo.class);
				setViewData(homeInfo);
			}
			initLocation();
		}
		regdevice(application.sharedUtils.readString("longitude"),
                application.sharedUtils.readString("latitude"));
	}
	
	public void isRunningForeground() {
		if (dbManager.getSaveBeHavior().size() > 0) {
			svaeBeHavior(dbManager.getSaveBeHavior());
		}
	}
	
	public void initHomeData(String city) {
		pageIndex = 0;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("city", city));
		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.HOME_URL, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						application.cacheUtils.put("home", response.result);
						HomeInfo homeInfo = JSON.parseObject(response.result, HomeInfo.class);
						setViewData(homeInfo);
					}
					locationUtils.stopListener();
//                    mListView.setPullLoadEnable(true);
					break;
				case Config.RESULT_CODE_ERROR:
//                    mListView.setPullLoadEnable(true);
					break;
				}
			}
		});
	}



    public void pullToLoadMore(String city) {
//        pageIndex = 0;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("city", city));

        pageIndex++;
        params.add(new BasicNameValuePair("pi", pageIndex + ""));
        Trace.e(TAG, "onLoadMore pageIndex" + pageIndex);

        params.add(new BasicNameValuePair("ps", pageSize + ""));
//        params.add(new BasicNameValuePair("refreshcache", true + ""));刷新缓存
        EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
        httpCilents.get(httpCilents.printURL(Url.search_recommend, params), new WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
                mListView.stopRefresh();
                mListView.stopLoadMore();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            List<SelInfo> list = JSON.parseArray(response.result, SelInfo.class);
                            Trace.e(TAG,"mAdapter.getCount() ="+mAdapter.getCount() + " " + "  pageIndex =" + pageIndex + "" + "  list.size()=" + list.size());

                            if (list.size() == 0) {
                                application.showMsg("亲，没有更多数据");
                            } else {
                                // 这种状态不太明确
                                mAdapter.addAll(list);
                            }
                            locationUtils.stopListener();
                        }
//                        mListView.setPullLoadEnable(true);
                        break;
                    case Config.RESULT_CODE_ERROR:
//                        mListView.setPullLoadEnable(true);
                        break;
                }
            }
        });
    }
	
	public void setViewData(HomeInfo homeInfo) {
//        mAdapter.setLists(homeInfo.getRecommends(), homeInfo.getPoporgs());
        mAdapter.setList(homeInfo.getRecommends());
		
		//顶部banner
		bannerList.clear();
		bannerList.addAll(homeInfo.getBanners());
		mViewPager.setAdapter(new BannerAdapter(mContext, bannerList));
		circleIndicator.setViewPager(mViewPager);
		sendScrollMessage(interval);//banner开始滚动
		
		//设置是否显示抢票入口
		if (homeInfo.isHasRushEvent()) {
			llRobTicket.setVisibility(View.VISIBLE);
		} else {
			llRobTicket.setVisibility(View.GONE);
		}
		
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



//    switch (msg.what) {
//        case 1:// 定位成功
//            locatinCallBack.callBack(1, (BDLocation) msg.obj);
//            break;
//        case 0:// 定位失败
//            locatinCallBack.callBack(0, null);
//            break;
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
					EventBus.getDefault().post(new ChangeCityEventBus("全国"));
					break;
				}
				locationUtils.stopListener();
			}
		});
	}

	protected boolean isRemindSwitchCity(String locationCity) {
		boolean sameCity = TextUtils.equals(locationCity, application.sharedUtils.readString("cityName"));
//        Trace.d(TAG,"application.sharedUtils.readString(cityName)="+application.sharedUtils.readString("cityName"));
//        Trace.d(TAG,"locationCity="+locationCity);
//        Trace.d(TAG,"sameCity="+sameCity);
//        sameCity一定是ture，则等价于 returned hasInCityList(locationCity)
		return !sameCity && hasInCityList(locationCity);
	}
	
	private boolean hasInCityList(String city) {
		List<String> hotCityList;
		String cityList = application.cacheUtils.getAsString(Keys.cityList);
		if (!TextUtils.isEmpty(cityList)) {
			hotCityList = JSON.parseArray(cityList, String.class);
		} else {
			hotCityList = new ArrayList<String>(Arrays.asList(CityConstant.hotCityName));
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
            Trace.d(TAG,"tvDialogRemove is click");
			locationDialog.dismiss();
			initLocation();
			break;
			
		case R.id.llHomeTypeNews:// 最新
			MobclickAgent.onEvent(mContext, "click_latest_count");
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_LAST", ""));
			startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 3));
			break;
		case R.id.llHomeTypeHots:// 热门
			MobclickAgent.onEvent(mContext, "click_hot_count");
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_HOT", ""));
			startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 2));
			break;
		case R.id.llHomeTypeNear:// 附近
			MobclickAgent.onEvent(mContext, "click_nearby_count");
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_NEAR", ""));
			if (application.sharedUtils.readString("longitude") != null)
				startActivity(new Intent(mContext, HomeTypeActivity.class).putExtra("type", 1));
			else if (application.sharedUtils.readBoolean("isLocation") == false) {
				dialog_location();
			}
			break;
		case R.id.llHomeTypeCalender:// 日历
			MobclickAgent.onEvent(mContext, "click_calendar_count");
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "Calendar", ""));
			startActivity(new Intent(mContext, CalenderTypeActivity.class).putExtra("type", 4));
			break;
			
		case R.id.llRobTicket:
			startActivity(new Intent(mContext, RobTicketActivity.class));
			break;
		case R.id.llNewTopic://最新专题  专题精选
			MobclickAgent.onEvent(mContext, "click_features_count");
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
        pageIndex = 0;
		initHomeData(eventBus.getCity());
	}
	
	private void sendScrollMessage(long delayTimeInMills) {
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeMessages(SCROLL_WHAT);
		mViewPager = null;
		EventBus.getDefault().unregister(this);
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

}