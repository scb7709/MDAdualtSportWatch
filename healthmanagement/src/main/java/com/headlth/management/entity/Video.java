package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/7/5.
 */
public class Video implements Serializable {
    private static final long serialVersionUID = 1010001232L;
    private String ID;
    private String Name;
    private String ImgUrl;
    private String Description;
    private String Duration;
    private String FitPerson;
    private String TotalCal;
    private String ActionCount;
    private String Tip;
    private String Quotes;
    private String TrainTime;
    private long VideoSize;
    private String TrainRule;
    private String Stage;
    private List<SubVideo> ActionList;


    public long getVideoSize() {
        return VideoSize;
    }

    public void setVideoSize(long videoSize) {
        VideoSize = videoSize;
    }

    public static class SubVideo implements Serializable {
        private static final long serialVersionUID = 1010001233L;
        private String Num;//本地编号（数据库的主键ID）
        private String ID;//网络给的ID
        private String Name;
        private String Address;//网络视频路径
        private String Count;
        private String Cal;
        private String Duration;
        private String ImgUrl;
        private String Interval;
        private String VerificationCode;//网络MD5
        private String VerificationCodeLocal;//本地MD5
        private String localAddress;//本地视频路径
        private long Size;
        private String FileName;
        private String CreateTime;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getVerificationCodeLocal() {
            return VerificationCodeLocal;
        }

        public void setVerificationCodeLocal(String verificationCodeLocal) {
            VerificationCodeLocal = verificationCodeLocal;
        }

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public long getSize() {
            return Size;
        }

        public void setSize(long size) {
            Size = size;
        }

        public SubVideo(String num, String ID, String name, String address, String count, String cal, String duration, String imgUrl, String interval, String verificationCode, String VerificationCodeLocal, String localAddress) {
            Num = num;
            this.ID = ID;
            Name = name;
            Address = address;
            Count = count;
            Cal = cal;
            Duration = duration;
            ImgUrl = imgUrl;
            Interval = interval;
            this.VerificationCodeLocal = VerificationCodeLocal;
            this.localAddress = localAddress;
            VerificationCode = verificationCode;
        }

        public String getNum() {
            return Num;
        }

        public void setNum(String num) {
            Num = num;
        }


        public String getLocalAddress() {
            return localAddress;
        }

        public void setLocalAddress(String localAddress) {
            this.localAddress = localAddress;
        }


        public String getVerificationCode() {
            return VerificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            VerificationCode = verificationCode;
        }


        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }

        public String getCal() {
            return Cal;
        }

        public void setCal(String cal) {
            Cal = cal;
        }

        public String getDuration() {
            return Duration;
        }

        public void setDuration(String duration) {
            Duration = duration;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getInterval() {
            return Interval;
        }

        public void setInterval(String interval) {
            Interval = interval;
        }

        @Override
        public String toString() {
            return "SubVideo{" +
                    "ID='" + ID + '\'' +
                    ", Name='" + Name + '\'' +
                    ", Address='" + Address + '\'' +
                    ", Count='" + Count + '\'' +
                    ", Cal='" + Cal + '\'' +
                    ", Duration='" + Duration + '\'' +
                    ", ImgUrl='" + ImgUrl + '\'' +
                    ", Interval='" + Interval + '\'' +
                    '}';
        }
    }

    public String getTip() {
        return Tip;
    }



    public void setTip(String tip) {
        Tip = tip;
    }

    public String getQuotes() {
        return Quotes;
    }

    public void setQuotes(String quotes) {
        Quotes = quotes;
    }

    public String getTrainTime() {
        return TrainTime;
    }

    public void setTrainTime(String trainTime) {
        TrainTime = trainTime;
    }

    public String getTrainRule() {
        return TrainRule;
    }

    public void setTrainRule(String trainRule) {
        TrainRule = trainRule;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getFitPerson() {
        return FitPerson;
    }

    public void setFitPerson(String fitPerson) {
        FitPerson = fitPerson;
    }

    public String getTotalCal() {
        return TotalCal;
    }

    public void setTotalCal(String totalCal) {
        TotalCal = totalCal;
    }

    public String getActionCount() {
        return ActionCount;
    }

    public void setActionCount(String actionCount) {
        ActionCount = actionCount;
    }

    public List<SubVideo> getActionList() {
        return ActionList;
    }

    public void setActionList(List<SubVideo> actionList) {
        ActionList = actionList;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }

    @Override
    public String toString() {
        return "Video{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", ImgUrl='" + ImgUrl + '\'' +
                ", Description='" + Description + '\'' +
                ", Duration='" + Duration + '\'' +
                ", FitPerson='" + FitPerson + '\'' +
                ", TotalCal='" + TotalCal + '\'' +
                ", ActionCount='" + ActionCount + '\'' +
                ", ActionList=" + ActionList +
                '}';
    }
}
