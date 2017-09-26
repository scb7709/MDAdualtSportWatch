package com.headlth.management.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.QuestionnaireAnswerAdapter;
import com.headlth.management.entity.QuestionnaireAnswerGson;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_questionnaire)//
public class QuestionnaireAnswerActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @ViewInject(R.id.activity_questionnaire_listview)
    private ListView activity_questionnaire_listview;

    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        view_publictitle_title.setText("问卷详情");

        UID = ShareUitls.getString(QuestionnaireAnswerActivity.this, "UID", "0");

        init();

    }

    private void init() {
        getRequest();
    }

    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;

        }
    }
    private void getRequest() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostQuestionnaireAnswerRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(QuestionnaireAnswerActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID",UID);
        params.addBodyParameter("SPID",ShareUitls.getString(QuestionnaireAnswerActivity.this, "SPID", "0"));

        Log.i("QSSSQQQWWAA",ShareUitls.getString(QuestionnaireAnswerActivity.this, "ResultJWT", "0")+"    "+UID+"   "+ShareUitls.getString(QuestionnaireAnswerActivity.this, "SPID", "0"));
        HttpUtils.getInstance(QuestionnaireAnswerActivity.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("QSSSQQQWW",response);
                        QuestionnaireAnswerGson questionnaireAnswerGson=new Gson().fromJson(response,QuestionnaireAnswerGson.class);
                        QuestionnaireAnswerAdapter questionnaireAnswerAdapter=new QuestionnaireAnswerAdapter(QuestionnaireAnswerActivity.this,questionnaireAnswerGson.QuestionnaireAnswer.QuestionAnswerList);
                        activity_questionnaire_listview.setAdapter(questionnaireAnswerAdapter);




                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                    }
                }

        );

    }



}
