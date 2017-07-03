package com.accuvally.hdtui.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.MailMessageInfo;
import com.accuvally.hdtui.utils.Trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Andy Liu on 2017/4/13.
 */

// ：1.帐号  2.类型  3.ID
public class MailMessageTable {
//    系统消息      sys_msg
//    推荐          recommend
//    新同伴        new_friend

//    public static final int sysMsg=1;
//    public static final int recommend=2;
//    public static final int newFriend=3;


    public static final String sysMsg="sys_msg";
    public static final String recommend="recommend";
    public static final String newFriend="new_friend";

    public static final String MAIL_MESSAGE = "mail_message";

    public static final String CREATE_MAIL_MESSAGE = "CREATE TABLE IF NOT EXISTS " + MAIL_MESSAGE
            +"(id INTEGER PRIMARY KEY AUTOINCREMENT,messageId INTEGER,source_id TEXT,title TEXT,content TEXT," +
            "userId TEXT,time DATETIME,logoUrl TEXT,content_logo TEXT,isRead INTEGER,inboxType TEXT," +
            "opType INTEGER,opValue TEXT,addFriend INTEGER)";


    public static long insertMailMessage(MailMessageInfo msg) {
        long id = 0;
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();
        if (!db.isOpen()) {
            Log.d("d", "db close Exception");
        }
        ContentValues values = new ContentValues();
        values.put("messageId", msg.messageId);
        values.put("source_id", msg.messages.source_id);
        values.put("title", msg.messages.title);
        values.put("content", msg.messages.message);
        values.put("userId", AccountManager.getAccount());
        values.put("time", msg.createdAt);
        values.put("logoUrl", msg.messages.logo_url);
        values.put("content_logo", msg.messages.content_logo);
        values.put("isRead", msg.isRead);
        values.put("inboxType", msg.inboxType);//
        values.put("opType", msg.messages.op_type);
        values.put("opValue", msg.messages.op_value);
        values.put("addFriend", msg.messages.add_friend);
        id = db.insert(MAIL_MESSAGE, null, values);
//        Log.d("d", "insert ==  " + id + msg.toString());
        return id;
    }


    public static void deleteAllMailMessage() {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        int i=db.delete(MAIL_MESSAGE, "userId=?", new String[] { AccountManager.getAccount() });
//        Trace.e("deleteAllMailMessage","i:"+i);
    }


    public static int deleteMailMessage(MailMessageInfo msg,String inboxType) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        int i=db.delete(MAIL_MESSAGE, "userId=? AND inboxType=? AND messageId=?",
                new String[] { AccountManager.getAccount(),inboxType, String.valueOf(msg.messageId)});
        return i;
    }


    public static boolean isMailMessageExit(MailMessageInfo msg,String inboxType ) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "select * from " + MAIL_MESSAGE + " where userId=? AND inboxType=? AND messageId=?";
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount(), inboxType, String.valueOf(msg.messageId)});
            result = cursor.getCount() > 0 ? true : false;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }


    public static boolean isMailTypeExit(String inboxType ) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "select * from " + MAIL_MESSAGE + " where userId=? AND inboxType=?";
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount(), inboxType});
            result = cursor.getCount() > 0 ? true : false;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Trace.e("isMailTypeExit","result:"+result);
        return result;
    }


    public static int bulkUpsertMailMessage(List<MailMessageInfo> msges,String inboxType) {
        // 插入数据库的条数
        int insertCount = 0;
        int updateCount=0;

        for(MailMessageInfo mailMessageInfo:msges){
            if(isMailMessageExit(mailMessageInfo,inboxType)){//存在，更新
                if(updateMailMessage(mailMessageInfo,inboxType))
                    updateCount++;
            }else {//不存在，插入
                if(insertMailMessage(mailMessageInfo)>0)
                    insertCount++;
            }

        }
        Trace.e("bulkUpsertMailMessage", inboxType +
                ",insertCount: " + insertCount+",updateCount:"+updateCount+",msges.size():"+msges.size());
        return insertCount;
    }

    public static int getMaxMailMessage(String inboxType){
        int ret=0;
        try {
            String sql="select max(messageId) from "+MAIL_MESSAGE+" where inboxType=? and userId=?";
            SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[] {inboxType,AccountManager.getAccount()});
            cursor.moveToFirst();
            ret = cursor.getInt(0);
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }


    public static int getminMailMessage(String inboxType){
        int ret=0;
        try {
            String sql="select min(messageId) from "+MAIL_MESSAGE+" where inboxType=? and userId=?";
            SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[] {inboxType,AccountManager.getAccount()});
            cursor.moveToFirst();
            ret = cursor.getInt(0);
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }


    public static int getMailMessageCount(String inboxType){
        int ret=0;
        try {

            String sql="select count(*) from "+MAIL_MESSAGE+" where inboxType=? and userId=?";
            SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[] {inboxType,AccountManager.getAccount()});
            cursor.moveToFirst();
            ret = cursor.getInt(0);
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }



    public static long getUnreadMailMessage(String inboxType){
        long ret=0;
        try {
            String sql="select count(*) from "+MAIL_MESSAGE+" where isRead=false and inboxType="+inboxType+
                    " and userId="+ AccountManager.getAccount();
            SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[] {});
            cursor.moveToFirst();
             ret = cursor.getLong(0);
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }



    public static boolean updateMailMessage(MailMessageInfo msg,String inboxType) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("messageId", msg.messageId);
            values.put("source_id", msg.messages.source_id);
            values.put("title", msg.messages.title);
            values.put("content", msg.messages.message);
            values.put("userId", AccountManager.getAccount());
            values.put("time", msg.createdAt);
            values.put("logoUrl", msg.messages.logo_url);
            values.put("content_logo", msg.messages.content_logo);
            values.put("isRead", msg.isRead);
            values.put("inboxType", msg.inboxType);//
            values.put("opType", msg.messages.op_type);
            values.put("opValue", msg.messages.op_value);
            values.put("addFriend", msg.messages.add_friend);

            long id = db.update(MAIL_MESSAGE, values, "userId=? AND inboxType=? AND messageId=?",
                    new String[] { AccountManager.getAccount(), inboxType, String.valueOf(msg.messageId)});
            if (id > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static List<MailMessageInfo> queryMailMessage(String userId,String type,int size,int offset) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "select * from " + MAIL_MESSAGE + " where userId=? and inboxType=? order by time desc limit "+size+" offset "+offset;
        Cursor cursor = db.rawQuery(sql, new String[] { userId,type });
        List<MailMessageInfo> list = new ArrayList<MailMessageInfo>();
        while (cursor != null && cursor.moveToNext()) {
            MailMessageInfo mailMessageInfo = geneMailMessage(cursor);
            list.add(mailMessageInfo);
        }
        cursor.close();
        return list;
    }


    private static MailMessageInfo geneMailMessage(Cursor cursor) {
        MailMessageInfo mailMessageInfo = new MailMessageInfo();
        MailMessageInfo.Message message=new MailMessageInfo.Message();

        mailMessageInfo.messageId=cursor.getInt(cursor.getColumnIndex("messageId"));
        mailMessageInfo.createdAt=cursor.getString(cursor.getColumnIndex("time"));
        mailMessageInfo.isRead=cursor.getInt(cursor.getColumnIndex("isRead"));
        mailMessageInfo.inboxType=cursor.getString(cursor.getColumnIndex("inboxType"));

        message.source_id=cursor.getString(cursor.getColumnIndex("source_id"));
        message.title=cursor.getString(cursor.getColumnIndex("title"));
        message.message=cursor.getString(cursor.getColumnIndex("content"));
        message.logo_url=cursor.getString(cursor.getColumnIndex("logoUrl"));
        message.content_logo=cursor.getString(cursor.getColumnIndex("content_logo"));
        message.op_type=cursor.getInt(cursor.getColumnIndex("opType"));
        message.op_value=cursor.getString(cursor.getColumnIndex("opValue"));
        message.add_friend=cursor.getInt(cursor.getColumnIndex("addFriend"));

        mailMessageInfo.messages=message;
        return mailMessageInfo;
    }

//==============================================================================================

    public static List<MailMessageInfo> queryNewFriendMailMessage(String userId,String type) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();

        String sql = "select * from " + MAIL_MESSAGE + " where userId=? and inboxType=? order by time desc";

        HashSet<String> hashSet=new HashSet<>();

        Cursor cursor = db.rawQuery(sql, new String[] { userId,type });
        List<MailMessageInfo> list = new ArrayList<MailMessageInfo>();
        while (cursor != null && cursor.moveToNext()) {

            MailMessageInfo mailMessageInfo = geneMailMessage(cursor);

            if(!hashSet.contains(mailMessageInfo.messages.source_id)){
                hashSet.add(mailMessageInfo.messages.source_id);
                list.add(mailMessageInfo);
            }

        }
        cursor.close();
        return list;
    }




    //根据source_id定位
    public static int updateNewFriendMailMessage(MailMessageInfo msg,String inboxType) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();

        int ret=0;
        try {
            ContentValues values = new ContentValues();
            values.put("messageId", msg.messageId);
            values.put("source_id", msg.messages.source_id);
            values.put("title", msg.messages.title);
            values.put("content", msg.messages.message);
            values.put("userId", AccountManager.getAccount());
            values.put("time", msg.createdAt);
            values.put("logoUrl", msg.messages.logo_url);
            values.put("content_logo", msg.messages.content_logo);
            values.put("isRead", msg.isRead);
            values.put("inboxType", msg.inboxType);//
            values.put("opType", msg.messages.op_type);
            values.put("opValue", msg.messages.op_value);
            values.put("addFriend", msg.messages.add_friend);

            ret = db.update(MAIL_MESSAGE, values, "userId=? AND inboxType=? AND source_id=?",
                    new String[] { AccountManager.getAccount(), inboxType, msg.messages.source_id});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }



    public static int deleteNewFriendMailMessage(MailMessageInfo msg,String inboxType) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        int i=db.delete(MAIL_MESSAGE, "userId=? AND inboxType=? AND source_id=?",
                new String[] { AccountManager.getAccount(),inboxType, msg.messages.source_id});
        return i;
    }




    public static List<MailMessageInfo> queryNFMailMessage_sourceid(String userId,String type,String source_id) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();

        String sql = "select * from " + MAIL_MESSAGE + " where userId=? and inboxType=? and source_id=? order by time desc";

        HashSet<String> hashSet=new HashSet<>();

        Cursor cursor = db.rawQuery(sql, new String[] { userId,type,source_id });
        List<MailMessageInfo> list = new ArrayList<MailMessageInfo>();
        while (cursor != null && cursor.moveToNext()) {

            MailMessageInfo mailMessageInfo = geneMailMessage(cursor);
            list.add(mailMessageInfo);
        }
        cursor.close();
        return list;
    }



}
