package com.accuvally.hdtui.activity;

import java.io.File;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.model.MessageInfo;
import com.accuvally.hdtui.ui.DialogShare;
import com.accuvally.hdtui.utils.FileCache;
import com.accuvally.hdtui.utils.ImageTools;
import com.accuvally.hdtui.utils.ShareUtils.shareCallBack;
import com.accuvally.hdtui.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageBrowserActivity extends BaseActivity implements OnClickListener {

	private ImageView mIv;
	private DisplayImageOptions options;
	private Dialog dialog;
	private DialogShare dialogShare;
	private File imgPath;
	private MessageInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagebrowser);
		
		imgPath = FileCache.getChatRootDir(this);

		mIv = (ImageView) findViewById(R.id.iv);
		mIv.setOnClickListener(this);
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(00)).bitmapConfig(Bitmap.Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(0).showImageForEmptyUri(0).showImageOnFail(0).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true).cacheOnDisk(true).build();

		info = (MessageInfo) getIntent().getSerializableExtra("MessageInfo");
		mIv.getLayoutParams().width = info.getImgWidth();
		mIv.getLayoutParams().height = info.getImgHeight();
		if (info != null) {
			String url = info.getFilePath();
			if (TextUtils.isEmpty(url) && !new File(url).exists()) {
				url = info.getFileThumbnailUrl();
				application.mImageLoader.displayImage(url, mIv);
				// 从网络获取
				url = info.getFileUrl();
				application.mImageLoader.loadImage(url, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						application.mImageLoader.displayImage(imageUri, mIv);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
			} else {
				application.mImageLoader.displayImage("file://" + url, mIv);
			}
			
			mIv.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					showDialog();
					return false;
				}
			});
		}
	}

	protected void showDialog() {
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
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}

}
