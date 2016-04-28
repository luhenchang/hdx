package com.accuvally.hdtui.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.accuvally.hdtui.AccuApplication;

public class MapUtils {

	public static void onGoogleClick(AccuApplication application, Context context, double chufajingdu, double chufaweidu, double daodajingdu, double daodaweidu) {
		if (!isAppInstalled(context, "com.google.android.apps.maps")) {
			application.showMsg("检测到您未安装google地图");
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr=" + chufaweidu + "," + chufajingdu + "&daddr=" + daodaweidu + "," + daodajingdu + "&hl=zh"));
		context.startActivity(i);
	}

	public static void onGaodeClick(String chufa, String daoda, AccuApplication application, Context context, double chufajingdu, double chufaweidu, double daodajingdu, double daodaweidu) {
		if (!isAppInstalled(context, "com.autonavi.minimap")) {
			application.showMsg("检测到您未安装高德地图");
			return;
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setData(Uri.parse("androidamap://route?sourceApplication=softname&slat=" + chufaweidu + "&slon=" + chufajingdu + "&sname=" + chufa + "&dlat=" + daodaweidu + "&dlon=" + daodajingdu + "&dname=" + daoda + "&dev=0&m=0&t=1&showType=1"));
		intent.setPackage("com.autonavi.minimap");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(intent);
	}

	// 判断具有某一包名的程序是否安装
	public static boolean isAppInstalled(Context context, String packageName) {
		// 获取到一个PackageManager的instance
		final PackageManager packageManager = context.getPackageManager();
		// PERMISSION_GRANTED = 0
		List<PackageInfo> mPackageInfo = packageManager.getInstalledPackages(0);
		boolean flag = false;
		if (mPackageInfo != null) {
			String tempName = null;
			for (int i = 0; i < mPackageInfo.size(); i++) {
				// 获取到AP包名
				tempName = mPackageInfo.get(i).packageName;
				if (tempName != null && tempName.equals(packageName)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
}
