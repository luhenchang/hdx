package com.accuvally.hdtui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.util.MapsActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.TicketBean;
import com.accuvally.hdtui.ui.OverScrollView;
import com.accuvally.hdtui.ui.OverScrollView.OverScrollListener;
import com.accuvally.hdtui.ui.SlideOnePageGallery;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.Trace;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
// 票券详情
public class TicketVolumeActivity extends BaseActivity implements OnClickListener{

	private CommonAdapter mAdapter;
	private List<TicketBean> mList = new ArrayList<TicketBean>();
	private SlideOnePageGallery image_gallery;

    private TextView tvActuallypaid;
    private TextView tvTicketprice;
    private FrameLayout frame_Ticketprice;
    private TextView tv_ticket_title;
    private TextView tv_ticket_status;
    private TextView tv_ticket_name;
    private TextView tv_ticket_phone;
    private TextView tv_ticket_tip;
    private ImageView iv_label_checked;
    private TextView tvRefundTicket;
    private TextView tvRefundtip;


	private ImageView ivCreatorLogo;
	private TextView tvCreatorName;
	private TextView tvDetailsTime;
	private TextView tvDetailsAddress;
	private String eid = "";
    private String tid;
    private double actuallyPaid;
    private String city;
	private int SourceType;// 0 表示来源是活动行   1表示来源是其他网站
	private int isDetails;//AccuvallyDetailsActivity,IntroductPushActivity首次报名成功的时候为1  从票券列表进来的时候为0
	private OverScrollView osView;
	private String key;

    private int refundTickType;//1退款(实付款大于0）  2取消  3不支持退票

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_volume_details);
		initProgress();
//       第一次报名成功：从AccuvallyDetailsActivity进来
// AccuvallyDetailsActivity startActivity(intent.putExtra("id", detailsInfo.id).putExtra("isDetails", 1).putExtra("SourceType", 0));
//  IntroductPushActivity  startActivity(intent.putExtra("id", detailsInfo.id).putExtra("isDetails", 1).putExtra("SourceType", 0));
		eid = getIntent().getStringExtra("id");
		SourceType = getIntent().getIntExtra("SourceType", 0);
		isDetails = getIntent().getIntExtra("isDetails", 0);
		
		key = application.getUserInfo().getAccount() + eid;
		initView();
		showCacheData();
        getJsonData();
	}

	private void initView() {
		setTitle("票券");
		ivCreatorLogo = (ImageView) findViewById(R.id.ivCreatorLogo);
		tvCreatorName = (TextView) findViewById(R.id.tvCreatorName);
		tvDetailsTime = (TextView) findViewById(R.id.tvDetailsTime);
		tvDetailsAddress = (TextView) findViewById(R.id.tvDetailsAddress);

        tvActuallypaid = (TextView) findViewById(R.id.tvActuallypaid);
        tvTicketprice = (TextView) findViewById(R.id.tvTicketprice);
        frame_Ticketprice = (FrameLayout) findViewById(R.id.frame_Ticketprice);
        tv_ticket_title = (TextView) findViewById(R.id.tv_ticket_title);
        tv_ticket_status = (TextView) findViewById(R.id.tv_ticket_status);
        tv_ticket_name = (TextView) findViewById(R.id.tv_ticket_name);
        tv_ticket_phone = (TextView) findViewById(R.id.tv_ticket_phone);
        tv_ticket_tip = (TextView) findViewById(R.id.tv_ticket_tip);
        iv_label_checked = (ImageView) findViewById(R.id.iv_label_checked);
        tvRefundTicket = (TextView) findViewById(R.id.tvRefundTicket);
        tvRefundtip = (TextView) findViewById(R.id.tvRefundtip);
        findViewById(R.id.tvRefundTicket).setOnClickListener(this);
        findViewById(R.id.lyDetailsAddr).setOnClickListener(this);
        findViewById(R.id.ll_creator).setOnClickListener(this);

		osView = (OverScrollView) findViewById(R.id.osView);
		osView.setOverScrollListener(new OverScrollListener() {

            @Override
            public void headerScroll() {
                getJsonData();
            }

            @Override
            public void footerScroll() {

            }
        });





		image_gallery = (SlideOnePageGallery) findViewById(R.id.app_app_image_gallery);
		image_gallery.setAdapter(mAdapter = new CommonAdapter<TicketBean>(this, R.layout.listitem_ticket_volume) {

            @Override
            public void convert(ViewHolder viewHolder, TicketBean item, int position) {
                viewHolder.setText(R.id.tvTicketTitle, item.title);
                viewHolder.setText(R.id.tvCode, "票号：" + item.id);
                viewHolder.setImageUrl(R.id.ivCode, item.qrcode, UILoptions.squareOptions);

            }
        });

        image_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setViewText(mList, (int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
	}



	private void getJsonData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("eid", eid));
		startProgress();
		httpCilents.get(httpCilents.printURL(Url.GET_TICKETS, params), new WebServiceCallBack() {
			public void callBack(int code, Object result) {
				stopProgress();
				
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						application.cacheUtils.put(key, response.result);
						mList = JSON.parseArray(response.getResult(), TicketBean.class);
						setViewData();
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}

		});
	}
	
	private void showCacheData() {
		String value = application.cacheUtils.getAsString(key);
		if (!TextUtils.isEmpty(value)) {
			mList = JSON.parseArray(value, TicketBean.class);
			setViewData();
		}
	}

	private void setViewData() {
		if (mList != null && !mList.isEmpty()) {
            mAdapter.setList(mList);

		}
	}


    private void startDetail(){
        if (isDetails == 1) {//如果为1的话为首次报名，则不用再次打开详情activity
            finish();
            return;
        }
        Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
        intent.putExtra("id", eid);
        intent.putExtra("isHuodong", SourceType);
        startActivity(intent);
    }


    private void startMapActivity() {
        Intent intent = new Intent(mContext, MapsActivity.class);
        intent.putExtra("address", tvDetailsAddress.getText().toString().replace(" ", "").replace("\n", "").trim());
        intent.putExtra("city", city);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

	protected void setViewText(List<TicketBean> mList,int id) {
		TicketBean bean = mList.get(id);
		if(bean != null) {
            tvActuallypaid.setText(bean.actuallypaid + "");

            if(bean.actuallypaid!=bean.ticketprice){
                tvTicketprice.setText(bean.ticketprice + "");
                frame_Ticketprice.setVisibility(View.VISIBLE);
            }else {
                frame_Ticketprice.setVisibility(View.GONE);
            }

            tv_ticket_title.setText(bean.tickettitle);
            tv_ticket_status.setText(bean.statusstr);
            tv_ticket_phone.setText(bean.userphone);
            tv_ticket_name.setText(bean.username);

            if(bean.ticketisverify){
                iv_label_checked.setVisibility(View.VISIBLE);
            }else {
                iv_label_checked.setVisibility(View.GONE);
            }

            tv_ticket_tip.setText(bean.ticketdesc);

			application.mImageLoader.displayImage(bean.actcreatorlogo, ivCreatorLogo, UILoptions.userOptions);
			tvCreatorName.setText(bean.actcreatorname);
			tvDetailsTime.setText(bean.getTimeStr());
			tvDetailsAddress.setText(bean.address);

            tid=bean.id;
            actuallyPaid=bean.actuallypaid;
            city=bean.city;
            refundTickType=bean.refundtype;
            switch (bean.refundtype){//1退款(实付款大于0）  2取消  3不支持退票
                case 1:
                    tvRefundTicket.setText("申请退款");
                    tvRefundTicket.setTextColor(0xfff84d4d);
                    tvRefundtip.setVisibility(View.GONE);
                    break;
                case 2:
                    tvRefundTicket.setText("取消报名");
                    tvRefundTicket.setTextColor(0xfff84d4d);
                    tvRefundtip.setVisibility(View.GONE);
                    break;
                case 3:
                    tvRefundTicket.setText("申请退票");
                    tvRefundTicket.setTextColor(0xff999999);
                    tvRefundtip.setText(bean.refundtips);
                    tvRefundtip.setVisibility(View.VISIBLE);
                    break;
            }
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Trace.e("onActivityResult","RESULT_OK");
                    tvRefundTicket.setTextColor(0xff999999);
                    getJsonData();
                }
                break;
        }
    }

    private void resolveRefundButton(){
        switch (refundTickType){//1退款(实付款大于0）  2取消  3不支持退票
            case 1:
                Intent intent=new Intent(this,RefundActivity.class);
                intent.putExtra(RefundActivity.REFUND_TYPE,1);
                intent.putExtra(RefundActivity.REFUND_EID,eid);
                intent.putExtra(RefundActivity.REFUND_TID,tid);
                intent.putExtra(RefundActivity.REFUND_PAY,actuallyPaid);
                startActivityForResult(intent, 1);
                break;
            case 2:
                Intent intent2=new Intent(this,RefundActivity.class);
                intent2.putExtra(RefundActivity.REFUND_TYPE,2);
                intent2.putExtra(RefundActivity.REFUND_EID,eid);
                intent2.putExtra(RefundActivity.REFUND_TID,tid);
                startActivityForResult(intent2, 1);
                break;
            case 3:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lyDetailsAddr:
                startMapActivity();
                break;
            case R.id.ll_creator:
                startDetail();
                break;
            case R.id.tvRefundTicket:
                resolveRefundButton();
                break;
        }
    }
}
