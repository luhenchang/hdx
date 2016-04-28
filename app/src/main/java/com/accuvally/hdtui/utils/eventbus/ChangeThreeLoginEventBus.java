package com.accuvally.hdtui.utils.eventbus;

import java.util.Map;

public class ChangeThreeLoginEventBus {

	private Map<String, Object> info;

	private int status;

	private String openid;

	public ChangeThreeLoginEventBus(int status, Map<String, Object> info, String openid) {
		this.info = info;
		this.status = status;
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public int getStatus() {
		return status;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

}
