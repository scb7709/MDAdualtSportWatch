package com.headlth.management.watchdatasqlite;

import android.app.Activity;
import android.util.Log;

import com.headlth.management.entity.PublicDataClass;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abc on 2017/8/31.
 */
public class UpLoadingWatchData {
    private static volatile UpLoadingWatchData sInstance = null;
    private Activity activity;
    private static String UID, SPID, PlanNameID, Duration;
    private MySQLiteDataDao mySQLiteDataDao;

    private UpLoadingWatchData(Activity activity) {
        this.activity = activity;
        this.mySQLiteDataDao = MySQLiteDataDao.getInstance(activity);
    }

    public static UpLoadingWatchData getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (MySQLiteDataDao.class) {
                if (sInstance == null) {
                    sInstance = new UpLoadingWatchData(activity);
                }
            }
        }
        return sInstance;
    }


    //腕表数据
    public void uploadingWatchData(final String flag, final String Data,final  int  Starttime) {
        MyToash.Log(flag+"   "+Data);
        if (flag==null||Data == null || Data.length() == 0) {
            return;
        }
        RequestParams params = null;
        switch (flag) {
            case "Single_motion_results":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSingleSportSummaryRequest&version=v2.9.6");
                params.addBodyParameter("SPID", SPID == null ? SPID = ShareUitls.getString(activity, "SPID", "") : SPID);
                params.addBodyParameter("PlanNameID", PlanNameID == null ? PlanNameID = ShareUitls.getString(activity, "PlanNameID", "") : PlanNameID);
                params.addBodyParameter("Duration", Duration == null ? Duration = ShareUitls.getString(activity, "Target", "") : Duration);
                //MyToash.Log("" + SPID + "  " + PlanNameID);
                break;
            case "Original_data":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSingleSportOriginalDataRequest&version=v2.9.6");
                params.addBodyParameter("StartTime", Starttime+"");
                params.addBodyParameter("SPID", SPID == null ? SPID = ShareUitls.getString(activity, "SPID", "") : SPID);
               // MyToash.Log(Starttime + "' " + SPID);
                break;
            case "Summary_of_the_day":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostDaySportSummaryRequest&version=v2.9.6");
                params.addBodyParameter("StartTime", new Date().getTime() / 1000 + "");
                break;
            case "Historical_static_heart_rate":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostHRrestRequest&version=v2.9.6");
                break;
            case "PostBloodPressureRequest":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostBloodPressureRequest&version=v2.9.6");
                break;

            case "PostSleepInfoRequest":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSleepInfoRequest&version=v2.9.6");
                break;

        }
        if (params == null) {
            return;
        }
        params.addBodyParameter("Data", Data);
       // MyToash.Log("Data=  " + Data);
        HttpUtils.getInstance(activity).sendRequestRequestParamsNew("", params, false, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                       // Log.i("myblue", flag);
                        if (mdResponse.IsSuccess.equals("true")) {

                            switch (flag) {
                                case "Single_motion_results":
                                 /*   if (mySQLiteDataDao.IsHaveOriginal_data(DataTransferUtils.getInt_10(Data.substring(4, 12))+"")) {//表示该单次对应的原始数据已经在数据库中可以删除
                                        mySQLiteDataDao.deleteone(Data);//删除数据库对应的记录
                                    }*/
                                    ShareUitls.putString(activity, "maidong", "1");//分析界面是否重新刷新
                                    ShareUitls.putString(activity, "analize", "1");//分析界面是否重新刷新

                                    break;
                                case "Original_data":
                                    mySQLiteDataDao.deletSingleAndOriginal(Starttime);//删除对应的原始数据和单次数据
                                    break;
                            /*    case "Summary_of_the_day":
                                    // break;
                                case "Historical_static_heart_rate":
                                    // break;
                                case "PostBloodPressureRequest":
                                    //   break;
                                case "PostSleepInfoRequest":*/
                                default:
                                    mySQLiteDataDao.deleteone(Data);//删除数据库对应的记录
                                    break;

                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {


                    }
                }

        );
    }

    public interface GetTemperatureeAndWeathereOrParameterHttp {
        void success(String response);

        // void fail();
    }

    //获取天气气温 采样参数
    public static void getTemperatureeAndWeathereOrParameterHttp(final Activity context, final String flag, final GetTemperatureeAndWeathereOrParameterHttp getTemperatureeAndWeathereOrParameterHttp) {
        RequestParams params = null;
        switch (flag) {
            case "PostWeatherInfoRequest":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostWeatherInfoRequest&version=v2.9.6");
                params.addBodyParameter("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                break;
            case "PostParameterRequest":
                params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostParameterRequest&version=v2.9.6");
                break;
        }
        if (params == null) {
            return;
        }
        HttpUtils.getInstance(context).sendRequestRequestParamsNew("", params, false, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                        Log.i("myblue", flag);
                        if (mdResponse.IsSuccess.equals("true")) {
                            getTemperatureeAndWeathereOrParameterHttp.success(response);

                        }

                        /*else {
                            getTemperatureeAndWeathereOrParameterHttp.fail();
                        }*/
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {
                        getTemperatureeAndWeathereOrParameterHttp.success(null);
                        // getTemperatureeAndWeathereOrParameterHttp.fail();
                    }
                }

        );
    }

}
