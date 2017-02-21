package com.accuvally.hdtui.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.accuvally.hdtui.AccuApplication;

import java.lang.reflect.AnnotatedElement;


/**
 * 描述：界面UI相关工具类
 * Created by huangyk on 2016/1/14.
 */
public class UIUtils {

    public static Context getContext() {
        return AccuApplication.getInstance();
    }



    public static void setBrightness(Activity activity, int value) {

        // BRIGHTNESS_OVERRIDE_FULL : 1.0
        // BRIGHTNESS_OVERRIDE_OFF : 0.0

        if (value < 10) {
            value = 10;
        } else if (value > 100) {
            value = 100;
        }

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = (float) value / 100;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

//
    private void test(){
        AnnotatedElement annotatedElement;
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }


    /**
     * 获取屏幕宽度和高度，单位为px
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;

    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    public static View inflate(int resId, ViewGroup root) {
        return LayoutInflater.from(getContext()).inflate(resId, root);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {

        }
        return value;
    }

    /**
     * 设置屏幕亮度
     */
    public static void setScreenBrightness(Activity activity, int value) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }

 /*   *//**
     * 判断当前的线程是不是在主线程
     *//*
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }
*/
  /*  *//**
     * 在主线程中运行
     *//*
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }*/

    /**
     * 在主线程执行runnable
     */
/*
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }
*/

/*    *//**
     * 获取主线程的handler
     *//*
    public static Handler getHandler() {
        return AccupassApplication.getMainThreadHandler();
    }*/

    /**
     * 判断触点是否落在该View上
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getRawX();
        float motionY = ev.getRawY();
        return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth())
                && motionY >= vLoc[1] && motionY <= (vLoc[1] + v.getHeight());
    }

}
