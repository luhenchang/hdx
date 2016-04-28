package com.accuvally.hdtui.db;

import com.accuvally.hdtui.model.UserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperationDB_User extends AccuvallySQLiteOpenHelper {

	public OperationDB_User(Context context) {
		super(context);
	}
	
	public void deleteUser(int userID){
		SQLiteDatabase db =  getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS "+TB_USER);
	}

	public void insertUser(UserInfo info){
		SQLiteDatabase db =  getWritableDatabase();
		UserInfo exist = selectUserByUserIdS(info);
		if (exist==null) {
			ContentValues values = new ContentValues();
			values.put("Account", info.getAccount());
			values.put("Brief", info.getBrief());
			values.put("City", info.getCity());
			values.put("Logo", info.getLogo());
			values.put("LogoLarge", info.getLogoLarge());
			values.put("Nick", info.getNick());
			values.put("OpenIdBaidu", info.getOpenIdBaidu());
			values.put("OpenIdQQ", info.getOpenIdQQ());
			values.put("OpenIdRenRen", info.getOpenIdRenRen());
			values.put("OpenIdWeibo", info.getOpenIdWeibo());
			values.put("Phone", info.getPhone());
			values.put("Gender", info.getGender());
			values.put("LoginType", info.getLoginType());
			values.put("OpenIdWeibo", info.getOpenIdWeibo());
			values.put("Status", info.getStatus());
			db.insert(AccuvallySQLiteOpenHelper.TB_USER, null, values);
			db.close();
		}
	}

	private UserInfo selectUserByUserIdS(UserInfo info) {
		SQLiteDatabase db =  this.getReadableDatabase();
		String sql = "select * from "+TB_USER+" where Account=" + info.getAccount() + "  limit 1";
		Cursor cursor = db.rawQuery(sql, null);
		UserInfo newInfo = null;
		while (cursor.moveToNext()) {
			newInfo = new UserInfo();
//			newInfo.setAccount(cursor.getColumnName(arg0))
		}
		return newInfo;
	}
}
