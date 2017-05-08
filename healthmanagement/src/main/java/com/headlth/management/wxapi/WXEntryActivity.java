package com.headlth.management.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.headlth.management.activity.Login;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.VersonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 2016/8/12.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private String chatflag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, "wx7d8b93a61963d44c", false);
        api.handleIntent(getIntent(), this);


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq arg0) {
        Log.i("AAAAAAAA", "" + arg0.toString());

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("AAAAAAAAW", "" + resp.toString());
        chatflag = ShareUitls.getLoginString(WXEntryActivity.this, "chatflag", "");
        // LogManager.show(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:" + resp.errStr, 1);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (chatflag.equals("login") || chatflag.equals("AccountManage")) {
                    String code = ((SendAuth.Resp) resp).code;//需要转换一下才可以
                    getAccess_token(code);
                } else {
                    Toast.makeText(WXEntryActivity.this, "微信分享成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (chatflag.equals("login")) {
                    Toast.makeText(WXEntryActivity.this, "微信登录已取消", Toast.LENGTH_LONG).show();
                } else if (chatflag.equals("share")) {
                    Toast.makeText(WXEntryActivity.this, "微信分享已取消", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(WXEntryActivity.this, "微信授权已取消", Toast.LENGTH_LONG).show();
                }
                Login.waitDialog.ShowDialog(false);
                ;
                finish();
                break;
            default:
                if (chatflag.equals("login")|| chatflag.equals("AccountManage")) {
                    Toast.makeText(WXEntryActivity.this, "微信授权失败", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(WXEntryActivity.this, "微信分享失败", Toast.LENGTH_LONG).show();

                }
                Login.waitDialog.ShowDialog(false);
                ;
                finish();
                break;
        }
    }

    private void getAccess_token(String code) {
        HttpUtils.getInstance(this).sendGetRequest("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.CHAT_ID + "&secret=09c9b63a16ae1433787cb7581aaa2cde&code=" + code + "&grant_type=authorization_code", new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        /*
                        *
                        * "access_token":"ACCESS_TOKEN",
                        "expires_in":7200,
                        "refresh_token":"REFRESH_TOKEN",
                        "openid":"OPENID",
                        "scope":"SCOPE",
                        "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL"*/
                        Log.e("json", response);
                        String verify_code = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (chatflag.equals("AccountManage")) {//绑定微信账号
                                Bound(jsonObject.getString("openid"));
                            } else if (chatflag.equals("login")) {//微信登录
                                getUser_info(jsonObject.getString("access_token"), jsonObject.getString("openid"));
                            }

                        } catch (JSONException e) {
                            Login.waitDialog.ShowDialog(false);
                            ;
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable errorint) {
                        Login.waitDialog.ShowDialog(false);
                        ;
                        Toast.makeText(WXEntryActivity.this, "getAccess_token shibai", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

        );
    }

    private void getUser_info(String access_token, String openid) {
        HttpUtils.getInstance(this).sendGetRequest("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Login.waitDialog.ShowDialog(false);
                        ;
                        //  Toast.makeText(WXEntryActivity.this,response, Toast.LENGTH_LONG).show();
                     /*   Intent intent = new Intent(WXEntryActivity.this, CompleteInformationActivity.class);
                       intent.putExtra("headimgurl",jsonObject.getString("headimgurl"));
                        intent.putExtra("nickname",response);
                        startActivity(intent);*/
                        /*
                     "openid":"OPENID",
                        "nickname":"NICKNAME",
                        "sex":1,
                        "province":"PROVINCE",
                        "city":"CITY",
                        "country":"COUNTRY",
                        "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
                        "privilege":[
                        "PRIVILEGE1",
                        "PRIVILEGE2"
                        ],
                        "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

                        */
                        Log.e("json", response);
                        String verify_code = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            /*Intent intent = new Intent(WXEntryActivity.this, CompleteInformationActivity.class);
                            intent.putExtra("loginflag", "chat");
                            intent.putExtra("openid", jsonObject.getString("openid"));
                            intent.putExtra("headimgurl", jsonObject.getString("headimgurl"));
                            intent.putExtra("nickname", jsonObject.getString("nickname"));
                            intent.putExtra("sex", jsonObject.getString("sex"));*/

                            Map<String, String> map = new HashMap<String, String>();
                            map.put("loginflag", "1");
                            map.put("openid", jsonObject.getString("openid"));
                            map.put("headimgurl", jsonObject.getString("headimgurl"));
                            map.put("nickname", jsonObject.getString("nickname"));
                            map.put("sex", jsonObject.getString("sex"));
                            HttpUtils.getInstance(WXEntryActivity.this).otherRegister(map, "LoginActivity");


                            // startActivity(intent);

                        } catch (JSONException e) {
                            Login.waitDialog.ShowDialog(false);
                            e.printStackTrace();
                        }
                        finish();
                    }

                    @Override
                    public void onErrorResponse(Throwable errorint) {
                        Login.waitDialog.ShowDialog(false);
                        Toast.makeText(WXEntryActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }

        );
    }

    private void Bound(String ThirdPartyCode) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostThirdPartyBindingRequest");
        params.addBodyParameter("ThirdPartyID", "1");
        params.addBodyParameter("ThirdPartyCode", ThirdPartyCode);
        params.addBodyParameter("UID",ShareUitls.getString(WXEntryActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(WXEntryActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));;
        HttpUtils.getInstance(WXEntryActivity.this).Bound(params);
        finish();
    }

}
