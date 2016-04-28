package com.accuvally.hdtui.ui.calender;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	public static final long weekm = 7 * 86400000;

	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static CustomDate getNextSunday(Resources res) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7 - getWeekDay() + 1);
		CustomDate date = CustomDate.newInstence(res, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		return date;
	}

	/***
	 * 获取当前周的日期
	 * 
	 * @param res
	 * @param year
	 * @param month
	 * @param day
	 * @return 返回下周日的日期
	 */
	public static CustomDate getSunday(Resources res, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(getDateFromString(year, month, day));
		c.add(Calendar.DAY_OF_YEAR, 7 - getWeekDayFromDate(year, month, day));
		// getLastWeek(cusD.year, cusD.month, cusD.day);
		CustomDate newDate = CustomDate.newInstence(res, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		return newDate;
	}

	/***
	 * 获取当前周的日期
	 * 
	 * @param res
	 * @param year
	 * @param month
	 * @param day
	 * @return 返回下周日的日期
	 */
	public static CustomDate getNextSunday(Resources res, CustomDate cusD) {
		Calendar c = Calendar.getInstance();
		c.setTime(getDateFromString(cusD.year, cusD.month, cusD.day));
		c.add(Calendar.DAY_OF_YEAR, 7);
		// getLastWeek(cusD.year, cusD.month, cusD.day);
		CustomDate date = CustomDate.newInstence(res, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		return date;
	}

	public static void getLastWeek(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
	}

	public static int[] getWeekSunday(int year, int month, int day, int pervious) {
		int[] time = new int[3];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_MONTH, pervious);
		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH);
		time[2] = c.get(Calendar.DAY_OF_MONTH);
		return time;

	}

	public static int getWeekDayFromDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	// 得到一个月的起始时间和结束时间，flag为0代表当前周（本周），为1，代表上一周，为2代表下一周。。。
	public static long[] getMonthStartTimeAndEndTimeByFlag(CustomDate cs) {
		long time[] = new long[2];
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, cs.year);
		calendar.set(Calendar.MONTH, cs.month-1);
		calendar.set(Calendar.DAY_OF_MONTH, cs.day);

		int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), minDay, 00, 00, 00);
		time[0] = calendar.getTimeInMillis();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), maxDay, 00,00, 00);
		time[1] = calendar.getTimeInMillis();
		return time;
	}

	// 得到一个月的起始时间和结束时间，flag为0代表当前周（本周），为1，代表上一周，为2代表下一周。。。
	public static long getTimeInMillis(CustomDate cs) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, cs.year);
		calendar.set(Calendar.MONTH, cs.month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, cs.day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
		}
		return date;
	}

	public static boolean isToday(CustomDate date) {
		return (date.year == DateUtil.getYear() && date.month == DateUtil.getMonth() && date.day == DateUtil.getCurrentMonthDay());
	}

	public static boolean isCurrentMonth(CustomDate date) {
		return (date.year == DateUtil.getYear() && date.month == DateUtil.getMonth());
	}

	public static int getWeekDayFromDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month, day));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	public static Date getDateFromString(int year, int month, int day) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month)) + "-" + (day > 9 ? day : ("0" + day));
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	public static boolean isSameWeek(CustomDate one, CustomDate two) {
		Calendar c1 = Calendar.getInstance();
		c1.set(one.year, one.month, one.day);

		Calendar c2 = Calendar.getInstance();
		c2.set(two.year, two.month, two.day);

		return weekm >= (Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis()));
	}

	public static String[] getDayStartEnd(CustomDate cd) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, cd.year);
		c.set(Calendar.MONTH, cd.month - 1);
		c.set(Calendar.DAY_OF_MONTH, cd.day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] times = new String[2];
		times[0] = sdf.format(new Date(c.getTimeInMillis()));
		times[1] = sdf.format(new Date(c.getTimeInMillis() + 86400000 - 1000));
		return times;
	}

	public static String getDayStartAndEnd(CustomDate cd, int add) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, cd.year);
		c.set(Calendar.MONTH, cd.month);
		c.set(Calendar.DAY_OF_MONTH, cd.day);
		if (add != 0) {
			c.add(Calendar.DAY_OF_MONTH, add);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
	}
}
