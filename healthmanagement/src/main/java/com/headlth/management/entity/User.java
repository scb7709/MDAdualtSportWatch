package com.headlth.management.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by abc on 2016/10/17.
 */
public class User  implements Serializable {
    private String UID;
    private String phone;
    private String PWD;
    private String sinaOpenID;
    private String chatOpenID;
    private String QQOpenID;
   // private String QQOpenID;

    private String loginFlag;
    public UserInformation userInformation;

    public User() {
    }

    public User(String UID, String phone, String PWD, String sinaOpenID, String chatOpenID, String QQOpenID, String loginFlag, UserInformation userInformation) {
        this.UID = UID;
        this.phone = phone;
        this.PWD = PWD;
        this.sinaOpenID = sinaOpenID;
        this.chatOpenID = chatOpenID;
        this.QQOpenID = QQOpenID;
        this.loginFlag = loginFlag;
        this.userInformation = userInformation;
    }

    @Override
    public String toString() {
        return "User{" +
                "UID='" + UID + '\'' +
                ", phone='" + phone + '\'' +
                ", sinaOpenID='" + sinaOpenID + '\'' +
                ", chatOpenID='" + chatOpenID + '\'' +
                ", QQOpenID='" + QQOpenID + '\'' +
                ", PWD='" + PWD + '\'' +
                ", loginFlag='" + loginFlag + '\'' +
                ", userInformation=" + userInformation +
                '}';
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSinaOpenID() {
        return sinaOpenID;
    }

    public void setSinaOpenID(String sinaOpenID) {
        this.sinaOpenID = sinaOpenID;
    }

    public String getChatOpenID() {
        return chatOpenID;
    }

    public void setChatOpenID(String chatOpenID) {
        this.chatOpenID = chatOpenID;
    }

    public String getQQOpenID() {
        return QQOpenID;
    }

    public void setQQOpenID(String QQOpenID) {
        this.QQOpenID = QQOpenID;
    }

    public String getPwd() {
        return PWD;
    }

    public void setPwd(String PWD) {
        this.PWD = PWD;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public UserInformation getUserInformation() {
        if(userInformation!=null){
            return userInformation;
        }else {
            return new UserInformation();
        }
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public class UserInformation  implements Serializable {
        private String NickName;//昵称
        private String Weight;
        private String Height;
        private String Gender;
        private String Birthday;
        private String File;//头像
       // private String [] Illness;//坏习惯
       // private String [] Badhabit;//疾病


        public UserInformation(){}
        public UserInformation(String nickName, String weight, String height, String gender, String birthday, String file) {
            NickName = nickName;
            Weight = weight;
            Height = height;
            Gender = gender;
            Birthday = birthday;
            File = file;
        }

   /*     public UserInformation(String nickName, String weight, String height, String gender, String birthday, String file*//*, String[] illness, String[] badhabit*//*) {
            NickName = nickName;
            Weight = weight;
            Height = height;
            Gender = gender;
            Birthday = birthday;
            File = file;
           // Illness = illness;
        //    Badhabit = badhabit;
        }*/

        @Override
        public String toString() {
            return "UserInformation{" +
                    "NickName='" + NickName + '\'' +
                    ", Weight='" + Weight + '\'' +
                    ", Height='" + Height + '\'' +
                    ", Gender='" + Gender + '\'' +
                    ", Birthday='" + Birthday + '\'' +
                    ", File='" + File + '\'' +
                  /*  ", Illness=" + Arrays.toString(Illness) +
                    ", Badhabit=" + Arrays.toString(Badhabit) +*/
                    '}';
        }

   /*     public String[] getIllness() {
            return Illness;
        }

        public void setIllness(String[] illness) {
            Illness = illness;
        }

        public String[] getBadhabit() {
            return Badhabit;
        }

        public void setBadhabit(String[] badhabit) {
            Badhabit = badhabit;
        }*/

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String weight) {
            Weight = weight;
        }

        public String getHeight() {
            return Height;
        }

        public void setHeight(String height) {
            Height = height;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getBirthday() {
            return Birthday;
        }

        public void setBirthday(String birthday) {
            Birthday = birthday;
        }

        public String getFile() {
            return File;
        }

        public void setFile(String file) {
            File = file;
        }

    }

}
