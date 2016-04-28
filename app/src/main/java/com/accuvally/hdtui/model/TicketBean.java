package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

import com.accuvally.hdtui.utils.TimeUtils;

/*
 * "id": "4019387733830",                            --  票券id
 "title": "APP测试票券活动",                       		--  活动标题 
 "qrcode": "http://www.huodongxingA91B78BA.gif",   	--  二维码
 "username": "林生",                               	--  用户名
 "userlogo": "http://cdn.huodo836324_large.jpg",   	--  用户头像
 "startutc": "2015-04-30 06:00",                   	--  开始UTC时间
 "endutc": "2015-05-15 08:00",                     	--  结束UTC时间
 "address": "深圳福田",                            	--  地址
 "status": 0                                       	--  票券状态，共四种：
															 0 已付款
															 1 未付款
															 2 审核中
															 3 已过期
															 4 已取消
 */
public class TicketBean implements Serializable {

	public String id;
	public String title;
	public String qrcode;
	public String username;
	public String userlogo;
	public String startutc;
	public String endutc;
	public String address;
	public String EncryptId;
	
	/** 活动开始本地时间 **/
	public String getStartutc() {
		return TimeUtils.utcToLocal(startutc);
	}

	public String getEndutc() {
		return TimeUtils.utcToLocal(endutc);
	}
	
	public String getTimeStr() {
		return TimeUtils.getTimeAreaStr(3, startutc, endutc);
	}

}
