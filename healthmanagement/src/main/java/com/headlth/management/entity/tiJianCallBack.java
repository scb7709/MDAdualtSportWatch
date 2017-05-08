package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class tiJianCallBack implements Serializable{
    /**
     * Status : 1
     * UserInfo : [{"Title":"姓名","Content":"李宇欣"},{"Title":"性别","Content":"女"},{"Title":"年龄","Content":"26"},{"Title":"身高","Content":"163"},{"Title":"体重","Content":"65"},{"Title":"安静心率","Content":"71"}]
     * AbnormalList : [{"Title":"异常A","Content":"肥胖，吃得太多，不爱运动"},{"Title":"异常B","Content":"心脏不好，吃得太多，不爱运动"}]
     * Message : 获取成功!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */
    private int Status;
    private String Message;
    private boolean IsSuccess;
    private boolean IsError;
    private Object ErrMsg;
    private Object ErrCode;
    /**
     * Title : 姓名
     * Content : 李宇欣
     */

    private List<UserInfoEntity> UserInfo;
    /**
     * Title : 异常A
     * Content : 肥胖，吃得太多，不爱运动
     */

    private List<AbnormalListEntity> AbnormalList;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
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

    public List<UserInfoEntity> getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(List<UserInfoEntity> UserInfo) {
        this.UserInfo = UserInfo;
    }

    public List<AbnormalListEntity> getAbnormalList() {
        return AbnormalList;
    }

    public void setAbnormalList(List<AbnormalListEntity> AbnormalList) {
        this.AbnormalList = AbnormalList;
    }

    public static class UserInfoEntity  implements  Serializable{
        private String Title;
        private String Content;

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
    }

    public static class AbnormalListEntity  implements  Serializable{
        private String Title;
        private String Content;

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
    }
}
