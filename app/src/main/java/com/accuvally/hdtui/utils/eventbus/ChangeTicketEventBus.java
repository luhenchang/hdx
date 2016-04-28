package com.accuvally.hdtui.utils.eventbus;

import android.view.View;

public class ChangeTicketEventBus {

	private int msg;
	
	public ChangeTicketEventBus(int msg){
		this.msg =msg;
	}

	public int getMsg() {
		return msg;
	}
	
}
