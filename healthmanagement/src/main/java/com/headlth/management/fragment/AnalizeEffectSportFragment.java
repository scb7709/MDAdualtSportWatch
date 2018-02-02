package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.anlyseCallBack;
import com.headlth.management.utils.GetWindowSize;
import com.headlth.management.utils.ImageUtil;
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

    @InjectView(R.id.fragment_layout_sport_zhou1)
    RelativeLayout fragment_layout_sport_zhou1;
    @InjectView(R.id.fragment_layout_sport_zhou2)
    RelativeLayout fragment_layout_sport_zhou2;
    @InjectView(R.id.fragment_layout_sport_zhou3)
    RelativeLayout fragment_layout_sport_zhou3;
    @InjectView(R.id.fragment_layout_sport_zhou4)
    RelativeLayout fragment_layout_sport_zhou4;
    @InjectView(R.id.fragment_layout_sport_zhou5)
    RelativeLayout fragment_layout_sport_zhou5;
    @InjectView(R.id.fragment_layout_sport_zhou6)
    RelativeLayout fragment_layout_sport_zhou6;
    @InjectView(R.id.fragment_layout_sport_zhou7)
    RelativeLayout fragment_layout_sport_zhou7;

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
    private LinearLayout zhu;
    private TextView midleTime;
    //  private TextView botomLin;
    View view;
    String tatal;

    List<Button> shows = null;

    List<Button> btalls = null;
    List<RelativeLayout> RelativeLayouts = null;
    List<TextView> ts = null;
    int target;
    private int screenWidth;
    //private int screenHeight;
    private Activity activity;
    int totalColar, lineColar, effectColar;
    List<anlyseCallBack.DataBean.DetailBean> detailBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout_sport, null);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.view);
        zhu = (LinearLayout) view.findViewById(R.id.zhu);
        // botomLin = (TextView) view.findViewById(R.id.t1);
        TotalDays = (TextView) view.findViewById(R.id.TotalDays);
        MaxTotalTime = (TextView) view.findViewById(R.id.MaxTotalTime);
        midleTime = (TextView) view.findViewById(R.id.midleTime);
        ButterKnife.inject(this, view);
        try {
            target = Integer.parseInt(ShareUitls.getString(getActivity(), "Target", "null"));
        } catch (Exception e) {

        }
        activity = getActivity();
        tatal = StringForTime.stringForTime3(target);
        screenWidth = GetWindowSize.getInstance(activity).getGetWindowwidth();
        //  screenHeight = GetWindowSize.getInstance(activity).getGetWindowheight();
        totalColar = activity.getResources().getColor(R.color.analizegray);
        lineColar = activity.getResources().getColor(R.color.analizeline);
        effectColar = activity.getResources().getColor(R.color.analizeeffect);
        return view;
    }
    int MaxHight;
    int daohangHigh;
    int MaxTime = 0;
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    if (backd.getWidth() != 0 && backd.getHeight() != 0 && relativeLayout.getWidth() != 0 && relativeLayout.getHeight() != 0 && zhu.getWidth() != 0 && zhu.getHeight() != 0) {
                        FrameLayout.LayoutParams youxiaolinearParams = (FrameLayout.LayoutParams) youxiao.getLayoutParams();
                        // 取控件aaa当前的布局参数
                        youxiaolinearParams.width = getPercent(anlyse) * backd.getWidth() / 100;//
                        youxiao.setLayoutParams(youxiaolinearParams);
                        int[] location666 = new int[2];
                        relativeLayout.getLocationOnScreen(location666);
                        daohangHigh = location666[1];
                        int[] location = new int[2];
                        zhu.getLocationOnScreen(location);
                        MaxHight = zhu.getHeight();// (botomLin.getTop() - zhu.getTop());

                        for (int i = 0; i < anlyse.getData().getDetail().size(); i++) {
                            RelativeLayout.LayoutParams linearParamsbtalls = (RelativeLayout.LayoutParams) btalls.get(i).getLayoutParams();
                            RelativeLayout.LayoutParams linearParamsshows = (RelativeLayout.LayoutParams) shows.get(i).getLayoutParams();
                            if (MaxTime != 0) {
                                int TotalTime = Integer.parseInt(detailBean.get(i).getTotalTime());
                                linearParamsshows.height = MaxHight * TotalTime / (MaxTime); //
                                shows.get(i).setLayoutParams(linearParamsshows);

                                int EffectTime = Integer.parseInt(detailBean.get(i).getEffectTime());
                                // MyToash.Log(TotalTime + "   " + EffectTime + "    " + "  " + MaxTime + "  " + MaxHight);

                                linearParamsbtalls.height = MaxHight * EffectTime / (MaxTime); //
                                btalls.get(i).setLayoutParams(linearParamsbtalls);
                                ts.get(i).setText(detailBean.get(i).getDay());


                            }
                        }
                    }else {
                        h.sendEmptyMessageDelayed(1, 1);
                    }
                } catch (Exception e) {
                    h.sendEmptyMessageDelayed(1, 1);
                }
            }


        }
    };

    draw d;
    RelativeLayout relativeLayout;

    private void showdraw(int TotalTime, int EffectTime, int x, int y) {
        if (d != null) {
            relativeLayout.removeView(d);
            d = null;
        }
        d = new draw(getContext(), TotalTime, EffectTime, x, (y - daohangHigh));
        relativeLayout.addView(d);

    }


    private void setOnClickListener(final int ShowPossition) {
        final RelativeLayout RelativeLayout = RelativeLayouts.get(ShowPossition);
        RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location55 = new int[2];
                anlyseCallBack.DataBean.DetailBean detailEntity = detailBean.get(ShowPossition);
                if (Integer.parseInt(detailEntity.getEffectTime()) > Integer.parseInt(detailEntity.getTotalTime())) {
                    btalls.get(ShowPossition).getLocationOnScreen(location55);
                } else {
                    shows.get(ShowPossition).getLocationOnScreen(location55);
                }
                showdraw(Integer.parseInt(detailEntity.getTotalTime()), Integer.parseInt(detailEntity.getEffectTime()), location55[0], location55[1]);
            }
        });
    }

    public class draw extends View {
        int x = 0;
        int y = 0;
        int EffectTime = 0;
        int TotalTime = 0;

        public draw(Context context, int TotalTime, int data, int x, int y) {
            super(context);
            setWillNotDraw(false);
            this.EffectTime = data;
            this.TotalTime = TotalTime;
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

            paint.setStrokeWidth((float) 1.0);
            if (screenWidth < 1080) {
                paint.setTextSize(22);
            } else {
                paint.setTextSize(32);
            }

            String effect = StringForTime.stringForTime3(EffectTime);
            String total = StringForTime.stringForTime3(TotalTime);
            int zhouyiall_Width = zhouyiall.getWidth();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_triangle_orange);

            //int  bitmap_height=bitmap.getHeight();
            int effectwidth;
            int tatalwidth;

            effectwidth = x + (zhouyiall_Width - AnalizeClFragment.getTextWidth(effect, paint)) / 2;//要居中的话 柱状图的宽度减去文字的宽度的一半 加上X 就等于文字的起始坐标

            tatalwidth = x + (zhouyiall_Width - AnalizeClFragment.getTextWidth(tatal, paint)) / 2;//要居中的话 柱状图的宽度减去文字的宽度的一半 加上X 就等于文字的起始坐标

            /*paint.setColor(totalColar);
            canvas.drawText(total, tatalwidth, y - ImageUtil.dp2px(activity, 35), paint);
            paint.setColor(lineColar);
            canvas.drawLine(x, y - ImageUtil.dp2px(activity, 32), x + zhouyiall_Width, y - ImageUtil.dp2px(activity, 32), paint);
            paint.setColor(effectColar);
            canvas.drawText(effect, effectwidth, y - ImageUtil.dp2px(activity, 20), paint);

            canvas.drawBitmap(bitmap, x + (zhouyiall_Width - bitmap.getWidth()) / 2, y - ImageUtil.dp2px(activity, 12), paint);*/

            paint.setColor(totalColar);
            canvas.drawText(total, tatalwidth, y - 90, paint);
            paint.setColor(lineColar);
            canvas.drawLine(x, y - 82, x + zhouyiall_Width, y - 82, paint);
            paint.setColor(effectColar);
            canvas.drawText(effect, effectwidth, y - 50, paint);

            canvas.drawBitmap(bitmap, x + (zhouyiall_Width - bitmap.getWidth()) / 2, y - ImageUtil.dp2px(activity, 12), paint);

        }
    }


    private anlyseCallBack anlyse;

    public int getPercent(anlyseCallBack anlyse) {

        return Integer.parseInt(changDataType(anlyse.getData().getSummary().get(0).getPercentage()).trim());
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = s3.split("\\.");
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
        //  MyToash.Log(screenWidth + "  " + screenHeight);
        btalls = new ArrayList<>();
        btalls.add(zhouyiall);
        btalls.add(zhouerall);
        btalls.add(zhousanall);
        btalls.add(zhousiall);
        btalls.add(zhouwuall);
        btalls.add(zhouliuall);
        btalls.add(zhouriall);
        RelativeLayouts = new ArrayList<>();
        RelativeLayouts.add(fragment_layout_sport_zhou1);
        RelativeLayouts.add(fragment_layout_sport_zhou2);
        RelativeLayouts.add(fragment_layout_sport_zhou3);
        RelativeLayouts.add(fragment_layout_sport_zhou4);
        RelativeLayouts.add(fragment_layout_sport_zhou5);
        RelativeLayouts.add(fragment_layout_sport_zhou6);
        RelativeLayouts.add(fragment_layout_sport_zhou7);
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
                detailBean = anlyse.getData().getDetail();
                for (int ShowPossition = 0; ShowPossition < 7; ShowPossition++) {
                    setOnClickListener(ShowPossition);
                }
                TotalDays.setText("达标天数：" + anlyse.getData().getSummary().get(0).getTotalDays() + "天");
                AvgEffectTime.setText(StringForTime.stringForTime3(Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgEffectTime())));
                MaxTime = Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxTotalTime());
                AvgTotalTime.setText(StringForTime.stringForTime3(Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgTotalTime())));
                Percentage.setText(anlyse.getData().getSummary().get(0).getPercentage() + "");
                MaxTotalTime.setText(MaxTime / 60 + "min");
                midleTime.setText("" + (MaxTime / 60) / 2);
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
