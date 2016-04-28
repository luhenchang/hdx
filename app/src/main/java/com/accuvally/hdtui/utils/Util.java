package com.accuvally.hdtui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.GeTuiWapActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class Util {

	// 每个字符后面加\n
	public static String appendSpace(String para) {
		int length = para.length();
		char[] value = new char[length << 1];
		for (int i = 0, j = 0; i < length; ++i, j = i << 1) {
			value[j] = para.charAt(i);
			value[1 + j] = '\n';
		}
		String returnStr = new String(value);
		String str = returnStr.substring(0, returnStr.length() - 1);
		return str;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":").replaceAll("，", ",").replaceAll("（", "(").replaceAll("）", ")").replaceAll("——", "-").replaceAll("&#34;", " ").replaceAll("&amp;", " ");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int version = packInfo.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	public static void setTextSpan(TextView tv) {
		final Context mContext = tv.getContext();
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			Spannable sp = (Spannable) text;
			URLSpan[] urlSpans = sp.getSpans(0, text.length(), URLSpan.class);

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();

			for (URLSpan urlSpan : urlSpans) {
				final String url = urlSpan.getURL();
				ClickableSpan clickableSpan = new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						if (url.indexOf("huodongxing.com/event") != -1 || url.indexOf("huodongxing.com/go") != -1) {
							mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", url));
						} else {
							Intent intent = new Intent(mContext, GeTuiWapActivity.class);
							intent.putExtra("title", "消息链接");
							intent.putExtra("url", url);
							mContext.startActivity(intent);
						}
					}
				};
				style.setSpan(clickableSpan, sp.getSpanStart(urlSpan), sp.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			
			tv.setText(style);
		}
	}
}
