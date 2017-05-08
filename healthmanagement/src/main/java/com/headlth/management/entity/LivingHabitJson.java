package com.headlth.management.entity;

import java.util.List;

/**
 * Created by abc on 2016/10/18.
 */
public class LivingHabitJson {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public List<ListDetail> ListDetail;

    public static class ListDetail {
        public String ListID;
        public String ListName;
        public List<ListItems> ListItems;

        public static class ListItems {
            public String ID;
            public String Content;
            public int IsChosen;

        }
    }
}
