package com.headlth.management.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.headlth.management.R;

/**
 * Created by abc on 2016/7/20.
 */
public class NetworkService extends Service {

    Intent intent;
    private Runnable mTicker;
    boolean flag = true;
    public Handler h = new Handler() {
    };
    private static final int NOTIFY_FAKEPLAYER_ID = 9909;
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
      //  registActivityToServiceReceiver();
        Log.i("JJJJJJJaaA", flag+"");
        intent = new Intent();
        intent.setAction("ServiceToActivityReceiver");
        timeCount();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        play();
        return START_STICKY;
    }

    public void timeCount() {
       //计时器if
    /*    new Thread() {
            @Override
            public void run() {
                super.run();
                while (flag){
                    Log.i("JJJJJJJaa", flag+"");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/
      mTicker = new Runnable() {
            public void run() {
                sendBroadcast(intent);
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                h.postAtTime(mTicker, next);
            }
            //  }
        };
        //启动计时线程，定时更新
        mTicker.run();
    }

    private void stop() {

        //将服务从forefround状态中移走，使得系统可以在低内存的情况下清除它。
        stopForeground(true);

    }
  private void registActivityToServiceReceiver() {
      IntentFilter filter = new IntentFilter();
      filter.addAction("ActivityToServiceReceiver");
      this.registerReceiver(ActivityToServiceReceiver, filter);
  }

    private BroadcastReceiver ActivityToServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            flag=false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(mTicker);
        stop();
        Log.i("JJJJJJJaaAonDestroy", flag+"");
        //flag=false;
       // unregisterReceiver(ActivityToServiceReceiver);
    }

    private void play() {
        Log.i("ffffffffffffffffff", "永不退出onStartCommand");
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.log)
                .setTicker("迈动健康正在运行")
                .setContentTitle("迈动健康正在运行")
                .setContentText("点击进入...")
                .setContentIntent(pi)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(0, notification);

    }

}