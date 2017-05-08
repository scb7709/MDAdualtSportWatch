package com.headlth.management.entity;

/**
 * Created by abc on 2016/11/4.
 */
public class VersionClass {

    public int Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public Version Version;

    public static class Version {
        public String VersionName;//版本名称
        public int VersionCode;//版本号
        public String Description;//版本描述
        public String DownloadUrl;//app下载地址

    }

    ;
}
