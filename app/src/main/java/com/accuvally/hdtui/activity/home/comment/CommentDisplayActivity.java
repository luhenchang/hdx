package com.accuvally.hdtui.activity.home.comment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommentAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.CommentInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.utils.HttpCilents;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentDisplayActivity extends BaseActivity implements View.OnClickListener{

    public static final String TYPE="TYPE";//type 查询主办人的 Type=11,活动 type=0，主办方 type=1
    public static final String TARGET="TARGET";
    public static final String REPLAY_TYPE="REPLAY_TYPE";//（1：活动评价，2：活动咨询）

    public static final String EVENT_NAME="EVENT_NAME";
    private String eventName;

    private int type;
    private String target;
    private int replyType;


    private ImageView edit_consulation;
    private CommentAdapter mAdapter;

    private XListView mListView;

    private ArrayList<CommentInfo> list;
    private boolean commentLoading =false ;//加载标志


    private int pageIndex = 1, pageSize = 20;
    RelativeLayout notCommentFoundHint;
    RelativeLayout notConsulationFoundHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_display);

        type=getIntent().getIntExtra(TYPE, 1);
        target=getIntent().getStringExtra(TARGET);
        replyType=getIntent().getIntExtra(REPLAY_TYPE, 1);
        eventName=getIntent().getStringExtra(EVENT_NAME);

        initView();

        setAdapter();
    }

    private void  initView(){

        initProgress();
        edit_consulation= (ImageView) findViewById(R.id.edit_consulation);


        if(replyType==2){
            setTitle("活动咨询");
            edit_consulation.setVisibility(View.VISIBLE);
            edit_consulation.setOnClickListener(this);
        }else {
            setTitle("历史评价");
        }
        mListView = (XListView) findViewById(R.id.commentlistview);
        mListView.setFocusable(false);
        notCommentFoundHint= (RelativeLayout) findViewById(R.id.comment_notfound);
        notConsulationFoundHint= (RelativeLayout) findViewById(R.id.consulation_notfound);

        findViewById(R.id.consulation_now).setOnClickListener(this);
    }


    private void setAdapter(){

        list = new ArrayList<CommentInfo>();
        mAdapter = new CommentAdapter(CommentDisplayActivity.this,list);
        mAdapter.reply_type=replyType+"";// 1:活动评价; 2:活动咨询
        mListView.setAdapter(mAdapter);
        mListView.setAutoLoadMore(true);

        mListView.setPullLoadEnable(true);

        mListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                if (!commentLoading) {
                    commentLoading = true;
                    pageIndex = 1;
                    requestData();
                }
            }

            @Override
            public void onLoadMore() {
//                pageIndex++;
                if (!commentLoading) {
                    commentLoading = true;
                    requestData();
                }
            }
        });

        if (!commentLoading) {
            commentLoading=true;
            requestData();
        }

    }



    private void requestData(){
        startProgress();
        List<NameValuePair> params = new ArrayList<NameValuePair>();


        params.add(new BasicNameValuePair("type", type+""));
        params.add(new BasicNameValuePair("target", target));////8941992442294 用户id，暂时写死
//            params.add(new BasicNameValuePair("target", "4711656448486"));
        params.add(new BasicNameValuePair("replyType", replyType+""));// // 1:活动评价; 2:活动咨询
        params.add(new BasicNameValuePair("pi", pageIndex+""));
        params.add(new BasicNameValuePair("ps", pageSize+""));

        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_COMMENT, params), new HttpCilents.WebServiceCallBack() {
            @Override
            public void callBack(int code, Object result) {
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (info.isSuccess()) {
                            try {
                                ArrayList<CommentInfo> arrayList=parserCommentInfos(info.getResult());
                                showCommentInfos(arrayList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            application.showMsg(info.getMsg());
                        }
                        stopProgress();
                        commentLoading=false;
                        break;

                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("网络连接断开，请检查网络");
                        stopProgress();
                        commentLoading=false;
                        break;
                }
            }
        });
    }


    private  ArrayList<CommentInfo>  parserCommentInfos(String content) throws Exception {


        ArrayList<CommentInfo> commentInfos=new ArrayList<>();
        JSONArray jsonArray=new JSONArray(content);
        CommentInfo commentInfo=new CommentInfo();
        ArrayList<CommentInfo.CommentReplyInfo> commentsReply=new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject1= (JSONObject) jsonArray.get(i);

            boolean isReply=false;//初始化为咨询或评价
            try {
                if(!jsonObject1.getString("rid").equals("0")){//如果Rid为非0的string字段，表示为回复
                    isReply=true;
                }
            }catch (Exception e){

                e.printStackTrace();
            }

            if(!isReply){//若为评价或咨询的开始

                if(!commentInfo.Id.equals("0")){//如果commentInfo有数据，在new之前要把他加入到arraylist
                    commentInfo.replays=commentsReply;//先将上一次的评论数据加到commentInfo中
                    commentInfos.add(commentInfo);
                }

                commentInfo=new CommentInfo();//重新new一个comment
                commentsReply=new ArrayList<>();//重新new一个commentReply

                if(jsonObject1.has("id")){
                    commentInfo.Id=jsonObject1.getString("id");
                }

                if(jsonObject1.has("rid")){
                    commentInfo.Rid=jsonObject1.getString("rid");
                }

                if(jsonObject1.has("logo")){
                    commentInfo.logo=jsonObject1.getString("logo");
                }

                if(jsonObject1.has("nick")){
                    commentInfo.nick=jsonObject1.getString("nick");
                }

                if(jsonObject1.has("zanstatus")){
                    commentInfo.zanstatus=jsonObject1.getInt("zanstatus");
                }

                if(jsonObject1.has("createdate")){
                    commentInfo.createdate=jsonObject1.getString("createdate");
                }


                if(jsonObject1.has("content")){
                    commentInfo.content=jsonObject1.getString("content");
                }

                if(jsonObject1.has("eventname")){
                    if(replyType==2){//活动咨询
                        commentInfo.eventname=eventName;
                    }else {
                        commentInfo.eventname=jsonObject1.getString("eventname");
                    }

                }

                if(jsonObject1.has("eventid")){
                    commentInfo.eventid=jsonObject1.getString("eventid");
                }

                if(jsonObject1.has("imgs")){//评价图片
                    ArrayList<String> arrayList=new ArrayList<>();

                    String imgString=jsonObject1.getString("imgs");
                    if((imgString!=null)&&(!imgString.equals("null"))){

                        JSONArray imgJsonArray=new JSONArray(imgString);
                        for(int m=0;m<imgJsonArray.length();m++){
                            arrayList.add(imgJsonArray.getString(m));
                        }
                    }
                    commentInfo.imgs=arrayList;
                }

            }else {//若为回复

                CommentInfo.CommentReplyInfo commentReplyInfo= new CommentInfo.CommentReplyInfo();

                if(jsonObject1.has("content")){
                    commentReplyInfo.commentContent=jsonObject1.getString("content");
                }

                if(jsonObject1.has("nick")){
                    commentReplyInfo.commentName=jsonObject1.getString("nick");
                }

                if(jsonObject1.has("id")){
                    commentReplyInfo.Id=jsonObject1.getString("id");
                }

                if(jsonObject1.has("rid")){
                    commentReplyInfo.Rid=jsonObject1.getString("rid");
                }
                commentsReply.add(commentReplyInfo);
            }
        }

        if(!commentInfo.Id.equals("0")){//退出整个循环之后，把最后一个加入到arraylist，防止最后一个数据丢失
            commentInfo.replays=commentsReply;//先将上一次的评论数据加到commentInfo中
            commentInfos.add(commentInfo);
        }

        return commentInfos;
    }
    Handler mHandle = new Handler();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){

                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!commentLoading) {
                                commentLoading=true;
                                pageIndex = 1;
                                requestData();
                            }
                        }
                    },1000);//延时1s，等待服务器缓存同步
                }
                break;
        }
    }

    public void stopProgress() {
        super.stopProgress();

        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    private void showCommentInfos(ArrayList<CommentInfo> CommentInfos){

        if(CommentInfos!=null){

            if(pageIndex==1){
                list.clear();
            }

            if(CommentInfos.size()!=0){
                pageIndex++;
            }

            list.addAll(CommentInfos);
            mAdapter.notifyDataSetChanged();//

            if(list.size()==0){
//                notFoundHint.setVisibility(View.VISIBLE);
                if(replyType==2){
                    notConsulationFoundHint.setVisibility(View.VISIBLE);
                }else {
                    notCommentFoundHint.setVisibility(View.VISIBLE);
                }
            }else {
                notConsulationFoundHint.setVisibility(View.GONE);
                notCommentFoundHint.setVisibility(View.GONE);

                if(CommentInfos.size()==0){
                    if(replyType==1){
                        AccuApplication.getInstance().showMsg("所有评论数据已经加载完毕");
                    }else {
                        AccuApplication.getInstance().showMsg("所有咨询数据已经加载完毕");
                    }

                }
            }
        }else {
            AccuApplication.getInstance().showMsg("加载数据失败，请检查网络后重新刷新");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_consulation:
            case R.id.consulation_now:
                Intent intent=new Intent(this,ConsulationActivity.class);
                intent.putExtra(TARGET,target);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
