package com.accuvally.hdtui.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypeFaceUtil {

	private static Typeface sTypeface;
	public static final String PATH ="fonts/fangzhengzhunyuan.ttf";
	// 方正圆体
	// TypeFaceUtil.setDefaultFont(this, "MONOSPACE", "fangzhengzhengyuan.ttf");
	/**
	 * 
	 * @param context
	 * @param appDefaultStyle
	 *            系统默认的字体
	 * @param replaceFontStyle
	 *            要替换的字体
	 */
	public static void setDefaultFont(Context context, String appDefaultStyle) {
		final Typeface regular = Typeface.createFromAsset(context.getAssets(), PATH);
		replaceFont(appDefaultStyle, regular);
	}

	public static Typeface getTypeface(Context ctx) {
		if (sTypeface == null) {
			sTypeface = Typeface.createFromAsset(ctx.getAssets(), PATH);
		}
		return sTypeface;
	}

	public static void setTypeFace(Context ctx, TextView tv) {
		tv.setTypeface(getTypeface(ctx));
	}

	protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
		try {
			final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
			staticField.setAccessible(true);
			staticField.set(null, newTypeface);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
