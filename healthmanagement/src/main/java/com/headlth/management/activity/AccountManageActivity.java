package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.AccountManagerJson;
import com.headlth.management.entity.CircleList;
import com.headlth.management.sina.AccessTokenKeeper;
import com.headlth.management.sina.UsersAPI;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_accountmanage)//复用我的处方布局
public class AccountManageActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_accountmanage_phone_is)
    private Button activity_accountmanage_phone_is;
    @ViewInject(R.id.activity_accountmanage_chat_is)
    private Button activity_accountmanage_chat_is;
    @ViewInject(R.id.activity_accountmanage_sina_is)
    private Button activity_accountmanage_sina_is;
    @ViewInject(R.id.activity_accountmanage_qq_is)
    private Button activity_accountmanage_qq_is;

    @ViewInject(R.id.activity_accountmanage_phone_text)
    private TextView activity_accountmanage_phone_text;
    @ViewInject(R.id.activity_accountmanage_chat_text)
    private TextView activity_accountmanage_chat_text;
    @ViewInject(R.id.activity_accountmanage_sina_text)
    private TextView activity_accountmanage_sina_text;
    @ViewInject(R.id.activity_accountmanage_qq_text)
    private TextView activity_accountmanage_qq_text;

    private String UID;
    private AccountManagerJson accountManagerJson;
    public static com.headlth.management.clenderutil.WaitDialog waitDialog;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        UID = ShareUitls.getString(AccountManageActivity.this, "UID", "0");
        activity = this;
        // Toast.makeText(AccountManageActivity.this,UID,Toast.LENGTH_LONG).show();
        initialize();

    }

    private void initialize() {
        view_publictitle_title.setText("账户管理");
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(AccountManageActivity.this);
        initializeOtherLogin();
    }

    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_accountmanage_phone
            , R.id.activity_accountmanage_chat
            , R.id.activity_accountmanage_sina
            , R.id.activity_accountmanage_qq
            , R.id.activity_accountmanage_exit

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_accountmanage_phone:
                if (accountManagerJson != null && accountManagerJson.MobileBinding.equals("false")) {


                    startActivity(new Intent(AccountManageActivity.this, BoundPhoneActivity.class).putExtra("FlagActivity","AccountManageActivity"));
                }

                break;
            case R.id.activity_accountmanage_chat:
                if (accountManagerJson != null && accountManagerJson.WXBinding.equals("false")) {
                    LoginChat();
                }
                break;
            case R.id.activity_accountmanage_sina:
                if (accountManagerJson != null && accountManagerJson.WBBinding.equals("false")) {
                    LoginSina();
                }
                break;
            case R.id.activity_accountmanage_qq:
                if (accountManagerJson != null && accountManagerJson.QQBinding.equals("false")) {
                    LoginQQ();
                }
                break;
            case R.id.activity_accountmanage_exit:

             /*   if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }
                //清除所有文件数据
                ShareUitls.cleanSharedPreference(AccountManageActivity.this);
                //清空圈子
                CircleList.getInstance().circlelist.clear();
                CircleList.getInstance().commentlist.clear();
                CircleList.getInstance().replylist.clear();
                Glide.get(AccountManageActivity.this).clearMemory();
                Intent i = new Intent(AccountManageActivity.this, Login.class);
                startActivity(i);
                finish();*/
               exit();

                break;
        }
    }

    ///QQ
    private IWXAPI api;
    private Tencent mTencent;
    public static String openidString = "";
    private IUiListener BaseUiListener;

    // 这里是调用QQ登录的关键代码
    public void LoginQQ() {

        if (Share.isQQInstalled(AccountManageActivity.this)) {
            waitDialog.setMessage("正在获取授权,请稍后...");
            waitDialog.ShowDialog(true);
            if (!mTencent.isSessionValid()) {
                waitDialog.ShowDialog(true);
                mTencent.login(AccountManageActivity.this, "all", BaseUiListener);

            }else {
                mTencent.logout(this);
                waitDialog.ShowDialog(true);
                mTencent.login(AccountManageActivity.this, "all", BaseUiListener);
            }
        } else {
            Toast.makeText(AccountManageActivity.this, "未检测到QQ客户端", Toast.LENGTH_LONG).show();
            return;
        }

    }
//chat


    private void LoginChat() {
        ShareUitls.putLoginString(AccountManageActivity.this, "chatflag", "AccountManage");
        if (api.isWXAppInstalled()) {
            ///  waitDialog.setMessage("正在获取授权,请稍后...");
            // waitDialog.ShowDialog(true);
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);


        } else {
            Toast.makeText(this, "未检测到微信客户端", Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeOtherLogin() {
        weiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);
        api = WXAPIFactory.createWXAPI(this, Constant.CHAT_ID);
        api.registerApp(Constant.CHAT_ID);
        mTencent = Tencent.createInstance(Constant.QQ_ID, getApplicationContext());
        BaseUiListener = new IUiListener() {
            public void onCancel() {
                // 取消
                waitDialog.ShowDialog(false);
                Toast.makeText(getApplicationContext(), openidString + "QQ登录已取消", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Object response) {
                // startActivity(new Intent(AccountManageActivity.this, TEXT.class).putExtra("jsonObject1", response + "   QQ"));
                // Toast.makeText(getApplicationContext(), "QQ登录获取用户信息异常", Toast.LENGTH_SHORT).show();
                waitDialog.ShowDialog(false);
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    final String openid = jsonObject.getString("openid");
                   // startActivity(new Intent(AccountManageActivity.this, TEXT.class).putExtra("jsonObject1",openid));
                    Bound("2", openid);
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
    //sina

    private IWeiboShareAPI weiboShareAPI;
    private AuthInfo authInfo;
    private SsoHandler ssoHandler;
    private Oauth2AccessToken accessToken;

    public void LoginSina() {
        if (Share.isWeiboInstalled(AccountManageActivity.this)) {
            waitDialog.setMessage("正在获取授权,请稍后...");
            waitDialog.ShowDialog(true);
            authInfo = new AuthInfo(this, Constant.SINA_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
            ssoHandler = new SsoHandler(AccountManageActivity.this, authInfo);
            ssoHandler.authorize(new AuthListener());
        } else {
            Toast.makeText(AccountManageActivity.this, "未检测到新浪客户端", Toast.LENGTH_LONG).show();

        }

    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            waitDialog.ShowDialog(false);
            accessToken = Oauth2AccessToken.parseAccessToken(values); // 从Bundle中解析Token
            String phoneNum = accessToken.getPhoneNum();// 从这里获取用户输入的 电话号码信息

            if (accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(AccountManageActivity.this, accessToken); // 保存Token
                Toast.makeText(AccountManageActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(AccountManageActivity.this, message, Toast.LENGTH_LONG).show();

            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    UsersAPI usersAPI = new UsersAPI(AccountManageActivity.this, Constant.SINA_KEY, accessToken);
                    usersAPI.show(Long.valueOf(accessToken.getUid()), new SinaRequestListener());

                }
            }).start();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            waitDialog.ShowDialog(false);
            Toast.makeText(AccountManageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            waitDialog.ShowDialog(false);
            Toast.makeText(AccountManageActivity.this, "微博授权已取消", Toast.LENGTH_SHORT).show();
        }
    }

    public class SinaRequestListener implements RequestListener { //新浪微博请求接口

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            try {
                JSONObject jsonObject = new JSONObject(response);
                String idStr = jsonObject.getString("idstr");// 唯一标识符(uid)
                Message message = Message.obtain();
                message.what = 1;
                message.obj = idStr;
                handler.sendMessage(message);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(AccountManageActivity.this, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("mylog", "Auth exception : " + e.getMessage());
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                // startActivity(new Intent(AccountManageActivity.this, TEXT.class).putExtra("jsonObject1", msg.obj.toString() + "   sina"));
                Bound("3", msg.obj.toString());
                waitDialog.ShowDialog(false);
            }
        }
    };


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


    private void getRequest() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetUserBindingListRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(AccountManageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", UID);
        HttpUtils.getInstance(AccountManageActivity.this).sendRequestRequestParams("  ", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("GGGGGGGGGGGG", response);
                        accountManagerJson = new Gson().fromJson(response, AccountManagerJson.class);
                        if (accountManagerJson.MobileBinding.equals("true")) {
                            activity_accountmanage_phone_is.setVisibility(View.GONE);
                            activity_accountmanage_phone_text.setVisibility(View.VISIBLE);

                        }
                        if (accountManagerJson.WXBinding.equals("true")) {
                            activity_accountmanage_chat_is.setVisibility(View.GONE);
                            activity_accountmanage_chat_text.setVisibility(View.VISIBLE);
                        }
                        if (accountManagerJson.WBBinding.equals("true")) {
                            activity_accountmanage_sina_is.setVisibility(View.GONE);
                            activity_accountmanage_sina_text.setVisibility(View.VISIBLE);
                        }
                        if (accountManagerJson.QQBinding.equals("true")) {
                            activity_accountmanage_qq_is.setVisibility(View.GONE);
                            activity_accountmanage_qq_text.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(AccountManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }


    private void Bound(String ThirdPartyID, String ThirdPartyCode) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostThirdPartyBindingRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(AccountManageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", UID);
        params.addBodyParameter("ThirdPartyID", ThirdPartyID);
        params.addBodyParameter("ThirdPartyCode", ThirdPartyCode);
        params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));
        HttpUtils.getInstance(AccountManageActivity.this).Bound(params, boundhandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        waitDialog.ShowDialog(false);
        getRequest();
    }


    private void exit() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserExitRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(AccountManageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", UID);
        HttpUtils.getInstance(AccountManageActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("adsfdsgdfg", response);

                        if (MainActivity.activity != null) {
                            MainActivity.activity.finish();
                        }
                        //清除所有文件数据
                        ShareUitls.cleanSharedPreference(AccountManageActivity.this);
                        //清空圈子
                        CircleList.getInstance().circlelist.clear();
                        CircleList.getInstance().commentlist.clear();
                        CircleList.getInstance().replylist.clear();
                        Glide.get(AccountManageActivity.this).clearMemory();
                        Intent i = new Intent(AccountManageActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        if (MainActivity.activity != null) {
                            MainActivity.activity.finish();
                        }
                        //清除所有文件数据
                        ShareUitls.cleanSharedPreference(AccountManageActivity.this);
                        //清空圈子
                        CircleList.getInstance().circlelist.clear();
                        CircleList.getInstance().commentlist.clear();
                        CircleList.getInstance().replylist.clear();
                        Glide.get(AccountManageActivity.this).clearMemory();
                        Intent i = new Intent(AccountManageActivity.this, Login.class);
                        startActivity(i);
                        finish();
                        //Toast.makeText(AccountManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    Handler boundhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(AccountManageActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    getRequest();
                    break;
                case 1:
                    Toast.makeText(AccountManageActivity.this, "该账户已被其他账户绑定", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(AccountManageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    };
}
