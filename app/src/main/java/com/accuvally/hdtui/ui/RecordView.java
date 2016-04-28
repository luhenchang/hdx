package com.accuvally.hdtui.ui;

import java.io.File;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.ChatActivity;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.SoundMeter;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RecordView {

	private ChatActivity chatActivity;
	private View rootView;

	private boolean isShosrt = false;
	private LinearLayout voice_loading, voice_rcding, voice_tooshort;

	private ImageView volume;
	private Handler mHandler = new Handler();
	private String voiceName;
	private long startVoiceT, endVoiceT;
	private Button mBtnRcd;
	private String mAudioPath;

	public RecordView(ChatActivity chatActivity, View findViewById) {
		this.chatActivity = chatActivity;
		this.rootView = findViewById;
		setupViews();
		mAudioPath = new FileCache(chatActivity).getAudioCacheDir().getAbsolutePath();
	}

	/**
	 * 是为了动态设置点击btn的背景图片
	 * 
	 * @param recorder
	 */
	public void setBtnRecorder(Button recorder) {
		this.mBtnRcd = recorder;
	}

	private void setupViews() {
		voice_rcding = (LinearLayout) rootView.findViewById(R.id.voice_rcd_hint_rcding);
		voice_loading = (LinearLayout) rootView.findViewById(R.id.voice_rcd_hint_loading);
		voice_tooshort = (LinearLayout) rootView.findViewById(R.id.voice_rcd_hint_tooshort);
		volume = (ImageView) rootView.findViewById(R.id.volume);
	}

	public OnTouchListener getTouchListener() {
		return mTouch;
	}

	private OnTouchListener mTouch = new OnTouchListener() {

		// 按下语音录制按钮时
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rootView.setVisibility(View.VISIBLE);
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(chatActivity, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
				voice_loading.setVisibility(View.VISIBLE);
				voice_rcding.setVisibility(View.GONE);
				voice_tooshort.setVisibility(View.GONE);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						if (!isShosrt) {
							voice_loading.setVisibility(View.GONE);
							voice_rcding.setVisibility(View.VISIBLE);
						}
					}
				}, 300);
				startVoiceT = System.currentTimeMillis();
				voiceName = startVoiceT + ".amr";
				String filename = mAudioPath + "/" + voiceName;
				start(filename);

			} else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开手势时执行录制完成
				
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				voice_rcding.setVisibility(View.GONE);
				endVoiceT = System.currentTimeMillis();
				float time = (endVoiceT - startVoiceT) / 1000.0f;
				if (time < 1) {
					isShosrt = true;
					voice_loading.setVisibility(View.GONE);
					voice_rcding.setVisibility(View.GONE);
					voice_tooshort.setVisibility(View.VISIBLE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							voice_tooshort.setVisibility(View.GONE);
							voice_rcding.setVisibility(View.GONE);
							isShosrt = false;
						}
					}, 500);
					File file = new File(mAudioPath + "/" + voiceName);
					if (file.exists()) {
						file.delete();
					}
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							try {
								stop();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, 1000 - endVoiceT - startVoiceT);
				} else {

					try {
						stop();
						File file = new File(mAudioPath + "/" + voiceName);
						if (file.exists() && file.length() > 0) {
							final ChatActivity chat = chatActivity;
							chat.sendRecorder(mAudioPath + "/" + voiceName, time / 1000.0d);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						Log.e("e", "ex =" + ex.getMessage());
					}
				}
			}
			return false;
		}
	};

	private void stop() throws Exception {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		SoundMeter.getInstance().stop();
		volume.setImageResource(R.drawable.amp1);
	}

	public void reset() {
		mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
		File file = new File(mAudioPath + "/" + voiceName);
		if (file.exists()) {
			file.delete();
		}
		mHandler.postDelayed(new Runnable() {
			public void run() {
				voice_tooshort.setVisibility(View.GONE);
				voice_loading.setVisibility(View.GONE);
				voice_rcding.setVisibility(View.GONE);
				isShosrt = false;
				if (!android.os.Build.MODEL.equals("ASUS_Z00ADB")) {
					try {
						stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 300);
	}

	private void start(String name) {
		SoundMeter.getInstance().start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			try {
				stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = SoundMeter.getInstance().getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}
}
