package com.accuvally.hdtui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.TicketBean;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.ui.OverScrollView;
import com.accuvally.hdtui.ui.OverScrollView.OverScrollListener;
import com.accuvally.hdtui.ui.SlideOnePageGallery;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TicketVolumeActivity extends BaseActivity {

	private CommonAdapter mAdapter;
	private List<TicketBean> mList = new ArrayList<TicketBean>();
	private SlideOnePageGallery image_gallery;
	private CircleImageView ivUserLogo;
	private TextView tvUserName;
	private TextView tvDetailsTime;
	private TextView tvDetailsAddress;
	private String id = "";
	private int SourceType;
	private int isDetails;
	private OverScrollView osView;
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_volume_details);
		initProgress();

		id = getIntent().getStringExtra("id");
		SourceType = getIntent().getIntExtra("SourceType", 0);
		isDetails = getIntent().getIntExtra("isDetails", 0);
		
		key = application.getUserInfo().getAccount() + id;
		initView();
		showCacheData();
		getJsonData();
	}

	private void initView() {
		setTitle("票券");
		ivUserLogo = (CircleImageView) findViewById(R.id.ivUserLogo);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvDetailsTime = (TextView) findViewById(R.id.tvDetailsTime);
		tvDetailsAddress = (TextView) findViewById(R.id.tvDetailsAddress);
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

		findViewById(R.id.tvAccuvallyDetail).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isDetails == 1) {
					finish();
					return;
				}
				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("isHuodong", SourceType);
				startActivity(intent);
			}
		});

//		findViewById(R.id.tvRefundTicket).setOnClickListener(new OnClickListener() {//取消票卷
//			
//			@Override
//			public void onClick(View v) {
//				if (mList != null && !mList.isEmpty()) {
//					requestRefundTicket();
//				}
//			}
//		});

		image_gallery = (SlideOnePageGallery) findViewById(R.id.app_app_image_gallery);
		image_gallery.setAdapter(mAdapter = new CommonAdapter<TicketBean>(this, R.layout.listitem_ticket_volume) {

			@Override
			public void convert(ViewHolder viewHolder, TicketBean item, int position) {
				viewHolder.setText(R.id.tvTicketTitle, item.title);
				viewHolder.setText(R.id.tvCode, "票号：" + item.id);
				viewHolder.setImageUrl(R.id.ivCode, item.qrcode, UILoptions.squareOptions);
			}
		});
	}

	protected void requestRefundTicket() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("eid", id));
		params.add(new BasicNameValuePair("encryptid", mList.get(0).EncryptId));
		params.add(new BasicNameValuePair("type", "2"));// 1 表示 取消付费票（申请退款） 2 表示取消报名
		httpCilents.postA(Url.REFUND_TICKET, params, new WebServiceCallBack() {
			
			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					application.showMsg(messageInfo.getResult());
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	private void getJsonData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("eid", id));
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
			setViewText(mList);
		}
	}

	protected void setViewText(List<TicketBean> mList) {
		TicketBean bean = mList.get(0);
		if(bean != null) {
			application.mImageLoader.displayImage(bean.userlogo, ivUserLogo, UILoptions.userOptions);
			tvUserName.setText(bean.username);
			tvDetailsTime.setText(bean.getTimeStr());
			tvDetailsAddress.setText(bean.address);
		}
	}
}
