package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.MyPrescriptionDetialsActivity;
import com.headlth.management.activity.PrescriptionDetailsActivity;
import com.headlth.management.adapter.MyOrderAdapter;
import com.headlth.management.adapter.MyPrescriptionAdapter;
import com.headlth.management.adapter.PrescriptionAdapter;
import com.headlth.management.entity.MyOrderJson;
import com.headlth.management.entity.MyPrescriptionJson;
import com.headlth.management.entity.Prescription;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/9/28.
 *
 * 此界面是个公共界面
 */
@ContentView(R.layout.fragment_prescription)
public class PrescriptionFragment extends BaseFragment {
    @ViewInject(R.id.fragment_presciption_listview)
    private ListView fragment_presciption_listview;
    private PrescriptionJson.PrescriptionClass prescriptionClass;
    private PrescriptionAdapter prescriptionAdapter;


    private MyPrescriptionAdapter myprescriptionAdapter;
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionList;
    private List<MyOrderJson.MyOrder> MyOrderList;
    private MyOrderAdapter myorderAdapter;


    int flag;
    int ordertype;

    @SuppressLint("ValidFragment")
    public PrescriptionFragment() {
    }

    @SuppressLint("ValidFragment")
    public PrescriptionFragment(PrescriptionJson.PrescriptionClass prescriptionClass) {
        this.prescriptionClass = prescriptionClass;
        flag = 0;//锻炼计划 所有处方界面
    }

    @SuppressLint("ValidFragment")
    public PrescriptionFragment(List<MyPrescriptionJson.MyPrescription> MyPrescriptionList) {
        this.MyPrescriptionList = MyPrescriptionList;
        flag = 1;//我的处方界面

    }

    @SuppressLint("ValidFragment")
    public PrescriptionFragment(List<MyOrderJson.MyOrder> MyOrderList, int ordertype) {
        this.MyOrderList = MyOrderList;
        flag = 2;//我的订单界面
        this.ordertype = ordertype;//订单类型 8 是已取消

    }

    @SuppressLint("ValidFragment")
    public PrescriptionFragment(int ordertype) {
        flag = 2;//我的订单界面
        this.ordertype = ordertype;//订单类型 8 是已取消

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (flag == 0) {//锻炼计划 所有处方界面
            getOnePrescription();
            fragment_presciption_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    startActivity(new Intent(getActivity(), PrescriptionDetailsActivity.class).putExtra("prescription", prescriptionClass));
                }
            });


        } else if (flag == 1) {//我的处方界面
            if (MyPrescriptionList != null) {
                myprescriptionAdapter = new MyPrescriptionAdapter(getActivity(), MyPrescriptionList);
                fragment_presciption_listview.setAdapter(myprescriptionAdapter);
                fragment_presciption_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), MyPrescriptionDetialsActivity.class).putExtra("MyPrescription", MyPrescriptionList.get(position)));
                    }
                });

            }


        }
    }

    @Override
    public void onStart() {
        super.onStart();
       if (flag == 2){//我的订单界面 可能涉及支付 取消 相应操作后 会引起数据源变化 放在onstart里 变化后便于刷新数据源
            //  if (MyOrderList != null) {//我的订单界面
            getCancleOrderRequest();
              /*  String CancleOrderNO = ShareUitls.getString(getActivity(), "CancleOrderNO", "0");//有订单取消需要刷新 已取消和全部 两个只块儿的数据源
                if (!CancleOrderNO.equals("0")) {
                    getCancleOrderRequest();
                } else {
                    myorderAdapter = new MyOrderAdapter(getActivity(), MyOrderList);
                    fragment_presciption_listview.setAdapter(myorderAdapter);
                }*/

            fragment_presciption_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //startActivity(new Intent(getActivity(), MyPrescriptionDetialsActivity.class).putExtra("MyPrescription", MyPrescriptionList.get(position)));
                }
            });

            //   }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if (flag == 2) {
            ShareUitls.putString(getActivity(), "CancleOrderNO", "0");//把  有订单取消需要刷新 已取消和全部 两个只块儿的数据源 的状态制0
        }*/

    }

    List<PrescriptionJson.PrescriptionClass> PrescriptionList;

    private void getOnePrescription() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetPlanNameListRequest");

        params.addBodyParameter("UID",ShareUitls.getString(getActivity(), "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        params.addBodyParameter("PlanClassID", prescriptionClass.PlanClassID);
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        PrescriptionJson prescriptionJson = new Gson().fromJson(response, PrescriptionJson.class);
                        if (prescriptionJson.Status.equals("1")) {
                            prescriptionAdapter = new PrescriptionAdapter(getActivity(), prescriptionJson.PlanNameList);
                            fragment_presciption_listview.setAdapter(prescriptionAdapter);
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    private void getCancleOrderRequest() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetOrderListRequest");

        params.addBodyParameter("UID",ShareUitls.getString(getActivity(), "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        params.addBodyParameter("OrderStatusID", ordertype+"");
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NNNNNNNN", response);
                        MyOrderJson myOrderJson = new Gson().fromJson(response, MyOrderJson.class);
                        myorderAdapter = new MyOrderAdapter(getActivity(), myOrderJson.OrderList);
                        fragment_presciption_listview.setAdapter(myorderAdapter);
                       // ShareUitls.putString(getActivity(), "CancleOrderNO", "0");//把  有订单取消需要刷新 已取消和全部 两个子块儿的数据源 的状态制0
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

}
