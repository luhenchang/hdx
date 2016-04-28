package com.accuvally.hdtui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.manager.AccountManager;

public class SPUtil {

//	public static String PREFERENCE_NAME = "Accuvally";

	public static String PREFERENCE_NAME = "data";
	public static Context mContext = AccuApplication.getInstance();

	/**************************************** String组 **********************************************/
	public static boolean putUserString(String key, String value) {
		String name = AccountManager.getUserInfo().getAccount();
		SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		return editor.putString(key, value).commit();
	}

	public static String getUserString(String key) {
		String name = AccountManager.getUserInfo().getAccount();
		SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	public static String getUserString(String key, String defaultValue) {
		String name = AccountManager.getUserInfo().getAccount();
		SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static boolean putString(String key, String value) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		return editor.putString(key, value).commit();
	}

	public static String getString(String key) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	public static String getString(String key, String defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	/**************************************** Boolean组 **********************************************/
	public static boolean putBoolean(String key, boolean value) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static boolean getBoolean(String key) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	/**************************************** Int组 **********************************************/
	public static void putInt(String key, int value) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(String key) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, 0);
	}

	public static int getInt(String key, int defValue) {
		SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
}
