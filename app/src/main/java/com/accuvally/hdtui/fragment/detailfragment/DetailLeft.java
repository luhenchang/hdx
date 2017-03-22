package com.accuvally.hdtui.fragment.detailfragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.entry.LinkedMiddleActivity;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.home.SponsorDetailActivity;
import com.accuvally.hdtui.activity.home.buy.PaySuccessActivity;
import com.accuvally.hdtui.activity.home.buy.PayWebActivity;
import com.accuvally.hdtui.activity.home.buy.SureOrderActivity;
import com.accuvally.hdtui.activity.home.comment.CommentActivity;
import com.accuvally.hdtui.activity.home.comment.CommentDisplayActivity;
import com.accuvally.hdtui.activity.home.register.BindPhoneBeforBuyActivity;
import com.accuvally.hdtui.activity.home.register.SelectTicketActivity;
import com.accuvally.hdtui.activity.home.register.SubmitFormActivity;
import com.accuvally.hdtui.activity.home.util.MapsActivity;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.message.user.UserDetailActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.activity.mine.setting.FeedBackActivity;
import com.accuvally.hdtui.adapter.CommentAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.AccuBean;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.CommentInfo;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.push.GetuiPushMessageReceiver;
import com.accuvally.hdtui.ui.OverScrollView;
import com.accuvally.hdtui.ui.PagerSlidingTabStrip;
import com.accuvally.hdtui.ui.pulltoloaddetail.PullLayout;
import com.accuvally.hdtui.utils.AndroidHttpClient;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.LoginUtil;
import com.accuvally.hdtui.utils.NetworkUtils;
import com.accuvally.hdtui.utils.PermissionUtil;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeAttentionState;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsDialogEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeDetailsRegEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangePaySuccessEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCollection;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
/**
 * Created by Andy Liu on 2017/2/7.
 */
@SuppressLint("HandlerLeak")
//@SuppressLint或者@SuppressWarnings
public class DetailLeft extends BaseFragment implements View.OnClickListener, PullLayout.OnPullListener,
        PullLayout.OnPageChangedListener {

    private PullLayout mLayout;
    private ScrollView mHeader, mFooter;
    private LinearLayout mHeaderContent, mFooterContent;


    private String homeId;

    private boolean ToCommentActivity = false;//是否加载完就跳转到评价界面
    private boolean TOCommentDisplayActivity_comment = false;//是否加载完就跳转到评价展示界面
    private boolean TOCommentDisplayActivity_consult = false;//是否加载完就跳转到咨询展示界面

    private int isHuodong;//isHuodong 0表示活动行的，1表示活动推

    private AccuDetailBean detailsInfo;


    private LinearLayout share_ly;//分享

    private ImageView ivDetailsLogo;//最上面的大img，
    private TextView tvDetailsTitle;//标题

    private TextView tvDetailsSell, tvVisitNum, tvLikeNum;//卖出的，浏览的，收藏的次数

    private TextView tvDetailsTime;//活动时间
    private LinearLayout lyDetailsAddr;//活动地点布局
    private TextView tvDetailsAddress; //活动地点
    private View vPriceLine;//一条横线
    private TextView tvDetailsTicket;//票价
    private TextView tvDetailsRegTicket;//报名文字

    private LinearLayout lyDetailsOrg;//主办方布局
    private ImageView iv_org_logo;//主办方logo


    private TextView tvDetailsOrgName;//主办方名称
    private ImageView ivCertification;//主办方标志，
    private ImageView ivVip;//认证 vip
//    private TextView tvContactOrganizer;//联系主办方
    private RelativeLayout rlToOrganizer;//主办方详情
    private RelativeLayout rlAddAttention;//关注主办方
    private TextView tvAddAttention;
    private TextView tv_followNum, tv_huodongNum, tv_likeNum;//关注数，活动数，点赞数
    private int likeNum;
    private TextView OrgDesc;//主办方描述

    private TextView toHistoryComment,toHistoryConsultion;
    private RelativeLayout llToHistoryComment,llToHistoryConsultion;
    private View viewOrgLine;//一条横线

    private TextView tvDetailsColl;//收藏提示
    private ImageView  ivDetailsColl;//收藏图片提示
    private  LinearLayout llCollect;//收藏layout

    private  LinearLayout llGroupChat,llEvaluate;//群聊，评价
    private  TextView tvGroupChat;//群聊textview

    private CustomViewPager viewpager;

    private View include_accuvallydetail_bottom;//底部layout


    private OverScrollView scrollview;  //123

    private Dialog shareDialog, shareSuccessDialog;//分享按钮
    private Dialog unRegDialog;// 没有登入,弹出对话框

    private UMSocialService mController;

    ShareUtils shareUtils;

    private RegSuccessInfo successInfo;

    public static final String TAG = "AccuvallyDetailsActivity";

    public static final int loginForComment = 1;
    public static final int submitComment = 2;

    private boolean firstTime=true;//防止个推跳转的时候，相应多次

    View rootView;
    AccuvallyDetailsActivity accuvallyDetailsActivity;

    public PagerSlidingTabStrip tabStriptwo;//活动，详情 两个tab
    public TextView tabStripone;//拖动后 只显示活动详情一个tab

     ImageView activity_header_progressbar;
     AnimationDrawable animationDrawable;
    public TextView tvRobTicket;//抢票文字
    public LinearLayout llRegTicket;
    Timer timer;
    int count;//离抢票的时间
    public final static String SCODE="SCODE";

    private String scode=null;

    private boolean hasGetTheCodeAddress=false;
    private boolean hasGetTheCode=false;

    public void startProgress() {
        activity_header_progressbar.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) activity_header_progressbar.getBackground();
        activity_header_progressbar.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
    }

    public void stopProgress() {
        activity_header_progressbar.setVisibility(View.GONE);
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail_left, container, false);
        accuvallyDetailsActivity= (AccuvallyDetailsActivity) getActivity();
        initView();
        initViewFromActivity();
        parseIntent();
        initUmengData();
        getDetails();

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }



    public void initView() {
        initScroll();

        ivDetailsLogo = (ImageView) rootView.findViewById(R.id.ivDetailsLogo);
        tvDetailsTitle = (TextView) rootView.findViewById(R.id.tvDetailsTitle);
        iv_org_logo = (ImageView) rootView.findViewById(R.id.iv_org_logo);

        tvDetailsTime = (TextView) rootView.findViewById(R.id.tvDetailsTime);
        tvDetailsAddress = (TextView) rootView.findViewById(R.id.tvDetailsAddress);
        tvDetailsTicket = (TextView) rootView.findViewById(R.id.tvDetailsTicket);
//        tvContactOrganizer = (TextView) rootView.findViewById(R.id.tvContactOrganizer);
        rlToOrganizer = (RelativeLayout) rootView.findViewById(R.id.rlToOrganizer);
        rlAddAttention = (RelativeLayout) rootView.findViewById(R.id.rlAddAttention);
        rlAddAttention.setOnClickListener(this);
        tvAddAttention = (TextView) rootView.findViewById(R.id.tvAddAttention);

        tv_followNum = (TextView) rootView.findViewById(R.id.tv_followNumber);
        tv_huodongNum = (TextView) rootView.findViewById(R.id.tv_huodongNumber);
        tv_likeNum = (TextView) rootView.findViewById(R.id.tv_likeNumber);
        OrgDesc = (TextView) rootView.findViewById(R.id.tvDetailsOrgDesc2);


        toHistoryConsultion = (TextView) rootView.findViewById(R.id.toHistoryConsultion);
        llToHistoryConsultion= (RelativeLayout) rootView.findViewById(R.id.llToHistoryConsultion);

        toHistoryComment = (TextView) rootView.findViewById(R.id.toHistoryComment);
        llToHistoryComment= (RelativeLayout) rootView.findViewById(R.id.llToHistoryComment);

        tvDetailsOrgName = (TextView) rootView.findViewById(R.id.tvDetailsOrgName);
        ivCertification = (ImageView) rootView.findViewById(R.id.ivCertification);
        ivVip = (ImageView) rootView.findViewById(R.id.ivVip);

        lyDetailsAddr = (LinearLayout) rootView.findViewById(R.id.lyDetailsAddr);

        tvDetailsSell = (TextView) rootView.findViewById(R.id.tvDetailsSell);
        tvVisitNum = (TextView) rootView.findViewById(R.id.tvVisitNum);
        tvLikeNum = (TextView) rootView.findViewById(R.id.tvLikeNum);

        scrollview = (OverScrollView) rootView.findViewById(R.id.header);
        viewOrgLine = rootView.findViewById(R.id.viewOrgLine);
        vPriceLine = rootView.findViewById(R.id.vPriceLine);


        include_accuvallydetail_bottom = rootView.findViewById(R.id.include_accuvallydetail_bottom);

        lyDetailsAddr.setOnClickListener(this);

        lyDetailsAddr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copy(detailsInfo.address, mContext);
                return true;
            }
        });

        scrollview.setOverScrollListener(new OverScrollView.OverScrollListener() {
            //123
            @Override
            public void headerScroll() {// 下拉刷新
                getDetails();
            }

            @Override
            public void footerScroll() {// 上拉
            }
        });

        tvDetailsTicket.setOnClickListener(this);
//        tvContactOrganizer.setOnClickListener(this);
        rlToOrganizer.setOnClickListener(this);


        llToHistoryComment.setOnClickListener(this);
        rootView.findViewById(R.id.tv_home_historyComment).setOnClickListener(this);

        llToHistoryConsultion.setOnClickListener(this);

    }

    private void initViewFromActivity(){

        activity_header_progressbar=accuvallyDetailsActivity.activity_header_progressbar;

        share_ly = accuvallyDetailsActivity.share_ly;//分享
        share_ly.setOnClickListener(this);

        tvDetailsColl = accuvallyDetailsActivity.tvDetailsColl;//收藏与否文字提示
        ivDetailsColl = accuvallyDetailsActivity.ivDetailsColl;//收藏与否图标提示
        llCollect= accuvallyDetailsActivity.llCollect;
        llCollect.setOnClickListener(this);// 收藏

        llGroupChat= accuvallyDetailsActivity.llGroupChat;
        tvGroupChat=accuvallyDetailsActivity.tvGroupChat;
        llGroupChat.setOnClickListener(this);// 群聊

        llEvaluate= accuvallyDetailsActivity.llEvaluate;
        llEvaluate.setOnClickListener(this);// 评价

        tvDetailsRegTicket = accuvallyDetailsActivity.tvDetailsRegTicket;//报名文字
        tvDetailsRegTicket.setOnClickListener(this);

        tvRobTicket = accuvallyDetailsActivity.tvRobTicket;//抢票提示
        llRegTicket=accuvallyDetailsActivity.llRegTicket;


        tabStripone=accuvallyDetailsActivity.tabStripone;
        tabStriptwo=accuvallyDetailsActivity.tabStriptwo;
        viewpager=accuvallyDetailsActivity.viewpager;

    }


    @SuppressWarnings("deprecation")
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        Toast.makeText(context, "地址已复制到剪切板上", Toast.LENGTH_SHORT).show();

    }


    private void initUmengData() {
        mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
        shareUtils = new ShareUtils(mContext, mController, homeId);
    }

    private void parseIntent() {

        Bundle bundle = getArguments();
        homeId = bundle.getString("id");
        isHuodong = bundle.getInt("isHuodong", 0);
        ToCommentActivity = bundle.getBoolean(GetuiPushMessageReceiver.ToCommentActivity, false);
        TOCommentDisplayActivity_comment = bundle.getBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment, false);
        TOCommentDisplayActivity_consult = bundle.getBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult, false);
    }






    //=========================================================================================
    // 获取详情
    public void getDetails() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", homeId));
        Trace.e("AccuvallyDetailsActivity", "homeId:" + homeId);
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

                            detailsInfo = JSON.parseObject(response.result, AccuDetailBean.class);

                            if (isHuodong == 1) {
                                initJelly();//活动推
                            } else {
                                initAccuDetail();//活动行
                            }

                            //若是个推导向CommentActivity：
                            if (ToCommentActivity&&firstTime) {
                                firstTime=false;
                                evalute();//个推导向
                            }

                            if (TOCommentDisplayActivity_comment&&firstTime) {
                                firstTime=false;
                                toCommentDisplay();//个推导向
                            }

                            if (TOCommentDisplayActivity_consult&&firstTime) {
                                firstTime=false;
                                toConsultionDisplay();//个推导向
                            }

                            if(accuvallyDetailsActivity.LinkedActivity_HASCOUPON){
                                linkedToSelectActivity();
                            }


                        }else {
                            application.showMsg(response.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg("网络连接断开，请检查网络");
                        break;
                }
            }
        });
    }


    //主办方界面
    private void setSporAccu() {
        follows = detailsInfo.OrgFollowsNum;
        tv_followNum.setText(follows + "");
        tv_huodongNum.setText(detailsInfo.OrgEventsNum + "");
        likeNum = detailsInfo.OrgZanNum;
        tv_likeNum.setText(likeNum + "");

        hasAttention = detailsInfo.IsFollowOrg;
        if (hasAttention) {//初始化
            tvAddAttention.setText("已关注");
//            tvAddAttention.setTextColor(Color.argb(255, 0x99, 0x99, 0x99));
            tvAddAttention.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            tvAddAttention.setText("关注");
//            tvAddAttention.setTextColor(Color.argb(255, 0x76, 0xbf, 0x47));
            tvAddAttention.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.add_attention_no_frame), null, null, null);
        }

        if (LoginUtil.isSporsor(detailsInfo.orgid)) {
            OrgDesc.setText(detailsInfo.orgdesc);
        } else {
            if (!("一句话介绍".equals(detailsInfo.CreatorDesc)))
                OrgDesc.setText(detailsInfo.CreatorDesc);
        }

    }



    //设置历史评价列表(0-3条)
    private void setCommentAccu(){

        if (detailsInfo == null || detailsInfo.commentlist == null)
            return;

//暂时屏蔽掉
//        toHistoryComment.setText("历史评价 (" + detailsInfo.commentlistcount + ")");
//        toHistoryConsultion.setText("活动咨询 ("+detailsInfo.consultlistcount+")");

        if (detailsInfo.commentlist.isEmpty()) {
            rootView.findViewById(R.id.includeComment).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.includeComment).setVisibility(View.VISIBLE);
            LinearLayout hsvChildll = (LinearLayout) rootView.findViewById(R.id.hsvComment);
            hsvChildll.removeAllViews();

            for (int i = 0; i < detailsInfo.commentlist.size(); i++) {
                CommentInfo commentInfo=detailsInfo.commentlist.get(i);
                commentInfo.eventname=detailsInfo.title;
                View itemView= CommentAdapter.getView(this.getActivity(), detailsInfo.commentlist.get(i));
                hsvChildll.addView(itemView);
            }
        }
    }


    //你可能感兴趣的
    private void setInterestAccu() {
        if (detailsInfo == null || detailsInfo.interestacts == null)
            return;

        if (detailsInfo.interestacts.isEmpty()) {
            rootView.findViewById(R.id.includeInterest).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.includeInterest).setVisibility(View.VISIBLE);
            LinearLayout hsvChildll = (LinearLayout) rootView.findViewById(R.id.hsvChildll);
            hsvChildll.removeAllViews();
            for (int i = 0; i < detailsInfo.interestacts.size(); i++) {
                View itemView = this.getActivity().getLayoutInflater().inflate(R.layout.listitem_home_recommend, null);
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
                        /*if (getIntent().getBooleanExtra("isSameStart", false)) {
                            finish();  change by
                        }*/
                    }
                });
            }
        }
    }


    // 活动行详情Text界面
    private void setTextAccuvally() {

        share_ly.setVisibility(View.VISIBLE);

        if(detailsInfo.GroupChatDisabled){
            tvGroupChat.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.acc_detail_group_disable), null,null);
        }

        application.mImageLoader.displayImage(detailsInfo.logo, ivDetailsLogo);
        tvDetailsTitle.setText(detailsInfo.title);
        tvDetailsTime.setText(detailsInfo.getTimeStr());
        tvDetailsAddress.setText(detailsInfo.address);
        if (detailsInfo.maxprice > 0)
            tvDetailsTicket.setText("￥" + detailsInfo.minprice + " - ￥" + detailsInfo.maxprice);
        else
            tvDetailsTicket.setText("免费");

        if (detailsInfo.showregisternum) {
            tvDetailsSell.setVisibility(View.VISIBLE);
            tvDetailsSell.setText("已售" + detailsInfo.regnum + "张");
        } else {
            tvDetailsSell.setVisibility(View.GONE);
        }

        tvVisitNum.setText("浏览" + detailsInfo.visitnum + "次");
        tvLikeNum.setText("收藏" + detailsInfo.likenum + "次");

        if (detailsInfo.isvip) {

            ivVip.setVisibility(View.VISIBLE);
        } else {
            ivVip.setVisibility(View.GONE);
        }

        if (detailsInfo.orgstatus == 1) {
            ivCertification.setVisibility(View.VISIBLE);
        } else {
            ivCertification.setVisibility(View.GONE);
        }


        if (!"0".equals(detailsInfo.orgid)) {//主办方 orgid!=0,代表有主办方，否则只有主办人？？
            String str = detailsInfo.orgname;
            tvDetailsOrgName.setText(str);
            application.mImageLoader.displayImage(detailsInfo.orglogo, iv_org_logo, UILoptions.squareOptions);
        } else {
            String str = detailsInfo.creator;


            tvDetailsOrgName.setText(str);
            application.mImageLoader.displayImage(detailsInfo.creatorlogo, iv_org_logo, UILoptions.squareOptions);
        }


        if (!detailsInfo.AllowReply) {
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
        } else {
            tvDetailsRegTicket.setText("活动评价");
            tvDetailsRegTicket.setClickable(true);
            tvDetailsRegTicket.setBackgroundResource(R.drawable.selector_click_btn);

            tvDetailsTicket.setClickable(false);
        }


        if (detailsInfo.isfollow) {
            ivDetailsColl.setImageResource(R.drawable.selected_shoucang);
            tvDetailsColl.setText("已收藏");
        } else {
            ivDetailsColl.setImageResource(R.drawable.shoucang);
            tvDetailsColl.setText("收藏");
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


        if (detailsInfo.StaticMode) {//抢票活动会判断是否展示抢票倒计时等

            tvRobTicket.setVisibility(View.VISIBLE);

            setStatus("倒计时中");

//            llRegTicket.setVisibility(View.GONE);
            setCountDownTimer();//获取抢票时间，并开始倒计时

        } else {

            tvRobTicket.setVisibility(View.GONE);

            tvDetailsRegTicket.setClickable(true);
            tvDetailsRegTicket.setBackgroundResource(R.drawable.selector_click_btn);
            tvDetailsTicket.setClickable(true);

//            llRegTicket.setVisibility(View.VISIBLE);
        }



        String url = detailsInfo.shareurl;
        shareUtils.initConfig(this.getActivity(), detailsInfo.title, Html.fromHtml(detailsInfo.summary).toString(),
                detailsInfo.logo, url);
    }

    private String parseScodeAddress(String str){
        String ret=null;
        try{
            String[] strings=str.split("=");
            ret=strings[1].trim().substring(1,strings[1].length()-3);
            System.out.println(str);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ret;

    }


    //获取验证码的地址
    private void getScodeAddress(){
        new Thread(new Runnable() {
            @Override
            public void run() {


                final int maxTime=40;
                int tryTime=0;
                hasGetTheCodeAddress=false;

                while (!hasGetTheCodeAddress){

                    tryTime++;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(mContext));
                        HttpGet request = new HttpGet("http://rush.huodongxing.com/address/"+homeId+".js"+"?time="+System.currentTimeMillis());//暂时写死
                        HttpResponse response = httpClient.execute(request);
                        String result= EntityUtils.toString(response.getEntity());
                        int statusCode = response.getStatusLine().getStatusCode();
                        Trace.e(TAG, "getScodeAddress   result:" + result + ",statusCode:" + statusCode+",tryTime:"+tryTime);
                        if(statusCode==200){

                            String address=parseScodeAddress(result);
                            if((address!=null) && address.startsWith("http")){
                                hasGetTheCodeAddress=true;
                                getScode(address);
                            }else {
                                if(tryTime>maxTime){
                                    hasGetTheCodeAddress=true;
                                    setTryRobTickView();
                                }
                            }
                        }//end 200
                        else{//除200外的其他情况
                            if(tryTime>maxTime){
                                hasGetTheCodeAddress=true;
                                setTryRobTickView();
                            }
                        }//end 除200外的其他情况


                    } catch (Exception e) {
                        if(tryTime>maxTime){
                            hasGetTheCodeAddress=true;
                            setTryRobTickView();
                        }
                        Trace.e(TAG,e.getMessage());
                        e.printStackTrace();
                    }


                }
            }
        }).start();
    }


    //由linked优惠码引导进入购票页面
    private void linkedToSelectActivity(){
        detailsInfo.desc=null;
        Intent intent=new Intent(mContext, SelectTicketActivity.class);
        intent.putExtra("info", detailsInfo);
        intent.putExtra(LinkedMiddleActivity.LinkedActivity_COUPON, accuvallyDetailsActivity.LinkedActivity_COUPON);
        intent.putExtra(LinkedMiddleActivity.LinkedActivity_HASCOUPON, true);
        startActivity(intent);
    }


    //有了scode，进入抢票
    private void starRobActivity(){
        detailsInfo.desc=null;
        Intent intent=new Intent(mContext, SelectTicketActivity.class);
        intent.putExtra("info", detailsInfo);
        intent.putExtra(SCODE, scode);
        startActivity(intent);
    }

    //获取验证码
    private void getScode(final String address){


        new Thread(new Runnable() {
            @Override
            public void run() {
                final int maxTime=40;
                int tryTime=0;
                hasGetTheCode=false;
                while (!hasGetTheCode){
                    tryTime++;

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(mContext));
                        HttpGet request = new HttpGet(address);
                        HttpResponse response = httpClient.execute(request);
                        String result= EntityUtils.toString(response.getEntity());
                        int statusCode = response.getStatusLine().getStatusCode();

                        Trace.e(TAG, "getScode   result:" + result + ",statusCode:" + statusCode+",tryTime:"+tryTime);
                        int scodeLength=result.trim().length();
                        if((10<scodeLength) &&(scodeLength<20)){//抢到了s码

                            hasGetTheCode=true;//a.抢到s码
                            scode=result.trim();//去掉换行符

                            setRobSuccessTickView();
                            starRobActivity();//获取到scode
                        }else if(result.trim().equals("1012")){//1012抢票结束
                            hasGetTheCode=true;//b.抢票结束
                            setRobStopTickView();
                        }else {//其他提示一直在

                            if(tryTime>maxTime){
                                hasGetTheCode=true;//c.超过次数了
                                setTryRobTickView();
                            }
                        }



                    } catch (Exception e) {
                        Trace.e(TAG,e.getMessage());
                        e.printStackTrace();

                        if(tryTime>maxTime){
                            hasGetTheCode=true;//c.超过次数了
                            setTryRobTickView();
                        }
                    }

                }

            }
        }).start();
    }

    //正在排队中的ui：包括获取scode地址，获取scode
    private void setRobIngTickView(){
        setProgressCancleable(false);
        showProgress("正在排队中");

    }

    //抢票结束
    private void setRobStopTickView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
                application.showMsg("很抱歉，此次抢票已结束");
                setStatus("抢票结束");
            }
        });
    }


    //40次获取s码之后，提示
    private void setTryRobTickView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgress();

                showTryRobAgainDialog();
            }
        });
    }


    //抢到s码之后，恢复可报名状态，从报名退出来之后可继续抢票
    private void setRobSuccessTickView(){
                                    getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dismissProgress();

            }
        });
    }

    DecimalFormat df=new DecimalFormat("00");
    //显示倒计时，并且在时间到了之后把买票按钮显示出来。
    private void setTimeDisplay(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (count <= 0) {//倒计时结束
                    if (timer != null) {
                        timer.cancel();
                    }
                    tvRobTicket.setVisibility(View.GONE);//可以抢票了
//                    llRegTicket.setVisibility(View.VISIBLE);//可以抢票了


                    tvDetailsRegTicket.setText("立即抢票");
                    tvDetailsRegTicket.setClickable(true);
                    tvDetailsRegTicket.setBackgroundResource(R.drawable.selector_click_btn);
                    tvDetailsTicket.setClickable(true);


                    ToastUtil.showMsgShort("现在可以开始抢票");

                } else {
                    int hour = count / 3600;
                    int temp = count % 3600;
                    int minute = temp / 60;
                    int secound = temp % 60;

                    if(AccountManager.checkIsLogin()){
                        tvRobTicket.setText("抢票倒计时:  " + df.format(hour) + ":" + df.format(minute) + ":" + df.format(secound) );
                    }else {
                        tvRobTicket.setText("(请提前登录) 抢票倒计时:  "  +df.format(hour) + ":" + df.format(minute) + ":" + df.format(secound) + ":");
                    }

                }


            }
        });


    }



    //倒计时
    private void handleCountDownTimer(){
        if(timer!=null){
            timer.cancel();//重复刷新的时候要先杀死之前的timer
        }

        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count--;
                setTimeDisplay();//倒计时

            }
        }, 1000, 1000);

    }


    //获取抢票时间，并开始倒计时
    private void setCountDownTimer(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HttpCilents.buildUserAgent(mContext));
                    HttpGet request = new HttpGet("http://rush.huodongxing.com/service/e2s?eid="+homeId);//暂时写死
                    HttpResponse response = httpClient.execute(request);
                    String result= EntityUtils.toString(response.getEntity());
                    int statusCode = response.getStatusLine().getStatusCode();
                    Trace.e(TAG, "setCountDownTimer    result:" + result + ",statusCode:" + statusCode);
                    try {
                        double temp=Double.parseDouble(result);
                        count=(int)temp;
                    }catch (Exception e){
                        e.printStackTrace();
                        count=-1;
                    }
                    setTimeDisplay();//先显示倒计时出来，或者时间到了，把买票页面显示出来

                    if(count>0){
                        handleCountDownTimer();
                    }

                } catch (Exception e) {
                    Trace.e(TAG,e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }



    //==========报名按钮的显示内容=====
    private void setStatus(String status) {
        tvDetailsRegTicket.setText(status);
        tvDetailsRegTicket.setClickable(false);
        tvDetailsRegTicket.setBackgroundResource(R.drawable.gary_bg_btn);
        tvDetailsTicket.setClickable(false);
    }


    private void initAccuDetail(){
        setTextAccuvally();
        setSporAccu();

        setInterestAccu();
        setCommentAccu();
        setupWebViewLeft();
        setupWebViewRight();
    }

    // 活动推详情
    public void initJelly() {
        Trace.e(TAG, "进入活动推");
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
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gary_title)), 3, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        shareUtils.initConfig(this.getActivity(), detailsInfo.title, Html.fromHtml(detailsInfo.summary).toString(), detailsInfo.logo, detailsInfo.shareurl);

        include_accuvallydetail_bottom.setVisibility(View.GONE);
        share_ly.setVisibility(View.VISIBLE);
    }

//==========================================================================================================



    //进入评价
    private void evalute() {

        if (!application.checkIsLogin()) {
            Intent intent = new Intent(mContext, LoginActivityNew.class);
            startActivityForResult(intent, loginForComment);
            return;
        }

        if (detailsInfo.AllowReply) {


            Intent evaluateIntent = new Intent(mContext, CommentActivity.class);
            evaluateIntent.putExtra(CommentActivity.TITLE, detailsInfo.title);
            evaluateIntent.putExtra(CommentActivity.TIME, detailsInfo.getStartutc());
            evaluateIntent.putExtra(CommentActivity.LOCATION, detailsInfo.address);
            evaluateIntent.putExtra(CommentActivity.LOGO, detailsInfo.logo);
            evaluateIntent.putExtra(CommentActivity.ID, detailsInfo.id);
            evaluateIntent.putExtra(CommentActivity.LIKENUM, detailsInfo.ZanUp);
            evaluateIntent.putExtra(CommentActivity.DISLIKENUM, detailsInfo.ZanDown);
            startActivityForResult(evaluateIntent, submitComment);

        }
    }


    private void startMapActivity() {
        Intent intent = new Intent(mContext, MapsActivity.class);
        intent.putExtra("address", detailsInfo.address.replace(" ", "").replace("\n", "").trim());
        intent.putExtra("city", detailsInfo.city);
        startActivity(intent);
    }


    private void toCommentDisplay(){
        if (detailsInfo != null) {
            if(!detailsInfo.CommentDisabled){
                Intent commentIntent = new Intent(mContext, CommentDisplayActivity.class);
                commentIntent.putExtra(CommentDisplayActivity.REPLAY_TYPE, 1);//评价
                if (!"0".equals(detailsInfo.orgid)){
                    commentIntent.putExtra(CommentDisplayActivity.TYPE, 1);//主办方
                    commentIntent.putExtra(CommentDisplayActivity.TARGET, detailsInfo.orgid);//
                }else {
                    commentIntent.putExtra(CommentDisplayActivity.TYPE, 11);//主办人
                    commentIntent.putExtra(CommentDisplayActivity.TARGET,  detailsInfo.createby);//
                }

                startActivity(commentIntent);
            }

        }
    }

    private void toConsultionDisplay(){
        if (detailsInfo != null) {

            if(!detailsInfo.CommentDisabled){
                Intent consulationIntent = new Intent(mContext, CommentDisplayActivity.class);
                consulationIntent.putExtra(CommentDisplayActivity.REPLAY_TYPE, 2);//活动咨询
                consulationIntent.putExtra(CommentDisplayActivity.TYPE, 0);//
                consulationIntent.putExtra(CommentDisplayActivity.TARGET,  detailsInfo.id);//
                consulationIntent.putExtra(CommentDisplayActivity.EVENT_NAME,  detailsInfo.title);//
                startActivity(consulationIntent);
            }

        }
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.llToHistoryComment:
            case R.id.tv_home_historyComment:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                toCommentDisplay();

                break;
            case R.id.llToHistoryConsultion:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                toConsultionDisplay();
                break;
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
            case R.id.share_ly:// 分享
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                MobclickAgent.onEvent(mContext, "click_event_share_count");
                share();
                break;
            case R.id.llEvaluate://用于测试
                if (Utils.isFastDoubleClick()) {
                    return;
                }

                detailsInfo.AllowReply=true;
                evalute();//用于测试
                break;

            case R.id.rlAddAttention://关注
                if (detailsInfo != null) {
                    attentionSponsor();
                }
                break;
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
                if (Utils.isFastDoubleClick())
                    return;
                if (detailsInfo != null) {
                    if (!detailsInfo.AllowReply) {//活动报名
                        if (isHuodong == 1) {
                            if (detailsInfo != null) {
                                application.showMsg("该活动不提供报名");
                            }
                        } else {
                                if(!detailsInfo.StaticMode){//  不是抢票活动才能从票价进入报名界面
                                    regTicket();//从票价进入
                                }
                        }
                    }
                }
                break;
            case R.id.tvDetailsRegTicket:// 底部报名按钮点击报名
                if (Utils.isFastDoubleClick())
                    return;
                if (detailsInfo != null) {
                    if (!detailsInfo.AllowReply) {//活动报名
                        if (isHuodong == 1) {
                            if (detailsInfo != null) {
                                application.showMsg("该活动不提供报名");
                            }
                        } else {
                            if (detailsInfo != null) {
                                regTicket();//从底部报名按钮进入
                            }
                        }
                    } else {
                        evalute();//活动报名或评价
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
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermission();
                        } else {
                            startMapActivity();
                        }
                    }
                }
                break;
            case R.id.rlToOrganizer:// 主办方小站
                if (Utils.isFastDoubleClick())
                    return;

                if (isHuodong == 0 && detailsInfo != null) {
                    if (!"0".equals(detailsInfo.orgid)) {
                        Intent intent = new Intent(mContext, SponsorDetailActivity.class);
                        intent.putExtra("orgId", detailsInfo.orgid);// 主办方id
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra("id", detailsInfo.createby);
                        intent.putExtra("nick", detailsInfo.creator);
                        intent.putExtra("avatarUrl", detailsInfo.creatorlogo);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.llGroupChat:// 群聊
                if (Utils.isFastDoubleClick())
                    return;

                MobclickAgent.onEvent(mContext, "click_event_groupchat_count");
                if (!application.checkIsLogin()) {
                    toActivity(LoginActivityNew.class);
                    return;
                }

                if (isHuodong == 0 && detailsInfo != null) {
                    if(!detailsInfo.GroupChatDisabled){
                        joinCircle(detailsInfo.id);//不论是否存在会话都请求，可能被踢出群就不能再加入了
                    }
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case loginForComment:
                if(AccuApplication.getInstance().checkIsLogin()){
                    firstTime=true;
                    getDetails();//登录之后，再次刷新评价按钮
                }
                break;
            case submitComment://成功发表评价
                if(resultCode==this.getActivity().RESULT_OK &&(data!=null)){
                    int likeState=data.getIntExtra(CommentActivity.LIKE_STATE,0);
                    if (likeState == 1) {//为1时点赞数加一
                        tv_likeNum.setText((likeNum + 1) + "");
                    }
                    tvDetailsRegTicket.setText("已评价");
                    tvDetailsRegTicket.setClickable(false);
                    tvDetailsRegTicket.setBackgroundResource(R.color.gary);
                }
                break;
        }
    }

    //立即报名
    public void regTicket() {
        //1是否登录  2登录了的话电话/email是否为空   3.是否要选择票种
        // 4.1是否填写表单 4.2是否抢票活动

        if (application.checkIsLogin()) {
            if (application.getUserInfo().isEmailActivated() || application.getUserInfo().isPhoneActivated()) {
                if (detailsInfo.ticks.size() == 0) {
                    if (!TextUtils.isEmpty(detailsInfo.form)) {
                        // 表单不为空   填写报名表单
                        Intent intent = new Intent(mContext, SubmitFormActivity.class);
                        intent.putExtra("info", detailsInfo);
                        intent.putExtra("isRobTicket", detailsInfo.isRush);
                        startActivity(intent);
                    } else {
                        // 表单为空,直接报名
                        submitSignUp();
                    }
                } else {//进入报名，选择票种界面
                    if(!detailsInfo.StaticMode){
                        detailsInfo.desc=null;
                        startActivity(new Intent(mContext, SelectTicketActivity.class).
                                putExtra("info", detailsInfo));


                    }else {//如果是抢票活动

                        if(scode!=null){
                            starRobActivity();
                        }else {
                            setRobIngTickView();
                            getScodeAddress();
                        }

                    }
                }
            } else {//第三方登录了，但电话/email为空
                showUnRegDialog();
            }
        } else {//未登录，跳转到登录界面
            startActivity(new Intent(mContext, LoginActivityNew.class));
        }

    }



    //在本界面直接报名
    private void submitSignUp() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Form", ""));
        params.add(new BasicNameValuePair("SN", ""));
        params.add(new BasicNameValuePair("id", detailsInfo.id));
        showProgress("正在报名");
        httpCilents.postB(Url.ACCUPASS_DETAILS_REG, params, new HttpCilents.WebServiceCallBack() {

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
                                    intent.setClass(mContext, PaySuccessActivity.class);
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
                            getDetails();
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


    private Dialog tryRobAgainDialog;

    public void showTryRobAgainDialog() {
        tryRobAgainDialog = new Dialog(mContext, R.style.DefaultDialog);
        tryRobAgainDialog.setCancelable(false);
        tryRobAgainDialog.setCanceledOnTouchOutside(false);
        tryRobAgainDialog.setContentView(R.layout.dialog_collect);
        ((TextView) tryRobAgainDialog.findViewById(R.id.title)).setText("亲~");
        ((TextView) tryRobAgainDialog.findViewById(R.id.message)).setText("当前排队人数过多，是否继续抢票");
        ((TextView) tryRobAgainDialog.findViewById(R.id.tvDialogMistake)).setText("放弃");
        ((TextView) tryRobAgainDialog.findViewById(R.id.tvDialogRemove)).setText("继续");

        //放弃
        tryRobAgainDialog.findViewById(R.id.tvDialogMistake).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                tryRobAgainDialog.dismiss();
            }
        });
        //继续
        tryRobAgainDialog.findViewById(R.id.tvDialogRemove).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                tryRobAgainDialog.dismiss();

                setRobIngTickView();
                getScodeAddress();

            }
        });
        tryRobAgainDialog.show();
    }


    // 第三方登录了，但电话/email为空
    public void showUnRegDialog() {
        if (unRegDialog == null) {
            unRegDialog = new Dialog(mContext, R.style.dialog);
            unRegDialog.setCancelable(true);
            unRegDialog.setCanceledOnTouchOutside(false);
            unRegDialog.setContentView(R.layout.dialog_unreg);
            unRegDialog.findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    unRegDialog.dismiss();
                    startActivity(new Intent(mContext, BindPhoneBeforBuyActivity.class));
                }
            });
        }
        unRegDialog.show();
    }


    public void onEventMainThread(ChangeDetailsDialogEventBus eventBus) {
        getDetails();
    }

    public void onEventMainThread(ChangePaySuccessEventBus eventBus) {
        getDetails();
    }

    public void onEventMainThread(ChangeDetailsRegEventBus eventBus) {
        if (detailsInfo != null) {
            if (!detailsInfo.StaticMode)// 抢票活动不报名,留在这个页面
                regTicket();//第三方登录，绑定手机号之后
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        shareUtils.setShareSuccessListener(null);// dialog Unable to add window
        shareUtils = null;
        EventBus.getDefault().unregister(this);
        hasGetTheCode=true;//退出界面
        hasGetTheCodeAddress=true;//退出界面

        if(leftWebView!=null){
            leftWebView.destroy();
        }

        if(rightWebView!=null){
            rightWebView.destroy();
        }



        if(timer!=null){
            timer.cancel();
        }
    }


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
//==================================分享有关=========================================================


    private void share() {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            application.showMsg(R.string.network_check);
            return;
        }
        if (detailsInfo != null) {
            showDialogShare();
        }
    }


    private void showDialogShare() {
        shareDialog = new Dialog(mContext, R.style.DefaultDialog);
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

    //==================================收藏有关=========================================================




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


    //==================================关注有关=========================================================
    private boolean hasAttention = true;//是否已经关注
    private boolean isAttentioning = false;//正在请求中
    //    private boolean hasRequested = false;
    private int follows;//关注数

    private void attentionSponsor() {
        if (!application.checkIsLogin()) {// 如果未登陆就进入登陆页面
            startActivity(new Intent(mContext, LoginActivityNew.class));
            return;
        }
        if (isAttentioning)
            return;
        isAttentioning = true;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "";
        if (hasAttention) {// 已经关注后点击就送取消关注（不同的url请求）
            if (LoginUtil.isSporsor(detailsInfo.orgid)) {
                params.add(new BasicNameValuePair("id", detailsInfo.orgid));
                url = Url.ORG_UNFOLLOW;
            } else {
                params.add(new BasicNameValuePair("id", detailsInfo.createby));
                url = Url.USER_UNFOLLOW;
            }

        } else {// 没关注则关注
            if (LoginUtil.isSporsor(detailsInfo.orgid)) {
                params.add(new BasicNameValuePair("id", detailsInfo.orgid));
                url = Url.ORG_FOLLOW;
            } else {
                params.add(new BasicNameValuePair("id", detailsInfo.createby));
                url = Url.USER_FOLLOW;
            }
        }
        httpCilents.postA(url, params, new HttpCilents.WebServiceCallBack() {

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
//                        ToastUtil.showMsg(response.msg);
                        if (hasAttention) {//请求成功之前的状态为已关注，即ui要的逻辑应该是取消关注后的
                            tvAddAttention.setText("关注");
//                            tvAddAttention.setTextColor(Color.argb(255, 0x76, 0xbf, 0x47));
                            tvAddAttention.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.add_attention_no_frame), null, null, null);

                            follows = follows - 1;
                            tv_followNum.setText(follows + "");
//                            dbManager.insertSaveBeHavior(application.addBeHavior(21, 1 + "", orgBean.getId(), "", "", "", ""));
                            dbManager.insertSaveBeHavior(application.addBeHavior(21, 1 + "", detailsInfo.orgid, "", "", "", ""));//????
                        } else {
                            MobclickAgent.onEvent(mContext, "follow_organizer_count");

                            tvAddAttention.setText("已关注");
//                            tvAddAttention.setTextColor(Color.argb(255, 0x99, 0x99, 0x99));
                            tvAddAttention.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                            follows = follows + 1;
                            tv_followNum.setText(follows + "");
//                            dbManager.insertSaveBeHavior(application.addBeHavior(20, 1 + "", orgBean.getId(), "", "", "", ""));
                            dbManager.insertSaveBeHavior(application.addBeHavior(21, 1 + "", detailsInfo.orgid, "", "", "", ""));
                        }
                        hasAttention = !hasAttention;
                    }
                }
            }
        });
    }

    //====================================群聊有关===================================================================

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

   /* //    创建与主办方的私人会话
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
                        if (response.msg != null) {
                            ToastUtil.showMsg(response.msg);
                        }

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
    }*/

    private void ToChatActivity() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("isPrivateChat", true);// 私聊
        startActivity(intent);
    }

//===============================permissions==================================================


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否有WRITE_SETTINGS权限

            if (PermissionUtil.findDeniedPermissions(this.getActivity(), PermissionUtil.needPermissions).size() != 0) {
                PermissionUtil.checkPermissions(this.getActivity(), PermissionUtil.needPermissions);
            } else {
                startMapActivity();
            }
        } else {
            startMapActivity();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.PERMISSON_REQUESTCODE: {

                if (PermissionUtil.verifyPermissions(grantResults)) {
                    startMapActivity();
                } else {
                    application.showMsg("请先授予permission权限");
                }
            }
        }
    }
    //====================================webview===================================================================

    private WebView leftWebView;
    private WebView rightWebView;
    private void setupWebViewRight() {
        rightWebView=accuvallyDetailsActivity.detailRight.detail_right_webView;
        if(rightWebView!=null){
            rightWebView.getSettings().setJavaScriptEnabled(true);
            rightWebView.getSettings().setSupportZoom(true);
            rightWebView.getSettings().setBuiltInZoomControls(true);// 是否开启缩放
            rightWebView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
//        content = getIntent().getStringExtra("content");
            rightWebView.getSettings().setLoadWithOverviewMode(true);
            rightWebView.getSettings().setUseWideViewPort(true);
            rightWebView.setWebChromeClient(new WebChromeClient());
            rightWebView.loadDataWithBaseURL("about:blank", detailsInfo.desc, "text/html", "utf-8", null);
        }
    }

    private void setupWebViewLeft() {

        leftWebView = (WebView) rootView.findViewById(R.id.webView);
        leftWebView.getSettings().setJavaScriptEnabled(true);
        leftWebView.getSettings().setSupportZoom(true);
        leftWebView.getSettings().setBuiltInZoomControls(true);// 是否开启缩放
        leftWebView.getSettings().setDomStorageEnabled(true);// 是否开启Dom存储Api
//        content = getIntent().getStringExtra("content");
        leftWebView.getSettings().setLoadWithOverviewMode(true);
        leftWebView.getSettings().setUseWideViewPort(true);
        leftWebView.setWebChromeClient(new WebChromeClient());
        leftWebView.loadDataWithBaseURL("about:blank", detailsInfo.desc, "text/html", "utf-8", null);
//        leftWebView.loadUrl("http://www.huodongxing.com/event/"+detailsInfo.id);

    }

    //=================================有弹性的继续拖动，查看更多详情========================================================

    private void initScroll() {
        mLayout = (PullLayout) rootView.findViewById(R.id.tblayout);
        mLayout.setOnPullListener(this);
        mLayout.setOnContentChangeListener(this);
        mHeader = (ScrollView) rootView.findViewById(R.id.header);
        mFooter = (ScrollView) rootView.findViewById(R.id.footer);
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

                tabStripone.setVisibility(View.GONE);
                tabStriptwo.setVisibility(View.VISIBLE);
                viewpager.setScanScroll(true);
//			Log.d("tag", "SCREEN_HEADER");
                break;
            case PullLayout.SCREEN_FOOTER:
//            Toast.makeText(mContext,"onPageChanged,SCREEN_FOOTER",Toast.LENGTH_SHORT).show();
                tabStripone.setVisibility(View.VISIBLE);
                tabStriptwo.setVisibility(View.GONE);
                viewpager.setScanScroll(false);
//			Log.d("tag", "SCREEN_FOOTER");
                break;
        }
    }
}
