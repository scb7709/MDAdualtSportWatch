package com.headlth.management.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by abc on 2017/8/4.
 * 所有网络请求返回的公共数据解析类
 */
public class PublicDataClass implements Serializable {
    public  MdResponse MdResponse;
    public  JSONObject Data;
    public  class MdResponse {
        @Override
        public String toString() {
            return "MdResponse{" +
                    "Status='" + Status + '\'' +
                    ", Message='" + Message + '\'' +
                    ", IsSuccess='" + IsSuccess + '\'' +
                    ", IsError='" + IsError + '\'' +
                    ", ErrMsg='" + ErrMsg + '\'' +
                    ", ErrCode='" + ErrCode + '\'' +
                    '}';
        }

        public String Status;
        public String Message;
        public String IsSuccess;
        public String IsError;
        public String ErrMsg;
        public String ErrCode;

    }

    @Override
    public String toString() {
        return "PublicDataClass{" +
                "MdResponse=" + MdResponse +
                ", Data=" + Data +
                '}';
    }

    public  static  JSONObject[] getPublicData(String string){
        JSONObject[] jsonObjects=new JSONObject[2];
        try {
            JSONObject jsonObject=new JSONObject(string);
            jsonObjects[0]=jsonObject.getJSONObject("MdResponse");
            jsonObjects[1]=jsonObject.getJSONObject("Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjects;
    }
}
