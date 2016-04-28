package com.accuvally.hdtui.ui.calender;

import java.io.Serializable;

import android.content.res.Resources;
/**
 * 自定义的日期类
 *
 */
public class CustomDate implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;
 
	public String lunarDay;
	public boolean applyed;
	
	public CustomDate(int year,int month,int day,String lunarDay){
		if(month > 12){
			month = 1;
			year++;
		}else if(month <1){
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
		this.lunarDay = lunarDay;
	}
	
	/***
	 * 
	 * @param res
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static CustomDate newInstence(Resources res,int year,int month,int day){
		CustomDate customDate =new CustomDate(year,month,day,"");
		customDate.lunarDay = LunarDateUtil.solar2lunar(res, year, month, day);
		return customDate;
	}
 
	
//	public CustomDate(int year,int month,int day){
//		if(month > 12){
//			month = 1;
//			year++;
//		}else if(month <1){
//			month = 12;
//			year--;
//		}
//		this.year = year;
//		this.month = month;
//		this.day = day;
//	}
	
	public CustomDate(){
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
	}
	
	public static CustomDate modifiDayForObject(Resources res,CustomDate date,int day){
		CustomDate modifiDate = newInstence(res,date.year,date.month,day);
		return modifiDate;
	}
	
	
	@Override
	public String toString() {
		return year+"-"+(month<10?("0"+month):month)+"-"+(day<10?("0"+day):day);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}
	
	public boolean isSameDay(CustomDate date){
		return this.year == date.year && this.month == date.month && this.day == date.day;
	}
	
	public boolean after(CustomDate date){
		if(this.year>date.year){
			return true;
		}else if(this.year==date.year){
			if(this.month > date.month){
				return true;
			}else if(this.month == date.month){
				if(this.day>date.day){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomDate other = (CustomDate) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

}
