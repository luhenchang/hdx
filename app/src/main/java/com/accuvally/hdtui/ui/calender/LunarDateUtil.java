package com.accuvally.hdtui.ui.calender;

import android.content.res.Resources;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.accuvally.hdtui.R;

public class LunarDateUtil {
	// 1900 to 2049.
	final private static long[] luYearData = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,// 1900
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,// 1910
			0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,// 1920
			0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,// 1930
			0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,// 1940
			0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,// 1950
			0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,// 1960
			0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,// 1970
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,// 1980
			0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x05ac0, 0x0ab60, 0x096d5, 0x092e0,// 1990
			0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,// 2000
			0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,// 2010
			0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,//
			0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	// Array iSolarLunarTable stored the offset days
	// in New Year of solar calendar and lunar calendar from 1901 to 2050;
	private static final char[] iSolarLunarOffsetTable = { 49, 38, 28, 46, 34, 24, 43, 32, 21, 40, // 1910
			29, 48, 36, 25, 44, 33, 22, 41, 31, 50, // 1920
			38, 27, 46, 35, 23, 43, 32, 22, 40, 29, // 1930
			47, 36, 25, 44, 34, 23, 41, 30, 49, 38, // 1940
			26, 45, 35, 24, 43, 32, 21, 40, 28, 47, // 1950
			36, 26, 44, 33, 23, 42, 30, 48, 38, 27, // 1960
			45, 35, 24, 43, 32, 20, 39, 29, 47, 36, // 1970
			26, 45, 33, 22, 41, 30, 48, 37, 27, 46, // 1980
			35, 24, 43, 32, 50, 39, 28, 47, 36, 26, // 1990
			45, 34, 22, 40, 30, 49, 37, 27, 46, 35, // 2000
			23, 42, 31, 21, 39, 28, 48, 37, 25, 44, // 2010
			33, 22, 40, 30, 49, 38, 27, 46, 35, 24, // 2020
			42, 31, 21, 40, 28, 47, 36, 25, 43, 33, // 2030
			22, 41, 30, 49, 38, 27, 45, 34, 23, 42, // 2040
			31, 21, 40, 29, 47, 36, 25, 44, 32, 22, // 2050
	};

	private static int[] lunarHolidaysTable = { 101, // 春节
			115, // 元宵�
			505, // 端午�
			707, // 七夕
			815, // 中秋
			909, // 重阳
			1208, // 蜡八
	};

	private static int[] solarHolidaysTable = { 101, // 元旦
			214, // 情人�
			308, // 妇女�
			312, // 植树�
			401, // 愚人�
			501, // 劳动�
			504, // 青年�
			601, // 儿童�
			701, // 建党�
			801, // 建军�
			910, // 教师�
			1001, // 国庆�
			1225, // 圣诞�
	};

	private static int[] solarHolidaysTable_TW = { 101, // 元旦
			214, // 情人�
			228, // 和平紀�
			308, // 妇女�
			312, // 植树�
			314, // 反侵略日
			329, // 青年�
			404, // 儿童�
			501, // 劳动�
			715, // 解严纪念�
			903, // 军人�
			928, // 教师�
			1010, // 国庆�
			1024, // 联合国日
			1025, // 光复�
			1112, // 國父誕辰
			1225, // 圣诞�
	};

	private static int[] solarHolidaysTable_HK = { 101, // 元旦
			214, // 情人�
			501, // 劳动�
			701, // 香港特别行政区成立纪念日
			1001, // 国庆�
			1225, // 圣诞�
	};

	private static int[] lunarHolidays = { R.string.the_spring_festival, // 春节
			R.string.lantern_festival, // 元宵�
			R.string.the_dragon_boat_festival, // 端午�
			R.string.double_seventh_day, // 七夕
			R.string.the_mid_autumn_festival, // 中秋�
			R.string.the_double_ninth_festival, // 重阳�
			R.string.the_laba_rice_porridge_festival, // 腊八
	};

	private static int[] solarHolidays = { R.string.new_years_day, // 元旦
			R.string.valentines_day, // 情人�
			R.string.international_womens_day, // 妇女�
			R.string.arbor_day, // 植树�
			R.string.fools_day, // 愚人�
			R.string.labour_day, // 劳动�
			R.string.chinese_youth_day, // 青年�
			R.string.childrens_day, // 儿童�
			R.string.partys_day, // 建党�
			R.string.the_armys_day, // 建军�
			R.string.teachers_day, // 教师�
			R.string.national_day, // 国庆�
			R.string.christmas_day, // 圣诞�
	};

	private static int[] solarHolidays_TW = { R.string.new_years_day, // 元旦
			R.string.valentines_day, // 情人�
			R.string.peace_day, // 和平紀�*
			R.string.international_womens_day, // 妇女�
			R.string.arbor_day, // 植树�
			R.string.anti_aggression_day, // 反侵略日
			R.string.tw_youth_day, // 青年�
			R.string.tw_childrens_day, // 儿童�
			R.string.labour_day, // 劳动�
			R.string.anniversary_of_lifting_martial_law, // 解严纪念�
			R.string.armed_forces_day, // 军人�
			R.string.teachers_day, // 教师�
			R.string.national_day, // 国庆�
			R.string.united_nations_day, // 联合国日
			R.string.retrocession_day, // 光复�
			R.string.national_father_day, // 國父誕辰 *
			R.string.christmas_day, // 圣诞�
	};

	private static int[] solarHolidays_HK = { R.string.new_years_day, // 元旦
			R.string.valentines_day, // 情人�
			R.string.labour_day, // 劳动�
			R.string.hksar_establishment_day, // 香港特别行政区成立纪念日
			R.string.national_day, // 国庆�
			R.string.christmas_day, // 圣诞�
	};

	private static int[] solarTerms = { R.string.slight_cold, // 小寒
			R.string.great_cold, // 大寒
			R.string.spring_begins, // 立春
			R.string.the_rains, // 雨水
			R.string.insects_awaken, // 惊蛰
			R.string.vernal_equinox, // 春分
			R.string.clear_and_bright, // 清明
			R.string.grain_rain, // 谷雨
			R.string.summer_begins, // 立夏
			R.string.grain_buds, // 小满
			R.string.grain_in_ear, // 芒种
			R.string.summer_solstice, // 夏至
			R.string.slight_heat, // 小暑
			R.string.great_heat, // 大暑
			R.string.autumn_begins, // 立秋
			R.string.stopping_the_heat, // 处暑
			R.string.white_dews, // 白露
			R.string.autumn_equinox, // 秋分
			R.string.cold_dews, // 寒露
			R.string.hoar_frost_falls, // 霜降
			R.string.winter_begins, // 立冬
			R.string.light_snow, // 小雪
			R.string.heavy_snow, // 大雪
			R.string.winter_solstice, // 冬至
	};

	// 每年的二十四节气对应的阳历日�
	// 每年的二十四节气对应的阳历日期几乎固定，平均分布于十二个月中
	// 1�2�3�4�5�6�
	// 小寒 大寒 立春 雨水 惊蛰 春分 清明 谷雨 立夏 小满 芒种 夏至
	// 7�8�9�10�11�12�
	// 小暑 大暑 立秋 处暑 白露 秋分 寒露 霜降 立冬 小雪 大雪 冬至
	// 这样每月两个节气对应数据都小�6,每月用一个字节存�高位存放第一个节气数�低位存放第二个节气的数据
	private static char[] solarTermsTable = { 0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1901
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x87, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1902
			0x96, 0xA5, 0x87, 0x96, 0x87, 0x87, 0x79, 0x69, 0x69, 0x69, 0x78, 0x78, // 1903
			0x86, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x78, 0x87, // 1904
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1905
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1906
			0x96, 0xA5, 0x87, 0x96, 0x87, 0x87, 0x79, 0x69, 0x69, 0x69, 0x78, 0x78, // 1907
			0x86, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1908
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1909
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1910
			0x96, 0xA5, 0x87, 0x96, 0x87, 0x87, 0x79, 0x69, 0x69, 0x69, 0x78, 0x78, // 1911
			0x86, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1912
			0x95, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1913
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1914
			0x96, 0xA5, 0x97, 0x96, 0x97, 0x87, 0x79, 0x79, 0x69, 0x69, 0x78, 0x78, // 1915
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1916
			0x95, 0xB4, 0x96, 0xA6, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x87, // 1917
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x77, // 1918
			0x96, 0xA5, 0x97, 0x96, 0x97, 0x87, 0x79, 0x79, 0x69, 0x69, 0x78, 0x78, // 1919
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1920
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x87, // 1921
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x77, // 1922
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x87, 0x79, 0x79, 0x69, 0x69, 0x78, 0x78, // 1923
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1924
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x87, // 1925
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1926
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x87, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1927
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1928
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1929
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1930
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x87, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1931
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1932
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1933
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1934
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1935
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1936
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1937
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1938
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1939
			0x96, 0xA5, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1940
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1941
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1942
			0x96, 0xA4, 0x96, 0x96, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1943
			0x96, 0xA5, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1944
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1945
			0x95, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x77, // 1946
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1947
			0x96, 0xA5, 0xA6, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 1948
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x79, 0x78, 0x79, 0x77, 0x87, // 1949
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x77, // 1950
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x79, 0x79, 0x79, 0x69, 0x78, 0x78, // 1951
			0x96, 0xA5, 0xA6, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 1952
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1953
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x78, 0x79, 0x78, 0x68, 0x78, 0x87, // 1954
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1955
			0x96, 0xA5, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 1956
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1957
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1958
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1959
			0x96, 0xA4, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1960
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1961
			0x96, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1962
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1963
			0x96, 0xA4, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1964
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1965
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1966
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1967
			0x96, 0xA4, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1968
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1969
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1970
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x79, 0x69, 0x78, 0x77, // 1971
			0x96, 0xA4, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1972
			0xA5, 0xB5, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1973
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1974
			0x96, 0xB4, 0x96, 0xA6, 0x97, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x77, // 1975
			0x96, 0xA4, 0xA5, 0xB5, 0xA6, 0xA6, 0x88, 0x89, 0x88, 0x78, 0x87, 0x87, // 1976
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 1977
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x78, 0x87, // 1978
			0x96, 0xB4, 0x96, 0xA6, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x77, // 1979
			0x96, 0xA4, 0xA5, 0xB5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1980
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x77, 0x87, // 1981
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1982
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x78, 0x79, 0x78, 0x69, 0x78, 0x77, // 1983
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x87, // 1984
			0xA5, 0xB4, 0xA6, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 1985
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1986
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x79, 0x78, 0x69, 0x78, 0x87, // 1987
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 1988
			0xA5, 0xB4, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1989
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 1990
			0x95, 0xB4, 0x96, 0xA5, 0x86, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1991
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 1992
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1993
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1994
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x76, 0x78, 0x69, 0x78, 0x87, // 1995
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 1996
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 1997
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 1998
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 1999
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2000
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2001
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 2002
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 2003
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2004
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2005
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2006
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x69, 0x78, 0x87, // 2007
			0x96, 0xB4, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x87, 0x78, 0x87, 0x86, // 2008
			0xA5, 0xB3, 0xA5, 0xB5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2009
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2010
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x78, 0x87, // 2011
			0x96, 0xB4, 0xA5, 0xB5, 0xA5, 0xA6, 0x87, 0x88, 0x87, 0x78, 0x87, 0x86, // 2012
			0xA5, 0xB3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x87, // 2013
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2014
			0x95, 0xB4, 0x96, 0xA5, 0x96, 0x97, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 2015
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x87, 0x88, 0x87, 0x78, 0x87, 0x86, // 2016
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x87, // 2017
			0xA5, 0xB4, 0xA6, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2018
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 2019
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x86, // 2020
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2021
			0xA5, 0xB4, 0xA5, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2022
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x79, 0x77, 0x87, // 2023
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x96, // 2024
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2025
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2026
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 2027
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x96, // 2028
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2029
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2030
			0xA5, 0xB4, 0x96, 0xA5, 0x96, 0x96, 0x88, 0x78, 0x78, 0x78, 0x87, 0x87, // 2031
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x96, // 2032
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x86, // 2033
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x78, 0x88, 0x78, 0x87, 0x87, // 2034
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2035
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x96, // 2036
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x86, // 2037
			0xA5, 0xB3, 0xA5, 0xA5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2038
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2039
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x96, // 2040
			0xA5, 0xC3, 0xA5, 0xB5, 0xA5, 0xA6, 0x87, 0x88, 0x87, 0x78, 0x87, 0x86, // 2041
			0xA5, 0xB3, 0xA5, 0xB5, 0xA6, 0xA6, 0x88, 0x88, 0x88, 0x78, 0x87, 0x87, // 2042
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2043
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x88, 0x87, 0x96, // 2044
			0xA5, 0xC3, 0xA5, 0xB4, 0xA5, 0xA6, 0x87, 0x88, 0x87, 0x78, 0x87, 0x86, // 2045
			0xA5, 0xB3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x88, 0x78, 0x87, 0x87, // 2046
			0xA5, 0xB4, 0x96, 0xA5, 0xA6, 0x96, 0x88, 0x88, 0x78, 0x78, 0x87, 0x87, // 2047
			0x95, 0xB4, 0xA5, 0xB4, 0xA5, 0xA5, 0x97, 0x87, 0x87, 0x88, 0x86, 0x96, // 2048
			0xA4, 0xC3, 0xA5, 0xA5, 0xA5, 0xA6, 0x97, 0x87, 0x87, 0x78, 0x87, 0x86, // 2049
			0xA5, 0xC3, 0xA5, 0xB5, 0xA6, 0xA6, 0x87, 0x88, 0x78, 0x78, 0x87, 0x87 // 2050
	};

	public static final int MIN_LUNAR_YEAR = 1900;
	public static final int MAX_LUNAR_YEAR = 2050;

	public static String getSolarTerm(Resources res, Calendar c) {
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH);
		final int day = c.get(Calendar.DATE);

		char flag = 0x00;
		flag = solarTermsTable[(year - 1901) * 12 + month];

		int termDay = flag % 16 + 15;

		if (day == termDay) {
			return res.getString(solarTerms[2 * month + 1]);
		}

		termDay = 15 - (flag / 16);

		if (day == termDay) {
			return res.getString(solarTerms[2 * month]);
		}

		return null;
	}

	/*
	 * private static Context context = ImageAdapter.mContext; // Get the
	 * Resources object from our context private static Resources res =
	 * context.getResources();
	 */

	/**
	 * Get days of lunar year
	 * 
	 * @param y
	 * @return
	 */
	final private static int yrDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((luYearData[y - MIN_LUNAR_YEAR] & i) != 0)
				sum += 1;
		}
		return (sum + rMthDays(y));
	}

	final public static int rMthDays(int y) {
		if (rMonth(y) != 0) {
			if ((luYearData[y - MIN_LUNAR_YEAR] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// 返回闰几�
	final public static int rMonth(int y) {
		return (int) (luYearData[y - MIN_LUNAR_YEAR] & 0xf);
	}

	// 返回农历年，�的天�
	final public static int mthDays(int y, int m) {
		if ((luYearData[y - MIN_LUNAR_YEAR] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	// 返回是否是闰�
	static boolean isSolarLeapYear(int iYear) {
		return ((iYear % 4 == 0) && (iYear % 100 != 0) || iYear % 400 == 0);
	}

	// 返回公历年月 的天�
	static int getSolarYearMonthDays(int iYear, int iMonth) {
		if ((iMonth == 1) || (iMonth == 3) || (iMonth == 5) || (iMonth == 7) || (iMonth == 8) || (iMonth == 10) || (iMonth == 12)) {
			return 31;
		} else if ((iMonth == 4) || (iMonth == 6) || (iMonth == 9) || (iMonth == 11)) {
			return 30;
		} else if (iMonth == 2) {
			if (isSolarLeapYear(iYear))
				return 29;
			else
				return 28;
		} else {
			return 0;
		}
	}

	/**
	 * Translate lunar date to solar date
	 * 
	 * @param y
	 *            lunar year
	 * @param m
	 *            lunar month of year, start from 1, >12 for leap month
	 * @param d
	 *            lunar day
	 * @return filed array in follow order: <li>solar year <li>solar month of
	 *         year, start from 1 <li>solar day of month
	 */
	public static int[] lunarToSolar(int y, int m, int d) {
		int[] solar_date = new int[3];
		int iSYear, iSMonth, iSDay;
		int iOffsetDays = getLunarNewYearOffsetDays(y, m, d) + iSolarLunarOffsetTable[y - 1901];

		int iYearDays = isSolarLeapYear(y) ? 366 : 365;

		if (iOffsetDays >= iYearDays) {
			iSYear = y + 1;
			iOffsetDays -= iYearDays;
		} else {
			iSYear = y;
		}
		iSDay = iOffsetDays + 1;
		for (iSMonth = 1; iOffsetDays >= 0; iSMonth++) {
			iSDay = iOffsetDays + 1;
			iOffsetDays -= getSolarYearMonthDays(iSYear, iSMonth);
		}
		iSMonth--;

		solar_date[0] = iSYear;
		solar_date[1] = iSMonth;
		solar_date[2] = iSDay;
		return solar_date;
	}

	// 如果m是闰�m > 12
	private static int getLunarNewYearOffsetDays(int y, int m, int d) {
		int iOffsetDays = 0;
		int iLeapMonth = rMonth(y);

		if ((iLeapMonth > 0) && (iLeapMonth == m - 12)) {
			m = iLeapMonth;
			iOffsetDays += mthDays(y, m);
		}

		for (int i = 1; i < m; i++) {
			iOffsetDays += mthDays(y, i);
			if (i == iLeapMonth)
				iOffsetDays += rMthDays(y);
		}

		iOffsetDays += d - 1;

		return iOffsetDays;
	}

	/**
	 * Translate solar date to lunar date
	 * 
	 * @param y
	 * @param m
	 *            start from 0
	 * @param d
	 * @return field array that in follow order: <li>lunar year <li>lunar month
	 *         of year, start from 1 <li>lunar day of month <li>years from 1864,
	 *         can be used to calculate TIANGAN and DIZHI <li>unknown value <li>
	 *         unknown value <li>is this leap month
	 */
	final public static long[] calLunar(int y, int m, int d) {
		long[] lunar_date = new long[7];
		int i = 0, temp = 0, leap = 0;
		long offset = getDayOffset(y, m, d);
		lunar_date[5] = offset + 40;
		lunar_date[4] = 14;

		for (i = MIN_LUNAR_YEAR; i < MAX_LUNAR_YEAR && offset > 0; i++) {
			temp = yrDays(i);
			offset -= temp;
			lunar_date[4] += 12;
		}
		if (offset < 0) {
			offset += temp;
			i--;
			lunar_date[4] -= 12;
		}
		lunar_date[0] = i;
		lunar_date[3] = i - 1864;
		leap = rMonth(i);
		lunar_date[6] = 0;

		for (i = 1; i < 13 && offset > 0; i++) {
			if (leap > 0 && i == (leap + 1) && lunar_date[6] == 0) {
				--i;
				lunar_date[6] = 1;
				temp = rMthDays((int) lunar_date[0]);
			} else {
				temp = mthDays((int) lunar_date[0], i);
			}

			if (lunar_date[6] == 1 && i == (leap + 1))
				lunar_date[6] = 0;
			offset -= temp;
			if (lunar_date[6] == 0)
				lunar_date[4]++;
		}

		if (offset == 0 && leap > 0 && i == leap + 1) {
			if (lunar_date[6] == 1) {
				lunar_date[6] = 0;
			} else {
				lunar_date[6] = 1;
				--i;
				--lunar_date[4];
			}
		}
		if (offset < 0) {
			offset += temp;
			--i;
			--lunar_date[4];
		}
		lunar_date[1] = i;
		lunar_date[2] = offset + 1;
		return lunar_date;
	}

	/**
	 * Get day offset from 1900-01-31
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	private final static int getDayOffset(int y, int m, int d) {
		int dayOffset = 0;

		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.clear();

		for (int i = MIN_LUNAR_YEAR; i < y; ++i) {
			if (cal.isLeapYear(i)) {
				dayOffset += 366;
			} else {
				dayOffset += 365;
			}
		}

		cal.set(y, m, d);
		dayOffset += cal.get(Calendar.DAY_OF_YEAR);

		cal.set(MIN_LUNAR_YEAR, 0, 31);
		dayOffset -= cal.get(Calendar.DAY_OF_YEAR);

		return dayOffset;
	}

	public final static String getDayString(Resources res, int day) {
		String a = "";
		if (day == 10)
			return res.getString(R.string.lunar_chu_shi);
		if (day == 20)
			return res.getString(R.string.lunar_er_shi);
		if (day == 30)
			return res.getString(R.string.lunar_san_shi);
		int two = (int) ((day) / 10);
		if (two == 0)
			a = res.getString(R.string.lunar_chu);
		if (two == 1)
			a = res.getString(R.string.lunar_shi);
		if (two == 2)
			a = res.getString(R.string.lunar_er);
		if (two == 3)
			a = res.getString(R.string.lunar_san);
		int one = (int) (day % 10);
		switch (one) {
		case 1:
			a += res.getString(R.string.lunar_yi);
			break;
		case 2:
			a += res.getString(R.string.lunar_er);
			break;
		case 3:
			a += res.getString(R.string.lunar_san);
			break;
		case 4:
			a += res.getString(R.string.lunar_si);
			break;
		case 5:
			a += res.getString(R.string.lunar_wu);
			break;
		case 6:
			a += res.getString(R.string.lunar_liu);
			break;
		case 7:
			a += res.getString(R.string.lunar_qi);
			break;
		case 8:
			a += res.getString(R.string.lunar_ba);
			break;
		case 9:
			a += res.getString(R.string.lunar_jiu);
			break;
		}
		return a;
	}

	public static String getMonthString(Resources res, int k) {
		if (k > 12)
			return null;

		switch (k) {
		case 0:
			return "";
		case 1:
			return res.getString(R.string.lunar_zheng);
		case 2:
			return res.getString(R.string.lunar_er);
		case 3:
			return res.getString(R.string.lunar_san);
		case 4:
			return res.getString(R.string.lunar_si);
		case 5:
			return res.getString(R.string.lunar_wu);
		case 6:
			return res.getString(R.string.lunar_liu);
		case 7:
			return res.getString(R.string.lunar_qi);
		case 8:
			return res.getString(R.string.lunar_ba);
		case 9:
			return res.getString(R.string.lunar_jiu);
		case 10:
			return res.getString(R.string.lunar_shi);
		case 11:
			return res.getString(R.string.lunar_shi_yi);
		case 12:
			return res.getString(R.string.lunar_shi_er);

		}
		return null;

	}

	public static String getYearString(Resources res, int year) {
		StringBuffer sb = new StringBuffer();
		int n = 0;
		int k = year;
		do {
			n = k % 10; // 得到个位�
			k = k / 10; // 得到剩下的几位数
			sb.insert(0, getDigitString(res, n));
		} while (k > 0);

		return sb.toString();
	}

	/**
	 * get String for lunar date, eg: 三月廿五
	 * 
	 * @param res
	 * @param millisecond
	 * @return
	 */
	public static String getString(Resources res, Calendar c) {
		return solar2lunar(res, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Get lunar date string from solar date
	 * 
	 * @param res
	 * @param year
	 * @param month
	 *            month of year, start from 0
	 * @param day
	 * @return lunar date string (闰四月初一)
	 */
	public static String solar2lunar(Resources res, int year, int month, int day) {
		StringBuffer sLunar = new StringBuffer();
		
		
		long[] l = calLunar(year, month-1, day);
		Calendar calendar =Calendar.getInstance();
		calendar.set(year, month-1, day);
		String string = getHoliday(res, l, calendar);
		if(string!=null){
			return string;
		}else{
			//去掉农历
			return "";
		}
	 
//		try {
//			// if (l[6] == 1)
//			// sLunar.append(res.getString(R.string.lunar_leap));
//			// sLunar.append(getMonthString(res, (int) l[1]));
//			// sLunar.append(res.getString(R.string.lunar_yue));
//			sLunar.append(getDayString(res, (int) (l[2])));
//			
//			Log.d("d", year+"-"+month+"-"+day+" h="+string+"  "+ sLunar.toString());
//			return sLunar.toString();
//		} finally {
//			sLunar = null;
//		}
	}

	public static String getHoliday(Resources res, long[] lunar, Calendar c) {
		try {
			// 查看是否是公历节�
			int solarMonth = c.get(Calendar.MONTH)+1;
			int solarDay = c.get(Calendar.DATE);

			int[] solarHolidayDates = null;
			int[] solarHolidayStrings = null;
			if (res.getConfiguration().locale.getCountry().equals("TW")) {
				solarHolidayDates = solarHolidaysTable_TW;
				solarHolidayStrings = solarHolidays_TW;
			} else if (res.getConfiguration().locale.getCountry().equals("HK")) {
				solarHolidayDates = solarHolidaysTable_HK;
				solarHolidayStrings = solarHolidays_HK;
			} else {
				solarHolidayDates = solarHolidaysTable;
				solarHolidayStrings = solarHolidays;
			}
			int length = solarHolidayDates.length;
			for (int i = 0; i < length; i++) {
				if (((solarHolidayDates[i] / 100) == solarMonth) && ((solarHolidayDates[i] % 100) == solarDay)) {
					return res.getString(solarHolidayStrings[i]);
				}
			}

			// 如果是香港版，判断是否是复活节相关节�
			if (res.getConfiguration().locale.getCountry().equals("HK")) {
				String easter = isEasterDay(res, c);
				if (!TextUtils.isEmpty(easter)) {
					return easter;
				}
			}

			// 一般农历假日不过闰月的�如果该月是闰月，直接返回�
			if (lunar[6] == 1) {
				return null;
			}

			int lunarMonth = (int) lunar[1];
			int lunarDay = (int) lunar[2];

			length = lunarHolidaysTable.length;
			for (int i = 0; i < length; i++) {
				if (((lunarHolidaysTable[i] / 100) == lunarMonth) && ((lunarHolidaysTable[i] % 100) == lunarDay)) {
					return res.getString(lunarHolidays[i]);
				}
			}
		} catch (Exception e) {

		}

		return null;
	}

	private static String getDigitString(Resources res, int digit) {
		switch (digit) {
		case 0:
			return res.getString(R.string.lunar_ling);
		case 1:
			return res.getString(R.string.lunar_yi);
		case 2:
			return res.getString(R.string.lunar_er);
		case 3:
			return res.getString(R.string.lunar_san);
		case 4:
			return res.getString(R.string.lunar_si);
		case 5:
			return res.getString(R.string.lunar_wu);
		case 6:
			return res.getString(R.string.lunar_liu);
		case 7:
			return res.getString(R.string.lunar_qi);
		case 8:
			return res.getString(R.string.lunar_ba);
		case 9:
			return res.getString(R.string.lunar_jiu);
		default:
			return "";
		}
	}

	/**
	 * Get lunar string for lunar date
	 * 
	 * @param res
	 * @param year
	 *            lunar year, <=0 if not show year
	 * @param month
	 *            lunar month, start from 0, >=12 for leap month
	 * @param day
	 *            lunar day
	 * @return lunar string (2012年闰四月十六)
	 */
	public static String getLunarString(Resources res, int year, int month, int day) {
		StringBuilder sb = new StringBuilder();

		if (year > 0) {
			sb.append(Integer.toString(year)).append(res.getString(R.string.lunar_nian));
		}
		if (month >= 12) {
			sb.append(res.getString(R.string.lunar_leap));
			month -= 12;
		}
		sb.append(getMonthString(res, month + 1));
		sb.append(res.getString(R.string.lunar_yue));
		sb.append(getDayString(res, day));

		return sb.toString();
	}

	/**
	 * format lunar date
	 * 
	 * @param year
	 * @param month
	 *            of year, start from 0, >=12 for leap month
	 * @param day
	 *            lunar day
	 * @return lunar string (2012-2-30)
	 */
	public static String formatLunarDate(int year, int monthOfYear, int dayOfMonth) {
		StringBuilder sb = new StringBuilder();

		if (year > 0) {
			sb.append(year);
			sb.append("-");
		}

		sb.append(monthOfYear + 1);
		sb.append("-");
		sb.append(dayOfMonth);

		return sb.toString();
	}

	/**
	 * @param lunarDate
	 *            (2012-2-30 or 2-30)
	 * @return dayOfMonth = date[0] monthOfYear = date[1] year = date[2]
	 */
	public static int[] parseLunarDate(String lunarDate) {
		if (TextUtils.isEmpty(lunarDate)) {
			return null;
		}

		int[] result = new int[3];

		try {
			String[] date = lunarDate.split("-");
			if (date.length == 2) {
				result[0] = Integer.parseInt(date[1].trim());
				result[1] = Integer.parseInt(date[0].trim()) - 1;
				result[2] = 0;
			} else if (date.length == 3) {
				result[0] = Integer.parseInt(date[2].trim());
				result[1] = Integer.parseInt(date[1].trim()) - 1;
				result[2] = Integer.parseInt(date[0].trim());
			} else {
				result = null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	/**
	 * Get solar birthdays by lunar date
	 * 
	 * @param lunarYear
	 * @param lunarMonth
	 *            start from 0, >=12 for leap month
	 * @param lunarDay
	 * @return order by time DESC if more than 1 birthday, so the first is
	 *         birthday of leap month. In each birthday item, values are in
	 *         order of year, monthOfYear, dayOfMonth and isDayAdjust.
	 */
	public static int[][] getLunarBirthdays(int lunarYear, int lunarMonth, int lunarDay) {
		int[][] results;
		lunarMonth += 1;

		// 如果这年的闰月与m相同，则增加一个闰月的生日提醒
		if (lunarMonth > 12) {
			if (lunarMonth - 12 == rMonth(lunarYear)) {
				results = new int[2][];

				// 调整日期，比如lunarDay�0日，但本月是小月，最�9�
				int day = Math.min(rMthDays(lunarYear), lunarDay);
				int[] lunarParts = lunarToSolar(lunarYear, lunarMonth, day);
				results[0] = new int[] { lunarParts[0], lunarParts[1] - 1, lunarParts[2], day == lunarDay ? 0 : 1 };
			} else {
				results = new int[1][];
			}

			lunarMonth -= 12;
		} else {
			results = new int[1][];
		}

		// 给正常月份增加生日提�
		// 调整日期，比如lunarDay�0日，但本月是小月，最�9�
		int day = Math.min(mthDays(lunarYear, lunarMonth), lunarDay);
		int[] lunarParts = lunarToSolar(lunarYear, lunarMonth, day);
		results[results.length - 1] = new int[] { lunarParts[0], lunarParts[1] - 1, lunarParts[2], day == lunarDay ? 0 : 1 };

		return results;
	}

	/**
	 * Get next lunar birthday in solar date, start from this day
	 * 
	 * @param lunarMonth
	 *            start from 0, >= 12 for leap month
	 * @param lunarDay
	 * @return milliseconds of birthday time in default time zone
	 */
	public static long getNextLunarBirthday(int lunarMonth, int lunarDay) {
		Time time = new Time();
		time.setToNow();

		long[] lunarParts = calLunar(time.year, time.month, time.monthDay);
		int lunarYear = (int) lunarParts[0];

		time.hour = time.minute = time.second = 0;
		long timeNow = time.normalize(false);
		long timeResult = Long.MAX_VALUE;

		while (Long.MAX_VALUE == timeResult && lunarYear >= MIN_LUNAR_YEAR && lunarYear < MAX_LUNAR_YEAR) {
			int[][] birthdays = getLunarBirthdays(lunarYear, lunarMonth, lunarDay);
			for (int[] birthday : birthdays) {
				time.set(birthday[2], birthday[1], birthday[0]);
				long timeInMillis = time.normalize(false);
				if (timeInMillis >= timeNow) {
					timeResult = Math.min(timeResult, timeInMillis);
				}
			}

			++lunarYear;
		}

		if (Long.MAX_VALUE == timeResult) {
			timeResult = 0;
		}

		return timeResult;
	}

	// 香港节假日， 计算复活节的日期
	// 计算出来当天是复活节�前一天是耶稣受难节翌日， 前两天是耶稣受难�
	private static final HashMap<Integer, Integer> sEasterCache = new HashMap<Integer, Integer>();

	private static String isEasterDay(Resources res, Calendar calendar) {
		final int y = calendar.get(Calendar.YEAR);
		int d = 0;
		if (!sEasterCache.containsKey(y)) {
			int n = y - 1900;
			int a = n % 19;
			int q = (int) Math.floor(n / 4);
			int b = (int) Math.floor((7 * a + 1) / 19);
			int m = (11 * a + 4 - b) % 29;
			int w = (n + q + 31 - m) % 7;
			d = 25 - m - w;
			sEasterCache.put(y, d);
		} else {
			d = sEasterCache.get(y);
		}

		int month = 0;
		int day = 0;
		if (d == 0) {
			month = 3;
			day = 31;
		} else if (d > 0) {
			month = 4;
			day = d;
		} else {
			month = 3;
			day = 31 + d;
		}

		Date date1 = new Date();
		date1.setMonth(calendar.get(Calendar.MONTH));
		date1.setDate(calendar.get(Calendar.DATE));

		Date date2 = new Date();
		date2.setMonth(month - 1);
		date2.setDate(day);

		final int dayOffset = (int) ((date2.getTime() - date1.getTime()) / DateUtils.DAY_IN_MILLIS);
		int resId = 0;
		if (dayOffset == 0) {
			// 当天是复活节
			resId = R.string.easter;
		} else if (dayOffset == 1) {
			// �个节日太长，目前的UI显示不下，所以暂时不显示
			// resId = R.string.day_following_good_friday;
		} else if (dayOffset == 2) {
			// resId = R.string.good_friday;
		}
		if (resId > 0) {
			return res.getString(resId);
		}
		return null;
	}

	/**
	 * 根据西元的生日时辰获取某个人的生辰八字，以及农历生日�
	 * 
	 * 生辰八字是指:年、月、日、时，各用干支的两个字来表示，一�个字，叫生辰八字�
	 * 
	 * 天干地支如何推算�
	 * 
	 * 1、排年柱：排�0甲子序列，将自己的虚龄生年放入其中，逆数上行即可找到相对公元年的干支是什么了�
	 * 或者比照当年或上年的干支，也能找出相应的你所需要的干支来。如你是1987�月生人， 只知道今年是兔年（丁卯）不知�7年是什么干支，
	 * 
	 * 1. 甲子 2.乙丑 3.丙寅 4.丁卯 5.戊辰 6.己巳 7.庚午 8.辛未 9.壬申 10.癸酉 11.甲戌 12.乙亥 13.丙子
	 * 14.丁丑 15.戊寅 16.己卯 17.庚辰 18.辛巳 19.壬午 20.癸未 21.甲申 22.乙酉 23.丙戌 24.丁亥 25.戊子
	 * 26.己丑 27.庚寅 28.辛卯 29.壬辰 30.癸巳 31.甲午 32.乙未 33.丙申 34.丁酉 35.戊戌 36.己亥 37.庚子
	 * 38.辛丑 39.壬寅 40.癸卯 41.甲辰 42.乙巳 43.丙午 44.丁未 45.戊申 46.己酉 47.庚戌 48.辛亥 49.壬子
	 * 50.癸丑 51.甲寅 52.乙卯 53.丙辰 54.丁巳 55.戊午 56.己未 57.庚申 58.辛酉 59.壬戌 60.癸亥
	 * 
	 * 2、排月柱：月柱的天干和地支排法在各类命书中都有这样的口诀�甲己之年丙作首， 乙庚之岁戊为头， 丙辛岁首寻庚起， 丁壬壬位顺行流，
	 * 若言戊癸何方发， 甲寅之上好追求�
	 * 
	 * 这歌诀也称《五虎遁年上起月歌》。至于何为“五虎遁”这里就不展开讲了，有兴趣的朋友找到相关命理书看一下便知�
	 * 这歌诀的意思是：年干逢甲或己，则该年的月份天干地支就以正月是丙寅排起，依次为二月丁卯，三月戊辰，四月己已…�
	 * 这样依次排到十二月；年干是乙、庚的则以戊寅为一月起始；年干是丙辛的，则以庚寅起始；年干为丁、壬的则以壬寅为一月；
	 * 年干为戊、癸则以甲寅起为一月。其余月份均可顺序推得。注意：论月份干支，地支始终是不变的�
	 * 即一月为寅、二月为卯、三月为辰、四月为已、五月为午、六月为未、七月为申、八月为酉、九月为戌，十月为亥，十一月为子，十二月为丑�
	 * 这是永远不变的。变的只是天干，随着年柱的天干之变而变�
	 * 
	 * 这里，我们已看到了十二地支的两个作用：一是用来表示年份，它与生肖相对应：
	 * 子为鼠、丑为牛、寅为虎、卯为兔、辰为龙、已为蛇、午为马、未为羊、申为猴、酉为鸡、戌为狗、亥为猪�二是用来表示月份，如前所说�
	 * 
	 * 3、日柱和时（时辰）柱也是类似�
	 **/
	public static class BirthHoroscope {

		private static final SimpleDateFormat sChineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		private static final String BASE_DATE_STRING = "1900-1-31";
		private static final long DAY_IN_MILLS = 86400000L;

		private static boolean sIsInitialized;
		private static String[] sTianGan;
		private static String[] sDiZhi;
		private static String[] sJiaZi;
		private static Date sBaseDate;

		private Calendar mCalendar;
		private int mYear;
		private int mMonth;
		private int mDay;
		private int mHour;

		public BirthHoroscope(Calendar c, int year, int month, int day, int hour) {
			mCalendar = c;
			mYear = year;
			mMonth = month;
			mDay = day;
			mHour = hour;
		}

		public static BirthHoroscope newInstance(Resources res, Calendar calendar) {
			try {
				if (!sIsInitialized) {
					sTianGan = res.getStringArray(R.array.tian_gan);
					sDiZhi = res.getStringArray(R.array.di_zhi);
					sJiaZi = res.getStringArray(R.array.jia_zi);
					sBaseDate = sChineseDateFormat.parse(BASE_DATE_STRING);
					sIsInitialized = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			int lunarYear, lunarMonth, lunarDay;
			int leapMonth = 0;

			// 求出�900��1日相差的天数
			int dayOffset = (int) ((calendar.getTime().getTime() - sBaseDate.getTime()) / DAY_IN_MILLS);
			lunarDay = dayOffset + 40;
			lunarMonth = 14;

			// 用offset减去每农历年的天� 计算当天是农历第几天
			// 最终结果是农历的年� offset是当年的第几�
			int iYear, dayOfYear = 0;
			for (iYear = 1900; iYear < 2050 && dayOffset > 0; iYear++) {
				dayOfYear = yrDays(iYear);
				dayOffset -= dayOfYear;
				lunarMonth += 12;
			}

			if (dayOffset < 0) {
				dayOffset += dayOfYear;
				iYear--;
				lunarMonth -= 12;
			}

			// 农历年份
			int year = iYear;
			lunarYear = iYear - 1864;
			leapMonth = rMonth(iYear);
			boolean isLeapYear = false;

			// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几�
			int iMonth, daysOfMonth = 0;
			for (iMonth = 1; iMonth < 13 && dayOffset > 0; iMonth++) {
				// 闰月
				if (leapMonth > 0 && iMonth == (leapMonth + 1) && !isLeapYear) {
					--iMonth;
					isLeapYear = true;
					daysOfMonth = rMthDays(year);
				} else {
					daysOfMonth = mthDays(year, iMonth);
				}
				dayOffset -= daysOfMonth;
				// 解除闰月
				if (isLeapYear && iMonth == (leapMonth + 1)) {
					isLeapYear = false;
				}
				if (!isLeapYear) {
					lunarMonth++;
				}
			}

			// offset�时，并且刚才计算的月份是闰月，要校正
			if (dayOffset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
				if (isLeapYear) {
					isLeapYear = false;
				} else {
					isLeapYear = true;
					--iMonth;
					--lunarMonth;
				}
			}

			// offset小于0时，也要校正
			if (dayOffset < 0) {
				dayOffset += daysOfMonth;
				--iMonth;
				--lunarMonth;
			}

			int month = iMonth;
			int day = dayOffset + 1;
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			return new BirthHoroscope(calendar, year, month, day, hour);
		}

		public String getBirthHoroscope() {
			// 1864年是甲子年，每隔六十年一个甲�
			int yearOffset = (mYear - 1864) % 60;
			// 没有过春节的话那么年还算上一年的，此处求的年份的干支
			String year = sJiaZi[yearOffset];

			yearOffset = yearOffset % 5;

			/**
			 * 年上起月 甲己之年丙作首，乙庚之岁戊为头， 丙辛必定寻庚起，丁壬壬位顺行流， 更有戊癸何方觅，甲寅之上好追求�
			 */
			// 求的月份的干�
			int monthOffset = (yearOffset + 1) * 2;
			if (monthOffset == 10) {
				monthOffset = 0;
			}
			String month = sTianGan[(monthOffset + mMonth - 1) % 10] + sDiZhi[(mMonth + 2 - 1) % 12];

			/*
			 * 求出�900��1日甲辰日相差的天�甲辰日是第四十天
			 */
			// 求的日的干支
			int dayOffset = getRiZhu();
			String day = sJiaZi[dayOffset];

			/**
			 * 日上起时 甲己还生甲，乙庚丙作初， 丙辛从戊起，丁壬庚子居， 戊癸何方发，壬子是真途�
			 */
			// 求得时辰的干�
			int hourOffset = (dayOffset % 5) * 2;
			int lunarHourIndex = getLunarHourIndex(mHour);
			String hour = sTianGan[(hourOffset + lunarHourIndex) % 10] + sDiZhi[lunarHourIndex];

			// 年月日时的天干地�
			return year + month + day + hour;
		}

		/**
		 * 子时23.00�.00 丑时1.00�.00 寅时3.00�.00 卯时5.00�.00, 辰时7.00�.00
		 * 巳时9.00�1.00 午时11.00�3.00 未时13.00�5.00 申时15.00�7.00 酉时17.00�9.00
		 * 戌时19.00�1.00 亥时21.00�3.00
		 **/
		private static int getLunarHourIndex(int hour) {
			return ((hour + 1) % 24) / 2;
		}

		/*
		 * 求出�900��1日甲辰日相差的天�甲辰日是第四十天
		 */
		// 求的日的干支
		public int getRiZhu() {
			int dayOffset = (int) ((mCalendar.getTime().getTime() - sBaseDate.getTime()) / DAY_IN_MILLS);
			dayOffset = (dayOffset + 40) % sJiaZi.length;
			return dayOffset;
		}
	}
}
