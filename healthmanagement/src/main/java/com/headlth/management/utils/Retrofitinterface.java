package com.headlth.management.utils;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by abc on 2016/8/3.
 */
public interface Retrofitinterface {
    @POST("/MdMobileService.ashx?do=LoginMobileRequest")
    @FormUrlEncoded
    void login(@FieldMap Map<String, String> params, Callback<String> call);
}
