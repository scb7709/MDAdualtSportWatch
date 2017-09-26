package com.headlth.management.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.headlth.management.entity.MaidongDataJson;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.entity.User;
import com.headlth.management.entity.VersionClass;
import com.headlth.management.entity.anlyseCallBack;

import java.io.File;

/**
 * Created by Administrator on 2015/8/18.
 */
public class ShareUitls {
    public static void putAdvancedPrescriptionString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("advancedprescription.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getAdvancedPrescriptionString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("advancedprescription.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("aa.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }
    public static void putWATCHSPORTString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("WATCHSPORT.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }
    public static String getWATCHSPORTSString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("WATCHSPORT.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putToken(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getToken(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putLoginString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("login.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getLoginString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("login.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putSportString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("sport.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getSportString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("sport.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void putStrengthString(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("strength.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getStrengthString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("strength.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }
    public static void putWatchSportData(Context c, String key, String msg) {
        SharedPreferences sp = c.getSharedPreferences("WatchSport.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, msg);
        e.commit();
    }

    public static String getWatchSportData(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("WatchSport.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }
    public static void cleanWatchSportData(Context c) {
        SharedPreferences sp = c.getSharedPreferences("WatchSport.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static String getString(Context c, String key, String d) {
        SharedPreferences sp = c.getSharedPreferences("aa.xml", Context.MODE_PRIVATE);
        return sp.getString(key, d);
    }

    public static void cleanStrengthString(Context c) {
        SharedPreferences sp = c.getSharedPreferences("strength.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void cleanString2(Context c) {
        SharedPreferences sp = c.getSharedPreferences("sport.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void putUser(Context c, User user) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("UID", user.getUID());
        e.putString("loginFlag", user.getLoginFlag());
        if (user.getPhone() != null) {
            e.putString("phone", user.getPhone());
        }
        if (user.getPwd() != null) {
            e.putString("PWD", user.getPwd());
        }
        if (user.getChatOpenID() != null) {
            e.putString("chatOpenID", user.getChatOpenID());
        }
        if (user.getQQOpenID() != null) {
            e.putString("QQOpenID", user.getQQOpenID());
        }
        if (user.getSinaOpenID() != null) {
            e.putString("sinaOpenID", user.getSinaOpenID());
        }
        if (user.getUserInformation() != null) {
            if (user.getUserInformation().getNickName() != null) {
                e.putString("NickName", user.getUserInformation().getNickName());
            }
            if (user.getUserInformation().getWeight() != null) {
                e.putString("Weight", user.getUserInformation().getWeight());
            }
            if (user.getUserInformation().getHeight() != null) {
                e.putString("Height", user.getUserInformation().getHeight());
            }
            if (user.getUserInformation().getGender() != null) {
                e.putString("Gender", user.getUserInformation().getGender());
            }
            if (user.getUserInformation().getBirthday() != null) {
                e.putString("Birthday", user.getUserInformation().getBirthday());
            }
            if (user.getUserInformation().getMAC() != null) {
                e.putString("MAC", user.getUserInformation().getMAC());
            }
            if (user.getUserInformation().getFile() != null) {
                e.putString("File", user.getUserInformation().getFile());
            }
        }
      /*  e.putString("Illness", Arrays.toString(user.getUserInformation().getIllness()));
        e.putString("Badhabit",Arrays.toString(user.getUserInformation().getBadhabit()));*/

        e.commit();
    }

    public static void putUserInformation(Context c, User.UserInformation userInformation) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        if (userInformation != null) {
            e.putString("NickName", userInformation.getNickName());
            e.putString("Weight", userInformation.getWeight());
            e.putString("Height", userInformation.getHeight());
            e.putString("Gender", userInformation.getGender());
            e.putString("Birthday", userInformation.getBirthday());
            if (userInformation.getFile() != null) {
                e.putString("File", userInformation.getFile());
            }

        }
      /*  e.putString("Illness", Arrays.toString(user.getUserInformation().getIllness()));
        e.putString("Badhabit",Arrays.toString(user.getUserInformation().getBadhabit()));*/

        e.commit();
    }

    public static void putUserInformationFile(Context c, String URL) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("File", URL);

        e.commit();
    }

    public static void putUserInformationMac(Context c, String MAC) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("MAC", MAC);

        e.commit();
    }

    public static String getUserInformationMac(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        return sp.getString("MAC", "");
    }

    public static void putUserInformationWatch(Context c, String HRrest,String WatchDuration,String UBound,String LBound) {//保存腕表运动需要同步的四个参数
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("HRrest", HRrest);
        e.putString("WatchDuration", WatchDuration);
        e.putString("UBound", UBound);
        e.putString("LBound", LBound);


        e.commit();
    }

    public static void putUserInformationSingle(Context c, String key, String value) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("key", value);
      /*  e.putString("Illness", Arrays.toString(user.getUserInformation().getIllness()));
        e.putString("Badhabit",Arrays.toString(user.getUserInformation().getBadhabit()));*/
        e.commit();
    }

    //String nickName, String weight, String height, String gender, String birthday, String file, String[] illness, String[] badhabit
    public static User getUser(Context c) {
        SharedPreferences sp = c.getSharedPreferences("user.xml", Context.MODE_PRIVATE);
        if (!sp.getString("UID", "").equals("")) {
            User user = new User();
            User.UserInformation userInformation = user.getUserInformation();
            userInformation.setNickName(sp.getString("NickName", ""));
            userInformation.setWeight(sp.getString("Weight", ""));
            userInformation.setHeight(sp.getString("Height", ""));
            userInformation.setGender(sp.getString("Gender", ""));
            userInformation.setBirthday(sp.getString("Birthday", ""));
            userInformation.setFile(sp.getString("File", ""));
            userInformation.setMAC(sp.getString("MAC", ""));
            userInformation.setHRrest(sp.getString("HRrest", "75"));
            userInformation.setWatchDuration(sp.getString("WatchDuration", ""+(19 * 60)));
            userInformation.setUBound(sp.getString("UBound", "100"));
            userInformation.setLBound(sp.getString("LBound", "120"));
            user.setUID(sp.getString("UID", ""));
            user.setPhone(sp.getString("phone", ""));
            user.setPwd(sp.getString("PWD", ""));
            user.setLoginFlag(sp.getString("loginFlag", ""));
            user.setSinaOpenID(sp.getString("sinaOpenID", ""));
            user.setChatOpenID(sp.getString("chatOpenID", ""));
            user.setQQOpenID(sp.getString("QQOpenID", ""));
            user.setUserInformation(userInformation);
            return user;
        } else {
            return null;
        }
    }

    //清除所有文件数据
    public static void cleanSharedPreference(Context context) {
        context.getSharedPreferences("aa.xml", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("user.xml", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("sport.xml", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("strength.xml", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("login.xml", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("WatchSport.xml", Context.MODE_PRIVATE).edit().clear().commit();
        putString(context, "questionnaire", "1");//设置首页刷新
        putString(context, "maidong", "1");//设置首页刷新
        putString(context, "analize", "1");//分析重新刷新
        // deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    public static void putVersion(Context c, VersionClass.Version version) {
        SharedPreferences sp = c.getSharedPreferences("version.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        if (version != null) {
            e.putString("VersionName", version.VersionName);
            e.putInt("VersionCode", version.VersionCode);
            e.putString("Description", version.Description);
            e.putString("DownloadUrl", version.DownloadUrl);
        }
        e.commit();
    }

    public static VersionClass.Version getVersion(Context c) {
        SharedPreferences sp = c.getSharedPreferences("version.xml", Context.MODE_PRIVATE);
        int VersionCode = sp.getInt("VersionCode", 0);
        if (VersionCode != 0) {
            VersionClass.Version version = new VersionClass.Version();
            version.VersionCode = VersionCode;
            version.VersionName = sp.getString("VersionName", "");
            version.Description = sp.getString("Description", "");
            version.DownloadUrl = sp.getString("DownloadUrl", "");
            return version;
        }
        return null;
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {

            for (File fi : directory.listFiles()) {
                if (fi.isDirectory()) {
                    deleteFilesByDirectory(fi);
                } else {
                    fi.delete();
                }
            }
        }

    }
}
