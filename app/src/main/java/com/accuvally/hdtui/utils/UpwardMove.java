package com.accuvally.hdtui.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.accuvally.hdtui.ui.CustomerScrollView;

public class UpwardMove {

	public  UpwardMove(Context  context) {	
	}
	/**
	 * 
	 * @param editText  获取焦点的控件
	 * @param customerScrollView    滚动控件
	 * @param multiple     移动距离等于获取控件高度乘以multiple
	 * @param up  如果up大于等于零表示editText失去焦点时回滚的位置，如果up小于零表示editText失去焦点时不回滚
	 */
	public void scroll(final EditText editText,final CustomerScrollView customerScrollView,final double multiple,final int up ){
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, final boolean hasFocus) {
				// TODO Auto-generated method stub
				customerScrollView.postDelayed(new Runnable() {

					@Override
					public void run() {
						if(hasFocus){	
							customerScrollView.scrollTo(0, (int)(multiple*editText.getHeight()));					
						}else{
							if(up>=0){
								customerScrollView.scrollTo(0, up);
							}

						}
					}
				},300);
			}
		});
	}
	public void scroll(TextView textView,final CustomerScrollView customerScrollView,final int down,final int up){
		textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, final boolean hasFocus) {
				// TODO Auto-generated method stub
				customerScrollView.postDelayed(new Runnable() {

					@Override
					public void run() {
						if(hasFocus){					
							customerScrollView.scrollTo(0, down);					
						}else{
							//							customerScrollView.scrollTo(0, up);
						}
					}
				},300);
			}
		});
	}
}
