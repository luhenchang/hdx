package com.accuvally.hdtui.adapter;//package com.accuvally.hdtui.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.OrganizersDetailsActivity;
//import com.accuvally.hdtui.activity.RegisterActivity;
//import com.accuvally.hdtui.config.Config;
//import com.accuvally.hdtui.config.Url;
//import com.accuvally.hdtui.model.HomeOrgsInfo;
//import com.accuvally.hdtui.utils.HttpCilents;
//import com.accuvally.hdtui.utils.HttpCilents.WebServiceCallBack;
//import com.accuvally.hdtui.utils.NetworkUtils;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
//public class OrganizerAdapter extends BaseListAdapter<HomeOrgsInfo> {
//
//	DisplayImageOptions options;
//
//	HttpCilents httpCilents;
//
//	public OrganizerAdapter(Context context) {
//		super(context);
//		httpCilents = new HttpCilents(mContext);
//		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square_image).showImageForEmptyUri(R.drawable.default_square_image).showImageOnFail(R.drawable.default_square_image).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisk(true).build();
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		final ViewHolder viewHolder;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.listitem_organizers, null);
//			viewHolder = new ViewHolder();
//			viewHolder.ivOrgLogoUrl = (ImageView) convertView.findViewById(R.id.ivOrgLogoUrl);
//			viewHolder.tvOrgName = (TextView) convertView.findViewById(R.id.tvOrgName);
//			viewHolder.tvOrgNum = (TextView) convertView.findViewById(R.id.tvOrgNum);
//			viewHolder.tvOrgFollow = (TextView) convertView.findViewById(R.id.tvOrgFollow);
//			viewHolder.ivIsFollow = (ImageView) convertView.findViewById(R.id.ivIsFollow);
//			viewHolder.org_progressbar = (ProgressBar) convertView.findViewById(R.id.org_progressbar);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		final HomeOrgsInfo info = mList.get(position);
//		viewHolder.tvOrgName.setText(info.getName());
//		viewHolder.tvOrgFollow.setText(info.getFollows() + "人");
//		viewHolder.tvOrgNum.setText(info.getEventNum() + "个活动");
//		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivOrgLogoUrl, options);
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				mContext.startActivity(new Intent(mContext, OrganizersDetailsActivity.class).putExtra("orgId", info.getId()));
//			}
//		});
//		if (info.isHasFollowed()) {
//			viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_followed_icon_bg);
//		} else {
//			viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_add_icon_bg);
//		}
//		viewHolder.ivIsFollow.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (!NetworkUtils.isNetworkAvailable(mContext)) {
//					return;
//				}
//				if (!application.checkIsLogin()) {
//					mContext.startActivity(new Intent(mContext, RegisterActivity.class));
//					return;
//				}
//				viewHolder.org_progressbar.setVisibility(View.VISIBLE);
//				viewHolder.ivIsFollow.setVisibility(View.GONE);
//				if (!info.isHasFollowed()) {
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("oid", info.getId()));
//					httpCilents.postA(Url.ACCUPASS_ORGFOLLOW, params, new WebServiceCallBack() {
//
//						@Override
//						public void callBack(int code, Object result) {
//							viewHolder.org_progressbar.setVisibility(View.GONE);
//							switch (code) {
//							case Config.RESULT_CODE_SUCCESS:
//								viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_followed_icon_bg);
//								viewHolder.ivIsFollow.setVisibility(View.VISIBLE);
//								info.setHasFollowed(true);
//								break;
//							case Config.RESULT_CODE_ERROR:
//								// viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_add_icon_bg);
//								viewHolder.ivIsFollow.setVisibility(View.VISIBLE);
//								application.showMsg(result.toString());
//								break;
//							}
//						}
//					});
//				} else {
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("oid", info.getId()));
//					httpCilents.postA(Url.ACCUPASS_ORGUNFOLLOW, params, new WebServiceCallBack() {
//
//						@Override
//						public void callBack(int code, Object result) {
//							viewHolder.org_progressbar.setVisibility(View.GONE);
//							switch (code) {
//							case Config.RESULT_CODE_SUCCESS:
//								viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_add_icon_bg);
//								viewHolder.ivIsFollow.setVisibility(View.VISIBLE);
//								info.setHasFollowed(false);
//								break;
//							case Config.RESULT_CODE_ERROR:
//								// viewHolder.ivIsFollow.setBackgroundResource(R.drawable.organizers_add_icon_bg);
//								viewHolder.ivIsFollow.setVisibility(View.VISIBLE);
//								application.showMsg(result.toString());
//								break;
//							}
//						}
//					});
//				}
//			}
//		});
//		return convertView;
//	}
//
//	static class ViewHolder {
//		ImageView ivOrgLogoUrl, ivIsFollow;
//		TextView tvOrgName, tvOrgNum, tvOrgFollow;
//		ProgressBar org_progressbar;
//	}
//}
