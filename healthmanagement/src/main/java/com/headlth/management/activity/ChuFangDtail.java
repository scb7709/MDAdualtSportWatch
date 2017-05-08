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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.chufang.fittimeFragment;
import com.headlth.management.chufang.rateFragment;
import com.headlth.management.chufang.strongFragment;
import com.headlth.management.chufang.timeFragment;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.adapter.MyFragmentPagerAdapter;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
@ContentView(R.layout.layout_newchufang)
public class ChuFangDtail extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    private ViewPager vPager;
    ArrayList<Fragment> fragmentsList;
    chufangCallBack chufang = null;

    strongFragment frag1;
    rateFragment frag2;
    timeFragment frag3;
    fittimeFragment frag4;
    private ViewGroup group;
    ImageView imageView;
    private ImageView[] imageViews;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        InitViewPager();

    }
    @Event(value = {R.id.view_publictitle_back

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;


        }}



    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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
        view_publictitle_title.setText("计划详情");


        Intent intent = this.getIntent();
        chufang = (chufangCallBack) intent.getSerializableExtra("chufang");
        Log.e("ffff", chufang + "跳到viewpager");


        //改动
     /*   spitString(untitled1.getPList().get(0).getSportTarget());*/

        vPager = (ViewPager) this.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        if (chufang != null) {

            frag1 = new strongFragment(chufang);
            frag2 = new rateFragment(chufang);
            frag3 = new timeFragment(chufang);
            frag4 = new fittimeFragment(chufang);

        }


        fragmentsList.add(frag1);
        fragmentsList.add(frag2);
        fragmentsList.add(frag3);
        fragmentsList.add(frag4);

        imageViews = new ImageView[fragmentsList.size()];
        group = (ViewGroup) this.findViewById(R.id.viewGroup);

        for (int i = 0; i < fragmentsList.size(); i++) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置每个小圆点距离左边的间距
            margin.setMargins(10, 0, 0, 0);
            imageView = new ImageView(ChuFangDtail.this);
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
