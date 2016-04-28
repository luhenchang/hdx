package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.accuvally.hdtui.R;

public class RobTickeyPopup {
	private PopupWindow popupWindow;
	private Context mContext;
	
	public RobTickeyPopup(Context context) {
		this.mContext = context;
		initPopupWindow();
	}

	private void initPopupWindow() {
		View mPopView = LayoutInflater.from(mContext).inflate(R.layout.popup_join_robticket, null);
		mPopView.findViewById(R.id.ivClosePopup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		popupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		Drawable drawable = mContext.getResources().getDrawable(android.R.color.white);
		popupWindow.setBackgroundDrawable(drawable);
		popupWindow.update();
	}
	
	public void showPopupWindow(View anchor) {
		popupWindow.showAsDropDown(anchor, LayoutParams.MATCH_PARENT, 0);
	}

}
