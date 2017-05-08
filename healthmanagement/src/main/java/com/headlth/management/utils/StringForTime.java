package com.headlth.management.utils;

import android.util.Log;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by abc on 2016/7/14.
 */
public class StringForTime {
    public static String stringForTime(long timeMs) {
        Log.i("RRRRRRR",timeMs+"");
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int  totalSeconds = (int)timeMs;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
