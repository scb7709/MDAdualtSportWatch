package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.ExerciseRecordActivity;
import com.headlth.management.clenderutil.CalendarView;
import com.headlth.management.clenderutil.CustomDate;
import com.headlth.management.entity.riliCallBack;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2017/9/24.
 */

@ContentView(R.layout.fragment_calendarview)
public class CalendarViewFragment extends BaseFragment {
    @ViewInject(R.id.fragment_calendarview_layout)
    private LinearLayout fragment_calendarview_layout;
    private String date;
    private Calendar calendar;
    private int DayCount;
    private String UID, ResultJWT;
    private CalendarViewClickListener calendarViewClickListener;
    private Activity activity;
    private Gson gson;
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;

    @SuppressLint("ValidFragment")
    public CalendarViewFragment(String date) {
        this.date = date;
    }

    @SuppressLint("ValidFragment")
    public CalendarViewFragment(Calendar calendar) {
        this.calendar = calendar;

    }

    private List<Integer> list;

    public CalendarViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity);
        waitDialog.setMessage("正在获取请稍后...");

        gson = new Gson();
        list = new ArrayList<>();
        UID = ShareUitls.getString(activity, "UID", "null");
        ResultJWT = ShareUitls.getString(activity, "ResultJWT", "null");
        calendarViewClickListener = new CalendarViewClickListener();
        DayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < DayCount; i++) {
            list.add(0);
        }
        fragment_calendarview_layout.removeAllViews();
        CalendarView calendarView = new CalendarView(activity, calendar, list, calendarViewClickListener);
        fragment_calendarview_layout.addView(calendarView);
        getMonthPlan();


    }


    private class CalendarViewClickListener implements CalendarView.CallBack {
        @Override
        public void clickDate(CustomDate date) {
            final String yearmonth = date.year + "-" + getNum(date.month);
            // if (getNumber(tViewCalendar.tv_date.getText().toString().substring(5, tViewCalendar.tv_date.getText().toString().length() - 1)).equals(yearmonth.substring(5, yearmonth.length()))) {
            MyToash.Log(yearmonth);
            String yearmonthdate = yearmonth + "-" + getNum(date.day);
            if (DataString.isToDayDate(yearmonthdate + "") != 1) {//不大于今天的
                ShareUitls.putString(activity, "CLICKDADE", yearmonthdate);
                if (ExerciseRecordActivity.activity != null) {
                    ExerciseRecordActivity.activity.finish();
                }
                Intent i = new Intent(activity, ExerciseRecordActivity.class);
                i.putExtra("time", yearmonthdate);
                startActivity(i);
                activity.finish();
            } else {
                Toast.makeText(activity, "暂无数据", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void initHandler() {

        }

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public void changeDate(CustomDate date) {

        }
    }


    private String getNum(int month) {
        if (month < 10) {
            return "0" + month;
        }
        return month + "";
    }


    private void getMonthPlan() {
        waitDialog.showDailog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostCalenderRequest");
        params.addBodyParameter("ResultJWT", ResultJWT);
        params.addBodyParameter("UID", UID);
        params.addBodyParameter("SportTime", getdate(calendar));
        MyToash.Log(ResultJWT + "    " + UID+  " "+getdate(calendar));
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        MyToash.Log(date + "    " + response);
                        fragment_calendarview_layout.removeAllViews();

                        riliCallBack riliCallBack = gson.fromJson(response.toString(), riliCallBack.class);
                        list.clear();
                        for (int i = 0; i < DayCount; i++) {
                            list.add(riliCallBack.getCalenderList().get(i));
                        }
                        CalendarView calendarView = new CalendarView(activity, calendar, list, calendarViewClickListener);
                        fragment_calendarview_layout.addView(calendarView);
                        waitDialog.dismissDialog();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        waitDialog.dismissDialog();

                    }
                }

        );
    }
    public String getdate(Calendar calendar) {
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd");
        Date dat = calendar.getTime();
        return dformat.format(dat);
    }

}
