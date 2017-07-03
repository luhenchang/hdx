package com.accuvally.hdtui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.model.SaveBeHaviorInfo;
import com.accuvally.hdtui.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //========================================================================================
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


    //========================================================================================

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


    //========================================================================================

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

    //========================================================================================

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
		db.insert(AccuvallySQLiteOpenHelper.TABLE_BEHAVIOR, null, values);
		db.close();
	}

	// 读取行为记录
	public List<SaveBeHaviorInfo> getSaveBeHavior() {
		SQLiteDatabase db = accuvallySQLiteOpenHelper.getReadableDatabase();
		List<SaveBeHaviorInfo> list = new ArrayList<SaveBeHaviorInfo>();
		String sql = "select * from " + AccuvallySQLiteOpenHelper.TABLE_BEHAVIOR;
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
	public static void deleteSaveBeHavior() {
		SQLiteDatabase db = AccuvallySQLiteOpenHelper.getInstance().getWritableDatabase();
		db.delete(AccuvallySQLiteOpenHelper.TABLE_BEHAVIOR, null, null);
	}

//========================================================================================

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

    //========================================================================================






}
