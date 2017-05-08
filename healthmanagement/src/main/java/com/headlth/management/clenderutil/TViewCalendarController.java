package com.headlth.management.clenderutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.activity.CalendarActivityCalendar;
import com.headlth.management.activity.ExerciseRecordActivity;

import com.headlth.management.entity.copy;
import com.headlth.management.entity.copy1;
import com.headlth.management.entity.copy2;
import com.headlth.management.entity.riliCallBack;

import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import org.xutils.http.RequestParams;
public class TViewCalendarController extends TViewControllerBase implements CalendarView.CallBack {
    private TViewCalendar tViewCalendar;
    private ViewPager viewPager;
    private CalendarView[] views;
    private CalendarViewBuilder builder;
    private WaitDialog waitDialog;
    private boolean isRequest = false;
    private String yearmonth;
    private String[] UNIT;
    public static String code;
    public static String userId = "";
    private static String url;
    private Activity constant;

    public TViewCalendarController(Activity activity, String code) {
        super(activity, code);
        url = Constant.BASE_URL;
        userId = code;
        this.code = code;
        this.constant = activity;
    }

    @Override
    public void initObject() {
        initDialog();
        UNIT = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        tViewCalendar = new TViewCalendar(mActivity);
        builder = new CalendarViewBuilder();
        views = builder.createMassCalendarViews(mActivity, 5, this);
        viewPager = tViewCalendar.getViewPager();
        setViewPager();
    }

    private void initDialog() {
        waitDialog = new WaitDialog(mActivity);
        waitDialog.setMessage("请稍候...");
        waitDialog.setCancleable(true);
    }

    public TViewCalendar getTViewCalendar() {
        return tViewCalendar;
    }

    private void showDialog(boolean isShow) {
        if (isShow) {
            waitDialog.showDailog();
        } else {
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }
    }

    private void setViewPager() {
        CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<>(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(498);
        viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(viewPagerAdapter));
    }

    //下面是网络请求年月中哪几天有运动数据
    private void getData(String yearmonth, int i) {
        Log.e("nianyue", yearmonth + "开始网络请求的年月");
        showDialog(true);
        rili(yearmonth, i);
        Log.e("next", 2 + "?");

    }

    static Boolean hasData = false;
    static Boolean askData = false;
    Boolean has = false;
    String temp;
    String yearmonthdate;

    @Override
    public void clickDate(CustomDate date) {
        final String yearmonth = date.year + "-" + getNum(date.month);
        if (getNumber(tViewCalendar.tv_date.getText().toString().substring(5, tViewCalendar.tv_date.getText().toString().length() - 1)).equals(yearmonth.substring(5, yearmonth.length()))) {
            int count = 0;
            askData = true;
            Log.e("nianyue", "点击日期了" + "");

            yearmonthdate = yearmonth + "-" + getNum(date.day);
       /* String userId="1081442139";*//*
        String yearmonthdate="2015-10";*/
            Log.e("rrrrr", yearmonth + "====你点了===" + yearmonthdate + "  ");
            //这里进行具体那年那月那天的网络请求
            if (DataString.isToDayDate(yearmonthdate + "") != 1) {
                //showDialog(true);
            //  rili2(yearmonthdate, 0);
                Intent i = new Intent(constant, ExerciseRecordActivity.class);
                i.putExtra("time", yearmonthdate);
                constant.startActivity(i);
                CalendarActivityCalendar.activity.finish();
            } else {
                Toast.makeText(mActivity, "暂无数据", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public void initHandler() {

    }

    @Override
    public Context getContext() {
        return constant;
    }

    public int firstt = 0;

    @Override
    public void changeDate(CustomDate date) {
        Log.e("DDD", "changeDate2");
        if (ShareUitls.getString(mActivity, "count", "null").equals("null")) {
            if (firstt == 0 || firstt == 1 || firstt == 2 || firstt == 3 || firstt == 4) {
                Log.e("DDD", "changeDate2------------------" + firstt + "firstt");
                if (firstt == 4) {
                    ShareUitls.putString(mActivity, "count", "5");
                }
                firstt++;
            }
        } else {
            int overYear;
            String preMonth;
            int preOverYear;
            logEntity = null;
            preYearMonth = null;
            backYearMonth = null;
            if (h != null) {
                h.sendEmptyMessageDelayed(10, 1);
                Log.e("DDD", "222222");
            }
            tViewCalendar.setDate(date.year + "/" + UNIT[date.month - 1] + "月");
            Log.e("ppp", "-" + (date.month - 1) + "-1");
            if ((date.month + 1) > 12) {
                overYear = date.year + 1;
            } else {
                overYear = date.year;
            }
            if ((date.month - 1) == 0) {
                preMonth = "12";
                preOverYear = date.year - 1;
                Log.e("ppp", "-preOverYear" + (date.year - 1) + "-1");
            } else {
                if (date.month - 1 == 10 || date.month - 1 == 11) {
                    preMonth = "" + (date.month - 1);
                    preOverYear = date.year;
                } else {
                    preMonth = "0" + (date.month - 1);
                    preOverYear = date.year;
                }
            }
            getData(preOverYear + "-" + preMonth, -1);
  /*  getData(-1, preOverYear + "-" + preMonth);*/
            Log.e("ppp", preOverYear + "-" + preMonth + "-1");
   /* getData(0, date.year + "-" + getNum2(date.month + 0));*/
            getData(date.year + "-" + getNum2(date.month + 0), 0);
            Log.e("ppp", date.year + "-" + getNum2(date.month + 0) + "+0");
  /*  getData(1,overYear + "-" + getNum2(date.month + 1));*/
            getData(overYear + "-" + getNum2(date.month + 1), 1);
            Log.e("ppp", overYear + "-" + getNum2(date.month + 1) + "+1");
        }


    }


    //日历
    // http://www.ssp365.com:8066/MdMobileService.ashx?do=PostCalenderRequest
    private riliCallBack rili;


    private void rili(final String monthDay, final int i) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostCalenderRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(constant, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(constant, "UID", "null"));
        params.addBodyParameter("SportTime",monthDay);
        HttpUtils.getInstance(constant).sendRequestRequestParams("", params,false, new HttpUtils.ResponseListener() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("xxxxxxxxxxxxxxxxx", i + response.toString());
                        rili = g.fromJson(response.toString(),
                                riliCallBack.class);
                        if (rili.getStatus() == 1) {
                            Message msg4 = h.obtainMessage();
                            msg4.obj = response;
                            msg4.what = i;
                            h.sendMessageDelayed(msg4, 1);
                        } else {
                         /*   Toast.makeText(mActivity,"请求失败", Toast.LENGTH_SHORT).show();*/
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        if (ShareUitls.getString(mActivity, "count", "null").equals("null")) {

                        } else {
                            if (i == -1 || i == 0 || i == 1) {
                                Toast.makeText(mActivity, "获取失败=" + i, Toast.LENGTH_SHORT).show();
                                showDialog(false);
                            }
                        }
                        return;

                    }
                }

        );
    }

    yearMonth logEntity;
    PreYearMonth preYearMonth;
    BackYearMonth backYearMonth;
    Gson g = new Gson();
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                if (logEntity != null && preYearMonth != null && backYearMonth != null) {
                    if (waitDialog.isShowing()) {
                        showDialog(false);
                        return;
                    }
                    Log.e("DDD", "33333");
                    showDialog(false);
                } else {
                    h.sendEmptyMessageDelayed(10, 1);
                }
            }
            if (msg.what == -1) {
                preYearMonth = g.fromJson(msg.obj.toString(), PreYearMonth.class);
                if (preYearMonth.getStatus() == 1) {
                    copy.getInstance().SSSportDay = preYearMonth.getCalenderList();
                    Log.e("llll", copy.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "-1" + "=====copy");
                    askData = false;
                  /*  showDialog(false);*/
                    return;
                } else {
                  /*  Toast.makeText(mActivity,"请求数据失败",Toast.LENGTH_SHORT).show();*/
                }
               /* showDialog(false);*/
            }
            if (msg.what == 0) {
                logEntity = g.fromJson(msg.obj.toString(), yearMonth.class);
        /*        Log.e("llll", logEntity.getCalenderList().toString() + "滑动新请求的日历数据获取" + "0");*/
                if (logEntity.getStatus() == 1) {
                    copy1.getInstance().SSSportDay = logEntity.getCalenderList();
                    Log.e("llll", copy1.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "0" + "=====copy1");
                    Log.e("ooo", "请求存copy1");
                    askData = false;
               /*     showDialog(false);*/
                    return;
                } else {
                   /* Toast.makeText(mActivity,"请求数据失败",Toast.LENGTH_SHORT).show();*/
                }
              /*  showDialog(false);*/
            }
            if (msg.what == 1) {
                backYearMonth = g.fromJson(msg.obj.toString(), BackYearMonth.class);
                if (backYearMonth.getStatus() == 1) {
                    copy2.getInstance().SSSportDay = backYearMonth.getCalenderList();
                    Log.e("llll", copy2.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "1" + "=====copy2");
                    Log.e("ooo", "请求存copy2");
                    Log.e("llll", backYearMonth.getCalenderList().toString() + "滑动新请求的日历数据获取" + "1");
                    askData = false;
                  /*  showDialog(false);*/
                    return;
                } else {
                   /* Toast.makeText(mActivity,"请求数据失败",Toast.LENGTH_SHORT).show();*/
                }
              /*  showDialog(false);*/
            }
         /*   showDialog(false);*/
        }

    };

    private String getNum2(int month) {
        if (month < 10) {
            return "0" + (month);
        }
        if (month > 12) {

            return "0" + (month - 12);
        }
        if (month == 0) {
            return "0" + 2;
        }
        return month + "";
    }

    private String getNum(int month) {
        if (month < 10) {
            return "0" + month;
        }
        return month + "";
    }

    private String getNumber(String chineseNumber) {
        String temp = "";
        switch (chineseNumber) {
            case "十二":
                temp = "12";
                break;
            case "十一":
                temp = "11";
                break;
            case "十":
                temp = "10";
                break;
            case "九":
                temp = "09";
                break;
            case "八":
                temp = "08";
                break;
            case "七":
                temp = "07";
                break;
            case "六":
                temp = "06";
                break;
            case "五":
                temp = "05";
                break;
            case "四":
                temp = "04";
                break;
            case "三":
                temp = "03";
                break;
            case "二":
                temp = "02";
                break;
            case "一":
                temp = "01";
                break;
        }
        return temp;
    }
}
