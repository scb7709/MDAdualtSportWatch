package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.MaidongDataJson;
import com.headlth.management.entity.PayOrderNumber;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.entity.QuestionaireResultJson;
import com.headlth.management.fragment.MaidongFragment;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/9/27.
 */
@ContentView(R.layout.activity_questionnaireresult)
public class QuestionnaireResultActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_questionnaireresult_commit)
    private Button activity_questionnaireresult_commit;

    @ViewInject(R.id.activity_questionnaireresult_pay_layout)
    private LinearLayout activity_questionnaireresult_pay_layout;
    @ViewInject(R.id.activity_questionnaireresult_pay_text)
    private TextView activity_questionnaireresult_pay_text;
    @ViewInject(R.id.activity_questionnaireresult_nopay_text)
    private TextView activity_questionnaireresult_nopay_text;

    @ViewInject(R.id.activity_questionnaireresult_nopay_im)
    private ImageView activity_questionnaireresult_nopay_im;
    @ViewInject(R.id.activity_questionnaireresult_nopay_layout)
    private LinearLayout activity_questionnaireresult_nopay_layout;

    private PrescriptionJson.PrescriptionClass prescription;
    private String UID, PlanNameID, QuestionnaireID;
    private String PAY = "";
    private Intent intent;
    private PopupWindow popupWindow;
    private PayOrderNumber payOrderNumber;
    public static Activity activity;
    //  QuestionaireResultJson.PrescriptionList prescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("问卷结果");
        activity = this;
        initialize();

    }

    private void initialize() {
        intent = getIntent();
        UID = ShareUitls.getString(activity, "UID", "0");
        PAY = intent.getStringExtra("PAY");
        PlanNameID = intent.getStringExtra("PlanNameID");
        QuestionnaireID = intent.getStringExtra("QuestionnaireID");
        // prescriptionList=(QuestionaireResultJson.PrescriptionList )intent.getSerializableExtra("prescriptionList");
        ShareUitls.putString(QuestionnaireResultActivity.this, "OrderNO", "");//微信支付回调后用来检查后台是否支付成功 此处置为空
        ShareUitls.putString(QuestionnaireResultActivity.this, "PAY", PAY);

        Log.i("WWWWWWWWQQQ", PAY + "            " + QuestionnaireID + "            " + PlanNameID + "            " + UID);
        getQuestionnairResultHttp();
        getMaidongFragmentData();//刷新首页
    }

    @Override
    protected void onStart() {
        super.onStart();
        PAY = ShareUitls.getString(QuestionnaireResultActivity.this, "PAY", PAY);//完成支付后改标会变为1
        Log.i("WWWWWWWWQQQ", PAY);
        if (PAY.equals("1")) {
            activity_questionnaireresult_pay_text.setMovementMethod(ScrollingMovementMethod.getInstance());
            activity_questionnaireresult_pay_text.setMaxLines(100);
            activity_questionnaireresult_nopay_im.setVisibility(View.GONE);
            activity_questionnaireresult_nopay_layout.setVisibility(View.GONE);
            activity_questionnaireresult_commit.setText("开始锻炼");
        } else {
            activity_questionnaireresult_pay_text.setMaxLines(4);
        }
    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_questionnaireresult_commit, R.id.activity_questionnaireresult_lockquestionnaire})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_questionnaireresult_commit:
                if (PAY.equals("0")) {
                    getOrderID();
                } else {
                    //  String    PowerFinishedCount=ShareUitls.getString(QuestionnaireResultActivity.this,"PowerFinishedCount","");
                    this.view = view;
                }
                break;

            case R.id.activity_questionnaireresult_lockquestionnaire:
                Intent intent = new Intent();
                intent.setClass(QuestionnaireResultActivity.this, QuestionnaireAnswerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getOrderID() {
        if (payOrderNumber == null) {

            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostOrderInfoRequest");

            params.addBodyParameter("ResultJWT", ShareUitls.getString(QuestionnaireResultActivity.this, "ResultJWT", "0"));
            params.addBodyParameter("QuestionnaireID", QuestionnaireID);
            params.addBodyParameter("UID", UID);
            params.addBodyParameter("PID", PlanNameID);
            HttpUtils.getInstance(QuestionnaireResultActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            payOrderNumber = gson.fromJson(response, PayOrderNumber.class);
                            if (payOrderNumber.IsSuccess.equals("true")) {
                                Intent intent = new Intent(QuestionnaireResultActivity.this, PayActivity.class);
                                ShareUitls.putString(QuestionnaireResultActivity.this, "OrderNO", payOrderNumber.OrderNO);//微信支付回调校验订单

                                intent.putExtra("PName", payOrderNumber.PName);
                                intent.putExtra("OrderNO", payOrderNumber.OrderNO);
                                intent.putExtra("Amount", payOrderNumber.Amount);
                                intent.putExtra("PlanNameID", PlanNameID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuestionnaireResultActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {

                            Toast.makeText(QuestionnaireResultActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            return;


                        }
                    }

            );
        } else {
            Intent intent = new Intent(QuestionnaireResultActivity.this, PayActivity.class);
            intent.putExtra("PName", payOrderNumber.PName);
            intent.putExtra("OrderNO", payOrderNumber.OrderNO);
            intent.putExtra("Amount", payOrderNumber.Amount);
            intent.putExtra("PlanNameID", PlanNameID);


            startActivity(intent);

        }

    }

    private void getQuestionnairResultHttp() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetQuestionnaireRequest");

        params.addBodyParameter("UID", ShareUitls.getString(QuestionnaireResultActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(QuestionnaireResultActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("QuestionnaireID", QuestionnaireID);
        params.addBodyParameter("SPID", ShareUitls.getString(QuestionnaireResultActivity.this, "SPID", "0"));
        params.addBodyParameter("PlanNameID", PlanNameID);

        Log.i("XXXXXXXX", PlanNameID + "  " + QuestionnaireID + "   " + ShareUitls.getString(QuestionnaireResultActivity.this, "UID", "") + "  " + ShareUitls.getString(QuestionnaireResultActivity.this, "SPID", "0"));

        HttpUtils.getInstance(QuestionnaireResultActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("XXXXXXXXXXXAAA", response);
                        QuestionaireResultJson questionaireResultJson = new Gson().fromJson(response, QuestionaireResultJson.class);
                        Log.i("XXXXXXXXXXXDDDD", questionaireResultJson.toString());
                        if (questionaireResultJson.Status.equals("1")) {

                            QuestionaireResultJson.PrescriptionList prescriptionList = questionaireResultJson.prescriptionList;
                            Log.i("XXXXXXXXXXXSSSSss", prescriptionList.toString());
                            String text1 = "";
                            String text2 = "";
                            String text3 = "";

                            text1 = "每周运动运动时长：  " + prescriptionList.TotalWeekSportDuration + " 分钟\n"
                                    + "每周运动天数：   " + prescriptionList.WeekSportDays + "天\n"
                                    + "每次运动时长：   " + prescriptionList.SportDurationOneTime + " 分钟\n"
                                    + "运动模式：   " + prescriptionList.SportModel + "\n"
                                    + "心率区间：   " + prescriptionList.LBound + "--" + prescriptionList.UBound + "\n\n\n\n"
                                    + "结果:    " + prescriptionList.Content + "\n\n"
                            ;
                            Log.i("XXXXXXXXXdddddd", text1);
                            if (prescriptionList.ListRemark != null) {
                                for (QuestionaireResultJson.PrescriptionList.ListRemarkClass ListRemark : prescriptionList.ListRemark) {
                                    text2 += "评价类型:   " + ListRemark.Type + "\n"
                                            + "评语：   " + ListRemark.Title + "\n"
                                            + "建议：   " + ListRemark.Content + "\n\n";


                                }
                            }
                            Log.i("XXXXXXXXXXXSSSSA", text1 + text2);
                            activity_questionnaireresult_pay_text.setText(text1 + text2);

                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        //  Toast.makeText(QuestionnaireResultActivity.this, "网络异常", Toast.LENGTH_SHORT).show();


                    }
                }

        );


    }

    //点击开始运动的对话框
    private void getAdadultFragmentStartSportDalog() {
        View view = null;
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        int hei = d.getHeight();
        // flag=true;
        view = LayoutInflater.from(QuestionnaireResultActivity.this).inflate(R.layout.dialog_adadultfragmentstartsport, null);
        View view2 = view.findViewById(R.id.AdadultFragmentStartSportDalog_view2);
        Button youyangSport = (Button) view.findViewById(R.id.AdadultFragmentStartSportDalog_youyang);
        Button liliangSport = (Button) view.findViewById(R.id.AdadultFragmentStartSportDalog_liliang);


        if (IsStrengthStart && IsSportStart) {
            hei *= 0.3;
        } else {
            hei *= 0.2;
            view2.setVisibility(View.GONE);
            if (!IsSportStart) {
                youyangSport.setVisibility(View.GONE);
            }
            if (!IsStrengthStart) {
                liliangSport.setVisibility(View.GONE);
            }
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
        if (IsSportStart) {
            youyangSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WebSiteSPStartMsg.length() == 0) {
                        MaidongFragment.getAdvancedPrescriptionRequest(QuestionnaireResultActivity.this);
                    } else {
                        bottomMenuDialog = new BottomMenuDialog.Builder(QuestionnaireResultActivity.this)
                                .addMenu(WebSiteSPStartMsg, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomMenuDialog.dismiss();
                                    }
                                }).create();

                        bottomMenuDialog.show();
                    }
                    popupWindow.dismiss();
                }
            });
        }
        if (IsStrengthStart) {

            liliangSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  if (PrescriptionDetailsActivity.activity != null) {
                        PrescriptionDetailsActivity.activity.finish();
                    }*/
                    startActivity(new Intent(QuestionnaireResultActivity.this, StrengthSportActivity.class));
                    popupWindow.dismiss();
                }
            });

        }

        popupWindow = new PopupWindow(view, (int) (d.getWidth() * 0.6), hei, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


    }

    String maidong = "";
    Gson g = new Gson();
    private String WebSiteSPStartMsg = "";
    boolean IsStrengthStart = false;
    boolean IsSportStart = false;
    View view;
    private BottomMenuDialog bottomMenuDialog;


    private void getMaidongFragmentData() {
        String maidong = ShareUitls.getString(QuestionnaireResultActivity.this, "maidong", "1");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）
        if ( maidong.equals("1")) {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetIndexInfoRequest");
            params.addBodyParameter("UID", UID);
            params.addBodyParameter("ResultJWT", ShareUitls.getString(QuestionnaireResultActivity.this, "ResultJWT", "0"));
            HttpUtils.getInstance(QuestionnaireResultActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VVVVVVVVVaaa", "" + UID + "   " + response.toString());
                            MaidongDataJson maidongDataJson = g.fromJson(response.toString(), MaidongDataJson.class);
                            Log.i("VVVVVVVVVCCC", "" + maidongDataJson.toString());
                            if (maidongDataJson != null && maidongDataJson.UserIndexList != null) {
                                setMaidongData(maidongDataJson,response);
                            } else {
                                Toast.makeText(QuestionnaireResultActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {


                        }
                    }

            );
        }
    }


    private void setMaidongData(MaidongDataJson maidongDataJson,String response) {

        if (maidongDataJson.Status == 1) {
            MaidongDataJson.UserIndexList UserIndexList = maidongDataJson.UserIndexList;
            WebSiteSPStartMsg = UserIndexList.WebSiteSPStartMsg;
                ShareUitls.putString(QuestionnaireResultActivity.this, "my", "1");//我界面是否需要刷新
                ShareUitls.putString(QuestionnaireResultActivity.this, "SPID", UserIndexList.SPID + "");
            // SPID = UserIndexList.SPID + "";
                ShareUitls.putString(QuestionnaireResultActivity.this, "PPID", UserIndexList.PPID + "");
                ShareUitls.putString(QuestionnaireResultActivity.this, "PlanNameID", UserIndexList.PlanNameID + "");
                ShareUitls.putString(QuestionnaireResultActivity.this, "UBound", UserIndexList.UBound + "");
                ShareUitls.putString(QuestionnaireResultActivity.this, "LBound", UserIndexList.LBound + "");
                ShareUitls.putString(QuestionnaireResultActivity.this, "Target", (UserIndexList.target) + "");
                ShareUitls.putString(QuestionnaireResultActivity.this, "IsPlay", UserIndexList.IsPlay + "");
               ShareUitls.putUserInformationWatch(activity, "", ( UserIndexList.target)+"", UserIndexList.UBound, UserIndexList.LBound);//保存安静心率
            if (UserIndexList.IsShowTodayPowerTrainPlan.equals("true")) {//
                if (UserIndexList.IsPlay.equals("true")) {
                    IsStrengthStart = true;
                    try {
                        ShareUitls.putString(QuestionnaireResultActivity.this, "vlist", UserIndexList.vlist.get(UserIndexList.PowerFinishedCount) + "");
                    } catch (IndexOutOfBoundsException i) {
                        try {
                            ShareUitls.putString(QuestionnaireResultActivity.this, "vlist", "1001");
                        } catch (IndexOutOfBoundsException j) {
                        }
                    }
                }
            }

            if (UserIndexList.IsSportStart.equals("true")) {
                IsSportStart = true;
            }
            if (IsSportStart || IsStrengthStart) {
                getAdadultFragmentStartSportDalog();
            } else {
                Toast.makeText(QuestionnaireResultActivity.this, "当前处方未开始", Toast.LENGTH_SHORT).show();
            }
            ShareUitls.putString(QuestionnaireResultActivity.this, "todaydata", response);//
            ShareUitls.putString(QuestionnaireResultActivity.this, "maidong", "0");

        }


    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

}