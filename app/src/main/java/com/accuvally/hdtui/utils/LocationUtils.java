package com.accuvally.hdtui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.accuvally.hdtui.AccuApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationUtils {

    public static final String TAG="LocationUtils";

	Context mContext;

	AccuApplication application;

	LocationClient mLocationClient = null;

	LocatinCallBack locatinCallBack;

	public LocationUtils(Context context) {
		this.mContext = context;
		application = (AccuApplication) mContext.getApplicationContext();
	}

    //1.MainActivityNew onCreateView()调用了   2.点击(是否开启定位)小助手对话框中的是
	public void location(final LocatinCallBack locatinCallBack) {

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
                stopListener();//change by 123
				switch (msg.what) {
				case 1:// 定位成功
                    Trace.d(TAG,"定位成功");
					locatinCallBack.callBack(1, (BDLocation) msg.obj);
					break;
				case 0:// 定位失败
                    Trace.d(TAG,"定位失败");
					locatinCallBack.callBack(0, null);
					break;
				}
			};
		};
        Trace.d(TAG,"location()");
		mLocationClient = new LocationClient(mContext);
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				Message msg = new Message();
				try {
					if (location != null && location.getCity() != null) {
						msg.what = 1;
						msg.obj = location;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					msg.what = 0;
					handler.sendMessage(msg);
				}
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {

			}
		});
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.disableCache(false);
		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	public interface LocatinCallBack {
		public void callBack(int code, BDLocation location);
	}
//homefragment调用：1.成功调用LocatinCallBack会最后调用stopListener()
	public void stopListener() {
        Trace.d(TAG,"stopListener");
		try {
                Trace.d(TAG,"stopListener");
			mLocationClient.stop();// 关闭定位SDK
			mLocationClient = null;
		} catch (Exception e) {
		}
	}
}
