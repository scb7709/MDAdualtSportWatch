package com.headlth.management.entity;

import java.io.Serializable;

/**
 * Created by abc on 2016/8/26.
 */
public class Reply implements Serializable {
    private String UserID;
    private  String ReplyText;
    private String UserRealname;
    private String CommentID;
    private String ContentID;
    private String ReplyID;
    private String BelongReplyID;
    private int ReplyCount;
    private int AttitudeCount;
    private String CreateTime;
    private String usericon;
    private String IsAttitude;
    private String UserImageUrl;
    @Override
    public String toString() {
        return "Comment{" +
                "UserID='" + UserID + '\'' +
                ", CommentText='" + ReplyText + '\'' +
                ", UserRealname='" + UserRealname + '\'' +
                ", CommentID='" + CommentID + '\'' +
                ", ContentID='" + ContentID + '\'' +
                ", ReplyCount='" + ReplyCount + '\'' +
                ", AttitudeCount='" + AttitudeCount + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", usericon='" + usericon + '\'' +
                ", IsAttitude='" + IsAttitude + '\'' +
                '}';
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

    public String getReplyText() {
        return ReplyText;
    }

    public void setReplyText(String replyText) {
        ReplyText = replyText;
    }

    public String getReplyID() {
        return ReplyID;
    }

    public void setReplyID(String replyID) {
        ReplyID = replyID;
    }

    public String getBelongReplyID() {
        return BelongReplyID;
    }

    public void setBelongReplyID(String belongReplyID) {
        BelongReplyID = belongReplyID;
    }

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        ContentID = contentID;
    }



    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserRealname() {
        return UserRealname;
    }

    public void setUserRealname(String userRealname) {
        UserRealname = userRealname;
    }

    public String getCommentID() {
        return CommentID;
    }

    public void setCommentID(String commentID) {
        CommentID = commentID;
    }

    public int getReplyCount() {
        return ReplyCount;
    }

    public void setReplyCount(int replyCount) {
        ReplyCount = replyCount;
    }

    public int getAttitudeCount() {
        return AttitudeCount;
    }

    public void setAttitudeCount(int attitudeCount) {
        AttitudeCount = attitudeCount;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getIsAttitude() {
        return IsAttitude;
    }

    public void setIsAttitude(String isAttitude) {
        IsAttitude = isAttitude;
    }
}
