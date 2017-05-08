package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
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
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.entity.User;
import com.headlth.management.entity.UserLogin;
import com.headlth.management.entity.VersionClass;
import com.headlth.management.entity.logcallback;
import com.headlth.management.entity.upTokenCallBack;
import com.headlth.management.myview.NumberProgressBar;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.Encryption;

import com.headlth.management.utils.FileViewer;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.VersonUtils;

import com.squareup.picasso.Picasso;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import com.umeng.message.PushAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
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
    private logcallback log;
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
                    login();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "您尚未登录，请先登录...", Toast.LENGTH_LONG).show();
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
        x.view().inject(this);


/*
        long sdsize[] = FileViewer.getSDSpace(HomeActivity.this);
        Log.e("aaaaaaaaaccc", sdsize[0] + "" + sdsize[1] + "'  ");*/
        activity = this;
        ShareUitls.putString(activity, "questionnaire", "1");//首页界面推荐内容重新刷新 (打完题)
        ShareUitls.putString(activity, "maidong", "1");//首页界面重新刷新 (新数据)
        ShareUitls.putString(activity, "analize", "1");//分析重新刷新
        ShareUitls.putString(activity, "todaydata", "{}");//
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ShareUitls.putString(HomeActivity.this, "CLICKDADE", format.format(new Date()));//把日历 点击 默认今天
        ShareUitls.putString(getApplicationContext(), "TODAY", format.format(new Date()));//保存启动APP的时间 确保每天都重新登录过
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        FeedbackAgent agent = new FeedbackAgent(this);//
        agent.sync();
        if (InternetUtils.internet(this)) {//判断网络连接
            //反馈消息提醒
        /*推送*/
            loginFlag = ShareUitls.getLoginString(getApplicationContext(), "loginFlag", "null");//获取登录方式 三方账户 和 迈动账户
            Log.e("loginFlag", loginFlag);
            //  getRegistrationId();
           checkVersion();//检查版本
            //   phoneLogin(true);
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

    @Override
    protected void onStart() {
        super.onStart();

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

    private void login() {

        if (loginFlag.equals("0")) {
            phoneLogin();
        } else {
            Map<String, String> map = new HashMap<String, String>();
            User user = ShareUitls.getUser(HomeActivity.this);
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
            HttpUtils.getInstance(HomeActivity.this).otherRegister(map, "HomeActivity");
        }
    }

    private void phoneLogin() {
        final User user = ShareUitls.getUser(HomeActivity.this);
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserLoginRequest");
        params.addBodyParameter("Mobile", user.getPhone());
        params.addBodyParameter("Pwd", Encryption.decode(Encryption.encodeByMD5(user.getPwd()).toString()));
        HttpUtils.getInstance(HomeActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAAphoneLogin", response);
                        UserLogin userLogin = g.fromJson(response.toString(), UserLogin.class);
                        Intent i = new Intent();
                        User u = new User();
                        String IsSuccess = userLogin.IsSuccess;
                        if (IsSuccess.equals("true")) {
                            ShareUitls.putString(HomeActivity.this, "ResultJWT", userLogin.ResultJWT);//请求头
                            String Status = userLogin.Status;
                            switch (Status) {
                                case "1":
                                    Toast.makeText(HomeActivity.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                                    i.setClass(HomeActivity.this, CompleteInformationActivity.class);
                                    i.putExtra("flag", "no");//没有初始信息传过去
                                    startActivity(i);

                                    break;
                                case "2":
                                case "3":
                                    i.setClass(HomeActivity.this, MainActivity.class);
                                    ShareUitls.putString(HomeActivity.this, "UID", userLogin.UserID);
                                    ShareUitls.putLoginString(HomeActivity.this, "loginFlag", "0");

                                    u.setUID(userLogin.UserID);
                                    u.setLoginFlag("0");
                                    u.setPhone(user.getPhone());
                                    u.setPwd(u.getPwd());
                                    User.UserInformation userInformation = u.getUserInformation();
                                    userInformation.setNickName(userLogin.NickName);
                                    userInformation.setFile(userLogin.ImgUrl);
                                    userInformation.setGender(userLogin.Gender);
                                    userInformation.setWeight(userLogin.Weight);
                                    userInformation.setHeight(userLogin.Height);
                                    userInformation.setBirthday(userLogin.Birthday);
                                    u.setUserInformation(userInformation);
                                    ShareUitls.putUser(HomeActivity.this, u);
                                    String token = ShareUitls.getString(getApplicationContext(), "token", "");
                                    if (token.length() != 0) {
                                        upToken(token, HomeActivity.this);
                                    }
                                    InternetUtils.internet2(HomeActivity.this);//检测并上传上次遗留数据
                                    startActivity(i);
                                    break;
                                case "0":
                                case "4":

                                default:
                                    break;
                            }
                        } else {
                            Log.i("AAAAAAAAASS", userLogin.toString());
                            Toast.makeText(HomeActivity.this, "账户信息过期，请重新登录..", Toast.LENGTH_SHORT).show();
                            i.setClass(HomeActivity.this, Login.class);
                            startActivity(i);
                        }
                        finish();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "HOMEupToken");

                        Toast.makeText(HomeActivity.this, "账户信息过期，请重新登录..", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(HomeActivity.this, Login.class);
                        startActivity(i);
                        finish();
                        return;

                    }
                }
        );
    }

    private void phoneLogin(boolean test) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserLoginRequest");
        params.addBodyParameter("Mobile", "18611347002");
        params.addBodyParameter("Pwd", Encryption.decode(Encryption.encodeByMD5("123456").toString()));
        HttpUtils.getInstance(HomeActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAAphoneLogin", response);
                        UserLogin userLogin = g.fromJson(response.toString(), UserLogin.class);
                        Intent i = new Intent();
                        User u = new User();
                        String IsSuccess = userLogin.IsSuccess;
                        if (IsSuccess.equals("true")) {
                            ShareUitls.putString(HomeActivity.this, "ResultJWT", userLogin.ResultJWT);//请求头
                            String Status = userLogin.Status;
                            switch (Status) {
                                case "1":
                                    Toast.makeText(HomeActivity.this, "请完善个人信息", Toast.LENGTH_SHORT).show();
                                    i.setClass(HomeActivity.this, CompleteInformationActivity.class);
                                    i.putExtra("flag", "no");//没有初始信息传过去
                                    startActivity(i);

                                    break;
                                case "2":
                                case "3":
                                    i.setClass(HomeActivity.this, MainActivity.class);
                                    ShareUitls.putString(HomeActivity.this, "UID", userLogin.UserID);
                                    ShareUitls.putLoginString(HomeActivity.this, "loginFlag", "0");

                                    u.setUID(userLogin.UserID);
                                    u.setLoginFlag("0");
                                    u.setPhone("18611347002");
                                    u.setPwd(u.getPwd());
                                    User.UserInformation userInformation = u.getUserInformation();
                                    userInformation.setNickName(userLogin.NickName);
                                    userInformation.setFile(userLogin.ImgUrl);
                                    userInformation.setGender(userLogin.Gender);
                                    userInformation.setWeight(userLogin.Weight);
                                    userInformation.setHeight(userLogin.Height);
                                    userInformation.setBirthday(userLogin.Birthday);
                                    u.setUserInformation(userInformation);
                                    ShareUitls.putUser(HomeActivity.this, u);
                                    startActivity(i);

                                    String token = ShareUitls.getString(getApplicationContext(), "token", "");
                                    if (token.length() != 0) {
                                        upToken(token, HomeActivity.this);
                                    }
                                    break;
                                case "0":
                                case "4":

                                default:
                                    break;
                            }
                        } else {
                            Log.i("AAAAAAAAASS", userLogin.toString());
                            Toast.makeText(HomeActivity.this, "账户信息过期，请重新登录..", Toast.LENGTH_SHORT).show();
                            i.setClass(HomeActivity.this, Login.class);
                            startActivity(i);
                        }
                        finish();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "HOMEupToken");

                        Toast.makeText(HomeActivity.this, "账户信息过期，请重新登录..", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(HomeActivity.this, Login.class);
                        startActivity(i);
                        finish();
                        return;

                    }
                }
        );
    }
    //消息推送获取DeviceToken接口
    //请求地址：http://www.ssp365.com:8066/MdMobileService.ashx?do=PostDeviceTokenRequest
    // static upTokenCallBack upToken;

    public static void upToken(final String token, final Activity activity) {
        Log.i("ttttttttttttttt", token);
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostDeviceTokenRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "0"));
        params.addBodyParameter("DeviceToken", token);
        params.addBodyParameter("DeviceTokenType", "1");

        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffff", response.toString());
                        upTokenCallBack upToken = new Gson().fromJson(response.toString(), upTokenCallBack.class);
                        if (upToken.getStatus() == 1) {
                            ShareUitls.putString(activity, "token", token);
                            return;
                        } else {
                            ShareUitls.putString(activity, "token", "");

                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
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
                            setTimeTask();
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

        // http://192.168.0.250:8082/
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("/");
        for (int j = 0; j < temp.length; j++) {
            Log.e("ffff", temp[j]);
        }
        return temp[0] + "-" + second(Integer.parseInt(temp[1])) + "-" + second(Integer.parseInt(temp[2]));
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

    VersionClass versionClass;

    public void checkVersion() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostVersionNewRequest");
        HttpUtils.getInstance(HomeActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING, params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        versionClass = g.fromJson(response, VersionClass.class);
                        JSONObject jsonObject;
                        Log.e("版本aaaaa", response.toString());
                        if (versionClass.Status == 1) {
                            ShareUitls.putVersion(HomeActivity.this, versionClass.Version);
                            if (versionClass.Version.VersionCode > VersonUtils.getVerisonCode(HomeActivity.this)) {


                                downloaddialog();
                                downloadFile(versionClass.Version.DownloadUrl);

                                //    showUpdateDialog();//提示下载新版本


                            } else {
                                getLoadingPages();//获取广告信息
                            }

                        } else {
                            getLoadingPages();//获取广告信息
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        getLoadingPages();//获取广告信息

                    }


                }

        );

    }


    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + versionClass.Version.VersionName);

        String[] Description = versionClass.Version.Description.split(";");

        String description = "";
        for (int i = 0; i < Description.length; i++) {
            description += Description[i] + ";\n";
        }
        builder.setMessage(description);
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 开始更新
//                        System.out.println("开始下载apk");
                        downloaddialog();
                        downloadFile(versionClass.Version.DownloadUrl);
                    }
                });

        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getLoadingPages();//获取广告信息
                    }
                }
        );
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 下载apk
     */
    private PopupWindow popupWindow;
    ;//下载的对话框
    private SeekBar progressBar;//下载进度条
    private TextView downtext;//下载进度条
    HttpManager httpManager;
    NumberProgressBar numberProgressBar;

    protected void downloaddialog() {


        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_download, null);
        popupWindow = new PopupWindow(view, ImageUtil.dp2px(this, 300), ImageUtil.dp2px(this, 400), true);
        progressBar = (SeekBar) view.findViewById(R.id.dialog_download_progress);
        // numberProgressBar = (NumberProgressBar) view.findViewById(R.id.dialog_download_NumberProgressBar);


        downtext = (TextView) view.findViewById(R.id.dialog_download_tv);
        TextView dialog_download_Description = (TextView) view.findViewById(R.id.dialog_download_Description);
        TextView dialog_download_versionname = (TextView) view.findViewById(R.id.dialog_download_versionname);
        String[] Description = versionClass.Version.Description.split(";");
        String description = "";
        for (int i = 0; i < Description.length; i++) {
            description += Description[i] + ";\n\n";
        }
        dialog_download_Description.setText(description);

        dialog_download_versionname.setText(versionClass.Version.VersionName);
        Button cancel = (Button) view.findViewById(R.id.dialog_download__cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (httpManager != null) {
                }

                getLoadingPages();//获取广告信息
            }
        });


        popupWindow.setTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(new View(this), Gravity.CENTER, 0, 0);
    }

    private void downloadFile(final String url) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "sdcard不存在!", Toast.LENGTH_SHORT).show();
            getLoadingPages();//获取广告信息
            return;
        }

        // 文件在sdcard的路径
        File tempfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/apk/" + new Date().getTime() + "Version");
        if (!tempfile.exists()) {
            tempfile.mkdirs();
        }
        String path = tempfile.getPath();
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        httpManager = x.http();
        httpManager.get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                int percent = (int) (current * 100 / total);
//                System.out.println("下载进度:" + percent + "%");
                downtext.setText("正在下载，已完成:" + percent + "%");// 更新下载进度
                progressBar.setMax((int) (total / 1000));
                progressBar.setProgress((int) (current / 1000));

                //  numberProgressBar.setMax((int) (total / 1000));
                //  numberProgressBar.setProgress((int) (current / 1000));
            }

            @Override
            public void onSuccess(File result) {
                ShareUitls.putString(activity, "todayvideo", "");//视频缓存数据清空


                Toast.makeText(HomeActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                VersonUtils.installApk(result, HomeActivity.this);// 安装apk
                popupWindow.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(HomeActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                getLoadingPages();//获取广告信息
                popupWindow.dismiss();

            }

            @Override
            public void onCancelled(CancelledException cex) {
                //getRegistrationId();
                popupWindow.dismiss();
            }

            @Override
            public void onFinished() {
                popupWindow.dismiss();
            }
        });
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
}
