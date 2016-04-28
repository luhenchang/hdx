package com.accuvally.hdtui.adapter;//package com.accuvally.hdtui.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.accuvally.hdtui.R;
//import com.accuvally.hdtui.activity.AccuvallyDetailsActivity;
//import com.accuvally.hdtui.activity.OrganizersDetailsActivity;
//import com.accuvally.hdtui.model.OrgInfo;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
//public class FollowAdapter extends BaseListAdapter<OrgInfo> {
//
//	DisplayImageOptions options;
//
//	public FollowAdapter(Context context) {
//		super(context);
//		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square_image).showImageForEmptyUri(R.drawable.default_square_image).showImageOnFail(R.drawable.default_square_image).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisk(true).build();
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.listitem_my_accuvally2, null);
//			viewHolder = new ViewHolder();
//			viewHolder.ivMyOrgLogo = (ImageView) convertView.findViewById(R.id.ivMyOrgLogo);
//			viewHolder.tvMyOrgTitle = (TextView) convertView.findViewById(R.id.tvMyOrgTitle);
//			viewHolder.tvOrgActTitle = (TextView) convertView.findViewById(R.id.tvOrgActTitle);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		final OrgInfo info = mList.get(position);
//
//		viewHolder.tvMyOrgTitle.setText(info.getName());
//		if (info.getActTitle() == null)
//			viewHolder.tvOrgActTitle.setText("暂无");
//		else
//			viewHolder.tvOrgActTitle.setText(info.getActTitle());
//		application.mImageLoader.displayImage(info.getLogo(), viewHolder.ivMyOrgLogo, options);
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				mContext.startActivity(new Intent(mContext, OrganizersDetailsActivity.class).putExtra("orgId", String.valueOf(info.getId())));
//			}
//		});
//		viewHolder.tvOrgActTitle.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (info.getActTitle() != null)
//					mContext.startActivity(new Intent(mContext, AccuvallyDetailsActivity.class).putExtra("id", info.getActID()).putExtra("isHuodong", 0));
//			}
//		});
//		return convertView;
//	}
//
//	static class ViewHolder {
//		ImageView ivMyOrgLogo;
//		TextView tvMyOrgTitle, tvOrgActTitle;
//	}
//}
