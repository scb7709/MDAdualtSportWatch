package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class logcallback implements Serializable {

    /**
     * Status : 1
     * UserList : [{"UserID":4,"UserRealname":"李宇欣","Sex":2,"NickName":"xiaohe","Mobile":"13811381138","LBound":100,"UBound":200,"LastSportTime":"2016/3/17 14:12:15","HeartRateAvg":"0.0000","Duration":0,"ValidTime":0,"BigCar":"0","target":"0"}]
     * Message : 登陆成功!
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
     * UserID : 4
     * UserRealname : 李宇欣
     * Sex : 2
     * NickName : xiaohe
     * Mobile : 13811381138
     * LBound : 100
     * UBound : 200
     * LastSportTime : 2016/3/17 14:12:15
     * HeartRateAvg : 0.0000
     * Duration : 0
     * ValidTime : 0
     * BigCar : 0
     * target : 0
     */

    private List<UserListEntity> UserList;



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

    public List<UserListEntity> getUserList() {
        return UserList;
    }

    public void setUserList(List<UserListEntity> UserList) {
        this.UserList = UserList;
    }


    public static class UserListEntity implements Serializable{
        private int UserID;
        private String UserRealname;
        private int Sex;
        private String NickName;
        private String Mobile;
        private int LBound;
        private int UBound;
        private String LastSportTime;
        private String HeartRateAvg;
        private int Duration;
        private int ValidTime;
        private String BigCar;
        private String target;
        private String PowerTrainDuration;
        private String  IsPlay;
        private String  PowerFinishedCount;

        private List<Integer> vlist;
        public int getUserID() {
            return UserID;
        }

        public List<Integer> getVlist() {
            return vlist;
        }

        public String getPowerFinishedCount() {
            return PowerFinishedCount;
        }

        public void setPowerFinishedCount(String powerFinishedCount) {
            PowerFinishedCount = powerFinishedCount;
        }

        public void setVlist(List<Integer> vlist) {
            this.vlist = vlist;
        }

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public String getUserRealname() {
            return UserRealname;
        }

        public void setUserRealname(String UserRealname) {
            this.UserRealname = UserRealname;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int Sex) {
            this.Sex = Sex;
        }

        public String getNickName() {
            return NickName;
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

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public int getLBound() {
            return LBound;
        }

        public void setLBound(int LBound) {
            this.LBound = LBound;
        }

        public int getUBound() {
            return UBound;
        }

        public void setUBound(int UBound) {
            this.UBound = UBound;
        }

        public String getLastSportTime() {
            return LastSportTime;
        }

        public void setLastSportTime(String LastSportTime) {
            this.LastSportTime = LastSportTime;
        }

        public String getHeartRateAvg() {
            return HeartRateAvg;
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

        public String getBigCar() {
            return BigCar;
        }

        public void setBigCar(String BigCar) {
            this.BigCar = BigCar;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }
}
