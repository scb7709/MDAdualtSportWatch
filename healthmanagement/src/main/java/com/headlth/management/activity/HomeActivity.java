package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.User;
import com.headlth.management.entity.VersionClass;

import com.headlth.management.utils.Constant;

import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import com.squareup.picasso.Picasso;

import com.umeng.analytics.MobclickAgent;


import com.umeng.message.PushAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abc on 2016/7/1.
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends Activity {
    private PushAgent mPushAgent;
    private Gson g = new Gson();
    private String loginFlag;
    @ViewInject(R.id.homeactivity_ad_im)
    private ImageView AD_PNG;
    @ViewInject(R.id.homeactivity_ad_vp)
    private ViewPager viewPager;
    @ViewInject(R.id.homeactivity_countdown)
    private Button homeactivity_countdown;

    @ViewInject(R.id.homeactivity_layout)
    private LinearLayout linearLayout;
    private List<String> LoadingPagesUrl;
    public static Activity activity;
    int prePosition;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent i = new Intent(HomeActivity.this, Login.class);
            switch (msg.what) {
                case 0:
                    login(HomeActivity.this, "HomeActivity");
                    break;
                case 1:
                    Toast.makeText(HomeActivity.this, "您尚未登录，请先登录...", Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                    break;
                case 2:
                    setTimeTaskLoadingPages();
                    break;
                case 3:
                    homeactivity_countdown.setText(msg.arg1 + " 秒后进入");
                    break;
                case 4:
                    if (!loginFlag.equals("null")) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                    break;


            }


        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ShareUitls.putString(HomeActivity.this, "WATCHSPORT", "START");
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {//避免桌面点击本应用图标是再次打开APP
            finish();
            return;
        }
        initialize();
        if (InternetUtils.internet(this)) {//判断网络连接
            //  webTest();
            loginFlag = ShareUitls.getLoginString(getApplicationContext(), "loginFlag", "null");//获取登录方式 三方账户 和 迈动账户
            Log.e("loginFlag", loginFlag);
            checkVersion();//检查版本
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Looper.prepare();
                    handler.sendEmptyMessage(5);
                    Looper.loop();
                }
            }, 3000);//5000单位是毫秒=5秒


        }
    }

    private void initialize() {



        x.view().inject(this);

        activity = this;
        ShareUitls.putString(activity, "questionnaire", "1");//控制首页界面推荐内容重新刷新
        ShareUitls.putString(activity, "maidong", "1");//控制首页界面重新刷新
        ShareUitls.putString(activity, "analize", "1");//控制分析重新刷新
        ShareUitls.putString(activity, "todaydata", "{}");//
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ShareUitls.putString(activity, "CLICKDADE", format.format(new Date()));//把日历 点击 默认今天
        ShareUitls.putString(activity, "todayvideo", "");
        // ShareUitls.putString(getApplicationContext(), "TODAY", format.format(new Date()));//保存启动APP的时间 确保每天都重新登录过

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();



    }

    private void setTimeTask() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                if (!loginFlag.equals("null")) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
                Looper.loop();
            }
        }, 3000);//5000单位是毫秒=5秒
    }


    private void setTimeTaskLoadingPages() {
        Log.i("fffGAUNG", LoadingPagesUrl.toString());
        if (LoadingPagesUrl.size() == 1) {
            final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(4000, 1000);
            myCountDownTimer.start();
            AD_PNG.setVisibility(View.VISIBLE);
            homeactivity_countdown.setVisibility(View.VISIBLE);
            homeactivity_countdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myCountDownTimer.cancel();
                    if (!loginFlag.equals("null")) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            });
            if (LoadingPagesUrl.get(0).substring(LoadingPagesUrl.get(0).length() - 3, LoadingPagesUrl.get(0).length()).equals("gif")) {
                Glide.with(HomeActivity.this)
                        .load(LoadingPagesUrl.get(0))
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(AD_PNG);
            } else {
                Picasso.with(HomeActivity.this)
                        .load(LoadingPagesUrl.get(0))
                        .config(Bitmap.Config.RGB_565)//图片网址
                        .error(R.mipmap.home)
                        .placeholder(R.mipmap.home)//默认图标
                        .into(AD_PNG);//控件
            }

        } else {
            viewPager.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            final List<View> list = new ArrayList<View>();
            for (int i = 0; i < LoadingPagesUrl.size(); i++) {
                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.viewpage_homeactivity_ad_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.vierpage_homeactivity_ad_im);
                Button button = (Button) view.findViewById(R.id.vierpage_homeactivity_ad_bt);
                Button pass = (Button) view.findViewById(R.id.vierpage_homeactivity_ad_pass);
                pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!loginFlag.equals("null")) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
                if (i == LoadingPagesUrl.size() - 1) {
                    pass.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!loginFlag.equals("null")) {
                                handler.sendEmptyMessage(0);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                }

                if (LoadingPagesUrl.get(i).substring(LoadingPagesUrl.get(i).length() - 3, LoadingPagesUrl.get(i).length()).equals("gif")) {
                    Glide.with(HomeActivity.this)
                            .load(LoadingPagesUrl.get(i))
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imageView);
                } else {
                    Picasso.with(HomeActivity.this)
                            .load(LoadingPagesUrl.get(i))
                            .config(Bitmap.Config.RGB_565)//图片网址
                            .error(R.mipmap.home)
                            .placeholder(R.mipmap.home)//默认图标
                            .into(imageView);//控件
                }
                ImageView icon = new ImageView(HomeActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    lp.setMargins(15, 0, 0, 0);
                }
                icon.setLayoutParams(lp);
                icon.setBackgroundResource(R.drawable.black_icon_shape);
                linearLayout.addView(icon);
                list.add(view);
            }
            linearLayout.getChildAt(0).setBackgroundResource(R.drawable.yellow_icon_shape);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    linearLayout.getChildAt(position).setBackgroundResource(R.drawable.yellow_icon_shape);
                    linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.black_icon_shape);
                    prePosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public View instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                    container.addView(list.get(position), 0);//添加页卡
                    return list.get(position);
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView(list.get(position));//删除页卡
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }
            });
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public static void login(Activity activity, String FlagActivity) {
        String loginFlag = ShareUitls.getLoginString(activity, "loginFlag", "null");//获取登录方式 三方账户 和 迈动账户
        if (!loginFlag.equals("null")) {
            if (loginFlag.equals("0")) {
                User user = ShareUitls.getUser(activity);
                Login.phoneLogin(activity, user.getPhone(), user.getPwd(), FlagActivity);
            } else {
                Map<String, String> map = new HashMap<String, String>();
                User user = ShareUitls.getUser(activity);
                map.put("loginflag", loginFlag);
                switch (loginFlag) {
                    case "1":
                        map.put("openid", user.getChatOpenID());
                        break;
                    case "2":
                        map.put("openid", user.getQQOpenID());
                        break;
                    case "3":
                        map.put("openid", user.getSinaOpenID());
                        break;
                }
                map.put("headimgurl", user.getUserInformation().getFile());
                map.put("nickname", user.getUserInformation().getNickName());
                map.put("sex", user.getUserInformation().getGender());
                HttpUtils.getInstance(activity).otherRegister(map, FlagActivity);
            }
        } else {
            Intent intent = new Intent(activity, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    //消息推送获取DeviceToken接口
    public static void upToken(final Activity activity) {
        final String token = ShareUitls.getToken(activity, "token", "null");
        if (!token.equals("null")) {


            Log.i("ttttttttttttttt", token);
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostDeviceTokenRequest");
            params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
            params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "0"));
            params.addBodyParameter("DeviceToken", token);
            params.addBodyParameter("DeviceTokenType", "1");
            Log.e("PostDeviceTokenRequest", ShareUitls.getString(activity, "UID", "0") + " " + token);
            HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("PostDeviceTokenRequest", response.toString());
                            //    upTokenCallBack upToken = new Gson().fromJson(response.toString(), upTokenCallBack.class);

                            return;
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {
                            Log.e("PostDeviceTokenRequest", "onErrorResponse");
/*

                        Log.i("AAAAAAAAA", "HOMEgo2");
                        Toast.makeText(getApplicationContext(), "账户信息过期，请重新登录..", Toast.LENGTH_SHORT).show();
                        //  waitDialog.ShowDialog(false);
                        Intent i = new Intent(HomeActivity.this, Login.class);
                        startActivity(i);
                        finish();
*/


                        }


                    }

            );

        }
    }

    //加载网络是否有广告
    private void getLoadingPages() {

        LoadingPagesUrl = new ArrayList<String>();
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetLoadingAdImgRequest");
        HttpUtils.getInstance(HomeActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        Log.e("ffff广告aaaaa", response.toString());
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Status").equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("AdImgList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.e("ffff广告-----", Constant.BASE_URL + "/" + jsonArray.getJSONObject(i).getString("ImgUrl"));
                                    LoadingPagesUrl.add(Constant.BASE_URL + "/" + jsonArray.getJSONObject(i).getString("ImgUrl"));
                                }
                                if (LoadingPagesUrl.size() != 0) {//广告展示
                                    Log.e("ffff广告-----AAA", LoadingPagesUrl.size() + "");
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Looper.prepare();
                                            handler.sendEmptyMessage(2);
                                            Looper.loop();
                                        }
                                    }, 2000);//5000单位是毫秒=5秒
                                } else {
                                    setTimeTask();//
                                }
                            } else {
                                setTimeTask();//
                            }

                        } catch (JSONException e) {
                            setTimeTask();//
                            e.printStackTrace();
                            Log.e("ffff广告-----", "异常");
                        }
                        //   upToken = g.fromJson(response.toString(), upTokenCallBack.class);


                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                        setTimeTask();


                    }


                }

        );

    }



    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (InternetUtils.internet(HomeActivity.this)) {
                startActivity(new Intent(this, HomeActivity.class));
            }

        }

    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {

            handler.sendEmptyMessage(4);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("VVVVVVVVVVVVVV", millisUntilFinished + "");
            Message msg = new Message();
            msg.what = 3;
            msg.arg1 = (int) (millisUntilFinished / 1000);
            handler.sendMessage(msg);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(this, HomeActivity.class));
    }

   // VersionClass versionClass;

    public void checkVersion() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostVersionNewRequest");
        HttpUtils.getInstance(HomeActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        VersionClass versionClass = g.fromJson(response, VersionClass.class);
                        Log.i("版本aaaaa", response.toString());
                        if (versionClass.Status == 1) {
                            ShareUitls.putVersion(HomeActivity.this, versionClass.Version);
                        /*    if (versionClass.Version.VersionCode > VersonUtils.getVerisonCode(HomeActivity.this)) {
                                if(InternetUtils.getNetworkState(activity)==1) {//wifi提示更新
                                    UpadteApp upadteApp = new UpadteApp(activity, versionClass.Version, false, new UpadteApp.UpdateResult() {
                                        @Override
                                        public void onSuccess() {
                                        }
                                        @Override
                                        public void onError() {
                                            getLoadingPages();//获取广告信息
                                        }
                                    });
                                }
                                getLoadingPages();//获取广告信息
                            } else {
                                getLoadingPages();//获取广告信息
                            }*/

                        }/* else {
                            getLoadingPages();//获取广告信息
                        }*/
                        getLoadingPages();//获取广告信息
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        getLoadingPages();//获取广告信息

                    }


                }

        );

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // huaweiApiClient.connect();

    }

    public void webTest() {
//http://127.0.0.1:8080/ls-server/api/user?flag=register&username=test&password=123456
        RequestParams params = new RequestParams("http://127.0.0.1:8080/PunchCard/loginServlet");
        params.addBodyParameter("username", "scb");
        params.addBodyParameter("password", "cbsun");
        params.addBodyParameter("flag", "login");

        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("webTest", response.toString());
                        //    upTokenCallBack upToken = new Gson().fromJson(response.toString(), upTokenCallBack.class);

                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.e("webTestERR", "onErrorResponse");


                    }


                }

        );

    }

}
