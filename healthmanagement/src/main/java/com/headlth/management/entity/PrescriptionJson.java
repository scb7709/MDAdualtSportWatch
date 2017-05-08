package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/10/31.
 */
public class PrescriptionJson {
    public String Status;
    public List<PrescriptionClass> PlanNameList;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public static class PrescriptionClass implements Serializable {
        public String PlanNameID;
        public String PlanName;
        public String PlanClassID;
        public String IsMoney;
        public String SportTarget;
        public String QuestionnaireID;
        public String IfUserOwn;
        public String PPID;
        public String SPID;
        public String IsUserPay;
        public int   IsToPay;
        @Override
        public String toString() {
            return "PrescriptionClass{" +
                    "PlanNameID='" + PlanNameID + '\'' +
                    ", PlanName='" + PlanName + '\'' +
                    ", PlanClassID='" + PlanClassID + '\'' +
                    ", IsMoney='" + IsMoney + '\'' +
                    ", SportTarget='" + SportTarget + '\'' +
                    ", QuestionnaireID='" + QuestionnaireID + '\'' +
                    ", IfUserOwn='" + IfUserOwn + '\'' +
                    ", PPID='" + PPID + '\'' +
                    ", SPID='" + SPID + '\'' +
                    ", IsUserPay='" + IsUserPay + '\'' +
                    '}';
        }
    }
}
