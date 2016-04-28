package com.accuvally.hdtui.ui.calender;


import android.content.Context;

import com.accuvally.hdtui.R;
import com.accuvally.hdtui.ui.calender.CalendarView.CallBack;

/**
 * CalendarView的辅助类
 * @author huang
 *
 */
public class CalendarViewBuilder {
		private CalendarView[] calendarViews;
		private int  style;
		
		public void setStyle(int style) {
			this.style = style;
		}

		public int getStyle() {
			return style;
		}

		/**
		 * 生产多个CalendarView
		 * @param context
		 * @param count
		 * @param style
		 * @param callBack
		 * @return
		 */
		public  CalendarView[] createMassCalendarViewsNoMark(Context context,int count,int style,CallBack callBack){
			setStyle(style);
			calendarViews = new CalendarView[count];
			for(int i = 0; i < count;i++){
				calendarViews[i] = new CalendarView(context, style,callBack,0);
			}
			return calendarViews;
		}
		
		
		/**
		 * 生产多个CalendarView
		 * @param context
		 * @param count
		 * @param style
		 * @param callBack
		 * @return
		 */
		public  CalendarView[] createMassCalendarViews(Context context,int count,int style,CallBack callBack){
			setStyle(style);
			calendarViews = new CalendarView[count];
			for(int i = 0; i < count;i++){
				calendarViews[i] = new CalendarView(context, style,callBack,R.drawable.huodongbiaoji);
			}
			return calendarViews;
		}
		
		 
		/**
		 * 切换CandlendarView的样式
		 * @param style
		 */
		public void swtichCalendarViewsStyle(int style){
			if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				if(calendarViews[i].getStyle()!=style)
					calendarViews[i].switchStyle(style);
			}
		}
		
		
//		public void swtichCalendarViewsStyle(Context ctx,int style,CustomDate date){
//			if(calendarViews != null)
//			for(int i = 0 ;i < calendarViews.length;i++){
//				CustomDate d =DateUtil.getNextSunday(ctx.getResources(),date);
//				calendarViews[i].switchStyle(style,d);
//			}
//		}
		/**
		 * CandlendarView回到当前日期
		 */
		
		public void backTodayCalendarViews(){
			if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].backToday();
			}
		}
		
		public void refresh(){
			if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].refreshIcon();
			}
		}
		
}
