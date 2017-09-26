package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.ExerciseRecordJson;
import com.headlth.management.entity.shouyeCallBack;
import com.headlth.management.entity.xinlvtuCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by abc on 2016/9/25.
 */
@ContentView(R.layout.activity_exerciserecordold)
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


    @ViewInject(R.id.fragment_maidong_heartrate_drawline)
    private SuitLines suitLines;

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
    int minHeartRate, maxHeartRate;

    private xinlvtuCallBack xinlvCall;
    private String tempData;
    private Gson g = new Gson();
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;
    private String UID;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        tempData = getIntent().getStringExtra("time");
        activity_exerciserecord_time.setText(tempData);
        initialize();

    }

    private void initialize() {
        view_publictitle_title.setText("锻炼记录");
        UID = ShareUitls.getString(activity, "UID", "0");
        ResultJWT = ShareUitls.getString(activity, "ResultJWT", "0");
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity);
        getData(tempData);
        //
    }


    @Event(value = {R.id.view_publictitle_back, R.id.activity_exerciserecord_layout})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                ShareUitls.putString(activity, "CLICKDADE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                finish();
                break;
            case R.id.activity_exerciserecord_layout:
                // calendarListener();
                startActivity(new Intent(activity, CalendarActivity.class));
                break;

        }
    }


    public void back(View v) {
        finish();
    }

    List<String> times = new ArrayList<String>();

    private void makeLine(xinlvtuCallBack xinlvtuCallBack) {

        List<com.headlth.management.entity.xinlvtuCallBack.HeartRateListBean> heartRateListBeanList = xinlvtuCallBack.getHeartRateList();

        List<Unit> lines = new ArrayList<>();
        for (int i = 0; i < heartRateListBeanList.size(); i++) {
            lines.add(new Unit(heartRateListBeanList.get(i).getHeartRate() - 50, ""));
        }
        suitLines.setLineForm(true);
        int[] colors = new int[2];
        suitLines.anim();
        colors[0] = Color.parseColor("#ff4763");
        colors[1] = Color.parseColor("#b51225");
        suitLines.setDefaultOneLineColor(colors);
        suitLines.setLineType(SuitLines.SEGMENT);
        suitLines.feedWithAnim(lines);

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


    private void xinlv(final String time) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostHeartRateRequest");
        params.addBodyParameter("UID", UID);
        params.addBodyParameter("ResultJWT", ResultJWT);
        params.addBodyParameter("SportTime", time);
        Log.e("xinlvtu", "xinlv" + ResultJWT + "  " + time);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        waitDialog.dismissDialog();
                        Log.e("xinlvtu", "xinlv" + response.toString());
                        xinlvCall = g.fromJson(response.toString(), xinlvtuCallBack.class);
                        if (xinlvCall.getStatus() == 1) {
                            makeLine(xinlvCall);

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

    private void getData(final String monthDay) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostChangeCalenderRequest");
        params.addBodyParameter("UID", UID);
        params.addBodyParameter("ResultJWT", ResultJWT);
        params.addBodyParameter("SportTime", monthDay);
        HttpUtils.getInstance(activity).sendRequestRequestParams("正在加载,请稍后...", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        waitDialog.dismissDialog();
                        //  Log.i("fffffffffWWWW", UID + "" + monthDay + "'   " + response.toString());

                        ExerciseRecordJson exerciseRecordJson = g.fromJson(response.toString(), ExerciseRecordJson.class);
                        if (exerciseRecordJson.Status == 1) {

                            ExerciseRecordJson.UserSportListBean userSportListBean = exerciseRecordJson.UserSportList.get(0);
                            Log.i("fffffffffAAAA", userSportListBean.toString());
                            activity_exerciserecord_strength_validtime.setText((userSportListBean.PowerTrainDuration / 60) + "'" + (userSportListBean.PowerTrainDuration % 60) + "");
                            activity_exerciserecord_yougang_validhreart.setText(userSportListBean.minHeartRate + "--" + userSportListBean.maxHeartRate);
                            activity_exerciserecord_yougang_validtime.setText((userSportListBean.ValidTime / 60) + "'" + (userSportListBean.ValidTime % 60) + "");
                            activity_exerciserecord_allcar.setText(userSportListBean.KCal);
                            activity_exerciserecord_alltime.setText((userSportListBean.Duration / 60) + "'" + (userSportListBean.Duration % 60));
                            activity_exerciserecord_averageheart.setText(userSportListBean.HeartRateAvg);

                            activity_exerciserecord_textView_jichuyouyangchufang.setText(userSportListBean.SportPrescriptionTitle);
                            activity_exerciserecord_textView_jichustrengthchufang.setText(userSportListBean.PowerPrescriptionTitle);
                            activity_exerciserecord_youyang_degree.setText("第" + userSportListBean.SportFinishedDays + "次");
                            activity_exerciserecord_strength_degree.setText("第" + userSportListBean.PowerFinishedDays + "次");
                            activity_exerciserecord_yougang_validheart.setText(userSportListBean.BodyPart);


                            minHeartRate = userSportListBean.minHeartRate;
                            maxHeartRate = userSportListBean.maxHeartRate;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            ShareUitls.putString(activity, "CLICKDADE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
