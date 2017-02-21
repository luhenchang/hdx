package com.accuvally.hdtui.utils;

import android.app.Activity;
import android.content.Intent;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.activity.entry.MainActivityNew;
import com.accuvally.hdtui.activity.entry.SetCategoryActivity;
import com.accuvally.hdtui.config.Keys;

/**
 * Created by Andy Liu on 2017/1/11.
 */
public class ActivityUtils {

    public static void toNext(Activity mContext){
        boolean have_select_categorys= AccuApplication.getInstance().
                sharedUtils.readBoolean(Keys.have_select_categorys);
        if(have_select_categorys){
            Intent intent = new Intent(mContext, MainActivityNew.class);
            mContext.startActivity(intent);
        }else {
            Intent intent = new Intent(mContext, SetCategoryActivity.class);//第一次开启app时显示失败，第二次开启app
            mContext.startActivity(intent);
        }
    }
}
