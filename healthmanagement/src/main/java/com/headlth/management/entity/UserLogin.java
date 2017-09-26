package com.headlth.management.entity;

import java.io.Serializable;

/**
 * Created by abc on 2016/10/17.
 */
public class UserLogin implements Serializable {
     /*  "UserID": "4",
    "Mobile": "13811381138",
            "NickName": "802280000",
            "Gender": "2",
            "Birthday": "1990/7/25 0:00:00",
            "Height": "163",
            "Weight": "65",
            "ImgUrl": "",
            "Status": 2,
            "Message": "登录成功，该用户有处方",
            "IsSuccess": true,
            "IsError": false,
            "ErrMsg": null,
            "ErrCode": null*/

    public String UserID;
    public String Mobile;
    public String NickName;
    public String Gender;
    public String Birthday;
    public String  Height;
    public String MACAddress;
    public String Weight;
    public String ImgUrl;
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public String MAC;
    public String ResultJWT;



    @Override
    public String toString() {
        return "UserLogin{" +
                "UserID='" + UserID + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", NickName='" + NickName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Birthday='" + Birthday + '\'' +
                ", Height='" + Height + '\'' +
                ", Weight='" + Weight + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                ", Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                '}';
    }
}
