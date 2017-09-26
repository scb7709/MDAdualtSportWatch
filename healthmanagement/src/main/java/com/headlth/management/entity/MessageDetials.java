package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2017/4/12
 */
public class MessageDetials {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public String[] PushPictureInfoList;

    public List<Medication> PushMedicInfoList;

    public class Medication implements Serializable {
      //  public  int medictimeslotid;
        public String MedicationTimeName;
        public String DrugName;
        public String DosageSize;
        public String MinDoseUnit;
        public boolean isfirst;

      /*  "medictimeslotid": 1,
                "MedicationTimeName": "早餐前",
                "medicationtimeid": 70,
                "DrugName": "kg",
                "DosageSize": "1",
                "MinDoseUnit": "g"*/

        @Override
        public String toString() {
            return "Medication{" +
                   // "medictimeslotid=" + medictimeslotid +
                    ", MedicationTimeName='" + MedicationTimeName + '\'' +
                    ", DrugName='" + DrugName + '\'' +
                    ", DosageSize='" + DosageSize + '\'' +
                    ", MinDoseUnit='" + MinDoseUnit + '\'' +
                    ", isfirst=" + isfirst +
                    '}';
        }
    }

}
