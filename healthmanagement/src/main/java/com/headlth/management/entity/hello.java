package com.headlth.management.entity;

/**
 * Created by Administrator on 2016/6/3.
 */
public class hello {
    int y;
    int x;
    String mm;
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

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public hello(int y, String mm, int x) {
        this.y = y;
        this.mm = mm;
        this.x = x;
    }

    @Override
    public String toString() {
        return "hello{" +
                "y=" + y +
                ", x=" + x +
                ", mm='" + mm + '\'' +
                '}';
    }

















    public int getUserID() {
        return UserID;
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
