package com.accuvally.hdtui.activity.home;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.buy.PaySuccessActivity;
import com.accuvally.hdtui.activity.home.buy.PayWebActivity;
import com.accuvally.hdtui.activity.home.buy.SureOrderActivity;
import com.accuvally.hdtui.activity.home.register.BindPhoneActivity;
import com.accuvally.hdtui.activity.home.register.BuyTicketFirstActivity;
import com.accuvally.hdtui.activity.home.register.BuyTicketThreeActivity;
import com.accuvally.hdtui.activity.home.register.RegAccuActivity;
import com.accuvally.hdtui.activity.home.util.MapsActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.message.user.UserDetailActivity;
import com.accuvally.hdtui.activity.mine.TicketVolumeActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.AccuBean;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.DetailsTicketInfo;
import com.accuvally.hdtui.model.FromInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.ui.OverScrollView;
import com.accuvally.hdtui.ui.pulltoloaddetail.PullLayout;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsRegEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCollection;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.accuvally.hdtui.utils.eventbus.EventRobSuccess;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Header's root is ScrollView , Footer's root is ScrollView
 *
 * @author zy
 *
 */
@SuppressWarnings("unused")
public class AccuvallyDetailsActivity extends BaseActivity implements View.OnClickListener , PullLayout.OnPullListener,
        PullLayout.OnPageChangedListener,OverScrollView.OverScrollListener {
    private PullLayout mLayout;
    private ScrollView mHeader, mFooter;
    private LinearLayout mHeaderContent, mFooterContent;


    private String homeId;

    private int isHuodong;//isHuodong 0表示活动行的，1表示活动推

//	private PushDetailsInfo pushDetailsInfo;//活动推详情
//	private DetailsInfo detailsInfo;//活动行详情

    private AccuDetailBean detailsInfo;

    private FromInfo fromInfo;

    private ImageView ivDetailsLogo, iv_org_logo, ivDetailsColl;

    private TextView tvDetailsTitle, tvDetailsTime, tvDetailsAddress, tvDetailsTicket, tvContactOrganizer, tvDetailsRegTicket;

    private TextView tvDetailsOrgName,   tvDetailsColl, tvDetailsSell, tvVisitNum, tvLikeNum;//tvIntroduction,tv_accu_brief,

    private TextView tvCheapTicket;

    private LinearLayout lyDetailsAddr, lyDetailsOrg, share_ly;

        	private OverScrollView scrollview;  //123
//    private ScrollView scrollview;

    private Dialog shareDialog, unRegDialog, shareSuccessDialog;

    private UMSocialService mController;

    private Dialog dialog;

    private View vPriceLine;

    ShareUtils shareUtils;

    private View viewOrgLine;

    private boolean isRobTicket;

    private View llRobTicket;// 抢票
    private View llEnroll;// 立即报名

    private View include_accuvallydetail_bottom;

    private RegSuccessInfo successInfo;

    private String statusStr;

    private boolean isStartBuyTicketThree;
    private ImageView ivCertification;

    /* @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_accuvally_details);
         initScroll();

     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuvally_details);
        initScroll();
        EventBus.getDefault().register(this);
        initProgress();
        initView();
        initData();
        initDetails();
    }

    public void initView() {
        setTitle("活动详情");
        share_ly = (LinearLayout) findViewById(R.id.share_ly);
        ivDetailsLogo = (ImageView) findViewById(R.id.ivDetailsLogo);
        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        iv_org_logo = (ImageView) findViewById(R.id.iv_org_logo);

        tvDetailsTime = (TextView) findViewById(R.id.tvDetailsTime);
        tvDetailsAddress = (TextView) findViewById(R.id.tvDetailsAddress);
        tvDetailsTicket = (TextView) findViewById(R.id.tvDetailsTicket);
        tvContactOrganizer = (TextView) findViewById(R.id.tvContactOrganizer);

        tvDetailsOrgName = (TextView) findViewById(R.id.tvDetailsOrgName);
        ivCertification = (ImageView) findViewById(R.id.ivCertification);

//        tv_accu_brief = (TextView) findViewById(R.id.tv_accu_brief);
//        tvIntroduction = (TextView) findViewById(R.id.tvIntroduction);

        tvDetailsColl = (TextView) findViewById(R.id.tvDetailsColl);
        ivDetailsColl = (ImageView) findViewById(R.id.ivDetailsColl);
        tvDetailsRegTicket = (TextView) findViewById(R.id.tvDetailsRegTicket);
        lyDetailsAddr = (LinearLayout) findViewById(R.id.lyDetailsAddr);
        lyDetailsOrg = (LinearLayout) findViewById(R.id.lyDetailsOrg);
        tvDetailsSell = (TextView) findViewById(R.id.tvDetailsSell);
        tvVisitNum = (TextView) findViewById(R.id.tvVisitNum);
        tvLikeNum = (TextView) findViewById(R.id.tvLikeNum);

                scrollview = (OverScrollView) findViewById(R.id.header);
//		scrollview = (OverScrollView) findViewById(R.id.scrollview); // 123
//        scrollview = (ScrollView) findViewById(R.id.scrollview);
        viewOrgLine = findViewById(R.id.viewOrgLine);
        vPriceLine = findViewById(R.id.vPriceLine);
        tvCheapTicket = (TextView) findViewById(R.id.tvCheapTicket);

        include_accuvallydetail_bottom = findViewById(R.id.include_accuvallydetail_bottom);

        share_ly.setOnClickListener(this);
//        tvIntroduction.setOnClickListener(this);
//        tv_accu_brief.setOnClickListener(this);

        tvDetailsColl.setOnClickListener(this);
        tvDetailsRegTicket.setOnClickListener(this);
        lyDetailsAddr.setOnClickListener(this);

        lyDetailsAddr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copy(detailsInfo.address,AccuvallyDetailsActivity.this);
                return true;
            }
        });
        lyDetailsOrg.setOnClickListener(this);
		scrollview.setOverScrollListener(this);  // 123

        tvDetailsTicket.setOnClickListener(this);
        tvContactOrganizer.setOnClickListener(this);

        findViewById(R.id.llCollect).setOnClickListener(this);// 收藏
        findViewById(R.id.llShare).setOnClickListener(this);// 群聊
        findViewById(R.id.llIsRobTicket).setOnClickListener(this);// 抢票

        parseIntent();
    }


    @SuppressWarnings("deprecation")
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        Toast.makeText(context,"地址已复制到剪切板上",Toast.LENGTH_SHORT).show();

    }

    private void setInterestAccu() {
        if (detailsInfo == null || detailsInfo.interestacts == null)
            return;
        setupWebView();
        if (detailsInfo.interestacts.isEmpty()) {
            findViewById(R.id.includeInterest).setVisibility(View.GONE);
        } else {
            findViewById(R.id.includeInterest).setVisibility(View.VISIBLE);
            LinearLayout hsvChildll = (LinearLayout) findViewById(R.id.hsvChildll);
            hsvChildll.removeAllViews();
            for (int i = 0; i < detailsInfo.interestacts.size(); i++) {
                View itemView = getLayoutInflater().inflate(R.layout.listitem_home_recommend, null);
                hsvChildll.addView(itemView);

                final AccuBean accuBean = detailsInfo.interestacts.get(i);
                ImageView ivItemRecommendImg = (ImageView) itemView.findViewById(R.id.ivItemRecommendImg);
                ImageLoader.getInstance().displayImage(accuBean.logo, ivItemRecommendImg);

                TextView tvItemTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
                TextView tvItemTime = (TextView) itemView.findViewById(R.id.tvItemTime);
                TextView tvItemAddress = (TextView) itemView.findViewById(R.id.tvItemAddress);
                TextView tvItemVisitNum = (TextView) itemView.findViewById(R.id.tvItemVisitNum);
                TextView tvItemPriceArea = (TextView) itemView.findViewById(R.id.tvItemPriceArea);

                tvItemTitle.setText(accuBean.title);
                tvItemTime.setText(accuBean.getTimeStr());
                tvItemAddress.setText(accuBean.address);
                tvItemVisitNum.setText(accuBean.visitnum);
                tvItemPriceArea.setText(accuBean.pricearea);

                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        MobclickAgent.onEvent(mContext, "click_event_guesslike_count");
                        dbManager.insertSaveBeHavior(application.addBeHavior(10, 0 + "", accuBean.id, "", "", "", ""));

                        Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
                        intent.putExtra("id", accuBean.id);
                        intent.putExtra("isHuodong", accuBean.sourcetype);
                        intent.putExtra("isSameStart", true);
                        startActivity(intent);
                        if (getIntent().getBooleanExtra("isSameStart", false)) {
                            finish();
                        }
                    }
                });
            }
        }
    }

    private void parseIntent() {
        homeId = getIntent().getStringExtra("id");
        isHuodong = getIntent().getIntExtra("isHuodong", 0);
        isRobTicket = getIntent().getBooleanExtra("isRobTicket", false);
        llEnroll = findViewById(R.id.llEnroll);
        llRobTicket = findViewById(R.id.llIsRobTicket);
//		setIsRobTicket();
    }

    private void setIsRobTicket() {
        if (isRobTicket) {// 是抢票的活动隐藏收藏和分享
            llEnroll.setVisibility(View.GONE);
            llRobTicket.setVisibility(View.VISIBLE);
            share_ly.setVisibility(View.GONE);// 抢票活动先隐藏圈子

            findViewById(R.id.llCollect).setVisibility(View.GONE);

            tvDetailsTicket.setClickable(false);// 抢票详情价格不能点击,右边的箭头也去掉
            tvDetailsTicket.setCompoundDrawablesWithIntrinsicBounds(R.drawable.details_ticket_icon_bg, 0, 0, 0);

            if (detailsInfo != null) {
                statusStr = detailsInfo.statusstr;
                tvCheapTicket.setText(statusStr);
                if (isRobGary()) {
                    llRobTicket.setBackgroundColor(0xffbdbdbd);
                    llRobTicket.setClickable(false);
                }
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
            share_ly.setVisibility(View.VISIBLE);
        }
    }

    private boolean isRobGary() {
        return "即将开始".equals(statusStr) || "已抢完".equals(statusStr) || "已结束".equals(statusStr);
    }

    public void initData() {
        mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
        shareUtils = new ShareUtils(mContext, mController, homeId);
    }

    // 正点闹钟
//	public void refresh() {
//		ClockData data = new ClockData();
//		if (TextUtils.isEmpty(detailsInfo.logo)) {
//			data.bgURL = "http://cdn.huodongxing.com/Content/v2.0/img/default_details_image.png";
//		} else {
//			data.bgURL = detailsInfo.logo;
//		}
//		if (detailsInfo.id.length() > 6)
//			data.clockId = Integer.parseInt(detailsInfo.id.substring(detailsInfo.id.length() - 6, detailsInfo.id.length()));
//		else
//			data.clockId = Integer.parseInt(detailsInfo.id);
//		data.iconURL = "http://www.huodongxing.com/Content/v2.0/img/icon-holder.png";
//		data.linkAppPackage = "com.accuvally.hdtui";
//		data.linkAppURI = "hdx://com.accuvally.hdtuix_detail?eid=" + detailsInfo.id;
//		data.linkTitle = "活动详情";
//		data.linkURL = detailsInfo.shareurl;
//		data.note = detailsInfo.title;
//		data.startTime = (TimeUtils.getStartTime(detailsInfo.getStartutc()));
//		data.title = "活动提醒";
//		data.type = ClockData.LoopType.ONCE;
//		CheckResult checkResult = data.checkAvailableAndGetError();
//		if (checkResult.isAvailable) {
//			clockButton.setup(Config.ACCUPASS_ZD_CLOCK_APP_KEY, Config.ACCUPASS_ZD_CLOCK_APP_SECRET, data);
//		} else {
//			application.showMsg(checkResult.message);
//		}
//	}

    // 获取详情
    public void initDetails() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", homeId));
        params.add(new BasicNameValuePair("type", isHuodong + ""));//isHuodong 0表示活动行的，1表示活动推
        startProgress();
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_DETAIL, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            //delete unused
                            detailsInfo = JSON.parseObject(response.result, AccuDetailBean.class);
                            Trace.e("AccuvallyDetailsActivity","detailsInfo.isvip:"+detailsInfo.isvip);
                            if(isHuodong == 1) {
                                initJelly();
                            }else {
                                initAccuvally();
                                if (isRobTicket) {// 分享成功监听 设置公布名单时间
                                    shareUtils.setShareSuccessListener(new ShareUtils.shareCallBack() {

                                        @Override
                                        public void shareSuccess() {
                                            regRobTicket();//立即报名，验证有没有表单
                                        }
                                    });
                                }

                                setInterestAccu();
                            }
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }

    // 活动行详情
    public void initAccuvally() {
        if (detailsInfo.isRush) {
            share_ly.setVisibility(View.GONE);
        } else {
            share_ly.setVisibility(View.VISIBLE);
        }

        application.mImageLoader.displayImage(detailsInfo.logo, ivDetailsLogo);
        tvDetailsTitle.setText(detailsInfo.title);
        tvDetailsTime.setText(detailsInfo.getTimeStr());
        tvDetailsAddress.setText(detailsInfo.address);
        if (detailsInfo.maxprice > 0)
            tvDetailsTicket.setText("￥" + detailsInfo.minprice + " - ￥" + detailsInfo.maxprice);
        else
            tvDetailsTicket.setText("免费");
//        String content = Util.stringFilter(Html.fromHtml(detailsInfo.summary).toString());
//        tv_accu_brief.setText(content);
        if (detailsInfo.showregisternum) {
            tvDetailsSell.setVisibility(View.VISIBLE);
            tvDetailsSell.setText("已售" + detailsInfo.regnum + "张");
        } else {
            tvDetailsSell.setVisibility(View.GONE);
        }
        tvVisitNum.setText("浏览" + detailsInfo.visitnum + "次");
        tvLikeNum.setText("收藏" + detailsInfo.likenum + "次");

        if(detailsInfo.isvip){
            ivCertification.setBackgroundResource(R.drawable.icon_vip);
        }else {
            if (detailsInfo.orgstatus == 1) {
                ivCertification.setBackgroundResource(R.drawable.v_certification);
            } else {
                ivCertification.setVisibility(View.GONE);
            }
        }


        if (!"0".equals(detailsInfo.orgid)) {
//			String str = detailsInfo.orgname + "主办";
            String str = detailsInfo.orgname;
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gary_title)), detailsInfo.orgname.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvDetailsOrgName.setText(style);
            application.mImageLoader.displayImage(detailsInfo.orglogo, iv_org_logo, UILoptions.squareOptions);
        } else {
//			String str = detailsInfo.creator + "发布";
            String str = detailsInfo.creator;
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gary_title)), detailsInfo.creator.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvDetailsOrgName.setText(style);
            application.mImageLoader.displayImage(detailsInfo.creatorlogo, iv_org_logo, UILoptions.squareOptions);
        }

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
            default:
                tvDetailsRegTicket.setText("立即报名");
                tvDetailsRegTicket.setClickable(true);
                tvDetailsRegTicket.setBackgroundResource(R.drawable.selector_click_btn);
                tvDetailsTicket.setClickable(true);
                break;
        }

        if (detailsInfo.isfollow) {
            ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
            tvDetailsColl.setText("已收藏");
        } else {
            ivDetailsColl.setImageResource(R.drawable.shoucang);
            tvDetailsColl.setText("收藏");
        }

        if (!TextUtils.isEmpty(detailsInfo.form)) {
            fromInfo = JSON.parseObject(detailsInfo.form, FromInfo.class);
        }

        if (detailsInfo.status == 3) {
            boolean visble = false;
            for (int i = 0; i < detailsInfo.ticks.size(); i++) {
                if (detailsInfo.ticks.get(i).isHadReg()) {
                    visble = true;
                    break;
                }
            }
            if (visble) {
                share_ly.setVisibility(View.VISIBLE);
            } else {
                share_ly.setVisibility(View.GONE);
            }
        }

        isRobTicket = detailsInfo.isRush;// 是否是抢票
        setIsRobTicket();

//		refresh();
        shareUtils.initConfig(this, detailsInfo.title, Html.fromHtml(detailsInfo.summary).toString(), detailsInfo.logo, detailsInfo.shareurl);
    }

    public void setStatus(String status) {
        tvDetailsRegTicket.setText(status);
        tvDetailsRegTicket.setClickable(false);
        tvDetailsRegTicket.setBackgroundResource(R.drawable.gary_bg_btn);
        tvDetailsTicket.setClickable(false);
    }

    // 活动推详情
    public void initJelly() {
        application.mImageLoader.displayImage(detailsInfo.logo, ivDetailsLogo);
        tvDetailsTitle.setText(detailsInfo.title);
        tvDetailsTime.setText(detailsInfo.getStartutc() + " - " + detailsInfo.getEndutc());
        tvDetailsAddress.setText(detailsInfo.address.replace(" ", "").replace("\n", "").trim());
        tvDetailsTicket.setVisibility(View.GONE);
        vPriceLine.setVisibility(View.GONE);
        viewOrgLine.setVisibility(View.GONE);
        if (detailsInfo.maxprice > 0)
            tvDetailsTicket.setText("￥" + detailsInfo.minprice + " - ￥" + detailsInfo.maxprice);
        else
            tvDetailsTicket.setText("免费");
//        tv_accu_brief.setText(Util.stringFilter(Html.fromHtml(detailsInfo.summary).toString()));
        String str = "活动行整理";
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gary_title)), 3, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDetailsOrgName.setText(style);
        iv_org_logo.setBackgroundResource(R.drawable.icon);
        tvDetailsRegTicket.setText("参加");
        tvVisitNum.setVisibility(View.GONE);
        if (detailsInfo.isfollow) {
            ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
            tvDetailsColl.setText("已收藏");
        } else {
            ivDetailsColl.setImageResource(R.drawable.shoucang);
            tvDetailsColl.setText("收藏");
        }
//		clockButton.setVisibility(View.GONE);
        shareUtils.initConfig(this, detailsInfo.title, Html.fromHtml(detailsInfo.summary).toString(), detailsInfo.logo, detailsInfo.shareurl);

        include_accuvallydetail_bottom.setVisibility(View.GONE);
        share_ly.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tvShareSina:// sina share
                if (!NetworkUtils.isNetworkAvailable(mContext)) {
                    application.showMsg(R.string.network_check);
                    return;
                }
                shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 0);
                shareDialog.dismiss();
                break;
            case R.id.tvShareQzone:// Qzone share
                if (!NetworkUtils.isNetworkAvailable(mContext)) {
                    application.showMsg(R.string.network_check);
                    return;
                }
                shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 0);
                shareDialog.dismiss();
                break;
            case R.id.tvSharewx:// weixin share
                if (!NetworkUtils.isNetworkAvailable(mContext)) {
                    application.showMsg(R.string.network_check);
                    return;
                }
                shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 0);
                shareDialog.dismiss();
                break;
            case R.id.tvSharewxpy:// py share
                if (!NetworkUtils.isNetworkAvailable(mContext)) {
                    application.showMsg(R.string.network_check);
                    return;
                }
                shareUtils.share(SHARE_MEDIA.WEIXIN_CIRCLE, 34, 0);
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
                String smsContent = "我在活动行APP上发现了一个好活动：" + detailsInfo.title + "," + detailsInfo.getStartutc() + "，一起去参加吧？记得联系我。" + detailsInfo.shareurl;
                mIntent.putExtra("sms_body", smsContent);// 短信内容
                startActivity(mIntent);
                shareDialog.dismiss();
                break;
            case R.id.llIsRobTicket:// 抢特价票也要先分享
                if (!application.checkIsLogin()) {
                    UnRegDialog();
                    return;
                }
                if (detailsInfo != null) {
                    dialogShare();
                }
                break;
            case R.id.share_ly:// 分享
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_event_share_count");
                share();
                break;
//            case R.id.tv_accu_brief:// 简介
//            case R.id.tvIntroduction:// 更多详情
//                if (Utils.isFastDoubleClick())
//                    return;
//                if (detailsInfo != null) {
//                    Intent intent = new Intent(mContext, IntroductPushActivity.class);
//                    intent.putExtra("content", detailsInfo.desc);
//                    intent.putExtra("isHuodong", isHuodong);
//                    intent.putExtra("accId", detailsInfo.id);
//                    intent.putExtra("bean", detailsInfo);
//                    intent.putExtra("fromInfo", fromInfo);
//                    intent.putExtra("isRobTicket", detailsInfo.isRush);//只有活动行才有抢票
//                    intent.putExtra("StatusStr", statusStr);
//                    startActivity(intent);
//                }
//                break;
            case R.id.llCollect:// 收藏
                if (Utils.isFastDoubleClick())
                    return;
                MobclickAgent.onEvent(mContext, "click_event_favorite_count");
                if (application.checkIsLogin()) {
                    if (detailsInfo != null)
                        if (detailsInfo.isfollow)
                            getUnCollection();
                        else
                            getCollection();
                } else {
                    startActivity(new Intent(mContext, LoginActivityNew.class));
                }
                break;
            case R.id.tvDetailsTicket:// 免费|收费栏 点击报名
            case R.id.tvDetailsRegTicket:// 底部报名按钮点击报名
                if (Utils.isFastDoubleClick())
                    return;
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
            case R.id.lyDetailsAddr:// 地图
                if (Utils.isFastDoubleClick())
                    return;
                if (detailsInfo != null) {
                    if (TextUtils.isEmpty(detailsInfo.address) || TextUtils.isEmpty(detailsInfo.city)) {
                        application.showMsg("暂无地理位置信息");
                    } else {
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        intent.putExtra("address", detailsInfo.address.replace(" ", "").replace("\n", "").trim());
                        intent.putExtra("city", detailsInfo.city);
                        intent.putExtra("title", detailsInfo.title);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tvContactOrganizer:
                if (Utils.isFastDoubleClick())
                    return;

                if (!application.checkIsLogin()) {
                    toActivity(LoginActivityNew.class);
                    return;
                }

                if (detailsInfo != null) {
                    SessionInfo privateSession = SessionTable.queryPrivateSession(detailsInfo.createby, AccountManager.getAccount());
                    if (privateSession == null) {
                        requestCreateSession(detailsInfo.createby);
                    } else {
                        application.setCurrentSession(privateSession);
                        ToChatActivity();
                    }
                }
                break;
            case R.id.lyDetailsOrg:// 主办方
                if (Utils.isFastDoubleClick())
                    return;

                if (!application.checkIsLogin()) {
                    toActivity(LoginActivityNew.class);
                    return;
                }

                if (isHuodong == 0 && detailsInfo != null) {
                    if (!"0".equals(detailsInfo.orgid)) {
                        Intent intent = new Intent(mContext, SponsorDetailActivity.class);
                        intent.putExtra("orgId", detailsInfo.orgid);// 主办方id
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra("id", detailsInfo.createby);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.llShare:// 群聊
                if (Utils.isFastDoubleClick())
                    return;
                MobclickAgent.onEvent(mContext, "click_event_groupchat_count");
                if (!application.checkIsLogin()) {
                    toActivity(LoginActivityNew.class);
                    return;
                }

                if (isHuodong == 0 && detailsInfo != null) {
                    joinCircle(detailsInfo.id);//不论是否存在会话都请求，可能被踢出群就不能再加入了
                }
                break;
        }
    }

    private void share() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            application.showMsg(R.string.network_check);
            return;
        }
        if (isHuodong == 1) {
            if (detailsInfo != null) {
                dialogShare();
            }
        } else {
            if (detailsInfo != null) {
                dialogShare();
            }
        }
    }

    //立即报名要先做些判断
    //1是否要上传文件  2是否登录  3登录了的话电话/email是否为空 4.是否填写表单，
    public void regTicket() {
        if (!isFile()) {
            if (application.checkIsLogin()) {
                if (application.getUserInfo().isEmailActivated() || application.getUserInfo().isPhoneActivated()) {
                    if (detailsInfo.ticks.size() == 0) {
                        if (!TextUtils.isEmpty(detailsInfo.form)) {
                            // 表单不为空   填写报名表单
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
            } else {//未登录，验证 购票人信息
                startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
            }
        } else {
            //要上传文件
            application.showMsg("报名此活动需要上传附件，请到活动行网站购买");
        }
    }

    //立即报名抢票
    public void regRobTicket() {
        if (!TextUtils.isEmpty(detailsInfo.form)) {// 表单不为空
            Intent intent = new Intent(mContext, BuyTicketThreeActivity.class);
            intent.putExtra("info", detailsInfo);
            intent.putExtra("isRobTicket", isRobTicket);
            startActivity(intent);
            isStartBuyTicketThree = true;
        } else {
            shareCompleteReg();// 分享成功之后调用报名接口，报名第一个票券
        }
    }

    public void getDetailsReg() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Form", ""));
        params.add(new BasicNameValuePair("SN", ""));
        params.add(new BasicNameValuePair("id", detailsInfo.id));
        showProgress("正在报名");
        httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (msg.isSuccess()) {
                            successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
                            addUmengEvent(successInfo);
                            if (successInfo.getPrice() > 0) {
                                // 收费票
                                try {
                                    if (!successInfo.isNeedApply()) {
                                        application.showMsg("报名活动成功");
                                        if (successInfo.getIsAppPay() == 1) {
                                            startActivity(new Intent(mContext, SureOrderActivity.class).putExtra("info", successInfo).putExtra("DetailsInfo", detailsInfo).putExtra("tag", 1));
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
                                    //无表单，无票卷 ，跳转报名成功页面
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("DetailsInfo", detailsInfo);
                                    bundle.putSerializable("info", successInfo);
                                    intent.putExtras(bundle);
                                    intent.setClass(AccuvallyDetailsActivity.this, PaySuccessActivity.class);
                                    startActivity(intent);
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
                            initDetails();
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

    public void getCollection() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", detailsInfo.id));
        params.add(new BasicNameValuePair("type", isHuodong + ""));
        startProgress();
        httpCilents.postA(Url.ACCUPASS_HOME_FOLLOW, params, new HttpCilents.WebServiceCallBack() {

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
                            tvLikeNum.setText("收藏" + (++detailsInfo.likenum) + "次");
                            dbManager.insertSaveBeHavior(application.addBeHavior(20, 0 + "", detailsInfo.id, "", "", "", ""));

                            EventBus.getDefault().post(new EventCollection());
                            application.showMsg("收藏成功");
                        } else {
                            application.showMsg("关注活动失败，请重新关注");
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
        params.add(new BasicNameValuePair("type", isHuodong + ""));
        startProgress();
        httpCilents.postA(Url.ACCUPASS_HOME_UNFOLLOW, params, new HttpCilents.WebServiceCallBack() {

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
                            tvLikeNum.setText("收藏" + (--detailsInfo.likenum) + "次");
                            dbManager.insertSaveBeHavior(application.addBeHavior(21, 0 + "", detailsInfo.id, "", "", "", ""));

                            EventBus.getDefault().post(new EventCollection());
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

    private void dialogShareSuccess() {
        if (shareSuccessDialog == null) {
            shareSuccessDialog = new Dialog(mContext, R.style.dialog);
            shareSuccessDialog.setCancelable(true);
            shareSuccessDialog.setCanceledOnTouchOutside(true);
            shareSuccessDialog.setContentView(R.layout.dialog_share_success);
        }
        shareSuccessDialog.show();
    }

    //分享成功之后调用报名接口，报名第一个票券
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
        showProgress("正在报名");
        httpCilents.postA(Url.ACCUPASS_DETAILS_REG, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                dismissProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse msg = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (msg.isSuccess()) {
                            successInfo = JSON.parseObject(msg.getResult(), RegSuccessInfo.class);
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
                            } else {
                                dialogShareSuccess();
                            }
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

    public void UnRegDialog() {
        if (unRegDialog == null) {
            unRegDialog = new Dialog(mContext, R.style.dialog);
            unRegDialog.setCancelable(true);
            unRegDialog.setCanceledOnTouchOutside(false);
            unRegDialog.setContentView(R.layout.dialog_unreg);
            unRegDialog.findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    unRegDialog.dismiss();
                    startActivity(new Intent(mContext, BuyTicketFirstActivity.class));
                }
            });
        }
        unRegDialog.show();
    }

    public void dialogShare() {
        shareDialog = new Dialog(this, R.style.DefaultDialog);
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
        LinearLayout llRobTips = (LinearLayout) shareDialog.findViewById(R.id.llRobTips);// 分享的抢票提示,
        // 默认隐藏
        if (isRobTicket) {// 是抢票活动
            llRobTips.setVisibility(View.VISIBLE);// 分享的抢票提示 ——显示
            tvShareMsg.setVisibility(View.INVISIBLE);// 短信分享不可见

            TextView tvTips = (TextView) shareDialog.findViewById(R.id.tvTips);
        } else {
            llRobTips.setVisibility(View.GONE);
        }

        shareSina.setOnClickListener(this);
        shareQzone.setOnClickListener(this);
        shareWx.setOnClickListener(this);
        shareWxpy.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        lydismiss.setOnClickListener(this);
        tvShareMsg.setOnClickListener(this);
        DialogUtils.dialogSet(shareDialog, this, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
        shareDialog.show();
    }

    public void RegSuccessDialog() {
        dialog = new Dialog(mContext, R.style.DefaultDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_collect);
        ((TextView) dialog.findViewById(R.id.title)).setText("报名成功咯~");
        ((TextView) dialog.findViewById(R.id.message)).setText("活动行小助手会帮您保管好票券的哟！");
        ((TextView) dialog.findViewById(R.id.tvDialogMistake)).setText("查看票券");
        ((TextView) dialog.findViewById(R.id.tvDialogRemove)).setText("分享");
        dialog.findViewById(R.id.tvDialogMistake).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, TicketVolumeActivity.class);
                startActivity(intent.putExtra("id", detailsInfo.id).putExtra("isDetails", 1).putExtra("SourceType", 0));
            }
        });
        dialog.findViewById(R.id.tvDialogRemove).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (detailsInfo != null) {
                    dialogShare();
                }
            }
        });
        dialog.show();
    }
//123
	@Override
	public void headerScroll() {// 下拉刷新
		initDetails();
	}

	@Override
	public void footerScroll() {// 上拉
//        AccuApplication.getInstance().showMsg("上拉刷新");
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 2015 && data != null) {
//			boolean isFollow = data.getBooleanExtra("isFollow", false);
//			if (isFollow) {
//				ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
//				tvDetailsColl.setText("已收藏");
//			} else {
//				ivDetailsColl.setImageResource(R.drawable.shoucang);
//				tvDetailsColl.setText("收藏");
//			}
//
//			detailsInfo.isfollow = isFollow;
//			if (isFollow) {
//				detailsInfo.likenum++;
//			} else {
//				detailsInfo.likenum--;
//			}
//			tvLikeNum.setText("收藏" + detailsInfo.likenum +
//
//		}
//	}


    public void onEventMainThread(ChangeDetailsDialogEventBus eventBus) {
        initDetails();
    }

    public void onEventMainThread(ChangePaySuccessEventBus eventBus) {
        initDetails();
    }

    public void onEventMainThread(ChangeDetailsRegEventBus eventBus) {
        if (detailsInfo != null) {
            if (!isRobTicket)// 抢票活动不报名留在这个页面
                regTicket();
        }
    }

    public void onEventMainThread(EventRobSuccess eventBus) {
        // 有可能是IntroductPushActivity打开的BuyTicketThreeActivity就不弹出对话框提示
        if (isStartBuyTicketThree)
            dialogShareSuccess();
    }

    public void onEventMainThread(EventCollection eventCollection) {
        if (eventCollection.state == eventCollection.favorite) {
            ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
            tvDetailsColl.setText("已收藏");
            detailsInfo.isfollow = true;
            detailsInfo.likenum++;
            tvLikeNum.setText("收藏" + detailsInfo.likenum + "次");
        } else if (eventCollection.state == eventCollection.unfavorite) {
            ivDetailsColl.setImageResource(R.drawable.shoucang);
            tvDetailsColl.setText("收藏");
            detailsInfo.isfollow = false;
            detailsInfo.likenum--;
            tvLikeNum.setText("收藏" + detailsInfo.likenum + "次");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareUtils.setShareSuccessListener(null);// dialog Unable to add window
        shareUtils = null;
        EventBus.getDefault().unregister(this);
//		clockButton.recycle();
    }

//	@Override
//	public void finish() {
//		if (!application.hasActivity(MainActivityNew.class)) {
//			Intent intent = new Intent(this, MainActivityNew.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			startActivity(intent);
//		}
//		super.finish();
//	}

    private void addUmengEvent(RegSuccessInfo info) {
        if (info != null) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", info.getId());
            map.put("title", info.getTitle());
            map.put("price", info.getPrice() + "");
            map.put("total", info.getTotal() + "");
            map.put("Count", info.getCount() + "");
            map.put("UserName", info.getUserName());
            map.put("Phone", info.getPhone());
            map.put("IsAppPay", info.getIsAppPay() + "");
            MobclickAgent.onEventValue(mContext, "sum_of_attendees", map, 1);
        }
    }

    /**
     * 加入圈子
     */
    public void joinCircle(final String sessionId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("eid", sessionId));
        startProgress();
        httpCilents.postA(Url.ACCUPASS_EVENT_CIRCLE, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                stopProgress();
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (response.isSuccess()) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", sessionId);
                            map.put("title", detailsInfo.title);
                            map.put("logo", detailsInfo.logo);
                            MobclickAgent.onEventValue(mContext, "sum_of_groupchat_people", map, 1);

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

    private void requestCreateSession(final String touid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uid", AccountManager.getAccount()));
        params.add(new BasicNameValuePair("touid", touid));
        httpCilents.postA(Url.SOCIAL_CONV, params, new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        ToastUtil.showMsg(response.msg);
                        if (response.isSuccess() && !TextUtils.isEmpty(response.result)) {
                            SessionInfo sessionInfo = new SessionInfo();
                            sessionInfo.userId = AccountManager.getAccount();
                            sessionInfo.setSessionId(response.result);

                            sessionInfo.setTime(System.currentTimeMillis());
                            sessionInfo.setTitle(detailsInfo.creator);
                            sessionInfo.setLogoUrl(detailsInfo.creatorlogo);
                            sessionInfo.friendId = touid;
                            application.setCurrentSession(sessionInfo);
                            SessionTable.insertSession(sessionInfo);
                            EventBus.getDefault().post(new ChangeMessageEventBus());

                            ToChatActivity();
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }

    private void ToChatActivity() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("isPrivateChat", true);// 私聊
        startActivity(intent);
    }
    //////////////////////==================================================================



    private String content;
    private WebView webView;

    private void setupWebView() {

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);// 是否开启缩放
        webView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
//        content = getIntent().getStringExtra("content");
        webView.loadDataWithBaseURL("about:blank", detailsInfo.desc, "text/html", "utf-8", null);
    }








    //////////////////////==================================================================

























    //=========================================================================================

    private void initScroll(){
        mLayout = (PullLayout) findViewById(R.id.tblayout);
        mLayout.setOnPullListener(this);
        mLayout.setOnContentChangeListener(this);
        mHeader = (ScrollView) findViewById(R.id.header);
        mFooter = (ScrollView) findViewById(R.id.footer);
        mHeaderContent = (LinearLayout) mHeader.getChildAt(0);
        mFooterContent = (LinearLayout) mFooter.getChildAt(0);
    }

    @Override
    public boolean headerFootReached(MotionEvent event) {
//        Toast.makeText(this,"headerFootReached,"+event.toString(),Toast.LENGTH_SHORT).show();
        if (mHeader.getScrollY() + mHeader.getHeight() >= mHeaderContent
                .getHeight()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean footerHeadReached(MotionEvent event) {
//        Toast.makeText(this,"footerHeadReached,"+event.toString(),Toast.LENGTH_SHORT).show();
        if (mFooter.getScrollY() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onPageChanged(int stub) {
        switch (stub) {
            case PullLayout.SCREEN_HEADER:
//            Toast.makeText(this,"onPageChanged,SCREEN_HEADER",Toast.LENGTH_SHORT).show();
//			Log.d("tag", "SCREEN_HEADER");
                break;
            case PullLayout.SCREEN_FOOTER:
//            Toast.makeText(this,"onPageChanged,SCREEN_FOOTER",Toast.LENGTH_SHORT).show();
//			Log.d("tag", "SCREEN_FOOTER");
                break;
        }
    }
}
