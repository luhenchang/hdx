package com.accuvally.hdtui.utils.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.accuvally.hdtui.utils.Trace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：UncaughtException处理类,当程序发生Uncaught异常的时候,
 * 有该类来接管程序,并记录发送错误报告
 * Created by huangyk on 2015/11/17.
 * /sdcard/huodongxing/logs
 * /storage/emulated/0/huodongxing/logs/crash_2017-02-08.log
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "ExceptionHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static ExceptionHandler INSTANCE = new ExceptionHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    //用于格式化时间,作为日志输出
    private DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private ExceptionHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static ExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        //异常退出的时候关闭跑步机

        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper mainLooper = Looper.getMainLooper();
                mainLooper.prepare();
//                AccuApplication.getInstance().showMsg("程序异常，将退出");
                mainLooper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private void saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        sb.append("\r\n").append("\r\n");
        sb.append("********" + formatTime.format(new Date()) + "**********");
        sb.append("\r\n");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        try {
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(printWriter);
        }
        String result = writer.toString();
        sb.append(result);
        FileOutputStream fos = null;
        try {
            String time = formatDate.format(new Date());
            String fileName = "crash_" + time + ".log";
            File dir = new File(FileUtils.getRootPath() + "/logs");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(dir.getPath() + "/" + fileName, true);
            Trace.e("日志文件路径",dir.getPath() + "/" + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing crash file...");
            LogUtils.e(TAG, e);
        } finally {
            IOUtils.close(fos);
        }
    }
}
