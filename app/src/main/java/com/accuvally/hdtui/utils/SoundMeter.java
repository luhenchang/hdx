package com.accuvally.hdtui.utils;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class SoundMeter {
	private static final  double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	
	private static class Holder {
		public final static SoundMeter instance = new SoundMeter();
	}

	public synchronized static SoundMeter getInstance() {
		return Holder.instance;
	}


	public synchronized void start(String name) {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(name);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public synchronized void stop() throws Exception {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
				mRecorder.release();
			} catch (Exception e) {
				throw e;
			}finally {
				mRecorder = null;
			}
		}
	}


	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}
}
