package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.utils.ShareUitls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

/**
 * Created by abc on 2016/9/13.
 */
@ContentView(R.layout.activity_findpassword)
public class FindPassWordActivity extends BaseActivity {


    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @ViewInject(R.id.qqtext)
    private TextView qqtext;

    @ViewInject(R.id.activity_findpasswordet_bt_commit)
    private Button activity_findpasswordet_bt_commit;


    @ViewInject(R.id.activity_findpassword_phone)
    private EditText activity_findpassword_phone;

public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity=this;
        view_publictitle_title.setText("找回密码");
        String qq = getIntent().getStringExtra("qq");
        qqtext.setText(qq);
    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_findpasswordet_bt_commit})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_findpasswordet_bt_commit:
                String phone = activity_findpassword_phone.getText().toString();
                if (phone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (phone.length() != 11) {
                        Toast.makeText(getApplicationContext(), "手机号必须11位", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!Login.isMobile(phone)) {
                            Toast.makeText(getApplicationContext(), "手机号格式错误", Toast.LENGTH_SHORT).show();
                        } else {
                            getSMS(phone);
                        }
                    }
                }

                break;
        }
    }
    private void getSMS(final String phone) {
       Login. getSMS(this,phone, "1",new Login.SMSInterface() {
            @Override
            public void onResponse(String verify_code) {
                Intent intent = new Intent(FindPassWordActivity.this, SetPassWordActivity.class);
                intent.putExtra("verify_code", verify_code);
                intent.putExtra("flag", "UpdatePassword");
                intent.putExtra("phone", phone);
                ShareUitls.putString(FindPassWordActivity.this, "SMSTIME", new Date().getTime() + "");
                startActivity(intent);
            }
        });

    }
}
