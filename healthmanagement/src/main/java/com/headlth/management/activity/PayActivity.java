package com.headlth.management.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.chatpay.PayResult;
import com.headlth.management.entity.ChatPay;
import com.headlth.management.entity.CircleList;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 2016/9/25.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_pay_commit)
    private Button activity_pay_commit;

    @ViewInject(R.id.activity_pay_name)
    private TextView activity_pay_name;
    @ViewInject(R.id.activity_pay_number)
    private TextView activity_pay_number;
    @ViewInject(R.id.activity_pay_price)
    private TextView activity_pay_price;

    @ViewInject(R.id.activity_pay_pay_layout)
    private LinearLayout activity_pay_pay_layout;
    @ViewInject(R.id.activity_pay_chat_layout)
    private LinearLayout activity_pay_chat_layout;

    @ViewInject(R.id.activity_pay_pay_im)
    private ImageView activity_pay_pay_im;
    @ViewInject(R.id.activity_pay_chat_im)
    private ImageView activity_pay_chat_im;
    private Gson gson = new Gson();
    private String orderInfo;
    private int payFlag = 2;//1 支付宝 2 微信
    private IWXAPI api;
    public static Activity activity;
    String OrderNo, PlanNameID;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }


        }

    };
    private Runnable payRunnable = new Runnable() {
        @Override
        public void run() {
            PayTask alipay = new PayTask(PayActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i("msp", result.toString());
            Message msg = new Message();
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("支付订单");
        activity = this;
        api = WXAPIFactory.createWXAPI(PayActivity.this, Constant.CHAT_ID);
        api.registerApp(Constant.CHAT_ID);
        intent = getIntent();
        initialize();
    }

    private void initialize() {
        OrderNo = intent.getStringExtra("OrderNO");
        PlanNameID = intent.getStringExtra("PlanNameID");


        Log.i("AAOrderAaaA", OrderNo + "  " + PlanNameID);
        activity_pay_name.setText(intent.getStringExtra("PName"));
        activity_pay_number.setText(intent.getStringExtra("OrderNO"));
        activity_pay_price.setText(Double.parseDouble(intent.getStringExtra("Amount")) * 0.01 + "元");
        ShareUitls.putString(PayActivity.this, "ISPAY", "0");
    }

    private void getChatPrepayIdHttpURLConnection() {
        initDialog();
        waitDialog.setMessage("正在调起微信支付,请稍后...");
        waitDialog.showDailog();
        // String url = Constant.BASE_URL + "/MdMobileService.ashx?do=PostWxPayRequest";
        final String urlPath = new String(Constant.BASE_URL + "/MdMobileService.ashx?do=PostWxPayRequest");

        //  final String OrderNo = "20170109103611623";


        //String urlPath = new String("http://localhost:8080/Test1/HelloWorld?name=丁丁".getBytes("UTF-8"));
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    String param = "OrderNo=" + URLEncoder.encode(OrderNo, "UTF-8")
                            + "&PlanNameID=" + URLEncoder.encode(PlanNameID, "UTF-8")
                            + "&UID=" + ShareUitls.getString(PayActivity.this, "UID", "") + ""
                            + "&ResultJWT=" + ShareUitls.getString(PayActivity.this, "ResultJWT", "0")
                            +   "&VersionNum=" + VersonUtils.getVersionName(PayActivity.this);


                    //String PlanNameID = "PlanNameID=" + URLEncoder.encode(PlanNameID, "UTF-8");
                    Log.i("AAOrderAA", OrderNo + "  " + PlanNameID);
                    //建立连接
                    URL url = new URL(urlPath);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    //设置参数
                    httpConn.setDoOutput(true);   //需要输出
                    httpConn.setDoInput(true);   //需要输入
                    httpConn.setUseCaches(false);  //不允许缓存
                    httpConn.setRequestMethod("POST");   //设置POST方式连接
                    //设置请求属性
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    httpConn.setRequestProperty("Charset", "UTF-8");
                    //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
                    httpConn.connect();
                    //建立输入流，向指向的URL传入参数
                    DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
                    dos.writeBytes(param);
                    dos.flush();
                    dos.close();
                    //获得响应状态
                    int resultCode = httpConn.getResponseCode();

                    Log.i("AAOrderNO111", resultCode + "");
                    waitDialog.dismissDialog();

                    if (HttpURLConnection.HTTP_OK == resultCode) {
                        StringBuffer sb = new StringBuffer();
                        String readLine = new String();
                        BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                        while ((readLine = responseReader.readLine()) != null) {
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        Log.i("AAOrderNO", sb.toString());


                        ChatPay chatPay = gson.fromJson(sb.toString(), ChatPay.class);
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = chatPay;
                        handler.sendMessage(message);


                    } else {
                        Toast.makeText(PayActivity.this, "网络异常,支付异常", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {

                    Log.i("AAOrderNO111aa", e.toString());

                }

            }

        }.start();
    }

    private void getChatPrepayId() {//接口有问题 这种方式访问不到。。？？？

        Map<String, String> map = new HashMap<String, String>();
        String OrderNo = intent.getStringExtra("OrderNO");
        map.put("OrderNo", OrderNo);

        Log.i("AAOrderNO", OrderNo + "    " + OrderNo.length());
        String url = Constant.BASE_URL + "/MdMobileService.ashx?do=PostWxPayRequest";
        HttpUtils.getInstance(PayActivity.this).sendRequest("正在开启微信支付,请稍后...", url, map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AA", response);
                        //  startActivity(new Intent(PayActivity.this, TEXT.class).putExtra("jsonObject1",response+ "   "));
                        ChatPay chatPay = gson.fromJson(response, ChatPay.class);
                        if (chatPay.ErrCode.equals("100")) {
                            Toast.makeText(PayActivity.this, "支付失败,订单已经超时", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (chatPay.Status.equals("1")) {
                                ChatPay.WxPayData wxPayData = chatPay.WxPayData;
                                // startActivity(new Intent(PayActivity.this, TEXT.class).putExtra("jsonObject1",wxPayData.toString()+ "   "));
                                PayReq req = new PayReq();
                                req.appId = wxPayData.Appid;
                                req.partnerId = wxPayData.Partnerid;
                                req.prepayId = wxPayData.Prepayid;
                                req.nonceStr = wxPayData.NonceStr;
                                req.timeStamp = wxPayData.TimeStamp;
                                req.packageValue = wxPayData.Package;
                                req.sign = wxPayData.Sign;
                                api.sendReq(req);
                            } else {
                                Toast.makeText(PayActivity.this, "支付异常", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");
                        Toast.makeText(PayActivity.this, "网络异常,支付失败", Toast.LENGTH_SHORT).show();
                        return;


                    }
                }

        );

    }


    @Event(value = {R.id.view_publictitle_back, R.id.activity_pay_commit, R.id.activity_pay_pay_layout, R.id.activity_pay_chat_layout})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_pay_commit:
                String ISPAY = ShareUitls.getString(PayActivity.this, "ISPAY", "0");
                if (ISPAY.equals("1")) {
                    queryIsSeccuss();
                }
            {
                if (payFlag == 1) {
                    orderInfo = "";
                    if (orderInfo != null) {
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                } else if (payFlag == 2) {
                    getChatPrepayIdHttpURLConnection();
                    //    getChatPrepayId();
                } else {
                    Toast.makeText(PayActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                }
            }
              /*  Intent intent = new Intent(PayActivity.this, QuestionnaireResultActivity.class);
                intent.putExtra("PAY","1");
                startActivity(intent);*/
            break;
            case R.id.activity_pay_pay_layout:
                activity_pay_pay_im.setImageResource(R.mipmap.icon_choice_active);
                activity_pay_chat_im.setImageResource(R.mipmap.icon_choice_negative);
                payFlag = 1;
                break;
            case R.id.activity_pay_chat_layout:
                if (api.isWXAppInstalled()) {
                    activity_pay_chat_im.setImageResource(R.mipmap.icon_choice_active);
                    activity_pay_pay_im.setImageResource(R.mipmap.icon_choice_negative);
                    payFlag = 2;
                } else {
                    Toast.makeText(this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
                    ChatPay chatPay = (ChatPay) msg.obj;
                    Log.i("SSSSSSSssqSSSSSS", chatPay.toString());
                    //startActivity(new Intent(PayActivity.this, TEXT.class).putExtra("jsonObject1",sb.toString()+ "   "+OrderNo));
                    if (chatPay.ErrCode != null && (chatPay.ErrCode.equals("601") || chatPay.ErrCode.equals("600"))) {
                        if(chatPay.ErrCode.equals("601")){
                            Toast.makeText(PayActivity.this, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(PayActivity.this, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                        }
                        Intent i = new Intent(PayActivity.this, Login.class);
                        startActivity(i);
                        //清除所有文件数据
                        ShareUitls.cleanSharedPreference(PayActivity.this);
                        //清空圈子
                        CircleList.getInstance().circlelist.clear();
                        CircleList.getInstance().commentlist.clear();
                        CircleList.getInstance().replylist.clear();
                        Glide.get(PayActivity.this).clearMemory();
                        finish();

                    } else if (chatPay.ErrCode != null && chatPay.ErrCode.equals("100")) {
                        Toast.makeText(PayActivity.this, "支付失败,订单已经超时", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (chatPay.ErrCode != null && chatPay.ErrCode.equals("200")) {
                        Toast.makeText(PayActivity.this, "该处方已经支付", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (chatPay.Status.equals("1")) {
                            ShareUitls.putString(PayActivity.this, "OrderNO", OrderNo);//微信支付成功以后 回调页面用来 查询后台支付结构
                            ChatPay.WxPayData wxPayData = chatPay.WxPayData;
                            // startActivity(new Intent(PayActivity.this, TEXT.class).putExtra("jsonObject1",wxPayData.toString()+ "   "));
                            PayReq req = new PayReq();
                            req.appId = wxPayData.Appid;
                            req.partnerId = wxPayData.Partnerid;
                            req.prepayId = wxPayData.Prepayid;
                            req.nonceStr = wxPayData.NonceStr;
                            req.timeStamp = wxPayData.TimeStamp;
                            req.packageValue = wxPayData.Package;
                            req.sign = wxPayData.Sign;
                            api.sendReq(req);
                            ShareUitls.putString(PayActivity.this, "ISPAY", "1");

                        } else {
                            //  Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;


            }


        }

    };
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;

    private void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(PayActivity.this);
        waitDialog.setCancleable(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String ISPAY = ShareUitls.getString(PayActivity.this, "ISPAY", "0");
        if (ISPAY.equals("1")) {
            queryIsSeccuss();
        }

    }


    private void queryIsSeccuss() {//微信支付成功以后，点击多窗口 物理按键 直接回到本APP  这种情况不执行 微信的回调 需要手动或者自动去查询 该订单的支付状态
        if (!OrderNo.equals("")) {

            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostOrderResultRequest");
            params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "") + "");
            params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
            params.addBodyParameter("OrderNo", OrderNo);
            HttpUtils.getInstance(this).sendRequestRequestParams("正在查询支付状态...", params, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            ShareUitls.putString(PayActivity.this, "ISPAY", "0");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String OrderStatus = jsonObject.getString("OrderStatus");
                                if (OrderStatus.equals("1")) {
                                    ShareUitls.putString(PayActivity.this, "questionnaire", "1");//首页界面是否重新刷新 (打完题)
                                    ShareUitls.putString(PayActivity.this, "maidong", "1");//首页界面是否重新刷新 (新数据)
                                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    ShareUitls.putString(PayActivity.this, "PAY", "1");
                                    finish();
                                } else {
                                    Toast.makeText(PayActivity.this, "校验支付失败,点击重试", Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PayActivity.this, "校验支付失败,点击重试", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {
                            ShareUitls.putString(PayActivity.this, "ISPAY", "0");
                            Toast.makeText(PayActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }

            );

        }


    }
}
