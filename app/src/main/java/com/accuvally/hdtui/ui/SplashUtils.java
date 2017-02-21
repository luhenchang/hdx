package com.accuvally.hdtui.ui;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Keys;

/**
 * Created by Andy Liu on 2017/1/19.
 */
public class SplashUtils {

    private static final  int INTERVAL=1000*60*2;
    public static  final String TAG="SplashUtils";
    public static void saveSplashTimeInPre(){
        AccuApplication.getInstance().sharedUtils.
                writeLong(Keys.KEY_SPLASH_LASTTIME,System.currentTimeMillis());
    }


    public static long getSplashTimeInPre(){
        return AccuApplication.getInstance().sharedUtils.readLong(Keys.KEY_SPLASH_LASTTIME);
    }


    public static boolean isEnoughtTimeToSplash(){
        long current=System.currentTimeMillis();
        long pre=getSplashTimeInPre();
//        Trace.e("registActivityLifeCycle", "current:"+current+"  pre:"+pre);
        return  current-pre>INTERVAL;
    }

    }
