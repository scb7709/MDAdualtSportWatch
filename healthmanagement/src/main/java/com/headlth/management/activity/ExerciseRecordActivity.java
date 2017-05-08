package com.headlth.management.activity;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.clenderutil.BackYearMonth;
import com.headlth.management.clenderutil.DateUtil;
import com.headlth.management.clenderutil.PreYearMonth;
import com.headlth.management.clenderutil.yearMonth;
import com.headlth.management.entity.ExerciseRecordJson;
import com.headlth.management.entity.copy;
import com.headlth.management.entity.copy1;
import com.headlth.management.entity.copy2;
import com.headlth.management.entity.riliCallBack;
import com.headlth.management.entity.shouyeCallBack;
import com.headlth.management.entity.xinlvtuCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/9/25.
 */
@ContentView(R.layout.activity_exerciserecord)
public class ExerciseRecordActivity extends BaseActivity {


    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;




    @ViewInject(R.id.activity_exerciserecord_calendar)
    private ImageButton activity_exerciserecord_calendar;
    @ViewInject(R.id.activity_exerciserecord_averageheart)
    private TextView activity_exerciserecord_averageheart;
    @ViewInject(R.id.activity_exerciserecord_alltime)
    private TextView activity_exerciserecord_alltime;
    @ViewInject(R.id.activity_exerciserecord_allcar)
    private TextView activity_exerciserecord_allcar;
    @ViewInject(R.id.activity_exerciserecord_youyang_degree)
    private TextView activity_exerciserecord_youyang_degree;
    @ViewInject(R.id.activity_exerciserecord_yougang_validtime)
    private TextView activity_exerciserecord_yougang_validtime;
    @ViewInject(R.id.activity_exerciserecord_yougang_validhreart)
    private TextView activity_exerciserecord_yougang_validhreart;
    @ViewInject(R.id.activity_exerciserecord_strength_degree)
    private TextView activity_exerciserecord_strength_degree;
    @ViewInject(R.id.activity_exerciserecord_strength_validtime)
    private TextView activity_exerciserecord_strength_validtime;
    @ViewInject(R.id.activity_exerciserecord_youyang_validheart)
    private TextView activity_exerciserecord_yougang_validheart;

    @ViewInject(R.id.activity_exerciserecord_textView_jichustrengthchufang)
    private TextView activity_exerciserecord_textView_jichustrengthchufang;
    @ViewInject(R.id.activity_exerciserecord_textView_jichuyouyangchufang)
    private TextView activity_exerciserecord_textView_jichuyouyangchufang;



    @ViewInject(R.id.activity_exerciserecord_time)
    private TextView activity_exerciserecord_time;
    @ViewInject(R.id.drawline)
    private RelativeLayout drawline;
    @ViewInject(R.id.qujian)
    private RelativeLayout qujian;
    @ViewInject(R.id.up)
    private TextView up;
    @ViewInject(R.id.low)
    private TextView low;
    @ViewInject(R.id.t0)
    private TextView t0;
    @ViewInject(R.id.t1)
    private TextView t1;
    @ViewInject(R.id.t2)
    private TextView t2;
    @ViewInject(R.id.t3)
    private TextView t3;
    @ViewInject(R.id.t4)
    private TextView t4;
    @ViewInject(R.id.t5)
    private TextView t5;
    private xinlvtuCallBack xinlvCall;
    private String tempData;
    private Gson g = new Gson();
    private Intent intent;

    private yearMonth logEntity;
    private PreYearMonth preYearMonth;
    private BackYearMonth backYearMonth;

    private static com.headlth.management.clenderutil.WaitDialog waitDialog;
    private String UID;
    int minHeartRate, maxHeartRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        tempData = getIntent().getStringExtra("time");
        activity_exerciserecord_time.setText(tempData);
        initialize();

    }

    private void initialize() {
        view_publictitle_title.setText("锻炼记录");
        UID = ShareUitls.getString(ExerciseRecordActivity.this, "UID", "0");
        ResultJWT= ShareUitls.getString(ExerciseRecordActivity.this, "ResultJWT", "0");
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(ExerciseRecordActivity.this);
        rili2(tempData);
      //
    }


    @Event(value = {R.id.view_publictitle_back, R.id.activity_exerciserecord_layout})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_exerciserecord_layout:
                calendarListener();
                break;

        }
    }

    private void calendarListener() {
        waitDialog.setMessage("正在加载,请稍后...");
        waitDialog.showDailog();
        String year = DateUtil.getYear() + "-" + getNum2(DateUtil.getMonth());
        Log.e("ppp", year + "当月");
        int overYear;
        String preMonth;
        int preOverYear;
        if ((DateUtil.getMonth() + 1) > 12) {
            overYear = DateUtil.getYear() + 1;
        } else {
            overYear = DateUtil.getYear();
        }
        if ((DateUtil.getMonth() - 1) == 0) {
            preMonth = "12";
            preOverYear = DateUtil.getYear() - 1;
            Log.e("ppp", "-preOverYear" + (DateUtil.getYear() - 1) + "-1");
        } else {
            if (DateUtil.getMonth() - 1 == 10 || DateUtil.getMonth() - 1 == 11) {
                preMonth = "" + (DateUtil.getMonth() - 1);
                preOverYear = DateUtil.getYear();
            } else {
                preMonth = "0" + (DateUtil.getMonth() - 1);
                preOverYear = DateUtil.getYear();
            }
        }
        hh.sendEmptyMessageDelayed(10, 1);
        rili(preOverYear + "-" + preMonth, -1);
        Log.e("ppp", preOverYear + "-" + preMonth + "-1");
        rili(DateUtil.getYear() + "-" + getNum2(DateUtil.getMonth() + 0), 0);
        Log.e("ppp", DateUtil.getYear() + "-" + getNum2(DateUtil.getMonth() + 0) + "+0");
        rili(overYear + "-" + getNum2(DateUtil.getMonth() + 1), 1);
        Log.e("ppp", overYear + "-" + getNum2(DateUtil.getMonth() + 1) + "+1");
    }

    public Handler hh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.e("pre", msg.obj.toString() + "");
                logEntity = g.fromJson(msg.obj.toString(), yearMonth.class);
                copy1.getInstance().SSSportDay = logEntity.getCalenderList();
                Log.e("llll", copy1.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "4" + "=====copy1");
                if (logEntity.getStatus() == 1) {
                    ShareUitls.putString(ExerciseRecordActivity.this, "user", msg.obj.toString());
                    Log.e("pre", logEntity.getCalenderList().toString() + "");
                } else {
                }
                return;
            }
            if (msg.what == -1) {
                preYearMonth = g.fromJson(msg.obj.toString(), PreYearMonth.class);
                copy.getInstance().SSSportDay = preYearMonth.getCalenderList();
                Log.e("llll", copy.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "5" + "=====copy");
                return;
            }
            if (msg.what == 1) {
                backYearMonth = g.fromJson(msg.obj.toString(), BackYearMonth.class);
                copy2.getInstance().SSSportDay = backYearMonth.getCalenderList();
                Log.e("llll", copy2.getInstance().SSSportDay.toString() + "复制的滑动新请求的日历数据获取" + "6" + "=====copy2");
                return;
            }

            if (msg.what == 10) {
                waitDialog.dismissDialog();
                if (logEntity != null && preYearMonth != null && backYearMonth != null) {
                    if (logEntity.getStatus() == 1 && preYearMonth.getStatus() == 1 && backYearMonth.getStatus() == 1) {
                        Intent intent = new Intent();
                        intent.setClass(ExerciseRecordActivity.this, CalendarActivityCalendar.class);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(ExerciseRecordActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                    return;


                } else {
                    hh.sendEmptyMessageDelayed(10, 1);
                }
            }
        }
    };
    private riliCallBack rili;

    private void rili(final String monthDay, final int i) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostCalenderRequest");
        params.addBodyParameter("ResultJWT",ResultJWT);
        params.addBodyParameter("UID",UID);
        params.addBodyParameter("SportTime",monthDay);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,false, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        waitDialog.dismissDialog();
                        Log.e("ffff", i + response.toString());
                        rili = g.fromJson(response.toString(), riliCallBack.class);
                        if (rili.getStatus() == 1) {
                            Message msg4 = hh.obtainMessage();
                            msg4.obj = response;
                            msg4.what = i;
                            hh.sendMessageDelayed(msg4, 1);
                        } else {
                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        waitDialog.dismissDialog();
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }


                }

        );
    }

    private String getNum2(int month) {
        if (month < 10) {
            return "0" + (month);
        }
        if (month > 12) {

            return "0" + (month - 12);
        }
        if (month == 0) {
            return "0" + 2;
        }
        return month + "";
    }


    //代码设置布局margin
    public static void setMargins(View v, int l, int t, int r, int b) {
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
                    int xp = maxHeartRate - minHeartRate;
                    //位置像素差
                    int xxp = (gap * minHeartRate - 50) / 200;
                    //高像素差
                    int xxp2 = (gap * xp) / 200;

                    setMargins(qujian, 0, 0, 0, xxp);

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
                    } else {
                        Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
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
                                Log.e("Xinlv", times.get(0).substring(11, 16) + "times.get(0).substring(11, 16)");
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
    public class draw2 extends View {
        int x = 0;
        int y = 0;
        List<xinlvtuCallBack.HeartRateListBean> HeartRate;

        public draw2(Context context, final List<xinlvtuCallBack.HeartRateListBean> HeartRate) {
            super(context);
            setWillNotDraw(false);
            this.HeartRate = HeartRate;
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

            Log.e("aaaa", HeartRate.size() + "onDraw" + "");
            if (HeartRate.size() > 1) {
                Path path = new Path();
                path.moveTo(0, gap);
                for (int i = 0; i < HeartRate.size(); i++) {
                    path.lineTo(i * (width / 30), (gap / 4 + gap) - ((HeartRate.get(i).getHeartRate() * (gap / 4 + gap)) / 250));
                /*    paint.setShader(new LinearGradient(i *( width/30), (gap - (datas.get(i)*gap))/150, i *( width/30), gap, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));*/
                }
                //结束的点
                path.lineTo((HeartRate.size() - 1) * (width / 30) + 300, gap);
                canvas.drawPath(path, paint);
            }
            invalidate();
        }
    }

    private void xinlv(final String time) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostHeartRateRequest");
        params.addBodyParameter("UID",UID);
        params.addBodyParameter("ResultJWT",ResultJWT);
        params.addBodyParameter("SportTime",time);
        HttpUtils.getInstance(ExerciseRecordActivity.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        waitDialog.dismissDialog();
                        Log.e("xinlvtu", "xinlv" + response.toString());
                        xinlvCall = g.fromJson(response.toString(), xinlvtuCallBack.class);
                        if (xinlvCall.getStatus() == 1) {
                            h.sendEmptyMessageDelayed(1, 1);
                        } else {
                            Toast.makeText(ExerciseRecordActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        waitDialog.dismissDialog();
                        Toast.makeText(ExerciseRecordActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    private shouyeCallBack shouye;
    private String ResultJWT;
    private void rili2(final String monthDay) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostChangeCalenderRequest");
        params.addBodyParameter("UID",UID);
        params.addBodyParameter("ResultJWT",ResultJWT);
        params.addBodyParameter("SportTime",monthDay);
        HttpUtils.getInstance(ExerciseRecordActivity.this).sendRequestRequestParams("正在加载,请稍后...", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        waitDialog.dismissDialog();
                        Log.i("fffffffffWWWW", UID+""+monthDay+"'   "+response.toString());

                        ExerciseRecordJson exerciseRecordJson = g.fromJson(response.toString(), ExerciseRecordJson.class);
                        if (exerciseRecordJson.Status == 1) {

                            ExerciseRecordJson.UserSportListBean userSportListBean = exerciseRecordJson.UserSportList.get(0);
                            Log.i("fffffffffAAAA", userSportListBean.toString());
                            activity_exerciserecord_strength_validtime.setText((userSportListBean.PowerTrainDuration / 60) + "'" + (userSportListBean.PowerTrainDuration % 60)+"");
                            activity_exerciserecord_yougang_validhreart.setText(userSportListBean.minHeartRate+"--"+userSportListBean.maxHeartRate);
                            activity_exerciserecord_yougang_validtime.setText((userSportListBean.ValidTime / 60) + "'" + (userSportListBean.ValidTime % 60)+"");
                            activity_exerciserecord_allcar.setText(userSportListBean.KCal);
                            activity_exerciserecord_alltime.setText((userSportListBean.Duration / 60) + "'" + (userSportListBean.Duration % 60));
                            activity_exerciserecord_averageheart.setText(userSportListBean.HeartRateAvg);

                            activity_exerciserecord_textView_jichuyouyangchufang.setText(userSportListBean.SportPrescriptionTitle);;
                            activity_exerciserecord_textView_jichustrengthchufang.setText(userSportListBean.PowerPrescriptionTitle);
                            activity_exerciserecord_youyang_degree.setText("第"+userSportListBean.SportFinishedDays+"次");
                            activity_exerciserecord_strength_degree.setText("第"+userSportListBean.PowerFinishedDays+"次");
                            activity_exerciserecord_yougang_validheart.setText(userSportListBean.BodyPart);


                            minHeartRate=userSportListBean.minHeartRate;
                            maxHeartRate=userSportListBean.maxHeartRate;
                            xinlv(tempData);

                        } else {
                            Toast.makeText(ExerciseRecordActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                         waitDialog.dismissDialog();
                       // Log.i("AAAAAAAAA", "LoginupToken");
                       // Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
}
