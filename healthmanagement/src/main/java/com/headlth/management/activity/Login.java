package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.entity.User;
import com.headlth.management.entity.UserLogin;
import com.headlth.management.sina.UsersAPI;
import com.headlth.management.sina.AccessTokenKeeper;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.Encryption;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
/*import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;*/
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/11.
 */
@ContentView(R.layout.activity_login)
public class Login extends Activity {
    @ViewInject(R.id.login_imageView)
    private ImageView login_imageView;


    @ViewInject(R.id.login_register_text)
    private TextView login_register_text;
    @ViewInject(R.id.login_login_text)
    private TextView login_login_text;

    @ViewInject(R.id.login_register_icon)
    private ImageView login_register_icon;
    @ViewInject(R.id.login_login_icon)
    private ImageView login_login_icon;


    @ViewInject(R.id.login_et_pwd_layout)
    private LinearLayout login_et_pwd_layout;

    @ViewInject(R.id.login_et_pwd_line)
    private View login_et_pwd_line;


    @ViewInject(R.id.login_bt_login)
    private Button login_bt_login;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_pwd)
    private EditText et_pwd;
    @ViewInject(R.id.login_forgetPwd)
    private TextView login_forgetPwd;
    @ViewInject(R.id.login_qq)
    private ImageView login_qq;
    @ViewInject(R.id.login_weixin)
    private ImageView login_weixinimg;
    @ViewInject(R.id.login_sina)
    // private LoginButton login_sina;

    private App app;
    String url;
    ;
    String token;
    private boolean loginOrregister;//点击的是注册还是登录  默认登录（false）
    public static com.headlth.management.clenderutil.WaitDialog waitDialog;
    private String loginFlag;
    private User user;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        PushAgent.getInstance(this).onAppStart();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ShareUitls.putString(Login.this, "CLICKDADE", format.format(new Date()));//把日历 点击 默认今天
        //ShareUitls.putString(getApplicationContext(), "TODAY", format.format(new Date()));//保存启动APP的时间 确保每天都重新登录过
     /*   if (InternetUtils.internet2(this)) {

        }*/
        try {//低版本找不到SO文件
            initializeOtherLogin();
        } catch (Exception e) {
        }
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(Login.this);
        url = Constant.BASE_URL;
        Log.e("urllll", url + "logurllll");

        user = ShareUitls.getUser(Login.this);
        token = ShareUitls.getString(getApplicationContext(), "token", "null");
        if (user != null) {
            et_phone.setText(user.getPhone());
            et_pwd.setText(user.getPwd());
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        loginFlag = ShareUitls.getLoginString(getApplicationContext(), "loginFlag", "null");
    }

    //登录
    private void Login() {
        if (et_phone.getText().toString().trim().length() == 0 || et_pwd.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            phoneLogin(et_phone.getText().toString().trim(), et_pwd.getText().toString().trim());
        }
    }

    //注册
    private void Register() {
        String phone = et_phone.getText().toString();
        if (phone.length() == 0) {
            Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (phone.length() != 11) {
                Toast.makeText(getApplicationContext(), "手机号必须11位", Toast.LENGTH_SHORT).show();
            } else {
                if (!isMobile(phone)) {
                    Toast.makeText(getApplicationContext(), "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else {

                    getSMS(phone);


                }
            }
        }
    }

    /*  "UserID": "4",
       "Mobile": "13811381138",
               "NickName": "802280000",
               "Gender": "2",
               "Birthday": "1990/7/25 0:00:00",
               "Height": "163",
               "Weight": "65",
               "ImgUrl": "",
               "Status": 2,
               "Message": "登录成功，该用户有处方",
               "IsSuccess": true,
               "IsError": false,
               "ErrMsg": null,
               "ErrCode": null*/
    private void phoneLogin(final String phone, final String pwd) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserLoginRequest");
        params.addBodyParameter("Mobile", phone);
        params.addBodyParameter("Pwd", Encryption.decode(Encryption.encodeByMD5(pwd).toString()));
        HttpUtils.getInstance(Login.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        String token = ShareUitls.getString(getApplicationContext(), "token", "");
                        Log.i("AAAAAAAAAphoneLogin", response);
                        UserLogin userLogin = g.fromJson(response.toString(), UserLogin.class);
                        Intent i = new Intent();
                        User user = new User();
                        String IsSuccess = userLogin.IsSuccess;
                        if (IsSuccess.equals("true")) {
                            ShareUitls.putString(Login.this, "ResultJWT", userLogin.ResultJWT);//请求头

                            String Status = userLogin.Status;
                            switch (Status) {
                                case "1":
                                    Toast.makeText(Login.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                                    i.setClass(Login.this, CompleteInformationActivity.class);
                                    ShareUitls.putString(Login.this, "UID", userLogin.UserID);
                                    ShareUitls.putLoginString(Login.this, "loginFlag", "0");
                                    user.setUID(userLogin.UserID);
                                    user.setLoginFlag("0");
                                    user.setPhone(phone);
                                    user.setPwd(pwd);
                                    ShareUitls.putUser(Login.this, user);
                                    i.putExtra("flag", "no");

                                    startActivity(i);

                                    if (token.length() != 0) {
                                        HomeActivity.upToken(token, Login.this);
                                    }
                                    break;
                                case "2":
                                case "3":

                                    if (MainActivity.Activity != null) {
                                        MainActivity.Activity.finish();
                                    }
                                    i.setClass(Login.this, MainActivity.class);
                                    ShareUitls.putString(Login.this, "UID", userLogin.UserID);
                                    ShareUitls.putLoginString(Login.this, "loginFlag", "0");
                                    user.setUID(userLogin.UserID);
                                    user.setLoginFlag("0");
                                    user.setPhone(phone);
                                    user.setPwd(pwd);
                                    User.UserInformation userInformation = user.getUserInformation();
                                    userInformation.setNickName(userLogin.NickName);
                                    userInformation.setFile(userLogin.ImgUrl);
                                    userInformation.setGender(userLogin.Gender);
                                    userInformation.setWeight(userLogin.Weight);
                                    userInformation.setHeight(userLogin.Height);
                                    userInformation.setBirthday(userLogin.Birthday);
                                    user.setUserInformation(userInformation);
                                    ShareUitls.putUser(Login.this, user);


                                    if (token.length() != 0) {
                                        HomeActivity.upToken(token, Login.this);//上传友盟token
                                    }
                                    InternetUtils.internet2(Login.this);//检测并上传上次遗留数据
                                    startActivity(i);
                                    finish();
                                    break;
                                case "0":
                                    Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case "4":

                                    break;
                                default:
                                    break;
                            }
                        } else {
                            Toast.makeText(Login.this, "手机号或者密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "HOMEupToken");

                        Toast.makeText(Login.this, "网络异常", Toast.LENGTH_SHORT).show();


                    }
                }
        );
    }

    Gson g = new Gson();
    public Handler hh = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* if (InternetUtils.internet(Login.this)) {
            startActivity(new Intent(this, Login.class));
        }*/
    }

    @Event(value = {R.id.login_register, R.id.login_login, R.id.login_bt_login, R.id.login_qq, R.id.login_weixin, R.id.login_sina, R.id.login_forgetPwd})
    private void getEvent(View view) {
        switch (view.getId()) {

            case R.id.login_register:
                loginOrregister = true;
                login_login_text.setTextColor(Color.parseColor("#666666"));
                login_register_text.setTextColor(Color.WHITE);
                login_register_icon.setVisibility(View.VISIBLE);
                login_login_icon.setVisibility(View.GONE);

                login_et_pwd_layout.setVisibility(View.GONE);
                login_et_pwd_line.setVisibility(View.GONE);
                login_forgetPwd.setVisibility(View.INVISIBLE);
                login_bt_login.setText("获取验证码");
                break;
            case R.id.login_login:
                loginOrregister = false;
                login_register_text.setTextColor(Color.parseColor("#666666"));
                login_login_text.setTextColor(Color.WHITE);
                login_register_icon.setVisibility(View.GONE);
                login_login_icon.setVisibility(View.VISIBLE);
                login_et_pwd_layout.setVisibility(View.VISIBLE);
                login_forgetPwd.setVisibility(View.VISIBLE);
                login_et_pwd_line.setVisibility(View.VISIBLE);
                login_bt_login.setText("登录");
                break;

            case R.id.login_bt_login:
                //   ZIP.UnZipFolder(Environment.getExternalStorageDirectory().getAbsolutePath() +"/text/AndroidLogCollector-master.zip",Environment.getExternalStorageDirectory().getAbsolutePath() +"/text");

               /* String  i=null;
                if(i.equals("")){}*/
                if (loginOrregister) {
                    Register();
                } else {
                    Login();
                }
                break;

            case R.id.login_qq:
                LoginQQ();
                break;
            case R.id.login_weixin:
                LoginChat();
                break;
            case R.id.login_sina:
                LoginSina();
                break;
            case R.id.login_forgetPwd:
                startActivity(new Intent(Login.this, FindPassWordActivity.class));
                break;

        }
    }

    IWXAPI api;

    private void LoginChat() {
        ShareUitls.putLoginString(Login.this, "chatflag", "login");
        if (loginFlag.equals("1")) {
            Map<String, String> map = new HashMap<String, String>();
            User user = ShareUitls.getUser(Login.this);
            map.put("loginflag", loginFlag);
            map.put("openid", user.getChatOpenID());
            map.put("headimgurl", user.getUserInformation().getFile());
            map.put("nickname", user.getUserInformation().getNickName());
            map.put("sex", user.getUserInformation().getGender());
            HttpUtils.getInstance(Login.this).otherRegister(map, "HomeActivity");
        } else {
            if (!api.isWXAppInstalled()) {
                Toast.makeText(this, "未检测到微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
            waitDialog.setMessage("正在获取授权,请稍后...");
            waitDialog.ShowDialog(true);
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            ShareUitls.putLoginString(Login.this, "chatflag", "login");
            // ShareUitls.putLoginString(Login.this, "chatflag", "login");
            api.sendReq(req);
        }
    }

    private Tencent mTencent;
    public static String openidString = "";
    private IUiListener BaseUiListener;

    // 这里是调用QQ登录的关键代码
    public void LoginQQ() {
        if (loginFlag.equals("2")) {
            Map<String, String> map = new HashMap<String, String>();
            User user = ShareUitls.getUser(Login.this);
            map.put("loginflag", loginFlag);
            map.put("openid", user.getQQOpenID());
            map.put("headimgurl", user.getUserInformation().getFile());
            map.put("nickname", user.getUserInformation().getNickName());
            map.put("sex", user.getUserInformation().getGender());
            HttpUtils.getInstance(Login.this).otherRegister(map, "HomeActivity");
        } else {
            if (Share.isQQInstalled(Login.this)) {
                waitDialog.setMessage("正在获取授权,请稍后...");
                waitDialog.ShowDialog(true);
                if (!mTencent.isSessionValid()) {
                   /* if(!ShareUitls.getString(Login.this,"QQopenid","").equals("")){
                        mTencent.setOpenId(ShareUitls.getString(Login.this,"QQopenid",""));
                        mTencent.setAccessToken(ShareUitls.getString(Login.this,"QQaccess_token",""),      ( (System.currentTimeMillis() + Long.parseLong(ShareUitls.getString(Login.this,"QQexpires_in","")) * 1000) + ""));
                    }*/
                    mTencent.login(Login.this, "all", BaseUiListener);
                }
            } else {
                Toast.makeText(Login.this, "未检测到QQ客户端", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }


    private void initializeOtherLogin() {
        weiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);
        api = WXAPIFactory.createWXAPI(this, Constant.CHAT_ID);
        api.registerApp(Constant.CHAT_ID);
        mTencent = Tencent.createInstance(Constant.QQ_ID, getApplicationContext());

      /*  mTencent.setAccessToken(sharedPreferences.getString("token", " "),expires );
        mTencent.setOpenId(sharedPreferences.getString("openid", ""));*/

        BaseUiListener = new IUiListener() {
            public void onCancel() {
                // 取消
                waitDialog.ShowDialog(false);
                Toast.makeText(getApplicationContext(), openidString + "QQ登录已取消", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Object response) {
                // startActivity(new Intent(Login.this, TEXT.class).putExtra("jsonObject1", response + "   "));
                // Toast.makeText(getApplicationContext(), "QQ登录获取用户信息异常", Toast.LENGTH_SHORT).show();
                waitDialog.ShowDialog(false);
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    final String openid = jsonObject.getString("openid");
                    final String access_token = jsonObject.getString("access_token");
                    final String expires_in = (System.currentTimeMillis() + Long.parseLong(jsonObject.getString("expires_in")) * 1000) + "";
                    String pf = jsonObject.getString("pf");
                    String pfkey = jsonObject.getString("pfkey");
                    String pay_token = jsonObject.getString("pay_token");
                    String ret = jsonObject.getString("ret");

                    mTencent.setOpenId(openid);
                    mTencent.setAccessToken(access_token, expires_in);

            /*        ShareUitls.putString(Login.this,"QQopenid",openid);
                    ShareUitls.putString(Login.this,"QQaccess_token",access_token);
                    ShareUitls.putString(Login.this,"QQexpires_in",jsonObject.getString("expires_in"));*/


                    QQToken qqToken = mTencent.getQQToken();
                    UserInfo info = new UserInfo(Login.this, qqToken);

                    info.getUserInfo(new IUiListener() {
                        public void onComplete(final Object response) {
                            //startActivity(new Intent(Login.this, TEXT.class).putExtra("jsonObject1", response.toString() + "   "));
                            try {
                                JSONObject jsonObject1 = (JSONObject) response;
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("loginflag", "2");
                                map.put("openid", openid);
                                map.put("headimgurl", jsonObject1.getString("figureurl_qq_1"));
                                map.put("nickname", jsonObject1.getString("nickname"));
                                map.put("sex", jsonObject1.getString("gender"));
                                HttpUtils.getInstance(Login.this).otherRegister(map, "LoginActivity");

                                //   JSONObject jsonObject1 = (JSONObject) response;
                                //  Intent intent = new Intent(Login.this, TEXT.class);
                                //  intent.putExtra("jsonObject1", jsonObject1.toString());
                              /*  intent.putExtra("headimgurl", jsonObject1.getString("figureurl_qq_1"));
                                intent.putExtra("nickname", jsonObject1.getString("nickname"));
                                intent.putExtra("sex", jsonObject1.getString("gender"));
                                intent.putExtra("loginflag", "QQ");
                                intent.putExtra("openid", openid);

                                ShareUitls.putLoginString(Login.this,"QQopenid",openid);
                                ShareUitls.putLoginString(Login.this,"QQaccess_token",access_token);
                                ShareUitls.putLoginString(Login.this,"QQexpires_in",expires_in);
                                ShareUitls.putLoginString(Login.this, "loginFlag", "QQ");
                                */

                                //  startActivity(intent);
                                // Toast.makeText(getApplicationContext(), "QQ登录成功", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "QQ登录获取用户信息异常", Toast.LENGTH_SHORT).show();

                            }
                        }

                        public void onCancel() {


                        }

                        public void onError(UiError arg0) {
                            Toast.makeText(getApplicationContext(), "QQ登录获取用户信息失败", Toast.LENGTH_SHORT).show();
                        }

                    });

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

            public void onError(UiError arg0) {
                waitDialog.ShowDialog(false);
                Toast.makeText(getApplicationContext(), "QQ登录异常", Toast.LENGTH_SHORT).show();

            }
        };
    }

    /* private AuthListener mLoginListener;
     private AuthInfo mAuthInfo;*/
    private IWeiboShareAPI weiboShareAPI;
    private AuthInfo authInfo;
    private SsoHandler ssoHandler;
    private Oauth2AccessToken accessToken;

    public void LoginSina() {
        if (loginFlag.equals("3")) {
            Map<String, String> map = new HashMap<String, String>();
            User user = ShareUitls.getUser(Login.this);
            map.put("loginflag", loginFlag);
            map.put("openid", user.getSinaOpenID());
            map.put("headimgurl", user.getUserInformation().getFile());
            map.put("nickname", user.getUserInformation().getNickName());
            map.put("sex", user.getUserInformation().getGender());
            HttpUtils.getInstance(Login.this).otherRegister(map, "HomeActivity");
        } else {

            //  if (Share.isWeiboInstalled(Login.this)) {
            waitDialog.setMessage("正在获取授权,请稍后...");//微博登录 网页验证无需客户端
            waitDialog.ShowDialog(true);
            authInfo = new AuthInfo(this, Constant.SINA_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
            ssoHandler = new SsoHandler(Login.this, authInfo);
            ssoHandler.authorize(new AuthListener());
/*
            } else {
                Toast.makeText(Login.this, "未检测到新浪客户端", Toast.LENGTH_LONG).show();
                return;
            }*/
        }

    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            waitDialog.ShowDialog(false);
           /* Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {

                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);


               AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }*/

            accessToken = Oauth2AccessToken.parseAccessToken(values); // 从Bundle中解析Token
            String phoneNum = accessToken.getPhoneNum();// 从这里获取用户输入的 电话号码信息

            if (accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(Login.this, accessToken); // 保存Token
                Toast.makeText(Login.this, "授权成功", Toast.LENGTH_SHORT).show();
            } else {

                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    UsersAPI usersAPI = new UsersAPI(Login.this, Constant.SINA_KEY, accessToken);
                    usersAPI.show(Long.valueOf(accessToken.getUid()), new SinaRequestListener());

                }
            }).start();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            waitDialog.ShowDialog(false);
            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            waitDialog.ShowDialog(false);
            Toast.makeText(Login.this, "登录已取消", Toast.LENGTH_SHORT).show();
        }
    }

    public class SinaRequestListener implements RequestListener { //新浪微博请求接口

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            try {

                JSONObject jsonObject = new JSONObject(response);
                // Intent intent = new Intent(Login.this, TEXT.class);
                //  intent.putExtra("jsonObject1", jsonObject.toString());

                String idStr = jsonObject.getString("idstr");// 唯一标识符(uid)
                String id = jsonObject.getString("id");// 唯一标识符(uid)
                String sex = jsonObject.getString("gender");// 性别
                String name = jsonObject.getString("name");// 姓名
                String avatarHd = jsonObject.getString("avatar_hd");// 头像


                Message message = Message.obtain();
                message.what = 1;
                String str[] = {idStr, name, sex, avatarHd, id};
                message.obj = str;
                handler.sendMessage(message);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(Login.this, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("mylog", "Auth exception : " + e.getMessage());
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String str[] = (String[]) (msg.obj);
        /*     Intent intent = new Intent(Login.this, CompleteInformationActivity.class);
                intent.putExtra("headimgurl", str[3]);
                intent.putExtra("nickname", str[1]);
                intent.putExtra("sex", str[2]);
                intent.putExtra("loginflag", "sina");
                intent.putExtra("openid", str[0]);*/

                Map<String, String> map = new HashMap<String, String>();
                map.put("loginflag", "3");
                map.put("openid", str[0]);
                map.put("headimgurl", str[3]);
                map.put("nickname", str[1]);
                map.put("sex", str[2]);


                HttpUtils.getInstance(Login.this).otherRegister(map, "LoginActivity");
                // startActivity(intent);
                waitDialog.ShowDialog(false);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            List<Activity> activities = ((App) getApplicationContext()).activities;
            for (Activity act : activities) {
                act.finish();// 显式结束
            }

        }
        return super.onKeyDown(keyCode, event);

    }


    //手机号判断
    public boolean isMobile(String mobiles) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void getSMS(final String phone) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetSendMessageRequest");
       /* params.addBodyParameter("ResultJWT",ShareUitls.getString(BoundPhoneActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(BoundPhoneActivity.this, "UID", "0"));*/
        params.addBodyParameter("Mobile", phone);
        HttpUtils.getInstance(Login.this).sendRequestRequestParams("正在获取手机验证码,请稍后...", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSMSjson", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String IsSuccess = jsonObject.getString("IsSuccess");
                            if (IsSuccess.equals("true")) {
                                String Status = jsonObject.getString("Status");
                                Intent intent = new Intent(Login.this, SetPassWordActivity.class);
                                intent.putExtra("verify_code", Status);
                                intent.putExtra("flag", "Register");
                                intent.putExtra("phone", phone);
                                ShareUitls.putString(Login.this, "SMSTIME", new Date().getTime() + "");
                                startActivity(intent);

                            } else {
                                Toast.makeText(Login.this, "获取获取码失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getApplicationContext(), "获取获取码失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, BaseUiListener);
        }
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
