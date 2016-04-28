package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.BaseFragment;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.MainActivity;
//import com.accuvally.hdtui.adapter.ClassfyAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.model.MessageInfo;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.Utils;
//import com.alibaba.fastjson.JSON;
//
//public class ClassflyFragment extends BaseFragment implements OnClickListener {
//
//	private ListView mListView;
//
//	private TextView tvSkip;
//
//	private TextView tvNext;
//
//	List<String> listString;
//
//	private LinearLayout preservation_ly;
//
//	private LinearLayout lyClassflyFirst;
//
//	private View viewline;
//
//	ClassfyAdapter mAdapter;
//	ImageView imageView;
//	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//
//	private int tag;
//
//	private LinearLayout lyMainHeader;
//
//	private LinearLayout ly_slidingMenu;
//
//	private TextView main_top_center;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.activity_classify, container, false);
//		initView(rootView);
//		return rootView;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		initData();
//	}
//
//	public void initView(View view) {
//		((RelativeLayout) view.findViewById(R.id.ll_top_bg)).setVisibility(View.GONE);
//		mListView = (ListView) view.findViewById(R.id.listview);
//		tvSkip = (TextView) view.findViewById(R.id.tvSkip);
//		tvNext = (TextView) view.findViewById(R.id.tvNext);
//		preservation_ly = (LinearLayout) view.findViewById(R.id.save_lys);
//		lyClassflyFirst = (LinearLayout) view.findViewById(R.id.lyClassflyFirst);
//		viewline = (View) view.findViewById(R.id.viewline);
//		lyMainHeader = (LinearLayout) view.findViewById(R.id.lyMainHeader);
//		ly_slidingMenu = (LinearLayout) view.findViewById(R.id.ly_slidingMenu);
//		main_top_center = (TextView) view.findViewById(R.id.main_top_center);
//
//		tvSkip.setOnClickListener(this);
//		tvNext.setOnClickListener(this);
//		preservation_ly.setOnClickListener(this);
//		ly_slidingMenu.setOnClickListener(this);
//		lyClassflyFirst.setOnClickListener(this);
//		preservation_ly.setVisibility(View.VISIBLE);
//		lyClassflyFirst.setVisibility(View.GONE);
//		viewline.setVisibility(View.GONE);
//		lyMainHeader.setVisibility(View.VISIBLE);
//		preservation_ly.setVisibility(View.VISIBLE);
//		main_top_center.setText("我的分类");
//	}
//
//	public void initData() {
//		listString = new ArrayList<String>();
//		listString = dbManager.getClassfy();
//		list = new ArrayList<HashMap<String, Object>>();
//		HashMap<String, Object> map1 = new HashMap<String, Object>();
//		HashMap<String, Object> map2 = new HashMap<String, Object>();
//		HashMap<String, Object> map3 = new HashMap<String, Object>();
//		HashMap<String, Object> map4 = new HashMap<String, Object>();
//		HashMap<String, Object> map5 = new HashMap<String, Object>();
//		HashMap<String, Object> map6 = new HashMap<String, Object>();
//		HashMap<String, Object> map7 = new HashMap<String, Object>();
//		HashMap<String, Object> map8 = new HashMap<String, Object>();
//		HashMap<String, Object> map9 = new HashMap<String, Object>();
//		HashMap<String, Object> map10 = new HashMap<String, Object>();
//
//		map1.put("image", R.drawable.classfly_bg8);
//		map1.put("title", "商务/会议");
//		map1.put("content", "电商峰会/媒体论坛/学术研讨会");
//
//		map2.put("image", R.drawable.classfly_bg4);
//		map2.put("title", "创业/分享");
//		map2.put("content", "创业路演/创业大赛/干货分享");
//
//		map3.put("image", R.drawable.classfly_bg5);
//		map3.put("title", "文化/沙龙");
//		map3.put("content", "文艺沙龙/读书签售/见面会");
//
//		map4.put("image", R.drawable.classfly_bg2);
//		map4.put("title", "公益/讲座");
//		map4.put("content", "NGO沙龙/志愿者招募/社会创新分享");
//
//		map5.put("image", R.drawable.classfly_bg3);
//		map5.put("title", "亲子/互动");
//		map5.put("content", "母婴活动/教育分享/亲子工作坊");
//
//		map6.put("image", R.drawable.classfly_bg7);
//		map6.put("title", "户外/健身");
//		map6.put("content", "长跑暴走/旅游健身/露营短途游");
//
//		map7.put("image", R.drawable.classfly_bg1);
//		map7.put("title", "生活/社交");
//		map7.put("content", "亲友聚会/主题派对/美食烘焙");
//
//		map8.put("image", R.drawable.classfly_bg6);
//		map8.put("title", "展览/展会");
//		map8.put("content", "博物馆/美术馆/主题展览/行业展会");
//
//		map9.put("image", R.drawable.classfly_bg9);
//		map9.put("title", "课程/培训");
//		map9.put("content", "技能培训/认证课程/语言学习等");
//
//		map10.put("image", R.drawable.classfly_bg10);
//		map10.put("title", "演出等其他");
//		map10.put("content", "其他");
//
//		list.add(map1);
//		list.add(map2);
//		list.add(map3);
//		list.add(map4);
//		list.add(map5);
//		list.add(map6);
//		list.add(map7);
//		list.add(map8);
//		list.add(map9);
//		list.add(map10);
//
//		mAdapter = new ClassfyAdapter(mContext, list, listString);
//
//		mListView.setAdapter(mAdapter);
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				imageView = (ImageView) arg1.findViewById(R.id.tvClassAddImg);
//				LinearLayout lyclass = (LinearLayout) arg1.findViewById(R.id.lyclass);
//				if (imageView.getTag() == null && imageView.getVisibility() == View.GONE) {
//					if (listString.size() < 5) {
//						imageView.setVisibility(View.VISIBLE);
//						imageView.setTag(arg2);
//						lyclass.setBackgroundResource(getResources().getColor(R.color.transparent));
//						listString.add(list.get(arg2).get("title").toString());
//					} else {
//						application.showMsg("最多订阅5个分类");
//					}
//				} else {
//					imageView.setTag(null);
//					imageView.setVisibility(View.GONE);
//					lyclass.setBackgroundResource(R.drawable.classfly_icon_add_normal);
//					if (listString.contains(list.get(arg2).get("title").toString())) {
//						listString.remove(list.get(arg2).get("title").toString());
//					}
//				}
//
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.ly_slidingMenu:
//			MainActivity.slidingMenu.showMenu();
//			break;
//		case R.id.tvSkip:
//			if (tag == 1) {
//				application.sharedUtils.writeBoolean("isFirstIn", true);
//				startActivity(new Intent(mContext, MainActivity.class));
//				// finish();
//			} else
//				// finish();
//				break;
//		case R.id.tvNext:
//			if (tag == 1) {
//				application.sharedUtils.writeBoolean("isFirstIn", true);
//				dbManager.deleteClassfy();
//				for (int i = 0; i < listString.size(); i++) {
//					dbManager.insertClassfy(listString.get(i));
//				}
//				startActivity(new Intent(mContext, MainActivity.class));
//				// finish();
//			} else {
//				dbManager.deleteClassfy();
//				StringBuilder builder = new StringBuilder();
//				for (int i = 0; i < listString.size(); i++) {
//					dbManager.insertClassfy(listString.get(i));
//					builder.append(listString.get(i) + ",");
//				}
//				if (application.checkIsLogin() && dbManager.getClassfy().size() != 0 && application.sharedUtils.readBoolean("isSynchronous")) {
//					setUserTags(builder.toString());
//				} else {
//					// Intent intent = new Intent(Config.WEBVIEW_HOME_TAG);
//					// sendBroadcast(intent);
//					// startActivity(new Intent(mContext,
//					// MainActivity.class).putExtra("tag", 1));
//					// finish();
//				}
//			}
//			break;
//		case R.id.save_lys:
//			dbManager.deleteClassfy();
//			StringBuilder builder = new StringBuilder();
//			for (int i = 0; i < listString.size(); i++) {
//				dbManager.insertClassfy(listString.get(i));
//				builder.append(listString.get(i) + ",");
//			}
//			if (NetworkUtils.isNetworkAvailable(mContext)) {
//
//				if (application.checkIsLogin() && dbManager.getClassfy().size() != 0 && application.sharedUtils.readBoolean("isSynchronous")) {
//					setUserTags(builder.toString());
//				} else {
//					if (dbManager.getClassfy().size() != 0) {
//						application.showMsg("设置成功");
//					} else {
//						if (!Utils.isFastDoubleClick()) {
//							application.showMsg("未选择分类，活动行将智能推荐");
//						}
//						HomeFragmentNew.interest = "";
//					}
//					// finish();
//				}
//			} else {
//				// finish();
//				application.showMsg("设置成功");
//			}
//			break;
//		}
//	}
//
//	public void getUserTag() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		httpCilents.get(httpCilents.printURL(Config.ACCUPASS_GETUSERTAGS, params), new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					dbManager.deleteClassfy();
//					MessageInfo messageInfo = JSON.parseObject(result.toString(), MessageInfo.class);
//					if (messageInfo.isRet()) {
//						listString = JSON.parseArray(messageInfo.getResult(), String.class);
//						for (int i = 0; i < listString.size(); i++) {
//							dbManager.insertClassfy(listString.get(i));
//						}
//						application.showMsg("保存成功");
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
//	public void setUserTags(String tags) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("tags", tags));
//		httpCilents.postA(Config.ACCUPASS_SETUSERTAGS, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					MessageInfo messageInfo = JSON.parseObject(result.toString(), MessageInfo.class);
//					if (messageInfo.isRet()) {
//						application.showMsg(messageInfo.getMsg());
//					} else {
//						application.showMsg(messageInfo.getMsg());
//					}
//					// finish();
//					break;
//				case Config.RESULT_CODE_ERROR:
//					application.showMsg(result.toString());
//					break;
//				}
//
//			}
//		});
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		try {
//			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//			childFragmentManager.setAccessible(true);
//			childFragmentManager.set(this, null);
//		} catch (NoSuchFieldException e) {
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
