package com.headlth.management.entity;


import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/10/31.
 */
public class MaidongDataJson implements Serializable{
    public int Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;

  //  public String  UserIndexList;
    public UserIndexList UserIndexList;

    @Override
    public String toString() {
        return "MaidongDataJson{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", UserIndexList=" + UserIndexList +
                '}';
    }

    public static class UserIndexList implements Serializable{

        public String LBound;
        public String UBound;
        public String LastSportTime;
        public String HeartRateAvg;
        public int Duration;
        public int ValidTime;
        public int target;

        public String BigCar;
        public int PowerTrainDuration;
        public String IsPlay;
        public int SPID;
        public int PPID;

        public String IsUserPay;
        public int PlanNameID;
        public int PowerFinishedCount;
        public List <Integer> vlist;

        public String WebSiteSPStartMsg;
        public String PowerPrescriptionTitle;
        public String SportPrescriptionTitle;
        public String BodyPart;
        public int PowerStage;
        public int SportStage;
        public String IsSportStart;
        public int SportFinishedDays;
        public int  PowerFinishedDays;


        public String IsShowTodayPowerTrainPlan;
        public String IsShowTodayScore;
        public int  NumberNotRead;

        @Override
        public String toString() {
            return "UserIndexList{" +
                    "LBound='" + LBound + '\'' +
                    ", UBound='" + UBound + '\'' +
                    ", LastSportTime='" + LastSportTime + '\'' +
                    ", HeartRateAvg='" + HeartRateAvg + '\'' +
                    ", Duration=" + Duration +
                    ", ValidTime=" + ValidTime +
                    ", target=" + target +
                    ", BigCar='" + BigCar + '\'' +
                    ", PowerTrainDuration=" + PowerTrainDuration +
                    ", IsPlay='" + IsPlay + '\'' +
                    ", SPID=" + SPID +
                    ", PPID=" + PPID +
                    ", IsUserPay='" + IsUserPay + '\'' +
                    ", PlanNameID=" + PlanNameID +
                    ", PowerFinishedCount=" + PowerFinishedCount +
                    ", vlist=" + vlist +
                    ", WebSiteSPStartMsg='" + WebSiteSPStartMsg + '\'' +
                    ", PowerPrescriptionTitle='" + PowerPrescriptionTitle + '\'' +
                    ", SportPrescriptionTitle='" + SportPrescriptionTitle + '\'' +
                    ", BodyPart='" + BodyPart + '\'' +
                    ", PowerStage=" + PowerStage +
                    ", SportStage=" + SportStage +
                    ", IsSportStart='" + IsSportStart + '\'' +
                    ", SportFinishedDays=" + SportFinishedDays +
                    ", PowerFinishedDays=" + PowerFinishedDays +
                    ", IsShowTodayPowerTrainPlan='" + IsShowTodayPowerTrainPlan + '\'' +
                    ", IsShowTodayScore='" + IsShowTodayScore + '\'' +
                    ", NumberNotRead=" + NumberNotRead +
                    '}';
        }
    }
/*

LBound	Int	心率下限
UBound  	Int	心率上限
SPID	Int	有氧处方ID
LastSportTime	Varchar	最后一次运动日期
IsSportStart	bool	有氧锻炼是否显示	true:显示，false:不显示
HeartRateAvg	Varchar	平均心率
target	Varchar	目标
ValidTime	Int	有效运动时间
IsUserPay	Int	是否需要用户支付	1：已支付，0：未支付
PlanNameID	Int	阶段ID
SportStage	Int	处方阶段
SportPrescriptionTitle	Varchar	处方标题
SportFinishedDays	Int	有氧完成天数
WebSiteSPStartMsg	Varchar	当有氧处方已生成未开始返回参数	提示：您的运动计划将于yyyy年MM月dd日开始
PPID	Int	力量处方ID
BigCar	Varchar	总消耗大卡
Duration	Int	总共运动时间
PowerTrainDuration	Int	力量训练时长
IsPlay	Bool	是否可以播放力量视频	True:可以，false:不可以
vlist   	List<int>	当前用户可以播放的视频	例：List<1,2,3,……>
PowerStage  	Int	力量训练阶段
PowerPrescriptionTitle	varchar	力量处方名称
BodyPart	varchar	力量锻炼的部位
PowerFinishedCount	Int	力量训练完成次数
IsShowTodayPowerTrainPlan 	Bool	是否显示今日锻炼计划中力量模块	true：不显示，false：显示
IsShowTodayScore	bool	首页是否显示今日成绩模块	true：不显示，false：显示

 */

}
