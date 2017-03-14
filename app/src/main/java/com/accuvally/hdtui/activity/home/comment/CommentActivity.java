package com.accuvally.hdtui.activity.home.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.ui.imageloader.GlideImageLoader;
import com.accuvally.hdtui.ui.imageloader.ImagePickerAdapter;
import com.accuvally.hdtui.ui.imageloader.WrappableGridLayoutManager;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.ImageBig;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.hawkj2.Algorithm;
import com.accuvally.hdtui.utils.hawkj2.AuthorizationHeader;
import com.accuvally.hdtui.utils.hawkj2.HawkContext;
import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Andy Liu on 2016/9/12.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener , ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final String TIME="EvaluateActivity_TIME";
    public static final String LOCATION="EvaluateActivity_LOCATION";
    public static final String TITLE="EvaluateActivity_TITLE";
    public static final String LOGO="EvaluateActivity_LOGO";
    public static final String ID="EvaluateActivity_ID";
    public static final String LIKENUM="EvaluateActivity_LIKENUM";
    public static final String DISLIKENUM="EvaluateActivity_DISLIKENUM";

    public static final String LIKE_STATE="like_state";



//    public static final String FROM="CommentActivity_FROM";
//    private boolean fromGetui =false;


    public static final String TAG="CommentActivity";

    private ImageView evaluateLogo;//，
    private TextView evaluateTitle,evaluateLocation,evaluateTime;
    private EditText edCommContent;
    private ImageButton likeButton,dislikeButton;
    //    private TextView likeTextView,dislikeTextView;
    private int likeNum,dislikeNum;

    private CheckBox shareButton;
    private TextView submitTextView;

    private boolean share=false;//是否分享
    private int likeState=0;//0没选，1喜欢，2不喜欢

    private String id;//活动 id

    private boolean uploading=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initView();
        parseIntent();
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();

    }

    private void parseIntent(){
        Intent intent=getIntent();
        if(intent!=null){
            id = getIntent().getStringExtra(ID);

            String logo=intent.getStringExtra(LOGO);
            application.mImageLoader.displayImage(logo, evaluateLogo);

            String title=intent.getStringExtra(TITLE);
            String time=intent.getStringExtra(TIME);
            String location=intent.getStringExtra(LOCATION);


            evaluateTitle.setText(title);
            evaluateTime.setText(time);
            evaluateLocation.setText(location);

            likeNum=intent.getIntExtra(LIKENUM, 0);
            dislikeNum=intent.getIntExtra(LIKENUM,0);

        }
    }


    private void initView(){
        setTitle("发表评价");

        evaluateLogo= (ImageView) findViewById(R.id.evaluate_ivItemRecommendImg);
        evaluateTitle= (TextView) findViewById(R.id.evaluate_tvItemTitle);
        evaluateLocation= (TextView) findViewById(R.id.evaluate_tvItemAddress);
        evaluateTime= (TextView) findViewById(R.id.evaluate_tvItemTime);

        edCommContent = (EditText) findViewById(R.id.evaluate_Content);

        likeButton= (ImageButton) findViewById(R.id.evaluate_like);
        likeButton.setOnClickListener(this);
        dislikeButton= (ImageButton) findViewById(R.id.evaluate_dislike);
        dislikeButton.setOnClickListener(this);
        shareButton= (CheckBox) findViewById(R.id.evaluate_share);
        shareButton.setOnClickListener(this);
        submitTextView= (TextView) findViewById(R.id.evaluate_submit);
        submitTextView.setOnClickListener(this);

    }





    public void uploadComment() {
        String content = edCommContent.getText().toString().trim();
        if (TextUtils.isEmpty(content) &&(selImageList.size() == 0) &&(likeState==0)) {
            application.showMsg("请输入评论内容");
            uploading=false;
            return;
        }

        if(content.trim()=="" &&(selImageList.size() == 0) &&(likeState==0) ){//" " 这种也要限制
            application.showMsg("请输入评论内容");
            uploading=false;
            return;
        }

        if(content.length()>300){
            application.showMsg("评价字数超过300，请重新输入");
            uploading=false;
            return;
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("type", "0"));
        params.add(new BasicNameValuePair("comment", content));
        params.add(new BasicNameValuePair("eventtype", "0"));
        params.add(new BasicNameValuePair("ZanStatus", likeState + ""));
        params.add(new BasicNameValuePair("ReplyType", "1"));//回复类型（1=活动评价，2=活动咨询）

//            Trace.e(TAG, "id:" + id);
//            Trace.e(TAG, "content:" + content);
//            Trace.e(TAG, "likeState:" + likeState);
        showProgress("正在提交评论");
        httpCilents.postA(Url.ACCUPASS_UPALOD_COMMENT, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(CommentActivity.this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (info.isSuccess()) {
                            if (selImageList.size() != 0) {
                                uploadImage(Url.ACCUPASS_UPALOD_IMG_COMMENT, info.getResult());
                            }

                            application.showMsg("评价成功");
                            Intent intent = new Intent();
                            intent.putExtra(LIKE_STATE, likeState);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            application.showMsg(info.getMsg());
                        }
                        Trace.e(TAG, info.toString());
                        uploading = false;
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("网络连接断开，请检查网络");
                        Trace.e(TAG, result.toString());
                        uploading = false;
                        break;
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evaluate_like://第二次按喜欢时表示取消选择
                if(likeState==1){
                    likeState=0;
                    likeButton.setBackgroundResource(R.drawable.like_nor_2x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_2x);

                }else {
                    likeState=1;
                    likeButton.setBackgroundResource(R.drawable.like_sel_2x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_2x);
                }

                break;
            case R.id.evaluate_dislike:
                if(likeState==2){//第二次按喜欢时表示取消选择
                    likeState=0;
                    likeButton.setBackgroundResource(R.drawable.like_nor_2x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_2x);
                }else {
                    likeState=2;
                    likeButton.setBackgroundResource(R.drawable.like_nor_2x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_sel_2x);
                }
                break;
            case R.id.evaluate_share:
                if(shareButton.isChecked()){
                    shareButton.setBackgroundResource(R.drawable.share_selected_3x);
                    share=true;
                }else {
                    shareButton.setBackgroundResource(R.drawable.share_nor_3x);
                    share=false;
                }
                break;
            case R.id.evaluate_submit:
                if(!uploading){
                    uploading=true;
                    uploadComment();
                }
                break;

        }
    }

    //===================================================================================
    public String  HawkPost(String url) throws MalformedURLException {
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

    void uploadImage(final String url,final String cid){
        new Thread(new Runnable() {
            @Override
            public void run() {

                int time=0;
                final int MAXTIME=5*selImageList.size();

                while ((selImageList.size()!=0) &&(time<MAXTIME)){
                    time++;
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(CommentActivity.this));
                    try {
                        ImageItem imageItem=selImageList.get(0);

                        String OUTPUTFILEPATH=imageItem.path;

                        String filePath = ImageBig.scalePicture(mContext, OUTPUTFILEPATH, 800, 800);
//                        Trace.e(TAG,"原图片路径："+OUTPUTFILEPATH);
//                        Trace.e(TAG,"压缩的图片路径："+filePath);

                        HttpPost request = new HttpPost(url);
                        request.addHeader("Authorization", HawkPost(url).toString());
                        request.addHeader("api-version", "v3.5.0");

                        MultipartEntity mutiEntity = new MultipartEntity();
                        File file = new File(filePath);
                        mutiEntity.addPart("cid", new StringBody(cid, Charset.forName("utf-8")));//评论id
                        mutiEntity.addPart("eid", new StringBody(id, Charset.forName("utf-8")));//活动id
                        mutiEntity.addPart("file", new FileBody(file));
                        request.setEntity(mutiEntity);

                        // getConsumer().sign(request);
                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        String content = HttpCilents.convertStreamToString(response.getEntity().getContent());
                        Trace.e(TAG,"statusCode:"+statusCode+"  content:"+content);
                        if(statusCode==200){
                            selImageList.remove(0);//图片提交成功，
                        }

                        if(file.exists()){
                            file.delete();//删除压缩的图片
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    } finally {
                        httpClient.close();
                    }
                }
                Trace.e(TAG,"图像上传线程退出");
            }
        }).start();
    }

    //===========================================================================================
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 6;               //允许选择图片最大数


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(ImageLoader.class));   //设置图片加载器
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器

        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new WrappableGridLayoutManager(this, 4));


//        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);

                for (int i=0;i<images.size();i++){
                    Trace.e(TAG,images.get(i).path+"  size"+images.get(i).size);
                }

            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);

                for (int i=0;i<images.size();i++){
                    Trace.e(TAG,images.get(i).path+"  size"+images.get(i).size);
                }

            }
        }
    }


}
