package com.headlth.management.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.historyDate;
import com.headlth.management.entity.upCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ShareUitls;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/29.
 */
public class UpHistoryDataService extends Service {
    private static final String TAG = "Test";

    //返回null
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "Service onBind--->");
        return null;
    }

    // Service创建时调用
    boolean isDestry = false;
    String url;
    int i = 0;
    List<historyDate> hisDates;
    boolean upFasle = false;

    public void onCreate() {
         /*       //删除为空的数据并且重新序列化进去
                                Iterator<historyDate> it = hisDates.iterator();
                                while (it.hasNext()) {
                                    historyDate value = it.next();
                                    if (value.equals(null)) {
                                        it.remove();
                                    }
                                }*/
        url = Constant.BASE_URL;
        try {
            hisDates = (List<historyDate>) deSerialization(getObject());
            Log.e("Test", hisDates.size() + "hisDates.size()");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (upFasle) {
                        return;
                    }
                    if (hisDates.size() != 0) {
                        if (hisDates.get(0) != null) {
                            upDate(0, hisDates.get(0).getUID(), hisDates.get(0).getData(), hisDates.get(0).getWatchType(), hisDates.get(0).getEveryTime(), hisDates.get(0).getEveryVolidTime());
                            return;
                        }
                    } else {
                        ShareUitls.putString(getApplicationContext(), "allUpted", "noHistoryDate");
                        Intent intent = new Intent();
                        // 设置Class属性
                        intent.setClass(getApplicationContext(), UpHistoryDataService.class);
                        // 启动该Service
                        stopService(intent);
                    }

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Service onCreate--->");

    }

    // 当客户端调用startService()方法启动Service时，该方法被调用
    public void onStart(Intent intent, int startId) {
        Log.e(TAG, "Service onStart--->");
    }

    // 当Service不再使用时调用
    public void onDestroy() {
        isDestry = true;
        Log.e(TAG, "Service onDestroy--->");
    }

    // 当解除绑定时调用
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "Service onUnbind--->");
        return super.onUnbind(intent);
    }


    //历史数据上传
    private upCallBack upBack;
    boolean noWire = false;
    Gson g = new Gson();
    //上传http://www.ssp365.com:8066/MdMobileService.ashx?do=PostSportDataRequest
    private List<Integer> thirdData = new ArrayList<>();
    private void upDate(final int i, final String UID, final String Data, final String WatchType, final String EveryTime, final String EveryVolidTime) {
      /*  upBack = new upCallBack();
        referenceQueue = new volleyque(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "/MdMobileService.ashx?do=PostSportDataRequest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("mmmm", response.toString());
                        upBack = g.fromJson(response.toString(),
                                upCallBack.class);
                        if (upBack.getStatus() == 1) {
                            hisDates.set(i, null);
                            *//*  for(int i=0;i<hisDates.size();i++){
                                Log.e("Test", hisDates.get(i).toString() + "成功了"+i);
                            }*//*
                            Log.e("Test", i + "成功了");
                            if (i + 1 == hisDates.size()) {
                                ShareUitls.putString(getApplicationContext(), "allUpted", "hasHistoryDate");


                                //达到集合总个数关闭服务
                                Intent intent = new Intent();
                                // 设置Class属性
                                intent.setClass(getApplicationContext(), UpHistoryDataService.class);
                                // 启动该Service
                                stopService(intent);
                                //添加后再次序列化

                                //取出上传成功的数据，然后重新保存集合。
                                Iterator<historyDate> it = hisDates.iterator();
                                while (it.hasNext()) {
                                    historyDate value = it.next();
                                    if (value==null) {
                                        it.remove();
                                    }
                                }
                                try {
                                    saveObject(serialize(hisDates));



                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            } else {
                                if (hisDates.get(i + 1) != null) {
                                    upDate(i + 1, hisDates.get(i + 1).getUID(), hisDates.get(i + 1).getData(), hisDates.get(i + 1).getWatchType(), hisDates.get(i + 1).getEveryTime(), hisDates.get(i + 1).getEveryVolidTime());
                                }
                            }

                        } else {
                            upFasle = true;
                            return;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                upFasle = true;
               *//* //保存上传失败的数据
                if (!ShareUitls.getString(getApplicationContext(), "tagHasHisList", "null").equals("null")) {
                    //现获取原来历史若原来有历史数据则添加进去，若原来无，则新建
                    try {
                        List<historyDate> hisDates = (List<historyDate>) deSerialization(getObject());
                        historyDate hisDate = new historyDate();
                        hisDate.setUID(UID);
                        hisDate.setData(Data);
                        hisDate.setWatchType(WatchType);
                        hisDate.setEveryTime(EveryTime);
                        hisDate.setEveryVolidTime(EveryVolidTime);
                        hisDates.add(hisDate);
                        Log.e("hislist", "集合不为0");
                        Log.e("hislist", hisDates.size() + "");
                        for (int i = 0; i < hisDates.size(); i++) {
                            Log.e("hislist", hisDates.get(i).getData() + "");
                        }
                        //添加后再次序列化
                        try {
                            saveObject(serialize(hisDates));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShareUitls.putString(getApplicationContext(), "tagHasHisList", 1 + "");
                    List<historyDate> hisDates = new ArrayList<>();
                    //保存历史数据
                    //对象序列化
                    historyDate hisDate = new historyDate();
                    hisDate.setUID(UID);
                    hisDate.setData(Data);
                    hisDate.setWatchType(WatchType);
                    hisDate.setEveryTime(EveryTime);
                    hisDate.setEveryVolidTime(EveryVolidTime);
                    hisDates.add(hisDate);
                    Log.e("hislist", "第一次集合为0");
                    //第一次保存最新的历史数据
                    try {
                        saveObject(serialize(hisDates));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*//*

                return;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                if (!ShareUitls.getString(getApplicationContext(), "UID", "null").equals("null")) {
                    map.put("UID", ShareUitls.getString(getApplicationContext(), "UID", "null"));
                }
                Log.e("update", Data + "Data" + WatchType + "WatchType" + EveryTime + "EveryTime" + EveryVolidTime + "EveryVolidTime");
                map.put("Data", Data);
                map.put("WatchType", WatchType);
                map.put("EveryTime", EveryTime);
                map.put("EveryVolidTime", EveryVolidTime);
                Log.e("pinjie", ShareUitls.getString(getApplicationContext(), "UID", "null") + "----" + Data + "----" + WatchType + "---" + EveryTime + "---" + EveryVolidTime + "---");
                return map;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        15 * 1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        referenceQueue.add(stringRequest);
        referenceQueue.start();*/
    }


    /**
     * 序列化对象
     *
     * @param hisDate
     * @return
     * @throws IOException
     */
    private String serialize(List<historyDate> hisDate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(hisDate);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    void saveObject(String strObject) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("historylist", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("historylist", strObject);
        Log.e("hislist", "保存成功");
        edit.commit();
    }


    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<historyDate> deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<historyDate> person = (List<historyDate>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return person;
    }

    String getObject() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("historylist", 0);
        return sp.getString("historylist", null);
    }

}
