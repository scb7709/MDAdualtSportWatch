package com.headlth.management.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25.
 */
public class verSionCallBack implements Serializable {

    /**
     * Status : 1
     * VerNo : 5
     * VerName : 5
     * IsNewVer : 0
     * Message : 此版本不是当前最新版本!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */

    private int Status;
    private String VerNo;
    private String VerName;
    private int IsNewVer;
    private String Message;
    private boolean IsSuccess;
    private boolean IsError;
    private Object ErrMsg;
    private Object ErrCode;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getVerNo() {
        return VerNo;
    }

    public void setVerNo(String VerNo) {
        this.VerNo = VerNo;
    }

    public String getVerName() {
        return VerName;
    }

    public void setVerName(String VerName) {
        this.VerName = VerName;
    }

    public int getIsNewVer() {
        return IsNewVer;
    }

    public void setIsNewVer(int IsNewVer) {
        this.IsNewVer = IsNewVer;
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
