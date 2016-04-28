package com.accuvally.hdtui.utils.eventbus;

public class ChangeImageLocationEventBus {
	private String logoUrl;
	private int x;
	private int y;

	public ChangeImageLocationEventBus(String logoUrl, int x, int y) {
		this.logoUrl = logoUrl;
		this.x = x;
		this.y = y;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
