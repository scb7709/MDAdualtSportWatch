package com.headlth.management.utils;

import android.util.Log;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by abc on 2016/7/14.
 */
public class StringForTime {
    public static String stringForTime(int time) {
        Log.i("RRRRRRR",time+"");

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;

    }

    public static String stringForTime2(int time) {
        Log.i("RRRRRRR",time+"");

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00'00''";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "'" + unitFormat(second)+"''";
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "'" + unitFormat(minute) + "'" + unitFormat(second)+"''";
            }
        }
        return timeStr;

    }
    public static String stringForTime3(int time) {
        Log.i("RRRRRRR",time+"");

        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00'00''";
        else {
            minute = time / 60;
            second = time % 60;
            timeStr = unitFormat(minute) + "'" + unitFormat(second)+"''";

        }
        return timeStr;

    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
