package com.accuvally.hdtui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.PublishBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.utils.HttpCilents;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Liu on 2017/7/9.
 */
public class PublishActivity extends BaseActivity implements View.OnClickListener{

    private XListView mListView;
    private List<PublishBean> listData = new ArrayList<>();
    private int pageIndex = 1, pageSize = 10;
    private CommonAdapter<PublishBean> mAdapter;

//    private LinearLayout lyFailure;
//    private TextView tvNoData;
//    private ImageView ivFailure;
//    private Button SquareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initProgress();
        initView();
    }


    public void initView() {
        setTitle("我发布的");
        mListView = (XListView) findViewById(R.id.lvPublish);


       /* lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        ivFailure = (ImageView) findViewById(R.id.ivFailure);
        SquareBtn = (Button) findViewById(R.id.SquareBtn);

        ivFailure.setBackgroundResource(R.drawable.no_emty);
        tvNoData.setText("加载失败，请刷新");
        SquareBtn.setText("刷新");
        SquareBtn.setTextColor(getResources().getColor(R.color.white));
        SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
        int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
        SquareBtn.setPadding(padding, padding, padding, padding);
        SquareBtn.setOnClickListener(this);*/

        mAdapter =  new CommonAdapter<PublishBean>(this, R.layout.listitem_publish) {

            @Override
            public void convert(ViewHolder viewHolder, final PublishBean item, final int position) {
                viewHolder.setImageUrl(R.id.ivItemPublishImg, item.logo, UILoptions.rectangleOptions);
                viewHolder.setText(R.id.tvItemTitle, item.title);
                viewHolder.setText(R.id.tvItemTime, item.getStartutc());
                viewHolder.setText(R.id.item_publish_reg, item.regnum+"");
//                viewHolder.setText(R.id.tvItemTitle, item.title);
                switch (item.statusstr){
                    case "未开始":
                        viewHolder.setTextColor(R.id.item_publish_status,0xffffa90a);
                        break;
                    case "进行中":
                        viewHolder.setTextColor(R.id.item_publish_status,0xff84c554);
                        break;
                    case "已结束":
                        viewHolder.setTextColor(R.id.item_publish_status,0xff999999);
                        break;
                    case "审核中":
                        viewHolder.setTextColor(R.id.item_publish_status,0xff0cbe06);
                        break;
                }
                viewHolder.setText(R.id.item_publish_status, item.statusstr);
//
            }
        };


        mAdapter.setList(listData);
        mListView.setAdapter(mAdapter);
        mListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                pageIndex = 1;
                getListData();
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                getListData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PublishBean publishBean=listData.get(position-1);
                startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                        putExtra("id", publishBean.id));
            }
        });

        // 缓存
        String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_publish");
        if (pageIndex == 1 && !TextUtils.isEmpty(cache)) {
            List<PublishBean> listCache = JSON.parseArray(cache, PublishBean.class);
            if (listCache != null) {
                listData.addAll(listCache);
                mAdapter.notifyDataSetChanged();
            }
        }
        getListData();
    }


    public void getListData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pi", pageIndex + ""));
        params.add(new BasicNameValuePair("ps", pageSize + ""));
        startProgress();
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYPUBLISH, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if(response.isSuccess()) {
                            List<PublishBean> list = JSON.parseArray(response.getResult(), PublishBean.class);
                            if (list == null){
                                if(pageIndex==1){
                                   /* lyFailure.setVisibility(View.VISIBLE);
                                    lyFailure.setVisibility(View.GONE);//临时加的
                                    mListView.setEmptyView(lyFailure);*/
                                }
                                return;
                            }
                            if (pageIndex == 1) {
                                listData.clear();
                                if(list.size()==0){
                                   /* lyFailure.setVisibility(View.VISIBLE);
                                    lyFailure.setVisibility(View.GONE);//临时加的
                                    mListView.setEmptyView(lyFailure);*/
                                }
                            }
                            listData.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            // 缓存
                            if (pageIndex == 1) {
                                application.cacheUtils.put(application.getUserInfo().getAccount() + "_publish", response.result);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.more_service_host:

                break;*/

        }
    }



}
