package com.accuvally.hdtui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.QuestionBean;
import com.accuvally.hdtui.utils.ViewHolder;

public class QuestionAdapter extends BaseListAdapter<QuestionBean> {

	public QuestionAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_question, null);
		}
		TextView tvQuestion = ViewHolder.get(convertView, R.id.tvQuestion);
		final QuestionBean bean = mList.get(position);
		tvQuestion.setText(bean.getQ());

//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(mContext, QuestionDetailActivity.class);
//				intent.putExtra("webData", bean.getA());
//				mContext.startActivity(intent);
//			}
//		});
		return convertView;
	}

}
