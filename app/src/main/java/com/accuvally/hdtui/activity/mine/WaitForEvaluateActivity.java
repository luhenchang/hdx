package com.accuvally.hdtui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.WaitForEvaAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.PublishBean;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.utils.HttpCilents;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Liu on 2017/7/9.
 */
public class WaitForEvaluateActivity extends BaseActivity{

    private XListView mListView;
    private ArrayList<PublishBean> listData = new ArrayList<PublishBean>();
    private WaitForEvaAdapter mAdapter;


    RelativeLayout waitforevaluate_notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitfor_evaluate);
        initProgress();
        initView();
    }


    public void initView() {
        setTitle("待评价");
        mListView = (XListView) findViewById(R.id.lvWaitForEvaluate);


        waitforevaluate_notfound= (RelativeLayout) findViewById(R.id.waitforevaluate_notfound);

        mAdapter =  new WaitForEvaAdapter(this,listData);
        mListView.setAdapter(mAdapter);

        mListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                getListData();
            }

            @Override
            public void onLoadMore() {
            }
        });
        getListData();
    }


    public void getListData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        startProgress();
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_NEEDTOCOMMENT, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                mListView.stopRefresh();
                mListView.stopLoadMore();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            List<PublishBean> list = JSON.parseArray(response.getResult(), PublishBean.class);

                                if (list.size() == 0) {
                                    waitforevaluate_notfound.setVisibility(View.VISIBLE);
                                    mListView.setVisibility(View.GONE);
                                }else {
                                    waitforevaluate_notfound.setVisibility(View.GONE);
                                    mListView.setVisibility(View.VISIBLE);
                                }

                            listData.clear();
                            listData.addAll(list);
                            mAdapter.notifyDataSetChanged();

                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }

            }

        });
    }



}
