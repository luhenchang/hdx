package com.accuvally.hdtui.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.accuvally.hdtui.AccuApplication;

public final class ImageTools {

	public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
		if (checkSDCardAvailable()) {
			File photoFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void savePhotoToSDCard(Bitmap photoBitmap, String path) {
		if (checkSDCardAvailable()) {
			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class Info {
		public int w;
		public int h;
		public long size;
		public String path;

		public Info(int w, int h, long size, String path) {
			super();
			this.w = w;
			this.h = h;
			this.size = size;
			this.path = path;
		}
	}

	/**
	 * 根据固定的尺寸，计算缩放比例
	 * 
	 * @param path
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 * @throws Exception
	 */
	public static Info savePhotoToSDCard(String path, float dstWidth, float dstHeight) throws Exception {
		if (!checkSDCardAvailable()) {
			throw new Exception("SD卡错误");
		}

		Bitmap bmp = saveFixImage(path, dstWidth, dstHeight);

		// 三星的设备会出现这个问题
		if (true) {
			// 读取图片旋转角度
			final int angle = PickPhoto.readPictureDegree(path);
			// 旋转图片 动作
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			// 创建新的图片
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		File photoFile = new File(path);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(photoFile);
			if (bmp != null) {
				if (bmp.compress(CompressFormat.PNG, 100, fileOutputStream)) {
					fileOutputStream.flush();
				}

			}
		} catch (Exception e) {
			photoFile.delete();
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Info(bmp.getWidth(), bmp.getHeight(), bmp.getRowBytes() * bmp.getHeight(), path);
	}

	/**
	 * 根据宽度求高度
	 * 
	 * @param context
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static int[] scaleXY(Context ctx, int srcWidth, int srcHeight) {
		WindowManager wManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(outMetrics);
		int[] pix = new int[2];
		float dstW = 0;
		int dstH = 0;
		if (srcWidth > srcHeight) {
			// 图像是横着拍的
			dstW = outMetrics.widthPixels * 0.36f;
		} else {
			dstW = (outMetrics.widthPixels * 0.2f);
		}
		// 如果小于，直接用bitmap的原始宽高
		if (srcWidth < dstW) {
			pix[0] = srcWidth;
			pix[1] = srcHeight;
			return pix;
		}

		double ratio = srcWidth / dstW;
		dstH = (int) (srcHeight / ratio);
		pix[0] = (int) dstW;
		pix[1] = dstH;
		return pix;
	}

	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	public static final Bitmap convertToBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			BitmapFactory.decodeFile(path, opts);
			int width = opts.outWidth;
			int height = opts.outHeight;
			float scaleWidth = 0.f, scaleHeight = 0.f;
			if (width > w || height > h) {
				scaleWidth = ((float) width) / w;
				scaleHeight = ((float) height) / h;
			}
			opts.inJustDecodeBounds = false;
			float scale = Math.max(scaleWidth, scaleHeight);
			opts.inSampleSize = (int) scale;
			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
			Bitmap bMapRotate = Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), null, true);
			if (bMapRotate != null) {
				return bMapRotate;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap saveFixImage(String srcPath, float dstWidth, float dstHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int inSampleSize = 1;
		if (w > h) {
			if (w > dstWidth) {
				inSampleSize = (int) (w / dstWidth);
			}
		} else {
			if (h > dstHeight) {
				inSampleSize = (int) (h / dstHeight);
			}
		}
		newOpts.inSampleSize = inSampleSize;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		return BitmapFactory.decodeFile(srcPath, newOpts);
	}

	public static Info compressBmpToFile(String srcFilePath, String compressPath) {
		File srcFile = new File(srcFilePath);
		File compressFile = new File(compressPath, srcFile.getName());

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		if (compressFile.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(compressFile.getAbsolutePath(), options);
			return new Info(bmp.getWidth(), bmp.getHeight(), bmp.getRowBytes() * bmp.getHeight(), compressFile.getAbsolutePath());
		}

		Bitmap bmp = BitmapFactory.decodeFile(srcFilePath, options);
		compressBitmap(compressFile, bmp);

		return new Info(bmp.getWidth(), bmp.getHeight(), bmp.getRowBytes() * bmp.getHeight(), compressFile.getAbsolutePath());
	}

	public static void saveImageToGallery(File filePath, Bitmap bmp) {
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(filePath, fileName);
		compressBitmap(file, bmp);

		AccuApplication mContext = AccuApplication.getInstance();
//		String insertPath = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bmp, fileName, "huodongxing");

		mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
	}

	public static void compressBitmap(File file, Bitmap bmp) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 70, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}