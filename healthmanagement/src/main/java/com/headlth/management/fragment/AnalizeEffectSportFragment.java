package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.headlth.management.R;
import com.headlth.management.entity.anlyseCallBack;
import com.headlth.management.entity.sevenDataTime;
import com.headlth.management.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AnalizeEffectSportFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.AvgEffectTime)
    TextView AvgEffectTime;
    @InjectView(R.id.AvgTotalTime)
    TextView AvgTotalTime;
    @InjectView(R.id.youxiao)
    TextView youxiao;
    @InjectView(R.id.backd)
    FrameLayout backd;
    @InjectView(R.id.Percentage)
    TextView Percentage;

    @InjectView(R.id.zhouyiall)
    Button zhouyiall;
    @InjectView(R.id.zhouyi)
    Button zhouyi;
    @InjectView(R.id.zhouerall)
    Button zhouerall;
    @InjectView(R.id.zhouer)
    Button zhouer;
    @InjectView(R.id.zhousanall)
    Button zhousanall;
    @InjectView(R.id.zhousan)
    Button zhousan;
    @InjectView(R.id.zhousiall)
    Button zhousiall;
    @InjectView(R.id.zhousi)
    Button zhousi;
    @InjectView(R.id.zhouwuall)
    Button zhouwuall;
    @InjectView(R.id.zhouwu)
    Button zhouwu;
    @InjectView(R.id.zhouliuall)
    Button zhouliuall;
    @InjectView(R.id.zhouliu)
    Button zhouliu;
    @InjectView(R.id.zhouriall)
    Button zhouriall;
    @InjectView(R.id.zhouri)
    Button zhouri;
    @InjectView(R.id.t1)
    TextView t1;
    @InjectView(R.id.t2)
    TextView t2;
    @InjectView(R.id.t3)
    TextView t3;
    @InjectView(R.id.t4)
    TextView t4;
    @InjectView(R.id.t5)
    TextView t5;
    @InjectView(R.id.t6)
    TextView t6;
    @InjectView(R.id.t7)
    TextView t7;
    @InjectView(R.id.show1)
    Button show1;
    @InjectView(R.id.show2)
    Button show2;
    @InjectView(R.id.show3)
    Button show3;
    @InjectView(R.id.show4)
    Button show4;
    @InjectView(R.id.show5)
    Button show5;
    @InjectView(R.id.show6)
    Button show6;
    @InjectView(R.id.show7)
    Button show7;

    private int progress = 0;
    /*    private RoundProgressBar mRoundProgressBar2;*/
    int roundnum = 0;
    private TextView TotalDays;
    private TextView MaxTotalTime;
    private RelativeLayout zhu;
    private TextView midleTime;
    private TextView botomLin;
    View view;
    List<Button> bts = null;
    List<Button> btalls = null;
    List<Button> shows = null;
    List<TextView> ts = null;
    int target;
    private int screenWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_sport, null);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.view);
        zhu = (RelativeLayout) view.findViewById(R.id.zhu);
        botomLin = (TextView) view.findViewById(R.id.t1);
        ButterKnife.inject(this, view);
        if (!ShareUitls.getString(getActivity(), "Target", "null").equals("null")) {
            Log.e("tttt", ShareUitls.getString(getActivity(), "Target", "null"));
            target = Integer.parseInt(ShareUitls.getString(getActivity(), "Target", "null")) * 60;
        }


        WindowManager wm = getActivity().getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();


        return view;
    }

    int x;
    int y;
    int bootom;
    int gap;
    int top;
    int daohangHigh;
    int fenmu = 0;
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (backd.getWidth() != 0 && backd.getHeight() != 0 && relativeLayout.getWidth() != 0 && relativeLayout.getHeight() != 0 && zhu.getWidth() != 0 && zhu.getHeight() != 0 && botomLin.getWidth() != 0 && botomLin.getHeight() != 0) {
                    FrameLayout.LayoutParams youxiaolinearParams = (FrameLayout.LayoutParams) youxiao.getLayoutParams();
                    // 取控件aaa当前的布局参数
                    youxiaolinearParams.width = getPercent(anlyse) * backd.getWidth() / 100;// 当控件的高强制设成365象素
                    youxiao.setLayoutParams(youxiaolinearParams);

                    int[] location666 = new int[2];
                    relativeLayout.getLocationOnScreen(location666);
                    daohangHigh = location666[1];
                    int[] location = new int[2];
                    zhu.getLocationOnScreen(location);
                    x = location[0];
                    y = location[1];
                    Log.e("zuobiao", "zhux:" + x + "zhuy:" + y);
                    Log.e("zuobiao", "zhuLeft：" + zhu.getLeft() + "zhuRight：" + zhu.getRight() + "zhuTop：" + zhu.getTop() + "zhuBottom：" + zhu.getBottom());

                    int[] location0 = new int[2];
                    botomLin.getLocationOnScreen(location0);
                    int x0 = location0[0];
                    int y0 = location0[1];

                    Log.e("zuobiao", "botomLinx1:" + x0 + "botomLiny1:" + y0);
                    Log.e("zuobiao", "botomLint1：" + botomLin.getLeft() + "botomLinRight：" + botomLin.getRight() + "botomLinTop：" + botomLin.getTop() + "botomLinBottom：" + botomLin.getBottom());
                    bootom = zhu.getTop();
                    top = botomLin.getTop();
                    gap = (botomLin.getTop() - zhu.getTop());
                    for (int i = 0; i < anlyse.getData().getDetail().size(); i++) {
                        Log.e("zhixin???", "gap:==");
                        FrameLayout.LayoutParams linearParamsall = (FrameLayout.LayoutParams) btalls.get(i).getLayoutParams();
                        // 设置
                        if (Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime()) <= 0) {

                        } else {
                            fenmu = Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime());
                        }
                        linearParamsall.height = gap * (target / 60) / (fenmu / 60 + 10); // 当控件的高强制设成365象素
                        btalls.get(i).setLayoutParams(linearParamsall);
                     /*   FrameLayout.LayoutParams linearParamsshow = (FrameLayout.LayoutParams) shows.get(i).getLayoutParams();



                        // 取控件aaa当前的布局参数
                  *//*      if(Integer.parseInt(anlyse.getData().getDetail().get(i).getEffectTime())>target){
                            Log.e("ssss","dfafasfasf");
                            linearParamsshow.height = gap * (Integer.parseInt(anlyse.getData().getDetail().get(i).getEffectTime()) / 60) / (fenmu / 60 + 10); // 当控件的高强制设成365象素
                        }else{
                            linearParamsshow.height = gap * (target / 60) / (fenmu / 60+10); // 当控件的高强制设成365象素
                        }*//*
                        linearParamsshow.height = gap * (target / 60) / (fenmu / 60+10); // 当控件的高强制设成365象素
                        shows.get(i).setLayoutParams(linearParamsall);*/


                        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) bts.get(i).getLayoutParams();
                        // 取控件aaa当前的布局参数
                        linearParams.height = gap * (Integer.parseInt(anlyse.getData().getDetail().get(i).getEffectTime()) / 60) / (fenmu / 60 + 10); // 当控件的高强制设成365象素
                        bts.get(i).setLayoutParams(linearParams);
                        ts.get(i).setText(anlyse.getData().getDetail().get(i).getDay());
                    }
                    Log.e("zuobiao", "gap:==" + gap);
                }
            }
            if (msg.what == 50) {
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.view);
                if (count == 1) {
                    d = new draw(getContext(), msg.obj, msg.arg1, (msg.arg2 - daohangHigh));
                    relativeLayout.addView(d);
                    count++;
                } else {
                    relativeLayout.removeView(d);
                    d = new draw(getContext(), msg.obj, msg.arg1, (msg.arg2 - daohangHigh));
                    relativeLayout.addView(d);
                }
            }

        }
    };
    int count;
    draw d;
    RelativeLayout relativeLayout;


    public class draw extends View {
        int x = 0;
        int y = 0;
        sevenDataTime data = null;

        public draw(Context context, Object data, int x, int y) {
            super(context);
            setWillNotDraw(false);
            Log.e("0000", x + "开始画了---1111" + y + "开始画了");
            this.data = (sevenDataTime) data;
            this.x = x;
            this.y = y;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            setWillNotDraw(false);
            Log.e("0000", x + "开始画了---22222" + y + "开始画了");
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#ffcc33"));
            paint.setStrokeWidth((float) 1.0);
            if (screenWidth < 1080) {
                paint.setTextSize(22);
            } else {
                paint.setTextSize(32);
            }

            canvas.drawText(data.getEffect(), x, y - 85, paint);
            canvas.drawLine(x - 10, y - 77, x + zhouyi.getWidth() + 10, y - 77, paint);
            canvas.drawText(data.getTotal(), x, y - 50, paint);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_triangle_orange);
            canvas.drawBitmap(bitmap, x + zhouyi.getWidth() / 4, y - 45, paint);
         /*   canvas.drawBitmap(drawableToBitamp(getResources().getDrawable(R.drawable.btn_date_active)), x, y - 10, paint);*/

         /*   drawImage(canvas, drawableToBitamp(getResources().getDrawable(R.drawable.icon_triangle_orange)),x+4,y-50,
           40, 40,100,100);*/
/*
            Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + 100, y + 50);
          *//*  PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
            paint.setPathEffect(effects);*//*
            canvas.drawPath(path, paint);*/


          /*  canvas.restore();*/
        }
    }


    private anlyseCallBack anlyse;

    public int getPercent(anlyseCallBack anlyse) {

        return Integer.parseInt(changDataType(anlyse.getData().getSummary().get(0).getPercentage()).trim());
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("\\.");
        Log.e("qqqq", str);
        for (int j = 0; j < temp.length; j++) {
            Log.e("qqqqq", temp[j]);
        }
        return temp[0].replace(",", "");
    }

    @SuppressLint("ValidFragment")
    public AnalizeEffectSportFragment(anlyseCallBack anlyse) {
        this.anlyse = anlyse;


    }

    public AnalizeEffectSportFragment() {
    }

    String avgTal;
    String avgEffec;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bts = new ArrayList<>();
        bts.add(zhouyi);
        bts.add(zhouer);
        bts.add(zhousan);
        bts.add(zhousi);
        bts.add(zhouwu);
        bts.add(zhouliu);
        bts.add(zhouri);
        btalls = new ArrayList<>();
        btalls.add(zhouyiall);
        btalls.add(zhouerall);
        btalls.add(zhousanall);
        btalls.add(zhousiall);
        btalls.add(zhouwuall);
        btalls.add(zhouliuall);
        btalls.add(zhouriall);
        shows = new ArrayList<>();
        shows.add(show1);
        shows.add(show2);
        shows.add(show3);
        shows.add(show4);
        shows.add(show5);
        shows.add(show6);
        shows.add(show7);
        ts = new ArrayList<>();
        ts.add(t1);
        ts.add(t2);
        ts.add(t3);
        ts.add(t4);
        ts.add(t5);
        ts.add(t6);
        ts.add(t7);

        if (anlyse != null) {
            if (anlyse.getStatus() != 0) {
                show1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }
                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);

                        int[] location55 = new int[2];
                        zhouyi.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouyiall.getLocationOnScreen(location555);
                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }

                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }



                       /* if((Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime())>target)){
                            mssg.arg2 = location55[1]-gap * ((Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime())-target) / 60) / (fenmu / 60+10);
                        }else {
                            mssg.arg2 = location55[1];
                        }*/
                       /* mssg.arg2 = location55[1];*/
                        Log.e("ssss", location55[1] + "location55[1]" + location55[0] + "location55[0]");
                        h.sendMessage(mssg);
                    }
                });
                show2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }
                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);


                        int[] location55 = new int[2];
                        zhouer.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouerall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }


                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }

                      /*  if((Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime())>target)){
                            mssg.arg2 = location55[1]-gap * ((Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime())-target) / 60) / (fenmu / 60+10);
                        }else {
                            mssg.arg2 = location55[1];
                        }*/
                        h.sendMessage(mssg);
                    }
                });
                show3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }
                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);
                        int[] location55 = new int[2];
                        zhousan.getLocationOnScreen(location55);
                        int[] location555 = new int[2];

                        zhousanall.getLocationOnScreen(location555);
                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }


                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }
                      /*  if((Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime())>target)){
                            mssg.arg2 = location55[1]-gap * ((Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime())-target) / 60) / (fenmu / 60+10);
                        }else {
                            mssg.arg2 = location55[1];
                        }*/
                        h.sendMessage(mssg);
                    }
                });
                show4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(3).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(3).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }
                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);
                        int[] location55 = new int[2];
                        zhousi.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhousiall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }

                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }


                        h.sendMessage(mssg);
                    }
                });
                show5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(4).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(4).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }

                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);
                        int[] location55 = new int[2];
                        zhouwu.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouwuall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }

                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }
                        h.sendMessage(mssg);
                    }
                });
                show6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(5).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(5).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }

                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);
                        int[] location55 = new int[2];
                        zhouliu.getLocationOnScreen(location55);

                        int[] location555 = new int[2];
                        zhouliuall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }

                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }
                        h.sendMessage(mssg);
                    }
                });
                show7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String effect="";
                        try {
                            effect = "" + second(Integer.parseInt(anlyse.getData().getDetail().get(6).getEffectTime()) / 60);
                            effect = effect + "'" + second(Integer.parseInt(anlyse.getData().getDetail().get(6).getEffectTime()) % 60) + "''";
                        } catch (IndexOutOfBoundsException I) {
                        }




                        String tatal = "" + target / 60;
                        tatal = tatal + "'" + second(target % 60) + "''";
                        sevenDataTime sdt = new sevenDataTime();
                        sdt.setEffect(effect);
                        sdt.setTotal(tatal);
                        int[] location55 = new int[2];
                        zhouri.getLocationOnScreen(location55);

                        int[] location555 = new int[2];
                        zhouriall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = sdt;

                        if (location55[0] > location555[0]) {
                            mssg.arg1 = location55[0];
                        } else {
                            mssg.arg1 = location555[0];
                        }

                        if (location55[1] > location555[1]) {
                            mssg.arg2 = location555[1];
                        } else {
                            mssg.arg2 = location55[1];
                        }
                        h.sendMessage(mssg);
                    }
                });
                TotalDays = (TextView) view.findViewById(R.id.TotalDays);
                MaxTotalTime = (TextView) view.findViewById(R.id.MaxTotalTime);
                midleTime = (TextView) view.findViewById(R.id.midleTime);
          /*  AvgTotalTime = (TextView) view.findViewById(R.id.TotalCal);
            TotalDays = (TextView) view.findViewById(R.id.TotalDays);
            MaxTotalTime = (TextView) view.findViewById(R.id.MaxTotalTime);
            midleTime = (TextView) view.findViewById(R.id.midleTime);


        /*    avgTal = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime()) / 60);
            avgTal = (avgTal + "") + "'" + (Integer.parseInt(spcl.getData().getSummary().get(0).getAvgTotalTime())) % 60 + "''";
            AvgTotalTime.setText(avgTal);*/

      /*      avgEffec = "" + (Integer.parseInt(spcl.getData().getSummary().get(0).getAvgEffectTime()) / 60);
            avgEffec = (avgEffec + "") + "'" + (Integer.parseInt(spcl.getData().getSummary().get(0).getAvgEffectTime())) % 60 + "''";
            AvgEffectTime.setText(avgEffec);
            TotalDays.setText("共运动：" + spcl.getData().getSummary().get(0).getTotalDays() + "天");*/
                TotalDays.setText("达标天数：" + anlyse.getData().getSummary().get(0).getTotalDays() + "天");


                //update by scb 2016/7/9/10


            /*    int youxiao=Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime()) ;

                if(ShareUitls.getString(getActivity(),"IsPlay","null").equals("1")){
                    int total=youxiao+Integer.parseInt(ShareUitls.getString(getActivity(),"PowerTrainDuration","0"))+youxiao;
                    avgEffec = "" + (total / 60);
                    avgEffec = (avgEffec + "") + "'" + second(total % 60) + "''";

                }
                else {*/
                avgEffec = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime()) / 60);
                avgEffec = (avgEffec + "") + "'" + second((Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime())) % 60) + "''";

                // }
                AvgEffectTime.setText(avgEffec);


                avgEffec = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime()) / 60);
                avgEffec = (avgEffec + "") + "'" + second((Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime())) % 60) + "''";
                AvgTotalTime.setText(avgEffec);
                Percentage.setText(anlyse.getData().getSummary().get(0).getPercentage() + "");
                MaxTotalTime.setText(Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime()) / 60 + 10 + "min");
                midleTime.setText("" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime()) / 60 + 10) / 2 + "min");





          /*  RoundProgressBar bar = new RoundProgressBar(getContext(), 0);
            if ((Integer.parseInt(spcl.getData().getSummary().get(0).getAvgTotalTime()) / 60) == 0) {
                bar.setMmnun(1000);
            } else {
                bar.setMmnun((Integer.parseInt(spcl.getData().getSummary().get(0).getAvgTotalTime()) ));
            }
            mRoundProgressBar2 = (RoundProgressBar) getActivity().findViewById(R.id.roundProgressBar2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progress <= roundnum) {
                        System.out.println(progress);
                        mRoundProgressBar2.setProgress(progress);
                        progress += 10;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();*/
                h.sendEmptyMessageDelayed(1, 1);
            }
        }

    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
