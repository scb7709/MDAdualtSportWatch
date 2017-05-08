package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.headlth.management.R;
import com.headlth.management.chufang.fittimeFragment;
import com.headlth.management.chufang.rateFragment;
import com.headlth.management.chufang.strongFragment;
import com.headlth.management.chufang.targetFragment;
import com.headlth.management.chufang.timeFragment;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;




public class YunDongChuFang extends BaseActivity {

    private ViewPager vPager;
    ArrayList<Fragment> fragmentsList;
    chufangCallBack chufang = null;
    targetFragment frag1;
    strongFragment frag2;
    rateFragment frag3;
    timeFragment frag4;
    fittimeFragment frag5;
    private ViewGroup group;
    ImageView imageView;
    private ImageView[] imageViews;
    Button bt_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chufang);
        InitViewPager();

    }
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

    public void back(View view) {
        finish();
    }

    List<String> SportTarget = new ArrayList<String>();

    public void spitString(String string) {
        String[] ss = string.split("\n");
        for (int i = 0; i < ss.length; i++) {
            SportTarget.add(ss[i]);
        }

       /* for(int i=0;i<questions.size();i++){
            Log.e("json", "调过来了的对象" + questions.get(i));
        }*/
    }
    private void InitViewPager() {
        Intent intent = this.getIntent();
        chufang = (chufangCallBack) intent.getSerializableExtra("chufang");
        Log.e("ffff", chufang + "跳到viewpager");


        //改动
     /*   spitString(untitled1.getPList().get(0).getSportTarget());*/


        bt_back = (Button) this.findViewById(R.id.bt_back);
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        if (chufang != null) {
            frag1 = new targetFragment(chufang.getPList().get(0).getSportTarget());
            frag2 = new strongFragment(chufang);
            frag3 = new rateFragment(chufang);
            frag4 = new timeFragment(chufang);
            frag5 = new fittimeFragment(chufang);

        }


        fragmentsList.add(frag1);
        fragmentsList.add(frag2);
        fragmentsList.add(frag3);
        fragmentsList.add(frag4);
        fragmentsList.add(frag5);

        imageViews = new ImageView[fragmentsList.size()];
        group = (ViewGroup) this.findViewById(R.id.viewGroup);

        for (int i = 0; i < fragmentsList.size(); i++) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置每个小圆点距离左边的间距
            margin.setMargins(10, 0, 0, 0);
            imageView = new ImageView(YunDongChuFang.this);
            //设置每个小圆点的宽高
            imageView.setLayoutParams(new LayoutParams(15, 15));
            imageViews[i] = imageView;
            if (i == 0) {
                // 默认选中第一张图片
                imageViews[i]
                        .setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                //其他图片都设置未选中状态
                imageViews[i]
                        .setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            group.addView(imageViews[i], margin);
        }


        // 跟换的画会有问题
        vPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentsList));
        Log.e("null?", vPager + "是空吗？");
        /*
         * mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(),
		 * fragmentsList));
		 */
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
    }

    public class MyOnPageChangeListener implements
            ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.page_indicator_focused);

                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
			/*  *//**//*
					 * onPageScrolled(int arg0,float arg1,int arg2)
					 * ，当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到 调用。其中三个参数的含义分别为： arg0
					 * :当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
					 *//**//**/
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    }

}
