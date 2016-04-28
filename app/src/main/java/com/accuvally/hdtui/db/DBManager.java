package com.accuvally.hdtui.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.accuvally.hdtui.model.SaveBeHaviorInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.TimeUtils;
import com.avos.avoscloud.im.v2.AVIMMessage;

public class DBManager {
	private final int BUFFER_SIZE = 400000;
	private static final String PACKAGE_NAME = "com.accuvally.hdtui";
	public static final String DB_NAME = "meituan_cities.db";
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; // 存放路径
	private Context mContext;
	private SQLiteDatabase database;
	private AccuvallySQLiteOpenHelper accuvallySQLiteOpenHelper;

	public DBManager(Context context) {
		accuvallySQLiteOpenHelper = new AccuvallySQLiteOpenHelper(context);
		this.mContext = context;
	}

	/**
	 * 被调用方法
	 */
	public void openDateBase() {
		this.database = this.openDateBase(DB_PATH + "/" + DB_NAME);
	}

	/**
	 * 打开数据库
	 * 
	 * @param dbFile
	 * @return SQLiteDatabase
	 * @author sy
	 */
	private synchronized SQLiteDatabase openDateBase(String dbFile) {
		File file = new File(dbFile);
		if (!file.exists()) {
			// // 打开raw中得数据库文件，获得stream�
			InputStream stream = this.mContext.getResources().openRawResource(R.raw.meituan_cities);
			try {

				// 将获取到的stream 流写入道data�
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
				return db;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return database;
	}

	public void closeDatabase() {
		if (database != null && database.isOpen()) {
			this.database.close();
		}
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	private boolean isHistoryExists(String historyId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_history where historyId=\"" + historyId + "\"";
		Cursor cursor = db.rawQuery(sql, null);
		boolean result = false;
		result = cursor.getCount() > 0 ? true : false;
		cursor.close();
		db.close();
		return result;
	}

	private boolean issearchHistoryExists(String tag) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_search where searchName=\"" + tag + "\"";
		Cursor cursor = db.rawQuery(sql, null);
		boolean result = false;
		result = cursor.getCount() > 0 ? true : false;
		cursor.close();
		db.close();
		return result;
	}

	public void insertSearchHistory(String tag) {
		boolean is = issearchHistoryExists(tag);
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		if (!is) {
			ContentValues values = new ContentValues();
			values.put("searchName", tag);
			db.insert(accuvallySQLiteOpenHelper.TABLE_SEARCH, null, values);
			db.close();
		}
	}

	public List<String> getSearchHistory() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		String sql = "select * from tb_search";
		Cursor cursor = db.rawQuery(sql, null);
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("searchName")));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void deleteSearchHisttory() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		db.delete(accuvallySQLiteOpenHelper.TABLE_SEARCH, null, null);
	}

	public void insertHistory(HomeEventInfo homeInfo) {
		boolean is = isHistoryExists(homeInfo.getId());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String createDate = TimeUtils.DF_YMD.format(curDate);
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		if (is) {
			ContentValues values = new ContentValues();
			values.put("HistoryId", homeInfo.getId());
			values.put("Title", homeInfo.getTitle());
			values.put("Address", homeInfo.getAddress());
			values.put("Start", createDate);
			values.put("End", homeInfo.getEnd());
			values.put("LogoUrl", homeInfo.getLogoUrl());
			values.put("LikeNum", homeInfo.getLikeNum());
			values.put("Url", homeInfo.getUrl());
			values.put("Source", homeInfo.getSource());
			values.put("SourceType", homeInfo.getSourceType());
			values.put("ShareUrl", homeInfo.getShareUrl());
			db.update(accuvallySQLiteOpenHelper.TABLE_HISTORICAL, values, "historyId=?", new String[] { String.valueOf(homeInfo.getId()) });
			db.close();
		} else {
			ContentValues values = new ContentValues();
			values.put("HistoryId", homeInfo.getId());
			values.put("Title", homeInfo.getTitle());
			values.put("Address", homeInfo.getAddress());
			values.put("Start", createDate);
			values.put("End", homeInfo.getEnd());
			values.put("LogoUrl", homeInfo.getLogoUrl());
			values.put("LikeNum", homeInfo.getLikeNum());
			values.put("Url", homeInfo.getUrl());
			values.put("Source", homeInfo.getSource());
			values.put("SourceType", homeInfo.getSourceType());
			values.put("ShareUrl", homeInfo.getShareUrl());
			db.insert(accuvallySQLiteOpenHelper.TABLE_HISTORICAL, null, values);
			db.close();
		}
	}

	public List<HomeEventInfo> getHistory() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<HomeEventInfo> homeInfos = new ArrayList<HomeEventInfo>();
		String sql;

		sql = "select * from tb_history order by id desc";

		Cursor cursor = db.rawQuery(sql, null);

		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			HomeEventInfo homeInfo = new HomeEventInfo();
			homeInfo.setId(cursor.getString(cursor.getColumnIndex("HistoryId")));
			homeInfo.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
			homeInfo.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
			homeInfo.setStart(cursor.getString(cursor.getColumnIndex("Start")));
			homeInfo.setEnd(cursor.getString(cursor.getColumnIndex("End")));
			homeInfo.setLogoUrl(cursor.getString(cursor.getColumnIndex("LogoUrl")));
			homeInfo.setLikeNum(cursor.getInt(cursor.getColumnIndex("LikeNum")));
			homeInfo.setUrl(cursor.getString(cursor.getColumnIndex("Url")));
			homeInfo.setSource(cursor.getString(cursor.getColumnIndex("Source")));
			homeInfo.setSourceType(cursor.getInt(cursor.getColumnIndex("SourceType")));
			homeInfo.setShareUrl(cursor.getString(cursor.getColumnIndex("ShareUrl")));
			homeInfos.add(homeInfo);
		}
		cursor.close();
		db.close();

		return homeInfos;
	}

	// 删除历史记录（根据活动ID�
	public void deleteHisttory(String id) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		db.delete(accuvallySQLiteOpenHelper.TABLE_HISTORICAL, "HistoryId=?", new String[] { id + "" });
		db.close();
	}

	public void deleteHisttory() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		db.delete(accuvallySQLiteOpenHelper.TABLE_HISTORICAL, null, null);
	}

	private boolean isClassfy(String name) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_classfy where name=\"" + name + "\"";
		Cursor cursor = db.rawQuery(sql, null);
		boolean result = false;
		result = cursor.getCount() > 0 ? true : false;
		cursor.close();
		db.close();
		return result;
	}

	public void insertClassfy(String name) {
		boolean is = isClassfy(name);
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		if (!is) {
			ContentValues values = new ContentValues();
			values.put("name", name);
			db.insert(accuvallySQLiteOpenHelper.TABLE_CLASSFY, null, values);
			db.close();
		}
	}

	public List<String> getClassfy() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<String> list = new ArrayList<String>();
		String sql;
		sql = "select * from tb_classfy order by id asc";
		Cursor cursor = db.rawQuery(sql, null);
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		db.close();
		return list;
	}

	public void deleteClassfy() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		db.delete(accuvallySQLiteOpenHelper.TABLE_CLASSFY, null, null);
	}

	public void insertAccuDetails(String accuId) {
		boolean is = isAccuDetails(accuId);
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		if (!is) {
			ContentValues values = new ContentValues();
			values.put("accuId", accuId);
			db.insert(accuvallySQLiteOpenHelper.TABLE_ACCU_DETAILS, null, values);
			db.close();
		}
	}

	public boolean isAccuDetails(String accuId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_accu_details where accuId=\"" + accuId + "\"";
		Cursor cursor = db.rawQuery(sql, null);
		boolean result = false;
		result = cursor.getCount() > 0 ? true : false;
		cursor.close();
		db.close();
		return result;
	}

	// -----------------
	// 记录用户行为数据
	public void insertSaveBeHavior(SaveBeHaviorInfo info) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", info.getUid());
		values.put("tid", info.getTid());
		values.put("type", info.getType());
		values.put("target_type", info.getTarget_type());
		values.put("target_id", info.getTarget_id());
		values.put("data_source", info.getData_source());
		values.put("location", info.getLocation());
		values.put("country", info.getCountry());
		values.put("province", info.getProvince());
		values.put("city", info.getCity());
		values.put("date_time", info.getDate_time());
		Log.d("ss", "info.getDate_time() =" + info.getDate_time());
		values.put("key_word", info.getKey_word());
		values.put("device_type", info.getDevice_type());
		values.put("tag", info.getTag());
		values.put("version", info.getVersion());
		values.put("event_name", info.getEvent_name());
		values.put("event_data", info.getEvent_data());
		db.insert(accuvallySQLiteOpenHelper.TABLE_BEHAVIOR, null, values);
		db.close();
	}

	// 读取行为记录
	public List<SaveBeHaviorInfo> getSaveBeHavior() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<SaveBeHaviorInfo> list = new ArrayList<SaveBeHaviorInfo>();
		String sql = "select * from " + accuvallySQLiteOpenHelper.TABLE_BEHAVIOR;
		Cursor cursor = db.rawQuery(sql, null);
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			SaveBeHaviorInfo info = new SaveBeHaviorInfo();
			info.setUid(cursor.getString(cursor.getColumnIndex("uid")));
			info.setTid(cursor.getString(cursor.getColumnIndex("tid")));
			info.setType(cursor.getString(cursor.getColumnIndex("type")));
			info.setTarget_type(cursor.getString(cursor.getColumnIndex("target_type")));
			info.setTarget_id(cursor.getString(cursor.getColumnIndex("target_id")));
			info.setData_source(cursor.getString(cursor.getColumnIndex("data_source")));
			info.setLocation(cursor.getString(cursor.getColumnIndex("location")));
			info.setCountry(cursor.getString(cursor.getColumnIndex("country")));
			info.setProvince(cursor.getString(cursor.getColumnIndex("province")));
			info.setCity(cursor.getString(cursor.getColumnIndex("city")));
			info.setDate_time(cursor.getString(cursor.getColumnIndex("date_time")));
			info.setKey_word(cursor.getString(cursor.getColumnIndex("key_word")));
			info.setDevice_type(cursor.getString(cursor.getColumnIndex("device_type")));
			info.setTag(cursor.getString(cursor.getColumnIndex("tag")));
			info.setVersion(cursor.getString(cursor.getColumnIndex("version")));
			info.setEvent_name(cursor.getString(cursor.getColumnIndex("event_name")));
			info.setEvent_data(cursor.getString(cursor.getColumnIndex("event_data")));
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}

	// 删除行为记录
	public void deleteSaveBeHavior() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		db.delete(accuvallySQLiteOpenHelper.TABLE_BEHAVIOR, null, null);
	}

	// *********************会话列表***************************//
	/**
	 * 添加会话
	 */
	public void insertSession(SessionInfo info, boolean isChat) {
		boolean is = isSession(info.getSessionId());
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		Log.i("info", "exist[" + is + "] " + info.toString());
		if (!is) {
			ContentValues values = new ContentValues();
			values.put("sessionId", info.getSessionId());
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
			db.insert("tb_session", null, values);
			// db.close();
		} else {
			updateSession(info, isChat);
		}
	}

	/**
	 * 检查会话列表中是否存在该会话
	 * 
	 * @sessionId 会话Id
	 * @return
	 */
	public boolean isSession(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_session where sessionId=?";
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { sessionId });
			result = cursor.getCount() > 0 ? true : false;
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		// db.close();
		return result;
	}

	/**
	 * 修改会话列表
	 */
	public boolean updateSession(SessionInfo info, boolean isChat) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("content", info.getContent());
			values.put("nickName", info.getNickName());
			values.put("time", info.getTime());
			if (!TextUtils.isEmpty(info.getLogoUrl())) {
				values.put("logoUrl", info.getLogoUrl());
			}
			values.put("title", info.getTitle());
			values.put("messageType", info.getMessageType());
			values.put("isNotification", info.getIsNotification());
			values.put("toUserId", info.getToUserId());
			values.put("op_type", info.getOp_type());
			values.put("op_value", info.getOp_value());
			Log.d("x", info.toString());
			if (!isChat)
				values.put("unReadNum", info.getUnReadNum());
			long id = db.update("tb_session", values, "sessionId=?", new String[] { info.getSessionId() });
			if (id > 0) {
				return true;
			}
			Log.i("info", id + "修改会话列表成功");
		} catch (Exception e) {
			Log.e("info", "修改会话列表失败:" + e.getMessage());
			e.printStackTrace();
		}
		// db.close();
		return false;
	}

	public SessionInfo querySessionById(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select * from tb_session where sessionId=?";
		Cursor cursor = null;
		cursor = db.rawQuery(sql, new String[] { sessionId });
		SessionInfo sessionInfo = null;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			sessionInfo = new SessionInfo();
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
		}
		cursor.close();
		db.close();
		return sessionInfo;

	}

	/**
	 * 根据会话id查询该会话的未读条数
	 * 
	 * @param sessionId
	 * @return
	 */
	public int querySessionByUnReadNum(String sessionId) {
		int num = 0;
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select unReadNum from tb_session where sessionId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			num = cursor.getInt(cursor.getColumnIndex("unReadNum"));
		}
		return num;
	}

	/**
	 * 修改未读条数，设置为已读
	 */
	public boolean updateSessionByUnReadNum(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("unReadNum", 0);
			long id = db.update("tb_session", values, "sessionId=?", new String[] { sessionId });
			if (id > 0) {
				return true;
			}
		} catch (Exception e) {
			Log.e("info", "修改未读条数失败:" + e.getMessage());
			e.printStackTrace();
		}
		// db.close();
		return false;
	}

	/**
	 * 查询所有未读消息条数
	 * 
	 * @param isNotification
	 *            1为通知 0为消息
	 * @return
	 */
	public int queryUnReadNum(int isNotification, String userId) {
		int maxUnReadNum = 0;
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select unReadNum from tb_session where isNotification=? and toUserId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(isNotification), userId });
		while (cursor.moveToNext()) {
			Integer unReadNum = cursor.getInt(cursor.getColumnIndex("unReadNum"));
			maxUnReadNum += unReadNum;
		}
		db.close();
		return maxUnReadNum;
	}

	/**
	 * 查询当前用户会话列表
	 * 
	 * @param userId 用户
	 * @param isNotification 0 是对话，1是通知
	 * @param pageSize 条数
	 * @return
	 */
	public List<SessionInfo> querySession(String userId, int isNotification, Integer pageSize) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<String> sessionIds = queryUserSessionByUserId(userId);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sessionIds.size(); i++) {
			if (i == sessionIds.size() - 1) {
				builder.append("'" + sessionIds.get(i) + "'");
			} else {
				builder.append("'" + sessionIds.get(i) + "',");
			}
		}
		List<SessionInfo> list = new ArrayList<SessionInfo>(); // sessionId
		String sql = "select * from tb_session where sessionId IN(" + builder.toString() + ") and isNotification=? order by time desc limit ?";//
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(isNotification), String.valueOf(pageSize) });
		while (cursor != null && cursor.moveToNext()) {
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
			list.add(sessionInfo);
		}
		Log.d("t", "list.size()====" + list.size());
		cursor.close();
		// db.close();
		return list;
	}
	
	public List<SessionInfo> querySession(String userId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<String> sessionIds = queryUserSessionByUserId(userId);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sessionIds.size(); i++) {
			if (i == sessionIds.size() - 1) {
				builder.append("'" + sessionIds.get(i) + "'");
			} else {
				builder.append("'" + sessionIds.get(i) + "',");
			}
		}
		List<SessionInfo> list = new ArrayList<SessionInfo>();
		String sql = "select * from tb_session where sessionId IN(" + builder.toString() + ") order by time desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor != null && cursor.moveToNext()) {
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
			list.add(sessionInfo);
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据会话Id删除会话 根据sessionId删除tb_session表记录
	 * 
	 * @param sessionId
	 */
	public void deleteSessionById(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		String sql = "delete from " + AccuvallySQLiteOpenHelper.TABLE_SESSION + " where sessionId=" + sessionId;
		db.execSQL(sql);
	}

	/**
	 * 根据sessionId,userId删除tb_user_session表记录
	 * 
	 */
	public void deleteUserSession(String sessionId, String userId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		String sql = "delete from " + AccuvallySQLiteOpenHelper.TB_USER_SESSION + " where sessionId=" + sessionId + " and userId=" + userId;
		db.execSQL(sql);
	}

	/**
	 * 根据sessionId,userId删除tb_message表记录
	 * 
	 */
	public boolean deleteMsgById(String sessionId, String userId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		String sql = "delete from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " where sessionId=" + sessionId + " and userId=" + userId;
		db.execSQL(sql);
		return true;
	}

	/**
	 * 查询某一个角色最后一次对话的服务器,时间最大
	 * 
	 * @param context
	 * @param user_id
	 * @return
	 */
	public MessageInfo queryLastMsg(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "SELECT * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " WHERE sessionId=? ORDER BY timestamp DESC limit 1";
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

	/**
	 * 查询某一个角色第一次对话的服务器时间
	 * 
	 * @param context
	 * @param user_id
	 * @return
	 */
	public MessageInfo queryFirstMsg(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
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
		db.close();
		return msg;
	}

	/**
	 * 查询某一个角色最后一次对话的服务器时间
	 * 
	 * @param limit
	 * 
	 * @param context
	 * @param user_id
	 * @return
	 */
	public ArrayList<MessageInfo> queryAllMsg(String sessionId, int limitStart, int limitEnd) {

		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "SELECT * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + "  WHERE sessionId=? ORDER BY timestamp ASC limit ?,?";
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

	public int queryAllMsgCount(String sessionId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "SELECT count(*) from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + "  WHERE sessionId=? ";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				return cursor.getInt(0);
			}
			cursor.close();
		}
		return 0;
	}

	public long insertFileMsg(MessageInfo msg) {

		if (selectMsgById(msg) != null) {
			return 0;
		}
		long id = 0;
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
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
		id = db.insert(AccuvallySQLiteOpenHelper.TABLE_MESSAGE, null, values);
		Log.d("d", "insert ==  " + id + msg.toString());
		return id;
	}

	public boolean updateMsg(MessageInfo msg) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
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
			id = db.update(AccuvallySQLiteOpenHelper.TABLE_MESSAGE, values, where, new String[] { msg.getSessionId(), msg.getUserId(), msg.getId() + "" });
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

	public static final String BULK_MSG = "INSERT INTO " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " ( " + "messageId,nickName,content,logourl,timestamp,receipTimestamp,msgType,userId)  " + "VALUES (?,?,?,?,?,?,?,?);";

	public MessageInfo bulkinsertMsg(List<AVIMMessage> msges, String userid) {
		// SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		// SQLiteStatement bodyIndexStatment = db.compileStatement(BULK_MSG);
		// if (db.isOpen()) {
		// db.beginTransaction();
		// try {
		// final int size = msges.size();
		// AVIMMessage message = null;
		// NotificationInfo nofity = null;
		// for (int i = 0; i < size; i++) {
		// message = msges.get(i);
		// Log.d("d", message.getContent());
		// nofity = JSON.parseObject(message.getContent(),
		// NotificationInfo.class);
		// bodyIndexStatment.bindString(1, message.getMessageId());
		// bodyIndexStatment.bindString(2, nofity.getSource_name());
		// bodyIndexStatment.bindString(3, nofity.getMessage());
		// bodyIndexStatment.bindString(4, nofity.getSource_logo_url());
		// timestamp = message.getTimestamp();
		// bodyIndexStatment.bindLong(5, timestamp);
		// bodyIndexStatment.bindLong(6, message.getReceiptTimestamp());
		// bodyIndexStatment.bindLong(7, nofity.getMessage_type());
		// bodyIndexStatment.bindString(8, userid);
		// bodyIndexStatment.executeInsert();
		// }
		// message = null;
		// nofity = null;
		// } catch (Exception e) {
		// Log.e("d", e.getMessage());
		// }
		// db.setTransactionSuccessful();
		// db.close();
		// }

		//
		// SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		// SQLiteStatement bodyIndexStatment = db.compileStatement(BULK_MSG);
		AVIMMessage message = null;
		NotificationInfo nofity = null;

		final int size = msges.size();
		MessageInfo result = null;
		// 插入数据库的条数
		int insertCount = 0;
		for (int i = size - 1; i >= 0; i--) {
			message = msges.get(i);
			// nofity = JSON.parseObject(message.getContent(),
			// NotificationInfo.class);
			// bodyIndexStatment.bindString(1, message.getMessageId());
			// bodyIndexStatment.bindString(2, nofity.getSource_name());
			// bodyIndexStatment.bindString(3, nofity.getMessage());
			// bodyIndexStatment.bindString(4, nofity.getSource_logo_url());
			// timestamp = message.getTimestamp();
			// bodyIndexStatment.bindLong(5, timestamp);
			// bodyIndexStatment.bindLong(6, message.getReceiptTimestamp());
			// bodyIndexStatment.bindLong(7, nofity.getMessage_type());
			// bodyIndexStatment.bindString(8, userid);
			// bodyIndexStatment.executeInsert();
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

	private MessageInfo selectMsgById(MessageInfo info) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		MessageInfo msg = null;
		String sql = "select * from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE + " where userId=? AND sessionId=? AND messageId=? AND timestamp=? limit 1";
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

	public boolean deleteMsg(MessageInfo msg) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
		// String sql = "delete from " + AccuvallySQLiteOpenHelper.TABLE_MESSAGE
		// + " where id=" + msg.getId();
		// db.execSQL(sql);
		// long id = 0;
		int id = db.delete(AccuvallySQLiteOpenHelper.TABLE_MESSAGE, " id=? And userId =? AND sessionId=?", new String[] { String.valueOf(msg.getId()), msg.getUserId(), msg.getSessionId() });
		return id > 0;
	}

	public boolean insertUserSession(String sessionId, String userId) {
		long id = 0;
		if (!existUserSession(sessionId, userId)) {
			SQLiteDatabase db = accuvallySQLiteOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("sessionId", sessionId);
			values.put("userId", userId);
			id = db.insert(AccuvallySQLiteOpenHelper.TB_USER_SESSION, null, values);
		}
		return id > 0;
	}

	public boolean existUserSession(String sessionId, String userId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		boolean exist = false;
		if (db != null) {
			String sql = "select * from " + AccuvallySQLiteOpenHelper.TB_USER_SESSION + " where sessionId= '" + sessionId + "' AND   userId='" + userId + "'  limit 1";
			Log.d("sql", sql);
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				exist = true;
			}
			cursor.close();
		}
		return exist;
	}

	public List<String> queryUserSessionByUserId(String userId) {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		String sql = "select sessionId from " + AccuvallySQLiteOpenHelper.TB_USER_SESSION + " where userId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { userId });
		List<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String sessionId = cursor.getString(cursor.getColumnIndex("sessionId"));
			list.add(sessionId);
		}
		cursor.close();
		return list;
	}

//	public List<SessionInfo> querySessions() {
//		// Log.d("info",
//		// "querySessions=======================================================");
//		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
//		String sql = "select * from tb_session ";//
//		Cursor cursor = db.rawQuery(sql, null);
//		List<SessionInfo> list = new ArrayList<SessionInfo>();
//		while (cursor != null && cursor.moveToNext()) {
//			SessionInfo sessionInfo = new SessionInfo();
//			sessionInfo.setSessionId(cursor.getString(cursor.getColumnIndex("sessionId")));
//			sessionInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
//			sessionInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
//			sessionInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
//			sessionInfo.setLogoUrl(cursor.getString(cursor.getColumnIndex("logoUrl")));
//			sessionInfo.setUnReadNum(cursor.getInt(cursor.getColumnIndex("unReadNum")));
//			sessionInfo.setFromUserId(cursor.getString(cursor.getColumnIndex("fromUserId")));
//			sessionInfo.setToUserId(cursor.getString(cursor.getColumnIndex("toUserId")));
//			sessionInfo.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
//			sessionInfo.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
//			sessionInfo.setSessionType(cursor.getInt(cursor.getColumnIndex("sessionType")));
//			sessionInfo.setMessageType(cursor.getInt(cursor.getColumnIndex("messageType")));
//			sessionInfo.setIsNotification(cursor.getInt(cursor.getColumnIndex("isNotification")));
//			sessionInfo.setOp_type(cursor.getInt(cursor.getColumnIndex("op_type")));
//			sessionInfo.setOp_value(cursor.getString(cursor.getColumnIndex("op_value")));
//			list.add(sessionInfo);
//			// Log.d("t",sessionInfo.toString());
//		}
//		return list;
//	}
//
//	public List<String> queryUserSessions() {
//		// Log.d("info",
//		// "=======================================================");
//		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
//		String sql = "select * from " + AccuvallySQLiteOpenHelper.TB_USER_SESSION;
//		Cursor cursor = db.rawQuery(sql, null);
//		List<String> list = new ArrayList<String>();
//		while (cursor.moveToNext()) {
//			list.add(cursor.getString(0) + ":" + cursor.getString(1));
//		}
//		// Log.d("t", Arrays.toString(list.toArray()));
//		return list;
//	}

}
