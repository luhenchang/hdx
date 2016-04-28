package com.accuvally.hdtui.utils;

import com.accuvally.hdtui.AccuApplication;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Context mContext = AccuApplication.getInstance();

	public static void showMsg(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}
	
	public static void showMsgShort(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

	public static void showMsg(String message, int duration) {
		Toast.makeText(mContext, message, duration).show();
	}

	public static void showMsg(int message) {
		Toast.makeText(mContext, mContext.getResources().getString(message), Toast.LENGTH_LONG).show();
	}
}
