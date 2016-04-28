package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class GroupMemberBean implements Serializable {

	public String id;
	public String creator;
	public String title;
	public String logo;
	
	public int total;
	
	public List<MemberBean> member;

}
