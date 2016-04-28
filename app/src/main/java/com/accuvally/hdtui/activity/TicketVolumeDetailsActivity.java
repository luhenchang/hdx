package com.accuvally.hdtui.activity;//package com.accuvally.hdtui.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.adapter.TicketDetailsAdapter;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.TicketDetails;
//import com.accuvally.hdtui.ui.SlideOnePageGallery;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.alibaba.fastjson.JSON;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//
///**
// * 票劵详情
// * 
// * @author Vanness
// * 
// */
//public class TicketVolumeDetailsActivity extends BaseActivity implements OnClickListener {
//
//	private ImageView delete_img;
//
//	private SlideOnePageGallery app_app_image_gallery;
//
//	String id;
//
//	private int tag = 0;
//
//	ImageView ivTicketDetailsBg;
//
//	private TicketDetailsAdapter mAdapter;
//
//	private List<TicketDetails> mList;
//
//	private LinearLayout lyLoading;
//
//	public static String ticketVolumeDetailsActivityReturn = null;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_ticket_volume_details);
//		initView();
//		initData();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		ticketVolumeDetailsActivityReturn = "TicketVolumeDetailsActivityReturn";
//	}
//
//	public void initView() {
//		delete_img = (ImageView) findViewById(R.id.two_img);
//		delete_img.setBackgroundResource(R.drawable.head_download);
//		delete_img.setOnClickListener(this);
//		app_app_image_gallery = (SlideOnePageGallery) findViewById(R.id.app_app_image_gallery);
//		lyLoading = (LinearLayout) findViewById(R.id.lyLoading);
//
//		ivTicketDetailsBg = (ImageView) findViewById(R.id.ivTicketDetailsBg);
//		String imgUrl = "assets://details_ticket_bg.png";
//		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(0).showImageForEmptyUri(0).showImageOnFail(0).cacheInMemory(true).cacheOnDisk(true).build();
//		application.mImageLoader.displayImage(imgUrl, ivTicketDetailsBg, options);
//
//		mAdapter = new TicketDetailsAdapter(mContext);
//		mList = new ArrayList<TicketDetails>();
//		mAdapter.setList(mList);
//		app_app_image_gallery.setAdapter(mAdapter);
//	}
//
//	public void initData() {
//		setTitle(R.string.title_ticket);
//		id = getIntent().getStringExtra("id");
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("id", id));
//		lyLoading.setVisibility(View.VISIBLE);
//		httpCilents.get(httpCilents.printURL(Url.GETTICKET, list), new WebServiceCallBack() {
//			public void callBack(int code, Object result) {
//				lyLoading.setVisibility(View.GONE);
//				switch (code) {
//				case Config.RESULT_CODE_SUCCESS:
//					application.cacheUtils.put(id, result.toString());
//					List<TicketDetails> ticketDetails = JSON.parseArray(result.toString(), TicketDetails.class);
//					mAdapter.addAll(ticketDetails);
//					tag = 1;
//					break;
//				case Config.RESULT_CODE_ERROR:
//					if (application.cacheUtils.getAsString(id) == null) {
//						application.showMsg(result.toString());
//						tag = 0;
//					} else {
//						List<TicketDetails> list = JSON.parseArray(application.cacheUtils.getAsString(id), TicketDetails.class);
//						mAdapter.addAll(list);
//						tag = 1;
//					}
//					break;
//				}
//			}
//		});
//	}
//
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.two_img:
//			// dialog();
//			if (tag == 1) {
//				if (application.cacheUtils.getAsString(id) == null) {
//					application.showMsg("缓存成功，离线也可查看票券");
//				} else {
//					application.showMsg("票券已缓存");
//				}
//			} else {
//				application.showMsg("缓存失败，请检查您的网络");
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	public void onDestroy() {
//		super.onDestroy();
//	}
//
//}
