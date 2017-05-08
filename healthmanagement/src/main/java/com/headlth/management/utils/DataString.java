package com.headlth.management.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataString {
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
      /*  if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }*/
        return mWay;
    }

    public static int isToDayDate(String goalDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Log.i("jjjjjjjjjjjjjjjj", goalDate + "  " + today + "  " + today.substring(0, 4) + " " + goalDate.substring(0, 4) + " " + goalDate.length() + "  " + today.length());

        int year = Integer.parseInt(today.substring(0, 4)) - Integer.parseInt(goalDate.substring(0, 4));
        int month = Integer.parseInt(today.substring(5, 7)) - Integer.parseInt(goalDate.substring(5, 7));
        int day = Integer.parseInt(today.substring(8, 10)) - Integer.parseInt(goalDate.substring(8, 10));
        if (year < 0) {
            return 1;
        } else if (year > 0) {
            return -1;
        } else {
            if (month < 0) {
                return 1;
            } else if (month > 0) {
                return -1;
            } else {
                if (day < 0) {
                    return 1;
                } else if (day > 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        Log.i("WWWWW",""+m.replaceAll("-").trim());
        return m.replaceAll("-").trim();
    }
    //static  int  interval[6];
    public static String showTime(String time)  {
        Log.i("WWWWW",""+time);
        ;
       /* int  interval[]=new int [6];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String today = format.format(new Date());
        String old[] = filterUnNumber(time).split("-");
        String now[] = today.split("-");
        for (int i = 0; i < old.length; i++) {
            interval[i]=Integer.parseInt(now[i])-Integer.parseInt(old[i]);
            Log.i("WWWWW",""+interval[i]);
        }
        if (interval[0] != 0) {
            return time;
        } else {
            if (interval[1] != 0) {
                return time;
            } else {
                if (interval[2] != 0) {
                    if (interval[2] == 1) {
                        return "昨天";
                    } else {
                        return time;
                    }
                } else {
                    if (interval[3] >= 1) {
                        if(interval[4]>=0) {
                            return interval[3] + "小时前";
                        }else {
                            return interval[4] + "分钟前";
                        }

                    } else if (interval[3] < 0) {
                        return time;
                    } else {
                        if (interval[4] >= 1) {
                            return interval[4] + "分钟前";
                        } else if (interval[4] < 0) {
                            return time;
                        } else {
                            return "刚刚";
                        }

                    }
                }
            }
        }*/

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(format(date));

        return  format(date);

    }

    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long delta = new Date().getTime() - date.getTime();
        long deltaa = new Date().getTime()/1000 - date.getTime()/1000;
       // Log.i("DDDDDDDDDD",format.format(date));
        if(deltaa<0){
            Log.i("DDDDDDDDDD",delta+"  "+deltaa);
            return format.format(date);
        }else {
            if (delta < 1L * ONE_MINUTE) {
              //  Log.i("DDDDDDDDDD",delta+"");
               // long seconds = toSeconds(delta);
                return /*(seconds <= 0 ? 1 : seconds) + */ONE_SECOND_AGO;
            }
            if (delta < 45L * ONE_MINUTE) {
                long minutes = toMinutes(delta);
                return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
            }
            if (delta < 24L * ONE_HOUR) {
                long hours = toHours(delta);
                return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
            }
            if (delta < 48L * ONE_HOUR) {
                return "昨天";
            }
            if (delta < 30L * ONE_DAY) {
                ///*(days <= 0 ? 1 : days) + ONE_DAY_AGO*/
                long days = toDays(delta);
                // format.format(date)
                return format.format(date);
            }
            if (delta < 12L * 4L * ONE_WEEK) {
                long months = toMonths(delta);
                //(months <= 0 ? 1 : months) + ONE_MONTH_AGO
                //format.format(date)format.format(date)
                return format.format(date);
            } else {
                long years = toYears(delta);
                //(years <= 0 ? 1 : years) + ONE_YEAR_AGO
                //format.format(date)
                return format.format(date);
            }
        }
    }



    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "刚刚";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}