package com.headlth.management.entity;

/**
 * Created by abc on 2016/9/20.
 */
public class LivingHabit {
    private String id;
    private String appellation;
    private String ischoose;


    @Override
    public String toString() {
        return "LivingHabit{" +
                "id='" + id + '\'' +
                ", appellation='" + appellation + '\'' +
                ", ischoose='" + ischoose + '\'' +
                '}';
    }

    public LivingHabit(String appellation, String ischoose) {
        this.appellation = appellation;
        this.ischoose = ischoose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String ischoose() {
        return ischoose;
    }

    public void setIschoose(String ischoose) {
        this.ischoose = ischoose;
    }
}
