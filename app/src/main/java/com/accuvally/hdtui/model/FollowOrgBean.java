package com.accuvally.hdtui.model;

import java.io.Serializable;

public class FollowOrgBean implements Serializable{

	//是否是轻活动
	public boolean IsQing;
	public int Status;
	public int Id;
	public String Title;
	public String ShareUrl;
	public boolean IsFollow;
	//关注数量
	public int LikeNum;
	//地址
	public String Address;
	
	public String Start;
	public String End;
	
	public int SourceType;
	public String Url;
	public String LogoUrl;
}
