package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AnalizeClFragment extends BaseFragment {


    @InjectView(R.id.AvgCal)
    TextView AvgCal;
    @InjectView(R.id.TotalCal)
    TextView TotalCal;
    /*  @InjectView(R.id.TotalDays)
      TextView TotalDays;*/
    @InjectView(R.id.MaxTotalTime)
    TextView MaxTotalTime;
    @InjectView(R.id.midleTime)
    TextView midleTime;
    @InjectView(R.id.clzhouyi)
    Button clzhouyi;
    @InjectView(R.id.clzhouer)
    Button clzhouer;
    @InjectView(R.id.clzhousan)
    Button clzhousan;
    @InjectView(R.id.clzhousi)
    Button clzhousi;
    @InjectView(R.id.clzhouwu)
    Button clzhouwu;
    @InjectView(R.id.clzhouliu)
    Button clzhouliu;
    @InjectView(R.id.clzhouri)
    Button clzhouri;
    @InjectView(R.id.zhu)
    RelativeLayout zhu;
    RelativeLayout relativeLayout;
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
    private TextView botomLin;
    View view;
    List<Button> bts = null;
    List<TextView> ts = null;
    @InjectView(R.id.pingji)
    TextView pingji;
    private int screenWidth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_cl, null);
        ButterKnife.inject(this, view);

        botomLin = (TextView) view.findViewById(R.id.t1);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.view);

        WindowManager wm = getActivity().getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();




        return view;
    }

    private anlyseCallBack anlyse;

    @SuppressLint("ValidFragment")
    public AnalizeClFragment(anlyseCallBack anlyse) {
        this.anlyse = anlyse;
    }

    public AnalizeClFragment() {
    }

    String avgTal;
    String totalCal;
    private int progress = 0;
    private RelativeLayout mRoundProgressBar4;
    int roundnum = 100;
    int daohangHigh;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bts = new ArrayList<>();
        bts.add(clzhouyi);
        bts.add(clzhouer);
        bts.add(clzhousan);
        bts.add(clzhousi);
        bts.add(clzhouwu);
        bts.add(clzhouliu);
        bts.add(clzhouri);
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
                clzhouyi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {


                            int[] location55 = new int[2];
                            clzhouyi.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(0).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);


                        }

                    }
                });
                clzhouer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhouer.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(1).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }

                    }
                });
                clzhousan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhousan.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(2).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }

                    }
                });
                clzhousi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhousi.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(3).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }

                    }
                });
                clzhouwu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhouwu.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(4).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }

                    }
                });
                clzhouliu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhouliu.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(5).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }
                    }
                });
                clzhouri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anlyse != null) {
                            int[] location55 = new int[2];
                            clzhouri.getLocationOnScreen(location55);
                            Message mssg = h.obtainMessage();
                            mssg.what = 50;
                            mssg.obj = anlyse.getData().getDetail().get(6).getCalory();
                            mssg.arg1 = location55[0];
                            mssg.arg2 = location55[1];
                            h.sendMessage(mssg);
                        }
                    }
                });


                totalCal = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getTotalCal()));
                TotalCal.setText(totalCal);
                avgTal = "" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getAvgCal()));
                AvgCal.setText(avgTal);
         /*   TotalDays.setText("共运动：" + anlyse.getData().getSummary().get(0).getTotalDays() + "天");*/
                MaxTotalTime.setText((Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxCalory()) + 20) + "");


                Log.e("qqq", MaxTotalTime.getText().toString().length() + "-----" + anlyse.getData().getSummary().get(0).getMaxCalory().length());
                if (((Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxCalory()) + 20) / 2 + "").length() != anlyse.getData().getSummary().get(0).getMaxCalory().length()) {
                    midleTime.setText((Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxCalory()) + 20) / 2 + "  ");
                } else {
                    midleTime.setText("" + (Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxCalory()) + 20) / 2 + "");
                }


                pingji.setText(anlyse.getData().getSummary().get(0).getCaloryRate());


           /* mRoundProgressBar4 = (RelativeLayout) getActivity().findViewById(R.id.roundProgressBar4);
            mRoundProgressBar4.addView(new MyTestView(getActivity()));*/

                h.sendEmptyMessageDelayed(1, 1);
            }
        }

    }


    public String countSize(String str) {

        return null;
    }

    public class draw extends View {
        int x = 0;
        int y = 0;
        String data = "";

        public draw(Context context, Object data, int x, int y) {
            super(context);
            setWillNotDraw(false);
            Log.e("0000", x + "开始画了---1111" + y + "开始画了");

            this.data = (String) data;
            this.x = x;
            this.y = y;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Log.e("0000", x + "开始画了---22222" + y + "开始画了");
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#ffcc33"));
            paint.setStrokeWidth((float) 1.0);
            if(screenWidth<1080){
                paint.setTextSize(22);
            }else{
                paint.setTextSize(32);
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_triangle_orange);
            canvas.drawText(data, x + 10, y - 80, paint);
            canvas.drawBitmap(bitmap, x + clzhouyi.getWidth() / 4, y - 60, paint);

        }

    }

    public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                                 int w, int h, int bx, int by) {
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形
        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        src = null;
        dst = null;
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }


    int zhouwuHign;
    int xxxx;
    int yyyy;
    int xxx;
    int yyy;
    int xx;
    int yy;
    int x;
    int y;
    int bootom;
    int gap;
    int top;
    int count;
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (relativeLayout.getWidth() != 0 && relativeLayout.getHeight() != 0 && zhu.getWidth() != 0 && zhu.getHeight() != 0 && botomLin.getWidth() != 0 && botomLin.getHeight() != 0) {
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
                        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) bts.get(i).getLayoutParams();
                        // 取控件aaa当前的布局参数
                        linearParams.height = gap * Integer.parseInt(anlyse.getData().getDetail().get(i).getCalory()) / (Integer.parseInt(anlyse.getData().getSummary().get(0).getMaxCalory()) + 20); // 当控件的高强制设成365象素
                        bts.get(i).setLayoutParams(linearParams); // 使设置好的布局参数应用到控件aaa
                        ts.get(i).setText(anlyse.getData().getDetail().get(i).getDay());

                    }
                    Log.e("zuobiao", "gap:==" + gap);
                } else {
                    h.sendEmptyMessageDelayed(1, 1);
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

    draw d;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public class MyTestView extends View {
        //构造器
        public MyTestView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            //绘制黑色背景
        /*    canvas.drawColor(Color.BLACK);*/
            //创建画笔
            Paint paint = new Paint();
            //设置画笔颜色为红色
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#42c3f7"));
            canvas.drawCircle(68, 68, 68, paint);// 小圆
        }

    }


}
