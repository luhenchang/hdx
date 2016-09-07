package com.accuvally.hdtui.activity.message;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.db.SystemMessageTable;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.SystemUtil;
import com.accuvally.hdtui.utils.ToastUtil;
import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends BaseActivity {

	private SwipeMenuListView mListView;
	private CommonAdapter<MessageInfo> mAdapter;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friend);
		initView();
	}

	public void initView() {
		setTitle("新同伴");

		mListView = (SwipeMenuListView) findViewById(R.id.listview);

		final List<MessageInfo> list = SystemMessageTable.queryAllMsg();

		mAdapter = new CommonAdapter<MessageInfo>(this, R.layout.listitem_new_friend) {

			@Override
			public void convert(ViewHolder viewHolder, final MessageInfo item, int position) {
				viewHolder.setImageUrl(R.id.ivFriendAvatar, item.getLogourl(), UILoptions.defaultUser);
				viewHolder.setText(R.id.tvTitle, item.title);
				viewHolder.setText(R.id.tvTime, item.getSessionTime());
				viewHolder.setText(R.id.tvContent, item.newContent);

				Button btAccept = viewHolder.getView(R.id.btAccept);
				if (item.extend == 1) {
					btAccept.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							requestAgree(item);
						}
					});
				} else if (item.extend == 2) {
					btAccept.setText("已添加");
					btAccept.setBackgroundResource(R.drawable.gary_bg_btn);
					btAccept.setEnabled(false);

					int padding = getResources().getDimensionPixelSize(R.dimen.padding_10);
					btAccept.setPadding(padding, padding, padding, padding);
				}
			}
		};
		mAdapter.setList(list);
		mListView.setAdapter(mAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				deleteItem.setWidth(SystemUtil.dpToPx(mContext, 90));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);
			}
		};
		mListView.setMenuCreator(creator);

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				SystemMessageTable.deleteMessage(list.get(position).getMessageId());
				list.remove(position);
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	private void requestAgree(final MessageInfo item) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("accept", true + ""));
		params.add(new BasicNameValuePair("target", item.getUserId()));

		showProgress("同意添加对方");
		httpCilents.postA(Url.social_accept, params, new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				dismissProgress();
				if (code == Config.RESULT_CODE_ERROR) {
					ToastUtil.showMsg("网络连接断开，请检查网络");
					return;
				}
				if (code == Config.RESULT_CODE_SUCCESS) {
					BaseResponse response = JSON.parseObject((String) result, BaseResponse.class);
					ToastUtil.showMsg(response.msg);

					SystemMessageTable.updateExtend(item.getMessageId(), 2);

					item.extend = 2;
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

}
