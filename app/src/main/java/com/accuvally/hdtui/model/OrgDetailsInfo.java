package com.accuvally.hdtui.model;

import java.util.List;

/**
 * 主办方详情
 * @author Semmer Wang
 *
 */
public class OrgDetailsInfo {
	
	private List<HomeEventInfo> Acts;
	

	private OrgInfo Org;

	public List<HomeEventInfo> getActs() {
		return Acts;
	}

	public void setActs(List<HomeEventInfo> acts) {
		Acts = acts;
	}



	public OrgInfo getOrg() {
		return Org;
	}

	public void setOrg(OrgInfo org) {
		Org = org;
	}
	
	
}
