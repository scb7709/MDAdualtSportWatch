package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/11/29.
 */
public class MyOrderJson {
    public String Status;
    public List<MyOrder> OrderList;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;

    public class MyOrder implements Serializable {
        public String OrderID;
        public String OrderStatusID;
        public String OrderStatus;
        public String OrderName;
        public double Amount;
        public String PlanNameID;

    }

}
