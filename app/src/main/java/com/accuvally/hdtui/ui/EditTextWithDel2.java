package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.accuvally.hdtui.R;

public class EditTextWithDel2 extends EditText {
	private final static String TAG = "EditTextWithDel2";
	private Drawable imgInable;
	private Drawable imgAble;
	private Context mContext;

	public EditTextWithDel2(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public EditTextWithDel2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public EditTextWithDel2(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		imgInable = mContext.getResources().getDrawable(R.drawable.login_detele_icon_bg);
		imgAble = mContext.getResources().getDrawable(R.drawable.login_detele_icon_bg);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
		setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.i("info", "hasFocus:" + hasFocus);
				if (hasFocus) {
					setDrawable();
				} else {
					setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}
		});
	}

	// 设置删除图片
	private void setDrawable() {
		if (length() < 1)
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		else
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
	}

	// 处理删除事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
			int eventX = (int) event.getRawX();
			int eventY = (int) event.getRawY();
			Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			rect.left = rect.right - 80;
			if (rect.contains(eventX, eventY))
				setText("");
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
