package com.accuvally.hdtui.fragment.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.LoginActivityNew;
import com.accuvally.hdtui.activity.TicketTabActivity;
import com.accuvally.hdtui.activity.TicketVolumeActivity;
import com.accuvally.hdtui.adapter.CommonAccuAdapter;
import com.accuvally.hdtui.adapter.CommonAdapter;
import com.accuvally.hdtui.adapter.CommonAdapter.OnItemListener;
import com.accuvally.hdtui.adapter.ViewHolder;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.UILoptions;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.fragment.ManagerFragment;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.ui.XListView;
import com.accuvally.hdtui.ui.XListView.IXListViewListener;
import com.accuvally.hdtui.ui.calender.CustomDate;
import com.accuvally.hdtui.ui.calender.DateUtil;
import com.accuvally.hdtui.ui.calender.ManagerFragmentHelp;
import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
import com.accuvally.hdtui.utils.eventbus.ChangeHomeLoaderEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeMainSelectEventBus;
import com.accuvally.hdtui.utils.eventbus.ChangeUserStateEventBus;
import com.accuvally.hdtui.utils.eventbus.EventCustomDate;
import com.accuvally.hdtui.utils.eventbus.EventEnroll;
import com.accuvally.hdtui.utils.eventbus.EventHideFooterView;
import com.alibaba.fastjson.JSON;

import de.greenrobot.event.EventBus;

/*
 * 报名活动，已报名,我的票券
 */
public class EnrollFragment extends BaseFragment implements OnClickListener {

	private XListView mListView;
	private CommonAdapter mAdapter;
	private int pageIndex = 1, pageSize = 10;
	private View emptyView;
	private LinearLayout lyFailure;

	private TextView tvNoData;

	private ImageView ivFailure;

	private Button SquareBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ChangeUserStateEventBus stateEventBus) {
		showStateView();
		if (stateEventBus.getMsg() == ChangeUserStateEventBus.LOGIN) {
			pageIndex = 1;
			mAdapter.removeAll();
			requestData();
		}
	}

	public void onEventMainThread(EventEnroll eventEnroll) {
		pageIndex = 1;
		requestData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
		emptyView = view.findViewById(R.id.manageEmpty);
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, LoginActivityNew.class);
				startActivity(intent);
			}
		};
		view.findViewById(R.id.btLogin).setOnClickListener(onClickListener);

		mListView = (XListView) view.findViewById(R.id.lvCollect);

		lyFailure = (LinearLayout) view.findViewById(R.id.lyFailure);
		tvNoData = (TextView) view.findViewById(R.id.tvNoData);
		ivFailure = (ImageView) view.findViewById(R.id.ivFailure);
		SquareBtn = (Button) view.findViewById(R.id.SquareBtn);

		ivFailure.setBackgroundResource(R.drawable.no_emty);
		tvNoData.setText("当前日期没有票券的活动噢");
		SquareBtn.setText("寻找活动");
		SquareBtn.setTextColor(getResources().getColor(R.color.white));
		SquareBtn.setBackgroundResource(R.drawable.selector_wane_green);
		int padding = getResources().getDimensionPixelSize(R.dimen.little_10);
		SquareBtn.setPadding(padding, padding, padding, padding);

		SquareBtn.setOnClickListener(this);
		
		setListView();

		String cache = application.cacheUtils.getAsString(application.getUserInfo().getAccount() + "_enroll");
		if (!TextUtils.isEmpty(cache)) {
			List<SelInfo> listCache = JSON.parseArray(cache, SelInfo.class);
			if (listCache != null) {
				mAdapter.addAll(listCache);
				ManagerFragmentHelp.putAll(EnrollFragment.class, listCache);
			}
		}

		showStateView();
		requestData();

		return view;
	}

	private void setListView() {
		mListView.setAdapter(mAdapter = new CommonAdapter<SelInfo>(mContext, R.layout.listitem_unfinish) {

			@Override
			public void convert(ViewHolder viewHolder, final SelInfo item, int position) {
				viewHolder.setImageUrl(R.id.ivSelImage, item.getLogo());
				viewHolder.setText(R.id.tvSelTitle,item.getTitle());
				viewHolder.setText(R.id.tvSelItemTime, item.getTimeStr());
				viewHolder.setText(R.id.tvSelAddress,item.getAddress());
				viewHolder.setText(R.id.tvState,item.getStatusstr());
				
				TextView tvItemPriceArea = viewHolder.getView(R.id.tvSelPriceArea);
				tvItemPriceArea.setText(item.getPriceArea());
				if ("免费".equals(item.getPriceArea())) {
					tvItemPriceArea.setTextColor(mContext.getResources().getColor(R.color.price_free));
				} else {
					tvItemPriceArea.setTextColor(mContext.getResources().getColor(R.color.price_charge));
				}
				
				viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(mContext, TicketVolumeActivity.class);
						intent.putExtra("id", item.getId());
						intent.putExtra("SourceType", item.getSourceType());

						mContext.startActivity(intent);
					}
				});
			}
		});
		
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				requestData();
			}

			@Override
			public void onLoadMore() {
				pageIndex++;
				requestData();
			}
		});
	}

	private void showStateView() {
		if (application.checkIsLogin()) {
			emptyView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		} else {
			emptyView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	// 点击日期做查询
	private CustomDate mCustomDate;// 查询的时间

	public void onEventMainThread(EventCustomDate bus) {
		if(bus.clazz==getClass())
			requestData();
	}

	public void onEventMainThread(EventHideFooterView event) {
		if (event.clazz == this.getClass()) {
			ManagerFragment parentF = (ManagerFragment) getParentFragment();
			if (parentF != null) {
				((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = parentF.getListViewBottomMargin();
			}
		}
	}

	public void requestData() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pi", pageIndex + ""));
		params.add(new BasicNameValuePair("ps", pageSize + ""));
		boolean onlyUnexpired = application.sharedUtils.readBoolean("onlyunexpired", true);
		params.add(new BasicNameValuePair("excludeExpired", String.valueOf(onlyUnexpired)));// 仅显示未过期
		// 增加日期查询
		TicketTabActivity parentF = (TicketTabActivity) getActivity();
		if (parentF != null) {
			((RelativeLayout.LayoutParams) mListView.getLayoutParams()).bottomMargin = parentF.getListViewBottomMargin();
		}
		if (parentF.getCustomDate() != null) {
			mCustomDate = parentF.getCustomDate();
			params.add(new BasicNameValuePair("start", DateUtil.getDayStartEnd(mCustomDate)[0]));
			params.add(new BasicNameValuePair("end", DateUtil.getDayStartEnd(mCustomDate)[1]));
		}

		EventBus.getDefault().post(new ChangeHomeLoaderEventBus(true));
		httpCilents.get(httpCilents.printURL(Url.ACCUPASS_MYATTEND, params), new WebServiceCallBack() {

			@Override
			public void callBack(int code, Object result) {
				EventBus.getDefault().post(new ChangeHomeLoaderEventBus(false));
				mListView.stopRefresh();
				mListView.stopLoadMore();
				switch (code) {
				case Config.RESULT_CODE_SUCCESS:
					BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
					if (response.isSuccess()) {
						List<SelInfo> list = JSON.parseArray(response.result, SelInfo.class);
						if (list == null) {
							if (pageIndex == 1) {
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
							return;
						}
						if (pageIndex == 1) {
							mAdapter.removeAll();
							if (list.size() == 0) {
								lyFailure.setVisibility(View.VISIBLE);
								mListView.setEmptyView(lyFailure);
							}
						}
						mAdapter.addAll(list);
						ManagerFragmentHelp.putAll(EnrollFragment.class, list);
						// 缓存
						if (pageIndex == 1 && list != null && list.size() != 0) {
							application.cacheUtils.put(application.getUserInfo().getAccount() + "_enroll", response.result);
						}
					} else {
						application.showMsg(response.getMsg());
					}
					break;
				case Config.RESULT_CODE_ERROR:
					application.showMsg(result.toString());
					break;
				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SquareBtn:
			EventBus.getDefault().post(new ChangeMainSelectEventBus(2));
			getActivity().finish();
			break;
		}
	}
}