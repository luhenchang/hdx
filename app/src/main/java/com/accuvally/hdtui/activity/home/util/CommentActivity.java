package com.accuvally.hdtui.activity.home.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.eventbus.EvaluateEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Andy Liu on 2016/9/12.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener {

    public static final String TIME="EvaluateActivity_TIME";
    public static final String LOCATION="EvaluateActivity_LOCATION";
    public static final String TITLE="EvaluateActivity_TITLE";
    public static final String LOGO="EvaluateActivity_LOGO";
    public static final String ID="EvaluateActivity_ID";
    public static final String LIKENUM="EvaluateActivity_LIKENUM";
    public static final String DISLIKENUM="EvaluateActivity_DISLIKENUM";


    public static final String FROM="CommentActivity_FROM";
    private boolean fromLinkedMe=false;


    public static final String TAG="CommentActivity";

    private ImageView evaluateLogo;//，
    private TextView evaluateTitle,evaluateLocation,evaluateTime;
    private EditText edCommContent;
    private ImageButton likeButton,dislikeButton;
    private TextView likeTextView,dislikeTextView;
    private int likeNum,dislikeNum;

    private CheckBox shareButton;
    private TextView submitTextView;

    private boolean share=false;//是否分享
    private int likeState=0;//0没选，1喜欢，2不喜欢

    private String id;//活动 id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initView();
        parseIntent();

    }

    private void parseIntent(){
        Intent intent=getIntent();
        if(intent!=null){

            if("LINKEDME".equals(intent.getStringExtra(FROM))){
                fromLinkedMe=true;
                String eid=getIntent().getStringExtra(ID);
                initDetails(eid);
            }else {
                fromLinkedMe=false;

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

                if(likeNum==0){
                    likeTextView.setVisibility(View.GONE);
                }else {
                    likeTextView.setText(likeNum + "");
//                    likeTextView.setVisibility(View.VISIBLE);
                    likeTextView.setVisibility(View.GONE);
                }

                if(dislikeNum==0){
                    dislikeTextView.setVisibility(View.GONE);
                }else {
                    dislikeTextView.setText(dislikeNum + "");
//                    dislikeTextView.setVisibility(View.VISIBLE);
                    dislikeTextView.setVisibility(View.GONE);
                }



//                initData();
            }
        }
    }

    // 获取详情
    public void initDetails(String homeId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", homeId));
        params.add(new BasicNameValuePair("type", 0 + ""));//isHuodong 0表示活动行的，1表示活动推
//        startProgress();
        showProgress("数据加载中，请稍等");
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_DETAIL, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
//                stopProgress();
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            AccuDetailBean detailsInfo = JSON.parseObject(response.result, AccuDetailBean.class);


                            id = detailsInfo.id;

                            String logo = detailsInfo.logo;
                            application.mImageLoader.displayImage(logo, evaluateLogo);

                            String title = detailsInfo.title;
                            String time = detailsInfo.getStartutc();
                            String location = detailsInfo.address;

                            evaluateTitle.setText(title);
                            evaluateTime.setText(time);
                            evaluateLocation.setText(location);



                            likeNum=detailsInfo.ZanUp;
                            dislikeNum=detailsInfo.ZanDown;

                            if(likeNum==0){
                                likeTextView.setVisibility(View.GONE);
                            }else {
                                likeTextView.setText(likeNum + "");
//                                likeTextView.setVisibility(View.VISIBLE);
                                likeTextView.setVisibility(View.GONE);
                            }

                            if(dislikeNum==0){
                                dislikeTextView.setVisibility(View.GONE);
                            }else {
                                dislikeTextView.setText(dislikeNum+"");
//                                dislikeTextView.setVisibility(View.VISIBLE);
                                dislikeTextView.setVisibility(View.GONE);
                            }

                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
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

        likeTextView= (TextView) findViewById(R.id.tv_evaluate_likeNum);
        dislikeTextView= (TextView) findViewById(R.id.tv_evaluate_dislikeNum);

    }

    private void initData(){

    }


    	public void uploadComment() {
		String content = edCommContent.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			application.showMsg("请输入评论内容");
			return;
		}

        if(content.length()>300){
            application.showMsg("评价字数超过300，请重新输入");
            return;
        }
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("type", "0"));
		params.add(new BasicNameValuePair("comment", content));
//		params.add(new BasicNameValuePair("replyid", "0"));
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
//						dbManager.insertSaveBeHavior(application.addBeHavior(70, 0+"", id, "","", "",""));
                            application.showMsg(info.getMsg());
                            edCommContent.setText("");
                            submitTextView.setClickable(false);
                            submitTextView.setText("已评价");
                            findViewById(R.id.evaluate_submit_ly).setBackgroundResource(R.color.gary);

                            if(likeState==1){
                                EventBus.getDefault().post(new EvaluateEventBus(1));
                            }else {
                                EventBus.getDefault().post(new EvaluateEventBus(0));
                            }

                            //						pageIndex = 1;
//						initComment(pageIndex);
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
                uploadComment();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(fromLinkedMe&&id!=null){
            mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                    putExtra("id", id).putExtra("isHuodong", 0));
        }
    }

    @Override
    public void onBack(View view) {
        finish();
        if(fromLinkedMe&&id!=null){
            mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                    putExtra("id", id).putExtra("isHuodong", 0));
        }
    }


}
