package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.fragment.CalendarViewFragment;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.NoPreloadViewPager;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2017/9/25.
 */

@ContentView(R.layout.activity_calendar)
public class CalendarActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;
    private Activity activity;
    @ViewInject(R.id.viewpager)
    private NoPreloadViewPager noPreloadViewPager;
    private ViewPagerCalendarViewAdapter viewPagerCalendarViewAdapter;

    private List<Fragment> myCalendarViewFragmentList;
    private List<String> dateList;
    //   private List<NumberAndDate> numberAndDates;
    Map<Integer, String> numberAndDates;
    private SimpleDateFormat dformatNoDay, dformatHaveDay;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;


        initialize();

    }

    private void initialize() {
        myCalendarViewFragmentList = new ArrayList<>();
        dateList = new ArrayList<>();
        numberAndDates = new HashMap<>();
        viewPagerCalendarViewAdapter = new ViewPagerCalendarViewAdapter();
        dformatNoDay = new SimpleDateFormat("yyyy-MM");
        dformatHaveDay = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = -36; i < 12; i++) {//今天为准 可查看过去两年未来一年的数据 （按需求自有更改）
            Calendar calendar = getYEAR_MONTH(i);
            String data = getdate(calendar);
            dateList.add(data);
            numberAndDates.put(i + 36, data);
            CalendarViewFragment calendarViewFragment = new CalendarViewFragment(calendar);
            myCalendarViewFragmentList.add(calendarViewFragment);

        }
        //位置移到上次点击的月份 默认今天
        String date = ShareUitls.getString(activity, "CLICKDADE", dformatHaveDay.format(new Date())).substring(0, 7);
        int CurrentItem = 36;
        for (int i = 0; i < 48; i++) {
            if (date.equals(dateList.get(i))) {
                CurrentItem = i;
                break;
            }
        }
        view_publictitle_title.setText(numberAndDates.get(CurrentItem));
        noPreloadViewPager.setAdapter(viewPagerCalendarViewAdapter);
        noPreloadViewPager.setCurrentItem(CurrentItem);
        noPreloadViewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String data = numberAndDates.get(position);
                view_publictitle_title.setText(data);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Event(value = {R.id.view_publictitle_back, R.id.bt_recent})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.bt_recent:
                if (ExerciseRecordActivity.activity != null) {
                    ExerciseRecordActivity.activity.finish();
                }
                Date date = new Date();
                String today = dformatHaveDay.format(date);
                ShareUitls.putString(activity, "CLICKDADE", today);
                Intent i = new Intent(activity, ExerciseRecordActivity.class);
                i.putExtra("time", today);
                startActivity(i);
                finish();
                break;
        }
    }

    public String getdate(Calendar calendar) {
        Date dat = calendar.getTime();
        return dformatNoDay.format(dat);
    }

    public Calendar getYEAR_MONTH(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, i);
        return calendar;
    }

    private class ViewPagerCalendarViewAdapter extends FragmentPagerAdapter {
        public ViewPagerCalendarViewAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return myCalendarViewFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return myCalendarViewFragmentList.size();
        }


    }

/*    class NumberAndDate {
        public int Number;
        public String date;

        public NumberAndDate(int number, String date) {
            Number = number;
            this.date = date;
        }
    }*/
}