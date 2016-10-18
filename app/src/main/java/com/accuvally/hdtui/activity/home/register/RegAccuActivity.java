package com.accuvally.hdtui.activity.home.register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.buy.PaySuccessActivity;
import com.accuvally.hdtui.activity.home.buy.PayWebActivity;
import com.accuvally.hdtui.activity.home.buy.SureOrderActivity;
import com.accuvally.hdtui.adapter.TicketAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.DetailsTicketInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

//报名
public class RegAccuActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;

	private AccuDetailBean info;

	private TicketAdapter mAdapter;

	private List<DetailsTicketInfo> list;

	private Dialog codeDialog;

    private Button btPromotionCode;

	private EditText etPromotionCode;

	private String promotionCode;// 输入框优惠码
	private String coupon;
    private boolean displayCoupon=false;//当验证优惠码成功之后，再次打开输入优惠码，显示优惠码

    public static final String ID="RegAccuActivity_ID";
    public static final String CODE="RegAccuActivity_CODE";
    public static final String FROM="RegAccuActivity_FROM";

    private boolean fromLinkedMe=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_accuvally);

        if("LINKEDME".equals(getIntent().getStringExtra(FROM))){
            fromLinkedMe=true;
            String eid=getIntent().getStringExtra(ID);
            promotionCode=getIntent().getStringExtra(CODE);
            initDetails(eid);

        }else {
            fromLinkedMe=false;
            info = (AccuDetailBean) getIntent().getSerializableExtra("info");
            initView();
            initData();
        }
	}


    // 获取详情
    public void initDetails(String homeId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", homeId));
        params.add(new BasicNameValuePair("type", 0 + ""));//isHuodong 0表示活动行的，1表示活动推
//        startProgress();
        showProgress("数据加载中，请稍等");
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_DETAIL, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
//                stopProgress();
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            info = JSON.parseObject(response.result, AccuDetailBean.class);
                            initView();
                            initData();
                            insertCode();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }

    private void insertCode(){
        if (!TextUtils.isEmpty(promotionCode)) {
//            codeDialog.dismiss();
            validateCoupon(promotionCode);
        }
    }




	public void initView() {
		setTitle("报名");

		if (!info.hascoupon) {// 没有优惠码隐藏button
			findViewById(R.id.llCouponCode).setVisibility(View.GONE);
		}

        btPromotionCode=(Button)findViewById(R.id.btPromotionCode);
        btPromotionCode.setOnClickListener(this);

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
                            if (!application.checkIsLogin()) {    //  未登录
                                startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
                            } else {
                                Intent intent = new Intent(mContext, BuyTicketThreeActivity.class);
                                intent.putExtra("info", info);
                                intent.putExtra("position", arg2);
                                if (!TextUtils.isEmpty(coupon)) {
                                    intent.putExtra("coupon", coupon);
                                }
                                startActivity(intent);
                                finish();
                            }

                        } else {// 表单为空
                            if (!application.checkIsLogin()) {    //  未登录
                                startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
                            } else {
                                getDetailsReg(arg2);
                            }

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
                                intent.setClass(RegAccuActivity.this, PaySuccessActivity.class);
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

            if(displayCoupon){
                etPromotionCode.setText(coupon);
            }

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
                            displayCoupon=true;
                            btPromotionCode.setText("优惠码已输入");

//                            btPromotionCode.setClickable(false);
                        } else {
                            application.showMsg(msg.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        displayCoupon=false;
                        application.showMsg(result.toString());
                        break;
                }

            }
        });
	}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(fromLinkedMe&&info!=null){
            mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                    putExtra("id", info.id).putExtra("isHuodong", 0));
        }
    }

        @Override
    public void onBack(View view) {
        finish();
            if(fromLinkedMe&&info!=null){
                mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).
                        putExtra("id", info.id).putExtra("isHuodong", 0));
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
