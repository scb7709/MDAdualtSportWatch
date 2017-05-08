package com.headlth.management.clenderutil;

import android.content.Context;
import android.util.Log;

import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;

import java.io.Serializable;

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
    /*    //获取当前日期
        if (str != null && !str.equals("")) {
            this.year = Integer.parseInt(str.substring(0,4));
            this.month = Integer.parseInt(str.substring(5, 7));
            this.day = Integer.parseInt(str.substring(8, 10));

        } else {
            this.year = DateUtil.getYear();
            this.month = DateUtil.getMonth();
            this.day = DateUtil.getCurrentMonthDay();
        }*/
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getCurrentMonthDay();
        Log.e("dangqian",year + "||" + month + "||" + day + "");

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
