package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class newChuFangCallback implements Serializable {


    /**
     * Status : 1
     * PList : {"Summary":{"Stage":"1","TotalWeek":"3","FinishWeek":"1","UnFinishWeek":"2"},"Current":{"CurTargetTime":"1800","FinishedPercent":"66.67%","FinishTime":"1200","WeekRank":"2","FinishList":[0,1,1,0,0,0,0],"WeekLeastDays":"3","FinishedSportDays":"2"}}
     * Message : 获取成功!
     * IsSuccess : true
     * IsError : false
     * ErrMsg : null
     * ErrCode : null
     */

    private int Status;
    /**
     * Summary : {"Stage":"1","TotalWeek":"3","FinishWeek":"1","UnFinishWeek":"2"}
     * Current : {"CurTargetTime":"1800","FinishedPercent":"66.67%","FinishTime":"1200","WeekRank":"2","FinishList":[0,1,1,0,0,0,0],"WeekLeastDays":"3","FinishedSportDays":"2"}
     */

    private PListBean PList;
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

    public PListBean getPList() {
        return PList;
    }

    public void setPList(PListBean PList) {
        this.PList = PList;
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

    public static class PListBean implements Serializable{
        /**
         * Stage : 1
         * TotalWeek : 3
         * FinishWeek : 1
         * UnFinishWeek : 2
         */

        private SummaryBean Summary;
        /**
         * CurTargetTime : 1800
         * FinishedPercent : 66.67%
         * FinishTime : 1200
         * WeekRank : 2
         * FinishList : [0,1,1,0,0,0,0]
         * WeekLeastDays : 3
         * FinishedSportDays : 2
         */

        private CurrentBean Current;

        public SummaryBean getSummary() {
            return Summary;
        }

        public void setSummary(SummaryBean Summary) {
            this.Summary = Summary;
        }

        public CurrentBean getCurrent() {
            return Current;
        }

        public void setCurrent(CurrentBean Current) {
            this.Current = Current;
        }

        public static class SummaryBean implements Serializable{
            private String Stage;
            private String TotalWeek;
            private String FinishWeek;
            private String UnFinishWeek;

            public String getStage() {
                return Stage;
            }

            public void setStage(String Stage) {
                this.Stage = Stage;
            }

            public String getTotalWeek() {
                return TotalWeek;
            }

            public void setTotalWeek(String TotalWeek) {
                this.TotalWeek = TotalWeek;
            }

            public String getFinishWeek() {
                return FinishWeek;
            }

            public void setFinishWeek(String FinishWeek) {
                this.FinishWeek = FinishWeek;
            }

            public String getUnFinishWeek() {
                return UnFinishWeek;
            }

            public void setUnFinishWeek(String UnFinishWeek) {
                this.UnFinishWeek = UnFinishWeek;
            }
        }

        public static class CurrentBean implements Serializable{
            private String CurTargetTime;
            private String FinishedPercent;
            private String FinishTime;
            private String WeekRank;
            private String WeekLeastDays;
            private String FinishedSportDays;
            private List<Integer> FinishList;
            private List<Integer> PowerTrainFinishList;
            public String getCurTargetTime() {
                return CurTargetTime;
            }

            public void setCurTargetTime(String CurTargetTime) {
                this.CurTargetTime = CurTargetTime;
            }

            public List<Integer> getPowerTrainFinishList() {
                return PowerTrainFinishList;
            }

            public void setPowerTrainFinishList(List<Integer> powerTrainFinishList) {
                PowerTrainFinishList = powerTrainFinishList;
            }

            public String getFinishedPercent() {
                return FinishedPercent;
            }

            public void setFinishedPercent(String FinishedPercent) {
                this.FinishedPercent = FinishedPercent;
            }

            public String getFinishTime() {
                return FinishTime;
            }

            public void setFinishTime(String FinishTime) {
                this.FinishTime = FinishTime;
            }

            public String getWeekRank() {
                return WeekRank;
            }

            public void setWeekRank(String WeekRank) {
                this.WeekRank = WeekRank;
            }

            public String getWeekLeastDays() {
                return WeekLeastDays;
            }

            public void setWeekLeastDays(String WeekLeastDays) {
                this.WeekLeastDays = WeekLeastDays;
            }

            public String getFinishedSportDays() {
                return FinishedSportDays;
            }

            public void setFinishedSportDays(String FinishedSportDays) {
                this.FinishedSportDays = FinishedSportDays;
            }

            public List<Integer> getFinishList() {
                return FinishList;
            }

            public void setFinishList(List<Integer> FinishList) {
                this.FinishList = FinishList;
            }
        }
    }
}
