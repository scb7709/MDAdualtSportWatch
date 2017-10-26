package com.headlth.management.MyBlue;

import android.os.Handler;
import android.os.Message;

/**
 * Created by abc on 2017/9/6.
 * <p/>
 * 发出的蓝牙指令5秒之内收不到回复都由本Handler处理
 */
public class MyWatchBlueHandler extends Handler {
    private MyWatchBlueHandler(MyWatchBlueHandlerListener myWatchBlueHandlerListener) {
        this.myWatchBlueHandlerListener = myWatchBlueHandlerListener;
    }

    private String flag = "";

    public interface MyWatchBlueHandlerListener {
        void strikeDelayed(String flag);
    }

    private MyWatchBlueHandlerListener myWatchBlueHandlerListener;
    private static MyWatchBlueHandler myWatchBlueHandler;

    public static MyWatchBlueHandler getInstance(MyWatchBlueHandlerListener myWatchBlueHandlerListener) {
/*        if (myWatchBlueHandler == null) {
            synchronized (MyWatchBlueHandler.class) {
                if (myWatchBlueHandler == null) {
                    myWatchBlueHandler = new MyWatchBlueHandler(myWatchBlueHandlerListener);
                }
            }
        }*/
        if (myWatchBlueHandler != null) {
            try {
                myWatchBlueHandler.removeMessages(0);

            } catch (Exception e) {
            }
            myWatchBlueHandler = null;
        }
        myWatchBlueHandler = new MyWatchBlueHandler(myWatchBlueHandlerListener);
        return myWatchBlueHandler;

    }

    public void sendMyWatchEmptyMessageDelayed() {
        if (myWatchBlueHandler != null) {
            try {
                myWatchBlueHandler.removeMessages(0);
            } catch (Exception e) {
            }
            myWatchBlueHandler.sendEmptyMessageDelayed(0, 10000);
        }
    }

    public void sendMyWatchEmptyMessageDelayed(String flag) {
        if (myWatchBlueHandler != null) {
            this.flag = flag;
            myWatchBlueHandler.removeMessages(0);
            if (flag.length() != 0) {
                myWatchBlueHandler.sendEmptyMessageDelayed(0, 10000);
            }else {

            }
        }
    }
    public void sendMyWatchEmptyMessageDelayed(String flag,int Delayedtime) {
        if (myWatchBlueHandler != null) {
            this.flag = flag;
            myWatchBlueHandler.removeMessages(0);
            if (flag.length() != 0) {
                myWatchBlueHandler.sendEmptyMessageDelayed(0, Delayedtime);
            }
        }
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            if (myWatchBlueHandlerListener != null) {
                myWatchBlueHandlerListener.strikeDelayed(flag);
            }

        }
    }
}
