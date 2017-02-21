package com.accuvally.hdtui.fragment.detailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;

/**
 * Created by Andy Liu on 2017/2/7.
 */
public class DetailRight extends BaseFragment {

    public WebView detail_right_webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_right, container, false);

        detail_right_webView= (WebView) rootView.findViewById(R.id.detail_right_webView);
        return rootView;
    }
}
