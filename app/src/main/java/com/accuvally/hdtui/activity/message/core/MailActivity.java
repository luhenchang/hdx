package com.accuvally.hdtui.activity.message.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.web.AccuvallyWebDetailsActivity;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.web.ProjectDetailsActivity;
import com.accuvally.hdtui.activity.home.SponsorDetailActivity;
import com.accuvally.hdtui.activity.mine.FriendsActivity;
import com.accuvally.hdtui.activity.mine.TicketTabActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.MailMessageTable;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MailMessageInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.MailCountUtil;
import com.accuvally.hdtui.utils.Trace;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MailActivity extends BaseActivity {

    private String inboxType="";
    private int unReadCount=0;
//    private int totalCount;
//    private int dbMailCount;

    private int pageSize =20;
    private int pageIndex =0;


    public static final String TAG="MailActivity";

    private ListView mListView;
    private CommonAdapter<MailMessageInfo> mAdapter;//消息fragme的adapter
    private final List<MailMessageInfo> list = new ArrayList<MailMessageInfo>();

    PtrClassicFrameLayout ptrClassicFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);


        inboxType=getIntent().getStringExtra("inboxType");

        initView();
        initData();

    }



    public  void deleteMessage(final String inboxType,final String messageId){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "https://api.leancloud.cn/1.1/subscribe/statuses/inbox";
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");

                    org.json.JSONObject json = new org.json.JSONObject();
                    json.put("__type", "Pointer");
                    json.put("className", "_User");
                    json.put("objectId", AccountManager.getAccount());//6801567573489
                    Trace.e(TAG, "try to deleteMessage,inboxType:"+inboxType+",messageId:"+messageId);

                    HttpDelete request = new HttpDelete(url+"?owner="+ URLEncoder.encode(json.toString(), "utf-8")+"&inboxType="+inboxType+"&messageId="+messageId);//暂时写死
                    request.addHeader("X-AVOSCloud-Application-Id", Config.ACCUPASS_LEANCLOUD_APP_ID);
                    request.addHeader("X-AVOSCloud-Application-Key", Config.ACCUPASS_LEANCLOUD_APP_KEY);
                    request.addHeader("Content-Type", "application/json");
                    HttpResponse response = httpClient.execute(request);
                    String result= EntityUtils.toString(response.getEntity());
                    int statusCode = response.getStatusLine().getStatusCode();
                    Trace.e(TAG, "deleteMessage,result:"+result+",statusCode:"+statusCode);

                    if(statusCode==200){
                        //删除成功
                    }

                }catch (Exception e) {
                    Trace.e(TAG,e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }




    private void initData(){
        getDataFromDB();//初次加载

        unReadCount= MailCountUtil.getUnreadCount(inboxType);
//        totalCount= MailCountUtil.getTotalCount(inboxType);
//        dbMailCount= MailMessageTable.getMailMessageCount(inboxType);

        if(unReadCount>0){//如果有未获取的消息，则获取未获取的
            getUnReadNumMessageDetail();

        }else {//判断是否加载过历史记录
            if(!MailCountUtil.getHistoryState(inboxType)){
                getReadMessageDetail(true);
            }
        }

    }

    private void initView() {

        initProgress();
        switch (inboxType){
            case MailMessageTable.sysMsg:
                setTitle("系统消息");
                break;
            case MailMessageTable.recommend:
                setTitle("官方推荐");
                break;
        }


        ptrClassicFrameLayout= (PtrClassicFrameLayout) findViewById(R.id.ticklist_pull);
        mListView = (ListView) findViewById(R.id.listview);

        mAdapter = new CommonAdapter<MailMessageInfo>(mContext, R.layout.listitem_mail) {

            @Override
            public void convert(ViewHolder viewHolder, MailMessageInfo item,final int position) {
                viewHolder.setText(R.id.item_mail_time, item.getMailTime());
                switch (inboxType){
                    case MailMessageTable.sysMsg:
                        viewHolder.setText(R.id.item_mail_mailtitle, "系统消息");
                        viewHolder.setImageResource(R.id.mail_logo, R.drawable.mail_sys_msg_circle);
                        viewHolder.setImageResource(R.id.item_mail_top_bg, R.drawable.shap_mail_corner_blue);
                        break;
                    case MailMessageTable.recommend:
                        viewHolder.setText(R.id.item_mail_mailtitle, "官方推荐");
                        viewHolder.setImageResource(R.id.mail_logo, R.drawable.mail_recommend_circle);
                        viewHolder.setImageResource(R.id.item_mail_top_bg, R.drawable.shap_mail_corner_green);
                        break;
                }
                viewHolder.setText(R.id.item_mail_title, item.messages.title);
                viewHolder.setText(R.id.item_mail_content, item.messages.message);
                viewHolder.setImageUrlWithEmpty(R.id.item_mail_content_logo,
                        item.messages.content_logo, UILoptions.squareOptions);


                View view=viewHolder.getView(R.id.item_mail_main_content);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //更新db isRead
                        MailMessageInfo info = list.get(position);
                        info.isRead = 1;
                        boolean b = MailMessageTable.updateMailMessage(info, inboxType);

                        Trace.e(TAG, "updateMailMessage:" + b);
                        switch (info.messages.op_type) {
                            case 1:// 活动
                                mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                                        putExtra("id", info.messages.op_value));
                                break;
                            case 2:// 专题
                                Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
                                intent.putExtra("title", info.messages.title);
                                int index= info.messages.op_value.indexOf("huodongxing.com/news/");
                                if( index!= -1){
                                    String str[]=info.messages.op_value.split("huodongxing.com/news/");
                                    intent.putExtra("id", str[1]);
                                    mContext.startActivity(intent);
                                }else {
                                    intent.putExtra("id", info.messages.op_value);
                                    mContext.startActivity(intent);
                                }
                                break;
                            case 3:// 圈子
                                SessionInfo session = SessionTable.querySessionById(info.messages.op_value);
                                if (session == null) {
                                    session = new SessionInfo();
                                    session.setSessionId(info.messages.op_value);
                                    session.setTitle(info.messages.title);
                                    session.setLogoUrl(info.messages.content_logo);
                                    SessionTable.insertOrUpdateSession(session);
                                }

                                application.setCurrentSession(session);
                                mContext.startActivity(new Intent(mContext, ChatActivity.class));

                                break;
                            case 4:// 主办方
                                mContext.startActivity(new Intent(mContext, SponsorDetailActivity.class).
                                        putExtra("orgId", info.messages.op_value));
                                break;
                            case 5:// 网页
                                mContext.startActivity(new Intent(mContext, AccuvallyWebDetailsActivity.class).putExtra("loadingUrl",
                                        info.messages.op_value).putExtra("injectJs", ""));
                                break;
                            case 6:// 票券
                                mContext.startActivity(new Intent(mContext, TicketTabActivity.class));
                                break;
                            case 9:// 同伴(朋友)列表
                                toActivity(FriendsActivity.class);
                                break;
                        }
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //return true if the callback consumed the long click, false otherwise
                        View root = getLayoutInflater().inflate(R.layout.popup_delete_mail, null);

                        final PopupWindow popup = new PopupWindow(root,
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        popup.setFocusable(true);//yao
                        popup.setOutsideTouchable(false);
                        popup.setTouchable(true);

                        root.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    popup.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        root.findViewById(R.id.popup_delete_mail_tv).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    popup.dismiss();
                                    MailMessageInfo info = list.get(position);
                                    deleteMessage(inboxType, String.valueOf(info.messageId));//API删除
                                    MailMessageTable.deleteMailMessage(info, inboxType);//数据库删除

                                    list.remove(info);//UI删除
                                    updateView(true);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        popup.showAtLocation(mListView, Gravity.CENTER, 0, 0);
                        return true;
                    }
                });

            }
        };

        mAdapter.setList(list);
        mListView.setAdapter(mAdapter);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(final PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pageIndex++;
                        getDataFromDBLoadMore();//loadmore
                        frame.refreshComplete();

                    }
                }, 0);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {

//                Trace.e(TAG, "下拉");
//                frame.refreshComplete();
            }
        });



        // the following are default settings
        ptrClassicFrameLayout.setResistance(1.7f);
        ptrClassicFrameLayout.setRatioOfHeaderHeightToRefresh(1.0f);
        ptrClassicFrameLayout.setDurationToClose(200);
        ptrClassicFrameLayout.setDurationToBack(200);
        ptrClassicFrameLayout.setDurationToCloseHeader(1000);
        // default is false
        ptrClassicFrameLayout.setPullToRefresh(true);
        // default is true
        ptrClassicFrameLayout.setKeepHeaderWhenRefresh(true);


        ptrClassicFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }


    private void updateView(boolean isRemove){
        if(list.size()==0){
            switch (inboxType){
                case MailMessageTable.sysMsg:
                    findViewById(R.id.sys_msg_notfound).setVisibility(View.VISIBLE);
                    findViewById(R.id.recommend_notfound).setVisibility(View.GONE);
                    if(isRemove){
                        TextView textView= (TextView) findViewById(R.id.sys_msg_notfound_tv);
                        textView.setText("系统消息已经全部删除~");
                    }
                    break;
                case MailMessageTable.recommend:
                    findViewById(R.id.sys_msg_notfound).setVisibility(View.GONE);
                    findViewById(R.id.recommend_notfound).setVisibility(View.VISIBLE);
                    if(isRemove){
                        TextView textView= (TextView) findViewById(R.id.recommend_notfound_tv);
                        textView.setText("官方推荐已经全部删除~");
                    }
                    break;
            }
            ptrClassicFrameLayout.setVisibility(View.GONE);
        }else {
            findViewById(R.id.sys_msg_notfound).setVisibility(View.GONE);
            findViewById(R.id.recommend_notfound).setVisibility(View.GONE);
            ptrClassicFrameLayout.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void getDataFromDB(){

        try{
            List<MailMessageInfo> mailMessageInfos= MailMessageTable.queryMailMessage(AccountManager.getAccount(),
                    inboxType, pageSize, pageSize*pageIndex);
            list.addAll(mailMessageInfos);
//            mAdapter.notifyDataSetChanged();
            updateView(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getDataFromDBLoadMore(){
        try{
            List<MailMessageInfo> mailMessageInfos= MailMessageTable.queryMailMessage(AccountManager.getAccount(),
                    inboxType, pageSize, pageSize*pageIndex);
            list.addAll(mailMessageInfos);
            mAdapter.notifyDataSetChanged();

            if(mailMessageInfos.size()==0) {
                switch (inboxType) {
                    case MailMessageTable.sysMsg:
                        application.showMsg("所有系统消息已加载完成");
                        break;
                    case MailMessageTable.recommend:
                        application.showMsg("所有推荐已加载完成");
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }




    public void getReadMessageDetail(final boolean isRefreshUi){//如果是刚登陆的话，尝试同步前50条
        int minMsgId=MailMessageTable.getminMailMessage(inboxType);
        Trace.e(TAG, "getReadMessageDetail，isRefreshUi："+isRefreshUi+",getminMailMessage:"+minMsgId);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("inboxType", inboxType));
        params.add(new BasicNameValuePair("lastMsgId", minMsgId + ""));
        params.add(new BasicNameValuePair("count", 100 + ""));
        startProgress();
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_INBOX, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            try {
                                ArrayList<MailMessageInfo> arrayList = (ArrayList<MailMessageInfo>) JSONObject.
                                        parseArray(response.getResult(), MailMessageInfo.class);
                                int i = MailMessageTable.bulkUpsertMailMessage(arrayList, inboxType);
                                MailCountUtil.saveHistoryStatet(inboxType,true);
                                if (i > 0 &isRefreshUi) {
                                    list.clear();
                                    pageIndex = 0;
                                    getDataFromDB();//getReadMessageDetail 从网络拉取后更新
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            if (isRefreshUi) {
                                application.showMsg(response.getMsg());
                            }
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        if (isRefreshUi) {
                            application.showMsg("网络连接断开，请检查网络");
                        }
                        break;
                }
            }
        });


    }




    public  void getUnReadNumMessageDetail(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("inboxType", inboxType));
        //接收到的最大messageId。默认为0，表示收件箱里的最大消息ID。 例如，当我不知道消息箱的最大消息数，
        // 只知道有三条未读消息，(实际消息总是是19)此时传messageId=0，消息数传3.则返回的是消息id为19，18，17的这三台消息

        //若我们传递消息messageId=15，消息数传3。 则返回的是消息id为14，13，12的这三台消息

        // messageId越大，表示越后面发的消息。
        //最早发送的消息的id为1

        params.add(new BasicNameValuePair("lastMsgId", 0 + ""));
        params.add(new BasicNameValuePair("count", unReadCount + ""));
        startProgress();
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_INBOX, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            try {
                                ArrayList<MailMessageInfo> arrayList = (ArrayList<MailMessageInfo>) JSONObject.
                                        parseArray(response.getResult(), MailMessageInfo.class);
                                int i = MailMessageTable.bulkUpsertMailMessage(arrayList,inboxType);
                                if(i>0){
                                    MailCountUtil.saveUnreadCount(inboxType,0);
                                    list.clear();
                                    pageIndex = 0;
                                    getDataFromDB();//getUnReadNumMessageDetail从网络拉取后更新
                                }
                                //尝试同步已读的消息。
//                                Trace.e(TAG, inboxType + ",getUnReadNumMessageDetail之后的，unReadCount:"
//                                        + unReadCount + ",totalCount" + totalCount + ",dbMailCount:" + dbMailCount);
                                if(!MailCountUtil.getHistoryState(inboxType)){//表示第一次登录，要读已读的
                                    getReadMessageDetail(true);//
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            application.showMsg(response.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("网络连接断开，请检查网络");
                        break;
                }
            }
        });

    }


}
