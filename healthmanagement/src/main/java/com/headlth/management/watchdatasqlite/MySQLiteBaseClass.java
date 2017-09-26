package com.headlth.management.watchdatasqlite;

/**
 * Created by abc on 2017/8/31.
 */
public class MySQLiteBaseClass {
    public int id;
    public String UID;
    public String FLAG;
    public String DATA;
    public Single_Original single_original;
    public static class Single_Original{
        public Single_Original(String starttime, String single_data) {
            Starttime = starttime;
            Single_data = single_data;
        }

        public Single_Original(String starttime, String single_data, String original_data) {
            Starttime = starttime;
            Single_data = single_data;
            Original_data = original_data;
        }

        public Single_Original(String starttime, String endtime, String single_data, String original_data) {
            Starttime = starttime;
            Endtime = endtime;
            Single_data = single_data;
            Original_data = original_data;
        }

        public String Starttime;
        public String Endtime;
        public String Single_data;
        public String Original_data;
    }
    public MySQLiteBaseClass(int id, String UID, String FLAG, String DATA) {
        this.id = id;
        this.UID = UID;
        this.FLAG = FLAG;
        this.DATA = DATA;
    }

    public MySQLiteBaseClass(String UID, String FLAG, String DATA) {
        this.UID = UID;
        this.FLAG = FLAG;
        this.DATA = DATA;
    }

    public MySQLiteBaseClass(int id, String FLAG, String DATA) {
        this.id = id;
        this.FLAG = FLAG;
        this.DATA = DATA;
    }

    public MySQLiteBaseClass(String FLAG, String DATA) {
        this.FLAG = FLAG;
        this.DATA = DATA;
    }

    public MySQLiteBaseClass(Single_Original single_original) {
        this.single_original = single_original;
    }
}
