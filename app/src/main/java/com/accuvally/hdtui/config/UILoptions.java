package com.accuvally.hdtui.config;

import android.graphics.Bitmap;

import com.accuvally.hdtui.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UILoptions {

	public static DisplayImageOptions rectangleOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image)
			.showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image)
//			.displayer(new RoundedBitmapDisplayer(15))
			.cacheInMemory(true).cacheOnDisk(true).build();

	public static DisplayImageOptions squareOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_square_image)
			.showImageForEmptyUri(R.drawable.default_square_image).showImageOnFail(R.drawable.default_square_image)
			.cacheInMemory(true).cacheOnDisk(true).build();

//	public static DisplayImageOptions sponsorOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//			.displayer(new FadeInBitmapDisplayer(500)).displayer(new RoundedBitmapDisplayer(12))
//			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image)
//			.showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image)
//			.cacheInMemory(true).cacheOnDisk(true).build();

	public static DisplayImageOptions userOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.user_name)
			.showImageForEmptyUri(R.drawable.user_name).showImageOnFail(R.drawable.user_name).cacheInMemory(true).cacheOnDisk(true)
			.build();
	
	public static DisplayImageOptions defaultUser = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_userimg)
			.showImageForEmptyUri(R.drawable.default_userimg).showImageOnFail(R.drawable.default_userimg).cacheInMemory(true).cacheOnDisk(true)
			.build();
	
	public static DisplayImageOptions msgOptions = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(1))
			.bitmapConfig(Bitmap.Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.showImageOnLoading(R.drawable.default_square_image).showImageForEmptyUri(R.drawable.default_square_image)
			.showImageOnFail(R.drawable.default_square_image).displayer(new RoundedBitmapDisplayer(5)).cacheInMemory(true)
			.cacheOnDisk(true).build();

//	public static DisplayImageOptions fadeOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
//			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.default_rectangle_image)
//			.showImageForEmptyUri(R.drawable.default_rectangle_image).showImageOnFail(R.drawable.default_rectangle_image)
//			.displayer(new RoundedBitmapDisplayer(15)).displayer(new FadeInBitmapDisplayer(500))
//			.cacheInMemory(true).cacheOnDisk(true)
//			.build();

}
