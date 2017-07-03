package com.accuvally.hdtui.activity.entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.ActivityUtils;
import com.accuvally.hdtui.utils.FileUtils;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.SharedUtils;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.io.File;

/**
 * welcome
 * 
 * @author Semmer Wang
 * 
 */
public class WelcomeActivity extends Activity {

	private SharedUtils sharedUtils;

	boolean isFirstIn;

	AccuApplication application;

	private Handler mHandle = new Handler();

//	protected boolean OpendIndetail;

    private boolean isAgainSplash=false;//
    public static final String IS_AGAIN_SPLASH="IS_AGAIN_SPLASH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);

        isAgainSplash=getIntent().getBooleanExtra(IS_AGAIN_SPLASH,false);

		application = (AccuApplication) getApplication();
		Intent i_getvalue = getIntent();
		String action = i_getvalue.getAction();
		// 从浏览器跳转过来的
		if (Intent.ACTION_VIEW.equals(action)) {
			Uri uri = i_getvalue.getData();
			if (uri != null) {
				if (null == application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME)) {
					application.sharedUtils.writeBoolean("isSynchronous", true);
					application.sharedUtils.writeInt("remind", 1);
					application.sharedUtils.writeInt("isBaidu", 3);
					application.sharedUtils.writeBoolean("isRegDevice", false);
					application.sharedUtils.writeBoolean("isFirstIn", true);
					application.sharedUtils.writeString(Config.KEY_ACCUPASS_USER_NAME, Config.ACCUPASS_ID);
					application.sharedUtils.writeString(Config.KEY_ACCUPASS_ACCESS_TOKEN, Config.ACCUPASS_KEY);
				}

				String homeId = uri.getQueryParameter("eid");
				try {
					if (homeId.matches("^\\d+$")) {
						Intent intent = new Intent(this, AccuvallyDetailsActivity.class);
						intent.putExtra("id", homeId);
						intent.putExtra("isHuodong", 0);// 默认为活动行的活动
						startActivity(intent);
						finish();
						return;
					}
				} catch (Exception e) {
				}
			}
		}

		sharedUtils = new SharedUtils(this);

		next();
		
//		Log.d("build", "{"+new Date(System.currentTimeMillis()).toLocaleString()+"}");
	}

	private void next() {
		// 第一次进入直接启动引导页
//        Trace.e("registActivityLifeCycle","saveSplashTimeInPre");
		isFirstIn = sharedUtils.readBoolean("isFirstIn");
		if (!isFirstIn) {
			mHandle.postDelayed(delayGuide, 3000);//首次登录进入引导页
		} else {
			// 判断当前的MainActivityNew是否已经启动了,如果已经启动直接跳转
			long delay = 0;
				String url = sharedUtils.readString("flash_logourl");
				// 没有闪屏
				if (TextUtils.isEmpty(url)) {
					// 缓存小于100KB
					if (getCacheSize() < 1024 * 300) {
						delay = 3000;
					} else {
						delay = 1000;
					}
				} else {
					displayDynamicLogo();
					// 闪屏停留 5秒，然后消失
					delay = 5000;
				}


            if(isAgainSplash){
                mHandle.postDelayed(delayFinish, delay);
            }else {
                mHandle.postDelayed(delayMain, delay);//显示welcome几秒之后进入主页
            }


		}
		getDynamicLogo();//动态读取LOGO,下次展示
	}
	 
	/**
	 * 相片按相框的比例动态缩放
	 * @param context 
	 *  要缩放的图片
	 *   模板宽度
	 *  height 模板高度
	 * @return
	 */
	public static Bitmap upImageSize(Context context,Bitmap bmp,int reduceH) {
		
		DisplayMetrics mDisplayMetrics =context.getResources().getDisplayMetrics();
		int width = mDisplayMetrics.widthPixels;
		int height = mDisplayMetrics.heightPixels;
		
		if(bmp==null){
			return null;
		}
		// 计算比例
		float scaleX = (float)width / bmp.getWidth();// 宽的比例
		float scaleY = (float)height / bmp.getHeight();// 高的比例
		//新的宽高
		int newW = 0;
		int newH = 0;
		if(scaleX > scaleY){
			newW = (int) (bmp.getWidth() * scaleX);
			newH = (int) (bmp.getHeight() * scaleX);
		}else if(scaleX <= scaleY){
			newW = (int) (bmp.getWidth() * scaleY);
			newH = (int) (bmp.getHeight() * scaleY);
		}
		return Bitmap.createScaledBitmap(bmp, newW, newH, true);
	}
	

	/**
	 * 展示动态LOGO
	 */
	private void displayDynamicLogo() {
		String logourl = sharedUtils.readString("flash_logourl");
		// 加载本地图片
		if (TextUtils.isEmpty(logourl)) {
			return;
		}
		ImageLoader.getInstance().loadImage(logourl, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //如果flash_logourl有url，而且加载成功，则显示动态的网络logo
				findViewById(R.id.static_logolayout).setVisibility(View.GONE);
				findViewById(R.id.sync_logolayout).setVisibility(View.VISIBLE);
				ImageView img = (ImageView) findViewById(R.id.flash_logo);
				int h = getResources().getDrawable(R.drawable.welcome_bg2).getIntrinsicHeight();
				loadedImage =upImageSize(WelcomeActivity.this,loadedImage,h);
				BitmapDrawable drawable = new BitmapDrawable(getResources(), loadedImage);
				img.setBackgroundDrawable(drawable);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});

		findViewById(R.id.flash_logo).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String accuid = sharedUtils.readString("flash_ActivityId");
                String returnurl = sharedUtils.readString("flash_returnurl");
                boolean opendindetail = sharedUtils.readBoolean("flash_opendindetail", false);


				Log.d("l"," onClick_ActivityId ============="+accuid);
				if (!TextUtils.isEmpty(accuid) && opendindetail) {
					Intent intent = new Intent();
					intent.putExtra("id", accuid);
					intent.putExtra("isHuodong", 0);// 1 活动推 0 活动行
					intent.setClass(WelcomeActivity.this, MainActivityNew.class);
					startActivity(intent);
					mHandle.removeCallbacks(delayMain);
					mHandle.removeCallbacks(delayGuide);
					finish();
				}else if(!TextUtils.isEmpty(returnurl)){
                    Intent intent = new Intent();
                    intent.putExtra("returnurl", returnurl);
                    intent.setClass(WelcomeActivity.this, MainActivityNew.class);
                    startActivity(intent);
                    mHandle.removeCallbacks(delayMain);
                    mHandle.removeCallbacks(delayGuide);
                    finish();

                }
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 * 动态读取LOGO,不展示
	 */
	private void getDynamicLogo() {
		HttpCilents httpCilents = new HttpCilents(WelcomeActivity.this);
		httpCilents.get(Url.GET_DYNAMIC_LOGO, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					String resultString = messageInfo.getResult();

					String activityId = "";
                    String returnurl = "";
                    String logourl = "";
                    boolean opendindetail=false;

					try {
						org.json.JSONObject jbJsonObject = new org.json.JSONObject(resultString);

						logourl = jbJsonObject.getString("logo");
						activityId = jbJsonObject.getString("activityid");
                        opendindetail = jbJsonObject.getBoolean("opendindetail");

                        if(jbJsonObject.has("returnurl")){
                            returnurl=jbJsonObject.getString("returnurl");
                        }

					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 网络下载图片
					if (!TextUtils.isEmpty(logourl)) {

						final String flash_ActivityId = activityId;
                        final String flash_returnurl = returnurl;
                        final boolean flash_opendindetail = opendindetail;

						Log.d("l"," flash_ActivityId ============="+flash_ActivityId);
						ImageLoader.getInstance().loadImage(logourl, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri, View view) {

							}

							@Override
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								sharedUtils.writeString("flash_logourl", imageUri);
								sharedUtils.writeString("flash_ActivityId", flash_ActivityId);
                                sharedUtils.writeString("flash_returnurl", flash_returnurl);
                                sharedUtils.writeBoolean("flash_opendindetail", flash_opendindetail);
								Log.d("l", "onLoadingComplete =" + imageUri);
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
							}
						});
					} else {
						// 清空
						sharedUtils.writeString("flash_logourl", "");
						sharedUtils.writeString("flash_ActivityId", "");
                        sharedUtils.writeString("flash_returnurl", "");
                        sharedUtils.writeBoolean("flash_opendindetail", false);

					}
					break;
				case Config.RESULT_CODE_ERROR:
					break;
				}
			}
		});
	}


    // 再次闪屏的时候直接删除自己
    private Runnable delayFinish = new Runnable() {

        @Override
        public void run() {
            mHandle.removeCallbacks(this, null);
            finish();
        }
    };

	// 主页
	private Runnable delayMain = new Runnable() {

		@Override
		public void run() {
			mHandle.removeCallbacks(this, null);
            ActivityUtils.toNext(WelcomeActivity.this);
            finish();
		}
	};

	// 引导
	private Runnable delayGuide = new Runnable() {

		@Override
		public void run() {
			mHandle.removeCallbacks(this, null);
			Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
			startActivity(intent);
			finish();
		}
	};

	public void skip(View view) {
		ImageLoader.getInstance().cancelDisplayTask(((ImageView) findViewById(R.id.flash_logo)));
        if(isAgainSplash){
            mHandle.post(delayFinish);//跳过
        }else {
            mHandle.post(delayMain);//跳过，进入主页
        }

	}

	public long getCacheSize() {
		long dataSize = 0;
		long fileSize = 0;
		try {
			File file = getFilesDir();
			File data = new File("/data/data/" + getPackageName() + "/databases");

			dataSize += FileUtils.getDirSize(data);
			fileSize += FileUtils.getDirSize(file);
		} catch (Exception e) {
		}
		return fileSize + dataSize;
	}
}
