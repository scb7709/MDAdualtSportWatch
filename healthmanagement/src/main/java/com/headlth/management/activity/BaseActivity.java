package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abc on 2016/7/11.
 */
public class BaseActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(BaseActivity.this).onAppStart();
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //InternetUtils.internet(this);

       /* if (ShareUitls.getString(this, "TODAY", "null") != null && !ShareUitls.getString(this, "TODAY", "null").equals(format.format(new Date()))) {
            Toast.makeText(this, "请重新登录", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            return;
        }else {*/
            InternetUtils. internet2(BaseActivity.this);

     //   }

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }
}
