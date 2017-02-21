package com.accuvally.hdtui.activity.home.util;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.config.CityConstant;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeCityEventBus;
import com.alibaba.fastjson.JSON;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchResultActivity extends ChooseCityActivity implements OnClickListener, IXListViewListener,TextView.OnEditorActionListener {
	private int pageIndex = 1, pageSize = 20;
	private XListView mListView;

	private CommonAdapter mAdapter;

	private TextView tvNoData;

    private TextView searchresult_city;
    private EditText searchContent;//搜索输入框

	private Button SquareBtn;

	private ImageView ivFailure;

	private LinearLayout lyFailure;

	private int tag = 0;//0为搜索失败，1为没搜到相关活动

	private int searchflag;//0为直接输入关键字，1为选择其中一个tab

	private String searchdata;//搜索的内容

	private LinearLayout lyLoading;

//	private int temp;

	protected int returnDataLength;//判断是否还能继续loadmore

    private LinearLayout searchresultHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        searchdata = getIntent().getStringExtra("searchdata");
		searchflag = getIntent().getIntExtra("flag", 0);
		setContentView(R.layout.activity_search);
		initView();
	}

    private void contentSearch(){

        pageIndex=1;
        searchdata=searchContent.getText().toString();
        searchflag=0;
        search(searchflag,searchdata);

    }


	public void initView() {

        searchContent= (EditText) findViewById(R.id.search_content);
        searchresult_city= (TextView) findViewById(R.id.searchresult_city);
        String cityNameText = application.sharedUtils.readString(Keys.cityName);
        searchresult_city.setText(cityNameText);

        searchresultHead= (LinearLayout) findViewById(R.id.searchresult_head);
      /*  if (searchflag == 1)
            setHint(searchdata);
		else
            setHint("搜索");*/
        searchContent.setText(searchdata);
        searchContent.setOnEditorActionListener(this);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);
		mListView = (XListView) findViewById(R.id.listview);

        findViewById(R.id.searchresult_back).setOnClickListener(this);
        findViewById(R.id.llsearchresult_city).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);

		SquareBtn.setOnClickListener(this);
		mListView.setXListViewListener(this);
		SquareBtn.setOnClickListener(this);
		mAdapter = new CommonAccuAdapter(mContext);
		mListView.setAdapter(mAdapter);

        search(searchflag, searchdata);//init

       /* temp = getIntent().getIntExtra("temp", 0);
		if (temp == 2) {
			searchtag = getIntent().getStringExtra("searchText");
			showProgress("正在努力搜索中");
			pageIndex = 1;
			search(1, searchtag);
		}*/
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
            case R.id.searchresult_back:
                finish();
                break;

            case R.id.iv_search:
                contentSearch();//直接点击搜索图标
                break;

            case R.id.llsearchresult_city:
                initPopupWindow();
                if (!popupWindow.isShowing()) {
//                    popupWindow.showAtLocation(searchresultHead, Gravity.TOP,0,searchresultHead.getHeight());
                    popupWindow.showAsDropDown(searchresultHead);
//                    popupWindow.showAsDropDown(lyTickType, mListView.getLayoutParams().width, 0);
                } else {
                    popupWindow.dismiss();
                }
                break;

		case R.id.SquareBtn:// 去广场页
			if (tag == 1) {// 广场 (没有搜索到相关活动)
				EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
				finish();
			} else {// 重试
				pageIndex = 1;
				search(0, searchdata);//重试
			}
			break;
		}
	}

	// TAG搜索  0为直接输入关键字，1为选择其中一个tab
	public void search(int flag, String qs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (flag == 1)
			params.add(new BasicNameValuePair("tag", qs));
		else
			params.add(new BasicNameValuePair("key", qs));
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		params.add(new BasicNameValuePair("onlyunexpired", application.sharedUtils.readBoolean("onlyunexpired", true) + ""));// 仅显示未过期
		params.add(new BasicNameValuePair("onlyunfull", application.sharedUtils.readBoolean("full", false) + ""));// 仅显示未额满
		params.add(new BasicNameValuePair("city", application.sharedUtils.readString("cityName")));
//		String latitude = application.sharedUtils.readString("longitude") + "," + application.sharedUtils.readString("latitude");
//		params.add(new BasicNameValuePair("coordinates", latitude));
		lyLoading.setVisibility(View.VISIBLE);
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_HOME_TAB, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				lyLoading.setVisibility(View.GONE);
				lyFailure.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					
					mListView.stopRefresh();
					mListView.stopLoadMore();

					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if(response.isSuccess()){

						List<SelInfo> list = JSON.parseArray(response.getResult(), SelInfo.class);
						if (pageIndex == 1 && list != null) {
							mAdapter.removeAll();
						}
						if (pageIndex == 1 && list.size() == 0) {
							mListView.setVisibility(View.GONE);
							lyFailure.setVisibility(View.VISIBLE);
							ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
							tvNoData.setText(getResources().getString(R.string.loading_no_data));
							SquareBtn.setText(R.string.search_guangchang);
							tag = 1;
						}
						returnDataLength = list.size();
						mAdapter.addAll(list);
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					if (pageIndex == 1) {
						mListView.setVisibility(View.GONE);
						lyFailure.setVisibility(View.VISIBLE);
						ivFailure.setBackgroundResource(R.drawable.loading_no_wifi_bg);
						tvNoData.setText(getResources().getString(R.string.loading_no_wifi));
						SquareBtn.setText("点击重试");
						tag = 0;
					}
					break;
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		if (mAdapter.getCount() < pageSize) {
			mListView.stopRefresh();
			return;
		}
		pageIndex = 1;
		search(searchflag, searchdata);//refresh
	}

	@Override
	public void onLoadMore() {
		if (returnDataLength < pageSize) {
			mListView.stopLoadMore();
			return;
		}
		pageIndex++;
		search(searchflag, searchdata);//loadMore
	}

    //=============================================================================

    private PopupWindow popupWindow;
    private View mPopupWindowView;
    List<String> hotCityList;

    private void initPopupWindow() {

        initPopupWindowView();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenHeigh = dm.heightPixels;

        popupWindow = new PopupWindow(mPopupWindowView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        popupWindow.setAnimationStyle(R.style.dialogWindowAnim);
        Drawable drawable = getResources().getDrawable(R.color.home_left_color);
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

//                tvTickType.setTextColor(getResources().getColor(R.color.gary_content));

                popupWindow.dismiss();
            }
        });
    }


    private void initPopupWindowView(){

        /*if(mPopupWindowView==null){ }*/
            mPopupWindowView = LayoutInflater.from(this).inflate(R.layout.include_city_display, null);

            String cityList = application.cacheUtils.getAsString(Keys.cityList);
            if (!TextUtils.isEmpty(cityList)) {
                hotCityList = JSON.parseArray(cityList, String.class);
            }else {
                hotCityList = new ArrayList<String>(Arrays.asList(CityConstant.hotCityName));
            }


            final String cityNameText = application.sharedUtils.readString(Keys.cityName);
            //linLocation 包含tvCityTitle和locationCityName
            if (TextUtils.isEmpty(cityNameText) || !hasInCityList(cityNameText)) {
                mPopupWindowView.findViewById(R.id.linLocation).setVisibility(View.GONE);
            } else {
                TextView tvCityTitle = (TextView) mPopupWindowView.findViewById(R.id.tvCityTitle);
                tvCityTitle.setText("当前选择的城市");
                TextView tvLocationCity = (TextView) mPopupWindowView.findViewById(R.id.locationCityName);
                tvLocationCity.setText(cityNameText);
                tvLocationCity.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseCurrentCity(cityNameText);
                    }
                });
            }
            initCityList(mPopupWindowView);



    }

    private boolean hasInCityList(String city) {
        for (int i = 0; i < hotCityList.size(); i++) {
            if (city.equals(hotCityList.get(i)))
                return true;
        }
        return false;
    }


    private void initCityList(View mPopupWindowView) {

        GridView gridView = (GridView) mPopupWindowView.findViewById(R.id.gridView);
        ArrayAdapter mAdapter = new ArrayAdapter<String>(mContext, R.layout.griditem_city, R.id.tvCityName, hotCityList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                switch (position % 3) {
                    case 0:
                        view.setGravity(Gravity.LEFT);
                        break;
                    case 1:
                        view.setGravity(Gravity.CENTER);
                        break;
                    case 2:
                        view.setGravity(Gravity.RIGHT);
                        break;
                }
                return view;
            }
        };
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityNameText = hotCityList.get(position);
                chooseCurrentCity(cityNameText);
            }
        });

    }

    private void chooseCurrentCity(String cityNameText) {
        EventBus.getDefault().post(new ChangeCityEventBus(cityNameText));
        application.sharedUtils.writeString(Keys.cityName, cityNameText);
        popupWindow.dismiss();
        searchresult_city.setText(cityNameText);
        contentSearch();//选择城市，触发刷新
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            contentSearch();//软键盘上的搜索按钮
        }

        return false;
    }
}
