package com.accuvally.hdtui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.MsgServices;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.MsgAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.model.AnnounceBean;
import com.accuvally.hdtui.model.AnnounceInfo;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.NotificationInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.CircleFlowIndicator;
import com.accuvally.hdtui.ui.CirclePageIndicator;
import com.accuvally.hdtui.ui.EmoteEditText;
import com.accuvally.hdtui.ui.MyGridView;
import com.accuvally.hdtui.ui.RecordView;
import com.accuvally.hdtui.ui.SmileGridAdapter;
import com.accuvally.hdtui.ui.ViewFlow;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ImageTools;
import com.accuvally.hdtui.utils.ImageTools.Info;
import com.accuvally.hdtui.utils.MediaManager;
import com.accuvally.hdtui.utils.PickPhoto;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class ChatActivity extends BaseActivity implements OnClickListener, OnItemClickListener, IXListViewListener {

	private AVIMConversation mConversation;
	private XListView mListView;
	private MsgAdapter mAdapter;
	private SessionInfo sessionInfo;
	private String sessionId;
	private String userId;
	private Button mBtnTextSender;
	private Button mBtnPicSend;
	private Button mBtnRecord;
	private Button mBtnRecording;
	private Button mBtnSoft;// 软件盘按钮，回归文字输入
	private EmoteEditText mEdtEmotic;
	private LinearLayout mPicLayout;
	private String OUTPUTFILEPATH;// 照片的路径
	private FileCache fileCache;// 照片缓存
	private RecordView mRecordView;
    //表情有关
	private LinearLayout lyContainer;
	private ViewFlow viewflow;
	private GridView myGridView;
	private CircleFlowIndicator indicator;
	private Button mEmote;
	// 当前activity是否处于可见状态
	public static boolean isShown = false;
	private int pageIndex = 1, pageSize = 18;
	private UserInfo mAtUser;
	private boolean isPrivateChat;
	
	private String title;
	private PopupWindow popupWindow;
	private View topView;
	private CirclePageIndicator circleIndicator;
	private ViewPager mViewPager;
	private CheckBox cbNotice;
	private boolean existPrivateSession;
	private List<AnnounceBean> listAnnounce;
	private TextView tvNoticeContent;
	private String creator;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initProgress();
		EventBus.getDefault().register(this);
		isPrivateChat = getIntent().getBooleanExtra("isPrivateChat", false);

		ImageView ivChatMore = (ImageView) findViewById(R.id.ivChatMore);
		if (isPrivateChat) {
			ivChatMore.setImageResource(R.drawable.chat_user);
		} else {
			ivChatMore.setImageResource(R.drawable.chat_group);
		}
		
		dbManager = new DBManager(this);
		onIntent();
		fileCache = new FileCache(mContext);
		mRecordView = new RecordView(this, findViewById(R.id.voice_rcd_hint_window));
		initView();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = dbManager.queryAllMsgCount(sessionId) / pageSize - 1;
				loadDate();
				// 移动到最底部
				mListView.setSelection(mAdapter.getCount());
			}
		}, 500);

		// 不弹出软键盘
		mEdtEmotic.clearFocus();
		if (!isPrivateChat) {
			requestAnnouncement();
		}
	}

	private void onIntent() {
		userId = application.getUserInfo().getAccount();
		sessionInfo = application.getCurrentSession();
		sessionId = sessionInfo.getSessionId();
		setTitle("".equals(sessionInfo.getTitle()) ? sessionInfo.getNickName() : sessionInfo.getTitle());
		
		mConversation = AVIMClient.getInstance(userId).getConversation(sessionId);
		
		if (!TextUtils.isEmpty(sessionId)) {
			// 异步加载数据
			Intent i = new Intent();
			Bundle extras = new Bundle();
			extras.putSerializable("user", application.getUserInfo());
			extras.putSerializable("session", sessionInfo);
			i.putExtras(extras);
			i.setClass(this, MsgServices.class);
			startService(i);
		}
	}

	private void loadDate() {
		if (TextUtils.isEmpty(sessionId)) {
			application.showMsg("无此会话");
			return;
		}

		MessageInfo last = dbManager.queryLastMsg(sessionId);
		if (null != last && userId != null) {
			ArrayList<MessageInfo> list = dbManager.queryAllMsg(sessionId, pageIndex * pageSize, dbManager.queryAllMsgCount(sessionId));
			if (list.size() > 0) {
				mAdapter.setList(list);
			}
		}
	}

	public void onEventMainThread(ChangeMessageEventBus eventBus) {
//		if (eventBus.getMsgId() == 2) {
//			mAdapter.removeAll();
//			ArrayList<MessageInfo> list = dbManager.queryAllMsg(sessionId, pageIndex * pageSize, dbManager.queryAllMsgCount(sessionId));
//			mAdapter.setList(list);
//			// 滚动到最底部
//			mListView.setSelection(mAdapter.getCount());
//		}
		
		MessageInfo message = eventBus.getMessageInfo();
		if (message != null && sessionId.equals(eventBus.sessionId)) {
			mAdapter.add(message);
			mListView.setSelection(mAdapter.getCount());
			if (message.message_type == NotificationInfo.ANNOUNCE) {
				application.sharedUtils.writeString(Keys.announceContent + sessionId, message.getTextStr());
				tvNoticeContent.setText(message.getTextStr());
				setAnnounceContent();
				popupWindow.showAsDropDown(topView);
			}
		}
	}

	public void initView() {
		mBtnTextSender = (Button) findViewById(R.id.chat_send);
		mBtnTextSender.setOnClickListener(this);
		mBtnPicSend = (Button) findViewById(R.id.chat_pic);
		mBtnPicSend.setOnClickListener(this);
		mBtnRecord = (Button) findViewById(R.id.chat_record);
		mBtnRecord.setOnClickListener(this);

		findViewById(R.id.llGroupMember).setOnClickListener(this);

		mBtnRecording = (Button) findViewById(R.id.chat_recording);
		mRecordView.setBtnRecorder(mBtnRecording);
		mBtnRecording.setOnTouchListener(mRecordView.getTouchListener());
		mPicLayout = (LinearLayout) findViewById(R.id.chat_pic_layout);
		mEdtEmotic = (EmoteEditText) findViewById(R.id.chat_textditor_eet_editer);
		mEdtEmotic.addTextChangedListener(mWatcher);
		mEdtEmotic.setOnClickListener(this);

		findViewById(R.id.chat_pic_camero).setOnClickListener(this);
		findViewById(R.id.chat_pic_file).setOnClickListener(this);
		
		mBtnSoft = (Button) findViewById(R.id.chat_softinput);
		mBtnSoft.setOnClickListener(this);
		mListView = (XListView) findViewById(R.id.listview);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);

		creator = mConversation.getCreator();
		if (TextUtils.isEmpty(creator)) {
			creator = sessionInfo.creator;
		}
		Log.d("conversation creator:", creator + "");
		mAdapter = new MsgAdapter(this, isPrivateChat, creator);
		// 不设置这个无法显示数据
		mAdapter.setList(new ArrayList<MessageInfo>());
		mListView.setAdapter(mAdapter);

		lyContainer = (LinearLayout) findViewById(R.id.chat_smile_container);
		SmileGridAdapter smileAdapter = new SmileGridAdapter(mContext);
		viewflow = (ViewFlow) findViewById(R.id.common_viewflow);
		indicator = (CircleFlowIndicator) findViewById(R.id.common_indicator);

		viewflow.setAdapter(smileAdapter);
		viewflow.setFlowIndicator(indicator);

		myGridView = (MyGridView) viewflow.getChildAt(0);
		myGridView.setOnItemClickListener(this);
		measureView(myGridView);
		viewflow.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, myGridView.getMeasuredHeight()));
		viewflow.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {

			@Override
			public void onSwitched(View view, int position) {
				myGridView = (MyGridView) view;
				myGridView.setOnItemClickListener(ChatActivity.this);
			}
		});

		mEmote = (Button) findViewById(R.id.chat_textditor_biaoqing);
		mEmote.setOnClickListener(this);

		topView = findViewById(R.id.topView);
		cbNotice = (CheckBox) findViewById(R.id.cbNotice);

		if (isPrivateChat) {
			cbNotice.setVisibility(View.GONE);
		} else {
			cbNotice.setVisibility(View.VISIBLE);
			initPopupWindow();
			cbNotice.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						popupWindow.showAsDropDown(topView);
					} else {
						popupWindow.dismiss();
					}
				}
			});
		}
		
	}
	
	private void initPopupWindow() {
		View pView = getLayoutInflater().inflate(R.layout.popupview_notice, null);
		popupWindow = new PopupWindow(pView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.dialogWindowAnim);
		popupWindow.setFocusable(true);
		Drawable drawable = getResources().getDrawable(R.color.transparent);
		popupWindow.setBackgroundDrawable(drawable);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				cbNotice.setChecked(false);
			}
		});

		tvNoticeContent = (TextView) pView.findViewById(R.id.tvNoticeContent);
		tvNoticeContent.setMovementMethod(LinkMovementMethod.getInstance());
		String announceContent = application.sharedUtils.readString(Keys.announceContent + sessionId);
		tvNoticeContent.setText(announceContent);
		setAnnounceContent();
//		if (TextUtils.isEmpty(announceContent)) {
//			tvNoticeContent.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					popupWindow.showAsDropDown(topView);
//				}
//			});
//		}
		
		tvNoticeContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GroupAnnounceActivity.class);
				intent.putExtra("cid", sessionId);
				intent.putExtra("creator", creator);
				startActivity(intent );
			}
		});
	}

	private void setAnnounceContent() {
		CharSequence text = tvNoticeContent.getText();
		if (text instanceof Spannable) {
			Spannable sp = (Spannable) text;
			URLSpan[] urlSpans = sp.getSpans(0, text.length(), URLSpan.class);

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();

			for (URLSpan urlSpan : urlSpans) {
				final String url = urlSpan.getURL();
				ClickableSpan clickableSpan = new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						if (url.indexOf("huodongxing.com/event") != -1 || url.indexOf("huodongxing.com/go") != -1) {
							startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", url));
						} else {
							Intent intent = new Intent(mContext, GeTuiWapActivity.class);
							intent.putExtra("title", "群公告");
							intent.putExtra("url", url);
							startActivity(intent);
						}
					}
				};
				style.setSpan(clickableSpan, sp.getSpanStart(urlSpan), sp.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			
			tvNoticeContent.setText(style);
		}
	}
	
	/** 获取公告内容 **/
	protected void requestAnnouncement() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", sessionId));

		startProgress();
		httpCilents.get(httpCilents.printURL(Url.announcement, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				stopProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}

				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					if (response.isSuccess()) {
						AnnounceInfo info = JSON.parseObject(response.result, AnnounceInfo.class);
						application.sharedUtils.writeString(Keys.announceContent + sessionId, info.content);
						tvNoticeContent.setText(info.content);
						setAnnounceContent();
						if (!TextUtils.isEmpty(info.content)) {
							popupWindow.showAsDropDown(topView);
						}
					}
				}
			}
		});
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void hideInputMethod() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private TextWatcher mWatcher = new TextWatcher() {

		private int curIndex = -1;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			System.out.println("  start =" + start + " before  " + before + "  count =" + count);
			curIndex = start - before;
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (TextUtils.isEmpty(s)) {
				mBtnPicSend.setVisibility(View.VISIBLE);
				mBtnTextSender.setVisibility(View.GONE);
			} else {
				if (curIndex >= 0 && curIndex < s.length()) {
					char a = s.charAt(curIndex);
					mBtnPicSend.setVisibility(View.GONE);
					mBtnTextSender.setVisibility(View.VISIBLE);
					if (!isPrivateChat && getAtCount(s.toString()) < 2) {
						if (a == '@' || a == '＠') {
							Intent intent = new Intent(ChatActivity.this, AtActivity.class);
							intent.putExtra("sessionID", sessionInfo.getSessionId());
							startActivityForResult(intent, 100);
							mAtUser = null;
						}
					}
				}
			}
		}
	};

	private int getAtCount(String text) {
		char[] arrs = text.toCharArray();
		int count = 0;
		for (int i = 0; i < arrs.length; i++) {
			if (arrs[i] == '@' || arrs[i] == '＠') {
				count++;
			}
		}
		return count;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_record:// 录音按钮
			mBtnRecording.setVisibility(View.VISIBLE);
			mEdtEmotic.setVisibility(View.GONE);
			v.setVisibility(View.GONE);
			mBtnSoft.setVisibility(View.VISIBLE);
			this.hideBottomLayout();
			this.hideInputMethod();
			break;
		case R.id.chat_pic:// 添加图片按钮
			if (mPicLayout.getVisibility() == View.GONE) {
				mPicLayout.setVisibility(View.VISIBLE);
			} else {
				mPicLayout.setVisibility(View.GONE);
			}
			lyContainer.setVisibility(View.GONE);
			break;
		case R.id.chat_pic_camero:// 拍照
			OUTPUTFILEPATH = fileCache.getImageCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
			PickPhoto.takePhoto(ChatActivity.this, OUTPUTFILEPATH);
			break;
		case R.id.chat_pic_file:// 从图库获取相册
			PickPhoto.pickPhoto(ChatActivity.this);

			break;
		case R.id.chat_send:
			sendText(mEdtEmotic.getText().toString());
			mPicLayout.setVisibility(View.GONE);// 隐藏图片上传按钮
			lyContainer.setVisibility(View.GONE);// 隐藏表情
			break;
		case R.id.chat_softinput:// 软件盘
			v.setVisibility(View.GONE);
			mBtnRecording.setVisibility(View.GONE);
			mEdtEmotic.setVisibility(View.VISIBLE);// 显示输入框
			mBtnRecord.setVisibility(View.VISIBLE);
			this.hideBottomLayout();
			mEdtEmotic.requestFocus();
			break;
		case R.id.chat_textditor_biaoqing:
			mPicLayout.setVisibility(View.GONE);
			mBtnRecording.setVisibility(View.GONE);
			mEdtEmotic.setVisibility(View.VISIBLE);// 显示输入框
			showSmileContainer();
			break;
		case R.id.chat_textditor_eet_editer:
			mPicLayout.setVisibility(View.GONE);// 隐藏图片上传界面
			lyContainer.setVisibility(View.GONE);// 隐藏表情
			mListView.setSelection(mAdapter.getCount());
			mListView.smoothScrollToPosition(mAdapter.getCount());
			mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			break;

		case R.id.llGroupMember://群信息
			if (sessionInfo != null && !TextUtils.isEmpty(sessionInfo.getSessionId())) {
				Intent intent = new Intent(this, GroupMemberActivity.class);
				intent.putExtra("title", sessionInfo.getTitle());
				intent.putExtra("cid", sessionInfo.getSessionId());
				intent.putExtra("creator", creator);
				intent.putExtra("isPrivateChat", isPrivateChat);
				startActivityForResult(intent, 123);
			}
			break;
		}
	}

	public void hideBottomLayout() {
		mPicLayout.setVisibility(View.GONE);// 隐藏图片上传按钮
		lyContainer.setVisibility(View.GONE);// 隐藏表情
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("dd", "requestCode " + requestCode + "  resultCode =" + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PickPhoto.TAKE_PHOTO:
				try {
					Info info = ImageTools.savePhotoToSDCard(OUTPUTFILEPATH, 1280, 720);
					if (info != null) {
						MobclickAgent.onEvent(mContext, "groupchat_takepic_count");
						sendPic(info);
					}
				} catch (Exception e1) {
					application.showMsg(e1.getMessage());
				}
				break;
			case PickPhoto.PICK_PHOTO:
				try {
					Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
					cursor.moveToFirst();
					Info info = ImageTools.savePhotoToSDCard(cursor.getString(1), 1280, 720);
					if (info != null) {
						sendPic(info);
					}
				} catch (Exception e) {
					application.showMsg("请选择相册中的照片");
				}
				break;
			case 100:
				mAtUser = (UserInfo) data.getSerializableExtra("UserInfo");
				String text = mEdtEmotic.getText().toString();
				int index = -1;
				if (text.lastIndexOf('@') >= 0) {
					index = text.lastIndexOf('@');
				} else if (text.lastIndexOf("＠") >= 0) {
					index = text.lastIndexOf("＠");
				}
				if (index >= 0) {
					SpannableString ss = new SpannableString(mEdtEmotic.getText().toString() + mAtUser.getNick() + " ");
					ss.setSpan(new ForegroundColorSpan(Color.parseColor("#61b754")), index, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					mEdtEmotic.setText(ss);
					mEdtEmotic.setAtUser(mAtUser);
					// 设置光标的位置
					CharSequence textChar = mEdtEmotic.getText();
					if (textChar instanceof Spannable) {
						Spannable spanText = (Spannable) textChar;
						Selection.setSelection(spanText, textChar.length());
					}
				}
			default:
				break;
			}
		} else if (resultCode == 123) {
			finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 发送图片
	 * 
	 * @param conversation
	 * @param path
	 */
	public void sendPic(Info info) {
		if (mConversation == null) {
			return;
		}

		final String localImagePath = info.path;
		try {
			final AVIMImageMessage message = new AVIMImageMessage(localImagePath);

			final MessageInfo msg = new MessageInfo();
			msg.setLogourl(application.getUserInfo().getLogo());
			msg.setNickName(application.getUserInfo().getNick());
			msg.setUserId(userId);
			msg.setTimestamp(System.currentTimeMillis());
			msg.setSessionId(sessionInfo.getSessionId());
			msg.setMsgType(MessageInfo.TYPE_PHOTO);
			msg.setFilePath(localImagePath);
			msg.setImgWidth(info.w);
			msg.setImgHeight(info.h);
			msg.setLengh(info.size);
			mAdapter.add(msg);

			mListView.smoothScrollToPosition(mAdapter.getCount());
			// 插入数据库
			long id = dbManager.insertFileMsg(msg);
			msg.setId(id);
			final String content = MessageInfo.toContentJSON(application.getUserInfo(), sessionInfo, msg, isPrivateChat);
			HashMap<String, Object> maps = new HashMap<String, Object>();
			maps.put("content", content);
			message.setAttrs(maps);
			resetSessionLogo();
			mConversation.sendMessage(message, new AVIMConversationCallback() {
				@Override
				public void done(AVIMException e) {
					if (null != e) {
						// 出错了。。。
						e.printStackTrace();
						Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
						dbManager.deleteMsg(msg);
						reConnectLeanCloud();
					} else {
						MobclickAgent.onEvent(mContext, "groupchat_gallery_count");//友盟记录 群聊图片次数
						Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

						msg.setFileUrl(message.getFileUrl());
						int[] xy = ImageTools.scaleXY(ChatActivity.this, msg.getImgWidth(), msg.getImgHeight());
						msg.setFileThumbnailUrl(message.getAVFile().getThumbnailUrl(false, xy[0], xy[1]));
						updateMessageByName(message, msg);
					}
				}
			});

		} catch (Exception ex) {
			Log.e("ex", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void sendRecorder(final String path, double lengh) {

		if (mConversation == null || TextUtils.isEmpty(path)) {
			Log.e("e", "sendRecorder fail because of "+"mConversation="+mConversation+" path="+path);
			Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
			return;
		}
		final String localAudioPath = path;

		Log.d("in", "sendRecorder-- =" + path +" lengh ="+lengh);
		try {

			final AVIMAudioMessage message = new AVIMAudioMessage(localAudioPath);
			final MessageInfo msg = new MessageInfo();
			msg.setLogourl(application.getUserInfo().getLogo());
			msg.setNickName(application.getUserInfo().getNick());
			msg.setUserId(userId);
			msg.setTimestamp(System.currentTimeMillis());
			msg.setSessionId(sessionInfo.getSessionId());
			msg.setMsgType(MessageInfo.TYPE_RECORDER);
			msg.setFilePath(path);
			msg.setLengh(lengh);
			mAdapter.add(msg);

			mListView.smoothScrollToPosition(mAdapter.getCount());
			long id = dbManager.insertFileMsg(msg);
			msg.setId(id);

			final String content = MessageInfo.toContentJSON(application.getUserInfo(), sessionInfo, msg, isPrivateChat);
			HashMap<String, Object> maps = new HashMap<String, Object>();
			maps.put("content", content);
			maps.put("lengh", lengh);
			message.setAttrs(maps);

			resetSessionLogo();

			mConversation.sendMessage(message, new AVIMConversationCallback() {
				@Override
				public void done(AVIMException e) {
					if (null != e) {
						// 出错了。。。
						Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
						dbManager.deleteMsg(msg);
						reConnectLeanCloud();
					} else {
						final AVFile file = message.getAVFile();
						file.saveInBackground(new SaveCallback() {
							@Override
							public void done(AVException arg0) {
								Log.d("a", "message.getFileUrl()="+message.getFileUrl());
								// 上传车成功
								//语音和视频才有的属性
								renameFile(path,message.getFileUrl());
								msg.setFileUrl(message.getFileUrl());
								updateMessageByName(message, msg);
								Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
															}
						}, new ProgressCallback() {

							@Override
							public void done(Integer arg0) {
								Log.d("in", "saveInBackground-- =" + arg0);
							}
						});
					}
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			Log.d("e", "Exception =" + ex.getMessage());
		}
	}
	
	
	private boolean renameFile(String oldpath,String url){
		final String path = oldpath.substring(0,oldpath.lastIndexOf("/"));
		final String newName = url.substring(url.lastIndexOf("/") + 1);
		File file = new File(oldpath);
		return file.renameTo(new File(path+File.separator+newName));
	}

	private void resetSessionLogo() {
		if (TextUtils.isEmpty(sessionInfo.getLogoUrl())) {
			Object obj = mConversation.getAttribute("logo");
			if (obj != null) {
				sessionInfo.setLogoUrl((String) obj);
			}
		}
	}

//	private void sendVideo(String path) {
//		try {
//			final AVIMVideoMessage message = new AVIMVideoMessage(path);
//			final MessageInfo msg = new MessageInfo();
//			msg.setLogourl(application.getUserInfo().getLogo());
//			msg.setNickName(application.getUserInfo().getNick());
//			msg.setUserId(userId);
//			msg.setContent(mEdtEmotic.getText().toString());
//			msg.setTimestamp(System.currentTimeMillis());
//			msg.setSessionId(sessionInfo.getSessionId());
//			msg.setMsgType(MessageInfo.TYPE_RECORDER);
//			msg.setFilePath(path);
//			msg.setLengh(2);
//			mAdapter.add(msg);
//
//			mListView.smoothScrollToPosition(mAdapter.getCount());
//			long id = dbManager.insertFileMsg(msg);
//			msg.setId(id);
//
//			final String content = MessageInfo.toContentJSON(application.getUserInfo(), sessionInfo, msg);
//			HashMap<String, Object> maps = new HashMap<String, Object>();
//			maps.put("content", content);
//			message.setAttrs(maps);
//
//			resetSessionLogo();
//
//			mConversation.sendMessage(message, new AVIMConversationCallback() {
//				@Override
//				public void done(AVException e) {
//					if (null != e) {
//						// 出错了。。。
//						e.printStackTrace();
//						dbManager.deleteMsg(msg);
//					} else {
//						final AVFile file = message.getAVFile();
//						file.saveInBackground(new SaveCallback() {
//							@Override
//							public void done(AVException arg0) {
//								Log.d("in", "saveInBackground-- =" + arg0);
//								// 上传车成功
//								updateMessageByName(message, msg);
//								Log.d("in", "file.getUrl() =" + file.getUrl());
//							}
//						}, new ProgressCallback() {
//
//							@Override
//							public void done(Integer arg0) {
//								Log.d("in", "saveInBackground-- =" + arg0);
//							}
//						});
//					}
//				}
//			});
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

	/**
	 * 发送文本
	 * 
	 * @param conversation
	 * @param path
	 */
	public void sendText(final String text) {

		if (mConversation == null) {
			Log.d("e", "mConversation= " + mConversation);
			return;
		}

		final AVIMTextMessage message = new AVIMTextMessage();
		message.setText(mEdtEmotic.getText().toString());

		final MessageInfo msg = new MessageInfo();
		msg.setLogourl(application.getUserInfo().getLogo());
		msg.setNickName(application.getUserInfo().getNick());
		msg.setUserId(userId);
		msg.setTextStr(text);
		msg.setTimestamp(System.currentTimeMillis());
		msg.setSessionId(sessionInfo.getSessionId());

		// 添加数据到数据库
		mAdapter.add(msg);

		// 插入数据库
		long id = dbManager.insertFileMsg(msg);
		msg.setId(id);

		// 滚动到最底部
		mListView.smoothScrollToPosition(mAdapter.getCount());

		final String content = MessageInfo.toContentJSON(application.getUserInfo(), sessionInfo, msg, isPrivateChat);
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("content", content);
		message.setAttrs(maps);
		resetSessionLogo();

		// 发AT消息
		Log.d("at", "request AT........................" + mEdtEmotic.getText().toString());

		if (isPrivateChat) {
			MobclickAgent.onEvent(mContext, "private_chat_count");// 私聊发文字次数
		} else {
			MobclickAgent.onEvent(mContext, "groupchat_textmsg_count");// 友盟记录群聊发文字次数
		}
		
		mConversation.sendMessage(message, new AVIMConversationCallback() {
			@Override
			public void done(AVIMException e) {
				if (null != e) {
					e.printStackTrace();
					Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
					dbManager.deleteMsg(msg);
					Log.d("e", "sendMessage................" + e.getMessage());
					reConnectLeanCloud();
				} else {
					Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
					// 同步消息内容更新消息
					updateMessageByName(message, msg);
					if (text.startsWith("@") || text.startsWith("＠")) {
						if (mAtUser != null) {
							at(mAtUser, text);
						}
					}
				}
			}
		});
		mEdtEmotic.setText("");
	}

	private void updateMessageByName(AVIMTypedMessage message, MessageInfo msg) {
		msg.setMessageId(message.getMessageId());
		msg.setTimestamp(message.getTimestamp());
		boolean sucess = dbManager.updateMsg(msg);
		if (sucess) {
			SessionInfo info = sessionInfo;
			info.setNickName(application.getUserInfo().getNick());
			info.setTime(msg.getTimestamp());
			info.setFromUserId(application.getUserInfo().getAccount());
			info.setMessageType(msg.getMsgType());
			info.setMessageId(msg.getMessageId());
			info.setNickName(application.getUserInfo().getNick());
			info.setContent(msg.getTextStr());
			info.setLogoUrl(sessionInfo.getLogoUrl());
			if (SessionTable.updateSession(info)) {
				EventBus.getDefault().post(new ChangeMessageEventBus(1));
			}
		}
	}

	// 显示和隐藏表情栏
	public void showSmileContainer() {
		if (lyContainer.getVisibility() == View.VISIBLE)
			lyContainer.setVisibility(View.GONE);
		else {
			try {
				hideInputMethod();
			} catch (Exception e) {
			}
			lyContainer.setVisibility(View.VISIBLE);

		}
		mListView.smoothScrollToPosition(mListView.getCount());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (myGridView != null) {
			MobclickAgent.onEvent(mContext, "groupchat_sticker_count");
			mEdtEmotic.insert(arg1.getTag().toString());
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	protected void onStart() {
		super.onStart();
		isShown = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isShown = false;
		MediaManager.pause();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (!hasFocus) {
			if (mRecordView != null) {
				mRecordView.reset();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isShown = true;
		application.notificationManager.cancelAll();
		MediaManager.resume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaManager.release();
		isShown = false;
		EventBus.getDefault().unregister(this);
		Intent i = new Intent();
		i.setClass(this, MsgServices.class);
		stopService(i);
	}

	private void at(UserInfo info, String msg) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		int type = 3;
		Log.d("at", "request AT........................");
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("cid", sessionId));
		params.add(new BasicNameValuePair("msg", msg));
		params.add(new BasicNameValuePair("toUid", info.getAccount()));
		httpCilents.postA(Url.ACCUPASS_SEND_IM_EVENT, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				Log.d("at", "result =" + result);
				BaseResponse info = JSON.parseObject((String) result, BaseResponse.class);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					// application.showMsg(result.toString());
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(info.getMsg());
					break;
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		mListView.stopRefresh();
		if (pageIndex > 0) {
			int position = mListView.getCount();
			pageIndex--;
			loadDate();
			// 移动到最顶部部
			int p = 0;
			if (mAdapter.getCount() - position > 0) {
				// 移动以前显示的位置
				p = mAdapter.getCount() - position;
			}
			Log.d("d", "position =" + p);
			mListView.setSelection(p);
		} else {
			application.showMsg("亲，已经没有数据啦~");
		}
	}

	@Override
	public void onLoadMore() {

	}
	
	
	public void reConnectLeanCloud(){
		application.leanCloudConnect();
	}

}
