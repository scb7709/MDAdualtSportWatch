package com.headlth.management.entity;

import java.util.List;

/**
 * Created by abc on 2016/10/26.
 */
public class QuestionnaireGson {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public List<Questionnaire> QuestionList;

    @Override
    public String toString() {
        return "QuestionnaireGson{" +
                "Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", QuestionList=" + QuestionList +
                '}';
    }

    public static class Questionnaire {
        public String Number;//显示时的排序编号


        public String QuestionTypeID;
        public String QuestionID;
        public String QuestionTitle;
        public String QuestionTypeName;
        public List<Option> QuestionAnswer;
        public String CompletionAnswer="";
        public String Unit;
        public String UserInputID;
        public int IsHRrest;

        public Questionnaire(String questionTypeID, String questionID, String questionTitle, String questionTypeName, List<Option> questionAnswer, String unit, String userInputID,int isHRrest) {
            QuestionTypeID = questionTypeID;
            QuestionID = questionID;
            QuestionTitle = questionTitle;
            QuestionTypeName = questionTypeName;
            QuestionAnswer = questionAnswer;
            Unit = unit;
            UserInputID = userInputID;
            IsHRrest = isHRrest;
        }

        public static class Option {
            public String ID;
            public String Content;
            public boolean Selected;

            @Override
            public String toString() {
                return "Option{" +
                        "OptionID='" + ID + '\'' +
                        ", OptionContent='" + Content + '\'' +
                        ", Selected=" + Selected +
                        '}';
            }

            public Option(String optionID, String optionContent) {
                ID = optionID;
                Content = optionContent;

            }
        }

        @Override
        public String toString() {
            return "Questionnaire{" +
                    "QuestionTypeID='" + QuestionTypeID + '\'' +
                    ", QuestionID='" + QuestionID + '\'' +
                    ", QuestionTitle='" + QuestionTitle + '\'' +
                    ", QuestionTypeName='" + QuestionTypeName + '\'' +
                    ", QuestionAnswer=" + QuestionAnswer +
                    '}';
        }
    }

    public static class QuestionnaireGapfilling {
        public String QuestionTypeID;
        public String QuestionID;
        public String QuestionTitle;
        public String QuestionTypeName;
        public String QuestionAnswer;

        @Override
        public String toString() {
            return "Questionnaire{" +
                    "QuestionTypeID='" + QuestionTypeID + '\'' +
                    ", QuestionID='" + QuestionID + '\'' +
                    ", QuestionTitle='" + QuestionTitle + '\'' +
                    ", QuestionTypeName='" + QuestionTypeName + '\'' +
                    ", QuestionAnswer=" + QuestionAnswer +
                    '}';
        }
    }
}
