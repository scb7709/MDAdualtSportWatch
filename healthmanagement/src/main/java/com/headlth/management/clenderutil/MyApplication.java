package com.headlth.management.clenderutil;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {


    private static Context mContext;
    private static MyApplication instance;

    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();


    }

    public static Context getContext() {
        return mContext;
    }



    public static MyApplication getInstance() {
        return instance;
    }
}
