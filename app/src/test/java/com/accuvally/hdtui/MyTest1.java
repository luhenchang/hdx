package com.accuvally.hdtui;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.InboxStatusFindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
//import org.apache.http.client.methods.HttpGet;
/**
 * Created by Andy Liu on 2017/4/13.
 */
public class MyTest1 {

    @Test
    public void test() throws Exception{

try {
    Thread.sleep(1000);
}catch (Exception e){

}

            String url1 = "http://www.xx.com";
            String url2 = "w.xx.com";
            String url3 = "http://w.xx.com";
            String url4 = "ssss";
        String url5 = "http://www.huodongxing.com/event/3386200498200?utm_source=主页&utm_medium=banner&utm_campaign=homepage";


//        URLEncoder urlEncoder
//        Pattern pattern = Pattern
//                .compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
            Pattern pattern = Pattern
                    .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
            System.out.println(pattern.matcher(url1).matches());
            System.out.println(pattern.matcher(url2).matches());
            System.out.println(pattern.matcher(url3).matches());
            System.out.println(pattern.matcher(url4).matches());
        System.out.println(pattern.matcher(url5).matches());

        /*try {
            KeyStore keyStore=KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            ArrayList<String> keyAliases = new ArrayList<>();
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        }
        catch(Exception e) {

        }*/



//        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
//                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
//        kpg.initialize(new KeyGenParameterSpec.Builder(
//                alias,
//                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
//                .setDigests(KeyProperties.DIGEST_SHA256,
//                        KeyProperties.DIGEST_SHA512)
//                .build());
//
//        KeyPair kp = kpg.generateKeyPair();
    }



    public static final String app_id = "jb6qxhlt8tqbdj1gg31tf9jc79q6qzduozsp0gkp0q0y1i82";
    public static final String app_key = "9mile0j83ypt6n889mkpze83qq3v4rizntrs235wuqpoynz4";

   /* @Test
    public void query_private_test() throws Exception{//查询某个人的最新私信列表
        System.out.println("test");
        HttpClient client=new HttpClient();
        String url = "https://leancloud.cn/1.1/subscribe/statuses";
        JSONObject json = new JSONObject();
        json.put("__type", "Pointer");
        json.put("className", "_User");
        json.put("objectId", "6801567573489");
        GetMethod get = new GetMethod(url+"?owner="+ URLEncoder.encode(json.toString(), "utf-8")+"&inboxType=new_friend");
        get.addRequestHeader("X-AVOSCloud-Application-Id", app_id);
        get.addRequestHeader("X-AVOSCloud-Application-Key", app_key);
        get.addRequestHeader("Content-Type", "application/json");
        client.executeMethod(get);
        String result = get.getResponseBodyAsString();
        System.out.println(result);
    }*/

   static final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    static final SimpleDateFormat displayFormat1 = new SimpleDateFormat("MM'月'dd'日' HH:mm");
    static final SimpleDateFormat displayFormat2 = new SimpleDateFormat("HH:mm");


    @Test
    public void getMailTime() {
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String ret="";
        String createdAt="2017-04-13T08:42:54.724Z";

        try {
            Calendar now = Calendar.getInstance();

            Date receiveDate=iso8601Format.parse(createdAt);
            Calendar receive = Calendar.getInstance();
            receive.setTime(receiveDate);

            if (now.get(Calendar.MONTH) == receive.get(Calendar.MONTH) &&
                    now.get(Calendar.DAY_OF_MONTH) == receive.get(Calendar.DAY_OF_MONTH)) {

                ret=displayFormat2.format(receiveDate);
            } else {
                ret=displayFormat1.format(receiveDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.print(ret);
        return ;

    }



    public void dateTest(){
        final  SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        final  SimpleDateFormat formatormat = new SimpleDateFormat("yyyy/MM/dd");
        System.out.println("test1");
        String d="2017-04-13T08:42:54.724Z";
        try {
            Date date=iso8601Format.parse(d);
            String s=formatormat.format(date);
            System.out.print(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void myTest1(){


        /*try{

        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(mContext));
        HttpGet request = new HttpGet("http://rush.huodongxing.com/service/e2s?eid="+homeId);//暂时写死
        HttpResponse response = httpClient.execute(request);
        String result= EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        Trace.e("TAG", "setCountDownTimer    result:" + result + ",statusCode:" + statusCode);
    }catch (Exception e) {
        Trace.e("TAG",e.getMessage());
        e.printStackTrace();
    }*/

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                loginLeancloud();
            }
        }).start();


        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }


    private void  getMessages(){
        //        AVStatusQuery<AVStatus> query = AVStatus.statusQuery(AVStatus.class, AVUser.getCurrentUser());
//        query.setLimit(50);    //设置最多返回 50 条状态
//        query.setSinceId(0);   //查询返回的 status 的 messageId 必须大于 sinceId，默认为 0
////query.setInboxType(AVStatus.INBOX_TYPE.TIMELINE.toString()); 此处可以通过这个方法来添加查询的状态条件，当然这里你也可以用你自己定义的状态类型，因为这里接受的其实是一个字符串类型。
//        query.findInBackground(new FindCallback<AVStatus>(){
//            @Override
//            public void done(final List<AVStatus> avObjects,final AVException avException) {
//
//            }
//        });
    }






    private void loginLeancloud(){
        AVUser.logInInBackground("haha", "123", new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
//                    Toast.makeText(MessageActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    handler.sendEmptyMessage(2);
                } else {
//                    Toast.makeText(MessageActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void sendMessage(){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("text", "we have new website, take a look!");
        data.put("link", "http://leancloud.cn");
        AVStatus status = AVStatus.createStatusWithData(data);
        status.setInboxType(AVStatus.INBOX_TYPE.TIMELINE.toString());

        status.sendInBackgroundWithBlock(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Log.i("sendmessage", "Send finished");

            }
        });
    }

    private void getMessage(){

        AVStatusQuery inboxQuery = AVStatus.inboxQuery(AVUser.getCurrentUser(), AVStatus.INBOX_TYPE.PRIVATE.toString());
        inboxQuery.setLimit(100);  //设置最多返回 50 条状态
        inboxQuery.setSinceId(0);  //查询返回的 status 的 messageId 必须大于 sinceId，默认为 0
        inboxQuery.findInBackground(new InboxStatusFindCallback() {
            @Override
            public void done(final List<AVStatus> avObjects, final AVException avException) {
                List<AVStatus> avObjects1 = avObjects;
            }
        });
    }


    private void register(){
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername("haha");// 设置用户名
        user.setPassword("123");// 设置密码
        user.setEmail("liuchengcheng@huodongxing.com");//设置邮箱
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
//                    startActivity(new Intent(MessageActivity.this, MainActivity.class));
//                    RegisterActivity.this.finish();
//                    Toast.makeText(MessageActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
//                    showProgress(false);
//                    Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //获取未读消息数
    private void getCount(){

        AVStatus.getUnreadStatusesCountInBackground(AVStatus.INBOX_TYPE.PRIVATE.toString(), new CountCallback() {
            public void done(int count, AVException e) {
                if (e == null) {
                    //count就是未读status数目
//                    Message message = new Message();
//                    message.what = display_toast;
//                    message.arg1 = count;
//                    handler.sendMessage(message);
                } else {
                    //有错误发生。
                }
            }
        });

    }
}
