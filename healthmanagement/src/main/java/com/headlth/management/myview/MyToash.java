package com.headlth.management.myview;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by abc on 2017/8/30.
 */
public class MyToash {
    public static  void  Toash(Activity activity,String Message){
        Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
    }
    public static  void  ToashNoNet(Activity activity){
        Toash(activity,"网络异常");
    }

    public static  void  Log(String Message){
        Log.i("myblue",Message);
    }
}
