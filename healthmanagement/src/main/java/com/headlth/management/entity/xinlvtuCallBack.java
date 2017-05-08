package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class xinlvtuCallBack implements Serializable {
    /**
     * Status : 1
     * HeartRateList : [{"SportTime":"2015-08-18T11:05:53","HeartRate":74},{"SportTime":"2015-08-18T11:05:53","HeartRate":76},{"SportTime":"2015-08-18T11:05:53","HeartRate":75},{"SportTime":"2015-08-18T11:05:59","HeartRate":76},{"SportTime":"2015-08-18T11:06:07","HeartRate":81},{"SportTime":"2015-08-18T11:06:14","HeartRate":88},{"SportTime":"2015-08-18T11:06:22","HeartRate":87},{"SportTime":"2015-08-18T11:06:29","HeartRate":79},{"SportTime":"2015-08-18T11:06:37","HeartRate":75},{"SportTime":"2015-08-18T11:06:44","HeartRate":73},{"SportTime":"2015-08-18T11:06:52","HeartRate":71},{"SportTime":"2015-08-18T11:06:59","HeartRate":71},{"SportTime":"2015-08-18T11:07:07","HeartRate":72},{"SportTime":"2015-08-18T11:07:14","HeartRate":75},{"SportTime":"2015-08-18T11:07:22","HeartRate":75},{"SportTime":"2015-08-18T11:07:29","HeartRate":73},{"SportTime":"2015-08-18T11:07:37","HeartRate":75},{"SportTime":"2015-08-18T11:07:45","HeartRate":73},{"SportTime":"2015-08-18T11:07:53","HeartRate":70},{"SportTime":"2015-08-18T11:08:00","HeartRate":70},{"SportTime":"2015-08-18T11:12:45","HeartRate":76},{"SportTime":"2015-08-18T11:12:59","HeartRate":76},{"SportTime":"2015-08-18T11:13:07","HeartRate":72},{"SportTime":"2015-08-18T11:15:29","HeartRate":74},{"SportTime":"2015-08-18T11:15:43","HeartRate":73},{"SportTime":"2015-08-18T11:15:50","HeartRate":72},{"SportTime":"2015-08-18T11:15:59","HeartRate":74},{"SportTime":"2015-08-18T11:16:06","HeartRate":74},{"SportTime":"2015-08-18T11:16:14","HeartRate":75},{"SportTime":"2015-08-18T11:16:21","HeartRate":75}]
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
    /**
     * SportTime : 2015-08-18T11:05:53
     * HeartRate : 74
     */

    private List<HeartRateListBean> HeartRateList;

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

    public List<HeartRateListBean> getHeartRateList() {
        return HeartRateList;
    }

    public void setHeartRateList(List<HeartRateListBean> HeartRateList) {
        this.HeartRateList = HeartRateList;
    }

    public static class HeartRateListBean implements Serializable{
        private String SportTime;
        private int HeartRate;

        public String getSportTime() {
            return SportTime;
        }

        public void setSportTime(String SportTime) {
            this.SportTime = SportTime;
        }

        public int getHeartRate() {
            return HeartRate;
        }

        public void setHeartRate(int HeartRate) {
            this.HeartRate = HeartRate;
        }
    }
}
