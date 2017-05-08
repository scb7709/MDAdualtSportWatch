package com.headlth.management.chufang;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Untitled1 implements Serializable{
    /**
     * ErrCode : null
     * Status : 1
     * IsSuccess : true
     * IsError : false
     * Message : 获取成功!
     * PList : [{"Description":null,"WeeklyTotalTo":200,"DurationRemark":"备注AAAB","UpdateTime":"2016-01-27T13:55:43","UserID":4,"WeeklyTotalFrom":100,"ReportID":3,"BestTimeRemark":"建议","Title":"李宇新的处方标题","DailyTo":50,"IsDel":0,"Problem":"问题1\n问题2\n问题3\n问题4\n问题5\n问题6\n问题7","UBound":200,"BestTime":"3-5点","SPID":8,"DailyFrom":30,"Duration":0,"CreateTime":"2016-01-27T13:55:23","LBound":100,"DailyDurationTo":40,"SportTarget":"目标A\n目标B\n目标C","SuggestReport":"游泳\n跑步\n爬山\n攀岩","DailyDurationFrom":30,"WeekAtLeast":3}]
     * ErrMsg : null
     */
    private Object ErrCode;
    private int Status;
    private boolean IsSuccess;
    private boolean IsError;
    private String Message;
    private Object ErrMsg;
    /**
     * Description : null
     * WeeklyTotalTo : 200
     * DurationRemark : 备注AAAB
     * UpdateTime : 2016-01-27T13:55:43
     * UserID : 4
     * WeeklyTotalFrom : 100
     * ReportID : 3
     * BestTimeRemark : 建议
     * Title : 李宇新的处方标题
     * DailyTo : 50
     * IsDel : 0
     * Problem : 问题1
     问题2
     问题3
     问题4
     问题5
     问题6
     问题7
     * UBound : 200
     * BestTime : 3-5点
     * SPID : 8
     * DailyFrom : 30
     * Duration : 0
     * CreateTime : 2016-01-27T13:55:23
     * LBound : 100
     * DailyDurationTo : 40
     * SportTarget : 目标A
     目标B
     目标C
     * SuggestReport : 游泳
     跑步
     爬山
     攀岩
     * DailyDurationFrom : 30
     * WeekAtLeast : 3
     */

    private List<PListEntity> PList;

    public void setErrCode(Object ErrCode) {
        this.ErrCode = ErrCode;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setErrMsg(Object ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public void setPList(List<PListEntity> PList) {
        this.PList = PList;
    }

    public Object getErrCode() {
        return ErrCode;
    }

    public int getStatus() {
        return Status;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public boolean isIsError() {
        return IsError;
    }

    public String getMessage() {
        return Message;
    }

    public Object getErrMsg() {
        return ErrMsg;
    }

    public List<PListEntity> getPList() {
        return PList;
    }

    public static class PListEntity implements Serializable{
        private Object Description;
        private int WeeklyTotalTo;
        private String DurationRemark;
        private String UpdateTime;
        private int UserID;
        private int WeeklyTotalFrom;
        private int ReportID;
        private String BestTimeRemark;
        private String Title;
        private int DailyTo;
        private int IsDel;
        private String Problem;
        private int UBound;
        private String BestTime;
        private int SPID;
        private int DailyFrom;
        private int Duration;
        private String CreateTime;
        private int LBound;
        private int DailyDurationTo;
        private String SportTarget;
        private String SuggestReport;
        private int DailyDurationFrom;
        private int WeekAtLeast;

        public void setDescription(Object Description) {
            this.Description = Description;
        }

        public void setWeeklyTotalTo(int WeeklyTotalTo) {
            this.WeeklyTotalTo = WeeklyTotalTo;
        }

        public void setDurationRemark(String DurationRemark) {
            this.DurationRemark = DurationRemark;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public void setWeeklyTotalFrom(int WeeklyTotalFrom) {
            this.WeeklyTotalFrom = WeeklyTotalFrom;
        }

        public void setReportID(int ReportID) {
            this.ReportID = ReportID;
        }

        public void setBestTimeRemark(String BestTimeRemark) {
            this.BestTimeRemark = BestTimeRemark;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public void setDailyTo(int DailyTo) {
            this.DailyTo = DailyTo;
        }

        public void setIsDel(int IsDel) {
            this.IsDel = IsDel;
        }

        public void setProblem(String Problem) {
            this.Problem = Problem;
        }

        public void setUBound(int UBound) {
            this.UBound = UBound;
        }

        public void setBestTime(String BestTime) {
            this.BestTime = BestTime;
        }

        public void setSPID(int SPID) {
            this.SPID = SPID;
        }

        public void setDailyFrom(int DailyFrom) {
            this.DailyFrom = DailyFrom;
        }

        public void setDuration(int Duration) {
            this.Duration = Duration;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setLBound(int LBound) {
            this.LBound = LBound;
        }

        public void setDailyDurationTo(int DailyDurationTo) {
            this.DailyDurationTo = DailyDurationTo;
        }

        public void setSportTarget(String SportTarget) {
            this.SportTarget = SportTarget;
        }

        public void setSuggestReport(String SuggestReport) {
            this.SuggestReport = SuggestReport;
        }

        public void setDailyDurationFrom(int DailyDurationFrom) {
            this.DailyDurationFrom = DailyDurationFrom;
        }

        public void setWeekAtLeast(int WeekAtLeast) {
            this.WeekAtLeast = WeekAtLeast;
        }

        public Object getDescription() {
            return Description;
        }

        public int getWeeklyTotalTo() {
            return WeeklyTotalTo;
        }

        public String getDurationRemark() {
            return DurationRemark;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public int getUserID() {
            return UserID;
        }

        public int getWeeklyTotalFrom() {
            return WeeklyTotalFrom;
        }

        public int getReportID() {
            return ReportID;
        }

        public String getBestTimeRemark() {
            return BestTimeRemark;
        }

        public String getTitle() {
            return Title;
        }

        public int getDailyTo() {
            return DailyTo;
        }

        public int getIsDel() {
            return IsDel;
        }

        public String getProblem() {
            return Problem;
        }

        public int getUBound() {
            return UBound;
        }

        public String getBestTime() {
            return BestTime;
        }

        public int getSPID() {
            return SPID;
        }

        public int getDailyFrom() {
            return DailyFrom;
        }

        public int getDuration() {
            return Duration;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public int getLBound() {
            return LBound;
        }

        public int getDailyDurationTo() {
            return DailyDurationTo;
        }

        public String getSportTarget() {
            return SportTarget;
        }

        public String getSuggestReport() {
            return SuggestReport;
        }

        public int getDailyDurationFrom() {
            return DailyDurationFrom;
        }

        public int getWeekAtLeast() {
            return WeekAtLeast;
        }
    }}
