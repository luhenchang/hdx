package com.accuvally.hdtui.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.entry.MainActivityNew;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RefundActivity extends BaseActivity {


    public static final String REFUND_TYPE="RefundActivity_REFUND_TYPE";
    public static final String REFUND_EID="RefundActivity_REFUND_EID";
    public static final String REFUND_TID="RefundActivity_REFUND_TID";
    public static final String REFUND_PAY="RefundActivity_REFUND_PAY";
    private int refundTickType;//1为退钱 2为取消
    private double actuallyPaid;
    private String eid;
    private String tid;
    private String remark;

    private boolean submitAble=true;

    private Button refund_submit;
    private EditText refund_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        Intent intent=getIntent();

        refundTickType=intent.getIntExtra(REFUND_TYPE, 1);
        eid=intent.getStringExtra(REFUND_EID);
        tid=intent.getStringExtra(REFUND_TID);
        actuallyPaid=intent.getDoubleExtra(REFUND_PAY, 0.0);

        initView();

    }


    public void shwoRefundDialog() {
        Dialog refundDialog = new Dialog(mContext, R.style.DefaultDialog);
        refundDialog.setCancelable(false);
        refundDialog.setCanceledOnTouchOutside(false);
        refundDialog.setContentView(R.layout.dialog_refund_success);

        refundDialog.findViewById(R.id.dialog_refund_toHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MainActivityNew.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        refundDialog.findViewById(R.id.dialog_refund_toTicket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        refundDialog.show();
    }


    private void initView(){

        refund_submit= (Button) findViewById(R.id.refund_submit);
        refund_comment= (EditText) findViewById(R.id.refund_comment);
        if(refundTickType==1){
            setTitle("退票");
            actuallyPaid=actuallyPaid*0.9;
            java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
            ((TextView)findViewById(R.id.refund_amount)).setText(df.format(actuallyPaid));
        }else {
            setTitle("取消报名");
            findViewById(R.id.ll_refund_amount_lujin).setVisibility(View.GONE);
            findViewById(R.id.ll_refund_tip).setVisibility(View.GONE);
        }

        refund_comment.addTextChangedListener(watcher);
        refund_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refund_ticket();
            }
        });

    }



    private void refund_ticket(){

        remark=refund_comment.getText().toString();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tid", tid));
        params.add(new BasicNameValuePair("eid", eid));
        params.add(new BasicNameValuePair("type",refundTickType+""));
        params.add(new BasicNameValuePair("remark", remark));
//        Trace.e("Url.TICKET_REFUND",params.toString());
        httpCilents.postA(Url.TICKET_REFUND, params, new HttpCilents.WebServiceCallBack() {
            @Override
            public void callBack(int code, Object result) {
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
//                        application.showMsg(result.toString());
//                        Trace.e("Url.TICKET_REFUND", result.toString());
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            submitAble=false;
                            refund_submit.setBackgroundResource(R.drawable.refund_bg_dis);
                            refund_submit.setEnabled(false);
                            shwoRefundDialog();
                        }else {
                            application.showMsg(response.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("提交失败，请检查网络后重试");
//                        Trace.e("Url.TICKET_REFUND", result.toString());
                        break;
                }
            }
        });

    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(!submitAble)
                return;

            boolean Sign1 = refund_comment.getText().length() > 0;
            if (Sign1 ) {
                refund_submit.setBackgroundResource(R.drawable.refund_bg);
                refund_submit.setEnabled(true);
            } else {
                refund_submit.setBackgroundResource(R.drawable.refund_bg_dis);
                refund_submit.setEnabled(false);
            }


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };


}
