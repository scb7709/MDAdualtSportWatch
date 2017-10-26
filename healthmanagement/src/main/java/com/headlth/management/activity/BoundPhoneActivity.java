package com.headlth.management.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
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
    private RelativeLayout view_publictitle_back;

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
    private Map<String, String> map = new HashMap<String, String>();

    private String phone, pwd;
    private String flag;//是三方验证登录的还是手机号登录的
    private String FlagActivity;//是那个界面过来的
    Map<String, String> phoneAndverify_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();

    }

    private void initialize() {
        phoneAndverify_code = new HashMap<>();
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        FlagActivity = intent.getStringExtra("FlagActivity");
        Log.i("FlagActivity", FlagActivity);
        if (flag.equals("other")) {
            map = (Map<String, String>) (getIntent().getSerializableExtra("map"));
        } else {
            phone = intent.getStringExtra("phone");
            pwd = intent.getStringExtra("pwd");
        }
        view_publictitle_title.setText("绑定手机号");
    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_boundphone_comfire, R.id.activity_boundphone_commit})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                Log.i("FlagActivityt", FlagActivity);
                if (FlagActivity.equals("HomeActivity")) {
                    Log.i("FlagActivitytr", FlagActivity);
                    Intent intent = new Intent(BoundPhoneActivity.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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

        Login.getSMS(this, phone, "0", new Login.SMSInterface() {
            @Override
            public void onResponse(String Verify_code) {
              /*  if(phoneAndverify_code.get(phone)!=null){
                    phoneAndverify_code.remove(phone);
                }*/
                phoneAndverify_code.put(phone,Verify_code);
                verify_code = Verify_code;
                ShareUitls.putString(BoundPhoneActivity.this, "SMSTIME", new Date().getTime() + "");
                Toast.makeText(BoundPhoneActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
            }
        });

    /*    RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetSendMessageRequest");
        params.addBodyParameter("IsForgotPw", "0");
        params.addBodyParameter("Code", Encryption.decode(Encryption.encodeByMD5(new StringBuffer(phone).reverse().toString()).toString()));
        params.addBodyParameter("Mobile", phone);
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("正在获取手机验证码,请稍后...", params, true, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        activity_boundphone_commit.setClickable(true);
                        Log.e("getSMSjson", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String IsSuccess = jsonObject.getString("IsSuccess");
                            String Status = jsonObject.getString("Status");
                            if (IsSuccess.equals("true")) {
                                verify_code = Status;
                                ShareUitls.putString(BoundPhoneActivity.this, "SMSTIME", new Date().getTime() + "");
                                Toast.makeText(BoundPhoneActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BoundPhoneActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                           *//*     if (Status.equals("3")) {
                                    Toast.makeText(BoundPhoneActivity.this, "该手机号已经被注册", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(BoundPhoneActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                                }*//*
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

        );*/
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
                if (!Login.isMobile(phone)) {
                    Toast.makeText(BoundPhoneActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    CheckExist(phone);

                }
            }
        }
    }


    //验证码校验
    private void VerifyVerifycode() {
        String verifycode = activity_boundphone_verifycode.getText().toString();
        String phone = activity_boundphone_phone.getText().toString();
        if (phone.length() != 0) {
            if (verify_code.length() != 0) {
                if (verifycode.length() != 0) {
                    //verify_code
                    if (verifycode.equals(verify_code)) {
                        long OLDSMSTIME = Long.parseLong(ShareUitls.getString(BoundPhoneActivity.this, "SMSTIME", 0 + ""));
                        if (OLDSMSTIME != 0) {
                            long NOWSMSTIME = new Date().getTime();
                            if ((double) ((NOWSMSTIME - OLDSMSTIME) / 60000) <= 15) {
                                BoundPhone();

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
        params.addBodyParameter("ResultJWT", ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));
        params.addBodyParameter("Mobile", phone);

        params.addBodyParameter("NickName", "0");
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        // activity_boundphone_commit.setClickable(true);
                        Log.e("getSMSjson", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("Status");
                            if (Status.equals("2")) {
                                activity_boundphone_comfire.setClickable(false);
                                activity_boundphone_comfire.setTextColor(Color.parseColor("#999999"));
                                time.start();
                                //  activity_boundphone_commit.setClickable(false);
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

    private void BoundPhone() {
        final String phone=activity_boundphone_phone.getText().toString();
        if (!Login.isMobile(phone)) {
            Toast.makeText(BoundPhoneActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneAndverify_code.get(phone)==null){
            Toast.makeText(BoundPhoneActivity.this, "该手机号还未获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!verify_code.equals(phoneAndverify_code.get(phone))){
            Toast.makeText(BoundPhoneActivity.this, "手机号和验证码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserMobileBindingRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));
        params.addBodyParameter("Mobile", phone);
        params.addBodyParameter("Pwd", "123456");
        HttpUtils.getInstance(BoundPhoneActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("Status");
                            if (Status.equals("2")) {
                                Toast.makeText(BoundPhoneActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();

                                if (flag.equals("other")) {
                                    HttpUtils.getInstance(BoundPhoneActivity.this).otherRegister(map, "BoundPhoneActivity");
                                } else {
                                    Login.phoneLogin(BoundPhoneActivity.this, phone, pwd, "BoundPhoneActivity");
                                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (FlagActivity.equals("HomeActivity")) {
                Intent intent = new Intent(BoundPhoneActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
