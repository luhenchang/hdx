package com.accuvally.hdtui.utils;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Andy Liu on 2016/8/5.
 */
public class LoginUtil {


//    是否主办方：
    public static boolean isSporsor(String orgid){

        if (!"0".equals(orgid))
            return true;
        else
            return false;
    }


    public static void threeLogin(int status, Map<String, Object> info) {
         HttpCilents httpCilents=new HttpCilents(AccuApplication.getInstance());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status",status+""));
        params.add(new BasicNameValuePair("info", info.toString()));

        httpCilents.postA(Url.ACCUPASS_THREE_LOGIN, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {

                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:

                        break;
                    case Config.RESULT_CODE_ERROR:

                        break;
                }
            }
        });
    }

    //======================================================================


    public static void getUserInfo(HttpCilents httpCilents,final AccuApplication application) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        showProgress("正在获取用户资料");
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_USER_INFO, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
//                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (info.isSuccess()) {
                            UserInfo userInfo = JSON.parseObject(info.getResult(), UserInfo.class);
                            Trace.d("PersonalActivity","从后台获取的性别数据 value="+userInfo.getGender());
                            application.setUserInfo(userInfo);
//                            application.sharedUtils.writeString("userName", name);
//                            application.showMsg("同步成功");
//                            application.sharedUtils.writeString("accountId", userInfo.getAccount());

//                            application.leanCloudLogin(userInfo.getAccount());
                            EventBus.getDefault().post(new ChangeUserStateEventBus(ChangeUserStateEventBus.LOGIN));
//                            finish();
                        } else {
                            application.showMsg(info.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }
}
