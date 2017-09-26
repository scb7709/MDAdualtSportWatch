package com.headlth.management.acs;

import android.app.Activity;

import android.app.Application;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;

import android.util.Log;
import android.widget.RemoteViews;

import com.headlth.management.R;
import com.headlth.management.activity.Login;

import com.headlth.management.activity.MessageDetialsActivity;

import com.headlth.management.utils.ShareUitls;
import com.tencent.bugly.Bugly;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {

    //用于ac销毁
    public List<Activity> activities = new ArrayList<Activity>();

    PackageInfo pi;


    private static final String TAG = App.class.getName();
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("APPPonCreate11", "onCreate");

        //腾讯崩溃日志上传
        Bugly.init(getApplicationContext(), "89043f65b3", false);

        //  CrashReport.initCrashReport(getApplicationContext(), "89043f65b3", false);

//Xutils3 初始化
        x.Ext.init(this);
        x.Ext.setDebug(false);

//友盟推送初始化
        ShareUitls.putString(this, "AerobicSportActivityistop", "");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        ShareUitls.putString(this, "StrengthVideoPlayActivityistop", "");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        ShareUitls.putString(this, "MessageDetialsActivity", "");//MessageDetialsActivity
        ShareUitls.putString(this, "MainActivity", "");//用来MainActivity界面是否存在
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        handler = new Handler();

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {

                ShareUitls.putToken(getApplicationContext(), "token", deviceToken);
                Log.i("APPPonCreate22", deviceToken);
                //  sendBroadcast(new Intent(UPDATE_STATUS_ACTION));

            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("APPPonCreate33", s + "  " + s1);

                //  UmLog.i(TAG, "register failed: " + s + " " +s1);
                //  sendBroadcast(new Intent(UPDATE_STATUS_ACTION));


            }
        });
        mPushAgent.setMessageHandler(messageHandler);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        //  CustomNotificationHandler customNotificationHandler = new CustomNotificationHandler();
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        //

        Log.i("APPPonCreate33", "over");
    }
    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        /* *
          * 自定义消息的回调方法
          * */
           /* @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent   sendBroadcast = new Intent();
                        sendBroadcast.setAction("main_listCount");
                        context.sendBroadcast(sendBroadcast);
                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                       // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();*//*
                    }
                });

            }*/
           /* *
             * 自定义通知栏样式的回调方法
             * */
        @Override
        public Notification getNotification(Context context, UMessage msg) {
            //广播 跟新首页未读通知条数
            Intent sendBroadcast = new Intent();
            sendBroadcast.setAction("main_listCount");
            context.sendBroadcast(sendBroadcast);
            Log.i("getNotification", "" + msg.custom);
            switch (msg.builder_id) {

                  /*  case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
                                getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();*/
                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
              /*  case 1:
                     Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                       // myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                      //  myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                        break;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.custom);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.custom);
                        // myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        //  myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();*/
            }
        }
    };

    /**
     * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * */
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {


        @Override
        public void dismissNotification(Context context, UMessage uMessage) {
            super.dismissNotification(context, uMessage);
        }

        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {

            Log.i("ASDFG1", msg.custom);
            //用来记录有氧运动或者 力量视频播放界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入消息详情
            String AerobicSportActivityistop = ShareUitls.getString(App.this, "AerobicSportActivityistop", "");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）

            String StrengthVideoPlayActivityistop = ShareUitls.getString(App.this, "StrengthVideoPlayActivityistop", "");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）
            String MessageActivityistop = ShareUitls.getString(App.this, "MessageDetialsActivity", "");//首页界面是否重新刷新 （是否答完题或者是否运动完有新数据）


            // Log.i("ASDFG", jsonArray.toString() + msg.custom);


            if (ShareUitls.getString(App.this, "UID", "").length() == 0) {
                Intent intent = new Intent(App.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i("ASDFGLogin", "Login");
                return;

            } else if (AerobicSportActivityistop.equals("yes") || StrengthVideoPlayActivityistop.equals("yes")) {
                Log.i("ASDFGYES", "yes");

                return;
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(msg.custom);
                    if (jsonObject.getString("MsgtypeId").equals("2")) {



                        if (MessageActivityistop.equals("yes")) {
                            Intent sendBroadcast = new Intent();
                            sendBroadcast.setAction("MessageDetialsActivityReceiver");//当前界面就在消息详情
                            sendBroadcast.putExtra("MedictimeslotId", jsonObject.getString("MedictimeslotId"));
                            sendBroadcast.putExtra("MsgtypeId", jsonObject.getString("MsgtypeId"));
                            sendBroadcast.putExtra("CreateTime", jsonObject.getString("CreateTime"));

                            context.sendBroadcast(sendBroadcast);

                        }else {
                            Intent intent = new Intent(App.this, MessageDetialsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("MedictimeslotId", jsonObject.getString("MedictimeslotId"));
                            intent.putExtra("MsgtypeId", jsonObject.getString("MsgtypeId"));
                            intent.putExtra("CreateTime", jsonObject.getString("CreateTime"));
                            startActivity(intent);
                        }

                        Log.i("ASDFGMsgtypeId", "JSONException");
                    }
                } catch (JSONException e) {
                    Log.i("ASDFGJSONException", "ASDFGMsgtypeId");

                    e.printStackTrace();
                }

            }


        }

    };

}