package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.ArrayList;

//获取评论
public class CommentInfoWithoutReply implements Serializable {

    public String content;
    public String createdate;
    public String eventdate;
	public String eventid;
    public String eventname;
    public String id;
    public ArrayList<String> imgs;

    public String logo;
    public String nick;
    public int rid;
    public ArrayList<String> thumbimgs;
    public int zanstatus;

}
