package com.accuvally.hdtui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.manager.LeanCloud;

public class AccuvallySQLiteOpenHelper extends SQLiteOpenHelper {

	// 浏览历史
	public final String TABLE_HISTORICAL = "tb_history";

	// 创建历史记录
	final String CREATE_HISTORICAL = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_HISTORICAL
			+ " (Id INTEGER PRIMARY KEY AUTOINCREMENT,HistoryId INTEGER,Title,Address,Start,End,LogoUrl,LikeNum,Url,Source,SourceType INTEGER,ShareUrl)";

	public final String TABLE_SEARCH = "tb_search";

	// 创建搜索历史
	final String CREATE_TABLE_SEARCH = "CREATE TABLE IF NOT EXISTS " + TABLE_SEARCH
			+ " (Id INTEGER PRIMARY KEY AUTOINCREMENT,searchName)";

	public final String TABLE_CLASSFY = "tb_classfy";

	final String CREATE_TABLE_CLASSFY = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSFY + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,name)";

	public final String TABLE_ACCU_DETAILS = "tb_accu_details";

	final String CREATE_TABLE_ACCU_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCU_DETAILS
			+ " (Id INTEGER PRIMARY KEY AUTOINCREMENT,accuId INTEGER)";

	public final String TABLE_BEHAVIOR = "tb_accu_behavior";

	final String CREATE_TABLE_BEHAVIOR = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_BEHAVIOR
			+ " (Id INTEGER PRIMARY KEY AUTOINCREMENT,uid,tid,type,target_type,target_id,data_source,location,country,province,city,date_time,key_word,device_type,tag,version,event_name,event_data)";

	/************************************** 会话相关 *****************************************************/
	/**
	 * 用户表，其中包括圈子里的
	 */
	public static final String TB_USER = "tb_user";

	final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ TB_USER
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, Account TEXT,City TEXT,Country TEXT,Email TEXT,EmailActivated boolean ,sex INTEGER,Brief TEXT,Logo TEXT, LogoLarge TEXT,Nick TEXT,OpenIdBaidu TEXT,OpenIdQQ TEXT,OpenIdRenRen TEXT,OpenIdWeibo TEXT,Phone TEXT,Status INTEGER,LoginType INTEGER)";

	public static final String TB_USER_SESSION = "tb_user_session";
	final String CREATE_TABLE_USER_SESSION = "CREATE TABLE IF NOT EXISTS  " + TB_USER_SESSION
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, sessionId TEXT,userId TEXT)";

	// 会话列表
	public static final String TABLE_SESSION = "tb_session";
	final String CREATE_TABLE_SESSION = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_SESSION
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,sessionId,title,content,time INTEGER,logoUrl,unReadNum INTEGER,fromUserId,toUserId,messageId,nickName,sessionType INTEGER,messageType INTEGER,isNotification INTEGER,extend INTEGER,op_type,op_value)";

	// 消息记录
	public static final String TABLE_MESSAGE = "tb_message";
	final String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MESSAGE
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,messageId TEXT,message_type,nickName TEXT,logourl TEXT,content TEXT,msgType TEXT,timestamp INTEGER, receipTimestamp INTEGER,sessionId TEXT,userId TEXT,filePath TEXT,fileUrl TEXT,fileThumbnailUrl TEXT,textStr TEXT,lengh REAL,imgWidth INTEGER,imgHeight INTEGER)";

	public static final String DB_NAME = "accuvally.db";
	public static final int DB_VERSION = 43;

	private static AccuvallySQLiteOpenHelper helper;

	public static AccuvallySQLiteOpenHelper getInstance() {
		if (helper == null) {
			synchronized (AccuvallySQLiteOpenHelper.class) {
				if (helper == null) {
					helper = new AccuvallySQLiteOpenHelper(AccuApplication.getInstance());
				}
			}
		}
		return helper;
	}

	public AccuvallySQLiteOpenHelper(Context context) {
		super(context, Config.DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_HISTORICAL);
		db.execSQL(CREATE_TABLE_SEARCH);
		db.execSQL(CREATE_TABLE_CLASSFY);
		db.execSQL(CREATE_TABLE_ACCU_DETAILS);
		db.execSQL(CREATE_TABLE_BEHAVIOR);

		db.execSQL(CREATE_TABLE_USER_SESSION);
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_MESSAGE);
		db.execSQL(SessionTable.CREATE_TABLE_SESSION);
		db.execSQL(SystemMessageTable.CREATE_SYSTEM_MESSAGE);
	}

	public void onDrop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORICAL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSFY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCU_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEHAVIOR);

		db.execSQL("DROP TABLE IF EXISTS " + TB_USER_SESSION);
		db.execSQL("DROP TABLE IF EXISTS " + TB_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
		db.execSQL("DROP TABLE IF EXISTS " + SystemMessageTable.SYSTEM_MESSAGE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			onDrop(db);
			onCreate(db);
			LeanCloud.leanCloudLogin();
		}
	}
}
