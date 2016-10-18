package com.accuvally.hdtui.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.SessionInfo;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionTable {

	/** 会话列表 **/
	public static final String TABLE_SESSION = "tb_session";
	public static final String CREATE_TABLE_SESSION = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSION
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,userId,friendId,sessionId,creator,isDBMessage INTEGER,isNotification INTEGER,extend INTEGER,title,content,time INTEGER,logoUrl,unReadNum INTEGER,fromUserId,toUserId,messageId,nickName,sessionType INTEGER,messageType INTEGER,op_type,op_value)";
	
	public static Uri uri = Uri.parse("content://accuvally/" + TABLE_SESSION);
//	private static ContentResolver contentResolver = AccuApplication.getInstance().getContentResolver();

//	private static SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
	
//	public static String createTable() {
//		String createQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSION + " (";
//
//		Map<String, String> columns = getColumnMapping();
//
//		boolean isAdd = false;
//		for (String column : columns.keySet()) {
//			if (isAdd) {
//				createQuery += ", ";
//			}
//			isAdd = true;
//			createQuery += column + " " + columns.get(column);
//		}
//
//		createQuery += ");";
//
//		return createQuery;
//	}
//
//	protected static Map<String, String> getColumnMapping() {
//		final Map<String, String> map = new LinkedHashMap<String, String>();
//		map.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
//		map.put("userId", "TEXT");
//		map.put("sessionId", "TEXT");
//		map.put("title", "TEXT");
//		map.put("content", "TEXT");
//		map.put("logoUrl", "TEXT");
//
//		map.put("isNotification", "INTEGER");
//		map.put("time", "INTEGER");
//		map.put("isDBMessage", "INTEGER");
//		return map;
//	}
	
	public static void createTable() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		db.execSQL(CREATE_TABLE_SESSION);
		db.close();
	}
	
	public static void deleteTable() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
		db.close();
	}
	
	public static void bulkInsertSession(List<AVIMConversation> list) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();

		db.beginTransaction();
		for (AVIMConversation conversation : list) {
			SessionInfo session = new SessionInfo(conversation, true);
			session.setTitle(conversation.getName());
			ContentValues values = geneContentValues(session);
			long id = db.insert(TABLE_SESSION, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private static ContentValues geneContentValues(SessionInfo info) {
		ContentValues values = new ContentValues();
		values.put("userId", info.userId);
		values.put("sessionId", info.getSessionId());
		values.put("creator", info.creator);
		
		values.put("title", info.getTitle());
		values.put("content", info.getContent());
		values.put("time", info.getTime());
		values.put("logoUrl", info.getLogoUrl());
		values.put("unReadNum", info.getUnReadNum());
		values.put("fromUserId", info.getFromUserId());
		values.put("toUserId", info.getToUserId());
		values.put("nickName", info.getNickName());
		values.put("sessionType", info.getSessionType());
		values.put("messageType", info.getMessageType());
		values.put("isNotification", info.getIsNotification());
		values.put("op_type", info.getOp_type());
		values.put("op_value", info.getOp_value());
		values.put("friendId", info.friendId);
		values.put("extend", info.extend);
		return values;
	}
	
	public static ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
	
	public static void AsyncInsertSession(final SessionInfo info) {
		singleExecutor.execute(new Runnable() {

			@Override
			public void run() {
				insertSession(info);
			}
		});
	}

	public static void insertSession(SessionInfo info) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		boolean is = isSession(info.getSessionId());
		
		if (!is) {
			ContentValues values = new ContentValues();
			values.put("userId", info.userId);
			values.put("sessionId", info.getSessionId());
			values.put("creator", info.creator);
			
			values.put("title", info.getTitle());
			values.put("content", info.getContent());
			values.put("time", info.getTime());
			values.put("logoUrl", info.getLogoUrl());
			values.put("unReadNum", info.getUnReadNum());
			values.put("fromUserId", info.getFromUserId());
			values.put("toUserId", info.getToUserId());
			values.put("nickName", info.getNickName());
			values.put("sessionType", info.getSessionType());
			values.put("messageType", info.getMessageType());
			values.put("isNotification", info.getIsNotification());
			values.put("op_type", info.getOp_type());
			values.put("op_value", info.getOp_value());
			values.put("friendId", info.friendId);
			values.put("extend", info.extend);

			db.insert(TABLE_SESSION, null, values);
		} else {
			updateSession(info);
		}
	}

	/**
	 * 检查会话列表中是否存在该会话
	 * 
	 * @sessionId 会话Id
	 * @return
	 */
	public static boolean isSession(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select * from " + TABLE_SESSION + " where userId=? AND sessionId=?";
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount(), sessionId });
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

	public static boolean updateSession(SessionInfo info) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			if (!info.isSelfSend) {
				if (!TextUtils.isEmpty(info.getLogoUrl())) {
					values.put("logoUrl", info.getLogoUrl());
				}
				if (!TextUtils.isEmpty(info.getTitle())) {
					values.put("title", info.getTitle());
				}
			}
			values.put("content", info.getContent());
			values.put("nickName", info.getNickName());
			values.put("time", info.getTime());
			values.put("messageType", info.getMessageType());
			values.put("isNotification", info.getIsNotification());
			values.put("toUserId", info.getToUserId());
			values.put("op_type", info.getOp_type());
			values.put("op_value", info.getOp_value());
			values.put("unReadNum", info.getUnReadNum());
			long id = db.update(TABLE_SESSION, values, "userId=? AND sessionId=?", new String[] { AccountManager.getAccount(), info.getSessionId() });
			if (id > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateSession(String sessionId, ContentValues values) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		long id = db.update(TABLE_SESSION, values, "userId=? AND sessionId=?", new String[] { AccountManager.getAccount(), sessionId });
		return id > 0;
	}

	public static List<SessionInfo> queryAllSession(String userId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select * from " + TABLE_SESSION + " where userId=? order by time desc";
		Cursor cursor = db.rawQuery(sql, new String[] { userId });
		List<SessionInfo> list = new ArrayList<SessionInfo>();
		while (cursor != null && cursor.moveToNext()) {
			SessionInfo sessionInfo = geneSession(cursor);
			list.add(sessionInfo);
		}
		cursor.close();
		return list;
	}

	public static List<SessionInfo> queryAllNotification(String userId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select * from " + TABLE_SESSION + " where userId=? AND isNotification='1' order by time desc";
		Cursor cursor = db.rawQuery(sql, new String[] { userId });
		List<SessionInfo> list = new ArrayList<SessionInfo>();
		while (cursor != null && cursor.moveToNext()) {
			SessionInfo sessionInfo = geneSession(cursor);
			list.add(sessionInfo);
		}
		cursor.close();
		return list;
	}
	
	public static List<SessionInfo> queryAllNewFriend(String userId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select * from " + TABLE_SESSION + " where userId=? AND extend='1' order by time desc";
		Cursor cursor = db.rawQuery(sql, new String[] { userId });
		List<SessionInfo> list = new ArrayList<SessionInfo>();
		while (cursor != null && cursor.moveToNext()) {
			SessionInfo sessionInfo = geneSession(cursor);
			list.add(sessionInfo);
		}
		cursor.close();
		return list;
	}
	
	public static void deleteAllUserSession() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		db.delete(TABLE_SESSION, "userId=?", new String[] { AccountManager.getAccount() });
	}
	
	public static void deleteSessionById(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		db.delete(TABLE_SESSION, "userId=? AND sessionId=?", new String[] { AccountManager.getAccount(), sessionId });
	}

	public static SessionInfo querySessionById(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select * from " + TABLE_SESSION + " where userId=? AND sessionId=?";
		Cursor cursor = null;
		cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount(), sessionId });
		SessionInfo sessionInfo = null;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			sessionInfo = geneSession(cursor);
		}
		cursor.close();
		return sessionInfo;
	}
	
	public static SessionInfo queryPrivateSession(String friendId, String userId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		SessionInfo sessionInfo = null;
		String sql = "select * from " + TABLE_SESSION + " where friendId=? AND userId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { friendId, userId });
		while (cursor.moveToNext()) {
			sessionInfo = geneSession(cursor);
		}
		cursor.close();
		return sessionInfo;
	}
	
	private static SessionInfo geneSession(Cursor cursor) {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
		sessionInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		sessionInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
		sessionInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
		sessionInfo.setLogoUrl(cursor.getString(cursor.getColumnIndex("logoUrl")));
		sessionInfo.setUnReadNum(cursor.getInt(cursor.getColumnIndex("unReadNum")));
		sessionInfo.setFromUserId(cursor.getString(cursor.getColumnIndex("fromUserId")));
		sessionInfo.setToUserId(cursor.getString(cursor.getColumnIndex("toUserId")));
		sessionInfo.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
		sessionInfo.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
		sessionInfo.setSessionType(cursor.getInt(cursor.getColumnIndex("sessionType")));
		sessionInfo.setMessageType(cursor.getInt(cursor.getColumnIndex("messageType")));
		sessionInfo.setIsNotification(cursor.getInt(cursor.getColumnIndex("isNotification")));
		sessionInfo.setOp_type(cursor.getInt(cursor.getColumnIndex("op_type")));
		sessionInfo.setOp_value(cursor.getString(cursor.getColumnIndex("op_value")));
		sessionInfo.userId = cursor.getString(cursor.getColumnIndex("userId"));
		sessionInfo.friendId = cursor.getString(cursor.getColumnIndex("friendId"));
		sessionInfo.creator = cursor.getString(cursor.getColumnIndex("creator"));
		sessionInfo.extend = cursor.getInt(cursor.getColumnIndex("extend"));
		return sessionInfo;
	}
	
	/** sessionId 会话是否已经插入了所有消息 **/
//	public static boolean isDBMessage(String sessionId) {
//		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
//		String sql = "select * from " + TABLE_SESSION + " where sessionId=?";
//		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
//		boolean result = false;
//		if (cursor.moveToNext()) {
//			result = cursor.getInt(cursor.getColumnIndex("isDBMessage")) == 1;
//		}
//		cursor.close();
//		return result;
//	}
	
	
	/** 查询所有私聊会话 **/
//	public ArrayList<SessionInfo> queryPrivateSession() {
//		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
//		ArrayList<SessionInfo> list = new ArrayList<SessionInfo>();
//		String sql = "select * from " + TABLE_SESSION + " where isConv='1' order by time desc";
//		Cursor cursor = db.rawQuery(sql, null);
//		while (cursor != null && cursor.moveToNext()) {
//			SessionInfo sessionInfo = geneSession(cursor);
//			list.add(sessionInfo);
//		}
//		cursor.close();
//		return list;
//	}

	/**
	 * 查询所有未读消息条数
	 * @param isNotification 1为通知 0为消息
	 * @return
	 */
	public static int queryUnReadNum(int isNotification, String userId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		int maxUnReadNum = 0;
		String sql = "select unReadNum from " + TABLE_SESSION + " where isNotification=? and toUserId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(isNotification), userId });
		while (cursor.moveToNext()) {
			Integer unReadNum = cursor.getInt(cursor.getColumnIndex("unReadNum"));
			maxUnReadNum += unReadNum;
		}
		cursor.close();
		return maxUnReadNum;
	}
	
	public static boolean hasUnReadMessage() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "select unReadNum from " + TABLE_SESSION + " where userId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount()});
		while (cursor.moveToNext()) {
			int unReadNum = cursor.getInt(cursor.getColumnIndex("unReadNum"));
			if (unReadNum > 0) {
				cursor.close();
				return true;
			}
		}
		cursor.close();
		return false;
	}

	/**
	 * 修改未读条数，设置为已读
	 */
	public static boolean updateSessionByUnReadNum(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("unReadNum", 0);
			long id = db.update(TABLE_SESSION, values, "userId=? AND sessionId=?", new String[] { AccountManager.getAccount(), sessionId });
			if (id > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.e("SessionTable", "修改未读条数失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据会话id查询该会话的未读条数
	 * 
	 * @param sessionId
	 * @return
	 */
	public static int querySessionByUnReadNum(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		int num = 0;
		String sql = "select unReadNum from " + TABLE_SESSION + " where userId=? AND sessionId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount(), sessionId });
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			num = cursor.getInt(cursor.getColumnIndex("unReadNum"));
		}
		cursor.close();
		return num;
	}
	
	public static void clearNotifyUnReadNum() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("unReadNum", 0);
		
		db.update(TABLE_SESSION, values, "userId=? AND isNotification='1'", new String[] { AccountManager.getAccount()});
		db.close();
	}
	
	public static void clearNewFriendUnReadNum() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("unReadNum", 0);

		db.update(TABLE_SESSION, values, "userId=? AND extend='1'", new String[] { AccountManager.getAccount()});
		db.close();
		
//		String sql = "select * from " + TABLE_SESSION + " where userId=? AND extend='1'";
//		Cursor cursor = db.rawQuery(sql, new String[] { AccountManager.getAccount() });
//		while (cursor.moveToNext()) {
//			String sessionId = cursor.getString(cursor.getColumnIndex("sessionId"));
//		}
	}
	
}
