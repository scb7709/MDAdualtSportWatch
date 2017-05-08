package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2017/4/17.
 */

@ContentView(R.layout.activity_recommendprescriptionlist)
public class RecommendPrescriptionListActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_recommendprescription_plan)
    private LinearLayout activity_recommendprescription_plan;


    private List<PrescriptionJson.PrescriptionClass> PrescriptionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getPrescriptionHttp();
        view_publictitle_title.setText("推荐锻炼计划");
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getPrescriptionHttp() {
        PrescriptionList = new ArrayList<>();
        //Log.i("XXXXXXXXXXBBSSSS","推荐处方刷新"+(++i));
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetPlanNameListRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(RecommendPrescriptionListActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(RecommendPrescriptionListActivity.this, "UID", "0"));
        params.addBodyParameter("PlanClassID", "0");
        HttpUtils.getInstance(RecommendPrescriptionListActivity.this).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VVVVVVAA", "" + response.toString());
                        PrescriptionJson prescriptionJson = new Gson().fromJson(response, PrescriptionJson.class);
                        PrescriptionList.addAll(prescriptionJson.PlanNameList);
                        setPrescriptionListData();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );


    }


    private void setPrescriptionListData() {
        activity_recommendprescription_plan.removeAllViews();
        for (int i = 0; i < PrescriptionList.size(); i++) {
            final PrescriptionJson.PrescriptionClass prescriptionClass = PrescriptionList.get(i);

            View view_fragment_maidong_recommend_exercise_plan = LayoutInflater.from(RecommendPrescriptionListActivity.this).inflate(R.layout.view_maidong_recommend_exercise_plan, null);
            LinearLayout view_fragment_maidong_recommend_exercise_plan_layout = (LinearLayout) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_layout);
            TextView view_fragment_maidong_recommend_exercise_plan_type = (TextView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_type);
            ImageView view_fragment_maidong_recommend_exercise_plan_charge = (ImageView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_charge);
            ImageView view_fragment_maidong_recommend_exercise_plan_goal_icon = (ImageView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_goal_icon);
            view_fragment_maidong_recommend_exercise_plan_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("SSAAAA", prescriptionClass.PlanClassID);
                    Intent intent = new Intent(RecommendPrescriptionListActivity.this, PrescriptionDetailsActivity.class);
                    intent.putExtra("prescription", prescriptionClass);
                    startActivity(intent);
                }
            });

            TextView view_fragment_maidong_recommend_exercise_plan_goal = (TextView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_goal);
            view_fragment_maidong_recommend_exercise_plan_type.setText(prescriptionClass.PlanName);
            view_fragment_maidong_recommend_exercise_plan_goal.setText(prescriptionClass.SportTarget);
            switch (prescriptionClass.IsToPay) {
                case 1://该处方属于用户已经支付
                    view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.icon_payment);
                    break;
                case 2://该处方属于用户未支付
                    view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.icon_payment_wait);
                    break;
                case 3://该处方不属于用户 且是收费的
                    view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.button_pay);
                    break;
                case 4://该处方免费属于该用户
                case 5://该处方 免费 不属于属于用户
                    view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.button_free);
                    break;


            }
            activity_recommendprescription_plan.addView(view_fragment_maidong_recommend_exercise_plan);
        }
    }


}
