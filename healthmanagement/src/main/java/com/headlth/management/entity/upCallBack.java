package com.headlth.management.entity;

/**
 * Created by Administrator on 2016/3/18.
 */
public class upCallBack  {


    /**
     * Status : 1
     * Message : 传送的数据成功!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */

    private int Status;
    private String Message;
    private boolean IsSuccess;
    private boolean IsError;
    private Object ErrMsg;
    private Object ErrCode;

    @Override
    public String toString() {
        return "upCallBack{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", IsSuccess=" + IsSuccess +
                ", IsError=" + IsError +
                ", ErrMsg=" + ErrMsg +
                ", ErrCode=" + ErrCode +
                '}';
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public boolean isIsError() {
        return IsError;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public Object getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(Object ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public Object getErrCode() {
        return ErrCode;
    }

    public void setErrCode(Object ErrCode) {
        this.ErrCode = ErrCode;
    }
}
