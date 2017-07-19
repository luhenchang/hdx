package com.accuvally.hdtui.utils.chunk;

import android.util.Log;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.hawkj2.Algorithm;
import com.accuvally.hdtui.utils.hawkj2.AuthorizationHeader;
import com.accuvally.hdtui.utils.hawkj2.HawkContext;
import com.alibaba.fastjson.JSON;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Andy Liu on 2017/7/10.
 */
public class UploadChunkUtil {

    public void chunkAndUpload(String path){


        File file=new File(path);
        FileInfo fileInfo=new FileInfo();
        fileInfo.setFilePath(path);
        fileInfo.setFileLength(file.length());
        fileInfo.setFileName(file.getName());
        long fileLength=fileInfo.getFileLength();
        int chunks=(int)(fileLength/ChunkInfo.chunkLength+(fileLength%ChunkInfo.chunkLength>0?1:0));

        ArrayList<ChunkInfo> chunkInfos=new ArrayList<>();

        for (int i=0;i<chunks;i++) {//当前分片值从1开始
            ChunkInfo chunkInfo = new ChunkInfo();
//            chunkInfo.setGguid(fileInfo.getGguid());
//            chunkInfo.setMd5(fileInfo.getMd5());
            chunkInfo.setChunkName(fileInfo.getFileName()+".part_"+(i+1)+"."+chunks);//filename.jpg.part_2.10
            chunkInfo.setChunk(i);
            chunkInfo.setChunks(chunks);
            chunkInfo.setFileLength(fileLength);
            chunkInfo.setFilePath(fileInfo.getFilePath());
            chunkInfos.add(chunkInfo);
            Trace.e("要提交的 chunks", chunkInfo.toString());
        }

        uploadChunks(Url.UPLOAD_CHUNK, chunkInfos);

    }

    //===================================================================================
    public String  HawkPost(String url,AccuApplication application) throws MalformedURLException {
        String accu_id = application.sharedUtils.readString(Config.KEY_ACCUPASS_USER_NAME);
        String accu_key = application.sharedUtils.readString(Config.KEY_ACCUPASS_ACCESS_TOKEN);
        Log.i("info", accu_id);
        Log.i("info", accu_key);
        AuthorizationHeader authorizationHeader = null;
        URL realUrl = new URL(url);
        try {
            HawkContext.HawkContextBuilder_C b = HawkContext.offset(application.hawkOffset).request("POST", realUrl.getPath(), Url.ACCUPASS_SERVICE_HOST, url.indexOf("https") != -1 ? Url.ACCUPASS_SERVICE_PORT2 : Url.ACCUPASS_SERVICE_PORT).credentials(accu_id, accu_key, Algorithm.SHA_256);
            authorizationHeader = b.build().createAuthorizationHeader();
            return authorizationHeader.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    void uploadChunks(final String url,final ArrayList<ChunkInfo> chunkInfos){
        final AccuApplication accuApplication=AccuApplication.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {

                int time=0;
                final int MAXTIME=3*chunkInfos.size();

                while ((chunkInfos.size()!=0) &&(time<MAXTIME)){
                    time++;
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(accuApplication));
                    ChunkInfo chunkInfo=null;
                    try {
//                        ImageItem imageItem=chunkInfos.get(0);
                        chunkInfo=chunkInfos.get(0);

//                        String filePath = ImageBig.scalePicture(mContext, OUTPUTFILEPATH, 800, 800);
//                        Trace.e(TAG,"原图片路径："+OUTPUTFILEPATH);
//                        Trace.e(TAG,"压缩的图片路径："+filePath);

                        HttpPost request = new HttpPost(url);
                        request.addHeader("Authorization", HawkPost(url,accuApplication).toString());
                        request.addHeader("api-version", "v3.5.0");

//                        request.addHeader("Content-Type", "application/octet-stream");
//                        request.addHeader("Content-Type", "multipart/form-data");
//                        request.addHeader("Content-Type", "image/jpeg");


                        MultipartEntity mutiEntity = new MultipartEntity();
                        mutiEntity.addPart("f", new StringBody("form_attach", Charset.forName("utf-8")));
                        mutiEntity.addPart("n", new StringBody(chunkInfo.getChunkName(), Charset.forName("utf-8")));
                        mutiEntity.addPart("file", new CustomFileBody(chunkInfo));
                        mutiEntity.addPart("fsize", new StringBody(chunkInfo.getFileLength()+"", Charset.forName("utf-8")));

//                        File file = new File(chunkInfo.getFilePath());
//                        mutiEntity.addPart("file", new FileBody(file));


                        request.setEntity(mutiEntity);

                        // getConsumer().sign(request);
                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        String content = HttpCilents.convertStreamToString(response.getEntity().getContent());
                        Trace.e("TAG","statusCode:"+statusCode+"  content:"+content);
                        if(statusCode==200){

                            try {
                                BaseResponse info = JSON.parseObject(content, BaseResponse.class);
                                if (info.isSuccess()) {
                                    chunkInfos.remove(0);//图片提交成功，
                                    Trace.e("提交成功的 chunks", chunkInfo.toString());
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else {
                            Trace.e("提交失败的 chunks", chunkInfo.toString());
                        }

                    } catch (Exception e) {
                        Trace.e("提交失败的 e  chunks", chunkInfo.toString()+"    "+e.getMessage());
                        e.printStackTrace();
                    } finally {
                        httpClient.close();
                    }

                }
                Trace.e("TAG","chunks上传线程退出");
            }
        }).start();
    }


}
