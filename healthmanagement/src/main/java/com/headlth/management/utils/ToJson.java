package com.headlth.management.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/29.
 */
public class ToJson {
    public static String getRequestData(ArrayList<String> dateArray, ArrayList<Integer> bmps) {
        try {
            JSONArray result = new JSONArray();
            for (int i = 0; i < bmps.size(); i++) {
                //拼接json字符串.
                //{"SportTime":"2015-07-20 06:21:16","HeartRate":106}
                JSONObject object = new JSONObject();
                object.put("SportTime", dateArray.get(i));
                object.put("HeartRate", bmps.get(i));
                result.put(object);
            }

            String temp = result.toString();
        /*    temp = Base64.encodeToString(temp.getBytes(), Base64.DEFAULT);*/
            Log.e("toJson",temp);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
