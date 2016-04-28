package com.accuvally.hdtui.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.text.format.Time;
import android.util.Log;

import com.accuvally.hdtui.AccuApplication;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TimeUtils {

	/**
	 * yyyy-MM-dd EE
	 */
	public static SimpleDateFormat SD_YMDE = new SimpleDateFormat("yyyy年MM月dd日 EE");

	/**
	 * yyyy-MM-dd EE HH:mm:ss
	 */
	public static SimpleDateFormat SD_YMDEHMS = new SimpleDateFormat("yyyy年MM月dd日 EE HH:mm:ss");

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static SimpleDateFormat SD_YMDHMS = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static SimpleDateFormat SD_YMDHM = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

	/**
	 * yyyy-MM-dd
	 */
	public static SimpleDateFormat SD_YMD = new SimpleDateFormat("yyyy年MM月dd日");
	
	/**
	 * MM月dd日
	 * MM-dd
	 */
	public static SimpleDateFormat SD_MD = new SimpleDateFormat("MM月dd日");

	/**
	 * MMddHHmmss
	 */
	public static SimpleDateFormat SD_MDHMS = new SimpleDateFormat("MMddHHmmss");
	
	public static SimpleDateFormat SD_MDTHMS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	/**
	 * HH:mm
	 */
	public static SimpleDateFormat SD_HM = new SimpleDateFormat("HH:mm");
	
	public static SimpleDateFormat MM_dd = new SimpleDateFormat("MM-dd");
	public static DateFormat DF_YMD = new SimpleDateFormat("yyyy-MM-dd");
	
	public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
	public static SimpleDateFormat yyyyMMdd_HHmm = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	
	public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static SimpleDateFormat formatBuilder;

	public static DateFormat SD_MDHM_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getDate(String format) {
		formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(new Date());
	}

	public static String getDate() {
		return getDate("hh:mm:ss");
	}

	@SuppressWarnings("unused")
	public static String getShortTime(String time) {
		String shortstring = null;
		long now = Calendar.getInstance().getTimeInMillis();
		@SuppressWarnings("deprecation")
		Date date = new Date(time);
		if (date == null)
			return shortstring;
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分前";
		} else if (deltime > 1) {
			shortstring = deltime + "秒前";
		} else {
			shortstring = "1秒前";
		}
		return shortstring;
	}

	@SuppressWarnings("unused")
	public static String getShortTime(long timeStamp) {
		String shortstring = null;
		long now = Calendar.getInstance().getTimeInMillis();
		@SuppressWarnings("deprecation")
		Date date = new Date(timeStamp);
		if (date == null)
			return shortstring;
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分前";
		} else if (deltime > 1) {
			shortstring = deltime + "秒前";
		} else {
			shortstring = "1秒前";
		}
		return shortstring;
	}

	public static String str_replace(String from, String to, String source) {
		StringBuffer bf = new StringBuffer("");
		StringTokenizer st = new StringTokenizer(source, from, true);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (tmp.equals(from)) {
				bf.append(to);
			} else {
				bf.append(tmp);
			}
		}
		return bf.toString();
	}

	public static final String getCurrentDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
		String date = sDateFormat.format(new Date());
		return date;
	}

	// 判断开始时间跟结束时间
	public static int timeCompareTo(String start, String end) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(formatter.parse(start));
			c2.setTime(formatter.parse(end));
		} catch (ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		return result;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getTimeDelta(Date date1, Date date2) {
		long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
		return timeDelta;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param dateStr1
	 *            :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2
	 *            :yyyy-MM-dd HH:mm:ss
	 */
	public static long getTimeDelta(String dateStr1, String dateStr2) {
		Date date1 = parseDateByPattern(dateStr1, "yyyy-MM-dd HH:mm");
		Date date2 = parseDateByPattern(dateStr2, "yyyy-MM-dd HH:mm");
		return getTimeDelta(date1, date2);
	}

	public static String getDates() {
		Date d = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.HOUR, now.get(Calendar.HOUR));
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		return sdf.format(now.getTime());
	}

	/**
	 * 将字符串转换为时间戳
	 * 
	 * @param user_time
	 * @return
	 */
	public static String getTime(String user_time) {
		String re_time = null;
		Date d;
		try {
			d = formatter.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str;// .substring(0, 10);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	public static long getStartTime(String time) {
		long date = (TimeUtils.getTimeLong(time) - 7200000) / 1000;
		// String t = formatter.format(new Date(date));
		Log.i("info", date + "---");
		return date;
	}

	public static long getTimeLong(String user_time) {
		long l = 0;
		Date d;
		try {
			d = formatter.parse(user_time);
			l = d.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static Date parseDateByPattern(String dateStr, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long TimeDiff(String pBeginTime, String pEndTime) throws Exception {
		Long beginL = formatter.parse(pBeginTime).getTime();
		Long endL = formatter.parse(pEndTime).getTime();
		Long day = (endL - beginL) / 86400000;
		Long hour = ((endL - beginL) % 86400000) / 3600000;
		Long min = ((endL - beginL) % 86400000 % 3600000) / 60000;
		Long mins = day * 24 * 60 + hour * 60 + min;
		Log.i("info", "相差" + day + "天" + hour + "小时" + min + "分");
		return mins;
	}

	public static void addEvent(Context context, String startTime, String title, String content, String city) {
		AccuApplication application = (AccuApplication) context.getApplicationContext();
		Time time = new Time("Asia/Hong_Kong");
		time.set(Long.parseLong(TimeUtils.getTime(startTime)));
		long calId = 1;
		ContentValues event = new ContentValues();
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(time.year, time.month, time.monthDay, time.hour, time.minute);
		long start = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(time.year, time.month, time.monthDay, time.hour, time.minute);// 开始时间和结束时间一致
		long end = endTime.getTimeInMillis();

		event.put(Events.DTSTART, start);
		event.put(Events.DTEND, end);
		event.put(Events.TITLE, title);
		event.put(Events.DESCRIPTION, content);
		event.put(Events.CALENDAR_ID, calId);
		event.put(Events.EVENT_LOCATION, city);
		event.put(Events.EVENT_TIMEZONE, "Asia/Shanghai");

		Uri newEvent = context.getContentResolver().insert(Events.CONTENT_URI, event);
		long id = Long.parseLong(newEvent.getLastPathSegment());

		ContentValues values = new ContentValues();
		if (application.sharedUtils.readInt("remind") == 1) {
			values.put(Reminders.MINUTES, 240);// 一天：1440，2天：2880，当天，240
		} else if (application.sharedUtils.readInt("remind") == 2) {
			values.put(Reminders.MINUTES, 1440);
		} else if (application.sharedUtils.readInt("remind") == 3) {
			values.put(Reminders.MINUTES, 2880);
		}
		values.put(Reminders.EVENT_ID, id);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		context.getContentResolver().insert(Reminders.CONTENT_URI, values);
	}
	
	

	/**
	 * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss"<br />
	 * 如果获取失败，返回null
	 * 
	 * @return
	 */
	public static String getUTCTimeStr() {
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		//重新设置时间
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		try {
			//转成固定的格式
			return SD_MDHM_S.format(new Date(cal.getTimeInMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	   /** 
     * 将UTC时间转换为东八区时间 
     * @param UTCTime 
     * @return 
     */  
    public static String getLocalTimeFromUTC(String UTCTime){  
        Date UTCDate = null ;
        String localTimeStr = null ;  
        try {  
            UTCDate = SD_MDHM_S.parse(UTCTime);  
            SD_MDHM_S.setTimeZone(TimeZone.getTimeZone("GMT-8")) ;  
            localTimeStr = SD_MDHM_S.format(UTCDate) ;  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
        return localTimeStr ;  
    }  
    
//	public static String utcToLocal(String UTCTime) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		df.setTimeZone(TimeZone.getDefault());
//		try {
//			Date date = df.parse(UTCTime);
//			String timeStr = SD_MDHM_S.format(date);
//			System.out.println(timeStr);
//			System.out.println(date.toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return UTCTime;
//	}
	
	public static DateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat mdFormat = new SimpleDateFormat("MM/dd");
	public static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy.MM/dd");
	public static SimpleDateFormat mdhmFormat = new SimpleDateFormat("MM/dd HH:mm");
	public static SimpleDateFormat ymdhmFormat = new SimpleDateFormat("yyyy.MM/dd HH:mm");
	
	public static String utcToLocal(String utcTime) {
		try {
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = utcFormater.parse(utcTime);
			SD_MDHM_S.setTimeZone(TimeZone.getDefault());
			String localTime = SD_MDHM_S.format(date);
			return localTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return utcTime;
	}
	
	
	private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    
	private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
	
	public static String relativeTimeStr(Long timestamp) {
		long delta = System.currentTimeMillis() - timestamp;
		return relative(delta);
	}
    
	public static String relativeTimeStr(String utcTime) {
		try {
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = utcFormater.parse(utcTime);
			long delta = System.currentTimeMillis() - date.getTime();
			return relative(delta);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return utcTime;
	}
	
	private static String relative(long delta) {
		if (delta < ONE_MINUTE) {
			long seconds = delta / 1000;
			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
		} else if (delta < ONE_HOUR) {
			long minutes = delta / ONE_MINUTE;
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		} else if (delta < ONE_DAY) {
			long hours = delta / ONE_HOUR;
			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
		} else {
			long days = delta / ONE_DAY;
			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}
	}
	
	
	public static Calendar cStart = Calendar.getInstance();
	public static Calendar cEnd = Calendar.getInstance();
	public static String getTimeAreaStr(int type, String startutc, String endutc) {
		try {
			utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date start = utcFormater.parse(startutc);
			Date end = utcFormater.parse(endutc);

			cStart.setTime(start);
			cEnd.setTime(end);
			
			boolean sameDay = false;
			boolean sameYear = false;
			if (cStart.get(Calendar.YEAR) == cEnd.get(Calendar.YEAR)) {
				sameYear = true;
				boolean sameMonth = cStart.get(Calendar.MONTH) == cEnd.get(Calendar.MONTH);
				if (sameMonth) {
					sameDay = cStart.get(Calendar.DAY_OF_MONTH) == cEnd.get(Calendar.DAY_OF_MONTH);
				}
			}

			switch (type) {
			case 1://显示月份  活动列表中的时间
				if (sameDay)
					return mdFormat.format(start);
				return mdFormat.format(start) + "~" + mdFormat.format(end);

			case 3://显示带年份 在活动详情、票券 时间用到
				if (sameDay)
					return ymdhmFormat.format(start) + " - " + hmFormat.format(end);
				if (sameYear)
					return ymdhmFormat.format(start) + " - " + mdhmFormat.format(end);
				return ymdhmFormat.format(start) + " - " + ymdhmFormat.format(end);
			default:
				return "";
			}
		} catch (Exception ex) {
			
		}
		return "";
	}
	
}
