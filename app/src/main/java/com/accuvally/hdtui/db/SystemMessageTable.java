package com.accuvally.hdtui.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.MessageInfo;

public class SystemMessageTable {

	public static final String SYSTEM_MESSAGE = "system_message";
	public static final String CREATE_SYSTEM_MESSAGE = "CREATE TABLE IF NOT EXISTS "
			+ SYSTEM_MESSAGE
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,account,messageId TEXT,title TEXT,newContent TEXT,extend,nickName TEXT,logourl TEXT,content TEXT,msgType,timestamp INTEGER, receipTimestamp INTEGER,sessionId TEXT,userId TEXT,filePath TEXT,fileUrl TEXT,fileThumbnailUrl TEXT,textStr TEXT,lengh REAL,imgWidth INTEGER,imgHeight INTEGER)";

	public static void insertMessage(MessageInfo msg) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = contentValuesMessage(msg);
		db.insert(SYSTEM_MESSAGE, null, values);
		db.close();
	}

	private static ContentValues contentValuesMessage(MessageInfo msg) {
		ContentValues values = new ContentValues();
		values.put("userId", msg.getUserId());
		values.put("sessionId", msg.getSessionId());
		values.put("messageId", msg.getMessageId());

		values.put("account", AccountManager.getAccount());
		values.put("title", msg.title);
		values.put("newContent", msg.newContent);
		values.put("extend", msg.extend);

		values.put("nickName", msg.getNickName());
		values.put("content", msg.getContent());
		values.put("logourl", msg.getLogourl());
		values.put("timestamp", msg.getTimestamp());
		values.put("receipTimestamp", msg.getReceipTimestamp());
		values.put("msgType", msg.getMsgType());
		values.put("filePath", msg.getFilePath());
		values.put("fileUrl", msg.getFileUrl());
		values.put("lengh", msg.getLengh());
		values.put("fileThumbnailUrl", msg.getFileThumbnailUrl());
		values.put("textStr", msg.getTextStr());
		values.put("imgWidth", msg.getImgWidth());
		values.put("imgHeight", msg.getImgHeight());
		return values;
	}

	public static ArrayList<MessageInfo> queryAllMsg() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + SYSTEM_MESSAGE + " ORDER BY timestamp desc";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<MessageInfo> list = new ArrayList<MessageInfo>();

		while (cursor.moveToNext()) {
			MessageInfo msg = messageFromCursor(cursor);
			list.add(msg);
		}

		cursor.close();
		db.close();
		return list;
	}

	private static MessageInfo messageFromCursor(Cursor cursor) {
		MessageInfo msg = new MessageInfo();
		msg.setId(cursor.getLong(cursor.getColumnIndex("id")));
		msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
		msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
		msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));

		msg.title = cursor.getString((cursor.getColumnIndex("title")));
		msg.newContent = cursor.getString((cursor.getColumnIndex("newContent")));
		msg.extend = cursor.getInt((cursor.getColumnIndex("extend")));

		msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
		msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
		msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
		msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
		msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
		msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
		msg.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
		msg.setFileUrl(cursor.getString(cursor.getColumnIndex("fileUrl")));
		msg.setLengh(cursor.getDouble(cursor.getColumnIndex("lengh")));
		msg.setFileThumbnailUrl(cursor.getString(cursor.getColumnIndex("fileThumbnailUrl")));
		msg.setTextStr(cursor.getString(cursor.getColumnIndex("textStr")));
		msg.setImgWidth(cursor.getInt(cursor.getColumnIndex("imgWidth")));
		msg.setImgHeight(cursor.getInt(cursor.getColumnIndex("imgHeight")));
		return msg;
	}

	public static MessageInfo queryFirstMsg() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + SYSTEM_MESSAGE + " WHERE account=? ORDER BY timestamp desc limit 1";
		Cursor cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount() });
		while (cursor.moveToNext()) {
			MessageInfo msg = messageFromCursor(cursor);
			return msg;
		}
		cursor.close();
		db.close();
		return null;
	}

	public static void deleteMessage(String messageId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		db.delete(SYSTEM_MESSAGE, "account=? AND messageId=?", new String[] { AccountManager.getAccount(), messageId });
		db.close();
	}

	public static void updateExtend(String messageId, int extend) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("extend", extend);
		db.update(SYSTEM_MESSAGE, values, "account=? AND messageId=?", new String[] { AccountManager.getAccount(), messageId });
		db.close();
	}

}
