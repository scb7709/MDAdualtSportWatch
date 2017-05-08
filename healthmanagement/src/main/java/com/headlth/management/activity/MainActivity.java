package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.fragment.AnalizeFragment;
import com.headlth.management.fragment.MaidongCircleFragment;
import com.headlth.management.fragment.MaidongFragment;
import com.headlth.management.fragment.MyFragment;
import com.headlth.management.fragment.NewMaidongCircleFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/9/23.
 */
@ContentView(R.layout.activity_mainn)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.activity_main_tabs)
    private RadioGroup activity_main_tabs;

    @ViewInject(R.id.activity_main_maindong)
    private RadioButton activity_main_maindong;
    @ViewInject(R.id.main_messages)
    private RelativeLayout main_messages;
    @ViewInject(R.id.main_listCount)
    private TextView main_listCount;


    @ViewInject(R.id.main_share)
    private RelativeLayout main_share;


    @ViewInject(R.id.today)
    private TextView today;
    @ViewInject(R.id.statechange)
    private TextView statechange;
    @ViewInject(R.id.second)
    private TextView second;

    private App app;
    public static android.app.Activity Activity;
    private FragmentManager fragmentManager;
    private Gson g = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        Activity = this;
       // app = (App) getApplicationContext();// 获取应用程序全局的实例引用
       // app.activities.add(this); // 把当前Activity放入集合中
        fragmentManager = getSupportFragmentManager();
        activity_main_tabs.check(activity_main_maindong.getId());
        activity_main_tabs.setOnCheckedChangeListener(this);
        main_listCount.setText("99+");



       changeFragment(new MaidongFragment(), "MaidongFragment");
    }

    private void changeFragment(Fragment fragment, String tag) {
        //1.获取事务对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //2.切换内容的显示
        transaction.replace(R.id.activity_main_frame, fragment, tag);
//		//3.进站
//		transaction.addToBackStack(null);
        //4.提交事务
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_main_maindong:
                statechange.setText("迈动");
                today.setText("");
                second.setText("");
                main_share.setVisibility(View.GONE);
                main_messages.setVisibility(View.VISIBLE);
                changeFragment(new MaidongFragment(), "MaidongFragment");

                break;
            case R.id.activity_main_analyze:
                main_share.setVisibility(View.GONE);

                statechange.setText("");
                today.setTextSize(17);
                today.setTextColor(Color.parseColor("#3c3c3c"));
                today.setText("有效运动");
                second.setTextSize(13);
                second.setTextColor(Color.parseColor("#000000"));
                second.setText("卡路里");
                main_messages.setVisibility(View.GONE);
                changeFragment(new AnalizeFragment(), "AnalizeFragment");
                break;
            case R.id.activity_main_maidongcircle:
                main_share.setVisibility(View.VISIBLE);
                statechange.setText("迈动圈");
                today.setText("");
                second.setText("");
                main_messages.setVisibility(View.GONE);
                //  changeFragment(new MaidongCircleFragment(), "MaidongCircleFragment");
                changeFragment(new NewMaidongCircleFragment(), "NewMaidongCircleFragment");
                break;
            case R.id.activity_main_my:
                main_share.setVisibility(View.GONE);
                statechange.setText("我");
                today.setText("");
                second.setText("");
                main_messages.setVisibility(View.GONE);
                changeFragment(new MyFragment(), "MyFragment");
                break;
        }
    }

    @Event(value = {R.id.main_messages})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.main_messages:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                main_listCount.setVisibility(View.GONE);
        }
    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    long temptime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - temptime > 2000) // 2s内再次选择back键有效
            {
                Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_SHORT).show();
                temptime = System.currentTimeMillis();
            } else {
               finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
