package com.accuvally.hdtui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.db.MailMessageTable;

/**
 * Created by Andy Liu on 2017/4/18.
 */
public class MailCountUtil {


    public static void clearAll(Context mContext){

        SharedPreferences sp = mContext.getSharedPreferences(SPUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Keys.KEY_RECOMD_UNREAD_NUM, 0);
        editor.putInt(Keys.KEY_SYSTEM_MSG_UNREAD_NUM, 0);
        editor.putInt(Keys.KEY_NEW_FRIEND_UNREAD_NUM, 0);

        editor.putInt(Keys.KEY_RECOMD_TOTAL_NUM, 0);
        editor.putInt(Keys.KEY_SYSTEM_MSG_TOTAL_NUM, 0);
        editor.putInt(Keys.KEY_NEW_FRIEND_TOTAL_NUM, 0);


        editor.putBoolean(Keys.KEY_SYSTEM_MSG_HAS_GET_HISTORY, false);
        editor.putBoolean(Keys.KEY_RECOMD_HAS_GET_HISTORY, false);
        editor.putBoolean(Keys.KEY_NEW_FRIEND_HAS_GET_HISTORY,false);


        editor.commit();
    }

    public static void saveUnreadCount(String type,int unReadNum){
        switch (type){
            case MailMessageTable.recommend:
                SPUtil.putInt(Keys.KEY_RECOMD_UNREAD_NUM,unReadNum);
                break;
            case MailMessageTable.sysMsg:
                SPUtil.putInt(Keys.KEY_SYSTEM_MSG_UNREAD_NUM,unReadNum);
                break;
            case MailMessageTable.newFriend:
                SPUtil.putInt(Keys.KEY_NEW_FRIEND_UNREAD_NUM,unReadNum);
                break;
        }
    }



    public static boolean getHistoryState(String type){
        boolean ret=false;
        switch (type){
            case MailMessageTable.recommend:
                ret=SPUtil.getBoolean(Keys.KEY_RECOMD_HAS_GET_HISTORY, false);
                break;
            case MailMessageTable.sysMsg:
                ret=SPUtil.getBoolean(Keys.KEY_SYSTEM_MSG_HAS_GET_HISTORY, false);
                break;
            case MailMessageTable.newFriend:
                ret=SPUtil.getBoolean(Keys.KEY_NEW_FRIEND_HAS_GET_HISTORY,false);
                break;
        }

        return ret;
    }



    public static void saveHistoryStatet(String type,boolean state){
        switch (type){
            case MailMessageTable.recommend:
                SPUtil.putBoolean(Keys.KEY_RECOMD_HAS_GET_HISTORY, state);
                break;
            case MailMessageTable.sysMsg:
                SPUtil.putBoolean(Keys.KEY_SYSTEM_MSG_HAS_GET_HISTORY, state);
                break;
            case MailMessageTable.newFriend:
                SPUtil.putBoolean(Keys.KEY_NEW_FRIEND_HAS_GET_HISTORY,state);
                break;
        }
    }


    public static void saveTotalCount(String type,int unReadNum){
        switch (type){
            case MailMessageTable.recommend:
                SPUtil.putInt(Keys.KEY_RECOMD_TOTAL_NUM,unReadNum);
                break;
            case MailMessageTable.sysMsg:
                SPUtil.putInt(Keys.KEY_SYSTEM_MSG_TOTAL_NUM,unReadNum);
                break;
            case MailMessageTable.newFriend:
                SPUtil.putInt(Keys.KEY_NEW_FRIEND_TOTAL_NUM,unReadNum);
                break;
        }
    }




    public static int getUnreadCount(String type){
        int ret=0;
        switch (type){
            case MailMessageTable.recommend:
                ret=SPUtil.getInt(Keys.KEY_RECOMD_UNREAD_NUM,0);
                break;
            case MailMessageTable.sysMsg:
                ret=SPUtil.getInt(Keys.KEY_SYSTEM_MSG_UNREAD_NUM,0);
                break;
            case MailMessageTable.newFriend:
                ret=SPUtil.getInt(Keys.KEY_NEW_FRIEND_UNREAD_NUM,0);
                break;
        }

        return ret;
    }

    public static int getTotalCount(String type){
        int ret=0;
        switch (type){
            case MailMessageTable.recommend:
                ret=SPUtil.getInt(Keys.KEY_RECOMD_TOTAL_NUM,0);
                break;
            case MailMessageTable.sysMsg:
                ret=SPUtil.getInt(Keys.KEY_SYSTEM_MSG_TOTAL_NUM,0);
                break;
            case MailMessageTable.newFriend:
                ret=SPUtil.getInt(Keys.KEY_NEW_FRIEND_TOTAL_NUM,0);
                break;
        }
        return ret;
    }
}
