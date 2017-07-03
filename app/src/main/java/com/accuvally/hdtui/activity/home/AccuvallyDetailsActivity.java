package com.accuvally.hdtui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.entry.LinkedMiddleActivity;
import com.accuvally.hdtui.fragment.detailfragment.CustomViewPager;
import com.accuvally.hdtui.fragment.detailfragment.DetailFragementAdapter;
import com.accuvally.hdtui.fragment.detailfragment.DetailLeft;
import com.accuvally.hdtui.fragment.detailfragment.DetailRight;
import com.accuvally.hdtui.push.GetuiPushMessageReceiver;
import com.accuvally.hdtui.ui.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
//分享有关
//收藏有关
//关注有关
//群聊有关

@SuppressWarnings("unused")
public class AccuvallyDetailsActivity extends BaseActivity {


    public CustomViewPager viewpager;
    public PagerSlidingTabStrip tabStriptwo;

    public TextView tabStripone;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    public LinearLayout share_ly;//分享


    private String homeId;
    private boolean ToCommentActivity = false;//是否加载完就跳转到评价界面
    private boolean TOCommentDisplayActivity_comment = false;//是否加载完就跳转到评价展示界面
    private boolean TOCommentDisplayActivity_consult = false;//是否加载完就跳转到咨询展示界面

    public boolean LinkedActivity_HASCOUPON = false;//是否加载完就跳转到咨询展示界面
    public String LinkedActivity_COUPON;

    private int isHuodong;//isHuodong 0表示活动行的，1表示活动推


    public TextView tvDetailsColl;//收藏提示
    public ImageView ivDetailsColl;//收藏图片提示
    public  LinearLayout llCollect;//收藏layout

    public  LinearLayout llGroupChat,llEvaluate;//群聊，评价
    public   TextView tvGroupChat;//群聊textview
    public LinearLayout llRegTicket;//普通底部layout
    public TextView tvRobTicket;//抢票文字
    public TextView tvDetailsRegTicket;//报名文字

    public DetailRight detailRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuvally_details);

        parseIntent();
        initView();
        setTabsValue();
    }

    private void parseIntent() {

        homeId = getIntent().getStringExtra("id");
        isHuodong = getIntent().getIntExtra("isHuodong", 0);
        ToCommentActivity = getIntent().getBooleanExtra(GetuiPushMessageReceiver.ToCommentActivity, false);
        TOCommentDisplayActivity_comment = getIntent().getBooleanExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment, false);
        TOCommentDisplayActivity_consult = getIntent().getBooleanExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult, false);


        LinkedActivity_HASCOUPON= getIntent().getBooleanExtra(LinkedMiddleActivity.LinkedActivity_HASCOUPON, false);
        LinkedActivity_COUPON= getIntent().getStringExtra(LinkedMiddleActivity.LinkedActivity_COUPON);

    }

    private void initView() {

        initProgress();

        tabStripone=(TextView) findViewById(R.id.tabStripone);
        share_ly= (LinearLayout) findViewById(R.id.share_ly);


        tvDetailsColl = (TextView) findViewById(R.id.tvDetailsColl);//收藏与否文字提示
        ivDetailsColl = (ImageView) findViewById(R.id.ivDetailsColl);//收藏与否图标提示
        llCollect= (LinearLayout) findViewById(R.id.llCollect);

        llGroupChat= (LinearLayout) findViewById(R.id.llGroupChat);
        tvGroupChat= (TextView) findViewById(R.id.tvGroupChat);
        llEvaluate= (LinearLayout) findViewById(R.id.llEvaluate);

        tvDetailsRegTicket = (TextView) findViewById(R.id.tvDetailsRegTicket);//报名文字

        tvRobTicket = (TextView) findViewById(R.id.tv_robTicket);//抢票提示
        llRegTicket = (LinearLayout) findViewById(R.id.ll_regTicket);
    }

    public DetailLeft newInstance(String homeId, int isHuodong, boolean ToCommentActivity,
                                  boolean TOCommentDisplayActivity_comment,boolean TOCommentDisplayActivity_consult) {
        DetailLeft f = new DetailLeft();
        Bundle b = new Bundle();
        b.putString("id", homeId);
        b.putInt("isHuodong", isHuodong);

        b.putBoolean(GetuiPushMessageReceiver.ToCommentActivity,ToCommentActivity);
        b.putBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment,TOCommentDisplayActivity_comment);
        b.putBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult,TOCommentDisplayActivity_consult);
        f.setArguments(b);
        return f;
    }



    private void setTabsValue() {

        fragments.add(newInstance( homeId,isHuodong,ToCommentActivity,
                TOCommentDisplayActivity_comment,TOCommentDisplayActivity_consult));

        detailRight=new DetailRight();
        fragments.add(detailRight);


        viewpager = (CustomViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        viewpager.setAdapter(new DetailFragementAdapter(getSupportFragmentManager(), fragments));
        tabStriptwo = (PagerSlidingTabStrip) findViewById(R.id.tabStriptwo);
        tabStriptwo.setViewPager(viewpager);
    }

}