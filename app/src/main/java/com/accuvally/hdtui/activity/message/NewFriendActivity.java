package com.accuvally.hdtui.activity.message;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.MailMessageTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MailMessageInfo;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.MailCountUtil;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.UIUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.HttpDelete;

public class NewFriendActivity extends BaseActivity {


    private SwipeMenuListView mListView;
    private CommonAdapter<MailMessageInfo> mAdapter;;
    private List<MailMessageInfo> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        inboxType=getIntent().getStringExtra("inboxType");
        initView();
        initData();
    }

    public void initView() {
        initProgress();
        setTitle("新同伴");

        mListView = (SwipeMenuListView) findViewById(R.id.listview);
        mAdapter = new CommonAdapter<MailMessageInfo>(this, R.layout.listitem_new_friend) {

            @Override
            public void convert(ViewHolder viewHolder, final MailMessageInfo item, int position) {
                viewHolder.setImageUrl(R.id.ivFriendAvatar, item.messages.content_logo, UILoptions.defaultUser);
                viewHolder.setText(R.id.tvTitle, item.messages.title);
                viewHolder.setText(R.id.tvContent, item.messages.message);

                TextView btAccept = viewHolder.getView(R.id.btAccept);
                if (item.messages.add_friend == 1) {
                    btAccept.setBackgroundResource(R.drawable.selector_wane_green2);
                    btAccept.setTextColor(getResources().getColor(R.color.white));
                    btAccept.setEnabled(true);
                    btAccept.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            requestAgree(item);
                        }
                    });
                } else if (item.messages.add_friend== 2) {
                    btAccept.setText("已添加");
                    btAccept.setBackgroundResource(R.drawable.shape_write_padding);
                    btAccept.setTextColor(getResources().getColor(R.color.dimgrey2));
                    btAccept.setEnabled(false);
                }
            }
        };
        mAdapter.setList(list);
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(SystemUtil.dpToPx(mContext, 90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {


                MailMessageInfo mailMessageInfo = list.get(position);

                List<MailMessageInfo> mailMessageInfos = MailMessageTable.
                        queryNFMailMessage_sourceid(AccountManager.getAccount(), inboxType, mailMessageInfo.messages.source_id);
                deleteAllMessage(inboxType, mailMessageInfos);//API删除

                int i = MailMessageTable.deleteNewFriendMailMessage(mailMessageInfo, inboxType);//本地数据库删除
                Trace.e(TAG, "deleteNewFriendMailMessage:" + i);

                list.remove(position);//UI删除
                updateView(true);

            }
        });
    }

    private void updateView(boolean isRemove){

        if(isRemove){
            TextView textView= (TextView) findViewById(R.id.friend_notfound_tv);
            textView.setText("你还没有新同伴请求哦~");
        }

        if(list.size()==0){
            findViewById(R.id.friend_notfound).setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            findViewById(R.id.friend_notfound).setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void getDataFromDB(){

        try{
            list.clear();
            List<MailMessageInfo> mailMessageInfos= MailMessageTable.
                    queryNewFriendMailMessage(AccountManager.getAccount(), inboxType);

            list.addAll(mailMessageInfos);
            updateView(false);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void initData() {
        getDataFromDB();//初次加载

        unReadCount = MailCountUtil.getUnreadCount(inboxType);

        if (unReadCount > 0) {//如果有未获取的消息，则获取未获取的
            getUnReadNumMessageDetail();

        } else {//判断是否全部已读的都加载进来了
            if (!MailCountUtil.getHistoryState(inboxType)) {//表示第一次登录，要读已读的
                getReadMessageDetail(true);//
            }
        }

    }

    private void requestAgree(final MailMessageInfo item) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accept", true + ""));
        params.add(new BasicNameValuePair("target", item.messages.source_id));
//        params.add(new BasicNameValuePair("target", item.getUserId()));   MessageInfo
        startProgress();
        httpCilents.postA(Url.social_accept, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                switch (code){
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
                        if(response.isSuccess()){
                            application.showMsg(response.getMsg());


                            List<MailMessageInfo> mailMessageInfos=MailMessageTable.
                                    queryNFMailMessage_sourceid(AccountManager.getAccount(), inboxType, item.messages.source_id);
                            deleteAllMessage(inboxType,mailMessageInfos);//API删除

                            item.messages.add_friend=2;
                            int num=MailMessageTable.updateNewFriendMailMessage(item,inboxType);



//                            Trace.e(TAG,"updateNewFriendMailMessage:"+num);
                            mAdapter.notifyDataSetChanged();
                        }else {
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


    //===================================================================================

    private String inboxType="";
    private int unReadCount=0;
    public static final String TAG="NewFriendActivity";

    public void getReadMessageDetail(final boolean isRefreshUi){//如果是刚登陆的话，尝试同步前50条
        int minMsgId=MailMessageTable.getminMailMessage(inboxType);
        Trace.e(TAG, "getReadMessageDetail，isRefreshUi：" + isRefreshUi + ",getminMailMessage:" + minMsgId);
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
                                MailCountUtil.saveHistoryStatet(inboxType, true);
                                if (i > 0 & isRefreshUi) {
                                    getDataFromDB();//getReadMessageDetail 从网络拉取已读后更新
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
                                int i = MailMessageTable.bulkUpsertMailMessage(arrayList, inboxType);
                                if (i > 0) {
                                    MailCountUtil.saveUnreadCount(inboxType, 0);
                                    getDataFromDB();//getUnReadNumMessageDetail 从网络拉取未读后更新
                                }
                                //尝试同步已读的消息。
                                if (!MailCountUtil.getHistoryState(inboxType)) {//表示第一次登录，要读已读的
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



    public  void deleteAllMessage(final String inboxType,List<MailMessageInfo> mailMessageInfos){

        for(MailMessageInfo mailMessageInfo:mailMessageInfos){
            deleteMessage(inboxType, String.valueOf(mailMessageInfo.messageId));
        }

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



}
