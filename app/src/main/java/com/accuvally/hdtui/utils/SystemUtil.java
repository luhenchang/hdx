package com.accuvally.hdtui.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SystemUtil {

	private static long lastClickTime;
	private static long span = 500;
	private static long exitSpan = 1200;

	public static boolean isNeedExit() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < exitSpan) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static boolean isFrequentlyOperation() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < span) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void showSoftInput(Context ctx, View v) {
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideSoftInput(Context ctx, View v) {
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public void hideInputMethod(Context ctx, View v) {
		InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int dpToPx(Context context, int dp) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
		return (int) px;
	}
	
	public static void copy(Context context, String content) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}
}
