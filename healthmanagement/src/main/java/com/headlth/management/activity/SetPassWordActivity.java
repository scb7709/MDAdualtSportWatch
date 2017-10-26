package com.headlth.management.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.User;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.Encryption;
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

/**
 * Created by abc on 2016/9/13.
 */
@ContentView(R.layout.activity_setpassword)
public class SetPassWordActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_setpassword_reminder)
    private TextView activity_setpassword_reminder;

    @ViewInject(R.id.activity_setpassword_et_verifycode)
    private EditText activity_setpassword_et_verifycode;
    @ViewInject(R.id.activity_setpasswordet_pwd)
    private EditText activity_setpasswordet_pwd;
    @ViewInject(R.id.activity_setpasswordet_et_pwd_again)
    private EditText activity_setpasswordet_et_pwd_again;


    @ViewInject(R.id.activity_setpasswordet_et_pwd_layout)
    private LinearLayout activity_setpasswordet_et_pwd_layout;

    @ViewInject(R.id.activity_setpasswordet_et_pwd_line)
    private View activity_setpasswordet_et_pwd_line;


    @ViewInject(R.id.activity_setpassword_bt_comfire)
    private Button activity_setpassword_bt_comfire;
    @ViewInject(R.id.activity_setpasswordet_bt_commit)
    private Button activity_setpasswordet_bt_commit;

    private String flag = "";//判断是设置密码还是重置密码
    private String verify_code = "";
    private String phone = "";
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

    }

    private void initialize() {
        x.view().inject(this);
        activity_setpassword_et_verifycode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        verify_code = intent.getStringExtra("verify_code");
        //verify_code="1234";
        phone = intent.getStringExtra("phone");
        setDate();
    }

    private void setDate() {
        activity_setpassword_bt_comfire.setClickable(false);
        time.start();
        activity_setpassword_reminder.setText("验证码已经发送到" + phone);
        if (flag.equals("Register")) {
            activity_setpasswordet_pwd.setHint("密码");
            view_publictitle_title.setText("注册");
            activity_setpasswordet_et_pwd_layout.setVisibility(View.GONE);
            activity_setpasswordet_et_pwd_line.setVisibility(View.GONE);
            activity_setpasswordet_bt_commit.setText("下一步");
        } else {

            view_publictitle_title.setText("重置密码");
        }

    }


    @Event(value = {R.id.view_publictitle_back, R.id.activity_setpassword_bt_comfire, R.id.activity_setpasswordet_bt_commit})
    private void getEvent(View view) {
        switch (view.getId()) {

            case R.id.view_publictitle_back:
                finish();
                time.cancel();
                break;
            case R.id.activity_setpassword_bt_comfire:
                activity_setpassword_bt_comfire.setClickable(false);
                activity_setpassword_bt_comfire.setTextColor(Color.parseColor("#999999"));
                time.start();
                getSMS(phone);
                activity_setpassword_reminder.setText("正在给 " + phone + " 发送验证码");
              // activity_setpasswordet_bt_commit.setClickable(false);
                break;

            case R.id.activity_setpasswordet_bt_commit:
                if (flag.equals("Register")) {
                    register();
                } else {
                    updatePassword();
                }

                break;


        }
    }

    private void updatePassword() {
        String verifycode = activity_setpassword_et_verifycode.getText().toString();
        String password = activity_setpasswordet_pwd.getText().toString();
        String password_again = activity_setpasswordet_et_pwd_again.getText().toString();
        if (verifycode.equals(verify_code)) {
            long OLDSMSTIME = Long.parseLong(ShareUitls.getString(SetPassWordActivity.this, "SMSTIME", 0 + ""));
            if (OLDSMSTIME != 0) {
                long NOWSMSTIME = new Date().getTime();
                if ((double) ((NOWSMSTIME - OLDSMSTIME) / 60000) <= 15) {
                    if (password.length() >= 6 && password_again.length() >= 6) {
                        if (password.equals(password_again)) {
                            updatePassWordHttp(phone, password);
                        } else {
                            Toast.makeText(SetPassWordActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SetPassWordActivity.this, "密码至少6位", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SetPassWordActivity.this, "验证码已经失效,请重新获取", Toast.LENGTH_LONG).show();
                }

            } else {
                if (password.length() >= 6 && password_again.length() >= 6) {
                    if (password.equals(password_again)) {


                        updatePassWordHttp(phone, password);


                    } else {
                        Toast.makeText(SetPassWordActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SetPassWordActivity.this, "密码至少6位", Toast.LENGTH_LONG).show();
                }
            }


        } else {
            Toast.makeText(SetPassWordActivity.this, "验证码验证错误", Toast.LENGTH_LONG).show();
        }


    }

    private void register() {
        String verifycode = activity_setpassword_et_verifycode.getText().toString();
        String password = activity_setpasswordet_pwd.getText().toString();
        if (verifycode.equals(verify_code)) {
            long OLDSMSTIME = Long.parseLong(ShareUitls.getString(SetPassWordActivity.this, "SMSTIME", 0 + ""));
            if (OLDSMSTIME != 0) {
                long NOWSMSTIME = new Date().getTime();
                if ((double) ((NOWSMSTIME - OLDSMSTIME) / 60000) <= 15) {

                    if (password.length() >= 6) {

                        registerHttp(phone, password);

                    } else {
                        Toast.makeText(SetPassWordActivity.this, "密码至少6位", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SetPassWordActivity.this, "验证码已经失效,请重新获取", Toast.LENGTH_LONG).show();
                }
            } else {

                if (password.length() >= 6) {

                    registerHttp(phone, password);

                } else {
                    Toast.makeText(SetPassWordActivity.this, "密码至少6位", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(SetPassWordActivity.this, "验证码验证错误", Toast.LENGTH_LONG).show();
        }


    }

    TimeCount time = new TimeCount(60000, 1000);
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            activity_setpassword_bt_comfire.setText("重获验证码");
            activity_setpassword_bt_comfire.setClickable(true);
            activity_setpassword_bt_comfire.setTextColor(Color.parseColor("#ffad00"));
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            activity_setpassword_bt_comfire.setText("重获验证码(" + (millisUntilFinished / 1000) + ")");
            activity_setpassword_bt_comfire.setTextColor(Color.GRAY);

        }
    }

    private void updatePassWordHttp(final String phone, final String PWD) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserPasswordRequest");
        params.addBodyParameter("Mobile", phone);
        params.addBodyParameter("NewPwd", Encryption.decode(Encryption.encodeByMD5(PWD).toString()));
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("json", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Status = jsonObject.getString("Status");
                            if (Status.equals("0") || Status.equals("1")) {
                                Toast.makeText(SetPassWordActivity.this, "重置密码失败", Toast.LENGTH_SHORT).show();

                            }
                            if (Status.equals("3")) {
                                Toast.makeText(SetPassWordActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SetPassWordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                FindPassWordActivity.activity.finish();
                                finish();
                                time.cancel();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    private void registerHttp(final String phone, final String PWD) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserRegisterRequest");
        params.addBodyParameter("Mobile", phone);
        params.addBodyParameter("Pwd", Encryption.decode(Encryption.encodeByMD5(PWD).toString()));
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("json", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String UID = jsonObject.getString("Status");
                            if (UID.equals("0")) {
                                Toast.makeText(SetPassWordActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            } else if (UID.equals("2")) {
                                Toast.makeText(SetPassWordActivity.this, "手机号已存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SetPassWordActivity.this, "注册成功,请完善个人信息", Toast.LENGTH_SHORT).show();
                                ShareUitls.putString(SetPassWordActivity.this, "UID", UID);
                                ShareUitls.putLoginString(SetPassWordActivity.this, "loginFlag", "0");
                                ShareUitls.putString(SetPassWordActivity.this, "ResultJWT", jsonObject.getString("ResultJWT"));

                                User user = new User();
                                user.setUID(UID);
                                user.setLoginFlag("0");
                                user.setPhone(phone);
                                user.setPwd(PWD);
                                ShareUitls.putUser(SetPassWordActivity.this, user);
                                Intent intent = new Intent(SetPassWordActivity.this, CompleteInformationActivity.class);
                                intent.putExtra("flag", "no");
                                startActivity(intent);


                                HomeActivity.upToken(SetPassWordActivity.this);

                                finish();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(SetPassWordActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    private void getSMS(final String phone) {

        Login. getSMS(this,phone,flag.equals("Register")?"0":"1", new Login.SMSInterface() {
            @Override
            public void onResponse(String Verify_code) {

                activity_setpassword_reminder.setText("验证码已经发送到 " + phone);
                verify_code = Verify_code;
                ShareUitls.putString(SetPassWordActivity.this, "SMSTIME", new Date().getTime() + "");
                Toast.makeText(SetPassWordActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
