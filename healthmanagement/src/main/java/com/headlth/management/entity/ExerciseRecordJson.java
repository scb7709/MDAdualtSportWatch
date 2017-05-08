package com.headlth.management.entity;


import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/10/31.
 */
public class ExerciseRecordJson implements Serializable {
    public int Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public List<UserSportListBean> UserSportList;

    public static class UserSportListBean implements Serializable {
        public String HeartRateAvg;
        public int Duration;
        public int ValidTime;
        public String KCal;
        public int PowerTrainDuration;
        public String BodyPart;
        public int maxHeartRate;
        public int minHeartRate;

        public String PowerPrescriptionTitle;
        public String SportPrescriptionTitle;
        public int PowerStage;
        public int SportStage;
        public int IsSportStart;
        public int SportFinishedDays;
        public int  PowerFinishedDays;

        @Override
        public String toString() {
            return "UserSportListBean{" +
                    "HeartRateAvg='" + HeartRateAvg + '\'' +
                    ", Duration=" + Duration +
                    ", ValidTime=" + ValidTime +
                    ", KCal='" + KCal + '\'' +
                    ", PowerTrainDuration='" + PowerTrainDuration + '\'' +
                    '}';
        }
    }

}
