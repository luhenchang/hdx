package com.google.zxing.client.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.activity.web.WebActivity;
import com.accuvally.hdtui.activity.home.AccuvallyDetailsActivity;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.model.QrCodeInfo;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.Trace;
import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

//import com.zxing.camera.CameraManager;
//import com.zxing.view.ViewfinderView;


public class CodeLoginActivity extends CaptureActivity implements Callback {

    public static final String TAG="CodeLoginActivity";

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private ImageView flashImageButton;


    public CameraManager getCameraManager() {
        return cameraManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.activity_qrscan_login);

        hasSurface = false;
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

//		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        ZXingApplication
                .print_i("CodeLoginActivity", "onCreate-----------finish");

        // 设置二维码扫描屏幕方向
        setOrientation();

        setTitle("扫码");


        flashImageButton = (ImageView) findViewById(R.id.lamp);
        flashImageButton.setOnClickListener(new View.OnClickListener() {
            //监听闪光灯按钮
            @Override
            public void onClick(View v) {
                cameraManager.flashHandler(flashImageButton);
            }
        });

    }


    private boolean isUrl(String str){
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return  pattern.matcher(str).matches();
    }
    AlertDialog.Builder dialog;
    private void resolveQrcode(String data){
        HttpCilents httpCilents =new HttpCilents(this);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("data", data));
        httpCilents.get(httpCilents.printURL(Url.resolve_qrcode, list), new HttpCilents.WebServiceCallBack() {
            public void callBack(int code, Object result) {
                if (code == Config.RESULT_CODE_SUCCESS) {
                    BaseResponse response = JSON.parseObject(result.toString(), BaseResponse.class);
                    if (response.isSuccess()) {
                        QrCodeInfo qrCodeInfo = JSON.parseObject(response.result, QrCodeInfo.class);
                        Trace.e(TAG, qrCodeInfo.toString());

                        switch (qrCodeInfo.type){
                            case 1://原始值
                                if(isUrl(qrCodeInfo.value)){
                                    CodeLoginActivity.this.startActivityForResult(new Intent(mContext, WebActivity.class).putExtra("loadingUrl",
                                            qrCodeInfo.value).putExtra("injectJs", ""), 1);
                                }else {

                                    dialog = new AlertDialog.Builder(mContext);
                                    dialog.setTitle("扫码结果:");
                                    dialog.setMessage(qrCodeInfo.value);
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            restartPreviewAfterDelay(0);
                                        }
                                    });
                                    dialog.show();
                                }
                                break;
                            case 2://活动
                                Intent intent = new Intent(mContext,
                                        AccuvallyDetailsActivity.class).putExtra("isHuodong", 0).
                                        putExtra("id", qrCodeInfo.value);
                                CodeLoginActivity.this.startActivityForResult(intent, 2);
                                break;
                        }
                    }else {
                        application.showMsg(response.getMsg());
                        restartPreviewAfterDelay(0);
                    }
                } else {//code != Config.RESULT_CODE_SUCCESS
                    application.showMsg("网络连接断开，请检查网络");
                    restartPreviewAfterDelay(0);
                }
            }
        });


    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);


        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        // inactivityTimer.onResume();
        decodeFormats = null;
        characterSet = null;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        ZXingApplication
                .print_i("CodeLoginActivity", "onResume-----------finish");



    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        // inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        ZXingApplication.print_i("CodeLoginActivity", "onPause-----------finish");
    }

    @Override
    protected void onDestroy() {
        // inactivityTimer.shutdown();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Trace.e(TAG,"onActivityResult"+requestCode);
        restartPreviewAfterDelay(0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2,
                               int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (holder == null) {
            ZXingApplication.print_i("CodeLoginActivity",
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        hasSurface = false;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            ZXingApplication
                    .print_i("CodeLoginActivity",
                            "initCamera() while already open -- late SurfaceView callback?");
            return;
        }

        try {
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    decodeHints, characterSet, cameraManager);
        }
        ZXingApplication.print_i("CodeLoginActivity",
                "initCamera-----------finish");
    }

    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        }
    }

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
    public void handleDecode(final Result rawResult, Bitmap barcode,
                             float scaleFactor) {
        // inactivityTimer.onActivity();
        Log.d("timer", "timeCountDown restart");
        String resultString = rawResult.getText();
        Log.d("handleDecode", "resultString is " + resultString);

        beepManager.playBeepSoundAndVibrate();

        handleDecodeInternally(rawResult, barcode);

    }


    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }


    // Put up our own UI for how to handle the decoded contents.
    private void handleDecodeInternally(Result rawResult,Bitmap barcode) {
//        viewfinderView.setVisibility(View.GONE);    20170518
        if (barcode == null) {
            // barcode = ((BitmapDrawable)
            // getResources().getDrawable(R.drawable.unknown_barcode)).getBitmap();
        }

        String resultText = rawResult.getText();

        Trace.d(TAG, "rawResult.getText():" + rawResult.getText());
        Trace.d(TAG,"rawResult.toString():"+rawResult.toString());
        resolveQrcode(resultText);

    }


}
