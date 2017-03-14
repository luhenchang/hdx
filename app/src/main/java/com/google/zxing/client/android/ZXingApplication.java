package com.google.zxing.client.android;

import android.util.Log;

public class ZXingApplication  {

	public static final String TAG = "zxing_dzt";
	private static final boolean mIsShowLog = true;
	private static ZXingApplication instance;

	/**
	 * ȫ�ֵĴ�ӡ��Ϣ����
	 * 
	 * @param className
	 *            ����
	 * @param msg
	 *            Ҫ��ӡ����Ϣ
	 * @date 2014.07.28
	 */
	public static void print_i(String className, String msg) {
		if (mIsShowLog)
			Log.i(TAG, className + "---------->" + msg);
	}


}
