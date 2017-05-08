package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/8/15.
 */
public class Circle implements Serializable {
    private String UserID;
    private String UserRealname;
    private String ContentID;
    private String UserImageUrl;
    private String ContentText;
    private String CreateTime;
    private String IsAttitude;
    private List<String> PicUrl;
    private int CommentCount;
    private int AttitudeCount;

    @Override
    public String toString() {
        return "Circle{" +
                "UserID='" + UserID + '\'' +
                ", UserRealname='" + UserRealname + '\'' +
                ", ContentID='" + ContentID + '\'' +
                ", avatarUrl='" + UserImageUrl + '\'' +
                ", ContentText='" + ContentText + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", IsAttitude='" + IsAttitude + '\'' +
                ", PicUrl=" + PicUrl +
                ", CommentCount=" + CommentCount +
                ", AttitudeCount=" + AttitudeCount +
                '}';
    }

    public Circle(String userphone, String username, String avatarUrl, String content, List<String> imageUrls, int commentCount, int likeCount) {
        this.UserID = userphone;
        this.UserRealname = username;
        this.UserImageUrl = avatarUrl;
        this.ContentText = content;
        this.PicUrl = imageUrls;
        this.CommentCount = commentCount;
        this.AttitudeCount = likeCount;
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

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        ContentID = contentID;
    }

    public String getContentText() {
        return ContentText;
    }

    public void setContentText(String contentText) {
        ContentText = contentText;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getIsAttitude() {
        return IsAttitude;
    }

    public void setIsAttitude(String isAttitude) {
        IsAttitude = isAttitude;
    }

    public List<String> getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        PicUrl = picUrl;
    }

    public int getAttitudeCount() {
        return AttitudeCount;
    }

    public void setAttitudeCount(int attitudeCount) {
        AttitudeCount = attitudeCount;
    }

    public Circle(String userID, String userRealname, String contentID, String avatarUrl, String contentText, String createTime, String isAttitude, List<String> picUrl, int commentCount, int attitudeCount) {
        UserID = userID;
        UserRealname = userRealname;
        ContentID = contentID;
        this.UserImageUrl = avatarUrl;
        ContentText = contentText;
        CreateTime = createTime;
        IsAttitude = isAttitude;
        PicUrl = picUrl;
        CommentCount = commentCount;
        AttitudeCount = attitudeCount;
    }

    public String getUserphone() {
        return UserID;
    }

    public void setUserphone(String userphone) {
        this.UserID = userphone;
    }

    public String getUsername() {
        return UserRealname;
    }

    public void setUsername(String username) {
        this.UserRealname = username;
    }

    public String getAvatarUrl() {
        return UserImageUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.UserImageUrl = avatarUrl;
    }
    public void setContent(String content) {
        this.ContentText = content;
    }

    public List<String> getImageUrls() {
        return PicUrl;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.PicUrl = imageUrls;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        this.CommentCount = commentCount;
    }

    public int getLikeCount() {
        return AttitudeCount;
    }

    public void setLikeCount(int likeCount) {
        this.AttitudeCount = likeCount;
    }
}
