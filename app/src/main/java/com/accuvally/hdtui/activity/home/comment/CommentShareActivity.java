package com.accuvally.hdtui.activity.home.comment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.utils.SharePictureUtils;
import com.accuvally.hdtui.utils.Trace;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.Timer;
import java.util.TimerTask;


//http://dev.umeng.com/social/android/share-detail#4
//http://blog.sina.com.cn/s/blog_4bc60c050101htap.html
/**
 * Created by Andy Liu on 2017/7/5.
 */
public class CommentShareActivity extends BaseActivity implements View.OnClickListener{

    private CircleImageView comment_share_logo;
    private TextView comment_share_name,comment_share_sponsor,comment_share_title,comment_share_content;
    private ImageView evaluateLogo;//，
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_share);
        initView();
        parseIntent();
    }


    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    private void initView(){

        comment_share_logo = (CircleImageView) findViewById(R.id.comment_share_logo);
        comment_share_name = (TextView) findViewById(R.id.comment_share_name);
        evaluateLogo= (ImageView) findViewById(R.id.comment_activity_share_logo);

        comment_share_title= (TextView) findViewById(R.id.comment_share_title);
        comment_share_content= (TextView) findViewById(R.id.comment_share_content);
        comment_share_sponsor= (TextView) findViewById(R.id.comment_share_sponsor);

        ratingBar = (RatingBar) findViewById(R.id.comment_share_rank);


        TextView shareWx = (TextView) findViewById(R.id.tvSharewx);
        TextView shareWxpy = (TextView) findViewById(R.id.tvSharewxpy);
        shareWx.setOnClickListener(this);
        shareWxpy.setOnClickListener(this);

        findViewById(R.id.comment_share_back).setOnClickListener(this);

    }


    private UMSocialService mController;
    SharePictureUtils shareUtils;
    Timer timer;
    private void setData(){

        application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), comment_share_logo,
                UILoptions.defaultUser);
        comment_share_name.setText(application.getUserInfo().getNick());

        mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
        shareUtils = new SharePictureUtils(mContext, mController);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout comment_share_picture= (LinearLayout) findViewById(R.id.comment_share_picture);
                        Bitmap bitmap=createViewBitmap(comment_share_picture);
                        if(bitmap==null){
                            Trace.e("", "bitmap is null");
                        }
                        shareUtils.initConfig(CommentShareActivity.this, bitmap);
                    }
                });
            }
        },500);


    }

    private Bitmap createViewBitmap(View v) {
        int width=v.getWidth();
        int height=v.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }




    private void parseIntent(){
        Intent intent=getIntent();
        if(intent!=null){

            String logo=intent.getStringExtra(CommentActivity.LOGO);
            application.mImageLoader.displayImage(logo, evaluateLogo);

            String title=intent.getStringExtra(CommentActivity.TITLE);
            comment_share_title.setText(title);

            comment_share_sponsor.setText(intent.getStringExtra(CommentActivity.SPONSOR));
            ratingBar.setProgress(intent.getIntExtra(CommentActivity.START_RANK, 5));

            comment_share_content.setText(intent.getStringExtra(CommentActivity.CONTENT));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSharewx:// weixin share
                shareUtils.share(SHARE_MEDIA.WEIXIN);
//                shareDialog.dismiss();
                break;
            case R.id.tvSharewxpy:// py share
                shareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE);
//                shareDialog.dismiss();
                break;
            case R.id.comment_share_back://
                finish();
                break;

        }
    }
}
