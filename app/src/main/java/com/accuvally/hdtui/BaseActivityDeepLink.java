package com.accuvally.hdtui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.activity.mine.setting.SettingActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.ui.MyProgressDialog;
import com.accuvally.hdtui.ui.TitleBar;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackActivityBase;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackActivityHelper;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackLayout;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.callback.LMLinkCreateListener;
import com.microquation.linkedme.android.callback.LMReferralCloseListener;
import com.microquation.linkedme.android.callback.LMSimpleInitListener;
import com.microquation.linkedme.android.indexing.LMUniversalObject;
import com.microquation.linkedme.android.referral.LMError;
import com.microquation.linkedme.android.util.LinkProperties;
import com.singulariti.deepshare.DeepShare;
import com.singulariti.deepshare.listeners.DSInappDataListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BaseActivityDeepLink extends FragmentActivity implements SwipeBackActivityBase {

	protected Context mContext;

	private BroadcastReceiver receiver;

	private IntentFilter filter;

	private ConnectivityManager connectivityManager;

	private NetworkInfo info;

	protected AccuApplication application;

	protected HttpCilents httpCilents;

	GestureDetector mGestureDetector;

	protected int onBackFlag = 0;

	protected MyProgressDialog myProgressDialog;

	protected DBManager dbManager;

	private TextView tvNoData;

	private Button SquareBtn;

	private ImageView ivFailure;

	private LinearLayout lyFailure;

	private SwipeBackActivityHelper mHelper;

	public ImageView activity_header_progressbar;

	AnimationDrawable animationDrawable;



    private LinkedME linkedME;
    private static final String TAG = "LinkedME-Demo";
    public static final String Channle = "BaseActivityDeepLink_Channle";
    /**
     * <p>解析深度链获取跳转参数，开发者自己实现参数相对应的页面内容</p>
     * <p>通过LinkProperties对象调用getControlParams方法获取自定义参数的HashMap对象,
     * 通过创建的自定义key获取相应的值,用于数据处理。</p>
     */
    //。linkProperties  lmUniversalObject都携带有数据
    LMSimpleInitListener simpleInitListener = new LMSimpleInitListener() {
        @Override
        public void onSimpleInitFinished(LMUniversalObject lmUniversalObject,
                                         LinkProperties linkProperties, LMError error) {
            try {
                Log.i(TAG, "开始处理deep linking数据... " + this.getClass().getSimpleName());
                if (error != null) {
                    Log.i("LinkedME-Demo", "LinkedME初始化失败. " + error.getMessage());
                } else {

                    //LinkedME SDK初始化成功，获取跳转参数，具体跳转参数在LinkProperties中，和创建深度链接时设置的参数相同；
                    Log.i("LinkedME-Demo", "LinkedME初始化完成");

                    if (linkProperties != null) {
                        Log.i("LinkedME-Demo", "Channel " + linkProperties.getChannel());
                        Log.i("LinkedME-Demo", "control params " + linkProperties.getControlParams());

                        //获取自定义参数封装成的hashmap对象
                        HashMap<String, String> hashMap = linkProperties.getControlParams();

                        //获取传入的参数
                        if(hashMap.containsKey("ActivityId")){
                            String ActivityId = hashMap.get("ActivityId");
                            String channel=linkProperties.getChannel();
                            Log.i("LinkedME-Demo", "ActivityId: " + ActivityId);
                            Log.i("LinkedME-Demo", "channel:" + channel);


                            switch (ActivityId){
                                case "1":
                                    Intent intent = new Intent(BaseActivityDeepLink.this, SettingActivity.class);
                                    intent.putExtra(BaseActivityDeepLink.Channle, channel);
                                    startActivity(intent);
                                    break;

                            }
                        }else {
                            Intent intent = new Intent(BaseActivityDeepLink.this, SettingActivity.class);
                            intent.putExtra(BaseActivityDeepLink.Channle, "来自 deepshare 222");
                            startActivity(intent);

                        }
                        }


                    if (lmUniversalObject != null) {
                        Log.i("LinkedME-Demo", "title " + lmUniversalObject.getTitle());
                        Log.i("LinkedME-Demo", "control " + linkProperties.getControlParams());
                        Log.i("LinkedME-Demo", "metadata " + lmUniversalObject.getMetadata());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    DSInappDataListener dsInappDataListener=new DSInappDataListener() {
        @Override
        public void onInappDataReturned(JSONObject params) {
            try {
                if (params == null) return;

                //获取传入的参数
                String ActivityId = (String) params.get("ActivityId");
                String channel=(String) params.get(BaseActivityDeepLink.Channle);
                Log.i("LinkedME-Demo", "ActivityId: " + ActivityId);
                Log.i("LinkedME-Demo", "channel:" + channel);

                switch (ActivityId){
                    case "1":
                        Intent intent = new Intent(BaseActivityDeepLink.this, SettingActivity.class);
                        intent.putExtra(BaseActivityDeepLink.Channle, channel);
                        startActivity(intent);
                        break;
                }

                /*Iterator<?> keys = params.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    params.getString(key);
//                    textViewParam.append("key:" + key + "; value:" + params.getString(key) + " ");
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(String s) {

        }
    };


    private void printLinkMeDevice(){
        String device_id = LinkedME.getInstance().getDeviceId();
        Log.e(TAG, "LinkedME device id:  " + device_id);
    }

    private void printLinkMeURL(){
        /**创建深度链接*/
        //深度链接属性设置
        final LinkProperties properties = new LinkProperties();
        //渠道
        properties.setChannel("LINKME");  //微信、微博、QQ
        //功能
        properties.setFeature("Share");
        //标签
        properties.addTag("LinkedME");
        properties.addTag("Demo");
        //阶段
        properties.setStage("Live");
        //自定义参数,用于在深度链接跳转后获取该数据
        properties.addControlParameter("ActivityId", "1");
        properties.addControlParameter("View", "Demo222");
        LMUniversalObject universalObject = new LMUniversalObject();
        universalObject.setTitle("aaaaa");

        // Async Link creation example
        universalObject.generateShortUrl(BaseActivityDeepLink.this, properties, new LMLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, LMError error) {
//                demo_edit.setText(url);
//                demo_link_view.setVisibility(View.VISIBLE);
                //获取自定义参数数据
//                demo_link_view.setText(properties.getControlParams().toString());
                Log.e(TAG, "LinkedME URL " + url);
            }
        });
    }




    //生成URL并打开
    private void printDeepShareURL(){
//        editShortUrl.setText("");
        // shareUrl should be filled with the destination url you want user to see when they click the button
        String shareUrl = "http://deepshare.io/deepshare-web-demo.html";
        // set the appid, you can find your appid in deepshare dashboard. URL:dashboard.deepshare.io
        String appId = "6f05fc8bb03de00e";

        // put key value pairs into JSONObject as inapp_data
        JSONObject dataToInclude = new JSONObject();
        try {

            dataToInclude.put("ActivityId", "1");
            dataToInclude.put(BaseActivityDeepLink.Channle, "DeepShare");

        } catch (JSONException ex) {
        }
        String inappData = dataToInclude.toString();

        // should pass appid, inapp_data, sender_id as parameters.
        String url = shareUrl + "?appid=" + appId + "&inapp_data=" +
                inappData + "&sender_id=" + DeepShare.getSenderId();

        Log.e(TAG, "DeepShare URL " + url);

        // using browser to open the url
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(url);
//        intent.setData(content_url);
//        startActivity(intent);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent: " + this.getClass().getSimpleName());
        simpleInitListener.reset();
        setIntent(intent);
    }



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		dbManager = new DBManager(this);
		MobclickAgent.updateOnlineConfig(mContext);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setDebugMode(false);
		httpCilents = new HttpCilents(this);
		filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new newWorkReceiver();
		application = (AccuApplication) getApplicationContext();
		application.addTask(this);
		registerReceiver(receiver, filter);
		// initGesture();
		init();
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		Log.i("info", application.currentActivity().getLocalClassName() + "");
	}

    @Override
    protected void onStart() {
        super.onStart();

        DeepShare.init(this, "6f05fc8bb03de00e", dsInappDataListener);

        Log.i(TAG, "onStart: " + this.getClass().getSimpleName());
        try {
            //如果消息未处理则会初始化initSession，因此不会每次都去处理数据，不会影响应用原有性能问题
            if (!LinkedME.getInstance().isHandleStatus()) {
                Log.i(TAG, "LinkedME +++++++ initSession... " + this.getClass().getSimpleName());
                //初始化LinkedME实例
                linkedME = LinkedME.getInstance();
                //初始化Session，获取Intent内容及跳转参数
                linkedME.initSession(simpleInitListener, this.getIntent().getData(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: " + this.getClass().getSimpleName());
        if (linkedME != null) {
            linkedME.closeSession(new LMReferralCloseListener() {
                @Override
                public void onCloseFinish() {
                }
            });
        }


        DeepShare.onStop();//停止DeepShare
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void initProgress() {
		activity_header_progressbar = (ImageView) findViewById(R.id.activity_header_progressbar);
		activity_header_progressbar.setBackgroundResource(R.drawable.loading);
	}

	public void startProgress() {
		activity_header_progressbar.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) activity_header_progressbar.getBackground();
		activity_header_progressbar.post(new Runnable() {
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
	}

	public void stopProgress() {
		activity_header_progressbar.setVisibility(View.GONE);
		if (animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
	}

	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {
		getSwipeBackLayout().scrollToFinishActivity();
	}

	/** 初始化自定义对话框 */
	private void init() {
		myProgressDialog = new MyProgressDialog(mContext);
		myProgressDialog.setMyCancelable(true);
		myProgressDialog.setMyTouchOutside(false);
	}


	public void showProgress(String message) {
		myProgressDialog.setMyMessage(message);
		myProgressDialog.myShow();
	}

	public void showDefaultProgress() {
		myProgressDialog.setMyMessage("数据加载中，请稍候！");
		myProgressDialog.myShow();
	}


	public void setMessage(String message) {
		myProgressDialog.setMyMessage(message);
	}

	public void dismissProgress() {
		if (myProgressDialog != null) {
			myProgressDialog.mydismiss();
		}
	}

	public void goneFailure() {
		lyFailure.setVisibility(View.GONE);
	}

	public void setViewFailure() {
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);
	}

	public void showFailure() {
		lyFailure.setVisibility(View.VISIBLE);
		ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
		tvNoData.setText(getResources().getString(R.string.loading_no_data));
		SquareBtn.setText(R.string.search_guangchang);
	}

	public interface OnClickCallBack {
		public void callBack(boolean is);
	}

	public void showFailureOnClick(final OnClickCallBack onClickCallBack) {
		SquareBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (SquareBtn.getText().toString().equals(getResources().getString(R.string.search_guangchang))) {
					onClickCallBack.callBack(true);
				} else {
					onClickCallBack.callBack(false);
				}
			}
		});
	}

	public void showWifi() {
		lyFailure.setVisibility(View.VISIBLE);
		ivFailure.setBackgroundResource(R.drawable.loading_no_wifi_bg);
		tvNoData.setText(getResources().getString(R.string.loading_no_wifi));
		SquareBtn.setText("点击重试");
	}

	public void setTitle(int title) {
		((TextView) findViewById(R.id.tvHeadTitle)).setText(getResources().getString(title));
	}

	public void setTitle(String title) {
		((TextView) findViewById(R.id.tvHeadTitle)).setText(title);
	}

	public void setTitleBar(String title) {
		TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
		titleBar.setTitle(title);
		titleBar.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void onBack(View view) {
		finish();
	}

	public void toActivity(Class<?> activity) {
		Intent intent = new Intent(mContext, activity);
		startActivity(intent);
	}

	public void toActivity(Intent intent) {
		startActivity(intent);
	}

	class newWorkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("info", "网络已连接" + name);
					application.leanCloudConnect();
				} else {
					Log.d("info", "网络连接断开");
//					application.showMsg(getResources().getString(R.string.network_check));
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			MobclickAgent.onPageEnd(getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {

        printLinkMeURL();
        printDeepShareURL();
        printLinkMeDevice();
		super.onResume();
		try {
			MobclickAgent.onPageStart(getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
