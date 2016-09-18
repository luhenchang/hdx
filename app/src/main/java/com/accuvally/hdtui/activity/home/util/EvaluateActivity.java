package com.accuvally.hdtui.activity.home.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;

/**
 * Created by Andy Liu on 2016/9/12.
 */
public class EvaluateActivity extends BaseActivity implements View.OnClickListener {

    public static final String TIME="EvaluateActivity_TIME";
    public static final String LOCATION="EvaluateActivity_LOCATION";
    public static final String TITLE="EvaluateActivity_TITLE";
    public static final String LOGO="EvaluateActivity_LOGO";

    private ImageView evaluateLogo;//，
    private TextView evaluateTitle,evaluateLocation,evaluateTime;
    private EditText contentEditText;
    private ImageButton likeButton,dislikeButton;
    private CheckBox shareButton;
    private TextView submitTextView;

    private boolean share=false;//是否分享
    private int likeState=0;//0没选，1喜欢，2不喜欢


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initView();
        parseIntent();
        initData();
    }

    private void parseIntent(){
        Intent intent=getIntent();
        if(intent!=null){
            String logo=intent.getStringExtra(LOGO);
            application.mImageLoader.displayImage(logo, evaluateLogo);

            String title=intent.getStringExtra(TITLE);
            String time=intent.getStringExtra(TIME);
            String location=intent.getStringExtra(LOCATION);


            evaluateTitle.setText(title);
            evaluateTime.setText(time);
            evaluateLocation.setText(location);

        }
    }

    private void submit(){

        String content = contentEditText.getText().toString().trim();
        if ("".equals(content)) {
            application.showMsg("您还未输入评价");
            return;
        }
        if(content.length()>300){
            application.showMsg("评价字数超过300，请重新输入");
            return;
        }

    }

    private void initView(){
        setTitle("发表评价");

        evaluateLogo= (ImageView) findViewById(R.id.evaluate_ivItemRecommendImg);
        evaluateTitle= (TextView) findViewById(R.id.evaluate_tvItemTitle);
        evaluateLocation= (TextView) findViewById(R.id.evaluate_tvItemAddress);
        evaluateTime= (TextView) findViewById(R.id.evaluate_tvItemTime);

        contentEditText= (EditText) findViewById(R.id.evaluate_Content);

        likeButton= (ImageButton) findViewById(R.id.evaluate_like);
        likeButton.setOnClickListener(this);
        dislikeButton= (ImageButton) findViewById(R.id.evaluate_dislike);
        dislikeButton.setOnClickListener(this);
        shareButton= (CheckBox) findViewById(R.id.evaluate_share);
        shareButton.setOnClickListener(this);
        submitTextView= (TextView) findViewById(R.id.evaluate_submit);
        submitTextView.setOnClickListener(this);

    }

    private void initData(){

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evaluate_like://第二次按喜欢时表示取消选择
                if(likeState==1){
                    likeState=0;
                    likeButton.setBackgroundResource(R.drawable.like_nor_3x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_3x);
                }else {
                    likeState=1;
                    likeButton.setBackgroundResource(R.drawable.like_sel_3x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_3x);
                }

                break;
            case R.id.evaluate_dislike:
                if(likeState==2){//第二次按喜欢时表示取消选择
                    likeState=0;
                    likeButton.setBackgroundResource(R.drawable.like_nor_3x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_nor_3x);
                }else {
                    likeState=2;
                    likeButton.setBackgroundResource(R.drawable.like_nor_3x);
                    dislikeButton.setBackgroundResource(R.drawable.dislike_sel_3x);
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
                submit();
                break;

        }
    }
}
