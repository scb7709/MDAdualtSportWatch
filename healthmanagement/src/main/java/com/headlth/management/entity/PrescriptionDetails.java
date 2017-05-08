package com.headlth.management.entity;

import java.util.List;

/**
 * Created by abc on 2016/10/31.
 */
public class PrescriptionDetails {

    public String PlanName;
    public String Description;
    public String QuestionnaireID;
    public String Status;
    public List<ImgUrlListClass> ImgUrlList;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public String IsExpire;
    public String IsUserPay;
    public String IfUserOwn;
    public String IsMoney;
    public String IsMoneyNew;
    public String  IsButtonsShow;
    public int   IsToPay;

    public  static  class ImgUrlListClass{
        public String ID;
        public String Content;
    }
}
