package com.accuvally.hdtui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageTable {


    public static final String TABLE_MESSAGE = "tb_message";
    public static final String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MESSAGE
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT,messageId TEXT,message_type,nickName TEXT,logourl TEXT,content TEXT,msgType TEXT,timestamp INTEGER, receipTimestamp INTEGER,sessionId TEXT,userId TEXT,filePath TEXT,fileUrl TEXT,fileThumbnailUrl TEXT,textStr TEXT,lengh REAL,imgWidth INTEGER,imgHeight INTEGER)";



    /**
     * 根据sessionId,userId删除tb_message表记录
     *
     */
    public static boolean deleteMsgById(String sessionId, String userId) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();
        String sql = "delete from " + MessageTable.TABLE_MESSAGE + " where sessionId=" + sessionId + " and userId=" + userId;
        db.execSQL(sql);
        return true;
    }

    /**
     * 查询某一个角色最后一次对话的服务器,时间最大
     *
     * @param sessionId
     * @return
     */
    public static MessageInfo queryLastMsg(String sessionId) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "SELECT * from " + MessageTable.TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp DESC limit 1";
        Cursor cursor = db.rawQuery(sql, new String[]{sessionId});
        MessageInfo msg = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                msg = new MessageInfo();
                msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
                msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
                msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
                msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
                msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
                msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
                msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            }
            cursor.close();
        }
        db.close();
        return msg;
    }



    /**
     * 查询某一个角色第一次对话的服务器时间
     *
     * @param
     * @param
     * @return
     */
    public static MessageInfo queryFirstMsg(String sessionId) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "SELECT * from " + MessageTable.TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp ASC limit 1";
        Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
        MessageInfo msg = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                msg = new MessageInfo();
                msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
                msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
                msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
                msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
                msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
                msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
                msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
            }
            cursor.close();
        }
        db.close();
        return msg;
    }






    public static ArrayList<MessageInfo> queryAllMsg(String sessionId, int limitStart, int limitEnd) {

        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "SELECT * from " + MessageTable.TABLE_MESSAGE + "  WHERE sessionId=? ORDER BY timestamp ASC limit ?,?";
        Log.d("s", String.valueOf(limitStart) + "  " + String.valueOf(limitEnd));
        Cursor cursor = db.rawQuery(sql, new String[] { sessionId, String.valueOf(limitStart), String.valueOf(limitEnd) });
        ArrayList<MessageInfo> list = new ArrayList<MessageInfo>();
        if (limitStart < 0) {
            limitStart = 0;
        }
        if (limitEnd < 0) {
            return list;
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                MessageInfo msg = new MessageInfo();
                msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
                msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
                msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
                msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
                msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
                msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
                msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                msg.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
                msg.setFileUrl(cursor.getString(cursor.getColumnIndex("fileUrl")));
                msg.setLengh(cursor.getDouble(cursor.getColumnIndex("lengh")));
                msg.setFileThumbnailUrl(cursor.getString(cursor.getColumnIndex("fileThumbnailUrl")));
                msg.setTextStr(cursor.getString(cursor.getColumnIndex("textStr")));
                msg.setImgWidth(cursor.getInt(cursor.getColumnIndex("imgWidth")));
                msg.setImgHeight(cursor.getInt(cursor.getColumnIndex("imgHeight")));
                list.add(msg);
            }
            cursor.close();
        }
        return list;
    }

    public static int queryAllMsgCount(String sessionId) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        String sql = "SELECT count(*) from " + MessageTable.TABLE_MESSAGE + "  WHERE sessionId=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{sessionId});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
            cursor.close();
        }
        return 0;
    }

    public static long insertFileMsg(MessageInfo msg) {

        if (selectMsgById(msg) != null) {
            return 0;
        }
        long id = 0;
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();
        if (!db.isOpen()) {
            Log.d("d", "db close Exception");
        }
        ContentValues values = new ContentValues();
        values.put("messageId", msg.getMessageId());
        values.put("nickName", msg.getNickName());
        values.put("content", msg.getContent());
        values.put("logourl", msg.getLogourl());
        values.put("timestamp", msg.getTimestamp());
        values.put("receipTimestamp", msg.getReceipTimestamp());
        values.put("msgType", msg.getMsgType());
        values.put("userId", msg.getUserId());
        values.put("sessionId", msg.getSessionId());
        values.put("filePath", msg.getFilePath());
        values.put("fileUrl", msg.getFileUrl());
        values.put("lengh", msg.getLengh());
        values.put("fileThumbnailUrl", msg.getFileThumbnailUrl());
        values.put("textStr", msg.getTextStr());
        values.put("imgWidth", msg.getImgWidth());
        values.put("imgHeight", msg.getImgHeight());
        id = db.insert(MessageTable.TABLE_MESSAGE, null, values);
        Log.d("d", "insert ==  " + id + msg.toString());
        return id;
    }

    public static boolean updateMsg(MessageInfo msg) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        long id = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("timestamp", msg.getTimestamp());
            values.put("messageId", msg.getMessageId());
            values.put("fileThumbnailUrl", msg.getFileThumbnailUrl());
            values.put("filePath", msg.getFilePath());
            values.put("fileUrl", msg.getFileUrl());
            values.put("lengh", msg.getLengh());
            values.put("textStr", msg.getTextStr());
            values.put("content", msg.getContent());
            values.put("imgWidth", msg.getImgWidth());
            values.put("imgHeight", msg.getImgHeight());
            String where = "sessionId=? AND userId=? And id=?";
            id = db.update(MessageTable.TABLE_MESSAGE, values, where, new String[] { msg.getSessionId(), msg.getUserId(), msg.getId() + "" });
            if (id > 0) {
                Log.e("info", "修改成功");
                return true;
            }
        } catch (Exception e) {
            Log.e("info", "修改会话列表失败:" + e.getMessage());
            e.printStackTrace();
        }
        return id > 0;
    }

    public static MessageInfo bulkinsertMsg(List<AVIMMessage> msges, String userid,Context mContext) {
        AVIMMessage message = null;
        NotificationInfo nofity = null;

        final int size = msges.size();
        MessageInfo result = null;
        // 插入数据库的条数
        int insertCount = 0;
        for (int i = size - 1; i >= 0; i--) {
            message = msges.get(i);
            try {
                MessageInfo info = new MessageInfo(mContext, message);
                Log.d("x", "insert to " + info.toString());
                // 如果库里已经有这条数据，说明已经全部缓存成功

                if (insertFileMsg(info) == 0) {
                    // 说明已经出现重复数据，数据库已经加载历史数据
                    if (result == null) {
                        result = info;
                    }
                } else {
                    insertCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("d", "insert to " + e.getMessage());
            }

        }
        // 说明全部数据都插入到数据库，没有重复的
        if (result == null && msges.size() > 0) {
            result = new MessageInfo(mContext, msges.get(0));
        }

        if (result != null) {
            result.setInsertCount(insertCount);
        }

        return result;
    }

    private static MessageInfo selectMsgById(MessageInfo info) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
        MessageInfo msg = null;
        String sql = "select * from " + MessageTable.TABLE_MESSAGE + " where userId=? AND sessionId=? AND messageId=? AND timestamp=? limit 1";
        Cursor cursor = db.rawQuery(sql, new String[] { info.getUserId(), info.getSessionId(), info.getMessageId(), String.valueOf(info.getTimestamp()) });
        while (cursor.moveToNext()) {
            msg = new MessageInfo();
            msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
            msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
            msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
            msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
            msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
            msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
            msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
            msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
        }
        cursor.close();
        return msg;
    }

    public static boolean deleteMsg(MessageInfo msg) {
        SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();
        int id = db.delete(MessageTable.TABLE_MESSAGE, " id=? And userId =? AND sessionId=?", new String[] { String.valueOf(msg.getId()), msg.getUserId(), msg.getSessionId() });
        return id > 0;
    }


}
