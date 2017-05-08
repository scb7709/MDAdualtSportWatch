package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class xiaoXiCallBack implements Serializable {


    /**
     * Status : 1
     * MsgList : {"Date":["04月25日","04月22日"],"Info":[{"List_msgdata":[{"ID":7,"UserID":4,"Title":"新的运动周已开始","Content":"本周有未完成的运动目标，请继续努力。","CreateTime":"2016/4/25 17:14:15"},{"ID":6,"UserID":4,"Title":"本周有未完成的运动目标","Content":"本周有未完成的运动目标，请继续努力。","CreateTime":"2016/4/25 17:13:32"},{"ID":4,"UserID":4,"Title":"新的运动周已开始","Content":"上周的运动目标未完成。本周的运动处方已开始，请继续努力。","CreateTime":"2016/4/25 17:12:41"}]},{"List_msgdata":[{"ID":1,"UserID":4,"Title":"新的运动周已开始","Content":"上周的运动目标未完成。本周的运动处方已开始，请继续努力。","CreateTime":"2016/4/22 14:34:35"}]}]}
     * Message : 获取用户消息成功!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */

    private int Status;
    private MsgListBean MsgList;
    private String Message;
    private boolean IsSuccess;
    private boolean IsError;
    private Object ErrMsg;
    private Object ErrCode;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public MsgListBean getMsgList() {
        return MsgList;
    }

    public void setMsgList(MsgListBean MsgList) {
        this.MsgList = MsgList;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public boolean isIsError() {
        return IsError;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public Object getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(Object ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public Object getErrCode() {
        return ErrCode;
    }

    public void setErrCode(Object ErrCode) {
        this.ErrCode = ErrCode;
    }

    public static class MsgListBean implements Serializable {
        private List<String> Date;
        private List<InfoBean> Info;

        public List<String> getDate() {
            return Date;
        }

        public void setDate(List<String> Date) {
            this.Date = Date;
        }

        public List<InfoBean> getInfo() {
            return Info;
        }

        public void setInfo(List<InfoBean> Info) {
            this.Info = Info;
        }

        public static class InfoBean implements Serializable {
            /**
             * ID : 7
             * UserID : 4
             * Title : 新的运动周已开始
             * Content : 本周有未完成的运动目标，请继续努力。
             * CreateTime : 2016/4/25 17:14:15
             */

            private List<ListMsgdataBean> List_msgdata;

            public List<ListMsgdataBean> getList_msgdata() {
                return List_msgdata;
            }

            public void setList_msgdata(List<ListMsgdataBean> List_msgdata) {
                this.List_msgdata = List_msgdata;
            }

            public static class ListMsgdataBean implements Serializable {
                private int ID;
                private int UserID;
                private String Title;
                private String Content;
                private String CreateTime;

                public int getID() {
                    return ID;
                }

                public void setID(int ID) {
                    this.ID = ID;
                }

                public int getUserID() {
                    return UserID;
                }

                public void setUserID(int UserID) {
                    this.UserID = UserID;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }

                public String getContent() {
                    return Content;
                }

                public void setContent(String Content) {
                    this.Content = Content;
                }

                public String getCreateTime() {
                    return CreateTime;
                }

                public void setCreateTime(String CreateTime) {
                    this.CreateTime = CreateTime;
                }
            }
        }
    }
}
