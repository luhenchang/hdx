package com.accuvally.hdtui.utils;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.db.MailMessageTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.utils.eventbus.EventRedDot;
import com.accuvally.hdtui.utils.eventbus.MailCountChange;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;

import de.greenrobot.event.EventBus;

/**
 * Created by Andy Liu on 2017/5/3.
 */
public class HttpMailCountUtil {
    private static int getUnReadCountTime=0;//当第三次的时候(即三种消息的未读数都加载完成之后)，重新排列session的位置
    private static int unreadNum=0;
    public static final String TAG="HttpMailCountUtil";


    public static   void getAllMessageCount(final String tagString){
        if(!AccuApplication.getInstance().checkIsLogin()){
            Trace.e(TAG, "getMessageCount,"+tagString+",not login");
            return;
        }

        getMessageCount(MailMessageTable.recommend,tagString);
        getMessageCount(MailMessageTable.sysMsg,tagString);
        getMessageCount(MailMessageTable.newFriend,tagString);
    }


    public static   void getMessageCount(final String inboxType,final String tagString){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "https://api.leancloud.cn/1.1/subscribe/statuses/count";
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");

                    org.json.JSONObject json = new org.json.JSONObject();
                    json.put("__type", "Pointer");
                    json.put("className", "_User");
                    json.put("objectId", AccountManager.getAccount());//
                    HttpGet request = new HttpGet(url+"?owner="+ URLEncoder.encode(json.toString(), "utf-8")+"&inboxType="+inboxType);//暂时写死
                    request.addHeader("X-AVOSCloud-Application-Id", Config.ACCUPASS_LEANCLOUD_APP_ID);
                    request.addHeader("X-AVOSCloud-Application-Key", Config.ACCUPASS_LEANCLOUD_APP_KEY);
                    request.addHeader("Content-Type", "application/json");
                    HttpResponse response = httpClient.execute(request);
                    String result= EntityUtils.toString(response.getEntity());
                    Trace.e(TAG, "getMessageCount,"+inboxType+",result:"+result);
                    try {
                        org.json.JSONObject jsonObject=new org.json.JSONObject(result);
                        if(jsonObject.has("total")) {
                            int total=jsonObject.getInt("total");
                            MailCountUtil.saveTotalCount(inboxType, total);
                        }

                        if(jsonObject.has("unread")) {
                            int unread=jsonObject.getInt("unread");
                            unreadNum+=unread;
                            MailCountUtil.saveUnreadCount(inboxType, unread);
                        }

                        getUnReadCountTime++;
                        if(getUnReadCountTime>=3) {

                            EventBus.getDefault().post(new EventRedDot(0,true));
                            if(unreadNum!=0){
                                EventBus.getDefault().post(new MailCountChange());
                            }
                            Trace.e(TAG, "getMessageCount,"+tagString+",unreadNum:"+unreadNum);
                            getUnReadCountTime=0;
                            unreadNum=0;
                        }
                    }catch (Exception e){
                        Trace.e(TAG,e.getMessage());
                    }

                }catch (Exception e) {
                    Trace.e(TAG,e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
