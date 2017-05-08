package com.headlth.management.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.headlth.management.R;
import com.headlth.management.entity.xinlvtuCallBack;
import com.headlth.management.utils.ShareUitls;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class XinlvTu extends Activity {
    private RelativeLayout drawline;
    private TextView up;
    private TextView low;
    private TextView t0;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private xinlvtuCallBack xinlvCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thisdaixinlv);
        drawline = (RelativeLayout) this.findViewById(R.id.drawline);
        qujian = (RelativeLayout) this.findViewById(R.id.qujian);
        up = (TextView) this.findViewById(R.id.up);
        low = (TextView) this.findViewById(R.id.low);
        t0= (TextView) this.findViewById(R.id.t0);
        t1= (TextView) this.findViewById(R.id.t1);
        t2= (TextView) this.findViewById(R.id.t2);
        t3= (TextView) this.findViewById(R.id.t3);
        t4= (TextView) this.findViewById(R.id.t4);
        t5= (TextView) this.findViewById(R.id.t5);


        Intent intent = this.getIntent();
        xinlvCall = (xinlvtuCallBack) intent.getSerializableExtra("xinlvCall");
        h.sendEmptyMessageDelayed(1, 1);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }










    //代码设置布局margin
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }



    List<String> times = new ArrayList<String>();
    public void back(View v) {
        finish();
    }

    int gap;
    int x0;
    int y0;
    int width;
    private RelativeLayout qujian;
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (up.getWidth() != 0 && up.getHeight() != 0 && low.getWidth() != 0 && low.getHeight() != 0 && drawline.getWidth() != 0 && drawline.getHeight() != 0) {
                    int[] location666 = new int[2];
                    drawline.getLocationOnScreen(location666);
                    x0 = location666[0];
                    y0 = location666[1];
                    gap = low.getTop() - up.getTop();
                    width = up.getWidth();
                    Log.e("Xinlv", xinlvCall.getHeartRateList() + "xinlvCall.getHeartRateList()" + xinlvCall.getHeartRateList().size() + "xinlvCall.getHeartRateList().size()");


                    // 判断动态靶目标显示
                    //数值差
                    int xp = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "UBound", "null")) - Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "null"));
                    //位置像素差
                    int xxp = (gap * (Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "null"))-50)) / 200;
                    //高像素差
                    int xxp2 = (gap * xp) / 200;

                    setMargins(qujian,0, 0, 0, xxp);

                    Log.e("xxp", xxp + "--------------xxp");
                    Log.e("xxp", gap + "--------------gap");
                    Log.e("xxp", xxp2 + "--------------xxp2");

                    //设置高度
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) qujian.getLayoutParams();
                    // 取控件aaa当前的布局参数
                    linearParams.height = xxp2; // 当控件的高
                    qujian.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件aaa




                    if (xinlvCall.getHeartRateList().size() != 0) {
                        drawline.addView(new draw2(getApplicationContext(), xinlvCall.getHeartRateList()));
                    }else{
                        Toast.makeText(getApplicationContext(),"没有数据",Toast.LENGTH_SHORT).show();
                    }
                    if (xinlvCall.getHeartRateList().size() == 0) {
                        t0.setText("");
                        t1.setText("");
                        t2.setText("");
                        t3.setText("");
                        t4.setText("");
                        t5.setText("");
                    } else {
                        Log.e("isNull", "为0的时候执行了？？");
                        for (int t = 0; t < xinlvCall.getHeartRateList().size(); t++) {
                            //  point2.add(dataRes.getDailySport().get(i).getHeartRateTable().get(t).getRate());
                            if (t == 3) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                Log.e("Xinlv", times.get(0).substring(11, 16)+"times.get(0).substring(11, 16)");
                                t0.setText(times.get(0).substring(11, 16));
                            }
                            if (t == 8) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                t1.setText(times.get(1).substring(11, 16));
                            }
                            if (t == 13) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                t2.setText(times.get(2).substring(11, 16));
                            }
                            if (t == 18) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                t3.setText(times.get(3).substring(11, 16));
                            }
                            if (t == 23) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                t4.setText(times.get(4).substring(11, 16));
                            }
                            if (t == 28) {
                                times.add(xinlvCall.getHeartRateList().get(t).getSportTime());
                                t5.setText(times.get(5).substring(11, 16));
                            }
                        }
                    }

                /*    drawline.addView(new draw2(getApplicationContext(),thirdData));*/

                } else {
                    h.sendEmptyMessageDelayed(1, 1);
                }
            }


        }
    };


    @Override
    protected void onStart() {
        super.onStart();

    }

    public class draw2 extends View {
        int x = 0;
        int y = 0;
        List<xinlvtuCallBack.HeartRateListBean> HeartRate;
        public draw2(Context context, final List<xinlvtuCallBack.HeartRateListBean> HeartRate) {
            super(context);
            setWillNotDraw(false);
            this.HeartRate=  HeartRate;
          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                  *//*      h.sendEmptyMessage(3);*//*
                    }
                }
            }).start();*/
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e("0000", x + "开始画了---22222" + y + "开始画了");
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#FFF68F"));
            paint.setStrokeWidth((float) 5.0);
            paint.setTextSize(20);

            Log.e("aaaa", HeartRate.size() + "onDraw" +"");
            if (HeartRate.size() > 1) {
                Path path = new Path();
                path.moveTo(0, gap);
                for (int i = 0; i < HeartRate.size(); i++) {
                    path.lineTo(i * (width / 30), (gap / 4 + gap) - ((HeartRate.get(i).getHeartRate() * (gap / 4 + gap)) / 250));
                /*    paint.setShader(new LinearGradient(i *( width/30), (gap - (datas.get(i)*gap))/150, i *( width/30), gap, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));*/
                }
                //结束的点
                path.lineTo((HeartRate.size() - 1) * (width / 30)+300, gap);
                canvas.drawPath(path, paint);
            }
            invalidate();
        }
    }


}
