package com.accuvally.hdtui.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.accuvally.hdtui.utils.ImageTools.Info;

public class ImageUtil {

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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		
//		int quality = 70;
//		bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//		while (baos.toByteArray().length / 1024 > 300) {
//			baos.reset();
//			quality -= 10;
//			bmp.compress(Bitmap.CompressFormat.PNG, quality, baos);
//		}

		try {
			FileOutputStream fos = new FileOutputStream(compressFile);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Info(bmp.getWidth(), bmp.getHeight(), bmp.getRowBytes() * bmp.getHeight(), compressFile.getAbsolutePath());
	}
}
