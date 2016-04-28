package com.accuvally.hdtui.fragment.session;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;

/**
 * 圈子列表
 * 
 * @author Semmer Wang
 * 
 */
public class CircleFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_circle, container, false);
		Log.i("info", "CircleFragment:onCreateView()");
		return view;
	}
}
