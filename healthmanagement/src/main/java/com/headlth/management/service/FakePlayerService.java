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
import com.headlth.management.activity.AerobicSportActivity;

/**
 * Created by abc on 2016/7/20.
 */
public class FakePlayerService extends Service {
    private int con_state = -1;//当前状态 -1：热身准备 ，0 ：暂停，1：运动中
    Intent intent;
    private Runnable mTicker;
    String flag = "";
    public Handler h = new Handler() {
    };
    /*  private BroadcastReceiver ActivityToServiceReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              Log.i("PPPPPPPPPPPP","Service接收到了参数");
              con_state=intent.getIntExtra("con_state",0);
             // clicked=intent.getBooleanExtra("clicked",false);
          }
      };*/
    private static final int NOTIFY_FAKEPLAYER_ID = 9909;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onDestroy() {

    /*    this.unregisterReceiver(mScreenActionReceiver);
        // 在此重新启动,使服务常驻内存
        startService(new Intent(this, LockService.class));*/

        h.removeCallbacks(mTicker);
        stop();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        play();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter mScreenOnFilter = new IntentFilter();
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_ON);
        FakePlayerService.this.registerReceiver(mScreenActionReceiver, mScreenOnFilter);

        intent = new Intent();
        intent.setAction("ServiceToActivityReceiver");
        Log.i("ffffffffffffffffff", "永不退出onCreate");
        // registActivityToServiceReceiver();
        timeCount();
        // startPoPThread();
        //  setDrawTheard();
    }

    private void setDrawTheard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; true; ) {
                    intent.putExtra("POP_instruct", "4");
                    sendBroadcast(intent);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void timeCount() {
        //计时器if

        mTicker = new Runnable() {
            public void run() {
                intent.putExtra("POP_instruct", "1");
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

    private void startPoPThread() {
        //pop检测线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (; true; ) {
                    if (con_state == 1) {
                        intent.putExtra("POP_instruct", "1");
                        sendBroadcast(intent);
                    } else if (con_state == 0) {
                        intent.putExtra("POP_instruct", "2");
                        sendBroadcast(intent);
                    } else if (con_state == 2) {

                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    private void play() {
        Log.i("ffffffffffffffffff", "永不退出onStartCommand");
        Intent i = new Intent(this, AerobicSportActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker("迈动起来")
                .setContentTitle("迈动健康正在记录您的运动心率")
                .setContentText("点击进入...")
                .setContentIntent(pi)
                .build();
        //  notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        // notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        //设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。
        // 步骤 2：startForeground( int, Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
        startForeground(NOTIFY_FAKEPLAYER_ID, notification);

    }

    private void stop() {
        unregisterReceiver(mScreenActionReceiver);
        //将服务从forefround状态中移走，使得系统可以在低内存的情况下清除它。
        stopForeground(true);
    }
  /*  private void registActivityToServiceReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ActivityToServiceReceiver");
        this.registerReceiver(ActivityToServiceReceiver, filter);
    }*/

    private BroadcastReceiver mScreenActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Intent LockIntent = new Intent(FakePlayerService.this, AerobicSportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  LockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AerobicSportActivity.activity.startActivity(LockIntent);
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                //  Log.e(TAG, "screen off");
            }
        }
    };
}