package com.headlth.management.utils;

/**
 * Created by abc on 2016/7/18.
 */
public class HttpurlconnectionUtils {
   /* private static HttpurlconnectionUtils httpUtils;
    private static Activity context;
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;

    public interface ResponseListener {
        void onResponse(String response);

        void onErrorResponse(Throwable ex);
    }


    private  void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
        waitDialog.setCancleable(true);

    }

    public void sendRequest(final String dialogMessage, final String url, final Map<String, String> map, int point, final ResponseListener responseListener) {
        final com.headlth.management.clenderutil.WaitDialog waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
        initDialog();
        waitDialog.setMessage("正在调起微信支付,请稍后...");
        waitDialog.showDailog();
        // String url = Constant.BASE_URL + "/MdMobileService.ashx?do=PostWxPayRequest";
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String param = "OrderNo=" + URLEncoder.encode(OrderNo, "UTF-8")+"&PlanNameID="+URLEncoder.encode(PlanNameID, "UTF-8");
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


        waitDialog.setCancleable(true);
        if (point != 10) {
            waitDialog.setMessage(dialogMessage);
            waitDialog.showDailog();
        }

        if (InternetUtils.internet(context)) {
            RequestParams params = new RequestParams(url);
            Set entries = map.entrySet();
            if (entries != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Log.i("AAAAAAAAUUU", entry.getKey() + "   " + entry.getValue());
                    params.addBodyParameter(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            httpManager.post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    responseListener.onResponse(result);
                    Log.i("AAAAAAAAAA", result + "'");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    responseListener.onErrorResponse(ex);

                }

                @Override
                public void onCancelled(CancelledException cex) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                }

                @Override
                public void onFinished() {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                }
            });
        }
    }*/
}


