package com.accuvally.hdtui.activity.home.comment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class ImageBrowserActivity2 extends BaseActivity implements OnClickListener {

    public static final String IMGURL="ImageBrowserActivity_IMGURL";
    private String imgUrl;

	private ImageView mIv;
    DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagebrowser);
        imgUrl=getIntent().getStringExtra(IMGURL);
		
//		imgPath = FileCache.getChatRootDir(this);

		mIv = (ImageView) findViewById(R.id.iv);
		mIv.setOnClickListener(this);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(00)).
                bitmapConfig(Bitmap.Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_INT).
                showImageOnLoading(0).showImageForEmptyUri(0).showImageOnFail(0).
                displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoader.getInstance().displayImage(imgUrl,mIv);

			
			mIv.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
//					showDialog();
					return false;
				}
			});
		}



/*	protected void showDialog() {
		if (dialog == null) {
			dialog = new Dialog(mContext, R.style.DefaultDialog);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.dialog_operate_img);

			dialog.findViewById(R.id.tvImgShare).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (info != null && !TextUtils.isEmpty(info.getFileUrl())) {
						showDialogShare("分享图片", "", info.getFileUrl());
					}
				}
			});
			dialog.findViewById(R.id.tvSave).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
					mIv.setDrawingCacheEnabled(true);
					Bitmap bmp = mIv.getDrawingCache();
					ImageTools.saveImageToGallery(imgPath, bmp);
					ToastUtil.showMsg("图片已保存到  " + imgPath.getAbsolutePath());
				}
			});
		}
		dialog.show();
	}
	
	private void showDialogShare(String shareTitle, String startTime, String shareUrl) {
		if (dialogShare == null) {
			dialogShare = new DialogShare(this, shareUrl, shareTitle, startTime, shareUrl);
			dialogShare.setHideShareMsg(true);
			dialogShare.initConfig(this, "分享图片", "", info.getFileUrl(), info.getFileUrl());
			dialogShare.setShareComplete(new shareCallBack() {
				
				@Override
				public void shareSuccess() {
					ToastUtil.showMsg("分享成功");
				}
			});
		}
		dialogShare.showDialog();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.zoomout);
	}*/

	@Override
	public void onClick(View arg0) {
		finish();
	}

}
