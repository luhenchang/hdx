package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 自定义ImageSpan
 * 
 * @author wangxiaojie
 */
public final class SmileImageSpan extends ImageSpan {

	public SmileImageSpan(Context context, int resourceId) {
		super(context, resourceId);
	}

	public final Drawable getDrawable() {
		Drawable drawable = super.getDrawable();
		return drawable;
	}
}
