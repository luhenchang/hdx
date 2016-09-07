
package com.accuvally.hdtui.utils;

import android.util.Log;

public class Trace {

    public static final int LEVEL_SILENCE = -1;

    public static final int LEVEL_ERROR = 0;

    public static final int LEVEL_WARN = 1;

    public static final int LEVEL_INFO = 2;

    public static final int LEVEL_DEBUG = 3;

    public static final int LEVEL_VERBOSE = 5;

    private static int level = LEVEL_VERBOSE;


    /*static {
        if(BuildConfig.DEBUG){
            level = LEVEL_VERBOSE;  //测试环境的,把所有日志都打开
        }else {
            level = LEVEL_SILENCE;  //发布环境的，把所有日志都关掉
        }
//        Trace.e("BuildConfig", "BuildConfig.DEBUG=" + BuildConfig.DEBUG + "\nBuildConfig.BUILD_TYPE:"+BuildConfig.BUILD_TYPE);
        Trace.e("Trace", "level=" + level);
    }*/

    public static void setLevel(int l) {
        level = l;
    }

    public static void d(String TAG, String msg) {
        if (level >= LEVEL_DEBUG)
            Log.d(TAG, msg == null ? "msg is Null" : msg);
    }

    public static void e(String TAG, String msg) {
        if (level >= LEVEL_ERROR)
            Log.e(TAG, msg == null ? "msg is Null" : msg);
    }

    public static void v(String TAG, String msg) {
        if (level >= LEVEL_VERBOSE)
            Log.v(TAG, msg == null ? "msg is Null" : msg);
    }

    public static void i(String TAG, String msg) {
        if (level >= LEVEL_INFO)
            Log.i(TAG, msg == null ? "msg is Null" : msg);
    }

    public static void w(String TAG, String msg) {
        if (level >= LEVEL_WARN)
            Log.w(TAG, msg == null ? "msg is Null" : msg);
    }
}
