package com.headlth.management.utils;


import java.util.Random;

/**
 * Created by abc on 2016/7/5.
 * 常量
 */
public class Constant {
        public static final String BASE_URL = "https://www.mdadult.com:443";//正式
    //  public static final String BASE_URL = "http://www.mdadult.com:4433";//正式(测试)
    //    public static final String BASE_URL = "http://192.168.1.250:8082";//测试
    // public static final String BASE_URL = "http://192.168.1.250:8089";//测试


    // public static final String BASE_URL = "http://www.mdadult.com:8056";
    //
    // public static final String BASE_URL = " http://192.168.1.132:8086";
    public static final String verify_code = "http://www.ssp356.com";
    public static final String BASE_URL_IMAGE = "http://192.168.0.116:8086";
    public static final String DIALOG_MESSAGE_LOADING = "正在加载，请稍后...";
    public static final String DIALOG_MESSAGE_LOGIN = "正在登录，请稍后...";
    public static String SINA_KEY = "976333681";
    public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";// 应用的回调页
    public static final String SCOPE =                               // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    public static String QQ_ID = "1105190496";
    public static String QQ_KEY = "W34bSqzZGkqUhJWE";


    public static String CHAT_ID = "wx7d8b93a61963d44c";
    public static String access_token = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static String CHAT_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static String CHAT_mch_id = "1390131602";

    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static final String[] WEIGHT1 = new String[]{"30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", ""};
    public static final String[] WEIGHT2 = new String[]{".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9"};
    public static final String[] HEIGHT = new String[]{"100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190"};
    public static final String QUESTION = "{\"Status\":1,\"QuestionList\":[{\"QuestionID\":\"1\",\"QuestionTitle\":\"性别\",\"QuestionContent\":\"1:男,2:女\",\"Unit\":\"null\"},{\"QuestionID\":\"2\",\"QuestionTitle\":\"年龄\",\"QuestionContent\":\"null\",\"Unit\":\"岁\"},{\"QuestionID\":\"3\",\"QuestionTitle\":\"身高__cm?\",\"QuestionContent\":\"null\",\"Unit\":\"cm\"},{\"QuestionID\":\"4\",\"QuestionTitle\":\"体重\",\"QuestionContent\":\"null\",\"Unit\":\"kg\"},{\"QuestionID\":\"5\",\"QuestionTitle\":\"安静心率\",\"QuestionContent\":\"null\",\"Unit\":\"次/分钟\"},{\"QuestionID\":\"6\",\"QuestionTitle\":\"年龄\",\"QuestionContent\":\"null\",\"Unit\":\"岁\"},{\"QuestionID\":\"7\",\"QuestionTitle\":\"已知或可能患病情况\\r\\n您是否存在已知的心血管、肺脏、代谢疾病？（心脏、外周血管或脑血管疾病；慢性阻塞性肺病、哮喘、囊性纤维化肺病；糖尿病、肝肾疾病等）\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"8\",\"QuestionTitle\":\"若患糖尿病，至今患病年限\",\"QuestionContent\":\"null\",\"Unit\":\"null\"},{\"QuestionID\":\"9\",\"QuestionTitle\":\"若存在心血管疾病，是否有过心脏房颤？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"10\",\"QuestionTitle\":\"您是否有心血管、肺脏、代谢疾病的主要症状或体征？（胸痛、缺血性不适、气短、眩晕、阵发性呼吸困难、踝部水肿、心悸或心动过速、间歇性跛行、心脏杂音等）\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"11\",\"QuestionTitle\":\"心血管疾病家族史\\r\\n是否存在以下情况：父亲或一级亲属（男性）在55岁以前患有心肌梗死、实施过冠脉血管重建或猝死；母亲或一级亲属（女性）在65岁以前患有心肌梗死、实施过冠脉血管重建或猝死。\\r\\n是否存在以下情况：父亲或一级亲属（男性）在55岁以前患有心肌梗死、实施过冠脉血管重建或猝死；母亲或一级亲属（女性）在65岁以前患有心肌梗死、实施过冠脉血管重建或猝死。\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"12\",\"QuestionTitle\":\"吸烟情况\\r\\n是否存在：当前吸烟习惯或 6个月内戒烟，或较常暴露于吸烟的环境中？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"13\",\"QuestionTitle\":\"高血压\\r\\n您的收缩压≥140mmHg 或舒张压≥90mmHg？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"14\",\"QuestionTitle\":\"是否正在通过服用降压药控制血压？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"15\",\"QuestionTitle\":\"高脂血症\\r\\n您的最近一次体检结果是否存在以下任何一项：\\r\\n?低密度脂蛋白胆固醇＞3.37mmol/L(130mg/dl)，或\\r\\n?高密度脂蛋白胆固醇＜1.04mmol/L(40mg/dl)或\\r\\n?血清总胆固醇＞5.18mmol/L(200mg/dl) 或\\r\\n?正服用降血脂药物\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"16\",\"QuestionTitle\":\"糖尿病前期\\r\\n您的最近一次体检结果是否存在：空腹血糖在5.6mmol/L (≥100mg/dl) 至7.0mmol/L (＜126mg/dl) 之间，或口服糖耐量实验2小时的血糖值≥7.8mmol/L (140mg/dl)且＜11.1mmol/L (200mg/dl) ？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"17\",\"QuestionTitle\":\"肥胖症\\r\\nBMI＞30kg/m2，或腰围：男性＞102cm，女性＞88cm？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"18\",\"QuestionTitle\":\"静坐少动的生活方式\\r\\n是否存在：至少3个月未参加每周至少3天，每天不少于30分钟的中等强度运动（指需要您花费中等力气完成，呼吸较平常稍微增强的活动，比如快走）？\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"},{\"QuestionID\":\"19\",\"QuestionTitle\":\"负性危险因素（可填可不填；若存在，危险因素减1）\\r\\n高密度脂蛋白胆固醇＞1.55mmol/L(60mg/dl)\",\"QuestionContent\":\"1:是,2:否\",\"Unit\":\"null\"}],\"prescriptionList\":null,\"Message\":\"您还没有答卷，请答卷\",\"IsSuccess\":true,\"IsError\":false,\"ErrMsg\":null,\"ErrCode\":null}";


}
