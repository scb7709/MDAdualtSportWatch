package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/22.
 */
public class shouyeCallBack implements Serializable {


    /**
     * Status : 1
     * UserSportList : [{"HeartRateAvg":"142","Duration":127462,"ValidTime":4477,"KCal":"18710"}]
     * Message : 获取数据成功!
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
     * HeartRateAvg : 142
     * Duration : 127462
     * ValidTime : 4477
     * KCal : 18710
     */

    private List<UserSportListBean> UserSportList;

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

    public List<UserSportListBean> getUserSportList() {
        return UserSportList;
    }

    public void setUserSportList(List<UserSportListBean> UserSportList) {
        this.UserSportList = UserSportList;
    }

    public static class UserSportListBean implements Serializable {
        private String HeartRateAvg;
        private int Duration;
        private int ValidTime;
        private String KCal;
        private String PowerTrainDuration;
        private String IsPlay;

        public String getHeartRateAvg() {
            return HeartRateAvg;
        }

        public String getPowerTrainDuration() {
            return PowerTrainDuration;
        }

        public void setPowerTrainDuration(String powerTrainDuration) {
            PowerTrainDuration = powerTrainDuration;
        }

        public String getIsPlay() {
            return IsPlay;
        }

        public void setIsPlay(String isPlay) {
            IsPlay = isPlay;
        }

        public void setHeartRateAvg(String HeartRateAvg) {
            this.HeartRateAvg = HeartRateAvg;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int Duration) {
            this.Duration = Duration;
        }

        public int getValidTime() {
            return ValidTime;
        }

        public void setValidTime(int ValidTime) {
            this.ValidTime = ValidTime;
        }

        public String getKCal() {
            return KCal;
        }

        public void setKCal(String KCal) {
            this.KCal = KCal;
        }
    }
}
