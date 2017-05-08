package com.headlth.management.clenderutil;

import java.util.List;

/**
 * Created by 1 on 2015/12/2.
 */
public class BackYearMonth {
    /**
     * Status : 1
     * CalenderList : [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0]
     * Message : 获取成功!
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
    private List<Integer> CalenderList;
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

    public List<Integer> getCalenderList() {
        return CalenderList;
    }

    public void setCalenderList(List<Integer> CalenderList) {
        this.CalenderList = CalenderList;
    }
}
