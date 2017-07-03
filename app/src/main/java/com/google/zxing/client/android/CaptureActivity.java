package com.google.zxing.client.android;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;

import com.accuvally.hdtui.BaseActivity;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
//import com.zxing.camera.CameraManager;
//import com.zxing.view.ViewfinderView;
//二维码现在用的是：zxing-android-portrait-master

abstract class CaptureActivity extends BaseActivity{


    public abstract Handler getHandler();

    public abstract CameraManager getCameraManager();
    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult
     *            The contents of the barcode.
     * @param scaleFactor
     *            amount by which thumbnail was scaled
     * @param barcode
     *            A greyscale bitmap of the camera data which was decoded.
     */
    public abstract void handleDecode(final Result rawResult, Bitmap barcode,
                             float scaleFactor);


    public abstract ViewfinderView getViewfinderView();


    public abstract void drawViewfinder();


    protected void setOrientation() {
        // 1：横屏， 2：竖屏
        if (QRCodeSConfig.SCREEN_ORIENTATION_SENSOR == false) {
            if (QRCodeSConfig.SCREEN_ORENTATION == 1) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                QRCodeSConfig.SCREEN_ORENTATION = 2;
            } else {
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                QRCodeSConfig.SCREEN_ORENTATION = 1;
            }
        }
    }

}