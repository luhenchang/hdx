package com.accuvally.hdtui.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.web.WebActivity;

/**
 * Created by Andy Liu on 2017/6/14.
 */
public class MoreServiceActivity extends BaseActivity implements View.OnClickListener {


    public static final String BBX_URL="http://bbx.huodongxing.com/bbx?app";
    public static final String MAIMAI_URL="https://maimai.cn/article/live_court";
    public static final String HOST_URL="http://www.huodongxing.com/guanjia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_service);
        initProgress();
        initView();
    }

    public void initView() {
        setTitle("更多服务");
        findViewById(R.id.more_service_host).setOnClickListener(this);
        findViewById(R.id.more_service_bbx).setOnClickListener(this);
        findViewById(R.id.more_service_maimai).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_service_host:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HOST_URL)));
//                startActivity(new Intent(mContext, WebActivity.class).putExtra("loadingUrl",
//                        HOST_URL).putExtra("injectJs", ""));
                break;
            case R.id.more_service_bbx:
                startActivity(new Intent(mContext, WebActivity.class).putExtra("loadingUrl",
                        BBX_URL).putExtra("injectJs", ""));
                break;
            case R.id.more_service_maimai:
                startActivity(new Intent(mContext, WebActivity.class).putExtra("loadingUrl",
                        MAIMAI_URL).putExtra("injectJs", ""));
                break;

        }
    }


}
