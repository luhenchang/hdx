package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.AttendAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.ui.CircleImageView;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import de.greenrobot.event.EventBus;

public class AttendActivity extends BaseActivity implements OnClickListener, IXListViewListener {

	private XListView mListView;

	private AttendAdapter mAdapter;

	private List<HomeEventInfo> list;

	private LinearLayout lyLoading;

	private int pageIndex = 1, pageSize = 10;

	private TextView tvNoData;

	private Button SquareBtn;

	private ImageView ivFailure;

	private LinearLayout lyFailure;

	private int tag = 0;


	private LinearLayout llAttendNoData;

	protected List<HomeEventInfo> tempList;

	protected int onlyOne=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attend);
		initView();
		initDate();
	}

	public void initView() {
		setTitle("我参加的");
		mListView = (XListView) findViewById(R.id.listview);
		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		lyFailure = (LinearLayout) findViewById(R.id.lyFailure);
		ivFailure = (ImageView) findViewById(R.id.ivFailure);
		SquareBtn = (Button) findViewById(R.id.SquareBtn);

		SquareBtn.setOnClickListener(this);
		mListView.setXListViewListener(this);
		findViewById(R.id.incAttend).setVisibility(View.GONE);
		View header = LayoutInflater.from(mContext).inflate(R.layout.include_attend, null);
		CircleImageView ivHead1 = (CircleImageView) header.findViewById(R.id.ivHead);
		TextView tvUserName1 = (TextView) header.findViewById(R.id.tvUserName);
		TextView tvUserCity1 = (TextView) header.findViewById(R.id.tvUserCity);
		TextView tvUserSex1 = (TextView) header.findViewById(R.id.tvUserSex);						
		TextView tvUpLoad1 = (TextView) header.findViewById(R.id.tvUpLoad);
		View uploadlineview1 = (View) header.findViewById(R.id.uploadlineview);
		mListView.addHeaderView(header);
		optionHeader(ivHead1, tvUserName1, tvUserCity1, tvUserSex1, tvUpLoad1,
				uploadlineview1);

		CircleImageView ivHead = (CircleImageView)findViewById(R.id.ivHead);
		TextView tvUserName = (TextView)findViewById(R.id.tvUserName);
		TextView tvUserCity = (TextView) findViewById(R.id.tvUserCity);
		TextView tvUserSex = (TextView)findViewById(R.id.tvUserSex);
		llAttendNoData = (LinearLayout) findViewById(R.id.llAttendNoData);
		TextView tvUpLoad = (TextView)findViewById(R.id.tvUpLoad);
		View uploadlineview = (View) findViewById(R.id.uploadlineview);

		optionHeader(ivHead, tvUserName, tvUserCity, tvUserSex, tvUpLoad,
				uploadlineview);
	}

	private void optionHeader(CircleImageView ivHead, TextView tvUserName,
			TextView tvUserCity, TextView tvUserSex, TextView tvUpLoad,
			View uploadlineview) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.not_lgin_head_bg).showImageForEmptyUri(R.drawable.not_lgin_head_bg).showImageOnFail(R.drawable.not_lgin_head_bg).displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true).cacheOnDisk(true).build();
		application.mImageLoader.displayImage(application.getUserInfo().getLogoLarge(), ivHead, options);
		tvUserName.setText(application.getUserInfo().getNick());
		if ("".equals(application.getUserInfo().getCity()))
			tvUserCity.setText("暂无地址");
		else
			tvUserCity.setText(application.getUserInfo().getCity());
		if (application.getUserInfo().getGender() == 0) {
			tvUserSex.setText("女");
			Drawable drawable = getResources().getDrawable(R.drawable.accu_sex_bg);
			tvUserSex.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		} else {
			Drawable drawable = getResources().getDrawable(R.drawable.accu_nan_bg);
			tvUserSex.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
			tvUserSex.setText("男");
		}
		tvUpLoad.setVisibility(View.GONE);
		uploadlineview.setVisibility(View.GONE);
		ivHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, PersonalActivity.class));
			}
		});
	}

	public void initDate() {
		mAdapter = new AttendAdapter(mContext);
		list = new ArrayList<HomeEventInfo>();
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);
		lyLoading.setVisibility(View.VISIBLE);
		initData(pageIndex, pageSize);
	}

	public void initData(final int pageIndexfinal, int pageSize) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndexfinal + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYATTEND, params), new WebServiceCallBack() {



			@Override
			public void callBack(int code, Object result) {
				lyLoading.setVisibility(View.GONE);
				lyFailure.setVisibility(View.GONE);
				llAttendNoData.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					List<HomeEventInfo> list = JSON.parseArray(result.toString(), HomeEventInfo.class);
					if (pageIndexfinal==1&&list.size() == 0) {
						findViewById(R.id.incAttend).setVisibility(View.VISIBLE);
						tempList=list;
						lyFailure.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
						ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
						tvNoData.setText(getResources().getString(R.string.loading_no_data));
						SquareBtn.setText(R.string.search_guangchang);
						tag = 1;
						llAttendNoData.setVisibility(View.VISIBLE);


					}else{
						mListView.setVisibility(View.VISIBLE);
						//						if(onlyOne==1){
						findViewById(R.id.incAttend).setVisibility(View.GONE);


						//							onlyOne++;
						//						}
					}
					if(tempList!=null&&tempList.size()==0&&pageIndexfinal==2&&list.size()==0){
						lyFailure.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
						ivFailure.setBackgroundResource(R.drawable.loading_no_data_bg);
						tvNoData.setText(getResources().getString(R.string.loading_no_data));
						SquareBtn.setText(R.string.search_guangchang);
						tag = 1;
						llAttendNoData.setVisibility(View.VISIBLE);
						pageIndex=1;

					}
					if (pageIndex == 1 && list != null) {
						mAdapter.removeAll();
					}
					mListView.stopRefresh();
					mListView.stopLoadMore();
					mAdapter.addAll(list);
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.SquareBtn:
			if (tag == 1) {// 广场
				EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
				finish();
			} else {// 重试
				lyFailure.setVisibility(View.GONE);
				pageIndex = 1;
				initData(pageIndex, pageSize);
			}
			break;
		}

	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		initData(pageIndex, pageSize);
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		initData(pageIndex, pageSize);
	}

}
