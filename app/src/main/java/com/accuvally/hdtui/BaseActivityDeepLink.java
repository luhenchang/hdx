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

import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.ui.MyProgressDialog;
import com.accuvally.hdtui.ui.TitleBar;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackActivityBase;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackActivityHelper;
import com.accuvally.hdtui.utils.swipebacklayout.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

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


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);//linkme
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
    }

    @Override
    public void onStop() {
        super.onStop();
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

//        printLinkMeURL();
//        printLinkMeDevice();
		super.onResume();
		try {
			MobclickAgent.onPageStart(getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
