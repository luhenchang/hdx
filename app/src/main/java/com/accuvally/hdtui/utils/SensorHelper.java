package com.accuvally.hdtui.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.util.Log;

public class SensorHelper implements SensorEventListener {

	private AudioManager audioManager;
	private SensorManager mSensorManager;
	private Sensor mSensor;

	public SensorHelper(Context ctx) {
		audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float range = event.values[0];
		if (range == mSensor.getMaximumRange()) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			Log.d("SensorHelper","range ="+range+"正常");
		} else {
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			Log.d("SensorHelper","range ="+range+"听筒");
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void play() {
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}

}
