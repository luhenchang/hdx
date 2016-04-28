package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.DetailsTicketInfo;
import com.accuvally.hdtui.model.FromInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.ui.DialogShare;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.ShareUtils.shareCallBack;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCollection;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.accuvally.hdtui.utils.eventbus.EventRobSuccess;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

public class IntroductPushActivity extends BaseActivity implements OnClickListener {

	private WebView webView;

	private String content;

	private int isHuodong;

	private boolean isRobTicket;

	private View llEnroll, llRobTicket;

//	private PushDetailsInfo pushDetailsInfo;// 活动推详情
//	private DetailsInfo detailsInfo;// 活动行详情
	private AccuDetailBean detailsInfo;
	private FromInfo fromInfo;

	private TextView tvCheapTicket;
	private TextView tvDetailsRegTicket;
	private TextView tvDetailsColl;
	private ImageView ivDetailsColl;

	private Dialog unRegDialog;//未登录弹出的对话框
	private Dialog regSuccessdialog;//报名成功弹出的对话框
	private Dialog shareSuccessDialog;//分享之后抢票成功弹出的公布名单时间对话框

	private DialogShare dialogShare;

	private String accId;

	private boolean isFollow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduct_push);
		EventBus.getDefault().register(this);
		initProgress();
		initView();
		parseIntent();
	}

	public void initView() {
		setTitle(getResources().getString(R.string.details_Introduction));

		findViewById(R.id.llCollect).setOnClickListener(this);// 收藏
		findViewById(R.id.llShare).setOnClickListener(this);// 分享
		findViewById(R.id.llIsRobTicket).setOnClickListener(this);// 抢票
		tvDetailsRegTicket = (TextView) findViewById(R.id.tvDetailsRegTicket);
		tvDetailsRegTicket.setOnClickListener(this);// 立即报名
		

		tvDetailsColl = (TextView) findViewById(R.id.tvDetailsColl);
		ivDetailsColl = (ImageView) findViewById(R.id.ivDetailsColl);
		
		tvCheapTicket = (TextView) findViewById(R.id.tvCheapTicket);

		setupWebView();
	}

	private void setupWebView() {
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
		webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
		content = getIntent().getStringExtra("content");
		webView.loadDataWithBaseURL("about:blank", content, "text/html", "utf-8", null);
	}

	private void parseIntent() {
		accId = getIntent().getStringExtra("accId");
		isHuodong = getIntent().getIntExtra("isHuodong", 0);
		isRobTicket = getIntent().getBooleanExtra("isRobTicket", false);
		fromInfo = (FromInfo) getIntent().getSerializableExtra("fromInfo");
		Object bean = getIntent().getSerializableExtra("bean");
		detailsInfo = (AccuDetailBean) bean;
		
		if (isHuodong == 1) {// 活动推活动隐藏底部栏
			findViewById(R.id.includeBottom).setVisibility(View.GONE);
			return;
		} else {
			setAccuStatus();
		}
		llEnroll = findViewById(R.id.llEnroll);
		llRobTicket = findViewById(R.id.llIsRobTicket);
		setIsRobTicket();
		setCollectState();
	}

	private void setAccuStatus() {
		switch (detailsInfo.status) {
		case 0:
			setStatus("未开始");
			break;
		case 1:
			setStatus("已截止");
			break;
		case 2:
			setStatus("名额已满");
			break;
		case 3:
			setStatus("已举办");
			break;
		case 5:
			setStatus("已取消");
			break;
		}
	}
	
	private void setIsRobTicket() {
		if (isRobTicket) {// 是抢票的活动
			llEnroll.setVisibility(View.GONE);
			llRobTicket.setVisibility(View.VISIBLE);

			findViewById(R.id.llCollect).setVisibility(View.GONE);

			String statusStr = getIntent().getStringExtra("StatusStr");
			tvCheapTicket.setText(statusStr);
			if (isRobGary(statusStr)) {
				llRobTicket.setBackgroundColor(0xffbdbdbd);
				llRobTicket.setClickable(false);
			}

			if (detailsInfo != null) {
				List<DetailsTicketInfo> ticks = detailsInfo.ticks;
				if (!ticks.isEmpty() && ticks.get(0).isHadReg()) {// 已抢票报名过了
					tvCheapTicket.setText("已报名");
					llRobTicket.setBackgroundColor(0xffbdbdbd);
					llRobTicket.setClickable(false);
				}
			}

		} else {
			llEnroll.setVisibility(View.VISIBLE);
			llRobTicket.setVisibility(View.GONE);// 隐藏抢特价票按钮,默认显示
		}
	}
	
	private boolean isRobGary(String statusStr) {
		return "即将开始".equals(statusStr) || "已抢完".equals(statusStr) || "已结束".equals(statusStr);
	}

	private void setCollectState() {
		isFollow = detailsInfo.isfollow;
		if (detailsInfo.isfollow) {
			ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
			tvDetailsColl.setText("已收藏");
		} else {
			ivDetailsColl.setImageResource(R.drawable.shoucang);
			tvDetailsColl.setText("收藏");
		}
	}
	
	public void setStatus(String status) {
		tvDetailsRegTicket.setText(status);
		tvDetailsRegTicket.setClickable(false);
		tvDetailsRegTicket.setBackgroundResource(R.drawable.gary_bg_btn);
	}

	@Override
	public void onClick(View v) {
		if (Utils.isFastDoubleClick()) return;

		switch (v.getId()) {
		case R.id.llCollect:// 收藏
			if (application.checkIsLogin()) {
				if (detailsInfo != null) {
					if (detailsInfo.isfollow)
						getUnCollection();
					else
						getCollection();
				}
			} else {
				startActivity(new Intent(mContext, LoginActivityNew.class));
			}
			break;

		case R.id.llIsRobTicket:// 抢特价票也要先分享
			if (!application.checkIsLogin()) {
				toActivity(LoginActivityNew.class);
				return;
			}
			if (detailsInfo != null) {
				showDialogShare(detailsInfo.title, detailsInfo.getStartutc(), detailsInfo.shareurl);
			}
			break;
		case R.id.llShare:// 分享
//			if (!NetworkUtils.isNetworkAvailable(mContext)) {
//				application.showMsg(R.string.network_check);
//				return;
//			}
//			if (detailsInfo != null) {
//				showDialogShare(detailsInfo.title, detailsInfo.getStartutc(), detailsInfo.shareurl);
//			}
			if (!application.checkIsLogin()) {
				toActivity(LoginActivityNew.class);
				return;
			}
			
			if (isHuodong == 0 && detailsInfo != null) {
				SessionInfo session = SessionTable.querySessionById(accId);
				if (session == null) {
					joinCircle(detailsInfo.id);
				} else {
					application.setCurrentSession(session);
					startActivity(new Intent(mContext, ChatActivity.class));
				}
			}
			break;

		case R.id.tvDetailsRegTicket:// 立即报名
			if (isHuodong == 1) {
				if (detailsInfo != null) {
					application.showMsg("该活动不提供报名");
				}
			} else {
				if (detailsInfo != null) {
					if (!application.checkIsLogin()) {// 没有登入,弹出对话框
						UnRegDialog();
						return;
					}
					regTicket();
				}
			}
			break;
		}
	}
	
	// 立即报名
	public void regTicket() {
		if (!isFile()) {
			if (application.checkIsLogin()) {
				if (application.getUserInfo().isEmailActivated() || application.getUserInfo().isPhoneActivated()) {
					if (detailsInfo.ticks.size() == 0) {
						if (!TextUtils.isEmpty(detailsInfo.form)) {
							// 表单不为空
							Intent intent = new Intent(mContext, BuyTicketThreeActivity.class);
							intent.putExtra("info", detailsInfo);
							intent.putExtra("isRobTicket", isRobTicket);
							startActivity(intent);
						} else {
							// 表单为空,直接报名
							getDetailsReg();
						}
					} else {
						startActivity(new Intent(mContext, RegAccuActivity.class).putExtra("info", detailsInfo));
					}
				} else {
					startActivity(new Intent(mContext, BindPhoneActivity.class).putExtra("TAG", 1));
				}
			} else {
				startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
			}
		} else {
			application.showMsg("报名此活动需要上传附件，请到活动行网站购买");
		}
	}
	
	// 立即报名抢票
	public void regRobTicket() {
		if (!TextUtils.isEmpty(detailsInfo.form)) {// 表单不为空
			Intent intent = new Intent(mContext, BuyTicketThreeActivity.class);
			intent.putExtra("info", detailsInfo);
			intent.putExtra("isRobTicket", isRobTicket);
			startActivity(intent);
		} else {
			shareCompleteReg();// 分享成功之后调用报名接口，报名第一个票券
		}
	}
	
	//是否需要上传附件
	public boolean isFile() {
		boolean isFile = false;
		if (!TextUtils.isEmpty(detailsInfo.form) && fromInfo != null) {
			for (int i = 0; i < fromInfo.items.size(); i++) {
				if (fromInfo.items.get(i).getType().contains("file")) {
					isFile = true;
				}
			}
		}
		return isFile;
	}

	public void UnRegDialog() {
		if (unRegDialog == null) {
			unRegDialog = new Dialog(mContext, R.style.dialog);
			unRegDialog.setCancelable(true);
			unRegDialog.setCanceledOnTouchOutside(false);
			unRegDialog.setContentView(R.layout.dialog_unreg);
			unRegDialog.findViewById(R.id.btLogin).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unRegDialog.dismiss();
					startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
				}
			});
		}
		unRegDialog.show();
	}

	private void getCollection() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", detailsInfo.id));
		params.add(new BasicNameValuePair("source", isHuodong + ""));
		startProgress();
		httpCilents.postA(Url.ACCUPASS_HOME_FOLLOW, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						detailsInfo.isfollow = true;
						ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
						tvDetailsColl.setText("已收藏");
						dbManager.insertSaveBeHavior(application.addBeHavior(20, 0 + "", detailsInfo.id, "", "", "", ""));
						application.showMsg("收藏成功");
						
						EventBus.getDefault().post(new EventCollection(EventCollection.favorite));
					} else {
						application.showMsg(response.msg);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}

	public void getUnCollection() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", detailsInfo.id));
		params.add(new BasicNameValuePair("source", isHuodong + ""));
		startProgress();
		httpCilents.postA(Url.ACCUPASS_HOME_UNFOLLOW, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						detailsInfo.isfollow = false;
						ivDetailsColl.setImageResource(R.drawable.shoucang);
						tvDetailsColl.setText("收藏");
						dbManager.insertSaveBeHavior(application.addBeHavior(21, 0 + "", detailsInfo.id, "", "", "", ""));
						
						EventBus.getDefault().post(new EventCollection(EventCollection.unfavorite));
						application.showMsg("取消收藏成功");
					} else {
						application.showMsg(response.msg);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
	
	public void getDetailsReg() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Form", ""));
		params.add(new BasicNameValuePair("SN", ""));
		params.add(new BasicNameValuePair("id", detailsInfo.id));
		showProgress("正在报名");
		httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msg.isSuccess()) {
						RegSuccessInfo successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
						Log.i("info", result.toString());
						if (successInfo.getPrice() > 0) {
							// 收费票
							try {
								if (!successInfo.isNeedApply()) {
									application.showMsg("报名活动成功");
									if (successInfo.getIsAppPay() == 1) {
										startActivity(new Intent(mContext, SureOrderActivity.class).putExtra("info", successInfo).putExtra("tag", 1));
									} else {
										startActivity(new Intent(mContext, PayWebActivity.class).putExtra("payUrl", successInfo.getPayUrl()));
									}
								} else {
									application.showMsg(successInfo.getMsg());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if (!successInfo.isNeedApply()) {
								application.showMsg("报名活动成功");
								RegSuccessDialog();
							} else {
								application.showMsg(successInfo.getMsg());
							}
							// 免费票
							try {
								String endTimes = TimeUtils.formatter.format(new java.util.Date());
								int results = Utils.timeCompareTo(detailsInfo.getStartutc(), endTimes);
								if (results > 0)
									if (!dbManager.isAccuDetails(detailsInfo.id))
										if (application.sharedUtils.readInt("remind") != 4) {
											try {
												TimeUtils.addEvent(mContext, detailsInfo.getStartutc(), detailsInfo.title, Html.fromHtml(detailsInfo.summary).toString(), detailsInfo.city);
												dbManager.insertAccuDetails(detailsInfo.id);
											} catch (Exception ex) {
											}
										}
							} catch (Exception e) {
							}
						}
						EventBus.getDefault().post(new EventEnroll());
						dbManager.insertSaveBeHavior(application.addBeHavior(40, 0 + "", detailsInfo.id, "", "", "", ""));
					} else {
						application.showMsg(msg.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}

		});
	}
	
	public void RegSuccessDialog() {
		regSuccessdialog = new Dialog(mContext, R.style.DefaultDialog);
		regSuccessdialog.setCancelable(true);
		regSuccessdialog.setCanceledOnTouchOutside(false);
		regSuccessdialog.setContentView(R.layout.dialog_collect);
		((TextView) regSuccessdialog.findViewById(R.id.title)).setText("报名成功咯~");
		((TextView) regSuccessdialog.findViewById(R.id.message)).setText("活动行小助手会帮您保管好票券的哟！");
		((TextView) regSuccessdialog.findViewById(R.id.tvDialogMistake)).setText("查看票券");
		((TextView) regSuccessdialog.findViewById(R.id.tvDialogRemove)).setText("分享");
		regSuccessdialog.findViewById(R.id.tvDialogMistake).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				regSuccessdialog.dismiss();
				Intent intent = new Intent(mContext, TicketVolumeActivity.class);
				startActivity(intent.putExtra("id", detailsInfo.id).putExtra("isDetails", 1).putExtra("SourceType", 0));
			}
		});
		regSuccessdialog.findViewById(R.id.tvDialogRemove).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				regSuccessdialog.dismiss();
				if (detailsInfo != null) {
					showDialogShare(detailsInfo.title, detailsInfo.getStartutc(), detailsInfo.shareurl);
				}
			}
		});
		regSuccessdialog.show();
	}
	
	private void showDialogShare(String shareTitle, String startTime, String shareUrl) {
		if (dialogShare == null) {
			dialogShare = new DialogShare(this, accId, shareTitle, startTime, shareUrl);
			dialogShare.setRobTicket(isRobTicket);
			if (isRobTicket) {
				dialogShare.setShareComplete(new shareCallBack() {
					
					@Override
					public void shareSuccess() {
						regRobTicket();//立即报名，验证有没有表单
					}
				});
			}
		}
		dialogShare.showDialog();
	}

//	private void setFollowResult() {
//		if (isFollow != detailsInfo.isfollow) {
//			Intent data = new Intent();
//			data.putExtra("isFollow", detailsInfo.isfollow);
//			setResult(2015, data);
//		}
//	}
	
	//分享成功之后调用报名接口，用第一个票券报名
	public void shareCompleteReg() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Form", ""));
		try {
			params.add(new BasicNameValuePair("SN", detailsInfo.ticks.get(0).getSN() + ""));
		} catch (Exception e) {
			e.printStackTrace();
			application.showMsg("数据有误");
			return;
		}
		params.add(new BasicNameValuePair("id", detailsInfo.id));
//		showProgress("正在报名");
		httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
//				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msg.isSuccess()) {
						RegSuccessInfo successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
						if (successInfo.getPrice() > 0) {// 收费票
							try {
								if (!successInfo.isNeedApply()) {// 不需要审核
									if (successInfo.getIsAppPay() == 1) {
										Intent intent = new Intent(mContext, SureOrderActivity.class);
										intent.putExtra("info", successInfo);
										intent.putExtra("DetailsInfo", detailsInfo);
										intent.putExtra("tag", 1);
										startActivity(intent);
									} else {
										startActivity(new Intent(mContext, PayWebActivity.class).putExtra("payUrl", successInfo.getPayUrl()));
									}
								} else {// 需要审核
									application.showMsg(successInfo.getMsg());// 报名成功，请等待审核！
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {// 免费票
							dialogShareSuccess();
						}
						EventBus.getDefault().post(new EventEnroll());
						dbManager.insertSaveBeHavior(application.addBeHavior(40, 0 + "", detailsInfo.id, "", "", "", ""));
					} else {
						application.showMsg(msg.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}
		});
	}
	
	private void dialogShareSuccess() {
		if (shareSuccessDialog == null) {
			shareSuccessDialog = new Dialog(mContext, R.style.dialog);
			shareSuccessDialog.setCancelable(true);
			shareSuccessDialog.setCanceledOnTouchOutside(true);
			shareSuccessDialog.setContentView(R.layout.dialog_share_success);
		}
		shareSuccessDialog.show();
	}
	
	//抢票成功弹出
	public void onEventMainThread(EventRobSuccess eventBus) {
		dialogShareSuccess();
	}
	
	public void joinCircle(final String sessionId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("eid", sessionId));
		startProgress();
		httpCilents.postA(Url.ACCUPASS_EVENT_CIRCLE, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						application.showMsg("加入圈子成功");
						SessionInfo sessionInfo = new SessionInfo();
						sessionInfo.setSessionId(sessionId);
						sessionInfo.setTitle(detailsInfo.title);
						sessionInfo.setLogoUrl(detailsInfo.logo);
						sessionInfo.setTime(System.currentTimeMillis());
						
						SessionTable.insertSession(sessionInfo);
						EventBus.getDefault().post(new ChangeMessageEventBus(1));
						application.setCurrentSession(sessionInfo);
						startActivity(new Intent(mContext, ChatActivity.class));
					} else {
						application.showMsg(response.getMsg());
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
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
}
