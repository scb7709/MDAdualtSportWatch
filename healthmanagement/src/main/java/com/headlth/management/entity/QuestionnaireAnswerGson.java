package com.headlth.management.entity;

import java.util.List;

/**
 * Created by abc on 2016/10/26.
 */
public class QuestionnaireAnswerGson {
    public String Status;
    public String Message;
    public String IsSuccess;
    public String IsError;
    public String ErrMsg;
    public String ErrCode;
    public QuestionnaireAnswer QuestionnaireAnswer;


    @Override
    public String toString() {
        return "QuestionnaireGson{" +
                "Status='" + Status + '\'' +
                ", Message='" + Message + '\'' +
                ", IsSuccess='" + IsSuccess + '\'' +
                ", IsError='" + IsError + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", QuestionnaireAnswer=" + QuestionnaireAnswer +
                '}';
    }

    public static class QuestionnaireAnswer {
        public List<Questionnaireanswer> QuestionAnswerList;
        public static class Questionnaireanswer {
            public String QuestionType;
            public String QuestionID;
            public String QuestionTitle;
            public String QuestionAnswer;
            public List<Option> QuestionContent;

            public static class Option {
                public String ID;
                public String Content;
                public int IsSelect;
            }
        }

    }


}
