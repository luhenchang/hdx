package com.google.zxing.client.android;

/**
 * 二维码扫描使用配置文件
 *
 * Created by 123 on 2015/11/5.
 */
public class QRCodeSConfig {

    public final static String PREF_NAME="data";


    /*
     * true: 由传感器决定屏幕方向，动态改变 SCREEN_ORENTATION 的值
     * false: 用户自定义屏幕方向
     * 注意：Manifest文件中CaptureActivity不需要配置screenOrientation属性
     */
    public final static boolean SCREEN_ORIENTATION_SENSOR = false;

    /*
     * 设置二维码扫描界面的屏幕方向：1：横屏， 2：竖屏
     * SCREEN_ORIENTATION_SENSOR为true的情况：值由传感器决定
     * SCREEN_ORIENTATION_SENSOR为false的情况：值由用户设定
     * 注意：Manifest文件中CaptureActivity的screenOrientation属性需要配置
     */
    public static int SCREEN_ORENTATION = 2;

    /*
     * 背景：Zxing默认是横屏模式，如果改成竖屏模式需要将摄像头旋转90度
     * 设置二维码扫描界面 竖屏模式下 摄像头旋转的角度
     * 注意：普通android设备只需要旋转90度，门禁机比较奇葩，摄像头默认是倒着的，需要旋转180度
     */
    public static int DISPLAY_ORIENTATION = 90;

    /*
     * 设置使用的摄像头：0：后置摄像头 1：前置摄像头
     *
     */
    public final static int CAMERA_USE = 0;

//    CameraManager设置扫描屏幕的的大小
//Point point= UIUtils.getScreenMetrics(context);
//    requestedFramingRectWidth = (int) (point.x*0.9);//
//    requestedFramingRectHeight = (int) (point.y*0.5);//



//    ViewfinderView:
    // 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
//    Rect frame = cameraManager.getFramingRect();
//    Rect previewFrame = cameraManager.getFramingRectInPreview();
}
