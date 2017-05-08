package com.headlth.management.entity;

/**
 * Created by abc on 2016/9/28.
 */
public class Prescription {
    private String id;
    private String type;
    private String name;
    private String details;
    private String pay;

    public Prescription(String name, String details, String pay) {
        this.name = name;
        this.details = details;
        this.pay = pay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
