package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.Animation;
//import android.view.animation.TranslateAnimation;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.CommentAdapter;
//import com.accuvally.hdtui.adapter.HotActivitiesAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.CommentInfo;
//import com.accuvally.hdtui.model.HomeEventInfo;
//import com.accuvally.hdtui.model.OrgDetailsInfo;
//import com.accuvally.hdtui.model.BaseResponse;
//import com.accuvally.hdtui.ui.CircleImageView;
//import com.accuvally.hdtui.ui.ScrolListView;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//
///**
// * 主办方详情
// * 
// * @author Semmer Wang
// * 
// */
//public class OrganizersDetailsActivity extends BaseActivity implements OnClickListener, OnEditorActionListener, TextWatcher {
//
//	private TextView tvAddAccuvally, tvComment;
//
//	private TextView tvOrgCommentNumber;// 评论数量
//
//	private CircleImageView ivOrgImageUrl;// logo
//
//	private TextView tvCityAndFollow;// 关注 城市
//
//	private TextView tvOrgDescription;// 简介
//
//	private LinearLayout lydetails1, lydetails2;
//
//	private LinearLayout lyFollow;
//
//	private LinearLayout lyComment;
//
//	private Button loadMoreCommentBtn;
//
//	private Button loadMoreOrgBtn;
//
//	private ScrolListView mListView1, mListView2;
//
//	private CommentAdapter commentAdapter;
//
//	private HotActivitiesAdapter hotActivitiesAdapter;
//
//	private TextView tvOrgDetailsTitle;
//
//	private EditText edComment;
//
//	private ImageView ivFollow;
//
//	private TextView tvFollow;
//
//	private LinearLayout lyLoading;
//
//	private int currIndex = 0;
//	private int tabTag = 0;
//	private int position_one;
//	private ImageView ivBottomLine;
//
//	private ScrollView scrollview;
//
//	private String orgId;
//
//	private OrgDetailsInfo info;
//
//	private int pageIndex = 1;
//
//	private int pageIndexAct = 1;
//
//	private ImageView ivDelContent;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_organizers_details);
//		orgId = getIntent().getStringExtra("orgId");
//		initView();
//		getDetailsById();
//		initWidth();
//	}
//
//	public void initView() {
//		setTitle(R.string.organizers_details_title);
//
//		tvAddAccuvally = (TextView) findViewById(R.id.tvAddAccuvally);
//		tvComment = (TextView) findViewById(R.id.tvComment);
//		lydetails1 = (LinearLayout) findViewById(R.id.lydetails1);
//		lydetails2 = (LinearLayout) findViewById(R.id.lydetails2);
//		mListView1 = (ScrolListView) findViewById(R.id.listview);
//		mListView2 = (ScrolListView) findViewById(R.id.commentlistview);
//		scrollview = (ScrollView) findViewById(R.id.scrollview);
//		lyComment = (LinearLayout) findViewById(R.id.lyComment);
//		tvOrgDetailsTitle = (TextView) findViewById(R.id.tvOrgDetailsTitle);
//		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
//		tvOrgCommentNumber = (TextView) findViewById(R.id.tvOrgCommentNumber);
//		ivOrgImageUrl = (CircleImageView) findViewById(R.id.ivOrgImageUrl);
//		tvCityAndFollow = (TextView) findViewById(R.id.tvCityAndFollow);
//		tvOrgDescription = (TextView) findViewById(R.id.tvOrgDescription);
//		lyFollow = (LinearLayout) findViewById(R.id.lyFollow);
//		tvFollow = (TextView) findViewById(R.id.tvFollow);
//		ivFollow = (ImageView) findViewById(R.id.ivFollow);
//		loadMoreCommentBtn = (Button) findViewById(R.id.load_more_comment_btn);
//		loadMoreOrgBtn = (Button) findViewById(R.id.load_more_org_btn);
//		edComment = (EditText) findViewById(R.id.edComment);
//		ivDelContent = (ImageView) findViewById(R.id.ivDelContent);
//		tvAddAccuvally.setOnClickListener(this);
//		lyComment.setOnClickListener(this);
//		lyFollow.setOnClickListener(this);
//		loadMoreCommentBtn.setOnClickListener(this);
//		loadMoreOrgBtn.setOnClickListener(this);
//		edComment.setOnEditorActionListener(this);
//		scrollview.setFocusable(false);
//		edComment.addTextChangedListener(this);
//		ivDelContent.setOnClickListener(this);
//	}
//
//	public void getDetailsById() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("oid", orgId));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_ORGANIZER_DETAILS, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				lyLoading.setVisibility(View.GONE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					info = JSON.parseObject(result.toString(), OrgDetailsInfo.class);
//					getOrgDetails(info);
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	public void getOrgDetails(OrgDetailsInfo info) {
//		scrollview.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				scrollview.scrollTo(0, 0);
//			}
//		}, 300);
//		DisplayImageOptions options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square_image).showImageForEmptyUri(R.drawable.default_square_image).showImageOnFail(R.drawable.default_square_image).cacheInMemory(true).cacheOnDisk(true).build();
//		tvOrgDetailsTitle.setText(info.getOrg().getName());
//		tvOrgCommentNumber.setText(info.getOrg().getComments() + "");
//		tvCityAndFollow.setText(info.getOrg().getFollows() + "人关注");
//		tvOrgDescription.setText(info.getOrg().getDescription());
//		application.mImageLoader.displayImage(info.getOrg().getLogo(), ivOrgImageUrl, options);
//
//		if (info.getOrg().isHasFollowed()) {
//			ivFollow.setBackgroundResource(R.drawable.organizers_details_followed_icon_bg);
//			tvFollow.setText("已关注");
//		} else {
//			ivFollow.setBackgroundResource(R.drawable.organizers_details_follow_icon_bg);
//			tvFollow.setText("关注");
//		}
//
//		hotActivitiesAdapter = new HotActivitiesAdapter(mContext, null, 0);
//		hotActivitiesAdapter.setList(info.getActs());
//		mListView1.setAdapter(hotActivitiesAdapter);
//
//		if (info.getActs().size() < 5) {
//			loadMoreOrgBtn.setVisibility(View.GONE);
//		} else {
//			loadMoreOrgBtn.setVisibility(View.VISIBLE);
//		}
//		commentAdapter = new CommentAdapter(mContext);
//		commentAdapter.setList(info.getCmts());
//		mListView2.setAdapter(commentAdapter);
//
//		if (info.getCmts().size() < 10) {
//			loadMoreCommentBtn.setVisibility(View.GONE);
//		} else {
//			loadMoreCommentBtn.setVisibility(View.VISIBLE);
//		}
//	}
//
//	private void initWidth() {
//		ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;
//		position_one = (int) (screenW / 2.0);
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.tvAddAccuvally:// 发布活动
//			try {
//				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(OrganizersDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			} catch (Exception e) {
//			}
//			if (tabTag != 0) {
//				tabTag = 0;
//				setCurrentItem(0);
//			}
//			break;
//		case R.id.lyComment:// 讨论
//			if (tabTag != 1) {
//				tabTag = 1;
//				setCurrentItem(1);
//			}
//			try {
//				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(OrganizersDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			} catch (Exception e) {
//			}
//			break;
//		case R.id.lyFollow:// 关注
//			if (Utils.isFastDoubleClick())
//				return;
//			if (!NetworkUtils.isNetworkAvailable(mContext)) {
//				return;
//			}
//			if (info != null)
//				follow();
//			break;
//		case R.id.load_more_comment_btn:// 加载更多评论
//			loadMoreCommentBtn.setText("正在加载中...");
//			pageIndex++;
//			initComment(pageIndex);
//			break;
//		case R.id.load_more_org_btn:// 加载更多活动
//			loadMoreOrgBtn.setText("正在加载中...");
//			pageIndexAct++;
//			initOrg(pageIndexAct);
//			break;
//		case R.id.ivDelContent:
//			edComment.setText("");
//			break;
//		}
//		try {
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(OrganizersDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		} catch (Exception e) {
//		}
//	}
//
//	public void initComment(final int pageIndex) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("type", "1"));
//		params.add(new BasicNameValuePair("target", info.getOrg().getId() + ""));
//		params.add(new BasicNameValuePair("pi", pageIndex + ""));
//		params.add(new BasicNameValuePair("ps", "10"));
//		params.add(new BasicNameValuePair("source", "0"));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_COMMENT_LIST, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<CommentInfo> list = JSON.parseArray(result.toString(), CommentInfo.class);
//					if (pageIndex == 1 && list != null) {
//						commentAdapter.removeAll();
//					}
//					commentAdapter.addAll(list);
//					if (list.size() < 10) {
//						loadMoreCommentBtn.setVisibility(View.GONE);
//					} else {
//						loadMoreCommentBtn.setVisibility(View.VISIBLE);
//						loadMoreCommentBtn.setText("加载更多");
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	public void initOrg(final int pageIndex) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("oid", info.getOrg().getId()));
//		params.add(new BasicNameValuePair("pi", pageIndex + ""));
//		params.add(new BasicNameValuePair("ps", "5"));
//		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_ORGACT_LIST, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					List<HomeEventInfo> list = JSON.parseArray(result.toString(), HomeEventInfo.class);
//					if (pageIndex == 1 && list != null) {
//						hotActivitiesAdapter.removeAll();
//					}
//					hotActivitiesAdapter.addAll(list);
//					if (list.size() < 5) {
//						loadMoreOrgBtn.setVisibility(View.GONE);
//					} else {
//						loadMoreOrgBtn.setVisibility(View.VISIBLE);
//						loadMoreOrgBtn.setText("加载更多");
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	public void uploadComment() {
//		String content = edComment.getText().toString().trim();
//		if (TextUtils.isEmpty(content)) {
//			application.showMsg("请输入评论内容");
//			return;
//		}
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", info.getOrg().getId()));
//		params.add(new BasicNameValuePair("type", "1"));
//		params.add(new BasicNameValuePair("comment", content));
//		params.add(new BasicNameValuePair("replyid", "0"));
//		params.add(new BasicNameValuePair("source", "0"));
//		showProgress("正在提交评论");
//		httpCilents.postA(Url.ACCUPASS_UPALOD_COMMENT, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
//					if (messageInfo.isSuccess()) {
//						dbManager.insertSaveBeHavior(application.addBeHavior(70, 1+"", info.getOrg().getId(), "","","",""));
//						application.showMsg(messageInfo.getMsg());
//						edComment.setText("");
//						int num = info.getOrg().getComments() + 1;
//						tvOrgCommentNumber.setText(num + "");
//						pageIndex = 1;
//						initComment(pageIndex);
//					} else {
//						application.showMsg(messageInfo.getMsg());
//					}
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//			}
//		});
//	}
//
//	public void follow() {
//		if (!application.checkIsLogin()) {
//			mContext.startActivity(new Intent(mContext, LoginActivityNew.class));
//			return;
//		}
//		if (!info.getOrg().isHasFollowed()) {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("oid", info.getOrg().getId()));
//			params.add(new BasicNameValuePair("source", "0"));
//			showProgress("正在关注主办方");
//			httpCilents.postA(Url.ACCUPASS_ORGFOLLOW, params, new WebServiceCallBack() {
//
//				@Override
//				public void callBack(int code, Object result) {
//					dismissProgress();
//					switch (code) {
//					case Config.RESULT_CODE_SUCCESS:
//						dbManager.insertSaveBeHavior(application.addBeHavior(20, 1+"", info.getOrg().getId(), "","","",""));
//						ivFollow.setBackgroundResource(R.drawable.organizers_details_followed_icon_bg);
//						info.getOrg().setHasFollowed(true);
//						tvFollow.setText("已关注");
//						break;
//					case Config.RESULT_CODE_ERROR:
//						application.showMsg(result.toString());
//						break;
//					}
//				}
//			});
//		} else {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("oid", info.getOrg().getId()));
//			params.add(new BasicNameValuePair("source", "0"));
//			showProgress("正在取消关注主办方");
//			httpCilents.postA(Url.ACCUPASS_ORGUNFOLLOW, params, new WebServiceCallBack() {
//
//				@Override
//				public void callBack(int code, Object result) {
//					dismissProgress();
//					switch (code) {
//					case Config.RESULT_CODE_SUCCESS:
//						dbManager.insertSaveBeHavior(application.addBeHavior(21, 1+"", info.getOrg().getId(), "","","",""));
//						ivFollow.setBackgroundResource(R.drawable.organizers_details_follow_icon_bg);
//						info.getOrg().setHasFollowed(false);
//						tvFollow.setText("关注");
//						break;
//					case Config.RESULT_CODE_ERROR:
//						application.showMsg(result.toString());
//						break;
//					}
//				}
//			});
//		}
//	}
//
//	public void setCurrentItem(int i) {
//		Animation animation = null;
//		switch (i) {
//		case 0:
//			if (currIndex == 1) {
//				animation = new TranslateAnimation(position_one, 0, 0, 0);
//				tvComment.setTextColor(getResources().getColor(R.color.gary_content));
//			}
//			tvAddAccuvally.setTextColor(getResources().getColor(R.color.txt_green));
//			lydetails1.setVisibility(View.VISIBLE);
//			lydetails2.setVisibility(View.GONE);
//			break;
//		case 1:
//			if (currIndex == 0) {
//				animation = new TranslateAnimation(0, position_one, 0, 0);
//				tvAddAccuvally.setTextColor(getResources().getColor(R.color.gary_content));
//			}
//			tvComment.setTextColor(getResources().getColor(R.color.txt_green));
//			lydetails1.setVisibility(View.GONE);
//			lydetails2.setVisibility(View.VISIBLE);
//			break;
//		}
//		currIndex = i;
//		animation.setFillAfter(true);
//		animation.setDuration(300);
//		ivBottomLine.startAnimation(animation);
//	}
//
//	@Override
//	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//
//		if (arg1 == EditorInfo.IME_ACTION_SEND) {
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(OrganizersDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			if (application.checkIsLogin()) {
//				if (NetworkUtils.isNetworkAvailable(mContext)) {
//					if (info != null) {
//						uploadComment();
//					}
//				} else
//					application.showMsg("请检查网络是否连接");
//			} else {
//				startActivity(new Intent(mContext, LoginActivityNew.class));
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void afterTextChanged(Editable s) {
//
//	}
//
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		if (TextUtils.isEmpty(s)) {
//			ivDelContent.setVisibility(View.GONE);
//		} else {
//			ivDelContent.setVisibility(View.VISIBLE);
//		}
//
//	}
//}
