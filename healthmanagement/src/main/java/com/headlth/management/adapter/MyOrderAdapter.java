package com.headlth.management.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.MyOrderDetialsActivity;
import com.headlth.management.activity.PayActivity;
import com.headlth.management.entity.MyOrderJson;
import com.headlth.management.fragment.PrescriptionFragment;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by abc on 2016/8/15.
 */
public class MyOrderAdapter extends BaseAdapter {
    private List<MyOrderJson.MyOrder> MyOrderList;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public MyOrderAdapter(Activity activity, List<MyOrderJson.MyOrder> MyOrderList) {
        layoutInflater = LayoutInflater.from(activity);
        this.MyOrderList = MyOrderList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return MyOrderList.size();
    }

    @Override
    public MyOrderJson.MyOrder getItem(int position) {
        return MyOrderList.get(position);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_myorder, null);
            holder = new Holder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final MyOrderJson.MyOrder myOrder = getItem(position);
        holder.view_myorder_id.setText(myOrder.OrderID);
        holder.view_myorder_money.setText(myOrder.Amount * 0.01 + "元");
        holder.view_myorder_name.setText(myOrder.OrderName);
        holder.view_myorder_type.setText(myOrder.OrderStatus);
        switch (myOrder.OrderStatusID) {
            case "1"://已支付
                holder.view_myorder_nowpay_layout.setVisibility(View.GONE);
                holder.view_myorder_cancleorder.setVisibility(View.GONE);
                break;
            case "3":
            case "0":
            case "7":
                holder.view_myorder_nowpay_layout.setVisibility(View.VISIBLE);
                holder.view_myorder_cancleorder.setVisibility(View.VISIBLE);
                holder.view_myorder_nowpay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, PayActivity.class);
                        ShareUitls.putString(activity, "OrderNO", myOrder.OrderID);
                        intent.putExtra("PName", myOrder.OrderName);
                        intent.putExtra("OrderNO", myOrder.OrderID);
                        intent.putExtra("Amount", myOrder.Amount + "");
                        intent.putExtra("PlanNameID", myOrder.PlanNameID + "");

                        activity.startActivity(intent);
                    }
                });
                holder.view_myorder_cancleorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CanaleOrder(myOrder.OrderID);
                    }
                });

                break;
            case "8"://已取消
                holder.view_myorder_nowpay_layout.setVisibility(View.GONE);
                holder.view_myorder_cancleorder.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.view_myorder_id)
        public TextView view_myorder_id;
        @ViewInject(R.id.view_myorder_type)
        public TextView view_myorder_type;
        @ViewInject(R.id.view_myorder_name)
        public TextView view_myorder_name;

        @ViewInject(R.id.view_myorder_money)
        public TextView view_myorder_money;

        @ViewInject(R.id.view_myorder_nowpay)
        public Button view_myorder_nowpay;
        @ViewInject(R.id.view_myorder_cancleorder)
        public Button view_myorder_cancleorder;


        @ViewInject(R.id.view_myorder_nowpay_layout)
        public RelativeLayout view_myorder_nowpay_layout;


    }

    private void CanaleOrder(final String OrderNO) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostCancelOrderInfoRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(activity, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(activity, "UID", "0"));
        params.addBodyParameter("OrderNO", OrderNO);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params,false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("NNNNNNNN", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String IsSuccess = jsonObject.getString("IsSuccess");
                            if (IsSuccess.equals("true")) {
                                Toast.makeText(activity, "取消成功", Toast.LENGTH_SHORT).show();
                                for (MyOrderJson.MyOrder myOrder : MyOrderList) {
                                    if (myOrder.OrderID.equals(OrderNO)) {
                                        myOrder.OrderStatusID = "8";
                                        myOrder.OrderStatus= "订单已取消";
                                    }
                                }
                                notifyDataSetChanged();
                              //  ShareUitls.putString(activity,"CancleOrderNO",OrderNO);//有订单取消需要刷新 已取消和全部 两个只块儿的数据源
                            } else {
                                Toast.makeText(activity, "订单异常,取消失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException j) {

                            Toast.makeText(activity, "网络异常,请求失败", Toast.LENGTH_SHORT).show();
                        }

                        // MyOrderJson myOrderJson = new Gson().fromJson(response, MyOrderJson.class);


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(activity, "网络异常,请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }
}