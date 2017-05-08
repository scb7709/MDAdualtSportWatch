package com.headlth.management.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abc on 2017/3/17.
 * <p/>
 * <p/>
 * 储存视频数据的数据库
 */
public class VideoDataOpenHelper extends SQLiteOpenHelper {

    public VideoDataOpenHelper(Context context) {
        super(context, "videodatahelper.db", null, 1);
    }
/*

   private String ID;
        private String Name;
        private String Address;
        private String Count;
        private String Cal;
        private String Duration;
        private String ImgUrl;
        private String Interval;
        private String md5;
        private String localAddress;





 */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table videodata(id integer primary key autoincrement ,"
                + "num varchar(100),"
                + "name varchar(100),"
                + "address varchar(100),"
                + "count varchar(100),"
                + "cal varchar(100),"
                + "duration varchar(100),"
                + "imgurl varchar(100),"
                + "interval varchar(100),"
                + "verificationcode varchar(100),"
                + "verificationcodelocal varchar(100),"
                + "localaddress varchar(100))");




    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
