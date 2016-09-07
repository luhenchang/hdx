package com.accuvally.hdtui.activity.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.QuestionAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.QuestionBean;
import com.accuvally.hdtui.model.QuestionResultBean;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 常见疑问
 * 
 * @author Semmer Wang
 * 
 */
public class QuestionActivity extends BaseActivity {

	private ListView mListView;
	private QuestionAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		initView();
		getJsonData();
	}

	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new QuestionAdapter(this);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				QuestionBean bean = mAdapter.getList().get(arg2);
				Intent intent = new Intent(mContext, QuestionDetailActivity.class);
				intent.putExtra("webData", bean.getA());
				intent.putExtra("title", bean.getQ());
				mContext.startActivity(intent);
			}
		});

		findViewById(R.id.ivBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		String value = application.cacheUtils.getAsString("faq");
		if (!TextUtils.isEmpty(value)) {
			QuestionResultBean object = JSON.parseObject(value, QuestionResultBean.class);
			List<QuestionBean> list = object.getFAQ();
			mAdapter.setList(list);
		}
	}

	private void getJsonData() {
		httpCilents.get(Url.ACCUPASS_GET_FAQ, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
					if (messageInfo.isSuccess()) {
						application.cacheUtils.put("faq", messageInfo.getResult());
						QuestionResultBean object = JSON.parseObject(messageInfo.getResult(), QuestionResultBean.class);
						List<QuestionBean> list = object.getFAQ();
						mAdapter.setList(list);
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
