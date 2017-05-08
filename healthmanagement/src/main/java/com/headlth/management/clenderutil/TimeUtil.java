package com.headlth.management.clenderutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static final SimpleDateFormat DATE_FORMAT_ISO8601 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_ISO8602 = new SimpleDateFormat(
            "yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_ISO8603 = new SimpleDateFormat(
            "HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_ISO8604 = new SimpleDateFormat(
            "yyMMdd");
    public static final SimpleDateFormat DATE_FORMAT_ISO8605 = new SimpleDateFormat(
            "yyyy年MM月dd日  E");
    public static final SimpleDateFormat DATE_FORMAT_ISO8606 = new SimpleDateFormat(
            "MM-dd HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_ISO8607 = new SimpleDateFormat(
            "yyyy年MM月");
    public static final SimpleDateFormat DATE_FORMAT_ISO8608 = new SimpleDateFormat(
            "MM月dd日");
    public static final SimpleDateFormat DATE_FORMAT_ISO8609 = new SimpleDateFormat(
            "MM月");
    public static final SimpleDateFormat DATE_FORMAT_ISO8610 = new SimpleDateFormat(
            "MM月dd日 HH:mm");

    /**
     * 判断时间是否是同一天
     */
    public static boolean isToday(long time1, long time2) {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        String strTime1 = DATE_FORMAT_ISO8602.format(date1);
        String strTime2 = DATE_FORMAT_ISO8602.format(date2);
        return strTime1.equals(strTime2);
    }

    /**
     * 判断时间是否是同一个月
     */
    public static boolean isMonth(long time1, long time2) {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        String strTime1 = DATE_FORMAT_ISO8607.format(date1);
        String strTime2 = DATE_FORMAT_ISO8607.format(date2);
        return strTime1.equals(strTime2);
    }

    /**
     * 获取系统当前时间
     */
    public static long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTime().getTime();
    }

    /**
     * 获得比日期d提前 day天的日期
     *
     * @param d 基准日期
     * @return 调准以后的日期
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获得比日期d延迟 day天的日期
     *
     * @param d 基准日期
     * @return 调准以后的日期
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static String formatChange(String source, SimpleDateFormat format1,
                                      SimpleDateFormat format2) {
        Date d;
        try {
            d = format1.parse(source);
            return format2.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期是月份
     *
     * @param time   时间串
     * @param format 格式时间串的方式
     */
    public static String getMonth(String time, SimpleDateFormat format) {
        try {
            Date date = TimeUtil.DATE_FORMAT_ISO8601.parse(time);
            String create_time = TimeUtil.DATE_FORMAT_ISO8609.format(date);
            Long timeNow = TimeUtil.getCurrentTime();
            Long timeOil = date.getTime();
            if (TimeUtil.isMonth(timeNow, timeOil)) {
                create_time = "本月";
            } else {
                create_time = create_time.startsWith("0") ? create_time.substring(1) : create_time;
            }
            return create_time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算时间和当前时间的间隔（秒）
     *
     * @param strTime 时间串
     */
    public static long getSecond(String strTime) {
        try {
            Date date = TimeUtil.DATE_FORMAT_ISO8601.parse(strTime);
            Long timeNow = TimeUtil.getCurrentTime();
            Long timeDate = date.getTime();
            return (timeNow - timeDate) / (1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间（秒）转换成  分：秒
     */
    public static String parseTime(long strTime) {
        StringBuffer buffer = new StringBuffer();
        long fen = strTime / 60;
        long miao = strTime % (60);

        String fenDaojishi = timeDaoJiShi(fen);
        String miaoDaojishi = timeDaoJiShi(miao);

        buffer.append(fenDaojishi).append(":").append(miaoDaojishi);
        return buffer.toString();
    }

    /**
     * 小于10前加0
     */
    private static String timeDaoJiShi(long time) {
        return (time < 10) ? ("0" + time) : ("" + time);
    }
}
