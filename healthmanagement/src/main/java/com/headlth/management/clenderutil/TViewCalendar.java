package com.headlth.management.clenderutil;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.headlth.management.R;


public class TViewCalendar extends TViewBase {
    private ViewPager viewPager;
    private Button back;
    private Button recent;
    public TextView tv_date;
    Activity context;

    public TViewCalendar(Activity activity) {

        super(activity, true);
        this.context = activity;
        initDialog();
    }

    @Override
    public int viewLayoutRes() {
        return R.layout.activity_calendar;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        back = (Button) view.findViewById(R.id.bt_back);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
    }

    @Override
    public void setViewListener() {
        back.setOnClickListener(this);
        /*iv_next.setOnClickListener(this);*/
    }

    @Override
    public void initData() {
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setDate(String date) {
        tv_date.setText(date);
    }

    private WaitDialog waitDialog;

    private void showDialog(boolean isShow) {
        if (isShow) {
            waitDialog.showDailog();
        } else {
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }
    }

    private void initDialog() {
        waitDialog = new WaitDialog(mActivity);
        waitDialog.setMessage("请稍候...");
        waitDialog.setCancleable(true);
    }


    //日历按钮前后监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_recent:


                break;
            case R.id.bt_back:
                mActivity.finish();
                break;
        }
    }


}