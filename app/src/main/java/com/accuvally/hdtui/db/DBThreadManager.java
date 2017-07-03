package com.accuvally.hdtui.db;

import com.accuvally.hdtui.model.SessionInfo;

import android.os.Handler;
import android.os.HandlerThread;

public class DBThreadManager {

	private static Handler mHandler;

	public DBThreadManager() {
		HandlerThread workerThread = new HandlerThread("LightTaskThread");
		workerThread.start();
		mHandler = new Handler(workerThread.getLooper());
	}

	public static void post(Runnable run) {
		mHandler.post(run);
	}

	public static void insertSession(final SessionInfo session) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				SessionTable.insertOrUpdateSession(session);
			}
		});
	}

	public void postAtFrontOfQueue(Runnable runnable) {
		mHandler.postAtFrontOfQueue(runnable);
	}

	public void postDelayed(Runnable runnable, long delay) {
		mHandler.postDelayed(runnable, delay);
	}

	public void postAtTime(Runnable runnable, long time) {
		mHandler.postAtTime(runnable, time);
	}

}
