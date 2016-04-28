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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.TicketAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.DetailsTicketInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

//报名
public class RegAccuActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;

	private AccuDetailBean info;

	private TicketAdapter mAdapter;

	private List<DetailsTicketInfo> list;

	private Dialog codeDialog;

	private EditText etPromotionCode;

	private String promotionCode;// 输入框优惠码
	private String coupon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_accuvally);
		initView();
		initData();
	}

	public void initView() {
		setTitle("报名");
		info = (AccuDetailBean) getIntent().getSerializableExtra("info");
		if (!info.hascoupon) {// 没有优惠码隐藏button
			findViewById(R.id.llCouponCode).setVisibility(View.GONE);
		}
		findViewById(R.id.btPromotionCode).setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (list.get(arg2).isHadReg()) {
					application.showMsg("您已报名此票种，请勿重复报名");
					return;
				}
				if (list.get(arg2).getStatus() == 4) {
					if (list.get(arg2).getMinOrder() > 1 || list.get(arg2).getQuantityUnit() > 1) {
						application.showMsg("购买多张票券，请到活动行网站购买");
					} else {
						if (!TextUtils.isEmpty(info.form)) {// 表单不为空
							Intent intent = new Intent(mContext, BuyTicketThreeActivity.class);
							intent.putExtra("info", info);
							intent.putExtra("position", arg2);
							if (!TextUtils.isEmpty(coupon)) {
								intent.putExtra("coupon", coupon);
							}
							startActivity(intent);
							finish();
						} else {// 表单为空
							getDetailsReg(arg2);
						}
					}

				} else {
					application.showMsg("该票种不可购买");
				}
			}
		});
	}

	public void getDetailsReg(int position) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sn", list.get(position).getSN() + ""));
		params.add(new BasicNameValuePair("id", info.id));
		params.add(new BasicNameValuePair("coupon", coupon));
		showProgress("正在报名");
		httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msg.isSuccess()) {
						// application.showMsg("报名活动成功");
						final RegSuccessInfo successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
						
						if (successInfo.getPrice() > 0) {// 收费票
							try {
								if (!successInfo.isNeedApply()) {
									if (successInfo.getIsAppPay() == 1) {
										startActivity(new Intent(mContext, SureOrderActivity.class).putExtra("info", successInfo).putExtra("DetailsInfo", info).putExtra("tag", 1));
									} else {
										startActivity(new Intent(mContext, PayWebActivity.class).putExtra("payUrl", successInfo.getPayUrl()));
									}
									EventBus.getDefault().post(new ChangePaySuccessEventBus(0));
								} else {
									application.showMsg(successInfo.getMsg());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if (!successInfo.isNeedApply()) {
								application.showMsg("报名活动成功");
								EventBus.getDefault().post(new ChangeDetailsDialogEventBus(successInfo));
							} else {
								application.showMsg(successInfo.getMsg());
							}
							// 免费票
							try {
								String endTimes = TimeUtils.formatter.format(new java.util.Date());
								int results = Utils.timeCompareTo(info.getStartutc(), endTimes);
								if (results > 0)
									if (!dbManager.isAccuDetails(info.id))
										if (application.sharedUtils.readInt("remind") != 4) {
											try {
												TimeUtils.addEvent(mContext, info.getStartutc(), info.title, Html.fromHtml(info.summary).toString(), info.city);
												dbManager.insertAccuDetails(info.id);
											} catch (Exception ex) {
											}
										}
							} catch (Exception e) {
							}

							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putSerializable("DetailsInfo", info);
							bundle.putSerializable("info", successInfo);
							bundle.putBoolean("NeedApply", successInfo.isNeedApply());
							intent.putExtras(bundle);
							intent.setClass(RegAccuActivity.this, PaySuccessActivity2.class);
							startActivity(intent);
						}
						dbManager.insertSaveBeHavior(application.addBeHavior(40, 0 + "", info.id, "", "", "", ""));
						finish();
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

	public void initData() {
		list = info.ticks;
		mAdapter = new TicketAdapter(mContext);
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btPromotionCode:
			showCodeDialog();
			break;
		case R.id.btOk:
			promotionCode = etPromotionCode.getText().toString();
			if (!TextUtils.isEmpty(promotionCode)) {
				codeDialog.dismiss();
				validateCoupon(promotionCode);
			}
			break;

		case R.id.ivGuanbi:
			codeDialog.dismiss();
			break;
		}
	}

	protected void showCodeDialog() {
		if (codeDialog == null) {
			codeDialog = new Dialog(mContext, R.style.DefaultDialog);
			codeDialog.setCancelable(true);
			codeDialog.setCanceledOnTouchOutside(true);
			codeDialog.setContentView(R.layout.dialog_promotion_code);

			etPromotionCode = (EditText) codeDialog.findViewById(R.id.etPromotionCode);
			codeDialog.findViewById(R.id.btOk).setOnClickListener(this);
			codeDialog.findViewById(R.id.ivGuanbi).setOnClickListener(this);
		}
		codeDialog.show();
	}

	private void validateCoupon(final String string) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", info.id));
		params.add(new BasicNameValuePair("coupon", string));
		showProgress("正在校验优惠码");
		httpCilents.postA(Url.ACCUPASS_VALIDATE_COUPON, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
					if (msg.isSuccess()) {
						List<DetailsTicketInfo> couponList = JSON.parseArray(msg.getResult(), DetailsTicketInfo.class);
						mAdapter.setHasCoupon(true);
						mAdapter.getOriginalPrice().clear();
						mAdapter.getOriginalPrice().addAll(mAdapter.getList());
						list.clear();
						mAdapter.addAll(couponList);
						coupon = string;
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
}
