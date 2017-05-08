package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.AdvancedPrescriptionActivity;
import com.headlth.management.activity.ChuFangDtail;
import com.headlth.management.activity.ExercisePlanActivity;
import com.headlth.management.activity.ExerciseRecordActivity;
import com.headlth.management.activity.NewChuFang;
import com.headlth.management.activity.PrescriptionDetailsActivity;
import com.headlth.management.activity.StrengthSportActivity;
import com.headlth.management.entity.AdvancedPrescription;
import com.headlth.management.entity.MaidongDataJson;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.entity.newChuFangCallback;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.scan.Search;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2016/9/21.
 */
@ContentView(R.layout.fragment_maidong)
public class MaidongFragment extends BaseFragment {
    @ViewInject(R.id.fragment_maidong_recommend_exercise_plan)
    private LinearLayout fragment_maidong_recommend_exercise_plan;
    @ViewInject(R.id.fragment_maidong_recommend_exercise_plan_text)
    private TextView fragment_maidong_recommend_exercise_plan_text;

    @ViewInject(R.id.fragment_maidong_exercise_plan_youyang)
    private LinearLayout fragment_maidong_exercise_plan_youyang;
    @ViewInject(R.id.fragment_maidong_exercise_plan_strenrth)
    private LinearLayout fragment_maidong_exercise_plan_strenrth;

    @ViewInject(R.id.fragment_maidong_exercise_plan_today_exercise_plan)
    private LinearLayout fragment_maidong_exercise_plan_today_exercise_plan;
    @ViewInject(R.id.fragment_maidong_exercise_plan_strength_layout)
    private LinearLayout fragment_maidong_exercise_plan_strength_layout;

    @ViewInject(R.id.fragment_maidong_exercise_plan_youyang_layout)
    private LinearLayout fragment_maidong_exercise_plan_youyang_layout;
    @ViewInject(R.id.fragment_maidong_exercise_plan_exercise_chievement)
    private LinearLayout fragment_maidong_exercise_plan_exercise_chievement;

    @ViewInject(R.id.fragment_maidong_exercise_plan)
    private LinearLayout fragment_maidong_exercise_plan;

    @ViewInject(R.id.fragment_maidong_exercise_plan_exercise_have_layout)
    private LinearLayout fragment_maidong_exercise_plan_exercise_have_layout;

    @ViewInject(R.id.fragment_maidong_exercise_plan_exercise_no_layout)
    private LinearLayout fragment_maidong_exercise_plan_exercise_no_layout;

    @ViewInject(R.id.fragment_maidong_strength_go)
    private Button fragment_maidong_strength_go;


    @ViewInject(R.id.fragment_maidong_todayachievement)
    private LinearLayout fragment_maidong_todayachievement;
    @ViewInject(R.id.fragment_maidong_averageheart)
    private TextView fragment_maidong_averageheart;

    @ViewInject(R.id.fragment_textView_jichuyouyangchufang)
    private TextView fragment_textView_jichuyouyangchufang;
    @ViewInject(R.id.fragment_textView_jichuliliangchufang)
    private TextView fragment_textView_jichuliliangchufang;


    @ViewInject(R.id.fragment_maidong_alltime)
    private TextView fragment_maidong_alltime;
    @ViewInject(R.id.fragment_maidong_allcar)
    private TextView fragment_maidong_allcar;
    @ViewInject(R.id.fragment_maidong_youyang_degree)
    private TextView fragment_maidong_youyang_degree;
    @ViewInject(R.id.fragment_maidong_yougang_validtime)
    private TextView fragment_maidong_yougang_validtime;
    @ViewInject(R.id.fragment_maidong_yougang_validhreart)
    private TextView fragment_maidong_yougang_validhreart;
    @ViewInject(R.id.fragment_maidong_strength_degree)
    private TextView fragment_maidong_strength_degree;
    @ViewInject(R.id.fragment_maidong_strength_validtime)
    private TextView fragment_maidong_strength_validtime;
    @ViewInject(R.id.fragment_maidong_strength_place)
    private TextView fragment_maidong_strength_place;
    Gson g = new Gson();
    private String UID, SPID;
    private String WebSiteSPStartMsg = "";
    int ValidTime;
    private List<PrescriptionJson.PrescriptionClass> PrescriptionList;
    private newChuFangCallback newChuFang;
    private chufangCallBack chufang;
    private String ResultJWT;
    private boolean newChufang = true;//查看处方   是否刷新 （首页有数据有更新就刷新）
    private BottomMenuDialog bottomMenuDialog;




    ///MdMobileService.ashx?do=GetPlanClassRequest  UserID  主页接口
    @SuppressLint("ValidFragment")
    public MaidongFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PrescriptionList = new ArrayList<>();
        UID = ShareUitls.getString(getActivity(), "UID", "0");
        ResultJWT = ShareUitls.getString(getActivity(), "ResultJWT", "0");
        Log.i("strstrstrstr","MaidongFragmentonCreateView");
        return x.view().inject(this, inflater, container);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("strstrstrstr","MaidongFragmentononActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
        getPrescriptionHttp();
    }

    private void getPrescriptionHttp() {

        String prescriptionlist = ShareUitls.getString(getActivity(), "prescriptionlist", "");//
        String questionnaire = ShareUitls.getString(getActivity(), "questionnaire", "0");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）

        PrescriptionJson prescriptionJson = null;
        try {
            prescriptionJson = g.fromJson(prescriptionlist, PrescriptionJson.class);
        } catch (Exception e) {
        }
        Log.i("XXXXXXXXXXBBSSSS", "推荐处是否刷新" + questionnaire);
        if (prescriptionlist.length() == 0 || questionnaire.equals("1") || prescriptionJson.PlanNameList == null) {


            //Log.i("XXXXXXXXXXBBSSSS","推荐处方刷新"+(++i));
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetPlanNameListRequest");
            params.addBodyParameter("ResultJWT", ResultJWT);
            params.addBodyParameter("UID", UID);
            params.addBodyParameter("PlanClassID", "0");
            HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            ShareUitls.putString(getActivity(), "prescriptionlist", response);//

                            Log.i("VVVVVVAA", "" + response.toString());
                            PrescriptionJson prescriptionJson = g.fromJson(response, PrescriptionJson.class);
                            setPrescription(prescriptionJson);
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {


                        }
                    }

            );
        } else {
            setPrescription(prescriptionJson);
        }

    }

    private void setPrescription(PrescriptionJson prescriptionJson) {
        PrescriptionList.clear();
        if (prescriptionJson.IsSuccess.equals("true")) {
            ShareUitls.putString(getActivity(), "questionnaire", "0");//首页界面是否重新刷新  指为不需要刷新(是否有答完题)
            PrescriptionList.addAll(prescriptionJson.PlanNameList);
            Log.i("VVVVVVCC", "" + PrescriptionList.size());
            setPrescriptionListData();
        }
    }

    private void setPrescriptionListData() {
        fragment_maidong_recommend_exercise_plan.removeAllViews();
        for (int i = 0; i < PrescriptionList.size(); i++) {
            final PrescriptionJson.PrescriptionClass prescriptionClass = PrescriptionList.get(i);

            View view_fragment_maidong_recommend_exercise_plan = LayoutInflater.from(getActivity()).inflate(R.layout.view_maidong_recommend_exercise_plan, null);
            LinearLayout view_fragment_maidong_recommend_exercise_plan_layout = (LinearLayout) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_layout);
            TextView view_fragment_maidong_recommend_exercise_plan_type = (TextView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_type);
            ImageView view_fragment_maidong_recommend_exercise_plan_charge = (ImageView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_charge);
            ImageView view_fragment_maidong_recommend_exercise_plan_goal_icon = (ImageView) view_fragment_maidong_recommend_exercise_plan.findViewById(R.id.view_fragment_maidong_recommend_exercise_plan_goal_icon);
            view_fragment_maidong_recommend_exercise_plan_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("SSAAAA", prescriptionClass.PlanClassID);
                    Intent intent = new Intent(getActivity(), PrescriptionDetailsActivity.class);
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
            fragment_maidong_recommend_exercise_plan.addView(view_fragment_maidong_recommend_exercise_plan);
        }
    }

    @Event(value = {R.id.fragment_maidong_more_project,
            R.id.fragment_maidong_todayachievement, R.id.fragment_maidong_exercise_plan_youyang,
            R.id.fragment_maidong_exercise_plan_strenrth, R.id.fragment_maidong_exercise_plan_youyang_detials_layout
            , R.id.fragment_maidong_yougang_go, R.id.fragment_maidong_exercise_plan_strength_detials_layout
            , R.id.fragment_maidong_strength_go, R.id.fragment_maidong_exercise_plan_today_exercise_plan
    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_maidong_more_project:
                startActivity(new Intent(getActivity(), ExercisePlanActivity.class));
                break;
            case R.id.fragment_maidong_todayachievement:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                startActivity(new Intent(getActivity(), ExerciseRecordActivity.class).putExtra("time", format.format(new Date())));
                break;
            case R.id.fragment_maidong_yougang_go:
                //   startActivity(new Intent(getActivity(), Search.class));
                if (WebSiteSPStartMsg.length() == 0) {
                    if (ValidTime >= 3600) {
                        showUpdateDialog("您今天已达到锻炼目标，过长时间运动容易产生疲劳，注意休息!", true);
                    } else
                        getAdvancedPrescriptionRequest(getActivity());
                } else {

                    bottomMenuDialog = new BottomMenuDialog.Builder(getActivity())
                            .addMenu(WebSiteSPStartMsg, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomMenuDialog.dismiss();
                                }
                            }).create();

                    bottomMenuDialog.show();
                }


                break;
            case R.id.fragment_maidong_strength_go:
                // if(InternetUtils.internet(getActivity())) {
                startActivity(new Intent(getActivity(), StrengthSportActivity.class));
                // }
                break;
            case R.id.fragment_maidong_exercise_plan_youyang_detials_layout:
                youyangPrescriptionDetials();
                break;
            case R.id.fragment_maidong_exercise_plan_strength_detials_layout:
                //  getActivity().startActivity(new Intent(getActivity(), StrengthSportActivity.class));
                break;
            case R.id.fragment_maidong_exercise_plan_today_exercise_plan:

                newChuFang();


                break;

        }
    }


    private void newChuFang() {
        if (newChuFang != null && newChuFang.getStatus() == 1) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), NewChuFang.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("newChuFang", newChuFang);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);

        } else {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPrescriptionNewRequest");
            params.addBodyParameter("ResultJWT", ResultJWT);
            params.addBodyParameter("UID", UID);
            HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VVVVVVVVVVVVVV", "" + response.toString());
                            newChuFang = g.fromJson(response.toString(), newChuFangCallback.class);
                            if (newChuFang.getStatus() == 1) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), NewChuFang.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("newChuFang", newChuFang);
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "对不起，您还没有运动处方可查看!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {

                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }

            );
        }


    }

    private void youyangPrescriptionDetials() {
        if (chufang != null && chufang.getStatus() == 1 && !newChufang) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ChuFangDtail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("chufang", chufang);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPrescriptionRequest");
            params.addBodyParameter("ResultJWT", ResultJWT);
            params.addBodyParameter("UID", UID);
            HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            newChufang = false;
                            Log.e("ffff", response.toString());
                            chufang = g.fromJson(response.toString(), chufangCallBack.class);
                            if (chufang.getStatus() == 1) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), ChuFangDtail.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("chufang", chufang);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {


                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }

            );
        }


    }


    String maidong = "";

    private void getData() {
        String todaydata = ShareUitls.getString(getActivity(), "todaydata", "{}");//
        maidong = ShareUitls.getString(getActivity(), "maidong", "0");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）
        MaidongDataJson maidongDataJson = null;

        Log.i("XXXXAAXXXXXX", maidong + "       " + todaydata);
        try {
            maidongDataJson = g.fromJson(todaydata.toString(), MaidongDataJson.class);
        } catch (Exception e) {



        }

        if (maidongDataJson == null || maidong.equals("1")) {
            newChufang = true;

            Log.i("XXXXXXXXXX", "我的处方刷新");
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetIndexInfoRequest");
            params.addBodyParameter("UID", UID);
            params.addBodyParameter("ResultJWT", ResultJWT);
            HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            ShareUitls.putString(getActivity(), "todaydata", response);//

                            Log.i("VVVVVVVVVaaa", "" + UID + "   " + response.toString());
                            MaidongDataJson maidongDataJson = g.fromJson(response.toString(), MaidongDataJson.class);
                            Log.i("VVVVVVVVVCCC", "" + maidongDataJson.toString());
                            if (maidongDataJson != null && maidongDataJson.UserIndexListV2_9 != null) {
                                setMaidongData(maidongDataJson);
                            } else {
                                Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                                fragment_maidong_exercise_plan_exercise_chievement.setVisibility(View.GONE);
                                fragment_maidong_exercise_plan_youyang_layout.setVisibility(View.GONE);
                                fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.GONE);
                                fragment_maidong_recommend_exercise_plan_text.setVisibility(View.GONE);
                                ((TextView) getActivity().findViewById(R.id.statechange)).setText("推荐锻炼计划");

                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {


                        }
                    }

            );
        } else {
            setMaidongData(maidongDataJson);
        }
    }

    static boolean startSport = true;//避免重复点击

    public static void getAdvancedPrescriptionRequest(final Activity activity) {
        if (startSport) {
            String SPID = ShareUitls.getString(activity, "SPID", "");//获取当前处方的SPID（本页刷新 和 答完问卷后会更新该参数 （QuestionnaireAdapter））
            if (SPID.length() != 0) {
                startSport = false;
                RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetAdvancedPrescriptionRequest");
                params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "0"));
                params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
                params.addBodyParameter("SPID", SPID);
                HttpUtils.getInstance(activity).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                startSport = true;
                                AdvancedPrescription advancedPrescription = new Gson().fromJson(response.toString(), AdvancedPrescription.class);
                                Log.i("//第三阶段", "  " + response.toString());
                                //   Intent intent = new Intent(activity, TEXT.class);
                                //  intent.putExtra("jsonObject1", response.toString());


   /*   advancedPrescription.Status

                    =0,
            *Normal = 100,//正常运动
            Intervallong = 200,//最近两周没有运动
            Advanced = 300,//进阶
            SmallVolidNum = 400,//次数不够或者两次运动间隔大于4天
            FallBack = 500,//回退
            ThreeStage=600,//第三阶段结束
   *
   * */
                                switch (advancedPrescription.Status) {
                                    case "100":
                                       /* if (PrescriptionDetailsActivity.activity != null) {//如果是从问卷结果页面进入的 去掉 处方详情界面
                                            PrescriptionDetailsActivity.activity.finish();
                                        }*/
                                        activity.startActivity(new Intent(activity, Search.class));
                                        break;
                                    default:
                                     /*   if (PrescriptionDetailsActivity.activity != null) {
                                            PrescriptionDetailsActivity.activity.finish();
                                        }*/
                                        activity.startActivity(new Intent(activity, AdvancedPrescriptionActivity.class).putExtra("advancedPrescription", advancedPrescription));
                                        break;
                              /*  case "200":
                                    // break;
                                case "300":
                                    //   break;
                                case "400":
                                    //   break;
                                case "500":
                                    //  break;
                                case "600":

                                    break;*/
                                }


                            }

                            @Override
                            public void onErrorResponse(Throwable ex) {
                                startSport = true;

                            }
                        }

                );
            }
        } else {

        }
    }


    private void setMaidongData(MaidongDataJson maidongDataJson) {


        if (maidongDataJson.Status == 1) {

            MaidongDataJson.UserIndexList UserIndexList = maidongDataJson.UserIndexListV2_9;

            WebSiteSPStartMsg = UserIndexList.WebSiteSPStartMsg;

            // WebSiteSPStartMsg="过期了";
            String oldremindtime = ShareUitls.getString(getActivity(), "remindtime", "");
            String newremindtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            ;

            if (UserIndexList.ValidTime >= 3600) {//当日有效运动时间超过60分钟 提醒一次
                if (!oldremindtime.equals(newremindtime)) {//两个时间不一致 提醒
                    ShareUitls.putString(getActivity(), "remindtime", newremindtime);
                    showUpdateDialog("您今天有效运动已达60分钟，非常棒!", false);

                }

            }


            if (maidong.equals("1")) {//数据是否需要重新保存
                ShareUitls.putString(getActivity(), "my", "1");//我界面是否需要刷新
                ShareUitls.putString(getActivity(), "SPID", UserIndexList.SPID + "");
                SPID = UserIndexList.SPID + "";
                ShareUitls.putString(getActivity(), "PPID", UserIndexList.PPID + "");
                ShareUitls.putString(getActivity(), "PlanNameID", UserIndexList.PlanNameID + "");
                ShareUitls.putString(getActivity(), "UBound", UserIndexList.UBound + "");
                ShareUitls.putString(getActivity(), "LBound", UserIndexList.LBound + "");
                ShareUitls.putString(getActivity(), "Target", (UserIndexList.target / 60) + "");
                ShareUitls.putString(getActivity(), "IsPlay", UserIndexList.IsPlay + "");
            }

            if (UserIndexList.IsSportStart.equals("true")) {
                fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.VISIBLE);
                fragment_maidong_exercise_plan_youyang_layout.setVisibility(View.VISIBLE);
                fragment_maidong_youyang_degree.setText("第" + UserIndexList.SportFinishedDays + "次");
                fragment_maidong_yougang_validtime.setText((UserIndexList.ValidTime / 60) + "'" + (UserIndexList.ValidTime % 60));
                fragment_maidong_yougang_validhreart.setText(UserIndexList.LBound + "--" + UserIndexList.UBound);
                fragment_textView_jichuyouyangchufang.setText(UserIndexList.SportPrescriptionTitle);
            }else {
                fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.GONE);
                fragment_maidong_exercise_plan_youyang_layout.setVisibility(View.GONE);

            }

            if (UserIndexList.IsShowTodayPowerTrainPlan.equals("true")) {//
                fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.VISIBLE);
                fragment_maidong_exercise_plan_strength_layout.setVisibility(View.VISIBLE);
                if (UserIndexList.IsPlay.equals("false")) {
                    // Log.i("DDDDDDDDDDDD",UserIndexList.PowerTrainDuration+"");
                    if (UserIndexList.PowerTrainDuration != 0) {
                        fragment_maidong_strength_go.setText("您今天已完成力量运动");
                    } else {
                        fragment_maidong_strength_go.setText("您今天没有力量运动");
                    }
                    fragment_maidong_strength_go.setClickable(false);
                } else {
                    fragment_textView_jichuliliangchufang.setText(UserIndexList.PowerPrescriptionTitle);
                    fragment_maidong_strength_degree.setText("第" + UserIndexList.PowerFinishedDays + "次");
                    fragment_maidong_strength_place.setText(UserIndexList.BodyPart);
                    fragment_maidong_strength_validtime.setText((UserIndexList.PowerTrainDuration / 60) + "'" + (UserIndexList.PowerTrainDuration % 60));
                    fragment_maidong_strength_go.setText("开始锻炼");
                    fragment_maidong_strength_go.setClickable(true);
                    try {
                        //  String[] vlist = g.fromJson(UserIndexList.vlist, String[].class);
                        if (maidong.equals("1")) {
                            ShareUitls.putString(getActivity(), "vlist", UserIndexList.vlist.get(UserIndexList.PowerFinishedCount) + "");
                        }
                    } catch (IndexOutOfBoundsException i) {
                        try {
                            ShareUitls.putString(getActivity(), "vlist", "1001");
                        } catch (IndexOutOfBoundsException j) {
                        }
                    }
                }
            }else {
                fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.GONE);
                fragment_maidong_exercise_plan_strength_layout.setVisibility(View.GONE);
            }
            Log.i("VVVVVVVVVCCC", UserIndexList.IsShowTodayScore);
            ValidTime = UserIndexList.ValidTime;//全局化有效运动时间 用于 开始有氧运动的时候 提醒

            if (UserIndexList.IsShowTodayPowerTrainPlan.equals("false") && UserIndexList.IsSportStart.equals("false")) {
                fragment_maidong_exercise_plan_exercise_no_layout.setVisibility(View.VISIBLE);
            } else {
                fragment_maidong_exercise_plan_exercise_no_layout.setVisibility(View.GONE);
            }
            if (UserIndexList.IsShowTodayScore.equals("true")) {//显示今日成绩
                fragment_maidong_todayachievement.setVisibility(View.VISIBLE);
                fragment_maidong_averageheart.setText(UserIndexList.HeartRateAvg);
                fragment_maidong_alltime.setText((UserIndexList.Duration / 60) + "'" + (UserIndexList.Duration % 60));
                fragment_maidong_allcar.setText(UserIndexList.BigCar);
                Log.i("VVVVVVVVVCCC", UserIndexList.IsShowTodayScore);
            }else {
                fragment_maidong_todayachievement.setVisibility(View.GONE);
            }
            ShareUitls.putString(getActivity(), "maidong", "0");//首页界面不需要刷新
        } else if (maidongDataJson.Status == 2) {

            fragment_maidong_exercise_plan_exercise_chievement.setVisibility(View.GONE);
            fragment_maidong_exercise_plan_youyang_layout.setVisibility(View.GONE);
            fragment_maidong_exercise_plan_strength_layout.setVisibility(View.GONE);
            fragment_maidong_exercise_plan_today_exercise_plan.setVisibility(View.GONE);
            fragment_maidong_recommend_exercise_plan_text.setVisibility(View.GONE);
            ((TextView) getActivity().findViewById(R.id.statechange)).setText("推荐锻炼计划");
        } else {
            Toast.makeText(getActivity(), "加载异常", Toast.LENGTH_SHORT).show();
        }
    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    /**
     * 弹出提示对话框
     */
    private void showUpdateDialog(String message, boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示:");
        builder.setMessage(message);

        if (flag) {
            builder.setNegativeButton("暂不运动",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.setPositiveButton("继续运动",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getAdvancedPrescriptionRequest(getActivity());
                        }
                    });
        } else {
            builder.setNegativeButton("我已知晓",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
        builder.setCancelable(false);
        builder.show();
    }
}

