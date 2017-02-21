package com.accuvally.hdtui.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * 压缩图片的工具
 * 
 */
public class ImageBig {

	/**
	 * 压缩图片
	 * 
	 * @param context
	 * @param filename
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static String scalePicture(Context context, String filename, int maxWidth, int maxHeight) {

		String url = "";
		Bitmap bitmap = null;
		Bitmap bitmap2 = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filename, opts);//把文件转为bitmap
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
//            Trace.e("scalePicture 原来尺寸","srcWidth："+srcWidth+"  srcHeight:"+srcHeight);
			if (srcWidth <= maxWidth && srcHeight <= maxHeight) {
				return filename;
			}

			int desWidth = 0;
			int desHeight = 0;
			double ratio = 0.0;
			if (srcWidth > srcHeight) {
				ratio = srcWidth / maxWidth;
				desWidth = maxWidth;
				desHeight = (int) (srcHeight / ratio);
			} else {
				ratio = srcHeight / maxHeight;
				desHeight = maxHeight;
				desWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();

//            如果被设置为一个值> 1,要求解码器解码出原始图像的一个子样本,返回一个较小的bitmap,以节省存储空间。
//            例如,inSampleSize = = 2，则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。
//            对于任何值< = 1的同样处置为1。
			newOpts.inSampleSize = (int) (ratio) + 1;
			newOpts.inJustDecodeBounds = false;
//inJustDecodeBounds  如果该 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。
// 但是允许我们查询图片的信息这其中就包括图片大小信息
			newOpts.outWidth = desWidth;
			newOpts.outHeight = desHeight;
			bitmap = BitmapFactory.decodeFile(filename, newOpts);//Decode a file path into a bitmap.

//            Trace.e("scalePicture bitmap.get()", "desWidth/ratio：" + desWidth / ratio + "  desHeight/ratio:"+desHeight/ratio);
//
//            Trace.e("scalePicture 计算","bitmap.getWidth()："+bitmap.getWidth()+"  bitmap.getHeight():"+bitmap.getHeight());

			// 读取图片旋转角度
			final int angle = PickPhoto.readPictureDegree(filename);
//            Trace.e("scalePicture 读取图片旋转角度","angle："+angle);

			bitmap2 = PickPhoto.rotaingImageView(angle, bitmap);
			url = new FileCache(context).getImageCacheDir().getAbsolutePath() + "/M" + System.currentTimeMillis() + ".jpg";
			FileOutputStream out = new FileOutputStream(url);
			if (bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
				out.flush();
				out.close();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
			if (bitmap2 != null && !bitmap2.isRecycled()) {
				bitmap2.recycle();
			}
		}
		return url;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context c, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = c.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public final static Bitmap lessenUriImage(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds
									// 设回 false 了
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		return bitmap;
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1280f;// 这里设置高度为800f
		float ww = 720f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
}
