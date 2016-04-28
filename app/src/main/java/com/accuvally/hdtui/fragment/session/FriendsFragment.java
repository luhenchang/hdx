package com.accuvally.hdtui.fragment.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.accuvally.hdtui.BaseFragment;
import com.accuvally.hdtui.R;


/**
 * 好友列表
 * @author Semmer Wang
 *
 */
public class FriendsFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends, container, false);
		Log.i("info", "FriendFragment:onCreateView()");
		initView(view);
		return view;
	}

	public void initView(View view) {
		ListView listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			list.add(map);
		}
		SimpleAdapter mAdapter = new SimpleAdapter(mContext, list, R.layout.listitem_city, new String[] {}, new int[] {});
		listView.setAdapter(mAdapter);
	}

}
