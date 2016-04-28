package com.accuvally.hdtui.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.R;

public class MyCount extends CountDownTimer {
	TextView textview;
	public MyCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}
	public MyCount(long millisInFuture, long countDownInterval,TextView textview) {
		super(millisInFuture, countDownInterval);
		this.textview=textview;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long millisUntilFinished) {
		Date date = new Date(millisUntilFinished);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String str = sdf.format(date);
	 
		textview.setTextColor(Color.argb(255, 118, 118, 118));
		String waitTime="请等待" + millisUntilFinished / 1000 + "秒";			
		SpannableStringBuilder style = new SpannableStringBuilder(waitTime);
		style.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 108,0)), 3, 3+(millisUntilFinished / 1000+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 20, 20, 15);
		textview.setText(style);
		textview.setLayoutParams(lp);
		textview.setBackgroundResource(R.drawable.response_verfication_code);
		textview.setClickable(false);
	}

	@Override
	public void onFinish() {
		textview.setBackgroundResource(R.drawable.request_verification_code);
		textview.setText(R.string.details_dialog2_text3);		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);			
		lp.setMargins(15, 20, 20, 15);
		textview.setLayoutParams(lp);
		textview.setTextColor(Color.argb(255, 255,255, 255));
		textview.setClickable(true);
	}

}
