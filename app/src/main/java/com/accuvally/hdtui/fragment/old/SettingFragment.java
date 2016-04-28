package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.io.File;
//import java.lang.reflect.Field;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AboutAccuvallyActivity;
//import com.accuvally.hdtui.activity.FeedBackActivity;
//import com.accuvally.hdtui.activity.LoginActivityNew;
//import com.accuvally.hdtui.activity.RemindActivity;
//import com.accuvally.hdtui.utils.DataCleanManager;
//import com.accuvally.hdtui.utils.FileUtils;
//import com.accuvally.hdtui.utils.UpdateExampleConfig;
//import com.accuvally.hdtui.utils.Util;
//import com.accuvally.hdtui.utils.Utils;
//import com.umeng.update.UmengUpdateAgent;
//import com.umeng.update.UmengUpdateListener;
//import com.umeng.update.UpdateResponse;
//import com.umeng.update.UpdateStatus;
//
///**
// * 设置fragment
// * 
// * @author Semmer Wang
// * 
// */
//public class SettingFragment extends BaseFragment implements OnClickListener {
//
//	private TextView tvFeedBack;
//
//	private TextView tvCacheSize;
//
//	private LinearLayout lyClear;
//
//	private Button loginBtn;
//
//	private TextView tvAboutAccuvally;
//
//	private LinearLayout lyRemind;
//
//	private TextView tvRemind;
//
//	private TextView tvCheckVersionName, tvVersionName;
//
//	private LinearLayout lyVersion;
//
//	private View loginView_Line;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
//		initView(rootView);
//		initData();
//		return rootView;
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//	}
//
//	public void initView(View view) {
//		tvFeedBack = (TextView) view.findViewById(R.id.tvFeedBack);
//		lyClear = (LinearLayout) view.findViewById(R.id.lyClear);
//		tvCacheSize = (TextView) view.findViewById(R.id.tvCacheSize);
//		loginBtn = (Button) view.findViewById(R.id.loginBtn);
//		tvAboutAccuvally = (TextView) view.findViewById(R.id.tvAboutAccuvally);
//		lyRemind = (LinearLayout) view.findViewById(R.id.lyRemindss);
//		tvRemind = (TextView) view.findViewById(R.id.tvRemind);
//		tvCheckVersionName = (TextView) view.findViewById(R.id.tvCheckVersionName);
//		tvVersionName = (TextView) view.findViewById(R.id.tvVersionName);
//		lyVersion = (LinearLayout) view.findViewById(R.id.lyVersion);
//		loginView_Line = (View) view.findViewById(R.id.loginView_Line);
//
//		tvFeedBack.setOnClickListener(this);
//		lyClear.setOnClickListener(this);
//		loginBtn.setOnClickListener(this);
//		lyVersion.setOnClickListener(this);
//		lyRemind.setOnClickListener(this);
//		tvAboutAccuvally.setOnClickListener(this);
//		lyRemind.setOnClickListener(this);
//		tvAboutAccuvally.setOnClickListener(this);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (application.checkIsLogin()) {
//			loginBtn.setVisibility(View.VISIBLE);
//			loginView_Line.setVisibility(View.VISIBLE);
//		} else {
//			loginBtn.setVisibility(View.GONE);
//			loginView_Line.setVisibility(View.GONE);
//		}
//		if (application.sharedUtils.readInt("remind") == 1)
//			tvRemind.setText("当天提醒");
//		else if (application.sharedUtils.readInt("remind") == 2)
//			tvRemind.setText("提前一天提醒");
//		else if (application.sharedUtils.readInt("remind") == 3)
//			tvRemind.setText("提前2天提醒");
//		else if (application.sharedUtils.readInt("remind") == 4)
//			tvRemind.setText("不提醒");
//	}
//
//	public void initData() {
//		initCacheSize();
//		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//
//			@Override
//			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//				switch (updateStatus) {
//				case UpdateStatus.No: // has no
//					application.showMsg("你的版本是最新版");
//					break;
//				case UpdateStatus.NoneWifi: // none
//					application.showMsg("没有wifi连接， 只在wifi下更新");
//					break;
//				case UpdateStatus.Timeout: // time
//					application.showMsg("检测超时，请重试");
//					break;
//				}
//			}
//		});
//
//		UpdateExampleConfig.setUpdateListener(true);
//		try {
//			String versionName = Util.getVersionName(mContext);
//			tvCheckVersionName.setText(versionName);
//			tvVersionName.setText(versionName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void initCacheSize() {
//		// long cacheSize = 0;
//		long dataSize = 0;
//		long fileSize = 0;
//		long externalCacheSize = 0;
//		// File cache = mContext.getCacheDir();
//		File file = mContext.getFilesDir();
//		// File exter = mContext.getExternalCacheDir();
//		File data = new File("/data/data/" + mContext.getPackageName() + "/databases");
//
//		// cacheSize += FileUtils.getDirSize(cache);
//		dataSize += FileUtils.getDirSize(data);
//		fileSize += FileUtils.getDirSize(file);
//		// externalCacheSize += FileUtils.getDirSize(exter);
//
//		String size = FileUtils.formatFileSize(dataSize + fileSize);
//		if ((dataSize + fileSize + externalCacheSize) <= 0) {
//			tvCacheSize.setText("0KB");
//		} else {
//			tvCacheSize.setText(size);
//		}
//	}
//
//	Timer timer;
//	TimerTask task;
//
//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				dismissProgress();
//				DataCleanManager.cleanCustomCache("/data/data/" + mContext.getPackageName() + "/app_webview");
//				DataCleanManager.cleanCustomCache("/data/data/" + mContext.getPackageName() + "/app_webview/Cache");
//				DataCleanManager.cleanCustomCache("/data/data/" + mContext.getPackageName() + "/app_webview/Local Storage");
//				application.logout();
//				application.sharedUtils.delete("ticket");
//				loginBtn.setVisibility(View.GONE);
//				loginView_Line.setVisibility(View.GONE);
//				stopTimer();
//				break;
//			}
//			super.handleMessage(msg);
//		}
//	};
//
//	public void stopTimer() {
//		if (task != null) {
//			task.cancel();
//			task = null;
//		}
//		if (timer != null) {
//			timer.cancel();
//			timer.purge();
//			timer = null;
//		}
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.tvFeedBack:
//			toActivity(FeedBackActivity.class);
//			break;
//		case R.id.lyClear:
//			try {
//				DataCleanManager.cleanApplicationData(mContext);
//				tvCacheSize.setText("0KB");
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			break;
//		case R.id.loginBtn:
//			showProgress("正在注销");
//			if (timer == null)
//				timer = new Timer(true);
//			if (task == null) {
//				task = new TimerTask() {
//					public void run() {
//						Message message = new Message();
//						message.what = 1;
//						handler.sendMessage(message);
//					}
//				};
//			}
//			if (timer != null && task != null)
//				timer.schedule(task, 3000, 5000);
//			break;
//		case R.id.tvAboutAccuvally:// 关于活动行
//			if (Utils.isFastDoubleClick())
//				return;
//			toActivity(AboutAccuvallyActivity.class);
//			break;
//		case R.id.lyRemindss:
//			if (Utils.isFastDoubleClick())
//				return;
//			if (application.checkIsLogin()) {
//				toActivity(RemindActivity.class);
//			} else
//				toActivity(new Intent(mContext, LoginActivityNew.class));
//			break;
//		case R.id.lyVersion:
//			if (Utils.isFastDoubleClick())
//				return;
//			UmengUpdateAgent.forceUpdate(mContext);
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
//}
