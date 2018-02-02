package com.headlth.management.entity;

import java.util.Objects;

/**
 * Created by Administrator on 2016/5/3.
 */
public class deviceEntity implements Comparable{
    private String address;
    private String name;
    private int rssi;//信号强度
    private double distance;//距离

    @Override
    public String toString() {
        return "deviceEntity{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", rssi=" + rssi +
                ", distance=" + distance +
                '}';
    }

    public deviceEntity() {
    }

    public deviceEntity(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


   /* @Override
    public int compareTo(deviceEntity deviceEntity) {


        return (int) (this.distance - deviceEntity.distance);
    }*/

    @Override
    public boolean equals(Object obj) {
        if (obj==null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof deviceEntity)) {
            return false;
        }
        deviceEntity deviceEntity = (deviceEntity) obj;

        if ( this.address.equals(deviceEntity.address)) {
            return true;
        }

       /* try {
            if ((deviceEntity.address.equals(this.address)) && this.name.equals(deviceEntity.name) && (deviceEntity.rssi == this.rssi) && (deviceEntity.distance == this.distance)) {
                return true;
            }
        }catch (NullPointerException n){

            return false;
        }*/

        return false;
    }


    //覆写Object中的hashCode方法
    public int hashCode() {
        return this.name.hashCode() * this.address.hashCode()* (int)(this.distance)* this.rssi;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
