package com.accuvally.hdtui.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.accuvally.hdtui.R;


public class ClipView extends View {

	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 图片
	 */
	private Bitmap mBitmap;
	/**
	 * 画布
	 */
	private Canvas mCanvas;
	/**
	 * 蒙版
	 */
	private Bitmap bitmap;
	/**
	 * 起点坐标
	 */
	private int startX, startY;
	/**
	 * 移动距离
	 */
	private int distanceX, distanceY;
	/**
	 * 图片坐标
	 */
	private int widthX, heightY;
	int x = 0, y = 0;

	public ClipView(Context context) {
		super(context);
		init();
	}

	public ClipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * 缩放图片
	 * 
	 * @param bgimage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	private Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 拿到图片首先进行缩放
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = zoomImage(bitmap, 600, 800);
		startX = -(600 / 2);
		startY = -(800 / 2);
		widthX = startX;
		heightY = startY;
		postInvalidate();
	}

	private void init() {
		// 创建空白画布
		bitmap = Bitmap.createBitmap(600, 800, Config.ARGB_8888);
		mCanvas = new Canvas(bitmap);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);
	}

	private void restartCanvas() {
		// 清空上一次的绘图状态
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mCanvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		mCanvas.drawColor(getResources().getColor(R.color.transparent));
	}

	/**
	 * 移动x位置
	 */
	private void getWidthX() {
		widthX = startX - distanceX;
		if (widthX > -200) {
			widthX = -200;
			distanceX = -100;
		} else if (widthX < -400) {
			widthX = -400;
			distanceX = 100;
		}
	}

	/**
	 * 移动y位置
	 */
	private void getHeightY() {
		heightY = startY - distanceY;
		if (heightY > -200) {
			heightY = -200;
			distanceY = -100;
		} else if (heightY < -600) {
			heightY = -600;
			distanceY = 100;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) event.getX();
			y = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			distanceX = x - (int) (event.getX());
			distanceY = y - (int) (event.getY());
			getWidthX();
			getHeightY();
			break;
		case MotionEvent.ACTION_UP:
			startX = widthX;
			startY = heightY;
			break;
		default:
			break;
		}
		postInvalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.translate(getWidth() / 2, getHeight() / 2);
		if (mBitmap != null) {
			restartCanvas();
			canvas.drawBitmap(mBitmap, widthX, heightY, null);
			mCanvas.drawCircle(-widthX, -heightY, 200, paint);
			canvas.drawBitmap(bitmap, widthX, heightY, null);
		}
	}
}
