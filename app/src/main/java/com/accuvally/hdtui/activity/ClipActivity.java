package com.accuvally.hdtui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.ui.ClipImageLayout;
import com.accuvally.hdtui.utils.ImageTools;

public class ClipActivity extends Activity {

	private ClipImageLayout clipView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);

		initView();
		String path = getIntent().getStringExtra("imagePath");
		Bitmap bitmap = ImageTools.convertToBitmap(path, 600, 600);
		if (bitmap == null) {
			Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
			return;
		}
		clipView.setBitmap(bitmap);
	}

	private void initView() {
		clipView = (ClipImageLayout) findViewById(R.id.clip);
	}
}
