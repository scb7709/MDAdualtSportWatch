package com.headlth.management.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.entity.newChuFangCallback;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/21.
 */
@ContentView(R.layout.newchufang)
public class NewChuFang extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @ViewInject(R.id.FinishWeek)
    TextView FinishWeek;
    @ViewInject(R.id.WeekRank)
    TextView WeekRank;

    @ViewInject(R.id.FinishedSportDays)
    TextView FinishedSportDays;
    @ViewInject(R.id.FinishTime)
    TextView FinishTime;
    @ViewInject(R.id.CurTargetTime)
    TextView CurTargetTime;
    @ViewInject(R.id.FinishedPercent)
    TextView FinishedPercent;
    @ViewInject(R.id.youxiao)
    TextView youxiao;
    @ViewInject(R.id.backd)
    FrameLayout backd;





    @ViewInject(R.id.target)
    Button target;
    @ViewInject(R.id.detail)
    Button detail;
    @ViewInject(R.id.d1)
    Button d1;
    @ViewInject(R.id.d2)
    Button d2;
    @ViewInject(R.id.d3)
    Button d3;
    @ViewInject(R.id.d4)
    Button d4;
    @ViewInject(R.id.d5)
    Button d5;
    @ViewInject(R.id.d6)
    Button d6;
    @ViewInject(R.id.d7)
    Button d7;

    //力量
    @ViewInject(R.id.newchufang_strengthtext)
    TextView newchufang_strengthtext;
    @ViewInject(R.id.strength_FinishedSportDays)
    TextView strength_FinishedSportDays;
    @ViewInject(R.id.strength_d1)
    Button strength_d1;
    @ViewInject(R.id.strength_d2)
    Button strength_d2;
    @ViewInject(R.id.strength_d3)
    Button strength_d3;
    @ViewInject(R.id.strength_d4)
    Button strength_d4;
    @ViewInject(R.id.strength_d5)
    Button strength_d5;
    @ViewInject(R.id.strength_d6)
    Button strength_d6;
    @ViewInject(R.id.strength_d7)
    Button strength_d7;

    @ViewInject(R.id.linear)
    LinearLayout linear;

    private newChuFangCallback newChuFang;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        view_publictitle_title.setText("运动计划");
        url = Constant.BASE_URL;
    }


    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
        }
    }



  /*  //可在该方法里进行属性参数的相关设置
    private void setBtnParams(Button button) {
        //参数：按钮的宽高
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, 8);
        params.weight=1.0f;//重量级
        params.gravity= Gravity.CENTER;//重心
        button.setLayoutParams(params);
        button.setBackgroundResource(R.drawable.adult);

    }*/

    List<Button> weekbts;

    @Override
    protected void onStart() {
        super.onStart();
        linear.removeAllViews();
        weekbts = new ArrayList<>();
        Intent intent = this.getIntent();
        newChuFang = (newChuFangCallback) intent.getSerializableExtra("newChuFang");
        Log.i("sssssssssssss", "" + newChuFang.getPList().getSummary().getTotalWeek().trim());
        h.sendEmptyMessage(1);
        FinishWeek.setText("已完成" + newChuFang.getPList().getSummary().getFinishWeek() + "周,剩余" + newChuFang.getPList().getSummary().getUnFinishWeek() + "周");
        WeekRank.setText("当前第" + newChuFang.getPList().getSummary().getStage() + "阶:第" + newChuFang.getPList().getCurrent().getWeekRank() + "周");
        FinishedSportDays.setText(newChuFang.getPList().getCurrent().getFinishedSportDays() + "/" + newChuFang.getPList().getCurrent().getWeekLeastDays());
        int m = 0;
        for (int i = 0; i < 7; i++) {
            m += newChuFang.getPList().getCurrent().getPowerTrainFinishList().get(i);
        }

        strength_FinishedSportDays.setText(m + "/" + 3);

        newchufang_strengthtext.setText("当前第" + newChuFang.getPList().getSummary().getStage() + "阶,第" + newChuFang.getPList().getCurrent().getWeekRank() + "周");
        FinishTime.setText(Integer.parseInt(newChuFang.getPList().getCurrent().getFinishTime()) / 60 + "'" + second(Integer.parseInt(newChuFang.getPList().getCurrent().getFinishTime()) % 60) + "''");
        CurTargetTime.setText("周目标: " + Integer.parseInt(newChuFang.getPList().getCurrent().getCurTargetTime()) / 60 + "分钟");
        FinishedPercent.setText(newChuFang.getPList().getCurrent().getFinishedPercent());


        linear.setOrientation(LinearLayout.HORIZONTAL);
        //动态加载按钮个数并且设置位置
        for (int i = 0; i < Integer.parseInt(newChuFang.getPList().getSummary().getTotalWeek().trim()); i++) {
            Button button = new Button(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, 16);
            if (i == 0) {
                params.setMargins(0, 0, 6, 0);
            }
            if (i == Integer.parseInt(newChuFang.getPList().getSummary().getTotalWeek().trim()) - 1) {
                params.setMargins(6, 0, 0, 0);
            } else {
                params.setMargins(6, 0, 6, 0);
            }
            params.weight = 1.0f;//重量级
            params.gravity = Gravity.CENTER;//重心
            button.setLayoutParams(params);
            linear.addView(button);
            weekbts.add(button);
        }
        //设置背景颜色
        for (int j = 0; j < Integer.parseInt(newChuFang.getPList().getSummary().getFinishWeek().trim()); j++) {
            weekbts.get(j).setBackgroundColor(Color.parseColor("#6FCF1A"));
        }
        for (int j = Integer.parseInt(newChuFang.getPList().getSummary().getFinishWeek().trim()); j < Integer.parseInt(newChuFang.getPList().getSummary().getTotalWeek().trim()); j++) {
            weekbts.get(j).setBackgroundColor(Color.parseColor("#ffffff"));
        }


        //当前周按钮显示
        List<Button> bts = new ArrayList<>();
        bts.add(d1);
        bts.add(d2);
        bts.add(d3);
        bts.add(d4);
        bts.add(d5);
        bts.add(d6);
        bts.add(d7);
        bts.add(strength_d1);
        bts.add(strength_d2);
        bts.add(strength_d3);
        bts.add(strength_d4);
        bts.add(strength_d5);
        bts.add(strength_d6);
        bts.add(strength_d7);


        String curWeekDay = DataString.StringData();
        Log.e("curWeekDay", curWeekDay);
        if ("1".equals(curWeekDay)) {
            d7.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d7.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("2".equals(curWeekDay)) {
            d1.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d1.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("3".equals(curWeekDay)) {
            d2.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d2.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("4".equals(curWeekDay)) {
            d3.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d3.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("5".equals(curWeekDay)) {
            d4.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d4.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("6".equals(curWeekDay)) {
            d5.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d5.setBackground(getResources().getDrawable(R.drawable.curday));
        } else if ("7".equals(curWeekDay)) {
            d6.setBackground(getResources().getDrawable(R.drawable.curday));
            strength_d6.setBackground(getResources().getDrawable(R.drawable.curday));
        }
        for (int i = 0; i < 14; i++) {
            if (i < 7) {
                if (newChuFang.getPList().getCurrent().getFinishList().get(i) == 1) {
                    bts.get(i).setBackground(getResources().getDrawable(R.drawable.complite));
                }
            } else {
                Log.i("wwwwww", "" + newChuFang.getPList().getCurrent().getPowerTrainFinishList().get(i - 7));
                if (newChuFang.getPList().getCurrent().getPowerTrainFinishList().get(i - 7) == 1) {
                    bts.get(i).setBackground(getResources().getDrawable(R.drawable.complite));
                }

            }
        }

        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2("", "");
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go3("", "");
            }
        });
    }
    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("\\.");

        for (int j = 0; j < temp.length; j++) {
            Log.e("qqq", temp[j]);
        }
        return temp[0];
    }

    public int getPercent(newChuFangCallback newChuFang) {
        return Integer.parseInt(changDataType(newChuFang.getPList().getCurrent().getFinishedPercent()).trim());
    }

    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (backd.getWidth() != 0 && backd.getHeight() != 0) {
                    FrameLayout.LayoutParams youxiaolinearParams = (FrameLayout.LayoutParams) youxiao.getLayoutParams();
                    // 取控件aaa当前的布局参数
                    youxiaolinearParams.width = getPercent(newChuFang) * backd.getWidth() / 100;// 当控件的高强制设成365象素
                    youxiao.setLayoutParams(youxiaolinearParams);

                }

            }
        }


    };


    //处方
    //http://www.ssp365.com:8066/ MdMobileService.ashx?do= PostPrescriptionRequest
    private void go3(final String phone, final String pwd) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPrescriptionRequest");
        params.addBodyParameter("UID", ShareUitls.getString(NewChuFang.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(NewChuFang.this, "ResultJWT", "0"));
        HttpUtils.getInstance(NewChuFang.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffff", response.toString());
                        chufang = g.fromJson(response.toString(),
                                chufangCallBack.class);
                        if (chufang.getStatus() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), ChuFangDtail.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("chufang", chufang);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );

    }

    //目标
    //http://www.ssp365.com:8066/ MdMobileService.ashx?do= PostPrescriptionRequest
    chufangCallBack chufang = null;

    Gson g = new Gson();
    private void go2(final String phone, final String pwd) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPrescriptionRequest");
        params.addBodyParameter("UID", ShareUitls.getString(NewChuFang.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(NewChuFang.this, "ResultJWT", "0"));
        HttpUtils.getInstance(NewChuFang.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffff", response.toString());
                        chufang = g.fromJson(response.toString(),
                                chufangCallBack.class);
                        if (chufang.getStatus() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), Target.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("chufang", chufang);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "获取失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
}
