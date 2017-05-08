package com.headlth.management.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.QuestionnaireAdapter;
import com.headlth.management.entity.PrescriptionDetails;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.entity.QuestionnaireGson;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
 * Created by abc on 2016/9/25.
 */
@ContentView(R.layout.activity_questionnaire)
public class  QuestionnaireActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;




    @ViewInject(R.id.activity_questionnaire_title_)
    private TextView activity_questionnaire_title_;
    @ViewInject(R.id.activity_questionnaire_listview)
    private ListView activity_questionnaire_listview;

    private List<QuestionnaireGson.Questionnaire> list;
    private QuestionnaireAdapter questionnaireAdapter;
    private Gson gson = new Gson();
    private PrescriptionJson.PrescriptionClass  prescription;
    private String UID;
 //   String PAY="0";
    public static Activity activity;
    String flag="";//用于判断是否是首次答题开具处方 还是进阶第二 第三阶段()
    String   QuestionnaireID="0";

    String   PlanNameID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//获取拍照权限
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }*/

        x.view().inject(this);

        view_publictitle_title.setText("问卷调查");


        activity=this;
        UID = ShareUitls.getString(QuestionnaireActivity.this, "UID", "");
        flag=getIntent().getStringExtra("flag");

      if(flag.equals("all")){//首次开具处方 打全部题
          prescription = (PrescriptionJson.PrescriptionClass) getIntent().getSerializableExtra("prescription");
          QuestionnaireID=prescription.QuestionnaireID;
          PlanNameID =prescription.PlanNameID;;
    //      PAY=getIntent().getStringExtra("PAY");
      }else {//进阶  答部分题
          QuestionnaireID =getIntent().getStringExtra("QuestionnaireID");
          PlanNameID =getIntent().getStringExtra("PlanNameID");

      }


        getQuestion();
    }

    private void getQuestion() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetNewQuestionnaireRequest");
        params.addBodyParameter("QuestionnaireID",QuestionnaireID);
        params.addBodyParameter("UID",ShareUitls.getString(QuestionnaireActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(QuestionnaireActivity.this, "ResultJWT", "0"));
        Log.i("P1AAA", QuestionnaireID+"  "+ShareUitls.getString(QuestionnaireActivity.this, "UID", "") + ""+"'"+ShareUitls.getString(QuestionnaireActivity.this, "ResultJWT", "0"));
        HttpUtils.getInstance(QuestionnaireActivity.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("P1AAA", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("IsSuccess").equals("true")) {
                                Log.i("P2", "");
                                List<QuestionnaireGson.Questionnaire> QuestionList = new ArrayList<QuestionnaireGson.Questionnaire>();
                                JSONArray jsonArray = jsonObject.getJSONArray("QustionList");
                                Log.i("P3", jsonArray.length() + "");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    QuestionnaireGson.Questionnaire questionnaire = gson.fromJson(jsonArray.getString(i), QuestionnaireGson.Questionnaire.class);
                                    QuestionList.add(questionnaire);
                                }
                                questionnaireAdapter = new QuestionnaireAdapter(true,QuestionnaireActivity.this, QuestionList,PlanNameID,QuestionnaireID);
                                activity_questionnaire_listview.setAdapter(questionnaireAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                      /*  QuestionnaireGson questionnaireGson = gson.fromJson(response, QuestionnaireGson.class);
                        Log.i("WWWWWWAAA", questionnaireGson.toString());
                        if (questionnaireGson.IsSuccess.equals("true")) {

                            list = questionnaireGson.QuestionList;
                            Log.i("AAAAADDDDDAAA", list.size() + "     " + list.toString());
                            questionnaireAdapter = new QuestionnaireAdapter(QuestionnaireActivity.this, list);
                            activity_questionnaire_listview.setAdapter(questionnaireAdapter);


                        } else {
                            Toast.makeText(getApplicationContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
                        }*/
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );
      /*  Map<String, String> map = new HashMap<String, String>();
        map.put("QuestionnaireID",prescription.QuestionnaireID);
        Log.i("AAACC",prescription.QuestionnaireID);
       // map.put("UserNo",UID);
        HttpUtils.getInstance(this).sendRequest("", Constant.BASE_URL + "/MdMobileService.ashx?do=GetNewQuestionnaireRequest", map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAADDDDDAAA", ex.toString());
                        Toast.makeText(getApplicationContext(), "网络异常,加载数据失败", Toast.LENGTH_SHORT).show();

                    }
                }

        );*/
    }

    String str = "";

    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
        }
    }

    public static String getABC(String str) {
        String temp = "";
        switch (str) {
            case "1":
                temp = "A";
                break;
            case "2":
                temp = "B";
                break;
            case "3":
                temp = "C";
                break;
            case "4":
                temp = "D";
                break;
            case "5":
                temp = "E";
                break;
            case "6":
                temp = "F";
                break;
            case "7":
                temp = "G";
                break;
        }
        return temp;
    }


}
