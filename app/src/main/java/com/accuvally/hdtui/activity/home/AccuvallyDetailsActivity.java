package com.accuvally.hdtui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
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


    private int isHuodong;//isHuodong 0表示活动行的，1表示活动推
    private boolean isRobTicket;

    public TextView tvDetailsColl;//收藏提示
    public ImageView ivDetailsColl;//收藏图片提示
    public  LinearLayout llCollect;//收藏layout

    public  LinearLayout llGroupChat,llEvaluate;//群聊，评价

    public View llRobTicket;// 抢票
    public View llEnroll;// 立即报名
    public TextView tvCheapTicket;//特价抢票

    public TextView tvDetailsRegTicket;//报名文字

    public DetailRight detailRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuvally_details2);

        parseIntent();
        initView();
        setTabsValue();
    }

    private void parseIntent() {
        homeId = getIntent().getStringExtra("id");
        isHuodong = getIntent().getIntExtra("isHuodong", 0);
        isRobTicket = getIntent().getBooleanExtra("isRobTicket", false);
        ToCommentActivity = getIntent().getBooleanExtra(GetuiPushMessageReceiver.ToCommentActivity, false);
        TOCommentDisplayActivity_comment = getIntent().getBooleanExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment, false);
        TOCommentDisplayActivity_consult = getIntent().getBooleanExtra(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult, false);

    }

    private void initView() {

        initProgress();

        tabStripone=(TextView) findViewById(R.id.tabStripone);
        share_ly= (LinearLayout) findViewById(R.id.share_ly);


        tvDetailsColl = (TextView) findViewById(R.id.tvDetailsColl);//收藏与否文字提示
        ivDetailsColl = (ImageView) findViewById(R.id.ivDetailsColl);//收藏与否图标提示
        llCollect= (LinearLayout) findViewById(R.id.llCollect);
//        llCollect.setOnClickListener(this);// 收藏

        llGroupChat= (LinearLayout) findViewById(R.id.llGroupChat);
//        llGroupChat.setOnClickListener(this);// 群聊

        llEvaluate= (LinearLayout) findViewById(R.id.llEvaluate);
//        llEvaluate.setOnClickListener(this);// 评价

        tvDetailsRegTicket = (TextView) findViewById(R.id.tvDetailsRegTicket);//报名文字
//        tvDetailsRegTicket.setOnClickListener(this);
        llEnroll = findViewById(R.id.llEnroll);//报名layout

        llRobTicket = findViewById(R.id.llIsRobTicket);// 抢票layout
//        llRobTicket.setOnClickListener(this);

        tvCheapTicket = (TextView) findViewById(R.id.tvCheapTicket);//抢票提示
    }

    public DetailLeft newInstance(String homeId, int isHuodong,
                                  boolean isRobTicket,boolean ToCommentActivity,
                                  boolean TOCommentDisplayActivity_comment,boolean TOCommentDisplayActivity_consult) {
        DetailLeft f = new DetailLeft();
        Bundle b = new Bundle();
        b.putString("id", homeId);
        b.putInt("isHuodong", isHuodong);
        b.putBoolean("isRobTicket", isRobTicket);
        b.putBoolean(GetuiPushMessageReceiver.ToCommentActivity,ToCommentActivity);
        b.putBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_comment,TOCommentDisplayActivity_comment);
        b.putBoolean(GetuiPushMessageReceiver.TOCommentDisplayActivity_consult,TOCommentDisplayActivity_consult);
        f.setArguments(b);
        return f;
    }



    private void setTabsValue() {
        fragments.add(newInstance( homeId,  isHuodong,  isRobTicket, ToCommentActivity,
                TOCommentDisplayActivity_comment,TOCommentDisplayActivity_consult));
        detailRight=new DetailRight();
        fragments.add(detailRight);


        viewpager = (CustomViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        viewpager.setAdapter(new DetailFragementAdapter(getSupportFragmentManager(), fragments));
        tabStriptwo = (PagerSlidingTabStrip) findViewById(R.id.tabStriptwo);
        tabStriptwo.setViewPager(viewpager);
    }

}