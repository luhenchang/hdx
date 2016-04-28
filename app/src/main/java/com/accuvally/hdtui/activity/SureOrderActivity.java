package com.accuvally.hdtui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.PayInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUnFinishedEventBus;
import com.accuvally.hdtui.utils.pay.PayUtils;
import com.accuvally.hdtui.utils.pay.Result;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 订单确认
 * 
 * @author Semmer Wang
 * 
 */
public class SureOrderActivity extends BaseActivity implements OnClickListener {

	private TextView alipayBtn;

	private TextView tvPayUserName;

	private TextView tvPayUserPhone;

	private TextView tvAccuTitle;

	private TextView tvTicketType;

	private TextView tvTicketCount;

	private TextView tvTicketPrice;

	private ImageView ivAccuImg;

	private TextView tvTotalPrice;

	private Button ExplainBtn;

	private Dialog dialog;

	private RegSuccessInfo info;

	private static final int SDK_PAY_FLAG = 1;

	private int tag;

	private String id;

	PayInfo payInfo;

	private LinearLayout ly_pay_type_wx, ly_pay_type_ali;
	
	private LinearLayout llRefundContent;

	private int PAY_TYPE = 1;

	private ImageView ivCheckWx, ivCheckAli;

	private ImageView ivRefund;
	
	IWXAPI api;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sure_order);
		EventBus.getDefault().register(this);
		setTitle(R.string.sure_order_text1);
		api = WXAPIFactory.createWXAPI(mContext, null);
		api.registerApp(Config.WX_APPID);
		tag = getIntent().getIntExtra("tag", 0);
		if (tag == 1) {
			info = (RegSuccessInfo) getIntent().getSerializableExtra("info");
		} else {
			id = getIntent().getStringExtra("id");
		}
		initView();
		if (tag == 1) {
			initData();
		} else {
			getsureOrder();
		}
		api = WXAPIFactory.createWXAPI(this, Config.WX_APPID);
	}

	@SuppressLint("CutPasteId")
	public void initView() {
		ExplainBtn = (Button) findViewById(R.id.ExplainBtn);
		alipayBtn = (TextView) findViewById(R.id.alipayBtn);
		tvPayUserName = (TextView) findViewById(R.id.tvPayUserName);
		tvPayUserPhone = (TextView) findViewById(R.id.tvPayUserPhone);
		tvAccuTitle = (TextView) findViewById(R.id.tvOrderTicketTitle);
		tvTicketCount = (TextView) findViewById(R.id.orderTicketNum);
		tvTicketPrice = (TextView) findViewById(R.id.orderTicketPrice);
		ivAccuImg = (ImageView) findViewById(R.id.ivOrderTicketImg);
		tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
		tvTicketType = (TextView) findViewById(R.id.orderTicketName);
		ly_pay_type_wx = (LinearLayout) findViewById(R.id.ly_pay_type_wx);
		ly_pay_type_ali = (LinearLayout) findViewById(R.id.ly_pay_type_ali);
		ivCheckWx = (ImageView) findViewById(R.id.ivCheckWx);
		ivCheckAli = (ImageView) findViewById(R.id.ivCheckAli);
		
		llRefundContent = (LinearLayout) findViewById(R.id.llRefundContent);
		llRefundContent.setVisibility(View.GONE);
		ivRefund = (ImageView) findViewById(R.id.ivRefund);
		findViewById(R.id.rlRefund).setOnClickListener(this);
		findViewById(R.id.tvHow).setOnClickListener(this);

		ExplainBtn.setOnClickListener(this);
		alipayBtn.setOnClickListener(this);
		ly_pay_type_wx.setOnClickListener(this);
		ly_pay_type_ali.setOnClickListener(this);

	}

	public void initData() {
		if (info != null) {
			tvPayUserName.setText(info.getUserName());
			tvPayUserPhone.setText(info.getPhone());
			tvAccuTitle.setText(info.getTitle());
			tvTicketCount.setText(info.getCount() + "张");
			tvTicketPrice.setText("￥" + info.getPrice());
			tvTicketType.setText(info.getTicketType() + "");
			application.mImageLoader.displayImage(info.getLogo(), ivAccuImg);
			tvTotalPrice.setText("￥" + info.getTotal());
			TextView tvRefundContent = (TextView) findViewById(R.id.tvRefundContent);// 退款内容
			if (!TextUtils.isEmpty(info.getRefundPolicy())) {
				tvRefundContent.setText(info.getRefundPolicy().replace("\\n", "\n"));
			}
		}
	}

	//从未完成票券进入tag=0才获取订单信息
	public void getsureOrder() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		showProgress("正在获取订单信息");
		httpCilents.get(httpCilents.printURL(Url.PAY_DETAILS, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						info = JSON.parseObject(messageInfo.getResult(), RegSuccessInfo.class);
						initData();
					} else {
						application.showMsg(messageInfo.getMsg());
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ExplainBtn:
			showDialog();
			break;
		case R.id.ivCloseDialog:
			dialog.dismiss();
			break;
		case R.id.alipayBtn:// 付款
			if (Utils.isFastDoubleClick())
				return;
			if (info != null) {
				checkout();
			}
			break;
		case R.id.ly_pay_type_ali:
			PAY_TYPE = 1;
			ivCheckAli.setBackgroundResource(R.drawable.alipay_right_bg_selected);
			ivCheckWx.setBackgroundResource(R.drawable.alipay_right_bg_normal);
			break;
		case R.id.ly_pay_type_wx:
			boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
			if (isPaySupported) {
				PAY_TYPE = 2;
				ivCheckAli.setBackgroundResource(R.drawable.alipay_right_bg_normal);
				ivCheckWx.setBackgroundResource(R.drawable.alipay_right_bg_selected);
			} else {
				application.showMsg("您手机未安装微信或版本太低");
			}
			break;
		case R.id.rlRefund:
			if(llRefundContent.getVisibility() == View.GONE) {
				llRefundContent.setVisibility(View.VISIBLE);
				ivRefund.setImageResource(R.drawable.tuikuanxia);
			} else {
				llRefundContent.setVisibility(View.GONE);
				ivRefund.setImageResource(R.drawable.tuikuanyou);
			}
			break;
		case R.id.tvHow:
			toActivity(HowHandleActivity.class);
			break;
		}
	}

	public void checkout() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "0"));
		params.add(new BasicNameValuePair("targets", info.getEncryptid()));
		if (PAY_TYPE == 1) {
			params.add(new BasicNameValuePair("paytype", "direct_pay"));
		} else {
			params.add(new BasicNameValuePair("paytype", "weixin_app"));
		}
		params.add(new BasicNameValuePair("currency", "RMB"));
		params.add(new BasicNameValuePair("amount", info.getTotal() + ""));
		showProgress("正在进入支付页面");
		httpCilents.postA(Url.PAY, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						if (PAY_TYPE == 1) {
							aliPay(messageInfo.getResult());
						} else {
							payInfo = JSON.parseObject(messageInfo.getResult(), PayInfo.class);
							wxPay(payInfo);
						}
					} else {
						application.showMsg(messageInfo.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	Timer timer;
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
//					String endTimes = TimeUtils.formatter.format(new java.util.Date());
//					int results = Utils.timeCompareTo(info.getStartTime(), endTimes);
					int results = Utils.timeCompareTo(info.getStartutc(), info.getEndutc());
					if (results > 0){
						if (!dbManager.isAccuDetails(info.getId()))
							if (application.sharedUtils.readInt("remind") != 4) {
								TimeUtils.addEvent(mContext, info.getStartutc(), info.getTitle(), Html.fromHtml(info.getSummary()).toString(), info.getCity());
								dbManager.insertAccuDetails(info.getId());
							}
					}
					
//					Intent intent = new Intent();
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("info", info);
//					bundle.putSerializable("DetailsInfo", getIntent().getSerializableExtra("DetailsInfo"));
//					intent.putExtras(bundle);
//					intent.setClass(SureOrderActivity.this, PaySuccessActivity2.class);
//					startActivity(intent);
				} catch (Exception e) {
				}
				addUmengEvent();
				dismissProgress();
				EventBus.getDefault().post(new ChangeDetailsDialogEventBus(info));
				EventBus.getDefault().post(new ChangeUnFinishedEventBus(true));
				if (timer != null) {
					timer.cancel();
				}
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG:
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				if (TextUtils.equals(resultStatus, Config.PAY_SUCCESS)) {
					showProgress("处理中");
					timer = new Timer(true);
					timer.schedule(task, 3000, 5000);
				} else {
					if (TextUtils.equals(resultStatus, Config.PAYING)) {
						// application.showMsg("支付结果确认中");
					} else {
						application.showMsg("支付失败");
					}
				}
				break;
			default:
				break;
			}
		};
	};

	public void aliPay(final String payStr) {
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(SureOrderActivity.this);
				String result = alipay.pay(payStr);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	public void onEventMainThread(ChangePaySuccessEventBus eventBus) {
		//微信支付成功的回调
//		if(eventBus.getMsg()==100){
//			Intent intent = new Intent();
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("info", info);
//			bundle.putSerializable("DetailsInfo", getIntent().getSerializableExtra("DetailsInfo"));
//			intent.putExtras(bundle);
//			intent.setClass(SureOrderActivity.this, PaySuccessActivity2.class);
//			startActivity(intent);
//		}
		
		addUmengEvent();
		EventBus.getDefault().post(new ChangeDetailsDialogEventBus(info));
		EventBus.getDefault().post(new ChangeUnFinishedEventBus(true));
		finish();
	}

	private void addUmengEvent() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", info.getId());
		map.put("title", info.getTitle());
		map.put("price", info.getPrice() + "");
		map.put("total", info.getTotal() + "");
		map.put("Count", info.getCount() + "");
		map.put("UserName", info.getUserName());
		map.put("Phone", info.getPhone());
		map.put("IsAppPay", info.getIsAppPay() + "");
		MobclickAgent.onEventValue(mContext, "sum_of_ticket_boxing", map, (int)info.getTotal());
	}

	// 付款说明
	public void showDialog() {
		dialog = new Dialog(mContext, R.style.dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.dialog_explain);
		ImageView ivCloseDialog = (ImageView) dialog.findViewById(R.id.ivCloseDialog);
		ivCloseDialog.setOnClickListener(this);
		DialogUtils.dialogSet(dialog, mContext, Gravity.CENTER, 0.95, 0.8, true, true, false);
		dialog.show();
	}

	public void wxPay(PayInfo info) {
		PayReq req = new PayReq();
//		req.appId = info.getAppId();
//		req.partnerId = info.getPartnerId();
//		req.packageValue = info.getPackage();
		req.appId = "wxe558f23f81119f7a";
		req.partnerId = "1227124401";
		req.packageValue = "Sign=WXPay";
		req.prepayId = info.getPrepayId();
		req.nonceStr = info.getNoncestr();
		req.timeStamp = info.getTimestamp();
		req.sign = info.getSign();
		req.extData = "app data"; // optional
		api.sendReq(req);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
