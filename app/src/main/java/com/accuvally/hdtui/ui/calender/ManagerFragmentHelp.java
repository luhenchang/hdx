package com.accuvally.hdtui.ui.calender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;

import com.accuvally.hdtui.fragment.manager.CollectionFragment;
import com.accuvally.hdtui.fragment.manager.EnrollFragment;
import com.accuvally.hdtui.fragment.manager.UnfinishedFragment;
import com.accuvally.hdtui.model.SelInfo;
import com.accuvally.hdtui.model.UnfinishedTicket;
import com.accuvally.hdtui.utils.eventbus.RefreshCalendar;

import de.greenrobot.event.EventBus;

public class ManagerFragmentHelp {

	private static HashMap<String, Boolean> collectionHashMap;
	private static HashMap<String, Boolean> enrollHashMap;
	private static HashMap<String, Boolean> unfinishedHashMap;
	private final static int clazzCollectionFragment = 0;// CollectionFragment.class;
	private final static int clazzEnrollFragment = 1;// EnrollFragment.class;
	private final static int clazzUnfinishedFragment = 2;// UnfinishedFragment.class;

	private static int currentFragment = clazzCollectionFragment;

	public static void init() {
		if (collectionHashMap == null) {
			collectionHashMap = new HashMap<String, Boolean>();
		}
		if (enrollHashMap == null) {
			enrollHashMap = new HashMap<String, Boolean>();
		}
		if (unfinishedHashMap == null) {
			unfinishedHashMap = new HashMap<String, Boolean>();
		}

		collectionHashMap.clear();
		enrollHashMap.clear();
		unfinishedHashMap.clear();
		 
	}

	public static void updateFragment(int index) {
		currentFragment = index;
	}

	public static boolean get(String date) {
		
		switch (currentFragment) {
		case clazzCollectionFragment:
			if (collectionHashMap!=null &&collectionHashMap.size() > 0){
				Boolean b =collectionHashMap.get(date);
				if(b!=null){
					return b;
				}
			}
		case clazzEnrollFragment:
			if (enrollHashMap!=null &&enrollHashMap.size() > 0){
				Boolean b =enrollHashMap.get(date);
				if(b!=null){
					return b;
				}
			}
		case clazzUnfinishedFragment:
			if (unfinishedHashMap!=null &&unfinishedHashMap.size() > 0){
				Boolean b =unfinishedHashMap.get(date);
				if(b!=null){
					return b;
				}
			}
		}
		return false;
	}

	public static void putAll(Class clazz, List list) {
		if(list.size()>0){
			if (clazz == CollectionFragment.class) {
				collection(list);
			} else if (clazz == EnrollFragment.class) {
				enroll(list);
			} else if (clazz == UnfinishedFragment.class) {
				unfiished(list);
			}
			EventBus.getDefault().post(new RefreshCalendar());
		}
	}

	private static void unfiished(List list) {
		final List<UnfinishedTicket> selInfos = list;
		final int len = selInfos.size();
		for (int i = 0; i < len; i++) {
			put(selInfos.get(i).getStartutc(), selInfos.get(i).getEndutc(), unfinishedHashMap);
		}
	}

	private static void enroll(List list) {
		final List<SelInfo> selInfos = list;
		final int len = selInfos.size();
		for (int i = 0; i < len; i++) {
			put(selInfos.get(i).getStartutc(), selInfos.get(i).getEndutc(), enrollHashMap);
		}
	}

	private static void collection(List list) {
		final List<SelInfo> selInfos = list;
		final int len = selInfos.size();
		for (int i = 0; i < len; i++) {
			put(selInfos.get(i).getStartutc(), selInfos.get(i).getEndutc(), collectionHashMap);
		}
	}

	private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static void put(String startTime, String endTime, HashMap<String, Boolean> list) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(sFormat.parse(startTime));
			c2.setTime(sFormat.parse(endTime));
			while (c1.getTimeInMillis() <= c2.getTimeInMillis()) {
				list.put(sFormat.format(c1.getTime()), true);
				c1.add(Calendar.DAY_OF_YEAR, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(HashMap<String, Boolean> map){
		Log.d("q",currentFragment+"  ");
		for(Entry<String, Boolean> enty:map.entrySet()){
			Log.d("q",enty.getKey()+" : "+enty.getValue());
		}
	}
}
