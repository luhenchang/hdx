package com.accuvally.hdtui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
import com.accuvally.hdtui.activity.ProjectDetailsActivity;
import com.accuvally.hdtui.db.DBManager;
import com.accuvally.hdtui.model.HomeEventInfo;
import com.accuvally.hdtui.model.Project;
import com.accuvally.hdtui.model.SquareLimitedTimeOffers;
import com.accuvally.hdtui.utils.Util;
import com.accuvally.hdtui.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class HomeEventNewAdapter extends BaseListAdapter<HomeEventInfo> {

	Animation animation;

	DisplayImageOptions options;
	DBManager db;
	int temp;
	private List<Project> topic;
	private List<SquareLimitedTimeOffers> limitActs;
	private int currentType;
	private final int FIRST_TYPE = 0;
	private final int OTHERS_TYPE = 1;

	private View firstItemView;

	private View othersItemView;

	private int controlPosition = -1;

	public List<Project> getTopic() {
		return topic;
	}

	public void setTopic(List<Project> topic) {
		if (topic != null) {
			this.topic = topic;
		} else {
			this.topic = new ArrayList<Project>();
		}
	}

	public List<SquareLimitedTimeOffers> getLimitActs() {
		return limitActs;
	}

	public void setLimitActs(List<SquareLimitedTimeOffers> limitActs) {
		if (limitActs != null) {
			this.limitActs = limitActs;
		} else {
			this.limitActs = new ArrayList<SquareLimitedTimeOffers>();
		}
	}

	public HomeEventNewAdapter(Context context, int temp) {
		super(context);
		db = new DBManager(context);
		this.temp = temp;
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_details_image).showImageForEmptyUri(R.drawable.default_details_image).showImageOnFail(R.drawable.default_details_image).cacheInMemory(true).displayer(new FadeInBitmapDisplayer(200)).cacheOnDisk(true).build();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if ((topic != null && topic.size() > 0) || (limitActs != null && limitActs.size() > 0)) {

			if (mList.size() > 3) {
				controlPosition = 4;
			} else {
				controlPosition = mList.size();
			}
			if (position == controlPosition) {
				return FIRST_TYPE;
			} else {
				return OTHERS_TYPE;
			}
		} else {
			return OTHERS_TYPE;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList == null) {
			return 0;
		} else {
			if ((topic != null && topic.size() > 0) && (limitActs != null && limitActs.size() > 0)) {
				return mList.size() + 1;
			} else {
				controlPosition = -1;
				return mList.size();
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentType = getItemViewType(position);
		if (currentType == FIRST_TYPE) {
			firstItemView = convertView;
			ViewHolder1 viewHolder1 = null;
			if (firstItemView == null) {
				firstItemView = mInflater.inflate(R.layout.listitem_home_new1, null);
				viewHolder1 = new ViewHolder1();
				viewHolder1.ll_limited = (LinearLayout) firstItemView.findViewById(R.id.ll_limited);
				viewHolder1.ll_topic = (LinearLayout) firstItemView.findViewById(R.id.ll_topic);
				firstItemView.setTag(viewHolder1);
				DisplayMetrics dm = new DisplayMetrics();
				((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = (int) (dm.widthPixels * 0.8);
				LayoutParams lp = new LayoutParams(width, (int) (width * 0.4));
				try {
					for (int i = 0; i < limitActs.size(); i++) {
						ImageView imageView = new ImageView(mContext);
						lp.setMargins(0, 0, 20, 0);
						imageView.setScaleType(ScaleType.FIT_START);
						imageView.setLayoutParams(lp);
						application.mImageLoader.displayImage(limitActs.get(i).getLogoUrl(), imageView);
						final String id = limitActs.get(i).getId();
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", id).putExtra("isHuodong", 0));
							}
						});
						viewHolder1.ll_limited.addView(imageView);
					}
				} catch (Exception e) {
				}
				try {
					for (int i = 0; i < topic.size(); i++) {
						ImageView imageView = new ImageView(mContext);
						lp.setMargins(0, 0, 20, 0);
						imageView.setScaleType(ScaleType.FIT_START);
						imageView.setLayoutParams(lp);
						application.mImageLoader.displayImage(topic.get(i).getLogo(), imageView);
						final String id = topic.get(i).getId();
						final String title = topic.get(i).getTitle();
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mContext.startActivity(new Intent(mContext, ProjectDetailsActivity.class).putExtra("id", id).putExtra("title", title));
							}
						});
						viewHolder1.ll_topic.addView(imageView);
					}
				} catch (Exception e) {
				}
			} else {
				viewHolder1 = (ViewHolder1) firstItemView.getTag();
			}
			convertView = firstItemView;
		} else {
			othersItemView = convertView;
			ViewHolder viewHolder = null;
			if (othersItemView == null) {
				othersItemView = mInflater.inflate(R.layout.listitem_home_new, null);
				viewHolder = new ViewHolder();
				viewHolder.ll_fy_homeItem = (LinearLayout) othersItemView.findViewById(R.id.ll_fy_homeItem);
				viewHolder.ivImage = (ImageView) othersItemView.findViewById(R.id.ivImage);
				viewHolder.ivIsFollow = (ImageView) othersItemView.findViewById(R.id.ivIsFollow);
				viewHolder.tvHomeTitle = (TextView) othersItemView.findViewById(R.id.tvHomeTitle);
				viewHolder.tvHomeAddress = (TextView) othersItemView.findViewById(R.id.tvHomeAddress);
				viewHolder.tvHomeTime = (TextView) othersItemView.findViewById(R.id.tvHomeTime);
				othersItemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) othersItemView.getTag();
			}
			final HomeEventInfo info;
			if (mList != null && mList.size() > 0) {
				if (position > controlPosition && controlPosition > 0) {
					info = mList.get(position - 1);
				} else {
					info = mList.get(position);

				}
				if (info != null) {
					viewHolder.tvHomeTitle.setText(Util.stringFilter(info.getTitle()));
					String str = "";
					if (info.getAddress().length() > 6) {
						str = info.getAddress().substring(0, 5) + "...";
					} else {
						str = info.getAddress();
					}
					String addressAndTime = str + "  |  " + info.getStart().substring(5, 10).replace("-", "月");
					viewHolder.tvHomeAddress.setText(addressAndTime);

					if (!"".equals(info.getStatusStr())) {
						viewHolder.tvHomeTime.setVisibility(View.VISIBLE);
						viewHolder.tvHomeTime.setText(info.getStatusStr());
					} else {
						viewHolder.tvHomeTime.setVisibility(View.GONE);
					}
					application.mImageLoader.displayImage(info.getLogoUrl(), viewHolder.ivImage);
					viewHolder.ll_fy_homeItem.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (Utils.isFastDoubleClick())
								return;
							db.insertHistory(info);
							db.insertSaveBeHavior(application.addBeHavior(10, 0+"", info.getId(), "","","",""));
							if (info.getSourceType() == 1) {
								mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 1));
							} else {
								mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getId()).putExtra("isHuodong", 0));
							}
						}
					});
					convertView = othersItemView;
				}
			}
		}
		return convertView;
	}

	static class ViewHolder {
		LinearLayout ll_fy_homeItem;
		ImageView ivImage;
		ImageView ivIsFollow;
		TextView tvHomeTitle, tvHomeAddress, tvHomeTime;

	}

	static class ViewHolder1 {
		// ImageView image1,image2,image3,image4;
		LinearLayout ll_limited, ll_topic;
	}
}
