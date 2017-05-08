package com.headlth.management.movelistview;

/**
 * Created by Administrator on 2016/4/27.
 */
public class messegeEntity {
   /* "ID": 4,
            "UserID": 4,
            "Title": "新的运动周已开始",
            "Content": "上周的运动目标未完成。本周的运动处方已开始，请继续努力。",
            "CreateTime": "2016/4/25 17:12:41"
*/


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

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
