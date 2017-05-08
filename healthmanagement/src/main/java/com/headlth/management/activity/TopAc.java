package com.headlth.management.activity;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/3/17.
 */
public class TopAc extends Activity {
    long temptime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - temptime > 2000) // 2s内再次选择back键有效
            {
                System.out.println(Toast.LENGTH_LONG);
                Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_SHORT).show();
                temptime = System.currentTimeMillis();
            } else {
                finish();
                //凡是非零都表示异常退出!0表示正常退出!
            }

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
