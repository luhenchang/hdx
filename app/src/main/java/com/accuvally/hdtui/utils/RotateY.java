package com.accuvally.hdtui.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RotateY extends Animation {
	private float mFromDegrees = 0;// 开始角度
	private float mToDegrees = 90;// 结束角度
	
	private float mCenterX;
	private float mCenterY;
	
	private Camera camera = new Camera();

	public RotateY(float mCenterX, float mCenterY) {
		this.mCenterX = mCenterX;
		this.mCenterY = mCenterY;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float degrees = mToDegrees * interpolatedTime;
		final Matrix matrix = t.getMatrix();//取得当前矩阵
		camera.save();
		camera.rotateY(degrees);//翻转
		camera.getMatrix(matrix);// 取得变换后的矩阵
		camera.restore();
		
		matrix.preTranslate(0, -mCenterY);  
		matrix.postTranslate(0, mCenterY);
	}

}
