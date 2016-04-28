/*
 * Copyright 2013 Storm Zhang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.accuvally.hdtui.ui;

import com.accuvally.hdtui.R;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
 

public class ClipLoading extends FrameLayout {

	private static final int MAX_PROGRESS = 100;
	private ClipDrawable mClipDrawable;
	private int mProgress = 0;
	private boolean running;
	 

	public ClipLoading(Context context) {
		this(context, null, 0);
	}

	public ClipLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		ImageView view = new ImageView(getContext());
//		view.setMinimumWidth(200);
//		view.setMinimumHeight(200);
//		view.setImageResource(R.drawable.clip_loading);
//		addView(view);
//		mClipDrawable = (ClipDrawable) view.getDrawable();
	}

	public void stop() {
		mProgress = 0;
		running = false;
	}
	
	public void setProgress(int progress){
		mClipDrawable.setLevel(mProgress);
	}
}
