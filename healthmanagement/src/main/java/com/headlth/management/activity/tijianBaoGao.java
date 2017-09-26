package com.headlth.management.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.newChuFangCallback;
import com.headlth.management.entity.tiJianCallBack;
import com.headlth.management.fragment.GerenXinXi;
import com.headlth.management.fragment.YiChang;
import com.headlth.management.utils.Constant;
import com.headlth.management.adapter.MyFragmentPagerAdapter;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_main3)
public class tijianBaoGao extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.line1)
    private TextView line1;
    @ViewInject(R.id.line2)
    private TextView line2;
    @ViewInject(R.id.geren)
    private Button geren;
    @ViewInject(R.id.yichang)
    private Button yichang;
    @ViewInject(R.id.chankanchufang)
    private Button chankanchufang;


    private ViewPager vPager;
    ArrayList<Fragment> fragmentsList;
    GerenXinXi frag1;
    YiChang frag2;



    private static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("体检报告");
        url = Constant.BASE_URL;
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        InitViewPager();
    }

    tiJianCallBack tijian;
    private void InitViewPager() {
        Intent intent = getIntent();
        tijian = (tiJianCallBack) intent.getSerializableExtra("tijian");
        Log.e("serial",tijian+"AnalizeFragment2");
        /*Log.e("serial", tijian + "AnalizeFragment2" + tijian.getStatus() + "");*/

        fragmentsList = new ArrayList<Fragment>();
        frag1 = new GerenXinXi(tijian);
        frag2 = new YiChang(tijian);
        fragmentsList.add(frag1);
        fragmentsList.add(frag2);
        //跟换的画会有问题
        vPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
      /*  mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentsList));*/
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        vPager.setOffscreenPageLimit(0);
        vPager.setCurrentItem(0);
    }
    @Event(value = {R.id.view_publictitle_back

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;


        }}

    int i = 1;
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Log.e("onPageSelected", arg0 + "");
            if (i % 2 != 0) {
               /* today.setTextSize(13);
                today.setTextColor(Color.parseColor("#8ad6f1"));
                today.setText("分析");
                first.setText("");
                second.setTextSize(17);
                second.setTextColor(Color.parseColor("#ffffff"));
                second.setText("卡路里");*/

                line1.setBackgroundColor(Color.parseColor("#00000000"));
                line2.setBackgroundColor(Color.parseColor("#ffad00"));
                geren.setTextColor(Color.parseColor("#999999"));
                yichang.setTextColor(Color.parseColor("#000000"));

                i++;
            } else {
              /*  today.setTextSize(17);
                today.setTextColor(Color.parseColor("#ffffff"));
                today.setText("分析");
                first.setText("");
                second.setTextSize(13);
                second.setTextColor(Color.parseColor("#8ad6f1"));
                second.setText("卡路里");*/

                line1.setBackgroundColor(Color.parseColor("#ffad00"));
                line2.setBackgroundColor(Color.parseColor("#00000000"));
                geren.setTextColor(Color.parseColor("#000000"));
                yichang.setTextColor(Color.parseColor("#999999"));

                i++;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
          /*  *//**//*onPageScrolled(int arg0,float arg1,int arg2)    ，当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到
           调用。其中三个参数的含义分别为：
           arg0 :当前页面，及你点击滑动的页面
           arg1:当前页面偏移的百分比
           arg2:当前页面偏移的像素位置 */
        }


        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 2) {
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        chankanchufang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newChuFang();
            }
        });

    }


    //新的处方接口
    newChuFangCallback newChuFang;
    private void newChuFang() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPrescriptionNewRequest");

        params.addBodyParameter("UID",ShareUitls.getString(tijianBaoGao.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(tijianBaoGao.this, "ResultJWT", "0"));
        HttpUtils.getInstance(tijianBaoGao.this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("dfgsdf", "dfgsdf" + response.toString());
                        newChuFang = g.fromJson(response.toString(),
                                newChuFangCallback.class);
                        if (newChuFang.getStatus() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), NewChuFang.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newChuFang", newChuFang);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"对不起，您还没有运动处方可查看!", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

        );
    }
    Gson g = new Gson();


}


