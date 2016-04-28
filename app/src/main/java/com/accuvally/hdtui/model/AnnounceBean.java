package com.accuvally.hdtui.model;

import java.io.Serializable;

public class AnnounceBean implements Serializable {

	public String sessionId;
	public String messageId;
	
	public int op_type;
	public String op_value;
	
	public String title;
	public String message;
	public String logourl;
	
	public int message_type;
	public Long sendTimestamp;

}
