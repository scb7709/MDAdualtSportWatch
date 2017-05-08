package com.headlth.management.entity;

import java.util.List;

/**
 * Created by 1 on 2015/12/29.
 */
public class spvscl {

    /**
     * Status : 1
     * Message : 成功
     * Data : {"Summary":[{"AvgEffectTime":"0","MaxTotalTime":"2040","AvgTotalTime":"2008","Percentage":"0%","AvgCal":"161","TotalDays":"1","CaloryRate":"中","TotalCal":"161","MaxCalory":"200","StudentID":"09091301"}],"Detail":[{"Calory":"161","TotalTime":"2008","EffectTime":"0","Day":"周三","StatDate":"2015-11-25"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周四","StatDate":"2015-11-26"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周五","StatDate":"2015-11-27"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周六","StatDate":"2015-11-28"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周日","StatDate":"2015-11-29"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周一","StatDate":"2015-11-30"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周二","StatDate":"2015-12-01"}]}
     */
    private int Status;
    private String Message;
    private DataEntity Data;

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public int getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        /**
         * Summary : [{"AvgEffectTime":"0","MaxTotalTime":"2040","AvgTotalTime":"2008","Percentage":"0%","AvgCal":"161","TotalDays":"1","CaloryRate":"中","TotalCal":"161","MaxCalory":"200","StudentID":"09091301"}]
         * Detail : [{"Calory":"161","TotalTime":"2008","EffectTime":"0","Day":"周三","StatDate":"2015-11-25"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周四","StatDate":"2015-11-26"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周五","StatDate":"2015-11-27"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周六","StatDate":"2015-11-28"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周日","StatDate":"2015-11-29"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周一","StatDate":"2015-11-30"},{"Calory":"0","TotalTime":"0","EffectTime":"0","Day":"周二","StatDate":"2015-12-01"}]
         */
        private List<SummaryEntity> Summary;
        private List<DetailEntity> Detail;

        public void setSummary(List<SummaryEntity> Summary) {
            this.Summary = Summary;
        }

        public void setDetail(List<DetailEntity> Detail) {
            this.Detail = Detail;
        }

        public List<SummaryEntity> getSummary() {
            return Summary;
        }

        public List<DetailEntity> getDetail() {
            return Detail;
        }

        public static class SummaryEntity {
            /**
             * AvgEffectTime : 0
             * MaxTotalTime : 2040
             * AvgTotalTime : 2008
             * Percentage : 0%
             * AvgCal : 161
             * TotalDays : 1
             * CaloryRate : 中
             * TotalCal : 161
             * MaxCalory : 200
             * StudentID : 09091301
             */
            private String AvgEffectTime;
            private String MaxTotalTime;
            private String AvgTotalTime;
            private String Percentage;
            private String AvgCal;
            private String TotalDays;
            private String CaloryRate;
            private String TotalCal;
            private String MaxCalory;
            private String StudentID;

            public void setAvgEffectTime(String AvgEffectTime) {
                this.AvgEffectTime = AvgEffectTime;
            }

            public void setMaxTotalTime(String MaxTotalTime) {
                this.MaxTotalTime = MaxTotalTime;
            }

            public void setAvgTotalTime(String AvgTotalTime) {
                this.AvgTotalTime = AvgTotalTime;
            }

            public void setPercentage(String Percentage) {
                this.Percentage = Percentage;
            }

            public void setAvgCal(String AvgCal) {
                this.AvgCal = AvgCal;
            }

            public void setTotalDays(String TotalDays) {
                this.TotalDays = TotalDays;
            }

            public void setCaloryRate(String CaloryRate) {
                this.CaloryRate = CaloryRate;
            }

            public void setTotalCal(String TotalCal) {
                this.TotalCal = TotalCal;
            }

            public void setMaxCalory(String MaxCalory) {
                this.MaxCalory = MaxCalory;
            }

            public void setStudentID(String StudentID) {
                this.StudentID = StudentID;
            }

            public String getAvgEffectTime() {
                return AvgEffectTime;
            }

            public String getMaxTotalTime() {
                return MaxTotalTime;
            }

            public String getAvgTotalTime() {
                return AvgTotalTime;
            }

            public String getPercentage() {
                return Percentage;
            }

            public String getAvgCal() {
                return AvgCal;
            }

            public String getTotalDays() {
                return TotalDays;
            }

            public String getCaloryRate() {
                return CaloryRate;
            }

            public String getTotalCal() {
                return TotalCal;
            }

            public String getMaxCalory() {
                return MaxCalory;
            }

            public String getStudentID() {
                return StudentID;
            }
        }

        public static class DetailEntity {
            /**
             * Calory : 161
             * TotalTime : 2008
             * EffectTime : 0
             * Day : 周三
             * StatDate : 2015-11-25
             */
            private String Calory;
            private String TotalTime;
            private String EffectTime;
            private String Day;
            private String StatDate;

            public void setCalory(String Calory) {
                this.Calory = Calory;
            }

            public void setTotalTime(String TotalTime) {
                this.TotalTime = TotalTime;
            }

            public void setEffectTime(String EffectTime) {
                this.EffectTime = EffectTime;
            }

            public void setDay(String Day) {
                this.Day = Day;
            }

            public void setStatDate(String StatDate) {
                this.StatDate = StatDate;
            }

            public String getCalory() {
                return Calory;
            }

            public String getTotalTime() {
                return TotalTime;
            }

            public String getEffectTime() {
                return EffectTime;
            }

            public String getDay() {
                return Day;
            }

            public String getStatDate() {
                return StatDate;
            }
        }
    }
}
