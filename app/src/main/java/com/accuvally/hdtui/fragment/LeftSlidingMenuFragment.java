package com.accuvally.hdtui.fragment;//package com.accuvally.hdtui.fragment;
//
//import java.io.File;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AboutAccuvallyActivity;
//import com.accuvally.hdtui.activity.FeedBackActivity;
//import com.accuvally.hdtui.activity.LoginActivityNew;
//import com.accuvally.hdtui.activity.PersonalActivity;
//import com.accuvally.hdtui.activity.RecommendActivity;
//import com.accuvally.hdtui.activity.RemindActivity;
//import com.accuvally.hdtui.activity.UpdateCityActivity;
//import com.accuvally.hdtui.ui.CircleImageView;
//import com.accuvally.hdtui.utils.DataCleanManager;
//import com.accuvally.hdtui.utils.FileUtils;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.UpdateExampleConfig;
//import com.accuvally.hdtui.utils.Util;
//import com.accuvally.hdtui.utils.Utils;
//import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
//import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.im.v2.AVIMClient;
//import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
//import com.umeng.update.UmengUpdateAgent;
//import com.umeng.update.UmengUpdateListener;
//import com.umeng.update.UpdateResponse;
//import com.umeng.update.UpdateStatus;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 侧边设置
// * 
// * @author Semmer Wang
// * 
// */
//public class LeftSlidingMenuFragment extends BaseFragment implements OnClickListener {
//
//	private TextView tvHomeLeftUserName, tvHomeLeftNickName, tvVersionName, tvHomeLeftCity, tvHomeLeftSize;
//
//	private TextView tvHomeLeftLogin, tvHomeLeftFeedBack, tvHomeLeftAbout, tvHomeLeftRemind, tvHomeLeftRecommend;
//
//	private LinearLayout lyVersion, lyHomeLeftCity, lyHomeLeftClean, lyHomeLeftRemind;
//
//	private CircleImageView ivHomeLeftLogo;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.main_left_layout, container, false);
//		EventBus.getDefault().register(this);
//		initView(view);
//		initData();
//		return view;
//	}
//
//	public void initView(View view) {
//		tvHomeLeftUserName = (TextView) view.findViewById(R.id.tvHomeLeftUserName);
//		tvHomeLeftNickName = (TextView) view.findViewById(R.id.tvHomeLeftNickName);
//		tvVersionName = (TextView) view.findViewById(R.id.tvVersionName);
//		tvHomeLeftCity = (TextView) view.findViewById(R.id.tvHomeLeftCity);
//		tvHomeLeftSize = (TextView) view.findViewById(R.id.tvHomeLeftSize);
//		ivHomeLeftLogo = (CircleImageView) view.findViewById(R.id.ivHomeLeftLogo);
//		tvHomeLeftLogin = (TextView) view.findViewById(R.id.tvHomeLeftLogin);
//		tvHomeLeftFeedBack = (TextView) view.findViewById(R.id.tvHomeLeftFeedBack);
//		tvHomeLeftAbout = (TextView) view.findViewById(R.id.tvHomeLeftAbout);
//		lyHomeLeftRemind = (LinearLayout) view.findViewById(R.id.lyHomeLeftRemind);
//		tvHomeLeftRemind = (TextView) view.findViewById(R.id.tvHomeLeftRemind);
//		tvHomeLeftRecommend = (TextView) view.findViewById(R.id.tvHomeLeftRecommend);
//
//		lyVersion = (LinearLayout) view.findViewById(R.id.lyVersion);
//		lyHomeLeftCity = (LinearLayout) view.findViewById(R.id.lyHomeLeftCity);
//		lyHomeLeftClean = (LinearLayout) view.findViewById(R.id.lyHomeLeftClean);
//
//		lyVersion.setOnClickListener(this);
//		lyHomeLeftCity.setOnClickListener(this);
//		lyHomeLeftClean.setOnClickListener(this);
//		ivHomeLeftLogo.setOnClickListener(this);
//		tvHomeLeftLogin.setOnClickListener(this);
//		tvHomeLeftFeedBack.setOnClickListener(this);
//		tvHomeLeftAbout.setOnClickListener(this);
//		lyHomeLeftRemind.setOnClickListener(this);
//		tvHomeLeftRecommend.setOnClickListener(this);
//		tvHomeLeftCity.setText(application.sharedUtils.readString("cityName"));
//		initCacheSize();
//
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		initUser();
//	}
//
//	public void onEventMainThread(ChangeCityEventBus eventBus) {
//		tvHomeLeftCity.setText(eventBus.getCity());
//	}
//
//	public void initUser() {
//		if (application.checkIsLogin()) {
//			application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivHomeLeftLogo);
//			tvHomeLeftUserName.setText(application.getUserInfo().getNick());
//			if ("".equals(application.getUserInfo().getBrief())) {
//				tvHomeLeftNickName.setText("我就是这样的一个人儿");
//			} else {
//				tvHomeLeftNickName.setText(application.getUserInfo().getBrief());
//			}
//			tvHomeLeftLogin.setText("退出");
//			tvHomeLeftLogin.setVisibility(View.VISIBLE);
//			tvHomeLeftNickName.setVisibility(View.VISIBLE);
//		} else {
//			ivHomeLeftLogo.setImageResource(R.drawable.icon);
//			tvHomeLeftUserName.setText("未登录");
//			tvHomeLeftNickName.setVisibility(View.INVISIBLE);
//			tvHomeLeftLogin.setVisibility(View.GONE);
//		}
//		if (application.sharedUtils.readInt("remind") == 1)
//			tvHomeLeftRemind.setText("当天提醒");
//		else if (application.sharedUtils.readInt("remind") == 2)
//			tvHomeLeftRemind.setText("提前一天提醒");
//		else if (application.sharedUtils.readInt("remind") == 3)
//			tvHomeLeftRemind.setText("提前2天提醒");
//		else if (application.sharedUtils.readInt("remind") == 4)
//			tvHomeLeftRemind.setText("不提醒");
//	}
//
//	public void initData() {
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
//		UpdateExampleConfig.setUpdateListener(true);
//		tvVersionName.setText("检查版本	" + Util.getVersionName(mContext));
//	}
//
//	public void initCacheSize() {
//		long dataSize = 0;
//		long fileSize = 0;
//		long externalCacheSize = 0;
//		File file = mContext.getFilesDir();
//		File data = new File("/data/data/" + mContext.getPackageName() + "/databases");
//
//		dataSize += FileUtils.getDirSize(data);
//		fileSize += FileUtils.getDirSize(file);
//
//		String size = FileUtils.formatFileSize(dataSize + fileSize);
//		if ((dataSize + fileSize + externalCacheSize) <= 0) {
//			tvHomeLeftSize.setText("0KB");
//		} else {
//			tvHomeLeftSize.setText(size);
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
//				application.logout();
//				dbManager.deleteSaveBeHavior();
//				initUser();
//				stopTimer();
//				application.showMsg("退出成功");
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
//		case R.id.lyHomeLeftClean:
//			try {
//				DataCleanManager.cleanApplicationData(mContext);
//				tvHomeLeftSize.setText("0KB");
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			break;
//		case R.id.lyVersion:
//			if (Utils.isFastDoubleClick())
//				return;
//			UmengUpdateAgent.forceUpdate(mContext);
//			// checkVersion();
//			break;
//		case R.id.lyHomeLeftCity:
//			if (!NetworkUtils.isNetworkAvailable(mContext)) {
//				application.showMsg(R.string.network_check);
//				return;
//			}
//			toActivity(new Intent(mContext, UpdateCityActivity.class).putExtra("tag", 1).putExtra("cityname", tvHomeLeftCity.getText().toString()));
//			break;
//		case R.id.ivHomeLeftLogo:
//			if (Utils.isFastDoubleClick())
//				return;
//			if (application.checkIsLogin()) {
//				toActivity(PersonalActivity.class);
//			} else
//				toActivity(new Intent(mContext, LoginActivityNew.class));
//			break;
//		case R.id.tvHomeLeftLogin:
//			if (application.checkIsLogin()) {
//				showProgress("正在注销");
//				AVIMClient avimClient = AVIMClient.getInstance(application.getUserInfo().getAccount());
//				avimClient.close(new AVIMClientCallback() {
//
//					@Override
//					public void done(AVIMClient arg0, AVException e) {
//						if (null != e) {
//							dismissProgress();
//							//application.showMsg("出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试。\n此时聊天服务不可用。");
//							e.printStackTrace();
//						} else {
//							if (timer == null)
//								timer = new Timer(true);
//							if (task == null) {
//								task = new TimerTask() {
//									public void run() {
//										Message message = new Message();
//										message.what = 1;
//										handler.sendMessage(message);
//										EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGOUT));
//									}
//								};
//							}
//							if (timer != null && task != null)
//								timer.schedule(task, 3000, 3000);
//						}
//					}
//				});
//				
//			} else {
//				toActivity(LoginActivityNew.class);
//			}
//			break;
//		case R.id.tvHomeLeftAbout:
//			if (Utils.isFastDoubleClick())
//				return;
//			toActivity(AboutAccuvallyActivity.class);
//			break;
//		case R.id.tvHomeLeftFeedBack:// 疑问与反馈
//			if (Utils.isFastDoubleClick())
//				return;
//			toActivity(FeedBackActivity.class);
//			break;
//		case R.id.lyHomeLeftRemind:// 活动设定
//			if (Utils.isFastDoubleClick())
//				return;
//			if (application.checkIsLogin()) {
//				toActivity(RemindActivity.class);
//			} else
//				toActivity(LoginActivityNew.class);
//			break;
//		case R.id.tvHomeLeftRecommend:// 应用推荐
//			if (Utils.isFastDoubleClick())
//				return;
//			toActivity(RecommendActivity.class);
//			break;
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		EventBus.getDefault().unregister(this);
//	}
//}
