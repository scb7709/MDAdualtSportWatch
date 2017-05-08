package com.headlth.management.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.headlth.management.activity.StrengthSportActivity;
import com.headlth.management.entity.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2017/3/17.
 */
public class VideoDataManageUtils {

    private static volatile VideoDataManageUtils sInstance = null;
    private static VideoDataOpenHelper helper;
    private String queryString = "";
    private Context context;

    private VideoDataManageUtils(Context ct) {

        this.context = ct;
        helper = new VideoDataOpenHelper(ct);
    }

    public static VideoDataManageUtils getInstance(Context ct) {
        if (sInstance == null) {
            synchronized (VideoDataManageUtils.class) {
                if (sInstance == null) {
                    sInstance = new VideoDataManageUtils(ct);
                }
            }
        }
        return sInstance;
    }

    public boolean insertListData(List<Video.SubVideo> list, final Activity context, final Dialog dialog) {
        if (list.size() <= 0) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "insert into videodata (num,name,address,count,cal,duration,imgurl,interval,verificationcode,verificationcodelocal,localaddress) values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (Video.SubVideo subVideo : list) {
                stat.bindString(1, subVideo.getID());
                stat.bindString(2, subVideo.getName());
                stat.bindString(3, subVideo.getAddress());
                stat.bindString(4, subVideo.getCount());
                stat.bindString(5, subVideo.getCal());
                stat.bindString(6, subVideo.getDuration());
                stat.bindString(7, subVideo.getImgUrl());
                stat.bindString(8, subVideo.getInterval());
                stat.bindString(9, subVideo.getVerificationCode());
                stat.bindString(10, subVideo.getVerificationCodeLocal());
                stat.bindString(11, subVideo.getLocalAddress());
                long result = stat.executeInsert();
                if (result < 0) {
                    context.startActivity(new Intent(context, StrengthSportActivity.class));
                    dialog.dismiss();
                    context.finish();
                    return false;
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean insertOneData(Video.SubVideo subVideo, final Activity context) {
        if (subVideo == null) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "insert into videodata (num,name,address,count,cal,duration,imgurl,interval,verificationcode,verificationcodelocal,localaddress) values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            stat.bindString(1, subVideo.getID());
            stat.bindString(2, subVideo.getName());
            stat.bindString(3, subVideo.getAddress());
            stat.bindString(4, subVideo.getCount());
            stat.bindString(5, subVideo.getCal());
            stat.bindString(6, subVideo.getDuration());
            stat.bindString(7, subVideo.getImgUrl());
            stat.bindString(8, subVideo.getInterval());
            stat.bindString(9, subVideo.getVerificationCode());
            stat.bindString(10, subVideo.getVerificationCodeLocal());
            stat.bindString(11, subVideo.getLocalAddress());

            long result = stat.executeInsert();
            if (result < 0) {
                context.startActivity(new Intent(context, StrengthSportActivity.class));
                context.finish();
                return false;
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void delete() {//删除表
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from videodata");
        db.close();
    }

    public void delete(String LocalAddress) {//删除表
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from videodata where localaddress="+LocalAddress);

        db.close();
    }



    public boolean tabIsExist() {//表是否存在
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();//
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from videodata", null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
        }
        db.close();
        return result;
    }


    public List<Video.SubVideo> queryListData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from videodata", null);
        List<Video.SubVideo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Video.SubVideo subVideo = new Video.SubVideo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
            list.add(subVideo);
        }
        cursor.close();
        db.close();
        return list;
    }

 /*   public List<Video.SubVideo>  queryListData(Video.SubVideo subVideo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from videodata", null);
        List<Video.SubVideo>  list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Video.SubVideo subVideo=new Video.SubVideo(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11));
            String brandd = cursor.getString(6);
        }
        cursor.close();
        db.close();
        return list;
    }*/
 /*   public List<String> queryCatgory(int parentid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select catid from goods where parentid=" + parentid, null);
        List<String> brand = new ArrayList<>();
        while (cursor.moveToNext()) {
            String catid = cursor.getInt(0) + "";
            if (!brand.contains(catid)) {
                brand.add(catid);
            }
        }
        List<String> cat = new ArrayList<>();
        for (String s : brand) {
            cat.add(getCatgory(Integer.parseInt(s)));
        }
        Log.i("分类" + parentid + ":", cat.size() + "AAAA" + cat.toString());
        cursor.close();
        db.close();
        return cat;
    }*/


}
