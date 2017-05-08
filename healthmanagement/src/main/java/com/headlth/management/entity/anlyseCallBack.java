package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class anlyseCallBack  implements Serializable{
    /**
     * Status : 1
     * Data : {"Detail":[{"Calory":"0","Day":"周六","EffectTime":"0","StatDate":"2016-03-12","TotalTime":"0"},{"Calory":"0","Day":"周日","EffectTime":"0","StatDate":"2016-03-13","TotalTime":"0"},{"Calory":"0","Day":"周一","EffectTime":"0","StatDate":"2016-03-14","TotalTime":"0"},{"Calory":"0","Day":"周二","EffectTime":"0","StatDate":"2016-03-15","TotalTime":"0"},{"Calory":"0","Day":"周三","EffectTime":"0","StatDate":"2016-03-16","TotalTime":"0"},{"Calory":"10","Day":"周四","EffectTime":"77","StatDate":"2016-03-17","TotalTime":"77"},{"Calory":"18710","Day":"周五","EffectTime":"4477","StatDate":"2016-03-18","TotalTime":"127462"}],"Summary":[{"AvgEffectTime":"197","AvgTotalTime":"360","AvgCal":"2674","CaloryRate":"希望你，再接再厉","MaxCalory":"18800","MaxTotalTime":"5400","Percentage":"54.72%","UID":"4","TotalCal":"18720","TotalDays":"1"}]}
     * Message : 获取成功!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */
    private int Status;
    private DataBean Data;
    private String Message;
    private boolean IsSuccess;
    private boolean IsError;
    private Object ErrMsg;
    private Object ErrCode;

    @Override
    public String toString() {
        return "anlyseCallBack{" +
                "Status=" + Status +
                ", Data=" + Data +
                ", Message='" + Message + '\'' +
                ", IsSuccess=" + IsSuccess +
                ", IsError=" + IsError +
                ", ErrMsg=" + ErrMsg +
                ", ErrCode=" + ErrCode +
                '}';
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
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

    public static class DataBean  implements  Serializable{

        @Override
        public String toString() {
            return "DataBean{" +
                    "Detail=" + Detail +
                    ", Summary=" + Summary +
                    '}';
        }

        /**
         * Calory : 0
         * Day : 周六
         * EffectTime : 0
         * StatDate : 2016-03-12
         * TotalTime : 0
         */

        private List<DetailBean> Detail;
        /**
         * AvgEffectTime : 197
         * AvgTotalTime : 360
         * AvgCal : 2674
         * CaloryRate : 希望你，再接再厉
         * MaxCalory : 18800
         * MaxTotalTime : 5400
         * Percentage : 54.72%
         * UID : 4
         * TotalCal : 18720
         * TotalDays : 1
         */

        private List<SummaryBean> Summary;

        public List<DetailBean> getDetail() {
            return Detail;
        }

        public void setDetail(List<DetailBean> Detail) {
            this.Detail = Detail;
        }

        public List<SummaryBean> getSummary() {
            return Summary;
        }

        public void setSummary(List<SummaryBean> Summary) {
            this.Summary = Summary;
        }

        public static class DetailBean  implements  Serializable{
            private String Calory;
            private String Day;
            private String EffectTime;
            private String StatDate;
            private String TotalTime;

            @Override
            public String toString() {
                return "DetailBean{" +
                        "Calory='" + Calory + '\'' +
                        ", Day='" + Day + '\'' +
                        ", EffectTime='" + EffectTime + '\'' +
                        ", StatDate='" + StatDate + '\'' +
                        ", TotalTime='" + TotalTime + '\'' +
                        '}';
            }

            public String getCalory() {
                return Calory;
            }

            public void setCalory(String Calory) {
                this.Calory = Calory;
            }

            public String getDay() {
                return Day;
            }

            public void setDay(String Day) {
                this.Day = Day;
            }

            public String getEffectTime() {
                return EffectTime;
            }

            public void setEffectTime(String EffectTime) {
                this.EffectTime = EffectTime;
            }

            public String getStatDate() {
                return StatDate;
            }

            public void setStatDate(String StatDate) {
                this.StatDate = StatDate;
            }

            public String getTotalTime() {
                return TotalTime;
            }

            public void setTotalTime(String TotalTime) {
                this.TotalTime = TotalTime;
            }
        }

        public static class SummaryBean implements  Serializable{
            private String AvgEffectTime;
            private String AvgTotalTime;
            private String AvgCal;
            private String CaloryRate;
            private String MaxCalory;
            private String MaxTotalTime;
            private String Percentage;
            private String UID;
            private String TotalCal;
            private String TotalDays;

            public String getAvgEffectTime() {
                return AvgEffectTime;
            }

            public void setAvgEffectTime(String AvgEffectTime) {
                this.AvgEffectTime = AvgEffectTime;
            }

            public String getAvgTotalTime() {
                return AvgTotalTime;
            }

            public void setAvgTotalTime(String AvgTotalTime) {
                this.AvgTotalTime = AvgTotalTime;
            }

            public String getAvgCal() {
                return AvgCal;
            }

            public void setAvgCal(String AvgCal) {
                this.AvgCal = AvgCal;
            }

            public String getCaloryRate() {
                return CaloryRate;
            }

            public void setCaloryRate(String CaloryRate) {
                this.CaloryRate = CaloryRate;
            }

            public String getMaxCalory() {
                return MaxCalory;
            }

            public void setMaxCalory(String MaxCalory) {
                this.MaxCalory = MaxCalory;
            }

            public String getMaxTotalTime() {
                return MaxTotalTime;
            }

            public void setMaxTotalTime(String MaxTotalTime) {
                this.MaxTotalTime = MaxTotalTime;
            }

            public String getPercentage() {
                return Percentage;
            }

            public void setPercentage(String Percentage) {
                this.Percentage = Percentage;
            }

            public String getUID() {
                return UID;
            }

            public void setUID(String UID) {
                this.UID = UID;
            }

            public String getTotalCal() {
                return TotalCal;
            }

            public void setTotalCal(String TotalCal) {
                this.TotalCal = TotalCal;
            }

            public String getTotalDays() {
                return TotalDays;
            }

            public void setTotalDays(String TotalDays) {
                this.TotalDays = TotalDays;
            }
        }
    }

}
