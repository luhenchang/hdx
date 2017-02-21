package com.accuvally.hdtui.utils.exception;

import android.text.TextUtils;
import android.util.Log;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：自定义日志打印类
 * 可控开关的日志调试，并可以输出到文件
 * Created by huangyk on 2016/1/14.
 */
public class LogUtils {

    private static String TAG = "LogUtils";

    // 日志输出级别E
    public static final int LEVEL_ERROR = 1;
    // 日志输出级别W
    public static final int LEVEL_WARN = 2;
    // 日志输出级别I
    public static final int LEVEL_INFO = 3;
    // 日志输出级别D
    public static final int LEVEL_DEBUG = 4;
    // 日志输出级别V
    public static final int LEVEL_VERBOSE = 5;

    // 日志文件总开关
    private static Boolean MYLOG_SWITCH = true;
    // 日志写入文件开关
    private static Boolean MYLOG_WRITE_TO_FILE = true;
    // 输入日志类型
    private static char MYLOG_LEVEL = 4;
    // 日志文件存放目录
    private static String MYLOG_PATH = "";
    // sd卡中日志文件的最多保存天数
    private static int MYLOG_FILE_SAVE_DAYS = 7;
    // 本类输出的日志文件名称
    private static String MYLOG_FILE_NAME = "run0219";
    // 日志的输出格式
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 日志文件的输出格式
    private static SimpleDateFormat logfileSdf = new SimpleDateFormat("yyyy-MM-dd");

    private LogUtils() {

    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    public static void e(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {
        if (MYLOG_SWITCH) {
            if ('e' == level && MYLOG_LEVEL >= LEVEL_ERROR) {
                Log.e(tag, msg);
            } else if ('w' == level && MYLOG_LEVEL >= LEVEL_WARN) {
                Log.w(tag, msg);
            } else if ('d' == level && MYLOG_LEVEL >= LEVEL_DEBUG) {
                Log.d(tag, msg);
            } else if ('i' == level && MYLOG_LEVEL >= LEVEL_INFO) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            if (MYLOG_WRITE_TO_FILE) {
                writeLogToFile(String.valueOf(level), tag, msg);
            }
        }
    }

    private static String initPath() {
        if (TextUtils.isEmpty(MYLOG_PATH)) {
            if (FileUtils.isSDCardAvailable()) {
                MYLOG_PATH = FileUtils.getRootPath() + "/logs";
                File dir = new File(MYLOG_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }
        return MYLOG_PATH;
    }

    /**
     * 打开日志文件并写入日志
     **/
    private synchronized static void writeLogToFile(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        StringBuilder builder = new StringBuilder();
        builder.append(myLogSdf.format(nowtime)).append("    ").append(mylogtype).append("    ")
                .append(tag).append("    ").append(text);
        File file = new File(initPath(), MYLOG_FILE_NAME + "_" + logfileSdf.format(nowtime) + ".log");

        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);//true 不进行覆盖
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(builder.toString());
            bufWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bufWriter);
            IOUtils.close(filerWriter);
        }
    }

    /**
     * 删除过期的日志文件
     */
    public static void delOutOfDateLogs() {
        long needDeltime = getDateBefore().getTime();
        File[] files = (new File(MYLOG_PATH)).listFiles();
        if (files.length > 0) {
            for (File file : files) {
                long lastModified = file.lastModified();
                if (lastModified < needDeltime) {
                    LogUtils.d(TAG, "===== delOutOfDateLogs：" + file.getPath());
                    file.delete();
                }
            }
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - MYLOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

}
