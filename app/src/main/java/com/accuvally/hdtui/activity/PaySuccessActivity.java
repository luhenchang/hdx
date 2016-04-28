package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.PaySuccessAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.HomeEventInfo;
//import com.accuvally.hdtui.model.PayInfo;
//import com.accuvally.hdtui.model.RegSuccessInfo;
//import com.accuvally.hdtui.model.BaseResponse;
//import com.accuvally.hdtui.ui.ScrolListView;
//import com.accuvally.hdtui.utils.DialogUtils;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.ShareUtils;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.sso.UMSsoHandler;
//
///**
// * 交易成功-失败
// * 
// * @author Semmer Wang
// * 
// */
//public class PaySuccessActivity extends BaseActivity implements OnClickListener {
//
//	private ScrollView scrollview;
//
//	private ScrolListView mListView;
//
//	private PaySuccessAdapter mAdapter;
//
//	private List<HomeEventInfo> list;
//
//	private TextView tvPayTitle, tvPayCity, tvPayTicketType, tvRemainingTimeStr;
//
//	private LinearLayout ly_share;
//
//	private RegSuccessInfo info;
//
//	private TextView RePaymentBtn;
//
//	PayInfo payInfo;
//
//	private String ticketId;
//
//	ShareUtils shareUtils;
//
//	private UMSocialService mController;
//
//	private Dialog shareDialog;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_pay_success);
//		info = (RegSuccessInfo) getIntent().getSerializableExtra("info");
//		ticketId = getIntent().getStringExtra("ticketId");
//		initView();
//		initData();
//	}
//
//	public void initView() {
//		scrollview = (ScrollView) findViewById(R.id.scrollview);
//		mListView = (ScrolListView) findViewById(R.id.listview);
//		tvPayTitle = (TextView) findViewById(R.id.tvPayTitle);
//		tvPayCity = (TextView) findViewById(R.id.tvPayCity);
//		tvPayTicketType = (TextView) findViewById(R.id.tvPayTicketType);
//		ly_share = (LinearLayout) findViewById(R.id.pay_ly_share);
//		RePaymentBtn = (TextView) findViewById(R.id.RePaymentBtn);
//		tvRemainingTimeStr = (TextView) findViewById(R.id.tvRemainingTimeStr);
//
//		RePaymentBtn.setOnClickListener(this);
//		ly_share.setOnClickListener(this);
//		scrollview.smoothScrollTo(0, 0);
//	}
//
//	public void initData() {
//		setTitle("报名成功");
//		tvPayTitle.setText(info.getTitle());
//		tvPayCity.setText(info.getCity() + " | " + info.getStartTime());
//		tvPayTicketType.setText(info.getTicketType());
//		tvRemainingTimeStr.setText(info.getRemainingTimeStr() + "");
//
//		mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
//
//		mAdapter = new PaySuccessAdapter(mContext);
//		list = new ArrayList<HomeEventInfo>();
//		mAdapter.setList(list);
//		mListView.setAdapter(mAdapter);
//		interest();
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				if (Utils.isFastDoubleClick())
//					return;
//				dbManager.insertHistory(list.get(arg2));
//				dbManager.insertSaveBeHavior(application.addBeHavior(10, 0+"", list.get(arg2).getId(), "", "","",""));
//				if (list.get(arg2).getSourceType() == 1) {
//					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 1));
//				} else {
//					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
//				}
//				finish();
//			}
//		});
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		shareUtils = new ShareUtils(mContext, mController, info.getId());
//		shareUtils.initConfig(PaySuccessActivity.this, info.getTitle(), Html.fromHtml(info.getSummary()).toString(), info.getLogo(), info.getShareUrl());
//	}
//
//	public void interest() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", info.getId()));
//		params.add(new BasicNameValuePair("pi", "1"));
//		params.add(new BasicNameValuePair("ps", "4"));
//		showProgress("正在获取数据");
//		httpCilents.get(httpCilents.printURL(Url.PAY_SUCCESS_INTEREST, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
//					if (messageInfo.isSuccess()) {
//						List<HomeEventInfo> list = JSON.parseArray(messageInfo.getResult(), HomeEventInfo.class);
//						mAdapter.addAll(list);
//					} else {
//						application.showMsg(messageInfo.getMsg());
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.RePaymentBtn:
//			startActivity(new Intent(mContext, TicketVolumeDetailsActivity.class).putExtra("id", ticketId));
//			finish();
//			break;
//		case R.id.pay_ly_share:
//			dialogShare();
//			break;
//		case R.id.tvShareSina:// sina share
//			shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 0);
//			shareDialog.dismiss();
//			break;
//		case R.id.tvShareQzone:// Qzone share
//			shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 0);
//			shareDialog.dismiss();
//			break;
//		case R.id.tvSharewx:// weixin share
//			shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 0);
//			shareDialog.dismiss();
//			break;
//		case R.id.tvSharewxpy:// py share
//			shareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE, 34, 0);
//			shareDialog.dismiss();
//			break;
//		case R.id.tvCancel:// dialog cancel
//			shareDialog.dismiss();
//			break;
//		case R.id.lydismiss:
//			shareDialog.dismiss();
//			break;
//		case R.id.tvShareMsg:
//			if (Utils.isFastDoubleClick())
//				return;
//			Uri smsToUri = Uri.parse("smsto:");// 联系人地址
//			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
//			mIntent.putExtra("sms_body", "我刚刚报名参加了：" + info.getTitle() + "," + info.getStartTime() + "，一起去参加吧？记得联系我。" + info.getShareUrl());// 短信内容
//			startActivity(mIntent);
//			shareDialog.dismiss();
//			break;
//		}
//	}
//
//	public void dialogShare() {
//		shareDialog = new Dialog(mContext, R.style.dialog);
//		shareDialog.setCancelable(true);
//		shareDialog.setCanceledOnTouchOutside(true);
//		shareDialog.setContentView(R.layout.dialog_share);
//		TextView shareSina = (TextView) shareDialog.findViewById(R.id.tvShareSina);
//		TextView shareQzone = (TextView) shareDialog.findViewById(R.id.tvShareQzone);
//		TextView shareWx = (TextView) shareDialog.findViewById(R.id.tvSharewx);
//		TextView shareWxpy = (TextView) shareDialog.findViewById(R.id.tvSharewxpy);
//		TextView tvCancel = (TextView) shareDialog.findViewById(R.id.tvCancel);
//		TextView tvShareMsg = (TextView) shareDialog.findViewById(R.id.tvShareMsg);
//		LinearLayout lydismiss = (LinearLayout) shareDialog.findViewById(R.id.lydismiss);
//
//		shareSina.setOnClickListener(this);
//		shareQzone.setOnClickListener(this);
//		shareWx.setOnClickListener(this);
//		shareWxpy.setOnClickListener(this);
//		tvCancel.setOnClickListener(this);
//		lydismiss.setOnClickListener(this);
//		tvShareMsg.setOnClickListener(this);
//		DialogUtils.dialogSet(shareDialog, mContext, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
//		shareDialog.show();
//	}
//
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
//		if (ssoHandler != null) {
//			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//		}
//	}
//
//}
