package com.headlth.management.watchdatasqlite;

import android.app.Activity;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scb on 2016/3/3.
 */
public class MySQLiteDataDao {

    private static volatile MySQLiteDataDao sInstance = null;
    private MySQLiteOpenHelper helper;
    private String queryString = "";
    private Activity context;
    private static String UID;

    private MySQLiteDataDao(Activity activity) {
        this.context = activity;
        helper = new MySQLiteOpenHelper(activity);
    }

    public static MySQLiteDataDao getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (MySQLiteDataDao.class) {
                if (sInstance == null) {
                    sInstance = new MySQLiteDataDao(activity);
                    UID = ShareUitls.getString(activity, "UID", "0");
                }
            }
        }
        return sInstance;
    }

    public boolean insertList(List<MySQLiteBaseClass> list) {
        if (list.size() <= 0) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "insert into watchdata (UID,FLAG,DATA) values (?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (MySQLiteBaseClass mySQLiteBaseClass : list) {
                stat.bindString(1, UID);
                stat.bindString(2, mySQLiteBaseClass.FLAG);
                stat.bindString(3, mySQLiteBaseClass.DATA);
                long result = stat.executeInsert();
                if (result < 0) {
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

    public boolean insert(MySQLiteBaseClass mySQLiteBaseClass) {
        if (mySQLiteBaseClass == null || mySQLiteBaseClass.DATA == null || mySQLiteBaseClass.DATA.length() == 0) {
            return false;
        }
        if (isexist(mySQLiteBaseClass.DATA)) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "insert into watchdata (UID,FLAG,DATA) values (?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            stat.bindString(1, UID);
            stat.bindString(2, mySQLiteBaseClass.FLAG);
            stat.bindString(3, mySQLiteBaseClass.DATA);
            long result = stat.executeInsert();
            if (result < 0) {
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

    public boolean insertSingleAndOriginal(MySQLiteBaseClass.Single_Original single_original) {
        if (single_original == null) {
            return false;
        }
        if (isexistSingle_Original(single_original.Single_data)) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "insert into SingleAndOriginal (UID,Starttime,Single_data,Original_data) values (?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            stat.bindString(1, UID);
            stat.bindString(2, single_original.Starttime);
            stat.bindString(3, single_original.Single_data == null ? "" : single_original.Single_data);
            stat.bindString(4, single_original.Original_data == null ? "" : single_original.Original_data);
            long result = stat.executeInsert();
            if (result < 0) {
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

    public boolean updateSingleAndOriginal(String starttime, String Original_data) {
        if (Original_data == null || Original_data.length() == 0) {
            return false;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "UPDATE SingleAndOriginal SET Original_data = '" + Original_data + "' WHERE Starttime = '" + starttime + "' ";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            long result = stat.executeInsert();
            if (result < 0) {
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

    public void deleteall() {
        SQLiteDatabase db = helper.getWritableDatabase();
        // ; --可以将递增数归零
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'watchdata'");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'SingleAndOriginal'");
        //db.execSQL("update watchdata SET seq = 0 where name ='TableName'");
        //db.execSQL("update SingleAndOriginal SET seq = 0 where name ='TableName'");
        //自增长ID为0
        db.close();
    }

    public void deletSingleAndOriginal(int starttime) {//删除原始数据和单次运动结果数据
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from SingleAndOriginal WHERE Starttime = '" + starttime + "' ");
        db.close();
    }

    public void deleteone(String DATA) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from watchdata where DATA ='" + DATA + "'");
        db.close();
    }

    public boolean deleteSingle_motion_results(List<Integer> starttimes) {//当获取完所有的原始数据后  再次查询数据库 删除没有找到对应的原始数据的删除 原始数据以_结尾的去掉_
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean IsHaveNoOriginal_data = false;
        Cursor cursor = null;
        for (Integer starttime : starttimes) {
            cursor = db.rawQuery("select Original_data  from SingleAndOriginal where  UID='" + UID + "' and  Starttime = '" + starttime + "' ", null);
            if (cursor.getCount() <= 0) {
                db.execSQL("delete from SingleAndOriginal WHERE  UID='" + UID + "' and Starttime = '" + starttime + "' ");
            } else {
                MyToash.Log("cursor.getCount()1 " + cursor.getCount() + "");
                while (cursor.moveToNext()) {
                    String Original_data = cursor.getString(0);
                    if (Original_data == null) {
                        db.execSQL("delete from SingleAndOriginal WHERE  UID='" + UID + "' and Starttime = '" + starttime + "' ");
                    } else {
                        int length = Original_data.length();
                        MyToash.Log("cursor.getCount()2 " + Original_data);
                        if (length == 0) {
                            db.execSQL("delete from SingleAndOriginal WHERE  UID='" + UID + "' and Starttime = '" + starttime + "' ");
                        } else if (Original_data.endsWith("_")) {//去掉_
                            try {
                                String sql = "UPDATE SingleAndOriginal SET Original_data = '" + Original_data.substring(0, length - 1) + "' where  UID='" + UID + "' AND   Starttime = '" + starttime + "' ";
                                db.execSQL(sql);
                            } catch (Exception e) {

                            }
                        }
                    }

                }
            }
        }
        cursor.close();
        db.close();
        return IsHaveNoOriginal_data;
    }

    public boolean tabIsExist() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();//
            cursor = db.rawQuery("select * from watchdata", null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        db.close();
        return false;
    }


    public List<MySQLiteBaseClass> query(String FLAG) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from watchdata where where  UID='" + UID + "' AND FLAG='" + FLAG + "'", null);
        List<MySQLiteBaseClass> mySQLiteBaseClassArrayList = new ArrayList<>();
        MySQLiteBaseClass mySQLiteBaseClass = null;
        while (cursor.moveToNext()) {
            mySQLiteBaseClass = new MySQLiteBaseClass(cursor.getInt(0), FLAG, cursor.getString(3));
            mySQLiteBaseClassArrayList.add(mySQLiteBaseClass);
        }
        cursor.close();
        db.close();
        return mySQLiteBaseClassArrayList;
    }

    public List<MySQLiteBaseClass.Single_Original> queryALLSingle_Original() {////查询没用  完整的单次运动及其原始数据
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from SingleAndOriginal where  UID='" + UID + "' ", null);
        List<MySQLiteBaseClass.Single_Original> mySQLiteBaseClassArrayList = new ArrayList<>();
        MySQLiteBaseClass.Single_Original single_original = null;
        while (cursor.moveToNext()) {
            String Original = cursor.getString(4);
            if (Original != null) {
                if (Original.length() != 0 && !Original.endsWith("_")) {
                    single_original = new MySQLiteBaseClass.Single_Original(cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    mySQLiteBaseClassArrayList.add(single_original);
                }
            }
        }
        cursor.close();
        db.close();
        return mySQLiteBaseClassArrayList;
    }

    public List<MySQLiteBaseClass.Single_Original> querySingleNOOriginal() {//查询单次运动数据但是没用对应的的原始数据 或者原始数据不完整
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from SingleAndOriginal where  UID='" + UID + "' ", null);
        List<MySQLiteBaseClass.Single_Original> Single = new ArrayList<>();
        MySQLiteBaseClass.Single_Original single_original = null;
        while (cursor.moveToNext()) {
            String Original = cursor.getString(4);
            if (Original != null) {
                if (Original.length() == 0 || Original.endsWith("_")) {
                    single_original = new MySQLiteBaseClass.Single_Original(cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    Single.add(single_original);
                }

            } else {
                single_original = new MySQLiteBaseClass.Single_Original(cursor.getString(2), cursor.getString(3), null);
                Single.add(single_original);
            }
        }
        cursor.close();
        db.close();
        return Single;
    }


    public List<MySQLiteBaseClass> queryAllNOSingle_motion_results() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from watchdata where  UID='" + UID + "'", null);//不查询单次数据
        List<MySQLiteBaseClass> mySQLiteBaseClassArrayList = new ArrayList<>();
        MySQLiteBaseClass mySQLiteBaseClass = null;
        while (cursor.moveToNext()) {
            mySQLiteBaseClass = new MySQLiteBaseClass(cursor.getInt(0), UID, cursor.getString(2), cursor.getString(3));
            mySQLiteBaseClassArrayList.add(mySQLiteBaseClass);

        }
        cursor.close();
        db.close();
        return mySQLiteBaseClassArrayList;
    }

    public boolean isexist(String DATA) {
        boolean isexist = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from watchdata where DATA='" + DATA + "'", null);
        if (cursor.getCount() != 0) {
            isexist = true;
        }
        cursor.close();
        db.close();
        return isexist;
    }

    public boolean isexistSingle_Original(String Single_data) {
        boolean isexist = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from SingleAndOriginal  where   UID='" + UID + "' and Single_data='" + Single_data + "'", null);
        if (cursor.getCount() != 0) {
            isexist = true;
        }
        cursor.close();
        db.close();
        return isexist;
    }


    public void BubbleSort() {
    }
}
