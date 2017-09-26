package com.headlth.management.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.TEXT;
import com.headlth.management.acs.App;
import com.headlth.management.activity.AccountManageActivity;
import com.headlth.management.activity.BoundPhoneActivity;
import com.headlth.management.activity.CompleteInformationActivity;
import com.headlth.management.activity.HomeActivity;
import com.headlth.management.activity.Login;
import com.headlth.management.activity.MainActivity;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.PublicDataClass;
import com.headlth.management.entity.User;
import com.headlth.management.entity.UserLogin;
import com.headlth.management.myview.MyToash;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by abc on 2016/7/18.
 */
public class HttpUtils {
    private static HttpUtils httpUtils;
    static HttpManager httpManager;
    private static Activity context;
    //private static com.headlth.management.clenderutil.WaitDialog waitDialog;
    private WaitDialog waitDialog;

    public interface ResponseListener {
        void onResponse(String response);

        void onErrorResponse(Throwable ex);
    }

    public interface ResponseListenerNew {
        void onResponse(String response, PublicDataClass.MdResponse mdResponse);

        void onErrorResponse(Throwable ex);
    }

    public HttpUtils(Activity activity) {
        context = activity;
        httpManager = x.http();
        httpUtils = new HttpUtils();
    }

    public HttpUtils() {

    }

    public static HttpUtils getInstance(Activity activity) {
        context = activity;
        httpManager = x.http();
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }

            }
        }
        return httpUtils;
    }


    private void initDialog() {
        if (waitDialog == null) {
            waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
            waitDialog.setCancleable(true);
        }
    }

    public void sendRequest(final String dialogMessage, final String url, final Map<String, String> map, int point, final ResponseListener responseListener) {
        final com.headlth.management.clenderutil.WaitDialog waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
        waitDialog.setCancleable(true);
        if (point != 10) {
            waitDialog.setMessage(dialogMessage);
            waitDialog.showDailog();
        }

        if (InternetUtils.internet(context)) {
            RequestParams params = new RequestParams(url);
            //params.addBodyParameter(entry.getKey().toString(), entry.getValue().toString());
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
                    Log.i("AAAAACCCAAAA", ex.toString() + "'" + isOnCallback);
                    responseListener.onErrorResponse(ex);

                }

                @Override
                public void onCancelled(CancelledException cex) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    // responseListener.onErrorResponse(new VolleyError());
                }

                @Override
                public void onFinished() {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    //responseListener.onErrorResponse(new VolleyError());
                }
            });

        } else {
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }

    }
    int  count=0;
    public void sendRequestRequestParams(final String dialogMessage, final RequestParams params, final boolean dialog, final ResponseListener responseListener) {
        MyToash.Log(count+"    =count");
        if (context == null) {
            responseListener.onErrorResponse(null);
            return;
        }
        if (dialog) {
            initDialog();
            waitDialog.setMessage(dialogMessage);
            waitDialog.showDailog();
        }
        if (InternetUtils.internet(context)) {
            String version = VersonUtils.getVersionName(context);
            params.addBodyParameter("VersionNum", version);
            Log.i("VersionNum", VersonUtils.getVersionName(context));
            httpManager.post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {

                            if (waitDialog != null) {
                                waitDialog.dismissDialog();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("ErrCode") != null && (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600"))) {

                                    //Toast.makeText(context, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                    if (jsonObject.getString("ErrCode").equals("601")) {
                                        Toast.makeText(context, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                                    }
                                    Intent i = new Intent(context, Login.class);
                                    context.startActivity(i);
                          /*  if (MainActivity.Activity != null) {
                                MainActivity.Activity.finish();
                            }*/
                                    //清除所有文件数据
                                    ShareUitls.cleanSharedPreference(context);
                                    //清空圈子
                                    CircleList.getInstance().circlelist.clear();
                                    CircleList.getInstance().commentlist.clear();
                                    CircleList.getInstance().replylist.clear();
                                    Glide.get(context).clearMemory();
                                    context.finish();
                                } else {
                                    responseListener.onResponse(result);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Log.i("AAAAAAAAAA", result + "'");
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.i("AAAAACCCAAAA", ex.toString() + "'" + isOnCallback);
                            //System.Net.Sockets.SocketException;
                            if (ex != null &&( ex instanceof SocketException)&&count<=1) {//进阶接口可能会出现这个异常
                                count++;
                                sendRequestRequestParams(dialogMessage,  params,  dialog,  responseListener);
                            } else {
                                if (waitDialog != null) {
                                    waitDialog.dismissDialog();
                                }
                                try {
                                    responseListener.onErrorResponse(ex);
                                } catch (Exception E) {
                                }
                            }

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                           // responseListener.onErrorResponse(null);
                            if (waitDialog != null) {
                                waitDialog.dismissDialog();
                            }
                            // responseListener.onErrorResponse(new VolleyError());
                        }

                        @Override
                        public void onFinished() {
                          //  responseListener.onErrorResponse(null);
                            if (waitDialog != null) {
                                waitDialog.dismissDialog();
                            }
                        }
                    }

            );

        } else {
            responseListener.onErrorResponse(null);
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }

    }

    //2.9.5以后的新方法

    public void sendRequestRequestParamsNew(final String dialogMessage, final RequestParams params, boolean dialog, final ResponseListenerNew responseListener) {
        if (dialog) {
            initDialog();
            waitDialog.setMessage(dialogMessage);
            waitDialog.showDailog();
        }
        if (InternetUtils.internet(context)) {
            params.addBodyParameter("UID", ShareUitls.getString(context, "UID", "0"));
            params.addBodyParameter("ResultJWT", ShareUitls.getString(context, "ResultJWT", "0"));
            httpManager.post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    JSONObject[] jsonObjects = PublicDataClass.getPublicData(result);
                    Log.i("myblue", result);
                    PublicDataClass.MdResponse mdResponse = new Gson().fromJson(jsonObjects[0].toString(), PublicDataClass.MdResponse.class);
                    if (mdResponse.ErrCode.equals("601") || mdResponse.ErrCode.equals("600")) {
                        if (mdResponse.ErrCode.equals("601")) {
                            Toast.makeText(context, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                        }
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);
                          /*  if (MainActivity.Activity != null) {
                                MainActivity.Activity.finish();
                            }*/
                        //清除所有文件数据
                        ShareUitls.cleanSharedPreference(context);
                        //清空圈子
                        CircleList.getInstance().circlelist.clear();
                        CircleList.getInstance().commentlist.clear();
                        CircleList.getInstance().replylist.clear();
                        Glide.get(context).clearMemory();
                        context.finish();
                    } else {
                        responseListener.onResponse(jsonObjects[1].toString(), mdResponse);
                    }


                    //  Log.i("myblue", result + "'");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    Log.i("AAAAACCCAAAA", ex.toString() + "'" + isOnCallback);
                    try {
                        responseListener.onErrorResponse(ex);
                    } catch (Exception E) {
                    }


                }

                @Override
                public void onCancelled(CancelledException cex) {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                    // responseListener.onErrorResponse(new VolleyError());
                }

                @Override
                public void onFinished() {
                    if (waitDialog != null) {
                        waitDialog.dismissDialog();
                    }
                }
            });

        } else {
            responseListener.onErrorResponse(null);
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }

    }


    Gson g = new Gson();

    //三方验证登录 1 微信 2 QQ 3 新浪
    public void otherRegister(final Map<String, String> map, final String FlagActivity) {
        initDialog();
        waitDialog.setMessage("");
        waitDialog.showDailog();

        if (InternetUtils.internet(context)) {
            String version = VersonUtils.getVersionName(context);
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostThirdPartyRegisterRequest&version=v" + version);
            params.addBodyParameter("ThirdPartyID", map.get("loginflag"));
            params.addBodyParameter("ThirdPartyCode", map.get("openid"));
            //  params.addBodyParameter("VersionNum", map.get(version));
            // Toast.makeText(context, map.get("loginflag")+"---"+map.get("openid"), Toast.LENGTH_SHORT).show();
            // context.startActivity(new Intent(context, TEXT.class).putExtra("jsonObject1",map.get("openid")));
            httpManager.post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    // PublicDataClass publicDataClass = new Gson().fromJson(response.toString(), PublicDataClass.class);
                    Log.i("AAAAAAAAAphoneLogin", response);
                    UserLogin userLogin = g.fromJson(response, UserLogin.class);

                    // context.startActivity(new Intent(context, TEXT.class).putExtra("jsonObject1", response+"   "+map.get("loginflag")+"  "+map.get("openid")));
                    Intent i = new Intent();
                    User user = new User();
                    User.UserInformation userInformation = user.getUserInformation();
                    String Status = userLogin.Status;
                    switch (Status) {
                        case "5":
                            ShareUitls.putString(context, "ResultJWT", userLogin.ResultJWT);//请求头
                            ShareUitls.putString(context, "UID", userLogin.UserID);
                            Toast.makeText(context, "请验证手机号", Toast.LENGTH_SHORT).show();
                            i.setClass(context, BoundPhoneActivity.class);
                            i.putExtra("map", (Serializable) map);
                            i.putExtra("flag", "other");
                            i.putExtra("FlagActivity", FlagActivity);
                            context.startActivity(i);
                            if (HomeActivity.activity != null) {
                                HomeActivity.activity.finish();
                            }
                            break;

                        case "1":
                        case "2":
                            Toast.makeText(context, "请完善个人信息", Toast.LENGTH_SHORT).show();
                            i.setClass(context, CompleteInformationActivity.class);
                            ShareUitls.putString(context, "UID", userLogin.UserID);
                            ShareUitls.putLoginString(context, "loginFlag", map.get("loginflag"));
                            user.setUID(userLogin.UserID);
                            user.setLoginFlag(map.get("loginflag"));
                            userInformation.setNickName(map.get("nickname"));
                            userInformation.setGender(map.get("sex"));
                            userInformation.setFile(map.get("headimgurl"));
                            user.setUserInformation(userInformation);
                            switch (map.get("loginflag")) {
                                case "1":
                                    user.setChatOpenID(map.get("openid"));
                                    break;
                                case "2":
                                    user.setQQOpenID(map.get("openid"));
                                    break;
                                case "3":
                                    user.setSinaOpenID(map.get("openid"));
                                    break;
                            }
                            ShareUitls.putUser(context, user);
                            i.putExtra("flag", "yes");//有初始信息传过去
                            i.putExtra("headimgurl", map.get("headimgurl"));
                            i.putExtra("sex", map.get("sex"));
                            i.putExtra("nickname", map.get("nickname"));


                            ShareUitls.putString(context, "ResultJWT", userLogin.ResultJWT);//请求头

                            HomeActivity.upToken(context);

                            context.startActivity(i);
                            context.finish();
                            if (HomeActivity.activity != null) {
                                HomeActivity.activity.finish();
                            }
                            if (Login.activity != null) {
                                Login.activity.finish();
                            }
                            break;
                        case "3":
                            // break;
                        case "4":
                            ShareUitls.putString(context, "IsfLoginToMain", "true");
                            i.setClass(context, MainActivity.class);
                            ShareUitls.putString(context, "UID", userLogin.UserID);
                            ShareUitls.putLoginString(context, "loginFlag", map.get("loginflag"));
                            user.setUID(userLogin.UserID);
                            user.setLoginFlag(map.get("loginflag"));
                            switch (map.get("loginflag")) {
                                case "1":
                                    user.setChatOpenID(map.get("openid"));
                                    break;
                                case "2":
                                    user.setQQOpenID(map.get("openid"));
                                    break;
                                case "3":
                                    user.setSinaOpenID(map.get("openid"));
                                    break;
                            }
                            userInformation.setNickName(userLogin.NickName);
                            userInformation.setFile(userLogin.ImgUrl);
                            userInformation.setGender(userLogin.Gender);
                            userInformation.setWeight(userLogin.Weight);
                            userInformation.setHeight(userLogin.Height);
                            userInformation.setMAC(userLogin.MACAddress);
                            userInformation.setBirthday(userLogin.Birthday);
                            user.setUserInformation(userInformation);
                            ShareUitls.putUser(context, user);
                            context.startActivity(i);
                            InternetUtils.internet2(context);//检测并上传上次遗留数据
                            ShareUitls.putString(context, "ResultJWT", userLogin.ResultJWT);//请求头
                            HomeActivity.upToken(context);
                            context.finish();
                            if (Login.activity != null) {
                                Login.activity.finish();
                            }
                            if (HomeActivity.activity != null) {
                                HomeActivity.activity.finish();
                            }
                            break;

                        case "0":
                            Toast.makeText(context, "账号验证有误,请重新登录...", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, Login.class));
                            if (HomeActivity.activity != null) {
                                HomeActivity.activity.finish();
                            }
                            break;

                        default:
                            break;
                    }
                    //  }
                    //  context.startActivity(new Intent(context, TEXT.class).putExtra("jsonObject1", response));
                    waitDialog.dismissDialog();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    waitDialog.dismissDialog();
                    Toast.makeText(context, "网络异常,登录失败", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Login.class));
                    HomeActivity.activity.finish();

                }

                @Override
                public void onCancelled(CancelledException cex) {
                    waitDialog.dismissDialog();
                    // responseListener.onErrorResponse(new VolleyError());
                }

                @Override
                public void onFinished() {
                    waitDialog.dismissDialog();
                    //responseListener.onErrorResponse(new VolleyError());
                }
            });

        }

    }

    //三方账户绑定
    public void Bound(RequestParams params, final Handler handler) {
        initDialog();
        waitDialog.setMessage("");
        waitDialog.showDailog();
        httpManager.post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String response) {
                // context.startActivity(new Intent(context, TEXT.class).putExtra("jsonObject1", response + "   "));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600")) {
                        if (jsonObject.getString("ErrCode").equals("601")) {
                            Toast.makeText(context, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                        }
                        Intent i = new Intent(context, Login.class);
                        context.startActivity(i);

                      /*  if (MainActivity.Activity != null) {
                            MainActivity.Activity.finish();
                        }*/
                        //清除所有文件数据
                        ShareUitls.cleanSharedPreference(context);
                        //清空圈子
                        CircleList.getInstance().circlelist.clear();
                        CircleList.getInstance().commentlist.clear();
                        CircleList.getInstance().replylist.clear();
                        Glide.get(context).clearMemory();
                        context.finish();
                    } else {
                        String Status = jsonObject.getString("Status");
                        if (Status.equals("2")) {
                            handler.sendEmptyMessage(0);
                        } else if (Status.equals("1")) {
                            handler.sendEmptyMessage(1);

                        } else {
                            handler.sendEmptyMessage(2);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
                waitDialog.dismissDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                waitDialog.dismissDialog();

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void sendGetRequest(final String url, final ResponseListener responseListener) {


        if (InternetUtils.internet(context)) {
            RequestParams params = new RequestParams(url);
            httpManager.get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    responseListener.onResponse(result);
                    Log.i("AAAAAAAAAA", result + "'");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    responseListener.onErrorResponse(ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    waitDialog.dismissDialog();

                }

                @Override
                public void onFinished() {
                    //   responseListener.onErrorResponse(new VolleyError());
                }
            });

        }
    }
}


