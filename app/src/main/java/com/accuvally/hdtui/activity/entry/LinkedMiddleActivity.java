package com.accuvally.hdtui.activity.entry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.register.RegAccuActivity;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.util.LinkProperties;

import java.util.HashMap;

/**
 * Created by Andy Liu on 2017/1/18.
 */
public class LinkedMiddleActivity extends AppCompatActivity {

    /*解析深度链获取跳转参数，开发者自己实现参数相对应的页面内容
    通过LinkProperties对象调用getControlParams方法获取自定义参数的HashMap对象,
            * 通过创建的自定义key获取相应的值,用于数据处理。
            */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            //获取与深度链接相关的值
            LinkProperties linkProperties = getIntent().getParcelableExtra(LinkedME.LM_LINKPROPERTIES);
            if (linkProperties != null) {
                HashMap<String, String> hashMap = linkProperties.getControlParams();

                //获取传入的参数
                if(hashMap.containsKey("sence")){
                    String sence = hashMap.get("sence");
//                            Log.e(TAG, "sence: " + sence);
                    switch (sence){
                        case "share":
                            Intent intent = new Intent(LinkedMiddleActivity.this, AccuvallyDetailsActivity.class);
                            intent.putExtra("id", hashMap.get("eid"));//活动ID
//                           intent.putExtra(BaseActivityDeepLink.Channle, channel);
                            startActivity(intent);
                            break;
        // (2)优惠码分享链接   sence=coupon&isapp=1&eid={活动id}&code={优惠码加密}
                        case "coupon":
                            Intent intent1=new Intent(LinkedMiddleActivity.this, RegAccuActivity.class);
                            intent1.putExtra(RegAccuActivity.ID,hashMap.get("eid"));
                            intent1.putExtra(RegAccuActivity.CODE,hashMap.get("code"));
                            intent1.putExtra(RegAccuActivity.FROM,"LINKEDME");
//                                    Log.e(TAG, "code: " + hashMap.get("code"));
//                                    Log.e(TAG, "id: " + hashMap.get("eid"));
                            startActivity(intent1);
                            break;
                    }
                }
            }
        }
        finish();
    }




}


