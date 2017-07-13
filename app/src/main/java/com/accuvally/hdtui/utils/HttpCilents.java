package com.accuvally.hdtui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.RobWaiteInfo;
import com.accuvally.hdtui.utils.hawkj2.Algorithm;
import com.accuvally.hdtui.utils.hawkj2.AuthorizationHeader;
import com.accuvally.hdtui.utils.hawkj2.HawkContext;
import com.accuvally.hdtui.utils.hawkj2.HawkContext.HawkContextBuilder_C;
import com.accuvally.hdtui.utils.hawkj2.HawkWwwAuthenticateContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("HandlerLeak")
public class HttpCilents {

    private static final String API_VERSION = "v3.5.0";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);


    AccuApplication application;

    Context context;

    public HttpCilents(Context context) {
        this.context = context;
        application = (AccuApplication) context.getApplicationContext();
    }

    public interface WebServiceCallBack {
        public void callBack(int code, Object result);
    }
    ///http://api.huodongxing.com/v3/search/home_data?city=%E5%85%A8%E5%9B%BD&data=xxx
    public String HawkGet(String url) throws MalformedURLException {
        String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
        String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
        String baseUrl = "";
        String str = "";
        URL realUrl = new URL(url);
        if (!Url.ISDEBUG) {
            str = url.split("com")[1];
        } else {
            if (url.indexOf("https") != -1)
                str = url.split(String.valueOf(Url.ACCUPASS_SERVICE_PORT2))[1];
            else
                str = url.split(String.valueOf(Url.ACCUPASS_SERVICE_PORT))[1];
        }
        boolean bl = str.endsWith("?");
        if (bl)
            baseUrl = realUrl.getPath();
        else {
            String plus = "";
            if (realUrl.getQuery() != null) {
                plus = "?" + realUrl.getQuery();//即?后面的string
            }
            baseUrl = realUrl.getPath() + plus;
        }
        AuthorizationHeader authorizationHeader = null;
        try {

            HawkContextBuilder_C b = HawkContext.offset(application.hawkOffset).
                    request("GET", baseUrl, Url.ACCUPASS_SERVICE_HOST,
                            url.indexOf("https") != -1 ?
                                    Url.ACCUPASS_SERVICE_PORT2 : Url.ACCUPASS_SERVICE_PORT).credentials(accu_id, accu_key, Algorithm.SHA_256);
            authorizationHeader = b.build().createAuthorizationHeader();
            return authorizationHeader.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String  HawkPost(String url) throws MalformedURLException {
        String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
        String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
//        Log.i("info", accu_id);
//        Log.i("info", accu_key);
        AuthorizationHeader authorizationHeader = null;
        URL realUrl = new URL(url);
        try {
            HawkContextBuilder_C b = HawkContext.offset(application.hawkOffset).
                    request("POST", realUrl.getPath(), Url.ACCUPASS_SERVICE_HOST,
                            url.indexOf("https") != -1 ? Url.ACCUPASS_SERVICE_PORT2 : Url.ACCUPASS_SERVICE_PORT).
                    credentials(accu_id, accu_key, Algorithm.SHA_256);
            authorizationHeader = b.build().createAuthorizationHeader();
            return authorizationHeader.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //带有？查询条件的要用这个签名。
    public String HawkPost2(String url) throws MalformedURLException {
        String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
        String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
        String baseUrl = "";
        String str = "";
        URL realUrl = new URL(url);
        if (!Url.ISDEBUG) {
            str = url.split("com")[1];
        } else {
            if (url.indexOf("https") != -1)
                str = url.split(String.valueOf(Url.ACCUPASS_SERVICE_PORT2))[1];
            else
                str = url.split(String.valueOf(Url.ACCUPASS_SERVICE_PORT))[1];
        }
        boolean bl = str.endsWith("?");
        if (bl)
            baseUrl = realUrl.getPath();
        else {
            String plus = "";
            if (realUrl.getQuery() != null) {
                plus = "?" + realUrl.getQuery();//即?后面的string
            }
            baseUrl = realUrl.getPath() + plus;
        }
        AuthorizationHeader authorizationHeader = null;
        try {
            HawkContextBuilder_C b = HawkContext.offset(application.hawkOffset).
                    request("POST", baseUrl, Url.ACCUPASS_SERVICE_HOST, url.indexOf("https") != -1 ?
                            Url.ACCUPASS_SERVICE_PORT2 : Url.ACCUPASS_SERVICE_PORT).credentials(accu_id, accu_key, Algorithm.SHA_256);
            authorizationHeader = b.build().createAuthorizationHeader();
            return authorizationHeader.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    /***
     * postB请求
     *
     * @param url
     * @param params
     * @param webServiceCallBack
     */
    public void postB(final String url, final List<NameValuePair> params, final WebServiceCallBack webServiceCallBack) {


        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.RESULT_CODE_SUCCESS:
                        try {
//						Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_SUCCESS, (Object) msg.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            if(msg.obj.toString().equals("网络繁忙，请稍候再试")){
                                webServiceCallBack.callBack(Config.RESULT_CODE_ERROR, "网络繁忙，请稍候再试");
                            }else {
                                webServiceCallBack.callBack(Config.RESULT_CODE_ERROR, "网络连接断开，请检查网络");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_HAWK401:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            application.hawkOffset = Long.parseLong(msg.obj.toString());
                            postA(url, params, webServiceCallBack);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(buildUserAgent(context));
                try {
                    HttpPost request = new HttpPost(url);
                    request.addHeader("Authorization", HawkPost(url).toString());
                    request.addHeader("api-version", API_VERSION);
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    HttpResponse response = httpClient.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    String content = convertStreamToString(response.getEntity().getContent());
                    //401
                    if (statusCode == 401 && response.containsHeader("WWW-Authenticate")
                            && response.getFirstHeader("WWW-Authenticate").getValue().contains("tsm=")) {

                        Pattern pattern = Pattern.compile("ts=\"(.*)\", tsm=\"(.*)\"");
                        Matcher matcher = pattern.matcher(response.getFirstHeader("WWW-Authenticate").getValue());
                        if (matcher.find()) {
                            String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
                            String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
                            HawkWwwAuthenticateContext k = HawkWwwAuthenticateContext.tsAndTsm(Long.parseLong(matcher.group(1)), matcher.group(2)).credentials(accu_id, accu_key, Algorithm.SHA_256).build();
                            if (k.isValidTimestampMac(k.getTsm())) {
                                long offset = k.getTs() - (System.currentTimeMillis() / 1000L);
                                if (offset > 0) {
                                    if (application.count == 0) {
                                        application.count = 1;
                                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_HAWK401, offset));
                                    } else {
                                        application.count = 0;
                                        if ("".equals(content)) {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                                        } else {
                                            JSONObject res = JSON.parseObject(content);
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, res.getString("Message")));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (statusCode >= 300) {
                        if ("".equals(content)) {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, content));
                        }
                    } else {//statusCode==200
                        BaseResponse baseResponse = JSON.parseObject(content, BaseResponse.class);

                        if(baseResponse.code==1050){//1050要继续处理

                            RobWaiteInfo robWaiteInfo = JSON.parseObject(baseResponse.getResult(), RobWaiteInfo.class);
                            int robTime=0;//
                            boolean continueTry=true;
                            Trace.e("postB", "robWaiteInfo.url:" + robWaiteInfo.url + ",robWaiteInfo.sec:" + robWaiteInfo.sec+",robWaiteInfo.Times:"+robWaiteInfo.Times);
                            final List<NameValuePair> params2=new ArrayList<NameValuePair>();
                            while(continueTry){
                                robTime++;//
                                try{
                                    Thread.sleep(robTime*robWaiteInfo.sec*1000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                try {
                                    HttpPost request2 = new HttpPost(robWaiteInfo.url);
                                    request2.addHeader("Authorization", HawkPost2(robWaiteInfo.url).toString());
                                    request2.addHeader("api-version", API_VERSION);

                                    request2.setEntity(new UrlEncodedFormEntity(params2, HTTP.UTF_8));

                                    HttpResponse response2 = httpClient.execute(request2);
                                    int statusCode2 = response2.getStatusLine().getStatusCode();
                                    String content2 = convertStreamToString(response2.getEntity().getContent());
                                    if (statusCode2 == 401 && response2.containsHeader("WWW-Authenticate") &&
                                            response2.getFirstHeader("WWW-Authenticate").getValue().contains("tsm=")) {
                                        continueTry=false;//a

                                        Pattern pattern = Pattern.compile("ts=\"(.*)\", tsm=\"(.*)\"");
                                        Matcher matcher = pattern.matcher(response2.getFirstHeader("WWW-Authenticate").getValue());
                                        if (matcher.find()) {
                                            String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
                                            String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
                                            HawkWwwAuthenticateContext k = HawkWwwAuthenticateContext.tsAndTsm(Long.parseLong(matcher.group(1)), matcher.group(2)).credentials(accu_id, accu_key, Algorithm.SHA_256).build();
                                            if (k.isValidTimestampMac(k.getTsm())) {
                                                long offset = k.getTs() - (System.currentTimeMillis() / 1000L);
                                                if (offset > 0) {
                                                    if (application.count == 0) {
                                                        application.count = 1;
                                                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_HAWK401, offset));
                                                    } else {
                                                        application.count = 0;
                                                        if ("".equals(content2)) {
                                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response2.getStatusLine().getReasonPhrase()));
                                                        } else {
                                                            JSONObject res = JSON.parseObject(content2);
                                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, res.getString("Message")));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (statusCode2>= 300) {
                                        continueTry=false;//b
                                        if ("".equals(content2)) {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response2.getStatusLine().getReasonPhrase()));
                                        } else {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, content2));
                                        }
                                    } else {//statusCode2==200
                                        BaseResponse baseResponse2 = JSON.parseObject(content2, BaseResponse.class);

                                        if(baseResponse2.code==1051){
                                            if(robTime >= robWaiteInfo.Times){
                                                continueTry=false;//c
                                                mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, "网络繁忙，请稍候再试"));
                                            }
                                        }else {
                                            continueTry=false;//d
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_SUCCESS, content2));

                                        }
                                    }
                                } catch (Exception e) {
                                    continueTry=false;//e
                                    mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, e.getMessage()));
                                    e.printStackTrace();
                                } finally {
                                    httpClient.close();
                                }

                            }//while end


                        }//code==1050 end
                        else {//code 为非1050都不再继续处理，给上层
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_SUCCESS, content));
                        }
                    }
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, e.getMessage()));
                    e.printStackTrace();
                } finally {
                    httpClient.close();
                }
            }
        });


    }



    /***
     * post请求
     *
     * @param url
     * @param params
     * @param webServiceCallBack
     */
    public void postA(final String url, final List<NameValuePair> params, final WebServiceCallBack webServiceCallBack) {

        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.RESULT_CODE_SUCCESS:
                        try {
//						Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_SUCCESS, (Object) msg.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_ERROR, "网络连接断开，请检查网络");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_HAWK401:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            application.hawkOffset = Long.parseLong(msg.obj.toString());
                            postA(url, params, webServiceCallBack);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(buildUserAgent(context));
                try {
                    HttpPost request = new HttpPost(url);
                    request.addHeader("Authorization", HawkPost(url).toString());
                    request.addHeader("api-version", API_VERSION);
//					Log.d("result", printURL(url, params));
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    // getConsumer().sign(request);
                    HttpResponse response = httpClient.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    String content = convertStreamToString(response.getEntity().getContent());
                    if (statusCode == 401 && response.containsHeader("WWW-Authenticate") && response.getFirstHeader("WWW-Authenticate").getValue().contains("tsm=")) {
                        // 时间处理
                        // mHandler.sendMessage(mHandler.obtainMessage(0,
                        // reason));
                        Pattern pattern = Pattern.compile("ts=\"(.*)\", tsm=\"(.*)\"");
                        Matcher matcher = pattern.matcher(response.getFirstHeader("WWW-Authenticate").getValue());
                        if (matcher.find()) {
                            String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
                            String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
                            HawkWwwAuthenticateContext k = HawkWwwAuthenticateContext.tsAndTsm(Long.parseLong(matcher.group(1)), matcher.group(2)).credentials(accu_id, accu_key, Algorithm.SHA_256).build();
                            if (k.isValidTimestampMac(k.getTsm())) {
                                long offset = k.getTs() - (System.currentTimeMillis() / 1000L);
                                if (offset > 0) {
                                    if (application.count == 0) {
                                        application.count = 1;
                                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_HAWK401, offset));
                                    } else {
                                        application.count = 0;
                                        if ("".equals(content)) {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                                        } else {
                                            JSONObject res = JSON.parseObject(content);
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, res.getString("Message")));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (statusCode >= 300) {
                        if ("".equals(content)) {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, content));
                        }
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_SUCCESS, content));
                    }
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, e.getMessage()));
                    e.printStackTrace();
                } finally {
                    httpClient.close();
                }
            }
        });
    }


    public void setMutiEntity(MultipartEntity mutiEntity) {
        this.mutiEntity = mutiEntity;
    }

    private MultipartEntity mutiEntity;

    /***
     * post请求
     *
     * @param url
     * @param params
     * @param webServiceCallBack
     */
    public void postFile(final String url, final WebServiceCallBack webServiceCallBack) {

        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.RESULT_CODE_SUCCESS:
                        try {
//						Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_SUCCESS, (Object) msg.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_ERROR, "网络连接断开，请检查网络");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_HAWK401:
                       /* try {
                            Log.i("webservice", msg.obj.toString());
                            application.hawkOffset = Long.parseLong(msg.obj.toString());
                            postA(url, params, webServiceCallBack);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        break;
                    default:
                        break;
                }
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(buildUserAgent(context));
                try {
                    HttpPost request = new HttpPost(url);
                    request.addHeader("Authorization", HawkPost(url).toString());
                    request.addHeader("api-version", API_VERSION);
//					Log.d("result", printURL(url, params));
//                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    if(mutiEntity!=null){
                        request.setEntity(mutiEntity);
                    }


                    // getConsumer().sign(request);
                    HttpResponse response = httpClient.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    String content = convertStreamToString(response.getEntity().getContent());
                    if (statusCode == 401 && response.containsHeader("WWW-Authenticate") && response.getFirstHeader("WWW-Authenticate").getValue().contains("tsm=")) {
                        // 时间处理
                        // mHandler.sendMessage(mHandler.obtainMessage(0,
                        // reason));
                        Pattern pattern = Pattern.compile("ts=\"(.*)\", tsm=\"(.*)\"");
                        Matcher matcher = pattern.matcher(response.getFirstHeader("WWW-Authenticate").getValue());
                        if (matcher.find()) {
                            String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
                            String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
                            HawkWwwAuthenticateContext k = HawkWwwAuthenticateContext.tsAndTsm(Long.parseLong(matcher.group(1)), matcher.group(2)).credentials(accu_id, accu_key, Algorithm.SHA_256).build();
                            if (k.isValidTimestampMac(k.getTsm())) {
                                long offset = k.getTs() - (System.currentTimeMillis() / 1000L);
                                if (offset > 0) {
                                    if (application.count == 0) {
                                        application.count = 1;
                                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_HAWK401, offset));
                                    } else {
                                        application.count = 0;
                                        if ("".equals(content)) {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                                        } else {
                                            JSONObject res = JSON.parseObject(content);
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, res.getString("Message")));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (statusCode >= 300) {
                        if ("".equals(content)) {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, content));
                        }
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_SUCCESS, content));
                    }
                } catch (Exception e) {
                    mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, e.getMessage()));
                    e.printStackTrace();
                } finally {
                    httpClient.close();
                }
            }
        });
    }



    /**
     * get请求
     *
     * @param url
     * @param webServiceCallBack
     */
    public void get(final String url, final WebServiceCallBack webServiceCallBack) {

        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.RESULT_CODE_SUCCESS:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_SUCCESS, (Object) msg.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            webServiceCallBack.callBack(Config.RESULT_CODE_ERROR, "网络连接断开，请检查网络");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Config.RESULT_CODE_HAWK401:
                        try {
                            Log.i("webservice", msg.obj.toString());
                            application.hawkOffset = Long.parseLong(msg.obj.toString());
                            get(url, webServiceCallBack);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                String resUrl = null;
                if (url.indexOf("applogin") != -1) {
                    try {
                        URL urls = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
                        conn.getResponseCode();
                        resUrl = conn.getURL().toString();
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    resUrl = url;
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance(buildUserAgent(context));
                String content = null;
                try {
                    HttpGet request = new HttpGet(resUrl);
                    request.addHeader("Authorization", HawkGet(resUrl).toString());
                    request.addHeader("api-version", API_VERSION);
//                    Log.d("result", resUrl);
                    // getConsumer().sign(request);
                    HttpResponse response = httpClient.execute(request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    content = convertStreamToString(response.getEntity().getContent());
                    if (statusCode == 401 && response.containsHeader("WWW-Authenticate") && response.getFirstHeader("WWW-Authenticate").getValue().contains("tsm=")) {
                        // 时间处理
                        // mHandler.sendMessage(mHandler.obtainMessage(0,
                        // reason));
                        Pattern pattern = Pattern.compile("ts=\"(.*)\", tsm=\"(.*)\"");
                        Matcher matcher = pattern.matcher(response.getFirstHeader("WWW-Authenticate").getValue());
                        if (matcher.find()) {
                            String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
                            String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
                            HawkWwwAuthenticateContext k = HawkWwwAuthenticateContext.tsAndTsm(Long.parseLong(matcher.group(1)), matcher.group(2)).credentials(accu_id, accu_key, Algorithm.SHA_256).build();
                            if (k.isValidTimestampMac(k.getTsm())) {
                                long offset = k.getTs() - (System.currentTimeMillis() / 1000L);
                                if (offset > 0) {
                                    if (application.count == 0) {
                                        application.count = 1;
                                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_HAWK401, offset));
                                    } else {
                                        application.count = 0;
                                        if ("".equals(content)) {
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                                        } else {
                                            JSONObject res = JSON.parseObject(content);
                                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, res.getString("Message")));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (statusCode >= 300) {
                        if ("".equals(content)) {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, response.getStatusLine().getReasonPhrase()));
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, content));
                        }

                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_SUCCESS, content));
                    }
                } catch (Exception x) {
                    mHandler.sendMessage(mHandler.obtainMessage(Config.RESULT_CODE_ERROR, x.getMessage()));
                    x.printStackTrace();
                } finally {
                    httpClient.close();
                }
            }
        });
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (sb.length() > 0)
                    line = line + "\n";
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //机器的唯一标识码
    public static String buildUserAgent(Context context) {
        String buildUserAgent = "";
        try {
            final PackageManager manager = context.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            String appversion = "appversion=" + info.versionName + "(" + info.versionCode + ")(gzip)";
            String deviceid = " deviceid=" + ((AccuApplication) context.getApplicationContext()).getIMEI();
            String devicetype = " devicetype=" + android.os.Build.MODEL + "_" + android.os.Build.VERSION.RELEASE;
            // 正式版本
            buildUserAgent = "Accupass-Android" + "/" + appversion + deviceid + devicetype;
            // 测试版本180服务器
            String buildUserAgent_ACCUPASS_BETA = buildUserAgent + " " + Url.ACCUPASS_BETA;
            // Log.d("buildUserAgent", buildUserAgent);
            // return buildUserAgent_ACCUPASS_BETA;
            return buildUserAgent;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    //增加头部参数
    public String printURL(String url, List<NameValuePair> list) {
        if (list.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(url + "?");
            for (int i = 0; i < list.size(); i++) {
                NameValuePair pair = list.get(i);
                if (i != 0) {
                    buffer.append("&");
                }
                try {
                    buffer.append(pair.getName() + "=" + URLEncoder.encode(pair.getValue() == null ? "" : pair.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            return buffer.toString();
        }else{
            return url;
        }
    }
}
