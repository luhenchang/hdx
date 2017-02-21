package com.accuvally.hdtui.activity.home.util;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.LocationAdapter;
import com.accuvally.hdtui.ui.ScrolListView;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.LocationUtils;
import com.accuvally.hdtui.utils.LocationUtils.LocatinCallBack;
import com.accuvally.hdtui.utils.MapUtils;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

//import com.baidu.location.BDLocation;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviParaOption;
//import com.baidu.mapapi.search.core.SearchResult;
//import com.baidu.mapapi.search.geocode.GeoCodeOption;
//import com.baidu.mapapi.search.geocode.GeoCodeResult;
//import com.baidu.mapapi.search.geocode.GeoCoder;
//import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
//import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

//import com.baidu.location.BDLocation;

//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviPara;
//import com.baidu.mapapi.search.core.SearchResult;
//import com.baidu.mapapi.search.geocode.GeoCodeOption;
//import com.baidu.mapapi.search.geocode.GeoCodeResult;
//import com.baidu.mapapi.search.geocode.GeoCoder;
//import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
//import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapsActivity extends BaseActivity implements OnGetGeoCoderResultListener, OnClickListener {

    private MapView mMapView;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;

    private Marker mMarker;
    BitmapDescriptor bdA;

    public static View popView;
    private InfoWindow mInfoWindow;

    private String city;
    private String address;
    double daodaLon, daodaLat;
//    String title;
    private List<HashMap<String, Object>> list;
    LocationAdapter adapter;
    double latitude;
    double longitude;
    CheckBox checkBox;
    View view;
    OverlayOptions ooA;
    private LocationUtils locationUtils;
    private Dialog locationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        initData();
        SearchButtonProcess();
    }

    public void initView() {
        locationUtils = new LocationUtils(mContext);
        city = getIntent().getStringExtra("city");
        address = getIntent().getStringExtra("address");
//        title = getIntent().getStringExtra("title");
        setTitle("地图");
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                try {
                    initLocation(80);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });
    }

    public void initData() {
        if (application.sharedUtils.readString("latitude") == null) {
            dialog_location();
            return;
        }
        latitude = Double.parseDouble(application.sharedUtils.readString("latitude"));
        longitude = Double.parseDouble(application.sharedUtils.readString("longitude"));
        list = new ArrayList<HashMap<String, Object>>();
        adapter = new LocationAdapter(mContext, handler);
        adapter.setList(list);
        if (MapUtils.isAppInstalled(mContext, "com.baidu.BaiduMap")) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("mapId", 1);
            map.put("mapName", "百度地图");
            list.add(map);
        }
        if (MapUtils.isAppInstalled(mContext, "com.autonavi.minimap")) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("mapId", 2);
            map.put("mapName", "高德地图");
            list.add(map);
        }
        if (application.sharedUtils.readString("latitude") == null) {
            dialog_location();
            return;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    startNavi(latitude, longitude);
                    if (checkBox.isChecked()) {
                        application.sharedUtils.writeInt("isBaidu", 1);
                    }
                    dialog.dismiss();
                    break;
                case 2:
                    MapUtils.onGaodeClick("我的位置", address, application, mContext, longitude, latitude, daodaLon, daodaLat);
                    if (checkBox.isChecked()) {
                        application.sharedUtils.writeInt("isBaidu", 2);
                    }
                    dialog.dismiss();
                    break;
            }
        };
    };

    /**
     * 发起搜索
     *
     * @param
     */
    public void SearchButtonProcess() {
        try {
            mSearch.geocode(new GeoCodeOption().city(city).address(address));
        } catch (Exception e) {
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        try {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapsActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
                return;
            }
            mBaiduMap.clear();
            mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.img_global_butten_map)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
            float mZoomLevel = 13.0f;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel));
            daodaLat = result.getLocation().latitude;
            daodaLon = result.getLocation().longitude;
            if (bdA != null) {
                bdA.recycle();
            }
            bdA = BitmapDescriptorFactory.fromResource(R.drawable.img_global_butten_map);
            try {
                initLocation(40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    public void initLocation(int num) {
        LatLng llA = new LatLng(daodaLat, daodaLon);
        ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(9);
        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        view = LayoutInflater.from(mContext).inflate(R.layout.listitem_map, null);
        TextView textView = (TextView) view.findViewById(R.id.tvLocation);
        textView.setText(address);
        final LatLng ll = mMarker.getPosition();
        Point p = mBaiduMap.getProjection().toScreenLocation(ll);
        p.y -= num;
        LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), llInfo, 10,listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick() {

            if (application.sharedUtils.readString("latitude") == null) {
                dialog_location();
                return;
            }
            if (application.sharedUtils.readInt("isBaidu") == 1) {
                if (MapUtils.isAppInstalled(mContext, "com.baidu.BaiduMap")) {
                    startNavi(latitude, longitude);
                } else if (list.size() == 1) {
                    if (Integer.parseInt(list.get(0).get("mapId").toString()) == 1) {
                        startNavi(latitude, longitude);
                    } else if (Integer.parseInt(list.get(0).get("mapId").toString()) == 2) {
                        MapUtils.onGaodeClick("我的位置", address, application, mContext, longitude, latitude, daodaLon, daodaLat);
                    }
                } else {
                    if (list.size() == 0) {
                        application.showMsg("亲，木有发现地图，无法导航！");
                    } else {
                        dialog();
                    }
                }
            } else if (application.sharedUtils.readInt("isBaidu") == 2) {
                if (MapUtils.isAppInstalled(mContext, "com.autonavi.minimap")) {
                    MapUtils.onGaodeClick("我的位置", address, application, mContext, longitude, latitude, daodaLon, daodaLat);
                } else if (list.size() == 1) {
                    if (Integer.parseInt(list.get(0).get("mapId").toString()) == 1) {
                        startNavi(latitude, longitude);
                    } else if (Integer.parseInt(list.get(0).get("mapId").toString()) == 2) {
                        MapUtils.onGaodeClick("我的位置", address, application, mContext, longitude, latitude, daodaLon, daodaLat);
                    }
                } else {
                    if (list.size() == 0) {
                        application.showMsg("亲，木有发现地图，无法导航！");
                    } else {
                        dialog();
                    }
                }
            } else {
                if (list.size() == 0) {
                    application.showMsg("亲，木有发现地图，无法导航！");
                } else if (list.size() == 1) {
                    if (Integer.parseInt(list.get(0).get("mapId").toString()) == 1) {
                        startNavi(latitude, longitude);
                    } else if (Integer.parseInt(list.get(0).get("mapId").toString()) == 2) {
                        MapUtils.onGaodeClick("我的位置", address, application, mContext, longitude, latitude, daodaLon, daodaLat);
                    }
                } else {
                    dialog();
                }
            }
        }
    };


    //    地理编码/反地理编码结果
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        try {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapsActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            }
            mBaiduMap.clear();
            mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.img_global_butten_map)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
            Toast.makeText(MapsActivity.this, result.getAddress(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }
        mSearch.destroy();
        super.onDestroy();
        try {
            bdA.recycle();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Dialog dialog;

    public void dialog() {
        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_location_map);
        ScrolListView listview = (ScrolListView) dialog.findViewById(R.id.location_listview);
        checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        listview.setAdapter(adapter);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        });
        DialogUtils.dialogSet(dialog, mContext, Gravity.CENTER, 0.95, 1.0, true, false, true);
        dialog.show();
    }

    public void startNavi(double latitude, double longitude) {
        if (application.sharedUtils.readString("latitude") == null) {
            dialog_location();
            return;
        }
        application.sharedUtils.writeInt("locationNum", 1);
        Log.i("info", "--------" + latitude + ",,,," + longitude);
        Log.i("info", "--------" + daodaLat + ",,,," + daodaLon);
        LatLng pt1 = new LatLng(latitude, longitude);
        LatLng pt2 = new LatLng(daodaLat, daodaLon);
        NaviParaOption para = new NaviParaOption();
        para.startPoint(pt1);
        para.startName("我的位置");

        para.endPoint(pt2)  ;
        para.endName(address) ;
//        NaviParaOption para = new NaviPara();
//        para.startPoint = pt1;
//        para.startName = "我的位置";
//        para.endPoint = pt2;
//        para.endName = address;
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, MapsActivity.this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            application.showMsg("您未安装百度地图");
        }
    }

    public void dialog_location() {
        locationDialog = new Dialog(mContext, R.style.DefaultDialog);
        locationDialog.setCancelable(false);
        locationDialog.setCanceledOnTouchOutside(false);
        locationDialog.setContentView(R.layout.dialog_collect);
        ((TextView) locationDialog.findViewById(R.id.title)).setText("亲~");
        ((TextView) locationDialog.findViewById(R.id.message)).setText("活动行小助手要使用您的地理位置，是否允许");
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
                        shareLocation(location);
                        break;
                    case 0:
                        application.sharedUtils.writeString("cityName", "北京");
                        break;
                }
                application.sharedUtils.writeBoolean("isLocation", true);
                locationUtils.stopListener();
                EventBus.getDefault().post(new ChangeCityEventBus(application.sharedUtils.readString("cityName")));
            }
        });

    }

    public void shareLocation(BDLocation location) {
        Log.i("info", "location.getCity():" + location.getCity());
        application.showMsg("定位成功，您可以使用导航");
        application.sharedUtils.writeString("longitude", location.getLongitude() + "");
        application.sharedUtils.writeString("latitude", location.getLatitude() + "");
        application.sharedUtils.writeString("cityName", location.getCity().replace("市", ""));
        application.sharedUtils.writeString("addrStr", location.getAddrStr());
        application.sharedUtils.writeString("province", location.getProvince());
        latitude = Double.parseDouble(application.sharedUtils.readString("latitude"));
        longitude = Double.parseDouble(application.sharedUtils.readString("longitude"));
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tvDialogMistake:
                locationDialog.dismiss();
                application.sharedUtils.writeBoolean("isLocation", false);
                break;
            case R.id.tvDialogRemove:
                locationDialog.dismiss();
                initLocation();
                break;
        }
    }

}
