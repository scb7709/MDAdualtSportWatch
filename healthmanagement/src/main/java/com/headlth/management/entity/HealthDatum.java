package com.headlth.management.entity;

import java.util.List;

/**
 * Created by abc on 2016/11/24.
 */
public class HealthDatum {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public List<Abnormal> AbnormalList;
    public static class Abnormal {
        public String Title;
        public String Content;
    }


}
