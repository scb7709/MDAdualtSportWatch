package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.logcallback;
import com.headlth.management.entity.upCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DiskBitmap;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by abc on 2016/7/7.
 */
@ContentView(R.layout.activity_strengthsportsummarize)
public class StrengthSportSummarizeActivity extends BaseActivity /*implements IWeiboHandler.Response */{
    Intent intent;
    @ViewInject(R.id.strengthsportsummarize_sport_time)
    TextView strengthsportsummarize_sport_time;
    @ViewInject(R.id.strengthsportsummarize_sport_consume)
    TextView strengthsportsummarize_sport_consume;
    @ViewInject(R.id.strengthsportsummarize_sport_tip)
    TextView strengthsportsummarize_sport_tip;
    @ViewInject(R.id.strengthsportsummarize_sport_Quotes)
    TextView strengthsportsummarize_sport_Quotes;
    @ViewInject(R.id.strengthsportsummarize_sport_QuotesName)
    TextView strengthsportsummarize_sport_QuotesName;
    @ViewInject(R.id.strengthsport_summarize_back)
    RelativeLayout strengthsport_summarize_back;
    @ViewInject(R.id.strengthsport_summarize_share)
    RelativeLayout strengthsport_summarize_share;

    private com.headlth.management.clenderutil.WaitDialog waitDialog;
    public static Activity activity;
    private boolean SEND;//数据是否已经上传过
    private boolean isOverSport;//是否完成运动过 如果分享等操作出现奔溃 本页面会出现在栈顶 并且重新调用oncreat 方法 此处用来标记此种情况
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);//注册微博分享SDK
        activity = this;
        initDialog(StrengthSportSummarizeActivity.this);
        intent = getIntent();
        setData();
        sendData();

    }

    private void setData() {
        strengthsport_summarize_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.getInstance().showPopFormBottom(StrengthSportSummarizeActivity.this, webHandler);
            }
        });

        strengthsport_summarize_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }*/
                startActivity(new Intent(StrengthSportSummarizeActivity.this, MainActivity.class));
                finish();
            }
        });
        strengthsportsummarize_sport_time.setText(stringForTime(Integer.parseInt(intent.getStringExtra("SportDuration"))));

        strengthsportsummarize_sport_consume.setText((int) Double.parseDouble(intent.getStringExtra("SportCal")) + "");
        strengthsportsummarize_sport_tip.setText("    " + intent.getStringExtra("Tip"));
        String str = intent.getStringExtra("Quotes");

        int i = str.indexOf("-");
        Log.i("DDDDDDDD", str + "  " + i + " " + str.length());
        try {
            strengthsportsummarize_sport_Quotes.setText("    " + str.substring(0, i));
            strengthsportsummarize_sport_QuotesName.setText(str.substring(i + 1, str.length()));
        } catch (StringIndexOutOfBoundsException n) {
            strengthsportsummarize_sport_Quotes.setText(str);
            strengthsportsummarize_sport_QuotesName.setText("");
        }


    }

    private void sendData() {
        // Log.i("SSSSSSSS", intent.getStringExtra("VID") + "  " + intent.getStringExtra("UID") + "   " + intent.getStringExtra("SportCal") + "'" + intent.getStringExtra("SportDuration") + "'" + intent.getStringExtra("SportDate"));
        Log.i("updateeeeeeeeee", "  UID=  " + intent.getStringExtra("UID") + "    VID= " + intent.getStringExtra("VID") + "  SportCal=  " + intent.getStringExtra("SportCal") + "    SportDuration= " + intent.getStringExtra("SportDuration") + "   SportDate= " + intent.getStringExtra("SportDate") + "  ");
        if (!SEND) {
            ShareUitls.putString(getApplicationContext(), "PowerTrainDuration", intent.getStringExtra("SportDuration"));
            if (InternetUtils.internett(this)) {
                if (!intent.getStringExtra("UID").equals("null")) {
                    waitDialog.showDailog();
                    //在这里设置需要post的参数

                    String ResultJWT = ShareUitls.getString(StrengthSportSummarizeActivity.this, "ResultJWT", "0");
                    RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPowertrainRequest");
                    params.addBodyParameter("UID", ShareUitls.getString(StrengthSportSummarizeActivity.this, "UID", "") + "");
                    params.addBodyParameter("ResultJWT", ResultJWT + "");
                    params.addBodyParameter("VID", intent.getStringExtra("VID") + "");
                    params.addBodyParameter("SportCal", intent.getStringExtra("SportCal"));
                    params.addBodyParameter("SportDuration", intent.getStringExtra("SportDuration"));
                    params.addBodyParameter("SportDate", intent.getStringExtra("SportDate"));
                    HttpUtils.getInstance(StrengthSportSummarizeActivity.this).sendRequestRequestParams("正在上传,请稍后...", params, true, new HttpUtils.ResponseListener() {
                                @Override
                                public void onResponse(String response) {
                                    SEND = true;
                                    ShareUitls.cleanStrengthString(StrengthSportSummarizeActivity.this);
                                    Log.e("rrrrrrrrrrrrr", response.toString());
                                    upCallBack upBack = g.fromJson(response.toString(), upCallBack.class);
                                    ShareUitls.putString(StrengthSportSummarizeActivity.this, "maidong", "1");//首页界面重新刷新
                                    ShareUitls.putString(StrengthSportSummarizeActivity.this, "analize", "1");//分析界面重新刷新
                                    if (upBack.getStatus() == 1) {
                                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                        strengthsport_summarize_share.setVisibility(View.VISIBLE);
                                    } else if (upBack.getStatus() == 2) {
                                        Toast.makeText(getApplicationContext(), "数据异常,上传失败", Toast.LENGTH_SHORT).show();

                                    }
                                    waitDialog.dismissDialog();
                                }

                                @Override
                                public void onErrorResponse(Throwable ex) {

                                    saveNativeData();
                                    Log.e("rrrrrrrrrrrrr", "shiabi ghaibsia ");
                                    Toast.makeText(getApplicationContext(), "上传失败,请确认网络连接", Toast.LENGTH_SHORT).show();
                                    waitDialog.dismissDialog();

                                }
                            }

                    );
                }
            } else {
                saveNativeData();
                Toast.makeText(StrengthSportSummarizeActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("当前无网络连接，是否前去设置网络？")//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
// 跳转到系统的网络设置界面
                                Intent intent = null;
                                // 先判断当前系统版本
                                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                    intent = new Intent(Settings.ACTION_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                                }
                                startActivityForResult(intent, 1234);


                            }

                        }).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {//
            if (InternetUtils.internett(this)) {
                sendData();
            } else {
                Toast.makeText(StrengthSportSummarizeActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
            }

        }

    }

    private String stringForTime(int timeMs) {
        Log.i("SSSSSSSSSSSSS", timeMs + "");
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d'%02d", minutes, seconds).toString();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
           /* if (MainActivity.Activity != null) {
                MainActivity.Activity.finish();
            }*/
            startActivity(new Intent(StrengthSportSummarizeActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initDialog(Context context) {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
        waitDialog.setMessage("正在上传,请稍候...");
    }

    private logcallback log;
    private Gson g = new Gson();

    private void saveNativeData() {
        ShareUitls.cleanStrengthString(StrengthSportSummarizeActivity.this);
        ShareUitls.putStrengthString(getApplicationContext(), "StrengthID", intent.getStringExtra("UID") + "");
        ShareUitls.putStrengthString(getApplicationContext(), "VID", intent.getStringExtra("VID") + "");
        ShareUitls.putStrengthString(getApplicationContext(), "SportCal", intent.getStringExtra("SportCal") + "");
        ShareUitls.putStrengthString(getApplicationContext(), "SportDuration", intent.getStringExtra("SportDuration") + "");
        ShareUitls.putStrengthString(getApplicationContext(), "SportDate", intent.getStringExtra("SportDate") + "");
    }


    IWeiboShareAPI mWeiboShareAPI;



 /*   @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }*/


    public void sinashare(String webImageUrl) {
        // 创建微博分享接口实例
        // Log.i("sssssssssssssIIII", ImageUrl);
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = getImageObj(webImageUrl);
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(this, request);
        //  waitDialog.ShowDialog(false);


    }

    //微博图片压缩weibo
    private ImageObject getImageObj(String webImageUrl) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        // Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.umsg);
        Bitmap bitmap = DiskBitmap.getDiskBitmap(webImageUrl, this);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    Handler webHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String webImageUrl = (String) msg.obj;
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                sinashare(webImageUrl);
            }


        }
    };


}
