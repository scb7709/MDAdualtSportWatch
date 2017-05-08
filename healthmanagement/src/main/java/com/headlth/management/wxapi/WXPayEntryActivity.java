package com.headlth.management.wxapi;


import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.TEXT;
import com.headlth.management.activity.PayActivity;
import com.headlth.management.activity.QuestionnaireResultActivity;
import com.headlth.management.entity.PayOrderNumber;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.pay_result)
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    @ViewInject(R.id.WXPayEntryActivity_title)
    private TextView WXPayEntryActivity_title;
    @ViewInject(R.id.WXPayEntryActivity_again)
    private Button WXPayEntryActivity_again;


    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private int again = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        WXPayEntryActivity_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (again != 0)
                    queryIsSeccuss();
                else {
                    callPhone("010-65535698");
                }
            }
        });
        api = WXAPIFactory.createWXAPI(this, Constant.CHAT_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        // startActivity(new Intent(WXPayEntryActivity.this, TEXT.class).putExtra("jsonObject1",chatpay.toString()+ "   "));
        Log.d("onPayFinish", "onPayFinish, errCode = " + resp.errCode);
        if (resp.errCode == 0) {
            queryIsSeccuss();
        } else if (resp.errCode == -1) {
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        } else if (resp.errCode == -2) {
            Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
            finish();
        }

     /*  if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("支付结果"+ resp.openId +"  "+resp.transaction+" "+resp.toString()+" "+resp.checkArgs()+"  "+resp.getType() );
			builder.show();
		}*/
    }

    private void callPhone(final String phoneNumber) {
        if (!phoneNumber.equals("") && phoneNumber != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WXPayEntryActivity.this);
            builder.setTitle("支付异常")
                    .setMessage("是否拨打客服电话  " + phoneNumber + " ?")
                    .setPositiveButton("拨号", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                            ShareUitls.putString(WXPayEntryActivity.this, "OrderNO", "");
                            finish();
                            PayActivity.activity.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ShareUitls.putString(WXPayEntryActivity.this, "OrderNO", "");
                            finish();
                            PayActivity.activity.finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void queryIsSeccuss() {
        again--;
        WXPayEntryActivity_again.setVisibility(View.GONE);
        WXPayEntryActivity_title.setText("微信支付");
        String OrderNo = ShareUitls.getString(WXPayEntryActivity.this, "OrderNO", "");
        if (!OrderNo.equals("")) {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostOrderResultRequest");
            params.addBodyParameter("OrderNo", OrderNo);
            params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "") + "");
            params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
            HttpUtils.getInstance(this).sendRequestRequestParams("正在校验,请稍后...", params,true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String OrderStatus = jsonObject.getString("OrderStatus");
                                if (OrderStatus.equals("1")) {
                                    ShareUitls.putString(WXPayEntryActivity.this,"questionnaire","1");//首页界面是否重新刷新 (打完题)
                                    ShareUitls.putString(WXPayEntryActivity.this,"maidong","1");//首页界面是否重新刷新 (新数据)
                                    WXPayEntryActivity_title.setText("微信支付成功,正在跳转...");
                                    Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    ShareUitls.putString(WXPayEntryActivity.this, "OrderNO", "");
                                    ShareUitls.putString(WXPayEntryActivity.this, "PAY", "1");
                                  /*  Intent intent = new Intent(WXPayEntryActivity.this, QuestionnaireResultActivity.class);
                                    intent.putExtra("PAY", "1");
                                    startActivity(intent);
                                    QuestionnaireResultActivity.activity.finish();*/
                                    PayActivity.activity.finish();
                                    finish();
                                } else {
                                    Toast.makeText(WXPayEntryActivity.this, "校验支付失败,点击重试", Toast.LENGTH_SHORT).show();
                                    WXPayEntryActivity_again.setVisibility(View.VISIBLE);
                                    ;
                                    WXPayEntryActivity_title.setText("微信支付失败");
                                    if (again == 0) {
                                        WXPayEntryActivity_again.setText("支付异常,点击返回");

                                    } else {
                                        WXPayEntryActivity_again.setText("再试一次(" + again + ")");
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(WXPayEntryActivity.this, "校验支付失败,点击重试", Toast.LENGTH_SHORT).show();
                                WXPayEntryActivity_again.setVisibility(View.VISIBLE);
                                WXPayEntryActivity_title.setText("微信支付失败");
                                if (again == 0) {
                                    WXPayEntryActivity_again.setText("支付异常,点击返回");
                                } else {
                                    WXPayEntryActivity_again.setText("再试一次(" + again + ")");
                                }
                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {
                            WXPayEntryActivity_again.setVisibility(View.VISIBLE);
                            Toast.makeText(WXPayEntryActivity.this, "网络异常,微信支付失败", Toast.LENGTH_SHORT).show();
                            WXPayEntryActivity_title.setText("网络异常,微信支付失败");
                            if (again == 0) {
                                WXPayEntryActivity_again.setText("支付异常,点击返回");
                            } else {
                                WXPayEntryActivity_again.setText("再试一次(" + again + ")");
                            }
                        }
                    }

            );

        } else {
            WXPayEntryActivity_title.setText("订单异常,支付失败");
            Toast.makeText(WXPayEntryActivity.this, "订单异常,支付失败", Toast.LENGTH_SHORT).show();
            ShareUitls.putString(WXPayEntryActivity.this, "OrderNO", "");
            finish();
            PayActivity.activity.finish();
        }


    }

    long temptime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}