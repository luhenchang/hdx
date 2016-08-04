package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class MemberInfo implements Serializable {

	public String account;
	public String logo;
	public String region;// 地区

	public String nick;
	public String brief;

	public int gender;// 1男————0女
//新规则：1男2女0保密
	public boolean isparter;

	public List<AccuBean> followorgs;// 已关注活动
	public List<AccuBean> follows;// 已关注活动
	public List<AccuBean> published;// 已发布活动

}
