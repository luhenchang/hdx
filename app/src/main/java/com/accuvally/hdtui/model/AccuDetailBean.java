package com.accuvally.hdtui.model;

import com.accuvally.hdtui.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

public class AccuDetailBean implements Serializable {

	public String id;
	public String title;
	public int likenum;
	public String address;
	/** 活动开始UTC时间 **/
	public String startutc;
	/** 活动结束UTC时间 **/
	public String endutc;
	public String statusstr;
	public int status;
	/** 活动logo **/
	public String logo;
	/** 来源 **/
	public int source;
	/** 最低票价 **/
	public int minprice;
	/** 最高票价 **/
	public int maxprice;
	public String pricearea;
	/** 是否有优惠券 **/
	public boolean hascoupon;
	public String city;
	/** 报名人数最大限额 **/
	public String maxnum;
	/** 已报名人数**/
	public String regnum;
	public String creatorlogo;
	public String shareurl;
	public String url;
	/** 当前用户是否已收藏该活动 **/
	public boolean isfollow;
    public boolean isvip;
	public boolean isreg;
	/** 剩余时间提示 **/
	public String remainingtimestr;
	public String visitnum;
	public String summary;
	public String orglogo;
	
	public String orgname;
	public String orgid;
	/** 创建人名称 **/
	public String creator;
	/** 活动创建人帐号id **/
	public String createby;
	
	/** 报名表单 **/
	public String form;
	/** 公布名单时间 **/
	public boolean isRush;
	
	/** 是否显示 已售x张 **/
	public boolean showregisternum;

    public String desc;//活动详情，用webview呈现
	public List<DetailsTicketInfo> ticks;//票
	public List<AccuBean> interestacts;//你可能感兴趣的
//    public List<AccuBean> interestacts;//你可能感兴趣的

    public int orgstatus;

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