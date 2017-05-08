package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * * Created by abc on 2017/4/12
 */
public class MessageList {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public List<Message> MsgList;

    @Override
    public String toString() {
        return "MessageList{" +
                "MsgList=" + MsgList +
                '}';
    }

    public class Message implements  Serializable {
        public int ID;
        public String Title;
        public String Content;
        public String CreateTime;
        public String CreateMonth;
        public int MsgtypeId;
        public int MedictimeslotId;

        @Override
        public String toString() {
            return "Message{" +
                    "ID=" + ID +
                    ", Title='" + Title + '\'' +
                    ", Content='" + Content + '\'' +
                    ", CreateTime='" + CreateTime + '\'' +
                    ", CreateMonth='" + CreateMonth + '\'' +
                    ", MsgtypeId=" + MsgtypeId +
                    ", MedictimeslotId=" + MedictimeslotId +
                    '}';
        }
    }
}
