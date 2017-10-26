package com.headlth.management.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by abc on 2016/11/4.
 */
public class VersonUtils {


    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVerisonCode(Activity activity) {
        PackageManager pm = activity.getPackageManager();// 获取包管理器
        try {
            PackageInfo packageInfo = pm.getPackageInfo(activity.getPackageName(), 0);// 获取当前应用信息
            int versionCode = packageInfo.versionCode;// 版本号
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有找到包名的异常
            e.printStackTrace();
        }

        return -1;
    }

    public static String getVersionName(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 安装apk
     */
    public static void installApk(File file, Context activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
        //安转完成后提示打开
       // android.os.Process.killProcess(android.os.Process.myPid());


    }

}
