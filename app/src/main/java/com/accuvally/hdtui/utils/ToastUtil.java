package com.accuvally.hdtui.utils;

import android.content.Context;
import android.widget.Toast;

import com.accuvally.hdtui.AccuApplication;

public class ToastUtil {

	private static Context mContext = AccuApplication.getInstance();

	public static void showMsg(String message) {
        if((message!=null)&&(!message.equals(""))){
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

	}
	
	public static void showMsgShort(String message) {
        if((message!=null)&&(!message.equals(""))){
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }

	}

	public static void showMsg(String message, int duration) {
        if((message!=null)&&(!message.equals(""))){
            Toast.makeText(mContext, message, duration).show();
        }

	}

	public static void showMsg(int message) {
		Toast.makeText(mContext, mContext.getResources().getString(message), Toast.LENGTH_LONG).show();
	}
}
