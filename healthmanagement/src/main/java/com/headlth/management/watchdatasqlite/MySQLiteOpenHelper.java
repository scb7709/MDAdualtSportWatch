package com.headlth.management.watchdatasqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**

 * Created by scb on 2016/3/3.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context) {
        super(context, "watchdata.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {//:CREATE INDEX data on watchdata (DATA)
        db.execSQL("create table watchdata(id integer primary key autoincrement,UID varchar(10),FLAG varchar(20),DATA varchar(999999999))");
       // db.execSQL("ALTER TABLE watchdata ADD unique(DATA)");//;
        db.execSQL("CREATE INDEX DATA ON watchdata (DATA)");

        db.execSQL("create table SingleAndOriginal(id integer primary key autoincrement,UID varchar(10),Starttime  varchar(15),Single_data varchar(100),Original_data varchar(999999999))");
        // db.execSQL("ALTER TABLE watchdata ADD unique(DATA)");//;
        db.execSQL("CREATE INDEX Starttime ON SingleAndOriginal (Starttime)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//CREATE INDEX  ON watchdata ()
}
