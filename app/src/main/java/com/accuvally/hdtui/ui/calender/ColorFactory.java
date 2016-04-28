package com.accuvally.hdtui.ui.calender;

import android.graphics.Color;
import android.util.Log;

public class ColorFactory {
	

	public static final String startDrawerBgColor = "#80D074";// 周历背景颜色
	public static final String endDrawerBgColor = "#FFFFFF";// 月历背景颜色
	
	public static final String startCalendarBgColor = "#80d074";// 周历背景颜色
	public static final String endCalendarBgColor = "#ECEDEC";// 月历背景颜色

	public static final String startTextColor = "#ffffff";// 周历的字体额颜色
	public static final String endTextColor = "#2D2C2D";// 月的字体额颜色
	
	public static final String startLunarTextColor = "#00000000";// 周历的字体额颜色
	public static final String endLunarTextColor = "#a0a0a0";// 月的字体额颜色
	
	public static final String startLunarTodayTextColor = "#00000000";// 周历的字体额颜色
	public static final String endLunarTodayTextColor = "#9961b754";// 月的字体额颜色
	
	public static final String startLunarClickTextColor = "#00000000";// 周历的字体额颜色
	public static final String endLunarClickTextColor = "#d6f5d1";// 月的字体额颜色
	
	public static final String startTodayTextColor = "#ffffff";// 周历的今天字体额颜色
	public static final String endTodayTextColor = "#61b754";// 月历的今天字体额颜色

	public static final String startClickTextColor = "#61b754";// 周历的今天字体额颜色
	public static final String endClickTextColor = "#ffffff";// 月历的今天字体额颜色

	public static final String startPassTextColor = "#eaeaea";// 周历的过去字体额颜色
	public static final String endPassTextColor = "#a0a0a0";// 月历的过去字体额颜色

	public static final String startTodayCircleColor = "#77c26c";// 周历的今天圈的颜色
	public static final String endTodayCircleColor = "#e3e5e3";// 月历的今天点击圈的颜色

	public static final String startClickCircleColor = "#ffffff";// 周历的点击圈的颜色
	public static final String endClickCircleColor = "#77c26c";// 月历的的点击圈的颜色
	
	
	public static final String startMonthTitleColor = "#ffffff";// 周历显示的月份
	public static final String endMonthTitleColor = "#80D074";// 月历的显示的月份
	
	
	public static final String startWeekColor = "#ffffff";// 周历显示的月份
	public static final String endWeekColor = "#8F8F8F";// 月历的显示的月份
	
	public static final String startHandleColor = "#80D074";// 周历显示的月份
	public static final String endHandleColor = "#FFFFFF";// 月历的显示的月份
	/**
	 * 显示的月份的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientWeek(float fraction){
		return gradient(fraction, startWeekColor, endWeekColor);
	}
	/**
	 * 显示的月份的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientMonthTitle(float fraction){
		return gradient(fraction, startMonthTitleColor, endMonthTitleColor);
	}
	
	
	
	/**
	 * 画日历背景的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientCalendarBg(float fraction){
		return gradient(fraction, startCalendarBgColor, endCalendarBgColor);
	}
	
	
	/**
	 * 画抽屉背景的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientViewPagerBg(float fraction){
		return gradient(fraction, startDrawerBgColor, endDrawerBgColor);
	}
	
	/**
	 * 画抽屉背景的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientDrawerBg(float fraction){
		return gradient(fraction, startDrawerBgColor, endDrawerBgColor);
	}
	
	/**
	 * 今天字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientTextTodayColor(float fraction) {
		return gradient(fraction, startTodayTextColor, endTodayTextColor);
	}

	/**
	 * 以前或过去字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientTextPassColor(float fraction) {
		return gradient(fraction, startPassTextColor, endPassTextColor);
	}

	/**
	 * 点击字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientTextClickColor(float fraction) {
		return gradient(fraction, startClickTextColor, endClickTextColor);
	}

	/**
	 * 当月字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientTextColor(float fraction) {
		return gradient(fraction, startTextColor, endTextColor);
	}
	
	
	/**
	 * 当月农历字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientLunarTextColor(float fraction) {
		return gradient(fraction, startLunarTextColor, endLunarTextColor);
	}
	
	/**
	 * 当月农历今天字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientLunarTodayTextColor(float fraction) {
		return gradient(fraction, startLunarTodayTextColor, endLunarTodayTextColor);
	}

	
	/**
	 * 当月农历今天字体的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientLunarClickTextColor(float fraction) {
		return gradient(fraction, startLunarClickTextColor, endLunarClickTextColor);
	}
	/**
	 * 今天圆圈的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientTodayCircle(float fraction) {
		return gradient(fraction, startTodayCircleColor, endTodayCircleColor);
	}

	/**
	 * 点击圆圈的颜色
	 * 
	 * @param fraction
	 * @return
	 */
	public static int gradientClickCircle(float fraction) {
		return gradient(fraction, startClickCircleColor, endClickCircleColor);
	}
	
	
	/**
	 * 显示的月份的颜色
	 * @param fraction
	 * @return
	 */
	public static int gradientHandleColor(float fraction){
		int color = gradient(fraction, startHandleColor, endHandleColor);
		Log.d("color", Color.parseColor(startHandleColor)+" = "+color);
		return color;
	}

	/**
	 * 0 是抽屉关闭
	 * 1是抽屉打开
	 * @param fraction
	 * @param startColor
	 * @param endColor
	 * @return
	 */
	public static int gradient(float fraction, String startColor, String endColor) {
		int startInt = Color.parseColor(startColor);
		int endInt = Color.parseColor(endColor);
		
		if (fraction > 1) {
			fraction =1;
			return endInt;
		} else if (fraction <= 0.1f) {
			fraction =0;
			return startInt;
		}

		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		int argb = (int) ((startA + (int) (fraction * (endA - startA))) << 24) | (int) ((startR + (int) (fraction * (endR - startR))) << 16) | (int) ((startG + (int) (fraction * (endG - startG))) << 8) | (int) ((startB + (int) (fraction * (endB - startB))));
		return argb;
	}

}
