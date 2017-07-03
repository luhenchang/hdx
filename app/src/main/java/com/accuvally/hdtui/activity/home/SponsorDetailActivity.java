package com.accuvally.hdtui.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.adapter.SponsorFragementAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.fragment.SponsorDetailFragment;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SponsorDetailBean;
import com.accuvally.hdtui.model.SponsorDetailOrg;
import com.accuvally.hdtui.ui.PagerSlidingTabStrip;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeAttentionState;
import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主办方详情页面
 * 
 */
public class SponsorDetailActivity extends BaseActivity implements OnClickListener {

	private ViewPager viewpager;
	private PagerSlidingTabStrip tabStrip;

	private List<Fragment> fragments = new ArrayList<Fragment>();

	private String orgId;
	private SponsorDetailBean bean;
	private SponsorDetailOrg orgBean;

	private boolean hasAttention = true;
	private boolean isAttentioning = false;
	private boolean hasRequested = false;
	private LinearLayout share_ly;

	private Dialog shareDialog;
	private ShareUtils shareUtils;
	private UMSocialService mController;
	private ImageView ivHasFollowed;
	private ImageView ivSponsorLogo;
	private View rlSponsorDesc;
	private TextView tvSponsorFollows;
	private int follows;
	private ImageView arrow;
	private TextView tvSponsorDesc;
	protected int lineCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sponsor_detail);

		parseIntent();
		initData();
		initView();
		getListData();
	}

	private void parseIntent() {
		orgId = getIntent().getStringExtra("orgId");
	}

	private void initData() {
		mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
		shareUtils = new ShareUtils(mContext, mController, orgId);
	}

	private void initView() {
		share_ly = (LinearLayout) findViewById(R.id.share_ly);
		share_ly.setOnClickListener(this);

		tvSponsorFollows = (TextView) findViewById(R.id.tvSponsorFollows);
		ivSponsorLogo = (ImageView) findViewById(R.id.ivSponsorLogo);
		ivHasFollowed = (ImageView) findViewById(R.id.ivHasFollowed);
		ivHasFollowed.setOnClickListener(this);

		rlSponsorDesc = findViewById(R.id.rlSponsorDesc);
		rlSponsorDesc.setOnClickListener(this);
		tvSponsorDesc = (TextView) findViewById(R.id.tvSponsorDesc);
		arrow = ((ImageView) findViewById(R.id.ivSponsorDesc));
	}

	public void getListData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", orgId));
//        Trace.e("intent传进来的：",orgId);
		String url = Url.ACCUPASS_ORGANIZER_DETAILS;
		httpCilents.get(httpCilents.printURL(url, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()) {
						bean = JSON.parseObject(response.result, SponsorDetailBean.class);
						orgBean = bean.org;
//                        Trace.e("从后台传进来的：",orgBean.getId());
						shareUtils.initConfig(SponsorDetailActivity.this, orgBean.getName(),
                                orgBean.getDesc(), orgBean.getLogo(), orgBean.getShareUrl());
						setTabsValue();
						updateView();
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}
			}

		});
	}

	private void setTabsValue() {
		fragments.add(SponsorDetailFragment.newInstance(orgId, bean, true));
		fragments.add(SponsorDetailFragment.newInstance(orgId, bean, false));

		tabStrip = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
		viewpager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
		viewpager.setAdapter(new SponsorFragementAdapter(getSupportFragmentManager(), fragments));
		tabStrip.setViewPager(viewpager);
	}

	protected void updateView() {
		hasRequested = true;

		((TextView) findViewById(R.id.tvSponsorName)).setText(orgBean.getName());
		tvSponsorDesc.setText(orgBean.getDesc());

		tvSponsorDesc.post(new Runnable() {

			@Override
			public void run() {
				lineCount = tvSponsorDesc.getLineCount();
				if (lineCount > 1) {
					arrow.setVisibility(View.VISIBLE);
					tvSponsorDesc.setSingleLine(true);
				}
			}
		});

		follows = orgBean.getFollows();
		tvSponsorFollows.setText(follows + "人关注");

		hasAttention = orgBean.isfollow;
		if (orgBean.isfollow) {
			ivHasFollowed.setImageResource(R.drawable.has_attention);
		} else {
			ivHasFollowed.setImageResource(R.drawable.add_attention);
		}

		application.mImageLoader.displayImage(orgBean.getLogo(), ivSponsorLogo, UILoptions.squareOptions);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvShareSina:// sina share
			shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 1);
			shareDialog.dismiss();
			break;
		case R.id.tvShareQzone:// Qzone share
			shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 1);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewx:// weixin share
			shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 1);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewxpy:// py share
			shareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE, 34, 1);
			shareDialog.dismiss();
			break;
		case R.id.tvCancel:// dialog cancel
			shareDialog.dismiss();
			break;
		case R.id.lydismiss:
			shareDialog.dismiss();
			break;
		case R.id.tvShareMsg:
			if (Utils.isFastDoubleClick())
				return;
			Uri smsToUri = Uri.parse("smsto:");// 联系人地址
			Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			mIntent.putExtra("sms_body", "亲~,来看下这个主办方的活动挺有趣：" + orgBean.getName());// 短信内容
			startActivity(mIntent);
			shareDialog.dismiss();
			break;
		case R.id.share_ly:// 分享
			if (Utils.isFastDoubleClick())
				return;
			if (bean != null)
				dialogShare();
			break;

		case R.id.rlSponsorDesc:
			if (lineCount > 1) {
				if (arrow.getVisibility() == View.GONE) {
					tvSponsorDesc.setSingleLine(true);
					arrow.setVisibility(View.VISIBLE);
				} else {
					tvSponsorDesc.setSingleLine(false);
					arrow.setVisibility(View.GONE);
				}
			}
			break;

		case R.id.ivHasFollowed:
			attentionSponsor();
			break;
		}
	}

	public void dialogShare() {
		shareDialog = new Dialog(mContext, R.style.dialog);
		shareDialog.setCancelable(true);
		shareDialog.setCanceledOnTouchOutside(true);
		shareDialog.setContentView(R.layout.dialog_share);
		TextView shareSina = (TextView) shareDialog.findViewById(R.id.tvShareSina);
		TextView shareQzone = (TextView) shareDialog.findViewById(R.id.tvShareQzone);
		TextView shareWx = (TextView) shareDialog.findViewById(R.id.tvSharewx);
		TextView shareWxpy = (TextView) shareDialog.findViewById(R.id.tvSharewxpy);
		TextView tvCancel = (TextView) shareDialog.findViewById(R.id.tvCancel);
		TextView tvShareMsg = (TextView) shareDialog.findViewById(R.id.tvShareMsg);
		LinearLayout lydismiss = (LinearLayout) shareDialog.findViewById(R.id.lydismiss);

		shareSina.setOnClickListener(this);
		shareQzone.setOnClickListener(this);
		shareWx.setOnClickListener(this);
		shareWxpy.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		lydismiss.setOnClickListener(this);
		tvShareMsg.setOnClickListener(this);
		DialogUtils.dialogSet(shareDialog, mContext, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
		shareDialog.show();
	}

	private void attentionSponsor() {
		if (!application.checkIsLogin()) {// 如果未登陆就进入登陆页面
			startActivity(new Intent(mContext, LoginActivityNew.class));
			return;
		}
		if (isAttentioning || !hasRequested)
			return;
		isAttentioning = true;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", orgId));
		String url = "";
		if (hasAttention) {// 已经关注后点击就送取消关注
			url = Url.ORG_UNFOLLOW;
		} else {
			url = Url.ORG_FOLLOW;
		}
		httpCilents.postA(url, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				isAttentioning = false;
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					EventBus.getDefault().post(new ChangeAttentionState());
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						ToastUtil.showMsg(response.msg);
						if (hasAttention) {
							ivHasFollowed.setImageResource(R.drawable.add_attention);
							follows = follows - 1;
							tvSponsorFollows.setText(follows + "人关注");
							dbManager.insertSaveBeHavior(application.addBeHavior(21, 1 + "", orgBean.getId(), "", "", "", ""));
						} else {
							MobclickAgent.onEvent(mContext, "follow_organizer_count");
							ivHasFollowed.setImageResource(R.drawable.has_attention);
							follows = follows + 1;
							tvSponsorFollows.setText(follows + "人关注");
							dbManager.insertSaveBeHavior(application.addBeHavior(20, 1 + "", orgBean.getId(), "", "", "", ""));
						}
						hasAttention = !hasAttention;
					}
				}
			}
		});
	}

}
