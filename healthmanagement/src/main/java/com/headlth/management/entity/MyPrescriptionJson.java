package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/12/5.
 */
public class MyPrescriptionJson {
    public String Status;
    public List<MyPrescription> PList;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;

    @Override
    public String toString() {
        return "MyPrescriptionJson{" +
                "Status='" + Status + '\'' +
                ", PList=" + PList +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                '}';
    }

    public class MyPrescription implements Serializable{
        public String SPID;
        public String UserID;
        public String Title;
      //  public String PrescriptionState;
      //  public String CreateTime;
      //  public String UpdateTime="";
      //  public String Procscript_Start;
        public String Procscript_Stop="";
       public String Description;
        public String IsDel;
        public String CreateTime;
        public String UpdateTime;
        public String UBound;
        public String LBound;
        public String Duration;
        public String ReportID;
        public String SportTarget;
        public String SuggestReport;
        public String WeeklyTotalFrom;
        public String WeeklyTotalTo;
        public String DailyFrom;
        public String DailyTo;
        public String WeekAtLeast;
        public String DailyDurationFrom;
        public String DailyDurationTo;

        public String DurationRemark;
        public String BestTime;
        public String BestTimeRemark;
        public String Problem;
        public String Stage;
        public String Stageweeks;
        public String Prescription_Start;
        public String Prescription_End;




        public String Arg;
        public String PrescriptionState;
        public String QuestionnaireID;
        public String PlanNameID;



        @Override
        public String toString() {
            return "MyPrescription{" +
                    "SPID='" + SPID + '\'' +
                    ", UserID='" + UserID + '\'' +
                    ", Title='" + Title + '\'' +
                    ", Procscript_Stop='" + Procscript_Stop + '\'' +
                    ", Description='" + Description + '\'' +
                    ", IsDel='" + IsDel + '\'' +
                    ", CreateTime='" + CreateTime + '\'' +
                    ", UpdateTime='" + UpdateTime + '\'' +
                    ", UBound='" + UBound + '\'' +
                    ", LBound='" + LBound + '\'' +
                    ", Duration='" + Duration + '\'' +
                    ", ReportID='" + ReportID + '\'' +
                    ", SportTarget='" + SportTarget + '\'' +
                    ", SuggestReport='" + SuggestReport + '\'' +
                    ", WeeklyTotalFrom='" + WeeklyTotalFrom + '\'' +
                    ", WeeklyTotalTo='" + WeeklyTotalTo + '\'' +
                    ", DailyFrom='" + DailyFrom + '\'' +
                    ", DailyTo='" + DailyTo + '\'' +
                    ", WeekAtLeast='" + WeekAtLeast + '\'' +
                    ", DailyDurationFrom='" + DailyDurationFrom + '\'' +
                    ", DailyDurationTo='" + DailyDurationTo + '\'' +
                    ", DurationRemark='" + DurationRemark + '\'' +
                    ", BestTime='" + BestTime + '\'' +
                    ", BestTimeRemark='" + BestTimeRemark + '\'' +
                    ", Problem='" + Problem + '\'' +
                    ", Stage='" + Stage + '\'' +
                    ", Stageweeks='" + Stageweeks + '\'' +
                    ", Procscript_Start='" + Prescription_Start + '\'' +
                    ", Arg='" + Arg + '\'' +
                    ", PrescriptionState='" + PrescriptionState + '\'' +
                    ", QuestionnaireID='" + QuestionnaireID + '\'' +
                    ", PlanNameID='" + PlanNameID + '\'' +
                    '}';
        }
    }

    /*

    "PList": [
    {
      "SPID": 506,
      "UserID": 4,
      "Title": "802280000的运动处方",
      "Description": null,
      "IsDel": 0,
      "CreateTime": "2016-11-02T19:01:46",
      "UpdateTime": "2016-11-02T19:01:46",
      "UBound": 20,
      "LBound": 0,
      "Duration": 0,
      "ReportID": 0,
      "SportTarget": "提高心肺功能\n延长寿命\n",
      "SuggestReport": "",
      "WeeklyTotalFrom": 0,
      "WeeklyTotalTo": 0,
      "DailyFrom": 1,
      "DailyTo": 2,
      "WeekAtLeast": 0,
      "DailyDurationFrom": 0,
      "DailyDurationTo": 0,
      "DurationRemark": "除热身与整理运动外，保持运动下心率维持在靶心率范围内至少0分钟以上。",
      "BestTime": "下午3-5点",
      "BestTimeRemark": "下午运动可降低运动损伤风险，同时有益于身体能源物质的消耗。",
      "Problem": "",
      "Stage": 0,
      "Stageweeks": 0,
      "Procscript_Start": "0001-01-01T00:00:00",
      "Arg": null,
      "PrescriptionState": 1


     */
}
