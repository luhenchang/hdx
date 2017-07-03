package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andy Liu on 2017/4/13.
 */
public class MailMessageInfo implements Serializable{

//    +"(id INTEGER PRIMARY KEY AUTOINCREMENT,messageId INTEGER,title TEXT,content TEXT," +
//            "time DATETIME,logoUrl TEXT,isRead BOOL,messageType INTEGER,opType INTEGER,opValue TEXT,addFriend INTEGER)";

    static final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    static final SimpleDateFormat displayFormat1 = new SimpleDateFormat("MM'月'dd'日' HH:mm");
    static final SimpleDateFormat displayFormat2 = new SimpleDateFormat("HH:mm");

    @Override
    public String toString() {
        return "MailMessageInfo{" +
                "messageId=" + messageId +
                ", inboxType='" + inboxType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", isRead=" + isRead +
                ", messages=" + messages +
                '}';
    }

    public int messageId;//***
    public String inboxType;// 类型名字，如new_friend，填入数据库的类型暂定位int
    public String createdAt;//*****   2017-04-13T08:42:54.724Z
    public int isRead=0;//自己填充 0表示未读，1表示已读

    public Message messages;
//



    public String getMailTime() {
        String ret="";
        try {
            Calendar now = Calendar.getInstance();
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date receiveDate=iso8601Format.parse(createdAt);
            Calendar receive = Calendar.getInstance();
            receive.setTime(receiveDate);

            if (now.get(Calendar.MONTH) == receive.get(Calendar.MONTH) &&
                    now.get(Calendar.DAY_OF_MONTH) == receive.get(Calendar.DAY_OF_MONTH)) {

                ret=displayFormat2.format(receiveDate);
            } else {
                ret=displayFormat1.format(receiveDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;

    }



//    public int getType(){
//        int ret=-1;
//        switch (inboxType){
//            case "sys_msg":
//                ret= MailMessageTable.sysMsg;
//                break;
//            case "recommend":
//                ret= MailMessageTable.recommend;
//                break;
//            case "new_friend":
//                ret= MailMessageTable.newFriend;
//                break;
//        }
//        return ret;
//    }

    public static class Message implements Serializable {

        public String title;
        public String message;
        public String logo_url;

        public int op_type;
        public String op_value;
        public int add_friend;//0表示非new_friend类型，1表示请求添加好友，2表示已经同意添加好友
        public String source_id;
        public  String content_logo;

        @Override
        public String toString() {
            return "Message{" +
                    "title='" + title + '\'' +
                    ", message='" + message + '\'' +
                    ", logo_url='" + logo_url + '\'' +
                    ", op_type=" + op_type +
                    ", op_value='" + op_value + '\'' +
                    ", add_friend=" + add_friend +
                    ", source_id='" + source_id + '\'' +
                    ", content_logo='" + content_logo + '\'' +
                    '}';
        }
    }






}
