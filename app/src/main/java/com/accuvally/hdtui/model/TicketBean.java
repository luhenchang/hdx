package com.accuvally.hdtui.model;

import com.accuvally.hdtui.utils.TimeUtils;

import java.io.Serializable;

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
	public String userlogo;
	public String startutc;//开始时间
	public String endutc;//结束时间
	public String address;

    public int refundtype;//1退款(实付款大于0）  2取消  3不支持退票
    public String refundtips;//不支持退票的时候要显示原因
    public boolean ticketisverify;//是否已经验票：判断是否加已验票图标

    public double ticketprice;//原票价
    public double actuallypaid;//实际支付
    public String tickettitle;//票种

    public String city;

    public String statusstr;//状态

    public String username;
    public String userphone;
    public String ticketdesc;//票券说明

    public String actcreatorname;//主办方名字
    public String actcreatorlogo;//主办方logo

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

    @Override
    public String toString() {
        return "TicketBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", userlogo='" + userlogo + '\'' +
                ", startutc='" + startutc + '\'' +
                ", endutc='" + endutc + '\'' +
                ", address='" + address + '\'' +
                ", refundtype=" + refundtype +
                ", refundtips='" + refundtips + '\'' +
                ", ticketisverify=" + ticketisverify +
                ", ticketprice=" + ticketprice +
                ", actuallypaid=" + actuallypaid +
                ", tickettitle='" + tickettitle + '\'' +
                ", statusstr='" + statusstr + '\'' +
                ", username='" + username + '\'' +
                ", userphone='" + userphone + '\'' +
                ", ticketdesc='" + ticketdesc + '\'' +
                ", actcreatorname='" + actcreatorname + '\'' +
                ", actcreatorlogo='" + actcreatorlogo + '\'' +
                '}';
    }
}
