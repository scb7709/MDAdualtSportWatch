package com.headlth.management.clenderutil;

import android.content.Context;
import android.util.Log;

import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CustomDate implements Serializable {


    private static final long serialVersionUID = 1L;
    public int year;
    public int month;
    public int day;
    public int week;

    public CustomDate(int year, int month, int day) {
        if (month > 12) {
            month = 1;
            year++;
        } else if (month < 1) {
            month = 12;
            year--;
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CustomDate(Context context) {
        String str = ShareUitls.getString(context, "CLICKDADE", "");
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getCurrentMonthDay();
        Log.e("dangqian", year + "||" + month + "||" + day + "");

    }

    public CustomDate(Calendar calendar) {

        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);

        String s=getdate(calendar);
        /*this.year = Integer.parseInt(s.substring(0,4));
        this.month = Integer.parseInt(s.substring(5,7));
        this.day = Integer.parseInt(s.substring(8,10));*/

        MyToash.Log(s+"  "+year + "||" + month + "||" + day + "");

    }

    public String getdate(Calendar calendar) {
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        Date dat = calendar.getTime();
        return dformat.format(dat);
    }


    public static CustomDate modifiDayForObject(CustomDate date, int day) {
        CustomDate modifiDate = new CustomDate(date.year, date.month, day);
        return modifiDate;
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}
