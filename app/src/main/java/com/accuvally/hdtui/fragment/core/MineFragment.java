package com.accuvally.hdtui.fragment.core;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.mine.CollectActivity;
import com.accuvally.hdtui.activity.mine.FriendsActivity;
import com.accuvally.hdtui.activity.mine.MoreServiceActivity;
import com.accuvally.hdtui.activity.mine.OrgListActivity;
import com.accuvally.hdtui.activity.mine.PublishActivity;
import com.accuvally.hdtui.activity.mine.TicketTabActivity;
import com.accuvally.hdtui.activity.mine.WaitForEvaluateActivity;
import com.accuvally.hdtui.activity.mine.login.LoginActivityNew;
import com.accuvally.hdtui.activity.mine.personal.PersonalActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.BadgeView;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.PickPhoto;
import com.accuvally.hdtui.utils.Trace;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.chunk.UploadChunkUtil;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 活动圈子
 * 
 * @author Semmer Wang
 * 
 */
public class MineFragment extends BaseFragment implements OnClickListener {

	private LinearLayout lyMineTicket, lyMineCollect, lyMineOrg, lyMineCircle,lyMineMore;
    private LinearLayout  lyMinePublish,lyMineEvaluate;

	private CircleImageView ivMineLogo;

	private TextView tvMineNickName, tvMineSignature;

	private TextView tvMineUnReadNum;
    private ImageView mineEvaluateUnread;

	BadgeView unread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String path = null;
            switch (requestCode) {

                case PickPhoto.PICK_PHOTO://从相册中选择
                    try {
                        Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        path= cursor.getString(1);
                        Trace.e("TAG","相册中原图片路径："+cursor.getString(1));
                        cursor.close();
                        if (path != null) {
                            UploadChunkUtil uploadChunkUtil=new UploadChunkUtil();
                            uploadChunkUtil.chunkAndUpload(path);
//                            uploadChunkUtil.uploadChunk2(AccuApplication.getInstance(), new HttpCilents(mContext),path,"fileName");
                        }
                    } catch (Exception e) {
                        application.showMsg("请选择相册中的照片");
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
		EventBus.getDefault().register(this);
		initView(rootView);
		return rootView;
	}


    @Override
	public void onResume() {
		super.onResume();
		initUserDetails();
        if(application.checkIsLogin()){
            getUserInfo(AccuApplication.getInstance());
        }
	}


    public  void getUserInfo(final AccuApplication application) {
        HttpCilents httpCilents=new HttpCilents(application);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        showProgress("正在获取用户资料");
        httpCilents.get(httpCilents.printURL(Url.ACCUPASS_USER_INFO, params), new HttpCilents.WebServiceCallBack() {

            @Override
            public void callBack(int code, Object result) {
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse info = JSON.parseObject(result.toString(), BaseResponse.class);
                        if (info.isSuccess()) {
                            UserInfo userInfo = JSON.parseObject(info.getResult(), UserInfo.class);
                            application.setUserInfo(userInfo);

                            if(userInfo.isHasComment()){//有待评价的
                                mineEvaluateUnread.setVisibility(View.VISIBLE);
                            }

                            if(userInfo.isHasPublish()){//有发布的
                                lyMinePublish.setVisibility(View.VISIBLE);
                            }

                        } else {
                            application.showMsg(info.getMsg());
                        }
                        break;
                    case Config.RESULT_CODE_ERROR:
                        application.showMsg(result.toString());
                        break;
                }
            }
        });
    }

	public void initView(View view) {
		lyMineTicket = (LinearLayout) view.findViewById(R.id.lyMineTicket);
        lyMinePublish = (LinearLayout) view.findViewById(R.id.lyMinePublish);
        lyMineEvaluate= (LinearLayout) view.findViewById(R.id.lyMineEvaluate);

		lyMineCollect = (LinearLayout) view.findViewById(R.id.lyMineCollect);
        lyMineMore = (LinearLayout) view.findViewById(R.id.lyMineMore);
		lyMineOrg = (LinearLayout) view.findViewById(R.id.lyMineOrg);
		lyMineCircle = (LinearLayout) view.findViewById(R.id.lyMineCircle);
		ivMineLogo = (CircleImageView) view.findViewById(R.id.ivMineLogo);
		tvMineNickName = (TextView) view.findViewById(R.id.tvMineNickName);
		tvMineSignature = (TextView) view.findViewById(R.id.tvMineSignature);
		tvMineUnReadNum = (TextView) view.findViewById(R.id.tvMineUnReadNum);

        mineEvaluateUnread = (ImageView) view.findViewById(R.id.mineEvaluateUnread);

        view.findViewById(R.id.mine_test).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


//              /*  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("**/*//*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent,1);*/

                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent2, PickPhoto.PICK_PHOTO);

//                Intent intent = new Intent(mContext, BindPhoneBeforBuyActivity.class);
//                startActivity(intent);
//                boolean b=NotificationsUtils.isNotificationEnabled(mContext);
//                application.showMsg("isNotificationEnabled: "+b);
//
//                Trace.e("MineFragment", "isNotificationEnabled: " + b);

//                Intent intent = new Intent(Android.permission.BROADCAST_WAP_PUSH);
//                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);

//                String packageName = mContext.getApplicationContext().getPackageName();
//                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
//                launchIntent.putExtras(launchIntent);
//                mContext.startActivity(launchIntent);


//                Intent manageAppIntent = new Intent();
//                manageAppIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                //第二个参数为包名
//
//                manageAppIntent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                startActivity(manageAppIntent);
            }
        });

        view.findViewById(R.id.mine_test2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                PermissionUtil.checkPermissions(getActivity(), PermissionUtil.needPermissions);
//                Intent intent = new Intent(Settings.);
//                mContext.startActivity(intent);
//                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);

//                MessageFragment.getMessageCount("sys_msg");
//                MessageFragment.getMessageCount("recommend");
//                MessageFragment.getMessageCount("new_friend");

//                Intent manageAppsIntent =  new Intent();
//                manageAppsIntent.setAction("android.intent.action.MAIN");
//                manageAppsIntent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
//                startActivity(manageAppsIntent);
            }
        });

        view.findViewById(R.id.mine_test3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//                mContext.startActivity(intent);

//                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);

//                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                startActivity(intent);
            }
        });

		lyMineTicket.setOnClickListener(this);
        lyMineEvaluate.setOnClickListener(this);
        lyMinePublish.setOnClickListener(this);
		lyMineCollect.setOnClickListener(this);
        lyMineMore.setOnClickListener(this);
		lyMineOrg.setOnClickListener(this);
		lyMineCircle.setOnClickListener(this);
		ivMineLogo.setOnClickListener(this);

		unread = new BadgeView(mContext, tvMineUnReadNum);
		unread.setBackgroundResource(R.drawable.leancloud_unreadnum_bg);
		unread.setTextColor(mContext.getResources().getColor(android.R.color.white));
		unread.setTextSize(12);
		unread.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
		unread.setGravity(Gravity.CENTER);
		unread.setBadgeMargin(10);
		unread.setPadding(0, 0, 0, 0);
		unread.hide();
	}

//	public void updateUnreadNum() {
//		String userId = application.getUserInfo().getAccount();
//		if (application.checkIsLogin() && dbManager.queryUnReadNum(0, userId) > 0) {
//			unread.setText(String.valueOf(dbManager.queryUnReadNum(0, userId)));
//			unread.show();
//			if (dbManager.queryUnReadNum(0, userId) > 100) {
//				unread.setText("99+");
//			} else {
//				unread.setText(String.valueOf(dbManager.queryUnReadNum(0, userId)));
//			}
//		} else {
//			unread.hide();
//		}
//	}

	public void initUserDetails() {
		if (application.checkIsLogin()) {
			application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivMineLogo, UILoptions.defaultUser);
			tvMineNickName.setText(application.getUserInfo().getNick());
			if ("".equals(application.getUserInfo().getBrief())) {
				tvMineSignature.setText("我就是这样的一个人儿");
			} else {
				tvMineSignature.setText(application.getUserInfo().getBrief());
			}
			tvMineSignature.setVisibility(View.VISIBLE);
		} else {
			tvMineNickName.setText("未登录");
			ivMineLogo.setImageResource(R.drawable.icon);
			tvMineSignature.setVisibility(View.INVISIBLE);
		}
	}





	public void onEventMainThread(ChangeUserStateEventBus eventBus) {
		initUserDetails();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lyMineCircle:// 同伴
			if (Utils.isFastDoubleClick())
				return;
			if (application.checkIsLogin())
				toActivity(FriendsActivity.class);
			else
				toActivity(LoginActivityNew.class);
			break;
            case R.id.lyMinePublish:// 我发布的
                if (Utils.isFastDoubleClick())
                    return;
                if (application.checkIsLogin())
                    toActivity(PublishActivity.class);
                else
                    toActivity(LoginActivityNew.class);
                break;
            case R.id.lyMineEvaluate:// 待评价的
                if (Utils.isFastDoubleClick())
                    return;
                if (application.checkIsLogin())
                    toActivity(WaitForEvaluateActivity.class);
                else
                    toActivity(LoginActivityNew.class);
                break;
		case R.id.lyMineTicket:// 票券
			if (Utils.isFastDoubleClick())
				return;
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_MY_TICKETS",""));
			if (application.checkIsLogin())
				toActivity(TicketTabActivity.class);
			else
				toActivity(LoginActivityNew.class);
			break;
		case R.id.lyMineOrg:// 主办方
			if (Utils.isFastDoubleClick())
				return;
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_MY_FOLLOW_ORG",""));
			if (application.checkIsLogin())
				toActivity(OrgListActivity.class);
			else
				toActivity(LoginActivityNew.class);
			break;
		case R.id.lyMineCollect:// 收藏
			if (Utils.isFastDoubleClick())
				return;
			dbManager.insertSaveBeHavior(application.addBeHavior(100, "", "", "", "", "APP_MY_FOLLOW_EVENT",""));
			if (application.checkIsLogin())
				toActivity(CollectActivity.class);
			else
				toActivity(LoginActivityNew.class);
			break;

            case R.id.lyMineMore:// 更多服务
                if (Utils.isFastDoubleClick())
                    return;
                    toActivity(MoreServiceActivity.class);
                break;
		case R.id.ivMineLogo:
			if (Utils.isFastDoubleClick())
				return;
			if (application.checkIsLogin()){

                toActivity(PersonalActivity.class);
            }
			else
				toActivity(LoginActivityNew.class);
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
}
