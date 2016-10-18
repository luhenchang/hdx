package com.accuvally.hdtui.activity.entry;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.register.RegAccuActivity;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.callback.LMLinkCreateListener;
import com.microquation.linkedme.android.callback.LMReferralCloseListener;
import com.microquation.linkedme.android.callback.LMSimpleInitListener;
import com.microquation.linkedme.android.indexing.LMUniversalObject;
import com.microquation.linkedme.android.referral.LMError;
import com.microquation.linkedme.android.util.LinkProperties;

import java.util.HashMap;

/**
 * Created by Andy Liu on 2016/9/23.
 */
public class BaseDeepLink extends FragmentActivity {


    private LinkedME linkedME;
    private static final String TAG = "LinkedME-Demo";
    /**
     * <p>解析深度链获取跳转参数，开发者自己实现参数相对应的页面内容</p>
     * <p>通过LinkProperties对象调用getControlParams方法获取自定义参数的HashMap对象,
     * 通过创建的自定义key获取相应的值,用于数据处理。</p>
     */
    //linkProperties  lmUniversalObject都携带有数据
    LMSimpleInitListener simpleInitListener = new LMSimpleInitListener() {
        @Override
        public void onSimpleInitFinished(LMUniversalObject lmUniversalObject,
                                         LinkProperties linkProperties, LMError error) {
            try {
//                Log.e(TAG, "开始处理deep linking数据... " + this.getClass().getSimpleName());
                if (error != null) {
                    Log.e(TAG, "LinkedME初始化失败. " + error.getMessage());
                } else {

//                    Log.e(TAG, "LinkedME初始化完成");

                    if (linkProperties != null) {
//                        Log.e("LinkedME-Demo", "Channel " + linkProperties.getChannel());

                        HashMap<String, String> hashMap = linkProperties.getControlParams();

                        //获取传入的参数
                        if(hashMap.containsKey("sence")){
                            String sence = hashMap.get("sence");
//                            Log.e(TAG, "sence: " + sence);

                            switch (sence){
                                case "share":
                                    Intent intent = new Intent(BaseDeepLink.this, AccuvallyDetailsActivity.class);
                                    intent.putExtra("id", hashMap.get("eid"));//活动ID
//                                    intent.putExtra(BaseActivityDeepLink.Channle, channel);
                                    startActivity(intent);
                                    break;
//                                (2)优惠码分享链接   sence=coupon&isapp=1&eid={活动id}&code={优惠码加密}
                                case "coupon":


                                    Intent intent1=new Intent(BaseDeepLink.this, RegAccuActivity.class);
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
                    if (lmUniversalObject != null) {
//                        Log.e("LinkedME-Demo", "title " + lmUniversalObject.getTitle());
//                        Log.e(TAG, "lmUniversalObject != null");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void printLinkMeDevice(){
        String device_id = LinkedME.getInstance().getDeviceId();
        Log.e(TAG, "LinkedME device id:  " + device_id);
    }

    private void printLinkMeURL(){
        /**创建深度链接*/
        //深度链接属性设置
        final LinkProperties properties = new LinkProperties();
        //渠道
        properties.setChannel("LINKME");  //微信、微博、QQ
        //功能
        properties.setFeature("Share");
        //标签
        properties.addTag("LinkedME");
        properties.addTag("Demo");
        //阶段
        properties.setStage("Live");
        //自定义参数,用于在深度链接跳转后获取该数据
        properties.addControlParameter("ActivityId", "1");
        properties.addControlParameter("View", "Demo222");
        LMUniversalObject universalObject = new LMUniversalObject();
        universalObject.setTitle("aaaaa");

        // Async Link creation example
        universalObject.generateShortUrl(BaseDeepLink.this, properties, new LMLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, LMError error) {
//                demo_edit.setText(url);
//                demo_link_view.setVisibility(View.VISIBLE);
                //获取自定义参数数据
//                demo_link_view.setText(properties.getControlParams().toString());
                Log.e(TAG, "LinkedME URL " + url);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


//        Log.i(TAG, "onStart: " + this.getClass().getSimpleName());
        try {
            //如果消息未处理则会初始化initSession，因此不会每次都去处理数据，不会影响应用原有性能问题
            if (!LinkedME.getInstance().isHandleStatus()) {
//                Log.e(TAG, "LinkedME initSession... " + this.getClass().getSimpleName());
                //初始化LinkedME实例
                linkedME = LinkedME.getInstance();
                //初始化Session，获取Intent内容及跳转参数
                linkedME.initSession(simpleInitListener, this.getIntent().getData(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        Log.i(TAG, "onNewIntent: " + this.getClass().getSimpleName());
        simpleInitListener.reset();
        setIntent(intent);
    }


    @Override
    public void onStop() {
        super.onStop();
//        Log.i(TAG, "onStop: " + this.getClass().getSimpleName());
        if (linkedME != null) {
            linkedME.closeSession(new LMReferralCloseListener() {
                @Override
                public void onCloseFinish() {
                }
            });
        }
    }
}
