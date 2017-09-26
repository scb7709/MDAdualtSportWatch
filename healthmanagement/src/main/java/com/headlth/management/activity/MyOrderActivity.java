package com.headlth.management.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.MyOrderJson;
import com.headlth.management.fragment.PrescriptionFragment;
import com.headlth.management.myview.NoPreloadViewPager;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

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

@ContentView(R.layout.activity_myprescription)//复用我的处方布局
public class MyOrderActivity extends BaseActivity {


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
    private NoPreloadViewPager activity_myprescription_ViewPager;


    private List<Fragment> myOrderFragmentList;

    private List<TextView> textList;
    private List<TextView> lineList;
    private List<MyOrderJson.MyOrder> MyOrderListAll;
    private List<MyOrderJson.MyOrder> MyOrderListPay;
    private List<MyOrderJson.MyOrder> MyOrderListNoPay;
    private List<MyOrderJson.MyOrder> MyOrderListCancle;
    private ViewPagerOrderAdapter viewPagerOrderAdapter;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
    }

    private void initialize() {
        view_publictitle_title.setText("我的订单");
        UID = ShareUitls.getString(MyOrderActivity.this, "UID", "");
        activity_myprescription_yetstarttext.setText("已支付");
        activity_myprescription_nostarttext.setText("未支付");
        activity_myprescription_overtext.setText("已取消");
        AddList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void AddList() {
        lineList = new ArrayList<>();
        textList = new ArrayList<>();
        MyOrderListAll = new ArrayList<>();
        MyOrderListPay = new ArrayList<>();
        MyOrderListNoPay = new ArrayList<>();
        MyOrderListCancle = new ArrayList<>();
        textList.add(activity_myprescription_alltext);
        lineList.add(activity_myprescription_allline);
        textList.add(activity_myprescription_yetstarttext);
        lineList.add(activity_myprescription_yetstartline);
        textList.add(activity_myprescription_nostarttext);
        lineList.add(activity_myprescription_onstartline);
        textList.add(activity_myprescription_overtext);
        lineList.add(activity_myprescription_overline);

        myOrderFragmentList = new ArrayList<>();
        myOrderFragmentList.add(new PrescriptionFragment(10));
        myOrderFragmentList.add(new PrescriptionFragment( 1));
        myOrderFragmentList.add(new PrescriptionFragment( 3));
        myOrderFragmentList.add(new PrescriptionFragment( 8));

        viewPagerOrderAdapter = new ViewPagerOrderAdapter();
        activity_myprescription_ViewPager.setAdapter(viewPagerOrderAdapter);
        activity_myprescription_ViewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
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
     //  getOrderRequest();
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

    private class ViewPagerOrderAdapter extends FragmentPagerAdapter {
        public ViewPagerOrderAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return myOrderFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return myOrderFragmentList.size();
        }


    }

    private void getOrderRequest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("UserID", UID);
        map.put("OrderStatusID", "10");
        HttpUtils.getInstance(MyOrderActivity.this).sendRequest("", Constant.BASE_URL + "/MdMobileService.ashx?do=GetOrderListRequest", map, 10, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NNNNNNNN",response);
                        MyOrderJson myOrderJson = new Gson().fromJson(response, MyOrderJson.class);
                        for(MyOrderJson.MyOrder myOrder:myOrderJson.OrderList){

                            switch (myOrder.OrderStatusID){
                                case "1"://已经支付
                                    MyOrderListPay.add(myOrder);
                                    break;
                                case "0":
                                case "7":
                                case "3":
                                    MyOrderListNoPay.add(myOrder);//未支付
                                    break;
                                case "8":
                                    MyOrderListCancle.add(myOrder);//已经取消
                                    break;


                            }
                            MyOrderListAll.add(myOrder);

                        }
                       /* MyOrderListAll.addAll(MyOrderListPay);
                        MyOrderListAll.addAll(MyOrderListNoPay);
                        MyOrderListAll.addAll(MyOrderListCancle);

                        MyOrderListAll.addAll(MyOrderListCancle);
                        MyOrderListAll.addAll(MyOrderListCancle);

                        myOrderFragmentList.add(new PrescriptionFragment(MyOrderListAll, 10));
                        myOrderFragmentList.add(new PrescriptionFragment(MyOrderListPay, 1));
                        myOrderFragmentList.add(new PrescriptionFragment(MyOrderListNoPay, 3));
                        myOrderFragmentList.add(new PrescriptionFragment(MyOrderListCancle, 8));
                      */
                        myOrderFragmentList.add(new PrescriptionFragment(10));
                        myOrderFragmentList.add(new PrescriptionFragment( 1));
                        myOrderFragmentList.add(new PrescriptionFragment( 3));
                        myOrderFragmentList.add(new PrescriptionFragment( 8));

                        viewPagerOrderAdapter = new ViewPagerOrderAdapter();
                        activity_myprescription_ViewPager.setAdapter(viewPagerOrderAdapter);

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(MyOrderActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }
}
