package com.accuvally.hdtui.utils.exception;

import java.io.Closeable;
import java.io.IOException;

/**
 * 描述：关闭IO公共方法
 * Created by huangyk on 2016/1/14.
 */
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e("IOUtils", e);
			}
		}
		return true;
	}
}
