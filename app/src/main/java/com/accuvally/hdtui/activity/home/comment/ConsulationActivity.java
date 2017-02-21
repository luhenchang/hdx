package com.accuvally.hdtui.activity.home.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.Trace;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ConsulationActivity extends BaseActivity implements View.OnClickListener{


    private EditText edCommContent;
    private boolean uploading=false;
    private String target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulation);
        target=getIntent().getStringExtra(CommentDisplayActivity.TARGET);
        initView();
    }

    private void initView(){
        setTitle("发表咨询");
        edCommContent= (EditText) findViewById(R.id.edContent);
        findViewById(R.id.consulation_rewrite).setOnClickListener(this);
        findViewById(R.id.commit_consulation).setOnClickListener(this);

    }


    public void uploadComment() {
        String content = edCommContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            application.showMsg("请输入咨询内容");
            uploading=false;
            return;
        }

        if(content.trim()==""){//" " 这种也要限制
            application.showMsg("请输入咨询内容");
            uploading=false;
            return;
        }

        if(content.length()>50){
            application.showMsg("咨询字数超过50，请重新输入");
            uploading=false;
            return;
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", target));
        params.add(new BasicNameValuePair("type", "0"));//活动来源，0表示活动行，1表示活动推
        params.add(new BasicNameValuePair("comment", content));
        params.add(new BasicNameValuePair("eventtype", "0"));//类型，0代表活动，1代表主办方
        params.add(new BasicNameValuePair("ReplyType", "2"));//回复类型（1=活动评价，2=活动咨询）

        showProgress("正在提交咨询");
        httpCilents.postA(Url.ACCUPASS_UPALOD_COMMENT, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(ConsulationActivity.this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        Trace.e("",info.toString());
                        if (info.isSuccess()) {
                            edCommContent.setText("");
                            application.showMsg("咨询提交成功");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            application.showMsg(info.getMsg());
                        }
                        uploading=false;
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("网络连接断开，请检查网络");
                        uploading=false;
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.consulation_rewrite:
                edCommContent.setText("");
                break;
            case R.id.commit_consulation:
                if(!uploading){
                    uploading=true;
                    uploadComment();
                }
                break;

        }
    }
}
