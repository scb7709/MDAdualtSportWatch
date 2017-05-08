package com.headlth.management.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.clenderutil.DateUtil;
import com.headlth.management.clenderutil.TViewCalendar;
import com.headlth.management.clenderutil.TViewCalendarController;
import com.headlth.management.entity.shouyeCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivityCalendar extends CalendarBaseActivity implements View.OnClickListener{
    private TViewCalendarController calendarController;
    private TViewCalendar tViewCalendar;
    private Button back;
    private Button recent;
    private Button recentData;
    public static String LastSportDate;
    public static String  EduCode;
    private ProgressDialog pd;
    private App app;
    private static String url;
    public static  Activity activity;
    public String hanzi(int i){
        if(i==1)
        return "一月";
        if(i==2)
            return "二月";
        if(i==3)
            return "三月";
        if(i==4)
            return "四月";
        if(i==5)
            return "五月";
        if(i==6)
            return "六月";
        if(i==7)
            return "七月";
        if(i==8)
            return "八月";
        if(i==9)
            return "九月";
        if(i==10)
            return "十月";
        if(i==11)
            return "十一月";
        if(i==12)
            return "十二月";
       return  "";
    }
    @Override
    public void onPause() {
        super.onPause();
        if (pd != null) {
            pd.dismiss();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        app = (App) getApplicationContext();// 获取应用程序全局的实例引用
        app.activities.add(this); // 把当前Activity放入集合中
        url = Constant.BASE_URL;
       /*  //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        ShareUitls.putString(getApplicationContext(), "count", "null");
        back = (Button)this .findViewById(R.id.bt_back);
        recent = (Button) this.findViewById(R.id.bt_recent);

        // 实例化对象
        calendarController = new TViewCalendarController(this,EduCode);
        tViewCalendar = calendarController.getTViewCalendar();
        // 加载视图
        ll_main.addView(tViewCalendar.getView());
        recentData = (Button)tViewCalendar.getView() .findViewById(R.id.bt_recent);
        // 设置监听
        tViewCalendar.setViewOnClickListener(this);
        recentData.setOnClickListener(CalendarActivityCalendar.this);
        String year = DateUtil.getYear() + "/" + hanzi(DateUtil.getMonth());
        tViewCalendar.setDate(year);
    }

//暂时没用
    @Override
    public void OnClick(int viewId) {
        switch (viewId) {
            case R.id.bt_back:
                  finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Button bt= (Button) v;
        switch (bt.getId()) {

            case R.id.bt_recent:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                ShareUitls.putString(CalendarActivityCalendar.this, "CLICKDADE", format.format(new Date()));//把日历 点击 设置今天
               /* Log.e("dangqian", "123456");
                int data=DateUtil.getCurrentMonthDay();
                Log.e("dangqian", DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay()) + "");
			    rili2(DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay()),0);*/

                Intent i = new Intent(CalendarActivityCalendar.this, ExerciseRecordActivity.class);
                i.putExtra("time",DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay()));
                startActivity(i);
                finish();
                break;


        }
    }
    //首页
    // http://www.ssp365.com:8066/MdMobileService.ashx?do=PostChangeCalenderRequest
    private shouyeCallBack shouye;
    Gson g = new Gson();
    private void rili2(final String monthDay, final int i) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostChangeCalenderRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(CalendarActivityCalendar.this, "UID", "null"));
        params.addBodyParameter("SportTime",monthDay);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {


                    @Override
                    public void onResponse(String response) {
                        Log.i("fffffffff回到今天的",response.toString());

                        shouye = g.fromJson(response.toString(), shouyeCallBack.class);
                        if (shouye.getStatus() == 1) {

                            ShareUitls.putString(CalendarActivityCalendar.this, "count", "null");
                            Log.e("tttt", response.toString());
                            Intent i = new Intent(CalendarActivityCalendar.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mark","1");
                            bundle.putString("time",monthDay);
                            bundle.putSerializable("shouye", (Serializable) shouye);
                            i.putExtras(bundle);
                            CalendarActivityCalendar.this.startActivity(i);
                            CalendarActivityCalendar.this.finish();
                        }else{
                            Toast.makeText(CalendarActivityCalendar.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA","LoginupToken");
                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

        );
    }
    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.activities.remove(this); // 把当前Activity从集合中移除
    }
}
