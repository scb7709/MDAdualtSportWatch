package com.headlth.management.entity;


import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/10/31.
 */
public class QuestionaireResultJson implements Serializable{
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public PrescriptionList prescriptionList;
    public String SPID;
    public String IsHighRisk;

    public String IsMoney;
    public String IsUserPay;
    public String IfUserOwn;


    @Override
    public String toString() {
        return "QuestionaireResultJson{" +
                "Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", prescriptionList=" + prescriptionList +
                '}';
    }

    public static class PrescriptionList implements Serializable{
        @Override
        public String toString() {
            return "PrescriptionList{" +
                    "TotalWeekSportDuration='" + TotalWeekSportDuration + '\'' +
                    ", WeekSportDays='" + WeekSportDays + '\'' +
                    ", SportDurationOneTime='" + SportDurationOneTime + '\'' +
                    ", SportModel='" + SportModel + '\'' +
                    ", UBound='" + UBound + '\'' +
                    ", LBound='" + LBound + '\'' +
                    ", Content='" + Content + '\'' +
                    ", ListRemark=" + ListRemark +
                    '}';
        }

        public String TotalWeekSportDuration;
        public String WeekSportDays;
        public String SportDurationOneTime;
        public String SportModel="";
        public String UBound;
        public String LBound;
        public String Content;
        public List<ListRemarkClass> ListRemark;


        public static class ListRemarkClass implements Serializable{
            @Override
            public String toString() {
                return "ListRemark{" +
                        "Type='" + Type + '\'' +
                        ", Title='" + Title + '\'' +
                        ", Content='" + Content + '\'' +
                        '}';
            }

            public String Type;
            public String Title;
            public String Content;
        }

    }

}
