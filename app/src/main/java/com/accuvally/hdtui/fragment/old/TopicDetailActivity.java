package com.accuvally.hdtui.fragment.old;//package com.accuvally.hdtui.fragment.old;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.accuvally.hdtui.BaseActivity;
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
//import com.accuvally.hdtui.adapter.CommonAdapter;
//import com.accuvally.hdtui.adapter.CommonAdapter.OnItemListener;
//import com.accuvally.hdtui.adapter.ViewHolder;
//import com.accuvally.hdtui.model.SelInfo;
//
///**
// * 专题详情
// * @author wan
// *
// */
//public class TopicDetailActivity extends BaseActivity {
//
//	private String title;
//	private WebView webView;
//	private ListView mListView;
//	private CommonAdapter<SelInfo> mAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_topic_detail);
//		title = getIntent().getStringExtra("title");
//		setTitle(title);
//		initProgress();
//
//		initView();
//		setListView();
//	}
//
//	private void initView() {
//		
//	}
//
//	private void setListView() {
//		mListView = (ListView) findViewById(R.id.listview);
//		View listHeader = getLayoutInflater().inflate(R.layout.listviewheader_topic_detail, null);
//		mListView.addHeaderView(listHeader);
//		
//		mListView.setAdapter(mAdapter = new CommonAdapter<SelInfo>(this,R.layout.listitem_topic_detail) {
//			
//			@Override
//			public int getCount() {
//				return 5;
//			}
//			
//			@Override
//			public SelInfo getItem(int position) {
//				return new SelInfo();
////				return "NO." + (position + 1);
//			}
//
//			@Override
//			public void convert(ViewHolder viewHolder, SelInfo item, int position) {
////				viewHolder.setText(R.id.tvNum, item);
//			}
//		});
//		
//		mAdapter.setOnItemListener(new OnItemListener() {
//
//			@Override
//			public void onItemClick(Object object, int position) {
//				Intent intent = new Intent(mContext, AccuvallyDetailsActivity.class);
//				intent.putExtra("id", ((SelInfo)object).getId());
//				intent.putExtra("isHuodong", ((SelInfo)object).getSourceType());
//				startActivity(intent);
//			}
//		});
//	}
//
//}
