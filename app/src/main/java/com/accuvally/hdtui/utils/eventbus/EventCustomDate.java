package com.accuvally.hdtui.utils.eventbus;

import com.accuvally.hdtui.activity.home.CalenderTypeActivity;
import com.accuvally.hdtui.ui.calender.CustomDate;

public class EventCustomDate {

	public Class clazz;
	public CustomDate customDate;

	
	/***
	 * {@link CalenderTypeActivity#}
	 * {@link com.accuvally.hdtui.fragment.HomeTypeFragment #onEventMainThread(EventCustomDate)}}
	 * @param clazz
	 * @param customDate
	 */
	public EventCustomDate(Class clazz, CustomDate customDate) {
		this.clazz = clazz;
		this.customDate = customDate;
	}

}
