package com.headlth.management.acs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.headlth.management.R;
import com.headlth.management.utils.InternetUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by abc on 2016/7/11.
 */
public class BaseActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

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

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {//大于4.4支持自定义状态栏颜色
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
            statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.title) );
            contentView.getChildAt(0).setFitsSystemWindows(true);
            contentView.addView(statusBarView, lp);
        }
    }
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
