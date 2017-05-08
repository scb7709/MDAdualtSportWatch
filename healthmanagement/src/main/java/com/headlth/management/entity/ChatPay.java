package com.headlth.management.entity;

/**
 * Created by abc on 2016/10/25.
 */
public class ChatPay {

    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public WxPayData WxPayData;

    @Override
    public String toString() {
        return "ChatPay{" +
                "Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", WxPayData=" + WxPayData +
                '}';
    }

    public  static  class WxPayData{
        public String Appid;
        public String Partnerid;
        public String Prepayid;
        public String Package;
        public String TimeStamp;
        public String NonceStr;
        public String Sign;

        @Override
        public String toString() {
            return "WxPayData{" +
                    "Appid='" + Appid + '\'' +
                    ", Partnerid='" + Partnerid + '\'' +
                    ", Prepayid='" + Prepayid + '\'' +
                    ", Package='" + Package + '\'' +
                    ", TimeStamp='" + TimeStamp + '\'' +
                    ", NonceStr='" + NonceStr + '\'' +
                    ", Sign='" + Sign + '\'' +
                    '}';
        }
    }
}
