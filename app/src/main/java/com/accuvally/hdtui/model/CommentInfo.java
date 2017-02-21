package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andy Liu on 2016/11/23.
 */
public class CommentInfo implements Serializable {

    public String logo;//评价人的头像地址
    public String nick;//评价人姓名
    public int zanstatus=0;//0没选，1喜欢，2不喜欢
    public String createdate;//评价时间

    public String content;//评价内容
    public String eventname;//评价的活动
    public String eventid;//评价的活动


    public ArrayList<String> imgs;//评价上传的图片 0-9张不等

    public ArrayList<CommentReplyInfo> replays;//回复的内容 0-n条不等（可回复多次）

    public String Id="0";//评论id
    public String Rid;//回复id

    public static class CommentReplyInfo implements Serializable{

         public String commentName;//回复人的名字
        public String commentContent;
        public String Id;//评论id
        public String Rid;//回复id
    }


}
