package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.AccountManagerJson;
import com.headlth.management.entity.AdvancedPrescription;
import com.headlth.management.utils.ShareUitls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_advancedprescription)//复用我的处方布局
public class AdvancedPrescriptionActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private Button view_publictitle_back;

    @ViewInject(R.id.activity_advancedprescription_text)
    private TextView activity_advancedprescription_text;
    @ViewInject(R.id.activity_advancedprescription_next)
    private Button activity_advancedprescription_next;

    @ViewInject(R.id.activity_advancedprescription_third)
    private LinearLayout activity_advancedprescription_third;
    @ViewInject(R.id.activity_advancedprescription_Individualization)
    private Button activity_advancedprescription_Individualization;
    @ViewInject(R.id.activity_advancedprescription_current)
    private Button activity_advancedprescription_current;


    private String UID;
    private AccountManagerJson accountManagerJson;
    public static com.headlth.management.clenderutil.WaitDialog waitDialog;
    public static Activity activity;


    AdvancedPrescription advancedPrescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        activity=this;
        UID = ShareUitls.getString(AdvancedPrescriptionActivity.this, "UID", "0");
        advancedPrescription=(AdvancedPrescription)getIntent().getSerializableExtra("advancedPrescription");
        initialize();

    }

    private void initialize() {
        view_publictitle_title.setText("提示");
        activity_advancedprescription_text.setText(advancedPrescription.Message);
        if(advancedPrescription.Status.equals("600")){//进阶到第二阶段
            activity_advancedprescription_current.setVisibility(View.VISIBLE);
            activity_advancedprescription_next.setVisibility(View.GONE);
        }
        if(advancedPrescription.Status.equals("700")){
            activity_advancedprescription_current.setVisibility(View.VISIBLE);
            activity_advancedprescription_next.setVisibility(View.GONE);
            activity_advancedprescription_current.setText("推荐慢性病运动计划");
        }
    }

    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_advancedprescription_next,R.id.activity_advancedprescription_Individualization,R.id.activity_advancedprescription_current


    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_advancedprescription_next:
                Intent intent = new Intent(AdvancedPrescriptionActivity.this, QuestionnaireActivity.class);
                intent.putExtra("flag", "simple");
                intent.putExtra("QuestionnaireID", advancedPrescription.QuestionnaireID);
                intent.putExtra("PlanNameID", advancedPrescription.PlanNameID);
                startActivity(intent);

                break;
            case R.id.activity_advancedprescription_Individualization://第三阶段


                // finish();
                break;
            case R.id.activity_advancedprescription_current://第三阶段
                if(advancedPrescription.Status.equals("600")){//进阶到第二阶段
                    startActivity(new Intent(activity, SearchBlueActivity.class));
                }
                if(advancedPrescription.Status.equals("700")){
                    startActivity(new Intent(activity, RecommendPrescriptionListActivity.class));
                }
                finish();
                break;
        }
    }




}
