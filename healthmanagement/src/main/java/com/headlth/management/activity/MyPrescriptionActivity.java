package com.headlth.management.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.MyOrderJson;
import com.headlth.management.entity.MyPrescriptionJson;
import com.headlth.management.fragment.PrescriptionFragment;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/11/21.
 */

@ContentView(R.layout.activity_myprescription)
public class MyPrescriptionActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_myprescription_alltext)
    private TextView activity_myprescription_alltext;
    @ViewInject(R.id.activity_myprescription_allline)
    private TextView activity_myprescription_allline;

    @ViewInject(R.id.activity_myprescription_yetstarttext)
    private TextView activity_myprescription_yetstarttext;
    @ViewInject(R.id.activity_myprescription_yetstartline)
    private TextView activity_myprescription_yetstartline;

    @ViewInject(R.id.activity_myprescription_nostarttext)
    private TextView activity_myprescription_nostarttext;
    @ViewInject(R.id.activity_myprescription_onstartline)
    private TextView activity_myprescription_onstartline;

    @ViewInject(R.id.activity_myprescription_overtext)
    private TextView activity_myprescription_overtext;
    @ViewInject(R.id.activity_myprescription_overline)
    private TextView activity_myprescription_overline;
    @ViewInject(R.id.activity_myprescription_ViewPager)
    private ViewPager activity_myprescription_ViewPager;
    private List<Fragment> myPrescriptionFragmentList;

    private List<TextView> textList;
    private List<TextView> lineList;
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionListAll;
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionListStart;
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionListNoStart;
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionListOver;
    private ViewPagerPrescriptionAdapter viewPagerPrescriptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        AddList();

    }

    private void AddList() {
        view_publictitle_title.setText("我的计划");
        lineList = new ArrayList<>();
        textList = new ArrayList<>();
        MyPrescriptionListAll = new ArrayList<>();
        MyPrescriptionListStart = new ArrayList<>();
        MyPrescriptionListNoStart = new ArrayList<>();
        MyPrescriptionListOver = new ArrayList<>();


        textList.add(activity_myprescription_alltext);
        lineList.add(activity_myprescription_allline);
        textList.add(activity_myprescription_yetstarttext);
        lineList.add(activity_myprescription_yetstartline);
        textList.add(activity_myprescription_nostarttext);
        lineList.add(activity_myprescription_onstartline);
        textList.add(activity_myprescription_overtext);
        lineList.add(activity_myprescription_overline);
        myPrescriptionFragmentList = new ArrayList<>();

        activity_myprescription_ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setClore(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getMyPrescriptionActivityRequest();
    }


    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_myprescription_all
            , R.id.activity_myprescription_yetstart
            , R.id.activity_myprescription_nostart
            , R.id.activity_myprescription_over

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_myprescription_all:
                setClore(0);
                activity_myprescription_ViewPager.setCurrentItem(0);
                break;
            case R.id.activity_myprescription_yetstart:
                setClore(1);
                activity_myprescription_ViewPager.setCurrentItem(1);
                break;
            case R.id.activity_myprescription_nostart:
                setClore(2);
                activity_myprescription_ViewPager.setCurrentItem(2);
                break;
            case R.id.activity_myprescription_over:
                setClore(3);
                activity_myprescription_ViewPager.setCurrentItem(3);
                break;
        }
    }

    private void setClore(int position) {
        for (int i = 0; i < 4; i++) {
            if (position == i) {
                textList.get(i).setTextColor(Color.parseColor("#ffac04"));
                lineList.get(i).setBackgroundColor(Color.parseColor("#ffac04"));
            } else {
                textList.get(i).setTextColor(Color.parseColor("#c7c7c7"));
                lineList.get(i).setBackgroundColor(0);
            }

        }

    }

    private class ViewPagerPrescriptionAdapter extends FragmentPagerAdapter {
        public ViewPagerPrescriptionAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return myPrescriptionFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return myPrescriptionFragmentList.size();
        }


    }


    private void getMyPrescriptionActivityRequest() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PrescriptionRequest");
        params.addBodyParameter("UID",ShareUitls.getString(MyPrescriptionActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(MyPrescriptionActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("Page", "1");
        params.addBodyParameter("PageSize", "10");
        HttpUtils.getInstance(MyPrescriptionActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                     Log.i("LLLLLLSSSS",response);
                        MyPrescriptionJson myPrescriptionJson = new Gson().fromJson(response, MyPrescriptionJson.class);
                      //  Log.i("LLLLLL",myPrescriptionJson.PList.size()+"    "+response);
                        for(MyPrescriptionJson.MyPrescription myPrescription : myPrescriptionJson.PList){
                            switch (myPrescription.PrescriptionState){
                                case "3":
                                    MyPrescriptionListStart.add(myPrescription);
                                    break;
                                case "1":
                                    MyPrescriptionListNoStart.add(myPrescription);
                                    break;
                                case "2":
                                    MyPrescriptionListOver.add(myPrescription);
                                    break;


                            }
                           // MyPrescriptionListAll.add(myPrescription);

                        }
                        MyPrescriptionListAll.addAll(MyPrescriptionListStart);
                        MyPrescriptionListAll.addAll(MyPrescriptionListNoStart);
                        MyPrescriptionListAll.addAll(MyPrescriptionListOver);
                        myPrescriptionFragmentList.add(new PrescriptionFragment(MyPrescriptionListAll));
                        myPrescriptionFragmentList.add(new PrescriptionFragment(MyPrescriptionListStart));
                        myPrescriptionFragmentList.add(new PrescriptionFragment(MyPrescriptionListNoStart));
                        myPrescriptionFragmentList.add(new PrescriptionFragment(MyPrescriptionListOver));
                        viewPagerPrescriptionAdapter = new ViewPagerPrescriptionAdapter();
                        activity_myprescription_ViewPager.setAdapter(viewPagerPrescriptionAdapter);

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(MyPrescriptionActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }
}
