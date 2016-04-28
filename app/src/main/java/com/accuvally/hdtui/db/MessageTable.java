package com.accuvally.hdtui.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.model.AnnounceBean;
import com.accuvally.hdtui.model.MessageInfo;
import com.avos.avoscloud.im.v2.AVIMMessage;

public class MessageTable {

	public static final String TABLE_MESSAGE = "tb_message";
	public static final String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MESSAGE
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,userId TEXT,sessionId TEXT,messageId TEXT,sendState INTEGER,nickName TEXT,logourl TEXT,content TEXT,msgType TEXT,timestamp INTEGER, receipTimestamp INTEGER,filePath TEXT,fileUrl TEXT,fileThumbnailUrl TEXT,textStr TEXT,lengh REAL,imgWidth INTEGER,imgHeight INTEGER)";

	private static MessageInfo selectMsgById(MessageInfo info) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		MessageInfo msg = null;
		String sql = "select * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE
				+ " where userId=? AND sessionId=? AND messageId=? AND timestamp=? limit 1";
		Cursor cursor = db.rawQuery(sql,
				new String[] { info.getUserId(), info.getSessionId(), info.getMessageId(), String.valueOf(info.getTimestamp()) });
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

	private static ContentValues contentValuesMessage(MessageInfo msg) {
		ContentValues values = new ContentValues();
		values.put("userId", msg.getUserId());
		values.put("sessionId", msg.getSessionId());
		values.put("messageId", msg.getMessageId());
//		values.put("sendState", msg.sendState);

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

	public static long insertMessage(MessageInfo msg) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = contentValuesMessage(msg);
		long id = db.insert(TABLE_MESSAGE, null, values);
		return id;
	}

	public static MessageInfo bulkinsertMsg(List<AVIMMessage> listMessages, String userid) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();

		final int size = listMessages.size();
		MessageInfo result = null;
		// 插入数据库的条数
		int insertCount = 0;
		for (int i = size - 1; i >= 0; i--) {
			try {
				AVIMMessage message = listMessages.get(i);
				MessageInfo info = new MessageInfo(AccuApplication.getInstance(), message);
				// 如果库里已经有这条数据，说明已经全部缓存成功
				if (insertMessage(info) == 0) {
					// 说明已经出现重复数据，数据库已经加载历史数据
					if (result == null) {
						result = info;
					}
				} else {
					insertCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// 说明全部数据都插入到数据库，没有重复的
		if (result == null && listMessages.size() > 0) {
			result = new MessageInfo(AccuApplication.getInstance(), listMessages.get(0));
		}

		if (result != null) {
			result.setInsertCount(insertCount);
		}

		return result;
	}

	public static boolean bulkInsertMsg(List<AVIMMessage> list) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		boolean result = true;
		db.beginTransaction();
		for (AVIMMessage avimMessage : list) {
			MessageInfo info = new MessageInfo(AccuApplication.getInstance(), avimMessage);
			ContentValues values = contentValuesMessage(info);
			long id = db.insert(TABLE_MESSAGE, null, values);
			if (id < 0) {
				result = false;
				break;
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return result;
	}

//	public static ArrayList<MessageInfo> bulkInsertMsg(List<AVIMMessage> list) {
//		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
//		ArrayList<MessageInfo> listMsg = new ArrayList<MessageInfo>();
//		db.beginTransaction();
//		for (AVIMMessage avimMessage : list) {
//			MessageInfo info = new MessageInfo(AccuApplication.getInstance(), avimMessage);
//			listMsg.add(info);
//			ContentValues values = contentValuesMessage(info);
//			long id = db.insert(TABLE_MESSAGE, null, values );
//			if (id < 0) {
//				break;
//			}
//		}
//		db.setTransactionSuccessful();
//		db.endTransaction();
//		return listMsg;
//	}

	public static int queryAllMsgCount(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT count(*) from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + "  WHERE sessionId=? ";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
		int count = 0;
		if (cursor != null && cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
	}

	public static ArrayList<MessageInfo> queryAllMsg(String sessionId, int limitStart, int limitEnd) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp ASC limit ?,?";
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
				MessageInfo msg = messageFromCursor(cursor);
				list.add(msg);
			}
			cursor.close();
		}
		return list;
	}

	private static MessageInfo messageFromCursor(Cursor cursor) {
		MessageInfo msg = new MessageInfo();
		msg.setId(cursor.getLong(cursor.getColumnIndex("id")));
		msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
		msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
		msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
//		msg.sendState = cursor.getInt((cursor.getColumnIndex("sendState")));

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

	public static ArrayList<MessageInfo> queryCurrentPageMsg(String sessionId, int end) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		Cursor cursor = db.query(TABLE_MESSAGE, null, "sessionId=?", new String[] { sessionId }, null, null, "timestamp desc", "0,"
				+ end);
		ArrayList<MessageInfo> list = new ArrayList<MessageInfo>();

		if (cursor.moveToLast()) {
			do {
				MessageInfo msg = messageFromCursor(cursor);
				list.add(msg);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}

	private static List<MessageInfo> queryFirstPageMsg(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp desc limit 0,20";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
		List<MessageInfo> list = new ArrayList<MessageInfo>();
		if (cursor.moveToLast()) {
			do {
				MessageInfo msg = messageFromCursor(cursor);
				list.add(msg);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}

	public static List<MessageInfo> queryFrontMsg(String sessionId, final List<MessageInfo> list) {
		if (list == null || list.isEmpty()) {
			return queryFirstPageMsg(sessionId);
		}

		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		MessageInfo message = list.get(0);
		String sql = "SELECT * from " + TABLE_MESSAGE + " WHERE sessionId=? AND timestamp<? ORDER BY timestamp desc limit 0,20";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId, String.valueOf(message.getTimestamp()) });
		ArrayList<MessageInfo> cursorList = new ArrayList<MessageInfo>();
		if (cursor.moveToLast()) {
			do {
				MessageInfo msg = messageFromCursor(cursor);
				cursorList.add(msg);
			} while (cursor.moveToPrevious());
		}
		cursorList.addAll(list);
		cursor.close();
		return cursorList;
	}

	/**
	 * 查询某一个角色第一次对话的服务器时间
	 * 
	 * @param context
	 * @param user_id
	 * @return
	 */
	public static MessageInfo queryFirstMsg(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp ASC limit 1";
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
		return msg;
	}

//	public static MessageInfo queryLastMsg(String sessionId) {
//		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
//		String sql = "SELECT * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp DESC limit 1";
//		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
//		MessageInfo msg = null;
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				msg = new MessageInfo();
//				msg.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
//				msg.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
//				msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
//				msg.setLogourl(cursor.getString(cursor.getColumnIndex("logourl")));
//				msg.setMsgType(cursor.getInt(cursor.getColumnIndex("msgType")));
//				msg.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
//				msg.setReceipTimestamp(cursor.getLong(cursor.getColumnIndex("receipTimestamp")));
//				msg.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
//				msg.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
//			}
//			cursor.close();
//		}
//		db.close();
//		return msg;
//	}

	public static boolean deleteMessage(MessageInfo msg) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		int id = db.delete(TABLE_MESSAGE, " id=? And userId =? AND sessionId=?",
				new String[] { String.valueOf(msg.getId()), msg.getUserId(), msg.getSessionId() });
		return id > 0;
	}

	public static boolean updateMessage(MessageInfo msg) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("messageId", msg.getMessageId());
//		values.put("sendState", msg.sendState);
		values.put("timestamp", msg.getTimestamp());

		values.put("fileThumbnailUrl", msg.getFileThumbnailUrl());
		values.put("fileUrl", msg.getFileUrl());

//		values.put("filePath", msg.getFilePath());
//		values.put("lengh", msg.getLengh());
//		values.put("imgWidth", msg.getImgWidth());
//		values.put("imgHeight", msg.getImgHeight());

		int id = db.update(TABLE_MESSAGE, values, "id=?", new String[] { msg.getId() + "" });
		return id > 0;
	}
}
