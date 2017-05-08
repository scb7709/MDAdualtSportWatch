package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.MyPrescriptionDetialsAdapter;
import com.headlth.management.entity.MyPrescriptionDetialsAdvise;
import com.headlth.management.entity.MyPrescriptionDetialsData;
import com.headlth.management.entity.MyPrescriptionJson;
import com.headlth.management.entity.QuestionaireResultJson;
import com.headlth.management.scan.Search;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/11/22.
 */

@ContentView(R.layout.activity_myprescriptiondetials)
public class MyPrescriptionDetialsActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @ViewInject(R.id.view_myprescription_name)
    public TextView view_myprescription_name;
    @ViewInject(R.id.view_myprescription_starttime)
    public TextView view_myprescription_starttime;
    @ViewInject(R.id.view_myprescription_stoptime)
    public TextView view_myprescription_stoptime;
    @ViewInject(R.id.view_myprescription_view)
    public View view_myprescription_view;

    @ViewInject(R.id.view_myprescription_flag)
    public ImageView view_myprescription_flag;

    @ViewInject(R.id.activity_myprescriptiondetials_listview)
    private ListView activity_myprescriptiondetials_listview;
    private MyPrescriptionDetialsAdapter myPrescriptionDetialsAdapter;
    private List<MyPrescriptionDetialsAdvise> MyPrescriptionDetialsAdviseList;
    private List<MyPrescriptionDetialsData> MyPrescriptionDetialsDataList;
    private Intent intent;
    private MyPrescriptionJson.MyPrescription myPrescription;
    private String UID;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        intent = getIntent();
        myPrescription = (MyPrescriptionJson.MyPrescription) intent.getSerializableExtra("MyPrescription");
        init();
    }

    private void init() {
        view_publictitle_title.setText("运动计划详情");
        MyPrescriptionDetialsAdviseList = new ArrayList<>();
        MyPrescriptionDetialsDataList = new ArrayList<>();
        UID = ShareUitls.getString(MyPrescriptionDetialsActivity.this, "UID", "0");
        view_myprescription_view.setVisibility(View.GONE);
        view_myprescription_name.setText(myPrescription.Title);
        view_myprescription_starttime.setText(myPrescription.Prescription_Start.replace("T", " "));
        view_myprescription_stoptime.setText(myPrescription.Prescription_End.replace("T", " "));
        switch (myPrescription.PrescriptionState) {
            case "3":
                view_myprescription_flag.setImageResource(R.mipmap.icon_begin);
                break;
            case "1":
                view_myprescription_flag.setImageResource(R.mipmap.icon_no_begin);
                break;
            case "2":
                view_myprescription_flag.setImageResource(R.mipmap.icon_over);
                break;
        }

        getQuestionnairResultHttp();

    }


    private void getQuestionnairResultHttp() {
        Log.i("XXXXXXXXXXXAAA", myPrescription.toString());
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetQuestionnaireRequest");
        params.addBodyParameter("UID",UID);
        params.addBodyParameter("ResultJWT",ShareUitls.getString(MyPrescriptionDetialsActivity.this, "ResultJWT", "0"));
        //params.addBodyParameter("UserID", UID);
        params.addBodyParameter("QuestionnaireID", myPrescription.QuestionnaireID);
        params.addBodyParameter("PlanNameID", myPrescription.PlanNameID);
        params.addBodyParameter("SPID", myPrescription.SPID);
        HttpUtils.getInstance(MyPrescriptionDetialsActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("XXXXXXXXXXXAAA", response);
                        QuestionaireResultJson questionaireResultJson = new Gson().fromJson(response, QuestionaireResultJson.class);
                        if (questionaireResultJson.Status.equals("1")&&questionaireResultJson.prescriptionList!=null) {

                            QuestionaireResultJson.PrescriptionList prescriptionList = questionaireResultJson.prescriptionList;
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("每周运动时间", prescriptionList.TotalWeekSportDuration + " 分钟"));
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("每周运动次数", prescriptionList.WeekSportDays + " 次"));
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("每次运动时间", prescriptionList.SportDurationOneTime + " 分钟"));
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("推荐运动项目", prescriptionList.SportModel));
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("运动心率下限", prescriptionList.LBound+"次/分钟"));
                            MyPrescriptionDetialsDataList.add(new MyPrescriptionDetialsData("运动心率上限", prescriptionList.UBound+"次/分钟"));
                            if (prescriptionList.ListRemark != null) {
                                for (QuestionaireResultJson.PrescriptionList.ListRemarkClass ListRemark : prescriptionList.ListRemark) {
                                    MyPrescriptionDetialsAdvise myPrescriptionDetialsAdvise = new MyPrescriptionDetialsAdvise(ListRemark.Type, ListRemark.Title, ListRemark.Content);
                                    MyPrescriptionDetialsAdviseList.add(myPrescriptionDetialsAdvise);
                                }
                            }

                            myPrescriptionDetialsAdapter = new MyPrescriptionDetialsAdapter(MyPrescriptionDetialsActivity.this, MyPrescriptionDetialsAdviseList, MyPrescriptionDetialsDataList);
                            activity_myprescriptiondetials_listview.setAdapter(myPrescriptionDetialsAdapter);
                        }else {
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        //  Toast.makeText(QuestionnaireResultActivity.this, "网络异常", Toast.LENGTH_SHORT).show();


                    }
                }

        );


    }


    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;

        }
    }
}
