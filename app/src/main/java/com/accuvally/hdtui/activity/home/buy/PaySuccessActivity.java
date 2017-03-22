package com.accuvally.hdtui.activity.home.buy;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.AccuDetailBean;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.GroupMemberBean;
import com.accuvally.hdtui.model.MemberBean;
import com.accuvally.hdtui.model.RegSuccessInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.utils.DialogUtils;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ShareUtils;
import com.accuvally.hdtui.utils.Utils;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 报名成功后参与热聊
 * 
 */
public class PaySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView mTvAccuTile;// 活动名称
	private TextView mTvAccuDate;// 活动时间
	private TextView mTvAccuAdress;// 活动地址
	private TextView mTvTicketPrice;//票价
	private TextView mTvTicketType;//票种
	private Button mBtnCircleEnter;
	private GridView mGridView;
	private CommonAdapter<MemberBean> mAdapter;
	private ImageView mIvShare;
	private UMSocialService mController;
	private AccuDetailBean info;
	private RegSuccessInfo successInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success2);
		initView();
		initData();
	
	}

	public void initView() {

		mTvAccuTile = (TextView) findViewById(R.id.accu_title);
		mTvAccuDate = (TextView) findViewById(R.id.accu_time);
		mTvAccuAdress = (TextView) findViewById(R.id.accu_place);
		mTvTicketPrice = (TextView) findViewById(R.id.accu_ticket_price);
		mTvTicketType= (TextView) findViewById(R.id.accu_ticket_type);
		mBtnCircleEnter = (Button) findViewById(R.id.circle_enter);
		mBtnCircleEnter.setOnClickListener(this);
		
		mIvShare = (ImageView) findViewById(R.id.two_img);
		mIvShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialogShare();
			}
		});
		mIvShare.setVisibility(View.VISIBLE);
		((View)mIvShare.getParent()).setVisibility(View.VISIBLE);
		
		
		boolean flag =application.sharedUtils.readBoolean("IsFirstShowCircleGuide");
		if(flag){
			findViewById(R.id.circle_enter_attion).setVisibility(View.GONE);
		}

	}

	public void initData() {
		if(getIntent().getBooleanExtra("NeedApply", false)){
			setTitle("报名成功,待审核");
		}else{
			setTitle("报名成功");
		}
		info = (AccuDetailBean) getIntent().getSerializableExtra("DetailsInfo");
		if (info != null) {
			mTvAccuTile.setText(info.title);
			mTvAccuDate.setText( info.getTimeStr());
			mTvAccuAdress.setText(info.address);
		}

		mController = UMServiceFactory.getUMSocialService(Config.SOCIAL_UMENG);
		successInfo =  (RegSuccessInfo) getIntent().getSerializableExtra("info");
		if (successInfo != null) {
			mTvTicketPrice.setText("￥" + successInfo.getPrice());
			mTvTicketType.setText(successInfo.getTicketType());
		}
		setGridView();
	}

	private void setGridView() {
		mGridView = (GridView) findViewById(R.id.grid);
		mAdapter = new CommonAdapter<MemberBean>(this, R.layout.griditem_group) {
			@Override
			public void convert(ViewHolder viewHolder, MemberBean item, int position) {
				viewHolder.setText(R.id.tvMemberNick, item.Nick);
				viewHolder.setImageUrl(R.id.ivMemberLogo, item.Logo);
			}
		};
		mGridView.setAdapter(mAdapter);
		// 读取圈子成员
		initProgress();
		startProgress();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", info.id));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_GET_CIRCLE_MEMBER, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse resultInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (resultInfo.isSuccess()) {
						GroupMemberBean bean = JSON.parseObject(resultInfo.getResult(), GroupMemberBean.class);
						//只有10个人才显示
						if(bean.member!=null&&bean.member.size()>10){
							mAdapter.setList(bean.member);
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.circle_enter:// 进入圈子
            joinCircle(info.id);
			/*String sessionId = info.id;
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setSessionId(sessionId);
			sessionInfo.setTitle(info.title);
			sessionInfo.setLogoUrl(info.logo);
			SessionTable.insertSession(sessionInfo);
			EventBus.getDefault().post(new ChangeMessageEventBus(1));
			application.setCurrentSession(sessionInfo);
			startActivity(new Intent(mContext, ChatActivity.class));
			
			boolean flag = application.sharedUtils.readBoolean("IsFirstShowCircleGuide");
			if (flag) {
			} else {
				application.sharedUtils.writeBoolean("IsFirstShowCircleGuide", true);
			}
			findViewById(R.id.circle_enter_attion).setVisibility(View.GONE);*/
			break;
		case R.id.tvShareSina:// sina share
			shareUtils.shareAfterOauth(SHARE_MEDIA.SINA, 31, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvShareQzone:// Qzone share
			shareUtils.shareAfterOauth(SHARE_MEDIA.QZONE, 32, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewx:// weixin share
			shareUtils.share(SHARE_MEDIA.WEIXIN, 30, 0);
			shareDialog.dismiss();
			break;
		case R.id.tvSharewxpy:// py share
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
			mIntent.putExtra("sms_body", "我刚刚报名参加了：" + info.title + "," + info.getStartutc() + "，一起去参加吧？记得联系我。" + info.shareurl);// 短信内容
			startActivity(mIntent);
			shareDialog.dismiss();
            break;
		default:
			break;
		}
	}
	
	//分享相关
	private Dialog shareDialog;
	private ShareUtils shareUtils;
	public void dialogShare() {
		shareDialog = new Dialog(this, R.style.dialog);
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
		DialogUtils.dialogSet(shareDialog, this, Gravity.BOTTOM, 1.0, 1.0, true, true, true);
		shareDialog.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(info!=null){
			shareUtils = new ShareUtils(mContext, mController, info.id);
			shareUtils.initConfig(this, info.title, Html.fromHtml(info.summary).toString(), info.logo, info.shareurl);
		}
	}
	
	/**
	 * 加入圈子
	 */
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
					BaseResponse resultInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (resultInfo.isSuccess()) {
						application.showMsg("加入圈子成功");
						SessionInfo sessionInfo = new SessionInfo();
						sessionInfo.setSessionId(sessionId);
						sessionInfo.setTitle(info.title);
						sessionInfo.setLogoUrl(info.logo);
						SessionTable.insertSession(sessionInfo);
						EventBus.getDefault().post(new ChangeMessageEventBus(1));
						application.setCurrentSession(sessionInfo);
						startActivity(new Intent(mContext, ChatActivity.class));
						
						boolean flag = application.sharedUtils.readBoolean("IsFirstShowCircleGuide");
						if (flag) {
						} else {
							application.sharedUtils.writeBoolean("IsFirstShowCircleGuide", true);
						}
						findViewById(R.id.circle_enter_attion).setVisibility(View.GONE);
					} else {
						application.showMsg(resultInfo.getMsg());
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
