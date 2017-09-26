package com.headlth.management.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.headlth.management.activity.Login;
import com.headlth.management.entity.PublicDataClass;
import com.headlth.management.entity.upCallBack;
import com.headlth.management.myview.MyToash;
import com.headlth.management.watchdatasqlite.MySQLiteBaseClass;
import com.headlth.management.watchdatasqlite.MySQLiteDataDao;
import com.headlth.management.watchdatasqlite.UpLoadingWatchData;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scb on 2016/2/16.
 */
public class InternetUtils {
    // / 没有连接
    public static final int NETWORN_NONE = 0;
    // / wifi连接
    public static final int NETWORN_WIFI = 1;
    // / 手机网络数据连接
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;

    public static boolean internet(final Activity context) {
        //检查当前网络连接
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;
        } else {
            NoNetWorkNotification(context);
        }
        return false;

    }

    private static boolean sport;//正在上传不允许再次触发
    private static boolean strength;//正在上传不允许再次触发
    private static boolean watch;//正在上传不允许再次触发

    private static MySQLiteDataDao mySQLiteDataDao;
    private static UpLoadingWatchData upLoadingWatchData;
    private static ConnectivityManager connectivityManager;
    public static boolean internet2(final Activity context) {
        //检查当前网络连接

        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            //上传上次力量训练上传失败的数据
            if (!ShareUitls.getStrengthString(context, "StrengthID", "").equals("") && !strength) {
                //  Log.i("strstrstrstr", "liliamng");
                strength = true;
                uploadingStrengthData(context);
            }
            if (!ShareUitls.getSportString(context, "SportID", "null").equals("null") && !sport) {
                //  Log.i("strstrstrstr", "SportID");
                sport = true;
                uploadingpPorlorData(context);
            }
          //  watchDataupload(context);
            return true;
        } else {
            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_LONG).show();
            //  NoNetWorkNotification(context);
            return false;
        }
    }

    public static void watchDataupload(Activity context) {
        if(!watch) {
            MyToash.Log("居然有本地数据0");

            if (mySQLiteDataDao == null) {
                mySQLiteDataDao = MySQLiteDataDao.getInstance(context);
            }
            if (upLoadingWatchData == null) {
                upLoadingWatchData = UpLoadingWatchData.getInstance(context);
            }
            List<MySQLiteBaseClass> mySQLiteBaseClassList = mySQLiteDataDao.queryAllNOSingle_motion_results();
            List<MySQLiteBaseClass.Single_Original> single_originals = mySQLiteDataDao.queryALLSingle_Original();


            MyToash.Log(mySQLiteBaseClassList.size()+"   "+single_originals.size());
            if (mySQLiteBaseClassList.size() > 0) {
                for (MySQLiteBaseClass mySQLiteBaseClass : mySQLiteBaseClassList) {
                    upLoadingWatchData.uploadingWatchData(mySQLiteBaseClass.FLAG, mySQLiteBaseClass.DATA, 0);//上传腕表数据
                    MyToash.Log("居然有本地数据1");
                }
            }
            if (single_originals.size() > 0) {
                for (MySQLiteBaseClass.Single_Original single_original : single_originals) {
                    MyToash.Log("居然有本地数据2");
                    upLoadingWatchData.uploadingWatchData("Single_motion_results", single_original.Single_data, 0);//上传腕表数据
                    upLoadingWatchData.uploadingWatchData("Original_data", single_original.Original_data, Integer.parseInt(single_original.Starttime));//上传腕表数据

                }
            }

        }
        watch=true;
    }

    private static void NoNetWorkNotification(final Activity context) {
        try {
            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_LONG).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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
                            context.startActivityForResult(intent, 100);


                        }

                    }).show();
        } catch (Exception E) {
        }
    }

    private static void uploadingpPorlorData(final Activity context) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSportDataRequest");
        //   params.addBodyParameter("UID", ShareUitls.getString(context, "UID", "null"));
        params.addBodyParameter("ResultJWT", ShareUitls.getString(context, "ResultJWT", "0"));
        //  params.addBodyParameter("VersionNum", VersonUtils.getVersionName(context));;
        params.addBodyParameter("UID", ShareUitls.getSportString(context, "SportID", "null"));
        params.addBodyParameter("Data", ShareUitls.getSportString(context, "Data", "null"));
        params.addBodyParameter("WatchType", ShareUitls.getSportString(context, "WatchType", "null"));
        params.addBodyParameter("EveryTime", ShareUitls.getSportString(context, "EveryTime", "null"));
        params.addBodyParameter("EveryVolidTime", ShareUitls.getSportString(context, "EveryVolidTime", "null"));
        params.addBodyParameter("UploadTime", ShareUitls.getSportString(context, "UploadTime", "null"));
        HttpUtils.getInstance(context).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        sport = false;
                        ShareUitls.cleanString2(context);
                        Log.e("mmmm", response.toString());
                        Gson g = new Gson();
                        upCallBack upBack = g.fromJson(response.toString(), upCallBack.class);
                        if (upBack.getStatus() == 1) {
                            ShareUitls.putString(context, "maidong", "1");//首页界面重新刷新
                            ShareUitls.putString(context, "analize", "1");//分析界面重新刷新
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable error) {
                        sport = false;
                        //   Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }

    private static void uploadingStrengthData(final Activity context) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostPowertrainRequest");
        // params.addBodyParameter("UID", ShareUitls.getString(context, "UID", "null"));
        params.addBodyParameter("ResultJWT", ShareUitls.getString(context, "ResultJWT", "0"));
        // params.addBodyParameter("VersionNum", VersonUtils.getVersionName(context));;
        params.addBodyParameter("VID", ShareUitls.getStrengthString(context, "VID", ""));
        params.addBodyParameter("UID", ShareUitls.getStrengthString(context, "StrengthID", ""));
        params.addBodyParameter("SportCal", ShareUitls.getStrengthString(context, "SportCal", ""));
        params.addBodyParameter("SportDuration", ShareUitls.getStrengthString(context, "SportDuration", ""));
        params.addBodyParameter("SportDate", ShareUitls.getStrengthString(context, "SportDate", ""));
        HttpUtils.getInstance(context).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        strength = false;
                        ShareUitls.cleanStrengthString(context);
                        Gson g = new Gson();
                        upCallBack upBack = g.fromJson(response.toString(), upCallBack.class);
                        if (upBack.getStatus() == 1) {
                            ShareUitls.putString(context, "maidong", "1");//分析界面是否重新刷新
                            ShareUitls.putString(context, "analize", "1");//分析界面是否重新刷新
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {
                        strength = false;
                        //     Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();


                    }
                }

        );
    }

    public static boolean internett(final Context context) {
        //检查当前网络连接
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;

    }

    public static boolean internetNoWifi(final Context context) {
        //检查当前网络连接
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;

        } else {
            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_LONG).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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
                            context.startActivity(intent);


                        }

                    }).show();
            return false;
        }


    }

    public static int getNetworkState(Context context) {

        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (null == connectivityManager)

            return NETWORN_NONE;

        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {

            return NETWORN_NONE;

        }

        // Wifi

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (null != wifiInfo) {

            NetworkInfo.State state = wifiInfo.getState();

            if (null != state)

                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {

                    return NETWORN_WIFI;

                }

        }

        // 网络

        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {

            NetworkInfo.State state = networkInfo.getState();

            String strSubTypeName = networkInfo.getSubtypeName();

            if (null != state)

                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {

                    switch (activeNetInfo.getSubtype()) {

                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g

                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g

                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g

                        case TelephonyManager.NETWORK_TYPE_1xRTT:

                        case TelephonyManager.NETWORK_TYPE_IDEN:

                            return NETWORN_2G;

                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g

                        case TelephonyManager.NETWORK_TYPE_UMTS:

                        case TelephonyManager.NETWORK_TYPE_EVDO_0:

                        case TelephonyManager.NETWORK_TYPE_HSDPA:

                        case TelephonyManager.NETWORK_TYPE_HSUPA:

                        case TelephonyManager.NETWORK_TYPE_HSPA:

                        case TelephonyManager.NETWORK_TYPE_EVDO_B:

                        case TelephonyManager.NETWORK_TYPE_EHRPD:

                        case TelephonyManager.NETWORK_TYPE_HSPAP:

                            return NETWORN_3G;

                        case TelephonyManager.NETWORK_TYPE_LTE:

                            return NETWORN_4G;

                        default://有机型返回16,17

                            //中国移动 联通 电信 三种3G制式

                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {

                                return NETWORN_3G;

                            } else {

                                return NETWORN_MOBILE;

                            }

                    }

                }

        }

        return NETWORN_NONE;

    }


}