package com.headlth.management.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 2016/11/25.
 */
@ContentView(R.layout.activity_boundphone)//复用我的处方布局
public class BoundPhoneActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private Button view_publictitle_back;

    @ViewInject(R.id.activity_boundphone_commit)
    private Button activity_boundphone_commit;
    @ViewInject(R.id.activity_boundphone_comfire)
    private Button activity_boundphone_comfire;
    @ViewInject(R.id.activity_boundphone_phone)
    private EditText activity_boundphone_phone;
    @ViewInject(R.id.activity_boundphone_verifycode)
    private EditText activity_boundphone_verifycode;
    TimeCount time = new TimeCount(60000, 1000);

    private String verify_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();

    }

    private void init() {
        view_publictitle_title.setText("绑定手机号");
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_boundphone_comfire, R.id.activity_boundphone_commit})
    private void getEvent(View view) {
        switch (view.getId()) {

            case R.id.view_publictitle_back:
                finish();
                time.cancel();
                break;
            case R.id.activity_boundphone_comfire:
                VerifyPhoneNumber();
                break;

            case R.id.activity_boundphone_commit:
                VerifyVerifycode();
                break;


        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            activity_boundphone_comfire.setText("重获验证码");
            activity_boundphone_comfire.setClickable(true);
            activity_boundphone_comfire.setTextColor(Color.parseColor("#ffad00"));
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            activity_boundphone_comfire.setText("重获验证码(" + (millisUntilFinished / 1000) + ")");
            activity_boundphone_comfire.setTextColor(Color.GRAY);

        }
    }

    private void getSMS(final String phone) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetSendMessageRequest");
       /* params.addBodyParameter("ResultJWT",ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));*/
        params.addBodyParameter("Mobile", phone);
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("正在获取手机验证码,请稍后...", params,true, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        activity_boundphone_commit.setClickable(true);
                        Log.e("getSMSjson", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String IsSuccess = jsonObject.getString("IsSuccess");
                            if (IsSuccess.equals("true")) {
                                String Status = jsonObject.getString("Status");

                                verify_code = Status;
                                ShareUitls.putString(BoundPhoneActivity.this, "SMSTIME", new Date().getTime() + "");
                                Toast.makeText(BoundPhoneActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(BoundPhoneActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        activity_boundphone_commit.setClickable(true);
                        Toast.makeText(BoundPhoneActivity.this, "获取获取码失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    //手机号校验
    private void VerifyPhoneNumber() {
        String phone = activity_boundphone_phone.getText().toString();
        if (phone.length() == 0) {
            Toast.makeText(BoundPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (phone.length() != 11) {
                Toast.makeText(BoundPhoneActivity.this, "手机号必须11位", Toast.LENGTH_SHORT).show();
            } else {
                if (!isMobile(phone)) {
                    Toast.makeText(BoundPhoneActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    CheckExist(phone);

                }
            }
        }
    }

    //手机号判断
    public boolean isMobile(String mobiles) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    //验证码校验
    private void VerifyVerifycode() {
        String verifycode = activity_boundphone_verifycode.getText().toString();
        String phone = activity_boundphone_phone.getText().toString();
        if (phone.length() != 0) {
            if (verify_code.length() != 0) {
                if (verifycode.length() != 0) {
                    if (verifycode.equals(verify_code)) {
                        long OLDSMSTIME = Long.parseLong(ShareUitls.getString(BoundPhoneActivity.this, "SMSTIME", 0 + ""));
                        if (OLDSMSTIME != 0) {
                            long NOWSMSTIME = new Date().getTime();
                            if ((double) ((NOWSMSTIME - OLDSMSTIME) / 60000) <= 15) {
                                BoundPhone(phone);

                            } else {
                                Toast.makeText(BoundPhoneActivity.this, "验证码已经失效,请重新获取", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                    } else {
                        Toast.makeText(BoundPhoneActivity.this, "验证码验证错误", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BoundPhoneActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(BoundPhoneActivity.this, "请获取验证码", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(BoundPhoneActivity.this, "请输入手机号", Toast.LENGTH_LONG).show();
        }

    }

    private void CheckExist(final String phone) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=CheckExistRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));
        params.addBodyParameter("Mobile", phone);

        params.addBodyParameter("NickName", "0");
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        activity_boundphone_commit.setClickable(true);
                        Log.e("getSMSjson", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("Status");
                            if (Status.equals("2")) {
                                activity_boundphone_comfire.setClickable(false);
                                activity_boundphone_comfire.setTextColor(Color.parseColor("#999999"));
                                time.start();
                                activity_boundphone_commit.setClickable(false);
                                getSMS(phone);


                            } else if (Status.equals("1")) {
                                Toast.makeText(BoundPhoneActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BoundPhoneActivity.this, "获取获取码失败", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(BoundPhoneActivity.this, "获取获取码失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    private void BoundPhone(final String phone) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserMobileBindingRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));
        params.addBodyParameter("Mobile", phone);

        params.addBodyParameter("Pwd", "123456");
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("Status");
                            if (Status.equals("2")) {
                                Toast.makeText(BoundPhoneActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (Status.equals("1")) {
                                Toast.makeText(BoundPhoneActivity.this, "该手机号已被其他账户绑定", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BoundPhoneActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(BoundPhoneActivity.this, "获取获取码失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }
}
