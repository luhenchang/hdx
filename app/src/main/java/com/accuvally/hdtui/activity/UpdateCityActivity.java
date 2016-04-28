package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.graphics.PixelFormat;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextWatcher;
//import android.text.style.ForegroundColorSpan;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.db.DBManager;
//import com.accuvally.hdtui.model.CityInfo;
//import com.accuvally.hdtui.model.BaseResponse;
//import com.accuvally.hdtui.model.UserInfo;
//import com.accuvally.hdtui.ui.MyLetterListView;
//import com.accuvally.hdtui.ui.MyLetterListView.OnTouchingLetterChangedListener;
//import com.accuvally.hdtui.ui.ScrollGridView;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.LocationUtils;
//import com.accuvally.hdtui.utils.LocationUtils.LocatinCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
//import com.alibaba.fastjson.JSON;
//import com.baidu.location.BDLocation;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 选择城市
// * 
// * @author Semmer Wang
// * 
// */
//public class UpdateCityActivity extends BaseActivity implements TextWatcher, OnEditorActionListener, OnClickListener {
//
//	private ListView mListView;
//
//	private MyLetterListView letterListView;
//
//	private List<CityInfo> cityList;
//
//	private List<CityInfo> allCityList;
//
//	private SQLiteDatabase database;
//
//	private BaseAdapter adapter;
//
//	private TextView overlay; // 对话框首字母textview
//
//	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
//
//	private String[] sections;// 存放存在的汉语拼音首字母
//
//	private Handler handler;
//
//	private OverlayThread overlayThread; // 显示首字母对话框
//
//	ListAdapter.TopViewHolder topViewHolder;
//
//	private int tag;
//	WindowManager windowManager;
//
//	private TextView cityName;
//
//	private EditText edSearch;
//
//	private ImageView ivSearch;
//
//	private Dialog locationDialog;
//
//	private TextView currentCityName;
//
//	private ScrollGridView gwAllCityName;
//
//	private String[] hotCityName = { "北京", "广州", "上海", "深圳", "成都", "杭州", "南京", "厦门", "宁波", "苏州", "武汉", "西安", "合肥", "长沙" };
//
//	private View header;
//
//	private String cityNameText;
//
//	private GridAdapter gridAdapter;
//
//	private LocationUtils locationUtils;
//
//	private DBManager dbManager;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_select_city);
//		cityNameText = getIntent().getStringExtra("cityname");
//		dbManager = new DBManager(this);
//		dbManager.openDateBase();
//		initView();
//		initOverlay();
//		initData();
//		hotCityInit();
//	}
//
//
//	public void initView() {
//		setTitle(R.string.personal_update_city);
//		mListView = (ListView) findViewById(R.id.city_list);
//		cityName = (TextView) findViewById(R.id.cityName);
//		currentCityName = (TextView) findViewById(R.id.currentCityName);
//		header = LayoutInflater.from(this).inflate(R.layout.update_city_header, null);
//		gwAllCityName = (ScrollGridView) header.findViewById(R.id.gwAllCityName);
//		edSearch = (EditText) findViewById(R.id.edSearch);
//		ivSearch = (ImageView) findViewById(R.id.ivSearch);
//		letterListView = (MyLetterListView) findViewById(R.id.cityLetterListView);
//		gridAdapter = new GridAdapter(cityNameText);
//		gwAllCityName.setAdapter(gridAdapter);
//		mListView.addHeaderView(header);
//
//		letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
//		tag = getIntent().getIntExtra("tag", 0);
//		gwAllCityName.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		gwAllCityName.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int arg2, long id) {
//
//				cityNameText = hotCityName[arg2];
//
//				Message msg = new Message();
//				msg.what = CUURENT_CITY_NAME;
//				msg.obj = cityNameText;
//				mHandler.sendMessage(msg);
//				if (tag == 2) {
//					update(cityNameText);
//				} else if (tag == 1) {
//					EventBus.getDefault().post(new ChangeCityEventBus(cityNameText));
//					application.sharedUtils.writeString("cityName", cityNameText);
//					finish();
//				}
//			}
//		});
//		String city;
//		if (cityNameText != null) {
//			city = "当前城市：" + cityNameText;
//			SpannableStringBuilder style = new SpannableStringBuilder(city);
//			style.setSpan(new ForegroundColorSpan(Color.rgb(51, 51, 51)), 5, 5 + cityNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			currentCityName.setText(style);
//		} else {
//			city = "当前城市：";
//			currentCityName.setText(city);
//		}
//
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				cityNameText = allCityList.get(arg2 - 1).getName();
//				Message msg = new Message();
//				msg.what = CUURENT_CITY_NAME;
//				msg.obj = cityNameText;
//				mHandler.sendMessage(msg);
//				if (tag == 2) {
//					update(cityNameText);
//				} else if (tag == 1) {
//					EventBus.getDefault().post(new ChangeCityEventBus(cityNameText));
//					application.sharedUtils.writeString("cityName", cityNameText);
//					finish();
//				}
//			}
//		});
//
//		edSearch.addTextChangedListener(this);
//		ivSearch.setOnClickListener(this);
//		edSearch.setOnEditorActionListener(this);
//		cityName.setOnClickListener(this);
//		locationUtils = new LocationUtils(mContext);
//	}
//
//	public void initData() {
//		allCityList = new ArrayList<CityInfo>();
//		alphaIndexer = new HashMap<String, Integer>();
//		handler = new Handler();
//		overlayThread = new OverlayThread();
//		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
//	}
//
//	public void hotCityInit() {
//
//		cityList = getCityList();
//		allCityList.addAll(cityList);
//		setAdapter(allCityList);
//	}
//
//	public class GridAdapter extends BaseAdapter {
//
//		private String cityName;
//
//		public GridAdapter(String cityName) {
//			this.cityName = cityName;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return hotCityName == null ? 0 : hotCityName.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return hotCityName == null ? null : hotCityName[position];
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			GridViewHoder gridViewHoder;
//			if (convertView == null) {
//				gridViewHoder = new GridViewHoder();
//				convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_view_city_item, null);
//				gridViewHoder.hot_city_name = (TextView) convertView.findViewById(R.id.hot_city_name);
//				gridViewHoder.iv_prompt_select_city = (ImageView) convertView.findViewById(R.id.iv_prompt_select_city);
//				convertView.setTag(gridViewHoder);
//			} else {
//				gridViewHoder = (GridViewHoder) convertView.getTag();
//			}
//			gridViewHoder.hot_city_name.setText(hotCityName[position]);
//			if (hotCityName[position].equals(cityName)) {
//				gridViewHoder.iv_prompt_select_city.setVisibility(View.VISIBLE);
//			} else {
//				gridViewHoder.iv_prompt_select_city.setVisibility(View.GONE);
//			}
//
//			return convertView;
//		}
//
//		private class GridViewHoder {
//			TextView hot_city_name;
//			ImageView iv_prompt_select_city;
//		}
//	}
//
//	private void setAdapter(List<CityInfo> list) {
//		adapter = new ListAdapter(this, list);
//		mListView.setAdapter(adapter);
//	}
//
//	public class ListAdapter extends BaseAdapter {
//
//		private LayoutInflater inflater;
//		private List<CityInfo> list;
//		final int VIEW_TYPE = 3;
//
//		public ListAdapter(Context context, List<CityInfo> list) {
//			this.inflater = LayoutInflater.from(context);
//			this.list = list;
//			alphaIndexer = new HashMap<String, Integer>();
//			sections = new String[list.size()];
//			for (int i = 0; i < list.size(); i++) {
//				// 当前汉语拼音首字母
//				String currentStr = getAlpha(list.get(i).getPinyi());
//				// 上一个汉语拼音首字母，如果不存在为“ ”
//				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1).getPinyi()) : " ";
//				if (!previewStr.equals(currentStr)) {
//					String name = getAlpha(list.get(i).getPinyi());
//					alphaIndexer.put(name, i);
//					sections[i] = name;
//				}
//			}
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public int getItemViewType(int position) {
//			// TODO Auto-generated method stub
//			int type = 0;
//			// if (position == 1) {
//			// type = 1;
//			// } else
//			// if (position == 0) {
//			// type = 2;
//			// }
//			return type;
//		}
//
//		@Override
//		public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
//			return VIEW_TYPE;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.listitem_city, null);
//				holder = new ViewHolder();
//				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
//				holder.name = (TextView) convertView.findViewById(R.id.name);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			// if (position >= 1) {
//			holder.name.setText(list.get(position).getName());
//			String currentStr = getAlpha(list.get(position).getPinyi());
//			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getPinyi()) : " ";
//			if (!previewStr.equals(currentStr)) {
//				holder.alpha.setVisibility(View.VISIBLE);
//				if (currentStr.equals("#")) {
//					currentStr = "热门城市";
//				}
//				holder.alpha.setText(currentStr);
//			} else {
//				holder.alpha.setVisibility(View.GONE);
//			}
//			return convertView;
//		}
//
//		private class ViewHolder {
//			TextView alpha; // 首字母标题
//			TextView name; // 城市名字
//		}
//
//		private class TopViewHolder {
//			TextView name; // 城市名字
//		}
//	}
//
//	// 初始化汉语拼音首字母弹出提示框
//	private void initOverlay() {
//		LayoutInflater inflater = LayoutInflater.from(this);
//		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
//		overlay.setVisibility(View.INVISIBLE);
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
//		windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//		windowManager.addView(overlay, lp);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (overlay.getParent() != null)
//			windowManager.removeView(overlay);
//	}
//
//	public void dialog_location() {
//		locationDialog = new Dialog(mContext, R.style.dialog);
//		locationDialog.setCancelable(false);
//		locationDialog.setCanceledOnTouchOutside(false);
//		locationDialog.setContentView(R.layout.dialog_gps_location);
//		TextView tvCancel = (TextView) locationDialog.findViewById(R.id.tvCancel);
//		TextView tvSure = (TextView) locationDialog.findViewById(R.id.tvSure);
//
//		tvSure.setOnClickListener(this);
//		tvCancel.setOnClickListener(this);
//		locationDialog.show();
//	}
//
//	private class LetterListViewListener implements OnTouchingLetterChangedListener {
//
//		@Override
//		public void onTouchingLetterChanged(final String s) {
//			if (alphaIndexer.get(s) != null) {
//				int position = alphaIndexer.get(s);
//				mListView.setSelection(position);
//				overlay.setText(sections[position]);
//				overlay.setVisibility(View.VISIBLE);
//				handler.removeCallbacks(overlayThread);
//				// 延迟一秒后执行，让overlay为不可见
//				handler.postDelayed(overlayThread, 1500);
//			}
//		}
//	}
//
//	// 设置overlay不可见
//	private class OverlayThread implements Runnable {
//		@Override
//		public void run() {
//			overlay.setVisibility(View.GONE);
//		}
//
//	}
//
//	// 获得汉语拼音首字母
//	@SuppressWarnings("unused")
//	private String getAlpha(String str) {
//		if (str.equals("-")) {
//			return "&";
//		}
//		if (str == null) {
//			return "#";
//		}
//		if (str.trim().length() == 0) {
//			return "#";
//		}
//		char c = str.trim().substring(0, 1).charAt(0);
//		// 正则表达式，判断首字母是否是英文字母
//		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
//		if (pattern.matcher(c + "").matches()) {
//			return (c + "").toUpperCase();
//		} else {
//			return "#";
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private ArrayList<CityInfo> getCityList() {
//		ArrayList<CityInfo> list = new ArrayList<CityInfo>();
//		try {
//			database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
//			Cursor cursor = database.rawQuery("select * from city", null);
//			CityInfo city;
//			while (cursor.moveToNext()) {
//				city = new CityInfo(cursor.getString(1), cursor.getString(2));
//				list.add(city);
//			}
//			cursor.close();
//			database.close();
//			Collections.sort(list, comparator);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
//
//	@SuppressWarnings("rawtypes")
//	Comparator comparator = new Comparator<CityInfo>() {
//		@Override
//		public int compare(CityInfo lhs, CityInfo rhs) {
//			String a = lhs.getPinyi().substring(0, 1);
//			String b = rhs.getPinyi().substring(0, 1);
//			int flag = a.compareTo(b);
//			if (flag == 0) {
//				return a.compareTo(b);
//			} else {
//				return flag;
//			}
//
//		}
//	};
//
//	private ArrayList<CityInfo> getCityItem(String name) {
//		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
//		ArrayList<CityInfo> list = new ArrayList<CityInfo>();
//		String sql = "select * from city where name=\"" + name + "\"";
//		Cursor cursor = database.rawQuery(sql, null);
//		CityInfo city;
//		while (cursor.moveToNext()) {
//			city = new CityInfo(cursor.getString(1), cursor.getString(2));
//			list.add(city);
//		}
//		cursor.close();
//		database.close();
//		return list;
//	}
//
//	public void initLocation() {
//		if (!NetworkUtils.isNetworkAvailable(mContext)) {
//			application.showMsg("啊哦，网络好像断开了！");
//			return;
//		}
//		locationUtils.location(new LocatinCallBack() {
//
//			@Override
//			public void callBack(int code, BDLocation location) {
//				switch (code) {
//				case 1:
//					topViewHolder.name.setText(location.getCity().replace("市", ""));
//					break;
//				case 0:
//					topViewHolder.name.setText(application.sharedUtils.readString("cityName"));
//					break;
//				}
//				adapter.notifyDataSetChanged();
//				locationUtils.stopListener();
//			}
//		});
//	}
//
//	public void update(final String cityName) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("City", cityName));
//
//		showProgress("正在修改资料");
//		httpCilents.postA(Url.ACCUPASS_UPDATE_USERINFO, params, new WebServiceCallBack() {
//
//			@Override
//			public void callBack(int code, Object result) {
//				dismissProgress();
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					UserInfo user = application.getUserInfo();
//					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
//					if (messageInfo.isSuccess()) {
//						application.showMsg(messageInfo.getMsg());
//						user.setCity(cityName);
//						application.setUserInfo(user);
//						finish();
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
//	@Override
//	public void afterTextChanged(Editable arg0) {
//		if ("".equals(arg0.toString())) {
//			mListView.setVisibility(View.VISIBLE);
//			letterListView.setVisibility(View.VISIBLE);
//			cityName.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//	}
//
//	@Override
//	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//	}
//
//	@Override
//	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//		if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			if (!"".equals(edSearch.getText().toString().trim())) {
//				if (getCityItem(edSearch.getText().toString().trim()).size() != 0) {
//					mListView.setVisibility(View.GONE);
//					letterListView.setVisibility(View.GONE);
//					cityName.setVisibility(View.VISIBLE);
//					cityName.setText(getCityItem(edSearch.getText().toString().trim()).get(0).getName());
//				} else {
//					application.showMsg("没有您搜索的城市");
//					mListView.setVisibility(View.VISIBLE);
//					letterListView.setVisibility(View.VISIBLE);
//					cityName.setVisibility(View.GONE);
//				}
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.ivSearch:
//			if (!"".equals(edSearch.getText().toString().trim())) {
//				if (getCityItem(edSearch.getText().toString().trim()).size() != 0) {
//					mListView.setVisibility(View.GONE);
//					letterListView.setVisibility(View.GONE);
//					cityName.setVisibility(View.VISIBLE);
//					cityName.setText(getCityItem(edSearch.getText().toString().trim()).get(0).getName());
//				} else {
//					application.showMsg("没有您搜索的城市");
//					mListView.setVisibility(View.VISIBLE);
//					letterListView.setVisibility(View.VISIBLE);
//					cityName.setVisibility(View.GONE);
//				}
//			}
//			break;
//		case R.id.cityName:
//			if (tag == 2) {
//				update(cityName.getText().toString().trim());
//			} else if (tag == 1) {
//				EventBus.getDefault().post(new ChangeCityEventBus(cityNameText));
//				application.sharedUtils.writeString("cityName", cityNameText);
//				finish();
//			}
//			break;
//		case R.id.tvCancel:
//			locationDialog.dismiss();
//			application.sharedUtils.writeBoolean("isLocation", false);
//			break;
//		case R.id.tvSure:
//			locationDialog.dismiss();
//			topViewHolder.name.setText("正在定位");
//			if (NetworkUtils.isNetworkAvailable(mContext)) {
//				initLocation();
//			}
//			application.sharedUtils.writeBoolean("isLocation", true);
//			break;
//		}
//	}
//
//	private static final int CUURENT_CITY_NAME = 2;
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 1:
//				topViewHolder.name.setText(application.sharedUtils.readString("cityName"));
//				adapter.notifyDataSetChanged();
//				break;
//			case CUURENT_CITY_NAME:
//				String city = "当前城市：" + msg.obj;
//				SpannableStringBuilder style = new SpannableStringBuilder(city);
//				style.setSpan(new ForegroundColorSpan(Color.rgb(51, 51, 51)), 5, 5 + cityNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				currentCityName.setText(style);
//				gridAdapter.notifyDataSetChanged();
//				break;
//			}
//		};
//	};
//
//	protected void onDestroy() {
//		super.onDestroy();
//		if(dbManager!=null){
//			dbManager.closeDatabase();
//		}
//	}
//}
