package com.accuvally.hdtui.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.Category;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.ui.CustomerScrollView;
import com.accuvally.hdtui.ui.ScrolListView;
import com.accuvally.hdtui.utils.TimeUtils;
import com.accuvally.hdtui.utils.eventbus.ChangeBackHomeEventBus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import de.greenrobot.event.EventBus;

/**
 * 浏览历史
 * 
 * @author Vanness
 * 
 */
public class HistoryActivity extends BaseActivity implements OnClickListener {
	private ScrolListView history_listview;
	private ArrayList<Category> mData;
	private HistoryAdapter historyAdapter;
	private boolean isMulChoice;
	private boolean isAllCheck;
	private ImageView delete_img;
	private ArrayList<String> selection = new ArrayList<String>();// 记录被选中条目id;
	private LinearLayout share_ly;
	private HashMap<Integer, Boolean> ischeck = new HashMap<Integer, Boolean>();;
	private CustomerScrollView scrollView;
	private List<HomeEventInfo> homes;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initView();
		initData();
	}

	public void initView() {
		setTitle(getResources().getString(R.string.setting_tab_history));
		setViewFailure();
		share_ly = (LinearLayout) findViewById(R.id.share_ly);
		share_ly.setVisibility(8);
		history_listview = (ScrolListView) findViewById(R.id.history_listview);
		scrollView = (CustomerScrollView) findViewById(R.id.scrollView);
		delete_img = (ImageView) findViewById(R.id.two_img);
		delete_img.setBackgroundResource(R.drawable.delete_bg);
		share_ly.setOnClickListener(this);
		showFailureOnClick(new OnClickCallBack() {
			public void callBack(boolean is) {
				if (is) {
					EventBus.getDefault().post(new ChangeBackHomeEventBus(0));
					finish();
				} else {
					initData();
				}
			}
		});
	}

	public void initData() {
		dbManager = new DBManager(HistoryActivity.this);
		mData = getData();
		if (homes.size() == 0) {
			scrollView.setVisibility(8);
			showFailure();
		} else {
			scrollView.setVisibility(0);
			notifyAdapter();
		}
	}

	/**
	 * 创建测试数据
	 */
	private ArrayList<Category> getData() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String createDate = TimeUtils.DF_YMD.format(curDate);
		homes = dbManager.getHistory();
		Category categoryOne = new Category("今天");
		Category categoryTwo = new Category("更早以前");
		for (int i = 0; i < homes.size(); i++) {
			if (homes.get(i).getStart().equals(createDate))
				categoryOne.addItem(homes.get(i));
			else
				categoryTwo.addItem(homes.get(i));
		}
		ArrayList<Category> listData = new ArrayList<Category>();
		listData.add(categoryOne);
		listData.add(categoryTwo);
		return listData;
	}

	private class HistoryAdapter extends BaseAdapter {
		private ArrayList<Category> lists;
		private LayoutInflater inflater;
		private static final int TYPE_CATEGORY_ITEM = 0;
		private static final int TYPE_ITEM = 1;
		private DisplayImageOptions options;

		public HistoryAdapter(ArrayList<Category> pData) {
			inflater = LayoutInflater.from(HistoryActivity.this);
			lists = pData;
			options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(500)).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image).showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image).cacheInMemory(true).cacheOnDisk(true).build();
		}

		public int getViewTypeCount() {
			return 2;
		}

		public Object getItem(int position) {
			// 异常情况处理
			if (null == lists || position < 0 || position > getCount()) {
				return null;
			}
			// 同一分类内，第一个元素的索引值
			int categroyFirstIndex = 0;
			for (Category category : lists) {
				int size = category.getItemCount();
				// 在当前分类中的索引值
				int categoryIndex = position - categroyFirstIndex;
				// item在当前分类内
				if (categoryIndex < size) {
					return category.getItem(categoryIndex);
				}
				// 索引移动到当前分类结尾，即下一个分类第一个元素索引
				categroyFirstIndex += size;
			}
			return null;
		}

		public int getItemViewType(int position) {
			// 异常情况处理
			if (null == lists || position < 0 || position > getCount()) {
				return TYPE_ITEM;
			}
			int categroyFirstIndex = 0;
			for (Category category : lists) {
				int size = category.getItemCount();
				// 在当前分类中的索引值
				int categoryIndex = position - categroyFirstIndex;
				if (categoryIndex == 0) {
					return TYPE_CATEGORY_ITEM;
				}
				categroyFirstIndex += size;
			}
			return TYPE_ITEM;
		}

		public int getCount() {
			int count = 0;
			if (null != lists) {
				// 所有分类中item的总和是ListVIew Item的总个数
				for (Category category : lists) {
					count += category.getItemCount();
				}
			}
			return count;
		}

		public long getItemId(int position) {
			return lists == null ? 0 : position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			int itemViewType = getItemViewType(position);
			switch (itemViewType) {
			case TYPE_CATEGORY_ITEM:
				if (null == convertView) {
					convertView = inflater.inflate(R.layout.listitem_history_list, null);
				}
				TextView textView = (TextView) convertView.findViewById(R.id.history_title);
				Button all_btn = (Button) convertView.findViewById(R.id.all_btn);
				View child_line_two = (View) convertView.findViewById(R.id.child_line_two);
				String itemValue = (String) getItem(position);
				textView.setText(itemValue);
				if (position == 0) {
					child_line_two.setVisibility(4);
					if (isMulChoice) {
						all_btn.setVisibility(0);
					} else {
						all_btn.setVisibility(8);
					}
				} else {
					child_line_two.setVisibility(0);
					all_btn.setVisibility(8);
				}
				all_btn.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						selection.clear();
						if (isAllCheck) {
							isAllCheck = false;
							selection.clear();
							if (ischeck != null)
								ischeck.clear();
						} else {
							isMulChoice = true;
							isAllCheck = true;
							for (int i = 0; i < lists.size(); i++) {
								List<HomeEventInfo> list = lists.get(i).getmCategoryItem();
								for (int j = 0; j < list.size(); j++) {
									selection.add(list.get(j).getId());
								}
							}
						}
						historyAdapter = new HistoryAdapter(mData);
						history_listview.setAdapter(historyAdapter);
					}
				});
				break;
			case TYPE_ITEM:
				ViewHolder viewHolder = null;
				if (null == convertView) {
					convertView = inflater.inflate(R.layout.listitem_history_child_list, null);
					viewHolder = new ViewHolder();
					viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
					viewHolder.item_checkbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
					viewHolder.history_child_ll = (LinearLayout) convertView.findViewById(R.id.history_child_ll);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				final HomeEventInfo home = (HomeEventInfo) getItem(position);
				// 绑定数据
				viewHolder.tv_title.setText(home.getTitle() + "");
				final CheckBox checkBox = viewHolder.item_checkbox;
				application.mImageLoader.displayImage(home.getLogoUrl(), viewHolder.iv_image, options);
				viewHolder.item_checkbox.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						String value = home.getId();
						if (checkBox.isChecked()) {
							ischeck.put(position, true);
							for (int i = 0; i < selection.size(); i++) {
								if (selection.get(i).equals(value)) {
									selection.remove(value);
								}
							}
							selection.add(value);
						} else {
							ischeck.put(position, false);
							selection.remove(value);
						}
					}
				});
				if (ischeck.get(position) != null) {
					viewHolder.item_checkbox.setChecked(ischeck.get(position));
				} else {
					viewHolder.item_checkbox.setChecked(false);
				}

				if (isAllCheck) {
					viewHolder.item_checkbox.setChecked(true);
				} else if (ischeck.get(position) == null) {
					viewHolder.item_checkbox.setChecked(false);
				}

				if (isMulChoice) {
					viewHolder.item_checkbox.setVisibility(View.VISIBLE);
				} else {
					viewHolder.item_checkbox.setVisibility(View.GONE);
				}

				viewHolder.history_child_ll.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View arg0) {
						isMulChoice = true;
						ischeck.put(position, true);
						String value = home.getId();
						for (int i = 0; i < selection.size(); i++) {
							if (selection.get(i).equals(value)) {
								selection.remove(value);
							}
						}
						selection.add(value);
						historyAdapter.notifyDataSetChanged();
						share_ly.setVisibility(View.VISIBLE);
						return true;
					}
				});
				viewHolder.history_child_ll.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						if (home.getSourceType() == 1) {
							mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", home.getId()).putExtra("isHuodong", 1));
						} else {
							mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", home.getId()).putExtra("isHuodong", 0));
						}
					}
				});

				break;
			}
			return convertView;
		}

		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItemViewType(position) != TYPE_CATEGORY_ITEM;
		}

		private class ViewHolder {
			TextView tv_title;
			CheckBox item_checkbox;
			LinearLayout history_child_ll;
			ImageView iv_image;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.share_ly:
			if (selection.size() == 0) {
				Toast.makeText(HistoryActivity.this, "还没有选择需要删除的项 ", 4).show();
			} else {
				if (ischeck != null)
					ischeck.clear();
				for (int i = 0; i < selection.size(); i++) {
					dbManager.deleteHisttory(selection.get(i));
					isMulChoice = false;
					mData = getData();
					if (homes.size() == 0) {
						scrollView.setVisibility(View.GONE);
						showFailure();
					} else {
						scrollView.setVisibility(View.VISIBLE);
						notifyAdapter();
					}
					share_ly.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}
	}

	private void notifyAdapter() {
		historyAdapter = new HistoryAdapter(mData);
		history_listview.setAdapter(historyAdapter);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && share_ly.getVisibility() == View.VISIBLE) {
			share_ly.setVisibility(View.GONE);
			isMulChoice = false;
			if (homes.size() == 0) {
				scrollView.setVisibility(8);
				showFailure();
			} else {
				scrollView.setVisibility(0);
				if (selection != null)
					selection.clear();
				if (ischeck != null)
					ischeck.clear();
				notifyAdapter();
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
}
