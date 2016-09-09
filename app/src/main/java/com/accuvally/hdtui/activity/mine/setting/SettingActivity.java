package com.accuvally.hdtui.activity.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.BaseActivityDeepLink;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.util.ChooseCityActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.utils.DataCleanManager;
import com.accuvally.hdtui.utils.FileUtils;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import de.greenrobot.event.EventBus;

//import com.umeng.update.UmengUpdateAgent;
//import com.umeng.update.UmengUpdateListener;
//import com.umeng.update.UpdateResponse;
//import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView tvHomeLeftCity;// 城市
	private TextView tvHomeLeftRemind;// 活动设定 提醒时间
	private TextView tvCurrentVersion;// 当前版本
	private TextView tvHomeLeftSize;// 缓存大小
	private TextView tvLoginOut;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		EventBus.getDefault().register(this);
		initView();

        resolveLinkData();
	}

    private void resolveLinkData(){
        Intent intent = getIntent();
        if(intent!=null){
            String data = intent.getStringExtra(BaseActivityDeepLink.Channle);
            AccuApplication.getInstance().showMsg("来自："+data);
        }

    }

	public void initView() {
		setTitle("设置");

		tvLoginOut = (TextView) findViewById(R.id.tvSettingLogout);
		tvLoginOut.setOnClickListener(this);// 退出当前账号click

		findViewById(R.id.llHomeLeftCity).setOnClickListener(this);// 我的城市click
		findViewById(R.id.llHomeLeftRemind).setOnClickListener(this);// 活动设定click
		findViewById(R.id.llHomeLeftFeedBack).setOnClickListener(this);// 疑问与反馈click
		findViewById(R.id.llVersion).setOnClickListener(this);// 版本更新click
		findViewById(R.id.llHomeLeftClean).setOnClickListener(this);// 清除缓存click
		findViewById(R.id.llHomeLeftRecommend).setOnClickListener(this);// 应用推荐click
		findViewById(R.id.llHomeLeftAbout).setOnClickListener(this);// 关于我们click

		tvHomeLeftCity = (TextView) findViewById(R.id.tvHomeLeftCity);
		tvHomeLeftCity.setText(application.sharedUtils.readString("cityName"));

		tvHomeLeftRemind = (TextView) findViewById(R.id.tvHomeLeftRemind);

		tvCurrentVersion = (TextView) findViewById(R.id.tvCurrentVersion);
		setVersioinUpdate();

		tvHomeLeftSize = (TextView) findViewById(R.id.tvHomeLeftSize);
		initCacheSize();

		resetLoginBtn();
	}

	private void resetLoginBtn() {
		if (application.checkIsLogin()) {
			tvLoginOut.setText(R.string.setting_login_out);
//			tvLoginOut.setBackgroundColor(getResources().getColor(R.color.red_delete));
		} else {
			tvLoginOut.setText(R.string.setting_login);
		}
	}

	public void setTextRemind() {
		if (application.sharedUtils.readInt("remind") == 1)
			tvHomeLeftRemind.setText("当天提醒");
		else if (application.sharedUtils.readInt("remind") == 2)
			tvHomeLeftRemind.setText("提前一天提醒");
		else if (application.sharedUtils.readInt("remind") == 3)
			tvHomeLeftRemind.setText("提前2天提醒");
		else if (application.sharedUtils.readInt("remind") == 4)
			tvHomeLeftRemind.setText("不提醒");
	}

	public void setVersioinUpdate() {
		/*UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.No: // has no
					application.showMsg("你的版本是最新版");
					break;
				case UpdateStatus.NoneWifi: // none
					application.showMsg("没有wifi连接， 只在wifi下更新");
					break;
				case UpdateStatus.Timeout: // time
					application.showMsg("检测超时，请重试");
					break;
				}
			}
		});*/
//		tvCurrentVersion.setText("当前版本" + Util.getVersionName(mContext));
        tvCurrentVersion.setText(Util.getVersionName(mContext));
	}

	public void initCacheSize() {
		long dataSize = 0;
		long fileSize = 0;
		long externalCacheSize = 0;
		File file = mContext.getFilesDir();
		File data = new File("/data/data/" + mContext.getPackageName() + "/databases");

		dataSize += FileUtils.getDirSize(data);
		fileSize += FileUtils.getDirSize(file);

		String size = FileUtils.formatFileSize(dataSize + fileSize);
		if ((dataSize + fileSize + externalCacheSize) <= 0) {
			tvHomeLeftSize.setText("0KB");
		} else {
			tvHomeLeftSize.setText(size);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setTextRemind();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.llHomeLeftCity:// 我的城市
			if (!NetworkUtils.isNetworkAvailable(mContext)) {
				application.showMsg(R.string.network_check);
				return;
			}
			toActivity(new Intent(mContext, ChooseCityActivity.class).putExtra("tag", 1).putExtra("cityname",
					tvHomeLeftCity.getText().toString()));
			break;
		case R.id.llHomeLeftRemind:// 活动设定
			if (Utils.isFastDoubleClick())
				return;
			if (application.checkIsLogin()) {
				toActivity(RemindActivity.class);
			} else
				toActivity(LoginActivityNew.class);
			break;
		case R.id.llHomeLeftFeedBack:// 疑问与反馈
			if (Utils.isFastDoubleClick())
				return;
			toActivity(FeedBackActivity.class);
			break;

		/*case R.id.llVersion:// 版本更新
			if (Utils.isFastDoubleClick())
				return;
			UmengUpdateAgent.forceUpdate(mContext);
			break;*/
		case R.id.llHomeLeftClean:// 清除缓存
			try {
				DataCleanManager.cleanApplicationData(mContext);
				tvHomeLeftSize.setText("0KB");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case R.id.llHomeLeftRecommend:// 应用推荐
			if (Utils.isFastDoubleClick())
				return;
			toActivity(RecommendActivity.class);
			break;
		case R.id.llHomeLeftAbout:// 关于我们
			if (Utils.isFastDoubleClick())
				return;
			toActivity(AboutAccuvallyActivity.class);
			break;
		case R.id.tvSettingLogout:// 退出当前账号
			if (application.checkIsLogin()) {
				showProgress("正在注销");
				AVIMClient avimClient = AVIMClient.getInstance(application.getUserInfo().getAccount());
				avimClient.close(new AVIMClientCallback() {

					@Override
					public void done(AVIMClient arg0, AVIMException e) {
						dismissProgress();
						if (null != e) {
							// application.showMsg("出错了，可能是网络问题无法连接 LeanCloud 云端，请检查网络之后重试");
							e.printStackTrace();
							application.showMsg("退出失败");
						} else {
							MobclickAgent.onEvent(mContext, "logout_count");
							application.logout();
							EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGOUT));
							dbManager.deleteSaveBeHavior();
							application.showMsg("退出成功");
							finish();
						}
					}
				});
			} else {
				toActivity(LoginActivityNew.class);
			}
			break;
		}
	}

	public void onEventMainThread(ChangeCityEventBus eventBus) {
		tvHomeLeftCity.setText(eventBus.getCity());
	}

	public void onEventMainThread(ChangeUserStateEventBus eventBus) {
		resetLoginBtn();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
