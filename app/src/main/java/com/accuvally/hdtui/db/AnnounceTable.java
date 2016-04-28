package com.accuvally.hdtui.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.accuvally.hdtui.model.AnnounceBean;
import com.accuvally.hdtui.model.NotificationInfo;

public class AnnounceTable {

	public static final String TABLE_ANNOUNCE = "announce";
	
	public static String createTable() {
		String createQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_ANNOUNCE + " (";

		Map<String, String> columns = getColumnMapping();

		boolean isAdd = false;
		for (String column : columns.keySet()) {
			if (isAdd) {
				createQuery += ", ";
			}
			isAdd = true;
			createQuery += column + " " + columns.get(column);
		}

		createQuery += ");";

		return createQuery;
	}

	protected static Map<String, String> getColumnMapping() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");

		map.put("sessionId", "TEXT");
		map.put("messageId", "TEXT");

		map.put("title", "TEXT");
		map.put("message", "TEXT");
		map.put("logourl", "TEXT");

		map.put("message_type", "INTEGER");
		map.put("sendTimestamp", "INTEGER");

		map.put("op_type", "INTEGER");
		map.put("op_value", "TEXT");
		
		return map;
	}

	public static void insert(NotificationInfo info, String messageId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();

		String sql = "select * from " + TABLE_ANNOUNCE + " where messageId=?";
		Cursor cursor = db.rawQuery(sql, new String[] { messageId });
		boolean isExist = cursor.getCount() > 0;
		cursor.close();
		
		if (isExist) return;

		ContentValues values = new ContentValues();
		values.put("sessionId", info.getTo_id());
		values.put("messageId", messageId);

		values.put("op_type", info.getOp_type());
		values.put("op_value", info.getOp_value());

		values.put("title", info.getTitle());
		values.put("logourl", info.getLogo_url());
		values.put("message", info.getMessage());

		values.put("message_type", info.getMessage_type());
		values.put("sendTimestamp", info.sendTimestamp);
		db.insert(TABLE_ANNOUNCE, null, values);
	}

	public static void insert(AnnounceBean msg) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("sessionId", msg.sessionId);
		values.put("messageId", msg.messageId);

		values.put("op_type", msg.op_type);
		values.put("op_value", msg.op_value);

		values.put("title", msg.title);
		values.put("message", msg.message);
		values.put("logourl", msg.logourl);

		values.put("message_type", msg.message_type);
		values.put("sendTimestamp", msg.sendTimestamp);
		db.insert(TABLE_ANNOUNCE, null, values);
	}

	public static List<AnnounceBean> query(String sessionId) {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getReadableDatabase();
		String sql = "SELECT * from " + TABLE_ANNOUNCE + " WHERE sessionId=? order by sendTimestamp desc limit 5";
		Cursor cursor = db.rawQuery(sql, new String[] { sessionId });

		ArrayList<AnnounceBean> cursorList = new ArrayList<AnnounceBean>();
		AnnounceBean bean = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				bean = new AnnounceBean();
				bean.op_type = cursor.getInt(cursor.getColumnIndex("op_type"));
				bean.op_value = cursor.getString(cursor.getColumnIndex("op_value"));

				bean.title = cursor.getString(cursor.getColumnIndex("title"));
				bean.message = cursor.getString(cursor.getColumnIndex("message"));
				bean.logourl = cursor.getString(cursor.getColumnIndex("logourl"));
				bean.sendTimestamp = cursor.getLong(cursor.getColumnIndex("sendTimestamp"));
				cursorList.add(bean);
			}
			cursor.close();
		}
		return cursorList;
	}
}
