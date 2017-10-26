package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.StringForTime;

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
    @InjectView(R.id.zhouerall)
    Button zhouerall;
    @InjectView(R.id.zhousanall)
    Button zhousanall;
    @InjectView(R.id.zhousiall)
    Button zhousiall;
    @InjectView(R.id.zhouwuall)
    Button zhouwuall;
    @InjectView(R.id.zhouliuall)
    Button zhouliuall;
    @InjectView(R.id.zhouriall)
    Button zhouriall;
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
    private TextView TotalDays;
    private TextView MaxTotalTime;
    private RelativeLayout zhu;
    private TextView midleTime;
    private TextView botomLin;
    View view;
    String tatal;
    List<Button> btalls = null;

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
        TotalDays = (TextView) view.findViewById(R.id.TotalDays);
        MaxTotalTime = (TextView) view.findViewById(R.id.MaxTotalTime);
        midleTime = (TextView) view.findViewById(R.id.midleTime);
        ButterKnife.inject(this, view);
        if (!ShareUitls.getString(getActivity(), "Target", "null").equals("null")) {
            Log.e("tttt", ShareUitls.getString(getActivity(), "Target", "null"));
            target = Integer.parseInt(ShareUitls.getString(getActivity(), "Target", "null"));
        }

        tatal = StringForTime.stringForTime3(target);
        WindowManager wm = getActivity().getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();


        return view;
    }

    int x;
    int y;
    int bootom;
    int MaxHight;

    int top;
    int daohangHigh;
    int MaxTime = 0;

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
                    int[] location0 = new int[2];
                    botomLin.getLocationOnScreen(location0);
                    bootom = zhu.getTop();
                    top = botomLin.getTop();
                    MaxHight = (botomLin.getTop() - zhu.getTop());
                    for (int i = 0; i < anlyse.getData().getDetail().size(); i++) {
                        FrameLayout.LayoutParams linearParamsall = (FrameLayout.LayoutParams) btalls.get(i).getLayoutParams();
                        linearParamsall.height = MaxHight * (target) / (MaxTime); //
                        btalls.get(i).setLayoutParams(linearParamsall);
                        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) btalls.get(i).getLayoutParams();
                        // 取控件aaa当前的布局参数
                        linearParams.height = MaxHight * (Integer.parseInt(anlyse.getData().getDetail().get(i).getEffectTime())) / (MaxTime); //
                        btalls.get(i).setLayoutParams(linearParams);
                        ts.get(i).setText(anlyse.getData().getDetail().get(i).getDay());
                    }
                }
            }
            if (msg.what == 50) {
                if (d != null) {
                    relativeLayout.removeView(d);
                    d = null;
                }
                d = new draw(getContext(), msg.obj, msg.arg1, (msg.arg2 - daohangHigh));
                relativeLayout.addView(d);

            }

        }
    };

    draw d;
    RelativeLayout relativeLayout;

    public class draw extends View {
        int x = 0;
        int y = 0;
        int EffectTime = 0;

        public draw(Context context, Object data, int x, int y) {
            super(context);
            setWillNotDraw(false);
            this.EffectTime = (Integer) data;
            this.x = x;
            this.y = y;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            setWillNotDraw(false);
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

            String effect = StringForTime.stringForTime3(EffectTime);
            int effectwidth = x+(zhouyiall.getWidth()-AnalizeClFragment.getTextWidth(effect,paint))/2;//要居中的话 柱状图的宽度减去文字的宽度的一半 加上X 就等于文字的起始坐标
            int tatalwidth = x+(zhouyiall.getWidth()-AnalizeClFragment.getTextWidth(tatal,paint))/2;//要居中的话 柱状图的宽度减去文字的宽度的一半 加上X 就等于文字的起始坐标
            canvas.drawText(effect, effectwidth, y - 90, paint);
            canvas.drawLine(x, y - 85, x + zhouyiall.getWidth(), y - 85, paint);
            canvas.drawText(tatal, tatalwidth, y - 60, paint);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_triangle_orange);
            canvas.drawBitmap(bitmap, x + zhouyiall.getWidth() / 4, y - 50, paint);
        }
    }


    private anlyseCallBack anlyse;

    public int getPercent(anlyseCallBack anlyse) {

        return Integer.parseInt(changDataType(anlyse.getData().getSummary().get(0).getPercentage()).trim());
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = s3.split("\\.");
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

    String avgEffec;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btalls = new ArrayList<>();
        btalls.add(zhouyiall);
        btalls.add(zhouerall);
        btalls.add(zhousanall);
        btalls.add(zhousiall);
        btalls.add(zhouwuall);
        btalls.add(zhouliuall);
        btalls.add(zhouriall);
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
                zhouyiall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime()));

                        int[] location55 = new int[2];
                        zhouyiall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouyiall.getLocationOnScreen(location555);
                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(0).getEffectTime());
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
                zhouerall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime()));

                        int[] location55 = new int[2];
                        zhouerall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouerall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(1).getEffectTime());

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
                zhousanall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime()));
                        int[] location55 = new int[2];
                        zhousanall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];

                        zhousanall.getLocationOnScreen(location555);
                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(2).getEffectTime());

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
                zhousiall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(3).getEffectTime()));
                        int[] location55 = new int[2];
                        zhousiall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhousiall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(3).getEffectTime());

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
                zhouwuall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(5).getEffectTime()));
                        int[] location55 = new int[2];
                        zhouwuall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouwuall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(4).getEffectTime());

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
                zhouliuall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(5).getEffectTime()));
                        int[] location55 = new int[2];
                        zhouliuall.getLocationOnScreen(location55);

                        int[] location555 = new int[2];
                        zhouliuall.getLocationOnScreen(location555);

                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(5).getEffectTime());

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
                zhouriall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       // String effect = StringForTime.stringForTime2(Integer.parseInt(anlyse.getData().getDetail().get(6).getEffectTime()));
                        int[] location55 = new int[2];
                        zhouriall.getLocationOnScreen(location55);
                        int[] location555 = new int[2];
                        zhouriall.getLocationOnScreen(location555);
                        Message mssg = h.obtainMessage();
                        mssg.what = 50;
                        mssg.obj = Integer.parseInt(anlyse.getData().getDetail().get(6).getEffectTime());
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

                TotalDays.setText("达标天数：" + anlyse.getData().getSummary().get(0).getTotalDays() + "天");

               /* avgEffec = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime()) / 60);
                avgEffec = (avgEffec + "") + "'" + second((Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime())) % 60) + "''";*/

                AvgEffectTime.setText(StringForTime.stringForTime3(Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime())));
                MaxTime = Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime());
               /* avgEffec = "" + ( / 60);
                avgEffec = (avgEffec + "") + "'" + second((Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime())) % 60) + "''";
                */
                AvgTotalTime.setText(StringForTime.stringForTime3(Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime())));

                Percentage.setText(anlyse.getData().getSummary().get(0).getPercentage() + "");
                MaxTotalTime.setText(Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime()) / 60 + "min");
                midleTime.setText("" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime()) / 60) / 2 + "min");
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
