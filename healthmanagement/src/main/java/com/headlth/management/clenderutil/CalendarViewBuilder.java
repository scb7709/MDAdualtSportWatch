package com.headlth.management.clenderutil;

import android.content.Context;

public class CalendarViewBuilder {
	private CalendarView[] calendarViews;
	/**
	 * 生产多个CalendarView
	 * @param context
	 * @param count
	 * @param callBack
	 * @return
	 */
	public  CalendarView[] createMassCalendarViews(Context context,int count,CalendarView.CallBack callBack){
		calendarViews = new CalendarView[count];
		for(int i = 0; i < count;i++){
			calendarViews[i] = new CalendarView(context,callBack);
		}
		return calendarViews;
	}

	/**
	 * CandlendarView回到当前日期
	 */

	public void backTodayCalendarViews(){
		if(calendarViews != null)
			for(int i = 0 ;i < calendarViews.length;i++){
				calendarViews[i].backToday();
			}
	}
}
