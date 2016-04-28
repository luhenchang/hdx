package com.accuvally.hdtui.model;

import java.io.Serializable;

import com.accuvally.hdtui.utils.TimeUtils;

public class AccuBean implements Serializable {

	public String id;
	public String title;
	public String logo;
	
	public String follows;
	public String desc;

	public String shareurl;

	public boolean isfollow;

	public String statusstr;

	public String address;

	public String startutc;

	public String endutc;

	/** 1 活动推 0活动行 **/
	public int sourcetype;

	public String url;

	public int likeNum;
	public String RemindStr;
	public String pricearea;
	public String visitnum;// 访问数
	public boolean isRush;// 是否是抢票活动

	public String getTimeStr() {
		return TimeUtils.getTimeAreaStr(1, startutc, endutc);
	}

}

/*
 *"id": 5255120108800,                                       -- 活动id
 "title": "apec期间【单身交友】主题活动",                   		-- 活动标题
 "shareurl": "http://192.168.0.107/event/5255120108800",    -- 活动分享地址
 "isfollow": false,                                         -- 是否已关注
 "statusstr": "正在报名",                                   	-- 活动状态文字描述 
 "address": "东直门东环广场负一层约吧",                     		-- 活动地址
 "startutc": "2014-11-07 18:30",                            -- 开始UTC时间
 "endutc": "2014-12-31 21:30",                              -- 结束UTC时间
 "sourcetype": 0,                                           -- 活动来源，0 表示来源于活动行，1表示来源于活动推
 "url": "http://192.168.0.107/event/5255120108800",         
 "logo": "http://192.168.0.107/logo/701771668527511.jpg"    -- 活动Logo
 */
