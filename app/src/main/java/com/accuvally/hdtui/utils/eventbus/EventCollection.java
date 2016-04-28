package com.accuvally.hdtui.utils.eventbus;

public class EventCollection {
	
	public static int favorite = 1;
	public static int unfavorite = 2;
	
	public int state;

	public EventCollection(int state) {
		this.state = state;
	}
	
	public EventCollection() {
	}

}
