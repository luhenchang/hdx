package com.accuvally.hdtui.adapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.message.core.ChatActivity;
import com.accuvally.hdtui.activity.ImageBrowserActivity;
import com.accuvally.hdtui.activity.message.user.UserDetailActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.db.SessionTable;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.model.SessionInfo;
import com.accuvally.hdtui.model.UserInfo;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.ui.ClipLoading;
import com.accuvally.hdtui.ui.EmoteTextView;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.ImageTools;
import com.accuvally.hdtui.utils.MediaManager;
import com.accuvally.hdtui.utils.SensorHelper;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.eventbus.ChangeMessageEventBus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

public class MsgAdapter extends BaseListAdapter<MessageInfo> {

	private int mMinItemWith, mMaxItemWith;
	private DisplayMetrics outMetrics;
	private AnimationDrawable soundAnim;
	private String path;
	private TextView mTvPlaying;
	private DBManager dbManager;
	private final Drawable mSoundDLeft, mSoundDRight, mSoundPlayLeft, mSoundPlayRight;// 语音的drawble
	private final UserInfo user;
	private SensorHelper mSensorHelper;
	private Dialog dialog;
	private boolean isPrivateChat;
	private HttpCilents httpCilents;
	private String creator;

	public MsgAdapter(Context context, boolean isPrivateChat, String creator) {
		super(context);
		this.isPrivateChat = isPrivateChat;
		this.creator = creator;

		WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(outMetrics);
		mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
		mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
		mSoundPlayRight = mContext.getResources().getDrawable(R.drawable.play_right);
		mSoundPlayLeft = mContext.getResources().getDrawable(R.drawable.play_left);
		mSoundDLeft = mContext.getResources().getDrawable(R.drawable.chatfrom_voice_playing);
		mSoundDRight = mContext.getResources().getDrawable(R.drawable.chatto_voice_playing);
		path = new FileCache(context).getAudioCacheDir().getAbsolutePath();
		dbManager = new DBManager(mContext);
		user = application.getUserInfo();
		mSensorHelper = new SensorHelper(mContext);

		httpCilents = new HttpCilents(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_msg, null);
			viewHolder = new ViewHolder(user.getAccount());
			viewHolder.ivImgLeft = (CircleImageView) convertView.findViewById(R.id.iv_msg_icon_left);
			viewHolder.tvContentLeft = (EmoteTextView) convertView.findViewById(R.id.tv_msg_content_left);
			viewHolder.left = (LinearLayout) convertView.findViewById(R.id.msg_session_left);
			viewHolder.tvTimeLeft = (TextView) convertView.findViewById(R.id.tv_msg_name_left);
			viewHolder.picLeft = (ImageView) convertView.findViewById(R.id.img_pic_left);
			viewHolder.nickLeft = (TextView) convertView.findViewById(R.id.tv_nick_left);

			viewHolder.ivImgRight = (CircleImageView) convertView.findViewById(R.id.iv_msg_icon_right);
			viewHolder.tvContentRight = (EmoteTextView) convertView.findViewById(R.id.tv_msg_content_right);
			viewHolder.right = (LinearLayout) convertView.findViewById(R.id.msg_session_right);
			viewHolder.tvTimeRight = (TextView) convertView.findViewById(R.id.tv_msg_name_right);
			viewHolder.picRight = (ImageView) convertView.findViewById(R.id.img_pic_right);
			viewHolder.nickRight = (TextView) convertView.findViewById(R.id.tv_nick_right);
			// 语音的view
			viewHolder.recordLenghLeft = (TextView) convertView.findViewById(R.id.record_len_left);
			viewHolder.recordLenghRight = (TextView) convertView.findViewById(R.id.record_len_right);
			viewHolder.recordImgLeft = (TextView) convertView.findViewById(R.id.record_img_left);
			viewHolder.recordImgRight = (TextView) convertView.findViewById(R.id.record_img_right);
			viewHolder.recordLayoutLeft = (RelativeLayout) convertView.findViewById(R.id.img_record_layout_left);
			viewHolder.recordLayoutRight = (RelativeLayout) convertView.findViewById(R.id.img_record_layout_right);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MessageInfo info = mList.get(position);
		switch (info.getMsgType()) {
		case MessageInfo.TYPE_TEXT:
			final EmoteTextView tvMsg = viewHolder.getTextView(info);
			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			tvMsg.setText(info.getTextStr());// 设置文字
			Util.setTextSpan(tvMsg);
			tvMsg.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					showDialog(tvMsg.getText().toString());
					return false;
				}
			});
			viewHolder.getPic(info).setVisibility(View.GONE);// 隐藏头像的输入框
			viewHolder.getRecordLayout(info).setVisibility(View.GONE);// 隐藏语音界面
			break;
		case MessageInfo.TYPE_PHOTO:

			viewHolder.getTextView(info).setVisibility(View.GONE);// 隐藏文字
			final ImageView iv = viewHolder.getPic(info);
			final int[] xy = ImageTools.scaleXY(mContext, info.getImgWidth(), info.getImgHeight());
			iv.getLayoutParams().width = xy[0];
			iv.getLayoutParams().height = xy[1];

			iv.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(info.getFilePath())) {
				application.mImageLoader.displayImage("file://" + info.getFilePath(), iv);
			} else {
				// 本地没有，取缩略图
				if (!TextUtils.isEmpty(info.getFileThumbnailUrl())) {
					application.mImageLoader.displayImage(info.getFileThumbnailUrl(), iv);
				} else {
					// 缩略图没有，取原图
					if (!TextUtils.isEmpty(info.getFileUrl()))
						application.mImageLoader.displayImage(info.getFileUrl(), iv);
				}
			}
			iv.setTag(info);
			iv.setOnClickListener(listenerPic);
			viewHolder.getRecordLayout(info).setVisibility(View.GONE);// 隐藏语音界面
			break;
		case MessageInfo.TYPE_RECORDER:

			viewHolder.getRecordLayout(info).setVisibility(View.VISIBLE);// 显示语音界面

			final TextView tv = viewHolder.getRecordImg(info);// 显示语音
			if (info != null && application.getUserInfo().getAccount().equals(info.getUserId())) {
				tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mSoundDRight, null);
			} else {
				tv.setCompoundDrawablesWithIntrinsicBounds(mSoundDLeft, null, null, null);
			}
			viewHolder.getRecordLengh(info).setText(toFloat(info.getLengh()) + "''");
			ViewGroup.LayoutParams lParams = tv.getLayoutParams();
			lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f * info.getLengh());
			tv.setText("");
			tv.setVisibility(View.VISIBLE);
			tv.setTag(info);
			tv.setLayoutParams(lParams);
			tv.setOnClickListener(listenerMusic);
			viewHolder.getPic(info).setVisibility(View.GONE);// 隐藏图片
			viewHolder.getTextView(info).setVisibility(View.GONE);// 隐藏文字
			break;
		default:
			break;
		}

		CircleImageView ivAvatar = viewHolder.getheadView(info);
		ImageLoader.getInstance().displayImage(getMyHeadUrl(info), ivAvatar, UILoptions.defaultUser); // 加载头像
		ivAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserDetailActivity.class);
				intent.putExtra("id", info.getUserId());
				intent.putExtra("nick", info.getNickName());
				intent.putExtra("avatarUrl", info.getLogourl());
				mContext.startActivity(intent);
			}
		});

		TextView time = viewHolder.getTime(info);
		if (isShowTimestamp(position)) {// 间隔超过2分钟才显示
			time.setVisibility(View.VISIBLE);
			time.setText(info.getMessageTime());
		} else {
			time.setVisibility(View.GONE);
		}
		
		TextView tvNick = viewHolder.getNickTv(info);
		tvNick.setText(info.getNickName());
		if (!isPrivateChat) {
			commonChat(info, tvNick, ivAvatar);
		}
		viewHolder.showLayout(info);// 显示左边或右边的布局
		return convertView;
	}

	private void commonChat(final MessageInfo info, TextView tvNick, CircleImageView ivAvatar) {
		if (TextUtils.equals(creator, info.getUserId())) {
			String str = "主办人";
			String creator = info.getNickName() + "(" + str + ")";

			SpannableStringBuilder style = new SpannableStringBuilder(creator);
			int red = mContext.getResources().getColor(R.color.deep_red);
			int indexOf = creator.indexOf(str);
			style.setSpan(new ForegroundColorSpan(red), indexOf, indexOf + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvNick.setText(style);
		} else {
			tvNick.setTextColor(mContext.getResources().getColor(R.color.gary_content));
//			int size = mContext.getResources().getDimensionPixelSize(R.dimen.little_13);
//			tvNick.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
	}

	// 间隔时间超过2分钟才显示
	public static final int twoMinute = 2 * 60 * 1000;

	public boolean isShowTimestamp(int position) {
		if (position == 0) {
			return true;
		}
		long thistimestamp = mList.get(position).getTimestamp();
		long lastTimestamp = mList.get(position - 1).getTimestamp();
		if (thistimestamp - lastTimestamp > twoMinute) {
			return true;
		}
		return false;
	}

	public void showDialog(final String text) {
		if (dialog == null) {
			dialog = new Dialog(mContext, R.style.DefaultDialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.dialog_operate_text);
		}
		dialog.findViewById(R.id.tvCopyText).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SystemUtil.copy(mContext, text);
				ToastUtil.showMsg("已复制消息到剪贴板");
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*// 每次同一对用户会创建不同会话id，所以不该用这个
	public void createConversation(final MessageInfo info) {
		List<String> clientIds = new ArrayList<String>();
		clientIds.add(AccountManager.getAccount());
		clientIds.add(info.getUserId());

		int ConversationType_OneOne = 1; // 两个人之间的单聊
		int ConversationType_Group = 0; // 多人之间的群聊
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("type", ConversationType_OneOne);
		attr.put("users", geneJson(info));
		AVIMClient imClient = AVIMClient.getInstance(user.getAccount());
		imClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {

			@Override
			public void done(AVIMConversation conversation, AVIMException e) {
				if (null != conversation) {
					SessionInfo sessionInfo = new SessionInfo();
					sessionInfo.userId = user.getAccount();
					sessionInfo.setSessionId(conversation.getConversationId());
					sessionInfo.setTime(System.currentTimeMillis());

					sessionInfo.setTitle(info.getNickName());
					sessionInfo.setLogoUrl(info.getLogourl());// 对方头像
					sessionInfo.friendId = info.getUserId();
					application.setCurrentSession(sessionInfo);
					SessionTable.insertSession(sessionInfo);

					ToChatActivity();
				} else {
					ToastUtil.showMsg("不能创建会话, 请检查网络");
				}
			}
		});
	}
*/
	public Object geneJson(MessageInfo info) {
		JSONArray array = new JSONArray();

		JSONObject self = new JSONObject();
		self.put("account", user.getAccount());
		self.put("logo", user.getLogoLarge());
		self.put("nick", user.getNick());

		JSONObject friend = new JSONObject();
		friend.put("account", info.getUserId());
		friend.put("logo", info.getLogourl());
		friend.put("nick", info.getNickName());

		array.add(self);
		array.add(friend);

		return array;
	}



	private void ToChatActivity() {
		Intent intent = new Intent(mContext, ChatActivity.class);
		intent.putExtra("isPrivateChat", true);// 私聊
		mContext.startActivity(intent);
	}

	private String getMyHeadUrl(MessageInfo info) {
		if (user.getAccount().equals(info.getUserId())) {
			return user.getLogoLarge();
		} else {
			return info.getLogourl();
		}
	}

	private OnClickListener listenerPic = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_pic_left:
			case R.id.img_pic_right:

				((ChatActivity) mContext).hideBottomLayout();
				Intent intent = new Intent();
				intent.setClass(mContext, ImageBrowserActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("MessageInfo", (MessageInfo) v.getTag());
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.zoomin, 0);
			default:
				break;
			}
		}

	};

	public static float toFloat(double count) {// 保留小数点后一位小数
		BigDecimal b = new BigDecimal(count * 1000);
		return b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	private OnClickListener listenerMusic = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.record_img_left:
			case R.id.record_img_right:
				((ChatActivity) mContext).hideBottomLayout();
				MessageInfo info = (MessageInfo) v.getTag();
				play((TextView) v, info);
				break;
			}
		}
	};

	public void updateView(ListView mListView, int itemIndex, int progress) {
		// 得到第一个可显示控件的位置，
		int visiblePosition = mListView.getFirstVisiblePosition();
		// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		if (itemIndex - visiblePosition >= 0) {
			// 得到要更新的item的view
			View view = mListView.getChildAt(itemIndex - visiblePosition);
			// 从view中取得holder
			ViewHolder holder = (ViewHolder) view.getTag();
			MessageInfo info = mList.get(itemIndex);
			switch (info.getMsgType()) {
			case MessageInfo.TYPE_TEXT:
				break;
			case MessageInfo.TYPE_PHOTO:
				holder.loadPic.setProgress(progress);
				break;
			case MessageInfo.TYPE_RECORDER:
				break;
			default:
				break;
			}

		}
	}

	static class ViewHolder {
		private String userId = "";

		ViewHolder(String account) {
			this.userId = account;
		}

		TextView nickLeft, nickRight;
		CircleImageView ivImgLeft, ivImgRight;
		ImageView picLeft, picRight;
		EmoteTextView tvContentLeft, tvContentRight;
		TextView tvTimeLeft, tvTimeRight;
		LinearLayout left, right;
		TextView recordImgLeft, recordImgRight;// 语音的图标
		TextView recordLenghLeft, recordLenghRight;// 语音的时长
		RelativeLayout recordLayoutLeft, recordLayoutRight;// 语音界面的父view
		ClipLoading loadPic, loadRecord;// 语音界面的父view

		/**
		 * 头像
		 * 
		 * @param info
		 * @return
		 */
		public CircleImageView getheadView(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return ivImgRight;
			} else {
				return ivImgLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public TextView getNickTv(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return nickRight;
			} else {
				return nickLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public EmoteTextView getTextView(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return tvContentRight;
			} else {
				return tvContentLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public ImageView getPic(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return picRight;
			} else {
				return picLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public TextView getTime(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return tvTimeRight;
			} else {
				return tvTimeLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public LinearLayout getParent(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return right;
			} else {
				return left;
			}
		}

		/**
		 * 语音的长度
		 * 
		 * @param info
		 * @return
		 */
		public TextView getRecordLengh(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return recordLenghRight;
			} else {
				return recordLenghLeft;
			}
		}

		/**
		 * 语音的长度
		 * 
		 * @param info
		 * @return
		 */
		public RelativeLayout getRecordLayout(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return recordLayoutRight;
			} else {
				return recordLayoutLeft;
			}
		}

		/**
		 * 语音的View
		 * 
		 * @param info
		 * @return
		 */
		public TextView getRecordImg(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				return recordImgRight;
			} else {
				return recordImgLeft;
			}
		}

		/**
		 * 文字的显示区域
		 * 
		 * @param info
		 * @return
		 */
		public void showLayout(MessageInfo info) {
			if (info != null && userId.equals(info.getUserId())) {
				right.setVisibility(View.VISIBLE);
				left.setVisibility(View.GONE);
			} else {
				right.setVisibility(View.GONE);
				left.setVisibility(View.VISIBLE);
			}
		}
	}

	public void downloadRecorder(final TextView tv, final MessageInfo info) {
		final String name = info.getFileUrl().substring(info.getFileUrl().lastIndexOf("/") + 1);
		Log.d("a", "url " + info.getFileUrl());
		AVFile avfile = new AVFile(name, info.getFileUrl(), null);
		avfile.getDataInBackground(new GetDataCallback() {

			@Override
			public void done(byte[] arg0, AVException arg1) {
				if (arg1 != null) {
					Log.d("a", arg1.getMessage());
				} else {
					Log.d("a", "length =" + arg0.length);
					FileOutputStream outSTr = null;
					BufferedOutputStream buff = null;
					try {
						final File file = new File(path + "/" + name);
						if (!file.exists()) {
							file.createNewFile();
						} else {
							// 重新下载
							if (file.length() < arg0.length) {
								file.delete();
							}
						}
						outSTr = new FileOutputStream(file);
						buff = new BufferedOutputStream(outSTr);
						buff.write(arg0, 0, arg0.length);
						buff.flush();
						info.setFilePath(file.getAbsolutePath());

						if (file.length() == arg0.length) {
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									playRecorder(tv, info);
									dbManager.updateMsg(info);
								}
							}, 250);
						}

					} catch (Exception e) {
						Log.d("e", "write " + e.getMessage());
					} finally {
						try {
							buff.close();
							outSTr.close();
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("e", "close stream " + e.getMessage());
						}
					}
				}
			}
		}, new ProgressCallback() {

			@Override
			public void done(Integer arg0) {
				Log.d("a", "arg0=" + arg0);
			}
		});

	}

	public void play(final TextView tv, MessageInfo info) {
		// 不播放
		if (info == null) {
			return;
		}
		// 如果文件不存在就去服务器下载
		File file = new File(info.getFilePath());
		if (file.exists() && file.length() >= info.getLengh() && file.length() > 0) {
			playRecorder(tv, info);
		} else {
			downloadRecorder(tv, info);
		}
	}

	public void playRecorder(final TextView tv, final MessageInfo info) {
		if (TextUtils.isEmpty(info.getFilePath())) {
			Log.e("e", "playRecorder fail because of " + "url =" + info.getUserId() + " path=" + info.getFilePath());
			Toast.makeText(mContext, "语音播放错误", Toast.LENGTH_SHORT).show();
			return;
		}

		// 暂停其他的
		if (mTvPlaying != null && soundAnim != null) {
			// 还原其原先的界面
			soundAnim.stop();
			if (info != null && application.getUserInfo().getAccount().equals(info.getUserId())) {
				mTvPlaying.setCompoundDrawablesWithIntrinsicBounds(null, null, mSoundDRight, null);
			} else {
				mTvPlaying.setCompoundDrawablesWithIntrinsicBounds(mSoundDLeft, null, null, null);
			}
		}
		if (info != null && application.getUserInfo().getAccount().equals(info.getUserId())) {
			tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mSoundPlayRight, null);
			soundAnim = (AnimationDrawable) tv.getCompoundDrawables()[2];
			mTvPlaying = tv;
		} else {
			tv.setCompoundDrawablesWithIntrinsicBounds(mSoundPlayLeft, null, null, null);
			soundAnim = (AnimationDrawable) tv.getCompoundDrawables()[0];
			mTvPlaying = tv;
		}

		try {
			mSensorHelper.play();
			MediaManager.playSound(info.getFilePath(), new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					if (info != null && application.getUserInfo().getAccount().equals(info.getUserId())) {
						tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mSoundDRight, null);
					} else {
						tv.setCompoundDrawablesWithIntrinsicBounds(mSoundDLeft, null, null, null);
					}
					mSensorHelper.stop();
				}
			});
			soundAnim.start();
		} catch (Exception e) {
			Log.d("a", e.getMessage());
			Toast.makeText(mContext, "播放音频失败", 1000).show();
			mSensorHelper.stop();
		}
	}
}
