package com.headlth.management.clenderutil;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast = null;


    public static void toastNoRepeat(Context context, int resId, int duration) {
        toastNoRepeat(context, context.getResources().getString(resId), duration);
    }

    public static void toastNoRepeat(Context context, String msg) {
        toastNoRepeat(context, msg, Toast.LENGTH_LONG);
    }

    public static void toastNoRepeat(Activity activity, String msg, int duration) {
        toastNoRepeat(activity.getApplicationContext(), msg, duration);
    }

    public static void toastNoRepeatException(Context context, Exception exception, int duration) {
        if (exception != null) {
            toastNoRepeat(context, exception.getMessage(), duration);
        }
    }

    public static void toastNoRepeat(Context context, String msg, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
}
