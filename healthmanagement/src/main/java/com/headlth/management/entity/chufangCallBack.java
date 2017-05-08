package com.headlth.management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class chufangCallBack  implements Serializable{
    /**
     * Status : 1
     * IsSuccess : true
     * Message : 获取成功!
     * PList : [{"DailyFrom":30,"WeeklyTotalTo":200,"SportTarget":[{"Content":"目标A\n目标B\n目标C","Title":"运动目标A"},{"Content":"目标A\n目标B\n目标C","Title":"运动目标B"},{"Content":"目标A\n目标B\n目标C","Title":"运动目标C"}],"Duration":20,"UBound":150,"BestTimeRemark":"建议","LBound":80,"WeeklyTotalFrom":100,"WeekAtLeast":3,"DailyDurationTo":40,"BestTime":"3-5点","DailyTo":50,"DailyDurationFrom":30,"SPID":8,"SuggestReport":"游泳\n跑步\n爬山\n攀岩"}]
     * ErrMsg : null
     * IsError : false
     * ErrCode : null
     */
    private int Status;
    private boolean IsSuccess;
    private String Message;
    private List<PListEntity> PList;
    private String ErrMsg;
    private boolean IsError;
    private String ErrCode;

    public chufangCallBack() {
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setPList(List<PListEntity> PList) {
        this.PList = PList;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public void setErrCode(String ErrCode) {
        this.ErrCode = ErrCode;
    }

    public int getStatus() {
        return Status;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<PListEntity> getPList() {
        return PList;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public boolean isIsError() {
        return IsError;
    }

    public String getErrCode() {
        return ErrCode;
    }


    public static class PListEntity  implements Serializable {
        /**
         * DailyFrom : 30
         * WeeklyTotalTo : 200
         * SportTarget : [{"Content":"目标A\n目标B\n目标C","Title":"运动目标A"},{"Content":"目标A\n目标B\n目标C","Title":"运动目标B"},{"Content":"目标A\n目标B\n目标C","Title":"运动目标C"}]
         * Duration : 20
         * UBound : 150
         * BestTimeRemark : 建议
         * LBound : 80
         * WeeklyTotalFrom : 100
         * WeekAtLeast : 3
         * DailyDurationTo : 40
         * BestTime : 3-5点
         * DailyTo : 50
         * DailyDurationFrom : 30
         * SPID : 8
         * SuggestReport : 游泳
         跑步
         爬山
         攀岩
         */
        private int DailyFrom;
        private int WeeklyTotalTo;
        private List<SportTargetEntity> SportTarget;
        private int Duration;
        private int UBound;
        private String BestTimeRemark;
        private int LBound;
        private int WeeklyTotalFrom;
        private int WeekAtLeast;
        private int DailyDurationTo;
        private String BestTime;
        private int DailyTo;
        private int DailyDurationFrom;
        private int SPID;
        private String SuggestReport;

        public void setDailyFrom(int DailyFrom) {
            this.DailyFrom = DailyFrom;
        }

        public void setWeeklyTotalTo(int WeeklyTotalTo) {
            this.WeeklyTotalTo = WeeklyTotalTo;
        }

        public void setSportTarget(List<SportTargetEntity> SportTarget) {
            this.SportTarget = SportTarget;
        }

        public void setDuration(int Duration) {
            this.Duration = Duration;
        }

        public void setUBound(int UBound) {
            this.UBound = UBound;
        }

        public void setBestTimeRemark(String BestTimeRemark) {
            this.BestTimeRemark = BestTimeRemark;
        }

        public void setLBound(int LBound) {
            this.LBound = LBound;
        }

        public void setWeeklyTotalFrom(int WeeklyTotalFrom) {
            this.WeeklyTotalFrom = WeeklyTotalFrom;
        }

        public void setWeekAtLeast(int WeekAtLeast) {
            this.WeekAtLeast = WeekAtLeast;
        }

        public void setDailyDurationTo(int DailyDurationTo) {
            this.DailyDurationTo = DailyDurationTo;
        }

        public void setBestTime(String BestTime) {
            this.BestTime = BestTime;
        }

        public void setDailyTo(int DailyTo) {
            this.DailyTo = DailyTo;
        }

        public void setDailyDurationFrom(int DailyDurationFrom) {
            this.DailyDurationFrom = DailyDurationFrom;
        }

        public void setSPID(int SPID) {
            this.SPID = SPID;
        }

        public void setSuggestReport(String SuggestReport) {
            this.SuggestReport = SuggestReport;
        }

        public int getDailyFrom() {
            return DailyFrom;
        }

        public int getWeeklyTotalTo() {
            return WeeklyTotalTo;
        }

        public List<SportTargetEntity> getSportTarget() {
            return SportTarget;
        }

        public int getDuration() {
            return Duration;
        }

        public int getUBound() {
            return UBound;
        }

        public String getBestTimeRemark() {
            return BestTimeRemark;
        }

        public int getLBound() {
            return LBound;
        }

        public int getWeeklyTotalFrom() {
            return WeeklyTotalFrom;
        }

        public int getWeekAtLeast() {
            return WeekAtLeast;
        }

        public int getDailyDurationTo() {
            return DailyDurationTo;
        }

        public String getBestTime() {
            return BestTime;
        }

        public int getDailyTo() {
            return DailyTo;
        }

        public int getDailyDurationFrom() {
            return DailyDurationFrom;
        }

        public int getSPID() {
            return SPID;
        }

        public String getSuggestReport() {
            return SuggestReport;
        }

        public static class SportTargetEntity  implements Serializable{
            /**
             * Content : 目标A
             目标B
             目标C
             * Title : 运动目标A
             */
            private String Content;
            private String Title;

            public void setContent(String Content) {
                this.Content = Content;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getContent() {
                return Content;
            }

            public String getTitle() {
                return Title;
            }
        }
    }
}
