package com.accuvally.hdtui.utils.exception;

import android.os.Environment;

import com.accuvally.hdtui.AccuApplication;

import java.io.File;


/**
 * 描述：File文件操作工具类
 * Created by huangyk on 2016/1/14.
 */
public class FileUtils {

    private static final String TAG = "FileUtils";
    private static String mRootPath;

    public static final String CACHE_ROOT_DIR = "/huodongxing";

    /**
     * 判断SDCard是否可用(是否挂载)
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取缓存目录
     */
    public synchronized static String getRootPath() {
        if (mRootPath == null) {
            mRootPath = getDir(CACHE_ROOT_DIR);
        }
        return mRootPath;
    }

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getSDCardPath());
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取应用的cache目录
     */
    public static String getCachePath() {
        File f = AccuApplication.getInstance().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 获取文件后缀
     */
    public static String getFileExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i).toLowerCase();
            }
        }
        return null;
    }

    /**
     * 获取文件后缀
     */
    public static String getFileExtension(String name) {
        if (name != null) {
            int i = name.lastIndexOf('.');
            if (i > 0 && i < name.length() - 1) {
                return name.substring(i).toLowerCase();
            }
        }
        return null;
    }

    public static void deleteFiles(String path) {

        // 遍历/autorepair/**/**目录下的所有文件 删除
        File filepath = new File(path);
        if (!filepath.exists())
            return;
        File[] files = filepath.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDir(file);
                    return;
                }
                file.delete();
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录
            for (int i = 0; i < children.length; i++) {
                File file = new File(dir, children[i]);
                boolean success = deleteDir(file);
                if (!success) {
                    LogUtils.e(TAG, "deleteDir file delete failed!-->path:" + file.getPath());
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 检测是否存在USB存储设备
     */
    public static boolean checkUsbDevices(String dir) {
        if (dir == null)
            return false;

        File directory = new File(dir);
        if (!directory.exists())
            return false;
        if (!directory.isDirectory())
            return false;

        // 得到所有的USB存储设备
        File[] files = directory.listFiles();
        if (files == null)
            return false;
        if (files.length == 0)
            return false;

        return true;
    }

}
