package com.headlth.management.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/29.
 */
public class historyDate implements Serializable {
    private String UID;
    private String Data;
    private String WatchType;
    private String EveryTime;
    private String EveryVolidTime;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getWatchType() {
        return WatchType;
    }

    public void setWatchType(String watchType) {
        WatchType = watchType;
    }

    public String getEveryTime() {
        return EveryTime;
    }

    public void setEveryTime(String everyTime) {
        EveryTime = everyTime;
    }

    public String getEveryVolidTime() {
        return EveryVolidTime;
    }

    public void setEveryVolidTime(String everyVolidTime) {
        EveryVolidTime = everyVolidTime;
    }

    @Override
    public String toString() {
        return "historyDate{" +
                "UID='" + UID + '\'' +
                ", Data='" + Data + '\'' +
                ", WatchType='" + WatchType + '\'' +
                ", EveryTime='" + EveryTime + '\'' +
                ", EveryVolidTime='" + EveryVolidTime + '\'' +
                '}';
    }
}
