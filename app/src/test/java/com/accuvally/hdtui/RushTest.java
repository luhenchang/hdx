package com.accuvally.hdtui;

/**
 * Created by Andy Liu on 2017/5/20.
 */
public class RushTest {

    public static final String TAG = "RushTest";

   /* //获取验证码的地址
    @Test
    public void getScodeAddress(){



            String homeId="3377649980900";
            boolean hasGetTheCodeAddress=false;

                while (!hasGetTheCodeAddress){


                    try {
                        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
                        HttpGet request = new HttpGet("http://rush.huodongxing.com/address/"+homeId+".js"+"?time="+System.currentTimeMillis());//暂时写死
                        HttpResponse response = httpClient.execute(request);
                        String result= EntityUtils.toString(response.getEntity());
                        int statusCode = response.getStatusLine().getStatusCode();
                        Trace.e(TAG, "getScodeAddress   result:" + result + ",statusCode:" + statusCode );
                        if(statusCode==200){

                        }//end 200
                        else{//除200外的其他情况
                        }//end 除200外的其他情况

                    } catch (Exception e) {
                        Trace.e(TAG,e.getMessage());
                        e.printStackTrace();
                    }

                }
            }*/

}
